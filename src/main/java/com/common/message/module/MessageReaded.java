package com.common.message.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 已读消息表
 * @author qinyongqian
 * @date 2019年5月30日
 *
 */
@Entity
@Table(name="P_MESSAGE_READED")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class MessageReaded extends BaseModule{
	
	private String userId;		    //用户ID
	private String readMsgs;		//已经读的消息id，多个用“,”隔开
	private String newReadMsg;		//最新读的消息id
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;        // 记录最近修改时间
	
	@Column(name = "USER_ID", length = 32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name = "READ_MSGS", length = 4000)
	public String getReadMsgs() {
		return readMsgs;
	}
	public void setReadMsgs(String readMsgs) {
		this.readMsgs = readMsgs;
	}
	@Column(name = "NEW_READMSG", length = 32)
	public String getNewReadMsg() {
		return newReadMsg;
	}
	public void setNewReadMsg(String newReadMsg) {
		this.newReadMsg = newReadMsg;
	}
	@Column(name = "UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
	

}
