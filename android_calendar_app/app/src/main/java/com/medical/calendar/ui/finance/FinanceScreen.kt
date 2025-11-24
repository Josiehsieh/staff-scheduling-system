package com.medical.calendar.ui.finance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.medical.calendar.data.model.Transaction
import com.medical.calendar.data.model.TransactionType
import com.medical.calendar.data.model.Budget
import com.medical.calendar.util.ColorManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(
    viewModel: FinanceViewModel = hiltViewModel(),
    onNavigateToFinanceEdit: (Long?) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadTransactions()
        viewModel.loadBudgets()
        viewModel.loadCategories()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("記帳") },
                actions = {
                    IconButton(onClick = { onNavigateToFinanceEdit(null) }) {
                        Icon(Icons.Default.Add, contentDescription = "新增交易")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 財務概覽
            FinanceOverview(
                totalIncome = uiState.totalIncome,
                totalExpense = uiState.totalExpense,
                balance = uiState.balance
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 預算進度
            if (uiState.budgets.isNotEmpty()) {
                BudgetsProgress(budgets = uiState.budgets)
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // 交易記錄
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.transactions.sortedByDescending { it.date }) { transaction ->
                    TransactionCard(
                        transaction = transaction,
                        onClick = { onNavigateToFinanceEdit(transaction.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FinanceOverview(
    totalIncome: Double,
    totalExpense: Double,
    balance: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatCard(
            title = "收入",
            value = "$${String.format("%.0f", totalIncome)}",
            icon = Icons.Default.TrendingUp,
            color = Color(0xFF4CAF50)
        )
        
        StatCard(
            title = "支出",
            value = "$${String.format("%.0f", totalExpense)}",
            icon = Icons.Default.TrendingDown,
            color = Color(0xFFF44336)
        )
        
        StatCard(
            title = "結餘",
            value = "$${String.format("%.0f", balance)}",
            icon = Icons.Default.AccountBalance,
            color = if (balance >= 0) Color(0xFF2196F3) else Color(0xFFFF9800)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier.weight(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun BudgetsProgress(budgets: List<Budget>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "預算進度",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            budgets.forEach { budget ->
                BudgetProgressItem(budget = budget)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun BudgetProgressItem(budget: Budget) {
    val progress = (budget.spent / budget.amount).coerceIn(0.0, 1.0)
    val isOverBudget = budget.spent > budget.amount
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = budget.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = "$${String.format("%.0f", budget.spent)} / $${String.format("%.0f", budget.amount)}",
                style = MaterialTheme.typography.bodySmall,
                color = if (isOverBudget) Color(0xFFF44336) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        LinearProgressIndicator(
            progress = progress.toFloat(),
            modifier = Modifier.fillMaxWidth(),
            color = if (isOverBudget) Color(0xFFF44336) else ColorManager.hexToColor(budget.color),
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
fun TransactionCard(
    transaction: Transaction,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 類別圖示
            Surface(
                color = ColorManager.hexToColor(transaction.category),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (transaction.type) {
                            TransactionType.INCOME -> Icons.Default.TrendingUp
                            TransactionType.EXPENSE -> Icons.Default.TrendingDown
                        },
                        contentDescription = transaction.type.name,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 交易資訊
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description.ifBlank { transaction.category },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = transaction.date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (transaction.location.isNotBlank()) {
                    Text(
                        text = transaction.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // 金額
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${if (transaction.type == TransactionType.EXPENSE) "-" else "+"}$${String.format("%.0f", transaction.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = when (transaction.type) {
                        TransactionType.INCOME -> Color(0xFF4CAF50)
                        TransactionType.EXPENSE -> Color(0xFFF44336)
                    }
                )
                
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 