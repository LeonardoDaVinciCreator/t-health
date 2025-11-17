package com.tbank.t_health.data.model.Dto

//data class ActivityCreateDto(
//    val userId: Long,
//    val type: String,        // STEPS, TRAINING, MOVING
//    val value: Double,       // количество шагов или минут
//    val calories: Double
//)

enum class ActivityType {
    STEPS,
    TRAINING,
    MOVING,
    NOT_SUPPORTED
}

data class ActivityCreateDto(
    val userId: Long,
    val type: ActivityType, // enum вместо String
    val value: Double,
    val calories: Double
)