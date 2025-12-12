package com.tbank.t_health.data.model.posts

import com.google.gson.annotations.SerializedName

data class LikeData(
    @SerializedName("id") val id: Long?,
    @SerializedName("postId") val postId: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("createdAt") val createdAt: String
)