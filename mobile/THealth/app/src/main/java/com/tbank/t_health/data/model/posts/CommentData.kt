package com.tbank.t_health.data.model.posts

import com.google.gson.annotations.SerializedName

data class CommentData(
    @SerializedName("id") val id: Long?,
    @SerializedName("postId") val postId: Long,
    @SerializedName("authorId") val authorId: Long,
    @SerializedName("authorName") val authorName: String,
    @SerializedName("text") val text: String,
    @SerializedName("createdAt") val createdAt: String
)

data class CommentCreateData(
    @SerializedName("postId") val postId: Long,
    @SerializedName("authorId") val authorId: Long,
    @SerializedName("text") val text: String
)