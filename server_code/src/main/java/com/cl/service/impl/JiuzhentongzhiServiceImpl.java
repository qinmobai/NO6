package com.cl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;


import com.cl.dao.JiuzhentongzhiDao;
import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.entity.YishengyuyueEntity;
import com.cl.service.JiuzhentongzhiService;
import com.cl.entity.view.JiuzhentongzhiView;

@Service("jiuzhentongzhiService")
public class JiuzhentongzhiServiceImpl extends ServiceImpl<JiuzhentongzhiDao, JiuzhentongzhiEntity> implements JiuzhentongzhiService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JiuzhentongzhiEntity> page = this.selectPage(
                new Query<JiuzhentongzhiEntity>(params).getPage(),
                new EntityWrapper<JiuzhentongzhiEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<JiuzhentongzhiEntity> wrapper) {
		  Page<JiuzhentongzhiView> page =new Query<JiuzhentongzhiView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<JiuzhentongzhiView> selectListView(Wrapper<JiuzhentongzhiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public JiuzhentongzhiView selectView(Wrapper<JiuzhentongzhiEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	@Override
	public void createNotificationsFromAppointment(YishengyuyueEntity appointment) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String[] notificationTypes = {"预约确认通知", "就诊前1天提醒", "就诊当天提醒"};
		
		for (int i = 0; i < notificationTypes.length; i++) {
			JiuzhentongzhiEntity notification = new JiuzhentongzhiEntity();
			
			notification.setTongzhibianhao(sdf.format(new Date()) + UUID.randomUUID().toString().substring(0, 8));
			notification.setYishengzhanghao(appointment.getYishengzhanghao());
			notification.setDianhua(appointment.getDianhua());
			notification.setJiuzhenshijian(appointment.getYuyueshijian());
			notification.setTongzhishijian(new Date());
			notification.setZhanghao(appointment.getZhanghao());
			notification.setShouji(appointment.getShouji());
			notification.setTongzhibeizhu(notificationTypes[i]);
			notification.setYuyueId(appointment.getId());
			notification.setTongzhiType(notificationTypes[i]);
			notification.setSendStatus(0);
			notification.setRetryCount(0);
			notification.setMaxRetry(3);
			
			this.insert(notification);
			
			sendNotification(notification);
		}
	}
	
	@Override
	public boolean sendNotification(JiuzhentongzhiEntity notification) {
		try {
			System.out.println("正在发送通知: " + notification.getTongzhibianhao() + ", 类型: " + notification.getTongzhiType());
			
			notification.setLastSendTime(new Date());
			
			boolean success = simulateSendNotification(notification);
			
			if (success) {
				notification.setSendStatus(1);
				notification.setFailReason(null);
				System.out.println("通知发送成功: " + notification.getTongzhibianhao());
			} else {
				notification.setSendStatus(2);
				notification.setRetryCount(notification.getRetryCount() + 1);
				notification.setFailReason("发送失败，请检查网络连接");
				System.out.println("通知发送失败: " + notification.getTongzhibianhao());
			}
			
			this.updateById(notification);
			return success;
		} catch (Exception e) {
			e.printStackTrace();
			notification.setSendStatus(2);
			notification.setRetryCount(notification.getRetryCount() + 1);
			notification.setLastSendTime(new Date());
			notification.setFailReason("发送异常: " + e.getMessage());
			this.updateById(notification);
			return false;
		}
	}
	
	@Override
	public void retryFailedNotifications() {
		EntityWrapper<JiuzhentongzhiEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("send_status", 2);
		wrapper.lt("retry_count", "max_retry");
		
		List<JiuzhentongzhiEntity> failedList = this.selectList(wrapper);
		
		for (JiuzhentongzhiEntity notification : failedList) {
			sendNotification(notification);
		}
	}
	
	private boolean simulateSendNotification(JiuzhentongzhiEntity notification) {
		return true;
	}
	


}
