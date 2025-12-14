package com.tbank.t_health.data.remote

import com.tbank.t_health.data.model.AchievementData
import com.tbank.t_health.data.model.ActivityData
import com.tbank.t_health.data.model.ActivityGetData
import com.tbank.t_health.data.model.NutritionCreateData
import com.tbank.t_health.data.model.NutritionGetData
import com.tbank.t_health.data.model.TrainingCreateData
import com.tbank.t_health.data.model.TrainingGetData
import com.tbank.t_health.data.model.TrainingUpdateData
import com.tbank.t_health.data.model.UserData
import com.tbank.t_health.data.model.WorkoutData
import com.tbank.t_health.data.model.posts.CommentCreateData
import com.tbank.t_health.data.model.posts.CommentData
import com.tbank.t_health.data.model.posts.LikeData
import com.tbank.t_health.data.model.posts.PostCreateData
import com.tbank.t_health.data.model.posts.PostData
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

    // ----------- Nutrition -----------

    @POST("nutrition")
    suspend fun createNutrition(
        @Body data: NutritionCreateData
    ): NutritionGetData

    @GET("nutrition/user/{userId}")
    suspend fun getUserNutritions(
        @Path("userId") userId: Long
    ): List<NutritionGetData>

    @GET("nutrition/{id}")
    suspend fun getNutritionById(
        @Path("id") id: Long
    ): NutritionGetData

    @DELETE("nutrition/{id}")
    suspend fun deleteNutrition(
        @Path("id") id: Long
    )

    // ----------- Posts -----------

    @POST("posts/post")
    suspend fun createPost(@Body post: PostCreateData): PostData

    @GET("posts")
    suspend fun getFeed(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): List<PostData>

    @DELETE("posts/post/{postId}")
    suspend fun deletePost(@Path("postId") postId: Long): Unit

    @POST("posts/{postId}/like")
    suspend fun like(
        @Path("postId") postId: Long,
        @Query("userId") userId: Long
    ): LikeData

    @DELETE("posts/post/{postId}/like")
    suspend fun unlikePost(
        @Path("postId") postId: Long,
        @Query("userId") userId: Long
    ): LikeData

    @POST("posts/comment")
    suspend fun createComment(@Body comment: CommentCreateData): CommentData

    @GET("posts/{postId}/comments")
    suspend fun getPostComments(@Path("postId") postId: Long): List<CommentData>

    @DELETE("posts/comment/{commentId}")
    suspend fun deleteComment(@Path("commentId") commentId: Long)
}