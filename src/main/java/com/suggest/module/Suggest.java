package com.suggest.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 建议
 * @author qinyongqian
 * @date 2019年3月1日
 *
 */
@Entity
@Table(name="P_SUGGEST")
public class Suggest extends BaseModule{

	private String content;		//内容
	private String reback;		//反馈内容
	private String rebackUserId;		//反馈人ID
	private Integer moduleClass;       //模块分类 (数据字典:suggest_moduleclass) 1 安全管理，2党建
	private Integer status;	        //状态	(数据字典:suggest_Status) 1 未阅，2已阅，3已反馈
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date readDate;		//阅读时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date rebackDate;		//反馈时间
	
	@Column(name = "suggest_content",length=1000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "suggest_reback",length=1000)
	public String getReback() {
		return reback;
	}
	public void setReback(String reback) {
		this.reback = reback;
	}
	@Column(name = "reback_userid",length=32)
	public String getRebackUserId() {
		return rebackUserId;
	}
	public void setRebackUserId(String rebackUserId) {
		this.rebackUserId = rebackUserId;
	}
	@Column(name = "module_class",length=2)
	public Integer getModuleClass() {
		return moduleClass;
	}
	public void setModuleClass(Integer moduleClass) {
		this.moduleClass = moduleClass;
	}
	@Column(name = "suggest_status",length=2)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "read_date")
	public Date getReadDate() {
		return readDate;
	}
	public void setReadDate(Date readDate) {
		this.readDate = readDate;
	}
	@Column(name = "reback_date")
	public Date getRebackDate() {
		return rebackDate;
	}
	public void setRebackDate(Date rebackDate) {
		this.rebackDate = rebackDate;
	}
	
	
	
}
