package com.medical.calendar.ui.menstrual

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.medical.calendar.data.model.MenstrualCycle
import com.medical.calendar.data.model.FlowIntensity
import com.medical.calendar.util.ColorManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenstrualCycleScreen(
    viewModel: MenstrualCycleViewModel = hiltViewModel(),
    onNavigateToCycleEdit: (Long?) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadCycles()
        viewModel.loadSettings()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("月經週期") },
                actions = {
                    IconButton(onClick = { onNavigateToCycleEdit(null) }) {
                        Icon(Icons.Default.Add, contentDescription = "新增記錄")
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
            // 統計資訊
            CycleStats(
                cycles = uiState.cycles,
                settings = uiState.settings
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 預測資訊
            if (uiState.settings.enablePredictions) {
                PredictionCard(
                    nextPredictedDate = uiState.nextPredictedDate,
                    onNavigateToCycleEdit = onNavigateToCycleEdit
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // 週期記錄列表
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.cycles.sortedByDescending { it.startDate }) { cycle ->
                    CycleCard(
                        cycle = cycle,
                        onClick = { onNavigateToCycleEdit(cycle.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CycleStats(
    cycles: List<MenstrualCycle>,
    settings: CycleSettings?
) {
    val averageCycleLength = if (cycles.size > 1) {
        cycles.zipWithNext { a, b ->
            java.time.temporal.ChronoUnit.DAYS.between(a.startDate, b.startDate)
        }.average().toInt()
    } else {
        settings?.averageCycleLength ?: 28
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatCard(
            title = "平均週期",
            value = "${averageCycleLength}天",
            icon = Icons.Default.Schedule,
            color = Color(0xFFE91E63)
        )
        
        StatCard(
            title = "記錄次數",
            value = cycles.size.toString(),
            icon = Icons.Default.History,
            color = Color(0xFF9C27B0)
        )
        
        StatCard(
            title = "平均經期",
            value = "${settings?.averagePeriodLength ?: 5}天",
            icon = Icons.Default.Today,
            color = Color(0xFFF44336)
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
fun PredictionCard(
    nextPredictedDate: LocalDate?,
    onNavigateToCycleEdit: (Long?) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFCE4EC)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "預測",
                        tint = Color(0xFFE91E63),
                        modifier = Modifier.size(20.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "下次預測",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE91E63)
                    )
                }
                
                TextButton(
                    onClick = { onNavigateToCycleEdit(null) }
                ) {
                    Text("記錄開始")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (nextPredictedDate != null) {
                val daysUntil = java.time.temporal.ChronoUnit.DAYS.between(
                    LocalDate.now(), nextPredictedDate
                )
                
                Text(
                    text = nextPredictedDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = if (daysUntil > 0) "還有 $daysUntil 天" else "今天",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "需要更多記錄來預測",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CycleCard(
    cycle: MenstrualCycle,
    onClick: () -> Unit
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
                        text = cycle.startDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (cycle.endDate != null) {
                        val duration = java.time.temporal.ChronoUnit.DAYS.between(
                            cycle.startDate, cycle.endDate
                        )
                        Text(
                            text = "持續 ${duration + 1} 天",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                if (cycle.isPredicted) {
                    Surface(
                        color = Color(0xFFFFF3E0),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "預測",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFF57C00)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 流量強度
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "流量: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                FlowIntensityIndicator(intensity = cycle.flowIntensity)
            }
            
            // 症狀和備註
            if (cycle.symptoms.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "症狀: ${cycle.symptoms}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            if (cycle.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "備註: ${cycle.notes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun FlowIntensityIndicator(intensity: FlowIntensity) {
    val (color, text) = when (intensity) {
        FlowIntensity.LIGHT -> Pair(Color(0xFF4CAF50), "輕")
        FlowIntensity.MEDIUM -> Pair(Color(0xFFFF9800), "中")
        FlowIntensity.HEAVY -> Pair(Color(0xFFF44336), "重")
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        
        Spacer(modifier = Modifier.width(4.dp))
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
} 