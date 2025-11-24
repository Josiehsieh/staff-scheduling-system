package com.medical.calendar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalDate

@Entity(tableName = "projects")
@Parcelize
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val status: ProjectStatus = ProjectStatus.IN_PROGRESS,
    val priority: ProjectPriority = ProjectPriority.MEDIUM,
    val color: String = "#2196F3", // 預設藍色
    val budget: Double? = null,
    val progress: Int = 0, // 0-100
    val tags: String = "", // 逗號分隔的標籤
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
) : Parcelable

enum class ProjectStatus {
    NOT_STARTED,
    IN_PROGRESS,
    ON_HOLD,
    COMPLETED,
    CANCELLED
}

enum class ProjectPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
} 