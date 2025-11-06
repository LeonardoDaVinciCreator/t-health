package com.tbank.t_health

import UserPrefs
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.tbank.t_health.screens.*
import com.tbank.t_health.screens.auth.AuthScreen
import com.tbank.t_health.screens.health.AddWorkoutScreen
import com.tbank.t_health.ui.components.Footer
import com.tbank.t_health.ui.components.Header
import com.tbank.t_health.ui.screens.PostsScreen
import com.tbank.t_health.ui.theme.THealthTheme

class MainActivity : ComponentActivity() {
    private lateinit var userPrefs: UserPrefs
    private var selectedFooterIndex by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPrefs = UserPrefs(this)
        enableEdgeToEdge()

        setContent {
            THealthTheme {
                val navController = rememberNavController()
                var showFooter by remember { mutableStateOf(true) }
                var showHeader by remember { mutableStateOf(true) }

                LaunchedEffect(navController) {
                    navController.currentBackStackEntryFlow.collect { entry ->
                        val currentRoute = entry.destination.route
                        showFooter = currentRoute != "auth"
                        showHeader = currentRoute != "auth"
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize().safeDrawingPadding(),
                    topBar = {
                        if (showHeader) {
                            Header()
                        }
                    },
                    bottomBar = {
                        if (showFooter) {
                            Footer(
                                navController,
                                selectedIndex = selectedFooterIndex,
                                onItemSelected = { index ->
                                    selectedFooterIndex = index
                                    when (index) {
                                        0 -> navigateSingleTop(navController, "health")
                                        1 -> navigateSingleTop(navController, "achievements")
                                        2 -> navigateSingleTop(navController, "posts")
                                        3 -> navigateSingleTop(navController, "chat")
                                        4 -> navigateSingleTop(navController, "profile")
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (userPrefs.isUserLoggedIn()) "health" else "auth",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("health") { HealthScreen(navController); selectedFooterIndex = 0 }
                        composable("achievements") { AchievementsScreen(navController); selectedFooterIndex = 1 }
                        composable("posts") { PostsScreen(navController); selectedFooterIndex = 2 }
                        composable("chat") { ChatScreen(navController); selectedFooterIndex = 3 }
                        composable("profile") { ProfileScreen(navController); selectedFooterIndex = 4 }

                        composable("workout") { WorkoutScreen(navController) }
                        composable("addWorkout") { AddWorkoutScreen(navController) }

                        composable("auth") {
                            AuthScreen(
                                onLoginSuccess = {
                                    navController.navigate("health") {
                                        popUpTo("auth") { inclusive = true }
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

private fun navigateSingleTop(navController: NavHostController, route: String) {
    val currentRoute = navController.currentDestination?.route
    if (currentRoute != route) {
        navController.navigate(route) {
            popUpTo("health") { inclusive = false }
            launchSingleTop = true
        }
    }
}
