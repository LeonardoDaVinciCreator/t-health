package com.tbank.t_health.data.model

import java.time.LocalDate

class AchievementData (
    val id: String,
    val title: String,
    val reason: String,
    val iconUrl: String?,
    val achievedDate: LocalDate?,
    val progress: Int? = null,
    val isAchieved: Boolean = false
)

//{
//    "title": "10 000 шагов в день!",
//    "reason": "Вы прошли 10 000 шагов за день впервые!",
//    "iconUrl": "https://example.com/icons/steps_10000.png",
//    "achievedDate": "2025-10-30",
//    "progress": 100,
//    "isAchieved": true
//}
