package com.tbank.t_health.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tbank.t_health.R
import com.tbank.t_health.data.ActivityRepository
import com.tbank.t_health.data.WorkoutRepository
import com.tbank.t_health.data.model.ActivityData
import com.tbank.t_health.data.model.WorkoutData
import com.tbank.t_health.ui.theme.RobotoFontFamily
import com.tbank.t_health.ui.theme.RobotoMonoFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()

    var workoutRunning by remember { mutableStateOf(false) }
    var activeWorkoutId by remember { mutableStateOf<String?>(null) }
    var remainingSeconds by remember { mutableStateOf(0) }
    var workouts by remember { mutableStateOf<List<WorkoutData>>(emptyList()) }

    val context = LocalContext.current
    val workoutRepo = remember { WorkoutRepository(context) }
    val activityRepo = remember { ActivityRepository(context) }
    val exerciseService = remember { com.tbank.composefoodtracker.services.ExerciseService(context) }

    // загрузка списка тренировок при открытии экрана
    LaunchedEffect(Unit) {
        workouts = workoutRepo.loadLocalWorkouts()
    }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .size(width = 230.dp, height = 40.dp),
                onClick = {

                    navController.navigate("addWorkout")
//                    coroutineScope.launch {
//                        val today = LocalDate.now()
//                        val plannedDates = listOf(
//                            today,
//                            today.plusDays(1),
//                            today.plusDays(2)
//                        )
//
//                        val workoutsToAdd = plannedDates.mapIndexed { index, date ->
//                            WorkoutData(
//                                id = UUID.randomUUID().toString(),
//                                name = "Тренировка #${index + 1}",
//                                type = "running",
//                                calories = 200.0,
//                                durationSeconds = 600,
//                                date = date,
//                                plannedDate = date,
//                                isCompleted = false
//                            )
//                        }
//
//                        workoutsToAdd.forEach { workoutRepo.saveWorkoutLocally(it) }
//                        workouts = workoutRepo.loadLocalWorkouts()
//
//                        Log.d("WorkoutScreen", "Добавлены 3 тренировки на 3 дня: $plannedDates")
//                    }
                },
                containerColor = Color(0xFFFDD500),
                shape = RoundedCornerShape(8.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
                text = {
                    Text(
                        "Добавить тренировку",
                        style = TextStyle(
                            fontFamily = RobotoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            lineHeight = 14.sp
                        )
                    )
                },
                icon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_plus),
                        modifier = Modifier.size(11.dp),
                        contentDescription = "Plus",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0xFF333333))
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (workouts.isEmpty()) {
                Text(
                    "Нет тренировок. Добавьте первую!",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                    color = Color.Gray
                )
            } else {

                // Аналитика за неделю
                WeeklyWorkoutStats(
                    workouts = workouts,
                    onSelectInterval = { /* TODO: открыть выбор периода */ }
                )


                //группировка по дням недели
                val groupedByDay = workouts
                    //конвертация в localDate
                    .map { workout ->
                        val parsedDate = LocalDate.parse(workout.plannedDate)
                        workout to parsedDate
                    }
                    .sortedBy  { it.second }
                    .groupBy { it.second }

                val dayNames = mapOf(
                    java.time.DayOfWeek.MONDAY to "Понедельник",
                    java.time.DayOfWeek.TUESDAY to "Вторник",
                    java.time.DayOfWeek.WEDNESDAY to "Среда",
                    java.time.DayOfWeek.THURSDAY to "Четверг",
                    java.time.DayOfWeek.FRIDAY to "Пятница",
                    java.time.DayOfWeek.SATURDAY to "Суббота",
                    java.time.DayOfWeek.SUNDAY to "Воскресенье"
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    groupedByDay.forEach { (date, workoutsInDayPairs) ->
                        val workoutsInDay = workoutsInDayPairs.map { it.first }

                        val dayName = dayNames[date.dayOfWeek] ?: ""
                        val formattedDate = date.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"))

                        item {
                            Text(
                                text = "$dayName, $formattedDate",
                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, start = 8.dp),
                                style = TextStyle(
                                    fontFamily = RobotoFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                ),
                                color = Color(0xFF333333)
                            )
                        }

                        items(workoutsInDay) { workout ->
                            val isRunning = workoutRunning && activeWorkoutId == workout.id
                            WorkoutCard(
                                workout = workout,
                                isRunning = isRunning,
                                remainingSeconds = remainingSeconds,
                                onClick = {
                                    coroutineScope.launch {
                                        val today = LocalDate.now()
                                        val plannedDate = LocalDate.parse(workout.plannedDate)

                                        // Проверка даты
                                        if (today != plannedDate) {
                                            Toast.makeText(
                                                context,
                                                "Только в запланированный день (${plannedDate})",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            return@launch
                                        }

                                        if (!workoutRunning) {
                                            workoutRunning = true
                                            activeWorkoutId = workout.id
                                            remainingSeconds = workout.durationSeconds

                                            exerciseService.startWorkout(
                                                name = workout.name,
                                                type = workout.type,
                                                durationSeconds = workout.durationSeconds,
                                                calories = workout.calories,
                                                plannedDate = LocalDate.parse(workout.date)
                                            )

                                            Log.d("WorkoutScreen", "Начата тренировка: ${workout.name}")

                                            // Таймер
                                            launch {
                                                while (remainingSeconds > 0 && workoutRunning) {
                                                    delay(1000)
                                                    remainingSeconds--
                                                }

                                                val finished = exerciseService.finishWorkout()


                                                finished?.let {
                                                    activityRepo.saveActivityLocally(
                                                        ActivityData(
                                                            id = it.id,
                                                            activeMinutes = it.durationSeconds / 60,
                                                            calories = it.calories,
                                                            date = LocalDate.now()
                                                        )
                                                    )
                                                }


                                                workoutRunning = false
                                                activeWorkoutId = null
                                                workoutRepo.markWorkoutCompleted(workout.id)
                                                workouts = workoutRepo.loadLocalWorkouts()
                                            }
                                        } else if (activeWorkoutId == workout.id) {
                                            workoutRunning = false
                                            activeWorkoutId = null
                                            val finished = exerciseService.finishWorkout()

                                            finished?.let {
                                                activityRepo.saveActivityLocally(
                                                    ActivityData(
                                                        id = it.id,
                                                        activeMinutes = it.durationSeconds / 60,
                                                        calories = it.calories,
                                                        date = LocalDate.now()
                                                    )
                                                )
                                            }

                                            workoutRepo.markWorkoutCompleted(workout.id)
                                            workouts = workoutRepo.loadLocalWorkouts()
                                        }
                                    }
                                }

                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutCard(
    workout: WorkoutData,
    isRunning: Boolean,
    remainingSeconds: Int,
    onClick: () -> Unit
) {
    // Цвет карточки: серый, если завершена
    val backgroundColor = when {
        isRunning -> Color(0xFFDCF8C6) // зелёный при активной тренировке
        workout.isCompleted -> Color(0xFFE0E0E0) // сероватый, если завершена
        else -> Color.White // обычный цвет
    }

    // Если завершена — клики отключены
    val clickableModifier = if (!workout.isCompleted) {
        Modifier.clickable { onClick() }
    } else {
        Modifier // без клика
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(clickableModifier),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = workout.name,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                color = if (workout.isCompleted) Color.Gray else Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when (workout.type) {
                        "running" -> "Кардио"
                        "strength" -> "Силовая"
                        "endurance" -> "Выносливость"
                        else -> workout.type
                    },
                    style = TextStyle(
                        fontFamily = RobotoMonoFontFamily,
                        fontSize = 12.sp
                    ),
                    color = if (workout.isCompleted) Color(0xFF9E9E9E) else Color(0xFF97A1B2)
                )

                Row(
                    modifier = Modifier.width(150.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val minutes = workout.durationSeconds / 60
                    val formattedTime = String.format("%02d:%02d", minutes, 0)

                    Text(
                        formattedTime,
                        style = TextStyle(fontFamily = RobotoMonoFontFamily, fontSize = 12.sp),
                        color = if (workout.isCompleted) Color(0xFF9E9E9E) else Color(0xFF97A1B2)
                    )

                    Text(
                        "${workout.calories.toInt()} ккал",
                        style = TextStyle(fontFamily = RobotoMonoFontFamily, fontSize = 12.sp),
                        color = if (workout.isCompleted) Color(0xFF9E9E9E) else Color(0xFF97A1B2)
                    )
                }
            }

            // отображение "в процессе"
            AnimatedVisibility(visible = isRunning) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(Color(0xFFDCF8C6))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val minutes = remainingSeconds / 60
                    val seconds = remainingSeconds % 60
                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        style = TextStyle(fontFamily = RobotoFontFamily, fontSize = 12.sp),
                        color = Color(0xFF000000)
                    )
                    Text(
                        "Тренировка в процессе...",
                        style = TextStyle(fontFamily = RobotoFontFamily, fontSize = 14.sp),
                        color = Color(0xFF000000)
                    )
                }
            }

            // надпись "Завершена"
            if (workout.isCompleted) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Завершена",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    ),
                    color = Color(0xFF616161)
                )
            }
        }
    }
}

