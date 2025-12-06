package com.tbank.t_health.data.repository

import com.tbank.t_health.data.model.NutritionCreateData
import com.tbank.t_health.data.remote.HealthApiService

class NutritionRepository(private val api: HealthApiService) {
    suspend fun createNutrition(data: NutritionCreateData) =
        api.createNutrition(data)

    suspend fun getUserNutritions(userId: Long) =
        api.getUserNutritions(userId)

    suspend fun deleteNutrition(id: Long) =
        api.deleteNutrition(id)

    suspend fun getNutritionById(id: Long) =
        api.getNutritionById(id)
}