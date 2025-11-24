# 醫療行事曆 Android 應用程式安裝指南

## 前置需求

### 1. 開發環境
- **Android Studio** Arctic Fox 或更新版本
- **Android SDK** API 24+ (Android 7.0+)
- **Java Development Kit (JDK)** 11 或更新版本
- **Kotlin** 1.8.20 或更新版本

### 2. Google 服務設定
- **Google Cloud Console** 專案
- **Google Calendar API** 啟用
- **OAuth 2.0 憑證** 設定

### 3. Supabase 設定
- 現有的 Supabase 專案
- 資料庫連接資訊

## 安裝步驟

### 1. 克隆專案
```bash
git clone <repository-url>
cd android_calendar_app
```

### 2. 設定 Google Calendar API

#### 2.1 建立 Google Cloud Console 專案
1. 前往 [Google Cloud Console](https://console.cloud.google.com/)
2. 建立新專案或選擇現有專案
3. 啟用 Google Calendar API

#### 2.2 建立 OAuth 2.0 憑證
1. 前往「憑證」頁面
2. 建立 OAuth 2.0 用戶端 ID
3. 設定應用程式類型為「Android」
4. 下載 `google-services.json` 檔案

#### 2.3 設定應用程式
1. 將 `google-services.json` 放入 `app/` 目錄
2. 在 `app/build.gradle` 中確認已添加 Google Services 插件

### 3. 設定 Supabase

#### 3.1 更新 Supabase 設定
在 `app/src/main/java/com/medical/calendar/data/remote/SupabaseService.kt` 中更新：
```kotlin
private val supabaseClient: SupabaseClient = createSupabaseClient(
    supabaseUrl = "YOUR_SUPABASE_URL",
    supabaseKey = "YOUR_SUPABASE_ANON_KEY"
)
```

#### 3.2 確認資料庫結構
確保 Supabase 資料庫包含以下表格：
- `v_shifts_detail` - 排班詳細資料
- `v_units_complete` - 事業單位完整資料

### 4. 建置和運行

#### 4.1 同步 Gradle 依賴
在 Android Studio 中：
1. 開啟專案
2. 等待 Gradle 同步完成
3. 解決任何依賴衝突

#### 4.2 運行應用程式
1. 連接 Android 裝置或啟動模擬器
2. 點擊「Run」按鈕
3. 選擇目標裝置
4. 等待應用程式安裝和啟動

## 權限設定

應用程式需要以下權限：

### 必要權限
- **網路存取** - 同步資料
- **日曆讀取/寫入** - 管理事件
- **通知** - 推送提醒

### 權限請求
應用程式會在首次啟動時請求必要權限，請允許以下權限：
- 日曆存取
- 網路連接
- 通知

## 功能測試

### 1. 基本功能
- [ ] 應用程式啟動
- [ ] 日曆視圖顯示
- [ ] 月份導航
- [ ] 日期選擇

### 2. 醫療排班同步
- [ ] 連接到 Supabase
- [ ] 載入排班資料
- [ ] 顯示排班事件
- [ ] 事件顏色對應

### 3. 個人事件
- [ ] 新增個人事件
- [ ] 編輯事件
- [ ] 刪除事件

### 4. Google 日曆整合
- [ ] Google 帳戶登入
- [ ] 日曆列表載入
- [ ] 事件同步
- [ ] 雙向同步

## 故障排除

### 常見問題

#### 1. 建置失敗
- 檢查 Android Studio 版本
- 確認 Gradle 版本相容性
- 清理並重新建置專案

#### 2. 網路連接問題
- 檢查網路連接
- 確認 Supabase URL 和 Key
- 檢查防火牆設定

#### 3. Google 登入失敗
- 確認 `google-services.json` 設定
- 檢查 OAuth 2.0 憑證
- 確認 SHA-1 指紋設定

#### 4. 日曆權限問題
- 在設定中手動啟用日曆權限
- 重新安裝應用程式
- 檢查 Android 版本相容性

### 除錯模式
啟用除錯模式以獲取詳細日誌：
```kotlin
// 在 MainActivity 中添加
if (BuildConfig.DEBUG) {
    Log.d("CalendarApp", "Debug mode enabled")
}
```

## 部署

### 1. 準備發布版本
```bash
./gradlew assembleRelease
```

### 2. 簽署 APK
1. 建立簽署金鑰
2. 設定 `signingConfigs`
3. 建置簽署版本

### 3. 發布到 Google Play Store
1. 建立開發者帳戶
2. 上傳 APK
3. 填寫應用程式資訊
4. 發布應用程式

## 支援

如有問題，請：
1. 檢查本文件的故障排除部分
2. 查看 GitHub Issues
3. 聯繫開發團隊

## 更新日誌

### v1.0.0
- 初始版本
- 基本日曆功能
- Supabase 整合
- Google Calendar 整合
- 個人事件管理 