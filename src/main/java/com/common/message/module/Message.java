package com.common.message.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.common.base.module.BaseModule;

@Entity
@Table(name="P_MESSAGE")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Message extends BaseModule{
	
	private String alias;		//发送目标用户ID集合，用“，”分隔
	private String tags;		//发送目标用户角色code，用“，”分隔
	private Integer type;		//消息类型 1教育考试 2营运动态 3规章制度 4安全管理 5党建 6考勤 
	private String content;		//消息内容
	private String title;		//消息标题
	private String sender;		//发件人
	
	
	@Column(name = "ALIAS", length = 4000)
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	@Column(name = "TAGS", length = 500)
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	@Column(name = "TYPE", length = 10)
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column(name = "CONTENT", length = 4000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "TITLE", length = 200)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "SNEDER", length = 128)
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}

}
