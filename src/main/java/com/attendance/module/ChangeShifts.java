package com.attendance.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 换班表
 * @author qinyongqian
 * @date 2019年5月17日
 *
 */
@Entity
@Table(name = "KQ_CHANGE_SHIFTS")
public class ChangeShifts extends BaseModule{
	
	private String approvalNumber;// 审批编号，记录生成的唯一编号，为时间+审批类别+随机3位数 格式如 yyyymmddhhmmss0101 
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date changeDate;// 调班日期
	private String applyMenShift; //申请人班次
	private String beApplyMenShift; //被调人班次
	
	private String applyMenId;// 申请人ID 
	private String beApplyMenId;// 被调人ID 
	
	private String changeType;// 调班方式  数据字典key:changeShifts_Type 
	private String changeReason;// 调班理由 
	
	private Integer approvalStatus; //审批状态  字典：Approval_Status 0未审批；1审批中；2审批通过；3审批不通过；
	private String fryPersonIds; //抄送人ID 存在多个值用“,”隔开，最多不多于5人
	
	@Column(name = "APPROVAL_NUMBER", length = 20)
	public String getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
	@Column(name = "CHANGE_DATE")
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	@Column(name = "APPLY_MEN_SHIFT", length = 32)
	public String getApplyMenShift() {
		return applyMenShift;
	}
	public void setApplyMenShift(String applyMenShift) {
		this.applyMenShift = applyMenShift;
	}
	@Column(name = "BE_APPLY_MEN_SHIFT", length = 32)
	public String getBeApplyMenShift() {
		return beApplyMenShift;
	}
	public void setBeApplyMenShift(String beApplyMenShift) {
		this.beApplyMenShift = beApplyMenShift;
	}
	@Column(name = "APPLY_MEN_ID", length = 32)
	public String getApplyMenId() {
		return applyMenId;
	}
	public void setApplyMenId(String applyMenId) {
		this.applyMenId = applyMenId;
	}
	@Column(name = "BE_APPLY_MEN_ID", length = 32)
	public String getBeApplyMenId() {
		return beApplyMenId;
	}
	public void setBeApplyMenId(String beApplyMenId) {
		this.beApplyMenId = beApplyMenId;
	}
	@Column(name = "CHANGE_TYPE", length = 10)
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	@Column(name = "CHANGE_REASON", length = 100)
	public String getChangeReason() {
		return changeReason;
	}
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
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
		return fryPersonIds;
	}
	public void setFryPersonIds(String fryPersonIds) {
		this.fryPersonIds = fryPersonIds;
	}
	
	

}
