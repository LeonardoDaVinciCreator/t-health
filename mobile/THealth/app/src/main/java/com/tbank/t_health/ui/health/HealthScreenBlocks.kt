package com.tbank.t_health.ui.health

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tbank.composefoodtracker.services.ExerciseService
import com.tbank.t_health.R
import com.tbank.t_health.constants.NavigationDestinations
import com.tbank.t_health.data.HealthDataMonth
import com.tbank.t_health.data.local.UserPrefs
import com.tbank.t_health.data.model.ActivityFullData
import com.tbank.t_health.data.repository.ActivityRepository
import com.tbank.t_health.data.toWeeklyGroups
import com.tbank.t_health.ui.theme.RobotoFontFamily
import com.tbank.t_health.ui.theme.StatsTypography
import java.time.LocalDate
import kotlin.collections.chunked
import kotlin.collections.forEach
import kotlin.collections.forEachIndexed
import kotlin.collections.maxOfOrNull

@Composable
fun ProfileHeaderBlock(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFFF), RoundedCornerShape(20.dp))
            .height(56.dp)
            .padding(horizontal = 12.dp, vertical = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_user),
            contentDescription = "Аватар",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = userName,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    lineHeight = 15.sp
                )
            )

            Text(
                text = "HR-отдел",
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 12.sp
                ),
                color = Color(0xFF7F7F7F)
            )
        }
    }
}

@Composable
fun ActivityStatsBlock(
    steps: Int,
    stepsGoal: Float,
    activeMinutes: Long,
    activeMinutesFormatted: String,
    activeMinutesGoal: Float,
    calories: Double,
    caloriesGoal: Float,
    onStepsGoalClick: () -> Unit,
    onActiveMinutesGoalClick: () -> Unit,
    onCaloriesGoalClick: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val circleSize = screenWidth / 3 - 24.dp-25.dp
    val circleSizeClamped = circleSize.coerceIn(89.dp, 128.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFFF), RoundedCornerShape(20.dp))
            .padding(12.dp, 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Активность сегодня:",
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 14.sp
                )
            )

            Image(
                painter = painterResource(id = R.drawable.ic_history),
                contentDescription = "История",
                modifier = Modifier
                    .size(22.dp)
                    .clickable { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCircleNew(
                valueTop = "$steps",
                valueBottom = "шагов пройдено",
                icon = R.drawable.ic_steps,
                progress = (steps / stepsGoal).coerceIn(0f, 1f),
                Color(0xFFFDD502),
                onClick = onStepsGoalClick
            )

            StatCircleNew(
                valueTop = activeMinutesFormatted,
                valueBottom = "часов активности",
                icon = R.drawable.ic_activity,
                progress = (activeMinutes.toFloat() / activeMinutesGoal).coerceIn(0f, 1f),
                Color(0xFFAAAFBA),
                onClick = onActiveMinutesGoalClick
            )

            StatCircleNew(
                valueTop = "${calories.toInt()}",
                valueBottom = "калорий сожжено",
                icon = R.drawable.ic_calories,
                progress = (calories / caloriesGoal).toFloat().coerceIn(0f, 1f),
                Color(0xFFD58C2F),
                onClick = onCaloriesGoalClick,
            )
        }
    }
}

@Composable
fun StatCircleNew(
    valueTop: String,
    valueBottom: String,
    icon: Int,
    progress: Float,
    color: Color = Color(0xFFFFC107),
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val baseWidth = 375.dp

    val scale = screenWidth / baseWidth

    val circleSize = (112.dp- 8.dp) * scale
    val topTextSize = 18.sp * scale
    val bottomTextSize = 10.sp * scale

    val strokeWidth = 16.dp * scale
    val strokeWidthPx = with(LocalDensity.current) { strokeWidth.toPx() }

    Box(
        modifier = Modifier
            .size(circleSize)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val sweepAngle = progress.coerceIn(0f, 1f) * 360f

            val gradientColors = listOf(
                color,
                color.copy(alpha = 0.38f),
                color.copy(alpha = 0.23f),
                color.copy(alpha = 0.0f)
            )

            val gradientStops = listOf(
                0.0f,
                (progress * 0.7f).coerceIn(0f, 1f),
                (progress * 0.9f).coerceIn(0f, 1f),
                progress.coerceIn(0f, 1f)
            ).toTypedArray()

            val gradient = Brush.sweepGradient(
                colorStops = gradientStops.zip(gradientColors).toTypedArray(),
                center = Offset(size.width / 2, size.height / 2)
            )

            rotate(-90f) {
                drawArc(
                    brush = gradient,
                    startAngle = 0f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
            }
        }

        // Внутри
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(strokeWidth)
        ) {
            val imageScale = 0.9f
            Image(
                painter = painterResource(id = icon),
                contentDescription = valueBottom,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(imageScale),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color(0xFFF3F3F7))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 2.dp * scale),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = valueTop,
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = topTextSize,
                        lineHeight = topTextSize
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(1.dp * scale))
                Text(
                    text = valueBottom,
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = bottomTextSize,
                        lineHeight = bottomTextSize,
                        color = Color(0xFF7F7F7F)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun StepDifferenceMessageBlock(
    todaySteps: Int,
    yesterdaySteps: Int,
    onClose: () -> Unit
) {
    val difference = todaySteps - yesterdaySteps
    val isPositive = difference >= 0

    val message = if (isPositive) {
        "Сегодня вы сделали на $difference\u00A0шагов больше, чем вчера!"
    } else {
        "Сегодня вы сделали на ${-difference}\u00A0шагов меньше, чем вчера!"
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = Color(0xFFFFD700), shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 0.dp, vertical = 0.dp)
    ) {
        Text(
            text = message,
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            ),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(44.dp, 0.dp)
        )
        Box(
            modifier = Modifier
                .size(27.dp)
                .align(Alignment.TopEnd)
                .clickable(onClick = onClose),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Аватар",
                modifier = Modifier
                    .size(27.dp)
                    .clip(CircleShape)
            )
        }
    }
}

