# 醫療行事曆 Android App - 建置指南

這是一個醫療排班管理的 Android 應用程式，整合 Google Sheets 和 Google Calendar。

---

## 🚀 快速開始

### 前置需求

1. **Android Studio** (最新版本，推薦 Hedgehog 或更新)
2. **JDK 17** 或更高版本
3. **Android SDK API 34**
4. **Google 帳號** (用於測試 Google Sheets 和 Calendar 功能)

### 建置步驟

#### 1. 複製專案

```bash
cd C:\Users\josie\staff-scheduling-system\android_calendar_app
```

#### 2. 開啟 Android Studio

1. 啟動 Android Studio
2. 選擇「Open」→ 選擇 `android_calendar_app` 資料夾
3. 等待 Gradle 同步完成

#### 3. 設定 Google API

請參考 [GOOGLE_SHEETS_SETUP.md](./GOOGLE_SHEETS_SETUP.md) 完成 Google API 設定：

- 建立 Google Cloud 專案
- 啟用 Google Sheets API 和 Google Calendar API
- 設定 OAuth 同意畫面
- 建立 OAuth 2.0 用戶端 ID (Android)

#### 4. 取得 SHA-1 指紋

在專案根目錄開啟終端機，執行：

**Windows (PowerShell):**
```powershell
keytool -list -v -keystore $env:USERPROFILE\.android\debug.keystore -alias androiddebugkey -storepass android -keypass android
```

**macOS/Linux:**
```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

複製輸出中的 `SHA1` 指紋，並在 Google Cloud Console 的 OAuth 用戶端 ID 設定中填入。

#### 5. 建置應用程式

在 Android Studio 中：

1. 選擇「Build」→「Make Project」(或按 `Ctrl+F9`)
2. 等待建置完成

#### 6. 執行應用程式

1. 連接 Android 裝置或啟動模擬器
2. 點擊「Run」→「Run 'app'」(或按 `Shift+F10`)
3. 等待應用程式安裝並啟動

---

## 📱 功能說明

### 核心功能

1. **Google Sheets 排班同步**
   - 從 Google Sheets 讀取排班資料
   - 自動解析日期、時間、人員名單等資訊
   - 支援自訂 Sheet ID 和資料範圍

2. **Google Calendar 整合**
   - 自動同步排班到 Google 日曆
   - 建立「醫療排班」日曆
   - 透過 Google 日曆同步到手機原生日曆

3. **本地資料庫快取**
   - 使用 Room 儲存本地資料
   - 離線時仍可查看已同步的排班
   - 恢復網路後自動同步

4. **多功能日曆**
   - 行事曆檢視
   - 專案管理
   - 月經週期追蹤
   - 記帳功能

5. **Widget 小工具**
   - 桌面小工具顯示近期排班
   - 自動更新事件資訊

---

## 🔧 技術架構

### 開發框架

- **語言**: Kotlin
- **UI**: Jetpack Compose
- **架構**: MVVM + Repository Pattern
- **依賴注入**: Hilt
- **資料庫**: Room
- **背景工作**: WorkManager

### 主要依賴

```gradle
// Jetpack Compose
androidx.compose.material3:material3
androidx.navigation:navigation-compose

// Google APIs
com.google.android.gms:play-services-auth
com.google.apis:google-api-services-sheets
com.google.apis:google-api-services-calendar

// Room Database
androidx.room:room-runtime
androidx.room:room-ktx

// Hilt DI
com.google.dagger:hilt-android
androidx.hilt:hilt-navigation-compose

// 其他
org.jetbrains.kotlinx:kotlinx-coroutines-android
org.jetbrains.kotlinx:kotlinx-datetime
```

### 專案結構

```
app/src/main/java/com/medical/calendar/
├── MedicalCalendarApplication.kt       # Application 類別
├── MainActivity.kt                      # 主 Activity
├── auth/                                # 認證相關
│   └── GoogleSignInHelper.kt
├── data/                                # 資料層
│   ├── local/                          # 本地資料庫
│   │   ├── CalendarDatabase.kt
│   │   ├── CalendarDao.kt
│   │   └── CalendarEventDao.kt
│   ├── model/                          # 資料模型
│   │   ├── Calendar.kt
│   │   ├── CalendarEvent.kt
│   │   └── ...
│   ├── remote/                         # 遠端服務
│   │   ├── GoogleSheetsService.kt     # Google Sheets 同步
│   │   └── GoogleCalendarService.kt   # Google Calendar 同步
│   └── repository/                     # Repository
│       └── CalendarRepository.kt
├── di/                                 # 依賴注入
│   ├── AppModule.kt
│   └── DatabaseModule.kt
├── ui/                                 # UI 層
│   ├── calendar/                       # 行事曆畫面
│   ├── settings/                       # 設定畫面
│   ├── event/                          # 事件詳情
│   ├── navigation/                     # 導航
│   └── theme/                          # 主題
├── util/                               # 工具類別
│   ├── ColorManager.kt
│   ├── LunarCalendarUtil.kt
│   └── NetworkUtil.kt
└── widget/                             # Widget 小工具
    ├── CalendarWidgetProvider.kt
    └── WidgetUpdateWorker.kt
