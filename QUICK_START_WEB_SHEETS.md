# 🚀 網頁版 Google Sheets 快速開始

30 分鐘完成設定，開始使用網頁版排班系統！

---

## 📋 需要準備

- ✅ Google 帳號
- ✅ 瀏覽器（Chrome 推薦）
- ✅ 30 分鐘時間

---

## 🎯 三步驟完成設定

### 步驟 1：設定 Google Cloud API（15 分鐘）

📖 **詳細步驟請看**：`WEB_GOOGLE_SHEETS_SETUP.md`

**快速版**：

1. 前往 [Google Cloud Console](https://console.cloud.google.com/)
2. 建立專案：「Medical Shift System」
3. 啟用「Google Sheets API」
4. 設定「OAuth 同意畫面」
   - 類型：外部
   - 加入測試使用者（您的 Google 帳號）
5. 建立「OAuth 2.0 用戶端 ID」
   - 類型：**網頁應用程式**
   - 授權 JavaScript 來源：`http://localhost` 和 `http://localhost:8000`
6. **記下用戶端 ID**（類似：`123456789-abc...apps.googleusercontent.com`）

✅ 完成後應該有：**用戶端 ID**

---

### 步驟 2：建立 Google Sheets（5 分鐘）

1. 前往 [Google Sheets](https://sheets.google.com)
2. 建立新試算表：「醫療排班資料」
3. 工作表命名為：「排班資料」
4. 第 1 列輸入標題：

| A | B | C | D | E | F | G | H |
|---|---|---|---|---|---|---|---|
| 日期 | 開始時間 | 結束時間 | 事業單位 | 人員名單 | 地點 | 顏色 | 備註 |

5. 第 2 列輸入測試資料：

| A | B | C | D | E | F | G | H |
|---|---|---|---|---|---|---|---|
| 2025-11-05 | 09:00 | 17:00 | 台北診所 | 王醫師, 李護理師 | 台北市 | #FF5733 | 日班 |

6. **記下 Sheet ID**（從網址取得）
   ```
   https://docs.google.com/spreadsheets/d/【Sheet ID】/edit
   ```

✅ 完成後應該有：**Sheet ID**

---

### 步驟 3：開啟網頁版並設定（10 分鐘）

#### 3.1 開啟 HTML 檔案

⚠️ **重要**：Google OAuth 需要使用 http:// 協定，不能直接雙擊 HTML 檔案！

**使用本機伺服器開啟**：

##### Windows 使用者（PowerShell）

```powershell
# 1. 開啟 PowerShell
按 Win + X，選擇「Windows PowerShell」

# 2. 切換到專案目錄
cd C:\Users\josie\staff-scheduling-system

# 3. 啟動本機伺服器
python -m http.server 8000

# 4. 看到「Serving HTTP on...」表示成功

# 5. 開啟瀏覽器，輸入網址：
http://localhost:8000/shift_management_system_sheets.html
```

##### 如果沒有 Python

**方法 A：安裝 Python（推薦）**
1. 下載：https://www.python.org/downloads/
2. 安裝時勾選「Add Python to PATH」
3. 重新開啟 PowerShell，執行上面指令

**方法 B：使用 VSCode（如果有安裝）**
1. 用 VSCode 開啟專案資料夾
2. 安裝「Live Server」擴充功能
3. 右鍵點擊 HTML 檔案 → 「Open with Live Server」

**方法 C：使用 Node.js（如果有安裝）**
```bash
npx http-server -p 8000
```

#### 3.2 登入 Google 帳號

1. 點擊「使用 Google 帳號登入」
2. 選擇您的 Google 帳號
3. 授權應用程式存取 Google Sheets

#### 3.3 填入設定

1. **Google API 用戶端 ID**：貼上步驟 1 取得的用戶端 ID
2. **Google Sheets ID**：貼上步驟 2 取得的 Sheet ID
3. **資料範圍**：保持預設 `排班資料!A2:H`
4. 點擊「💾 儲存設定」

#### 3.4 測試連線

1. 點擊「✅ 測試連線」
2. 看到「連線成功」訊息

#### 3.5 載入資料

1. 點擊「🔄 載入排班資料」
2. 看到測試資料顯示在表格中

✅ **完成！開始使用！**

---

## 🎉 開始使用

### 查看排班

1. 開啟網頁版
2. 登入 Google 帳號
3. 點擊「載入排班資料」

### 新增/編輯排班

1. 開啟 [Google Sheets](https://sheets.google.com)
2. 找到「醫療排班資料」試算表
3. 在「排班資料」工作表中編輯
4. 回到網頁版，點擊「載入排班資料」更新

### 資料格式

| 欄位 | 格式 | 範例 |
|------|------|------|
| 日期 | YYYY-MM-DD | 2025-11-05 |
| 開始時間 | HH:MM | 09:00 |
| 結束時間 | HH:MM | 17:00 |
| 事業單位 | 文字 | 台北診所 |
| 人員名單 | 逗號分隔 | 王醫師, 李護理師 |
| 地點 | 文字 | 台北市中山區 |
| 顏色 | #RRGGBB | #FF5733 |
| 備註 | 文字 | 日班 |

---

## ❓ 常見問題

### Q: 為什麼需要本機伺服器？

**A**: OAuth 2.0 需要從 `http://` 或 `https://` 協定執行。
- ✅ 推薦：`http://localhost:8000`
- ❌ 不行：`file:///C:/...`

### Q: 登入失敗怎麼辦？

**A**: 檢查以下項目：
1. OAuth 用戶端 ID 類型是否為「網頁應用程式」
2. 授權的 JavaScript 來源是否正確
3. 您的 Google 帳號是否加入「測試使用者」

### Q: 無法載入資料？

**A**: 檢查：
1. Sheet ID 是否正確
2. 工作表名稱是否為「排班資料」
3. 資料是否從第 2 列開始（第 1 列是標題）
4. Google 帳號是否有權限存取該試算表

### Q: 可以給其他人用嗎？

**A**: 可以！
- **方法 A**：將他們的 Google 帳號加入「測試使用者」（最多 100 個）
- **方法 B**：將應用程式「發布」（需要審查）
- **方法 C**：部署到網路，分享網址

---

## 🚀 進階：部署到網路

如果想要在任何地方存取（不只是本機）：

### 選項 A：GitHub Pages（免費）

1. 上傳 `shift_management_system_sheets.html` 到 GitHub
2. 啟用 GitHub Pages
3. 更新 OAuth 設定的「授權 JavaScript 來源」
   ```
   https://yourusername.github.io
   ```
4. 得到網址：`https://yourusername.github.io/shift_management_system_sheets.html`

### 選項 B：Netlify（免費）

1. 拖放檔案到 [Netlify](https://netlify.com)
2. 自動部署
3. 更新 OAuth 設定
4. 得到 HTTPS 網址

---

## 📱 未來：加入 Android App

這個設定完成後，未來要加入 Android App 非常簡單：

### 共用資料

```
網頁版（現在）
   ↓
Google Sheets ← 共同資料來源
   ↑
Android App（未來）
```

### 需要做的

1. 參考 `android_calendar_app/QUICK_START_CN.md`
2. 建置 Android App
3. 設定「Android」類型的 OAuth 2.0（另外一個）
4. 使用**同一個 Google Sheets**

**優點**：
- ✅ 資料即時同步
- ✅ 網頁編輯，App 查看
- ✅ 不用資料遷移

---

## 📚 相關文檔

| 文檔 | 說明 |
|------|------|
| `WEB_GOOGLE_SHEETS_SETUP.md` | 詳細的 Google API 設定教學 |
| `DEPLOYMENT_PLAN.md` | 完整部署策略 |
| `android_calendar_app/QUICK_START_CN.md` | Android App 快速開始（未來用） |

---

## ✅ 檢查清單

完成以下項目就可以開始使用：

- [ ] Google Cloud 專案已建立
- [ ] Google Sheets API 已啟用
- [ ] OAuth 同意畫面已設定
- [ ] 測試使用者已新增
- [ ] OAuth 用戶端 ID（網頁應用程式）已建立
- [ ] 用戶端 ID 已記錄
- [ ] Google Sheets 試算表已建立
- [ ] 工作表格式正確
- [ ] Sheet ID 已記錄
- [ ] 已開啟 HTML 檔案（使用本機伺服器）
- [ ] 已登入 Google 帳號
- [ ] 設定值已填入並儲存
- [ ] 測試連線成功
- [ ] 可以載入排班資料

---

## 🎊 完成！

恭喜！您現在有一個：
- ✅ 連接 Google Sheets 的網頁版排班系統
- ✅ 可以在試算表中編輯資料
- ✅ 未來可以無縫加入 Android App

**開始使用吧！** 🚀

---

**最後更新**: 2025-11-03  
**版本**: 1.0.0

