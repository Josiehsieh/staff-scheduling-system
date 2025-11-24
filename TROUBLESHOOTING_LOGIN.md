# 🔧 網頁版登入問題排除指南

如果您在開啟網頁後無法登入 Google 帳號，請依照以下步驟逐一檢查。

---

## 📋 問題檢查清單

### ✅ 檢查項目 1：是否使用本機伺服器？

#### 檢查方法
看瀏覽器網址列：

**❌ 錯誤（不能登入）**
```
file:///C:/Users/josie/staff-scheduling-system/shift_management_system_sheets.html
↑↑↑↑↑
file:// 協定 → Google OAuth 不支援！
```

**✅ 正確（可以登入）**
```
http://localhost:8000/shift_management_system_sheets.html
↑↑↑↑
http:// 協定 → Google OAuth 支援！
```

#### 解決方法
如果看到 `file://`，請：
1. 關閉瀏覽器
2. 啟動本機伺服器
   ```powershell
   cd C:\Users\josie\staff-scheduling-system
   python -m http.server 8000
   ```
3. 開啟瀏覽器，輸入：
   ```
   http://localhost:8000/shift_management_system_sheets.html
   ```

---

### ✅ 檢查項目 2：是否完成 Google Cloud API 設定？

#### 必須完成的設定

1. **建立 Google Cloud 專案** ✓
2. **啟用 Google Sheets API** ✓
3. **設定 OAuth 同意畫面** ✓
4. **建立 OAuth 2.0 用戶端 ID（網頁應用程式）** ✓
5. **加入測試使用者** ✓

#### 檢查方法

