package com.tbank.t_health.domain.model

data class DailyStats(
    val steps: Int,
    val activeMinutes: Long,
    val calories: Double
) {
    companion object {
        fun empty() = DailyStats(0, 0, 0.0)
    }
}
