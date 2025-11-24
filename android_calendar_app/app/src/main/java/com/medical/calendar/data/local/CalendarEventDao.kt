package com.medical.calendar.data.local

import androidx.room.*
import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.model.CalendarType
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface CalendarEventDao {
    
    @Query("SELECT * FROM calendar_events WHERE isDeleted = 0 ORDER BY startTime ASC")
    fun getAllEvents(): Flow<List<CalendarEvent>>
    
    @Query("SELECT * FROM calendar_events WHERE calendarType = :calendarType AND isDeleted = 0 ORDER BY startTime ASC")
    fun getEventsByType(calendarType: CalendarType): Flow<List<CalendarEvent>>
    
    @Query("SELECT * FROM calendar_events WHERE calendarType = :calendarType AND isDeleted = 0 ORDER BY startTime ASC")
    suspend fun getEventsByTypeSync(calendarType: CalendarType): List<CalendarEvent>
    
    @Query("SELECT * FROM calendar_events WHERE startTime >= :startDate AND startTime <= :endDate AND isDeleted = 0 ORDER BY startTime ASC")
    fun getEventsInDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<CalendarEvent>>
    
    @Query("SELECT * FROM calendar_events WHERE calendarId = :calendarId AND isDeleted = 0 ORDER BY startTime ASC")
    fun getEventsByCalendarId(calendarId: String): Flow<List<CalendarEvent>>
    
    @Query("SELECT * FROM calendar_events WHERE id = :eventId AND isDeleted = 0")
    suspend fun getEventById(eventId: Long): CalendarEvent?
    
    @Query("SELECT * FROM calendar_events WHERE eventId = :externalEventId AND calendarType = :calendarType AND isDeleted = 0")
    suspend fun getEventByExternalId(externalEventId: String, calendarType: CalendarType): CalendarEvent?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CalendarEvent): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<CalendarEvent>)
    
    @Update
    suspend fun updateEvent(event: CalendarEvent)
    
    @Query("UPDATE calendar_events SET isDeleted = 1 WHERE id = :eventId")
    suspend fun deleteEvent(eventId: Long)
    
    @Query("UPDATE calendar_events SET isDeleted = 1 WHERE calendarId = :calendarId")
    suspend fun deleteEventsByCalendarId(calendarId: String)
    
    @Query("DELETE FROM calendar_events WHERE calendarType = :calendarType")
    suspend fun deleteAllEventsByType(calendarType: CalendarType)
    
    @Query("SELECT COUNT(*) FROM calendar_events WHERE calendarType = :calendarType AND isDeleted = 0")
    suspend fun getEventCountByType(calendarType: CalendarType): Int
} 