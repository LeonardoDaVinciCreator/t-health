package com.tbank.t_health.screens

import UserPrefs
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
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
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.navigation.NavController
import com.tbank.composefoodtracker.services.ExerciseService
import com.tbank.composefoodtracker.services.StepCounterService
import com.tbank.t_health.R
import com.tbank.t_health.data.ActiveStorage
import com.tbank.t_health.data.repository.ActivityRepository
import com.tbank.t_health.ui.theme.StatsTypography
import kotlinx.coroutines.launch
import com.tbank.t_health.data.HealthDataMonth
import com.tbank.t_health.data.toWeeklyGroups
import com.tbank.t_health.ui.theme.RobotoFontFamily
import androidx.compose.ui.graphics.graphicsLayer
import com.tbank.t_health.data.model.ActivityFullData
import com.tbank.t_health.data.model.ActivityGetData
import com.tbank.t_health.data.repository.WorkoutRepository
import kotlinx.coroutines.delay
import java.time.LocalDate


// Разрешения
val PERMISSIONS = setOf(
    // Чтение
    HealthPermission.getReadPermission(StepsRecord::class),
    HealthPermission.getReadPermission(ActiveCaloriesBurnedRecord::class),
    HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
    HealthPermission.getReadPermission(ExerciseSessionRecord::class),
    HealthPermission.getReadPermission(SpeedRecord::class),
    HealthPermission.getReadPermission(DistanceRecord::class),


            // Запись
    HealthPermission.getWritePermission(ActiveCaloriesBurnedRecord::class),
    HealthPermission.getWritePermission(TotalCaloriesBurnedRecord::class),
    HealthPermission.getWritePermission(ExerciseSessionRecord::class),
    HealthPermission.getWritePermission(DistanceRecord::class)



)

