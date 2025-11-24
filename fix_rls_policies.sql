-- 修復 RLS 政策，允許 anon 用戶進行所有操作
-- 執行這些 SQL 語句來修復 401 Unauthorized 錯誤

-- 1. 刪除現有的 RLS 政策
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON units;
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON unit_addresses;
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON staff;
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON monthly_hours;
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON shifts;
DROP POLICY IF EXISTS "Enable all operations for authenticated users" ON shift_staff;

-- 2. 建立新的 RLS 政策，允許 anon 用戶進行所有操作
CREATE POLICY "Enable all operations for anon users" ON units FOR ALL USING (true);
CREATE POLICY "Enable all operations for anon users" ON unit_addresses FOR ALL USING (true);
CREATE POLICY "Enable all operations for anon users" ON staff FOR ALL USING (true);
CREATE POLICY "Enable all operations for anon users" ON monthly_hours FOR ALL USING (true);
CREATE POLICY "Enable all operations for anon users" ON shifts FOR ALL USING (true);
CREATE POLICY "Enable all operations for anon users" ON shift_staff FOR ALL USING (true);

-- 3. 確認 RLS 已啟用
ALTER TABLE units ENABLE ROW LEVEL SECURITY;
ALTER TABLE unit_addresses ENABLE ROW LEVEL SECURITY;
ALTER TABLE staff ENABLE ROW LEVEL SECURITY;
ALTER TABLE monthly_hours ENABLE ROW LEVEL SECURITY;
ALTER TABLE shifts ENABLE ROW LEVEL SECURITY;
ALTER TABLE shift_staff ENABLE ROW LEVEL SECURITY;

-- 4. 驗證政策
SELECT schemaname, tablename, policyname, permissive, roles, cmd, qual 
FROM pg_policies 
WHERE tablename IN ('units', 'unit_addresses', 'staff', 'monthly_hours', 'shifts', 'shift_staff');

-- 完成！現在 anon 用戶應該可以進行所有操作了 