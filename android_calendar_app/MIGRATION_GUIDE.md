# 從 Supabase 遷移到 Google Sheets 指南

本文件說明如何將現有的 Supabase 排班資料遷移到 Google Sheets。

---

## 📋 遷移概覽

### 變更內容

```diff
- SupabaseService (付費，資料庫)
+ GoogleSheetsService (免費，試算表)

- 複雜的 SQL 查詢
+ 簡單的試算表格式

- 需要資料庫管理知識
+ 使用熟悉的試算表介面
```

### 新增功能

✅ **Google 日曆自動同步**
- 排班資料會自動同步到 Google 日曆
- 透過 Google 日曆同步到所有裝置
- 支援提醒通知

✅ **更簡單的資料管理**
- 直接在 Google Sheets 中編輯
- 支援多人協作
- 自動備份

---

## 🔄 遷移步驟

### 步驟 1：匯出現有資料（如果需要）

如果您有現有的 Supabase 資料需要遷移：

1. 登入 Supabase Dashboard
2. 前往「Table Editor」
3. 選擇 `v_shifts_detail` 視圖
4. 點擊「Export」→「CSV」
5. 下載 CSV 檔案

### 步驟 2：準備 Google Sheets

1. 按照 [GOOGLE_SHEETS_SETUP.md](GOOGLE_SHEETS_SETUP.md) 建立新的試算表
2. 設定正確的欄位格式

### 步驟 3：轉換資料格式

從 Supabase 到 Google Sheets 的欄位對應：

| Supabase 欄位 | Google Sheets 欄位 | 說明 |
|--------------|-------------------|------|
| shift_date | 日期 | 格式：YYYY-MM-DD |
| start_time | 開始時間 | 格式：HH:MM |
| end_time | 結束時間 | 格式：HH:MM |
| unit_name | 事業單位 | 直接複製 |
| staff_names | 人員名單 | 用逗號分隔 |
| branch_name | 地點 | 直接複製 |
| unit_color | 顏色 | 格式：#RRGGBB |

### 步驟 4：匯入資料

**方法 1：手動複製貼上**
1. 開啟匯出的 CSV 檔案
2. 選擇需要的欄位
3. 複製並貼到 Google Sheets 對應欄位

**方法 2：Google Sheets 匯入**
1. 在 Google Sheets 中點擊「檔案」→「匯入」
2. 選擇匯出的 CSV 檔案
3. 選擇「附加到目前工作表」
4. 調整欄位順序和格式

### 步驟 5：更新應用程式

1. **更新程式碼**（已完成）
   - ✅ GoogleSheetsService 已建立
   - ✅ GoogleCalendarService 已增強
   - ✅ CalendarRepository 已更新
   - ✅ build.gradle 已更新

2. **重新編譯應用程式**
   ```bash
   cd android_calendar_app
   ./gradlew clean
   ./gradlew assembleDebug
   ```

3. **安裝新版本**
   ```bash
   ./gradlew installDebug
   ```

### 步驟 6：設定應用程式

1. 開啟應用程式
2. 前往「設定」頁面
3. 登入 Google 帳號
4. 輸入 Google Sheet ID
5. 測試連線
6. 點擊「同步」按鈕

---

## 🔍 驗證遷移

### 檢查清單

- [ ] Google Sheets 資料格式正確
- [ ] 應用程式成功連線到 Google Sheets
- [ ] 同步功能正常運作
- [ ] 排班資料正確顯示在應用程式中
- [ ] 排班資料成功同步到 Google 日曆
- [ ] 手機原生日曆顯示排班事件

### 測試步驟

1. **測試讀取**
   - 在應用程式中點擊「同步」
   - 確認排班資料正確載入

2. **測試新增**
   - 在 Google Sheets 中新增一筆排班
   - 在應用程式中同步
   - 確認新排班出現

3. **測試修改**
   - 在 Google Sheets 中修改一筆排班
   - 在應用程式中同步
   - 確認修改生效

4. **測試 Google 日曆同步**
   - 開啟 Google 日曆網頁或 App
   - 查看「醫療排班」日曆
   - 確認排班事件正確顯示

5. **測試離線功能**
   - 關閉網路連線
   - 開啟應用程式
   - 確認仍可查看已同步的排班

---

## 🗑️ 清理舊系統（選擇性）

