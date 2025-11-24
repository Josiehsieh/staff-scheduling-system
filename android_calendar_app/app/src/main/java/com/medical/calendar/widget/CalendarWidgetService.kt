package com.medical.calendar.widget

import android.content.Intent
import android.widget.RemoteViewsService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return CalendarWidgetFactory(applicationContext, intent)
    }
} 