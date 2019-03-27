package com.urms.role.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.urms.subsystem.module.Subsystem;
import com.urms.subsystem.service.ISubsystemService;
import com.urms.subsystem.vo.SubsystemVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.common.base.controller.BaseController;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.role.module.Role;
import com.urms.role.service.IRoleService;
import com.urms.role.vo.RoleVo;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

@Controller
@RequestMapping("/urms")
public class RoleController extends BaseController{
	
	@Autowired
	public IRoleService roleServiceImpl;
	@Autowired
	public IUserService userServiceImpl;
	@Autowired
	public ISubsystemService subsystemServiceImpl;
	/**
	 * @intruduction 角色列表页面
	 * @param httpSession
	 * @param response
	 * @param roleVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月6日下午5:27:30
	 */
	@RequestMapping(value="/role_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,RoleVo roleVo,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		List<Subsystem> subsystems = this.subsystemServiceImpl.getSubsystemList(new SubsystemVo());
		this.getRequest().setAttribute("subsystems", subsystems);
		User user = (User)this.getHttpSession().getAttribute("user");
		if(user.getType()==1){//超管
			return "/page/urms/role/role_listAdmin";
		}else{
			return "/page/urms/role/role_list";
		}
	}
	
	/**
	 * @intruduction 角色列表
	 * @param request
	 * @param response
	 * @param roleVo
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 * @Date 2016年1月6日下午5:29:56
	 */
	@RequestMapping(value="/role_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,RoleVo roleVo,Integer page,Integer rows) {
		User user = (User)this.getHttpSession().getAttribute("user");
//		if(roleVo.getOrgFrame()==null){
//			OrgFrame org=new OrgFrame();
//			org.setId("297ef6c657747a9f01577482f4e90000");
//			roleVo.setOrgFrame(org);
//		}
		Pager pager = this.roleServiceImpl.queryEntityList(page, rows, roleVo,user);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"users","roleRights","orgFrame","menuDefinitions"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 编辑
	 * @param request
	 * @param roleVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月7日上午10:36:47
	 */
	@RequestMapping(value="/role_edit") 
	public String edit(HttpServletRequest request,RoleVo roleVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(roleVo.getId())){
			Role role = this.roleServiceImpl.getEntityById(Role.class,roleVo.getId());
			BeanUtils.copyProperties(role,roleVo);
			request.setAttribute("roleVo", roleVo);
		}else{
			request.setAttribute("roleVo", roleVo);
		}
		return "/page/urms/role/role_edit";
	}
	
