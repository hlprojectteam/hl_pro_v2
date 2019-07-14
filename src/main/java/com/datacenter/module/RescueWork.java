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
 * @Description 拯救作业
 * @author xuezb
 * @date 2019年2月15日
 */
@Entity
@Table(name="dc_RescueWork")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class RescueWork extends BaseModule{
	
	private String title;             //标题
	private String formNumber;        //表单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDate;            //日期
	@DateTimeFormat(pattern="HH:mm")
	private Date receiptTime;         //接报时间
	@DateTimeFormat(pattern="HH:mm")
	private Date arrivalTime;         //到达时间
	private String usedTime;          //到场用时(到达时间-接报时间)
	@DateTimeFormat(pattern="HH:mm")
	private Date evacuationTime;      //清场时间 
	private String site;              //地点   
	private String faultPlates;       //故障车牌 
	private String carType;          //车型		(数据字典：dc_carType,	1:小型客车 2:中型客车 3:大型客车 4：小型货车 5：中型货车 6：大型货车 7：重型牵挂车 8：其它（人工输入）)
	private String paymentOrder;      //缴费单号 
	private Double rescueCharge;      //拯救费  
	private Double trailerMileage;    //拖车里程 
	private String whereabouts;      //车辆去向	(数据字典：dc_whereabouts)
	private String rescuePlates;      //拯救车牌 
    private String driverPhone;       //司机电话 
    private String remark;            //备注  	(可以为空) 
    private String ttId;              //主表id 
    
    private double x;				  //坐标X
	private double y;				  //坐标y
    
    
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
	
	@Column(name = "arrival_Time")
	public Date getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	@Column(name = "used_Time", length=6)
	public String getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(String usedTime) {
		this.usedTime = usedTime;
	}
	
	@Column(name = "evacuation_Time")
	public Date getEvacuationTime() {
		return evacuationTime;
	}
	public void setEvacuationTime(Date evacuationTime) {
		this.evacuationTime = evacuationTime;
	}
	
	@Column(name = "site_", length=50)
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	
	@Column(name = "fault_Plates", length=12)
	public String getFaultPlates() {
		return faultPlates;
	}
	public void setFaultPlates(String faultPlates) {
		this.faultPlates = faultPlates;
	}
	
	@Column(name = "car_Type", length=20)
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	
	@Column(name = "payment_Order", length=8)
	public String getPaymentOrder() {
		return paymentOrder;
	}
	public void setPaymentOrder(String paymentOrder) {
		this.paymentOrder = paymentOrder;
	}
	
	@Column(name = "rescue_Charge", length=6)
	public Double getRescueCharge() {
		return rescueCharge;
	}
	public void setRescueCharge(Double rescueCharge) {
		this.rescueCharge = rescueCharge;
	}
	
	@Column(name = "trailer_Mileage", length=6)
	public Double getTrailerMileage() {
		return trailerMileage;
	}
	public void setTrailerMileage(Double trailerMileage) {
		this.trailerMileage = trailerMileage;
	}
	
	@Column(name = "whereabouts_", length=20)
	public String getWhereabouts() {
		return whereabouts;
	}
	public void setWhereabouts(String whereabouts) {
		this.whereabouts = whereabouts;
	}
	
	@Column(name = "rescue_Plates", length=12)
	public String getRescuePlates() {
		return rescuePlates;
	}
	public void setRescuePlates(String rescuePlates) {
		this.rescuePlates = rescuePlates;
	}
	
	@Column(name = "driver_Phone", length=16)
	public String getDriverPhone() {
		return driverPhone;
	}
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
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
	
	@Column(name = "x_")
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	
	@Column(name = "y_")
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
}