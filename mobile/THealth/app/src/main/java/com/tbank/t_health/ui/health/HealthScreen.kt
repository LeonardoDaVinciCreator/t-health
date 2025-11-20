package com.tbank.t_health.ui.health

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tbank.t_health.R
import com.tbank.t_health.constants.HealthPermissions
import com.tbank.t_health.constants.NavigationDestinations
import com.tbank.t_health.data.local.*
import com.tbank.t_health.data.model.ActivityFullData
import com.tbank.t_health.data.repository.ActivityRepository
import com.tbank.t_health.screens.PERMISSIONS
import com.tbank.t_health.ui.theme.RobotoFontFamily
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun HealthScreen(
    navController: NavController,
    viewModel: HealthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val healthConnectClient = remember { HealthConnectClient.getOrCreate(context) }

    val todayStats by viewModel.todayStats.collectAsState()
    val yesterdaySteps by viewModel.yesterdaySteps.collectAsState()
    val monthlyStats by viewModel.monthlyStats.collectAsState()

    val user by viewModel.user.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadMonth() }

    LaunchedEffect(todayStats) {
        Log.d("HealthScreen", "Обновление todayStats: steps=${todayStats.steps}, activeMinutes=${todayStats.activeMinutes}, calories=${todayStats.calories}")
    }
    LaunchedEffect(user) {
        Log.d("HealthScreen", "Пользователь: $user")
    }

    val stepsGoal by viewModel.stepsGoal.collectAsState()
    val activeMinutesGoal by viewModel.activeMinutesGoal.collectAsState()
    val caloriesGoal by viewModel.caloriesGoal.collectAsState()

    val activeMinutesFormatted = viewModel.formatActiveMinutes(todayStats.activeMinutes)

    var showStepsDialog by remember { mutableStateOf(false) }
    var showCaloriesDialog by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(todayStats.steps > yesterdaySteps) }

    var permissionRequested by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        if (granted.containsAll(HealthPermissions.PERMISSIONS)) {
            coroutineScope.launch {
                viewModel.loadTodayData()
                user?.id?.let { userId ->
                    viewModel.sync(userId)
                } ?: run {
                    Log.e("HealthScreen", "userId is null, синхронизация невозможна")
                }
            }
        } else {
            Log.d("HealthScreen", "Разрешения Health Connect не получены")
        }
    }

    // Проверка разрешений
    LaunchedEffect(Unit) {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        if (!granted.containsAll(PERMISSIONS) && !permissionRequested) {
            permissionRequested = true
            permissionLauncher.launch(PERMISSIONS)
        } else if (granted.containsAll(PERMISSIONS)) {
            viewModel.loadTodayData()
        }
    }



    Scaffold(containerColor = Color(0xFFE0E2E3)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeaderBlock(user?.username ?: "Гость")

            Spacer(modifier = Modifier.height(12.dp))

            ActivityStatsBlock(
                steps = todayStats.steps,
                stepsGoal = stepsGoal,
                activeMinutes = todayStats.activeMinutes,
                activeMinutesFormatted = activeMinutesFormatted,
                activeMinutesGoal = activeMinutesGoal,
                calories = todayStats.calories,
                caloriesGoal = caloriesGoal,
                onStepsGoalClick = { showStepsDialog = true },
                onActiveMinutesGoalClick = { /* TODO */ },
                onCaloriesGoalClick = { showCaloriesDialog = true }
            )

            Spacer(modifier = Modifier.height(8.dp))
            if (showMessage) {
                StepDifferenceMessageBlock(
                    todaySteps = todayStats.steps,
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
                        viewModel.setStepsGoal(newGoal.toFloat())
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
                        viewModel.setCaloriesGoal(newGoal.toFloat())
                        showCaloriesDialog = false
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            StepsChart2(
                stepsGoal = stepsGoal,
                activeMinutesGoal = activeMinutesGoal,
                caloriesGoal = caloriesGoal,
                monthlyData = monthlyStats
            )

            Spacer(modifier = Modifier.height(14.dp))
            MenuSection(navController = navController, calories = todayStats.calories)
        }
    }
}
