# 醫療排班系統 - 部署計畫

本文件說明醫療排班系統的階段性部署策略。

---

## 📋 部署階段

### ✅ 階段 1：網頁版（現在 - 立即可用）

#### 現狀
- **檔案**: `shift_management_system.html`
- **狀態**: ✅ 已完成，可直接使用
- **功能**: 完整的排班管理功能

#### 使用方式

##### 方案 A：本機使用
```bash
# 直接開啟檔案
雙擊: shift_management_system.html
```

##### 方案 B：區域網路使用
```bash
# 啟動本機伺服器
cd C:\Users\josie\staff-scheduling-system
python -m http.server 8000

# 同網域的裝置可存取
http://[電腦IP]:8000/shift_management_system.html
```

##### 方案 C：部署到網路（推薦）
```bash
# 使用 GitHub Pages（免費）
1. 上傳到 GitHub Repository
2. 啟用 GitHub Pages
3. 得到公開網址

# 或使用 Netlify（免費）
1. 拖放檔案到 Netlify
2. 自動部署
3. 得到 HTTPS 網址
```

#### 功能清單
- ✅ 排班表檢視（月/週/日）
- ✅ 新增/編輯/刪除排班
- ✅ 匯出功能
- ✅ 響應式設計（手機/平板/電腦都能用）
- ✅ 本地儲存（資料存在瀏覽器）

---

### 🔄 階段 2：Android App（未來需要時）

#### 現狀
- **位置**: `android_calendar_app/`
- **狀態**: ✅ 架構已建立完成
- **需要**: 約 2-3 小時設定 Google API 和建置

#### 何時需要啟動階段 2？

當您需要以下功能時：

1. **原生手機體驗**
   - ❓ 需要手機 App 圖示
   - ❓ 需要原生操作體驗
   - ❓ 需要離線查看（本地資料庫）

2. **手機日曆整合**
   - ❓ 自動同步到 Google Calendar
   - ❓ 顯示在手機原生日曆
   - ❓ 跨裝置同步

3. **推送通知**
   - ❓ 排班提醒通知
   - ❓ 排班變更通知
   - ❓ 自訂提醒時間

4. **桌面小工具**
   - ❓ 在手機桌面顯示今日排班
   - ❓ 快速查看近期班表

#### 啟動步驟（未來執行）

```bash
# 第 1 步：設定 Google Cloud API（30 分鐘）
1. 建立 Google Cloud 專案
2. 啟用 Google Sheets API
3. 啟用 Google Calendar API
4. 設定 OAuth 2.0

# 第 2 步：建置 Android App（1 小時）
1. 開啟 Android Studio
2. 載入專案
3. 取得 SHA-1 指紋
4. 設定 Google API 憑證
5. 建置並執行

# 第 3 步：測試和部署（30 分鐘）
1. 在測試裝置上安裝
2. 測試所有功能
3. 產生 Release APK
4. 分發給使用者
```

#### 已準備就緒的功能

✅ **核心架構**
- Application 類別 (`MedicalCalendarApplication.kt`)
- Hilt 依賴注入模組
- Room 本地資料庫
- Repository Pattern

✅ **Google 整合**
- Google Sign-In (`GoogleSignInHelper.kt`)
- Google Sheets 服務 (`GoogleSheetsService.kt`)
- Google Calendar 服務 (`GoogleCalendarService.kt`)

✅ **UI 介面**
- Jetpack Compose + Material Design 3
- 主導航（5 個頁面）
- 設定畫面（完整）
- 事件詳情/編輯畫面

✅ **文檔**
- 建置指南 (`README_BUILD.md`)
- 快速開始 (`QUICK_START_CN.md`)
- Google API 設定 (`GOOGLE_SHEETS_SETUP.md`)
- 專案總覽 (`PROJECT_SUMMARY.md`)

---

## 🎯 推薦時程

### 現在（階段 1）

```
目標：開始使用排班系統
時間：5 分鐘
行動：
  1. 開啟 shift_management_system.html
  2. 測試功能
  3. 開始管理排班
```

### 1-2 週後（評估）

```
評估：網頁版是否滿足需求？
問題：
  ☐ 手機瀏覽體驗是否足夠？
  ☐ 是否需要日曆整合？
  ☐ 是否需要推送通知？
  
決定：
  → 需求滿足 = 繼續使用網頁版
  → 需要更多功能 = 啟動階段 2
```

