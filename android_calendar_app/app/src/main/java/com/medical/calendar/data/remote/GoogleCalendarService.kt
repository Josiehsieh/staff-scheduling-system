package com.medical.calendar.data.remote

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.CalendarList
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import com.google.api.services.calendar.model.Events
import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.model.CalendarType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Google Calendar 服務
 * 用於同步排班資料到 Google 日曆，讓排班可以同步到手機日曆
 */
@Singleton
class GoogleCalendarService @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val APPLICATION_NAME = "Medical Calendar App"
        private const val MEDICAL_SHIFT_CALENDAR_NAME = "醫療排班"
        
        // 排班事件的 Extended Property 鍵值，用於識別由 App 建立的事件
        private const val EXTENDED_PROPERTY_SOURCE = "source"
        private const val EXTENDED_PROPERTY_SOURCE_VALUE = "medical_calendar_app"
        private const val EXTENDED_PROPERTY_EVENT_ID = "app_event_id"
    }
    
    private val SCOPES = listOf(
        CalendarScopes.CALENDAR,
        CalendarScopes.CALENDAR_EVENTS
    )
    
    private var calendarService: Calendar? = null
    private var currentAccount: GoogleSignInAccount? = null
    private var medicalShiftCalendarId: String? = null
    
    /**
     * 初始化服務
     */
    fun initialize(account: GoogleSignInAccount) {
        currentAccount = account
        
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            SCOPES
        ).setSelectedAccount(account.account)
        
        calendarService = Calendar.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName(APPLICATION_NAME)
            .build()
        
        println("✅ Google Calendar 服務已初始化")
    }
    
    /**
     * 檢查服務是否已初始化
     */
    fun isInitialized(): Boolean {
        return calendarService != null && currentAccount != null
    }
    
    /**
     * 取得 Calendar 服務（向後相容）
     */
    fun getCalendarService(account: GoogleSignInAccount): Calendar {
        if (!isInitialized() || currentAccount != account) {
            initialize(account)
        }
        return calendarService!!
    }
    
    /**
     * 取得所有 Google 日曆列表
     */
    suspend fun getCalendarList(): List<com.google.api.services.calendar.model.Calendar> = withContext(Dispatchers.IO) {
        if (!isInitialized()) {
            println("❌ Google Calendar 服務未初始化")
            return@withContext emptyList()
        }
        
        return@withContext try {
            val calendarList: CalendarList = calendarService!!.calendarList().list().execute()
            calendarList.items ?: emptyList()
        } catch (e: Exception) {
            println("❌ 獲取 Google 日曆列表失敗: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * 取得或建立「醫療排班」日曆
     * 如果不存在則自動建立一個新的日曆
     */
    suspend fun getOrCreateMedicalShiftCalendar(): String? = withContext(Dispatchers.IO) {
        if (!isInitialized()) {
            println("❌ Google Calendar 服務未初始化")
            return@withContext null
        }
        
        // 如果已經有快取的 ID，直接返回
        if (medicalShiftCalendarId != null) {
            return@withContext medicalShiftCalendarId
        }
        
        return@withContext try {
            // 查詢是否已存在「醫療排班」日曆
            val calendars = getCalendarList()
            val existingCalendar = calendars.firstOrNull { 
                it.summary == MEDICAL_SHIFT_CALENDAR_NAME 
            }
            
            if (existingCalendar != null) {
                println("✅ 找到現有的「醫療排班」日曆: ${existingCalendar.id}")
                medicalShiftCalendarId = existingCalendar.id
                existingCalendar.id
            } else {
                // 建立新的「醫療排班」日曆
                val newCalendar = com.google.api.services.calendar.model.Calendar().apply {
                    summary = MEDICAL_SHIFT_CALENDAR_NAME
                    description = "由醫療行事曆 App 自動建立，用於同步排班資料"
                    timeZone = "Asia/Taipei"
                }
                
                val createdCalendar = calendarService!!.calendars().insert(newCalendar).execute()
                println("✅ 建立新的「醫療排班」日曆: ${createdCalendar.id}")
                medicalShiftCalendarId = createdCalendar.id
                createdCalendar.id
            }
        } catch (e: Exception) {
            println("❌ 取得或建立「醫療排班」日曆失敗: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    /**
     * 批量同步排班事件到 Google 日曆
     * 
     * @param events 要同步的排班事件列表
     * @return 同步結果統計 (成功數量, 失敗數量, 已存在數量)
     */
    suspend fun syncMedicalShiftsToGoogleCalendar(
        events: List<CalendarEvent>
    ): Triple<Int, Int, Int> = withContext(Dispatchers.IO) {
        if (!isInitialized()) {
            println("❌ Google Calendar 服務未初始化")
            return@withContext Triple(0, events.size, 0)
        }
        
        // 取得或建立「醫療排班」日曆
        val calendarId = getOrCreateMedicalShiftCalendar()
        if (calendarId == null) {
            println("❌ 無法取得「醫療排班」日曆")
            return@withContext Triple(0, events.size, 0)
        }
        
        var successCount = 0
        var failCount = 0
        var existingCount = 0
        
        println("📤 開始同步 ${events.size} 筆排班事件到 Google 日曆...")
        
        events.forEach { event ->
            try {
                // 檢查事件是否已存在
                val existingEvent = findExistingEvent(calendarId, event)
                
                if (existingEvent != null) {
                    // 事件已存在，更新它
                    updateGoogleCalendarEvent(calendarId, existingEvent.id, event)
                    existingCount++
                    println("   ⟳ 已更新: ${event.title}")
                } else {
                    // 建立新事件
                    createMedicalShiftEvent(calendarId, event)
                    successCount++
                    println("   ✓ 已建立: ${event.title}")
                }
            } catch (e: Exception) {
                failCount++
                println("   ✗ 失敗: ${event.title} - ${e.message}")
            }
        }
        
        println("✅ 同步完成！成功: $successCount, 更新: $existingCount, 失敗: $failCount")
        
        Triple(successCount, failCount, existingCount)
    }
    
    /**
     * 建立醫療排班事件到 Google 日曆
     */
    private suspend fun createMedicalShiftEvent(
        calendarId: String,
        event: CalendarEvent
    ): Event = withContext(Dispatchers.IO) {
        val googleEvent = Event().apply {
            summary = event.title
            description = event.description
            location = event.location
            colorId = "11" // 紅色，用於排班
            
            // 設定開始時間
            start = EventDateTime().apply {
                dateTime = com.google.api.client.util.DateTime(
                    event.startTime.toJavaLocalDateTime()
                        .atZone(ZoneId.of("Asia/Taipei"))
                        .toInstant()
                        .toEpochMilli()
                )
                timeZone = "Asia/Taipei"
            }
            
            // 設定結束時間
            end = EventDateTime().apply {
                dateTime = com.google.api.client.util.DateTime(
                    event.endTime.toJavaLocalDateTime()
                        .atZone(ZoneId.of("Asia/Taipei"))
                        .toInstant()
                        .toEpochMilli()
                )
                timeZone = "Asia/Taipei"
            }
            
            // 設定 Extended Properties 用於識別
            extendedProperties = Event.ExtendedProperties().apply {
                private = mapOf(
                    EXTENDED_PROPERTY_SOURCE to EXTENDED_PROPERTY_SOURCE_VALUE,
                    EXTENDED_PROPERTY_EVENT_ID to (event.eventId ?: "")
                )
            }
            
            // 設定提醒
            reminders = Event.Reminders().apply {
                useDefault = false
                overrides = listOf(
                    Event.Reminders.Override().apply {
                        method = "popup"
                        minutes = 60 // 提前 1 小時提醒
                    }
                )
            }
        }
        
        calendarService!!.events().insert(calendarId, googleEvent).execute()
    }
    
    /**
     * 更新 Google 日曆中的事件
     */
    private suspend fun updateGoogleCalendarEvent(
        calendarId: String,
        eventId: String,
        event: CalendarEvent
    ): Event = withContext(Dispatchers.IO) {
        // 先取得現有事件
        val existingEvent = calendarService!!.events().get(calendarId, eventId).execute()
        
        // 更新事件內容
        existingEvent.apply {
            summary = event.title
            description = event.description
            location = event.location
            
            // 更新開始時間
            start = EventDateTime().apply {
                dateTime = com.google.api.client.util.DateTime(
                    event.startTime.toJavaLocalDateTime()
                        .atZone(ZoneId.of("Asia/Taipei"))
                        .toInstant()
                        .toEpochMilli()
                )
                timeZone = "Asia/Taipei"
            }
            
            // 更新結束時間
            end = EventDateTime().apply {
                dateTime = com.google.api.client.util.DateTime(
                    event.endTime.toJavaLocalDateTime()
                        .atZone(ZoneId.of("Asia/Taipei"))
                        .toInstant()
                        .toEpochMilli()
                )
                timeZone = "Asia/Taipei"
            }
        }
        
        calendarService!!.events().update(calendarId, eventId, existingEvent).execute()
    }
    
    /**
     * 查找是否已存在相同的事件
     */
    private suspend fun findExistingEvent(
        calendarId: String,
        event: CalendarEvent
    ): Event? = withContext(Dispatchers.IO) {
        try {
            // 查詢該日期的所有事件
            val timeMin = com.google.api.client.util.DateTime(
                event.startTime.toJavaLocalDateTime()
                    .toLocalDate()
                    .atStartOfDay(ZoneId.of("Asia/Taipei"))
                    .toInstant()
                    .toEpochMilli()
            )
            
            val timeMax = com.google.api.client.util.DateTime(
                event.endTime.toJavaLocalDateTime()
                    .toLocalDate()
                    .plusDays(1)
                    .atStartOfDay(ZoneId.of("Asia/Taipei"))
                    .toInstant()
                    .toEpochMilli()
            )
            
            val events = calendarService!!.events().list(calendarId)
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
                .setPrivateExtendedProperty("$EXTENDED_PROPERTY_SOURCE=$EXTENDED_PROPERTY_SOURCE_VALUE")
                .execute()
            
            // 尋找具有相同 app_event_id 的事件
            events.items?.firstOrNull { googleEvent ->
                googleEvent.extendedProperties?.private?.get(EXTENDED_PROPERTY_EVENT_ID) == event.eventId
            }
        } catch (e: Exception) {
            println("⚠️  查找現有事件失敗: ${e.message}")
            null
        }
    }
    
    /**
     * 清除「醫療排班」日曆中的所有事件
     */
    suspend fun clearMedicalShiftCalendar(): Int = withContext(Dispatchers.IO) {
        if (!isInitialized()) {
            println("❌ Google Calendar 服務未初始化")
            return@withContext 0
        }
        
        val calendarId = medicalShiftCalendarId ?: getOrCreateMedicalShiftCalendar()
        if (calendarId == null) {
            println("❌ 無法取得「醫療排班」日曆")
            return@withContext 0
        }
        
        return@withContext try {
            // 查詢所有由 App 建立的事件
            val events = calendarService!!.events().list(calendarId)
                .setPrivateExtendedProperty("$EXTENDED_PROPERTY_SOURCE=$EXTENDED_PROPERTY_SOURCE_VALUE")
                .execute()
            
            var deletedCount = 0
            events.items?.forEach { event ->
                try {
                    calendarService!!.events().delete(calendarId, event.id).execute()
                    deletedCount++
                } catch (e: Exception) {
                    println("⚠️  刪除事件失敗: ${event.summary} - ${e.message}")
                }
            }
            
            println("✅ 已清除 $deletedCount 筆排班事件")
            deletedCount
        } catch (e: Exception) {
            println("❌ 清除排班事件失敗: ${e.message}")
            0
        }
    }
    
    /**
     * 從 Google 日曆讀取事件
     */
    suspend fun syncGoogleCalendarEvents(
        calendarId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<CalendarEvent> = withContext(Dispatchers.IO) {
        if (!isInitialized()) {
            println("❌ Google Calendar 服務未初始化")
            return@withContext emptyList()
        }
        
        return@withContext try {
            val timeMin = com.google.api.client.util.DateTime(
                startDate.toJavaLocalDateTime()
                    .atZone(ZoneId.of("Asia/Taipei"))
                    .toInstant()
                    .toEpochMilli()
            )
            
            val timeMax = com.google.api.client.util.DateTime(
                endDate.toJavaLocalDateTime()
                    .atZone(ZoneId.of("Asia/Taipei"))
                    .toInstant()
                    .toEpochMilli()
            )
            
            val events: Events = calendarService!!.events().list(calendarId)
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()
            
            events.items?.map { event ->
                CalendarEvent(
                    title = event.summary ?: "無標題",
                    description = event.description ?: "",
                    startTime = parseGoogleDateTime(event.start),
                    endTime = parseGoogleDateTime(event.end),
                    location = event.location ?: "",
                    calendarType = CalendarType.GOOGLE_CALENDAR,
                    calendarId = calendarId,
                    eventId = event.id,
                    color = event.colorId?.let { getGoogleCalendarColor(it) } ?: "#4285f4",
                    isAllDay = event.start.dateTime == null,
                    isRecurring = event.recurringEventId != null
                )
            } ?: emptyList()
        } catch (e: Exception) {
            println("❌ 同步 Google 日曆事件失敗: ${e.message}")
            emptyList()
        }
    }
    
    /**
     * 建立個人事件到 Google 日曆（向後相容）
     */
    suspend fun createGoogleCalendarEvent(
        account: GoogleSignInAccount,
        calendarId: String,
        event: CalendarEvent
    ): Boolean {
        if (!isInitialized() || currentAccount != account) {
            initialize(account)
        }
        
        return try {
            val googleEvent = Event().apply {
                summary = event.title
                description = event.description
                location = event.location
                
                start = EventDateTime().apply {
                    if (event.isAllDay) {
                        date = com.google.api.client.util.DateTime(
                            event.startTime.toJavaLocalDateTime().toLocalDate().toString()
                        )
                    } else {
                        dateTime = com.google.api.client.util.DateTime(
                            event.startTime.toJavaLocalDateTime()
                                .atZone(ZoneId.of("Asia/Taipei"))
                                .toInstant()
                                .toEpochMilli()
                        )
                        timeZone = "Asia/Taipei"
                    }
                }
                
                end = EventDateTime().apply {
                    if (event.isAllDay) {
                        date = com.google.api.client.util.DateTime(
                            event.endTime.toJavaLocalDateTime().toLocalDate().toString()
                        )
                    } else {
                        dateTime = com.google.api.client.util.DateTime(
                            event.endTime.toJavaLocalDateTime()
                                .atZone(ZoneId.of("Asia/Taipei"))
                                .toInstant()
                                .toEpochMilli()
                        )
                        timeZone = "Asia/Taipei"
                    }
                }
            }
            
            calendarService!!.events().insert(calendarId, googleEvent).execute()
            true
        } catch (e: Exception) {
            println("❌ 創建 Google 日曆事件失敗: ${e.message}")
            false
        }
    }
    
    /**
     * 解析 Google 日曆的日期時間
     */
    private fun parseGoogleDateTime(dateTime: EventDateTime): LocalDateTime {
        return try {
            when {
                dateTime.dateTime != null -> {
                    val instant = java.time.Instant.ofEpochMilli(dateTime.dateTime.value)
                    val zonedDateTime = instant.atZone(ZoneId.of("Asia/Taipei"))
                    LocalDateTime.parse(zonedDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                }
                dateTime.date != null -> {
                    LocalDateTime.parse(dateTime.date.toString() + "T00:00:00")
                }
                else -> {
                    println("⚠️  無法解析日期時間，使用當前時間")
                    kotlinx.datetime.Clock.System.now()
                        .toLocalDateTime(kotlinx.datetime.TimeZone.of("Asia/Taipei"))
                }
            }
        } catch (e: Exception) {
            println("⚠️  解析日期時間失敗: ${e.message}")
            kotlinx.datetime.Clock.System.now()
                .toLocalDateTime(kotlinx.datetime.TimeZone.of("Asia/Taipei"))
        }
    }
    
    /**
     * Google Calendar 顏色對應
     */
    private fun getGoogleCalendarColor(colorId: String): String {
        val colorMap = mapOf(
            "1" to "#a4bdfc",  // 淺藍
            "2" to "#7ae7bf",  // 淺綠
            "3" to "#dbadff",  // 淺紫
            "4" to "#ff887c",  // 淺紅
            "5" to "#fbd75b",  // 淺黃
            "6" to "#ffb878",  // 淺橙
            "7" to "#46d6db",  // 青色
            "8" to "#e1e1e1",  // 灰色
            "9" to "#5484ed",  // 藍色
            "10" to "#51b749", // 綠色
            "11" to "#dc2127"  // 紅色
        )
        return colorMap[colorId] ?: "#4285f4"
    }
} 