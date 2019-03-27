package com.safecheck.hiddenDanger.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * @Description 经办事件表
 * @author xuezb
 * @Date 2018年6月6日
 */
@Entity
@Table(name = "EVENT_HANDLE")
public class EventHandle extends BaseModule{

	private String eventId;			//事件ID(对应Event_Info表的ID)

	private String epNowNode;		//当前节点(节点编码)
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date epNowNodeArriveTime;	//到达时间(当前节点)
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date epNowNodeLeavleTime;	//离开时间(当前节点)
	private String epNowRole;			//处理角色(当前节点)——角色编码
	private String epNowRoleName;		//处理角色名称(当前节点)
	private String epNowPersonId;		//处理人(当前节点)——用户ID
	private String epNowPersonName;		//处理人姓名(当前节点)	
	
	private String eventCode;	//事件编码/流水号 例:ET201805231048520001		组成规则:ET+年月日时分秒+当天的第几个事件(四位,如0001是当天的第一个事件)
	private String eventTitle;	//事件标题
	private Integer eventurgency;	//紧急程度（1一般，2紧急，3特急）
	private Integer	eventType;	//事件类型(1公共安全，2城市管理，3消费安全···)
	
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date eventReportTime;		//事件上报事件
	private String eventReportName;	//事件上报人姓名
	
	
	@Column(name = "EventInfo_ID", length = 32)
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	@Column(name = "Ep_NowNode", length = 30)
	public String getEpNowNode() {
		return epNowNode;
	}
	public void setEpNowNode(String epNowNode) {
		this.epNowNode = epNowNode;
	}
	
	@Column(name = "Ep_NowNodeArriveTime")
	public Date getEpNowNodeArriveTime() {
		return epNowNodeArriveTime;
	}
	public void setEpNowNodeArriveTime(Date epNowNodeArriveTime) {
		this.epNowNodeArriveTime = epNowNodeArriveTime;
	}
	
	@Column(name = "Ep_NowNodeLeavleTime")
	public Date getEpNowNodeLeavleTime() {
		return epNowNodeLeavleTime;
	}
	public void setEpNowNodeLeavleTime(Date epNowNodeLeavleTime) {
		this.epNowNodeLeavleTime = epNowNodeLeavleTime;
	}
	
	@Column(name = "Ep_NowRole", length = 30)
	public String getEpNowRole() {
		return epNowRole;
	}
	public void setEpNowRole(String epNowRole) {
		this.epNowRole = epNowRole;
	}
	
	@Column(name = "Ep_NowRoleName", length = 32)
	public String getEpNowRoleName() {
		return epNowRoleName;
	}
	public void setEpNowRoleName(String epNowRoleName) {
		this.epNowRoleName = epNowRoleName;
	}
	
	@Column(name = "Ep_NowPersonId", length = 32)
	public String getEpNowPersonId() {
		return epNowPersonId;
	}
	public void setEpNowPersonId(String epNowPersonId) {
		this.epNowPersonId = epNowPersonId;
	}
	
	@Column(name = "Ep_NowPersonName", length = 32)
	public String getEpNowPersonName() {
		return epNowPersonName;
	}
	public void setEpNowPersonName(String epNowPersonName) {
		this.epNowPersonName = epNowPersonName;
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
	
	@Column(name = "EVENT_REPORTTIME")
	public Date getEventReportTime() {
		return eventReportTime;
	}
	public void setEventReportTime(Date eventReportTime) {
		this.eventReportTime = eventReportTime;
	}
	
	@Column(name = "EVENT_REPORTNAME", length = 32)
	public String getEventReportName() {
		return eventReportName;
	}
	public void setEventReportName(String eventReportName) {
		this.eventReportName = eventReportName;
	}
	
}
