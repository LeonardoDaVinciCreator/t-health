//package com.tbank.t_health.data.model
//
//import java.time.LocalDate
//
//data class ActivityData (
//    val id: String,
//    val activeMinutes: Int,
//    val calories: Double,
//    val activeMinutes: Int,
//    val date: LocalDate
//)

package com.tbank.t_health.data.model

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class ActivityData(
    val id: Long? = null,
    val userId: Long,
    val value: BigDecimal,
    val type: ActivityType, // STEPS, TRAINING, MOVING
    val calories: Double,
    //val date: LocalDateTime? = null
)

data class ActivityGetData(
    val id: Long? = null,
    val userId: Long,
    val value: BigDecimal,
    val type: ActivityType, // STEPS, TRAINING, MOVING
    val calories: Double,
    val date: String ? = null
)

data class ActivityFullData(
    val steps: Int,
    val activeMinutes: Int,
    val calories: Double,
    val date: LocalDate
)

enum class ActivityType {
    STEPS,
    TRAINING,
    MOVING,
    NOT_SUPPORTED
}