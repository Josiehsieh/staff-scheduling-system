# 如何執行網頁版排班系統 - 圖解教學

本文說明如何正確開啟 `shift_management_system_sheets.html`

---

## ❌ 錯誤方式（不能用）

### ❌ 方式 1：直接雙擊 HTML 檔案

```
你做的：
雙擊 shift_management_system_sheets.html

結果：
瀏覽器顯示：file:///C:/Users/josie/.../shift_management_system_sheets.html

問題：
❌ Google OAuth 不支援 file:// 協定
❌ 無法登入 Google 帳號
❌ 無法存取 Google Sheets
```

**為什麼不能直接雙擊？**

Google OAuth 2.0 安全性要求：
- ✅ 允許：`http://localhost`
- ✅ 允許：`https://yourdomain.com`
- ❌ 不允許：`file:///C:/...`

---

## ✅ 正確方式（必須用本機伺服器）

### 原理說明

```
本機伺服器
    ↓
透過 http:// 協定提供檔案
    ↓
瀏覽器看到：http://localhost:8000/...
    ↓
Google OAuth 允許 ✅
    ↓
可以正常登入和使用
```

---

## 🚀 方法一：使用 Python（最簡單）⭐ 推薦

### 步驟 1：檢查是否有 Python

開啟 PowerShell 或命令提示字元，輸入：

```powershell
python --version
```

**如果看到**：
```
Python 3.x.x
```
✅ 已安裝，直接跳到步驟 3

**如果看到**：
```
'python' 不是內部或外部命令...
```
❌ 未安裝，繼續步驟 2

### 步驟 2：安裝 Python（如果沒有）

1. **下載 Python**
   - 前往：https://www.python.org/downloads/
   - 點擊「Download Python 3.x.x」

2. **安裝 Python**
   - 執行下載的安裝檔
   - ⚠️ **重要**：勾選「Add Python to PATH」
   - 點擊「Install Now」
   - 等待安裝完成

3. **驗證安裝**
   - 重新開啟 PowerShell（舊的要關掉）
   - 再次執行：`python --version`
   - 應該看到版本號

### 步驟 3：啟動本機伺服器

1. **開啟 PowerShell**
   ```
   方法 A：按 Win + X，選擇「Windows PowerShell」
   方法 B：按 Win + R，輸入 powershell，按 Enter
   方法 C：在開始選單搜尋「PowerShell」
   ```

2. **切換到專案目錄**
   ```powershell
   cd C:\Users\josie\staff-scheduling-system
   ```

3. **啟動伺服器**
   ```powershell
   python -m http.server 8000
   ```

4. **看到成功訊息**
   ```
   Serving HTTP on :: port 8000 (http://[::]:8000/) ...
   ```
   ✅ 成功！伺服器正在執行

### 步驟 4：開啟瀏覽器

1. 開啟 Chrome 或 Edge 瀏覽器

2. 在網址列輸入：
   ```
   http://localhost:8000/shift_management_system_sheets.html
   ```

3. 按 Enter

✅ **應該看到登入畫面！**

### 步驟 5：使用完畢後停止伺服器

在 PowerShell 視窗中按 `Ctrl + C` 停止伺服器

---

## 🎨 方法二：使用 Visual Studio Code（如果有安裝）

### 前提條件

- ✅ 已安裝 Visual Studio Code

### 步驟

1. **開啟 VSCode**

2. **開啟專案資料夾**
   - 點擊「File」→「Open Folder...」
   - 選擇：`C:\Users\josie\staff-scheduling-system`
   - 點擊「選擇資料夾」

3. **安裝 Live Server 擴充功能**
   - 點擊左側的「Extensions」圖示（或按 Ctrl+Shift+X）
   - 搜尋：`Live Server`
   - 找到「Live Server by Ritwick Dey」
   - 點擊「Install」

4. **執行 Live Server**
   - 在左側檔案列表找到 `shift_management_system_sheets.html`
   - 右鍵點擊檔案
   - 選擇「Open with Live Server」
   - 瀏覽器會自動開啟

✅ **完成！**

---

## 💻 方法三：使用 Node.js（如果有安裝）

### 前提條件

- ✅ 已安裝 Node.js

### 步驟

1. **開啟 PowerShell**

2. **切換到專案目錄**
   ```powershell
   cd C:\Users\josie\staff-scheduling-system
   ```