前往 [Google Cloud Console](https://console.cloud.google.com/)

**檢查 1：專案是否存在**
```
左上角應該顯示您的專案名稱
例如：Medical Shift System
```

**檢查 2：API 是否啟用**
```
左側選單 → API 和服務 → 已啟用的 API 和服務
應該看到：Google Sheets API
```

**檢查 3：OAuth 同意畫面**
```
左側選單 → API 和服務 → OAuth 同意畫面
狀態應該是：測試中
```

**檢查 4：OAuth 用戶端 ID**
```
左側選單 → API 和服務 → 憑證
應該看到：
  類型：網頁應用程式
  名稱：Medical Shift Web App（或類似）
```

**檢查 5：測試使用者**
```
左側選單 → API 和服務 → OAuth 同意畫面
點擊「測試使用者」標籤
應該看到您的 Google 帳號
```

#### 解決方法
如果缺少任何項目，請參考 `WEB_GOOGLE_SHEETS_SETUP.md` 完成設定。

---

### ✅ 檢查項目 3：OAuth 授權的 JavaScript 來源是否正確？

#### 檢查方法

1. 前往 [Google Cloud Console](https://console.cloud.google.com/)
2. 左側選單 → **「API 和服務」** → **「憑證」**
3. 點擊您的 OAuth 2.0 用戶端 ID
4. 查看 **「授權的 JavaScript 來源」**

#### 必須包含

```
http://localhost
http://localhost:8000
```

如果使用其他 port，也要加入：
```
http://localhost:3000
http://localhost:5000
```

#### 常見錯誤

❌ **錯誤 1：沒有設定**
```
授權的 JavaScript 來源：（空白）
```

❌ **錯誤 2：設定錯誤**
```
http://localhost/          ← 多了斜線
http://127.0.0.1:8000      ← 應該用 localhost
https://localhost:8000     ← 應該用 http（本機測試）
```

✅ **正確設定**
```
http://localhost
http://localhost:8000
```

#### 解決方法

1. 點擊 **「編輯」**
2. 在「授權的 JavaScript 來源」區域
3. 點擊 **「新增 URI」**
4. 分別加入：
   - `http://localhost`
   - `http://localhost:8000`
5. 點擊 **「儲存」**
6. **等待 5 分鐘**（設定需要時間生效）
7. 重新整理網頁，再次嘗試登入

---

### ✅ 檢查項目 4：用戶端 ID 是否正確填入網頁？

#### 檢查方法

1. 開啟網頁
2. 登入 Google 帳號（任何方式，即使失敗）
3. 在主畫面查看 **「Google API 用戶端 ID」** 欄位

#### 常見錯誤

❌ **錯誤 1：空白**
```
Google API 用戶端 ID: [                    ]
                      空白，沒有填入
```

❌ **錯誤 2：填錯**
```
123456789-abc...apps.googleusercontent.com
↑
缺少部分字串、有空格、或複製錯誤
```

✅ **正確格式**
```
123456789-abcdefghijklmnopqrstuvwxyz.apps.googleusercontent.com
↑
完整的用戶端 ID，沒有空格
```

#### 解決方法

1. 前往 [Google Cloud Console](https://console.cloud.google.com/)
2. 左側選單 → **「API 和服務」** → **「憑證」**
3. 找到您的 OAuth 2.0 用戶端 ID
4. 點擊名稱（不是編輯圖示）
5. 複製 **「用戶端 ID」**（完整字串）
6. 回到網頁
7. 貼上到「Google API 用戶端 ID」欄位
8. 點擊 **「💾 儲存設定」**
9. **重新整理頁面**（F5）
10. 再次嘗試登入

---

### ✅ 檢查項目 5：瀏覽器主控台是否有錯誤？

#### 檢查方法

1. 在網頁上按 `F12` 開啟開發者工具
2. 點擊 **「Console」** 標籤
3. 點擊「使用 Google 帳號登入」按鈕
4. 查看是否有紅色錯誤訊息

#### 常見錯誤訊息

**❌ 錯誤 1：`redirect_uri_mismatch`**
```javascript
Error: redirect_uri_mismatch
```
**原因**：OAuth 授權的 JavaScript 來源設定錯誤
**解決**：參考「檢查項目 3」

**❌ 錯誤 2：`popup_closed_by_user`**
```javascript
Error: popup_closed_by_user
```
**原因**：登入視窗被手動關閉
**解決**：再次嘗試登入，不要關閉彈出視窗

**❌ 錯誤 3：`access_denied`**
```javascript
Error: access_denied
```
**原因**：
1. 未加入測試使用者
2. 使用了未授權的 Google 帳號
**解決**：確認使用的 Google 帳號在「測試使用者」名單中

**❌ 錯誤 4：`invalid_client`**
```javascript
Error: invalid_client
```
**原因**：用戶端 ID 錯誤
**解決**：參考「檢查項目 4」重新填入正確的用戶端 ID

**❌ 錯誤 5：`tokenClient is undefined`**
```javascript
TypeError: Cannot read property 'requestAccessToken' of undefined
```
**原因**：Google API 尚未載入完成，或用戶端 ID 未設定
**解決**：
1. 等待頁面完全載入
2. 確認已填入用戶端 ID 並儲存

---

### ✅ 檢查項目 6：是否被瀏覽器阻擋彈出視窗？

#### 檢查方法

點擊「使用 Google 帳號登入」後，注意瀏覽器網址列右側：

```
🚫 彈出式視窗已封鎖
```

#### 解決方法

**方法 A：允許彈出視窗**
1. 點擊瀏覽器網址列的 🚫 圖示
2. 選擇「一律允許 localhost 顯示彈出式視窗」
3. 重新整理頁面
4. 再次嘗試登入

**方法 B：暫時停用封鎖**
- Chrome：設定 → 隱私權和安全性 → 網站設定 → 彈出式視窗和重新導向 → 允許
- Edge：類似設定

---

### ✅ 檢查項目 7：使用的 Google 帳號是否正確？

#### 檢查方法

確認您嘗試登入的 Google 帳號：
1. 是否在「測試使用者」名單中
2. 是否有權限存取 Google Sheets 試算表

#### 解決方法

**加入測試使用者**：
1. 前往 [Google Cloud Console](https://console.cloud.google.com/)
2. 左側選單 → **「API 和服務」** → **「OAuth 同意畫面」**
3. 點擊「測試使用者」區域的 **「新增使用者」**
4. 輸入 Google 帳號 Email
5. 點擊 **「儲存」**
6. **等待幾分鐘**
7. 再次嘗試登入

---

## 🔍 逐步除錯流程

### 步驟 1：基本檢查

```
□ 使用 http://localhost:8000/... 開啟（不是 file://）
□ 已完成 Google Cloud API 設定
□ 用戶端 ID 已正確填入並儲存
□ 使用的 Google 帳號在測試使用者名單中
```

### 步驟 2：打開開發者工具

```
1. 按 F12
2. 切換到 Console 標籤
3. 點擊「使用 Google 帳號登入」
4. 查看錯誤訊息
```

### 步驟 3：根據錯誤訊息處理

| 錯誤訊息 | 問題 | 解決方法 |
|---------|------|---------|
| `redirect_uri_mismatch` | JavaScript 來源錯誤 | 檢查項目 3 |
| `access_denied` | 未加入測試使用者 | 檢查項目 7 |
| `invalid_client` | 用戶端 ID 錯誤 | 檢查項目 4 |
| `popup_closed` | 關閉了登入視窗 | 重新登入 |
| 彈出視窗被阻擋 | 瀏覽器設定 | 檢查項目 6 |

---

## 📸 截圖指南

如果以上方法都無法解決，請提供以下截圖：

### 截圖 1：瀏覽器網址列
```
顯示完整網址，確認是 http:// 還是 file://
```

### 截圖 2：Console 錯誤訊息
```
按 F12 → Console 標籤
點擊登入後的錯誤訊息
```

### 截圖 3：OAuth 設定
```
Google Cloud Console → 憑證 → OAuth 2.0 用戶端 ID
顯示「授權的 JavaScript 來源」
```

### 截圖 4：測試使用者
```
Google Cloud Console → OAuth 同意畫面 → 測試使用者
顯示已加入的帳號
```

---

## ✅ 成功登入的確認

如果登入成功，您應該看到：

1. ✅ 彈出 Google 登入視窗
2. ✅ 選擇帳號
3. ✅ 授權畫面（第一次登入）
   ```
   醫療排班管理系統 想要：
   ☑ 查看您的 Google 試算表
   
   [取消] [允許]
   ```
4. ✅ 彈出視窗關閉
5. ✅ 回到主畫面，顯示您的 Email
6. ✅ 可以填入設定、測試連線、載入資料

---

## 🚀 快速解決方案（90% 的情況）

### 最常見問題組合

**問題**：點擊登入沒反應或出錯

**解決**：
```powershell
# 1. 確認使用本機伺服器
cd C:\Users\josie\staff-scheduling-system
python -m http.server 8000

# 2. 開啟瀏覽器
http://localhost:8000/shift_management_system_sheets.html

# 3. 檢查 Google Cloud 設定
- OAuth 用戶端 ID（網頁應用程式）已建立 ✓
- 授權的 JavaScript 來源包含 http://localhost:8000 ✓
- 測試使用者已加入您的 Google 帳號 ✓

# 4. 在網頁填入用戶端 ID
- 複製完整的用戶端 ID
- 貼到「Google API 用戶端 ID」欄位
- 點擊「儲存設定」
- 重新整理頁面（F5）

# 5. 再次嘗試登入
```

---

## ❌ 錯誤 6：已封鎖存取權 - OAuth 2.0 政策錯誤

### 🔴 錯誤訊息

```
已封鎖存取權：授權錯誤

You can't sign in to this app because it doesn't comply 
with Google's OAuth 2.0 policy for keeping apps secure.
```

### 🔍 原因

您的 Google Cloud 專案處於「測試」模式，但您的 Gmail 帳號**不在測試使用者清單中**。

### ✅ 解決方法：加入測試使用者

#### 步驟 1：前往 Google Cloud Console

1. 開啟：https://console.cloud.google.com
2. 確認登入正確的 Google 帳號

#### 步驟 2：進入 OAuth 同意畫面

```
Google Cloud Console 首頁
    ↓
左側選單：「API 和服務」
    ↓
點擊：「OAuth 同意畫面」
```

#### 步驟 3：新增測試使用者

1. 在「OAuth 同意畫面」頁面，**向下捲動**到「測試使用者」區域

2. 點擊 **「+ ADD USERS」** 或 **「新增使用者」** 按鈕

3. 在彈出的對話框中輸入您的 Gmail：
   ```
   your-email@gmail.com
   ```
   例如：`JosieCrew1987@gmail.com`

4. 點擊 **「新增」** 或 **「ADD」**

5. 確認您的帳號出現在測試使用者清單中

6. 點擊 **「儲存」** 或 **「SAVE」**

#### 步驟 4：重新登入測試

1. 回到應用程式頁面：
   ```
   http://localhost:8000/shift_management_system_sheets_full.html
   ```

2. 重新整理頁面（F5）

3. 點擊「使用 Google 帳號登入」

4. 選擇您剛才加入的 Gmail 帳號

5. 應該可以成功登入了！✅

### 📝 補充說明

#### 關於測試模式

- ⚠️ 在「測試」模式下，**只有**加入測試使用者清單的帳號才能登入
- ✅ 最多可加入 **100 位**測試使用者
- 🔒 這是 Google 的安全機制，保護未發布的應用程式

#### 如果需要多人使用

1. **個人/小團隊（推薦）**：
   - 將所有需要使用的人加入「測試使用者」
   - 保持應用程式在「測試」模式
   - 簡單快速，不需審核

2. **公開發布（不推薦）**：
   - 需要提交 Google 審核
   - 需要準備隱私政策、服務條款等文件
   - 審核時間可能需要數週
   - 對個人使用過於複雜

#### 常見問題

**Q：需要重新建立 Client ID 嗎？**  
**A：** 不需要！Client ID 保持不變，只要加入測試使用者即可。

**Q：可以同時加入多個測試使用者嗎？**  
**A：** 可以！重複步驟 3，每次加入一位使用者。

**Q：加入測試使用者需要多久生效？**  
**A：** 通常是立即生效，最多等待 1-2 分鐘。

---

## 📞 仍然無法解決？

如果試過所有方法仍無法登入，請提供：

1. ✅ 瀏覽器網址（確認 http:// 或 file://）
2. ✅ Console 錯誤訊息（完整）
3. ✅ Google Cloud 專案設定狀態
4. ✅ 使用的瀏覽器和版本

---

**記住**：
- ❌ 不能用 `file://` 協定
- ✅ 必須用 `http://localhost` 協定
- ⏰ OAuth 設定更改需要 5-10 分鐘生效
- 🔄 更改設定後要重新整理頁面

---

**最後更新**：2025-11-03  
**版本**：1.0.0

