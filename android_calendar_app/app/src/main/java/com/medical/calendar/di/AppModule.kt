package com.medical.calendar.di

import android.content.Context
import com.medical.calendar.data.remote.GoogleCalendarService
import com.medical.calendar.data.remote.GoogleSheetsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt 應用程式模組
 * 提供應用程式級別的依賴注入
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    /**
     * 提供 Application Context
     */
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
    
    /**
     * 提供 Google Sheets 服務
     */
    @Provides
    @Singleton
    fun provideGoogleSheetsService(@ApplicationContext context: Context): GoogleSheetsService {
        return GoogleSheetsService(context)
    }
    
    /**
     * 提供 Google Calendar 服務
     */
    @Provides
    @Singleton
    fun provideGoogleCalendarService(@ApplicationContext context: Context): GoogleCalendarService {
        return GoogleCalendarService(context)
    }
}



