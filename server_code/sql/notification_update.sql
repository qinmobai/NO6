-- 就诊通知功能升级SQL
-- 1. 创建通知发送记录表
-- 用于记录每次通知发送的状态、重试次数等信息

DROP TABLE IF EXISTS `tongzhijilu`;
CREATE TABLE `tongzhijilu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yuyueid` bigint(20) DEFAULT NULL COMMENT '预约ID',
  `yuyuebianhao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '预约编号',
  `zhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `shouji` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户手机',
  `yishengzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '医生账号',
  `dianhua` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '医生电话',
  `tongzhileixing` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通知类型(预约成功/就诊提醒/就诊前1天提醒等)',
  `tongzhineirong` text COLLATE utf8mb4_unicode_ci COMMENT '通知内容',
  `fasongshijian` datetime DEFAULT NULL COMMENT '发送时间',
  `jieshouzhuangtai` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '接收状态(0:待发送,1:发送成功,2:发送失败,3:已接收)',
  `shibaiyuanyin` text COLLATE utf8mb4_unicode_ci COMMENT '失败原因',
  `zhongshicishu` int(11) DEFAULT '0' COMMENT '重试次数',
  `zuihouzhongshishijian` datetime DEFAULT NULL COMMENT '最后重试时间',
  `chulizhuangtai` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '处理状态(0:未处理,1:已处理,2:已忽略)',
  `chulibeizhu` text COLLATE utf8mb4_unicode_ci COMMENT '处理备注',
  `chuliren` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '处理人',
  `chulishijian` datetime DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`id`),
  KEY `idx_yuyueid` (`yuyueid`),
  KEY `idx_zhanghao` (`zhanghao`),
  KEY `idx_jieshouzhuangtai` (`jieshouzhuangtai`),
  KEY `idx_chulizhuangtai` (`chulizhuangtai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知发送记录表';

-- 2. 修改jiuzhentongzhi表，添加状态字段
ALTER TABLE `jiuzhentongzhi` 
ADD COLUMN IF NOT EXISTS `tongzhizhuangtai` varchar(50) DEFAULT '0' COMMENT '通知状态(0:待通知,1:已发送,2:已接收)' AFTER `tongzhibeizhu`,
ADD COLUMN IF NOT EXISTS `jieshoushijian` datetime DEFAULT NULL COMMENT '用户接收时间' AFTER `tongzhizhuangtai`,
ADD COLUMN IF NOT EXISTS `yuyueid` bigint(20) DEFAULT NULL COMMENT '关联预约ID' AFTER `jieshoushijian`;

-- 3. 修改yishengyuyue表，添加通知相关字段
ALTER TABLE `yishengyuyue` 
ADD COLUMN IF NOT EXISTS `tongzhifasongzhuangtai` varchar(50) DEFAULT '0' COMMENT '通知发送状态(0:未发送,1:已发送,2:部分失败,3:全部失败)' AFTER `shhf`,
ADD COLUMN IF NOT EXISTS `zuihoutongzhishijian` datetime DEFAULT NULL COMMENT '最后通知发送时间' AFTER `tongzhifasongzhuangtai`;

-- 4. 添加菜单配置
-- 注意：这会更新菜单表，添加通知发送记录管理菜单
-- 请先备份原有菜单数据，或者通过管理后台手动添加菜单

-- 方式一：直接更新菜单JSON（会覆盖原有菜单，请谨慎使用）
-- UPDATE `menu` SET `menujson` = REPLACE(`menujson`, 
-- '"menu":"就诊通知管理"', 
-- '"menu":"就诊通知管理"},{"child":[{"allButtons":["查看","修改","删除","处理","重试"],"appFrontIcon":"cuIcon-notice","buttons":["查看","修改","删除","处理","重试"],"classname":"tongzhijilu","menu":"通知发送记录","menuJump":"列表","tableName":"tongzhijilu"}],"fontClass":"icon-notice","menu":"通知记录管理","unicode":"&#xef9c;"},{"child":[{"allButtons":["新增","查看","修改","删除","通知"],"appFrontIcon":"cuIcon-notice","buttons":["新增","查看","修改","删除","通知"],"classname":"jiuzhentongzhi","menu":"就诊通知","menuJump":"列表","tableName":"jiuzhentongzhi"}],"fontClass":"icon-common28","menu":"就诊通知管理"');

