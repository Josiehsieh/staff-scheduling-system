package com.medical.calendar.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import java.util.*

object LunarCalendarUtil {
    
    /**
     * 農曆月份名稱
     */
    private val LUNAR_MONTHS = arrayOf(
        "正月", "二月", "三月", "四月", "五月", "六月",
        "七月", "八月", "九月", "十月", "冬月", "臘月"
    )
    
    /**
     * 農曆日期名稱
     */
    private val LUNAR_DAYS = arrayOf(
        "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
        "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
        "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
    )
    
    /**
     * 天干
     */
    private val HEAVENLY_STEMS = arrayOf("甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸")
    
    /**
     * 地支
     */
    private val EARTHLY_BRANCHES = arrayOf("子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥")
    
    /**
     * 生肖
     */
    private val ZODIAC_ANIMALS = arrayOf("鼠", "牛", "虎", "兔", "龍", "蛇", "馬", "羊", "猴", "雞", "狗", "豬")
    
    /**
     * 農曆年份資料 (1900-2100)
     * 每個年份的資料包含：閏月月份(0表示無閏月)、每月天數(12位二進制，1表示30天，0表示29天)
     */
    private val LUNAR_YEAR_DATA = intArrayOf(
        0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
        0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
        0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
        0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
        0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
        0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
        0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
        0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
        0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
        0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
        0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
        0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
        0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
        0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0
    )
    
    /**
     * 將公曆日期轉換為農曆日期
     */
    fun gregorianToLunar(date: LocalDate): LunarDate {
        val calendar = Calendar.getInstance()
        calendar.set(date.year, date.monthNumber - 1, date.dayOfMonth)
        
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        
        // 計算從1900年1月31日到指定日期的天數
        val baseDate = Calendar.getInstance()
        baseDate.set(1900, 0, 31)
        val offset = ((calendar.timeInMillis - baseDate.timeInMillis) / 86400000).toInt()
        
        // 計算農曆年份
        var lunarYear = 1900
        var temp = 0
        for (i in 0..200) {
            temp = getLunarYearDays(lunarYear)
            if (offset < temp) break
            offset -= temp
            lunarYear++
        }
        
        // 計算農曆月份
        var lunarMonth = 1
        var isLeap = false
        val leapMonth = getLeapMonth(lunarYear)
        
        for (i in 1..12) {
            if (leapMonth > 0 && i == leapMonth + 1) {
                if (!isLeap) {
                    isLeap = true
                    i--
                    temp = getLeapDays(lunarYear)
                } else {
                    isLeap = false
                    temp = getLunarMonthDays(lunarYear, i)
                }
            } else {
                temp = getLunarMonthDays(lunarYear, i)
            }
            
            if (offset < temp) break
            offset -= temp
            
            if (!isLeap && i == leapMonth) {
                isLeap = true
                i--
            } else {
                lunarMonth++
            }
        }
        
        // 計算農曆日期
        val lunarDay = offset + 1
        
        return LunarDate(
            year = lunarYear,
            month = lunarMonth,
            day = lunarDay,
            isLeap = isLeap,
            yearName = getLunarYearName(lunarYear),
            monthName = getLunarMonthName(lunarMonth, isLeap),
            dayName = getLunarDayName(lunarDay),
            zodiac = getZodiac(lunarYear)
        )
    }
    
    /**
     * 將農曆日期轉換為公曆日期
     */
    fun lunarToGregorian(lunarDate: LunarDate): LocalDate {
        val year = lunarDate.year
        val month = lunarDate.month
        val day = lunarDate.day
        val isLeap = lunarDate.isLeap
        
        // 計算從1900年1月31日到指定農曆日期的天數
        var offset = 0
        
        // 計算年份天數
        for (i in 1900 until year) {
            offset += getLunarYearDays(i)
        }
        
        // 計算月份天數
        val leapMonth = getLeapMonth(year)
        for (i in 1 until month) {
            offset += getLunarMonthDays(year, i)
            if (leapMonth > 0 && i == leapMonth) {
                offset += getLeapDays(year)
            }
        }
        
        // 如果是閏月
        if (isLeap && leapMonth == month) {
            offset += getLunarMonthDays(year, month)
        }
        
        // 加上當月天數
        offset += day - 1
        
        // 轉換為公曆日期
        val baseDate = Calendar.getInstance()
        baseDate.set(1900, 0, 31)
        baseDate.add(Calendar.DAY_OF_MONTH, offset)
        
        return LocalDate(
            baseDate.get(Calendar.YEAR),
            baseDate.get(Calendar.MONTH) + 1,
            baseDate.get(Calendar.DAY_OF_MONTH)
        )
    }
    
