package com.urms.roleRight.service.impl;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.urms.menu.module.Menu;
import com.urms.menu.module.MenuDefinition;
import com.urms.role.module.Role;
import com.urms.roleRight.dao.IRoleRightDao;
import com.urms.roleRight.module.RoleRight;
import com.urms.roleRight.service.IRoleRightService;
import com.urms.roleRight.vo.RoleRightVo;

@Repository("roleRightServiceImpl")
public class RoleRightServiceImpl extends BaseServiceImpl implements IRoleRightService{
	
	@Autowired
	public IRoleRightDao roleRightDaoImpl;

	@Override
	public void saveOrUpdate(String menuIds, String roleId,String sysCode){
		String[] menuIdsZ = menuIds.split(",");
		Role r = this.getEntityById(Role.class, roleId);
		Set<MenuDefinition> menuDefinitions = new TreeSet<MenuDefinition>();
		menuDefinitions.addAll(r.getMenuDefinitions());
		Set<MenuDefinition> mds = new TreeSet<MenuDefinition>();
		for (int i = 0; i < menuIdsZ.length; i++) {
			RoleRight roleRight = new RoleRight();
			Menu menu = new Menu();
			menu.setId(menuIdsZ[i]);
			roleRight.setMenu(menu);
			Role role = new Role();
			role.setId(roleId);
			roleRight.setRole(role);
			roleRight.setSysCode(sysCode);
			RoleRightVo roleRightVo = new RoleRightVo();
			BeanUtils.copyProperties(roleRight, roleRightVo);
			List<RoleRight> list = this.queryRoleRightList(roleRightVo);
			//先记录下原来保存的菜单功能点
			String menuId = "";
			if(!menuDefinitions.isEmpty()){
				for (MenuDefinition md : menuDefinitions) {
					menuId = md.getMenu().getId();
					if(menuId.equals(menuIdsZ[i])){
						mds.add(md);
					}
				}
			}
			if(!list.isEmpty()){
				this.saveClean(roleId, sysCode);//清空后再保存
			}	
			this.save(roleRight);
		}
		r.setMenuDefinitions(mds);
		this.update(r);//更新角色的菜单按钮权限
	}
	
	@Override
	public void saveClean(String roleId, String sysCode) {
		//先清除角色菜单所有功能点权限
		Role role = this.getEntityById(Role.class, roleId);
		role.setMenuDefinitions(null);
		this.update(role);
		RoleRightVo roleRightVo = new RoleRightVo();
		Role r = new Role();
		r.setId(roleId);
		roleRightVo.setRole(r);
		roleRightVo.setSysCode(sysCode);
		List<RoleRight> list = this.queryRoleRightList(roleRightVo);
		//删除角色菜单权限
		for (RoleRight roleRight : list) {
			this.delete(roleRight);
		}
	}
	
	@Override
	public List<RoleRight> queryRoleRightList(RoleRightVo roleRightVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(roleRightVo.getId()))
			criterionsList.add(Restrictions.eq("id", roleRightVo.getId()));
		if(StringUtils.isNotBlank(roleRightVo.getSysCode()))
			criterionsList.add(Restrictions.eq("sysCode", roleRightVo.getSysCode()));
		if(roleRightVo.getRole()!=null){
			if(StringUtils.isNotBlank(roleRightVo.getRole().getId()))
				criterionsList.add(Restrictions.eq("role.id", roleRightVo.getRole().getId()));
		}
		if(roleRightVo.getMenu()!=null){
			if(StringUtils.isNotBlank(roleRightVo.getMenu().getId()))
				criterionsList.add(Restrictions.eq("menu.id", roleRightVo.getMenu().getId()));
		}
		return roleRightDaoImpl.queryList(criterionsList,null, RoleRight.class);
	}
	
	public IRoleRightDao getRoleRightDaoImpl() {
		return roleRightDaoImpl;
	}

	public void setRoleRightDaoImpl(IRoleRightDao roleRightDaoImpl) {
		this.roleRightDaoImpl = roleRightDaoImpl;
	}

}
