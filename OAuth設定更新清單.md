# 📝 OAuth 設定更新清單

部署到 GitHub Pages 後，必須更新 OAuth 設定。

---

## 🎯 需要新增的 URI

### 您的 GitHub Pages 網址

假設您的 GitHub 用戶名是 `josie`，Repository 名稱是 `staff-scheduling-system`：

```
基礎網址：https://josie.github.io/staff-scheduling-system/
```

**請將下面所有的 `josie` 替換成您的 GitHub 用戶名！**

---

## 📋 完整的 OAuth 設定

### 前往 Google Cloud Console

```
https://console.cloud.google.com/apis/credentials
```

### 編輯 OAuth 2.0 用戶端 ID

找到您之前建立的用戶端 ID，點擊編輯（鉛筆圖示）

---

### 已授權的 JavaScript 來源

**保留原有的：**
```
http://localhost:8000
```

**新增以下項目：**
```
https://josie.github.io
```

**最終應該有 2 個：**
```
✅ http://localhost:8000
✅ https://josie.github.io
```

---

### 已授權的重新導向 URI

**保留原有的（本地開發用）：**
```
http://localhost:8000
http://localhost:8000/
http://localhost:8000/shift_management_system_sheets_full.html
```

**新增以下項目（GitHub Pages 用）：**
```
https://josie.github.io/staff-scheduling-system
https://josie.github.io/staff-scheduling-system/
https://josie.github.io/staff-scheduling-system/shift_management_system_sheets_full.html
```

**最終應該有 6 個：**
```
✅ http://localhost:8000
✅ http://localhost:8000/
✅ http://localhost:8000/shift_management_system_sheets_full.html
✅ https://josie.github.io/staff-scheduling-system
✅ https://josie.github.io/staff-scheduling-system/
✅ https://josie.github.io/staff-scheduling-system/shift_management_system_sheets_full.html
```

---

## 🔍 為什麼需要這麼多 URI？

### 有/無結尾斜線
```
https://josie.github.io/staff-scheduling-system   ← 沒有斜線
https://josie.github.io/staff-scheduling-system/  ← 有斜線
```
不同的瀏覽器和情況可能使用不同格式，所以都要加。

### 具體頁面
```
https://josie.github.io/staff-scheduling-system/shift_management_system_sheets_full.html
```
直接訪問 HTML 檔案時使用。

### localhost
```
http://localhost:8000
```
保留這些是為了本地開發時還能測試。

---

## ✅ 檢查清單

設定完成後，確認以下項目：

```
□ JavaScript 來源有 2 個（localhost + GitHub Pages）
□ 重新導向 URI 有 6 個（3 個 localhost + 3 個 GitHub Pages）
□ 所有 GitHub Pages 的 URI 都使用 https://
□ 所有 localhost 的 URI 都使用 http://
□ GitHub 用戶名拼寫正確
□ Repository 名稱拼寫正確
□ 已點擊「儲存」
```

---

## 🧪 測試步驟

設定完成後，測試是否成功：

1. **開啟 GitHub Pages 網站**
   ```
   https://josie.github.io/staff-scheduling-system/
   ```

2. **點擊「🔐 登入 Google」**

3. **選擇您的 Google 帳號**

4. **應該看到授權畫面**
   - 如果成功：會看到要求授權 Sheets 和 Calendar 的畫面
   - 如果失敗：會看到錯誤訊息（redirect_uri_mismatch 或 invalid_request）

5. **授權後應該成功登入**
   - 頂部顯示您的名字
   - 可以正常使用所有功能

---

## ⚠️ 常見錯誤

### 錯誤 1：redirect_uri_mismatch

**錯誤訊息：**
```
Error: redirect_uri_mismatch
The redirect URI in the request, https://josie.github.io/staff-scheduling-system/, 
does not match the ones authorized for the OAuth client.
```

**原因：**
- 重新導向 URI 沒有正確設定
- 拼寫錯誤
- 忘記加結尾斜線

**解決：**
1. 檢查錯誤訊息中的 URI
2. 確認該 URI 已加入 OAuth 設定
3. 注意大小寫和斜線
4. 儲存後等待 1-2 分鐘

### 錯誤 2：Access blocked

**原因：**
- 沒有在測試使用者中加入您的 Gmail

**解決：**
1. 前往 OAuth 同意畫面
2. 測試使用者 → 新增您的 Gmail
3. 儲存

### 錯誤 3：網站可以開但無法登入

**檢查：**
1. 按 F12 開啟開發者工具
2. 查看 Console 的錯誤訊息
3. 確認用戶端 ID 已在設定中填入
4. 確認 OAuth 設定正確

---

## 📸 設定截圖參考

### JavaScript 來源設定應該像這樣：

```
已授權的 JavaScript 來源
────────────────────────────────────
http://localhost:8000
https://josie.github.io

[+ 新增 URI]
```

### 重新導向 URI 設定應該像這樣：

```
已授權的重新導向 URI
────────────────────────────────────
http://localhost:8000
http://localhost:8000/
http://localhost:8000/shift_management_system_sheets_full.html
https://josie.github.io/staff-scheduling-system
https://josie.github.io/staff-scheduling-system/
https://josie.github.io/staff-scheduling-system/shift_management_system_sheets_full.html

[+ 新增 URI]
```

---

## 🎉 成功！

如果您能成功登入並使用所有功能，恭喜您完成部署！

您的排班系統現在可以在任何地方、任何裝置上使用了！

---

**記得將此文件中的 `josie` 替換成您的 GitHub 用戶名！**


