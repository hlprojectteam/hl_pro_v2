package com.attendance.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 请假表
 * @author qinyongqian
 * @date 2019年5月17日
 *
 */
@Entity
@Table(name = "KQ_LEAVE")
public class Leave extends BaseModule{
	
	private String approvalNumber;// 审批编号，记录生成的唯一编号，为时间+审批类别+随机3位数 格式如 yyyymmddhhmmss0101 
	private String leaveType;// 请假类别  数据字典key:Leave_Type 存在多个值用“,”隔开，如“事假,产假”
	private String userId;// 请假人ID 
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;// 开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;// 结束时间
	private double timeLength;//时长
	private String leaveReason;// 请假理由 
	private String outAddress;// 外出地址
	private Integer approvalStatus; //审批状态  字典：Approval_Status 0未审批；1审批中；2审批通过；3审批不通过；
	private String FryPersonIds; //抄送人ID 存在多个值用“,”隔开，最多不多于5人
	
	@Column(name = "LEAVE_TYPE", length = 30)
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	@Column(name = "USER_ID", length = 32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Column(name = "TIME_LENGTH")
	public double getTimeLength() {
		return timeLength;
	}
	public void setTimeLength(double timeLength) {
		this.timeLength = timeLength;
	}
	@Column(name = "LEAVE_REASON", length = 30)
	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	@Column(name = "OUT_ADDRESS", length = 30)
	public String getOutAddress() {
		return outAddress;
	}
	public void setOutAddress(String outAddress) {
		this.outAddress = outAddress;
	}
	@Column(name = "APPROVAL_STATUS", length = 2)
	public Integer getApprovalStatus() {
		return approvalStatus;
	}
	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	@Column(name = "FRY_PERSON_IDS", length = 200)
	public String getFryPersonIds() {
		return FryPersonIds;
	}
	public void setFryPersonIds(String fryPersonIds) {
		FryPersonIds = fryPersonIds;
	}
	@Column(name = "APPROVAL_NUMBER", length = 20)
	public String getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	
	

}
