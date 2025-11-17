package com.tbank.t_health.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.tbank.composefoodtracker.services.StepCounterService
import com.tbank.t_health.data.ActiveStorage
import com.tbank.t_health.data.model.ActivityData
import com.tbank.t_health.data.model.ActivityFullData
import com.tbank.t_health.data.model.ActivityGetData
import com.tbank.t_health.data.model.ActivityType
import com.tbank.t_health.data.model.UserData
import com.tbank.t_health.network.GsonProvider
import com.tbank.t_health.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

class ActivityRepository(private val context: Context) {
    private val gson = GsonProvider.gson
    private val activityDataFile = "activity_data.json"

    private fun getFile(): File = File(context.filesDir, activityDataFile)

    suspend fun saveActivityLocally(activity: ActivityData) = withContext(Dispatchers.IO) {
        val file = getFile()
        val existingList: List<ActivityData> = if (file.exists()) {
            val json = file.readText()
            gson.fromJson(json, object : TypeToken<List<ActivityData>>() {}.type)
        } else emptyList()

        val newList = existingList + activity
        file.writeText(gson.toJson(newList))
        Log.d("ActivityRepository", "Activity saved locally:")
    }

    suspend fun loadLocalActivities(): List<ActivityData> = withContext(Dispatchers.IO) {
        val file = getFile()
        if (!file.exists()) return@withContext emptyList()
        val json = file.readText()
        gson.fromJson(json, object : TypeToken<List<ActivityData>>() {}.type)
    }

    // Отправка локальных данных на сервер (Retrofit)
    suspend fun syncToServer(userId: Long, clearAfterSync: Boolean = false) = withContext(Dispatchers.IO) {
        try {
            val activities = loadLocalActivities()
            if (activities.isEmpty()) {
                Log.d("ActivityRepository", "No activities to sync")
                return@withContext
            }

            for (activity in activities) {
                RetrofitInstance.api.createActivity(userId, activity)
                Log.d("ActivityRepository", "Synced activity ${activity.id ?: "local"} for user $userId")
            }

            if (clearAfterSync) {
                getFile().delete()
                Log.d("ActivityRepository", "Local activity data cleared after sync")
            }
        } catch (e: Exception) {
            Log.e("ActivityRepository", "Sync error: ${e.message}")
        }
    }

    suspend fun getUserActivitiesFromServer(userId: Long, date: LocalDate? = null): List<ActivityGetData> {
        return try {
            val dateStr = date?.toString()
            RetrofitInstance.api.getUserActivities(userId, dateStr)
        } catch (e: Exception) {
            Log.e("ActivityRepository", "Error fetching activities for user $userId: ${e.message}")
            emptyList()
        }
    }

    suspend fun getUserFromServer(userId: Long): UserData? {
        return try {
            RetrofitInstance.api.getUserById(userId)
        } catch (e: Exception) {
            Log.e("ActivityRepository", "Error fetching user $userId: ${e.message}")
            null
        }
    }


    suspend fun collectAndSaveDailyData(
        stepService: StepCounterService,
        activeStorage: ActiveStorage,
        userId: Long
    ) = withContext(Dispatchers.IO) {
        try {
            val now = LocalDateTime.now()

            val steps = stepService.getStepsForToday()
            val activeMinutes = stepService.getActiveMinutesForToday().toInt() +
                    (activeStorage.getActiveSeconds() / 60)

            val activeCalories = activeStorage.getCalories()
            val calories = stepService.getCaloriesFromStepsAndActiveCalories() + activeCalories

            val activities = listOf(
                ActivityData(
                    id = null,
                    userId = userId,
                    value = BigDecimal(steps.toDouble()),
                    type = ActivityType.STEPS,
                    calories = calories,

                ),
                ActivityData(
                    id = null,
                    userId = userId,
                    value = BigDecimal(activeMinutes.toDouble()),
                    type = ActivityType.TRAINING,
                    calories = calories,

                )
            )

            for (activity in activities) {
                saveActivityLocally(activity)
            }

            Log.d("ActivityRepository", "✅ Saved daily data for user $userId — steps=$steps, activeMinutes=$activeMinutes, calories=$calories")

            activeStorage.resetDaily()

        } catch (e: Exception) {
            Log.e("ActivityRepository", "❌ Error collecting daily data: ${e.message}")
        }
    }

