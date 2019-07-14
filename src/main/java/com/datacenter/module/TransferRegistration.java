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
 * @Description 交接班登记表
 * @author xuezb
 * @date 2019年2月15日
 */
@Entity
@Table(name="dc_TransferRegistration")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class TransferRegistration extends BaseModule{
	
    private String title;			     //标题
	private String formNumber;           //表单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDate;               //日期
	private Integer shift;               //班次	(数据字典：dc_shift, 1:早 2:中 3:晚)
	private Integer weather;             //天气	(数据字典：dc_weather, 1:晴  2:阴  3:多云  4:小雨 5:中雨 6:大雨 7:暴雨 8:雷阵雨  9:霜冻 10:雾  11:台风)
	private String thisWatcher;     	 //本班次值班人员  字典：dc_headOfDuty
	@DateTimeFormat(pattern="HH:mm")
	private Date watchTimeStart;         //值班时间Start
	@DateTimeFormat(pattern="HH:mm")
	private Date watchTimeEnd;           //值班时间End
	private String laseWatcher;     	 //上班次值班人员  字典：dc_headOfDuty
	@DateTimeFormat(pattern="HH:mm")
	private Date handoverTime;           //交接时间
	private String handoverMatters;      //交接事项
	private String exception;            //接班异常情况
	private String ttId;                 //主表id
	
	
	@Column(name = "watch_Time_Start")
	public Date getWatchTimeStart() {
		return watchTimeStart;
	}
	public void setWatchTimeStart(Date watchTimeStart) {
		this.watchTimeStart = watchTimeStart;
	}
	
	@Column(name = "watch_Time_End")
	public Date getWatchTimeEnd() {
		return watchTimeEnd;
	}
	public void setWatchTimeEnd(Date watchTimeEnd) {
		this.watchTimeEnd = watchTimeEnd;
	}
	
	@Column(name = "handover_Time")
	public Date getHandoverTime() {
		return handoverTime;
	}
	public void setHandoverTime(Date handoverTime) {
		this.handoverTime = handoverTime;
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
	
	@Column(name = "shift_", length=11)
	public Integer getShift() {
		return shift;
	}
	public void setShift(Integer shift) {
		this.shift = shift;
	}
	
	@Column(name = "weather_", length=11)
	public Integer getWeather() {
		return weather;
	}
	public void setWeather(Integer weather) {
		this.weather = weather;
	}

	@Column(name = "this_Watcher", length=20)
	public String getThisWatcher() {
		return thisWatcher;
	}
	public void setThisWatcher(String thisWatcher) {
		this.thisWatcher = thisWatcher;
	}
	
	@Column(name = "lase_Watcher", length=20)
	public String getLaseWatcher() {
		return laseWatcher;
	}
	public void setLaseWatcher(String laseWatcher) {
		this.laseWatcher = laseWatcher;
	}
	
	@Column(name = "handover_Matters", length=200)
	public String getHandoverMatters() {
		return handoverMatters;
	}
	public void setHandoverMatters(String handoverMatters) {
		this.handoverMatters = handoverMatters;
	}
	
	@Column(name = "exception_", length=200)
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	
	@Column(name = "ttId", length=32)
	public String getTtId() {
		return ttId;
	}
	public void setTtId(String ttId) {
		this.ttId = ttId;
	}
	
	
}
