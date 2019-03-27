package com.urms.menu.module;


import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.common.base.module.BaseModule;
import com.urms.roleRight.module.RoleRight;
import com.urms.subsystem.module.Subsystem;

@Entity
@Table(name="UM_MENU")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
public class Menu extends BaseModule{
	private String pId;//父id
	private String menuName;
	private String menuCode;
	private String pIds;//所有父idfu
	private String pNames;//所有父name
	private Integer isLeaf;//是否叶节点
	private Integer order;//排序
	private String url;//访问的url地址
	private Integer level;//层数
	private String icon;//图标
	private Integer menuType;//菜单类型 1：超管菜单  2：系统菜单 3:地图菜单
	private Integer state;//状态

	private Set<RoleRight> roleRights = new TreeSet<RoleRight>();
	private Set<Subsystem> subsystems = new TreeSet<Subsystem>();
	
	private Set<MenuDefinition> menuDefinitions = new TreeSet<MenuDefinition>();
	
	@Column(name = "MENU_NAME", length=128)
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	@Column(name = "MENU_CODE", length=128)
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	@Column(name = "ORDER_")
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	@Column(name = "P_ID", length=32)
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	@Column(name = "P_IDS", length=1000)
	public String getpIds() {
		return pIds;
	}
	public void setpIds(String pIds) {
		this.pIds = pIds;
	}
	@Column(name = "P_NAMES", length=1000)
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
	@Column(name = "URL_",length=1000)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name = "LEVEL_")
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	@Column(name = "ICON_", length=128)
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Column(name = "MENU_TYPE")
	public Integer getMenuType() {
		return menuType;
	}
	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}
	@Column(name = "STATE_")
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@OneToMany(cascade=CascadeType.MERGE,mappedBy="menu",fetch=FetchType.LAZY)
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
	public Set<RoleRight> getRoleRights() {
		return roleRights;
	}
	public void setRoleRights(Set<RoleRight> roleRights) {
		this.roleRights = roleRights;
	}
	@ManyToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY)    
    @JoinTable(name = "um_subsystem_menu", joinColumns = { @JoinColumn(name = "menu_Id") }, inverseJoinColumns = { @JoinColumn(name = "subsystem_Id") }) 
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
	public Set<Subsystem> getSubsystems() {
		return subsystems;
	}
	public void setSubsystems(Set<Subsystem> subsystems) {
		this.subsystems = subsystems;
	}
	@OneToMany(cascade=CascadeType.MERGE,mappedBy="menu",fetch=FetchType.LAZY)
	@OrderBy("order asc")
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
	public Set<MenuDefinition> getMenuDefinitions() {
		return menuDefinitions;
	}
	public void setMenuDefinitions(Set<MenuDefinition> menuDefinitions) {
		this.menuDefinitions = menuDefinitions;
	}
	
}
