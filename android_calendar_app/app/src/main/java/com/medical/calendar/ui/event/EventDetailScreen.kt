package com.medical.calendar.ui.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.model.CalendarType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Long,
    viewModel: EventDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onEditEvent: (Long) -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val event by viewModel.event.collectAsState()
    
    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("事件詳情") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { event?.let { onEditEvent(it.id) } }) {
                        Icon(Icons.Default.Edit, contentDescription = "編輯")
                    }
                    IconButton(onClick = { viewModel.deleteEvent(context) }) {
                        Icon(Icons.Default.Delete, contentDescription = "刪除")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                event != null -> {
                    EventDetailContent(event = event!!)
                }
                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("事件不存在")
                    }
                }
            }
        }
    }
}

@Composable
fun EventDetailContent(event: CalendarEvent) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // 事件標題和顏色
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(android.graphics.Color.parseColor(event.color)))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = event.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 事件詳細資訊
        EventInfoSection(event = event)
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 日曆來源資訊
        CalendarSourceSection(event = event)
        
        if (event.description.isNotBlank()) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // 事件描述
            DescriptionSection(description = event.description)
        }
    }
}

@Composable
fun EventInfoSection(event: CalendarEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "事件資訊",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 日期
            InfoRow(
                icon = Icons.Default.DateRange,
                label = "日期",
                value = LocalDate.parse(event.date).format(
                    DateTimeFormatter.ofPattern("yyyy年MM月dd日 (E)", Locale.CHINESE)
                )
            )
            
            // 時間
            InfoRow(
                icon = Icons.Default.Schedule,
                label = "時間",
                value = "${event.startTime} - ${event.endTime}"
            )
            
            // 位置
            if (event.location.isNotBlank()) {
                InfoRow(
                    icon = Icons.Default.LocationOn,
                    label = "位置",
                    value = event.location
                )
            }
            
            // 重複規則
            if (event.recurrenceRule.isNotBlank()) {
                InfoRow(
                    icon = Icons.Default.Repeat,
                    label = "重複",
                    value = formatRecurrenceRule(event.recurrenceRule)
                )
            }
        }
    }
}

@Composable
fun CalendarSourceSection(event: CalendarEvent) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "日曆來源",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (event.calendarType) {
                        CalendarType.MEDICAL_SHIFT -> Icons.Default.LocalHospital
                        CalendarType.PERSONAL -> Icons.Default.Person
                        CalendarType.GOOGLE_CALENDAR -> Icons.Default.Event
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = when (event.calendarType) {
                            CalendarType.MEDICAL_SHIFT -> "醫療排班"
                            CalendarType.PERSONAL -> "個人日曆"
                            CalendarType.GOOGLE_CALENDAR -> "Google日曆"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Text(
                        text = event.calendarName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun DescriptionSection(description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "描述",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun formatRecurrenceRule(rule: String): String {
    return when {
        rule.contains("FREQ=DAILY") -> "每天"
        rule.contains("FREQ=WEEKLY") -> "每週"
        rule.contains("FREQ=MONTHLY") -> "每月"
        rule.contains("FREQ=YEARLY") -> "每年"
        else -> "自定義重複"
    }
} 