package com.tbank.t_health.domain

import com.tbank.t_health.data.model.ActivityFullData
import com.tbank.t_health.domain.model.DailyStats

fun ActivityFullData.toDailyStats(): DailyStats {
    return DailyStats(
        steps = steps,
        activeMinutes = activeMinutes.toLong(),
        calories = calories.toDouble()
    )
}