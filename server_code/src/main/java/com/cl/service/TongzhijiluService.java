package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.TongzhijiluEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.TongzhijiluView;


/**
 * 通知发送记录
 *
 * @author 
 * @email 
 * @date 2025-04-03
 */
public interface TongzhijiluService extends IService<TongzhijiluEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<TongzhijiluView> selectListView(Wrapper<TongzhijiluEntity> wrapper);
   	
   	TongzhijiluView selectView(@Param("ew") Wrapper<TongzhijiluEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<TongzhijiluEntity> wrapper);
   	
   	/**
	 * 查询需要重试的通知记录
	 */
	List<TongzhijiluEntity> selectRetryList(Integer maxRetryCount);
	
	/**
	 * 查询发送失败且未处理的记录
	 */
	List<TongzhijiluView> selectFailedList(Wrapper<TongzhijiluEntity> wrapper);
   	
   	/**
	 * 处理通知记录
	 */
	boolean handleNotification(Long id, String chulizhuangtai, String chulibeizhu, String chuliren);
   	
   	/**
	 * 重试发送失败的通知
	 */
	boolean retryNotification(Long id);
}
