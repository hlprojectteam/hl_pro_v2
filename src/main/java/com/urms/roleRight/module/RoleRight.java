package com.urms.roleRight.module;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.common.base.module.BaseModule;
import com.urms.menu.module.Menu;
import com.urms.role.module.Role;

/**
 * @intruduction 角色权限
 * @author Mr.Wang
 * @Date 2016年1月14日上午9:10:02
 */
@Entity
@Table(name="UM_ROLERIGHT")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
public class RoleRight extends BaseModule{
	
	private Menu menu;
	private Role role;
	
	@ManyToOne(cascade={CascadeType.MERGE})           
    @JoinColumn(name="MENU_ID") 
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	@ManyToOne(cascade={CascadeType.MERGE})           
    @JoinColumn(name="ROLE_ID") 
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	
}
