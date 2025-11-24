# 修復 Supabase RLS 政策錯誤

## 問題描述
儲存事業單位時出現 401 Unauthorized 錯誤：
```
new row violates row-level security policy for table "units"
```

## 解決方案

### 方法 1：在 Supabase Dashboard 中修復（推薦）

1. **登入 Supabase Dashboard**
   - 前往 https://supabase.com/dashboard
   - 選擇你的專案：`litnrtwcihcvpxpfufeg`

2. **進入 SQL Editor**
   - 點擊左側選單的 "SQL Editor"
   - 點擊 "New query"

3. **執行以下 SQL 語句**：

```sql
-- 刪除現有的 RLS 政策
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON units;
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON unit_addresses;
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON staff;
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON monthly_hours;
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON shifts;
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON shift_staff;

-- 建立新的 RLS 政策，允許 anon 用戶進行所有操作
CREATE POLICY "Enable all operations for anon users" ON units FOR ALL USING (true);
CREATE POLICY "Enable all operations for anon users" ON unit_addresses FOR ALL USING (true);
CREATE POLICY "Enable all operations for anon users" ON staff FOR ALL USING (true);
CREATE POLICY "Enable all operations for anon users" ON monthly_hours FOR ALL USING (true);
CREATE POLICY "Enable all operations for anon users" ON shifts FOR ALL USING (true);
CREATE POLICY "Enable all operations for anon users" ON shift_staff FOR ALL USING (true);
```

4. **點擊 "Run" 執行**

### 方法 2：使用提供的 SQL 文件

1. 使用 `fix_rls_policies.sql` 文件中的內容
2. 在 Supabase Dashboard 的 SQL Editor 中執行

## 驗證修復

執行 SQL 後，重新嘗試儲存事業單位，應該不會再出現 401 錯誤。

## 注意事項

- 這個修復允許匿名用戶進行所有操作
- 如果需要在生產環境中使用，建議設定更嚴格的 RLS 政策
- 或者考慮實作用戶認證系統 