package com.medical.calendar.ui.calendar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.repository.CalendarRepository
import com.medical.calendar.widget.CalendarWidgetProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: CalendarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _currentDate = MutableStateFlow(LocalDate.now())
    val currentDate: StateFlow<LocalDate> = _currentDate.asStateFlow()

    private val _selectedDate = MutableStateFlow<LocalDate?>(LocalDate.now())
    val selectedDate: StateFlow<LocalDate?> = _selectedDate.asStateFlow()

    private val _showLunarDate = MutableStateFlow(true)
    val showLunarDate: StateFlow<Boolean> = _showLunarDate.asStateFlow()

    init {
        loadEvents()
        loadCalendars()
        loadSettings()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val events = repository.getAllEvents()
                _uiState.update { 
                    it.copy(
                        events = events,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "載入事件失敗"
                    )
                }
            }
        }
    }

    fun loadCalendars() {
        viewModelScope.launch {
            try {
                val calendars = repository.getAllCalendars()
                _uiState.update { it.copy(calendars = calendars) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "載入日曆失敗")
                }
            }
        }
    }

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun previousMonth() {
        _currentDate.value = _currentDate.value.minusMonths(1)
        loadEventsForCurrentMonth()
    }

    fun nextMonth() {
        _currentDate.value = _currentDate.value.plusMonths(1)
        loadEventsForCurrentMonth()
    }

    fun goToToday() {
        val today = LocalDate.now()
        _currentDate.value = today
        _selectedDate.value = today
        loadEventsForCurrentMonth()
    }

    private fun loadSettings() {
        // 這裡應該從 SharedPreferences 或其他本地存儲載入設定
        // 暫時使用預設值
        _showLunarDate.value = true
    }

    fun setShowLunarDate(show: Boolean) {
        _showLunarDate.value = show
        // 這裡應該保存設定到本地存儲
    }

    private fun loadEventsForCurrentMonth() {
        viewModelScope.launch {
            try {
                val startOfMonth = _currentDate.value.withDayOfMonth(1)
                val endOfMonth = _currentDate.value.withDayOfMonth(_currentDate.value.lengthOfMonth())
                
                val events = repository.getEventsByDateRange(startOfMonth, endOfMonth)
                _uiState.update { it.copy(events = events) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "載入月份事件失敗")
                }
            }
        }
    }

    fun syncWithSupabase() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            
            try {
                repository.syncWithSupabase()
                val lastSyncTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
                _uiState.update { 
                    it.copy(
                        isSyncing = false,
                        lastSyncTime = lastSyncTime,
                        error = null
                    )
                }
                loadEvents() // 重新載入事件
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isSyncing = false,
                        error = e.message ?: "同步失敗"
                    )
                }
            }
        }
    }

    fun syncWithGoogleCalendar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            
            try {
                repository.syncWithGoogleCalendar()
                val lastSyncTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))
                _uiState.update { 
                    it.copy(
                        isSyncing = false,
                        lastSyncTime = lastSyncTime,
                        error = null
                    )
                }
                loadEvents() // 重新載入事件
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isSyncing = false,
                        error = e.message ?: "Google日曆同步失敗"
                    )
                }
            }
        }
    }

    fun addEvent(event: CalendarEvent, context: Context? = null) {
        viewModelScope.launch {
            try {
                repository.addEvent(event)
                loadEvents() // 重新載入事件
                context?.let { CalendarWidgetProvider.updateAllWidgets(it) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "新增事件失敗")
                }
            }
        }
    }

    fun updateEvent(event: CalendarEvent, context: Context? = null) {
        viewModelScope.launch {
            try {
                repository.updateEvent(event)
                loadEvents() // 重新載入事件
                context?.let { CalendarWidgetProvider.updateAllWidgets(it) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "更新事件失敗")
                }
            }
        }
    }

    fun deleteEvent(eventId: Long, context: Context? = null) {
        viewModelScope.launch {
            try {
                repository.deleteEvent(eventId)
                loadEvents() // 重新載入事件
                context?.let { CalendarWidgetProvider.updateAllWidgets(it) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "刪除事件失敗")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun getEventsForDate(date: LocalDate): List<CalendarEvent> {
        return uiState.value.events.filter { 
            LocalDate.parse(it.date) == date 
        }.sortedBy { it.startTime }
    }

    fun getEventsForCurrentMonth(): List<CalendarEvent> {
        val startOfMonth = _currentDate.value.withDayOfMonth(1)
        val endOfMonth = _currentDate.value.withDayOfMonth(_currentDate.value.lengthOfMonth())
        
        return uiState.value.events.filter { event ->
            val eventDate = LocalDate.parse(event.date)
            eventDate >= startOfMonth && eventDate <= endOfMonth
        }
    }
}

data class CalendarUiState(
    val events: List<CalendarEvent> = emptyList(),
    val calendars: List<com.medical.calendar.data.model.Calendar> = emptyList(),
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val lastSyncTime: String? = null,
    val error: String? = null
) 