package com.safecheck.hiddenDanger.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.common.base.module.BaseModule;

/**
 * @Description 事件表
 * @author xuezb
 * @Date 2018年5月23日
 */
@Entity
@Table(name = "EVENT_INFO")
public class EventInfo extends BaseModule{
	
	private String eventCode;		//事件编码/流水号 例:ET201805231048520001		组成规则:ET+年月日时分秒+当天的第几个事件(四位,如0001是当天的第一个事件)
	private String eventTitle;		//事件标题
	private Integer eventurgency;	//紧急程度（1一般，2紧急，3特急）
	private Integer	eventType;		//事件类型(1公共安全，2城市管理，3消费安全···)
	private String eventContent;	//事件详情(500个字符)
	
	private String eventAddress;	//事发地址(具体详细地址)
	private Double eventGPSX;		//事件X上报坐标(经度)
	private Double eventGPSY;		//事件Y上报坐标(纬度)
	
	private String contactPhone;	//联系电话
	private Integer dataSource;		//数据来源(1电脑端，2移动APP，3微信，4其它)
	
	private String reporterOrgId;	//上报人所在部门Id
	
	
	@Column(name = "reporter_OrgId", length = 32)
	public String getReporterOrgId() {
		return reporterOrgId;
	}
	public void setReporterOrgId(String reporterOrgId) {
		this.reporterOrgId = reporterOrgId;
	}
	
	@Column(name = "EVENT_CODE", length = 30)
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	
	@Column(name = "EVENT_TITLE", length = 200)
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	
	@Column(name = "EVENT_URGENCY", length = 11)
	public Integer getEventurgency() {
		return eventurgency;
	}
	public void setEventurgency(Integer eventurgency) {
		this.eventurgency = eventurgency;
	}
	
	@Column(name = "EVENT_TYPE", length = 11)
	public Integer getEventType() {
		return eventType;
	}
	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}
	@Column(name = "EVENT_ADDRESS", length = 100)
	public String getEventAddress() {
		return eventAddress;
	}
	public void setEventAddress(String eventAddress) {
		this.eventAddress = eventAddress;
	}
	
	@Column(name = "EVENT_CONTENT", length = 500)
	public String getEventContent() {
		return eventContent;
	}
	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}
	
	@Column(name = "EVENT_GPSX")
	public Double getEventGPSX() {
		return eventGPSX;
	}
	public void setEventGPSX(Double eventGPSX) {
		this.eventGPSX = eventGPSX;
	}
	
	@Column(name = "EVENT_GPSY")
	public Double getEventGPSY() {
		return eventGPSY;
	}
	public void setEventGPSY(Double eventGPSY) {
		this.eventGPSY = eventGPSY;
	}
	
	@Column(name = "CONTACT_PHONE", length = 20)
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	
	@Column(name = "DATA_SOURCE", length = 10)
	public Integer getDataSource() {
		return dataSource;
	}
	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
	}
	
}
