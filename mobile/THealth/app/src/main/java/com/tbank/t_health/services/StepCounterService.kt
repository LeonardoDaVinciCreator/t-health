package com.tbank.composefoodtracker.services

import android.content.Context
import android.util.Log

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit


import androidx.health.connect.client.units.Length
import androidx.health.connect.client.units.Mass


class StepCounterService(private val context: Context) {

    suspend fun getDistanceForToday(): Double {
        val startOfDay = ZonedDateTime.now().toLocalDate()
            .atStartOfDay(ZoneId.systemDefault()).toInstant()
        val endOfDay = startOfDay.plus(Duration.ofDays(1)).minusNanos(1)
        return getDistanceForRange(startOfDay, endOfDay)
    }

    suspend fun getDistanceForDate(date: LocalDate): Double {
        val start = date.atStartOfDayInSystemZone()
        val end = date.atEndOfDayInSystemZone()
        return getDistanceForRange(start, end)
    }

    private suspend fun getDistanceForRange(startTime: Instant, endTime: Instant): Double {
        val client = HealthConnectClient.getOrCreate(context)
        return try {
            val response = client.readRecords(
                ReadRecordsRequest(
                    recordType = DistanceRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // сумма всех расстояний в метрах
            response.records.sumOf { it.distance.inMeters }
        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }

    private suspend fun readSteps(startTime: Instant, endTime: Instant): Int {
        val client = HealthConnectClient.getOrCreate(context)
        return try {
            val response = client.readRecords(
                ReadRecordsRequest(
                    recordType = StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            response.records.sumOf { it.count.toInt() }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    suspend fun getStepsForToday(): Int {
        val startOfDay = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()
        val endOfDay = startOfDay.plus(Duration.ofDays(1)).minusNanos(1)
        return readSteps(startOfDay, endOfDay)
    }

    suspend fun getCaloriesForToday(): Double {
        val startOfDay = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()
        val endOfDay = startOfDay.plus(Duration.ofDays(1)).minusNanos(1)
        val calories = getTotalCaloriesForDate(startOfDay, endOfDay)
        if (calories > 0) return calories
        return 0.0
    }

    suspend fun getStepsForDate(date: LocalDate): Int {
        val start = date.atStartOfDayInSystemZone()
        val end = date.atEndOfDayInSystemZone()
        return readSteps(start, end)
    }
    suspend fun getStepsForDate(startTime: Instant, endTime: Instant): Int {
        return readSteps(startTime, endTime)
    }

    suspend fun getCaloriesForDate(date: LocalDate): Double {
        val start = date.atStartOfDayInSystemZone()
        val end = date.atEndOfDayInSystemZone()
        return getTotalCaloriesForDate(start, end)
    }

    suspend fun getCaloriesForDate(startTime: Instant, endTime: Instant): Double {
        return getTotalCaloriesForDate(startTime, endTime)
    }

    private suspend fun getTotalCaloriesForDate(startTime: Instant, endTime: Instant): Double {
        val healthConnectClient = HealthConnectClient.getOrCreate(context)
        return try {
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = TotalCaloriesBurnedRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            response.records.sumOf { it.energy.inKilocalories }
        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }

    suspend fun getCaloriesFromStepsAndActiveCalories(): Double {
        val client = HealthConnectClient.getOrCreate(context)

        val startOfDay = ZonedDateTime.now().toLocalDate()
            .atStartOfDay(ZoneId.systemDefault()).toInstant()
        val endOfNow = Instant.now()

        return try {
            val stepsRecords = client.readRecords(
                ReadRecordsRequest(
                    recordType = StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startOfDay, endOfNow)
                )
            ).records

            val totalSteps = stepsRecords.sumOf { it.count }

            val minutesFromSteps = totalSteps / 100        // 100 шагов ≈ 1 минута
            val caloriesFromSteps = minutesFromSteps * 4.5 // 4.5 ккал/мин — примерное среднее

            val activeCaloriesRecords = client.readRecords(
                ReadRecordsRequest(
                    recordType = ActiveCaloriesBurnedRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startOfDay, endOfNow)
                )
            ).records


            val caloriesFromActive = activeCaloriesRecords.sumOf { it.energy.inKilocalories }
            Log.d("caloriesFromActive", "Калории сейчас: $caloriesFromActive")

            caloriesFromSteps + caloriesFromActive

        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }


    fun LocalDate.atStartOfDayInSystemZone(): Instant {
        return this.atStartOfDay(ZoneId.systemDefault()).toInstant()
    }

    fun LocalDate.atEndOfDayInSystemZone(): Instant {
        return this.atStartOfDay(ZoneId.systemDefault())
            .plus(Duration.ofDays(1))
            .minusNanos(1)
            .toInstant()
    }

    suspend fun getActiveMinutesForToday(): Long {
        val startOfDay = ZonedDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()
        val endOfDay = startOfDay.plus(Duration.ofDays(1)).minusNanos(1)
        return getActiveMinutesForRange(startOfDay, endOfDay)
    }

    suspend fun getActiveMinutesForDate(date: LocalDate): Long {
        val start = date.atStartOfDayInSystemZone()
        val end = date.atEndOfDayInSystemZone()
        return getActiveMinutesForRange(start, end)
    }

    private suspend fun getActiveMinutesForRange(startTime: Instant, endTime: Instant): Long {
        val client = HealthConnectClient.getOrCreate(context)


        return try {
            val stepsResponse = client.readRecords(
                ReadRecordsRequest(
                    recordType = StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            val allSteps = stepsResponse.records

            val sessionsResponse = client.readRecords(
                ReadRecordsRequest(
                    recordType = ExerciseSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            val sessions = sessionsResponse.records

            val totalSteps = allSteps.sumOf { it.count }
            val minutesFromSteps = totalSteps / 100  // 100 шагов ≈ 1 минута

            val minutesFromSessions = sessions.sumOf {
                ChronoUnit.MINUTES.between(it.startTime, it.endTime)
            }

            // - шаги, которые попали во время тренировок
            val stepsDuringSessions = allSteps.filter { step ->
                sessions.any { session ->
                    step.startTime < session.endTime && step.endTime > session.startTime
                }
            }.sumOf { it.count }

            val minutesFromStepsOutsideSessions = (totalSteps - stepsDuringSessions) / 100

            minutesFromStepsOutsideSessions + minutesFromSessions

        } catch (e: Exception) {
            e.printStackTrace()
            0L
        }
    }



}