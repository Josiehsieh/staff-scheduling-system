# 醫療行事曆 Android App - 專案總覽

## 📱 專案概述

**醫療行事曆** 是一個專為醫療機構設計的排班管理 Android 應用程式，整合 Google Sheets 和 Google Calendar，提供便捷的排班查看和同步功能。

### 核心特色

- 🔄 **Google Sheets 整合** - 直接從 Google Sheets 讀取排班資料
- 📅 **Google Calendar 同步** - 自動同步到 Google 日曆和手機日曆
- 💾 **離線支援** - 本地資料庫快取，離線也能查看排班
- 🎨 **現代化 UI** - Jetpack Compose + Material Design 3
- 🔔 **智能提醒** - 排班提醒通知
- 📊 **多功能整合** - 專案管理、月經週期追蹤、記帳功能

---

## 🏗️ 技術架構

### 開發環境

```
開發語言: Kotlin
最低 SDK: Android 7.0 (API 24)
目標 SDK: Android 14 (API 34)
建置工具: Gradle 8.3.0
IDE: Android Studio Hedgehog+
```

### 核心技術棧

#### UI 層
- **Jetpack Compose** - 現代化聲明式 UI
- **Material Design 3** - Google 最新設計規範
- **Navigation Compose** - 類型安全的導航

#### 資料層
- **Room Database** - 本地 SQLite 資料庫
- **Kotlin Coroutines** - 非同步程式設計
- **Flow / StateFlow** - 響應式資料流

#### 業務層
- **MVVM 架構** - Model-View-ViewModel
- **Repository Pattern** - 統一資料來源
- **Use Case** - 業務邏輯封裝

#### 依賴注入
- **Hilt** - Google 推薦的 DI 框架
- **Dagger** - 編譯時依賴注入

#### 背景工作
- **WorkManager** - 定期背景任務
- **Foreground Service** - 長時間執行任務

#### Google API
- **Google Sheets API v4** - 試算表讀取
- **Google Calendar API v3** - 日曆管理
- **Google Sign-In** - OAuth 2.0 認證

---

## 📂 專案結構

```
android_calendar_app/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/medical/calendar/
│   │   │   │   ├── MedicalCalendarApplication.kt    # App 進入點
│   │   │   │   ├── MainActivity.kt                   # 主 Activity
│   │   │   │   │
│   │   │   │   ├── auth/                            # 認證模組
│   │   │   │   │   └── GoogleSignInHelper.kt       # Google 登入
│   │   │   │   │
│   │   │   │   ├── data/                            # 資料層
│   │   │   │   │   ├── local/                      # 本地資料庫
│   │   │   │   │   │   ├── CalendarDatabase.kt
│   │   │   │   │   │   ├── CalendarDao.kt
│   │   │   │   │   │   └── CalendarEventDao.kt
│   │   │   │   │   ├── model/                      # 資料模型
│   │   │   │   │   │   ├── Calendar.kt
│   │   │   │   │   │   ├── CalendarEvent.kt
│   │   │   │   │   │   ├── Finance.kt
│   │   │   │   │   │   ├── Holiday.kt
│   │   │   │   │   │   ├── MenstrualCycle.kt
│   │   │   │   │   │   └── Project.kt
│   │   │   │   │   ├── remote/                     # 遠端服務
│   │   │   │   │   │   ├── GoogleSheetsService.kt # Sheets 同步
│   │   │   │   │   │   ├── GoogleCalendarService.kt
│   │   │   │   │   │   └── ShiftSystemService.kt
│   │   │   │   │   └── repository/                 # Repository
│   │   │   │   │       └── CalendarRepository.kt
│   │   │   │   │
│   │   │   │   ├── di/                             # 依賴注入
│   │   │   │   │   ├── AppModule.kt               # 應用模組
│   │   │   │   │   └── DatabaseModule.kt          # 資料庫模組
│   │   │   │   │
│   │   │   │   ├── ui/                             # UI 層
│   │   │   │   │   ├── calendar/                  # 行事曆畫面
│   │   │   │   │   │   ├── CalendarScreen.kt
│   │   │   │   │   │   └── CalendarViewModel.kt
│   │   │   │   │   ├── event/                     # 事件詳情
│   │   │   │   │   │   ├── EventDetailScreen.kt
│   │   │   │   │   │   ├── EventDetailViewModel.kt
│   │   │   │   │   │   ├── EventEditScreen.kt
│   │   │   │   │   │   └── EventEditViewModel.kt
│   │   │   │   │   ├── settings/                  # 設定畫面
│   │   │   │   │   │   ├── SettingsScreen.kt
│   │   │   │   │   │   └── SettingsViewModel.kt
│   │   │   │   │   ├── projects/                  # 專案管理
│   │   │   │   │   ├── menstrual/                 # 月經週期
│   │   │   │   │   ├── finance/                   # 記帳功能
│   │   │   │   │   ├── navigation/                # 導航
│   │   │   │   │   │   ├── CalendarNavHost.kt
│   │   │   │   │   │   └── Screen.kt
│   │   │   │   │   └── theme/                     # 主題
│   │   │   │   │       ├── Color.kt
│   │   │   │   │       ├── Theme.kt
│   │   │   │   │       └── Type.kt
│   │   │   │   │
│   │   │   │   ├── util/                           # 工具類
│   │   │   │   │   ├── ColorManager.kt
│   │   │   │   │   ├── LunarCalendarUtil.kt
│   │   │   │   │   └── NetworkUtil.kt
│   │   │   │   │
│   │   │   │   └── widget/                         # Widget
│   │   │   │       ├── CalendarWidgetProvider.kt
│   │   │   │       ├── CalendarWidgetService.kt
│   │   │   │       └── WidgetUpdateWorker.kt
│   │   │   │
│   │   │   ├── AndroidManifest.xml                 # Manifest
│   │   │   └── res/                                # 資源檔案
│   │   │       ├── drawable/                       # 圖片資源
│   │   │       ├── layout/                         # XML Layout
│   │   │       ├── values/                         # 字串、顏色等
│   │   │       │   └── strings.xml
│   │   │       └── xml/                            # 設定檔
│   │   │
│   │   └── test/                                    # 測試
│   │
│   ├── build.gradle                                 # App 建置腳本
│   └── proguard-rules.pro                          # ProGuard 規則
│
├── build.gradle                                     # 專案建置腳本
├── settings.gradle                                  # Gradle 設定
├── gradle.properties                                # Gradle 屬性
│
└── 文檔/
    ├── README.md                                    # 專案說明
    ├── README_BUILD.md                              # 建置指南
    ├── QUICK_START_CN.md                           # 快速開始
    ├── GOOGLE_SHEETS_SETUP.md                      # Google API 設定
    ├── CHANGELOG.md                                 # 變更日誌
    ├── PROJECT_SUMMARY.md                          # 專案總覽（本檔）
    └── IMPLEMENTATION_SUMMARY.md                   # 實作細節
```

