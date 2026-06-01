-- 安全模块初始化SQL脚本
-- 执行前请先切换到你的医院管理系统数据库

-- ======================================
-- 1. RBAC角色权限体系表
-- ======================================

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    role_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称',
    role_desc VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '系统角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    perm_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
    perm_code VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    perm_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    perm_desc VARCHAR(200) COMMENT '权限描述',
    menu_path VARCHAR(200) COMMENT '关联菜单路径',
    status TINYINT DEFAULT 1 COMMENT '状态 1-启用 0-禁用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '系统权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    role_id INT NOT NULL COMMENT '角色ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (username, role_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(role_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    role_id INT NOT NULL COMMENT '角色ID',
    perm_id INT NOT NULL COMMENT '权限ID',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_perm (role_id, perm_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(role_id) ON DELETE CASCADE,
    FOREIGN KEY (perm_id) REFERENCES sys_permission(perm_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '角色权限关联表';

-- ======================================
-- 2. 安全审计日志表
-- ======================================
CREATE TABLE IF NOT EXISTS sys_audit_log (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    username VARCHAR(50) COMMENT '操作人用户名',
    operate_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    operate_ip VARCHAR(50) COMMENT '操作IP/机器名',
    operate_type VARCHAR(20) COMMENT '操作类型：LOGIN/QUERY/ADD/UPDATE/DELETE/EXPORT',
    operate_content VARCHAR(500) COMMENT '操作内容描述',
    operate_status TINYINT COMMENT '操作结果：1-成功 0-失败',
    error_msg VARCHAR(500) COMMENT '失败原因',
    INDEX idx_username (username),
    INDEX idx_operate_time (operate_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '系统操作审计日志表';

-- ======================================
-- 3. 初始化预设数据
-- ======================================

-- 初始化角色
INSERT INTO sys_role (role_name, role_desc) VALUES 
('超级管理员', '系统最高权限，可操作所有功能'),
('医生', '仅可操作与医生相关功能：查看病人、写诊断、排班查询'),
('护士', '仅可操作护士相关功能：病人信息录入、床位安排、入出院管理'),
('药房人员', '仅可操作药品相关功能：药品库存管理、开药记录'),
('收费员', '仅可操作收费相关功能：收费管理、医保结算');

-- 初始化权限
INSERT INTO sys_permission (perm_code, perm_name, perm_desc) VALUES 
('user:manage', '用户管理', '用户注册、账号管理'),
('patient:query', '病人信息查询', '查询病人信息'),
('patient:add', '病人信息录入', '新增病人信息'),
('patient:update', '病人信息修改', '修改病人信息'),
('patient:admission', '入出院管理', '病人入出院操作'),
('doctor:schedule', '医生排班管理', '维护医生排班'),
('doctor:info', '医生信息管理', '维护医生基础信息'),
('doctor:performance', '医生绩效统计', '查看医生绩效'),
('medicine:manage', '药品库存管理', '维护药品库存信息'),
('audit:view', '审计日志查看', '查看系统操作审计日志');

-- 给超级管理员分配所有权限
INSERT INTO sys_role_permission (role_id, perm_id) 
SELECT 1, perm_id FROM sys_permission;

-- 给医生分配权限
INSERT INTO sys_role_permission (role_id, perm_id) VALUES 
(2, 3), (2, 2), (2, 7);

-- 给护士分配权限
INSERT INTO sys_role_permission (role_id, perm_id) VALUES 
(3, 2), (3, 3), (3, 4), (3, 5);

-- 给药房人员分配权限
INSERT INTO sys_role_permission (role_id, perm_id) VALUES 
(4, 9);

-- 给收费员分配权限（目前功能未开发，后续扩展）
INSERT INTO sys_role_permission (role_id, perm_id) VALUES 
(5, 2);

-- 给默认admin用户分配超级管理员角色（如果你的users表已有admin账号的话执行）
-- INSERT INTO sys_user_role (username, role_id) VALUES ('admin', 1);