@Composable
fun HealthScreen(navController: NavController) {
    val context = LocalContext.current
    val stepService = StepCounterService(context)
    val activityRepo = ActivityRepository(context)
    val activeStorage = ActiveStorage(context)

    val coroutineScope = rememberCoroutineScope()
    val healthConnectClient = remember { HealthConnectClient.getOrCreate(context) }

    var stepsGoal by remember { mutableStateOf(10000f) }
    var activeMinutesGoal by remember { mutableStateOf(240f) } // 4 часа = 240 минут
    var caloriesGoal by remember { mutableStateOf(1200f) }

    var yesterdaySteps by remember { mutableStateOf(0) }
    var showMessage by remember { mutableStateOf(true) }

    var steps by remember { mutableStateOf(0) }
    var activeMinutes by remember { mutableStateOf(0) }
    var activeCalories by remember { mutableStateOf(0.0) }
    var calories by remember { mutableStateOf(0.0) }
    var loading by remember { mutableStateOf(true) }
    var permissionRequested by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        if (granted.containsAll(PERMISSIONS)) {
            coroutineScope.launch {
                steps = StepCounterService(context).getStepsForToday()
                //activeMinutes = StepCounterService(context).getMinutesForToday()
                calories = StepCounterService(context).getCaloriesForToday()
                loading = false
            }
        } else {
            println("Разрешения Health Connect не получены")
        }
    }

    // Проверка разрешений после Compose
    LaunchedEffect(Unit) {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        if (!granted.containsAll(PERMISSIONS) && !permissionRequested) {
            permissionRequested = true
            permissionLauncher.launch(PERMISSIONS)
        } else if (granted.containsAll(PERMISSIONS)) {
            // текущие данные шагов и активности
            steps = stepService.getStepsForToday()
            activeMinutes = stepService.getActiveMinutesForToday().toInt() + (activeStorage.getActiveSeconds() / 60)
            activeCalories = activeStorage.getCalories()
            calories = stepService.getCaloriesFromStepsAndActiveCalories() + activeCalories
            yesterdaySteps = stepService.getStepsForDate(LocalDate.now().minusDays(1))
            showMessage = steps > yesterdaySteps

            // Получаем пользователя
            val userPrefs = UserPrefs(context)
            val user = userPrefs.getUser()

            if (user != null && user.id != null) {
                coroutineScope.launch {
                    try {
                        // сбор и сохранение локально
                        activityRepo.collectAndSaveDailyData(stepService, activeStorage, user.id!!)

                        // отправка на сервер
                        activityRepo.syncToServer(user.id!!, clearAfterSync = true)
                        delay(300)

                        val serverActivities = activityRepo.getUserActivitiesFromServer(user.id!!)
                        Log.d("HealthScreen", "User from server: ${user.username}, phone=${user.phone}")
                        Log.d("HealthScreen", "Server activities count=${serverActivities.size}")
                        for (a in serverActivities) {
                            Log.d("HealthScreen", "Activity: type=${a.type}, value=${a.value}, calories=${a.calories}, data=${a.date}")
                        }
                    } catch (e: Exception) {
                        Log.e("HealthScreen", "Sync error: ${e.message}")
                    }
                }
            } else {
                Log.e("HealthScreen", "⚠️ User not found — skipping sync")
            }
        }
    }

    Log.d("StepCounter", "Активных минут сегодня: $activeMinutes")

    var showStepsDialog by remember { mutableStateOf(false) }
    var showCaloriesDialog by remember { mutableStateOf(false) }

    Scaffold(containerColor = Color(0xFFE0E2E3)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 0.dp, vertical = 10.dp)
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ProfileHeaderBlock()
            Spacer(modifier = Modifier.height(12.dp))
            ActivityStatsBlock(
                steps = steps,
                stepsGoal = stepsGoal,
                activeMinutes = activeMinutes,
                activeMinutesGoal = activeMinutesGoal,
                calories = calories,
                caloriesGoal = caloriesGoal,
                onStepsGoalClick = { showStepsDialog = true },
                onActiveMinutesGoalClick = { /* TODO */ },
                onCaloriesGoalClick = { showCaloriesDialog = true }
            )



            Spacer(modifier = Modifier.height(8.dp))
            if (showMessage) {
                StepDifferenceMessageBlock(
                    todaySteps = steps,
                    yesterdaySteps = yesterdaySteps,
                    onClose = { showMessage = false }
                )
            }



            if (showStepsDialog) {
                StatGoalDialog(
                    label = "Цель по шагам",
                    currentGoal = stepsGoal.toDouble(),
                    onDismiss = { showStepsDialog = false },
                    onConfirm = { newGoal ->
                        stepsGoal = newGoal.toFloat()
                        showStepsDialog = false
                    }
                )
            }

            if (showCaloriesDialog) {
                StatGoalDialog(
                    label = "Цель по калориям",
                    currentGoal = caloriesGoal.toDouble(),
                    onDismiss = { showCaloriesDialog = false },
                    onConfirm = { newGoal ->
                        caloriesGoal = newGoal.toFloat()
                        showCaloriesDialog = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
//            StepsChart()

            StepsChart2(stepsGoal, activeMinutesGoal, caloriesGoal)

            Spacer(modifier = Modifier.height(14.dp))
            //MenuSection()

            MenuSection(navController, activeCalories)




        }
    }
}

