package com.tbank.t_health.ui.posts.addPosts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tbank.t_health.R
import com.tbank.t_health.ui.theme.RobotoFontFamily
import com.tbank.t_health.ui.theme.RobotoMonoFontFamily


@Composable
fun AddPostTopBar(
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Создание поста",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                lineHeight = 17.sp
            )
        )
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
    var currentText by remember { mutableStateOf(text) }

    Dialog(
        onDismissRequest = onDismiss,
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
                        .padding(16.dp, 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Текст поста:",
                        style = TextStyle(
                            fontFamily = RobotoFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    )
                    IconButton(onClick = onDismiss) {
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

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        BasicTextField(
                            value = currentText,
                            onValueChange = { currentText = it },
                            maxLines = Int.MAX_VALUE,
                            textStyle = TextStyle(
                                fontFamily = RobotoMonoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp,
                                color = Color.Black,
                                lineHeight = 18.sp
                            ),
                            decorationBox = { innerTextField ->
                                Column {
                                    if (currentText.isEmpty()) {
                                        Text(
                                            text = "Расскажите подробнее...",
                                            style = TextStyle(
                                                fontFamily = RobotoMonoFontFamily,
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 14.sp,
                                                color = Color(0xFF8C8E92)
                                            ),
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                    innerTextField()
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(end = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.padding(end = 8.dp),
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

                        Button(
                            onClick = { onConfirm(currentText) },
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
                    }
                }
            }
        }
    }
}






@Composable
fun MediaAttachBlock(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    mediaSelected: Boolean = false
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
            Image(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp)),
                painter = painterResource(id = R.drawable.ic_add_media),
                contentDescription = null,
                alpha = if (mediaSelected) 0.7f else 1f
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (mediaSelected) "Фото выбрано" else "Добавьте фото",
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
