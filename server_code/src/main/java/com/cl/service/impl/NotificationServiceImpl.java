package com.cl.service.impl;

import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.entity.TongzhijiluEntity;
import com.cl.entity.YishengyuyueEntity;
import com.cl.service.JiuzhentongzhiService;
import com.cl.service.NotificationService;
import com.cl.service.TongzhijiluService;
import com.cl.service.YishengyuyueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 通知服务实现类
 */
@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private TongzhijiluService tongzhijiluService;

    @Autowired
    private JiuzhentongzhiService jiuzhentongzhiService;

    @Autowired
    private YishengyuyueService yishengyuyueService;

    // 通知类型常量
    public static final String TYPE_APPOINTMENT_SUCCESS = "预约成功通知";
    public static final String TYPE_ONE_DAY_BEFORE = "就诊前1天提醒";
    public static final String TYPE_ONE_HOUR_BEFORE = "就诊前1小时提醒";
    public static final String TYPE_ARRIVAL_NOTICE = "就诊时间通知";

    // 接收状态常量
    public static final String STATUS_PENDING = "0";
    public static final String STATUS_SUCCESS = "1";
    public static final String STATUS_FAILED = "2";
    public static final String STATUS_RECEIVED = "3";

    // 最大重试次数
    private static final int MAX_RETRY_COUNT = 3;

    @Override
    @Transactional
    public Map<String, Object> sendAllNotifications(YishengyuyueEntity yuyue) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> details = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        Date yuyueshijian = yuyue.getYuyueshijian();
        if (yuyueshijian == null) {
            result.put("success", false);
            result.put("message", "预约时间为空，无法发送通知");
            return result;
        }

        // 定义需要发送的通知类型及其计划发送时间
        List<Map<String, Object>> notificationPlans = new ArrayList<>();

        // 1. 预约成功通知 - 立即发送
        notificationPlans.add(createPlan(TYPE_APPOINTMENT_SUCCESS, new Date()));

        // 2. 就诊前1天提醒 - 就诊前1天发送
        Calendar cal1Day = Calendar.getInstance();
        cal1Day.setTime(yuyueshijian);
        cal1Day.add(Calendar.DAY_OF_MONTH, -1);
        notificationPlans.add(createPlan(TYPE_ONE_DAY_BEFORE, cal1Day.getTime()));

        // 3. 就诊前1小时提醒 - 就诊前1小时发送
        Calendar cal1Hour = Calendar.getInstance();
        cal1Hour.setTime(yuyueshijian);
        cal1Hour.add(Calendar.HOUR_OF_DAY, -1);
        notificationPlans.add(createPlan(TYPE_ONE_HOUR_BEFORE, cal1Hour.getTime()));

        // 4. 就诊时间通知 - 就诊时间发送
        notificationPlans.add(createPlan(TYPE_ARRIVAL_NOTICE, yuyueshijian));

        // 立即创建并发送所有通知
        for (Map<String, Object> plan : notificationPlans) {
            String tongzhileixing = (String) plan.get("type");
            Date plannedTime = (Date) plan.get("time");

            // 生成通知内容
            String content = generateNotificationContent(yuyue, tongzhileixing);

            // 创建通知记录
            TongzhijiluEntity record = createNotificationRecord(yuyue, tongzhileixing, content, plannedTime);

            // 立即发送通知
            boolean sendSuccess = sendSingleNotification(record);

            Map<String, Object> detail = new HashMap<>();
            detail.put("type", tongzhileixing);
            detail.put("recordId", record.getId());
            detail.put("success", sendSuccess);

            if (sendSuccess) {
                successCount++;
                detail.put("status", "发送成功");
            } else {
                failCount++;
                detail.put("status", "发送失败");
                detail.put("reason", record.getShibaiyuanyin());
            }
            details.add(detail);
        }

        // 更新预约的通知发送状态
        YishengyuyueEntity updateYuyue = new YishengyuyueEntity();
        updateYuyue.setId(yuyue.getId());
        if (failCount == 0) {
            updateYuyue.setTongzhifasongzhuangtai("1"); // 全部成功
        } else if (successCount == 0) {
            updateYuyue.setTongzhifasongzhuangtai("3"); // 全部失败
        } else {
            updateYuyue.setTongzhifasongzhuangtai("2"); // 部分失败
        }
        updateYuyue.setZuihoutongzhishijian(new Date());
        yishengyuyueService.updateById(updateYuyue);

        result.put("success", failCount == 0);
        result.put("totalCount", notificationPlans.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("details", details);

        return result;
    }

    private Map<String, Object> createPlan(String type, Date time) {
        Map<String, Object> plan = new HashMap<>();
        plan.put("type", type);
        plan.put("time", time);
        return plan;
    }

    @Override
    public boolean sendSingleNotification(TongzhijiluEntity tongzhijilu) {
        try {
            // 模拟发送通知的逻辑
            // 实际项目中这里可以调用短信接口、推送接口等
            boolean sendResult = doSendNotification(tongzhijilu);

            if (sendResult) {
                tongzhijilu.setJieshouzhuangtai(STATUS_SUCCESS);
                tongzhijilu.setFasongshijian(new Date());
            } else {
                tongzhijilu.setJieshouzhuangtai(STATUS_FAILED);
                tongzhijilu.setShibaiyuanyin("通知发送失败，可能是网络原因或接收方不可用");
                tongzhijilu.setZhongshicishu(tongzhijilu.getZhongshicishu() + 1);
            }

            tongzhijiluService.updateById(tongzhijilu);

            // 同时创建就诊通知记录
            if (sendResult) {
                createJiuzhentongzhi(tongzhijilu);
            }

            return sendResult;
        } catch (Exception e) {
            tongzhijilu.setJieshouzhuangtai(STATUS_FAILED);
            tongzhijilu.setShibaiyuanyin("发送异常: " + e.getMessage());
            tongzhijilu.setZhongshicishu(tongzhijilu.getZhongshicishu() + 1);
            tongzhijiluService.updateById(tongzhijilu);
            return false;
        }
    }

    /**
     * 实际发送通知的方法
     * 这里模拟发送过程，实际项目中替换为真实的短信/推送接口调用
     */
    private boolean doSendNotification(TongzhijiluEntity tongzhijilu) {
        // 模拟发送成功率 90%
        // 实际项目中这里调用短信API或推送服务
        // 例如：调用阿里云短信服务、腾讯云短信服务、极光推送等

        // 这里为了演示，假设都发送成功
        // 如果需要模拟失败情况，可以添加随机逻辑
        return true;
    }

    /**
     * 创建就诊通知记录
     */
    private void createJiuzhentongzhi(TongzhijiluEntity tongzhijilu) {
        try {
            JiuzhentongzhiEntity jiuzhentongzhi = new JiuzhentongzhiEntity();
            jiuzhentongzhi.setTongzhibianhao(generateTongzhiBianhao());
            jiuzhentongzhi.setYishengzhanghao(tongzhijilu.getYishengzhanghao());
            jiuzhentongzhi.setDianhua(tongzhijilu.getDianhua());
            jiuzhentongzhi.setJiuzhenshijian(tongzhijilu.getFasongshijian());
            jiuzhentongzhi.setTongzhishijian(new Date());
            jiuzhentongzhi.setZhanghao(tongzhijilu.getZhanghao());
            jiuzhentongzhi.setShouji(tongzhijilu.getShouji());
            jiuzhentongzhi.setTongzhibeizhu(tongzhijilu.getTongzhineirong());
            jiuzhentongzhi.setYuyueid(tongzhijilu.getYuyueid());
            jiuzhentongzhi.setTongzhizhuangtai("1"); // 已发送

            jiuzhentongzhiService.insert(jiuzhentongzhi);
        } catch (Exception e) {
            // 记录日志，但不影响主流程
            System.err.println("创建就诊通知记录失败: " + e.getMessage());
        }
    }

    /**
     * 生成通知编号
     */
    private String generateTongzhiBianhao() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "TZ" + sdf.format(new Date()) + System.currentTimeMillis() % 1000;
    }

    @Override
    public TongzhijiluEntity createNotificationRecord(YishengyuyueEntity yuyue, String tongzhileixing,
                                                       String tongzhineirong, Date plannedTime) {
        TongzhijiluEntity record = new TongzhijiluEntity();
        record.setYuyueid(yuyue.getId());
        record.setYuyuebianhao(yuyue.getYuyuebianhao());
        record.setZhanghao(yuyue.getZhanghao());
        record.setShouji(yuyue.getShouji());
        record.setYishengzhanghao(yuyue.getYishengzhanghao());
        record.setDianhua(yuyue.getDianhua());
        record.setTongzhileixing(tongzhileixing);
        record.setTongzhineirong(tongzhineirong);
        record.setJieshouzhuangtai(STATUS_PENDING);
        record.setZhongshicishu(0);
        record.setChulizhuangtai("0");

        tongzhijiluService.insert(record);
        return record;
    }

    @Override
    public String generateNotificationContent(YishengyuyueEntity yuyue, String tongzhileixing) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String yuyueshijianStr = yuyue.getYuyueshijian() != null ?
                sdf.format(yuyue.getYuyueshijian()) : "待定";

        StringBuilder content = new StringBuilder();
        content.append("【医院预约系统】");

        switch (tongzhileixing) {
            case TYPE_APPOINTMENT_SUCCESS:
                content.append("尊敬的").append(yuyue.getZhanghao()).append("，您的预约已成功！");
                content.append("预约医生：").append(yuyue.getYishengzhanghao()).append("，");
                content.append("就诊时间：").append(yuyueshijianStr).append("，");
                content.append("请按时就诊。如有变动请提前联系。");
                break;
            case TYPE_ONE_DAY_BEFORE:
                content.append("尊敬的").append(yuyue.getZhanghao()).append("，提醒您明天(");
                content.append(yuyueshijianStr).append(")有预约就诊，");
                content.append("医生：").append(yuyue.getYishengzhanghao()).append("，");
                content.append("请提前做好准备，按时到达。");
                break;
            case TYPE_ONE_HOUR_BEFORE:
                content.append("尊敬的").append(yuyue.getZhanghao()).append("，您预约的就诊将在1小时后开始(");
                content.append(yuyueshijianStr).append(")，");
                content.append("医生：").append(yuyue.getYishengzhanghao()).append("，");
                content.append("请尽快前往医院。");
                break;
            case TYPE_ARRIVAL_NOTICE:
                content.append("尊敬的").append(yuyue.getZhanghao()).append("，您预约的就诊时间已到(");
                content.append(yuyueshijianStr).append(")，");
                content.append("医生：").append(yuyue.getYishengzhanghao()).append("，");
                content.append("请立即前往诊室就诊。");
                break;
            default:
                content.append("您有一条新的就诊通知，请查看。");
        }

        return content.toString();
    }

    @Override
    public Map<String, Object> retryFailedNotifications(Integer maxRetryCount) {
        Map<String, Object> result = new HashMap<>();

        if (maxRetryCount == null) {
            maxRetryCount = MAX_RETRY_COUNT;
        }

        // 查询需要重试的通知记录
        List<TongzhijiluEntity> retryList = tongzhijiluService.selectRetryList(maxRetryCount);

        int successCount = 0;
        int failCount = 0;
        List<Map<String, Object>> details = new ArrayList<>();

        for (TongzhijiluEntity record : retryList) {
            // 更新重试次数
            record.setZhongshicishu(record.getZhongshicishu() + 1);
            record.setZuihouzhongshishijian(new Date());

            boolean sendSuccess = sendSingleNotification(record);

            Map<String, Object> detail = new HashMap<>();
            detail.put("recordId", record.getId());
            detail.put("type", record.getTongzhileixing());
            detail.put("retryCount", record.getZhongshicishu());
            detail.put("success", sendSuccess);

            if (sendSuccess) {
                successCount++;
            } else {
                failCount++;
            }
            details.add(detail);
        }

        result.put("totalRetryCount", retryList.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("details", details);

        return result;
    }

    @Override
    public boolean updateReceiveStatus(Long tongzhiId, String status) {
        // 更新通知记录表的接收状态
        TongzhijiluEntity record = tongzhijiluService.selectById(tongzhiId);
        if (record == null) {
            return false;
        }
        record.setJieshouzhuangtai(status);
        return tongzhijiluService.updateById(record);
    }

    @Override
    public List<Map<String, String>> getNotificationTypes() {
        List<Map<String, String>> types = new ArrayList<>();

        Map<String, String> type1 = new HashMap<>();
        type1.put("code", "1");
        type1.put("name", TYPE_APPOINTMENT_SUCCESS);
        types.add(type1);

        Map<String, String> type2 = new HashMap<>();
        type2.put("code", "2");
        type2.put("name", TYPE_ONE_DAY_BEFORE);
        types.add(type2);

        Map<String, String> type3 = new HashMap<>();
        type3.put("code", "3");
        type3.put("name", TYPE_ONE_HOUR_BEFORE);
        types.add(type3);

        Map<String, String> type4 = new HashMap<>();
        type4.put("code", "4");
        type4.put("name", TYPE_ARRIVAL_NOTICE);
        types.add(type4);

        return types;
    }

    @Override
    public Date calculateSendTime(Date jiuzhenshijian, String tongzhileixing) {
        if (jiuzhenshijian == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(jiuzhenshijian);

        switch (tongzhileixing) {
            case TYPE_APPOINTMENT_SUCCESS:
                // 立即发送
                return new Date();
            case TYPE_ONE_DAY_BEFORE:
                // 就诊前1天
                cal.add(Calendar.DAY_OF_MONTH, -1);
                return cal.getTime();
            case TYPE_ONE_HOUR_BEFORE:
                // 就诊前1小时
                cal.add(Calendar.HOUR_OF_DAY, -1);
                return cal.getTime();
            case TYPE_ARRIVAL_NOTICE:
                // 就诊时间
                return jiuzhenshijian;
            default:
                return new Date();
        }
    }
}