	/**
	 * @intruduction 保存or编辑
	 * @param httpSession
	 * @param response
	 * @param roleVo
	 * @author Mr.Wang
	 * @Date 2016年1月7日上午10:36:56
	 */
	@RequestMapping(value="/role_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,RoleVo roleVo) {
		JsonObject json = new JsonObject();
		try {
			Role role = new Role();
			BeanUtils.copyProperties(roleVo,role);
			this.roleServiceImpl.saveOrUpdate(role);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @intruduction 删除角色
	 * @param httpSession
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月7日下午1:36:44
	 */
	@RequestMapping(value="/role_delete",method = RequestMethod.POST) 
	public void delete(HttpSession httpSession,HttpServletResponse response,String ids) {
		this.roleServiceImpl.delete(Role.class,ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}

	/**
	 * @intruduction 选择角色页面
	 * @param winName iframe name
	 * @param id 回填id
	 * @param name 回填name
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月11日上午10:45:42
	 */
	@RequestMapping(value="/role_chooseRole") 
	public String chooseRole(String winName,String id,String name){
		this.getRequest().setAttribute("winName",winName);
		this.getRequest().setAttribute("id",id);
		this.getRequest().setAttribute("name", name);
		return "/page/urms/role/role_choose";
	}
	
	/**
	 * @intruduction 获得角色
	 * @param httpSession
	 * @param response
	 * @param roleVo 查询条件
	 * @author Mr.Wang
	 * @Date 2016年1月9日下午4:42:49
	 */
	@RequestMapping(value="/role_getRoleList") 
	public void getRoleList(HttpSession httpSession,HttpServletRequest request,HttpServletResponse response,RoleVo roleVo){
		roleVo.setSysCode(this.getSessionUser().getSysCode());//子系统编码
		List<Role> list = this.roleServiceImpl.queryRoleList(roleVo);
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"users","roleRights","menuDefinitions","orgFrame"});  //只要设置这个数组，指定过滤哪些字段。     
		this.print(JSONArray.fromObject(list,config));
	}
	
	/**
	 * @intruduction 选择角色用户
	 * @param winName iframe name
	 * @param id 回填id
	 * @param name 回填name
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月11日上午10:45:42
	 */
	@RequestMapping(value="/role_roleUser") 
	public String roleUser(String winName,String id,String name){
		this.getRequest().setAttribute("winName",winName);
		this.getRequest().setAttribute("id",id);
		this.getRequest().setAttribute("name", name);
		return "/page/urms/role/role_user";
	}
	
	/**
	 * @intruduction 角色下一处理人
	 * @param winName
	 * @param id
	 * @param name
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年4月18日下午3:51:55
	 */
	@RequestMapping(value="/role_roleActor") 
	public String roleActor(String winName,String id,String name,String selectNum,String roleIds){
		this.getRequest().setAttribute("winName",winName);
		this.getRequest().setAttribute("id",id);
		this.getRequest().setAttribute("name", name);
		this.getRequest().setAttribute("selectNum", selectNum);
		String[] roleId = roleIds.split(",");
		List<Role> list = new ArrayList<Role>();
		for (int i = 0; i < roleId.length; i++) {
			Role role = this.userServiceImpl.getEntityById(Role.class, roleId[i]);
			list.add(role);
		}
		this.getRequest().setAttribute("roleList", list);
		return "/page/urms/role/role_actor";
	}
	
	/**
	 * @intruduction 获得角色用户列表
	 * @param winName iframe name
	 * @param id 回填id
	 * @param name 回填name
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月11日上午10:45:42
	 */
	@RequestMapping(value="/role_queryUserList") 
	public void queryUserList(HttpServletResponse response,String winName,String roleId,String name){
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"roles","orgFrame"});  //只要设置这个数组，指定过滤哪些字段。     
		String json = "";
		if(StringUtils.isNotBlank(roleId)){
			Role role = this.getRoleServiceImpl().getEntityById(Role.class, roleId);	
			json = JSONArray.fromObject(role.getUsers(),config).toString();
		}else{
			 Pager userList = this.userServiceImpl.queryEntityList(1, 200, new UserVo(), this.getSessionUser(), 3);
			 json = JSONArray.fromObject(userList.getPageList(),config).toString();
		}
		this.print(json);
	}
	
	/**
	 * @intruduction 关联用户
	 * @param httpSession
	 * @param response
	 * @param roleVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月7日下午1:37:00
	 */
	@RequestMapping(value="/role_relationUser") 
	public String relationUser(HttpSession httpSession,HttpServletResponse response,RoleVo roleVo) {
		Role role = this.roleServiceImpl.getEntityById(Role.class, roleVo.getId());
		this.getRequest().setAttribute("roleId",role.getId());
		StringBuffer userIds = new StringBuffer();
		StringBuffer userNames = new StringBuffer();
		for (User user : role.getUsers()) {  
			userIds.append(user.getId()+",");
			userNames.append(user.getUserName()+",");
		}  
		if(userIds.length()>0){
			this.getRequest().setAttribute("userIds",userIds.deleteCharAt(userIds.length()-1));
			this.getRequest().setAttribute("userNames",userNames.deleteCharAt(userNames.length()-1));			
		}else{
			this.getRequest().setAttribute("userIds","");
			this.getRequest().setAttribute("userNames","");			
		}
		return "/page/urms/role/role_relationUser";
	}
	
	
	/**
	 * @intruduction 保存关联用户
	 * @param response
	 * @param userId
	 * @param roleId
	 * @author Mr.Wang
	 * @Date 2016年1月11日上午11:45:46
	 */
	@RequestMapping(value="/role_saveRelationUser",method = RequestMethod.POST) 
	public void saveRelationUser(HttpServletResponse response,String userIds,String roleId) {
		JsonObject json = new JsonObject();
		try {
			Role role = this.roleServiceImpl.getEntityById(Role.class, roleId);
			Set<User> users = new TreeSet<User>();
			String[] userIdz  = userIds.split(",");
			for(int i = 0; i < userIdz.length; i++) {
				User user = this.roleServiceImpl.getEntityById(User.class, userIdz[i]);
				users.add(user);
			}
			role.setUsers(users);
			this.roleServiceImpl.update(role);
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
	 * @intruduction 清除关联用户
	 * @param response
	 * @param userIds
	 * @param roleId
	 * @author Mr.Wang
	 * @Date 2016年8月16日上午9:39:32
	 */
	@RequestMapping(value="/role_cleanRelationUser",method = RequestMethod.POST) 
	public void cleanRelationUser(HttpServletResponse response,String roleId) {
		JsonObject json = new JsonObject();
		try {
			Role role = this.roleServiceImpl.getEntityById(Role.class, roleId);
			role.setUsers(null);
			this.roleServiceImpl.update(role);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	public IRoleService getRoleServiceImpl() {
		return roleServiceImpl;
	}
	public void setRoleServiceImpl(IRoleService roleServiceImpl) {
		this.roleServiceImpl = roleServiceImpl;
	}
	public IUserService getUserServiceImpl() {
		return userServiceImpl;
	}
	public void setUserServiceImpl(IUserService userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}
}