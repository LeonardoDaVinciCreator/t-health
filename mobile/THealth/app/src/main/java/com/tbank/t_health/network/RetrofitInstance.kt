package com.tbank.t_health.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://exampe:8080/api/v1/" // поменять на свой сервер
    // Адаптер беспроводной локальной сети Беспроводная сеть:
    //IPv4-адрес. . . . . . . . . . . . : значение

    val api: HealthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HealthApiService::class.java)
    }
}
