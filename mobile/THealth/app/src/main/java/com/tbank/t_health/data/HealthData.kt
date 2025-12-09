package com.tbank.t_health.data

import java.time.LocalDate

data class HealthData(
    val date: LocalDate,
    val steps: Int = 0,
    val calories: Double = 0.0,
    val activeMinutes: Int = 0//минуты
)

