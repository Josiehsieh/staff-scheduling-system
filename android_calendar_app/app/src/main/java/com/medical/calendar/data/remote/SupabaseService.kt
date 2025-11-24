package com.medical.calendar.data.remote

import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.model.CalendarType
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Returning
import io.github.jan.supabase.postgrest.query.gte
import io.github.jan.supabase.postgrest.query.lte
import io.github.jan.supabase.postgrest.query.select
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseService @Inject constructor() {
    
    private val supabaseClient: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://litnrtwcihcvpxpfufeg.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxpdG5ydHdjaWhjdnB4cGZ1ZmVnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM1Mzc3NTMsImV4cCI6MjA2OTExMzc1M30.NmnxXnbo1LD-BVBoxUxeFCNVHyUZaGtSCDGdXtVNLfY"
    ) {
        install(Postgrest)
    }
    
    suspend fun syncMedicalShifts(startDate: LocalDateTime, endDate: LocalDateTime): List<CalendarEvent> {
        return try {
            println("開始同步醫療排班資料...")
            println("查詢日期範圍: ${startDate} 到 ${endDate}")
            
            val response = supabaseClient.postgrest["v_shifts_detail"].select {
                gte("shift_date", startDate.toString())
                lte("shift_date", endDate.toString())
            }
            
            println("Supabase回應狀態: ${response.status}")
            
            val shifts = response.decodeList<ShiftData>()
            println("獲取到 ${shifts.size} 筆排班資料")
            
            shifts.map { shift ->
                CalendarEvent(
                    title = "${shift.unit_name} - ${shift.staff_names.joinToString(", ")}",
                    description = "排班時間: ${shift.start_time} - ${shift.end_time}",
                    startTime = LocalDateTime.parse("${shift.shift_date}T${shift.start_time}"),
                    endTime = LocalDateTime.parse("${shift.shift_date}T${shift.end_time}"),
                    location = shift.branch_name ?: shift.unit_name,
                    calendarType = CalendarType.MEDICAL_SHIFT,
                    calendarId = "medical_shifts",
                    eventId = shift.id.toString(),
                    color = shift.unit_color ?: "#667eea" // 使用事業單位原本設定的顏色
                )
            }
        } catch (e: Exception) {
            println("同步醫療排班失敗: ${e.message}")
            println("錯誤類型: ${e.javaClass.simpleName}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun syncUnits(): List<UnitData> {
        return try {
            val response = supabaseClient.postgrest["v_units_complete"].select()
            response.decodeList<UnitData>()
        } catch (e: Exception) {
            println("同步事業單位失敗: ${e.message}")
            emptyList()
        }
    }
}

// 資料模型
data class ShiftData(
    val id: Long,
    val shift_date: String,
    val start_time: String,
    val end_time: String,
    val unit_id: Long,
    val unit_name: String,
    val unit_color: String?,
    val branch_name: String?,
    val staff_names: List<String>,
    val total_hours: Double
)

data class UnitData(
    val id: Long,
    val name: String,
    val color: String,
    val is_distributed: Boolean,
    val has_subsidy: Boolean,
    val addresses: List<AddressData>?,
    val doctors: List<String>?,
    val nurses: List<String>?
)

data class AddressData(
    val name: String?,
    val address: String,
    val contact: String?
) 