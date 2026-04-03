package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;


/**
 * 通知发送记录
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-04-03
 */
@TableName("tongzhijilu")
public class TongzhijiluEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public TongzhijiluEntity() {
		
	}
	
	public TongzhijiluEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	
	/**
	 * 预约ID
	 */
				
	private Long yuyueid;
	
	/**
	 * 预约编号
	 */
				
	private String yuyuebianhao;
	
	/**
	 * 用户账号
	 */
				
	private String zhanghao;
	
	/**
	 * 用户手机
	 */
				
	private String shouji;
	
	/**
	 * 医生账号
	 */
				
	private String yishengzhanghao;
	
	/**
	 * 医生电话
	 */
				
	private String dianhua;
	
	/**
	 * 通知类型(预约成功/就诊提醒/就诊前1天提醒等)
	 */
				
	private String tongzhileixing;
	
	/**
	 * 通知内容
	 */
				
	private String tongzhineirong;
	
	/**
	 * 发送时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date fasongshijian;
	
	/**
	 * 接收状态(0:待发送,1:发送成功,2:发送失败,3:已接收)
	 */
				
	private String jieshouzhuangtai;
	
	/**
	 * 失败原因
	 */
				
	private String shibaiyuanyin;
	
	/**
	 * 重试次数
	 */
				
	private Integer zhongshicishu;
	
	/**
	 * 最后重试时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date zuihouzhongshishijian;
	
	/**
	 * 处理状态(0:未处理,1:已处理,2:已忽略)
	 */
				
	private String chulizhuangtai;
	
	/**
	 * 处理备注
	 */
				
	private String chulibeizhu;
	
	/**
	 * 处理人
	 */
				
	private String chuliren;
	
	/**
	 * 处理时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date chulishijian;
	

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 设置：预约ID
	 */
	public void setYuyueid(Long yuyueid) {
		this.yuyueid = yuyueid;
	}
	/**
	 * 获取：预约ID
	 */
	public Long getYuyueid() {
		return yuyueid;
	}
	/**
	 * 设置：预约编号
	 */
	public void setYuyuebianhao(String yuyuebianhao) {
		this.yuyuebianhao = yuyuebianhao;
	}
	/**
	 * 获取：预约编号
	 */
	public String getYuyuebianhao() {
		return yuyuebianhao;
	}
	/**
	 * 设置：用户账号
	 */
	public void setZhanghao(String zhanghao) {
		this.zhanghao = zhanghao;
	}
	/**
	 * 获取：用户账号
	 */
	public String getZhanghao() {
		return zhanghao;
	}
	/**
	 * 设置：用户手机
	 */
	public void setShouji(String shouji) {
		this.shouji = shouji;
	}
	/**
	 * 获取：用户手机
	 */
	public String getShouji() {
		return shouji;
	}
	/**
	 * 设置：医生账号
	 */
	public void setYishengzhanghao(String yishengzhanghao) {
		this.yishengzhanghao = yishengzhanghao;
	}
	/**
	 * 获取：医生账号
	 */
	public String getYishengzhanghao() {
		return yishengzhanghao;
	}
	/**
	 * 设置：医生电话
	 */
	public void setDianhua(String dianhua) {
		this.dianhua = dianhua;
	}
	/**
	 * 获取：医生电话
	 */
	public String getDianhua() {
		return dianhua;
	}
	/**
	 * 设置：通知类型(预约成功/就诊提醒/就诊前1天提醒等)
	 */
	public void setTongzhileixing(String tongzhileixing) {
		this.tongzhileixing = tongzhileixing;
	}
	/**
	 * 获取：通知类型(预约成功/就诊提醒/就诊前1天提醒等)
	 */
	public String getTongzhileixing() {
		return tongzhileixing;
	}
	/**
	 * 设置：通知内容
	 */
	public void setTongzhineirong(String tongzhineirong) {
		this.tongzhineirong = tongzhineirong;
	}
	/**
	 * 获取：通知内容
	 */
	public String getTongzhineirong() {
		return tongzhineirong;
	}
	/**
	 * 设置：发送时间
	 */
	public void setFasongshijian(Date fasongshijian) {
		this.fasongshijian = fasongshijian;
	}
	/**
	 * 获取：发送时间
	 */
	public Date getFasongshijian() {
		return fasongshijian;
	}
	/**
	 * 设置：接收状态(0:待发送,1:发送成功,2:发送失败,3:已接收)
	 */
	public void setJieshouzhuangtai(String jieshouzhuangtai) {
		this.jieshouzhuangtai = jieshouzhuangtai;
	}
	/**
	 * 获取：接收状态(0:待发送,1:发送成功,2:发送失败,3:已接收)
	 */
	public String getJieshouzhuangtai() {
		return jieshouzhuangtai;
	}
	/**
	 * 设置：失败原因
	 */
	public void setShibaiyuanyin(String shibaiyuanyin) {
		this.shibaiyuanyin = shibaiyuanyin;
	}
	/**
	 * 获取：失败原因
	 */
	public String getShibaiyuanyin() {
		return shibaiyuanyin;
	}
	/**
	 * 设置：重试次数
	 */
	public void setZhongshicishu(Integer zhongshicishu) {
		this.zhongshicishu = zhongshicishu;
	}
	/**
	 * 获取：重试次数
	 */
	public Integer getZhongshicishu() {
		return zhongshicishu;
	}
	/**
	 * 设置：最后重试时间
	 */
	public void setZuihouzhongshishijian(Date zuihouzhongshishijian) {
		this.zuihouzhongshishijian = zuihouzhongshishijian;
	}
	/**
	 * 获取：最后重试时间
	 */
	public Date getZuihouzhongshishijian() {
		return zuihouzhongshishijian;
	}
	/**
	 * 设置：处理状态(0:未处理,1:已处理,2:已忽略)
	 */
	public void setChulizhuangtai(String chulizhuangtai) {
		this.chulizhuangtai = chulizhuangtai;
	}
	/**
	 * 获取：处理状态(0:未处理,1:已处理,2:已忽略)
	 */
	public String getChulizhuangtai() {
		return chulizhuangtai;
	}
	/**
	 * 设置：处理备注
	 */
	public void setChulibeizhu(String chulibeizhu) {
		this.chulibeizhu = chulibeizhu;
	}
	/**
	 * 获取：处理备注
	 */
	public String getChulibeizhu() {
		return chulibeizhu;
	}
	/**
	 * 设置：处理人
	 */
	public void setChuliren(String chuliren) {
		this.chuliren = chuliren;
	}
	/**
	 * 获取：处理人
	 */
	public String getChuliren() {
		return chuliren;
	}
	/**
	 * 设置：处理时间
	 */
	public void setChulishijian(Date chulishijian) {
		this.chulishijian = chulishijian;
	}
	/**
	 * 获取：处理时间
	 */
	public Date getChulishijian() {
		return chulishijian;
	}

}
