package com.tbank.t_health.ui.health.addFood

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.t_health.components.SelectableTypeItem
import com.tbank.t_health.components.SelectableTypePickerDialog
import com.tbank.t_health.data.model.MealType
import com.tbank.t_health.screens.health.CustomDatePickerDialog
import com.tbank.t_health.ui.theme.RobotoFontFamily
import com.tbank.t_health.ui.theme.RobotoMonoFontFamily
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class FoodFieldData(
    val type: String,
    val value: String,
    val onValueChange: (String) -> Unit,
    val label: String? = null,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val shape: Shape = RoundedCornerShape(20.dp),
    val height: Dp = 39.dp
)


@Composable
fun FoodFields(
    fields: List<FoodFieldData>,
    selectedMealType: MealType?,
    onMealTypeSelected: (MealType) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showMealTypePicker by remember { mutableStateOf(false) }

    val dateField = fields.find { it.type == "Дата:" }
    val currentDateValue = dateField?.value ?: ""

    Column {
        fields.forEach { field ->

            Text(
                text = field.type,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            when (field.type) {

                "Тип приёма пищи:" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(39.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(horizontal = 10.dp)
                            .clickable { showMealTypePicker = true },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = selectedMealType?.displayName ?: field.label.orEmpty(),
                            color = if (selectedMealType == null) Color(0xFF8C8E92) else Color.Black
                        )

                        if (showMealTypePicker) {
                            SelectableTypePickerDialog(
                                titleText = "Выберите тип приёма пищи",
                                items = MealType.entries.toTypedArray(),
                                initialSelection = selectedMealType,
                                onDismiss = { showMealTypePicker = false },
                                onConfirm = { type ->
                                    onMealTypeSelected(type)
                                    showMealTypePicker = false
                                }
                            )
                        }
                    }
                }

                "Дата:" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(field.height)
                            .clip(field.shape)
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .clickable { showDatePicker = true },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = if (field.value.isEmpty()) field.label.orEmpty() else field.value,
                            style = TextStyle(
                                fontFamily = RobotoMonoFontFamily,
                                fontSize = 12.sp,
                                color = if (field.value.isEmpty())
                                    Color(0xFF8C8E92)
                                else
                                    Color.Black
                            )
                        )
                    }

                    if (showDatePicker) {
                        CustomDatePickerDialog(
                            currentDate = currentDateValue.takeIf { it.isNotEmpty() }?.let {
                                runCatching {
                                    LocalDate.parse(it, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                }.getOrNull()
                            },
                            onDismiss = { showDatePicker = false },
                            onDateSelected = { date ->
                                field.onValueChange(
                                    date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                )
                                showDatePicker = false
                            }
                        )
                    }
                }

                else -> {
                    BasicTextField(
                        value = field.value,
                        onValueChange = field.onValueChange,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = field.keyboardType),
                        textStyle = TextStyle(
                            fontFamily = RobotoMonoFontFamily,
                            fontSize = 12.sp,
                            color = Color.Black
                        ),
                        decorationBox = { inner ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(field.height)
                                    .clip(field.shape)
                                    .background(Color.White)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (field.value.isEmpty()) {
                                    Text(
                                        text = field.label.orEmpty(),
                                        style = TextStyle(
                                            fontFamily = RobotoMonoFontFamily,
                                            fontSize = 12.sp,
                                            color = Color(0xFF8C8E92)
                                        )
                                    )
                                }
                                inner()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