遷移完成並確認正常運作後，可以清理舊的 Supabase 相關設定：

### 保留 Supabase（建議）

如果未來可能需要切換回 Supabase，建議保留設定：

1. Supabase 依賴已在 build.gradle 中註解
2. SupabaseService.kt 檔案可以保留
3. 需要時取消註解即可恢復

### 完全移除 Supabase

如果確定不再使用 Supabase：

1. **刪除 SupabaseService.kt**
   ```bash
   rm android_calendar_app/app/src/main/java/com/medical/calendar/data/remote/SupabaseService.kt
   ```

2. **從 build.gradle 移除註解的依賴**
   ```gradle
   // 刪除以下註解區塊
   // implementation 'io.github.jan-tennert.supabase:postgrest-kt:1.4.7'
   // implementation 'io.github.jan-tennert.supabase:realtime-kt:1.4.7'
   // implementation 'io.github.jan-tennert.supabase:storage-kt:1.4.7'
   // implementation 'io.github.jan-tennert.supabase:gotrue-kt:1.4.7'
   ```

3. **移除相關的 import**
   - 檢查是否有其他檔案 import SupabaseService
   - 如有則需要一併更新

---

## ⚡ 效能對比

### 前後對比

| 項目 | Supabase | Google Sheets | 差異 |
|-----|----------|--------------|------|
| 成本 | 免費版限制，超過需付費 | 完全免費 | 💰 省錢 |
| API 配額 | 有限制 | 每日 500 讀 / 100 寫 | ✅ 足夠 |
| 查詢速度 | ~0.3 秒 | ~0.5-2 秒 | ⚠️ 稍慢 |
| 資料管理 | 需要 SQL 知識 | 試算表界面 | ✅ 更簡單 |
| 多人協作 | 需要設定權限 | 原生支援 | ✅ 更容易 |
| 備份 | 需要手動 | 自動備份 | ✅ 更安全 |
| 離線功能 | 需要快取 | 需要快取 | ⚖️ 相同 |

### 結論

對於每月更新一次的排班資料：
- ✅ Google Sheets 是更好的選擇
- ✅ 成本更低
- ✅ 管理更簡單
- ⚠️ 速度稍慢但可接受

---

## 🆘 問題排除

### 問題 1：無法連線到 Google Sheets

**可能原因**：
- Sheet ID 錯誤
- 沒有權限存取試算表
- Google Sheets API 未啟用

**解決方法**：
1. 檢查 Sheet ID 是否正確
2. 確認 Google 帳號有存取權限
3. 前往 Google Cloud Console 確認 API 已啟用

### 問題 2：同步後資料不正確

**可能原因**：
- Google Sheets 格式錯誤
- 日期時間格式不符

**解決方法**：
1. 檢查日期格式：必須是 `YYYY-MM-DD`
2. 檢查時間格式：必須是 `HH:MM`
3. 確認必填欄位都有資料

### 問題 3：Google 日曆沒有顯示排班

**可能原因**：
- Google Calendar 服務未初始化
- 沒有授權 Google 日曆權限

**解決方法**：
1. 重新登入 Google 帳號
2. 確認授權時勾選了「管理 Google 日曆」
3. 檢查「醫療排班」日曆是否已建立

### 問題 4：編譯錯誤

**可能原因**：
- 依賴未正確下載
- Hilt 注入設定問題

**解決方法**：
1. 執行 Gradle Sync
   ```bash
   ./gradlew --refresh-dependencies
   ```
2. 清理並重新編譯
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

---

## 📞 支援

如果遇到問題：

1. **查看日誌**
   ```bash
   adb logcat | grep "GoogleSheets\|GoogleCalendar"
   ```

2. **檢查設定**
   - Google Sheets 格式
   - API 權限
   - Sheet ID

3. **聯絡支援**
   - Email: your-email@example.com
   - GitHub Issues: [專案連結]

---

## 🎯 下一步

遷移完成後，建議：

1. ✅ 閱讀 [GOOGLE_SHEETS_SETUP.md](GOOGLE_SHEETS_SETUP.md) 了解詳細使用方式
2. ✅ 定期備份 Google Sheets 資料
3. ✅ 設定適當的共享權限
4. ✅ 測試所有功能確保正常運作

---

**最後更新**：2025-10-20
**版本**：2.0.0

祝遷移順利！🚀


