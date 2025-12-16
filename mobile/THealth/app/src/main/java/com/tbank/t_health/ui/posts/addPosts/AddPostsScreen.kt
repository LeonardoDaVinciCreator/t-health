package com.tbank.t_health.ui.posts.addPosts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    viewModel: AddPostsViewModel = hiltViewModel(),
    //onBackClick: () -> Unit
) {
    var selectedType by remember { mutableStateOf<PostType?>(null) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var showFullTextEditor by remember { mutableStateOf(false) }

    val isSaveEnabled = title.isNotBlank() && content.isNotBlank() && selectedType != null


    Scaffold(
        topBar = {
            AddPostTopBar()
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.onSavePostClick(
                        title = title,
                        content = content,
                        mediaUrl = null
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                containerColor = Color(0xFFFDD500),
                shape = RoundedCornerShape(11.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Text(
                    text = "Сохранить",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

//            PostTypeBlock(
//                selectedType = selectedType,
//                onTypeSelected = { selectedType = it }
//            )

            TitleBlock(
                value = title,
                onValueChange = { title = it }
            )

            TextContentBlock(
                value = content,
                onClick = { showFullTextEditor = true }
            )

            MediaAttachBlock()
        }
    }

    if (showFullTextEditor) {
        FullScreenTextEditor(
            text = content,
            onDismiss = { showFullTextEditor = false },
            onConfirm = {
                content = it
                showFullTextEditor = false
            }
        )
    }
}
