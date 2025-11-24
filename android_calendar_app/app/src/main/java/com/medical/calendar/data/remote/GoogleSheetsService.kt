package com.medical.calendar.data.remote

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.model.CalendarType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Google Sheets 服務
 * 用於從 Google Sheets 讀取排班資料，取代 Supabase
 * 
 * Google Sheets 格式要求：
 * 欄位順序：日期 | 開始時間 | 結束時間 | 事業單位 | 人員名單 | 地點 | 顏色
 * 日期格式：YYYY-MM-DD
 * 時間格式：HH:MM
 * 人員名單：使用逗號分隔多個人員
 */
@Singleton
class GoogleSheetsService @Inject constructor(
    private val context: Context
) {
    
    companion object {
        // Google Sheets 設定
        private const val APPLICATION_NAME = "Medical Calendar App"
        private const val DEFAULT_SHEET_ID = "YOUR_SHEET_ID_HERE" // 需要在設定中配置
        private const val DEFAULT_RANGE = "排班資料!A2:G" // 從第2列開始讀取（第1列是標題）
        
        // 欄位索引
        private const val COL_DATE = 0        // A欄：日期
        private const val COL_START_TIME = 1  // B欄：開始時間
        private const val COL_END_TIME = 2    // C欄：結束時間
        private const val COL_UNIT_NAME = 3   // D欄：事業單位
        private const val COL_STAFF_NAMES = 4 // E欄：人員名單
        private const val COL_LOCATION = 5    // F欄：地點
        private const val COL_COLOR = 6       // G欄：顏色（選填，格式：#RRGGBB）
    }
    
    private var sheetsService: Sheets? = null
    private var sheetId: String = DEFAULT_SHEET_ID
    private var sheetRange: String = DEFAULT_RANGE
    
    /**
     * 初始化 Google Sheets 服務
     */
    fun initialize(account: GoogleSignInAccount, sheetId: String? = null, sheetRange: String? = null) {
        this.sheetId = sheetId ?: DEFAULT_SHEET_ID
        this.sheetRange = sheetRange ?: DEFAULT_RANGE
        
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            listOf(SheetsScopes.SPREADSHEETS_READONLY)
        ).setSelectedAccount(account.account)
        
        sheetsService = Sheets.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName(APPLICATION_NAME)
            .build()
        
        println("✅ Google Sheets 服務已初始化")
        println("   Sheet ID: $sheetId")
        println("   Range: $sheetRange")
    }
    
    /**
     * 檢查服務是否已初始化
     */
    fun isInitialized(): Boolean {
        return sheetsService != null && sheetId != DEFAULT_SHEET_ID
    }
    
    /**
     * 從 Google Sheets 同步排班資料
     * 
     * @param startDate 開始日期（用於過濾）
     * @param endDate 結束日期（用於過濾）
     * @return 排班事件列表
     */
    suspend fun syncMedicalShifts(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<CalendarEvent> = withContext(Dispatchers.IO) {
        if (!isInitialized()) {
            println("❌ Google Sheets 服務未初始化")
            return@withContext emptyList()
        }
        
        return@withContext try {
            println("📊 開始從 Google Sheets 讀取排班資料...")
            println("   查詢日期範圍: $startDate 到 $endDate")
            println("   Sheet ID: $sheetId")
            println("   Range: $sheetRange")
            
            // 讀取 Google Sheets 資料
            val response: ValueRange? = sheetsService
                ?.spreadsheets()
                ?.values()
                ?.get(sheetId, sheetRange)
                ?.execute()
            
            val values = response?.getValues()
            
            if (values.isNullOrEmpty()) {
                println("⚠️  Google Sheets 沒有資料")
                return@withContext emptyList()
            }
            
            println("   獲取到 ${values.size} 筆原始資料")
            
            // 解析並過濾資料
            val events = values.mapNotNull { row ->
                parseSheetRow(row)
            }.filter { event ->
                // 過濾日期範圍
                event.startTime >= startDate && event.endTime <= endDate
            }
            
            println("   過濾後剩餘 ${events.size} 筆排班資料")
            println("✅ Google Sheets 同步完成")
            
            events
            
        } catch (e: Exception) {
            println("❌ Google Sheets 同步失敗: ${e.message}")
            println("   錯誤類型: ${e.javaClass.simpleName}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * 解析 Google Sheets 的一列資料
     * 
     * @param row 資料列
     * @return CalendarEvent 或 null（如果資料不完整或格式錯誤）
     */
    private fun parseSheetRow(row: List<Any>): CalendarEvent? {
        return try {
            // 檢查必要欄位是否存在
            if (row.size < 5) {
                println("⚠️  資料列不完整，跳過: $row")
                return null
            }
            
            // 讀取欄位（安全處理可能不存在的欄位）
            val date = row.getOrNull(COL_DATE)?.toString() ?: return null
            val startTime = row.getOrNull(COL_START_TIME)?.toString() ?: return null
            val endTime = row.getOrNull(COL_END_TIME)?.toString() ?: return null
            val unitName = row.getOrNull(COL_UNIT_NAME)?.toString() ?: return null
            val staffNamesStr = row.getOrNull(COL_STAFF_NAMES)?.toString() ?: ""
            val location = row.getOrNull(COL_LOCATION)?.toString() ?: unitName
            val color = row.getOrNull(COL_COLOR)?.toString() ?: "#667eea"
            
            // 解析人員名單（逗號分隔）
            val staffNames = if (staffNamesStr.isNotBlank()) {
                staffNamesStr.split(",", "、", ";")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
            } else {
                emptyList()
            }
            
            // 組合日期時間字串
            val startDateTimeStr = "${date}T${startTime}"
            val endDateTimeStr = "${date}T${endTime}"
            
            // 解析日期時間
            val startDateTime = try {
                LocalDateTime.parse(startDateTimeStr)
            } catch (e: Exception) {
                println("⚠️  日期時間格式錯誤: $startDateTimeStr")
                return null
            }
            
            val endDateTime = try {
                LocalDateTime.parse(endDateTimeStr)
            } catch (e: Exception) {
                println("⚠️  日期時間格式錯誤: $endDateTimeStr")
                return null
            }
            
            // 建立事件標題
            val title = if (staffNames.isNotEmpty()) {
                "$unitName - ${staffNames.joinToString(", ")}"
            } else {
                unitName
            }
            
            // 建立事件描述
            val description = buildString {
                append("排班時間: $startTime - $endTime")
                if (staffNames.isNotEmpty()) {
                    append("\n人員: ${staffNames.joinToString(", ")}")
                }
            }
            
            // 建立 CalendarEvent
            CalendarEvent(
                title = title,
                description = description,
                startTime = startDateTime,
                endTime = endDateTime,
                location = location,
                calendarType = CalendarType.MEDICAL_SHIFT,
                calendarId = "medical_shifts",
                eventId = "${date}_${startTime}_${unitName}", // 生成唯一 ID
                color = color,
                isAllDay = false
            )
            
        } catch (e: Exception) {
            println("⚠️  解析資料列失敗: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    /**
     * 測試連線
     * 
     * @return 測試結果訊息
     */
    suspend fun testConnection(): String = withContext(Dispatchers.IO) {
        if (!isInitialized()) {
            return@withContext "❌ 服務未初始化，請先登入 Google 帳號"
        }
        
        return@withContext try {
            // 嘗試讀取 Sheet 標題
            val response = sheetsService
                ?.spreadsheets()
                ?.get(sheetId)
                ?.execute()
            
            val sheetTitle = response?.properties?.title ?: "未知"
            
            "✅ 連線成功！\n試算表名稱: $sheetTitle"
            
        } catch (e: Exception) {
            "❌ 連線失敗: ${e.message}"
        }
    }
    
    /**
     * 設定 Sheet ID 和範圍
     */
    fun configure(sheetId: String, sheetRange: String = DEFAULT_RANGE) {
        this.sheetId = sheetId
        this.sheetRange = sheetRange
        println("📋 Google Sheets 設定已更新")
        println("   Sheet ID: $sheetId")
        println("   Range: $sheetRange")
    }
    
    /**
     * 取得目前設定
     */
    fun getCurrentConfig(): Pair<String, String> {
        return Pair(sheetId, sheetRange)
    }
}