enum class ChartType {
    STEPS, ACTIVEMINUTES, CALORIES
}

@Composable
fun StepsChart2(
    stepsGoal: Float,
    activeMinutesGoal: Float,
    caloriesGoal: Float,
    monthlyData: List<ActivityFullData>,
) {
    var currentWeek by remember { mutableStateOf(0) }
    var currentChartType by remember { mutableStateOf(ChartType.STEPS) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

    // разбика данных 28 дней на недели
    val weeklyData = remember(monthlyData) {
        if (monthlyData.size == 28) {
            monthlyData.chunked(7)
        } else {
            // проверка в getUserActivitiesFor28Days, но не дает все равно без проверки
            List(4) { weekIndex ->
                List(7) { dayIndex ->
                    val date = LocalDate.now()
                        .with(java.time.DayOfWeek.SUNDAY)
                        .minusDays(27 - (weekIndex * 7 + dayIndex).toLong())
                    ActivityFullData(
                        steps = 0,
                        activeMinutes = 0,
                        calories = 0.0,
                        date = date
                    )
                }
            }
        }
    }

    //текущая неделя как последняя
    LaunchedEffect(weeklyData) {
        if (weeklyData.isNotEmpty()) {
            currentWeek = weeklyData.size - 1
        }
    }

    // Отформатированные даты для диаграммы
    val weekDates = remember(weeklyData) {
        weeklyData.map { week ->
            week.map {
                "${it.date.dayOfMonth.toString().padStart(2, '0')}.${it.date.monthValue.toString().padStart(2, '0')}"
            }
        }
    }

    val allWeeks = weeklyData
    val threshold = 100f

    Box(
        modifier = Modifier
            .height(255.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        when {
                            dragOffset > threshold -> currentChartType = when (currentChartType) {
                                ChartType.STEPS -> ChartType.CALORIES
                                ChartType.CALORIES -> ChartType.ACTIVEMINUTES
                                ChartType.ACTIVEMINUTES -> ChartType.STEPS
                            }
                            dragOffset < -threshold -> currentChartType = when (currentChartType) {
                                ChartType.STEPS -> ChartType.ACTIVEMINUTES
                                ChartType.ACTIVEMINUTES -> ChartType.CALORIES
                                ChartType.CALORIES -> ChartType.STEPS
                            }
                        }
                        dragOffset = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset += dragAmount.x
                    }
                )
            }
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            val title = when (currentChartType) {
                ChartType.STEPS -> "Шаги"
                ChartType.ACTIVEMINUTES -> "Активность"
                ChartType.CALORIES -> "Калории"
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
                        lineHeight = 18.sp
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(start = 0.dp, top = 0.dp)
                )
            }

            val goal = when (currentChartType) {
                ChartType.STEPS -> stepsGoal
                ChartType.CALORIES -> caloriesGoal
                ChartType.ACTIVEMINUTES -> activeMinutesGoal
            }

            val maxValue = goal * 1.25f
            val lineCount = 6

            Spacer(modifier = Modifier.height(10.dp))


            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 10.dp)
            ) {
                val chartHeight = constraints.maxHeight.toFloat()
                val density = LocalDensity.current
                val labelColumnWidth = with(density) { 44.dp.toPx() }

                val goalLineValue = goal.coerceAtMost(maxValue)
                val stepY = chartHeight / (lineCount - 1)
                val goalY = chartHeight - (goalLineValue / maxValue) * chartHeight

                // линии
                Canvas(modifier = Modifier.matchParentSize()) {
                    val lineColor = Color(0xFFAAAFBA)
                    val goalColor = Color(0xFF757981)
                    for (i in 0 until lineCount) {
                        val y = size.height - i * stepY
                        val isGoalLine = kotlin.math.abs(y - goalY) < stepY / 3f
                        drawLine(
                            color = if (isGoalLine) goalColor else lineColor,
                            start = Offset(labelColumnWidth + 8f, y),
                            end = Offset(size.width, y),
                            strokeWidth = if (isGoalLine) 3f else 1.5f
                        )
                    }
                }

                val yOffsetAdjustment = with(density) { (-6).dp.toPx() }

                // Левая колонка с числами
                Box(
                    modifier = Modifier
                        .width(with(density) { labelColumnWidth.toDp() })
                        .fillMaxHeight()
                ) {
                    for (i in 0 until lineCount) {
                        val rawValue = (maxValue / (lineCount - 1)) * i
                        val y = chartHeight - (rawValue / maxValue) * chartHeight + yOffsetAdjustment
                        Text(
                            text = when (currentChartType) {
                                ChartType.STEPS -> "%,d".format(rawValue.toInt())
                                ChartType.CALORIES -> rawValue.toInt().toString()
                                ChartType.ACTIVEMINUTES -> if (rawValue.toInt() == 0) "0 ч" else "%.1f".format(rawValue / 60f)
                            },
                            style = TextStyle(
                                fontFamily = RobotoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                lineHeight = 12.sp,
                                textAlign = TextAlign.Right
                            ),
                            color = if (rawValue == goalLineValue) Color(0xFF757981) else Color(
                                0xFFAAAFBA
                            ),
                            modifier = Modifier
                                .offset(y = with(density) { y.toDp() })
                                .width(50.dp)
                        )
                    }
                }

                // Столбики с датами
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(start = 40.dp + 12.dp, end = 35.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    allWeeks[currentWeek].forEachIndexed { index, data ->
                        val value = when (currentChartType) {
                            ChartType.STEPS -> data.steps.toFloat()
                            ChartType.ACTIVEMINUTES -> data.activeMinutes.toFloat()
                            ChartType.CALORIES -> data.calories.toFloat()
                        }

                        val clampedValue = value.coerceAtMost(maxValue)
                        val fraction = clampedValue / maxValue
                        val barHeight = with(density) { (chartHeight * fraction).toDp() }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.width(35.dp)
                                .offset(y = 16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(barHeight)
                                    .clip(RoundedCornerShape(20.dp, 20.dp))
                                    .background(
                                        when {
                                            value / goal >= 1f -> Color(0xFFFFD700)
                                            value / goal >= 0.5f -> Color(0xFFABB0BB)
                                            else -> Color(0xFFD78C2F)
                                        }
                                    )
                                    .align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = weekDates[currentWeek][index],
                                style = TextStyle(
                                    fontFamily = RobotoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 11.sp,
                                    lineHeight = 12.sp,
                                    textAlign = TextAlign.Center
                                ),
                                color = Color(0xFFAAAFBA),
                                modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally)
                                    .offset(y = 17.dp)
                            )
                        }
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(horizontal = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { if (currentWeek > 0) currentWeek-- },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = null
                    )
                }

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { if (currentWeek < allWeeks.size - 1) currentWeek++ },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = null,
                        modifier = Modifier.graphicsLayer {
                            scaleX = -1f
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun MenuSection(navController: NavController, calories:Double) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        MenuItem("Тренировка","-${calories.toInt()}", onClick = {
            navController.navigate(NavigationDestinations.WORKOUT)
        },
            onClickAdd = {
                navController.navigate(NavigationDestinations.ADD_WORKOUT)
            }
        )

        //добавить переменную для калорий из еды
        MenuItem("Питание", onClick = {
            Log.d("Exercise", "Питание нажато")
        },
            onClickAdd = {
                Log.d("Exercise", "Добавить калории за еду")
            }
        )
    }
}


