package com.medical.calendar.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.sheets.v4.SheetsScopes
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Google 登入輔助類別
 * 
 * 處理 Google 帳號登入、登出和授權範圍
 * 需要的權限：
 * - Google Sheets (唯讀)
 * - Google Calendar (完整存取)
 */
@Singleton
class GoogleSignInHelper @Inject constructor(
    private val context: Context
) {
    
    private var googleSignInClient: GoogleSignInClient? = null
    
    /**
     * 取得 Google Sign-In Client
     */
    fun getSignInClient(): GoogleSignInClient {
        if (googleSignInClient == null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(
                    // Google Sheets 唯讀權限
                    Scope(SheetsScopes.SPREADSHEETS_READONLY),
                    // Google Calendar 完整權限
                    Scope(CalendarScopes.CALENDAR),
                    Scope(CalendarScopes.CALENDAR_EVENTS)
                )
                .build()
            
            googleSignInClient = GoogleSignIn.getClient(context, gso)
        }
        return googleSignInClient!!
    }
    
    /**
     * 取得目前登入的帳號
     */
    fun getSignedInAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
    
    /**
     * 檢查是否已登入
     */
    fun isSignedIn(): Boolean {
        return getSignedInAccount() != null
    }
    
    /**
     * 取得登入 Intent
     * 需要在 Activity 中使用 ActivityResultLauncher 啟動
     */
    fun getSignInIntent(): Intent {
        return getSignInClient().signInIntent
    }
    
    /**
     * 從 Intent 結果中取得帳號
     */
    suspend fun getAccountFromIntent(data: Intent?): GoogleSignInAccount? {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.await()
        } catch (e: Exception) {
            println("❌ Google 登入失敗: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    /**
     * 登出
     */
    suspend fun signOut(): Boolean {
        return try {
            getSignInClient().signOut().await()
            println("✅ Google 登出成功")
            true
        } catch (e: Exception) {
            println("❌ Google 登出失敗: ${e.message}")
            false
        }
    }
    
    /**
     * 撤銷存取權限（完全移除授權）
     */
    suspend fun revokeAccess(): Boolean {
        return try {
            getSignInClient().revokeAccess().await()
            println("✅ Google 存取權限已撤銷")
            true
        } catch (e: Exception) {
            println("❌ 撤銷 Google 存取權限失敗: ${e.message}")
            false
        }
    }
    
    /**
     * 檢查是否有必要的權限
     */
    fun hasRequiredScopes(): Boolean {
        val account = getSignedInAccount() ?: return false
        
        val requiredScopes = setOf(
            Scope(SheetsScopes.SPREADSHEETS_READONLY),
            Scope(CalendarScopes.CALENDAR)
        )
        
        return GoogleSignIn.hasPermissions(account, *requiredScopes.toTypedArray())
    }
    
    /**
     * 請求額外的權限
     */
    fun requestAdditionalScopes(scopes: List<Scope>): Intent {
        val account = getSignedInAccount()
        val options = GoogleSignInOptions.Builder()
            .requestScopes(*scopes.toTypedArray())
            .build()
        
        return GoogleSignIn.getClient(context, options).signInIntent
    }
    
    /**
     * 取得帳號資訊摘要
     */
    fun getAccountInfo(): String {
        val account = getSignedInAccount()
        return if (account != null) {
            """
            帳號: ${account.email ?: "未知"}
            名稱: ${account.displayName ?: "未知"}
            已授權範圍: ${account.grantedScopes.size} 個
            """.trimIndent()
        } else {
            "未登入"
        }
    }
}