@Composable
fun ProfileHeaderBlock() {
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
                text = "Сергей Третьяков",
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
    activeMinutes: Int,
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
                onClick = onStepsGoalClick,
                //circleSize = circleSizeClamped
            )

            val hours = activeMinutes / 60
            val minutes = activeMinutes % 60
            val formattedTime = String.format("%d:%02d", hours, minutes)


            StatCircleNew(
                valueTop = formattedTime,
                valueBottom = "часов активности",
                icon = R.drawable.ic_activity,
                progress = (activeMinutes / activeMinutesGoal).coerceIn(0f, 1f),
                Color(0xFFAAAFBA),
                onClick = onActiveMinutesGoalClick,
                //circleSize = circleSizeClamped
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
fun StepsChart() {
    var currentWeek by remember { mutableStateOf(0) }
    var currentChartType by remember { mutableStateOf(ChartType.STEPS) }

    val allHealthData = HealthDataMonth()
    val weeklyData = allHealthData.toWeeklyGroups()
    var dragOffset by remember { mutableFloatStateOf(0f) }

    val (dataList, title, formatValue) = when (currentChartType) {
        ChartType.STEPS -> Triple(
            weeklyData.map { week -> week.map { it.steps } },
            "Шаги",
            { value: Number -> value.toString() }
        )
        ChartType.ACTIVEMINUTES -> Triple(
            weeklyData.map { week -> week.map { it.activeMinutes } },
            "Ходьба",
            { value: Number -> "${value} мин" }
        )
        ChartType.CALORIES -> Triple(
            weeklyData.map{ week -> week.map { it.calories }},
            "Калории",
            { value: Number -> "${value} ккал" }
        )
    }

    val weekDates = weeklyData.map { week ->
        week.map { it.date.dayOfMonth.toString().padStart(2, '0') + "." + it.date.monthValue.toString().padStart(2, '0') }
    }

    val allWeeks = dataList

    val threshold = 100f // порог свайпа

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFFF), RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (dragOffset > threshold) {
                            // Свайп вправо → назад по кругу
                            currentChartType = when (currentChartType) {
                                ChartType.STEPS -> ChartType.CALORIES
                                ChartType.CALORIES -> ChartType.ACTIVEMINUTES
                                ChartType.ACTIVEMINUTES -> ChartType.STEPS
                            }
                        } else if (dragOffset < -threshold) {
                            // Свайп влево → вперёд по кругу
                            currentChartType = when (currentChartType) {
                                ChartType.STEPS -> ChartType.CALORIES
                                ChartType.CALORIES -> ChartType.ACTIVEMINUTES
                                ChartType.ACTIVEMINUTES -> ChartType.STEPS
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
    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE5E5E5), RoundedCornerShape(16.dp))
    ) {

        val steps = allWeeks[currentWeek]
        val maxSteps = steps.maxOfOrNull { it.toFloat() } ?: 1f

        Text(
            text = title,
            style = StatsTypography.bodyLarge,
            modifier = Modifier.padding(start = 15.dp, top = 15.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    steps.forEachIndexed { _, value ->
                        val barHeight = (120 * (value.toFloat() / maxSteps)).dp

                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(barHeight),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(25.dp)
                                    .height(barHeight)
                                    .background(Color(0xFFAAAAAA))
                            )
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(barHeight)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFCDCDCD))
                                        .border(3.87.dp, Color(0xFFAAAAAA), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = formatValue(value),
                                        style = StatsTypography.bodySmall,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp),
                    color = Color(0xFFAAAAAA)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 2.dp, end = 2.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .clip(CircleShape)
                            .offset(x = 4.dp)
                            .clickable { if (currentWeek > 0) currentWeek-- },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("<-", style = StatsTypography.labelMedium)
                    }

                    weekDates[currentWeek].forEach { date ->
                        Text(
                            text = date,
                            style = StatsTypography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(40.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .clip(CircleShape)
                            .offset(x = (-4).dp)
                            .clickable { if (currentWeek < allWeeks.size - 1) currentWeek++ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("->", style = StatsTypography.labelMedium)
                    }
                }
            }
        }
    }
    }
}

@Composable
fun StepsChart2(
    stepsGoal: Float,
    activeMinutesGoal: Float,
    caloriesGoal: Float
) {
    var currentWeek by remember { mutableStateOf(0) }
    var currentChartType by remember { mutableStateOf(ChartType.STEPS) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

    val context = LocalContext.current
    val activityRepo = remember { ActivityRepository(context) }
    val userPrefs = remember { UserPrefs(context) }

    var monthlyServerData by remember { mutableStateOf<List<ActivityFullData>>(emptyList()) }

    LaunchedEffect(Unit) {
        val user = userPrefs.getUser()
        if (user?.id != null) {
            try {
                val monthActivities: List<ActivityFullData> = activityRepo.getUserActivitiesFor28Days(user.id, LocalDate.now())
                monthlyServerData = monthActivities
                Log.d("ACTIVITY_MONTH", "monthActivities=$monthActivities")

                monthActivities.forEach { a ->
                    Log.d(
                        "ACTIVITY_MONTH",
                        "steps=${a.steps}, activeMinutes=${a.activeMinutes}, calories=${a.calories}, date=${a.date}"
                    )
                }
            } catch (e: Exception) {
                Log.e("StepsChart2", "Error fetching monthly activities: ${e.message}")
            }
        }
    }

    // разбика данных 28 дней на недели
    val weeklyData = remember(monthlyServerData) {
        if (monthlyServerData.size == 28) {
            monthlyServerData.chunked(7)
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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val exerciseService = remember { ExerciseService(context) }

    var workoutRunning by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        MenuItem("Тренировка","-${calories.toInt()}", onClick = {
            navController.navigate("workout")
            },
            onClickAdd = {
                navController.navigate("addWorkout")
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




