package com.urms.roleRight.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.common.base.controller.BaseController;
import com.google.gson.JsonObject;
import com.urms.menu.module.Menu;
import com.urms.menu.module.MenuDefinition;
import com.urms.menu.service.IMenuService;
import com.urms.menu.vo.MenuVo;
import com.urms.role.module.Role;
import com.urms.roleRight.module.RoleRight;
import com.urms.roleRight.service.IRoleRightService;
import com.urms.roleRight.vo.RoleRightVo;
import com.urms.subsystem.module.Subsystem;
import com.urms.subsystem.service.ISubsystemService;
import com.urms.subsystem.vo.SubsystemVo;

@Controller
@RequestMapping("/urms")
@SuppressWarnings({"unchecked","rawtypes"})
public class RoleRightController extends BaseController{
	
	@Autowired
	public IRoleRightService roleRightServiceImpl;
	@Autowired
	public IMenuService menuServiceImpl;
	@Autowired
	public ISubsystemService subsystemServiceImpl;
	/**
	 * @intruduction 菜单列表页面
	 * @param httpSession
	 * @param response
	 * @param roleRightVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:21:41
	 */
	@RequestMapping(value="/roleRight_list") 
	public String list(HttpSession httpSession,HttpServletResponse response) {
		return "/page/urms/roleRight/roleRight_list";
	}
	
	
	/**
	 * 加载所属子系统菜单树
	 * @param httpSession
	 * @param response
	 * @param menuVo
	 */
	@RequestMapping(value="/roleRight_menuTree") 
	public void menuTree(HttpServletResponse response,String id,String roleId,String sysCode) {
		StringBuffer tree = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		//检查是否有权限
		if(StringUtils.isNotBlank(roleId)&&StringUtils.isNotBlank(sysCode)){
			RoleRightVo roleRightVo = new RoleRightVo();
			roleRightVo.setSysCode(sysCode);
			Role role = new Role();
			role.setId(roleId);
			roleRightVo.setRole(role);
			List<RoleRight> list = this.roleRightServiceImpl.queryRoleRightList(roleRightVo);
			for (int i = 0; i < list.size(); i++) {
				sb.append(list.get(i).getMenu().getId()+",");
			}			
		}
		SubsystemVo subsystemVo = new SubsystemVo();
		subsystemVo.setSysCode(sysCode);
		List<Subsystem> subsystemList= subsystemServiceImpl.getSubsystemList(subsystemVo);
		tree.append("[");
		if(!subsystemList.isEmpty()){
			List<Menu> list = new ArrayList<Menu>(subsystemList.get(0).getMenus());
			ComparatorMenu comparator=new ComparatorMenu();
			Collections.sort(list, comparator);//排序
			for (Menu menu : list) {
				tree.append("{");
				tree.append("id:'"+menu.getId()+"',");
				tree.append("pId:'"+menu.getpId()+"',");
				tree.append("name:'"+menu.getMenuName()+"'");
				tree.append(",open:true");
				if(sb.toString().contains(menu.getId()))
					tree.append(",checked:true");
				if(menu.getIsLeaf()==0)
					tree.append(",isParent:true");
				tree.append("},");
			}
		}
		tree.deleteCharAt(tree.toString().length()-1);
		tree.append("]");
		logger.info("输出树结构:"+tree.toString());
		this.print(tree.toString());
	}
	
