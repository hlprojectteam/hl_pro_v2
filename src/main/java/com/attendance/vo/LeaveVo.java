package com.attendance.vo;

import java.util.Date;

import com.attendance.module.Leave;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年5月19日
 *
 */
public class LeaveVo extends Leave{
	
	private String startTimeStr;// 开始时间
	private String endTimeStr;// 结束时间
	private String approvalUserId;// 审批人ID
	private String approvalUserName;// 审批人
	private String approvalUserAvatar;// 审批人头像路径
	private Date readTime;// 阅读时间
	private Date approvalTime;// 审批时间
	
	public String getApprovalUserId() {
		return approvalUserId;
	}
	public void setApprovalUserId(String approvalUserId) {
		this.approvalUserId = approvalUserId;
	}
	public String getApprovalUserName() {
		return approvalUserName;
	}
	public void setApprovalUserName(String approvalUserName) {
		this.approvalUserName = approvalUserName;
	}
	public String getStartTimeStr() {
		return startTimeStr;
	}
	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}
	public String getEndTimeStr() {
		return endTimeStr;
	}
	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	public Date getApprovalTime() {
		return approvalTime;
	}
	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}
	public String getApprovalUserAvatar() {
		return approvalUserAvatar;
	}
	public void setApprovalUserAvatar(String approvalUserAvatar) {
		this.approvalUserAvatar = approvalUserAvatar;
	}


}
