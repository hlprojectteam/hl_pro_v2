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
 * @Description 顾客意见反馈
 * @author xuezb
 * @date 2019年2月18日
 */
@Entity
@Table(name="dc_FeedBack")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class FeedBack extends BaseModule{
	
	private String title;                  //标题
	private String formNumber;             //表单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDate;                 //日期
	@DateTimeFormat(pattern="HH:mm")
	private Date receiptTime;              //接报时间
	private String reportedPerson;         //报告人员
	private Integer customerSex;           //性别			(数据字典：sex)
	private String plateNum;               //车辆号牌
	private String customerPhone;          //联系电话
	private Integer fbType;                //反馈类型		(数据字典：dc_fbType)
	private String watcher;                //值班员 (数据字典：dc_dutyPerson)
	private String situationDesc;          //情况概述
	private String disposalSituation;      //处理情况
	private String remark;                 //备注
	private String ttId;                   //主表id
	
	
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
	
	@Column(name = "receipt_Time")
	public Date getReceiptTime() {
		return receiptTime;
	}
	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}
	
	@Column(name = "reported_Person", length=20)
	public String getReportedPerson() {
		return reportedPerson;
	}
	public void setReportedPerson(String reportedPerson) {
		this.reportedPerson = reportedPerson;
	}
	
	@Column(name = "customer_Sex", length=11)
	public Integer getCustomerSex() {
		return customerSex;
	}
	public void setCustomerSex(Integer customerSex) {
		this.customerSex = customerSex;
	}
	
	@Column(name = "plate_Num", length=12)
	public String getPlateNum() {
		return plateNum;
	}
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
	
	@Column(name = "customer_Phone", length=16)
	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	
	@Column(name = "fb_Type", length=11)
	public Integer getFbType() {
		return fbType;
	}
	public void setFbType(Integer fbType) {
		this.fbType = fbType;
	}
	
	@Column(name = "watcher_", length=20)
	public String getWatcher() {
		return watcher;
	}
	public void setWatcher(String watcher) {
		this.watcher = watcher;
	}
	
	@Column(name = "situation_Desc", length=250)
	public String getSituationDesc() {
		return situationDesc;
	}
	public void setSituationDesc(String situationDesc) {
		this.situationDesc = situationDesc;
	}
	
	@Column(name = "disposal_Situation", length=250)
	public String getDisposalSituation() {
		return disposalSituation;
	}
	public void setDisposalSituation(String disposalSituation) {
		this.disposalSituation = disposalSituation;
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
