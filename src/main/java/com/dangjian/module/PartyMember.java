package com.dangjian.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 党员
 * @author qinyongqian
 * @date 2018年12月2日
 *
 */
@Entity
@Table(name="P_DJ_PARTYMEMBER")
public class PartyMember  extends BaseModule{
	
	private Integer memberDuty;     //党员头衔 字典key:dj_partyMember_duty
	private String duties ;		//职务描述，分工
	@DateTimeFormat(pattern="yyyy-MM-dd")	
	private Date joininTime;        //入党时间
	@DateTimeFormat(pattern="yyyy-MM-dd")	
	private Date changeRegularTime;        //转正时间
	@DateTimeFormat(pattern="yyyy-MM-dd")	
	private Date increaseTime;        //增加时间
	private Integer increaseType;     //党员增加方式 字典key:memberIncreaseType
	private Integer points;		    //积分
	private String orgRelationshipUnit;		    //组织关系所在单位
	private String branchId;		//所属支部ID
	private String userId ;		    //关联用户ID
	private String photo ;		    //头像照片路径
	
	
	@Column(name = "MEMBER_DUTY" ,length=4)
	public Integer getMemberDuty() {
		return memberDuty;
	}
	public void setMemberDuty(Integer memberDuty) {
		this.memberDuty = memberDuty;
	}
	@Column(name = "DUTIES",length=1000)
	public String getDuties() {
		return duties;
	}
	public void setDuties(String duties) {
		this.duties = duties;
	}
	@Column(name = "JOININ_TIME")
	public Date getJoininTime() {
		return joininTime;
	}
	public void setJoininTime(Date joininTime) {
		this.joininTime = joininTime;
	}
	@Column(name = "CHANGE_REGULAR_TIME")
	public Date getChangeRegularTime() {
		return changeRegularTime;
	}
	public void setChangeRegularTime(Date changeRegularTime) {
		this.changeRegularTime = changeRegularTime;
	}
	@Column(name = "INCREASE_TIME")
	public Date getIncreaseTime() {
		return increaseTime;
	}
	public void setIncreaseTime(Date increaseTime) {
		this.increaseTime = increaseTime;
	}
	@Column(name = "INCREASE_TYPE" ,length=4)
	public Integer getIncreaseType() {
		return increaseType;
	}
	public void setIncreaseType(Integer increaseType) {
		this.increaseType = increaseType;
	}
	@Column(name = "POINTS" ,length=4)
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	@Column(name = "BRANCH_ID",length=32)
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	@Column(name = "Photo" ,length=200)
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	@Column(name = "USER_ID" ,length=32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name = "ORG_RELATION_UNIT" ,length=30)
	public String getOrgRelationshipUnit() {
		return orgRelationshipUnit;
	}
	public void setOrgRelationshipUnit(String orgRelationshipUnit) {
		this.orgRelationshipUnit = orgRelationshipUnit;
	}
	

	
}
