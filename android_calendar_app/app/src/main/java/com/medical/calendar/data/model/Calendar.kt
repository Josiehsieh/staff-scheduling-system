package com.medical.calendar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Entity(tableName = "calendars")
@Parcelize
data class Calendar(
    @PrimaryKey
    val id: String,
    val name: String,
    val color: String,
    val calendarType: CalendarType,
    val isVisible: Boolean = true,
    val isSyncEnabled: Boolean = true,
    val accountName: String? = null, // Google 帳戶名稱
    val lastSyncTime: Long = 0L
) : Parcelable 