package com.medical.calendar.ui.projects

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.medical.calendar.data.model.Project
import com.medical.calendar.data.model.ProjectPriority
import com.medical.calendar.data.model.ProjectStatus
import com.medical.calendar.util.ColorManager
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    viewModel: ProjectsViewModel = hiltViewModel(),
    onNavigateToProjectDetail: (Long) -> Unit = {},
    onNavigateToProjectEdit: (Long?) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadProjects()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("專案管理") },
                actions = {
                    IconButton(onClick = { onNavigateToProjectEdit(null) }) {
                        Icon(Icons.Default.Add, contentDescription = "新增專案")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 統計卡片
            ProjectsStats(projects = uiState.projects)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 專案列表
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.projects) { project ->
                    ProjectCard(
                        project = project,
                        onClick = { onNavigateToProjectDetail(project.id) },
                        onEdit = { onNavigateToProjectEdit(project.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectsStats(projects: List<Project>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatCard(
            title = "總專案",
            value = projects.size.toString(),
            icon = Icons.Default.Assignment,
            color = MaterialTheme.colorScheme.primary
        )
        
        StatCard(
            title = "進行中",
            value = projects.count { it.status == ProjectStatus.IN_PROGRESS }.toString(),
            icon = Icons.Default.PlayArrow,
            color = MaterialTheme.colorScheme.secondary
        )
        
        StatCard(
            title = "已完成",
            value = projects.count { it.status == ProjectStatus.COMPLETED }.toString(),
            icon = Icons.Default.CheckCircle,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier.weight(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProjectCard(
    project: Project,
    onClick: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (project.description.isNotBlank()) {
                        Text(
                            text = project.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                }
                
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "編輯專案",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 專案資訊
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 狀態標籤
                StatusChip(status = project.status)
                
                // 優先級標籤
                PriorityChip(priority = project.priority)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 進度條
            LinearProgressIndicator(
                progress = project.progress / 100f,
                modifier = Modifier.fillMaxWidth(),
                color = ColorManager.hexToColor(project.color),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "${project.progress}% 完成",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 日期資訊
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "開始: ${project.startDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                project.endDate?.let { endDate ->
                    Text(
                        text = "結束: ${endDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: ProjectStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        ProjectStatus.NOT_STARTED -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "未開始"
        )
        ProjectStatus.IN_PROGRESS -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            "進行中"
        )
        ProjectStatus.ON_HOLD -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "暫停"
        )
        ProjectStatus.COMPLETED -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            "已完成"
        )
        ProjectStatus.CANCELLED -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            "已取消"
        )
    }
    
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}

@Composable
fun PriorityChip(priority: ProjectPriority) {
    val (backgroundColor, textColor, text) = when (priority) {
        ProjectPriority.LOW -> Triple(
            Color(0xFFE8F5E8),
            Color(0xFF2E7D32),
            "低"
        )
        ProjectPriority.MEDIUM -> Triple(
            Color(0xFFFFF3E0),
            Color(0xFFF57C00),
            "中"
        )
        ProjectPriority.HIGH -> Triple(
            Color(0xFFFFEBEE),
            Color(0xFFD32F2F),
            "高"
        )
        ProjectPriority.URGENT -> Triple(
            Color(0xFFFCE4EC),
            Color(0xFFC2185B),
            "緊急"
        )
    }
    
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
} 