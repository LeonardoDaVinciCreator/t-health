package com.tbank.t_health.ui.screens

import HeaderPost
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.tbank.t_health.data.PostData
import com.tbank.t_health.ui.components.*

@Composable
fun PostsScreen(navController: NavController) {
    val posts = List(10) { index ->
        PostData(
            userName = "User_$index",
            date = "22 Октября в ${17 + index % 6}:30",
            title = "Заголовок поста №$index",
            text = "Это пример текста для поста номер $index.Это пример текста для поста номер $index.Это пример текста для поста номер $index.Это пример текста для поста номер $index.Это пример текста для поста номер $index.Это пример текста для поста номер $index.",
            likes = (100..500).random(),
            comments = (5..40).random()
        )
    }

    Scaffold(
        topBar = { HeaderPost() },
        bottomBar = { Footer(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(posts) { post ->
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PostBlock(
                        userName = post.userName,
                        date = post.date,
                        title = post.title,
                        text = post.text,
                        likes = post.likes,
                        comments = post.comments
                    )
                }

            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PostsScreenPreview() {
//    PostsScreen()
//}