### 需要時（階段 2）

```
目標：建置 Android App
時間：3-4 小時
前置準備：
  1. 確認有 Android Studio
  2. 準備 Google 帳號
  3. 準備測試裝置
  
執行：
  參考 android_calendar_app/QUICK_START_CN.md
```

---

## 💰 成本比較

### 階段 1：網頁版

| 項目 | 成本 |
|------|------|
| 開發 | $0（已完成）|
| 伺服器 | $0（GitHub Pages 或本機）|
| 維護 | $0（HTML 檔案）|
| **總計** | **$0** |

### 階段 2：Android App

| 項目 | 成本 |
|------|------|
| 開發 | $0（已完成）|
| Google Cloud | $0（免費額度足夠）|
| Play Store | $25（一次性，可選）|
| 維護 | 時間成本 |
| **總計** | **$0-25** |

---

## 📊 功能對照表

| 功能 | 網頁版 | Android App |
|------|--------|-------------|
| 排班管理 | ✅ | ✅ |
| 檢視排班 | ✅ | ✅ |
| 匯出資料 | ✅ | ✅ |
| 手機存取 | ✅（瀏覽器）| ✅（原生）|
| 離線使用 | ⚠️（有限）| ✅ |
| Google Calendar 整合 | ❌ | ✅ |
| 手機日曆同步 | ❌ | ✅ |
| 推送通知 | ❌ | ✅ |
| 桌面小工具 | ❌ | ✅ |
| 跨裝置同步 | ⚠️（手動）| ✅（自動）|
| 安裝需求 | 無 | Android 裝置 |
| 設定難度 | ⭐ 簡單 | ⭐⭐⭐ 中等 |

---

## 🚀 快速決策指南

### 只用網頁版的情況

```
✅ 主要在電腦上使用
✅ 不需要日曆整合
✅ 不需要推送通知
✅ 手機偶爾用瀏覽器查看就好
✅ 希望零設定、立即可用
```

### 需要 Android App 的情況

```
❗ 需要頻繁在手機上查看排班
❗ 需要整合手機日曆（自動顯示在日曆 App）
❗ 需要排班提醒通知
❗ 需要桌面小工具快速查看
❗ 需要多裝置自動同步
❗ 需要離線查看
```

---

## 📝 行動檢查清單

### ✅ 階段 1 檢查清單（現在）

- [ ] 開啟 `shift_management_system.html`
- [ ] 測試排班新增功能
- [ ] 測試排班編輯功能
- [ ] 測試排班刪除功能
- [ ] 測試匯出功能
- [ ] 在手機瀏覽器測試（如需要）
- [ ] 決定是否需要部署到網路

### 🔄 階段 2 檢查清單（未來）

當決定要建置 Android App 時：

- [ ] 閱讀 `android_calendar_app/QUICK_START_CN.md`
- [ ] 安裝 Android Studio
- [ ] 取得 SHA-1 指紋
- [ ] 設定 Google Cloud 專案
- [ ] 啟用 Google APIs
- [ ] 設定 OAuth 2.0
- [ ] 建置 App
- [ ] 測試功能
- [ ] 產生 APK
- [ ] 分發給使用者

---

## 📞 需要協助時

### 階段 1 問題

如果網頁版遇到問題：
- 檢查瀏覽器相容性
- 清除瀏覽器快取
- 檢查 JavaScript 是否啟用

### 階段 2 問題

如果要建置 Android App：
- 參考 `android_calendar_app/GOOGLE_SHEETS_SETUP.md`
- 查看 `android_calendar_app/README_BUILD.md`
- 檢查 Logcat 錯誤訊息

---

## 🎊 總結

**您的明智選擇**：

✅ **現在**：使用網頁版，零設定，立即可用  
🔄 **未來**：需要時才建置 Android App，架構已準備好

**現在就能做的事**：
1. 雙擊 `shift_management_system.html`
2. 開始管理排班
3. 享受使用！

**未來要做的事**：
1. 當需要手機 App 功能時
2. 參考 `QUICK_START_CN.md`
3. 約 3 小時完成設定和建置

---

**最後更新**: 2025-11-03  
**當前階段**: 階段 1（網頁版）  
**下階段**: 待定（根據需求決定）



