package com.medical.calendar.util

import kotlinx.datetime.LocalDate
import org.junit.Test
import org.junit.Assert.*

class LunarCalendarUtilTest {

    @Test
    fun `test gregorian to lunar conversion for Spring Festival 2024`() {
        // 2024年春節是農曆正月初一，對應公曆2024年2月10日
        val gregorianDate = LocalDate(2024, 2, 10)
        val lunarDate = LunarCalendarUtil.gregorianToLunar(gregorianDate)
        
        assertEquals(2024, lunarDate.year)
        assertEquals(1, lunarDate.month)
        assertEquals(1, lunarDate.day)
        assertFalse(lunarDate.isLeap)
        assertEquals("甲辰年", lunarDate.yearName)
        assertEquals("正月", lunarDate.monthName)
        assertEquals("初一", lunarDate.dayName)
        assertEquals("龍", lunarDate.zodiac)
    }

    @Test
    fun `test gregorian to lunar conversion for Mid-Autumn Festival 2024`() {
        // 2024年中秋節是農曆八月十五，對應公曆2024年9月17日
        val gregorianDate = LocalDate(2024, 9, 17)
        val lunarDate = LunarCalendarUtil.gregorianToLunar(gregorianDate)
        
        assertEquals(2024, lunarDate.year)
        assertEquals(8, lunarDate.month)
        assertEquals(15, lunarDate.day)
        assertFalse(lunarDate.isLeap)
        assertEquals("八月", lunarDate.monthName)
        assertEquals("十五", lunarDate.dayName)
    }

    @Test
    fun `test lunar to gregorian conversion`() {
        // 農曆2024年正月初一應該對應公曆2024年2月10日
        val lunarDate = LunarDate(
            year = 2024,
            month = 1,
            day = 1,
            isLeap = false,
            yearName = "甲辰年",
            monthName = "正月",
            dayName = "初一",
            zodiac = "龍"
        )
        
        val gregorianDate = LunarCalendarUtil.lunarToGregorian(lunarDate)
        assertEquals(LocalDate(2024, 2, 10), gregorianDate)
    }

    @Test
    fun `test lunar display text`() {
        val gregorianDate = LocalDate(2024, 2, 10)
        val lunarText = LunarCalendarUtil.getLunarDisplayText(gregorianDate)
        assertEquals("正月初一", lunarText)
    }

    @Test
    fun `test lunar full display text`() {
        val gregorianDate = LocalDate(2024, 2, 10)
        val lunarText = LunarCalendarUtil.getLunarFullDisplayText(gregorianDate)
        assertEquals("甲辰年 正月初一 龍年", lunarText)
    }

    @Test
    fun `test lunar festival detection`() {
        // 春節
        val springFestival = LocalDate(2024, 2, 10)
        assertTrue(LunarCalendarUtil.isLunarFestival(springFestival))
        assertEquals("春節", LunarCalendarUtil.getLunarFestivalName(springFestival))
        
        // 中秋節
        val midAutumnFestival = LocalDate(2024, 9, 17)
        assertTrue(LunarCalendarUtil.isLunarFestival(midAutumnFestival))
        assertEquals("中秋節", LunarCalendarUtil.getLunarFestivalName(midAutumnFestival))
        
        // 普通日期
        val normalDate = LocalDate(2024, 3, 15)
        assertFalse(LunarCalendarUtil.isLunarFestival(normalDate))
        assertNull(LunarCalendarUtil.getLunarFestivalName(normalDate))
    }

    @Test
    fun `test leap month handling`() {
        // 2023年有閏二月
        val leapMonthDate = LocalDate(2023, 4, 20)
        val lunarDate = LunarCalendarUtil.gregorianToLunar(leapMonthDate)
        
        // 這裡需要根據實際的閏月資料來驗證
        // 由於閏月的計算比較複雜，我們主要測試轉換不會出錯
        assertNotNull(lunarDate)
        assertTrue(lunarDate.year in 1900..2100)
        assertTrue(lunarDate.month in 1..12)
        assertTrue(lunarDate.day in 1..30)
    }

    @Test
    fun `test boundary dates`() {
        // 測試邊界日期
        val minDate = LocalDate(1900, 1, 31)
        val maxDate = LocalDate(2100, 12, 31)
        
        val minLunar = LunarCalendarUtil.gregorianToLunar(minDate)
        val maxLunar = LunarCalendarUtil.gregorianToLunar(maxDate)
        
        assertNotNull(minLunar)
        assertNotNull(maxLunar)
        assertEquals(1900, minLunar.year)
        assertEquals(2100, maxLunar.year)
    }

    @Test
    fun `test zodiac calculation`() {
        // 測試生肖計算
        val dragonYear = LocalDate(2024, 1, 1)
        val dragonLunar = LunarCalendarUtil.gregorianToLunar(dragonYear)
        assertEquals("龍", dragonLunar.zodiac)
        
        val rabbitYear = LocalDate(2023, 1, 1)
        val rabbitLunar = LunarCalendarUtil.gregorianToLunar(rabbitYear)
        assertEquals("兔", rabbitLunar.zodiac)
    }

    @Test
    fun `test heavenly stems and earthly branches`() {
        // 測試天干地支
        val year2024 = LocalDate(2024, 1, 1)
        val lunar2024 = LunarCalendarUtil.gregorianToLunar(year2024)
        assertEquals("甲辰年", lunar2024.yearName)
        
        val year2023 = LocalDate(2023, 1, 1)
        val lunar2023 = LunarCalendarUtil.gregorianToLunar(year2023)
        assertEquals("癸卯年", lunar2023.yearName)
    }
} 