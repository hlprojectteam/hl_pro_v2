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
 * @Description 监控巡检
 * @author xuezb
 * @date 2019年2月15日
 */
@Entity
@Table(name="dc_SurveillanceInspection")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class SurveillanceInspection extends BaseModule{
	
	private String title;                 //标题
	private String formNumber;            //表单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDate;                //日期
	private Integer weather;              //天气	(数据字典：dc_weather, 1:晴  2:阴  3:多云  4:小雨 5:中雨 6:大雨 7:暴雨 8:雷阵雨  9:霜冻 10:雾 )
	@DateTimeFormat(pattern="HH:mm")
	private Date inspectionTimeStart;     //巡检时间Start
	@DateTimeFormat(pattern="HH:mm")
	private Date inspectionTimeEnd;       //巡检时间End
	private String shiftSupervisor;       //值班主任
	private Integer inspectionlocation;   //巡检位置	(数据字典：dc_inspectionlocation, 1:主线	2：收费站   3:匝道)
	private String inspectionDetails;     //巡检情况描述
	private String followMeasure;         //跟进措施	(可以为空)
	private String ttId;                  //主表id

	private Integer failureEquipment;	  //故障设备	(数据字典: dc_failureEquipment,	1、无; 2、路面摄像枪; 3、收费站广场摄像枪; 4、情报板系统; 5、其它)
	
	
	@Column(name = "inspection_Time_Start")
	public Date getInspectionTimeStart() {
		return inspectionTimeStart;
	}
	public void setInspectionTimeStart(Date inspectionTimeStart) {
		this.inspectionTimeStart = inspectionTimeStart;
	}
	
	@Column(name = "inspection_Time_End")
	public Date getInspectionTimeEnd() {
		return inspectionTimeEnd;
	}
	public void setInspectionTimeEnd(Date inspectionTimeEnd) {
		this.inspectionTimeEnd = inspectionTimeEnd;
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
	
	@Column(name = "weather_", length=11)
	public Integer getWeather() {
		return weather;
	}
	public void setWeather(Integer weather) {
		this.weather = weather;
	}
	
	@Column(name = "shift_Supervisor", length=20)
	public String getShiftSupervisor() {
		return shiftSupervisor;
	}
	public void setShiftSupervisor(String shiftSupervisor) {
		this.shiftSupervisor = shiftSupervisor;
	}
	
	@Column(name = "inspection_location", length=11)
	public Integer getInspectionlocation() {
		return inspectionlocation;
	}
	public void setInspectionlocation(Integer inspectionlocation) {
		this.inspectionlocation = inspectionlocation;
	}
	
	@Column(name = "inspection_Details", length=500)
	public String getInspectionDetails() {
		return inspectionDetails;
	}
	public void setInspectionDetails(String inspectionDetails) {
		this.inspectionDetails = inspectionDetails;
	}
	
	@Column(name = "follow_Measure", length=100)
	public String getFollowMeasure() {
		return followMeasure;
	}
	public void setFollowMeasure(String followMeasure) {
		this.followMeasure = followMeasure;
	}
	
	@Column(name = "ttId", length=32)
	public String getTtId() {
		return ttId;
	}
	public void setTtId(String ttId) {
		this.ttId = ttId;
	}

	@Column(name = "failure_Equipment")
	public Integer getFailureEquipment() {
		return failureEquipment;
	}
	public void setFailureEquipment(Integer failureEquipment) {
		this.failureEquipment = failureEquipment;
	}
}
