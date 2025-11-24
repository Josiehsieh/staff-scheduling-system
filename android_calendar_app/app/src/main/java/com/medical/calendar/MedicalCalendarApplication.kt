package com.medical.calendar

import android.app.Application
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.medical.calendar.widget.WidgetUpdateWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

/**
 * Medical Calendar Application
 * 
 * 使用 Hilt 進行依賴注入
 * 初始化必要的服務和背景工作
 */
@HiltAndroidApp
class MedicalCalendarApplication : Application(), Configuration.Provider {
    
    override fun onCreate() {
        super.onCreate()
        
        println("🚀 Medical Calendar Application 啟動中...")
        
        // 初始化 WorkManager 進行背景同步
        setupBackgroundWork()
        
        println("✅ Medical Calendar Application 啟動完成")
    }
    
    /**
     * 設定背景工作（Widget 更新、資料同步等）
     */
    private fun setupBackgroundWork() {
        // 設定 Widget 定期更新工作
        val widgetUpdateRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            15, TimeUnit.MINUTES // 每 15 分鐘更新一次
        ).build()
        
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "widget_update",
            ExistingPeriodicWorkPolicy.KEEP,
            widgetUpdateRequest
        )
        
        println("📋 背景工作已設定")
    }
    
    /**
     * WorkManager 配置
     */
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }
}



