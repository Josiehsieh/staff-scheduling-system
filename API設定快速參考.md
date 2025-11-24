# ⚡ Google 行事曆 API 設定 - 快速參考

## 5 步驟完成設定

### 📍 步驟 1：建立專案
```
1. 訪問：https://console.cloud.google.com
2. 點擊：「選取專案」→「新增專案」
3. 名稱：排班管理系統
4. 點擊：「建立」
```

### 📍 步驟 2：啟用 Calendar API
```
1. 左側選單 → API 和服務 → 程式庫
2. 搜尋：calendar
3. 點擊：Google Calendar API
4. 點擊：「啟用」
```

### 📍 步驟 3：設定 OAuth（重要！）
```
1. 左側選單 → API 和服務 → OAuth 同意畫面
2. 選擇：⚪ 外部 → 建立

第一頁：
  • 應用程式名稱：Medical Shift System
  • 使用者支援電子郵件：選擇您的 Gmail
  • 開發人員聯絡資訊：輸入您的 Gmail
  → 儲存並繼續

第二頁（範圍）：
  • 新增或移除範圍
  • 搜尋：calendar
  • ✅ 勾選：.../auth/calendar
  • 更新
  → 儲存並繼續

第三頁（測試使用者）：
  • + ADD USERS
  • 輸入您的完整 Gmail 地址
  • 新增
  → 儲存並繼續

第四頁：
  → 返回資訊主頁
```

### 📍 步驟 4：建立用戶端 ID
```
1. 左側選單 → API 和服務 → 憑證
2. 建立憑證 → OAuth 用戶端 ID
3. 應用程式類型：⚠️ 網頁應用程式 ⚠️（重要！不要選桌面應用程式）
4. 名稱：Medical Shift System

5. JavaScript 來源：
   點擊「+ 新增 URI」
   輸入：http://localhost:8000

6. 重新導向 URI（重要！全部新增）：
   點擊「+ 新增 URI」分別輸入：
   • http://localhost:8000
   • http://localhost:8000/
   • http://localhost:8000/shift_management_system_sheets_full.html

7. 點擊「建立」
8. 複製：用戶端 ID
```

### 📍 步驟 5：填入系統
```
1. 開啟：http://localhost:8000/shift_management_system_sheets_full.html
2. 設定 → 貼上用戶端 ID
3. 點擊：「🔐 登入 Google」
4. 授權行事曆權限
5. 完成！
```

---

## 🎯 關鍵資訊

### 需要的網址

| 項目 | 網址 |
|------|------|
| Google Cloud Console | https://console.cloud.google.com |
| API 程式庫 | https://console.cloud.google.com/apis/library |
| OAuth 設定 | https://console.cloud.google.com/apis/credentials/consent |
| 憑證頁面 | https://console.cloud.google.com/apis/credentials |

### 需要設定的內容

```
✅ 應用程式名稱：排班管理系統
✅ OAuth 類型：外部
✅ 範圍：.../auth/calendar
✅ 測試使用者：您的 Gmail
✅ 用戶端類型：網頁應用程式
✅ JavaScript 來源：http://localhost:8000
✅ 重新導向 URI：http://localhost:8000
```

---

## ⚠️ 常見錯誤

### 錯誤 0：redirect_uri=storagerelay://file/?id=auth... (最常見！)

**原因**：選擇了錯誤的應用程式類型（選了桌面應用程式）  
**解決**：
1. ❌ 刪除舊的「桌面應用程式」用戶端 ID
2. ✅ 重新建立，選擇「**網頁應用程式**」
3. 設定所有重新導向 URI
4. 更新系統中的用戶端 ID
5. 清除瀏覽器快取
👉 詳細步驟：[修正 OAuth 用戶端類型錯誤.md](./修正%20OAuth%20用戶端類型錯誤.md)

### 錯誤 1：invalid_request / doesn't comply with OAuth 2.0 policy

**原因**：OAuth 同意畫面設定不完整  
**解決**：
1. 確認已填寫：應用程式名稱、使用者支援電子郵件、開發人員聯絡資訊
2. 確認已新增 calendar 範圍
3. **確認已新增測試使用者（最重要！）**
4. 完成後等待 2-3 分鐘

### 錯誤 2：Access blocked

**原因**：未加入測試使用者  
**解決**：OAuth 同意畫面 → 測試使用者 → 新增您的 Gmail

### 錯誤 3：redirect_uri_mismatch

**原因**：URI 設定不完整  
**解決**：確認已新增全部三個 URI：
- `http://localhost:8000`
- `http://localhost:8000/`
- `http://localhost:8000/shift_management_system_sheets_full.html`

### 錯誤 4：API not enabled

**原因**：未啟用 Calendar API  
**解決**：API 程式庫 → 搜尋 calendar → 啟用

### 🔍 無法登入？

查看詳細的故障排除指南：
👉 [登入問題故障排除.md](./登入問題故障排除.md)

---

## ✅ 測試檢查

### 確認設定成功

1. ✅ 能成功登入 Google
2. ✅ Console 沒有錯誤訊息
3. ✅ 新增排班後在 Google 行事曆看到事件

### Console 應該顯示

```
📅 載入自動同步設定：已啟用
🔄 開始自動同步排班到行事曆...
✅ 自動同步成功，事件ID：xxxxx
```

---

## 📚 完整文件

詳細步驟請參考：[Google行事曆API設定指南.md](./Google行事曆API設定指南.md)

---

**預估時間**：10-15 分鐘  
**難度**：⭐⭐☆☆☆



