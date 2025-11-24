package com.medical.calendar.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medical.calendar.R
import com.medical.calendar.ui.theme.MedicalCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarWidgetConfigureActivity : ComponentActivity() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set the result to CANCELED. This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)
        
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        // If this activity was started with an invalid widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContent {
            MedicalCalendarTheme {
                WidgetConfigureScreen(
                    onConfigureComplete = { configureWidget() }
                )
            }
        }
    }

    private fun configureWidget() {
        // Save widget configuration
        getSharedPreferences("widget_prefs", MODE_PRIVATE).edit().apply {
            putBoolean("widget_configured_$appWidgetId", true)
            apply()
        }

        // Update the widget
        val appWidgetManager = AppWidgetManager.getInstance(this)
        CalendarWidgetProvider.updateAllWidgets(this)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}

@Composable
fun WidgetConfigureScreen(
    onConfigureComplete: () -> Unit
) {
    var showEventsCount by remember { mutableStateOf(true) }
    var showLocation by remember { mutableStateOf(true) }
    var showTime by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "行事曆小工具設定",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 20.sp
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "顯示選項",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("顯示事件數量")
                    Switch(
                        checked = showEventsCount,
                        onCheckedChange = { showEventsCount = it }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("顯示地點")
                    Switch(
                        checked = showLocation,
                        onCheckedChange = { showLocation = it }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("顯示時間")
                    Switch(
                        checked = showTime,
                        onCheckedChange = { showTime = it }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onConfigureComplete,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("完成設定")
        }
    }
} 