    /**
     * 獲取農曆年份的天數
     */
    private fun getLunarYearDays(year: Int): Int {
        val yearData = LUNAR_YEAR_DATA[year - 1900]
        var total = 348
        for (i in 0x8000 downTo 0x8) {
            if (yearData and i != 0) {
                total += 1
            }
        }
        return total + getLeapDays(year)
    }
    
    /**
     * 獲取農曆年份閏月的天數
     */
    private fun getLeapDays(year: Int): Int {
        return if (getLeapMonth(year) != 0) {
            if (LUNAR_YEAR_DATA[year - 1900] and 0x10000 != 0) 30 else 29
        } else {
            0
        }
    }
    
    /**
     * 獲取農曆年份閏月的月份
     */
    private fun getLeapMonth(year: Int): Int {
        return LUNAR_YEAR_DATA[year - 1900] and 0xf
    }
    
    /**
     * 獲取農曆月份的天數
     */
    private fun getLunarMonthDays(year: Int, month: Int): Int {
        return if (LUNAR_YEAR_DATA[year - 1900] and (0x10000 shr month) != 0) 30 else 29
    }
    
    /**
     * 獲取農曆年份名稱
     */
    private fun getLunarYearName(year: Int): String {
        val heavenlyStem = HEAVENLY_STEMS[(year - 4) % 10]
        val earthlyBranch = EARTHLY_BRANCHES[(year - 4) % 12]
        return "$heavenlyStem$earthlyBranch年"
    }
    
    /**
     * 獲取農曆月份名稱
     */
    private fun getLunarMonthName(month: Int, isLeap: Boolean): String {
        val monthName = LUNAR_MONTHS[month - 1]
        return if (isLeap) "閏$monthName" else monthName
    }
    
    /**
     * 獲取農曆日期名稱
     */
    private fun getLunarDayName(day: Int): String {
        return LUNAR_DAYS[day - 1]
    }
    
    /**
     * 獲取生肖
     */
    private fun getZodiac(year: Int): String {
        return ZODIAC_ANIMALS[(year - 4) % 12]
    }
    
    /**
     * 獲取農曆日期的簡短顯示格式
     */
    fun getLunarDisplayText(date: LocalDate): String {
        val lunar = gregorianToLunar(date)
        return "${lunar.monthName}${lunar.dayName}"
    }
    
    /**
     * 獲取農曆日期的完整顯示格式
     */
    fun getLunarFullDisplayText(date: LocalDate): String {
        val lunar = gregorianToLunar(date)
        return "${lunar.yearName} ${lunar.monthName}${lunar.dayName} ${lunar.zodiac}年"
    }
    
    /**
     * 檢查是否為農曆節日
     */
    fun isLunarFestival(date: LocalDate): Boolean {
        val lunar = gregorianToLunar(date)
        return when {
            lunar.month == 1 && lunar.day == 1 -> true  // 春節
            lunar.month == 1 && lunar.day == 15 -> true // 元宵節
            lunar.month == 5 && lunar.day == 5 -> true  // 端午節
            lunar.month == 7 && lunar.day == 7 -> true  // 七夕
            lunar.month == 8 && lunar.day == 15 -> true // 中秋節
            lunar.month == 9 && lunar.day == 9 -> true  // 重陽節
            lunar.month == 12 && lunar.day == 8 -> true // 臘八節
            lunar.month == 12 && lunar.day == 23 -> true // 小年
            lunar.month == 12 && lunar.day == 30 -> true // 除夕
            else -> false
        }
    }
    
    /**
     * 獲取農曆節日名稱
     */
    fun getLunarFestivalName(date: LocalDate): String? {
        val lunar = gregorianToLunar(date)
        return when {
            lunar.month == 1 && lunar.day == 1 -> "春節"
            lunar.month == 1 && lunar.day == 15 -> "元宵節"
            lunar.month == 5 && lunar.day == 5 -> "端午節"
            lunar.month == 7 && lunar.day == 7 -> "七夕"
            lunar.month == 8 && lunar.day == 15 -> "中秋節"
            lunar.month == 9 && lunar.day == 9 -> "重陽節"
            lunar.month == 12 && lunar.day == 8 -> "臘八節"
            lunar.month == 12 && lunar.day == 23 -> "小年"
            lunar.month == 12 && lunar.day == 30 -> "除夕"
            else -> null
        }
    }
}

/**
 * 農曆日期資料類
 */
data class LunarDate(
    val year: Int,
    val month: Int,
    val day: Int,
    val isLeap: Boolean,
    val yearName: String,
    val monthName: String,
    val dayName: String,
    val zodiac: String
) 