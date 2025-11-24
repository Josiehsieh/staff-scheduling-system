package com.medical.calendar.ui.event

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.repository.CalendarRepository
import com.medical.calendar.widget.CalendarWidgetProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: CalendarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    private val _event = MutableStateFlow<CalendarEvent?>(null)
    val event: StateFlow<CalendarEvent?> = _event.asStateFlow()

    fun loadEvent(eventId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val event = repository.getEventById(eventId)
                _event.value = event
                _uiState.update { 
                    it.copy(
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

    fun deleteEvent(context: Context? = null) {
        viewModelScope.launch {
            val currentEvent = _event.value ?: return@launch
            
            _uiState.update { it.copy(isDeleting = true) }
            
            try {
                repository.deleteEvent(currentEvent.id)
                
                // 更新小工具
                context?.let { CalendarWidgetProvider.updateAllWidgets(it) }
                
                _uiState.update { 
                    it.copy(
                        isDeleting = false,
                        isDeleted = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isDeleting = false,
                        error = e.message ?: "刪除事件失敗"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetDeleteState() {
        _uiState.update { it.copy(isDeleted = false) }
    }
}

data class EventDetailUiState(
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val isDeleted: Boolean = false,
    val error: String? = null
) 