---

## 🔄 資料流程

### 排班同步流程

```
┌─────────────────┐
│  Google Sheets  │ (資料來源)
│   排班試算表     │
└────────┬────────┘
         │
         ↓ Google Sheets API (讀取)
         │
┌────────┴────────┐
│ GoogleSheets    │
│    Service      │ (解析資料)
└────────┬────────┘
         │
         ↓ Repository
         │
┌────────┴────────┐
│  Room Database  │ (本地快取)
│    (SQLite)     │
└────────┬────────┘
         │
         ↓ ViewModel
         │
┌────────┴────────┐
│  Compose UI     │ (顯示)
│  CalendarScreen │
└─────────────────┘
         │
         ↓ Google Calendar API (寫入)
         │
┌─────────────────┐
│ Google Calendar │ (雲端日曆)
│  「醫療排班」    │
└────────┬────────┘
         │
         ↓ 自動同步
         │
┌─────────────────┐
│  手機日曆 App   │ (系統整合)
└─────────────────┘
```

### 使用者操作流程

```
1. 使用者開啟 App
   ↓
2. MainActivity 啟動
   ↓
3. 檢查 Google 登入狀態
   ├─ 未登入 → 顯示登入提示
   └─ 已登入 → 載入資料
      ↓
4. 從本地資料庫載入快取資料 (即時顯示)
   ↓
5. 背景同步最新資料
   ├─ Google Sheets → 讀取排班
   └─ Google Calendar → 寫入事件
   ↓
6. 更新 UI 顯示最新資料
```

---

## 🔒 安全性

### 資料保護

- **OAuth 2.0** - 安全的 Google 帳號認證
- **加密儲存** - 敏感資料加密存儲
- **HTTPS** - 所有網路請求使用 HTTPS
- **ProGuard** - 程式碼混淆保護

### 權限管理

```xml
必要權限:
- INTERNET              # 網路存取
- READ_CALENDAR         # 讀取日曆
- WRITE_CALENDAR        # 寫入日曆
- POST_NOTIFICATIONS    # 通知 (Android 13+)

可選權限:
- WAKE_LOCK            # 背景喚醒
- FOREGROUND_SERVICE   # 前景服務
```

---

## 📊 資料模型

### CalendarEvent (排班事件)

