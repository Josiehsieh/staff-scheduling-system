# 🌐 部署排班系統到 GitHub Pages

## 💡 回答您的問題

**Q：放到 GitHub 當網頁是否會減少 OAuth 配置錯誤？**

**A：會有幫助，但無法完全避免。**

### ✅ 會改善的部分

1. **更明確是網頁應用程式**
   - 使用真實網址（https://yourusername.github.io/project）
   - 不是 localhost，讓人更清楚這是網頁
   - HTTPS 協定更正式

2. **不需要本地 HTTP Server**
   - 用戶不用執行 `python -m http.server`
   - 直接開啟網址就能用
   - 減少技術門檻

3. **URL 穩定且統一**
   - 每個人使用相同的基礎 URL
   - 不會有 port 衝突問題
   - 更容易在文檔中說明

### ⚠️ 仍需要注意的部分

1. **還是要選「網頁應用程式」**
   - 這個錯誤來自不理解應用程式類型
   - 不管是 localhost 還是 GitHub Pages，都要選「網頁應用程式」

2. **每個用戶需要自己的 Google Cloud 專案**
   - 無法共用用戶端 ID（安全性問題）
   - 每個人都要走一次 OAuth 設定流程

3. **OAuth 配置還是需要正確設定**
   - 重新導向 URI 要改成 GitHub Pages 的網址
   - 測試使用者還是要設定

---

## 🎯 部署到 GitHub Pages 的優缺點

### ✅ 優點

| 項目 | localhost | GitHub Pages |
|------|-----------|--------------|
| **安裝** | 需要 Python/HTTP Server | 不需要任何安裝 |
| **網址** | http://localhost:8000 | https://user.github.io/project |
| **分享** | 困難（只能本地使用） | 容易（直接分享網址） |
| **HTTPS** | ❌ 只有 HTTP | ✅ 強制 HTTPS |
| **執行** | 需要啟動 server | 直接開啟網址 |
| **更新** | 改檔案就生效 | Push 到 GitHub 自動更新 |

### ⚠️ 缺點與挑戰

1. **安全性問題**
   ```
   ❌ 不能把用戶端 ID 寫死在 HTML 中（會公開）
   ✅ 需要讓用戶自己設定（就像現在的設定頁面）
   ```

2. **每個用戶需要自己配置**
   ```
   - 每個人建立自己的 Google Cloud 專案
   - 設定自己的 OAuth 用戶端 ID
   - 在網頁的設定頁面輸入
   ```

3. **資料庫限制**
   ```
   - GitHub Pages 只支援靜態網頁
   - 如果需要資料庫，需要另外處理（Firebase、Supabase 等）
   ```

---

## 📋 部署到 GitHub Pages 的步驟

### 步驟 1：準備 Repository

1. **在 GitHub 建立新 Repository**
   ```
   Repository name: staff-scheduling-system
   Description: 醫療人員排班管理系統
   Public（公開）或 Private（私有）
   ```

2. **上傳檔案**
   ```bash
   cd C:\Users\josie\staff-scheduling-system
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin https://github.com/yourusername/staff-scheduling-system.git
   git push -u origin main
   ```

### 步驟 2：啟用 GitHub Pages

1. **進入 Repository 設定**
   - 在 GitHub 上開啟您的 repository
   - 點擊「Settings」

2. **設定 Pages**
   - 左側選單找到「Pages」
   - Source: Deploy from a branch
   - Branch: main
   - Folder: / (root)
   - 點擊「Save」

3. **等待部署**
   - 幾分鐘後會出現網址
   - 例如：`https://yourusername.github.io/staff-scheduling-system/`

### 步驟 3：更新 OAuth 設定

**重要：需要為 GitHub Pages 網址重新設定 OAuth！**

1. **前往 Google Cloud Console**
   ```
   https://console.cloud.google.com/apis/credentials
   ```

2. **編輯您的 OAuth 用戶端 ID**

3. **更新已授權的 JavaScript 來源**
   - 保留：`http://localhost:8000`（開發用）
   - 新增：`https://yourusername.github.io`

4. **更新已授權的重新導向 URI**
   - 保留 localhost 的三個（開發用）
   - 新增以下三個（正式用）：
   ```
   https://yourusername.github.io/staff-scheduling-system
   https://yourusername.github.io/staff-scheduling-system/
   https://yourusername.github.io/staff-scheduling-system/shift_management_system_sheets_full.html
   ```

5. **儲存**

### 步驟 4：測試

1. **開啟 GitHub Pages 網址**
   ```
   https://yourusername.github.io/staff-scheduling-system/shift_management_system_sheets_full.html
   ```

2. **進入設定頁面**
   - 輸入您的用戶端 ID
   - 儲存設定

3. **登入 Google**
   - 應該能正常登入
   - 授權 Sheets 和 Calendar 權限

4. **測試功能**
   - 新增排班
   - 上傳到行事曆

---

## 🔐 安全性考量

### 方案 1：讓每個用戶自己設定（推薦）

**目前的方式**：
```javascript
// 用戶在設定頁面輸入自己的用戶端 ID
// 儲存在 localStorage
// 不會暴露給其他人
```

