package com.medical.calendar.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object NetworkUtil {
    
    /**
     * 檢查網路連線狀態
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
    
    /**
     * 測試特定URL的連線
     */
    suspend fun testUrlConnection(url: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connectTimeout = 10000 // 10秒
            connection.readTimeout = 10000
            connection.requestMethod = "GET"
            connection.connect()
            
            val responseCode = connection.responseCode
            connection.disconnect()
            
            responseCode in 200..299
        } catch (e: Exception) {
            println("網路連線測試失敗: ${e.message}")
            false
        }
    }
    
    /**
     * 測試Supabase連線
     */
    suspend fun testSupabaseConnection(): Boolean {
        return testUrlConnection("https://litnrtwcihcvpxpfufeg.supabase.co")
    }
    
    /**
     * 獲取網路類型
     */
    fun getNetworkType(context: Context): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return "無網路"
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return "無網路"
        
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "行動網路"
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "乙太網路"
            else -> "其他"
        }
    }
} 