//package com.tbank.t_health.data.model
//
//import java.time.LocalDate
//
//data class WorkoutData(
//    val id: String,
//    val name: String,
//    val type: String,
//    val calories: Double,
//    val durationSeconds: Int,
//    val date: String,
//
//    val plannedDate: String,
//
//    val isCompleted: Boolean
//)

package com.tbank.t_health.data.model

import java.time.LocalDate

data class WorkoutData(
    val id: Long? = null,
    val userId: Long,
    val name: String,
    val type: String,
    val calories: Double,
    val durationSeconds: Int,
    val plannedDate: LocalDate,
    val isCompleted: Boolean = false
)

enum class WorkoutType(val displayName: String) {
    CARDIO("Кардио"),
    STRENGTH("Силовая"),
    ENDURANCE("На выносливость"),
    FLEXIBILITY("Гибкость"),
    BALANCE("Баланс");

    companion object {
        fun fromDisplayName(name: String): WorkoutType? =
            entries.firstOrNull { it.displayName == name }
    }
}

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
