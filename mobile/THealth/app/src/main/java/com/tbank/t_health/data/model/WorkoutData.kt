package com.tbank.t_health.data.model

import java.time.LocalDate

data class WorkoutData(
    val id: String,
    val name: String,
    val type: String,
    val calories: Double,
    val durationSeconds: Int,
    val date: String,

    val plannedDate: String,

    val isCompleted: Boolean
)


//{
//    "name": "Беговая тренировка",
//    "type": "running",
//    "calories": 420.8,
//    "durationMinutes": 35,
//    "date": "2025-10-30",
//    "steps": 4800,
//    "distanceMeters": 3800.5,
//    "maxSpeedMps": 3.6
//}
