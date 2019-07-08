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
 * @Description 外勤作业
 * @author xuezb
 * @date 2019年2月18日
 */
@Entity
@Table(name="dc_FieldOperations")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class FieldOperations extends BaseModule{
	
	private String title;               //标题
	private String formNumber;          //表单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")   
	private Date dutyDate;              //日期
	@DateTimeFormat(pattern="HH:mm") 
    private Date receiptTime;           //接报时间
	private String reportedPerson;      //报告人员
	private Integer receiptWay;         //接报方式		(数据字典：dc_receiptWay)
	private String outworker;           //外勤人员
	private String scene;               //事发地点
	private String involvedUnits;       //涉事单位
	private String violationOrderNo;    //违章单号
	private String receiptSituation;    //接报情况
	private String disposeDesc;         //处置简述
	private String remark;              //备注
	private String ttId;                //主表id
	
	
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
	
	@Column(name = "receipt_Way", length=11)
	public Integer getReceiptWay() {
		return receiptWay;
	}
	public void setReceiptWay(Integer receiptWay) {
		this.receiptWay = receiptWay;
	}
	
	@Column(name = "out_worker", length=20)
	public String getOutworker() {
		return outworker;
	}
	public void setOutworker(String outworker) {
		this.outworker = outworker;
	}
	
	@Column(name = "scene_", length=30)
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	
	@Column(name = "involved_Units", length=30)
	public String getInvolvedUnits() {
		return involvedUnits;
	}
	public void setInvolvedUnits(String involvedUnits) {
		this.involvedUnits = involvedUnits;
	}
	
	@Column(name = "violation_OrderNo", length=30)
	public String getViolationOrderNo() {
		return violationOrderNo;
	}
	public void setViolationOrderNo(String violationOrderNo) {
		this.violationOrderNo = violationOrderNo;
	}
	
	@Column(name = "receipt_Situation", length=1000)
	public String getReceiptSituation() {
		return receiptSituation;
	}
	public void setReceiptSituation(String receiptSituation) {
		this.receiptSituation = receiptSituation;
	}
	
	@Column(name = "dispose_Desc", length=1000)
	public String getDisposeDesc() {
		return disposeDesc;
	}
	public void setDisposeDesc(String disposeDesc) {
		this.disposeDesc = disposeDesc;
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
