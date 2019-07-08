package com.datacenter.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * @Description 信息通传
 * @author xuezb
 * @date 2019年2月18日
 */
@Entity
@Table(name="dc_InfoThrough")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class InfoThrough extends BaseModule{
	
	private String title;                //标题
	private String formNumber;           //表单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")   
	private Date dutyDate;               //日期
	@DateTimeFormat(pattern="HH:mm")     
	private Date throughTime;            //通报时间
	private String reportedPerson;       //报告人员
	private Integer infoType;            //信息类型	(数据字典：dc_infoType)
	private Integer infoSource;          //信息来源	(数据字典：dc_infoSource  1：全路段及收费站 2：其它（人工输入）)
	private Integer throughWay;          //通传方式	(数据字典：dc_throughWay)
	private String watcher;              //值班员
	private String infoContent;          //信息内容
	private String throughSituation;     //通传情况
	private String remark;               //备注
	private String ttId;                 //主表id
	
	
	@Column(name = "title_", length=50)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "form_Number", length=12)
	public String getFormNumber() {
		return formNumber;
	}
	public void setFormNumber(String formNumber) {
		this.formNumber = formNumber;
	}
	
	@Column(name = "duty_Date")
	public Date getDutyDate() {
		return dutyDate;
	}
	public void setDutyDate(Date dutyDate) {
		this.dutyDate = dutyDate;
	}
	
	@Column(name = "through_Time")
	public Date getThroughTime() {
		return throughTime;
	}
	public void setThroughTime(Date throughTime) {
		this.throughTime = throughTime;
	}
	
	@Column(name = "reported_Person", length=20)
	public String getReportedPerson() {
		return reportedPerson;
	}
	public void setReportedPerson(String reportedPerson) {
		this.reportedPerson = reportedPerson;
	}
	
	@Column(name = "info_Type", length=11)
	public Integer getInfoType() {
		return infoType;
	}
	public void setInfoType(Integer infoType) {
		this.infoType = infoType;
	}
	
	@Column(name = "info_Source", length=11)
	public Integer getInfoSource() {
		return infoSource;
	}
	public void setInfoSource(Integer infoSource) {
		this.infoSource = infoSource;
	}
	
	@Column(name = "through_Way", length=11)
	public Integer getThroughWay() {
		return throughWay;
	}
	public void setThroughWay(Integer throughWay) {
		this.throughWay = throughWay;
	}
	
	@Column(name = "watcher_", length=20)
	public String getWatcher() {
		return watcher;
	}
	public void setWatcher(String watcher) {
		this.watcher = watcher;
	}
	
	@Column(name = "info_Content", length=800)
	public String getInfoContent() {
		return infoContent;
	}
	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
	
	@Column(name = "through_Situation", length=1000)
	public String getThroughSituation() {
		return throughSituation;
	}
	public void setThroughSituation(String throughSituation) {
		this.throughSituation = throughSituation;
	}
	
	@Column(name = "remark_", length=250)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "ttId", length=32)
	public String getTtId() {
		return ttId;
	}
	public void setTtId(String ttId) {
		this.ttId = ttId;
	}
	
}
