package com.tbank.t_health.ui.health

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tbank.t_health.constants.HealthPermissions
import com.tbank.t_health.constants.HealthPermissions.PERMISSIONS
import kotlinx.coroutines.launch

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

    val trainingCalories by viewModel.trainingCalories.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        if (granted.containsAll(HealthPermissions.PERMISSIONS)) {
            coroutineScope.launch {
                viewModel.loadTodayData()
                user?.id?.let { userId ->
                    viewModel.sync(userId)
                    Log.d("ActivityRepository", "syncToServer() CALLED")
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

            user?.id?.let { userId ->
                viewModel.sync(userId)
                Log.d("HealthScreen", "sync() called because permissions already granted")
            }
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
            MenuSection(navController = navController, calories = trainingCalories)
        }
    }
}
