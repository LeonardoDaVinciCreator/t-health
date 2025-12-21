package com.tbank.t_health.ui.posts.posts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tbank.t_health.R
import com.tbank.t_health.data.model.posts.CommentData
import com.tbank.t_health.data.model.posts.PostData
import com.tbank.t_health.ui.theme.RobotoFontFamily
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tbank.t_health.ui.theme.RobotoMonoFontFamily

@Composable
fun PostCard(
    post: PostData,
    heartPainter: Painter,
    commentsPainter: Painter,
    addMediaPainter: Painter,
    viewModel: PostsViewModel,
    onPostClick: (PostData) -> Unit,
    onCommentsClick: (Long) -> Unit,
    onLikeClick: (Long) -> Unit,
    isLiked: Boolean = false
) {
    val userName = viewModel.getUserName(post.userId)
    val formattedDate = formatDate(post.createdAt)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PostAuthorRow(
            authorName = userName
        )

        if (post.mediaUrl != null) {
            PostMedia(post.mediaUrl, addMediaPainter)
        }

        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(
                text = post.title,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    lineHeight = 15.sp,
                    color = Color.Black
                )
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = post.content,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    lineHeight = 10.sp,
                    color = Color.Black
                ),
                maxLines = 3
            )
            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        color = Color(0xFF9E9E9E)
                    )
                )

                Text(
                    modifier = Modifier.clickable { onPostClick(post) },
                    text = "Читать далее...",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        color = Color(0xFF97A1B2)
                    )
                )
            }

            Spacer(Modifier.height(17.dp))

            PostActionsRow(
                likes = post.likesCount,
                comments = post.commentsCount,
                heartPainter = heartPainter,
                commentsPainter = commentsPainter,
                onLikeClick = { onLikeClick(post.id ?: 0) },
                onCommentsClick = { onCommentsClick(post.id ?: 0) },
                isLiked = isLiked,

            )
        }
    }
}

