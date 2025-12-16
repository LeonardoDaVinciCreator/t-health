package com.tbank.t_health.ui.posts.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.tbank.t_health.R
import com.tbank.t_health.data.model.posts.PostData

@Composable
fun PostsScreen(
    userId: Long? = null,
    viewModel: PostsViewModel = hiltViewModel()
) {
    val posts by viewModel.posts.collectAsState()
    val userLikes by viewModel.userLikes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val heartPainter =  painterResource(R.drawable.ic_heart)
    val commentsPainter = painterResource(R.drawable.ic_comments)
    val addMediaPainter = painterResource(R.drawable.ic_add_media)

    var showPostDetail by remember { mutableStateOf<PostData?>(null) }
    var showComments by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadFeed()
    }

    showPostDetail?.let { post ->
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showPostDetail = null },
            title = { },
            text = {
                PostDetailScreen(
                    post = post,
                    onClose = { showPostDetail = null },
                    onCommentsClick = {
                        showPostDetail = null
                        showComments = post.id
                    },
                    userId = userId
                )
            },
            confirmButton = {},
            dismissButton = {}
        )
    }

    // Модальное окно комментариев
    showComments?.let { postId ->
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showComments = null },
            title = { },
            text = {
                CommentsScreen(
                    postId = postId,
                    onBack = { showComments = null },
                    userId = userId
                )
            },
            confirmButton = {},
            dismissButton = {}
        )
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(12.dp)
        ) {
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator()
                    }
                }
            }

            items(posts, key = { it.id.toString() }) { post ->
                PostCard(
                    post = post,
                    heartPainter = heartPainter,
                    commentsPainter = commentsPainter,
                    addMediaPainter = addMediaPainter,
                    onPostClick = { showPostDetail = post },
                    onCommentsClick = { showComments = post.id ?: 0L },
                    onLikeClick = {
                        userId?.let { uid ->
                            viewModel.toggleLike(post.id ?: 0, uid)
                        }
                    },
                    userId = userId
                )
            }
        }
    }
}
