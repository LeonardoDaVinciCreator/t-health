package com.tbank.t_health.data

data class User(
    val nickname: String,
    val fullName: String,
    val phone: String,//рф номер
    val code: String//4-значный код
)