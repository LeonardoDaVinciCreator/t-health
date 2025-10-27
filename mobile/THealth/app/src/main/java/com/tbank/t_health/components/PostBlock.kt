package com.tbank.t_health.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.t_health.R
import com.tbank.t_health.ui.theme.InterFontFamily
import com.tbank.t_health.ui.theme.PostBlockTypography

@Composable
fun PostBlock(
    modifier: Modifier = Modifier,
    userName: String = "Sergei_PM",
    date: String = "20 Октября в 18:35",
    imageRes: Int = R.drawable.ic_notification,
    title: String = "Заголовок",
    text: String = "Основной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текстОсновной текст",
    likes: Int = 123,
    comments: Int = 211,
    onUserClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            //.size(342.dp)
            .fillMaxWidth()
            .height(342.dp)
            .padding(horizontal = 12.dp, vertical = 4.dp)
            
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFCDCDCD))
            .padding(12.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Аватар пользователя
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = "Аватар",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50))
                    .clickable { onUserClick() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = userName,
                        style = PostBlockTypography.headlineMedium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_achievement),
                        contentDescription = "Достижение",
                        modifier = Modifier.size(15.dp)
                    )
                }

                Row(modifier.height(18.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = date,
                        style = PostBlockTypography.bodySmall
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_dumbbell),
                        contentDescription = "Тип поста",
                        modifier = Modifier.size(36.dp, 18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.picture),
            contentDescription = "Фото поста",
            modifier = Modifier
                //.size(302.dp, 186.dp)
                .height(186.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = title,
            style = PostBlockTypography.headlineLarge
        )

        if (expanded) {
            Text(
                text = text,
                style = PostBlockTypography.bodyLarge,
                color = Color.Black,
            )
        } else {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    //.width(302.dp)
            ) {
                Text(
                    text = text,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    style = PostBlockTypography.bodyMedium,
                )
                Text(
                    text = " еще",
                    style = PostBlockTypography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-10).dp, y = (-3.5).dp)
                        .clickable {
                            expanded = true
                            onMoreClick()
                        }
                        .background(color = Color(205, 205, 205, 255),
                            shape = RoundedCornerShape(2.dp))
                )
            }

            Divider(
                color = Color(131, 131, 131, 255),
                thickness = 1.dp,
                modifier = Modifier//.size(302.dp, 0.5.dp)
                    .height(0.5.dp)
                    .fillMaxWidth()
            )

        }

        Spacer(modifier = Modifier.height(10.dp))


        Row(
            modifier = Modifier//.size(305.dp, 30.dp
                .height(30.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Image(
                    painter = painterResource(id = R.drawable.ic_like),
                    contentDescription = "Лайк",
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF838383))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "$likes",
                    style = PostBlockTypography.labelMedium,
                    modifier = Modifier.align(Alignment.Bottom)
                    )

                Spacer(modifier = Modifier.width(14.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_chat),
                    contentDescription = "Комментарий",
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF838383))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "$comments",
                    style = PostBlockTypography.labelMedium,
                    modifier = Modifier.align(Alignment.Bottom)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Image(
                    painter = painterResource(id = R.drawable.ic_message),
                    contentDescription = "Отправить",
                    modifier = Modifier.size(16.dp),
                    colorFilter = ColorFilter.tint(Color(0xFF838383))
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_pin),
                contentDescription = "Избранное",
                modifier = Modifier.size(16.dp),
                colorFilter = ColorFilter.tint(Color(0xFF838383))
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PostItemPreview() {
    PostBlock()
}