```kotlin
data class CalendarEvent(
    val id: Long = 0,
    val title: String,                    // 事件標題
    val description: String?,             // 事件描述
    val startTime: LocalDateTime,         // 開始時間
    val endTime: LocalDateTime,           // 結束時間
    val location: String?,                // 地點
    val calendarType: CalendarType,       // 日曆類型
    val calendarId: String,               // 日曆 ID
    val eventId: String?,                 // Google Calendar Event ID
    val color: String = "#667eea",        // 顏色
    val isAllDay: Boolean = false,        // 是否全天
    val reminders: List<Int> = listOf(60) // 提醒時間（分鐘）
)
```

### Calendar (日曆)

```kotlin
data class Calendar(
    val id: Long = 0,
    val name: String,                     // 日曆名稱
    val calendarType: CalendarType,       // 類型
    val color: String,                    // 顏色
    val isVisible: Boolean = true,        // 是否顯示
    val googleCalendarId: String?,        // Google Calendar ID
    val createdAt: LocalDateTime,         // 建立時間
    val updatedAt: LocalDateTime          // 更新時間
)
```

### CalendarType (日曆類型)

```kotlin
enum class CalendarType {
    MEDICAL_SHIFT,      // 醫療排班
    PERSONAL,           // 個人行事曆
    GOOGLE_CALENDAR,    // Google 日曆
    PROJECT,            // 專案
    HOLIDAY             // 假日
}
```

---

## 🎨 UI 設計

### 主要畫面

1. **行事曆畫面** (`CalendarScreen`)
   - 月檢視
   - 日檢視
   - 週檢視
   - 事件列表

2. **設定畫面** (`SettingsScreen`)
   - Google 帳號管理
   - Google Sheets 設定
   - 同步設定
   - 日曆設定
   - 顯示設定

3. **事件詳情畫面** (`EventDetailScreen`)
   - 事件資訊
   - 編輯功能
   - 分享功能

4. **事件編輯畫面** (`EventEditScreen`)
   - 標題、描述輸入
   - 時間選擇器
   - 地點選擇
   - 顏色選擇

### 設計規範

- **色彩**: Material Design 3 動態配色
- **排版**: Roboto 字體系列
- **間距**: 8dp 網格系統
- **圖示**: Material Icons Extended

---

## 🚀 效能優化

### 載入優化

- **漸進式載入** - 先顯示快取，再更新網路資料
- **分頁載入** - 大量資料分批載入
- **圖片快取** - Coil 圖片載入和快取

### 記憶體優化

- **LazyColumn** - 回收視圖
- **Flow** - 非同步資料流
- **避免記憶體洩漏** - Lifecycle 感知組件

### 網路優化

- **請求合併** - 批次處理 API 請求
- **快取策略** - 本地快取減少網路請求
- **超時處理** - 合理的超時和重試機制

---

## 📈 未來規劃

### Phase 2 功能

- [ ] 推送通知完整實作
- [ ] 排班衝突偵測
- [ ] 排班統計報表
- [ ] 匯出 PDF/Excel
- [ ] 多人協作功能

### Phase 3 功能

- [ ] 班表範本管理
- [ ] 自動排班建議
- [ ] 假勤管理整合
- [ ] 多語言支援
- [ ] Dark Mode 優化

### 技術優化

- [ ] 完整的單元測試
- [ ] UI 測試
- [ ] CI/CD 整合
- [ ] 效能監控
- [ ] 錯誤追蹤 (Firebase Crashlytics)

---

## 📚 相關資源

### 開發文檔

- [建置指南](./README_BUILD.md)
- [快速開始](./QUICK_START_CN.md)
- [Google Sheets 設定](./GOOGLE_SHEETS_SETUP.md)
- [變更日誌](./CHANGELOG.md)

### 技術文檔

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Hilt 依賴注入](https://developer.android.com/training/dependency-injection/hilt-android)
- [Room 資料庫](https://developer.android.com/training/data-storage/room)
- [Google Sheets API](https://developers.google.com/sheets/api)
- [Google Calendar API](https://developers.google.com/calendar/api)

---

## 🤝 貢獻指南

### 開發流程

1. Fork 專案
2. 建立功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交變更 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 開啟 Pull Request

### 程式碼規範

- **Kotlin** 程式碼遵循 [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- **Compose** UI 遵循 [Compose API Guidelines](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-api-guidelines.md)
- **命名規範** 使用有意義的變數和函數名稱
- **註解** 複雜邏輯需要適當註解

---

## 📞 聯絡方式

**專案維護者**: Medical Calendar Team  
**Email**: [專案聯絡信箱]  
**GitHub**: [專案 GitHub 連結]

---

## 📄 授權

此專案為私人專案，僅供內部使用。

---

**最後更新**: 2025-11-03  
**版本**: 1.0.0  
**狀態**: ✅ 可用



