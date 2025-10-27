package com.tbank.t_health.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tbank.t_health.R

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tbank.t_health.ui.theme.FooterTypography
import com.tbank.t_health.ui.theme.InterFontFamily

//при нажатии на кнопку экрана картинка увеличивается, а текст исчезает, изначально посты

@Composable
fun Footer(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)

            .background(Color(0xFFAAAAAA)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FooterItem(R.drawable.ic_home, "Главная", 21, 19){ navController.navigate("posts") }
        FooterItem(R.drawable.ic_trophy, "Достижения", 25, 23){ navController.navigate("achievements") }
//
        FooterItem(R.drawable.ic_add, "Лента", 25, 25){ navController.navigate("health") }
        FooterItem(R.drawable.ic_chat, "Чат", 25, 25){ navController.navigate("chat") }
        FooterItem(R.drawable.ic_profile, "Профиль", 30, 30){ navController.navigate("profile") }
    }
}

@Composable
fun FooterItem(icon: Int, label: String, iconWidth: Int, iconHeight: Int, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .width(70.dp)
            .height(45.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = label,
                    modifier = Modifier.size(iconWidth.dp, iconHeight.dp)
                )
            }
            Text(
                text = label,
                style = FooterTypography.headlineLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 0.dp)
                    .height(15.dp)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun FooterPreview() {
//    Footer()
//}