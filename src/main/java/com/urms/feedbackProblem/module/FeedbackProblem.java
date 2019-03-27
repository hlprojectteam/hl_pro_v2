package com.urms.feedbackProblem.module;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.common.base.module.BaseModule;

/**
 * @intruduction 问题反馈
 * @author Mr.Wang
 * @Date 2016年6月6日上午9:29:41
 */

@Entity
@Table(name="UM_FEEDBACK_PROBLEM")
public class FeedbackProblem extends BaseModule{
	
	private Integer feedbackPeopleType;		// 反馈人类型（1.本人反馈，2.他人反馈）
	private String feedbackPeople;	// 反馈人姓名(如果反馈人类型（feedback_people_type）为1，则保存账号关联人姓名，若为2，则保存反馈人名字)
	private String phoneNumber;//反馈人联系方式（如果反馈人类型（feedback_people_type）为1，则不需保存直接带出，若为2，则保存）
	private Integer feedbackType;//反馈类型（1.问题反馈，2.意见建议）
	private String typeCode;//问题反馈所属菜单
	private String description;			//问题描述
	private Integer state;//状态（1.未处理，2.处理中，3.已处理）
	private Date submitTime;			//处理提交时间
	private String handlingSuggestion;		// 处理意见
	private String handlingResults;		//处理结果
	
	@Column(name = "FEEDBACK_PEOPLE_TYPE",length=2)
	public Integer getFeedbackPeopleType() {
		return feedbackPeopleType;
	}
	public void setFeedbackPeopleType(Integer feedbackPeopleType) {
		this.feedbackPeopleType = feedbackPeopleType;
	}
	@Column(name = "FEEDBACK_PEOPLE",length=10)
	public String getFeedbackPeople() {
		return feedbackPeople;
	}
	public void setFeedbackPeople(String feedbackPeople) {
		this.feedbackPeople = feedbackPeople;
	}
	@Column(name = "PHONE_NUMBER",length=15)
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	@Column(name = "FEEDBACK_TYPE",length=2)
	public Integer getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(Integer feedbackType) {
		this.feedbackType = feedbackType;
	}
	@Column(name = "TYPE_CODE",length=256)
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	@Column(name = "DESCRIPTION_",length=2000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "STATE_")
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@Column(name = "SUBMIT_TIME")
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	@Column(name = "HANDLING_SUGGESTION",length=256)
	public String getHandlingSuggestion() {
		return handlingSuggestion;
	}
	public void setHandlingSuggestion(String handlingSuggestion) {
		this.handlingSuggestion = handlingSuggestion;
	}
	@Column(name = "HANDLING_RESULTS",length=256)
	public String getHandlingResults() {
		return handlingResults;
	}
	public void setHandlingResults(String handlingResults) {
		this.handlingResults = handlingResults;
	}
	
}
