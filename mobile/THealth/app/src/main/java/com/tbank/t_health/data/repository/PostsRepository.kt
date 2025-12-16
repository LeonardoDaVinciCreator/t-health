package com.tbank.t_health.data.repository

import android.util.Log
import com.tbank.t_health.data.model.posts.CommentCreateData
import com.tbank.t_health.data.model.posts.CommentData
import com.tbank.t_health.data.model.posts.PostCreateData
import com.tbank.t_health.data.remote.HealthApiService
import javax.inject.Inject

class PostsRepository @Inject constructor(
    private val api: HealthApiService
) {
    suspend fun getFeed(
        page: Int = 0,
        size: Int = 20
    ) = api.getFeed(page, size)

    suspend fun createPost(userId: Long, title: String, content: String, mediaUrl: String?) {
        val request = PostCreateData(
            userId = userId,
            title = title,
            content = content,
            mediaUrl = mediaUrl
        )
        val created = api.createPost(request)
        Log.d("POSTS", "Created post: $created")
    }

    suspend fun loadFeed(page: Int = 0, size: Int = 20) {
        val feed = api.getFeed(page, size)
        Log.d("POSTS", "Feed size = ${feed.size}")
        feed.forEach { Log.d("POSTS", "Post item = $it") }
    }

    suspend fun likePost(postId: Long, userId: Long) {
        val like = api.like(postId, userId)
        Log.d("POSTS", "Like created: $like")
    }

    suspend fun unlikePost(postId: Long, userId: Long) {
        api.unlikePost(postId, userId)
        Log.d("POSTS", "Like removed for post $postId by user $userId")
    }

    suspend fun loadComments(postId: Long): List<CommentData> {
        val comments = api.getPostComments(postId)
        Log.d("POSTS", "Comments count = ${comments.size}")
        comments.forEach { Log.d("POSTS", "Comment = $it") }
        return comments
    }

    suspend fun createComment(postId: Long, authorId: Long, text: String): CommentData {
        val comment = CommentCreateData(
            postId = postId,
            authorId = authorId,
            text = text
        )
        return api.createComment(comment)
    }

    suspend fun getComments(postId: Long): List<CommentData> {
        return api.getPostComments(postId)
    }
}
