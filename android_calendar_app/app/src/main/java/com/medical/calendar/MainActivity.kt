package com.medical.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.medical.calendar.ui.navigation.CalendarNavHost
import com.medical.calendar.ui.navigation.Screen
import com.medical.calendar.ui.theme.MedicalCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedicalCalendarTheme {
                MedicalCalendarApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalCalendarApp() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                val items = listOf(
                    NavigationItem(
                        route = Screen.Calendar.route,
                        title = "行事曆",
                        icon = Icons.Default.CalendarToday
                    ),
                    NavigationItem(
                        route = Screen.Projects.route,
                        title = "專案",
                        icon = Icons.Default.Assignment
                    ),
                    NavigationItem(
                        route = Screen.MenstrualCycle.route,
                        title = "月經週期",
                        icon = Icons.Default.Favorite
                    ),
                    NavigationItem(
                        route = Screen.Finance.route,
                        title = "記帳",
                        icon = Icons.Default.AccountBalanceWallet
                    ),
                    NavigationItem(
                        route = Screen.Settings.route,
                        title = "設定",
                        icon = Icons.Default.Settings
                    )
                )
                
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        CalendarNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

data class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) 