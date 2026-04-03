# 医院管理系统 - 就诊通知功能修改说明

## 一、功能概述

本次修改实现了以下功能：
1. **立即发送所有通知**：预约审核通过后立即发送所有后续提醒（而非分时段发送）
2. **通知状态跟踪**：记录通知发送状态、用户接收状态
3. **失败重试机制**：自动重试发送失败的通知，支持手动重试
4. **系统记录**：记录所有通知发送记录及失败原因
5. **后台管理**：管理员可在后台查看和处理通知记录

---

## 二、数据库变更

### 1. 新增表

#### `tongzhijilu` - 通知发送记录表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| yuyueid | bigint | 预约ID |
| yuyuebianhao | varchar | 预约编号 |
| zhanghao | varchar | 用户账号 |
| shouji | varchar | 用户手机 |
| yishengzhanghao | varchar | 医生账号 |
| dianhua | varchar | 医生电话 |
| tongzhileixing | varchar | 通知类型 |
| tongzhineirong | text | 通知内容 |
| fasongshijian | datetime | 发送时间 |
| jieshouzhuangtai | varchar | 接收状态(0:待发送,1:发送成功,2:发送失败,3:已接收) |
| shibaiyuanyin | text | 失败原因 |
| zhongshicishu | int | 重试次数 |
| chulizhuangtai | varchar | 处理状态(0:未处理,1:已处理,2:已忽略) |
| chulibeizhu | text | 处理备注 |
| chuliren | varchar | 处理人 |
| chulishijian | datetime | 处理时间 |

### 2. 修改表

#### `jiuzhentongzhi` 表新增字段
- `tongzhizhuangtai` - 通知状态
- `jieshoushijian` - 用户接收时间
- `yuyueid` - 关联预约ID

#### `yishengyuyue` 表新增字段
- `tongzhifasongzhuangtai` - 通知发送状态
- `zuihoutongzhishijian` - 最后通知发送时间

---

## 三、后端代码变更

### 1. 新增文件

| 文件路径 | 说明 |
|----------|------|
| `entity/TongzhijiluEntity.java` | 通知记录实体类 |
| `entity/view/TongzhijiluView.java` | 通知记录视图类 |
| `dao/TongzhijiluDao.java` | 通知记录DAO接口 |
| `mapper/TongzhijiluDao.xml` | 通知记录Mapper XML |
| `service/TongzhijiluService.java` | 通知记录Service接口 |
| `service/impl/TongzhijiluServiceImpl.java` | 通知记录Service实现 |
| `service/NotificationService.java` | 通知服务接口 |
| `service/impl/NotificationServiceImpl.java` | 通知服务实现 |
| `controller/TongzhijiluController.java` | 通知记录Controller |
| `task/NotificationRetryTask.java` | 通知重试定时任务 |

### 2. 修改文件

| 文件路径 | 修改内容 |
|----------|----------|
| `entity/YishengyuyueEntity.java` | 添加通知相关字段 |
| `entity/JiuzhentongzhiEntity.java` | 添加状态跟踪字段 |
| `controller/YishengyuyueController.java` | 审核通过后发送通知 |
| `SpringbootSchemaApplication.java` | 启用定时任务(@EnableScheduling) |

### 3. 权限控制

通知记录管理仅管理员可访问，通过 `tableName="users"` 进行权限判断。

---

## 四、前端代码变更

### 1. 新增文件

| 文件路径 | 说明 |
|----------|------|
| `views/tongzhijilu/list.vue` | 通知记录列表页面 |
| `views/tongzhijilu/formModel.vue` | 通知记录详情页面 |

### 2. 修改文件

| 文件路径 | 修改内容 |
|----------|----------|
| `router/index.js` | 添加通知记录路由 |

---

## 五、菜单配置

### 方式一：执行SQL自动添加（推荐）

```sql
source d:\worke\NO6\server_code\sql\notification_update.sql
```

### 方式二：手动添加菜单

1. 登录管理后台
2. 进入：系统管理 → 菜单管理
3. 在管理员菜单中添加：
   - **菜单名称**：通知记录管理
   - **图标**：`cuIcon-notice`
   - **子菜单**：
     - 菜单名称：通知发送记录
     - 表名：`tongzhijilu`
     - 按钮权限：查看、修改、删除、处理、重试
     - 图标：`cuIcon-notice`

---

## 六、使用说明

### 1. 预约审核后自动发送通知

当医生/管理员审核通过预约后，系统会自动发送以下通知：
- 预约成功通知（立即发送）
- 就诊前1天提醒
- 就诊前1小时提醒
- 就诊时间通知

### 2. 查看通知记录

管理员登录后，在左侧菜单：
```
通知记录管理 → 通知发送记录
```

### 3. 处理失败通知

1. 在通知记录列表中筛选"发送失败"的记录
2. 点击"处理"按钮，填写处理备注
3. 点击"重试"按钮重新发送
4. 或选择"批量重试"处理多条记录

### 4. 定时重试任务

系统每5分钟自动执行一次重试任务，重试发送失败的通知（最多重试3次）。

---

## 七、测试数据

SQL 中已包含7条测试数据：
- 5条发送失败记录（用于测试重试功能）
- 2条发送成功记录（用于对比）

---

## 八、注意事项

1. **MyBatis-Plus版本**：项目使用2.3版本，XML中使用 `${ew.sqlSegment}` 而非 `${ew.customSqlSegment}`
2. **权限控制**：通知记录仅管理员可见，通过token中的tableName判断
3. **菜单缓存**：添加菜单后需清除浏览器缓存或重新登录
4. **定时任务**：确保SpringBoot主类添加了@EnableScheduling注解

---

## 九、API接口

| 接口 | 说明 | 权限 |
|------|------|------|
| GET /tongzhijilu/page | 分页查询 | 管理员 |
| GET /tongzhijilu/list | 列表查询 | 管理员 |
| GET /tongzhijilu/info/{id} | 详情查询 | 管理员 |
| POST /tongzhijilu/save | 新增记录 | 管理员 |
| POST /tongzhijilu/update | 修改记录 | 管理员 |
| POST /tongzhijilu/delete | 删除记录 | 管理员 |
| GET /tongzhijilu/failedList | 失败列表 | 管理员 |
| POST /tongzhijilu/handle | 处理记录 | 管理员 |
| POST /tongzhijilu/retry/{id} | 重试发送 | 管理员 |
| POST /tongzhijilu/retryBatch | 批量重试 | 管理员 |

---

## 十、问题排查

### 菜单不显示
1. 检查SQL是否执行成功
2. 清除浏览器缓存（Ctrl+F5）
3. 重新登录系统
4. 检查数据库menu表中的JSON格式

### 通知不发送
1. 检查预约审核是否通过
2. 查看控制台日志是否有错误
3. 检查tongzhijilu表是否有记录生成

### 权限错误
1. 确认登录的是管理员账号
2. 检查token是否过期
3. 查看控制台返回的403错误信息
