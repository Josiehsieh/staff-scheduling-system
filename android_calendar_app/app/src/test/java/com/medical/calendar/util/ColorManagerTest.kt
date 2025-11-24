package com.medical.calendar.util

import com.medical.calendar.data.model.CalendarType
import org.junit.Test
import org.junit.Assert.*

class ColorManagerTest {
    
    @Test
    fun `test getAvailableColors for different calendar types`() {
        // 測試醫療排班顏色
        val medicalColors = ColorManager.getAvailableColors(CalendarType.MEDICAL_SHIFT)
        assertNotNull(medicalColors)
        assertTrue(medicalColors.isNotEmpty())
        assertTrue(medicalColors.contains("#FF6B6B"))
        assertTrue(medicalColors.contains("#667eea"))
        
        // 測試個人事件顏色
        val personalColors = ColorManager.getAvailableColors(CalendarType.PERSONAL)
        assertNotNull(personalColors)
        assertTrue(personalColors.isNotEmpty())
        assertTrue(personalColors.contains("#9C27B0"))
        assertTrue(personalColors.contains("#E91E63"))
        
        // 測試Google日曆顏色
        val googleColors = ColorManager.getAvailableColors(CalendarType.GOOGLE_CALENDAR)
        assertNotNull(googleColors)
        assertTrue(googleColors.isNotEmpty())
        assertTrue(googleColors.contains("#4285F4"))
    }
    
    @Test
    fun `test getDefaultColor for different calendar types`() {
        assertEquals("#667eea", ColorManager.getDefaultColor(CalendarType.MEDICAL_SHIFT))
        assertEquals("#9C27B0", ColorManager.getDefaultColor(CalendarType.PERSONAL))
        assertEquals("#4285F4", ColorManager.getDefaultColor(CalendarType.GOOGLE_CALENDAR))
    }
    
    @Test
    fun `test isMedicalShiftColor`() {
        assertTrue(ColorManager.isMedicalShiftColor("#FF6B6B"))
        assertTrue(ColorManager.isMedicalShiftColor("#667eea"))
        assertFalse(ColorManager.isMedicalShiftColor("#9C27B0"))
        assertFalse(ColorManager.isMedicalShiftColor("#4285F4"))
    }
    
    @Test
    fun `test isPersonalEventColor`() {
        assertTrue(ColorManager.isPersonalEventColor("#9C27B0"))
        assertTrue(ColorManager.isPersonalEventColor("#E91E63"))
        assertFalse(ColorManager.isPersonalEventColor("#FF6B6B"))
        assertFalse(ColorManager.isPersonalEventColor("#4285F4"))
    }
    
    @Test
    fun `test getRecommendedPersonalColor`() {
        val usedColors = listOf("#9C27B0", "#E91E63")
        val recommendedColor = ColorManager.getRecommendedPersonalColor(usedColors)
        assertNotNull(recommendedColor)
        assertFalse(usedColors.contains(recommendedColor))
        assertTrue(ColorManager.PERSONAL_EVENT_COLORS.contains(recommendedColor))
    }
    
    @Test
    fun `test getRecommendedMedicalColor`() {
        val usedColors = listOf("#FF6B6B", "#4ECDC4")
        val recommendedColor = ColorManager.getRecommendedMedicalColor(usedColors)
        assertNotNull(recommendedColor)
        assertFalse(usedColors.contains(recommendedColor))
        assertTrue(ColorManager.MEDICAL_SHIFT_COLORS.contains(recommendedColor))
    }
    
    @Test
    fun `test color separation between medical and personal`() {
        // 確保醫療排班和個人事件的顏色沒有重複
        val medicalColors = ColorManager.MEDICAL_SHIFT_COLORS.toSet()
        val personalColors = ColorManager.PERSONAL_EVENT_COLORS.toSet()
        val intersection = medicalColors.intersect(personalColors)
        
        assertTrue("醫療排班和個人事件顏色不應該重複", intersection.isEmpty())
    }
    
    @Test
    fun `test all colors are valid hex colors`() {
        val allColors = ColorManager.MEDICAL_SHIFT_COLORS + 
                       ColorManager.PERSONAL_EVENT_COLORS + 
                       ColorManager.GOOGLE_CALENDAR_COLORS
        
        allColors.forEach { color ->
            assertTrue("顏色 $color 應該是有效的HEX格式", color.matches(Regex("^#[0-9A-Fa-f]{6}$")))
        }
    }
} 