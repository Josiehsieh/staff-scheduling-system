# 🚀 快速開始 - 醫療行事曆 App

這是一個 5 分鐘快速設定指南，幫助您快速開始使用醫療行事曆 Android 應用程式。

---

## 📋 準備清單

在開始之前，請確保您有：

- ✅ Android Studio (已安裝)
- ✅ Android 裝置或模擬器
- ✅ Google 帳號
- ✅ 5-10 分鐘的時間

---

## 🎯 步驟 1: 開啟專案 (30秒)

1. 啟動 **Android Studio**
2. 點擊 **Open** 
3. 選擇資料夾：`C:\Users\josie\staff-scheduling-system\android_calendar_app`
4. 等待 Gradle 同步完成（看到「BUILD SUCCESSFUL」）

---

## 🔑 步驟 2: 取得 SHA-1 指紋 (1分鐘)

### Windows 用戶

1. 在 Android Studio 底部開啟 **Terminal**
2. 貼上並執行以下指令：

```powershell
keytool -list -v -keystore $env:USERPROFILE\.android\debug.keystore -alias androiddebugkey -storepass android -keypass android
```

3. 找到並複製 **SHA1** 那一行（類似：`AA:BB:CC:DD:EE:...`）

### 範例輸出
```
Certificate fingerprints:
SHA1: 1A:2B:3C:4D:5E:6F:7A:8B:9C:0D:1E:2F:3A:4B:5C:6D:7E:8F:9A:0B  ← 複製這個
```

---

## ☁️ 步驟 3: 設定 Google Cloud (3分鐘)

### 3.1 建立專案

