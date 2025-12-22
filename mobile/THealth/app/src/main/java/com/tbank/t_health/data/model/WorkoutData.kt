package com.tbank.t_health.data.model

import com.tbank.t_health.components.DisplayableType
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

enum class WorkoutType(override val displayName: String) : DisplayableType {
    CARDIO("Кардио"),
    STRENGTH("Силовая"),
    ENDURANCE("На выносливость"),
    FLEXIBILITY("Гибкость"),
    BALANCE("Баланс")
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
