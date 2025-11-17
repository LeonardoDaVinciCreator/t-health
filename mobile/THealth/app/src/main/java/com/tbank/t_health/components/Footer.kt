// components/Footer.kt
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
import com.tbank.t_health.constants.NavigationTabs
import com.tbank.t_health.constants.NavigationDestinations

@Composable
fun Footer(
    navController: NavController,
    currentDestination: String,
    onItemSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationTabs.AllTabs.forEach { item ->
            FooterItem(
                item = item,
                isSelected = currentDestination == item.id,
                onClick = {
                    onItemSelected(item.id)
                    navController.navigate(item.id) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

data class FooterItemData(
    val id: String,
    val iconName: String,
    val label: String,
    val iconDefaultWidth: Int,
    val iconDefaultHeight: Int
)

@Composable
fun FooterItem(
    item: FooterItemData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val iconResId = context.resources.getIdentifier(
        if (isSelected) "${item.iconName}2" else item.iconName,
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
                contentDescription = item.label,
                modifier = Modifier.size(
                    width = item.iconDefaultWidth.dp,
                    height = item.iconDefaultHeight.dp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.label,
                style = FooterTypography.headlineLarge,
                color = if (isSelected) Color.Black else Color.Gray
            )
        }
    }
}