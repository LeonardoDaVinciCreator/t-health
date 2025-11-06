package com.tbank.t_health.data.model

import java.time.LocalDate

data class ActivityData (
    val id: String,
    val activeMinutes: Int,
    val calories: Double,
    val date: LocalDate
)

//{
//    "steps": 9500,
//    "activeMinutes": 110,
//    "calories": 392.5,
//    "date": "2025-10-30"
//}