1. 前往 [Google Cloud Console](https://console.cloud.google.com/)
2. 點擊頂部的 **「選擇專案」** → **「新增專案」**
3. 專案名稱：`Medical Calendar App`
4. 點擊 **「建立」**

### 3.2 啟用 API

1. 左側選單：**「API 和服務」** → **「程式庫」**
2. 搜尋並點擊 **「Google Sheets API」** → 點擊 **「啟用」**
3. 回到程式庫，搜尋並點擊 **「Google Calendar API」** → 點擊 **「啟用」**

### 3.3 設定 OAuth 同意畫面

1. 左側選單：**「API 和服務」** → **「OAuth 同意畫面」**
2. 選擇 **「外部」** → 點擊 **「建立」**
3. 填寫基本資訊：
   - **應用程式名稱**: `醫療行事曆 App`
   - **使用者支援電子郵件**: 您的 Email
   - **開發人員聯絡資訊**: 您的 Email
4. 點擊 **「儲存並繼續」**
5. **「範圍」** 頁面：直接點擊 **「儲存並繼續」**（範圍會由 App 請求）
6. **「測試使用者」** 頁面：
   - 點擊 **「新增使用者」**
   - 輸入您的 Google 帳號 Email
   - 點擊 **「新增」**
7. 點擊 **「儲存並繼續」**

### 3.4 建立 OAuth 用戶端 ID

1. 左側選單：**「API 和服務」** → **「憑證」**
2. 點擊 **「建立憑證」** → **「OAuth 用戶端 ID」**
3. 應用程式類型：選擇 **「Android」**
4. 填寫資訊：
   - **名稱**: `Medical Calendar Android App`
   - **套件名稱**: `com.medical.calendar`
   - **SHA-1 憑證指紋**: 貼上步驟 2 複製的 SHA-1
5. 點擊 **「建立」**
6. 看到成功訊息後，點擊 **「確定」**

✅ **Google Cloud 設定完成！**

---

## 📊 步驟 4: 準備 Google Sheets (2分鐘)

### 4.1 建立試算表

1. 前往 [Google Sheets](https://sheets.google.com)
2. 點擊 **「空白」** 建立新試算表
3. 將試算表命名為：**「醫療排班資料」**

### 4.2 設定表格

1. 將第一個工作表命名為：**「排班資料」**
2. 在第 1 列（標題列）輸入以下欄位：

| A | B | C | D | E | F | G |
|---|---|---|---|---|---|---|
| 日期 | 開始時間 | 結束時間 | 事業單位 | 人員名單 | 地點 | 顏色 |

3. 在第 2 列輸入測試資料：

| A | B | C | D | E | F | G |
|---|---|---|---|---|---|---|
| 2025-11-05 | 09:00 | 17:00 | 台北診所 | 王醫師, 李護理師 | 台北市中山區XXX路 | #FF5733 |

### 4.3 取得 Sheet ID

1. 看瀏覽器網址列，複製 `/d/` 和 `/edit` 之間的部分

```
https://docs.google.com/spreadsheets/d/【這段就是 Sheet ID】/edit
                                      ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
```

範例：
```
https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
                                       ↑
                        Sheet ID: 1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms
```

✅ **Google Sheets 準備完成！記下這個 Sheet ID**

---

## 📱 步驟 5: 執行 App (1分鐘)

### 5.1 建置並執行

1. 在 Android Studio 中：
   - 連接 Android 裝置（啟用 USB 偵錯）
   - 或啟動模擬器
2. 點擊綠色的 **「Run」** 按鈕（或按 `Shift+F10`）
3. 等待 App 安裝並啟動

### 5.2 首次設定

#### 登入 Google 帳號

1. App 啟動後，點擊底部的 **「設定」** 圖示
2. 在「Google 帳號」區域，點擊 **「登入 Google 帳號」**
3. 選擇您的 Google 帳號
4. 授權應用程式存取（點擊 **「允許」**）

#### 設定 Google Sheets

1. 在設定頁面，點擊 **「設定 Google Sheets」**
2. 貼上您的 **Sheet ID**（步驟 4.3 複製的）
3. 資料範圍保持預設：`排班資料!A2:G`
4. 點擊 **「確定」**
5. 點擊 **「測試連線」**
6. 看到 **「✅ 連線成功」** 表示設定正確

#### 同步資料

1. 點擊 **「立即同步」** 按鈕
2. 等待同步完成（約 2-3 秒）
3. 返回首頁（點擊底部的 **「行事曆」** 圖示）
4. 您應該能看到剛才在 Google Sheets 輸入的排班資料

✅ **完成！您的醫療行事曆 App 已經可以使用了！**

---

## 🎉 下一步

### 測試功能

1. **查看排班**
   - 在行事曆頁面滑動查看不同日期
   - 點擊事件查看詳細資訊

2. **修改排班**
   - 回到 Google Sheets
   - 修改或新增排班資料
   - 在 App 中點擊「同步」
   - 查看更新後的資料

3. **查看 Google 日曆**
   - 開啟手機的日曆 App
   - 找到「醫療排班」日曆
   - 排班會自動同步到這裡

### 新增更多排班

回到 Google Sheets，在新的列中輸入更多排班資料：

```
| 2025-11-06 | 13:00 | 21:00 | 新竹診所 | 陳醫師, 張護理師 | 新竹市東區YYY路 | #33C3FF |
| 2025-11-07 | 08:30 | 16:30 | 台中診所 | 黃醫師 | 台中市西區ZZZ路 | #8E44AD |
```

然後在 App 中同步即可！

---

## ❓ 常見問題

### Q: Google 登入失敗？

**A**: 檢查以下項目：
- ✅ SHA-1 指紋是否正確
- ✅ 套件名稱是否為 `com.medical.calendar`
- ✅ 您的 Google 帳號是否加入測試使用者

### Q: 測試連線失敗？

**A**: 檢查以下項目：
- ✅ Sheet ID 是否正確（沒有多餘空格）
- ✅ Google 帳號是否有權限存取該試算表
- ✅ 工作表名稱是否為「排班資料」

### Q: 同步後沒有資料？

**A**: 檢查以下項目：
- ✅ Google Sheets 的日期格式是否為 `YYYY-MM-DD`
- ✅ 時間格式是否為 `HH:MM`
- ✅ 資料是否從第 2 列開始（第 1 列是標題）
- ✅ 查看 Logcat 是否有錯誤訊息

---

## 📚 進階設定

完成基本設定後，您可以：

- 📖 閱讀 [完整建置指南](./README_BUILD.md)
- 📖 閱讀 [Google Sheets 設定詳解](./GOOGLE_SHEETS_SETUP.md)
- 📖 查看 [變更日誌](./CHANGELOG.md)
- 🔧 自訂 App 功能和 UI

---

## 🆘 需要幫助？

如果遇到問題：

1. **查看文檔**：閱讀 `GOOGLE_SHEETS_SETUP.md` 的常見問題區段
2. **檢查 Logcat**：在 Android Studio 中查看錯誤訊息
3. **重新設定**：登出後重新登入並設定

---

## 🎊 恭喜！

您已經成功設定醫療行事曆 App！

享受使用吧！ 🚀

---

**最後更新**: 2025-11-03  
**版本**: 1.0.0



