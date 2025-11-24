package com.medical.calendar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.medical.calendar.ui.calendar.CalendarScreen
import com.medical.calendar.ui.event.EventDetailScreen
import com.medical.calendar.ui.event.EventEditScreen
import com.medical.calendar.ui.settings.SettingsScreen
import com.medical.calendar.ui.projects.ProjectsScreen
import com.medical.calendar.ui.menstrual.MenstrualCycleScreen
import com.medical.calendar.ui.finance.FinanceScreen
import androidx.compose.ui.Modifier

@Composable
fun CalendarNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Calendar.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Calendar.route) {
            CalendarScreen(
                onNavigateToEventDetail = { eventId ->
                    navController.navigate(Screen.EventDetail.createRoute(eventId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(
            route = Screen.EventDetail.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getLong("eventId") ?: 0L
            EventDetailScreen(
                eventId = eventId,
                onNavigateBack = { navController.popBackStack() },
                onEditEvent = { editEventId ->
                    navController.navigate(Screen.EventEdit.createRoute(editEventId))
                }
            )
        }
        
        composable(
            route = Screen.EventEdit.route,
            arguments = listOf(
                navArgument("eventId") { 
                    type = NavType.StringType 
                    nullable = true
                    defaultValue = "new"
                }
            )
        ) { backStackEntry ->
            val eventIdStr = backStackEntry.arguments?.getString("eventId")
            val eventId = if (eventIdStr == "new") null else eventIdStr?.toLongOrNull()
            
            EventEditScreen(
                eventId = eventId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // 專案管理路由
        composable(Screen.Projects.route) {
            ProjectsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProjectDetail = { projectId ->
                    navController.navigate(Screen.ProjectDetail.createRoute(projectId))
                },
                onNavigateToProjectEdit = { projectId ->
                    navController.navigate(Screen.ProjectEdit.createRoute(projectId))
                }
            )
        }
        
        composable(
            route = Screen.ProjectDetail.route,
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getLong("projectId") ?: 0L
            // TODO: 實作 ProjectDetailScreen
            // ProjectDetailScreen(
            //     projectId = projectId,
            //     onNavigateBack = { navController.popBackStack() },
            //     onEditProject = { editProjectId ->
            //         navController.navigate(Screen.ProjectEdit.createRoute(editProjectId))
            //     }
            // )
        }
        
        composable(
            route = Screen.ProjectEdit.route,
            arguments = listOf(
                navArgument("projectId") { 
                    type = NavType.StringType 
                    nullable = true
                    defaultValue = "new"
                }
            )
        ) { backStackEntry ->
            val projectIdStr = backStackEntry.arguments?.getString("projectId")
            val projectId = if (projectIdStr == "new") null else projectIdStr?.toLongOrNull()
            // TODO: 實作 ProjectEditScreen
            // ProjectEditScreen(
            //     projectId = projectId,
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }
        
        // 月經週期追蹤路由
        composable(Screen.MenstrualCycle.route) {
            MenstrualCycleScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCycleEdit = { cycleId ->
                    navController.navigate(Screen.MenstrualCycleEdit.createRoute(cycleId))
                }
            )
        }
        
        composable(
            route = Screen.MenstrualCycleEdit.route,
            arguments = listOf(
                navArgument("cycleId") { 
                    type = NavType.StringType 
                    nullable = true
                    defaultValue = "new"
                }
            )
        ) { backStackEntry ->
            val cycleIdStr = backStackEntry.arguments?.getString("cycleId")
            val cycleId = if (cycleIdStr == "new") null else cycleIdStr?.toLongOrNull()
            // TODO: 實作 MenstrualCycleEditScreen
            // MenstrualCycleEditScreen(
            //     cycleId = cycleId,
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }
        
        // 記帳功能路由
        composable(Screen.Finance.route) {
            FinanceScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTransactionEdit = { transactionId ->
                    navController.navigate(Screen.FinanceEdit.createRoute(transactionId))
                }
            )
        }
        
        composable(
            route = Screen.FinanceEdit.route,
            arguments = listOf(
                navArgument("transactionId") { 
                    type = NavType.StringType 
                    nullable = true
                    defaultValue = "new"
                }
            )
        ) { backStackEntry ->
            val transactionIdStr = backStackEntry.arguments?.getString("transactionId")
            val transactionId = if (transactionIdStr == "new") null else transactionIdStr?.toLongOrNull()
            // TODO: 實作 FinanceEditScreen
            // FinanceEditScreen(
            //     transactionId = transactionId,
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }
        
        // 節日管理路由
        composable(Screen.Holidays.route) {
            // TODO: 實作 HolidaysScreen
            // HolidaysScreen(
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }
        
        // 顏色設定路由
        composable(Screen.ColorSettings.route) {
            // TODO: 實作 ColorSettingsScreen
            // ColorSettingsScreen(
            //     onNavigateBack = { navController.popBackStack() }
            // )
        }
    }
} 