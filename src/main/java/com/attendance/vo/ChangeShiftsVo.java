package com.attendance.vo;

import java.util.Date;

import com.attendance.module.ChangeShifts;

/**
 * 
 * @Description 换班VO
 * @author qinyongqian
 * @date 2019年5月19日
 *
 */
public class ChangeShiftsVo extends ChangeShifts{
	
	private String approvalUserId;// 审批人ID
	private String changeDateStr;// 调班日期
	private String approvalUserName;// 审批人
	private String approvalUserAvatar;// 审批人头像路径
	private String approvalContent;// 审批意见内容
	private Date readTime;// 阅读时间
	private Date approvalTime;// 审批时间
	private String applyMenName;// 申请人名称 
	private String beApplyMenName;// 被调人名称 
	private String creatorOrgName;// 申请人部门
	private String creatorOrgId;// 申请人部门Id
	private String creatorAvatar;// 申请人头像路径
	
	public String getCreatorOrgName() {
		return creatorOrgName;
	}
	public void setCreatorOrgName(String creatorOrgName) {
		this.creatorOrgName = creatorOrgName;
	}
	public String getCreatorOrgId() {
		return creatorOrgId;
	}
	public void setCreatorOrgId(String creatorOrgId) {
		this.creatorOrgId = creatorOrgId;
	}
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
	public String getChangeDateStr() {
		return changeDateStr;
	}
	public void setChangeDateStr(String changeDateStr) {
		this.changeDateStr = changeDateStr;
	}
	public String getApplyMenName() {
		return applyMenName;
	}
	public void setApplyMenName(String applyMenName) {
		this.applyMenName = applyMenName;
	}
	public String getBeApplyMenName() {
		return beApplyMenName;
	}
	public void setBeApplyMenName(String beApplyMenName) {
		this.beApplyMenName = beApplyMenName;
	}
	public String getCreatorAvatar() {
		return creatorAvatar;
	}
	public void setCreatorAvatar(String creatorAvatar) {
		this.creatorAvatar = creatorAvatar;
	}
	public String getApprovalContent() {
		return approvalContent;
	}
	public void setApprovalContent(String approvalContent) {
		this.approvalContent = approvalContent;
	}


}
