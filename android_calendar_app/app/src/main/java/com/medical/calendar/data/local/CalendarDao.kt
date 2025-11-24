package com.medical.calendar.data.local

import androidx.room.*
import com.medical.calendar.data.model.Calendar
import com.medical.calendar.data.model.CalendarType
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao {
    
    @Query("SELECT * FROM calendars ORDER BY name ASC")
    fun getAllCalendars(): Flow<List<Calendar>>
    
    @Query("SELECT * FROM calendars WHERE calendarType = :calendarType ORDER BY name ASC")
    fun getCalendarsByType(calendarType: CalendarType): Flow<List<Calendar>>
    
    @Query("SELECT * FROM calendars WHERE isVisible = 1 ORDER BY name ASC")
    fun getVisibleCalendars(): Flow<List<Calendar>>
    
    @Query("SELECT * FROM calendars WHERE id = :calendarId")
    suspend fun getCalendarById(calendarId: String): Calendar?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendar(calendar: Calendar)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendars(calendars: List<Calendar>)
    
    @Update
    suspend fun updateCalendar(calendar: Calendar)
    
    @Delete
    suspend fun deleteCalendar(calendar: Calendar)
    
    @Query("DELETE FROM calendars WHERE calendarType = :calendarType")
    suspend fun deleteCalendarsByType(calendarType: CalendarType)
    
    @Query("UPDATE calendars SET isVisible = :isVisible WHERE id = :calendarId")
    suspend fun updateCalendarVisibility(calendarId: String, isVisible: Boolean)
    
    @Query("UPDATE calendars SET lastSyncTime = :syncTime WHERE id = :calendarId")
    suspend fun updateLastSyncTime(calendarId: String, syncTime: Long)
} 