@Composable
fun PostAuthorRow(authorName: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(Modifier.width(8.dp))

        Column {
            Text(authorName, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun PostMedia(url: String, addMediaPainter: Painter) {
    val context = LocalContext.current

    //перенести в veiw model
    val cleanUrl = if (url.startsWith("data:image")) {
        val startIndex = url.indexOf("base64,") + 6
        if (startIndex < url.length) url.substring(startIndex) else url
    } else {
        url
    }

    Log.d("PostMedia", "Original: ${url.take(50)}... -> Clean: ${cleanUrl.take(50)}...")

    //конвертация в байты
    val imageData = try {
        android.util.Base64.decode(cleanUrl, android.util.Base64.DEFAULT)
    } catch (e: IllegalArgumentException) {
        Log.e("PostMedia", "Неверный base64: ${e.message}")
        null
    }

    AsyncImage(
        model = when {
            imageData != null -> imageData
            cleanUrl.startsWith("http") -> ImageRequest.Builder(context)
                .data(cleanUrl)
                .setHeader("User-Agent", "Mozilla/5.0 (compatible; THealthApp/1.0)")
                .crossfade(true)
                .build()
            else -> R.drawable.ic_add_media
        },
        placeholder = addMediaPainter,
        error = addMediaPainter,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp)),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun PostActionsRow(
    likes: Int,
    comments: Int,
    heartPainter: Painter,
    commentsPainter: Painter,
    onLikeClick: () -> Unit,
    onCommentsClick: () -> Unit,
    isLiked: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable { onLikeClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionItem(heartPainter, likes.toString(), isLiked = isLiked)
        }
        Spacer(Modifier.width(12.dp))
        Row(
            modifier = Modifier.clickable { onCommentsClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            ActionItem(commentsPainter, comments.toString())
        }
    }
}

@Composable
fun ActionItem(painter: Painter, text: String, isLiked: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End, modifier = Modifier.width(50.dp)) {
        if (text.isNotEmpty()) {

            Text(text,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    color = Color.Black
                )
            )
        }
        Spacer(Modifier.width(4.dp))
        Icon(
            modifier = Modifier.size(17.dp),
            painter = painter,
            contentDescription = null,
            tint = if (isLiked) Color.Red else Color.Gray
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailDialog(
    post: PostData,
    onClose: () -> Unit,
    onCommentsClick: () -> Unit,
    userId: Long? = null,
    viewModel: PostsViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(20.dp)),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = post.title,
                        style = TextStyle(
                            fontFamily = RobotoMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            lineHeight = 18.sp,
                            color = Color.Black
                        ),
                        maxLines = 2
                    )
                    IconButton(onClick = onClose) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "Закрыть",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Фото
                    if (post.mediaUrl != null) {
                        PostMediaFull(post.mediaUrl)
                    }

                    // Контент
                    Text(
                        text = post.content,
                        style = TextStyle(
                            fontFamily = RobotoMonoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Статистика
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Лайков: ${post.likesCount}",
                            style = TextStyle(
                                fontFamily = RobotoMonoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 14.sp
                            )
                        )
                        Text(
                            "Комментариев: ${post.commentsCount}",
                            style = TextStyle(
                                fontFamily = RobotoMonoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }

}


@Composable
fun PostMediaFull(url: String) {
    val context = LocalContext.current
    val cleanUrl = if (url.startsWith("data:image")) {
        val startIndex = url.indexOf("base64,") + 6
        if (startIndex < url.length) url.substring(startIndex) else url
    } else url

    val imageData = try {
        android.util.Base64.decode(cleanUrl, android.util.Base64.DEFAULT)
    } catch (e: Exception) {
        null
    }

    AsyncImage(
        model = when {
            imageData != null -> imageData
            cleanUrl.startsWith("http") -> ImageRequest.Builder(context)
                .data(cleanUrl)
                .crossfade(true)
                .build()
            else -> R.drawable.ic_add_media
        },
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(20.dp))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsDialog(
    postId: Long,
    onBack: () -> Unit,
    userId: Long? = null,
    viewModel: PostsViewModel = hiltViewModel()
) {
    val comments by viewModel.comments.collectAsState()
    val isLoading by viewModel.commentsLoading.collectAsState()

    LaunchedEffect(postId) {
        viewModel.loadComments(postId)
    }

    var newCommentText by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onBack,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(20.dp)),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Комментарии",
                        style = TextStyle(
                            fontFamily = RobotoMonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            lineHeight = 18.sp,
                            color = Color.Black
                        )
                    )
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = "Назад",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    if (isLoading && comments.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.9f),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color(0xFFFDD500))
                            }
                        }
                    } else if (comments.isEmpty() && !isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.9f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Комментариев пока нет",
                                        style = TextStyle(
                                            fontFamily = RobotoFontFamily,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF97A1B2)
                                        )
                                    )
                                }
                            }
                        }
                    }

                    items(
                        items = comments,
                        key = { comment -> comment.id ?: 0L }
                    ) { comment ->
                        CommentItem(comment = comment)
                    }
                }

                CommentInputBlock(
                    value = newCommentText,
                    onValueChange = { newCommentText = it },
                    onSendClick = {
                        if (newCommentText.isNotBlank() && userId != null) {
                            viewModel.createComment(postId = postId, authorId = userId, text = newCommentText)
                            newCommentText = ""
                        }
                    }
                )

            }
        }
    }
}

@Composable
fun CommentInputBlock(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                color = Color.Black
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "Введите комментарий",
                            style = TextStyle(
                                fontFamily = RobotoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                lineHeight = 14.sp,
                                color = Color(0xFF8C8E92)
                            )
                        )
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        innerTextField()
                    }
                }
            }
        )

        IconButton(
            onClick = onSendClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = "Отправить",
                tint = if (value.isNotBlank()) Color(0xFFFDD500) else Color(0xFF97A1B2),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun CommentItem(comment: CommentData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Ник автора
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = comment.authorName,
                        style = TextStyle(
                            fontFamily = RobotoFontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                color = Color(0xFFE0E0E0),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Текст комментария (растягивается по высоте)
            Text(
                text = comment.text,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = Color(0xFF333333)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
