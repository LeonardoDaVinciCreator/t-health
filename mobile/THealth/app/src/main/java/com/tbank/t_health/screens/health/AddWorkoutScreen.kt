package com.tbank.t_health.screens.health


import WorkoutType
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp


import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.tbank.t_health.R
import com.tbank.t_health.data.WorkoutRepository
import com.tbank.t_health.data.model.WorkoutData
import com.tbank.t_health.ui.theme.HeaderTypography
import com.tbank.t_health.ui.theme.InterFontFamily
import com.tbank.t_health.ui.theme.RobotoFontFamily
import com.tbank.t_health.ui.theme.RobotoMonoFontFamily
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(navController: NavController) {
    val context = LocalContext.current
    val workoutRepo = remember { WorkoutRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var durationMinutes by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                ){
                    Image(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(11.dp)
                            .align(Alignment.Center)
                    )
                }


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
                FieldData(type = "Продолжительность:", value = durationMinutes, onValueChange = { durationMinutes = it }, label = "ч:мм:cc", keyboardType = KeyboardType.Number),
                FieldData(type = "Потрачено калорий:", value = calories, onValueChange = { calories = it }, label = "300", keyboardType = KeyboardType.Number, shape = RoundedCornerShape(39.dp)),
                FieldData(type = "Дата:", value = date, onValueChange = { date = it }, label = "дд.мм.гггг", shape = RoundedCornerShape(30.dp), height = 39.dp)
            )
            TrainingFields(fields)



            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isBlank() || type.isBlank() || durationMinutes.isBlank() || calories.isBlank() || date.isBlank()) {
                        Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val localDate = try {
                        LocalDate.parse(date, dateFormatter)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Неверный формат даты", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val totalSeconds = try {
                        val parts = durationMinutes.split(":").map { it.toIntOrNull() ?: 0 }
                        val seconds = when (parts.size) {
                            3 -> parts[0] * 3600 + parts[1] * 60 + parts[2]
                            2 -> parts[0] * 60 + parts[1]
                            else -> {
                                Toast.makeText(context, "Неверный формат времени", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                        }
                        if (seconds < 1) {
                            Toast.makeText(context, "Продолжительность должна быть не менее 1 секунды", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                         seconds
                    } catch (e: Exception) {
                        Toast.makeText(context, "Ошибка в формате продолжительности", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    coroutineScope.launch {
                        val workout = WorkoutData(
                            id = UUID.randomUUID().toString(),
                            name = name,
                            type = type,
                            calories = calories.toDoubleOrNull() ?: 0.0,
                            durationSeconds = totalSeconds,
                            date = localDate.toString(),
                            plannedDate = localDate.toString(),
                            isCompleted = false
                        )

                        workoutRepo.saveWorkoutLocally(workout)
                        Toast.makeText(context, "Тренировка сохранена", Toast.LENGTH_SHORT).show()
                        navController.popBackStack() // Возврат на экран тренировок
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFDD500)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Сохранить",
                    style = TextStyle(
                        fontFamily = RobotoFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                )
            }
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
fun DurationPickerField(
    label: String = "Продолжительность:",
    onDurationSelected: (Int, Int, Int) -> Unit
) {
    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }

    Column {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = RobotoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(vertical = 12.dp, horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // часы
                DurationNumberPicker(
                    value = hours,
                    range = 0..23,
                    label = "ч"
                ) { new ->
                    hours = new
                    onDurationSelected(hours, minutes, seconds)
                }

                // минуты
                DurationNumberPicker(
                    value = minutes,
                    range = 0..59,
                    label = "мин"
                ) { new ->
                    minutes = new
                    onDurationSelected(hours, minutes, seconds)
                }

                // секунды
                DurationNumberPicker(
                    value = seconds,
                    range = 0..59,
                    label = "сек"
                ) { new ->
                    seconds = new
                    onDurationSelected(hours, minutes, seconds)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun DurationNumberPicker(
    value: Int,
    range: IntRange,
    label: String,
    onValueChange: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // кнопка -
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
                    .clickable { if (value > range.first) onValueChange(value - 1) },
                contentAlignment = Alignment.Center
            ) {
                Text("-", fontSize = 20.sp, color = Color.Black)
            }

            // значение
            Text(
                text = "%02d".format(value),
                modifier = Modifier.padding(horizontal = 12.dp),
                style = TextStyle(
                    fontFamily = RobotoMonoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            )

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
                    .clickable { if (value < range.last) onValueChange(value + 1) },
                contentAlignment = Alignment.Center
            ) {
                Text("+", fontSize = 20.sp, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = TextStyle(
                fontFamily = RobotoMonoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color(0xFF8C8E92)
            )
        )
    }
}


@Composable
fun TrainingFields(fields: List<FieldData>) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var durationHours by remember { mutableStateOf(0) }
    var durationMinutes by remember { mutableStateOf(0) }
    var durationSeconds by remember { mutableStateOf(0) }
    var showSecondsDialog by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var selectedWorkoutType by remember { mutableStateOf<WorkoutType?>(null) }

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
                // ======= Тип тренировки =======
                "Тип нагрузки:" -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(field.height)
                            .clip(field.shape)
                            .background(Color.White)
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .clickable { expanded = true },
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

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                        ) {
                            WorkoutType.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.displayName) },
                                    onClick = {
                                        selectedWorkoutType = type
                                        field.onValueChange(type.displayName) // 🔹 Сохраняем по-русски
                                        expanded = false
                                    }
                                )
                            }
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
                                val now = LocalDate.now()
                                val datePicker = android.app.DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        val pickedDate = LocalDate.of(year, month + 1, dayOfMonth)
                                        if (pickedDate.isBefore(now)) {
                                            Toast.makeText(context, "Дата не может быть раньше сегодня", Toast.LENGTH_SHORT).show()
                                        } else {
                                            val formatted = pickedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                            field.onValueChange(formatted)
                                        }
                                    },
                                    now.year, now.monthValue - 1, now.dayOfMonth
                                )
                                datePicker.show()
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
                            onConfirm = { h, m, s ->
                                val formatted = String.format("%02d:%02d:%02d", h, m, s)
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
fun DurationPickerDialog(
    initialHours: Int = 0,
    initialMinutes: Int = 0,
    initialSeconds: Int = 0,
    onDismiss: () -> Unit,
    onConfirm: (hours: Int, minutes: Int, seconds: Int) -> Unit
) {
    var hours by remember { mutableStateOf(initialHours) }
    var minutes by remember { mutableStateOf(initialMinutes) }
    var seconds by remember { mutableStateOf(initialSeconds) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите продолжительность") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NumberPickerColumn(
                    label = "ч",
                    range = 0..23,
                    value = hours,
                    onValueChange = { hours = it }
                )
                NumberPickerColumn(
                    label = "мин",
                    range = 0..59,
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
            TextButton(onClick = {
                onConfirm(hours, minutes, seconds)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
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
        // Используем compose-numberpicker
        androidx.compose.foundation.layout.Box {
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
fun WorkoutTypeSelector(
    selectedType: WorkoutType?,
    onTypeSelected: (WorkoutType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedType?.displayName ?: "",
            onValueChange = {},
            label = { Text("Тип тренировки") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            enabled = false,

        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            WorkoutType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}
