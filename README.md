
# 医院管理系统 - 就诊通知功能完整实现说明

## 项目概述
本次修改完善了医院管理系统的就诊通知功能，解决了原有的通知触发逻辑错误，并添加了完整的通知发送失败记录、重试机制和管理员后台管理功能。

## 需求分析
### 原始问题
1. 通知触发逻辑错误：通知分时段发送，而非预约成功后立即发送
2. 缺少通知发送状态跟踪
3. 缺少失败通知的重试机制
4. 缺少管理员后台的通知记录查看和处理功能

### 解决方案
1. 预约成功后立即创建并发送所有通知
2. 记录通知发送状态和失败原因
3. 实现自动和手动重试机制
4. 提供完整的管理员后台管理界面

---

## 技术实现

### 一、数据库层修改

#### 1.1 数据库表结构更新
**文件位置**: `server_code/sql/update_jiuzhentongzhi.sql`

新增字段说明：
| 字段名 | 类型 | 说明 | 备注 |
|--------|------|------|------|
| yuyue_id | BIGINT | 关联预约ID | 关联到医生预约表 |
| tongzhi_type | VARCHAR(50) | 通知类型 | 预约确认、就诊前提醒、就诊当天提醒 |
| send_status | INT | 发送状态 | 0-待发送，1-发送成功，2-发送失败 |
| retry_count | INT | 重试次数 | 默认0 |
| max_retry | INT | 最大重试次数 | 默认3 |
| last_send_time | DATETIME | 最后发送时间 | 记录每次发送的时间 |
| fail_reason | TEXT | 失败原因 | 记录发送失败的具体原因 |

#### 1.2 索引优化
为提高查询性能，添加了以下索引：
- `idx_yuyue_id`: 关联预约ID索引
- `idx_send_status`: 发送状态索引
- `idx_last_send_time`: 最后发送时间索引

---

### 二、后端实现

#### 2.1 实体类修改
**文件位置**: `server_code/src/main/java/com/cl/entity/JiuzhentongzhiEntity.java`

新增属性：
- yuyueId (Long): 关联预约ID
- tongzhiType (String): 通知类型
- sendStatus (Integer): 发送状态
- retryCount (Integer): 重试次数
- maxRetry (Integer): 最大重试次数
- lastSendTime (Date): 最后发送时间
- failReason (String): 失败原因

#### 2.2 服务层实现

**JiuzhentongzhiService.java** - 服务接口
新增方法：
```java
// 从预约创建通知
void createNotificationsFromAppointment(YishengyuyueEntity appointment);

// 发送单个通知
boolean sendNotification(JiuzhentongzhiEntity notification);

// 重试失败的通知
void retryFailedNotifications();
```

**JiuzhentongzhiServiceImpl.java** - 服务实现

核心功能：
1. **创建通知**: 预约成功后创建3条通知
   - 预约确认通知
   - 就诊前1天提醒
   - 就诊当天提醒

2. **发送通知**: 模拟短信/邮件发送
   - 记录发送状态
   - 更新最后发送时间
   - 记录失败原因

3. **重试机制**: 自动重试失败通知
   - 每次重试增加重试计数
   - 达到最大重试次数后不再重试

#### 2.3 控制器层

**YishengyuyueController.java** - 预约控制器
修改内容：
- 在 `save` 方法中，预约保存成功后调用 `jiuzhentongzhiService.createNotificationsFromAppointment()`
- 立即创建并发送所有通知

**JiuzhentongzhiController.java** - 通知控制器
新增接口：

| 接口路径 | 方法 | 功能 |
|---------|------|------|
| `/jiuzhentongzhi/retry/{id}` | POST | 手动重试单个通知 |
| `/jiuzhentongzhi/retryBatch` | POST | 批量重试通知 |
| `/jiuzhentongzhi/statistics` | GET | 获取通知统计数据 |

#### 2.4 定时任务

**NotificationRetryTask.java** - 定时任务类
- 使用 `@Scheduled(cron = "0 */5 * * * ?")` 注解
- 每5分钟自动执行一次
- 自动查找发送失败且未达到最大重试次数的通知
- 自动重试发送

**SpringbootSchemaApplication.java** - 主启动类
- 添加 `@EnableScheduling` 注解启用定时任务

---

### 三、前端管理端实现

