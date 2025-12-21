package com.tbank.t_health.ui.posts.posts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.t_health.data.model.posts.CommentData
import com.tbank.t_health.data.model.posts.PostData
import com.tbank.t_health.data.repository.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*
fun formatDate(createdAt: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault())
        val date = inputFormat.parse(createdAt)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        createdAt.takeLast(16)
    }
}

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<PostData>>(emptyList())
    val posts: StateFlow<List<PostData>> = _posts

    private val _userNames = MutableStateFlow<Map<Long, String>>(emptyMap())
    val userNames: StateFlow<Map<Long, String>> = _userNames

    private val userCache = mutableMapOf<Long, String>()

    private val postCounters = mutableMapOf<Long, PostCounters>()
    private data class PostCounters(
        var likesCount: Int,
        var commentsCount: Int
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedPost = MutableStateFlow<PostData?>(null)
    val selectedPost: StateFlow<PostData?> = _selectedPost

    private val _comments = MutableStateFlow<List<CommentData>>(emptyList())
    val comments: StateFlow<List<CommentData>> = _comments

    private val _commentsLoading = MutableStateFlow(false)
    val commentsLoading: StateFlow<Boolean> = _commentsLoading

    private val _userLikes = MutableStateFlow<Set<Long>>(emptySet())
    val userLikes: StateFlow<Set<Long>> = _userLikes

    private val _showPostDetail = MutableStateFlow<PostData?>(null)
    val showPostDetail: StateFlow<PostData?> = _showPostDetail

    private val _showComments = MutableStateFlow<Long?>(null)
    val showComments: StateFlow<Long?> = _showComments

    private fun updatePostCountersLocally(postId: Long, likesDelta: Int = 0, commentsDelta: Int = 0) {
        val counters = postCounters.getOrPut(postId) {
            PostCounters(0, 0)
        }
        counters.likesCount += likesDelta
        counters.commentsCount += commentsDelta

        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                post.copy(
                    likesCount = counters.likesCount,
                    commentsCount = counters.commentsCount
                )
            } else post
        }
    }

    fun loadFeed() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val feed = postsRepository.getFeed() // реальные данные
                _posts.value = feed

                loadUsersForPosts(feed)

                postCounters.clear()
                feed.forEach { post ->
                    postCounters[post.id ?: 0L] = PostCounters(post.likesCount, post.commentsCount)
                }
            } catch (e: Exception) {
                Log.e("PostsViewModel", "Error loading feed", e)
                _posts.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadUsersForPosts(posts: List<PostData>) {
        try {
            val allUsers = postsRepository.getAllUsers()
            val uniqueUserIds = posts.map { it.userId }.distinct()

            allUsers.forEach { user ->
                if(user.id != null){
                    userCache[user.id] = user.username
                }
            }

            _userNames.value = userCache.toMap()
        } catch (e: Exception) {
            Log.e("PostsViewModel", "Error loading users", e)
        }
    }

    fun getUserName(userId: Long): String {
        return userCache[userId] ?: "anonim"
    }

    fun getFormattedDate(createdAt: String): String = formatDate(createdAt)

    fun selectPost(post: PostData) {
        _selectedPost.value = post
    }

    // Диалоги
    fun openPostDetail(post: PostData) {
        _showPostDetail.value = post
    }

    fun closePostDetail() {
        _showPostDetail.value = null
    }

    fun openComments(postId: Long) {
        _showComments.value = postId
    }

    fun closeComments() {
        _showComments.value = null
    }

    fun loadComments(postId: Long) {
        viewModelScope.launch {
            _commentsLoading.value = true
            try {
                val comments = postsRepository.getComments(postId)
                _comments.value = comments
            } catch (e: Exception) {
                Log.e("PostsViewModel", "Error loading comments", e)
                _comments.value = emptyList()
            } finally {
                _commentsLoading.value = false
            }
        }
    }

    fun createComment(postId: Long, authorId: Long, text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            try {
                val newComment = postsRepository.createComment(postId, authorId, text)

                updatePostCountersLocally(postId, commentsDelta = +1)

                _comments.value = listOf(newComment) + _comments.value
                loadComments(postId)
            } catch (e: Exception) {
                Log.e("PostsViewModel", "Failed to create comment", e)
            }
        }
    }

    fun toggleLike(postId: Long, userId: Long) {
        viewModelScope.launch {
            val currentLikes = _userLikes.value
            val isCurrentlyLiked = postId in currentLikes

            if (isCurrentlyLiked) {
                _userLikes.value = currentLikes - postId
                updatePostCountersLocally(postId, likesDelta = -1)
                try {
                    postsRepository.unlikePost(postId, userId)
                } catch (e: Exception) {
                    _userLikes.value = currentLikes
                    updatePostCountersLocally(postId, likesDelta = +1)
                    Log.e("PostsViewModel", "Error unliking post", e)
                }
            } else {
                _userLikes.value = currentLikes + postId
                updatePostCountersLocally(postId, likesDelta = +1)
                try {
                    postsRepository.likePost(postId, userId)
                } catch (e: Exception) {
                    _userLikes.value = currentLikes
                    updatePostCountersLocally(postId, likesDelta = -1)
                    Log.e("PostsViewModel", "Error liking post", e)
                }
            }
        }
    }
}