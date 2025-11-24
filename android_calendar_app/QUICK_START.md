# 快速開始指南 - Google Sheets 排班系統

這是一個簡化的快速開始指南，幫助您在 15 分鐘內完成設定並開始使用。

---

## ⚡ 快速設定（3 步驟）

### 步驟 1：準備 Google Sheets（5 分鐘）

1. **建立新試算表**
   - 前往 [Google Sheets](https://sheets.google.com)
   - 點擊「空白」建立新試算表

2. **設定表格**
   - 將工作表命名為：`排班資料`
   - 在第一列輸入標題：

   ```
   | 日期       | 開始時間 | 結束時間 | 事業單位 | 人員名單   | 地點 | 顏色    |
   ```

3. **新增範例資料**
   
   ```
   | 2025-10-20 | 09:00 | 17:00 | 台北診所 | 王醫師, 李護理師 | 台北市XXX路 | #FF5733 |
   | 2025-10-21 | 09:00 | 17:00 | 新竹診所 | 陳醫師           | 新竹市YYY路 | #33C3FF |
   ```

4. **取得 Sheet ID**
   - 從網址列複製 `/d/` 和 `/edit` 之間的字串
   - 範例：`https://docs.google.com/spreadsheets/d/【這裡】/edit`

### 步驟 2：設定 Google API（5 分鐘）

1. **建立專案**
   - 前往 [Google Cloud Console](https://console.cloud.google.com/)
   - 建立新專案：`Medical Calendar`

2. **啟用 API**
   - 搜尋並啟用「Google Sheets API」
   - 搜尋並啟用「Google Calendar API」

3. **設定 OAuth**
   - 前往「OAuth 同意畫面」
   - 選擇「外部」並填寫基本資訊
   - 新增範圍：
     - `.../auth/spreadsheets.readonly`
     - `.../auth/calendar`

4. **建立憑證**
   - 建立「OAuth 用戶端 ID」
   - 類型選擇「Android」
   - 套件名稱：`com.medical.calendar`
   - 輸入 SHA-1 指紋（Debug）：
     ```bash
     keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
     ```

### 步驟 3：安裝應用程式（5 分鐘）

1. **編譯應用程式**
   ```bash
   cd android_calendar_app
   ./gradlew clean assembleDebug
   ```

2. **安裝到裝置**
   ```bash
   ./gradlew installDebug
   ```

3. **設定應用程式**
   - 開啟應用程式
   - 前往「設定」
   - 點擊「登入 Google 帳號」
   - 輸入 Sheet ID
   - 點擊「同步」

---

## ✅ 驗證設定

### 檢查清單

執行以下檢查確認一切正常：

- [ ] **Google Sheets**
  - [ ] 試算表已建立
  - [ ] 欄位格式正確
  - [ ] 已取得 Sheet ID

- [ ] **Google API**
  - [ ] Google Sheets API 已啟用
  - [ ] Google Calendar API 已啟用
  - [ ] OAuth 已設定

- [ ] **應用程式**
  - [ ] 應用程式已安裝
  - [ ] Google 帳號已登入
  - [ ] Sheet ID 已設定
  - [ ] 測試連線成功

- [ ] **功能測試**
  - [ ] 可以讀取排班資料
  - [ ] 排班顯示在應用程式中
  - [ ] 排班同步到 Google 日曆
  - [ ] 手機日曆顯示排班

---

## 📝 基本使用

### 新增排班

1. 在 Google Sheets 中新增一列：
   ```
   | 2025-10-22 | 09:00 | 17:00 | 台中診所 | 黃醫師 | 台中市ZZZ路 | #8E44AD |
   ```

2. 在應用程式中點擊「同步」

3. 排班會自動：
   - 顯示在應用程式中
   - 同步到 Google 日曆
   - 出現在手機日曆

### 修改排班

1. 直接在 Google Sheets 中修改

2. 在應用程式中點擊「同步」

3. 變更會自動更新到所有地方

### 刪除排班

1. 在 Google Sheets 中刪除該列

2. 在應用程式中點擊「同步」

3. 排班會從所有地方移除

---

## 💡 小技巧

### 1. 快速填入日期

在 Google Sheets 中：
- 輸入第一個日期：`2025-10-20`
- 拖曳填滿控點（右下角小方塊）
- 日期會自動遞增

### 2. 顏色代碼建議

```
日班：   #FFD700 (金色)
小夜班： #FF8C00 (橘色)
大夜班： #4169E1 (藍色)
```

### 3. 批量編輯

直接在 Google Sheets 中：
- 複製整列
- 修改日期和人員
- 貼上多列

### 4. 共享編輯

點擊 Google Sheets 的「共用」按鈕：
- 輸入協作者 Email
- 設定為「編輯者」
- 多人可同時編輯

---

## 🆘 常見問題

### Q: 無法連線到 Google Sheets？

**檢查**：
1. Sheet ID 是否正確？
2. Google 帳號是否有權限存取？
3. Google Sheets API 是否已啟用？

**解決**：
- 重新確認 Sheet ID
- 在 Google Sheets 中點擊「共用」→ 加入自己的帳號
- 前往 Google Cloud Console 確認 API 狀態

### Q: 排班沒有出現在 Google 日曆？

**檢查**：
1. Google Calendar API 是否已啟用？
2. 是否授權了日曆權限？
3. 是否有顯示「醫療排班」日曆？

**解決**：
- 重新登入 Google 帳號
- 確認授權時勾選了「管理 Google 日曆」
- 在 Google 日曆設定中檢查「醫療排班」日曆是否可見

### Q: 日期格式錯誤？

**正確格式**：
- 日期：`YYYY-MM-DD` 例如 `2025-10-20`
- 時間：`HH:MM` 例如 `09:00`

**錯誤範例**：
- ❌ `2025/10/20` (使用斜線)
- ❌ `20-10-2025` (順序錯誤)
- ❌ `9:00` (缺少前導零)

### Q: 應用程式編譯失敗？

**解決**：
```bash
# 清理並重新編譯
./gradlew clean
./gradlew --refresh-dependencies
./gradlew assembleDebug
```

---

## 📚 進階設定

### 自訂 Sheet 範圍

預設範圍：`排班資料!A2:G`

如果您的資料在不同位置：
```
工作表名稱!開始欄位:結束欄位
```

範例：
- `Sheet1!A2:G` - Sheet1 的 A2 到 G 欄
- `十月排班!A2:G100` - 十月排班工作表的前 100 列
- `排班!B3:H` - 從 B3 開始到 H 欄

### 提醒設定

排班事件預設提前 1 小時提醒。

**修改方式**：
1. 在 Google 日曆中開啟事件
2. 點擊「編輯」
3. 修改提醒時間
4. 儲存

---

## 🎯 下一步

設定完成後，建議：

1. **閱讀完整文件**
   - [GOOGLE_SHEETS_SETUP.md](GOOGLE_SHEETS_SETUP.md) - 詳細設定指南
   - [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md) - 從 Supabase 遷移

2. **測試所有功能**
   - 新增排班
   - 修改排班
   - 刪除排班
   - 離線使用

3. **設定協作**
   - 邀請團隊成員
   - 設定適當權限
   - 測試多人編輯

4. **定期備份**
   - 下載 Google Sheets 為 Excel/CSV
   - 定期檢查資料正確性

---

## 📞 需要幫助？

如果遇到問題：

1. **查看完整文件**
   - [GOOGLE_SHEETS_SETUP.md](GOOGLE_SHEETS_SETUP.md)
   - [MIGRATION_GUIDE.md](MIGRATION_GUIDE.md)

2. **檢查日誌**
   ```bash
   adb logcat | grep "GoogleSheets\|GoogleCalendar"
   ```

3. **聯絡支援**
   - Email: your-email@example.com
   - GitHub Issues: [專案連結]

---

## 🎉 完成！

恭喜您完成設定！現在您可以：

- ✅ 在 Google Sheets 中管理排班
- ✅ 在應用程式中查看排班
- ✅ 在 Google 日曆中查看排班
- ✅ 在手機日曆中接收提醒

**開始使用吧！** 🚀

---

**最後更新**：2025-10-20  
**版本**：2.0.0


