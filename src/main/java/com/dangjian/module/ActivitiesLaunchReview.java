package com.dangjian.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 活动开展评审
 * @author qinyongqian
 * @date 2019年3月29日
 *
 */
@Entity
@Table(name="P_DJ_ACTIVITIES_LAUNCH_REVIEW")
public class ActivitiesLaunchReview extends BaseModule {
	
	private String userId ;		    //用户的ID
	private String activitiesLaunchId ;		    //开展的活动ID
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date reviewTime;		    //评审时间
	private String opinion ;		    //评审意见
	private Integer statusNode;             //评审状态节点   1初审 2复审
	private Integer isPass;             //是否通过 0不通过 1通过
	
	@Column(name = "USER_ID" ,length=32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name = "AC_LAUNCH_ID" ,length=32)
	public String getActivitiesLaunchId() {
		return activitiesLaunchId;
	}
	public void setActivitiesLaunchId(String activitiesLaunchId) {
		this.activitiesLaunchId = activitiesLaunchId;
	}
	@Column(name = "REVIEW_TIME")
	public Date getReviewTime() {
		return reviewTime;
	}
	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}
	@Column(name = "OPINION" ,length=100)
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	@Column(name = "IS_PASS" ,length=2)
	public Integer getIsPass() {
		return isPass;
	}
	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}
	@Column(name = "STATUS_NODE" ,length=2)
	public Integer getStatusNode() {
		return statusNode;
	}
	public void setStatusNode(Integer statusNode) {
		this.statusNode = statusNode;
	}
	
	
	

}