#### 3.1 通知列表页面
**文件位置**: `manage_code/src/views/jiuzhentongzhi/list.vue`

功能特性：

1. **搜索筛选**
   - 按医生账号搜索
   - 按用户账号搜索
   - 按发送状态筛选（全部/待发送/发送成功/发送失败）

2. **数据展示**
   - 通知编号
   - 通知类型
   - 发送状态（彩色标签显示）
     - 蓝色标签：待发送
     - 绿色标签：发送成功
     - 红色标签：发送失败
   - 医生账号
   - 用户账号
   - 就诊时间
   - 通知时间
   - 重试次数（格式：当前/最大）
   - 最后发送时间
   - 失败原因（鼠标悬停显示完整内容）

3. **操作按钮**
   - 查看：查看通知详情
   - 修改：编辑通知信息
   - 重试：仅对发送失败的通知显示
   - 删除：删除通知记录
   - 批量重试：可选择多条失败通知进行批量重试
   - 查看统计：显示通知发送统计

4. **统计功能**
   - 总通知数
   - 发送成功数
   - 发送失败数
   - 待发送数

#### 3.2 通知表单页面
**文件位置**: `manage_code/src/views/jiuzhentongzhi/formModel.vue`

新增字段展示：
- 通知类型（只读）
- 发送状态（标签显示）
- 重试次数（只读）
- 最大重试次数（只读）
- 最后发送时间（只读）
- 失败原因（只读，文本域显示）

#### 3.3 页面访问地址

**管理端地址**:
- 主页面: http://localhost:8081
- 就诊通知列表: http://localhost:8081/#/jiuzhentongzhi

**路由配置**: `manage_code/src/router/index.js` 第77-81行

---

## 使用指南

### 一、部署步骤

#### 1.1 数据库升级
执行SQL脚本：
```bash
# 连接到MySQL数据库
mysql -u root -p

# 执行升级脚本
source d:/worke/NO6/server_code/sql/update_jiuzhentongzhi.sql
```

#### 1.2 启动后端服务
```bash
cd d:/worke/NO6/server_code
mvn spring-boot:run
```

后端服务地址: http://localhost:8080

#### 1.3 启动前端服务

**用户端**:
```bash
cd d:/worke/NO6/client_code
npm install
npm run serve
```
访问地址: http://localhost:8082

**管理端**:
```bash
cd d:/worke/NO6/manage_code
npm install
npm run serve
```
访问地址: http://localhost:8081

### 二、功能使用流程

#### 2.1 用户预约流程
1. 用户登录用户端（http://localhost:8082）
2. 选择医生进行预约
3. 预约成功后，系统立即：
   - 创建3条通知记录
   - 尝试发送所有通知
   - 记录发送状态

#### 2.2 管理员查看通知
1. 登录管理端（http://localhost:8081）
2. 在菜单中找到"就诊通知"（或直接访问 http://localhost:8081/#/jiuzhentongzhi）
3. 查看所有通知列表
4. 使用筛选器按状态筛选

#### 2.3 重试失败通知
**单个重试**:
1. 在通知列表中找到发送失败的通知
2. 点击"重试"按钮
3. 确认后系统重新发送该通知

**批量重试**:
1. 勾选多条发送失败的通知
2. 点击"批量重试"按钮
3. 确认后系统批量重试

#### 2.4 查看统计
1. 点击"查看统计"按钮
2. 查看通知发送统计信息：
   - 总通知数
   - 发送成功数
   - 发送失败数
   - 待发送数

---

## 系统架构

### 通知状态流转图

```
待发送 (0)
    ↓
发送中...
    ↓
┌───────────┬───────────┐
↓           ↓           ↓
成功(1)   失败(2)   超过重试
```

### 重试机制说明

**自动重试**:
- 触发时间：每5分钟（可配置）
- 重试条件：sendStatus=2 且 retryCount &lt; maxRetry
- 最大重试次数：3次（可配置）

**手动重试**:
- 管理员可随时手动重试
- 不受最大重试次数限制
- 手动重试会重置重试计数

---

## 配置说明

### 定时任务配置
**文件**: `server_code/src/main/java/com/cl/task/NotificationRetryTask.java`

