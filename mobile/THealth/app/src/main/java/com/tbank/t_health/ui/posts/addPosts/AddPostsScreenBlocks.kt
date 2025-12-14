package com.tbank.t_health.ui.posts.addPosts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.t_health.R
import com.tbank.t_health.ui.theme.RobotoFontFamily


@Composable
fun AddPostTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.size(48.dp))

        Text(
            text = "Создание поста",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                lineHeight = 17.sp
            )
        )

        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.Black
            )
        }
    }
}

enum class PostType(val title: String) {
    RECIPE("Рецепт"),
    WORKOUT("Тренировка")
}

@Composable
fun PostTypeBlock(
    selectedType: PostType?,
    onTypeSelected: (PostType) -> Unit
) {
    Column {
        Text(
            text = "Выберите тип:",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 14.sp
            ),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            PostTypeItem(
                text = PostType.RECIPE.title,
                selected = selectedType == PostType.RECIPE,
                onClick = { onTypeSelected(PostType.RECIPE) }
            )
            PostTypeItem(
                text = PostType.WORKOUT.title,
                selected = selectedType == PostType.WORKOUT,
                onClick = { onTypeSelected(PostType.WORKOUT) }
            )
        }
    }
}

@Composable
fun PostTypeItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(39.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color(0xFFFDD500) else Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                lineHeight = 15.sp
            ),
            color = Color.Black
        )
    }
}

@Composable
fun TitleBlock(
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Введите заголовок:",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 14.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 12.sp
            ),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(39.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = "ПП твороженная запеканка",
                            style = TextStyle(
                                fontFamily = RobotoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                lineHeight = 12.sp
                            ),
                            color = Color(0xFF8C8E92)
                        )
                    }
                    inner()
                }
            }
        )
    }
}

@Composable
fun TextContentBlock(
    value: String,
    onClick: () -> Unit
) {
    Column {
        Text(
            text = "Текст поста:",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 12.sp
            ),
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(12.dp)
                .clickable { onClick() }
        ) {
            Text(
                text = if (value.isEmpty()) "Расскажите подробнее..." else value,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 12.sp
                ),
                color = if (value.isEmpty()) Color(0xFF8C8E92) else Color.Black,
                maxLines = 3
            )
        }
    }
}

@Composable
fun FullScreenTextEditor(
    text: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var value by remember { mutableStateOf(text) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(value) }) {
                Text("Готово")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        text = {
            TextField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Введите текст поста") }
            )
        }
    )
}

@Composable
fun MediaAttachBlock() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(39.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable { /* open picker */ },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Добавить фото или видео",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 14.sp
            ),
            color = Color.Black
        )
    }
}