```

---

## 🎯 使用流程

### 首次使用

1. **安裝應用程式**
   - 在 Android Studio 中執行 app
   - 或建置 APK 並安裝到裝置

2. **登入 Google 帳號**
   - 開啟應用程式
   - 前往「設定」頁面
   - 點擊「登入 Google 帳號」
   - 選擇您的 Google 帳號並授權

3. **設定 Google Sheets**
   - 在設定頁面點擊「設定 Google Sheets」
   - 輸入您的 Sheet ID (從 Google Sheets 網址中取得)
   - 輸入資料範圍 (預設: `排班資料!A2:G`)
   - 點擊「測試連線」確認設定正確

4. **同步排班資料**
   - 點擊「立即同步」
   - 等待同步完成
   - 在行事曆頁面查看排班

### Google Sheets 格式

您的 Google Sheets 需要包含以下欄位（第1列為標題）：

| A欄 | B欄 | C欄 | D欄 | E欄 | F欄 | G欄 |
|-----|-----|-----|-----|-----|-----|-----|
| 日期 | 開始時間 | 結束時間 | 事業單位 | 人員名單 | 地點 | 顏色 |

**範例資料：**

```
| 2025-11-05 | 09:00 | 17:00 | 台北診所 | 王醫師, 李護理師 | 台北市中山區XXX路123號 | #FF5733 |
```

**欄位說明：**
- **日期**: YYYY-MM-DD 格式
- **開始時間**: HH:MM 格式
- **結束時間**: HH:MM 格式
- **事業單位**: 排班單位名稱
- **人員名單**: 多人用逗號分隔
- **地點**: 地址（選填，空白則使用事業單位名稱）
- **顏色**: 十六進位色碼（選填，空白則使用預設顏色 #667eea）

---

## 🐛 除錯

### 常見問題

#### 1. Gradle 同步失敗

**解決方法：**
```bash
# 清理專案
./gradlew clean

# 重新同步
File → Sync Project with Gradle Files
```

#### 2. Google 登入失敗

**檢查項目：**
- [ ] SHA-1 指紋是否正確設定在 Google Cloud Console
- [ ] OAuth 2.0 用戶端 ID 的套件名稱是否為 `com.medical.calendar`
- [ ] 測試帳號是否已加入到「測試使用者」名單

#### 3. Google Sheets 連線失敗

**檢查項目：**
- [ ] 是否已啟用 Google Sheets API
- [ ] Sheet ID 是否正確
- [ ] Google 帳號是否有權限存取該試算表
- [ ] 試算表的工作表名稱是否為「排班資料」

#### 4. 應用程式閃退

**檢查 Logcat：**
```
View → Tool Windows → Logcat
```
搜尋錯誤訊息並根據錯誤類型處理。

---

## 📦 建置 Release APK

### 1. 建立簽名金鑰

```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
```

### 2. 設定 build.gradle

在 `app/build.gradle` 中加入：

```gradle
android {
    signingConfigs {
        release {
            storeFile file("path/to/my-release-key.jks")
            storePassword "your-store-password"
            keyAlias "my-alias"
            keyPassword "your-key-password"
        }
    }
    
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### 3. 建置 APK

```bash
./gradlew assembleRelease
```

APK 位置：`app/build/outputs/apk/release/app-release.apk`

---

## 📝 授權

此專案為私人專案，僅供內部使用。

---

## 👥 聯絡資訊

如有問題或建議，請聯絡專案維護者。

---

**最後更新**: 2025-11-03
**版本**: 1.0.0

