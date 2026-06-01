-- 初始化测试用户SQL脚本
-- 执行前确保已经执行过init_security_tables.sql，且存在users表

-- 插入5个角色对应的测试用户（适配你的users表结构，只保留必选字段）
INSERT INTO users (username, password) VALUES 
('admin', '123456'),
('doctor', '123456'),
('nurse', '123456'),
('pharmacy', '123456'),
('cashier', '123456')
ON DUPLICATE KEY UPDATE password = VALUES(password); -- 如果用户已存在则更新密码

-- 给测试用户分配对应的角色
INSERT INTO sys_user_role (username, role_id) VALUES 
('admin', 1), -- 超级管理员
('doctor', 2), -- 医生
('nurse', 3), -- 护士
('pharmacy', 4), -- 药房人员
('cashier', 5) -- 收费员
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id); -- 已分配过角色则覆盖
