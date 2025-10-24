package com.tbank.t_health

import UserPrefs
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.tbank.t_health.ui.theme.THealthTheme

import com.tbank.t_health.screens.auth.AuthScreen
import com.tbank.t_health.ui.screens.PostsScreen

import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tbank.t_health.screens.AchievementsScreen
import com.tbank.t_health.screens.ChatScreen
import com.tbank.t_health.screens.HealthScreen
import com.tbank.t_health.screens.ProfileScreen
import com.tbank.t_health.ui.screens.PostsScreen
import com.tbank.t_health.screens.auth.AuthScreen

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import com.tbank.composefoodtracker.services.StepCounterService
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {




    private lateinit var userPrefs: UserPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPrefs = UserPrefs(this)
        enableEdgeToEdge()

        setContent {
            THealthTheme {



                Scaffold(modifier = Modifier.fillMaxSize()
                                            .safeDrawingPadding()) { innerPadding ->

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = if(userPrefs.isUserLoggedIn()) "health" else "auth"
                    ) {
                        composable("posts") { PostsScreen(navController) }
                        composable("health") { HealthScreen(navController) }
                        composable("achievements") { AchievementsScreen(navController) }
                        composable("chat") { ChatScreen(navController) }
                        composable("profile") { ProfileScreen(navController) }
                        composable("auth") {
                            AuthScreen(
                                modifier = Modifier.padding(innerPadding),
                                onLoginSuccess = {
                                    navController.navigate("posts") {
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

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    THealthTheme {
//        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//            AuthScreen(
//                modifier = Modifier.padding(innerPadding),
//                onLoginSuccess = {}
//            )
//            PostsScreen()
//
//        }
//    }
//}