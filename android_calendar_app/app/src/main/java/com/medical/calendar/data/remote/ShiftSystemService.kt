package com.medical.calendar.data.remote

import com.medical.calendar.data.model.CalendarEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShiftSystemService @Inject constructor() {
    
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    
    /**
     * 從排班系統HTML頁面同步資料
     */
    suspend fun syncFromShiftSystem(htmlUrl: String): List<CalendarEvent> = withContext(Dispatchers.IO) {
        try {
            val doc: Document = Jsoup.connect(htmlUrl).get()
            val events = mutableListOf<CalendarEvent>()
            
            // 解析排班表格
            val shiftTable = doc.select("table.shift-table, table.schedule-table").first()
            if (shiftTable != null) {
                val rows = shiftTable.select("tr")
                
                for (row in rows) {
                    val cells = row.select("td")
                    if (cells.size >= 4) {
                        val event = parseShiftRow(cells)
                        if (event != null) {
                            events.add(event)
                        }
                    }
                }
            }
            
            // 解析其他可能的排班資料
            val shiftElements = doc.select(".shift-item, .schedule-item")
            for (element in shiftElements) {
                val event = parseShiftElement(element)
                if (event != null) {
                    events.add(event)
                }
            }
            
            events
        } catch (e: Exception) {
            throw ShiftSyncException("同步排班系統失敗: ${e.message}", e)
        }
    }
    
    private fun parseShiftRow(cells: List<Element>): CalendarEvent? {
        return try {
            val dateStr = cells[0].text().trim()
            val timeStr = cells[1].text().trim()
            val department = cells[2].text().trim()
            val staffName = cells[3].text().trim()
            
            val date = LocalDate.parse(dateStr, dateFormatter)
            val (startTime, endTime) = parseTimeRange(timeStr)
            
            CalendarEvent(
                title = "$department - $staffName",
                description = "排班時間: $timeStr",
                startTime = LocalDateTime.of(date, startTime),
                endTime = LocalDateTime.of(date, endTime),
                calendarType = CalendarType.MEDICAL_SHIFT,
                calendarId = "shift_system",
                color = "#FF5722" // 排班系統專用紅色
            )
        } catch (e: Exception) {
            null
        }
    }
    
    private fun parseShiftElement(element: Element): CalendarEvent? {
        return try {
            val dateStr = element.attr("data-date") ?: element.select(".date").text()
            val timeStr = element.attr("data-time") ?: element.select(".time").text()
            val department = element.attr("data-department") ?: element.select(".department").text()
            val staffName = element.attr("data-staff") ?: element.select(".staff").text()
            
            if (dateStr.isBlank() || timeStr.isBlank()) return null
            
            val date = LocalDate.parse(dateStr, dateFormatter)
            val (startTime, endTime) = parseTimeRange(timeStr)
            
            CalendarEvent(
                title = "$department - $staffName",
                description = "排班時間: $timeStr",
                startTime = LocalDateTime.of(date, startTime),
                endTime = LocalDateTime.of(date, endTime),
                calendarType = CalendarType.MEDICAL_SHIFT,
                calendarId = "shift_system",
                color = "#FF5722"
            )
        } catch (e: Exception) {
            null
        }
    }
    
    private fun parseTimeRange(timeStr: String): Pair<LocalTime, LocalTime> {
        val timePattern = Regex("(\\d{1,2}):(\\d{2})\\s*-\\s*(\\d{1,2}):(\\d{2})")
        val match = timePattern.find(timeStr)
        
        return if (match != null) {
            val (startHour, startMin, endHour, endMin) = match.destructured
            val startTime = LocalTime.of(startHour.toInt(), startMin.toInt())
            val endTime = LocalTime.of(endHour.toInt(), endMin.toInt())
            Pair(startTime, endTime)
        } else {
            // 預設8小時班
            val startTime = LocalTime.of(8, 0)
            val endTime = LocalTime.of(16, 0)
            Pair(startTime, endTime)
        }
    }
    
    /**
     * 檢查排班系統連接狀態
     */
    suspend fun checkConnection(htmlUrl: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val doc: Document = Jsoup.connect(htmlUrl).timeout(10000).get()
            doc.select("table.shift-table, table.schedule-table, .shift-item, .schedule-item").isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }
}

class ShiftSyncException(message: String, cause: Throwable? = null) : Exception(message, cause) 