-- 方式二：更新现有管理员菜单，添加通知记录管理（推荐）
-- 将通知记录管理添加到就诊通知管理后面
-- 注意：根据实际数据库中的unicode值进行替换
UPDATE `menu` SET `menujson` = REPLACE(`menujson`, 
'"menu":"就诊通知管理","unicode":"&#xeeba;"}',
'"menu":"就诊通知管理","unicode":"&#xeeba;"},{"child":[{"allButtons":["查看","修改","删除","处理","重试"],"appFrontIcon":"cuIcon-notice","buttons":["查看","修改","删除","处理","重试"],"classname":"tongzhijilu","menu":"通知发送记录","menuJump":"列表","tableName":"tongzhijilu","parentMenu":"通知记录管理"}],"fontClass":"icon-notice","menu":"通知记录管理","unicode":"&#xef9c;"}')
WHERE `id` = 1;

-- 方式三：通过管理后台界面手动添加（最简单）
-- 1. 登录管理后台
-- 2. 进入"系统管理" -> "菜单管理"
-- 3. 点击"新增"按钮
-- 4. 填写以下信息：
--    - 菜单名称：通知记录管理
--    - 图标：cuIcon-notice
--    - 子菜单：
--      * 菜单名称：通知发送记录
--      * 表名：tongzhijilu
--      * 按钮权限：查看、修改、删除、处理、重试
-- 5. 保存

-- 5. 添加测试数据 - 需要重试的通知记录
-- 先确保有对应的预约记录，这里使用 yishengyuyue 表中已有的 id
INSERT INTO `tongzhijilu` (`yuyueid`, `yuyuebianhao`, `zhanghao`, `shouji`, `yishengzhanghao`, `dianhua`, `tongzhileixing`, `tongzhineirong`, `fasongshijian`, `jieshouzhuangtai`, `shibaiyuanyin`, `zhongshicishu`, `chulizhuangtai`) VALUES
(1, 'YY202504030001', 'zhanghao1', '13800138001', 'yisheng001', '13900139001', '预约成功通知', '您的预约已成功，就诊时间：2025-04-05 09:00，医生：张医生', NOW() - INTERVAL 2 HOUR, '2', '用户手机信号不稳定，短信发送失败', 2, '0'),
(2, 'YY202504030002', 'zhanghao2', '13800138002', 'yisheng002', '13900139002', '就诊前1天提醒', '提醒您：明天上午09:00有预约就诊，请准时到达', NOW() - INTERVAL 3 HOUR, '2', '运营商网络故障，短信网关超时', 1, '0'),
(3, 'YY202504030003', 'zhanghao3', '13800138003', 'yisheng003', '13900139003', '就诊前1小时提醒', '您有一个就诊提醒：1小时后（10:00）请前往诊室就诊', NOW() - INTERVAL 1 HOUR, '2', '用户手机关机，无法接收短信', 3, '0'),
(4, 'YY202504030004', 'zhanghao4', '13800138004', 'yisheng004', '13900139004', '预约成功通知', '您的预约已成功，就诊时间：2025-04-06 14:00，医生：李医生', NOW() - INTERVAL 4 HOUR, '2', '短信平台API调用失败，HTTP 500错误', 0, '0'),
(5, 'YY202504030005', 'zhanghao5', '13800138005', 'yisheng005', '13900139005', '就诊时间通知', '您的就诊时间已到，请前往2号诊室就诊', NOW() - INTERVAL 30 MINUTE, '2', '用户号码已停机，无法发送短信', 1, '0');

-- 添加一些发送成功的记录作为对比
INSERT INTO `tongzhijilu` (`yuyueid`, `yuyuebianhao`, `zhanghao`, `shouji`, `yishengzhanghao`, `dianhua`, `tongzhileixing`, `tongzhineirong`, `fasongshijian`, `jieshouzhuangtai`, `zhongshicishu`, `chulizhuangtai`) VALUES
(6, 'YY202504030006', 'zhanghao6', '13800138006', 'yisheng006', '13900139006', '预约成功通知', '您的预约已成功，就诊时间：2025-04-07 10:00，医生：王医生', NOW() - INTERVAL 1 HOUR, '1', 0, '0'),
(7, 'YY202504030007', 'zhanghao7', '13800138007', 'yisheng007', '13900139007', '就诊前1天提醒', '提醒您：明天上午10:00有预约就诊，请准时到达', NOW() - INTERVAL 30 MINUTE, '1', 0, '0');
