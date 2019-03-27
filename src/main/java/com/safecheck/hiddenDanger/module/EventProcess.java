 package com.safecheck.hiddenDanger.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * @Description 事件过程表
 * @author xuezb
 * @Date 2018年5月23日
 */
@Entity
@Table(name = "EVENT_PROCESS")
public class EventProcess extends BaseModule{
	
	private String eventId;			//事件ID(对应Event_Info表的ID)
	
	private String epUpNode;		//上一节点(节点编码)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date epUpNodeArriveTime;	//到达时间(上一节)
	private String epUpRole;		//处理角色(上一节点)——角色编码
	private String epUpRoleName;	//处理角色名称(上一节点)
	private String epUpPersonId;	//处理人(上一节点)——用户ID
	private String epUpPersonName;	//处理人姓名(上一节点)

	private String epNowNode;		//当前节点(节点编码)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date epNowNodeArriveTime;	//到达时间(当前节点)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date epNowNodeLeavleTime;	//离开时间(当前节点)
	private String epNowRole;			//处理角色(当前节点)——角色编码
	private String epNowRoleName;		//处理角色名称(当前节点)
	private String epNowPersonId;		//处理人(当前节点)——用户ID
	private String epNowPersonName;		//处理人姓名(当前节点)
	
	private String epNextNode;			//下一节点(节点编码)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date epNextNodeLeavleTime;	//离开时间(下一节点)
	private String epNextRole;			//处理角色(下一节点)——角色编码
	private String epNextRoleName;		//处理角色名称(下一节点)
	private String epNextPersonId;		//处理人(下一节点)——用户ID
	private String epNextPersonName;	//处理人姓名(当前节点)
	
	private String epAttachId;			//附件ID	 (最多7个附件)
	private Integer epDealState;		//处理状态 (0关闭，1未处理，2处理中，3处理完)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date epLimitTime;			//处理时限 (超时时限)
	private Integer epDealWay;			//处理端(1电脑，2APP，3微信，4其它)
	private String epDealContent;		//处理意见/评价
	private Integer epWhetherFinish;	//是否办结
	private Integer epAppraise;			//满意度(1非常满意，2满意，3一般，4不满意)
	
	private Integer epIsSite;				//是否本站处理
	private Integer epIsReturned;			//是否退回
	private String epReturnReason;			//退回理由
	
	
	
	@Column(name = "Ep_WhetherFinish", length = 11)
	public Integer getEpWhetherFinish() {
		return epWhetherFinish;
	}
	public void setEpWhetherFinish(Integer epWhetherFinish) {
		this.epWhetherFinish = epWhetherFinish;
	}
	
	@Column(name = "Ep_UpRoleName", length = 32)
	public String getEpUpRoleName() {
		return epUpRoleName;
	}
	public void setEpUpRoleName(String epUpRoleName) {
		this.epUpRoleName = epUpRoleName;
	}
	
	@Column(name = "Ep_UpPersonName", length = 32)
	public String getEpUpPersonName() {
		return epUpPersonName;
	}
	public void setEpUpPersonName(String epUpPersonName) {
		this.epUpPersonName = epUpPersonName;
	}
	
	@Column(name = "Ep_NowRoleName", length = 32)
	public String getEpNowRoleName() {
		return epNowRoleName;
	}
	public void setEpNowRoleName(String epNowRoleName) {
		this.epNowRoleName = epNowRoleName;
	}
	
	@Column(name = "Ep_NowPersonName", length = 32)
	public String getEpNowPersonName() {
		return epNowPersonName;
	}
	public void setEpNowPersonName(String epNowPersonName) {
		this.epNowPersonName = epNowPersonName;
	}
	
	@Column(name = "Ep_NextRoleName", length = 32)
	public String getEpNextRoleName() {
		return epNextRoleName;
	}
	public void setEpNextRoleName(String epNextRoleName) {
		this.epNextRoleName = epNextRoleName;
	}
	
	@Column(name = "Ep_NextPersonName", length = 32)
	public String getEpNextPersonName() {
		return epNextPersonName;
	}
	public void setEpNextPersonName(String epNextPersonName) {
		this.epNextPersonName = epNextPersonName;
	}
	
	@Column(name = "Ep_IsSite", length = 11)
	public Integer getEpIsSite() {
		return epIsSite;
	}
	public void setEpIsSite(Integer epIsSite) {
		this.epIsSite = epIsSite;
	}
	
