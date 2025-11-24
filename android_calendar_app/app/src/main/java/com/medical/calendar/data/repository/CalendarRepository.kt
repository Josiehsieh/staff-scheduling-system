package com.medical.calendar.data.repository

import com.medical.calendar.data.local.CalendarDao
import com.medical.calendar.data.local.CalendarEventDao
import com.medical.calendar.data.model.Calendar
import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.model.CalendarType
import com.medical.calendar.data.remote.GoogleCalendarService
import com.medical.calendar.data.remote.GoogleSheetsService
import com.medical.calendar.util.ColorManager
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepository @Inject constructor(
    private val calendarEventDao: CalendarEventDao,
    private val calendarDao: CalendarDao,
    private val googleSheetsService: GoogleSheetsService,
    private val googleCalendarService: GoogleCalendarService
) {
    
    // 本地資料庫操作
    fun getAllEvents(): Flow<List<CalendarEvent>> = calendarEventDao.getAllEvents()
    
    fun getEventsByType(calendarType: CalendarType): Flow<List<CalendarEvent>> =
        calendarEventDao.getEventsByType(calendarType)
    
    fun getEventsInDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<CalendarEvent>> =
        calendarEventDao.getEventsInDateRange(startDate, endDate)
    
    fun getAllCalendars(): Flow<List<Calendar>> = calendarDao.getAllCalendars()
    
    fun getVisibleCalendars(): Flow<List<Calendar>> = calendarDao.getVisibleCalendars()
    
    suspend fun insertEvent(event: CalendarEvent): Long = calendarEventDao.insertEvent(event)
    
    suspend fun updateEvent(event: CalendarEvent) = calendarEventDao.updateEvent(event)
    
    suspend fun deleteEvent(eventId: Long) = calendarEventDao.deleteEvent(eventId)
    
    suspend fun insertCalendar(calendar: Calendar) = calendarDao.insertCalendar(calendar)
    
    suspend fun updateCalendar(calendar: Calendar) = calendarDao.updateCalendar(calendar)
    
    suspend fun deleteCalendar(calendar: Calendar) = calendarDao.deleteCalendar(calendar)
    
    // 同步操作
    suspend fun syncAllCalendars(startDate: LocalDateTime, endDate: LocalDateTime) {
        try {
            println("📅 開始同步日曆資料...")
            
            // 同步醫療排班（從 Google Sheets）
            syncMedicalShiftsFromSheets(startDate, endDate)
            
            // 同步到 Google 日曆（如果已連接）
            syncMedicalShiftsToGoogleCalendar()
            
            println("✅ 日曆同步完成")
        } catch (e: Exception) {
            println("❌ 同步日曆失敗: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * 從 Google Sheets 同步排班資料到本地資料庫
     */
    suspend fun syncMedicalShiftsFromSheets(startDate: LocalDateTime, endDate: LocalDateTime) {
        try {
            if (!googleSheetsService.isInitialized()) {
                println("⚠️  Google Sheets 服務未初始化，跳過同步")
                return
            }
            
            println("📊 從 Google Sheets 同步排班資料...")
            
            // 從 Google Sheets 讀取排班資料
            val events = googleSheetsService.syncMedicalShifts(startDate, endDate)
            
            if (events.isEmpty()) {
                println("⚠️  沒有取得排班資料")
                return
            }
            
            // 清除舊的醫療排班事件
            calendarEventDao.deleteAllEventsByType(CalendarType.MEDICAL_SHIFT)
            println("   清除舊的排班資料")
            
            // 插入新的事件
            calendarEventDao.insertEvents(events)
            println("   插入 ${events.size} 筆新的排班資料")
            
            // 確保醫療排班日曆存在
            val medicalCalendar = Calendar(
                id = "medical_shifts",
                name = "醫療排班",
                color = ColorManager.getDefaultColor(CalendarType.MEDICAL_SHIFT),
                calendarType = CalendarType.MEDICAL_SHIFT,
                isVisible = true,
                isSyncEnabled = true
            )
            calendarDao.insertCalendar(medicalCalendar)
            
            println("✅ Google Sheets 同步完成")
            
        } catch (e: Exception) {
            println("❌ 同步醫療排班失敗: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * 將排班資料同步到 Google 日曆
     */
    suspend fun syncMedicalShiftsToGoogleCalendar() {
        try {
            if (!googleCalendarService.isInitialized()) {
                println("⚠️  Google Calendar 服務未初始化，跳過同步")
                return
            }
            
            println("📤 同步排班資料到 Google 日曆...")
            
            // 取得本地的醫療排班事件
            val events = calendarEventDao.getEventsByTypeSync(CalendarType.MEDICAL_SHIFT)
            
            if (events.isEmpty()) {
                println("⚠️  沒有排班資料需要同步")
                return
            }
            
            // 批量同步到 Google 日曆
            val (successCount, failCount, existingCount) = 
                googleCalendarService.syncMedicalShiftsToGoogleCalendar(events)
            
            println("✅ Google 日曆同步完成")
            println("   新增: $successCount 筆")
            println("   更新: $existingCount 筆")
            println("   失敗: $failCount 筆")
            
        } catch (e: Exception) {
            println("❌ 同步到 Google 日曆失敗: ${e.message}")
            e.printStackTrace()
        }
    }
    
    /**
     * 從 Google 日曆讀取事件（向後相容）
     */
    suspend fun syncGoogleCalendars(calendarId: String, startDate: LocalDateTime, endDate: LocalDateTime) {
        try {
            if (!googleCalendarService.isInitialized()) {
                println("⚠️  Google Calendar 服務未初始化")
                return
            }
            
            val events = googleCalendarService.syncGoogleCalendarEvents(calendarId, startDate, endDate)
            
            // 清除舊的 Google 日曆事件
            calendarEventDao.deleteAllEventsByType(CalendarType.GOOGLE_CALENDAR)
            
            // 插入新的事件
            if (events.isNotEmpty()) {
                calendarEventDao.insertEvents(events)
            }
            
            println("✅ Google 日曆事件同步完成：${events.size} 筆")
            
        } catch (e: Exception) {
            println("❌ 同步 Google 日曆失敗: ${e.message}")
        }
    }
    
    /**
     * 向後相容：舊的 syncMedicalShifts 方法
     */
    @Deprecated("使用 syncMedicalShiftsFromSheets 替代", ReplaceWith("syncMedicalShiftsFromSheets(startDate, endDate)"))
    suspend fun syncMedicalShifts(startDate: LocalDateTime, endDate: LocalDateTime) {
        syncMedicalShiftsFromSheets(startDate, endDate)
    }
    
    // 個人事件操作
    suspend fun createPersonalEvent(event: CalendarEvent): Long {
        val personalEvent = event.copy(
            calendarType = CalendarType.PERSONAL,
            calendarId = "personal_calendar"
        )
        
        // 確保個人日曆存在 - 使用與醫療排班系統不同的顏色
        val personalCalendar = Calendar(
            id = "personal_calendar",
            name = "個人行事曆",
            color = ColorManager.getDefaultColor(CalendarType.PERSONAL),
            calendarType = CalendarType.PERSONAL,
            isVisible = true,
            isSyncEnabled = true
        )
        calendarDao.insertCalendar(personalCalendar)
        
        return calendarEventDao.insertEvent(personalEvent)
    }
    
    suspend fun updatePersonalEvent(event: CalendarEvent) {
        calendarEventDao.updateEvent(event)
    }
    
    suspend fun deletePersonalEvent(eventId: Long) {
        calendarEventDao.deleteEvent(eventId)
    }
    
    // 日曆設定操作
    suspend fun toggleCalendarVisibility(calendarId: String, isVisible: Boolean) {
        calendarDao.updateCalendarVisibility(calendarId, isVisible)
    }
    
    suspend fun updateCalendarSync(calendarId: String, isSyncEnabled: Boolean) {
        val calendar = calendarDao.getCalendarById(calendarId)
        calendar?.let {
            val updatedCalendar = it.copy(isSyncEnabled = isSyncEnabled)
            calendarDao.updateCalendar(updatedCalendar)
        }
    }
} 