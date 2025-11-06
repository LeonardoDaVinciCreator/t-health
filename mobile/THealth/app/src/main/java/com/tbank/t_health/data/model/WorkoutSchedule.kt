package com.tbank.t_health.data.model

import java.time.DayOfWeek

data class WorkoutSchedule(
    val dayOfWeek: DayOfWeek,
    val workouts: List<WorkoutData>
)
