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
import androidx.hilt.navigation.compose.hiltViewModel
import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.model.CalendarType
import com.medical.calendar.util.ColorManager
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEditScreen(
    eventId: Long? = null,
    viewModel: EventEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val event by viewModel.event.collectAsState()
    
    LaunchedEffect(eventId) {
        if (eventId != null) {
            viewModel.loadEvent(eventId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (eventId == null) "新增事件" else "編輯事件") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { 
                            viewModel.saveEvent(context)
                            onNavigateBack()
                        },
                        enabled = uiState.isValid
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "儲存")
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
            EventEditForm(
                event = event,
                onEventChange = { viewModel.updateEvent(it) },
                calendars = uiState.calendars,
                selectedCalendarId = uiState.selectedCalendarId,
                onCalendarSelected = { viewModel.selectCalendar(it) }
            )
        }
    }
}

@Composable
fun EventEditForm(
    event: CalendarEvent?,
    onEventChange: (CalendarEvent) -> Unit,
    calendars: List<com.medical.calendar.data.model.Calendar>,
    selectedCalendarId: Long?,
    onCalendarSelected: (Long) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // 事件標題
        OutlinedTextField(
            value = event?.title ?: "",
            onValueChange = { 
                event?.let { onEventChange(it.copy(title = it)) }
            },
            label = { Text("事件標題") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 日期選擇
        DatePickerSection(
            selectedDate = event?.date?.let { LocalDate.parse(it) } ?: LocalDate.now(),
            onDateSelected = { date ->
                event?.let { onEventChange(it.copy(date = date.toString())) }
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 時間選擇
        TimePickerSection(
            startTime = event?.startTime ?: "09:00",
            endTime = event?.endTime ?: "10:00",
            onStartTimeChange = { time ->
                event?.let { onEventChange(it.copy(startTime = time)) }
            },
            onEndTimeChange = { time ->
                event?.let { onEventChange(it.copy(endTime = time)) }
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 位置
        OutlinedTextField(
            value = event?.location ?: "",
            onValueChange = { 
                event?.let { onEventChange(it.copy(location = it)) }
            },
            label = { Text("位置") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 描述
        OutlinedTextField(
            value = event?.description ?: "",
            onValueChange = { 
                event?.let { onEventChange(it.copy(description = it)) }
            },
            label = { Text("描述") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 日曆選擇
        CalendarSelectionSection(
            calendars = calendars,
            selectedCalendarId = selectedCalendarId,
            onCalendarSelected = onCalendarSelected
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 顏色選擇
        ColorSelectionSection(
            selectedColor = event?.color ?: "#667eea",
            onColorSelected = { color ->
                event?.let { onEventChange(it.copy(color = color)) }
            },
            calendarType = event?.calendarType ?: CalendarType.PERSONAL
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 重複選項
        RecurrenceSection(
            recurrenceRule = event?.recurrenceRule ?: "",
            onRecurrenceChange = { rule ->
                event?.let { onEventChange(it.copy(recurrenceRule = rule)) }
            }
        )
    }
}

@Composable
fun DatePickerSection(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    
    OutlinedTextField(
        value = selectedDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
        onValueChange = { },
        label = { Text("日期") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.DateRange, contentDescription = "選擇日期")
            }
        }
    )
    
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("確定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("取消")
                }
            }
        ) {
            // 這裡應該使用 DatePicker，但為了簡化，我們使用基本的日期選擇邏輯
            Text("日期選擇器")
        }
    }
}

@Composable
fun TimePickerSection(
    startTime: String,
    endTime: String,
    onStartTimeChange: (String) -> Unit,
    onEndTimeChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = startTime,
            onValueChange = onStartTimeChange,
            label = { Text("開始時間") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        
        OutlinedTextField(
            value = endTime,
            onValueChange = onEndTimeChange,
            label = { Text("結束時間") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )
    }
}

@Composable
fun CalendarSelectionSection(
    calendars: List<com.medical.calendar.data.model.Calendar>,
    selectedCalendarId: Long?,
    onCalendarSelected: (Long) -> Unit
) {
    Column {
        Text(
            text = "選擇日曆",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        calendars.forEach { calendar ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedCalendarId == calendar.id,
                    onClick = { onCalendarSelected(calendar.id) }
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Icon(
                    imageVector = when (calendar.type) {
                        CalendarType.MEDICAL_SHIFT -> Icons.Default.LocalHospital
                        CalendarType.PERSONAL -> Icons.Default.Person
                        CalendarType.GOOGLE_CALENDAR -> Icons.Default.Event
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = calendar.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ColorSelectionSection(
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    calendarType: CalendarType = CalendarType.PERSONAL
) {
    // 根據日曆類型獲取可用的顏色
    val colors = ColorManager.getAvailableColors(calendarType)
    
    Column {
        Text(
            text = "選擇顏色",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colors.size) { index ->
                val color = colors[index]
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(android.graphics.Color.parseColor(color)))
                        .clickable { onColorSelected(color) }
                        .then(
                            if (selectedColor == color) {
                                Modifier.border(
                                    width = 3.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(20.dp)
                                )
                            } else {
                                Modifier
                            }
                        )
                )
            }
        }
    }
}

@Composable
fun RecurrenceSection(
    recurrenceRule: String,
    onRecurrenceChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        Text(
            text = "重複",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = when {
                recurrenceRule.contains("FREQ=DAILY") -> "每天"
                recurrenceRule.contains("FREQ=WEEKLY") -> "每週"
                recurrenceRule.contains("FREQ=MONTHLY") -> "每月"
                recurrenceRule.contains("FREQ=YEARLY") -> "每年"
                else -> "不重複"
            },
            onValueChange = { },
            label = { Text("重複規則") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "展開"
                    )
                }
            }
        )
        
        if (expanded) {
            Column(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            ) {
                listOf(
                    "不重複" to "",
                    "每天" to "FREQ=DAILY",
                    "每週" to "FREQ=WEEKLY",
                    "每月" to "FREQ=MONTHLY",
                    "每年" to "FREQ=YEARLY"
                ).forEach { (label, rule) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = recurrenceRule == rule,
                            onClick = { onRecurrenceChange(rule) }
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
} 