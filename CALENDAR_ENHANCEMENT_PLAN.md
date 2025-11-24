# 📅 月曆排班增強計畫 v4.0.0

## 🎯 功能需求

### 1. ✅ 點擊日期直接新增排班
- 月曆上的日期可點擊
- 點擊後自動開啟排班表單
- 自動填入選擇的日期

### 2. ✅ 開始時間自動計算結束時間
- 輸入開始時間後
- 自動設定結束時間 = 開始時間 + 2小時
- 可手動修改

### 3. ✅ 事業單位顯示剩餘時數
下拉選單格式：
```
台北診所 (醫:160h / 護:180h)
新竹診所 (醫:120h / 護:140h)
```
- 即時計算當月已排時數
- 顯示剩餘可排時數
- 選擇單位後自動更新

### 4. ✅ 人員名單改為勾選方式
- 不是文字輸入
- 從該單位的人員列表動態生成勾選框
- 醫生、護理師、其他人員分組顯示
- 多選

### 5. ✅ 地點選擇
- 如果是非分散型：自動顯示單位地址
- 如果是分散型：下拉選單選擇分點
- 自動填入地點欄位

### 6. ✅ 假日顯示
- 整合假日 API
- 假日顯示為紅色背景
- 顯示假日名稱
- 支援多國假日

### 7. ✅ 設定國家假日
在設定頁籤新增：
```
假日設定
國家選擇：[台灣 ▼]
- 台灣
- 中國
- 香港
- 日本
- 美國
等...
```

---

## 📊 實現步驟

### 階段 1：月曆增強
- [ ] 月曆日期改為可點擊
- [ ] 點擊觸發新增排班
- [ ] 自動填入日期

### 階段 2：時間自動計算
- [ ] 監聽開始時間輸入
- [ ] 自動計算+2小時
- [ ] 設定結束時間

### 階段 3：事業單位時數顯示
- [ ] 載入單位時取得當月時數配額
- [ ] 計算已排時數
- [ ] 計算剩餘時數
- [ ] 更新下拉選單顯示
- [ ] 選擇單位後即時更新

### 階段 4：人員勾選
- [ ] 修改表單HTML
- [ ] 載入單位人員列表
- [ ] 動態生成勾選框
- [ ] 分組顯示（醫/護/其他）
- [ ] 收集勾選結果

### 階段 5：地點選擇
- [ ] 判斷單位類型
- [ ] 非分散型：顯示地址
- [ ] 分散型：顯示分點下拉選單
- [ ] 自動填入地點

### 階段 6：假日整合
- [ ] 選擇假日API服務
- [ ] 實現API調用
- [ ] 解析假日資料
- [ ] 月曆上標記假日
- [ ] 紅色背景顯示

### 階段 7：設定頁面
- [ ] 新增國家選擇下拉選單
- [ ] 儲存國家設定
- [ ] 重新載入假日

---

## 🔧 技術實現

### 假日 API 選擇

#### 選項 1：Calendarific API（推薦）
```
https://calendarific.com/
- 免費：每月1000次請求
- 支援200+國家
- 完整的假日資料
```

#### 選項 2：Nager.Date API
```
https://date.nager.at/
- 完全免費
- 開源
- 支援100+國家
```

#### 選項 3：Abstract API
```
https://www.abstractapi.com/holidays-api
- 免費：每月1000次請求
- 簡單易用
```

**建議使用：Nager.Date（完全免費，無限制）**

### API 範例

```javascript
// Nager.Date API
const year = 2025;
const countryCode = 'TW'; // 台灣
const url = `https://date.nager.at/api/v3/PublicHolidays/${year}/${countryCode}`;

fetch(url)
  .then(response => response.json())
  .then(holidays => {
    holidays.forEach(holiday => {
      console.log(holiday.date, holiday.localName);
    });
  });
```

---

## 📋 資料結構

### 假日資料格式

```json
{
  "date": "2025-01-01",
  "localName": "中華民國開國紀念日",
  "name": "New Year's Day",
  "countryCode": "TW",
  "fixed": false,
  "global": true,
  "counties": null,
  "launchYear": null,
  "types": ["Public"]
}
```

### 支援國家代碼

| 國家 | 代碼 | 說明 |
|------|------|------|
| 台灣 | TW | Taiwan |
| 中國 | CN | China |
| 香港 | HK | Hong Kong |
| 日本 | JP | Japan |
| 美國 | US | United States |
| 英國 | GB | United Kingdom |
| 澳洲 | AU | Australia |

---

## 🎨 UI 設計

### 月曆日期（假日）

```html
<div class="calendar-day holiday">
  <div class="day-number">1</div>
  <div class="holiday-name">元旦</div>
  <div class="shifts">...</div>
