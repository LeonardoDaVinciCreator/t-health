package com.tbank.t_health.ui.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.t_health.data.model.NotificationType
import com.tbank.t_health.domain.model.NotificationUiModel
import com.tbank.t_health.R
import com.tbank.t_health.ui.theme.RobotoFontFamily
import com.tbank.t_health.ui.theme.RobotoMonoFontFamily


@Composable
fun NotificationItem(
    item: NotificationUiModel
) {
    val iconBg =
        if (item.isRead) Color(0xFFE0E0E0) else Color(0xFFFFD600)
    val textColor =
        if (item.isRead) Color.Gray else Color.Black

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .height(68.dp+16.dp)
            .background(Color.White, shape = RoundedCornerShape(20.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(modifier = Modifier.padding(horizontal = 12.dp)){
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBg, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(
                        when (item.iconType) {
                            NotificationType.STEPS -> R.drawable.ic_steps
                            NotificationType.ACTIVE_MINUTES -> R.drawable.ic_activity
                            NotificationType.CALORIES -> R.drawable.ic_calories
                        }
                    ),
                    modifier = Modifier.padding(4.dp),
                    contentDescription = null
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(item.title,
                    color = textColor,
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        lineHeight = 15.sp,
                        textAlign = TextAlign.Left,
                        color = Color(0xFF97A1B2)
                    ),
                )
                Text(item.description,
                    color = textColor,
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        lineHeight = 12.sp,
                        textAlign = TextAlign.Left,
                        color = Color(0xFF97A1B2)
                    ),
                )
            }

            item.timeText?.let {
                Text(it,
                    color = Color.Gray,
                    style = TextStyle(
                        fontFamily = RobotoMonoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        lineHeight = 10.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF97A1B2)
                    ),
                )
            }
        }
    }
}
