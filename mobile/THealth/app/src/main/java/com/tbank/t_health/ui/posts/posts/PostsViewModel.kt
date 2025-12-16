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

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<PostData>>(emptyList())
    val posts: StateFlow<List<PostData>> = _posts

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

    fun loadFeed() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val feed = postsRepository.getFeed() // реальные данные
                _posts.value = feed
            } catch (e: Exception) {
                Log.e("PostsViewModel", "Error loading feed", e)
                _posts.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectPost(post: PostData) {
        _selectedPost.value = post
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

                _comments.value = listOf(newComment) + _comments.value
                loadComments(postId)
            } catch (e: Exception) {
                Log.e("PostsViewModel", "Failed to create comment", e)
            }
        }
    }

    fun likePost(postId: Long, userId: Long) {
        viewModelScope.launch {
            try {
                postsRepository.likePost(postId, userId)
                loadFeed()
            } catch (e: Exception) {
                Log.e("PostsViewModel", "Error liking post", e)
            }
        }
    }

    fun toggleLike(postId: Long, userId: Long) {
        viewModelScope.launch {
            val currentLikes = _userLikes.value
            if (postId in currentLikes) {
                postsRepository.unlikePost(postId, userId)
                _userLikes.value = currentLikes - postId
            } else {
                postsRepository.likePost(postId, userId)
                _userLikes.value = currentLikes + postId
            }
            loadFeed()
        }
    }
}