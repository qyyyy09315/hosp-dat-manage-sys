-- 初始化业务表和模拟数据
-- 执行前请切换到oh数据库

-- ======================================
-- 1. 创建业务表
-- ======================================
-- 病人表
CREATE TABLE IF NOT EXISTS patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '病人ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) NOT NULL COMMENT '性别',
    birth_date DATE NOT NULL COMMENT '出生日期',
    contact_info VARCHAR(20) COMMENT '联系方式',
    admission_status VARCHAR(20) DEFAULT '未入院' COMMENT '住院状态：入院/出院/未入院',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '病人信息表';

-- 医生表
CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '医生ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    specialization VARCHAR(100) NOT NULL COMMENT '专业科室',
    schedule VARCHAR(200) COMMENT '排班信息',
    performance_score DECIMAL(5,2) DEFAULT 0 COMMENT '绩效得分',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '医生信息表';

-- 药品表
CREATE TABLE IF NOT EXISTS medicines (
    medicine_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '药品ID',
    name VARCHAR(100) NOT NULL COMMENT '药品名称',
    description VARCHAR(500) COMMENT '药品描述',
    stock INT DEFAULT 0 COMMENT '库存数量',
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '药品信息表';

-- 账单表
CREATE TABLE IF NOT EXISTS bills (
    bill_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '账单ID',
    patient_id INT NOT NULL COMMENT '关联病人ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    status VARCHAR(20) DEFAULT '未支付' COMMENT '状态：未支付/已支付/已取消',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '账单表';

-- ======================================
-- 2. 插入模拟数据
-- ======================================
-- 插入病人模拟数据（20条）
INSERT INTO patients (name, gender, birth_date, contact_info, admission_status) VALUES 
('张三', '男', '1980-03-15', '13800000001', '入院'),
('李四', '女', '1975-07-22', '13800000002', '入院'),
('王五', '男', '1990-11-08', '13800000003', '出院'),
('赵六', '女', '2000-01-30', '13800000004', '未入院'),
('孙七', '男', '1965-05-12', '13800000005', '入院'),
('周八', '女', '1988-09-25', '13800000006', '出院'),
('吴九', '男', '1995-02-18', '13800000007', '未入院'),
('郑十', '女', '1972-12-05', '13800000008', '入院'),
('冯十一', '男', '1983-04-30', '13800000009', '出院'),
('陈十二', '女', '1998-06-17', '13800000010', '未入院'),
('褚十三', '男', '1960-08-23', '13800000011', '入院'),
('卫十四', '女', '1978-10-11', '13800000012', '出院'),
('蒋十五', '男', '1985-01-09', '13800000013', '未入院'),
('沈十六', '女', '1992-03-27', '13800000014', '入院'),
('韩十七', '男', '1970-07-19', '13800000015', '出院'),
('杨十八', '女', '1989-09-03', '13800000016', '未入院'),
('朱十九', '男', '1993-12-14', '13800000017', '入院'),
('秦二十', '女', '1968-04-21', '13800000018', '出院'),
('尤二一', '男', '1981-06-07', '13800000019', '未入院'),
('许二二', '女', '1997-08-29', '13800000020', '入院');

-- 插入医生模拟数据（10条）
INSERT INTO doctors (name, specialization, schedule, performance_score) VALUES 
('王建国', '内科', '周一、周三、周五上午', 95.5),
('李丽', '外科', '周二、周四、周六上午', 92.3),
('张伟', '儿科', '周一、周三、周五下午', 88.7),
('刘芳', '妇产科', '周二、周四、周日全天', 96.8),
('陈军', '骨科', '周一、周四、周六下午', 91.2),
('赵静', '眼科', '周三、周五、周日上午', 87.9),
('杨明', '耳鼻喉科', '周二、周五、周六全天', 94.6),
('黄燕', '口腔科', '周一、周三、周四全天', 90.5),
('吴强', '皮肤科', '周二、周四、周六下午', 86.4),
('周敏', '中医科', '周一、周五、周日下午', 97.1);

-- 插入药品模拟数据（20条）
INSERT INTO medicines (name, description, stock) VALUES 
('阿莫西林胶囊', '抗生素类药物，用于敏感菌所致的感染', 200),
('头孢克肟片', '第三代头孢菌素，适用于敏感菌所致的呼吸、泌尿等感染', 150),
('布洛芬缓释胶囊', '解热镇痛类非处方药，用于缓解轻至中度疼痛', 300),
('连花清瘟胶囊', '中成药，用于治疗流行性感冒属热毒袭肺证', 250),
('板蓝根颗粒', '清热解毒，凉血利咽，用于肺胃热盛所致的咽喉肿痛', 400),
('藿香正气水', '解表化湿，理气和中，用于外感风寒、内伤湿滞', 350),
('999感冒灵颗粒', '解热镇痛，用于感冒引起的头痛、发热、鼻塞等', 280),
('云南白药气雾剂', '活血散瘀，消肿止痛，用于跌打损伤、瘀血肿痛', 120),
('健胃消食片', '健胃消食，用于脾胃虚弱所致的食积', 320),
('复方丹参片', '活血化瘀，理气止痛，用于气滞血瘀所致的胸痹', 180),
('硝苯地平缓释片', '钙通道阻滞剂，用于高血压、冠心病的治疗', 160),
('二甲双胍片', '双胍类降血糖药，用于2型糖尿病的治疗', 190),
('阿司匹林肠溶片', '抗血小板聚集药物，用于预防心肌梗死复发', 220),
('氯雷他定片', '抗组胺药，用于缓解过敏性鼻炎有关的症状', 170),
('蒙脱石散', '止泻药，用于成人及儿童急、慢性腹泻', 240),
('六味地黄丸', '滋阴补肾，用于肾阴亏损、头晕耳鸣、腰膝酸软', 210),
('维生素C片', '维生素类药，用于预防和治疗维生素C缺乏症', 380),
('碘伏消毒液', '外用消毒药，用于皮肤、黏膜的消毒', 360),
('医用口罩', '一次性使用医用口罩，用于日常防护', 1000),
('生理盐水', '0.9%氯化钠注射液，用于补液、冲洗伤口等', 150);

-- 插入账单模拟数据（15条）
INSERT INTO bills (patient_id, total_amount, status) VALUES 
(1, 356.80, '已支付'),
(2, 1258.50, '已支付'),
(3, 689.20, '已支付'),
(4, 235.00, '未支付'),
(5, 1890.00, '已支付'),
(6, 456.90, '未支付'),
(7, 128.60, '已支付'),
(8, 2560.80, '已支付'),
(9, 789.30, '已支付'),
(10, 326.50, '未支付'),
(11, 1680.00, '已支付'),
(12, 569.80, '已支付'),
(13, 215.60, '未支付'),
(14, 3280.90, '已支付'),
(15, 458.70, '未支付');

-- ======================================
-- 3. 初始化权限（给超级管理员增加数据库概览权限）
-- ======================================
INSERT INTO sys_permission (perm_code, perm_name, perm_desc) VALUES 
('database:overview', '数据库概览', '查看数据库统计信息和数据概览');
INSERT INTO sys_role_permission (role_id, perm_id) 
SELECT 1, perm_id FROM sys_permission WHERE perm_code = 'database:overview';
