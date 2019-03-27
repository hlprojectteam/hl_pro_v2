package com.urms.loginLog.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户登录日志
 * 
 * @author Mr.Wang 2018年7月24日 15:12:26
 */

@Entity
@Table(name = "um_user_login_log")
public class LoginLog {

	private String loginLogId; // 主键
	private String sysCode; // 系统编码
	private String userId; // 登录用户id
	private String loginAccount; // 登录帐号
	private String loginName; // 用户名称
	private String loginIp; // 用户登录的公网IP
	private String loginAddress; // 登录地址
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date loginTime;// 登录时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date logOutTime; // 退出时间
	private Integer loginSource;// 登录方式 1电脑，2APP，3微信，4其它
	private Integer loginState;// 当前状态 1在线，2离线
	private Long onlineTime;// 在线时长 (以毫秒记录，前端显示：1小时24分34秒)

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")
	@Column(name = "loginLog_Id", nullable = false, length = 32)
	public String getLoginLogId() {
		return loginLogId;
	}

	public void setLoginLogId(String loginLogId) {
		this.loginLogId = loginLogId;
	}

	@Column(name = "sys_Code", length = 20)
	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	@Column(name = "login_Account", length = 50)
	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	@Column(name = "login_Name", length = 50)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = "login_Ip", length = 50)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@Column(name = "login_Address", length = 30)
	public String getLoginAddress() {
		return loginAddress;
	}

	public void setLoginAddress(String loginAddress) {
		this.loginAddress = loginAddress;
	}

	@Column(name = "login_Time")
	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Column(name = "logOut_Time")
	public Date getLogOutTime() {
		return logOutTime;
	}

	public void setLogOutTime(Date logOutTime) {
		this.logOutTime = logOutTime;
	}

	@Column(name = "login_Source", length = 11)
	public Integer getLoginSource() {
		return loginSource;
	}

	public void setLoginSource(Integer loginSource) {
		this.loginSource = loginSource;
	}

	@Column(name = "login_State", length = 11)
	public Integer getLoginState() {
		return loginState;
	}

	public void setLoginState(Integer loginState) {
		this.loginState = loginState;
	}

	@Column(name = "online_Time")
	public Long getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Long onlineTime) {
		this.onlineTime = onlineTime;
	}

	@Column(name = "user_Id", length = 32)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
