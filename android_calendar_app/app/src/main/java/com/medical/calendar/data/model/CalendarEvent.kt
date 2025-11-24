package com.medical.calendar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalDate
import com.medical.calendar.util.ColorManager
import com.medical.calendar.util.LunarCalendarUtil

@Entity(tableName = "calendar_events")
@Parcelize
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val location: String = "",
    val calendarType: CalendarType,
    val calendarId: String, // 來源日曆的 ID
    val eventId: String? = null, // 外部日曆的事件 ID
    val color: String = ColorManager.getDefaultColor(CalendarType.PERSONAL),
    val isAllDay: Boolean = false,
    val isRecurring: Boolean = false,
    val recurrenceRule: String? = null,
    val lastModified: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false,
    val lunarDate: String? = null // 農曆日期資訊
) : Parcelable

enum class CalendarType {
    MEDICAL_SHIFT,    // 醫療排班
    PERSONAL,         // 個人行事曆
    GOOGLE_CALENDAR   // Google 行事曆
} 