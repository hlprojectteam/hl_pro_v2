package com.urms.user.module;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="UM_USER_EXTEND")
public class UserExtend {
	
	private String id;
	private String userId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date lastLoginTime;//最后一次登录时间
	private Integer loginTimes;//登录次数  新增初始化为0
	private String loginIp;//登录ip
	private Integer loginWay;//登录途径 1：web 2:手机端
	
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
	
	@Column(name = "last_Login_Time")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	@Column(name = "login_Times")
	public Integer getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}
	@Column(name = "login_Ip",length=64)
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	@Column(name = "LOGIN_WAY",length=2)
	public Integer getLoginWay() {
		return loginWay;
	}
	public void setLoginWay(Integer loginWay) {
		this.loginWay = loginWay;
	}
	@Column(name = "USER_ID",length=32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
