package com.tbank.t_health.ui.health.food

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tbank.t_health.data.model.MealType
import com.tbank.t_health.data.model.NutritionGetData
import com.tbank.t_health.screens.MultiSelectCalendar
import com.tbank.t_health.ui.theme.RobotoFontFamily
import com.tbank.t_health.ui.theme.RobotoMonoFontFamily
import java.time.LocalDate

@Composable
fun MealBlock(
    mealType: MealType,
    meals: List<NutritionGetData>
) {
    val totalCalories = meals.sumOf { it.mealCalories }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text(
                    "${mealType.displayName}",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp
                    )
                )

                Text(
                    "$totalCalories ккал",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            meals.forEach {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        it.mealName,
                        style = TextStyle(
                            fontFamily = RobotoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        "${it.mealCalories} ккал",
                        style = TextStyle(
                            fontFamily = RobotoMonoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color(0xFF97A1B2)
                        )
                    )
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}


fun NutritionGetData.parseDate(): LocalDate =
    LocalDate.parse(date.substringBefore("T"))

fun List<NutritionGetData>.filterByDateRange(
    range: Pair<LocalDate?, LocalDate?>?
): List<NutritionGetData> {
    if (range == null) return this
    val (start, end) = range
    return filter {
        val d = it.parseDate()
        (start == null || !d.isBefore(start)) &&
                (end == null || !d.isAfter(end))
    }
}

@Composable
fun WeeklyNutritionStats(
    nutritions: List<NutritionGetData>,
    selectedDateRange: Pair<LocalDate?, LocalDate?>?,
    onDateRangeSelected: (LocalDate?, LocalDate?) -> Unit
) {
    var isSelected by remember { mutableStateOf(false) }

    val filtered = nutritions.filterByDateRange(selectedDateRange)

    val protein = filtered.sumOf { it.parameters.protein }
    val fats = filtered.sumOf { it.parameters.fats }
    val carbs = filtered.sumOf { it.parameters.carbohydrate }
    val total = (protein + fats + carbs).takeIf { it > 0 } ?: 1.0

    val progressList = listOf(
        "Белки" to protein / total,
        "Жиры" to fats / total,
        "Углеводы" to carbs / total
    )

    val colorMap = mapOf(
        "Белки" to Color(0xFFFFD54F),
        "Жиры" to Color(0xFFFF8A65),
        "Углеводы" to Color(0xFF64B5F6)
    )

    val (rangeStart, rangeEnd) = selectedDateRange ?: run {
        val start = LocalDate.now().with(java.time.DayOfWeek.MONDAY)
        start to start.plusDays(6)
    }

    val totalCalories = filtered.sumOf { it.mealCalories }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {

        Text(
            "За выбранный период употреблено $totalCalories ккал",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 17.sp
            )
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //диаграма
            Box(modifier = Modifier.padding(start = 16.dp).size(112.dp)) {
                Canvas(modifier = Modifier.size(87.dp)) {
                    var startAngle = -90f
                    progressList.forEach { (label, progress) ->
                        val sweep = (progress * 360f).toFloat()
                        drawArc(
                            color = colorMap[label] ?: Color.Gray,
                            startAngle = startAngle,
                            sweepAngle = sweep,
                            useCenter = false,
                            style = Stroke(width = 50f, cap = StrokeCap.Butt)
                        )
                        startAngle += sweep
                    }
                }
            }

            // Легенда
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(112.dp)
                    .padding(16.dp)
            ) {

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start){
                    Spacer(Modifier.width(22.dp))

                    Text(
                        "БЖУ:",
                        style = TextStyle(
                            fontFamily = RobotoFontFamily,
                            fontSize = 14.sp
                        )
                    )
                }

                progressList.forEach { (label, progress) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(6.dp)
                                .background(colorMap[label]!!)
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(
                            "${(progress * 100).toInt()}% $label",
                            style = TextStyle(
                                fontFamily = RobotoFontFamily,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = if (isSelected) "Скрыть календарь"
            else "Выберите интервал для просмотра питания",
            modifier = Modifier.clickable { isSelected = !isSelected },
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF7F7F7F)
            )
        )

        if (isSelected) {
            MultiSelectCalendar(
                initiallySelectedStart = rangeStart,
                initiallySelectedEnd = rangeEnd,
                onRangeSelected = onDateRangeSelected
            )
        }
    }
}
