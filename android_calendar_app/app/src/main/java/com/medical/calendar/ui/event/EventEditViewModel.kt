package com.medical.calendar.ui.event

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.model.CalendarType
import com.medical.calendar.data.repository.CalendarRepository
import com.medical.calendar.widget.CalendarWidgetProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EventEditViewModel @Inject constructor(
    private val repository: CalendarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventEditUiState())
    val uiState: StateFlow<EventEditUiState> = _uiState.asStateFlow()

    private val _event = MutableStateFlow<CalendarEvent?>(null)
    val event: StateFlow<CalendarEvent?> = _event.asStateFlow()

    init {
        loadCalendars()
        createNewEvent()
    }

    fun loadEvent(eventId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val event = repository.getEventById(eventId)
                _event.value = event
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        selectedCalendarId = event.calendarId,
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

    private fun createNewEvent() {
        val newEvent = CalendarEvent(
            id = 0,
            title = "",
            date = LocalDate.now().toString(),
            startTime = "09:00",
            endTime = "10:00",
            description = "",
            location = "",
            color = "#667eea",
            calendarId = 1, // 預設為個人日曆
            calendarName = "個人日曆",
            calendarType = CalendarType.PERSONAL,
            recurrenceRule = "",
            isAllDay = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        _event.value = newEvent
    }

    fun updateEvent(updatedEvent: CalendarEvent) {
        _event.value = updatedEvent
        validateEvent()
    }

    fun selectCalendar(calendarId: Long) {
        _uiState.update { it.copy(selectedCalendarId = calendarId) }
        
        // 更新事件的日曆資訊
        val calendar = uiState.value.calendars.find { it.id == calendarId }
        calendar?.let { cal ->
            _event.value = _event.value?.copy(
                calendarId = cal.id,
                calendarName = cal.name,
                calendarType = cal.type
            )
        }
        
        validateEvent()
    }

    fun saveEvent(context: Context? = null) {
        viewModelScope.launch {
            val currentEvent = _event.value ?: return@launch
            
            _uiState.update { it.copy(isSaving = true) }
            
            try {
                if (currentEvent.id == 0L) {
                    // 新增事件
                    repository.addEvent(currentEvent)
                } else {
                    // 更新事件
                    repository.updateEvent(currentEvent)
                }
                
                // 更新小工具
                context?.let { CalendarWidgetProvider.updateAllWidgets(it) }
                
                _uiState.update { 
                    it.copy(
                        isSaving = false,
                        isSaved = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isSaving = false,
                        error = e.message ?: "儲存事件失敗"
                    )
                }
            }
        }
    }

    private fun loadCalendars() {
        viewModelScope.launch {
            try {
                val calendars = repository.getAllCalendars()
                _uiState.update { it.copy(calendars = calendars) }
                
                // 如果沒有選中日曆，預設選擇第一個
                if (uiState.value.selectedCalendarId == null && calendars.isNotEmpty()) {
                    selectCalendar(calendars.first().id)
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "載入日曆失敗")
                }
            }
        }
    }

    private fun validateEvent() {
        val currentEvent = _event.value
        val isValid = currentEvent != null && 
                     currentEvent.title.isNotBlank() &&
                     currentEvent.startTime.isNotBlank() &&
                     currentEvent.endTime.isNotBlank() &&
                     uiState.value.selectedCalendarId != null
        
        _uiState.update { it.copy(isValid = isValid) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetSaveState() {
        _uiState.update { it.copy(isSaved = false) }
    }
}

data class EventEditUiState(
    val calendars: List<com.medical.calendar.data.model.Calendar> = emptyList(),
    val selectedCalendarId: Long? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isValid: Boolean = false,
    val error: String? = null
) 