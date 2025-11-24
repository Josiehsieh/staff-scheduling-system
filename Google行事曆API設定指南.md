# 📅 Google 行事曆 API 設定指南

## 完整步驟說明

本指南將幫助您完成 Google Calendar API 的設定，讓排班系統能夠自動同步到 Google 行事曆。

---

## 📋 前置需求

- ✅ Google 帳號
- ✅ 網路瀏覽器
- ✅ 約 10-15 分鐘

---

## 🚀 設定步驟

### 步驟 1：進入 Google Cloud Console

#### 1.1 開啟瀏覽器
訪問：**https://console.cloud.google.com**

#### 1.2 登入 Google 帳號
使用您的 Gmail 帳號登入

---

### 步驟 2：建立新專案（如果還沒有）

#### 2.1 點擊頂部的專案選擇器
```
畫面左上角：「My Project」或「選取專案」
```

#### 2.2 點擊「新增專案」
```
對話框右上角：「新增專案」按鈕
```

#### 2.3 填寫專案資訊
```
專案名稱：排班管理系統
（或任何您喜歡的名稱）

位置：不用選擇組織（選擇「無組織」）

點擊：「建立」
```

#### 2.4 等待建立完成
```
通常需要幾秒鐘
建立完成後會自動切換到新專案
```

---

### 步驟 3：啟用 Google Calendar API

#### 3.1 開啟 API 程式庫
```
方法 1：點擊左側選單 ≡ → API 和服務 → 程式庫

方法 2：直接訪問
https://console.cloud.google.com/apis/library
```

#### 3.2 搜尋 Calendar API
```
在搜尋框輸入：calendar
或
在搜尋框輸入：Google Calendar API
```

#### 3.3 點擊「Google Calendar API」
```
圖示：藍色日曆圖標
標題：Google Calendar API
說明：Manipulates events and other calendar data.
```

#### 3.4 啟用 API
```
點擊：「啟用」按鈕（藍色）

等待幾秒鐘，API 就啟用了
```

#### 3.5 確認啟用成功
```
畫面會顯示：
✅ Google Calendar API 已啟用
```

---

### 步驟 4：設定 OAuth 同意畫面

#### 4.1 進入 OAuth 同意畫面
```
左側選單 ≡ → API 和服務 → OAuth 同意畫面

或直接訪問：
https://console.cloud.google.com/apis/credentials/consent
```

#### 4.2 選擇使用者類型
```
選擇：⚪ 外部（External）

點擊：「建立」
```

#### 4.3 填寫應用程式資訊

**第 1 頁：OAuth 同意畫面**
```
應用程式名稱：排班管理系統
使用者支援電子郵件：（選擇您的 Gmail）
應用程式標誌：（可選，跳過）

開發人員聯絡資訊：
電子郵件地址：（輸入您的 Gmail）

點擊：「儲存並繼續」
```

**第 2 頁：範圍**
```
點擊：「新增或移除範圍」

在篩選器中搜尋：calendar

勾選：
☑️ .../auth/calendar
   查看、編輯、共用及永久刪除您可在 Google 日曆上存取的所有日曆

點擊：「更新」
點擊：「儲存並繼續」
```

**第 3 頁：測試使用者**
```
點擊：「+ ADD USERS」

輸入您的 Gmail 地址：
例如：JosieCrew1987@gmail.com

點擊：「新增」
點擊：「儲存並繼續」
```

**第 4 頁：摘要**
```
檢查資訊
點擊：「返回資訊主頁」
```

---

### 步驟 5：建立 OAuth 用戶端 ID

#### 5.1 進入憑證頁面
```
左側選單 ≡ → API 和服務 → 憑證

或直接訪問：
https://console.cloud.google.com/apis/credentials
```

#### 5.2 建立憑證
```
點擊頂部：「+ 建立憑證」
選擇：「OAuth 用戶端 ID」
```

#### 5.3 選擇應用程式類型
```
應用程式類型：網頁應用程式
```

#### 5.4 填寫詳細資料

**名稱**
```
網頁用戶端
（或任何您喜歡的名稱）
```

**已授權的 JavaScript 來源**
```
點擊：「+ 新增 URI」

輸入：http://localhost:8000

說明：這是您本機執行網頁的位址
```

