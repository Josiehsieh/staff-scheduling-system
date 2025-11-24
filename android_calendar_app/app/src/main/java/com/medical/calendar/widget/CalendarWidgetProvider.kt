package com.medical.calendar.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RemoteViews
import com.medical.calendar.MainActivity
import com.medical.calendar.R
import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.repository.CalendarRepository
import com.medical.calendar.ui.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class CalendarWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var calendarRepository: CalendarRepository

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_calendar)
        
        // Set up click listeners
        setupClickListeners(context, views, appWidgetId)
        
        // Set up the list view
        val intent = Intent(context, CalendarWidgetService::class.java).apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            data = Uri.fromParts("content", appWidgetId.toString(), null)
        }
        views.setRemoteAdapter(R.id.widget_events_list, intent)
        views.setEmptyView(R.id.widget_events_list, R.id.widget_empty_text)
        
        // Update widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
        
        // Load today's events
        loadTodayEvents(context, views, appWidgetManager, appWidgetId)
    }

    private fun setupClickListeners(
        context: Context,
        views: RemoteViews,
        appWidgetId: Int
    ) {
        // Header click to open app
        val headerIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val headerPendingIntent = PendingIntent.getActivity(
            context, 0, headerIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_header, headerPendingIntent)

        // Add event button
        val addEventIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to", Screen.EventEdit.route.replace("{eventId}", "new"))
        }
        val addEventPendingIntent = PendingIntent.getActivity(
            context, 1, addEventIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_add_event, addEventPendingIntent)

        // Refresh button
        val refreshIntent = Intent(context, CalendarWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
        }
        val refreshPendingIntent = PendingIntent.getBroadcast(
            context, 2, refreshIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_refresh, refreshPendingIntent)
    }

    private fun loadTodayEvents(
        context: Context,
        views: RemoteViews,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val today = LocalDate.now()
                val events = calendarRepository.getEventsForDate(today)
                
                // Update header with today's date
                val dateFormatter = DateTimeFormatter.ofPattern("MM/dd (E)")
                val todayFormatted = today.format(dateFormatter)
                
                // Update on main thread
                CoroutineScope(Dispatchers.Main).launch {
                    views.setTextViewText(R.id.widget_date, todayFormatted)
                    views.setTextViewText(R.id.widget_event_count, "${events.size} 個事件")
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            } catch (e: Exception) {
                // Handle error silently for widget
            }
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        updateWidget(context, appWidgetManager, appWidgetId)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        
        when (intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)
                appWidgetIds?.let { ids ->
                    onUpdate(context, AppWidgetManager.getInstance(context), ids)
                }
            }
        }
    }

    companion object {
        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(context, CalendarWidgetProvider::class.java)
            )
            if (appWidgetIds.isNotEmpty()) {
                val intent = Intent(context, CalendarWidgetProvider::class.java).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                }
                context.sendBroadcast(intent)
            }
        }
    }
} 