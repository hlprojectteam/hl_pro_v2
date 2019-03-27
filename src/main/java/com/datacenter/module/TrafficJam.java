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
 * @Description 交通阻塞
 * @author xuezb
 * @date 2019年2月18日
 */
@Entity
@Table(name="dc_TrafficJam")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class TrafficJam extends BaseModule{
	
	private String title;                //标题
    private String formNumber;           //表单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")   
	private Date dutyDate;               //日期
	@DateTimeFormat(pattern="HH:mm")   
	private Date receiptTime;            //接报时间	
	private Integer receiptWay;          //接报方式	(数据字典：dc_receiptWay)
	private String reportedPerson;       //报告人员
	private String jamSection;           //阻塞路段
	private double jamDistance;          //阻塞距离(单位是公里)
	@DateTimeFormat(pattern="HH:mm")   
	private Date startTime;              //开始时间
	@DateTimeFormat(pattern="HH:mm")   
	private Date endTime;                //结束时间
	@DateTimeFormat(pattern="HH:mm")   
	private Date jjdcTime;               //交警到场时间
	@DateTimeFormat(pattern="HH:mm")   
	private Date lgydcTime;              //路管员到场时间
	private String jamReason;            //阻塞原因
	private String disposalSituation;    //处理情况
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
	
	@Column(name = "receipt_Time")
	public Date getReceiptTime() {
		return receiptTime;
	}
	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}
	
	@Column(name = "receipt_Way", length=11)
	public Integer getReceiptWay() {
		return receiptWay;
	}
	public void setReceiptWay(Integer receiptWay) {
		this.receiptWay = receiptWay;
	}
	
	@Column(name = "reported_Person", length=20)
	public String getReportedPerson() {
		return reportedPerson;
	}
	public void setReportedPerson(String reportedPerson) {
		this.reportedPerson = reportedPerson;
	}
	
	@Column(name = "jam_Section", length=50)
	public String getJamSection() {
		return jamSection;
	}
	public void setJamSection(String jamSection) {
		this.jamSection = jamSection;
	}
	
	@Column(name = "jam_Distance")
	public double getJamDistance() {
		return jamDistance;
	}
	public void setJamDistance(double jamDistance) {
		this.jamDistance = jamDistance;
	}
	
	@Column(name = "start_Time")
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Column(name = "end_Time")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "jjdc_Time")
	public Date getJjdcTime() {
		return jjdcTime;
	}
	public void setJjdcTime(Date jjdcTime) {
		this.jjdcTime = jjdcTime;
	}
	
	@Column(name = "lgydc_Time")
	public Date getLgydcTime() {
		return lgydcTime;
	}
	public void setLgydcTime(Date lgydcTime) {
		this.lgydcTime = lgydcTime;
	}
	
	@Column(name = "jam_Reason", length=300)
	public String getJamReason() {
		return jamReason;
	}
	public void setJamReason(String jamReason) {
		this.jamReason = jamReason;
	}
	
	@Column(name = "disposal_Situation", length=300)
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