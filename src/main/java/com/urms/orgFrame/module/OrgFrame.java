package com.urms.orgFrame.module;


import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.common.base.module.BaseModule;
import com.urms.role.module.Role;
import com.urms.user.module.User;

@Entity
@Table(name="UM_ORGFRAME")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
public class OrgFrame extends BaseModule{
	private String pId;//父id
	private String orgFrameName;
	private String orgFrameCode;
	private String pIds;//所有父idfu
	private String pNames;//所有父name
	private Integer isLeaf;//是否叶节点
	private Integer order;//排序
	private Integer level;//层数
	
	private Set<User> users = new TreeSet<User>();
	private Set<Role> roles = new TreeSet<Role>();
	
	@Column(name = "P_ID", length=32)
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	@Column(name = "ORG_FRAME_NAME", length=128)
	public String getOrgFrameName() {
		return orgFrameName;
	}
	public void setOrgFrameName(String orgFrameName) {
		this.orgFrameName = orgFrameName;
	}
	@Column(name = "ORG_FRAME_CODE", length=128)
	public String getOrgFrameCode() {
		return orgFrameCode;
	}
	public void setOrgFrameCode(String orgFrameCode) {
		this.orgFrameCode = orgFrameCode;
	}
	@Column(name = "P_IDS", length=1024)
	public String getpIds() {
		return pIds;
	}
	public void setpIds(String pIds) {
		this.pIds = pIds;
	}
	@Column(name = "P_NAMES", length=1024)
	public String getpNames() {
		return pNames;
	}
	public void setpNames(String pNames) {
		this.pNames = pNames;
	}
	@Column(name = "IS_LEAF")
	public Integer getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
	@Column(name = "ORDER_")
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	@Column(name = "LEVEL_")
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	@OneToMany(cascade=CascadeType.MERGE,mappedBy="orgFrame",fetch=FetchType.LAZY)
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	@OneToMany(cascade=CascadeType.MERGE,mappedBy="orgFrame",fetch=FetchType.LAZY)
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
}

