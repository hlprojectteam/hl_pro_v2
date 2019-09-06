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
 * @Description 营运异常记录
 * @author xuezb
 * @date 2019年2月18日
 */
@Entity
@Table(name="dc_ExceptionRecord")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ExceptionRecord extends BaseModule{
	
	private String title;                     //标题        
	private String formNumber;                //表单编号      
	@DateTimeFormat(pattern="yyyy-MM-dd")                 
	private Date dutyDate;                    //日期
	private Integer exceptionType; 			  //异常类型	(数据字典: dc_exceptionType  1:营运异常  2:其他异常)
	@DateTimeFormat(pattern="HH:mm")                      
	private Date receiptTime;                 //接报时间      
	private String reportedDp;                //报告部门      字典：dc_reportingDepartment
	private String reportedPerson;            //报告人员      
	private String reportedWay;              //报告方式	(数据字典：dc_reportedWay_ER 1：服务热线 2：内线电话 3：路面监控 4：环龙营运动态信息群 5：其它（人工输入）)
	private String trafficRoad;               //通行路段      
	private String processingDp;              //通知处理部门    字典：dc_NotificationDepartment
	private String briefIntroduction;         //情况简述      
	private String result;                    //处理结果      
	private String remark;                    //备注        
	private String ttId;                      //主表id      


	@Column(name = "exception_Type", length=11)
	public Integer getExceptionType() {
		return exceptionType;
	}
	public void setExceptionType(Integer exceptionType) {
		this.exceptionType = exceptionType;
	}

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
	
	@Column(name = "reported_Dp", length=30)
	public String getReportedDp() {
		return reportedDp;
	}
	public void setReportedDp(String reportedDp) {
		this.reportedDp = reportedDp;
	}
	
	@Column(name = "reported_Person", length=20)
	public String getReportedPerson() {
		return reportedPerson;
	}
	public void setReportedPerson(String reportedPerson) {
		this.reportedPerson = reportedPerson;
	}
	
	@Column(name = "reported_Way", length=20)
	public String getReportedWay() {
		return reportedWay;
	}
	public void setReportedWay(String reportedWay) {
		this.reportedWay = reportedWay;
	}
	
	@Column(name = "traffic_Road", length=30)
	public String getTrafficRoad() {
		return trafficRoad;
	}
	public void setTrafficRoad(String trafficRoad) {
		this.trafficRoad = trafficRoad;
	}
	
	@Column(name = "processing_Dp", length=30)
	public String getProcessingDp() {
		return processingDp;
	}
	public void setProcessingDp(String processingDp) {
		this.processingDp = processingDp;
	}
	
	@Column(name = "brief_Introduction", length=800)
	public String getBriefIntroduction() {
		return briefIntroduction;
	}
	public void setBriefIntroduction(String briefIntroduction) {
		this.briefIntroduction = briefIntroduction;
	}
	
	@Column(name = "result_", length=800)
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	@Column(name = "remark_", length=500)
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
