package com.tbank.t_health

import com.tbank.t_health.data.local.UserPrefs
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tbank.t_health.components.HeaderViewModel
import com.tbank.t_health.constants.NavigationDestinations
import com.tbank.t_health.constants.NavigationTabs
import com.tbank.t_health.data.local.NotificationHelper
import com.tbank.t_health.screens.*
import com.tbank.t_health.screens.auth.AuthScreen
import com.tbank.t_health.screens.health.AddWorkoutScreen
import com.tbank.t_health.ui.components.Footer
import com.tbank.t_health.ui.components.Header
import com.tbank.t_health.ui.screens.PostsScreen
import com.tbank.t_health.ui.theme.THealthTheme
import dagger.hilt.android.AndroidEntryPoint

import com.tbank.t_health.ui.health.HealthScreen
import com.tbank.t_health.ui.notifications.NotificationsScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var userPrefs: UserPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPrefs = UserPrefs(this)

        NotificationHelper.createNotificationChannel(this)
        enableEdgeToEdge()

        setContent {
            THealthTheme {
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination?.route

                // определение видимости header и footer
                val showHeaderAndFooter = currentDestination != NavigationDestinations.AUTH
                val headerViewModel: HeaderViewModel = hiltViewModel()
                val hasUnreadNotifications by headerViewModel.hasUnreadNotifications.collectAsState()

                val currentTab = NavigationTabs.AllTabs.find { it.id == currentDestination }

                LaunchedEffect(Unit) {
                    if (userPrefs.isUserLoggedIn()) {
                        Log.d("Notifications123", "Пользователь залогинен. Есть непрочитанные уведомления: $hasUnreadNotifications")
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    topBar = {
                        if (showHeaderAndFooter) {
                            Header(
                                hasUnreadNotifications = hasUnreadNotifications,
                                onNotificationClick = {
                                    Log.d("Notifications123", "Нажата иконка уведомлений")
                                    navController.navigate(NavigationDestinations.NOTIFICATIONS)
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (showHeaderAndFooter) {
                            Footer(
                                navController = navController,
                                currentDestination = currentDestination ?: NavigationDestinations.HEALTH,
                                onItemSelected = { destination ->
                                    navigateSingleTop(navController, destination)
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = if (userPrefs.isUserLoggedIn()) {
                            NavigationDestinations.HEALTH
                        } else {
                            NavigationDestinations.AUTH
                        },
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Основные экраны
                        composable(NavigationDestinations.HEALTH) {
                            HealthScreen(navController)
                        }
                        composable(NavigationDestinations.ACHIEVEMENTS) {
                            AchievementsScreen(navController)
                        }
                        composable(NavigationDestinations.POSTS) {
                            PostsScreen(navController)
                        }
                        composable(NavigationDestinations.CHAT) {
                            ChatScreen(navController)
                        }
                        composable(NavigationDestinations.PROFILE) {
                            ProfileScreen(navController)
                        }

                        // Второстепенные экраны
                        composable(NavigationDestinations.WORKOUT) {
                            WorkoutScreen(navController)
                        }
                        composable(NavigationDestinations.ADD_WORKOUT) {
                            AddWorkoutScreen(navController)
                        }
                        composable(NavigationDestinations.NOTIFICATIONS) {
                            NotificationsScreen()
                        }

                        composable(NavigationDestinations.AUTH) {
                            AuthScreen(
                                onLoginSuccess = {
                                    navController.navigate(NavigationDestinations.HEALTH) {
                                        popUpTo(NavigationDestinations.AUTH) { inclusive = true }
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
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) {
            saveState = true
        }
        restoreState = true
        // нет одинаковых экранов
        launchSingleTop = true
    }
}