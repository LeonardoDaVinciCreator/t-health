package com.tbank.t_health.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tbank.t_health.R

@Composable
fun Header(
    hasUnreadNotifications: Boolean,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Левая часть — логотип
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo), // логотип "T" в щите
                contentDescription = "T-Health logo",
                modifier = Modifier
                    .height(34.dp)
                    .width(71.dp)
            )
        }

        // Правая часть — иконки поиска, меню, уведомлений
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search",
                modifier = Modifier
                    .size(48.dp)
                    .padding(horizontal = 14.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = "Menu",
                modifier = Modifier
                    .size(48.dp)
                    .padding(horizontal = 16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = "Notifications",
                modifier = Modifier
                    .size(48.dp)
                    .padding(horizontal = 14.dp)
                    .clickable { onNotificationClick() },
                colorFilter = if (hasUnreadNotifications) {
                    androidx.compose.ui.graphics.ColorFilter.tint(Color.Red) // или любой акцентный цвет
                } else {
                    null // оригинальный цвет иконки
                }
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HeaderPostPreview() {
//    Header()
//}