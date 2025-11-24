package com.medical.calendar.ui.debug

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medical.calendar.util.NetworkUtil
import kotlinx.coroutines.launch

@Composable
fun NetworkDiagnosticScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var networkStatus by remember { mutableStateOf("檢查中...") }
    var supabaseStatus by remember { mutableStateOf("檢查中...") }
    var networkType by remember { mutableStateOf("") }
    var isChecking by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        performNetworkCheck()
    }

    fun performNetworkCheck() {
        scope.launch {
            isChecking = true
            
            // 檢查網路連線
            val isNetworkAvailable = NetworkUtil.isNetworkAvailable(context)
            networkStatus = if (isNetworkAvailable) "✅ 網路連線正常" else "❌ 網路連線失敗"
            
            // 獲取網路類型
            networkType = NetworkUtil.getNetworkType(context)
            
            // 測試Supabase連線
            val isSupabaseAvailable = NetworkUtil.testSupabaseConnection()
            supabaseStatus = if (isSupabaseAvailable) "✅ Supabase連線正常" else "❌ Supabase連線失敗"
            
            isChecking = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "網路診斷工具",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "網路狀態",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text("網路連線: $networkStatus")
                Text("網路類型: $networkType")
                Text("Supabase連線: $supabaseStatus")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "常見問題解決方案",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text("1. 檢查WiFi或行動網路是否開啟")
                Text("2. 確認網路連線穩定")
                Text("3. 檢查防火牆設定")
                Text("4. 嘗試重新啟動應用程式")
                Text("5. 檢查Supabase服務狀態")
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Supabase連線資訊",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Text("URL: https://litnrtwcihcvpxpfufeg.supabase.co")
                Text("API Key: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                Text("狀態: $supabaseStatus")
            }
        }

        Button(
            onClick = { performNetworkCheck() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isChecking
        ) {
            if (isChecking) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (isChecking) "檢查中..." else "重新檢查網路")
        }

        if (networkStatus.contains("❌") || supabaseStatus.contains("❌")) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "⚠️ 網路問題檢測",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    
                    Text(
                        text = "檢測到網路連線問題，請檢查：",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    
                    if (networkStatus.contains("❌")) {
                        Text(
                            text = "• 網路連線已斷開，請檢查WiFi或行動網路",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    
                    if (supabaseStatus.contains("❌")) {
                        Text(
                            text = "• Supabase服務無法連線，可能是網路問題或服務暫時不可用",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
} 