**優點**：
- ✅ 安全（每個人用自己的 Google Cloud 專案）
- ✅ 不需要後端
- ✅ 適合內部使用

**缺點**：
- ⚠️ 每個用戶需要設定 Google Cloud

### 方案 2：使用環境變數 + GitHub Actions（進階）

**需要後端支援**，不適合純靜態網頁。

### 方案 3：Firebase / Supabase（推薦給多人使用）

如果要給多人使用，考慮使用：
- Firebase Authentication
- Supabase
- 提供統一的後端和認證

---

## 📝 建議的 README.md 內容

為 GitHub Pages 部署建立一個清楚的 README：

```markdown
# 🏥 醫療人員排班管理系統

## 🌐 線上版本

直接使用：https://yourusername.github.io/staff-scheduling-system/

## ⚙️ 首次設定

### 1. 建立 Google Cloud 專案

1. 訪問：https://console.cloud.google.com
2. 建立新專案
3. 啟用 Google Sheets API 和 Google Calendar API

### 2. 設定 OAuth

詳細步驟請參考：[API設定快速參考.md](./API設定快速參考.md)

**重要**：在設定重新導向 URI 時，使用：
```
https://yourusername.github.io/staff-scheduling-system/
https://yourusername.github.io/staff-scheduling-system/shift_management_system_sheets_full.html
```

### 3. 在系統中輸入用戶端 ID

1. 開啟系統
2. 點擊「⚙️ 設定」
3. 貼上您的用戶端 ID
4. 儲存

## 🔧 本地開發

如果要在本地開發：

```bash
python -m http.server 8000
```

開啟：http://localhost:8000

OAuth 設定需要包含 localhost 的重新導向 URI。

## 📚 文檔

- [API設定快速參考](./API設定快速參考.md)
- [登入問題故障排除](./登入問題故障排除.md)
- [如何在現有專案中加入行事曆功能](./如何在現有專案中加入行事曆功能.md)
```

---

## 🎯 推薦做法

### 對於個人使用

```
✅ 可以使用 GitHub Pages
✅ 自己設定 Google Cloud 專案
✅ 方便在不同電腦使用
```

### 對於團隊使用（5-10人）

```
✅ 使用 GitHub Pages
⚠️ 每個人設定自己的 Google Cloud 專案
或
✅ 建立一個共用的 Google Cloud 專案
✅ 將所有人加入測試使用者
✅ 分享用戶端 ID（內部使用還好）
```

### 對於大規模使用（>10人）

```
✅ 考慮部署到真實伺服器（Vercel、Netlify）
✅ 使用後端服務（Firebase、Supabase）
✅ 實作正式的用戶系統
✅ 申請 Google OAuth 驗證（公開發布）
```

---

## 📊 比較總結

| 情境 | localhost | GitHub Pages | 正式伺服器 |
|------|-----------|--------------|-----------|
| **設定難度** | ⭐⭐ 中等 | ⭐⭐⭐ 稍高 | ⭐⭐⭐⭐ 高 |
| **使用便利性** | ⭐⭐ 需啟動 | ⭐⭐⭐⭐ 直接用 | ⭐⭐⭐⭐⭐ 最佳 |
| **安全性** | ⭐⭐⭐ HTTP | ⭐⭐⭐⭐ HTTPS | ⭐⭐⭐⭐⭐ 完整 |
| **分享性** | ⭐ 困難 | ⭐⭐⭐⭐ 容易 | ⭐⭐⭐⭐⭐ 最佳 |
| **成本** | ✅ 免費 | ✅ 免費 | ⚠️ 需付費 |
| **OAuth 配置錯誤風險** | ⭐⭐⭐ 中 | ⭐⭐ 較低 | ⭐ 最低 |

---

## ✅ 結論

### 是否推薦部署到 GitHub Pages？

**✅ 推薦，理由：**

1. **改善用戶體驗**
   - 不需要安裝任何東西
   - 直接開啟網址就能用
   - HTTPS 更安全

2. **減少部分配置錯誤**
   - HTTPS 網址讓人更明確這是「網頁應用程式」
   - 不會有 localhost 的混淆

3. **方便維護和更新**
   - Push 到 GitHub 自動部署
   - 版本控制

4. **免費且穩定**
   - GitHub Pages 是免費的
   - 穩定可靠

### ⚠️ 但仍需要：

1. **清楚的文檔**
   - 明確說明要選「網頁應用程式」
   - 提供完整的設定步驟
   - 附上截圖

2. **每個用戶的配置**
   - 還是需要建立 Google Cloud 專案
   - 設定 OAuth（但 URI 會不同）

3. **更新現有的文檔**
   - 提供 GitHub Pages 和 localhost 兩種設定方式
   - 說明 URI 的差異

---

## 🚀 下一步

如果您決定部署到 GitHub Pages，我可以幫您：

1. ✅ 調整 HTML 檔案（如果需要）
2. ✅ 建立完整的 README.md
3. ✅ 更新所有設定文檔，加入 GitHub Pages 的說明
4. ✅ 建立部署腳本

需要我協助嗎？