	/**
	 * @intruduction 保存角色权限表
	 * @param httpSession
	 * @param response
	 * @param menuIds
	 * @param roleId
	 * @param sysCode
	 * @author Mr.Wang
	 * @Date 2016年1月14日下午3:29:16
	 */
	@RequestMapping(value="/roleRight_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,String menuIds, String roleId,String sysCode) {
		JsonObject json = new JsonObject();
		try {
			this.roleRightServiceImpl.saveOrUpdate(menuIds, roleId, sysCode);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 清除角色菜单权限
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param roleId 角色id
	 * @param sysCode 子系统编码
	 * @author Mr.Wang
	 * @Date 2016年8月16日下午2:14:12
	 */
	@RequestMapping(value="/roleRight_clean",method = RequestMethod.POST) 
	public void clean(HttpSession httpSession,HttpServletResponse response,String roleId,String sysCode) {
		JsonObject json = new JsonObject();
		try {
			this.roleRightServiceImpl.saveClean(roleId, sysCode);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @intruduction 保存角色菜单功能
	 * @param httpSession
	 * @param response
	 * @param menuIds
	 * @param roleId
	 * @author Mr.Wang
	 * @Date 2016年2月2日下午5:39:45
	 */
	@RequestMapping(value="/roleMenuDefinition_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdateRoleMenuDefinition(HttpServletResponse response,String menuDefinitionIds, String roleId,String menuId) {
		JsonObject json = new JsonObject();
		try {
			Role role = this.roleRightServiceImpl.getEntityById(Role.class, roleId);
			MenuVo menuVo = new MenuVo();
			menuVo.setId(menuId);
			List<MenuDefinition> menuDefinitionList = menuServiceImpl.queryMenuDefinitionList(menuVo);
			List<String> listYes = new ArrayList<String>();
			List<String> listNo = new ArrayList<String>();
			//选上和没有选上的
			for (int i = 0; i < menuDefinitionList.size(); i++) {
				if(menuDefinitionIds.indexOf(menuDefinitionList.get(i).getId())>-1){//如果存在
					listYes.add(menuDefinitionList.get(i).getId());//选上
				}else{
					listNo.add(menuDefinitionList.get(i).getId());//没有选上
				}
			}
			List<MenuDefinition> on = new ArrayList<MenuDefinition>();
			for (int i = 0; i < listYes.size(); i++) {
				MenuDefinition m = this.menuServiceImpl.getEntityById(MenuDefinition.class, listYes.get(i));
				if(!role.getMenuDefinitions().contains(m)){//如果没有选上
					MenuDefinition md = new MenuDefinition();
					md.setId(listYes.get(i));
					role.getMenuDefinitions().add(md);//添加
				}
			}
			List<MenuDefinition> del = new ArrayList<MenuDefinition>();
			for (int i = 0; i < listNo.size(); i++) {
				Iterator<MenuDefinition> it = role.getMenuDefinitions().iterator();
				while (it.hasNext()) {
					MenuDefinition mds = it.next();
					if (listNo.get(i).toString().equals(mds.getId())) {
						del.add(mds);//删除没有选上的
					}
				}
			}
			for (int i = 0; i < on.size(); i++) {
				MenuDefinition md = new MenuDefinition();
				md.setId(on.get(i).getId());
				role.getMenuDefinitions().add(md);
			}
			for (int i = 0; i < del.size(); i++) {
				role.getMenuDefinitions().remove(del.get(i));
			}
			this.roleRightServiceImpl.update(role);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 
	 * @intruduction 清除角色菜单功能
	 * @param response
	 * @param menuDefinitionIds
	 * @param roleId
	 * @param menuId
	 * @author Mr.Wang
	 * @Date 2016年8月16日下午1:51:30
	 */
	@RequestMapping(value="/roleMenuDefinition_claen",method = RequestMethod.POST) 
	public void cleanRoleMenuDefinition(HttpServletResponse response, String roleId,String menuDefinitionIds) {
		JsonObject json = new JsonObject();
		try {
			Role role = this.roleRightServiceImpl.getEntityById(Role.class, roleId);
			Iterator it = role.getMenuDefinitions().iterator();  
			while(it.hasNext()) {  
				MenuDefinition md = (MenuDefinition)it.next();
				if(menuDefinitionIds.indexOf(md.getId())>-1){//清除对应的菜单功能点
					it.remove();
				}
			}  
			this.roleRightServiceImpl.update(role);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @intruduction 检查角色的菜单功能点权限
	 * @param response
	 * @param roleId
	 * @author Mr.Wang
	 * @Date 2016年2月3日上午11:13:22
	 */
	@RequestMapping(value="/checkRoleMenuDefinition",method = RequestMethod.POST) 
	public void checkRoleMenuDefinition(HttpServletResponse response, String roleId) {
		Role role = this.roleRightServiceImpl.getEntityById(Role.class, roleId);
		StringBuffer sb = new StringBuffer();
		for (MenuDefinition md : role.getMenuDefinitions()) {
			sb.append(md.getId()+",");
		}
		if(sb.length()>0)
			sb.deleteCharAt(sb.length()-1);
		this.print(sb.toString());
	}
	
	public IRoleRightService getRoleRightServiceImpl() {
		return roleRightServiceImpl;
	}
	public void setRoleRightServiceImpl(IRoleRightService roleRightServiceImpl) {
		this.roleRightServiceImpl = roleRightServiceImpl;
	}

	public IMenuService getMenuServiceImpl() {
		return menuServiceImpl;
	}
	public void setMenuServiceImpl(IMenuService menuServiceImpl) {
		this.menuServiceImpl = menuServiceImpl;
	}


	public ISubsystemService getSubsystemServiceImpl() {
		return subsystemServiceImpl;
	}


	public void setSubsystemServiceImpl(ISubsystemService subsystemServiceImpl) {
		this.subsystemServiceImpl = subsystemServiceImpl;
	}
	
	//菜单排序
	public class ComparatorMenu implements Comparator{
		public int compare(Object arg0, Object arg1) {
			Menu menu0=(Menu)arg0;
			Menu menu1=(Menu)arg1;
			return menu0.getOrder().compareTo(menu1.getOrder());
		}
	}
}