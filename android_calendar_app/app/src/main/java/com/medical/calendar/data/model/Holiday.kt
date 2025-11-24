package com.medical.calendar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import kotlinx.datetime.LocalDate

@Entity(tableName = "holidays")
@Parcelize
data class Holiday(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val date: LocalDate,
    val lunarDate: String? = null, // 農曆日期
    val type: HolidayType,
    val region: String = "TW", // 地區代碼
    val description: String = "",
    val isPublicHoliday: Boolean = false,
    val color: String = "#FF5722", // 預設橙色
    val isRecurring: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

enum class HolidayType {
    SOLAR_TERM,      // 二十四節氣
    TRADITIONAL,      // 傳統節日
    MODERN,          // 現代節日
    RELIGIOUS,       // 宗教節日
    CUSTOM           // 自定義節日
}

@Entity(tableName = "lunar_dates")
@Parcelize
data class LunarDate(
    @PrimaryKey
    val date: LocalDate,
    val lunarYear: Int,
    val lunarMonth: Int,
    val lunarDay: Int,
    val isLeapMonth: Boolean = false,
    val solarTerm: String? = null,
    val festival: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable 