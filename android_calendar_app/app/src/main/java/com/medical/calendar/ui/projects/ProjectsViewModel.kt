package com.medical.calendar.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.calendar.data.model.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()
    
    fun loadProjects() {
        viewModelScope.launch {
            // TODO: 從資料庫載入專案資料
            // 暫時使用測試資料
            val testProjects = listOf(
                Project(
                    id = 1L,
                    name = "健康管理系統開發",
                    description = "開發醫院健康管理系統",
                    status = com.medical.calendar.data.model.ProjectStatus.IN_PROGRESS,
                    priority = com.medical.calendar.data.model.ProjectPriority.HIGH,
                    color = "#FF5722"
                ),
                Project(
                    id = 2L,
                    name = "排班系統整合",
                    description = "整合現有排班系統",
                    status = com.medical.calendar.data.model.ProjectStatus.PLANNING,
                    priority = com.medical.calendar.data.model.ProjectPriority.MEDIUM,
                    color = "#2196F3"
                )
            )
            _uiState.value = _uiState.value.copy(
                projects = testProjects,
                isLoading = false
            )
        }
    }
    
    fun addProject(project: Project) {
        viewModelScope.launch {
            // TODO: 儲存專案到資料庫
            val currentProjects = _uiState.value.projects.toMutableList()
            currentProjects.add(project.copy(id = (currentProjects.maxOfOrNull { it.id } ?: 0) + 1))
            _uiState.value = _uiState.value.copy(projects = currentProjects)
        }
    }
    
    fun updateProject(project: Project) {
        viewModelScope.launch {
            // TODO: 更新專案到資料庫
            val currentProjects = _uiState.value.projects.toMutableList()
            val index = currentProjects.indexOfFirst { it.id == project.id }
            if (index != -1) {
                currentProjects[index] = project
                _uiState.value = _uiState.value.copy(projects = currentProjects)
            }
        }
    }
    
    fun deleteProject(projectId: Long) {
        viewModelScope.launch {
            // TODO: 從資料庫刪除專案
            val currentProjects = _uiState.value.projects.toMutableList()
            currentProjects.removeAll { it.id == projectId }
            _uiState.value = _uiState.value.copy(projects = currentProjects)
        }
    }
}

data class ProjectsUiState(
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) 