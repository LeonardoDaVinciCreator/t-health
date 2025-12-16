package com.tbank.t_health.ui.posts.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _posts = MutableStateFlow<List<PostData>>(fakePosts)//фейковые данные для проверки
    val posts: StateFlow<List<PostData>> = _posts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadFeed() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val feed = postsRepository.getFeed()
//                _posts.value = feed
//            } finally {
//                _isLoading.value = false
//            }
//        }
    }

    fun likePost(postId: Long, userId: Long) {
        viewModelScope.launch {
            postsRepository.likePostAndLog(postId, userId)
        }
    }
}

private val fakePosts = listOf(
    PostData(
        id = 1,
        userId = 101,
        title = "Салат с сыром фета и кедровыми орешками",
        content = "Приготовьте вкуснейший салат из свежих овощей с добавлением феты, кедровых орешков и зелени. Приготовьте вкуснейший салат из свежих овощей с добавлением феты, кедровых орешков и зелени.",
        mediaUrl = "https://ic.pics.livejournal.com/foodmorning/76676210/108899/108899_800.jpg", // позже можно подставить URL
        createdAt = "2025-02-15",
        likesCount = 0,
        commentsCount = 0
    ),
    PostData(
        id = 2,
        userId = 102,
        title = "Пора начинать бегать",
        content = "Утренние пробежки укрепляют сердце и улучшают настроение. Начните с 10 минут.",
        mediaUrl = "https://img.championat.com/news/big/w/x/kak-pravilno-nachat-begat_16196125021604080516.jpg",
        createdAt = "2025-02-14",
        likesCount = 0,
        commentsCount = 0
    ),
    PostData(
        id = 1,
        userId = 101,
        title = "Салат с сыром фета и кедровыми орешками",
        content = "Приготовьте вкуснейший салат из свежих овощей с добавлением феты, кедровых орешков и зелени.",
        mediaUrl = null, // позже можно подставить URL
        createdAt = "2025-02-15",
        likesCount = 0,
        commentsCount = 0
    ),
    PostData(
        id = 2,
        userId = 102,
        title = "Пора начинать бегать",
        content = "Утренние пробежки укрепляют сердце и улучшают настроение. Начните с 10 минут.",
        mediaUrl = null,
        createdAt = "2025-02-14",
        likesCount = 0,
        commentsCount = 0
    )
)