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
 * @Description 工作简报
 * @author xuezb
 * @date 2019年2月15日
 */
@Entity
@Table(name="dc_Brief")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Brief extends BaseModule{
	
	private String title;					//标题
	private String formNumber;              //表单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDate;                  //日期
	private String cwfzjl;                  //常务副总经理
	private String zgfzjl;                  //分管领导
	private String zxfzr;                   //中心副主任
	private String fhry;                    //复核人员
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private Date riseTime;                  //简报生成时间(打印文件时的当前时间)

	private String operatingData;           //营运数据
	private String trafficOperation;        //交通运行情况
	private String equipmentOperation;      //设备运行情况
	private String ttId;                    //主表id

	private Integer status;					//状态  0初始 1修订
	
	
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
	
	@Column(name = "cwfzjl_", length=20)
	public String getCwfzjl() {
		return cwfzjl;
	}
	public void setCwfzjl(String cwfzjl) {
		this.cwfzjl = cwfzjl;
	}
	
	@Column(name = "zgfzjl_", length=20)
	public String getZgfzjl() {
		return zgfzjl;
	}
	public void setZgfzjl(String zgfzjl) {
		this.zgfzjl = zgfzjl;
	}
	
	@Column(name = "zxfzr_", length=20)
	public String getZxfzr() {
		return zxfzr;
	}
	public void setZxfzr(String zxfzr) {
		this.zxfzr = zxfzr;
	}
	
	@Column(name = "fhry_", length=20)
	public String getFhry() {
		return fhry;
	}
	public void setFhry(String fhry) {
		this.fhry = fhry;
	}
	
	@Column(name = "rise_Time")
	public Date getRiseTime() {
		return riseTime;
	}
	public void setRiseTime(Date riseTime) {
		this.riseTime = riseTime;
	}
	
	@Column(name = "operating_Data", length=500)
	public String getOperatingData() {
		return operatingData;
	}
	public void setOperatingData(String operatingData) {
		this.operatingData = operatingData;
	}
	
	@Column(name = "traffic_Operation", length=800)
	public String getTrafficOperation() {
		return trafficOperation;
	}
	public void setTrafficOperation(String trafficOperation) {
		this.trafficOperation = trafficOperation;
	}
	
	@Column(name = "equipment_Operation", length=1000)
	public String getEquipmentOperation() {
		return equipmentOperation;
	}
	public void setEquipmentOperation(String equipmentOperation) {
		this.equipmentOperation = equipmentOperation;
	}
	
	@Column(name = "ttId", length=32)
	public String getTtId() {
		return ttId;
	}
	public void setTtId(String ttId) {
		this.ttId = ttId;
	}

	@Column(name = "status_")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
