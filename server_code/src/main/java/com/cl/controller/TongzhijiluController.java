package com.cl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.cl.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.annotation.SysLog;

import com.cl.entity.TongzhijiluEntity;
import com.cl.entity.view.TongzhijiluView;

import com.cl.service.TongzhijiluService;
import com.cl.service.NotificationService;
import com.cl.service.TokenService;
import com.cl.entity.TokenEntity;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 通知发送记录
 * 后端接口
 * @author 
 * @email 
 * @date 2025-04-03
 */
@RestController
@RequestMapping("/tongzhijilu")
public class TongzhijiluController {
    @Autowired
    private TongzhijiluService tongzhijiluService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private TokenService tokenService;
    
    /**
     * 检查是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        if(StringUtils.isBlank(token)) {
            return false;
        }
        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if(tokenEntity == null) {
            return false;
        }
        // 只有管理员(tableName=users)才能访问
        return "users".equals(tokenEntity.getTablename());
    }






    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,TongzhijiluEntity tongzhijilu,
                                                                                                                                                                                        HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可查看通知记录");
        }
        
        EntityWrapper<TongzhijiluEntity> ew = new EntityWrapper<TongzhijiluEntity>();
                                                                                                                                                                                                        
        
        
        PageUtils page = tongzhijiluService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhijilu), params), params));
        return R.ok().put("data", page);
    }






    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,TongzhijiluEntity tongzhijilu,
		HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可查看通知记录");
        }
        
        EntityWrapper<TongzhijiluEntity> ew = new EntityWrapper<TongzhijiluEntity>();

		PageUtils page = tongzhijiluService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhijilu), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(TongzhijiluEntity tongzhijilu, HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可查看通知记录");
        }
        
      	EntityWrapper<TongzhijiluEntity> ew = new EntityWrapper<TongzhijiluEntity>();
      	ew.allEq(MPUtil.allEQMapPre( tongzhijilu, "tongzhijilu")); 
        return R.ok().put("data", tongzhijiluService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(TongzhijiluEntity tongzhijilu, HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可查看通知记录");
        }
        
        EntityWrapper< TongzhijiluEntity> ew = new EntityWrapper< TongzhijiluEntity>();
 		ew.allEq(MPUtil.allEQMapPre( tongzhijilu, "tongzhijilu")); 
		TongzhijiluView tongzhijiluView =  tongzhijiluService.selectView(ew);
		return R.ok("查询通知发送记录成功").put("data", tongzhijiluView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可查看通知记录");
        }
        
        TongzhijiluEntity tongzhijilu = tongzhijiluService.selectById(id);
		tongzhijilu = tongzhijiluService.selectView(new EntityWrapper<TongzhijiluEntity>().eq("id", id));
        return R.ok().put("data", tongzhijilu);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可查看通知记录");
        }
        
        TongzhijiluEntity tongzhijilu = tongzhijiluService.selectById(id);
		tongzhijilu = tongzhijiluService.selectView(new EntityWrapper<TongzhijiluEntity>().eq("id", id));
        return R.ok().put("data", tongzhijilu);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @SysLog("新增通知发送记录")
    public R save(@RequestBody TongzhijiluEntity tongzhijilu, HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可操作通知记录");
        }
        
    	//ValidatorUtils.validateEntity(tongzhijilu);
        tongzhijiluService.insert(tongzhijilu);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @SysLog("新增通知发送记录")
    @RequestMapping("/add")
    public R add(@RequestBody TongzhijiluEntity tongzhijilu, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(tongzhijilu);
        tongzhijiluService.insert(tongzhijilu);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改通知发送记录")
    public R update(@RequestBody TongzhijiluEntity tongzhijilu, HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可操作通知记录");
        }
        
        //ValidatorUtils.validateEntity(tongzhijilu);
        tongzhijiluService.updateById(tongzhijilu);//全部更新
        return R.ok();
    }

    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除通知发送记录")
    public R delete(@RequestBody Long[] ids, HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可操作通知记录");
        }
        
        tongzhijiluService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 获取通知类型列表
     */
    @RequestMapping("/types")
    @IgnoreAuth
    public R getNotificationTypes(){
        return R.ok().put("data", notificationService.getNotificationTypes());
    }
    
    /**
     * 获取发送失败的通知列表
     */
    @RequestMapping("/failedList")
    public R failedList(@RequestParam Map<String, Object> params, HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可查看通知记录");
        }
        
        EntityWrapper<TongzhijiluEntity> ew = new EntityWrapper<TongzhijiluEntity>();
        ew.eq("jieshouzhuangtai", "2"); // 发送失败
        ew.andNew().eq("chulizhuangtai", "0").or().isNull("chulizhuangtai"); // 未处理
        
        PageUtils page = tongzhijiluService.queryPage(params, MPUtil.sort(MPUtil.between(ew, params), params));
        return R.ok().put("data", page);
    }
    
    /**
     * 处理通知记录
     */
    @RequestMapping("/handle")
    @Transactional
    @SysLog("处理通知记录")
    public R handle(@RequestBody Map<String, Object> params, HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可操作通知记录");
        }
        
        Long id = Long.valueOf(params.get("id").toString());
        String chulizhuangtai = params.get("chulizhuangtai").toString();
        String chulibeizhu = params.get("chulibeizhu") != null ? params.get("chulibeizhu").toString() : "";
        String chuliren = request.getSession().getAttribute("username").toString();
        
        boolean success = tongzhijiluService.handleNotification(id, chulizhuangtai, chulibeizhu, chuliren);
        if(success) {
            return R.ok("处理成功");
        } else {
            return R.error("处理失败，记录不存在");
        }
    }
    
    /**
     * 重试发送通知
     */
    @RequestMapping("/retry/{id}")
    @Transactional
    @SysLog("重试发送通知")
    public R retry(@PathVariable("id") Long id, HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可操作通知记录");
        }
        
        TongzhijiluEntity record = tongzhijiluService.selectById(id);
        if(record == null) {
            return R.error("通知记录不存在");
        }
        
        boolean success = notificationService.sendSingleNotification(record);
        if(success) {
            return R.ok("重试发送成功");
        } else {
            return R.error("重试发送失败: " + record.getShibaiyuanyin());
        }
    }
    
    /**
     * 批量重试失败的通知
     */
    @RequestMapping("/retryBatch")
    @Transactional
    @SysLog("批量重试失败通知")
    public R retryBatch(@RequestParam(required = false) Integer maxRetryCount, HttpServletRequest request){
        // 权限检查：只有管理员才能访问
        if(!isAdmin(request)) {
            return R.error(403, "无权访问，仅管理员可操作通知记录");
        }
        
        Map<String, Object> result = notificationService.retryFailedNotifications(maxRetryCount);
        return R.ok().put("data", result);
    }
    
    /**
     * 更新用户接收状态
     */
    @RequestMapping("/updateReceiveStatus")
    @IgnoreAuth
    public R updateReceiveStatus(@RequestBody Map<String, Object> params){
        Long tongzhiId = Long.valueOf(params.get("tongzhiId").toString());
        String status = params.get("status").toString();
        
        boolean success = notificationService.updateReceiveStatus(tongzhiId, status);
        if(success) {
            return R.ok("状态更新成功");
        } else {
            return R.error("状态更新失败");
        }
    }
    
	




}
