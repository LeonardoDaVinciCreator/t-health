package com.tbank.t_health.screens.health


import UserPrefs
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp


import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.tbank.t_health.R
import com.tbank.t_health.data.repository.WorkoutRepository
import com.tbank.t_health.data.model.WorkoutData
import com.tbank.t_health.data.model.WorkoutType
import com.tbank.t_health.ui.theme.RobotoFontFamily
import com.tbank.t_health.ui.theme.RobotoMonoFontFamily
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(navController: NavController) {
    val context = LocalContext.current
    val userPrefs = remember { UserPrefs(context) }
    val workoutRepo = remember { WorkoutRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var durationMinutes by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Невидимый элемент для центрирования
                Box(modifier = Modifier.size(48.dp))

                Text(
                    "Добавление тренировки",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        lineHeight = 17.sp
                    )
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { navController.popBackStack() }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(11.dp)
                            .align(Alignment.Center),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(40.dp),
                onClick = {
                    if (name.isBlank() || type.isBlank() || durationMinutes.isBlank() || calories.isBlank() || date.isBlank()) {
                        Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                        return@ExtendedFloatingActionButton
                    }

                    val localDate = try {
                        LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    } catch (_: Exception) {
                        Toast.makeText(context, "Неверный формат даты", Toast.LENGTH_SHORT).show()
                        return@ExtendedFloatingActionButton
                    }


                    val totalSeconds = try {
                        val parts = durationMinutes.split(":").map { it.toIntOrNull() ?: 0 }
                        val seconds = when (parts.size) {
                            3 -> parts[0] * 3600 + parts[1] * 60 + parts[2]
                            2 -> parts[0] * 60 + parts[1]
                            else -> {
                                Toast.makeText(context, "Неверный формат времени", Toast.LENGTH_SHORT).show()
                                return@ExtendedFloatingActionButton
                            }
                        }
                        if (seconds < 1) {
                            Toast.makeText(context, "Продолжительность должна быть не менее 1 секунды", Toast.LENGTH_SHORT).show()
                            return@ExtendedFloatingActionButton
                        }
                        seconds
                    } catch (_: Exception) {
                        Toast.makeText(context, "Ошибка в формате продолжительности", Toast.LENGTH_SHORT).show()
                        return@ExtendedFloatingActionButton
                    }

                    coroutineScope.launch {
                        val user = userPrefs.getUser() // ✅ получаем пользователя
                        if (user == null || user.id == null) {
                            Toast.makeText(context, "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        val workout = WorkoutData(
                            userId = user.id,
                            name = name,
                            type = type,
                            calories = calories.toDoubleOrNull() ?: 0.0,
                            durationSeconds = totalSeconds,
                            plannedDate = localDate,
                            isCompleted = false
                        )

                        workoutRepo.saveWorkoutLocally(workout)
                        navController.previousBackStackEntry?.savedStateHandle?.set("workoutSaved", true)
                        navController.popBackStack()
                    }
                },
                containerColor = Color(0xFFFDD500),
                shape = RoundedCornerShape(11.dp),
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
            ){
                Text(
                    "Сохранить",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 14.sp
                    )
                )
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val fields = listOf(
                FieldData(type = "Название тренировки:", name, onValueChange = { name = it }, label = "жимовая"),
                FieldData(type = "Тип нагрузки:", value = type, onValueChange = { type = it }, label = "Анаэробная"),
                FieldData(type = "Продолжительность:", value = durationMinutes, onValueChange = { durationMinutes = it }, label = "мм:cc", keyboardType = KeyboardType.Number),
                FieldData(type = "Потрачено калорий:", value = calories, onValueChange = { calories = it }, label = "300", keyboardType = KeyboardType.Number, shape = RoundedCornerShape(39.dp)),
                FieldData(type = "Дата:", value = date, onValueChange = { date = it }, label = "дд.мм.гггг", shape = RoundedCornerShape(30.dp), height = 39.dp)
            )
            TrainingFields(fields)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

data class FieldData(
    val type: String,
    val value: String,
    val onValueChange: (String) -> Unit,
    val label: String?,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val shape: Shape = RoundedCornerShape(20.dp),
    val height: Dp = 39.dp
)

@Composable
fun TrainingFields(fields: List<FieldData>) {

    var showWorkoutTypePicker by remember { mutableStateOf(false) }
    var selectedWorkoutType by remember { mutableStateOf<WorkoutType?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }

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

            var showDurationDialog by remember { mutableStateOf(false) }
            val textValue = field.value

            when (field.type) {
                "Тип нагрузки:" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(field.height)
                            .clip(field.shape)
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .clickable { showWorkoutTypePicker = true },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        val textToShow = selectedWorkoutType?.displayName ?: field.label.orEmpty()

                        Text(
                            text = textToShow,
                            style = TextStyle(
                                fontFamily = RobotoMonoFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = if (selectedWorkoutType == null) Color(0xFF8C8E92) else Color.Black
                            )
                        )

                        if (showWorkoutTypePicker) {
                            WorkoutTypePickerDialog(
                                initialType = selectedWorkoutType,
                                onDismiss = { showWorkoutTypePicker = false },
                                onConfirm = { type ->
                                    selectedWorkoutType = type
                                    field.onValueChange(type.displayName)
                                    showWorkoutTypePicker = false
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
                            .clickable {
                                showDatePicker = true
                            },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (textValue.isEmpty()) {
                            Text(
                                text = field.label ?: "",
                                style = TextStyle(
                                    fontFamily = RobotoMonoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = Color(0xFF8C8E92)
                                )
                            )
                        } else {
                            Text(
                                text = textValue,
                                style = TextStyle(
                                    fontFamily = RobotoMonoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            )
                        }
                    }

                    if (showDatePicker) {
                        CustomDatePickerDialog(
                            currentDate = if (currentDateValue.isNotEmpty()) {
                                try {
                                    LocalDate.parse(currentDateValue, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                } catch (_: Exception) {
                                    null
                                }
                            } else {
                                null
                            },
                            onDismiss = { showDatePicker = false },
                            onDateSelected = { date ->
                                val formatted = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

                                fields.find { it.type == "Дата:" }?.onValueChange?.invoke(formatted)
                                showDatePicker = false
                            }
                        )
                    }
                }

                "Продолжительность:" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(field.height)
                            .clip(field.shape)
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .clickable { showDurationDialog = true },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (textValue.isEmpty()) {
                            Text(
                                text = field.label ?: "",
                                style = TextStyle(
                                    fontFamily = RobotoMonoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = Color(0xFF8C8E92)
                                )
                            )
                        } else {
                            Text(
                                text = textValue,
                                style = TextStyle(
                                    fontFamily = RobotoMonoFontFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = Color.Black
                                )
                            )
                        }
                    }

                    if (showDurationDialog) {
                        DurationPickerDialog(
                            onDismiss = { showDurationDialog = false },
                            onConfirm = { m, s ->
                                val formatted = String.format("%02d:%02d", m, s)
                                field.onValueChange(formatted)
                                showDurationDialog = false
                            }
                        )
                    }
                }

                else -> {
                    BasicTextField(
                        value = textValue,
                        onValueChange = field.onValueChange,
                        singleLine = true,
                        textStyle = TextStyle(
                            fontFamily = RobotoMonoFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = field.keyboardType),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(field.height)
                                    .clip(field.shape)
                                    .background(Color.White)
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (textValue.isEmpty()) {
                                    Text(
                                        text = field.label ?: "",
                                        style = TextStyle(
                                            fontFamily = RobotoMonoFontFamily,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp,
                                            color = Color(0xFF8C8E92)
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun WorkoutTypePickerDialog(
    initialType: WorkoutType? = null,
    onDismiss: () -> Unit,
    onConfirm: (WorkoutType) -> Unit
) {
    var selectedType by remember { mutableStateOf(initialType) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFDFDFD),
        title = {
            Text(
                "Выберите тип нагрузки",
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
                WorkoutType.entries.forEach { type ->
                    WorkoutTypeItem(
                        workoutType = type,
                        isSelected = selectedType == type,
                        onSelect = { selectedType = type }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedType?.let { onConfirm(it) }
                },
                enabled = selectedType != null,
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

@Composable
fun WorkoutTypeItem(
    workoutType: WorkoutType,
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
            text = workoutType.displayName,
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
fun DurationPickerDialog(
    initialMinutes: Int = 0,
    initialSeconds: Int = 0,
    onDismiss: () -> Unit,
    onConfirm: (minutes: Int, seconds: Int) -> Unit
) {
    var minutes by remember { mutableStateOf(initialMinutes) }
    var seconds by remember { mutableStateOf(initialSeconds) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFDFDFD),
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NumberPickerColumn(
                    label = "мин",
                    range = 0..99,
                    value = minutes,
                    onValueChange = { minutes = it }
                )
                NumberPickerColumn(
                    label = "сек",
                    range = 0..59,
                    value = seconds,
                    onValueChange = { seconds = it }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(minutes, seconds)
                    onDismiss()
                },
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
                onClick = {
                    onDismiss()
                },
                enabled = true,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDD500)),
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

@Composable
fun NumberPickerColumn(
    label: String,
    range: IntRange,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label)
        Spacer(modifier = Modifier.height(4.dp))
        Box {
            AndroidView(
                modifier = Modifier.height(120.dp),
                factory = { context ->
                    android.widget.NumberPicker(context).apply {
                        minValue = range.first
                        maxValue = range.last
                        this.value = value
                        setOnValueChangedListener { _, _, newVal ->
                            onValueChange(newVal)
                        }
                    }
                },
                update = {
                    it.value = value
                }
            )
        }
    }
}

@Composable
fun CustomDatePickerDialog(
    currentDate: LocalDate? = null,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(currentDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFDFDFD),
        title = {
            Text(
                "Выберите дату",
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            )
        },
        text = {
            SingleSelectCalendar(
                initiallySelected = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedDate?.let { onDateSelected(it) }
                },
                enabled = selectedDate != null,
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
                onClick = {
                    onDismiss()
                },
                enabled = true,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDD500)),
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

@Composable
fun SingleSelectCalendar(
    modifier: Modifier = Modifier,
    initiallySelected: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit = { }
) {
    val monthYearFormatter = DateTimeFormatter.ofPattern("LLLL yyyy")

    var currentMonth by remember {
        mutableStateOf(initiallySelected?.let { YearMonth.from(it) } ?: YearMonth.now())
    }
    var selectedDate by remember { mutableStateOf(initiallySelected) }
    val daysOfWeek = listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС")

    // нахождение первого дня для сетки календаря
    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value + 6) % 7
    val firstGridDate = firstDayOfMonth.minusDays(firstDayOfWeek.toLong())

    // Всегда 6 недель в месяце (6*7 = 42 ячейки)
    val days = List(42) { firstGridDate.plusDays(it.toLong()) }

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
                    contentDescription = "Предыдущий месяц"
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
                    contentDescription = "Следующий месяц",
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
            contentAlignment = Alignment.Center
        ){
            Column{
                for (week in 0 until 6) {
                    Row {
                        for (dayIdx in 0 .. 6) {
                            val date = days[week * 7 + dayIdx]
                            val inCurrentMonth = date.month == currentMonth.month
                            val isSelected = date == selectedDate
                            val isToday = date == LocalDate.now()

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> Color(0xFFFDD500)
                                            isToday -> Color(0xFFE8E8E8)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable(
                                        enabled = inCurrentMonth && !date.isBefore(LocalDate.now()),
                                        onClick = {
                                            selectedDate = date
                                            onDateSelected(date)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    color = when {
                                        !inCurrentMonth -> Color.LightGray
                                        date.isBefore(LocalDate.now()) -> Color.LightGray
                                        else -> Color.Black
                                    },
                                    style = TextStyle(
                                        fontFamily = RobotoFontFamily,
                                        fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
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