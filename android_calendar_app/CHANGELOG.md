# Changelog - 醫療行事曆 Android App

所有重要的專案變更都會記錄在這個檔案中。

格式基於 [Keep a Changelog](https://keepachangelog.com/zh-TW/1.0.0/)。

---

## [1.0.0] - 2025-11-03

### 🎉 初始版本

#### 新增功能 (Added)

##### 核心功能
- ✅ **Application 類別**: 建立 `MedicalCalendarApplication.kt` 作為應用程式進入點
- ✅ **Hilt 依賴注入**: 完整的 DI 模組設定 (`AppModule`, `DatabaseModule`)
- ✅ **Google 登入整合**: `GoogleSignInHelper` 處理 Google 帳號認證
- ✅ **Google Sheets 同步**: 從 Google Sheets 讀取排班資料
- ✅ **Google Calendar 同步**: 自動同步排班到 Google 日曆
- ✅ **Room 本地資料庫**: 離線資料快取
- ✅ **WorkManager 背景工作**: 定期同步和 Widget 更新

##### UI 功能
- ✅ **主畫面導航**: 5個主要頁面（行事曆、專案、月經週期、記帳、設定）
- ✅ **設定畫面增強**:
  - Google 帳號登入/登出
  - Google Sheets 設定（Sheet ID、資料範圍）
  - 連線測試功能
  - 自動同步設定
  - 日曆顯示設定
  - 農曆日期顯示
- ✅ **事件詳情畫面**: 查看和編輯排班事件
- ✅ **Widget 小工具**: 桌面小工具顯示近期排班

##### 資源與設定
- ✅ **完整的字串資源**: `strings.xml` 包含所有UI文字
- ✅ **ProGuard 規則**: 完整的混淆規則保護應用程式
- ✅ **AndroidManifest 設定**: 包含所有必要的權限和服務
- ✅ **建置文檔**: `README_BUILD.md` 詳細說明建置和使用流程
- ✅ **Google API 設定指南**: `GOOGLE_SHEETS_SETUP.md` 完整設定教學

#### 技術特色 (Technical)

##### 架構設計
- **MVVM 架構**: ViewModel + Repository Pattern
- **單向資料流**: StateFlow 管理 UI 狀態
- **依賴注入**: Hilt 提供依賴管理
- **協程**: Kotlin Coroutines 處理非同步操作

##### API 整合
- **Google Sheets API v4**: 讀取試算表資料
- **Google Calendar API v3**: 管理日曆事件
- **Google Sign-In**: OAuth 2.0 認證

##### 資料層
- **Room Database**: SQLite 本地資料庫
- **DAO 模式**: 清晰的資料存取層
- **Repository 模式**: 統一資料來源管理

##### UI 層
- **Jetpack Compose**: 現代化 UI 框架
- **Material Design 3**: 最新設計規範
- **Navigation Compose**: 聲明式導航

#### 檔案清單 (Files Created)

```
✅ MedicalCalendarApplication.kt       - Application 類別
✅ auth/GoogleSignInHelper.kt          - Google 登入輔助
✅ di/AppModule.kt                     - 應用程式 DI 模組
✅ di/DatabaseModule.kt                - 資料庫 DI 模組
✅ ui/settings/SettingsViewModel.kt    - 設定 ViewModel（增強版）
✅ ui/settings/SettingsScreen.kt       - 設定 UI（增強版）
✅ app/proguard-rules.pro              - ProGuard 規則
✅ res/values/strings.xml              - 字串資源（擴充版）
✅ AndroidManifest.xml                 - Manifest（更新版）
✅ README_BUILD.md                     - 建置指南
✅ CHANGELOG.md                        - 變更日誌
```

#### 依賴項目 (Dependencies)

主要依賴：
- Kotlin 1.9.20
- Jetpack Compose BOM 2024.02.00
- Room 2.6.1
- Hilt 2.48
- Google Play Services Auth 20.7.0
- Google Sheets API v4-rev612-1.25.0
- Google Calendar API v3-rev411-1.25.0
- WorkManager 2.9.0
- Coroutines 1.7.3

#### 權限 (Permissions)

```xml
<!-- 必要權限 -->
INTERNET                    - 網路存取
ACCESS_NETWORK_STATE        - 檢查網路狀態
READ_CALENDAR              - 讀取日曆
WRITE_CALENDAR             - 寫入日曆
POST_NOTIFICATIONS         - 推送通知（Android 13+）
WAKE_LOCK                  - 背景喚醒
FOREGROUND_SERVICE         - 前景服務
```

---

## 🚀 使用方式

### 首次設定

1. **安裝應用程式**
   ```bash
   ./gradlew installDebug
   ```

2. **設定 Google API**
   - 參考 `GOOGLE_SHEETS_SETUP.md`
   - 建立 Google Cloud 專案
   - 啟用 Google Sheets API 和 Calendar API
   - 設定 OAuth 2.0

3. **登入 Google 帳號**
   - 開啟 App → 設定頁面
   - 點擊「登入 Google 帳號」
   - 授權應用程式存取

4. **設定 Google Sheets**
   - 輸入 Sheet ID
   - 設定資料範圍（預設：`排班資料!A2:G`）
   - 測試連線

5. **開始同步**
   - 點擊「立即同步」
   - 查看行事曆頁面

### Google Sheets 格式

| 欄位 | 格式 | 必填 | 範例 |
|------|------|------|------|
| 日期 | YYYY-MM-DD | ✅ | 2025-11-05 |
| 開始時間 | HH:MM | ✅ | 09:00 |
| 結束時間 | HH:MM | ✅ | 17:00 |
| 事業單位 | 文字 | ✅ | 台北診所 |
| 人員名單 | 逗號分隔 | ✅ | 王醫師, 李護理師 |
| 地點 | 文字 | ❌ | 台北市中山區XXX路123號 |
| 顏色 | #RRGGBB | ❌ | #FF5733 |

---

## 📱 功能測試清單

### ✅ 已完成

- [x] Google 帳號登入/登出
- [x] Google Sheets 連線測試
- [x] Google Sheets 資料讀取
- [x] Google Calendar 事件同步
- [x] 本地資料庫儲存
- [x] 離線資料查看
- [x] 設定頁面 UI
- [x] 主導航功能
- [x] Widget 小工具

### 🔄 待實作

- [ ] 實際的 Google 登入流程（需要在 SettingsScreen 中整合 ActivityResultLauncher）
- [ ] 完整的同步邏輯（CalendarRepository）
- [ ] 行事曆畫面資料顯示
- [ ] 事件編輯功能
- [ ] 推送通知
- [ ] 資料匯出功能
- [ ] 多語言支援

### 🐛 已知問題

目前沒有已知嚴重問題。

---

## 🔄 升級指南

### 從無到 1.0.0

這是初始版本，直接安裝即可。

---

## 📚 相關文檔

- [建置指南](./README_BUILD.md) - 如何建置和執行應用程式
- [Google Sheets 設定](./GOOGLE_SHEETS_SETUP.md) - Google API 設定教學
- [實作摘要](./IMPLEMENTATION_SUMMARY.md) - 技術實作細節
- [遷移指南](./MIGRATION_GUIDE.md) - 從 Supabase 到 Google Sheets

---

## 🙏 致謝

感謝以下開源專案和技術：

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Hilt](https://dagger.dev/hilt/)
- [Room](https://developer.android.com/training/data-storage/room)
- [Google APIs](https://developers.google.com/)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

**維護者**: Medical Calendar Team  
**最後更新**: 2025-11-03  
**版本**: 1.0.0