**已授權的重新導向 URI**
```
點擊：「+ 新增 URI」

輸入：http://localhost:8000

說明：OAuth 完成後導向的位址
```

#### 5.5 建立
```
點擊：「建立」
```

#### 5.6 記下用戶端 ID
```
會彈出對話框顯示：

您的用戶端 ID：
63810182329-xxxxxxxxxxxxxxxxxxxxxxxxxx.apps.googleusercontent.com

用戶端密鑰：
GOCSPX-xxxxxxxxxxxxxxxxxxxx

⚠️ 重要：複製「用戶端 ID」（只需要這個）
```

---

### 步驟 6：將用戶端 ID 填入系統

#### 6.1 開啟排班系統
```
使用 HTTP Server 開啟：
http://localhost:8000/shift_management_system_sheets_full.html
```

#### 6.2 進入設定頁面
```
點擊：「⚙️ 設定」頁籤
```

#### 6.3 填入用戶端 ID
```
找到：「Google API 設定」區域

貼上您的用戶端 ID：
63810182329-xxxxxxxxxxxxxxxxxxxxxxxxxx.apps.googleusercontent.com

點擊：「儲存設定」
```

---

### 步驟 7：測試設定

#### 7.1 登入 Google
```
點擊頂部：「🔐 登入 Google」
```

#### 7.2 選擇帳號
```
選擇您的 Google 帳號
（應該是您設定為測試使用者的帳號）
```

#### 7.3 授權應用程式
```
會看到授權畫面：

排班管理系統 想要存取您的 Google 帳戶

此應用程式將能夠：
☑️ 查看、編輯、共用及永久刪除您可在 Google 日曆上存取的所有日曆

點擊：「繼續」
```

#### 7.4 確認成功
```
如果成功，會看到：
✅ 已登入 Google 帳號
```

---

## 🎯 測試行事曆同步

### 測試步驟

#### 1. 啟用自動同步
```
設定頁面 → 📅 Google 行事曆自動同步
勾選：☑️ 啟用自動同步到 Google 行事曆
```

#### 2. 新增一個測試排班
```
排班管理 → ➕ 新增排班

填寫：
- 日期：今天或明天
- 時間：09:00 - 17:00
- 事業單位：測試單位
- 人員：測試人員
- 地點：測試地點

點擊：💾 儲存
```

#### 3. 檢查 Console
```
按 F12 開啟開發者工具
查看 Console 標籤

應該看到：
✅ 排班已儲存到 Google Sheets
🔄 開始自動同步排班到行事曆...
✅ 自動同步成功，事件ID：xxxxx
```

#### 4. 確認 Google 行事曆
```
開啟新分頁：https://calendar.google.com

查看對應日期

應該看到：測試單位 - 排班
```

---

## ✅ 設定完成檢查清單

請確認以下項目都已完成：

- [ ] 建立 Google Cloud 專案
- [ ] 啟用 Google Calendar API
- [ ] 設定 OAuth 同意畫面
- [ ] 選擇「外部」使用者類型
- [ ] 新增 Calendar 範圍
- [ ] 新增測試使用者（您的 Gmail）
- [ ] 建立 OAuth 用戶端 ID（網頁應用程式）
- [ ] 新增授權的 JavaScript 來源（http://localhost:8000）
- [ ] 複製用戶端 ID
- [ ] 在系統中填入用戶端 ID
- [ ] 成功登入 Google
- [ ] 授權行事曆權限
- [ ] 測試新增排班並同步

---

## 🔧 常見問題排除

### 問題 1：找不到 Google Calendar API

**解決方案**：
1. 確認已在正確的專案中
2. 在 API 程式庫搜尋框輸入：`calendar`
3. 應該會看到「Google Calendar API」

### 問題 2：無法建立 OAuth 用戶端 ID

**錯誤訊息**：
```
需要先設定 OAuth 同意畫面
```

**解決方案**：
先完成「步驟 4：設定 OAuth 同意畫面」

### 問題 3：登入時出現「已封鎖存取權」

**完整錯誤訊息**：
```
Access blocked: Authorization Error
Your email@gmail.com
You can't sign in to this app because it doesn't comply 
with Google's OAuth 2.0 policy
```

**原因**：
您的 Gmail 未加入測試使用者名單

