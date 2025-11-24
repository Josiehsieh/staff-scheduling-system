package com.medical.calendar.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSheetIdDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("設定") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
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
            // Google 帳號設定
            GoogleAccountSection(
                isSignedIn = uiState.isGoogleSignedIn,
                accountEmail = uiState.googleAccountEmail,
                accountName = uiState.googleAccountName,
                onSignIn = { viewModel.signInToGoogle { /* TODO: 啟動登入 */ } },
                onSignOut = { viewModel.signOutFromGoogle() }
            )
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            // Google Sheets 設定
            if (uiState.isGoogleSignedIn) {
                GoogleSheetsSection(
                    sheetId = uiState.googleSheetId,
                    sheetRange = uiState.googleSheetRange,
                    testResult = uiState.testConnectionResult,
                    onSetupSheets = { showSheetIdDialog = true },
                    onTestConnection = { viewModel.testGoogleSheetsConnection() }
                )
                
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }
            
            // 同步設定
            SyncSettingsSection(
                isAutoSync = uiState.isAutoSync,
                syncInterval = uiState.syncInterval,
                isSyncing = uiState.isSyncing,
                onAutoSyncChanged = { viewModel.setAutoSync(it) },
                onSyncIntervalChanged = { viewModel.setSyncInterval(it) },
                onManualSync = { viewModel.performManualSync() }
            )
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            // 日曆設定
            CalendarSettingsSection(
                calendars = uiState.calendars,
                onCalendarToggle = { calendarId, enabled -> 
                    viewModel.toggleCalendar(calendarId, enabled) 
                }
            )
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            // 顯示設定
            DisplaySettingsSection(
                showWeekNumbers = uiState.showWeekNumbers,
                startWeekOnMonday = uiState.startWeekOnMonday,
                showLunarDate = uiState.showLunarDate,
                onShowWeekNumbersChanged = { viewModel.setShowWeekNumbers(it) },
                onStartWeekOnMondayChanged = { viewModel.setStartWeekOnMonday(it) },
                onShowLunarDateChanged = { viewModel.setShowLunarDate(it) }
            )
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            // 關於
            AboutSection(
                appVersion = uiState.appVersion,
                lastSyncTime = uiState.lastSyncTime
            )
        }
    }
    
    // Sheet ID 設定對話框
    if (showSheetIdDialog) {
        SheetIdDialog(
            currentSheetId = uiState.googleSheetId,
            currentRange = uiState.googleSheetRange,
            onDismiss = { showSheetIdDialog = false },
            onConfirm = { sheetId, range ->
                viewModel.setGoogleSheetId(sheetId, range)
                showSheetIdDialog = false
            }
        )
    }
}

@Composable
fun GoogleAccountSection(
    isSignedIn: Boolean,
    accountEmail: String?,
    accountName: String?,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Google 帳號",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isSignedIn) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = accountName ?: "Google 使用者",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = accountEmail ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Button(onClick = onSignOut) {
                        Text("登出")
                    }
                }
            } else {
                Button(
                    onClick = onSignIn,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("登入 Google 帳號")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "登入後即可使用 Google Sheets 排班同步和 Google Calendar 功能",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun GoogleSheetsSection(
    sheetId: String,
    sheetRange: String,
    testResult: String?,
    onSetupSheets: () -> Unit,
    onTestConnection: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Google Sheets 設定",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (sheetId.isNotEmpty()) {
                Text(
                    text = "Sheet ID",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = sheetId,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "資料範圍",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = sheetRange,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onSetupSheets,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("編輯")
                    }
                    Button(
                        onClick = onTestConnection,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("測試連線")
                    }
                }
                
                if (testResult != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = testResult,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (testResult.startsWith("✅")) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.error
                    )
                }
            } else {
                Text(
                    text = "尚未設定 Google Sheets",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onSetupSheets,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("設定 Google Sheets")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetIdDialog(
    currentSheetId: String,
    currentRange: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var sheetId by remember { mutableStateOf(currentSheetId) }
    var sheetRange by remember { mutableStateOf(currentRange) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("設定 Google Sheets") },
        text = {
            Column {
                Text(
                    text = "請輸入 Google Sheets 的 Sheet ID 和資料範圍",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = sheetId,
                    onValueChange = { sheetId = it },
                    label = { Text("Sheet ID") },
                    placeholder = { Text("1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = sheetRange,
                    onValueChange = { sheetRange = it },
                    label = { Text("資料範圍") },
                    placeholder = { Text("排班資料!A2:G") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "💡 Sheet ID 可從 Google Sheets 網址中取得",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(sheetId, sheetRange) },
                enabled = sheetId.isNotEmpty()
            ) {
                Text("確定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
fun SyncSettingsSection(
    isAutoSync: Boolean,
    syncInterval: Int,
    isSyncing: Boolean = false,
    onAutoSyncChanged: (Boolean) -> Unit,
    onSyncIntervalChanged: (Int) -> Unit,
    onManualSync: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "同步設定",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 自動同步開關
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Sync,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("自動同步")
                }
                Switch(
                    checked = isAutoSync,
                    onCheckedChange = onAutoSyncChanged
                )
            }
            
            if (isAutoSync) {
                Spacer(modifier = Modifier.height(12.dp))
                
                // 同步間隔選擇
                Text(
                    text = "同步間隔",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(15, 30, 60, 120).forEach { minutes ->
                        FilterChip(
                            selected = syncInterval == minutes,
                            onClick = { onSyncIntervalChanged(minutes) },
                            label = { Text("${minutes}分鐘") }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 手動同步按鈕
            Button(
                onClick = onManualSync,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Sync, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("立即同步")
            }
        }
    }
}

@Composable
fun CalendarSettingsSection(
    calendars: List<CalendarSetting>,
    onCalendarToggle: (Long, Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "日曆設定",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            calendars.forEach { calendar ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when (calendar.type) {
                                "MEDICAL_SHIFT" -> Icons.Default.LocalHospital
                                "PERSONAL" -> Icons.Default.Person
                                "GOOGLE_CALENDAR" -> Icons.Default.Event
                                else -> Icons.Default.CalendarToday
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = calendar.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = calendar.type,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = calendar.isEnabled,
                        onCheckedChange = { onCalendarToggle(calendar.id, it) }
                    )
                }
            }
        }
    }
}

@Composable
fun DisplaySettingsSection(
    showWeekNumbers: Boolean,
    startWeekOnMonday: Boolean,
    showLunarDate: Boolean,
    onShowWeekNumbersChanged: (Boolean) -> Unit,
    onStartWeekOnMondayChanged: (Boolean) -> Unit,
    onShowLunarDateChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "顯示設定",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 顯示週數
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.ViewWeek,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("顯示週數")
                }
                Switch(
                    checked = showWeekNumbers,
                    onCheckedChange = onShowWeekNumbersChanged
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 週一為第一天
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("週一為第一天")
                }
                Switch(
                    checked = startWeekOnMonday,
                    onCheckedChange = onStartWeekOnMondayChanged
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 顯示農曆日期
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("顯示農曆日期")
                }
                Switch(
                    checked = showLunarDate,
                    onCheckedChange = onShowLunarDateChanged
                )
            }
        }
    }
}

@Composable
fun AboutSection(
    appVersion: String,
    lastSyncTime: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "關於",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("版本")
                Text(appVersion)
            }
            
            if (lastSyncTime != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("上次同步")
                    Text(lastSyncTime)
                }
            }
        }
    }
}

data class CalendarSetting(
    val id: Long,
    val name: String,
    val type: String,
    val isEnabled: Boolean
) 