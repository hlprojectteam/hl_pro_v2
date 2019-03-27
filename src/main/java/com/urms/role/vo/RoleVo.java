package com.urms.role.vo;

import com.urms.role.module.Role;


public class RoleVo extends Role{

	private String orgFrameNames;//父节点 组织架构名称

	
	public String getOrgFrameNames() {
		return orgFrameNames;
	}
	public void setOrgFrameNames(String orgFrameNames) {
		this.orgFrameNames = orgFrameNames;
	}
	
}
