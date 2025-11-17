package com.tbank.t_health.network

import com.tbank.t_health.data.model.*
import retrofit2.http.*

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
