-- 初始化测试用户SQL脚本
-- 执行前确保已经执行过init_security_tables.sql，且存在users表
-- 注意：密码已使用BCrypt哈希存储，原始密码均为123456

-- 确保密码列足够存放BCrypt哈希值（60字符）
ALTER TABLE users MODIFY password VARCHAR(255) NOT NULL;

-- 插入5个角色对应的测试用户（适配你的users表结构，只保留必选字段）
-- 以下密码字段均为BCrypt($2a$10)哈希值，原始密码: 123456
INSERT INTO users (username, password) VALUES 
('admin', '$2a$10$K/zGcbYZ786gEXepN7GWOOqNtnIJZJ14c68yolZ4TQ2mCjhbDXxqW'),
('doctor', '$2a$10$K/zGcbYZ786gEXepN7GWOOqNtnIJZJ14c68yolZ4TQ2mCjhbDXxqW'),
('nurse', '$2a$10$K/zGcbYZ786gEXepN7GWOOqNtnIJZJ14c68yolZ4TQ2mCjhbDXxqW'),
('pharmacy', '$2a$10$K/zGcbYZ786gEXepN7GWOOqNtnIJZJ14c68yolZ4TQ2mCjhbDXxqW'),
('cashier', '$2a$10$K/zGcbYZ786gEXepN7GWOOqNtnIJZJ14c68yolZ4TQ2mCjhbDXxqW')
ON DUPLICATE KEY UPDATE password = VALUES(password);

-- 给测试用户分配对应的角色
INSERT INTO sys_user_role (username, role_id) VALUES 
('admin', 1), -- 超级管理员
('doctor', 2), -- 医生
('nurse', 3), -- 护士
('pharmacy', 4), -- 药房人员
('cashier', 5) -- 收费员
ON DUPLICATE KEY UPDATE role_id = VALUES(role_id);
