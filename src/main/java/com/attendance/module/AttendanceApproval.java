package com.attendance.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 考勤审批表
 * @author qinyongqian
 * @date 2019年5月17日
 *
 */
@Entity
@Table(name = "KQ_ATTENDANCE_APPROVAL")
public class AttendanceApproval extends BaseModule{
	
	private String approvalUserId;// 审批人Id 
	private String approvalRecordId;// 审批记录Id
	private String approvalContent;// 审批意见内容
	private Integer approvalResult;// 审批结果 1通过 0 不通过
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date readTime;// 读取时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date approvalTime;// 审批时间
	private Integer approvalType;// 审批分类  字典：Approval_Classification 1换班，2请假
	
	@Column(name = "APPROVAL_USER_ID", length = 32)
	public String getApprovalUserId() {
		return approvalUserId;
	}
	public void setApprovalUserId(String approvalUserId) {
		this.approvalUserId = approvalUserId;
	}
	@Column(name = "APPROVAL_CONTENT", length = 30)
	public String getApprovalContent() {
		return approvalContent;
	}
	public void setApprovalContent(String approvalContent) {
		this.approvalContent = approvalContent;
	}
	@Column(name = "APPROVAL_RESULT", length = 2)
	public Integer getApprovalResult() {
		return approvalResult;
	}
	public void setApprovalResult(Integer approvalResult) {
		this.approvalResult = approvalResult;
	}
	@Column(name = "READ_TIME")
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	@Column(name = "APPROVAL_TIME")
	public Date getApprovalTime() {
		return approvalTime;
	}
	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}
	@Column(name = "APPROVAL_TYPE", length = 2)
	public Integer getApprovalType() {
		return approvalType;
	}
	public void setApprovalType(Integer approvalType) {
		this.approvalType = approvalType;
	}
	@Column(name = "APPROVAL_RECORD_ID", length = 32)
	public String getApprovalRecordId() {
		return approvalRecordId;
	}
	public void setApprovalRecordId(String approvalRecordId) {
		this.approvalRecordId = approvalRecordId;
	}
	
	
	

}
