package com.tbank.t_health.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tbank.t_health.data.model.ActivityData
import com.tbank.t_health.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate
import java.util.UUID

class ActivityRepository(private val context: Context) {
    private val gson = Gson()
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
        Log.d("ActivityRepository", "Activity saved locally: ${activity.date}")
    }

    suspend fun loadLocalActivities(): List<ActivityData> = withContext(Dispatchers.IO) {
        val file = getFile()
        if (!file.exists()) return@withContext emptyList()
        val json = file.readText()
        gson.fromJson(json, object : TypeToken<List<ActivityData>>() {}.type)
    }

    // Отправка локальных данных на сервер (Retrofit)
    suspend fun syncToServer(clearAfterSync: Boolean = false) = withContext(Dispatchers.IO) {
        try {
            val activities = loadLocalActivities()
            if (activities.isEmpty()) {
                Log.d("ActivityRepository", "No activities to sync")
                return@withContext
            }

            for (activity in activities) {
                RetrofitInstance.api.postActivity(activity)
                Log.d("ActivityRepository", "Synced activity ${activity.id} to server")
            }

            if (clearAfterSync) {
                getFile().delete()
                Log.d("ActivityRepository", "Local activity data cleared after sync")
            }
        } catch (e: Exception) {
            Log.e("ActivityRepository", "Sync error: ${e.message}")
        }
    }

    suspend fun collectAndSaveYesterdayData(activeStorage: ActiveStorage) = withContext(Dispatchers.IO) {
        try {
            val yesterday = LocalDate.now().minusDays(1)

            // Сбрасываем ежедневные показатели, но сохраняем их перед этим
            val activeMinutes = activeStorage.getWorkoutMinutesForToday()
            val calories = activeStorage.getCalories()

            val activity = ActivityData(
                id = UUID.randomUUID().toString(),
                activeMinutes = activeMinutes,
                calories = calories,
                date = yesterday
            )

            saveActivityLocally(activity)
            Log.d("ActivityRepository", "Collected and saved yesterday’s data: $activity")

            activeStorage.resetDaily()

        } catch (e: Exception) {
            Log.e("ActivityRepository", "Error collecting yesterday’s data: ${e.message}")
        }
    }
}
