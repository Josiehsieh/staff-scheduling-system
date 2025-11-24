# 🚨 修正「錯誤 400: invalid_request」- redirect_uri 問題

## 錯誤訊息
```
錯誤 400：invalid_request
要求詳情： redirect_uri=storagerelay://file/?id=auth618502 flowName=GeneralOAuthFlow
```

## 🔍 問題原因

**您選擇了錯誤的應用程式類型！**

- ❌ 您建立了：**桌面應用程式** (Desktop App)
- ✅ 應該建立：**網頁應用程式** (Web Application)

`storagerelay://` 是桌面應用程式的協定，但您的排班系統在瀏覽器中執行，需要使用 `http://` 協定。

---

## ✅ 解決方法：刪除舊的並建立正確的用戶端 ID

### 步驟 1：刪除錯誤的用戶端 ID

1. **前往憑證頁面**
   ```
   https://console.cloud.google.com/apis/credentials
   ```

2. **找到您的 OAuth 2.0 用戶端 ID**
   - 在「OAuth 2.0 用戶端 ID」區域
   - 找到類型顯示為「桌面」或「Desktop」的項目

3. **刪除它**
   - 點擊右側的「🗑️」（垃圾桶圖示）
   - 確認刪除

### 步驟 2：建立正確的網頁應用程式用戶端 ID

1. **點擊「+ 建立憑證」**
   - 選擇「OAuth 用戶端 ID」

2. **選擇應用程式類型**（重要！）
   ```
   應用程式類型：網頁應用程式 ← 務必選這個！
   ```

3. **填寫名稱**
   ```
   名稱：Medical Shift System Web Client
   ```

4. **設定已授權的 JavaScript 來源**
   - 點擊「+ 新增 URI」
   - 輸入：
   ```
   http://localhost:8000
   ```

5. **設定已授權的重新導向 URI**（關鍵步驟！）
   - 點擊「+ 新增 URI」，**分別**輸入以下**所有** URI：
   
   ```
   http://localhost:8000
   ```
   
   - 再次點擊「+ 新增 URI」：
   ```
   http://localhost:8000/
   ```
   
   - 再次點擊「+ 新增 URI」：
   ```
   http://localhost:8000/shift_management_system_sheets_full.html
   ```

6. **點擊「建立」**

7. **複製新的用戶端 ID**
   - 會看到一個彈出視窗顯示用戶端 ID
   - 格式像：`63810182329-xxxxxxxxxxxxxxxx.apps.googleusercontent.com`
   - **複製這個 ID**

---

## 📋 正確設定畫面截圖說明

### 應該看到這樣的設定

```
應用程式類型：
⚪ 網頁應用程式 ← 選這個
⚪ Android
⚪ iOS
⚪ Chrome 應用程式
⚪ 通用 Windows 平台 (UWP)
⚪ 電視與有限輸入裝置

名稱：
Medical Shift System Web Client

已授權的 JavaScript 來源：
http://localhost:8000

已授權的重新導向 URI：
http://localhost:8000
http://localhost:8000/
http://localhost:8000/shift_management_system_sheets_full.html
```

---

## 🔄 步驟 3：在系統中更新用戶端 ID

1. **開啟排班系統**
   ```
   http://localhost:8000/shift_management_system_sheets_full.html
   ```

2. **點擊「⚙️ 設定」頁籤**

3. **找到「Google API 設定」區域**

4. **貼上新的用戶端 ID**
   - 刪除舊的 ID
   - 貼上剛才複製的新 ID
   - 確保沒有多餘的空格

5. **點擊「儲存設定」**
   - 應該看到「✅ 設定已儲存」訊息

---

## 🧹 步驟 4：清除快取並測試

1. **清除瀏覽器快取**
   ```
   按 Ctrl + Shift + Delete
   選擇：
   ☑️ Cookie 和其他網站資料
   ☑️ 快取的圖片和檔案
   時間範圍：不限時間
   點擊「清除資料」
   ```

2. **關閉所有瀏覽器視窗**

3. **重新開啟瀏覽器**

4. **訪問系統**
   ```
   http://localhost:8000/shift_management_system_sheets_full.html
   ```

5. **點擊「🔐 登入 Google」**

6. **應該會看到正確的 Google 授權畫面**
   - 選擇您的 Google 帳號
   - 點擊「允許」授權

---

## ✅ 成功的標誌

如果設定正確，您會看到：

### 登入過程
```
1. 點擊「🔐 登入 Google」
2. 出現 Google 登入視窗（不是錯誤訊息）
3. 選擇帳號
4. 看到授權畫面（要求存取 Sheets 和 Calendar）
5. 點擊「允許」
6. 視窗自動關閉
7. 系統頂部顯示您的名字
```

### 如果還是看到錯誤
代表設定還有問題，請回到步驟 2 重新檢查。

