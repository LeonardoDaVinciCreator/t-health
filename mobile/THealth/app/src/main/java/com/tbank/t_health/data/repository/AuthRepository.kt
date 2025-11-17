package com.tbank.t_health.data.repository

import com.tbank.t_health.data.model.UserData
import com.tbank.t_health.network.RetrofitInstance

class AuthRepository {
    private val api = RetrofitInstance.api

    suspend fun registerUser(nickname: String, phone: String): UserData {
        val request = UserData(username = nickname, phone = phone)
        return api.createUser(request)
    }
}