</div>
```

CSS:
```css
.calendar-day.holiday {
    background: #ffebee;
    border-left: 4px solid #f44336;
}

.holiday-name {
    font-size: 10px;
    color: #f44336;
    font-weight: bold;
}
```

### 事業單位下拉選單

```html
<select id="shiftUnit">
  <option value="">請選擇</option>
  <option value="台北診所">
    台北診所 (醫:160h / 護:180h 剩餘)
  </option>
  <option value="新竹診所">
    新竹診所 (醫:120h / 護:140h 剩餘)
  </option>
</select>
```

### 人員勾選

```html
<div class="staff-selection">
  <h4>醫生</h4>
  <div class="checkbox-group">
    <label><input type="checkbox" value="王醫師"> 王醫師</label>
    <label><input type="checkbox" value="李醫師"> 李醫師</label>
  </div>
  
  <h4>護理師</h4>
  <div class="checkbox-group">
    <label><input type="checkbox" value="陳護理師"> 陳護理師</label>
    <label><input type="checkbox" value="林護理師"> 林護理師</label>
  </div>
</div>
```

---

## 💾 資料儲存

### Google Sheets 不需改動

現有的 Google Sheets 結構無需變更：
- 人員名單仍以逗號分隔字串儲存
- 地點以文字儲存
- 假日資料不儲存（即時從API取得）

### LocalStorage 新增

```javascript
// 假日設定
{
  "holidayCountry": "TW",
  "holidayEnabled": true,
  "holidayCache": {
    "2025": [...假日資料]
  }
}
```

---

## ⚡ 效能優化

### 假日快取

```javascript
// 每年只載入一次
const cacheKey = `holidays_${year}_${countryCode}`;
const cached = localStorage.getItem(cacheKey);

if (cached) {
  return JSON.parse(cached);
} else {
  const holidays = await fetchHolidays(year, countryCode);
  localStorage.setItem(cacheKey, JSON.stringify(holidays));
  return holidays;
}
```

### 時數計算快取

```javascript
// 當月時數計算結果快取
const monthKey = `${year}-${month}`;
if (monthlyHoursCache[monthKey]) {
  return monthlyHoursCache[monthKey];
}
```

---

## 🎯 使用流程

### 新增排班（新流程）

```
1. 在月曆上點擊日期（例如：11月15日）
   ↓
2. 自動開啟排班表單
   - 日期：自動填入 2025-11-15
   - 開始時間：空白
   ↓
3. 選擇開始時間：09:00
   - 結束時間：自動填入 11:00（+2小時）
   ↓
4. 選擇事業單位：台北診所 (醫:160h / 護:180h)
   - 自動載入該單位人員
   - 自動載入地點選項
   ↓
5. 勾選人員
   ☑ 王醫師
   ☑ 李醫師
   ☑ 陳護理師
   ↓
6. 選擇地點（如果是分散型）
   - 下拉選單：台北分點
   ↓
7. 點擊儲存
   - 自動計算並扣除時數
   - 更新剩餘時數顯示
```

---

## 📱 響應式設計

- ✅ 手機上也能輕鬆點擊日期
- ✅ 勾選框適合觸控
- ✅ 下拉選單友善

---

## 🚀 預期效果

### 使用者體驗提升

1. **更快速**：點擊日期直接新增，不用手動輸入日期
2. **更智慧**：時間自動計算，減少錯誤
3. **更清楚**：一眼看到剩餘時數，避免超排
4. **更方便**：勾選人員，不用打字
5. **更直覺**：假日一目了然

### 管理效率提升

1. **時數控管**：即時顯示剩餘時數
2. **避免衝突**：假日提醒
3. **快速操作**：減少輸入時間
4. **減少錯誤**：自動計算和驗證

---

## ✅ 驗收標準

### 功能測試

- [ ] 點擊日期能開啟表單
- [ ] 日期自動填入正確
- [ ] 開始時間輸入後，結束時間自動+2小時
- [ ] 事業單位顯示剩餘時數
- [ ] 剩餘時數計算正確
- [ ] 人員勾選框正確顯示
- [ ] 勾選後正確儲存
- [ ] 分散型單位顯示地點選擇
- [ ] 假日正確顯示（紅色）
- [ ] 假日名稱正確顯示
- [ ] 設定頁面可選擇國家
- [ ] 更改國家後假日更新

### 效能測試

- [ ] 假日載入快速（使用快取）
- [ ] 時數計算即時
- [ ] 介面反應流暢

---

## 📚 相關文檔

完成後需要更新：
- [ ] 使用者手冊
- [ ] 快速開始指南
- [ ] 更新日誌
- [ ] 功能說明

---

**預計完成時間**：2-3小時  
**版本號**：v4.0.0  
**主要更新**：智慧月曆排班系統

---

**準備開始實現！** 🚀