**解決方案**：
1. 回到 Google Cloud Console
2. OAuth 同意畫面 → 測試使用者
3. 點擊「+ ADD USERS」
4. 輸入您的 Gmail
5. 點擊「新增」
6. 重新測試登入

### 問題 4：redirect_uri_mismatch 錯誤

**錯誤訊息**：
```
Error 400: redirect_uri_mismatch
```

**原因**：
重新導向 URI 設定不正確

**解決方案**：
1. 確認使用 `http://localhost:8000` 開啟系統
2. 在 OAuth 用戶端 ID 設定中：
   - 已授權的 JavaScript 來源：`http://localhost:8000`
   - 已授權的重新導向 URI：`http://localhost:8000`
3. 不要使用 `file://` 協議

### 問題 5：無法啟用 API

**錯誤訊息**：
```
需要啟用計費功能
```

**說明**：
Google Calendar API 是免費的，但可能需要驗證帳號

**解決方案**：
1. 按照提示驗證 Google 帳號
2. 不需要真的付費
3. 每日免費配額足夠個人使用

### 問題 6：自動同步失敗

**Console 訊息**：
```
❌ 自動同步失敗：未登入 Google 帳號
```

**解決方案**：
1. 確認已點擊「🔐 登入 Google」
2. 確認已授權行事曆權限
3. 重新整理頁面後再試

---

## 📊 API 使用配額

### 免費配額

Google Calendar API 提供以下免費配額：

```
每日配額：
- 1,000,000 個請求/天

每個使用者配額：
- 5,000 個請求/使用者/天

速率限制：
- 5 個請求/秒
```

### 對個人使用的影響

```
每次新增排班 = 1 個請求
每天新增 100 個排班 = 100 個請求

結論：配額非常充足，完全夠用！
```

---

## 🔒 安全性說明

### 用戶端 ID 是公開的嗎？

**是的，用戶端 ID 是公開的**
- ✅ 可以安全地儲存在前端 JavaScript
- ✅ 不需要保密
- ✅ 不會洩漏您的資料

### 什麼需要保密？

**用戶端密鑰（Client Secret）**
- ⚠️ 需要保密
- ✅ 本系統不使用（網頁應用程式不需要）
- ✅ 只用於後端應用程式

### OAuth 授權流程

```
1. 使用者點擊「登入」
2. 導向到 Google 授權頁面
3. 使用者同意授權
4. Google 返回臨時 Token
5. 系統使用 Token 存取行事曆
6. Token 儲存在瀏覽器（不外傳）
```

---

## 📞 需要協助？

### 檢查步驟

1. ✅ 確認所有設定步驟都已完成
2. 🔍 查看 Console 錯誤訊息（F12）
3. 📋 對照「常見問題排除」
4. 🔄 嘗試重新整理頁面

### 除錯資訊

開啟開發者工具（F12）查看：

```
Console 標籤：
- 載入設定訊息
- API 初始化訊息
- 同步成功/失敗訊息

Network 標籤：
- API 請求狀態
- 錯誤回應
```

---

## 📚 相關資源

### Google 官方文件

- **Calendar API 文件**  
  https://developers.google.com/calendar

- **OAuth 2.0 說明**  
  https://developers.google.com/identity/protocols/oauth2

- **API 配額與限制**  
  https://developers.google.com/calendar/api/guides/quota

### 本系統文件

- [Google行事曆自動同步功能說明.md](./Google行事曆自動同步功能說明.md)
- [自動同步快速開始.md](./自動同步快速開始.md)
- [GOOGLE_CALENDAR_功能指南.md](./GOOGLE_CALENDAR_功能指南.md)

---

## 📝 總結

### 設定流程回顧

```
1. 建立 Google Cloud 專案 ✅
2. 啟用 Calendar API ✅
3. 設定 OAuth 同意畫面 ✅
4. 建立 OAuth 用戶端 ID ✅
5. 填入系統設定 ✅
6. 登入授權 ✅
7. 測試同步 ✅
```

### 完成後您可以

- ✅ 自動同步排班到 Google 行事曆
- ✅ 在手機上查看排班
- ✅ 接收行事曆提醒
- ✅ 與他人共用行事曆

---

**文件版本**：v1.0  
**更新日期**：2025-11-05  
**適用系統**：排班管理系統 v6.2.0

**預估設定時間**：10-15 分鐘  
**難度**：⭐⭐☆☆☆（中等）




