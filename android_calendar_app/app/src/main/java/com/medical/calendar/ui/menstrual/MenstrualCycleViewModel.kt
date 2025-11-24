package com.medical.calendar.ui.menstrual

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.calendar.data.model.MenstrualCycle
import com.medical.calendar.data.model.CycleSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MenstrualCycleViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(MenstrualCycleUiState())
    val uiState: StateFlow<MenstrualCycleUiState> = _uiState.asStateFlow()
    
    fun loadCycles() {
        viewModelScope.launch {
            // TODO: 從資料庫載入月經週期資料
            // 暫時使用測試資料
            val testCycles = listOf(
                MenstrualCycle(
                    id = 1L,
                    startDate = LocalDate.now().minusDays(30),
                    endDate = LocalDate.now().minusDays(26),
                    cycleLength = 28,
                    flowIntensity = com.medical.calendar.data.model.FlowIntensity.MEDIUM,
                    symptoms = listOf("腹痛", "疲勞"),
                    notes = "正常週期"
                ),
                MenstrualCycle(
                    id = 2L,
                    startDate = LocalDate.now().minusDays(58),
                    endDate = LocalDate.now().minusDays(54),
                    cycleLength = 28,
                    flowIntensity = com.medical.calendar.data.model.FlowIntensity.LIGHT,
                    symptoms = listOf("輕微腹痛"),
                    notes = "週期正常"
                )
            )
            
            val nextPredictedDate = predictNextCycle()
            
            _uiState.value = _uiState.value.copy(
                cycles = testCycles,
                nextPredictedDate = nextPredictedDate,
                isLoading = false
            )
        }
    }
    
    fun loadSettings() {
        viewModelScope.launch {
            // TODO: 從資料庫載入設定
            val settings = CycleSettings(
                averageCycleLength = 28,
                averagePeriodLength = 5,
                enablePredictions = true,
                enableNotifications = true,
                reminderDays = 3
            )
            _uiState.value = _uiState.value.copy(settings = settings)
        }
    }
    
    fun addCycle(cycle: MenstrualCycle) {
        viewModelScope.launch {
            // TODO: 儲存週期到資料庫
            val currentCycles = _uiState.value.cycles.toMutableList()
            currentCycles.add(cycle.copy(id = (currentCycles.maxOfOrNull { it.id } ?: 0) + 1))
            _uiState.value = _uiState.value.copy(cycles = currentCycles)
        }
    }
    
    fun updateCycle(cycle: MenstrualCycle) {
        viewModelScope.launch {
            // TODO: 更新週期到資料庫
            val currentCycles = _uiState.value.cycles.toMutableList()
            val index = currentCycles.indexOfFirst { it.id == cycle.id }
            if (index != -1) {
                currentCycles[index] = cycle
                _uiState.value = _uiState.value.copy(cycles = currentCycles)
            }
        }
    }
    
    fun deleteCycle(cycleId: Long) {
        viewModelScope.launch {
            // TODO: 從資料庫刪除週期
            val currentCycles = _uiState.value.cycles.toMutableList()
            currentCycles.removeAll { it.id == cycleId }
            _uiState.value = _uiState.value.copy(cycles = currentCycles)
        }
    }
    
    fun predictNextCycle(): LocalDate? {
        val cycles = _uiState.value.cycles
        if (cycles.size < 2) return null
        
        val averageCycleLength = cycles.map { it.cycleLength }.average().toInt()
        val lastCycle = cycles.maxByOrNull { it.startDate }
        
        return lastCycle?.startDate?.plusDays(averageCycleLength.toLong())
    }
}

data class MenstrualCycleUiState(
    val cycles: List<MenstrualCycle> = emptyList(),
    val settings: CycleSettings = CycleSettings(),
    val nextPredictedDate: LocalDate? = null,
    val isLoading: Boolean = true,
    val error: String? = null
) 