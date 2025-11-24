package com.medical.calendar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import kotlinx.datetime.LocalDate

@Entity(tableName = "menstrual_cycles")
@Parcelize
data class MenstrualCycle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val cycleLength: Int? = null, // 週期長度（天）
    val flowIntensity: FlowIntensity = FlowIntensity.MEDIUM,
    val symptoms: String = "", // 症狀描述
    val notes: String = "",
    val isPredicted: Boolean = false, // 是否為預測日期
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class FlowIntensity {
    LIGHT,
    MEDIUM,
    HEAVY
}

@Entity(tableName = "cycle_settings")
@Parcelize
data class CycleSettings(
    @PrimaryKey
    val id: Int = 1,
    val averageCycleLength: Int = 28,
    val averagePeriodLength: Int = 5,
    val lastPeriodStart: LocalDate? = null,
    val enablePredictions: Boolean = true,
    val enableReminders: Boolean = true,
    val reminderDays: Int = 3, // 提前幾天提醒
    val color: String = "#E91E63" // 粉紅色
) : Parcelable 