修改定时任务频率：
```java
// 每5分钟执行一次
@Scheduled(cron = "0 */5 * * * ?")

// 修改为每10分钟执行一次
@Scheduled(cron = "0 */10 * * * ?")

// 修改为每1小时执行一次
@Scheduled(cron = "0 0 * * * ?")
```

### 最大重试次数配置
**文件**: `server_code/src/main/java/com/cl/service/impl/JiuzhentongzhiServiceImpl.java`

修改最大重试次数：
```java
// 默认3次
notification.setMaxRetry(3);

// 修改为5次
notification.setMaxRetry(5);
```

### 通知发送实现
当前使用模拟实现，如需接入真实短信/邮件服务：

**文件**: `server_code/src/main/java/com/cl/service/impl/JiuzhentongzhiServiceImpl.java`

修改 `simulateSendNotification` 方法：
```java
private boolean simulateSendNotification(JiuzhentongzhiEntity notification) {
    // 替换为真实的短信发送代码
    // 例如：调用阿里云短信、腾讯云短信等API
    // 例如：调用邮件发送服务
    
    return true; // 发送成功返回true，失败返回false
}
```

---

## 文件清单

### 新增文件
| 文件路径 | 说明 |
|---------|------|
| `server_code/sql/update_jiuzhentongzhi.sql` | 数据库升级脚本 |
| `server_code/src/main/java/com/cl/task/NotificationRetryTask.java` | 定时任务类 |

### 修改文件
| 文件路径 | 修改内容 |
|---------|---------|
| `server_code/src/main/java/com/cl/entity/JiuzhentongzhiEntity.java` | 添加新字段 |
| `server_code/src/main/java/com/cl/service/JiuzhentongzhiService.java` | 添加新方法接口 |
| `server_code/src/main/java/com/cl/service/impl/JiuzhentongzhiServiceImpl.java` | 实现通知发送和重试逻辑 |
| `server_code/src/main/java/com/cl/controller/YishengyuyueController.java` | 预约成功后创建通知 |
| `server_code/src/main/java/com/cl/controller/JiuzhentongzhiController.java` | 添加管理接口 |
| `server_code/src/main/java/com/cl/SpringbootSchemaApplication.java` | 启用定时任务 |
| `manage_code/src/views/jiuzhentongzhi/list.vue` | 更新通知列表页面 |
| `manage_code/src/views/jiuzhentongzhi/formModel.vue` | 更新通知表单页面 |

### 文档文件
| 文件路径 | 说明 |
|---------|------|
| `server_code/通知功能修改说明.md` | 本文档 |

---

## 注意事项

### 开发注意事项
1. **数据库备份**: 执行升级脚本前请先备份数据库
2. **兼容性**: 新增字段允许为空，不影响现有数据
3. **测试**: 建议先在测试环境验证后再部署到生产环境

### 生产环境建议
1. **短信服务**: 接入真实的短信发送服务，替换模拟实现
2. **监控**: 添加通知发送失败的监控告警
3. **日志**: 完善通知发送的日志记录
4. **性能**: 通知量大时考虑使用消息队列异步发送

### 安全建议
1. **权限控制**: 确保只有管理员可以访问通知管理功能
2. **数据脱敏**: 手机号等敏感信息在展示时进行脱敏处理
3. **审计日志**: 记录管理员对通知的操作

---

## 常见问题

### Q: 如何查看通知是否发送成功？
A: 登录管理端，进入"就诊通知"页面，查看"发送状态"列。

### Q: 定时任务不执行怎么办？
A: 检查 `SpringbootSchemaApplication.java` 是否有 `@EnableScheduling` 注解。

### Q: 如何修改定时任务执行频率？
A: 修改 `NotificationRetryTask.java` 中的 cron 表达式。

### Q: 通知发送失败后会自动重试吗？
A: 会的，系统每5分钟自动重试失败的通知，最多重试3次。

### Q: 菜单中看不到"就诊通知"选项怎么办？
A: 可以直接访问 http://localhost:8081/#/jiuzhentongzhi，或在后台菜单管理中添加菜单项。

---

## 总结

本次修改完整实现了：
✅ 预约成功后立即发送所有通知
✅ 通知发送状态跟踪
✅ 失败通知自动重试机制
✅ 管理员后台通知记录查看
✅ 单个和批量手动重试功能
✅ 通知发送统计功能
✅ 完整的前后端实现

所有功能已开发完成并可以正常使用！
