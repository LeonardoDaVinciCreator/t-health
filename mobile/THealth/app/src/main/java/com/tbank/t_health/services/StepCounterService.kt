package com.tbank.composefoodtracker.services

import android.content.Context

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import java.time.ZonedDateTime
import androidx.health.connect.client.records.metadata.Metadata


class StepCounterService(private val context: Context) {

    //suspend - корутина, асинхронка
    suspend fun getStepsForTimePeriod(minutes: Int): Int {
        val healthConnectSteps = getStepsFromHealthService(minutes)
        if (healthConnectSteps > 0) return healthConnectSteps

        return 0
    }


    //точно работает
    private suspend fun getStepsFromHealthService(minutes: Int): Int {
        val healthConnectClient = HealthConnectClient.getOrCreate(context)
        val endTime = Instant.now()
        val startTime = endTime.minusSeconds(minutes * 60L)
        return try {
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType  = StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            response.records.sumOf { it.count.toInt() }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    suspend fun writeStepsToHealthConnect(startTime: Instant,
                                                  endTime: Instant,
                                                  stepsCount: Long) {
        if (stepsCount <= 0) return

        val healthConnectClient = HealthConnectClient.getOrCreate(context)
        val stepsRecord = StepsRecord(
            count = stepsCount,
            startTime = startTime,
            endTime = endTime,
            startZoneOffset = ZonedDateTime.now().offset,
            endZoneOffset = ZonedDateTime.now().offset,
            // Метаданные (необязательно)
            metadata = Metadata()
        )
        try {
            healthConnectClient.insertRecords(listOf(stepsRecord))
            println("Successfully wrote $stepsCount steps to Health Connect for the period: $startTime to $endTime")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to write steps to Health Connect: ${e.message}")
        }
    }
    suspend fun getStepsForToday(): Int {
        val healthConnectSteps = getStepsFromHealthServiceForToday()
        if (healthConnectSteps > 0) return healthConnectSteps

        return 0
    }

    private suspend fun getStepsFromHealthServiceForToday(): Int {
        val healthConnectClient = HealthConnectClient.getOrCreate(context)

        val now = Instant.now()
        val startOfDay = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).toInstant()
        val endOfDay = ZonedDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999).toInstant()

        return try {
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startOfDay, endOfDay)
                )
            )
            response.records.sumOf { it.count.toInt() }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

}