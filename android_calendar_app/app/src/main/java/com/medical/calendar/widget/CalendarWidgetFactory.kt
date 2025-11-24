package com.medical.calendar.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
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
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class CalendarWidgetFactory(
    private val context: Context,
    private val intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

    @Inject
    lateinit var calendarRepository: CalendarRepository

    private var events: List<CalendarEvent> = emptyList()
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun onCreate() {
        // Initialize
    }

    override fun onDataSetChanged() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val today = LocalDate.now()
                events = calendarRepository.getEventsForDate(today)
                    .sortedBy { it.startTime }
            } catch (e: Exception) {
                events = emptyList()
            }
        }
    }

    override fun onDestroy() {
        events = emptyList()
    }

    override fun getCount(): Int = events.size

    override fun getViewAt(position: Int): RemoteViews? {
        if (position >= events.size) return null

        val event = events[position]
        val views = RemoteViews(context.packageName, R.layout.widget_event_item)

        // Set event title
        views.setTextViewText(R.id.widget_event_title, event.title)

        // Set event time
        val timeText = if (event.isAllDay) {
            "全天"
        } else {
            val startTime = event.startTime.format(timeFormatter)
            val endTime = event.endTime?.format(timeFormatter)
            if (endTime != null) "$startTime - $endTime" else startTime
        }
        views.setTextViewText(R.id.widget_event_time, timeText)

        // Set event location if available
        if (event.location.isNullOrBlank()) {
            views.setViewVisibility(R.id.widget_event_location, android.view.View.GONE)
        } else {
            views.setViewVisibility(R.id.widget_event_location, android.view.View.VISIBLE)
            views.setTextViewText(R.id.widget_event_location, event.location)
        }

        // Set event color based on calendar
        val color = event.calendar?.color ?: "#2196F3"
        views.setInt(R.id.widget_event_color, "setBackgroundColor", android.graphics.Color.parseColor(color))

        // Set click listener to open event detail
        val eventDetailIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("navigate_to", Screen.EventDetail.route.replace("{eventId}", event.id.toString()))
        }
        val pendingIntent = PendingIntent.getActivity(
            context, position, eventDetailIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_event_container, pendingIntent)

        return views
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
} 