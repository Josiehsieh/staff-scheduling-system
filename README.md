# 🏥 醫療人員排班管理系統

一個功能完整的醫療人員排班管理系統，支援 Google Sheets 儲存和 Google Calendar 同步。

## 🌐 線上使用

**方法 1：使用 GitHub Pages（推薦）**

如果您已部署到 GitHub Pages，可以直接訪問：
```
https://your-username.github.io/staff-scheduling-system/shift_management_system_sheets_full.html
```

**方法 2：本地使用**

1. 下載或 clone 此專案
2. 使用 Python 啟動 HTTP Server：
   ```bash
   python -m http.server 8000
   ```
3. 開啟瀏覽器訪問：
   ```
   http://localhost:8000/shift_management_system_sheets_full.html
   ```

---

## ⚙️ 首次設定（必須完成）

### 步驟 1：建立 Google Cloud 專案

1. 前往 [Google Cloud Console](https://console.cloud.google.com)
2. 點擊「選取專案」→「新增專案」
3. 專案名稱：`排班管理系統`
4. 點擊「建立」

### 步驟 2：啟用 API

前往「API 和服務」→「程式庫」，搜尋並啟用：
- ✅ Google Sheets API
- ✅ Google Calendar API

### 步驟 3：設定 OAuth 同意畫面

1. 前往「API 和服務」→「OAuth 同意畫面」
2. 選擇「外部」→「建立」
3. 填寫必要資訊：
   - 應用程式名稱：`Medical Shift System`
   - 使用者支援電子郵件：您的 Gmail
   - 開發人員聯絡資訊：您的 Gmail
4. 在「範圍」頁面，新增：
   - `https://www.googleapis.com/auth/spreadsheets`
   - `https://www.googleapis.com/auth/calendar`
5. 在「測試使用者」頁面，新增您的 Gmail 地址

### 步驟 4：建立 OAuth 用戶端 ID

1. 前往「API 和服務」→「憑證」
2. 點擊「建立憑證」→「OAuth 用戶端 ID」
3. **應用程式類型：網頁應用程式**（重要！）
4. 名稱：`Medical Shift System Client`

#### 如果使用 GitHub Pages：

**已授權的 JavaScript 來源：**
```
https://your-username.github.io
```

**已授權的重新導向 URI：**
```
https://your-username.github.io/staff-scheduling-system/
https://your-username.github.io/staff-scheduling-system/shift_management_system_sheets_full.html
```

#### 如果使用本地 (localhost)：

**已授權的 JavaScript 來源：**
```
http://localhost:8000
```

**已授權的重新導向 URI：**
```
http://localhost:8000
http://localhost:8000/
http://localhost:8000/shift_management_system_sheets_full.html
```

5. 點擊「建立」並複製用戶端 ID

### 步驟 5：在系統中設定

1. 開啟系統
2. 點擊「⚙️ 設定」頁籤
3. 在「Google API 設定」區域：
   - 貼上您的用戶端 ID
   - 輸入 Google Sheet ID（如果有）
4. 點擊「儲存設定」
5. 點擊「🔐 登入 Google」
6. 授權應用程式

---

## ✨ 功能特色

### 📅 排班管理
- ✅ 視覺化月曆介面
- ✅ 快速新增/編輯/刪除排班
- ✅ 重複排程功能
- ✅ 拖曳移動排班（規劃中）

### 📊 資料管理
- ✅ 匯出 Excel 報表
- ✅ 同步到 Google Sheets
- ✅ 自動統計時數

### 📆 Google Calendar 整合
- ✅ 手動上傳到 Google Calendar
- ✅ 自動同步功能（可選）
- ✅ 批量上傳多個排班

### 👥 人員管理
- ✅ 新增/編輯人員資料
- ✅ 支援多個事業單位
- ✅ 人員時數統計

### 📍 地點管理
- ✅ 自訂工作地點
- ✅ 快速選擇常用地點

---

## 📚 詳細文檔

- [API設定快速參考](./API設定快速參考.md) - 最簡化的設定步驟
- [Google行事曆API設定指南](./Google行事曆API設定指南.md) - 完整詳細的設定說明
- [登入問題故障排除](./登入問題故障排除.md) - 遇到登入問題時查看
- [修正 OAuth 用戶端類型錯誤](./修正%20OAuth%20用戶端類型錯誤.md) - 修正常見的配置錯誤
- [如何在現有專案中加入行事曆功能](./如何在現有專案中加入行事曆功能.md)
- [自動同步快速開始](./自動同步快速開始.md) - 設定自動同步到 Google Calendar
- [部署到 GitHub Pages 說明](./部署到%20GitHub%20Pages%20說明.md) - 部署指南

---

## ⚠️ 常見問題

### 無法登入 Google

**錯誤：`redirect_uri=storagerelay://`**
- 原因：選擇了「桌面應用程式」而非「網頁應用程式」
- 解決：參考 [修正 OAuth 用戶端類型錯誤.md](./修正%20OAuth%20用戶端類型錯誤.md)

**錯誤：`Error 400: invalid_request`**
- 原因：OAuth 同意畫面設定不完整
- 解決：確認已填寫所有必填欄位並新增測試使用者

**錯誤：`Access blocked`**
- 原因：未在測試使用者中加入您的 Gmail
- 解決：前往 OAuth 同意畫面 → 測試使用者 → 新增您的 Gmail

### 無法儲存到 Google Sheets

- 確認已啟用 Google Sheets API
- 確認已在設定中填入正確的 Sheet ID
- 確認該 Sheet 已分享給您的 Google 帳號（可編輯權限）

### 無法上傳到 Google Calendar

- 確認已啟用 Google Calendar API
- 確認 OAuth 範圍包含 `calendar` 權限
- 重新登入以獲取新權限

---

## 🔐 安全性說明

### 用戶端 ID 安全

此系統使用 **OAuth 2.0 授權流程**：

- ✅ 用戶端 ID 可以公開（設計如此）
- ✅ 每個用戶使用自己的 Google 帳號授權
- ✅ 資料儲存在用戶自己的 Google 帳號中
- ✅ 沒有後端伺服器，不會洩漏資料

### 建議做法

**個人使用：**
- 建立自己的 Google Cloud 專案
- 使用自己的用戶端 ID

**團隊使用（小型團隊）：**
- 可以共用一個 Google Cloud 專案
- 將所有團隊成員加入測試使用者
- 每個人授權存取自己的資料

**大型組織：**
- 建議部署到私有伺服器
- 實作完整的用戶管理系統
- 申請 Google OAuth 驗證

---

## 🛠️ 技術架構

- **前端**：純 HTML + CSS + JavaScript
- **資料儲存**：Google Sheets API
- **行事曆整合**：Google Calendar API
- **認證**：Google OAuth 2.0（使用 Google Identity Services）
- **匯出**：SheetJS (xlsx.js)

### 不需要的技術

- ❌ 不需要後端伺服器
- ❌ 不需要資料庫
- ❌ 不需要 Node.js 或其他套件管理
- ❌ 不需要編譯或建置過程

---

## 📦 專案結構

```
staff-scheduling-system/
├── shift_management_system_sheets_full.html  # 主要系統（推薦使用）
├── shift_management_system_sheets.html       # 簡化版本
├── shift_management_system.html              # 基本版本（無 Google 整合）
├── API設定快速參考.md                         # 快速設定指南
├── Google行事曆API設定指南.md                 # 詳細設定指南
├── 登入問題故障排除.md                        # 疑難排解
└── 其他文檔...                                 # 各種功能說明
```

---

## 🚀 部署到 GitHub Pages

### 快速部署

1. **建立 GitHub Repository**
   ```bash
   # 在專案目錄中
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/your-username/staff-scheduling-system.git
   git push -u origin main
   ```

2. **啟用 GitHub Pages**
   - 前往 Repository → Settings → Pages
   - Source: Deploy from a branch
   - Branch: main
   - Folder: / (root)
   - Save

3. **更新 OAuth 設定**
   - 在 Google Cloud Console 的 OAuth 用戶端 ID 中
   - 新增 GitHub Pages 的網址到重新導向 URI
   - 格式：`https://your-username.github.io/staff-scheduling-system/`

4. **測試**
   - 訪問：`https://your-username.github.io/staff-scheduling-system/shift_management_system_sheets_full.html`
   - 登入並測試所有功能

詳細步驟請參考：[部署到 GitHub Pages 說明.md](./部署到%20GitHub%20Pages%20說明.md)

---

## 📄 授權

此專案供內部使用。請根據您的需求調整授權條款。

---

## 🆘 需要幫助？

- 查看 [登入問題故障排除.md](./登入問題故障排除.md)
- 查看 [API設定快速參考.md](./API設定快速參考.md)
- 檢查瀏覽器 Console (F12) 的錯誤訊息

---

## 📝 更新日誌

### v6.2.0 (2025-11)
- ✅ 新增 Google Calendar 自動同步功能
- ✅ 改進 OAuth 登入流程
- ✅ 完善文檔和故障排除指南

### v6.1.0
- ✅ 整合 Google Sheets API
- ✅ 新增 Google Calendar 手動上傳
- ✅ 改進使用者介面

---

**版本**：v6.2.0  
**最後更新**：2025-11-11