	@Column(name = "Ep_IsReturned", length = 11)
	public Integer getEpIsReturned() {
		return epIsReturned;
	}
	public void setEpIsReturned(Integer epIsReturned) {
		this.epIsReturned = epIsReturned;
	}
	
	@Column(name = "Ep_ReturnReason", length = 200)
	public String getEpReturnReason() {
		return epReturnReason;
	}
	public void setEpReturnReason(String epReturnReason) {
		this.epReturnReason = epReturnReason;
	}
	
	@Column(name = "EventInfo_ID", length = 32)
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	@Column(name = "Ep_UpNode", length = 30)
	public String getEpUpNode() {
		return epUpNode;
	}
	public void setEpUpNode(String epUpNode) {
		this.epUpNode = epUpNode;
	}
	
	@Column(name = "Ep_UpNodeArriveTime")
	public Date getEpUpNodeArriveTime() {
		return epUpNodeArriveTime;
	}
	public void setEpUpNodeArriveTime(Date epUpNodeArriveTime) {
		this.epUpNodeArriveTime = epUpNodeArriveTime;
	}
	
	@Column(name = "Ep_UpRole", length = 30)
	public String getEpUpRole() {
		return epUpRole;
	}
	public void setEpUpRole(String epUpRole) {
		this.epUpRole = epUpRole;
	}
	
	@Column(name = "Ep_UpPersonId", length = 32)
	public String getEpUpPersonId() {
		return epUpPersonId;
	}
	public void setEpUpPersonId(String epUpPersonId) {
		this.epUpPersonId = epUpPersonId;
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
	
	@Column(name = "Ep_NowPersonId", length = 32)
	public String getEpNowPersonId() {
		return epNowPersonId;
	}
	public void setEpNowPersonId(String epNowPersonId) {
		this.epNowPersonId = epNowPersonId;
	}
	
	@Column(name = "Ep_NextNode", length = 30)
	public String getEpNextNode() {
		return epNextNode;
	}
	public void setEpNextNode(String epNextNode) {
		this.epNextNode = epNextNode;
	}
	
	@Column(name = "Ep_NextNodeLeavleTime")
	public Date getEpNextNodeLeavleTime() {
		return epNextNodeLeavleTime;
	}
	public void setEpNextNodeLeavleTime(Date epNextNodeLeavleTime) {
		this.epNextNodeLeavleTime = epNextNodeLeavleTime;
	}
	
	@Column(name = "Ep_NextRole", length = 30)
	public String getEpNextRole() {
		return epNextRole;
	}
	public void setEpNextRole(String epNextRole) {
		this.epNextRole = epNextRole;
	}
	
	@Column(name = "Ep_NextPersonId", length = 32)
	public String getEpNextPersonId() {
		return epNextPersonId;
	}
	public void setEpNextPersonId(String epNextPersonId) {
		this.epNextPersonId = epNextPersonId;
	}
	
	@Column(name = "Ep_AttachId", length = 256)
	public String getEpAttachId() {
		return epAttachId;
	}
	public void setEpAttachId(String epAttachId) {
		this.epAttachId = epAttachId;
	}
	
	@Column(name = "Ep_DealState", length = 11)
	public Integer getEpDealState() {
		return epDealState;
	}
	public void setEpDealState(Integer epDealState) {
		this.epDealState = epDealState;
	}
	
	@Column(name = "Ep_limitTime")
	public Date getEpLimitTime() {
		return epLimitTime;
	}
	public void setEpLimitTime(Date epLimitTime) {
		this.epLimitTime = epLimitTime;
	}
	
	@Column(name = "Ep_DealWay", length = 11)
	public Integer getEpDealWay() {
		return epDealWay;
	}
	public void setEpDealWay(Integer epDealWay) {
		this.epDealWay = epDealWay;
	}
	
	@Column(name = "Ep_DealContent", length = 500)
	public String getEpDealContent() {
		return epDealContent;
	}
	public void setEpDealContent(String epDealContent) {
		this.epDealContent = epDealContent;
	}
	
	@Column(name = "Ep_Appraise", length = 11)
	public Integer getEpAppraise() {
		return epAppraise;
	}
	public void setEpAppraise(Integer epAppraise) {
		this.epAppraise = epAppraise;
	}
	
}
