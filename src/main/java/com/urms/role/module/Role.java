package com.urms.role.module;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import com.common.base.module.BaseModule;
import com.urms.menu.module.MenuDefinition;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.roleRight.module.RoleRight;
import com.urms.user.module.User;

@Entity
@Table(name="UM_ROLE")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
public class Role extends BaseModule{
	private String roleName;
	private String roleCode;
	
	private OrgFrame orgFrame;
	private Set<User> users = new TreeSet<User>();
	
	private Set<RoleRight> roleRights = new TreeSet<RoleRight>();
	private Set<MenuDefinition> menuDefinitions = new TreeSet<MenuDefinition>();
	
	@Column(name = "ROLE_NAME", length=128)
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	@Column(name = "ROLE_CODE", length=128)
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	@ManyToMany   
    @JoinTable(name = "um_user_role", joinColumns = { @JoinColumn(name = "role_Id") }, inverseJoinColumns = { @JoinColumn(name = "user_Id") }) 
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	@OneToMany(cascade=CascadeType.MERGE,mappedBy="role",fetch=FetchType.LAZY)
	public Set<RoleRight> getRoleRights() {
		return roleRights;
	}
	public void setRoleRights(Set<RoleRight> roleRights) {
		this.roleRights = roleRights;
	}
	@ManyToOne(cascade={CascadeType.MERGE})           
    @JoinColumn(name="ORGFRAME_ID") 
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
	public OrgFrame getOrgFrame() {
		return orgFrame;
	}
	public void setOrgFrame(OrgFrame orgFrame) {
		this.orgFrame = orgFrame;
	}
	@ManyToMany(cascade={CascadeType.REFRESH})
    @JoinTable(name = "UM_ROLE_MENU_DEFINITION", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "MENU_DEFINITION_Id") })  
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	@Cacheable(value="urms_cache")
	public Set<MenuDefinition> getMenuDefinitions() {
		return menuDefinitions;
	}
	public void setMenuDefinitions(Set<MenuDefinition> menuDefinitions) {
		this.menuDefinitions = menuDefinitions;
	}
	
}
