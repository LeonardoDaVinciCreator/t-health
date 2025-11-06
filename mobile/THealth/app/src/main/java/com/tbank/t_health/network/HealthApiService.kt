package com.tbank.t_health.network

import com.tbank.t_health.data.model.*
import retrofit2.http.*

interface HealthApiService {

    @GET("activity")
    suspend fun getAllActivities(): List<ActivityData>

    @GET("activity/{id}")
    suspend fun getActivityById(@Path("id") id: String): ActivityData

    @POST("activity")
    suspend fun postActivity(@Body activity: ActivityData)

    @PUT("activity/{id}")
    suspend fun updateActivity(
        @Path("id") id: String,
        @Body activity: ActivityData
    )

    @DELETE("activity/{id}")
    suspend fun deleteActivity(@Path("id") id: String)




    @GET("workouts")
    suspend fun getAllWorkouts(): List<WorkoutData>

    @GET("workouts/{id}")
    suspend fun getWorkoutById(@Path("id") id: String): WorkoutData

    @POST("workouts")
    suspend fun postWorkout(@Body workout: WorkoutData)

    @PUT("workouts/{id}")
    suspend fun updateWorkout(
        @Path("id") id: String,
        @Body workout: WorkoutData
    )

    @DELETE("workouts/{id}")
    suspend fun deleteWorkout(@Path("id") id: String)




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
