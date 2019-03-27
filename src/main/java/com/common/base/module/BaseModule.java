package com.common.base.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @intruduction 基本Entity
 * @author Mr.Wang
 * @Date 2016年7月1日下午2:27:58
 */
@MappedSuperclass
public class BaseModule implements Comparable<Object> {

	private String id;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	private String sysCode;//子系统编码
	private String creatorId;//创建人id
	private String creatorName;//创建人名称
	
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
	@Column(name = "SYS_CODE",length=16)	
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	@Column(name = "CREATOR_ID",length=32)	
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	@Column(name = "CREATOR_NAME",length=64)	
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	@Override
	public int compareTo(Object o) {
		return 1;
	}
}
