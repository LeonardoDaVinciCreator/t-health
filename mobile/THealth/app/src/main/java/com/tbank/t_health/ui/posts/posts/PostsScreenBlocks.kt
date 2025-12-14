package com.tbank.t_health.ui.posts.posts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tbank.t_health.R
import com.tbank.t_health.data.model.posts.PostData
import com.tbank.t_health.ui.theme.RobotoFontFamily

@Composable
fun PostCard(post: PostData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PostAuthorRow(
            authorName = "nickname"
        )

        if (post.mediaUrl != null) {
            PostMedia(post.mediaUrl)
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
                    text = "15:45 15.02.2025",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        color = Color(0xFF9E9E9E)
                    )
                )

                Text(
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
                comments = post.commentsCount
            )
        }
    }
}

@Composable
fun PostAuthorRow(authorName: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_add_media),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.width(8.dp))

        Column {
            Text(authorName, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun PostMedia(url: String) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36")
            .crossfade(true)
            .listener(
                onStart = {
                    android.util.Log.d("PostMedia", "Loading started: $url")
                },
                onSuccess = { _, _ ->
                    android.util.Log.d("PostMedia", "Loading success")
                },
                onError = { _, result ->
                    android.util.Log.e(
                        "PostMedia",
                        "Loading error",
                        result.throwable
                    )
                }
            )
            .build(),
        placeholder = painterResource(R.drawable.ic_add_media),
        error = painterResource(R.drawable.ic_add_media),
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
    comments: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionItem(R.drawable.ic_like, likes.toString())
        ActionItem(R.drawable.ic_add_media, comments.toString())
        ActionItem(R.drawable.ic_add_media, "0")
        ActionItem(R.drawable.ic_add_media, "0")
    }
}

@Composable
fun ActionItem(icon: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (text.isNotEmpty()) {
            Spacer(Modifier.width(4.dp))
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
        Icon(
            modifier = Modifier.size(17.dp),
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Gray
        )

    }
}