3. **啟動伺服器**
   ```powershell
   npx http-server -p 8000
   ```

4. **開啟瀏覽器**
   ```
   http://localhost:8000/shift_management_system_sheets.html
   ```

✅ **完成！**

---

## 📱 方法四：部署到網路（進階）

如果想要在任何地方存取（不只是本機）：

### 選項 A：GitHub Pages（免費）

1. 上傳 HTML 到 GitHub Repository
2. 啟用 GitHub Pages
3. 得到網址：`https://username.github.io/project/shift_management_system_sheets.html`
4. ⚠️ 記得更新 OAuth 設定的授權來源

### 選項 B：Netlify（免費）

1. 註冊 Netlify 帳號
2. 拖放檔案到 Netlify
3. 自動部署
4. 得到 HTTPS 網址
5. ⚠️ 記得更新 OAuth 設定

---

## 🔧 疑難排解

### 問題 1：python 指令找不到

**錯誤訊息**：
```
'python' 不是內部或外部命令...
```

**解決方法**：
1. 安裝 Python（參考上面步驟）
2. 或嘗試使用 `python3` 替代 `python`
   ```powershell
   python3 -m http.server 8000
   ```

### 問題 2：port 8000 已被佔用

**錯誤訊息**：
```
OSError: [WinError 10048] 通常每個通訊端位址只允許使用一次
```

**解決方法**：
1. 更換 port：
   ```powershell
   python -m http.server 8080
   ```
   然後開啟：`http://localhost:8080/shift_management_system_sheets.html`

2. 或關閉佔用 port 8000 的程式

### 問題 3：權限被拒絕

**錯誤訊息**：
```
PermissionError: [WinError 5] 存取被拒絕
```

**解決方法**：
1. 以系統管理員身分執行 PowerShell
2. 或更換到有權限的目錄

### 問題 4：OAuth 錯誤

**錯誤訊息**：
```
redirect_uri_mismatch
```

**解決方法**：
1. 檢查 OAuth 設定的「授權的 JavaScript 來源」
2. 確認包含：`http://localhost:8000`
3. 確認網址列顯示的是 `http://localhost:8000/...`（不是 `file:///...`）

---

## ✅ 快速檢查清單

開啟網頁版前，確認：

- [ ] 已完成 Google Cloud API 設定
- [ ] 已建立 Google Sheets 試算表
- [ ] 已記下用戶端 ID 和 Sheet ID
- [ ] 已安裝 Python（或其他伺服器工具）
- [ ] 已啟動本機伺服器
- [ ] 瀏覽器網址顯示 `http://localhost:8000/...`（不是 `file:///...`）

---

## 📊 方法比較

| 方法 | 難度 | 速度 | 適合對象 |
|------|------|------|---------|
| **Python http.server** | ⭐ 簡單 | ⚡ 快 | 所有人（推薦） |
| **VSCode Live Server** | ⭐⭐ 中等 | ⚡⚡ 很快 | 已有 VSCode 的開發者 |
| **Node.js http-server** | ⭐⭐ 中等 | ⚡⚡ 很快 | 已有 Node.js 的開發者 |
| **部署到網路** | ⭐⭐⭐ 困難 | ⚡⚡⚡ 最快 | 需要公開存取的情況 |

---

## 🎯 推薦方案

### 給初學者
✅ **使用 Python http.server**
- 安裝簡單
- 操作簡單
- 跨平台

### 給開發者
✅ **使用 VSCode Live Server**
- 自動重新載入
- 開發體驗好
- 功能豐富

### 給多人使用
✅ **部署到 GitHub Pages 或 Netlify**
- 不用每次啟動伺服器
- 可以分享網址
- HTTPS 加密

---

## 💡 最佳實踐

### 開發時（本機測試）

```powershell
# 在專案目錄
cd C:\Users\josie\staff-scheduling-system

# 啟動伺服器
python -m http.server 8000

# 開啟瀏覽器
# http://localhost:8000/shift_management_system_sheets.html

# 完成後按 Ctrl+C 停止
```

### 正式使用（建議部署）

部署到 GitHub Pages 或 Netlify，得到固定網址：
- 不用每次啟動伺服器
- 手機也能存取
- 可以分享給其他人

---

**記住**：❌ 不能直接雙擊 HTML，✅ 必須使用本機伺服器！

---

**最後更新**：2025-11-03  
**版本**：1.0.0



