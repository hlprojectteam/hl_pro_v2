package com.urms.user.vo;

import com.urms.user.module.User;

public class UserVo extends User{
	
	private String orgFrameNames;//父节点 组织架构名称
	
	private String orgFrameId;//组织架构id
	private String orgFrameName;//组织架构名称

	
	public String getOrgFrameNames() {
		return orgFrameNames;
	}
	public void setOrgFrameNames(String orgFrameNames) {
		this.orgFrameNames = orgFrameNames;
	}
	public String getOrgFrameId() {
		return orgFrameId;
	}
	public void setOrgFrameId(String orgFrameId) {
		this.orgFrameId = orgFrameId;
	}
	public String getOrgFrameName() {
		return orgFrameName;
	}
	public void setOrgFrameName(String orgFrameName) {
		this.orgFrameName = orgFrameName;
	}
	
}
