package com.medical.calendar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalDate

@Entity(tableName = "transactions")
@Parcelize
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val description: String = "",
    val date: LocalDate,
    val time: LocalDateTime? = null,
    val paymentMethod: String = "",
    val location: String = "",
    val tags: String = "", // 逗號分隔的標籤
    val receiptImage: String? = null, // 收據圖片路徑
    val isRecurring: Boolean = false,
    val recurringRule: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
) : Parcelable

enum class TransactionType {
    INCOME,
    EXPENSE
}

@Entity(tableName = "categories")
@Parcelize
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val type: TransactionType,
    val color: String,
    val icon: String = "",
    val isDefault: Boolean = false,
    val sortOrder: Int = 0
) : Parcelable

@Entity(tableName = "budgets")
@Parcelize
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val amount: Double,
    val spent: Double = 0.0,
    val period: BudgetPeriod = BudgetPeriod.MONTHLY,
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val categories: String = "", // 逗號分隔的類別ID
    val color: String = "#4CAF50",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class BudgetPeriod {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY
} 