package com.medical.calendar.ui.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medical.calendar.data.model.Transaction
import com.medical.calendar.data.model.Budget
import com.medical.calendar.data.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(FinanceUiState())
    val uiState: StateFlow<FinanceUiState> = _uiState.asStateFlow()
    
    fun loadTransactions() {
        viewModelScope.launch {
            // TODO: 從資料庫載入交易資料
            // 暫時使用測試資料
            val testTransactions = listOf(
                Transaction(
                    id = 1L,
                    amount = 1500.0,
                    type = com.medical.calendar.data.model.TransactionType.EXPENSE,
                    category = "餐飲",
                    date = LocalDate.now().minusDays(1),
                    description = "午餐",
                    tags = listOf("日常", "餐飲")
                ),
                Transaction(
                    id = 2L,
                    amount = 5000.0,
                    type = com.medical.calendar.data.model.TransactionType.INCOME,
                    category = "薪資",
                    date = LocalDate.now().minusDays(5),
                    description = "月薪",
                    tags = listOf("收入", "薪資")
                )
            )
            
            _uiState.value = _uiState.value.copy(
                transactions = testTransactions,
                isLoading = false
            )
        }
    }
    
    fun loadBudgets() {
        viewModelScope.launch {
            // TODO: 從資料庫載入預算資料
            val testBudgets = listOf(
                Budget(
                    id = 1L,
                    name = "餐飲預算",
                    amount = 3000.0,
                    spent = 1500.0,
                    period = com.medical.calendar.data.model.BudgetPeriod.MONTHLY,
                    categories = listOf("餐飲")
                )
            )
            _uiState.value = _uiState.value.copy(budgets = testBudgets)
        }
    }
    
    fun loadCategories() {
        viewModelScope.launch {
            // TODO: 從資料庫載入分類資料
            val testCategories = listOf(
                Category(
                    id = 1L,
                    name = "餐飲",
                    type = com.medical.calendar.data.model.TransactionType.EXPENSE,
                    color = "#FF5722",
                    icon = "restaurant"
                ),
                Category(
                    id = 2L,
                    name = "薪資",
                    type = com.medical.calendar.data.model.TransactionType.INCOME,
                    color = "#4CAF50",
                    icon = "account_balance_wallet"
                )
            )
            _uiState.value = _uiState.value.copy(categories = testCategories)
        }
    }
    
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // TODO: 儲存交易到資料庫
            val currentTransactions = _uiState.value.transactions.toMutableList()
            currentTransactions.add(transaction.copy(id = (currentTransactions.maxOfOrNull { it.id } ?: 0) + 1))
            _uiState.value = _uiState.value.copy(transactions = currentTransactions)
        }
    }
    
    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // TODO: 更新交易到資料庫
            val currentTransactions = _uiState.value.transactions.toMutableList()
            val index = currentTransactions.indexOfFirst { it.id == transaction.id }
            if (index != -1) {
                currentTransactions[index] = transaction
                _uiState.value = _uiState.value.copy(transactions = currentTransactions)
            }
        }
    }
    
    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            // TODO: 從資料庫刪除交易
            val currentTransactions = _uiState.value.transactions.toMutableList()
            currentTransactions.removeAll { it.id == transactionId }
            _uiState.value = _uiState.value.copy(transactions = currentTransactions)
        }
    }
    
    val balance: Double
        get() {
            val transactions = _uiState.value.transactions
            return transactions.sumOf { transaction ->
                when (transaction.type) {
                    com.medical.calendar.data.model.TransactionType.INCOME -> transaction.amount
                    com.medical.calendar.data.model.TransactionType.EXPENSE -> -transaction.amount
                }
            }
        }
    
    val totalIncome: Double
        get() {
            val currentMonth = LocalDate.now().monthValue
            val currentYear = LocalDate.now().year
            return _uiState.value.transactions
                .filter { 
                    it.type == com.medical.calendar.data.model.TransactionType.INCOME &&
                    it.date.monthValue == currentMonth &&
                    it.date.year == currentYear
                }
                .sumOf { it.amount }
        }
    
    val totalExpense: Double
        get() {
            val currentMonth = LocalDate.now().monthValue
            val currentYear = LocalDate.now().year
            return _uiState.value.transactions
                .filter { 
                    it.type == com.medical.calendar.data.model.TransactionType.EXPENSE &&
                    it.date.monthValue == currentMonth &&
                    it.date.year == currentYear
                }
                .sumOf { it.amount }
        }
}

data class FinanceUiState(
    val transactions: List<Transaction> = emptyList(),
    val budgets: List<Budget> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val balance: Double
        get() = transactions.sumOf { transaction ->
            when (transaction.type) {
                com.medical.calendar.data.model.TransactionType.INCOME -> transaction.amount
                com.medical.calendar.data.model.TransactionType.EXPENSE -> -transaction.amount
            }
        }
    
    val totalIncome: Double
        get() {
            val currentMonth = LocalDate.now().monthValue
            val currentYear = LocalDate.now().year
            return transactions
                .filter { 
                    it.type == com.medical.calendar.data.model.TransactionType.INCOME &&
                    it.date.monthValue == currentMonth &&
                    it.date.year == currentYear
                }
                .sumOf { it.amount }
        }
    
    val totalExpense: Double
        get() {
            val currentMonth = LocalDate.now().monthValue
            val currentYear = LocalDate.now().year
            return transactions
                .filter { 
                    it.type == com.medical.calendar.data.model.TransactionType.EXPENSE &&
                    it.date.monthValue == currentMonth &&
                    it.date.year == currentYear
                }
                .sumOf { it.amount }
        }
} 