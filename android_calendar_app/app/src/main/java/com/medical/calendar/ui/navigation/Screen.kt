package com.medical.calendar.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Calendar : Screen("calendar")
    object EventDetail : Screen("event_detail/{eventId}") {
        fun createRoute(eventId: Long) = "event_detail/$eventId"
    }
    object EventEdit : Screen("event_edit/{eventId}") {
        fun createRoute(eventId: Long?) = "event_edit/${eventId ?: "new"}"
    }
    object Settings : Screen("settings")
    object Sync : Screen("sync")
    
    // 新增功能模組
    object Projects : Screen("projects")
    object ProjectDetail : Screen("project_detail/{projectId}") {
        fun createRoute(projectId: Long) = "project_detail/$projectId"
    }
    object ProjectEdit : Screen("project_edit/{projectId}") {
        fun createRoute(projectId: Long?) = "project_edit/${projectId ?: "new"}"
    }
    
    object MenstrualCycle : Screen("menstrual_cycle")
    object MenstrualCycleEdit : Screen("menstrual_cycle_edit/{cycleId}") {
        fun createRoute(cycleId: Long?) = "menstrual_cycle_edit/${cycleId ?: "new"}"
    }
    
    object Finance : Screen("finance")
    object FinanceEdit : Screen("finance_edit/{transactionId}") {
        fun createRoute(transactionId: Long?) = "finance_edit/${transactionId ?: "new"}"
    }
    
    object Holidays : Screen("holidays")
    object ColorSettings : Screen("color_settings")
} 