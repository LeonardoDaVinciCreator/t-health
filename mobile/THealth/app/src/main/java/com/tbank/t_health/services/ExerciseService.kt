package com.tbank.composefoodtracker.services

import android.content.Context
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.units.Energy
import com.tbank.t_health.data.ActiveStorage
import com.tbank.t_health.data.model.WorkoutData
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.UUID
import kotlin.math.max

import androidx.health.connect.client.records.metadata.Metadata
class ExerciseService(private val context: Context) {

    private val activeStorage = ActiveStorage(context)
    private var currentWorkout: OngoingWorkout? = null

    fun startWorkout(
        name: String,
        type: String,
        durationSeconds: Int? = null,
        calories: Double? = null,
        plannedDate: LocalDate = LocalDate.now()
    ) {
        if (currentWorkout != null) {
            Log.w("ExerciseService", "Тренировка уже запущена!")
            return
        }

        if (plannedDate.isBefore(LocalDate.now())) {
            Log.w("ExerciseService", "Нельзя создать тренировку на прошедшую дату!")
            return
        }

        val startTime = Instant.now()
        currentWorkout = OngoingWorkout(
            id = UUID.randomUUID().toString(),
            name = name,
            type = type,
            startTime = startTime,
            durationSeconds = durationSeconds,
            calories = calories,
            plannedDate = plannedDate
        )

        Log.d(
            "ExerciseService",
            "Начата тренировка: $name ($type), план: $plannedDate, $calories ккал/мин"
        )
    }

    suspend fun finishWorkout(): WorkoutData? {
        val workout = currentWorkout ?: run {
            Log.w("ExerciseService", "Нет активной тренировки для завершения!")
            return null
        }

        val endTime = Instant.now()
        val date = LocalDate.now()

        val durationSeconds =
            ((endTime.toEpochMilli() - workout.startTime.toEpochMilli()) / 1000).toInt().coerceAtLeast(1)
        val durationMinutes = (durationSeconds / 60).coerceAtLeast(1)

        val calories = workout.calories ?: 0.0

        val currentActiveSeconds = activeStorage.getActiveSeconds()

        val currentCalories = activeStorage.getCalories()

        val newActiveSeconds = currentActiveSeconds + durationSeconds
        val newCalories = currentCalories + calories

        activeStorage.setActiveSeconds(newActiveSeconds)
        activeStorage.setCalories(newCalories)

        val workoutData = WorkoutData(
            id = workout.id,
            name = workout.name,
            type = workout.type,
            calories = calories,
            durationSeconds = durationSeconds,
            date = date.toString(),
            plannedDate = workout.plannedDate.toString(),
            isCompleted = true
        )

        Log.d(
            "ExerciseService", """
            Завершена тренировка:
            Тип: ${workout.type}
            Длительность: ${durationMinutes} мин (${durationSeconds} сек)
            Калорий: ${"%.1f".format(calories)}
            Активность: $currentActiveSeconds → $newActiveSeconds
        """.trimIndent()
        )

        val client = HealthConnectClient.getOrCreate(context)
        val caloriesRecord = ActiveCaloriesBurnedRecord(
            startTime = workout.startTime,
            startZoneOffset = ZoneOffset.systemDefault().rules.getOffset(workout.startTime),
            endTime = endTime,
            endZoneOffset = ZoneOffset.systemDefault().rules.getOffset(endTime),
            energy = Energy.kilocalories(workoutData.calories),
            metadata = Metadata.manualEntry()
        )
        client.insertRecords(listOf(caloriesRecord))

        currentWorkout = null
        return workoutData
    }


    data class OngoingWorkout(
        val id: String,
        val name: String,
        val type: String,
        val startTime: Instant,
        val durationSeconds: Int? = null,
        val calories: Double? = null,
        val plannedDate: LocalDate
    )
}
