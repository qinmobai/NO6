package com.cl.service;

import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.entity.TongzhijiluEntity;
import com.cl.entity.YishengyuyueEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通知服务接口
 * 用于处理就诊通知的发送、重试等逻辑
 */
public interface NotificationService {

    /**
     * 预约审核通过后，立即发送所有后续提醒通知
     * 包括：
     * 1. 预约成功通知
     * 2. 就诊前1天提醒
     * 3. 就诊前1小时提醒
     * 4. 就诊时间通知
     *
     * @param yuyue 预约信息
     * @return 发送结果
     */
    Map<String, Object> sendAllNotifications(YishengyuyueEntity yuyue);

    /**
     * 发送单条通知
     *
     * @param tongzhijilu 通知记录
     * @return 是否发送成功
     */
    boolean sendSingleNotification(TongzhijiluEntity tongzhijilu);

    /**
     * 创建通知记录
     *
     * @param yuyue 预约信息
     * @param tongzhileixing 通知类型
     * @param tongzhineirong 通知内容
     * @param plannedTime 计划发送时间
     * @return 通知记录
     */
    TongzhijiluEntity createNotificationRecord(YishengyuyueEntity yuyue, String tongzhileixing,
                                                String tongzhineirong, Date plannedTime);

    /**
     * 生成通知内容
     *
     * @param yuyue 预约信息
     * @param tongzhileixing 通知类型
     * @return 通知内容
     */
    String generateNotificationContent(YishengyuyueEntity yuyue, String tongzhileixing);

    /**
     * 重试发送失败的通知
     *
     * @param maxRetryCount 最大重试次数
     * @return 重试结果
     */
    Map<String, Object> retryFailedNotifications(Integer maxRetryCount);

    /**
     * 更新用户接收状态
     *
     * @param tongzhiId 通知ID
     * @param status 接收状态
     * @return 是否更新成功
     */
    boolean updateReceiveStatus(Long tongzhiId, String status);

    /**
     * 获取通知类型列表
     *
     * @return 通知类型列表
     */
    List<Map<String, String>> getNotificationTypes();

    /**
     * 计算通知发送时间
     *
     * @param jiuzhenshijian 就诊时间
     * @param tongzhileixing 通知类型
     * @return 计划发送时间
     */
    Date calculateSendTime(Date jiuzhenshijian, String tongzhileixing);
}
