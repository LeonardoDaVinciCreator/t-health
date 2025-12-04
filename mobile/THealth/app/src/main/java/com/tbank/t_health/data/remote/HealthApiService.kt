package com.tbank.t_health.data.remote

import com.tbank.t_health.data.model.AchievementData
import com.tbank.t_health.data.model.ActivityData
import com.tbank.t_health.data.model.ActivityGetData
import com.tbank.t_health.data.model.TrainingCreateData
import com.tbank.t_health.data.model.TrainingGetData
import com.tbank.t_health.data.model.TrainingUpdateData
import com.tbank.t_health.data.model.UserData
import com.tbank.t_health.data.model.WorkoutData
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface HealthApiService {

    // ----------- USERS -----------

    @GET("users")
    suspend fun getAllUsers(): List<UserData>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Long): UserData

    @POST("users")
    suspend fun createUser(@Body user: UserData): UserData


    // ----------- ACTIVITIES -----------

    @GET("users/{userId}/activities")
    suspend fun getUserActivities(
        @Path("userId") userId: Long,
        @Query("date") date: String? = null
    ): List<ActivityGetData>

    @POST("users/{userId}/activitie" +
            "s")
    suspend fun createActivity(
        @Path("userId") userId: Long,
        @Body activity: ActivityData
    ): ActivityData

    // ----------- Training -----------

    @GET("training/user/{userId}")
    suspend fun getUserTrainings(
        @Path("userId") userId: Long
    ): List<TrainingGetData>

    @POST("training")
    suspend fun createTraining(
        @Body training: TrainingCreateData
    ): TrainingGetData

    @PATCH("training/{id}")
    suspend fun updateTraining(
        @Path("id") id: Long,
        @Body training: TrainingUpdateData
    ): TrainingGetData

    @DELETE("training/{id}")
    suspend fun deleteTraining(
        @Path("id") id: Long
    )










    @GET("achievements")
    suspend fun getAllAchievements(): List<AchievementData>

    @GET("achievements/{id}")
    suspend fun getAchievementById(@Path("id") id: String): AchievementData

    @POST("achievements")
    suspend fun postAchievement(@Body achievement: AchievementData)

    @PUT("achievements/{id}")
    suspend fun updateAchievement(
        @Path("id") id: String,
        @Body achievement: AchievementData
    )

    @DELETE("achievements/{id}")
    suspend fun deleteAchievement(@Path("id") id: String)
}