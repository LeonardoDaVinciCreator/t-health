package com.tbank.t_health.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tbank.t_health.R
import com.tbank.t_health.ui.theme.FooterTypography

@Composable
fun Footer(navController: NavController, selectedIndex: Int, onItemSelected: (Int) -> Unit) {

    val items = listOf(
        FooterItemData("ic_home", "Главная", 21, 19, 0),
        FooterItemData("ic_trophy", "Достижения", 25, 23, 1),
        FooterItemData("ic_posts", "Лента", 25, 25, 2),
        FooterItemData("ic_chat", "Чат", 25, 25, 3),
        FooterItemData("ic_profile", "Профиль", 30, 30, 4)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            FooterItem(
                baseIconName = item.iconName,
                label = item.label,
                iconWidth = item.iconDefaultWidth,
                iconHeight = item.iconDefaultHeight,
                isSelected = selectedIndex == item.index,
                onClick = {
                    onItemSelected(item.index)
                    when (item.index) {
                        0 -> navController.navigate("health")
                        1 -> navController.navigate("achievements")
                        2 -> navController.navigate("posts")
                        3 -> navController.navigate("chat")
                        4 -> navController.navigate("profile")
                        else -> navController.navigate("health")
                    }
                }
            )
        }
    }
}

data class FooterItemData(
    val iconName: String,
    val label: String,
    val iconDefaultWidth: Int,
    val iconDefaultHeight: Int,
    val index: Int
)

@Composable
fun FooterItem(
    baseIconName: String,
    label: String,
    iconWidth: Int,
    iconHeight: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val iconResId = context.resources.getIdentifier(
        if (isSelected) "${baseIconName}2" else baseIconName,
        "drawable",
        context.packageName
    )

    Box(
        modifier = Modifier
            .width(70.dp)
            .height(60.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = label,
                modifier = Modifier.size(iconWidth.dp, iconHeight.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = FooterTypography.headlineLarge,
                color = if (isSelected) Color.Black else Color.Gray
            )
        }
    }
}