//добавить калории из еды
@Composable
fun MenuItem(label: String, calories:String = "0", onClick: () -> Unit, onClickAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFFFFFFF)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable { onClick() }
                .padding(start = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 14.sp
                )
            )
        }

        Row(
            modifier = Modifier.width(100.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.width(44.dp),
                text = calories,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    color = Color(0xFF8C8E92)
                )
            )
            Divider(modifier = Modifier.width(1.dp).height(20.dp), color = Color(0xFF8C8E92))
            Box(modifier = Modifier.size(40.dp).clickable { onClickAdd() }){
                Image(painter = painterResource(id = R.drawable.ic_plus),
                    contentDescription = null,
                    modifier = Modifier
                        .size(11.dp)
                        .align(Alignment.Center),
                    colorFilter = ColorFilter.tint(Color(0xFF8C8E92))
                )

            }

        }

    }
}

@Composable
fun StatGoalDialog(
    label: String,
    currentGoal: Double,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit,
) {
    var textInput by remember { mutableStateOf(currentGoal.toString()) }

    val inputAsDouble = textInput.toDoubleOrNull()
    val isValid = inputAsDouble != null && inputAsDouble >= 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Установить $label") },
        text = {
            TextField(
                value = textInput,
                onValueChange = { input ->
                    textInput = input
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                label = { Text(label) },
                isError = textInput.isNotEmpty() && inputAsDouble == null
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    inputAsDouble?.let { onConfirm(it) }
                },
                enabled = isValid
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}