---

## 🎯 關鍵要點總結

### ❌ 常見錯誤

| 錯誤選擇 | 結果 |
|---------|------|
| 選擇「桌面應用程式」 | ❌ 出現 `storagerelay://` 錯誤 |
| 選擇「Android」 | ❌ 無法在網頁中使用 |
| 選擇「iOS」 | ❌ 無法在網頁中使用 |
| 忘記設定重新導向 URI | ❌ 出現 `redirect_uri_mismatch` |

### ✅ 正確設定

| 項目 | 正確值 |
|------|-------|
| **應用程式類型** | 網頁應用程式 |
| **JavaScript 來源** | `http://localhost:8000` |
| **重新導向 URI 1** | `http://localhost:8000` |
| **重新導向 URI 2** | `http://localhost:8000/` |
| **重新導向 URI 3** | `http://localhost:8000/shift_management_system_sheets_full.html` |

---

## 🔍 如何確認類型正確

### 方法 1：查看憑證列表

前往：https://console.cloud.google.com/apis/credentials

在「OAuth 2.0 用戶端 ID」區域，應該看到：

```
名稱：Medical Shift System Web Client
類型：網頁應用程式 ← 確認這裡！
建立時間：2025-xx-xx
```

如果「類型」顯示「桌面」、「Android」、「iOS」等，就是錯的！

### 方法 2：點擊編輯查看

點擊用戶端 ID 名稱或鉛筆圖示，查看詳細設定：

應該看到：
```
應用程式類型：網頁應用程式（無法修改）
已授權的 JavaScript 來源：http://localhost:8000
已授權的重新導向 URI：（列出三個 URI）
```

---

## 🆘 常見問題

### Q：我可以修改現有的用戶端 ID 類型嗎？

A：**不行**。應用程式類型在建立後無法修改。必須刪除舊的，重新建立正確類型的。

### Q：刪除舊的用戶端 ID 會影響其他設定嗎？

A：不會影響其他設定（OAuth 同意畫面、API 啟用等）。只需要重新建立用戶端 ID 並更新到系統中。

### Q：我有多個用戶端 ID 可以嗎？

A：可以。您可以同時保留多個用戶端 ID，但在系統中只使用正確的那一個（網頁應用程式類型）。

### Q：為什麼會選錯類型？

A：Google 的介面容易讓人混淆。「桌面應用程式」聽起來像是在本地執行的程式，但實際上網頁應用程式（在瀏覽器中執行）需要選擇「網頁應用程式」。

### Q：如果我不確定該選哪一個？

A：
- 在**瀏覽器**中執行 → 選「網頁應用程式」
- 使用 `http://localhost` 或網址 → 選「網頁應用程式」
- 桌面程式（.exe、.app） → 選「桌面應用程式」
- 手機 App → 選「Android」或「iOS」

您的排班系統在瀏覽器中開啟，所以是「網頁應用程式」。

---

## 📱 進階：如何判斷應該選哪種類型

### 網頁應用程式 (Web Application)
```
✅ 在瀏覽器中執行
✅ 使用 http:// 或 https:// 開頭的網址
✅ 用戶透過網址訪問
✅ 像您的排班系統
```

### 桌面應用程式 (Desktop)
```
✅ 在電腦上安裝的程式
✅ 雙擊圖示啟動
✅ 不需要瀏覽器
✅ 例如：Electron 應用程式
```

### Android / iOS
```
✅ 手機或平板 App
✅ 從 Google Play 或 App Store 下載
✅ 在手機上執行
```

---

## 🎯 完整檢查清單

完成後確認以下項目：

```
□ ✅ 已刪除舊的「桌面應用程式」類型的用戶端 ID
□ ✅ 已建立新的「網頁應用程式」類型的用戶端 ID
□ ✅ JavaScript 來源設定為：http://localhost:8000
□ ✅ 重新導向 URI 包含全部三個：
  □ http://localhost:8000
  □ http://localhost:8000/
  □ http://localhost:8000/shift_management_system_sheets_full.html
□ ✅ 已複製新的用戶端 ID
□ ✅ 已在系統設定中更新用戶端 ID
□ ✅ 已清除瀏覽器快取
□ ✅ 重新開啟瀏覽器
□ ✅ 可以成功登入 Google（沒有錯誤）
```

---

## 📚 相關文件

- [API設定快速參考.md](./API設定快速參考.md) - 完整設定步驟
- [登入問題故障排除.md](./登入問題故障排除.md) - 其他登入問題
- [如何在現有專案中加入行事曆功能.md](./如何在現有專案中加入行事曆功能.md) - 加入 Calendar API

---

**最後更新**：2025-11-11  
**版本**：v1.0

## 💡 記住

**應用程式類型是關鍵！**

在瀏覽器中執行 = **網頁應用程式**  
不是桌面應用程式！


