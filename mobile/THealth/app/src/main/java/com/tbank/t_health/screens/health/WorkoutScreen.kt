package com.tbank.t_health.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.tbank.t_health.data.repository.ActivityRepository
import com.tbank.t_health.data.repository.WorkoutRepository
import com.tbank.t_health.data.model.ActivityData
import com.tbank.t_health.data.model.WorkoutData
import com.tbank.t_health.ui.theme.RobotoFontFamily
import com.tbank.t_health.ui.theme.RobotoMonoFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ColorFilter.Companion.tint
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import com.tbank.t_health.data.model.ActivityType
import com.tbank.t_health.data.model.WorkoutType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()

    var showSuccessMessage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("workoutSaved")?.let { saved ->
            Log.d("WorkoutScreen", "Получен флаг workoutSaved: $saved")
            if (saved) {
                showSuccessMessage = true
                delay(5000)//время для сообщения
                showSuccessMessage = false
                navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("workoutSaved")
            }
        }
    }

    val initialWeekRange = remember {
        val today = LocalDate.now()
        val start = today.with(java.time.DayOfWeek.MONDAY)
        val end = start.plusDays(6)
        Pair(start, end)
    }

    var selectedDateRange by remember { mutableStateOf<Pair<LocalDate?, LocalDate?>?>(initialWeekRange) }

    var workoutRunning by remember { mutableStateOf(false) }
    var activeWorkoutId by remember { mutableStateOf<Long?>(null) }
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
                },
                containerColor = Color(0xFFFDD500),
                shape = RoundedCornerShape(11.dp),
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
                        colorFilter = tint(Color(0xFF333333))
                    )
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                val filteredWorkouts = if (selectedDateRange?.first != null || selectedDateRange?.second != null) {
                    val (start, end) = selectedDateRange!!
                    workouts.filter { workout ->
                        val date = workout.plannedDate ?: return@filter false
                        (start == null || !date.isBefore(start)) &&
                                (end == null || !date.isAfter(end))
                    }
                } else workouts


                val groupedByDay = filteredWorkouts
                    .mapNotNull { it.plannedDate?.let { date -> it to date } }
                    .sortedBy { it.second }
                    .groupBy { it.second }


                if (workouts.isEmpty()) {
                    item{
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
                    }
                } else {

                    // Аналитика за неделю
                    item{
                        WeeklyWorkoutStats(
                            workouts = workouts,
                            selectedDateRange = selectedDateRange,
                            onDateRangeSelected = { start, end ->
                                selectedDateRange = Pair(start, end)
                            }
                        )
                    }

                    groupedByDay.forEach { (date, workoutsInDayPairs) ->
                        val workoutsInDay = workoutsInDayPairs.map { it.first }

                        val headerText = formatDayHeader(date)

                        item {
                            Text(
                                text = headerText,
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
                                        val plannedDate = workout.plannedDate

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
                                                plannedDate = workout.plannedDate ?: LocalDate.now()
                                            )


                                            Log.d("WorkoutScreen", "Начата тренировка: ${workout.name}")

                                            // Таймер
                                            launch {
                                                while (remainingSeconds > 0 && workoutRunning) {
                                                    delay(1000)
                                                    remainingSeconds--
                                                }

                                                val finished = exerciseService.finishWorkout()


                                                finished?.let{
                                                    activityRepo.saveActivityLocally(
                                                        ActivityData(
                                                            id = it.id,
                                                            userId = workout.userId,
                                                            value = BigDecimal(workout.durationSeconds.toDouble()),
                                                            type = ActivityType.TRAINING,
                                                            calories = it.calories,
                                                            //date = LocalDateTime.now()
                                                        )
                                                    )
                                                }




                                                workoutRunning = false
                                                activeWorkoutId = null
                                                workout.id?.let { id ->
                                                    workoutRepo.markWorkoutCompleted(id)
                                                }
                                                workouts = workoutRepo.loadLocalWorkouts()
                                            }
                                        } else if (activeWorkoutId == workout.id) {
                                            workoutRunning = false
                                            activeWorkoutId = null
                                            val finished = exerciseService.finishWorkout()

                                            finished?.let{
                                                activityRepo.saveActivityLocally(
                                                    ActivityData(
                                                        id = it.id,
                                                        userId = workout.userId,
                                                        value = BigDecimal(workout.durationSeconds.toDouble()),
                                                        type = ActivityType.TRAINING,
                                                        calories = it.calories,
                                                        //date = LocalDateTime.now()
                                                    )
                                                )
                                            }

                                            workout.id?.let { id ->
                                                workoutRepo.markWorkoutCompleted(id)
                                            }

                                            workouts = workoutRepo.loadLocalWorkouts()
                                        }
                                    }
                                }

                            )
                        }
                    }
                }
            }

            //сообщение о сохранении тренировки
            if (showSuccessMessage) {
                SuccessMessage()
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
                    text = workout.type,
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
                    val totalSeconds = workout.durationSeconds
                    val minutes = totalSeconds / 60
                    val seconds = totalSeconds % 60
                    val formattedTime = String.format("%02d:%02d", minutes, seconds)

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
    selectedDateRange: Pair<LocalDate?, LocalDate?>? = null,
    onDateRangeSelected: (LocalDate?, LocalDate?) -> Unit
) {
    var isSelected by remember { mutableStateOf(false) }

    val (rangeStart, rangeEnd) = selectedDateRange ?: run {
        val now = LocalDate.now()
        val start = now.with(java.time.DayOfWeek.MONDAY)
        val end = start.plusDays(6)
        Pair(start, end)
    }

    val workoutsInPeriod = workouts.filter {
        val workoutDate = it.plannedDate ?: return@filter false
        (rangeStart == null || !workoutDate.isBefore(rangeStart)) &&
                (rangeEnd == null || !workoutDate.isAfter(rangeEnd)) &&
                it.isCompleted
    }


    val totalWorkouts = workoutsInPeriod.size
    val typeCounts = workoutsInPeriod.groupingBy { it.type }.eachCount()
    val totalSeconds = workoutsInPeriod.sumOf { it.durationSeconds }
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
            .fillMaxWidth().heightIn(max = 1000.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {
        val periodText = if (selectedDateRange != null) {
            "За выбранный период"
        } else {
            "На этой неделе"
        }

        Text(
            text = "$periodText вы провели $totalWorkouts ${getWorkoutWord(totalWorkouts)}, из них:",
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Диаграмма
            Box(modifier = Modifier.size(112.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(112.dp-25.dp)) {
                    var startAngle = -90f
                    typeProgressList.forEach { (type, progress) ->
                        if (progress > 0f) {
                            val sweep = progress * 360f
                            drawArc(
                                color = colorMap[type] ?: Color.Gray,
                                startAngle = startAngle,
                                sweepAngle = sweep,
                                useCenter = false,
                                style = Stroke(width = 50f, cap = StrokeCap.Butt)
                            )
                            startAngle += sweep
                        }
                    }
                }
            }

            // Легенда
            Column(modifier = Modifier.weight(1f).height(112.dp).padding(16.dp), verticalArrangement = Arrangement.Top) {
                WorkoutType.entries.forEach { type ->
                    val count = typeCounts[type.displayName] ?: 0
                    if (count > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(colorMap[type] ?: Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
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
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
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

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isSelected) "Скрыть календарь" else "Выберите интервал для просмотра тренировок",
            modifier = Modifier.clickable(onClick = {
                isSelected = !isSelected
                }
            ),
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                color = Color(0xFF7F7F7F)
            )
        )

        if (isSelected) {
            MultiSelectCalendar(
                initiallySelectedStart = rangeStart,
                initiallySelectedEnd = rangeEnd,
                onRangeSelected = { start, end ->
                    onDateRangeSelected(start, end)
                }
            )
        }

    }
}

@Composable
fun MultiSelectCalendar(
    modifier: Modifier = Modifier,
    initiallySelectedStart: LocalDate? = null,
    initiallySelectedEnd: LocalDate? = null,
    onRangeSelected: (LocalDate?, LocalDate?) -> Unit = { _, _ -> }
) {
    val monthYearFormatter = DateTimeFormatter.ofPattern("LLLL yyyy")

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    var selectedStart by remember { mutableStateOf(initiallySelectedStart) }
    var selectedEnd by remember { mutableStateOf(initiallySelectedEnd) }
    val daysOfWeek = listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС")

    // нахождение первого дня для сетки календаря
    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value + 6) % 7
    val firstGridDate = firstDayOfMonth.minusDays(firstDayOfWeek.toLong())

    // Всегда 6 недель в месяце (6*7 = 42 ячейки)
    val days = List(42) { firstGridDate.plusDays(it.toLong()) }

    fun isInRange(date: LocalDate): Boolean {
        if (selectedStart != null && selectedEnd != null) {
            return (date.isAfter(selectedStart) && date.isBefore(selectedEnd)) ||
                    date == selectedStart || date == selectedEnd
        }
        return false
    }

    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                currentMonth = currentMonth.minusMonths(1)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null
                )
            }
            Text(
                text = currentMonth.format(monthYearFormatter).replaceFirstChar { it.uppercase() },
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                ),
                color = Color.Black
            )
            IconButton(onClick = {
                currentMonth = currentMonth.plusMonths(1)
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = null,
                    modifier = Modifier.graphicsLayer {
                        scaleX = -1f
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Дни недели
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.size(32.dp),
                        style = TextStyle(
                            fontFamily = RobotoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp
                        ),
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))


        Box(
            modifier = Modifier.fillMaxWidth(),
            Alignment.Center
        ){
            Column{
                for (week in 0 until 6) {
                    Row {
                        for (dayIdx in 0 .. 6) {
                            val date = days[week * 7 + dayIdx]
                            val inCurrentMonth = date.month == currentMonth.month

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            date == selectedStart || date == selectedEnd -> Color(0xFFFDD500)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .border(
                                        width = if (isInRange(date)) 1.dp else 0.dp,
                                        color = if (isInRange(date)) Color(0xFFFDD500) else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        if (selectedStart == null || (selectedStart != null && selectedEnd != null)) {
                                            selectedStart = date
                                            selectedEnd = null
                                        } else {
                                            if (date.isBefore(selectedStart)) {
                                                selectedEnd = selectedStart
                                                selectedStart = date
                                            } else if (date == selectedStart) {
                                                selectedStart = null
                                                selectedEnd = null
                                            } else {
                                                selectedEnd = date
                                            }
                                        }
                                        onRangeSelected(selectedStart, selectedEnd)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    color = if (inCurrentMonth) Color.Black else Color.LightGray,
                                    style = TextStyle(
                                        fontFamily = RobotoFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 15.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .width(270.dp)
                .height(34.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "Success",
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Тренировка успешно добавлена",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                )
            }
        }
    }
}

fun formatDayHeader(date: LocalDate, today: LocalDate = LocalDate.now()): String {
    return when {
        date == today -> "Сегодня"
        date == today.minusDays(1) -> "Вчера"
        date == today.plusDays(1) -> "Завтра"
        else -> {
            val startOfWeek = today.with(java.time.DayOfWeek.MONDAY)
            val endOfWeek = startOfWeek.plusDays(6)

            if (date in startOfWeek..endOfWeek) {
                // Только день недели для текущей недели
                when (date.dayOfWeek) {
                    java.time.DayOfWeek.MONDAY -> "Понедельник"
                    java.time.DayOfWeek.TUESDAY -> "Вторник"
                    java.time.DayOfWeek.WEDNESDAY -> "Среда"
                    java.time.DayOfWeek.THURSDAY -> "Четверг"
                    java.time.DayOfWeek.FRIDAY -> "Пятница"
                    java.time.DayOfWeek.SATURDAY -> "Суббота"
                    java.time.DayOfWeek.SUNDAY -> "Воскресенье"
                    else -> date.format(DateTimeFormatter.ofPattern("EEEE"))
                }
            } else {
                // Полный формат Понедельник, 10.11.2025 для дат не в текущей недели
                val dayName = when (date.dayOfWeek) {
                    java.time.DayOfWeek.MONDAY -> "Понедельник"
                    java.time.DayOfWeek.TUESDAY -> "Вторник"
                    java.time.DayOfWeek.WEDNESDAY -> "Среда"
                    java.time.DayOfWeek.THURSDAY -> "Четверг"
                    java.time.DayOfWeek.FRIDAY -> "Пятница"
                    java.time.DayOfWeek.SATURDAY -> "Суббота"
                    java.time.DayOfWeek.SUNDAY -> "Воскресенье"
                    else -> date.format(DateTimeFormatter.ofPattern("EEEE"))
                }
                val formattedDate = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                "$dayName, $formattedDate"
            }
        }
    }
}