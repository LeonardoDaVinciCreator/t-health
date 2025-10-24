package com.tbank.t_health.data

data class PostData(
    val userName: String,
    val date: String,
    val title: String,
    val text: String,
    val likes: Int,
    val comments: Int
)