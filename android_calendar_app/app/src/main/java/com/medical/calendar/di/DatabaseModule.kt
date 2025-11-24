package com.medical.calendar.di

import android.content.Context
import androidx.room.Room
import com.medical.calendar.data.local.CalendarDatabase
import com.medical.calendar.data.local.CalendarDao
import com.medical.calendar.data.local.CalendarEventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt 資料庫模組
 * 提供 Room 資料庫和 DAO 的依賴注入
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * 提供 Calendar 資料庫實例
     */
    @Provides
    @Singleton
    fun provideCalendarDatabase(@ApplicationContext context: Context): CalendarDatabase {
        return Room.databaseBuilder(
            context,
            CalendarDatabase::class.java,
            "medical_calendar_database"
        )
            .fallbackToDestructiveMigration() // 開發階段使用，正式版應移除
            .build()
    }
    
    /**
     * 提供 Calendar DAO
     */
    @Provides
    @Singleton
    fun provideCalendarDao(database: CalendarDatabase): CalendarDao {
        return database.calendarDao()
    }
    
    /**
     * 提供 CalendarEvent DAO
     */
    @Provides
    @Singleton
    fun provideCalendarEventDao(database: CalendarDatabase): CalendarEventDao {
        return database.calendarEventDao()
    }
}



