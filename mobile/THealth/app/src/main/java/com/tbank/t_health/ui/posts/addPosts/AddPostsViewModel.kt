package com.tbank.t_health.ui.posts.addPosts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.t_health.data.repository.PostsRepository
import com.tbank.t_health.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPostsViewModel @Inject constructor(
    private val postsRepository: PostsRepository,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    fun onSavePostClick(title: String, content: String, mediaUrl: String?) {
        val user = getUserUseCase() ?: return
        val userId = user.id ?: return
        viewModelScope.launch {
            try {
                postsRepository.createPost(userId, title, content, mediaUrl)
                postsRepository.loadFeed()
            } catch (e: Exception) {
                Log.e("POSTS", "Error: ${e.message}", e)
            }
        }
    }
}