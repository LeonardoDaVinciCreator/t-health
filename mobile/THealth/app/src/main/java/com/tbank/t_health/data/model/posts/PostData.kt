package com.tbank.t_health.data.model.posts

import com.google.gson.annotations.SerializedName

data class PostData(
    @SerializedName("id") val id: Long?,
    @SerializedName("userId") val userId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("mediaUrl") val mediaUrl: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("likesCount") val likesCount: Int,
    @SerializedName("commentsCount") val commentsCount: Int
)

data class PostCreateData(
    @SerializedName("userId") val userId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("mediaUrl") val mediaUrl: String? = null//
)