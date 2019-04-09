package com.dangjian.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 活动表开展
 * @author qinyongqian
 * @date 2018年12月2日
 *
 */
@Entity
@Table(name="P_DJ_ACTIVITIES_LAUNCH")
public class ActivitiesLaunch extends BaseModule{
	
	private String branchId ;		    //开展的支部ID
	private String activityId ;		    //开展的活动ID
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date launchDate;		    //开展时间
	private String launchAddress ;		//开展的活动地点
	private String launchContent ;		//开展的活动描述
	private Integer points;             //开展活动的得分
	private Integer status;             //活动的评审状态 字典：dj_activity_status  0未评审 1初审 2复审 3审定通过 4审定不通过（只有亮点工作才要评审）
	
	@Column(name = "BRANCH_ID" ,length=32)
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	@Column(name = "ACTIVITY_ID" ,length=32)
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	@Column(name = "LAUNCH_DATE")
	public Date getLaunchDate() {
		return launchDate;
	}
	public void setLaunchDate(Date launchDate) {
		this.launchDate = launchDate;
	}
	@Column(name = "LAUNCH_CONTENT" ,length=1000)
	public String getLaunchContent() {
		return launchContent;
	}
	public void setLaunchContent(String launchContent) {
		this.launchContent = launchContent;
	}
	@Column(name = "LAUNCH_ADDRESS" ,length=100)
	public String getLaunchAddress() {
		return launchAddress;
	}
	public void setLaunchAddress(String launchAddress) {
		this.launchAddress = launchAddress;
	}
	@Column(name = "LAUNCH_POINTS" ,length=2)
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	@Column(name = "LAUNCH_STATUS" ,length=2)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}
