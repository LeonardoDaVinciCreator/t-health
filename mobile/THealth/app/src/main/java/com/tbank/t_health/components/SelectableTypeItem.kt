package com.tbank.t_health.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.t_health.ui.theme.RobotoFontFamily

@Composable
fun <T : DisplayableType> SelectableTypeItem(
    type: T,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) Color(0xFFFDD500) else Color(0xFFF5F5F5)
            )
            .clickable(onClick = onSelect)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = type.displayName,
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Black
            )
        )
    }
}

@Composable
fun <T : DisplayableType> SelectableTypePickerDialog(
    titleText: String,
    items: Array<T>,
    initialSelection: T? = null,
    onDismiss: () -> Unit,
    onConfirm: (T) -> Unit
) {
    var selectedItem by remember { mutableStateOf(initialSelection) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFDFDFD),
        title = {
            Text(
                titleText,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEach { item ->
                    SelectableTypeItem(
                        type = item,
                        isSelected = selectedItem == item,
                        onSelect = { selectedItem = item }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { selectedItem?.let { onConfirm(it) } },
                enabled = selectedItem != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDD500)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Выбрать",
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
