package com.tbank.t_health.ui.posts.addPosts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.t_health.R
import com.tbank.t_health.ui.theme.RobotoFontFamily
import com.tbank.t_health.ui.theme.RobotoMonoFontFamily


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
                onClick = { onTypeSelected(PostType.RECIPE) },
                modifier = Modifier.weight(1f)
            )
            PostTypeItem(
                text = PostType.WORKOUT.title,
                selected = selectedType == PostType.WORKOUT,
                onClick = { onTypeSelected(PostType.WORKOUT) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PostTypeItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val editorHeight = (screenHeight.value * 0.4f).dp // 40% от высоты экрана

    var currentText by remember { mutableStateOf(text) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFDFDFD),
        title = {
            Text(
                "Текст поста:",
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(editorHeight)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                BasicTextField(
                    value = currentText,
                    onValueChange = { currentText = it },
                    maxLines = Int.MAX_VALUE, // много строк
                    minLines = 1,
                    textStyle = TextStyle(
                        fontFamily = RobotoMonoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.Black,
                        lineHeight = 16.sp
                    ),
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (currentText.isEmpty()) {
                                Text(
                                    text = "Расскажите подробнее...",
                                    style = TextStyle(
                                        fontFamily = RobotoMonoFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp,
                                        color = Color(0xFF8C8E92)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(currentText)
                },
                enabled = currentText.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDD500)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Сохранить",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Отмена",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                )
            }
        }
    )
}





@Composable
fun MediaAttachBlock(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 13.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка слева
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.height(38.dp),
                    painter = painterResource(id = R.drawable.ic_add_media),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Добавьте фото или видео",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        color = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PNG, JPG, HEIC, MOV",
                        style = TextStyle(
                            fontFamily = RobotoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color(0xFF97A1B2)
                        )
                    )

                    Text(
                        text = "  |  ",
                        style = TextStyle(
                            fontFamily = RobotoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color(0xFF97A1B2)
                        )
                    )

                    Text(
                        text = "15 MB max.",
                        style = TextStyle(
                            fontFamily = RobotoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color(0xFF97A1B2)
                        )
                    )
                }
            }
        }
    }
}
