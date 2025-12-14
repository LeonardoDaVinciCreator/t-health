package com.tbank.t_health.data.repository

import android.util.Log
import com.tbank.t_health.data.model.posts.PostCreateData
import com.tbank.t_health.data.remote.HealthApiService
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val api: HealthApiService
) {
    suspend fun createPostAndLog(userId: Long, title: String, content: String, mediaUrl: String?) {
        val request = PostCreateData(
            userId = userId,
            title = title,
            content = content,
            mediaUrl = mediaUrl
        )
        val created = api.createPost(request)
        Log.d("POSTS", "Created post: $created")
    }

    suspend fun loadFeedAndLog(page: Int = 0, size: Int = 20) {
        val feed = api.getFeed(page, size)
        Log.d("POSTS", "Feed size = ${feed.size}")
        feed.forEach { Log.d("POSTS", "Post item = $it") }
    }

    suspend fun likePostAndLog(postId: Long, userId: Long) {
        val like = api.like(postId, userId)
        Log.d("POSTS", "Like created: $like")
    }

    suspend fun loadCommentsAndLog(postId: Long) {
        val comments = api.getPostComments(postId)
        Log.d("POSTS", "Comments count = ${comments.size}")
        comments.forEach { Log.d("POSTS", "Comment = $it") }
    }
}
