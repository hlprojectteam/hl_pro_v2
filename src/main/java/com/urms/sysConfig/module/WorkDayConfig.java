package com.urms.sysConfig.module;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @intruduction 工作日配置
 * @author Mr.Wang
 * @Date 2016年8月8日下午2:46:04
 */
@Entity
@Table(name="UM_WORKDAYCONFIG")
public class WorkDayConfig{
	
	private String id;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	private String sysKey;//子系统名称
	private String sysValue;//子系统编码
	private String memo;//备注
	
	@Id
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")   
	@Column(name = "ID", nullable = false,length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime==null?new Date():createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "SYS_VALUE", length=1024)
	public String getSysValue() {
		return sysValue;
	}
	public String getSysKey() {
		return sysKey;
	}
	@Column(name = "SYS_KEY", length=128)
	public void setSysKey(String sysKey) {
		this.sysKey = sysKey;
	}
	public void setSysValue(String sysValue) {
		this.sysValue = sysValue;
	}
	@Column(name = "MEMO_", length=1024)
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
