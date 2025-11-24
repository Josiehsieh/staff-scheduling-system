package com.medical.calendar.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import com.medical.calendar.util.LunarCalendarUtil
import kotlinx.datetime.toLocalDate

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    onNavigateToEventDetail: (Long) -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val showLunarDate by viewModel.showLunarDate.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadEvents()
        viewModel.loadCalendars()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 頂部導航欄
        CalendarTopBar(
            currentDate = currentDate,
            onPreviousMonth = { viewModel.previousMonth() },
            onNextMonth = { viewModel.nextMonth() },
            onToday = { viewModel.goToToday() },
            onSettings = onNavigateToSettings
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 同步狀態指示器
        SyncStatusIndicator(
            isSyncing = uiState.isSyncing,
            lastSyncTime = uiState.lastSyncTime
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 日曆網格
        CalendarGrid(
            currentDate = currentDate,
            selectedDate = selectedDate,
            events = uiState.events,
            onDateSelected = { viewModel.selectDate(it) },
            showLunarDate = showLunarDate
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 選中日期的詳細事件列表
        if (selectedDate != null) {
            SelectedDateEvents(
                date = selectedDate!!,
                events = uiState.events.filter { 
                    LocalDate.parse(it.date) == selectedDate 
                },
                onEventClick = { onNavigateToEventDetail(it.id) }
            )
        }
    }
}

@Composable
fun CalendarTopBar(
    currentDate: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onToday: () -> Unit,
    onSettings: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "上個月")
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = currentDate.format(DateTimeFormatter.ofPattern("yyyy年MM月")),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onToday) {
                Text("今天")
            }
        }
        
        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.ChevronRight, contentDescription = "下個月")
        }
        
        IconButton(onClick = onSettings) {
            Icon(Icons.Default.Settings, contentDescription = "設定")
        }
    }
}

@Composable
fun SyncStatusIndicator(
    isSyncing: Boolean,
    lastSyncTime: String?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSyncing) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("同步中...", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
        } else {
            Icon(
                Icons.Default.Sync,
                contentDescription = "同步狀態",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = lastSyncTime?.let { "上次同步: $it" } ?: "尚未同步",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CalendarGrid(
    currentDate: LocalDate,
    selectedDate: LocalDate?,
    events: List<CalendarEvent>,
    onDateSelected: (LocalDate) -> Unit,
    showLunarDate: Boolean = true
) {
    val firstDayOfMonth = currentDate.withDayOfMonth(1)
    val lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth())
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 週日為0
    
    Column {
        // 星期標題
        Row(modifier = Modifier.fillMaxWidth()) {
            val weekDays = listOf("日", "一", "二", "三", "四", "五", "六")
            weekDays.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 日曆網格
        var currentRow = 0
        var currentDay = 1
        
        repeat(6) { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(7) { dayOfWeek ->
                    val dayNumber = if (week == 0 && dayOfWeek < firstDayOfWeek) {
                        null // 空白
                    } else if (currentDay > lastDayOfMonth.dayOfMonth) {
                        null // 超出當月
                    } else {
                        currentDay++
                    }
                    
                    val date = if (dayNumber != null) {
                        currentDate.withDayOfMonth(dayNumber)
                    } else null
                    
                    val isSelected = date == selectedDate
                    val dayEvents = if (date != null) {
                        events.filter { LocalDate.parse(it.date) == date }
                    } else emptyList()
                    
                    CalendarDay(
                        dayNumber = dayNumber,
                        isSelected = isSelected,
                        isCurrentMonth = date?.month == currentDate.month,
                        events = dayEvents,
                        onClick = { date?.let { onDateSelected(it) } },
                        currentDate = currentDate,
                        showLunarDate = showLunarDate
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun CalendarDay(
    dayNumber: Int?,
    isSelected: Boolean,
    isCurrentMonth: Boolean,
    events: List<CalendarEvent>,
    onClick: () -> Unit,
    currentDate: LocalDate,
    showLunarDate: Boolean = true
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .padding(1.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.primary
                    else -> Color.Transparent
                }
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(4.dp)
        ) {
            if (dayNumber != null) {
                Text(
                    text = dayNumber.toString(),
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = when {
                        isSelected -> MaterialTheme.colorScheme.onPrimary
                        isCurrentMonth -> MaterialTheme.colorScheme.onSurface
                        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    }
                )
                
                // 農曆日期顯示
                if (showLunarDate) {
                    val date = currentDate.withDayOfMonth(dayNumber)
                    val lunarDate = LunarCalendarUtil.getLunarDisplayText(date.toLocalDate())
                    Text(
                        text = lunarDate,
                        fontSize = 10.sp,
                        color = when {
                            isSelected -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant
                            else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                        }
                    )
                }
                
                // 事件指示器
                if (events.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        events.take(3).forEach { event ->
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(
                                        Color(android.graphics.Color.parseColor(event.color))
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedDateEvents(
    date: LocalDate,
    events: List<CalendarEvent>,
    onEventClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = date.format(DateTimeFormatter.ofPattern("M月d日 (E)", Locale.CHINESE)),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            if (events.isEmpty()) {
                Text(
                    text = "當日無事件",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(events.sortedBy { it.startTime }) { event ->
                        EventItem(
                            event = event,
                            onClick = { onEventClick(event.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventItem(
    event: CalendarEvent,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 顏色指示器
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(android.graphics.Color.parseColor(event.color)))
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = "${event.startTime} - ${event.endTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (event.description.isNotBlank()) {
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "查看詳情",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
} 