    suspend fun getUserActivitiesForWeek(
        userId: Long,
        anyDateInWeek: LocalDate
    ): List<ActivityFullData> {
        val allActivities = getUserActivitiesFromServer(userId)

        val startOfWeek = anyDateInWeek.with(java.time.DayOfWeek.MONDAY)

        val activityByDate: Map<LocalDate, List<ActivityGetData>> = allActivities
            .mapNotNull { activity ->
                activity.date?.let { dateStr ->
                    val date = try {
                        LocalDate.parse(dateStr)
                    } catch (e: DateTimeParseException) {
                        try {
                            LocalDateTime.parse(dateStr).toLocalDate()
                        } catch (e2: Exception) {
                            null
                        }
                    }
                    date?.let { it to activity }
                }
            }
            .groupBy({ it.first }, { it.second })

        // генерация 7 дней недели
        return (0L..6L).map { offset ->
            val date = startOfWeek.plusDays(offset)
            val activitiesForDay = activityByDate[date] ?: emptyList()

            if (activitiesForDay.isNotEmpty()) {
                var steps = 0
                var activeMinutes = 0
                var calories = 0.0

                activitiesForDay.forEach { a ->
                    when (a.type) {
                        ActivityType.STEPS -> {
                            steps = a.value.toInt()
                            calories = a.calories
                        }
                        ActivityType.TRAINING -> {
                            activeMinutes = a.value.toInt()
                        }
                        else -> {} // MOVING и др игнор
                    }
                }

                ActivityFullData(
                    steps = steps,
                    activeMinutes = activeMinutes,
                    calories = calories,
                    date = date
                )
            } else {
                // Если данных нет ставим нули
                ActivityFullData(
                    steps = 0,
                    activeMinutes = 0,
                    calories = 0.0,
                    date = date
                )
            }
        }
    }

    suspend fun getUserActivitiesFor28Days(
        userId: Long,
        anyDateWeek: LocalDate = LocalDate.now()
    ): List<ActivityFullData> {
        val allActivities = getUserActivitiesFromServer(userId)

        val endDate = anyDateWeek.with(java.time.DayOfWeek.SUNDAY)
        val startDate = endDate.minusDays(27) // 28 дней включая endDate

        val activityByDate: Map<LocalDate, List<ActivityGetData>> = allActivities
            .mapNotNull { activity ->
                activity.date?.let { dateStr ->
                    val date = try {
                        LocalDate.parse(dateStr)
                    } catch (e: DateTimeParseException) {
                        try {
                            LocalDateTime.parse(dateStr).toLocalDate()
                        } catch (e2: Exception) {
                            null
                        }
                    }
                    date?.let { it to activity }
                }
            }
            .groupBy({ it.first }, { it.second })

        // генерация 28 дней
        return (0L..27L).map { offset ->
            val date = startDate.plusDays(offset)
            val activitiesForDay = activityByDate[date] ?: emptyList()

            if (activitiesForDay.isNotEmpty()) {
                var steps = 0
                var activeMinutes = 0
                var calories = 0.0

                activitiesForDay.forEach { a ->
                    when (a.type) {
                        ActivityType.STEPS -> {
                            steps = a.value.toInt()
                            calories = a.calories
                        }
                        ActivityType.TRAINING -> {
                            activeMinutes = a.value.toInt()
                        }
                        else -> {} // MOVING и др игнор
                    }
                }

                ActivityFullData(
                    steps = steps,
                    activeMinutes = activeMinutes,
                    calories = calories,
                    date = date
                )
            } else {
                // Если данных нет ставим нули
                ActivityFullData(
                    steps = 0,
                    activeMinutes = 0,
                    calories = 0.0,
                    date = date
                )
            }
        }
    }

}