fun getWorkoutWord(count: Int): String {
    return when {
        count % 10 == 1 && count % 100 != 11 -> "тренировку"
        count % 10 in 2..4 && count % 100 !in 12..14 -> "тренировки"
        else -> "тренировок"
    }
}

@Composable
fun WeeklyWorkoutStats(
    workouts: List<WorkoutData>,
    onSelectInterval: () -> Unit
) {
    val startOfWeek = LocalDate.now().with(java.time.DayOfWeek.MONDAY)
    val endOfWeek = startOfWeek.plusDays(6)

    val workoutsThisWeek = workouts.filter {
        val date = LocalDate.parse(it.date)
        date in startOfWeek..endOfWeek && it.isCompleted
    }

    val totalWorkouts = workoutsThisWeek.size
    val typeCounts = workoutsThisWeek.groupingBy { it.type }.eachCount()
    val totalSeconds = workoutsThisWeek.sumOf { it.durationSeconds }
    val totalMinutes = totalSeconds / 60
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    val colorMap = mapOf(
        WorkoutType.CARDIO to Color(0xFFFFD54F),
        WorkoutType.STRENGTH to Color(0xFFFF8A65),
        WorkoutType.ENDURANCE to Color(0xFF64B5F6),
        WorkoutType.FLEXIBILITY to Color(0xFF81C784),
        WorkoutType.BALANCE to Color(0xFFBA68C8)
    )

    val total = typeCounts.values.sum().takeIf { it > 0 } ?: 1

    val typeProgressList = WorkoutType.entries.map { type ->
        val progress = (typeCounts[type.displayName] ?: 0) / total.toFloat()
        type to progress
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "На этой неделе вы провели $totalWorkouts ${getWorkoutWord(totalWorkouts)}, из них:",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Диаграмма
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(120.dp)) {
                    var startAngle = -90f
                    typeProgressList.forEach { (type, progress) ->
                        if (progress > 0f) {
                            val sweep = progress * 360f
                            drawArc(
                                color = colorMap[type] ?: Color.Gray,
                                startAngle = startAngle,
                                sweepAngle = sweep,
                                useCenter = false,
                                style = Stroke(width = 22f, cap = StrokeCap.Round)
                            )
                            startAngle += sweep
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Легенда
            Column(modifier = Modifier.weight(1f)) {
                WorkoutType.entries.forEach { type ->
                    val count = typeCounts[type.displayName] ?: 0
                    if (count > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(colorMap[type] ?: Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "$count ${type.displayName.lowercase()}",
                                style = TextStyle(
                                    fontFamily = RobotoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Общая продолжительность тренировок: " +
                    "${if (hours > 0) "$hours часов " else ""}$minutes минут",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Выбрать другой интервал",
            modifier = Modifier.clickable(onClick = onSelectInterval),
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF7F7F7F)
            )
        )
    }
}
