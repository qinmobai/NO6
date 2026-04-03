package com.cl.task;

import com.cl.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 通知重试定时任务
 * 定时检查并重试发送失败的通知
 */
@Component
public class NotificationRetryTask {

    @Autowired
    private NotificationService notificationService;

    /**
     * 每5分钟执行一次重试任务
     * 检查发送失败的通知并进行重试
     */
    @Scheduled(fixedRate = 5 * 60 * 1000) // 5分钟
    public void retryFailedNotifications() {
        try {
            System.out.println("开始执行通知重试任务...");
            Map<String, Object> result = notificationService.retryFailedNotifications(3);
            System.out.println("通知重试任务完成: 总重试数=" + result.get("totalRetryCount") +
                    ", 成功=" + result.get("successCount") +
                    ", 失败=" + result.get("failCount"));
        } catch (Exception e) {
            System.err.println("通知重试任务执行异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 每小时执行一次清理任务
     * 清理超过7天且已处理的通知记录
     */
    @Scheduled(cron = "0 0 * * * ?") // 每小时执行一次
    public void cleanupOldNotifications() {
        try {
            System.out.println("开始执行通知记录清理任务...");
            // 这里可以添加清理逻辑
            // 例如删除超过7天且状态为已处理或已忽略的记录
        } catch (Exception e) {
            System.err.println("通知记录清理任务执行异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
