# 🔐 您的 OAuth 設定 - Josiehsieh

## 📍 您的網站網址

```
https://josiehsieh.github.io/staff-scheduling-system/
```

---

## ⚙️ Google OAuth 設定（必須完成！）

### 前往 Google Cloud Console

```
https://console.cloud.google.com/apis/credentials
```

### 編輯您的 OAuth 2.0 用戶端 ID

找到您之前建立的 OAuth 用戶端 ID，點擊編輯（鉛筆圖示）

---

## 📋 需要新增的 URI

### 1. 已授權的 JavaScript 來源

**保留原有的：**
```
http://localhost:8000
```

**新增以下項目：**
```
https://josiehsieh.github.io
```

**最終應該有 2 個：**
```
✅ http://localhost:8000
✅ https://josiehsieh.github.io
```

---

### 2. 已授權的重新導向 URI

**保留原有的（本地開發用）：**
```
http://localhost:8000
http://localhost:8000/
http://localhost:8000/shift_management_system_sheets_full.html
```

**新增以下項目（GitHub Pages 用）：**

請逐一點擊「+ 新增 URI」，分別輸入：

```
https://josiehsieh.github.io/staff-scheduling-system
```

```
https://josiehsieh.github.io/staff-scheduling-system/
```

```
https://josiehsieh.github.io/staff-scheduling-system/shift_management_system_sheets_full.html
```

**最終應該有 6 個重新導向 URI：**
```
✅ http://localhost:8000
✅ http://localhost:8000/
✅ http://localhost:8000/shift_management_system_sheets_full.html
✅ https://josiehsieh.github.io/staff-scheduling-system
✅ https://josiehsieh.github.io/staff-scheduling-system/
✅ https://josiehsieh.github.io/staff-scheduling-system/shift_management_system_sheets_full.html
```

---

## 📸 設定預覽

### JavaScript 來源應該像這樣：

```
已授權的 JavaScript 來源
────────────────────────────────────
http://localhost:8000
https://josiehsieh.github.io

[+ 新增 URI]
```

### 重新導向 URI 應該像這樣：

```
已授權的重新導向 URI
────────────────────────────────────
http://localhost:8000
http://localhost:8000/
http://localhost:8000/shift_management_system_sheets_full.html
https://josiehsieh.github.io/staff-scheduling-system
https://josiehsieh.github.io/staff-scheduling-system/
https://josiehsieh.github.io/staff-scheduling-system/shift_management_system_sheets_full.html

[+ 新增 URI]
```

---

## ⚠️ 重要提醒

1. **所有 URI 都要完整複製**
   - 不要漏掉結尾的斜線 `/`
   - 注意大小寫（您的用戶名是 `Josiehsieh`，大寫 J）

2. **GitHub Pages 使用 HTTPS**
   - 所有 GitHub Pages 的 URI 都是 `https://`
   - localhost 使用 `http://`

3. **設定後要點「儲存」**
   - 不要忘記儲存！

4. **等待 1-2 分鐘**
   - Google 需要時間同步設定

---

## ✅ 檢查清單

設定前確認：

```
□ 已開啟 https://console.cloud.google.com/apis/credentials
□ 找到正確的 OAuth 用戶端 ID
□ 點擊編輯（鉛筆圖示）
□ JavaScript 來源新增：https://josiehsieh.github.io
□ 重新導向 URI 新增 3 個 GitHub Pages 網址
□ 確認所有網址拼寫正確
□ 已點擊「儲存」
□ 等待 1-2 分鐘
```

---

## 🔍 完整設定截圖

**JavaScript 來源（2 個）：**
- ✅ `http://localhost:8000`
- ✅ `https://josiehsieh.github.io`

**重新導向 URI（6 個）：**
- ✅ `http://localhost:8000`
- ✅ `http://localhost:8000/`
- ✅ `http://localhost:8000/shift_management_system_sheets_full.html`
- ✅ `https://josiehsieh.github.io/staff-scheduling-system`
- ✅ `https://josiehsieh.github.io/staff-scheduling-system/`
- ✅ `https://josiehsieh.github.io/staff-scheduling-system/shift_management_system_sheets_full.html`

---

**設定完成後，就可以測試您的網站了！** 🎉





