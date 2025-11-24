package com.medical.calendar.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.calendar.auth.GoogleSignInHelper
import com.medical.calendar.data.local.CalendarDao
import com.medical.calendar.data.remote.GoogleCalendarService
import com.medical.calendar.data.remote.GoogleSheetsService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 設定畫面 ViewModel
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleSignInHelper: GoogleSignInHelper,
    private val googleSheetsService: GoogleSheetsService,
    private val googleCalendarService: GoogleCalendarService,
    private val calendarDao: CalendarDao
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
        checkGoogleSignInStatus()
    }
    
    /**
     * 載入設定
     */
    private fun loadSettings() {
        viewModelScope.launch {
            try {
                // 從 DataStore 或 SharedPreferences 載入設定
                // 這裡先使用預設值
                _uiState.update { it.copy(
                    isLoading = false
                ) }
                
                // 載入日曆列表
                loadCalendars()
                
            } catch (e: Exception) {
                println("❌ 載入設定失敗: ${e.message}")
                _uiState.update { it.copy(
                    isLoading = false,
                    error = "載入設定失敗: ${e.message}"
                ) }
            }
        }
    }
    
    /**
     * 檢查 Google 登入狀態
     */
    private fun checkGoogleSignInStatus() {
        val isSignedIn = googleSignInHelper.isSignedIn()
        val account = googleSignInHelper.getSignedInAccount()
        
        _uiState.update { it.copy(
            isGoogleSignedIn = isSignedIn,
            googleAccountEmail = account?.email,
            googleAccountName = account?.displayName
        ) }
    }
    
    /**
     * 載入日曆列表
     */
    private fun loadCalendars() {
        viewModelScope.launch {
            try {
                val calendars = calendarDao.getAllCalendars().map { calendar ->
                    CalendarSetting(
                        id = calendar.id,
                        name = calendar.name,
                        type = calendar.calendarType.toString(),
                        isEnabled = calendar.isVisible
                    )
                }
                
                _uiState.update { it.copy(calendars = calendars) }
                
            } catch (e: Exception) {
                println("❌ 載入日曆失敗: ${e.message}")
            }
        }
    }
    
    /**
     * Google 登入
     */
    fun signInToGoogle(onGetIntent: () -> Unit) {
        onGetIntent()
    }
    
    /**
     * 處理 Google 登入結果
     */
    fun handleGoogleSignInResult(success: Boolean, email: String? = null) {
        if (success) {
            _uiState.update { it.copy(
                isGoogleSignedIn = true,
                googleAccountEmail = email
            ) }
            
            // 初始化 Google 服務
            initializeGoogleServices()
        } else {
            _uiState.update { it.copy(
                error = "Google 登入失敗"
            ) }
        }
    }
    
    /**
     * Google 登出
     */
    fun signOutFromGoogle() {
        viewModelScope.launch {
            try {
                googleSignInHelper.signOut()
                _uiState.update { it.copy(
                    isGoogleSignedIn = false,
                    googleAccountEmail = null,
                    googleAccountName = null
                ) }
            } catch (e: Exception) {
                println("❌ Google 登出失敗: ${e.message}")
                _uiState.update { it.copy(
                    error = "登出失敗: ${e.message}"
                ) }
            }
        }
    }
    
    /**
     * 初始化 Google 服務
     */
    private fun initializeGoogleServices() {
        viewModelScope.launch {
            try {
                val account = googleSignInHelper.getSignedInAccount()
                if (account != null) {
                    googleSheetsService.initialize(account)
                    googleCalendarService.initialize(account)
                    println("✅ Google 服務初始化成功")
                }
            } catch (e: Exception) {
                println("❌ Google 服務初始化失敗: ${e.message}")
            }
        }
    }
    
    /**
     * 設定自動同步
     */
    fun setAutoSync(enabled: Boolean) {
        _uiState.update { it.copy(isAutoSync = enabled) }
        // TODO: 儲存到 DataStore
    }
    
    /**
     * 設定同步間隔
     */
    fun setSyncInterval(minutes: Int) {
        _uiState.update { it.copy(syncInterval = minutes) }
        // TODO: 儲存到 DataStore 並更新 WorkManager
    }
    
    /**
     * 手動同步
     */
    fun performManualSync() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            
            try {
                // 從 Google Sheets 同步資料
                if (googleSheetsService.isInitialized()) {
                    println("📊 開始同步 Google Sheets...")
                    // TODO: 實作同步邏輯
                }
                
                // 同步到 Google Calendar
                if (googleCalendarService.isInitialized()) {
                    println("📅 開始同步 Google Calendar...")
                    // TODO: 實作同步邏輯
                }
                
                _uiState.update { it.copy(
                    isSyncing = false,
                    lastSyncTime = getCurrentTimeString()
                ) }
                
                println("✅ 同步完成")
                
            } catch (e: Exception) {
                println("❌ 同步失敗: ${e.message}")
                _uiState.update { it.copy(
                    isSyncing = false,
                    error = "同步失敗: ${e.message}"
                ) }
            }
        }
    }
    
    /**
     * 測試 Google Sheets 連線
     */
    fun testGoogleSheetsConnection() {
        viewModelScope.launch {
            try {
                val result = googleSheetsService.testConnection()
                _uiState.update { it.copy(
                    testConnectionResult = result
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    error = "測試連線失敗: ${e.message}"
                ) }
            }
        }
    }
    
    /**
     * 設定 Google Sheet ID
     */
    fun setGoogleSheetId(sheetId: String, sheetRange: String = "排班資料!A2:G") {
        googleSheetsService.configure(sheetId, sheetRange)
        _uiState.update { it.copy(
            googleSheetId = sheetId,
            googleSheetRange = sheetRange
        ) }
        // TODO: 儲存到 DataStore
    }
    
    /**
     * 切換日曆顯示
     */
    fun toggleCalendar(calendarId: Long, enabled: Boolean) {
        viewModelScope.launch {
            try {
                // TODO: 更新資料庫
                val updatedCalendars = _uiState.value.calendars.map { calendar ->
                    if (calendar.id == calendarId) {
                        calendar.copy(isEnabled = enabled)
                    } else {
                        calendar
                    }
                }
                _uiState.update { it.copy(calendars = updatedCalendars) }
            } catch (e: Exception) {
                println("❌ 更新日曆設定失敗: ${e.message}")
            }
        }
    }
    
    /**
     * 設定顯示週數
     */
    fun setShowWeekNumbers(show: Boolean) {
        _uiState.update { it.copy(showWeekNumbers = show) }
        // TODO: 儲存到 DataStore
    }
    
    /**
     * 設定週一為第一天
     */
    fun setStartWeekOnMonday(startOnMonday: Boolean) {
        _uiState.update { it.copy(startWeekOnMonday = startOnMonday) }
        // TODO: 儲存到 DataStore
    }
    
    /**
     * 設定顯示農曆日期
     */
    fun setShowLunarDate(show: Boolean) {
        _uiState.update { it.copy(showLunarDate = show) }
        // TODO: 儲存到 DataStore
    }
    
    /**
     * 清除錯誤訊息
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    /**
     * 取得目前時間字串
     */
    private fun getCurrentTimeString(): String {
        val now = java.time.LocalDateTime.now()
        return now.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    }
}

/**
 * 設定 UI 狀態
 */
data class SettingsUiState(
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val error: String? = null,
    
    // Google 帳號
    val isGoogleSignedIn: Boolean = false,
    val googleAccountEmail: String? = null,
    val googleAccountName: String? = null,
    
    // Google Sheets 設定
    val googleSheetId: String = "",
    val googleSheetRange: String = "排班資料!A2:G",
    val testConnectionResult: String? = null,
    
    // 同步設定
    val isAutoSync: Boolean = true,
    val syncInterval: Int = 30, // 分鐘
    val lastSyncTime: String? = null,
    
    // 日曆設定
    val calendars: List<CalendarSetting> = emptyList(),
    
    // 顯示設定
    val showWeekNumbers: Boolean = false,
    val startWeekOnMonday: Boolean = true,
    val showLunarDate: Boolean = true,
    
    // 應用資訊
    val appVersion: String = "1.0.0"
)
