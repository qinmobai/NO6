package com.cl.dao;

import com.cl.entity.TongzhijiluEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cl.entity.view.TongzhijiluView;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * 通知发送记录Dao
 *
 * @author 
 * @email 
 * @date 2025-04-03
 */
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

public interface TongzhijiluDao extends BaseMapper<TongzhijiluEntity> {
	
	List<TongzhijiluView> selectListView(@Param("ew") Wrapper<TongzhijiluEntity> wrapper);
	
	List<TongzhijiluView> selectListView(Pagination page, @Param("ew") Wrapper<TongzhijiluEntity> wrapper);

	TongzhijiluView selectView(@Param("ew") Wrapper<TongzhijiluEntity> wrapper);
	
	/**
	 * 查询需要重试的通知记录
	 */
	List<TongzhijiluEntity> selectRetryList(@Param("maxRetryCount") Integer maxRetryCount);
	
	/**
	 * 查询发送失败且未处理的记录
	 */
	List<TongzhijiluView> selectFailedList(@Param("ew") Wrapper<TongzhijiluEntity> wrapper);
}
