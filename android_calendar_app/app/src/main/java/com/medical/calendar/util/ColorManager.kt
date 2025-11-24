package com.medical.calendar.util

import androidx.compose.ui.graphics.Color
import com.medical.calendar.data.model.CalendarType

object ColorManager {
    
    // 10種主要顏色（與排班系統不同）
    val CALENDAR_COLORS = listOf(
        "#FF6B6B", // 珊瑚紅
        "#4ECDC4", // 青綠色
        "#45B7D1", // 天藍色
        "#96CEB4", // 薄荷綠
        "#FFEAA7", // 金黃色
        "#DDA0DD", // 梅紅色
        "#98D8C8", // 海藍綠
        "#F7DC6F", // 檸檬黃
        "#BB8FCE", // 薰衣草紫
        "#85C1E9"  // 淺藍色
    )
    
    // 專案顏色
    val PROJECT_COLORS = listOf(
        "#E74C3C", // 紅色
        "#3498DB", // 藍色
        "#2ECC71", // 綠色
        "#F39C12", // 橙色
        "#9B59B6", // 紫色
        "#1ABC9C", // 青色
        "#E67E22", // 深橙色
        "#34495E", // 深灰色
        "#F1C40F", // 黃色
        "#E91E63"  // 粉紅色
    )
    
    // 記帳類別顏色
    val FINANCE_COLORS = listOf(
        "#FF5722", // 深橙色
        "#2196F3", // 藍色
        "#4CAF50", // 綠色
        "#FF9800", // 橙色
        "#9C27B0", // 紫色
        "#00BCD4", // 青色
        "#795548", // 棕色
        "#607D8B", // 藍灰色
        "#FFEB3B", // 黃色
        "#FF4081"  // 粉紅色
    )
    
    fun getDefaultColor(type: CalendarType): String {
        return when (type) {
            CalendarType.MEDICAL_SHIFT -> "#FF5722" // 排班系統用紅色
            CalendarType.PERSONAL -> CALENDAR_COLORS[0]
            CalendarType.GOOGLE_CALENDAR -> CALENDAR_COLORS[2]
        }
    }
    
    fun getColorByIndex(index: Int, colorSet: List<String> = CALENDAR_COLORS): String {
        return colorSet[index % colorSet.size]
    }
    
    fun getRandomColor(colorSet: List<String> = CALENDAR_COLORS): String {
        return colorSet.random()
    }
    
    fun hexToColor(hex: String): Color {
        return Color(android.graphics.Color.parseColor(hex))
    }
    
    fun colorToHex(color: Color): String {
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()
        return String.format("#%02X%02X%02X", red, green, blue)
    }
    
    // 檢查顏色是否為淺色
    fun isLightColor(hex: String): Boolean {
        val color = android.graphics.Color.parseColor(hex)
        val red = android.graphics.Color.red(color)
        val green = android.graphics.Color.green(color)
        val blue = android.graphics.Color.blue(color)
        
        // 計算亮度
        val brightness = (red * 299 + green * 587 + blue * 114) / 1000
        return brightness > 128
    }
    
    // 獲取對比色（用於文字）
    fun getContrastColor(hex: String): String {
        return if (isLightColor(hex)) "#000000" else "#FFFFFF"
    }
} 