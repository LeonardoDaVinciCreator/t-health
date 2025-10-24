package com.tbank.t_health.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.health.connect.client.HealthConnectClient
import androidx.navigation.NavController
import com.tbank.composefoodtracker.services.StepCounterService
import com.tbank.t_health.R
import com.tbank.t_health.ui.components.Footer
import com.tbank.t_health.ui.theme.PostBlockTypography
import com.tbank.t_health.ui.theme.StatsTypography

@Composable
fun  HealthScreen(navController: NavController) {
    val context = LocalContext.current

    val healthConnectClient = remember { HealthConnectClient.getOrCreate(context) }

    var steps by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }

    var showDialog by remember { mutableStateOf(false) }

    


//    if (showDialog) {
//        StepInputDialog(
//            currentSteps = steps,
//            onConfirm = { newSteps ->
//                steps = newSteps
//                //  вызвать метод для обновления шагов в Health Connect
//            },
//            onDismiss = { showDialog = false }
//        )
//    }


    Scaffold(
    bottomBar = { Footer(navController) },
    containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileStatsBlock(steps){
                showDialog = true
            }
            Spacer(modifier = Modifier.height(10.dp))
            StepsChart()
            Spacer(modifier = Modifier.height(14.dp))
            MenuSection()
        }
    }
}

private suspend fun loadSteps(
    context: Context,
    onStepsUpdate: (Int) -> Unit,
    onLoadingUpdate: (Boolean) -> Unit
) {
    onLoadingUpdate(true)
    try {
        val stepCounterService = StepCounterService(context)
        val steps = stepCounterService.getStepsForToday()
        onStepsUpdate(steps)
    } catch (e: Exception) {
        onStepsUpdate(0)
    } finally {
        onLoadingUpdate(false)
    }
}

@Composable
fun ProfileStatsBlock(steps: Int, onStepsClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(181.dp)
            .background(Color(0xFFE5E5E5), RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)

    ) {
        ProfileHeader()

        Spacer(modifier = Modifier.height(13.dp))

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp),
            color = Color(0xFF000000)
        )
        Spacer(modifier = Modifier.height(12.dp))

        StatsSection(onStepsClick, steps)
    }
}

@Composable
fun ProfileHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = "Аватар",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Сергей",
                style = StatsTypography.headlineLarge
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_reload),
            contentDescription = "Обновить",
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.TopEnd)
        )
    }
}


@Composable
fun StatsSection(onStepsClick: () -> Unit, steps: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .background(Color(0xFFE5E5E5), RoundedCornerShape(16.dp))
            ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatCircle(
            label = "Шаги",
            current = steps.toFloat(),
            goal = 8000f,
            unit = "",
            color = Color(0xFF00FD22),
            onClick = onStepsClick
        )

        StatCircle(
            label = "Ходьба",
            timeMinutes = 3 * 60 + 15,
            goal = 4f,
            unit = "ч",
            color = Color(0xFF2196F3),
            onClick = onStepsClick
        )

        StatCircle(
            label = "Ккал",
            current = 500f,
            goal = 1200f,
            unit = "Kcal",
            color = Color(0xFFFF9800),
            onClick = onStepsClick
        )
    }
}

//изменить ширину
@Composable
fun StatCircle(
    label: String,
    current: Float = 0f,
    goal: Float,
    timeMinutes: Int? = null,
    unit: String? = null,
    color: Color = Color(0xFF00FD22),
    onClick: () -> Unit
) {

    val progress = when {
        timeMinutes != null -> (timeMinutes / (goal * 60f)).coerceIn(0f, 1f)
        else -> (current / goal).coerceIn(0f, 1f)
    }
    val achieved = progress >= 1f

    val strokeWidthDp = 14.52.dp
    val strokeWidthPx = with(LocalDensity.current) { strokeWidthDp.toPx() }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color.White, if (achieved) color else Color(0xFFBDBDBD), if (achieved) color else Color(0xFFBDBDBD)),
        startY = 0.0f,
        endY = 100.0f


    )

    //val progress = (current / goal).coerceIn(0f, 1f)
    //val achieved = current >= goal
    val arcColor = if (achieved) color else Color(0xFFAAAAAA)

    val displayValue = when {
        timeMinutes != null -> {
            val hours = timeMinutes / 60
            val minutes = timeMinutes % 60
            "${hours}h ${minutes}m"
        }
        unit != null -> "${current.toInt()} $unit"
        else -> "${current.toInt()}"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.48.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawArc(
                    color = Color(0xFFE0E0E0),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidthPx)
                )

                drawArc(
                    brush = gradientBrush,//градиент
                    startAngle = -90f,
                    sweepAngle = progress * 360f,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidthPx)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(45.dp)
            ) {
                Text(
                    text = displayValue,
                    style = StatsTypography.bodySmall.copy(
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

    }
}

@Composable
fun StepsChart() {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE5E5E5), RoundedCornerShape(16.dp))
            //.padding(12.dp)
    ) {
        var currentWeek by remember { mutableStateOf(0) }

        val allWeeks = listOf(
            listOf(8000, 8900, 6700, 9500, 8600, 9100, 9700),
            listOf(7200, 6500, 8300, 9100, 7500, 9800, 10500),
            listOf(5000, 6000, 4000, 7000, 6500, 8000, 8200)
        )
        val weekDates = listOf(
            listOf("14.10", "15.10", "16.10", "17.10", "18.10", "19.10", "20.10"),
            listOf("21.10", "22.10", "23.10", "24.10", "25.10", "26.10", "27.10"),
            listOf("28.10", "29.10", "30.10", "31.10", "01.11", "02.11", "03.11")
        )

        val steps = allWeeks[currentWeek]
        val maxSteps = steps.maxOrNull() ?: 10000

        Text(
            text = "Шаги",
            style = StatsTypography.bodyLarge,
            modifier = Modifier.padding(start = 15.dp, top = 15.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Ряд со столбцами
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    steps.forEachIndexed { _, value ->
                        val barHeight = (120 * (value / maxSteps.toFloat())).dp

                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(barHeight),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            // Столбик
                            Box(
                                modifier = Modifier
                                    .width(25.dp)
                                    .height(barHeight)
                                    .background(Color(0xFFAAAAAA))
                            )

                            Box(modifier = Modifier
                                .width(40.dp)
                                .height(barHeight)){
                                // Круг по центру столбца
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
                                        text = value.toString(),
                                        style = StatsTypography.bodySmall,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }


                        }
                    }
                }

                // Divider под всеми столбцами
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp),
                    color = Color(0xFFAAAAAA)
                )

                // Даты и кнопки под Divider
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, start = 2.dp, end = 2.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Кнопка "Назад"
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .clip(CircleShape)
                            .offset(x = (4).dp)
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

                    // Кнопка "Вперед"
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



@Composable
fun MenuSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        MenuItem(R.drawable.ic_dumbbell, "Тренировки", 45.dp)
        MenuItem(R.drawable.ic_food, "Питание",55.dp)
        MenuItem(R.drawable.ic_tip, "Какой-то совет", 25.dp)//просто знак !
    }
}

@Composable
fun MenuItem(icon: Int, label: String, width: Dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFCDCDCD))
            .clickable { }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(46.dp)
            .background(Color(0xFFAAAAAA), CircleShape),
            Alignment.Center){
            Image(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(width)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = StatsTypography.labelMedium
        )
    }
}

//@Composable
//@Preview(showBackground = true)
//fun ProfileScreenPreview() {
//    HealthScreen()
//}