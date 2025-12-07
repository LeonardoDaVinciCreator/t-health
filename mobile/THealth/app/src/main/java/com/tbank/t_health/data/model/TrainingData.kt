package com.tbank.t_health.data.model

data class TrainingGetData(
    val id: Long? = null,
    val userId: Long,
    val title: String,
    val durationSecs: Long,
    val type: String,
    val calories: Int,
    val date: String
)

data class TrainingCreateData(
    val userId: Long,
    val title: String,
    val type: String,
    val durationSecs: Long,
    val calories: Int,
    val date: String
)

data class TrainingUpdateData(
    val title: String? = null,
    val type: String? = null,
    val duration: Long? = null,
    val calories: Int? = null,
    val date: String? = null
)