package com.urms.user.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
import com.common.utils.helper.SpringUtils;
import com.google.gson.JsonObject;
import com.urms.menu.vo.MenuVo;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.role.module.Role;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

@Controller
@RequestMapping("/urms")
public class UserController<T> extends BaseController {
	
	@Autowired
	public IUserService userServiceImpl;
	@Autowired
	public IOrgFrameService orgFrameServiceImpl;
	@Autowired
	public ISubsystemService subsystemServiceImpl;
	/**
	 * @intruduction 列表页面
	 * @param httpSession
	 * @param response
	 * @param menuVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月6日下午4:09:32
	 */
	@RequestMapping(value="/user_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,MenuVo menuVo,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		List<Subsystem> subsystems = this.subsystemServiceImpl.getSubsystemList(new SubsystemVo());
		this.getRequest().setAttribute("subsystems", subsystems);
		User user = (User)this.getHttpSession().getAttribute("user");
		if(user.getType()==1){//超管
			return "/page/urms/user/user_listAdmin";
		}else{
			return "/page/urms/user/user_list";
		}

	}
	
	/**
	 * @intruduction 加载用户数据
	 * @param request
	 * @param response
	 * @param userVo
	 * @param page
	 * @param rows
	 * @param sign 标志 1：管理员  2：普通用户
	 * @author Mr.Wang
	 * @Date 2016年1月13日下午4:57:37
	 */
	@RequestMapping(value="/user_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,UserVo userVo,Integer page,Integer rows,Integer sign) {
		User user = (User)this.getHttpSession().getAttribute("user");
		Pager pager = this.userServiceImpl.queryEntityList(page, rows, userVo, user, sign);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"roles","orgFrame","userExtend"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 新增or编辑 页面
	 * @param request
	 * @param userVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月6日下午4:13:07
	 */
	@RequestMapping(value="/user_edit") 
	public String edit(HttpServletRequest request,UserVo userVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(userVo.getId())){
			User user = this.userServiceImpl.getEntityById(User.class, userVo.getId());
			BeanUtils.copyProperties(user,userVo);
			request.setAttribute("userVo", userVo);
		}else{
			User user = (User)this.getHttpSession().getAttribute("user");
			if(user.getType()!=1)
				userVo.setSysCode(user.getSysCode());				
			request.setAttribute("userVo", userVo);
		}
		return "/page/urms/user/user_edit";
	}
	
	/**
	 * 新的用户资料页面
	 * @intruduction
	 * @param request
	 * @param userVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年10月25日上午8:58:15
	 */
	@RequestMapping(value="/user_edit_user") 
	public String editUser(HttpServletRequest request,UserVo userVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(userVo.getId())){
			User user = this.userServiceImpl.getEntityById(User.class, userVo.getId());
			BeanUtils.copyProperties(user,userVo);
			request.setAttribute("userVo", userVo);
		}else{
			User user = (User)this.getHttpSession().getAttribute("user");
			if(user.getType()!=1)
				userVo.setSysCode(user.getSysCode());				
			request.setAttribute("userVo", userVo);
		}
		return "/page/urms/user/user_edit_user";
	}
	
	/**
	 * @intruduction 保存or更新
	 * @param httpSession
	 * @param response
	 * @param userVo
	 * @author Mr.Wang
	 * @Date 2016年1月7日上午9:41:01
	 */
	@RequestMapping(value="/user_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,UserVo userVo) {
		JsonObject json = new JsonObject();
		try {
			User user = new User();
			if(userVo.getId()!=null && !"".equals(userVo.getId())){
				userVo.setDataSource(1); // 通过电脑端新增用户信息
			}
			BeanUtils.copyProperties(userVo,user);
			this.userServiceImpl.saveOrUpdate(user);
			if(user.getId().equals(this.getSessionUser().getId())){//更新人是当登录人就更新session
				this.getHttpSession().setAttribute("user", user);
			}
			json.addProperty("id", user.getId());
			json.addProperty("sysCode", user.getSysCode());
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}

	/**
	 * @intruduction 删除用户
	 * @param httpSession
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月7日下午1:38:41
	 */
	@RequestMapping(value="/user_delete",method = RequestMethod.POST) 
	public void delete(HttpSession httpSession,HttpServletResponse response,String ids) {
		JsonObject json = new JsonObject();
		try {
			this.userServiceImpl.deleteUser(ids);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 选择用户页面
	 * @param winName iframe name
	 * @param id 回填id
	 * @param name 回填name
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月11日上午10:45:42
	 */
	@RequestMapping(value="/user_chooseUser") 
	public String chooseUser(String winName,String id,String name,String selectNum){
		this.getRequest().setAttribute("winName",winName);
		this.getRequest().setAttribute("id",id);
		this.getRequest().setAttribute("name", name);
		this.getRequest().setAttribute("selectNum", selectNum);
		return "/page/urms/user/user_choose";
	}
	
	
	/**
	 * @intruduction 组织选人
	 * @param winName
	 * @param id
	 * @param name
	 * @param selectNum
	 * @param orgFrameIds
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年4月18日下午4:29:28
	 */
	@RequestMapping(value="/user_orgFrameActor") 
	public String orgFrameActor(String winName,String id,String name,String selectNum,String orgFrameIds){
		this.getRequest().setAttribute("winName",winName);
		this.getRequest().setAttribute("id",id);
		this.getRequest().setAttribute("name", name);
		this.getRequest().setAttribute("selectNum", selectNum);
		this.getRequest().setAttribute("orgFrameIds", orgFrameIds);
		String[] orgFrameId = orgFrameIds.split(",");
		List<OrgFrame> list = new ArrayList<OrgFrame>();
		for (int i = 0; i < orgFrameId.length; i++) {
			OrgFrame orgFrame = this.userServiceImpl.getEntityById(OrgFrame.class, orgFrameId[i]);
			list.add(orgFrame);
		}
		this.getRequest().setAttribute("orgFrameList", list);
		return "/page/urms/user/user_orgFrameActor";
	}
	
	/**
	 * @intruduction 选择用户页面
	 * @param winName iframe name
	 * @param id 回填id
	 * @param name 回填name
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月11日上午10:45:42
	 */
	@RequestMapping(value="/chooseNextUser") 
	public String chooseNextUser(String winName,String id,String name,String selectNum,String showUserName){
		this.getRequest().setAttribute("winName",winName);
		this.getRequest().setAttribute("id",id);
		this.getRequest().setAttribute("name", name);
		this.getRequest().setAttribute("selectNum", selectNum);
		this.getRequest().setAttribute("showUserName", showUserName);//显示名字
		return "/page/urms/user/user_chooseNext";
	}
	
	/**
	 * @intruduction 参与者选择
	 * @param winName
	 * @param id
	 * @param name
	 * @param selectNum
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年4月18日下午2:47:28
	 */
	@RequestMapping(value="/user_chooseActor") 
	public String chooseActor(String winName,String id,String name,String selectNum,String actorIds,String showUserName){
		this.getRequest().setAttribute("winName",winName);
		this.getRequest().setAttribute("id",id);
		this.getRequest().setAttribute("name", name);
		this.getRequest().setAttribute("selectNum", selectNum);
		this.getRequest().setAttribute("showUserName", showUserName);//显示名字
		String[] actorId = actorIds.split(",");
		List<User> list = new ArrayList<User>();
		for (int i = 0; i < actorId.length; i++) {
			User user = this.userServiceImpl.getEntityById(User.class, actorId[i]);
			list.add(user);
		}
		this.getRequest().setAttribute("userList", list);
		return "/page/urms/user/user_actor";
	}
	
	/**
	 * @intruduction 获得当前节点用户
	 * @param response
	 * @param userVo 查询条件
	 * @author Mr.Wang
	 * @Date 2016年1月9日下午4:42:49
	 */
	@RequestMapping(value="/user_getUserList") 
	public void getUserList(HttpServletRequest request,HttpServletResponse response,UserVo userVo){
		userVo.setState(1);//状态正常
		userVo.setSysCode(this.getSessionUser().getSysCode());//子系统编码
		List<User> list = this.userServiceImpl.queryUserList(userVo);
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"roles","orgFrame","userExtend"});  //只要设置这个数组，指定过滤哪些字段。     
		this.print(JSONArray.fromObject(list,config));
	}
	
	/**
	 * @intruduction 获得当前节点及所有下级节点用户
	 * @param httpSession
	 * @param response
	 * @param roleVo 查询条件
	 * @author Mr.Wang
	 * @Date 2016年1月9日下午4:42:49
	 */
	@RequestMapping(value="/user_getUserChildList") 
	public void getUserChildList(HttpServletRequest request,HttpServletResponse response,UserVo userVo){
		userVo.setState(1);//状态正常
		userVo.setSysCode(this.getSessionUser().getSysCode());//子系统编码
		List<User> list = this.userServiceImpl.queryUserChildList(userVo);
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"roles","orgFrame","userExtend"});  //只要设置这个数组，指定过滤哪些字段。     
		this.print(JSONArray.fromObject(list,config));
	}
	
	/**
	 * @intruduction 关联角色页面
	 * @param httpSession
	 * @param response
	 * @param userVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月11日上午11:44:56
	 */
	@RequestMapping(value="/user_relationRole") 
	public String relationRole(HttpSession httpSession,HttpServletResponse response,UserVo userVo) {
		User user = this.userServiceImpl.getEntityById(User.class, userVo.getId());
		this.getRequest().setAttribute("userId",user.getId());
		StringBuffer roleIds = new StringBuffer();
		StringBuffer roleNames = new StringBuffer();
		for (Role role : user.getRoles()) {  
			roleIds.append(role.getId()+",");
			roleNames.append(role.getRoleName()+",");
		}  
		if(roleIds.length()>0){
			this.getRequest().setAttribute("roleIds",roleIds.deleteCharAt(roleIds.length()-1));
			this.getRequest().setAttribute("roleNames",roleNames.deleteCharAt(roleNames.length()-1));			
		}else{
			this.getRequest().setAttribute("roleIds","");
			this.getRequest().setAttribute("roleNames","");			
		}
		return "/page/urms/user/user_relationRole";
	}
	
	/**
	 * @intruduction 保存关联角色
	 * @param response
	 * @param userId
	 * @param roleId
	 * @author Mr.Wang
	 * @Date 2016年1月11日上午10:48:16
	 */
	@RequestMapping(value="/user_saveRelationRole",method = RequestMethod.POST) 
	public void saveRelationRole(HttpServletResponse response,String userId,String roleIds) {
		JsonObject json = new JsonObject();
		try {
			User user = this.userServiceImpl.getEntityById(User.class, userId);
			Set<Role> roles = new TreeSet<Role>();
			String[] roleIdz  = roleIds.split(",");
			for(int i = 0; i < roleIdz.length; i++) {
				Role role = this.userServiceImpl.getEntityById(Role.class, roleIdz[i]);
				roles.add(role);
			}
			user.setRoles(roles);
			this.userServiceImpl.update(user);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @intruduction 清除关联角色
	 * @param response
	 * @param userId
	 * @param roleId
	 * @author Mr.Wang
	 * @Date 2016年1月11日上午10:48:16
	 */
	@RequestMapping(value="/user_cleanRelationRole",method = RequestMethod.POST) 
	public void cleanRelationRole(HttpServletResponse response,String userId) {
		JsonObject json = new JsonObject();
		try {
			User user = this.userServiceImpl.getEntityById(User.class, userId);
			user.setRoles(null);
			this.userServiceImpl.update(user);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 检查用户登录是否唯一,个子系统登录名都只能有一个
	 * @intruduction
	 * @param response
	 * @param userVo
	 * @author Mr.Wang
	 * @Date 2016年11月1日下午2:51:03
	 */
	@RequestMapping(value="/user_checkUserLoginName") 
	public void checkUserLoginName(HttpServletResponse response,UserVo userVo) {
		if(StringUtils.isBlank(userVo.getId())){//新增情况下
			UserVo uvo = new UserVo();
			uvo.setLoginName(userVo.getLoginName());
			uvo.setSysCode(this.getSessionUser().getSysCode());//子系统编码
			List<User> list= this.userServiceImpl.queryUserList(uvo);
			if(!list.isEmpty())
				this.print(false);
			else
				this.print(true);
		}else{//修改情况下
			List<User> list= this.userServiceImpl.queryUserList(userVo);
			if(!list.isEmpty()){
				this.print(true);//已经存在				
			}else{
				UserVo uvo = new UserVo();
				uvo.setLoginName(userVo.getLoginName());
				uvo.setSysCode(this.getSessionUser().getSysCode());//子系统编码
				List<User> list2= this.userServiceImpl.queryUserList(uvo);
				if(!list2.isEmpty())
					this.print(false);
				else
					this.print(true);
			}
		}
	}
	
	/**
	 * 检查手机号码是否唯一,个子系统登录名都只能有一个
	 * @intruduction
	 * @param response
	 * @param userVo
	 * @author Mr.Wang
	 * @Date 2016年11月1日下午2:50:55
	 */
	@RequestMapping(value="/user_checkMobilePhone") 
	public void checkMobilePhone(HttpServletResponse response,UserVo userVo) {
		if(StringUtils.isBlank(userVo.getId())){//新增情况下
			UserVo uvo = new UserVo();
			uvo.setMobilePhone(userVo.getMobilePhone());
			uvo.setSysCode(this.getSessionUser().getSysCode());//子系统编码
			List<User> list= this.userServiceImpl.queryUserList(uvo);
			if(!list.isEmpty())
				this.print(false);
			else
				this.print(true);
		}else{//修改情况下
			List<User> list= this.userServiceImpl.queryUserList(userVo);
			if(!list.isEmpty()){
				this.print(true);//已经存在				
			}else{
				UserVo uvo = new UserVo();
				uvo.setMobilePhone(userVo.getMobilePhone());
				uvo.setSysCode(this.getSessionUser().getSysCode());//子系统编码
				List<User> list2= this.userServiceImpl.queryUserList(uvo);
				if(!list2.isEmpty())
					this.print(false);
				else
					this.print(true);
			}
		}
	}
	
	/**
	 * 检查Email是否唯一,个子系统登录名都只能有一个
	 * @intruduction
	 * @param response
	 * @param userVo
	 * @author Mr.Wang
	 * @Date 2016年11月1日下午2:50:35
	 */
	@RequestMapping(value="/user_checkEmail") 
	public void checkEmail(HttpServletResponse response,UserVo userVo) {
		if(StringUtils.isBlank(userVo.getId())){//新增情况下
			UserVo uvo = new UserVo();
			uvo.setEmail(userVo.getEmail());
			uvo.setSysCode(this.getSessionUser().getSysCode());//子系统编码
			List<User> list= this.userServiceImpl.queryUserList(uvo);
			if(!list.isEmpty())
				this.print(false);
			else
				this.print(true);
		}else{//修改情况下
			List<User> list= this.userServiceImpl.queryUserList(userVo);
			if(!list.isEmpty()){
				this.print(true);//已经存在				
			}else{
				UserVo uvo = new UserVo();
				uvo.setEmail(userVo.getEmail());
				uvo.setSysCode(this.getSessionUser().getSysCode());//子系统编码
				List<User> list2= this.userServiceImpl.queryUserList(uvo);
				if(!list2.isEmpty())
					this.print(false);
				else
					this.print(true);
			}
		}
	}
	
	/**
	 * @intruduction 组织架构下的用户
	 * @param httpSession
	 * @param response
	 * @param orgFrameVo
	 * @author Mr.Wang
	 * @Date 2016年4月18日下午4:46:58
	 */
	@RequestMapping(value="/user_queryOrgFrameActor") 
	public void queryOrgFrameActor(HttpSession httpSession,HttpServletResponse response,String orgFrameId){
		UserVo userVo = new UserVo();
		OrgFrame orgFrame = new OrgFrame();
		orgFrame.setId(orgFrameId);
		userVo.setOrgFrame(orgFrame);
		List<User> userList = this.userServiceImpl.queryUserList(userVo);
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"roles","orgFrame","userExtend"});  //只要设置这个数组，指定过滤哪些字段。     
		this.print(JSONArray.fromObject(userList,config).toString());
	}
	
	/**
	 * 
	 * @intruduction 获得当前用户是上下级人员
	 * @param httpSession
	 * @param response
	 * @param type 类型
	 * @author Mr.Wang
	 * @Date 2016年8月10日下午3:11:55
	 */
	@RequestMapping(value="/user_getDeptUser") 
	public void getDeptUser(HttpServletRequest request,HttpServletResponse response,String type){
		User user = this.getSessionUser();
		JSONObject json = new JSONObject();
		StringBuffer ids = new StringBuffer();
		StringBuffer names = new StringBuffer();
		UserVo userVo = new UserVo();
		OrgFrame orgFrame = new OrgFrame();
		if(type.equals("@")){//当前部门
			orgFrame.setId(user.getOrgFrame().getId());
			userVo.setOrgFrame(orgFrame);
			List<User> list = this.userServiceImpl.queryUserList(userVo);
			for (User u : list) {
				ids.append(u.getId()+",");
				names.append(u.getUserName()+",");
			}
		}else if(type.equals("up")){//上级
			orgFrame.setId(user.getOrgFrame().getpId());//父节点
			userVo.setOrgFrame(orgFrame);
			List<User> list = this.userServiceImpl.queryUserList(userVo);
			for (User u : list) {
				ids.append(u.getId()+",");
				names.append(u.getUserName()+",");
			}
		}else if(type.equals("allUp")){//所有上级
			String[] pIds = user.getOrgFrame().getpIds().split("/");
			for (String pId : pIds) {
				orgFrame.setId(pId);//父节点
				userVo.setOrgFrame(orgFrame);
				List<User> list = this.userServiceImpl.queryUserList(userVo);
				for (User u : list) {
					ids.append(u.getId()+",");
					names.append(u.getUserName()+",");
				}
			}
		}else if(type.equals("down")){//下级
			List<OrgFrame> orgFrameList  = this.orgFrameServiceImpl.queryEntityListByPId(user.getOrgFrame().getId());
			for (OrgFrame of : orgFrameList) {
				userVo.setOrgFrame(of);
				List<User> list = this.userServiceImpl.queryUserList(userVo);
				for (User u : list) {
					ids.append(u.getId()+",");
					names.append(u.getUserName()+",");
				}
			}
		}else if(type.equals("allDown")){//所有下级
			OrgFrameVo orgFrameVo = new OrgFrameVo();
			orgFrameVo.setpIds(user.getOrgFrame().getId());
			List<OrgFrame> orgFrameList = this.orgFrameServiceImpl.queryOrgFrameList(orgFrameVo);
			for (OrgFrame of : orgFrameList) {
				userVo.setOrgFrame(of);
				List<User> list = this.userServiceImpl.queryUserList(userVo);
				for (User u : list) {
					ids.append(u.getId()+",");
					names.append(u.getUserName()+",");
				}
			}
		}
		json.put("ids", ids.deleteCharAt(ids.length()-1).toString());
		json.put("names", names.deleteCharAt(names.length()-1).toString());
		this.print(json.toString());
	}
	
	/**
	 * 选择不同类型的人员 人 组织 角色
	 * @intruduction
	 * @param request
	 * @param response
	 * @return
	 * @author Mr.Wang
	 * @Date 2017年3月14日上午10:27:19
	 */
	@RequestMapping(value="/user_chooseType") 
	public String userChooseType(HttpServletRequest request ,HttpServletResponse response, String winName ,String selectNum,String id,String name,String type) {
		this.getRequest().setAttribute("id", id);
		this.getRequest().setAttribute("name", name);
		this.getRequest().setAttribute("type", type);
		this.getRequest().setAttribute("winName", winName);
		this.getRequest().setAttribute("selectNum", selectNum);//单选、多选
		return "/page/urms/user/user_chooseType";
	}

	/**
	 * 用户选择页面 列表
	 * @param httpSession
	 * @param response
	 * @param dataAreaCode
	 * @param winName
	 * @return
	 */
	@RequestMapping(value="/user_userSelect")
	public String enterpriseMemberSelect(HttpSession httpSession,HttpServletResponse response,String dataAreaCode,Integer type,String winName) {
		this.getRequest().setAttribute("dataAreaCode", dataAreaCode);
		this.getRequest().setAttribute("type", type);
		this.getRequest().setAttribute("winName", winName);
		return "/page/urms/user/user_select";
	}
	/**
	 * 更新头像路径
	 * @intruduction
	 * @param request
	 * @param response
	 * @param type
	 * @author Mr.Wang
	 * @Date 2016年10月26日上午10:35:10
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/user_updateAvatar") 
	public void updateAvatar(HttpServletRequest request,HttpServletResponse response,String id){
		JSONObject json = new JSONObject();
		try {
//			List<Attach> attachList =  this.attachServiceImpl.queryAttchListByFormId(id);
			//由于 采用分块打包代码 需采用 反射方式
			Object obj = (Object) SpringUtils.getBean("attachServiceImpl");
			Class<? extends Object> cl = obj.getClass();//得到类
			Method method = cl.getMethod("queryAttchListByFormId", String.class);//获得方法 输入参数类型
			List<T> attachList = (List<T>)method.invoke(obj,id);//获得附件
			User user = this.userServiceImpl.getEntityById(User.class, id);
			if(!attachList.isEmpty()){
				Field[] field = attachList.get(0).getClass().getDeclaredFields();  //返回所有属性
				for(int j=0 ; j<field.length ; j++){     //遍历所有属性
					String name = field[j].getName();    //获取属性的名字
	                if(name.equals("pathUpload")){
	                	Method m = attachList.get(0).getClass().getMethod("getPathUpload");
	                	String pathUpload = (String) m.invoke(attachList.get(0));    //调用getter方法获取属性值
	                	user.setAvatarPathUpload(pathUpload);
	                }
				}
//				user.setAvatarPathUpload(attachList.get(0).getPathUpload());
			}else{
				user.setAvatarPathUpload("");
			}
			this.getHttpSession().setAttribute("user", user);//更新session
			this.userServiceImpl.update(user);
			json.put("result", true);
		} catch (Exception e) {
			json.put("result", false);
			logger.error(e.getMessage(), e);
		}
		this.print(json.toString());
	}
	
	
	/**
	 * @intruduction 打开拍照页面
	 * @param response
	 * @author Mr.Wang
	 * @Date 2016年1月28日上午8:57:39
	 */
	@RequestMapping(value="/user_avatar") 
	public String avatar(HttpServletResponse response) {
		return "/page/urms/user/user_avatar";
	}
	
	public IUserService getUserServiceImpl() {
		return userServiceImpl;
	}

	public void setUserServiceImpl(IUserService userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	public IOrgFrameService getOrgFrameServiceImpl() {
		return orgFrameServiceImpl;
	}

	public void setOrgFrameServiceImpl(IOrgFrameService orgFrameServiceImpl) {
		this.orgFrameServiceImpl = orgFrameServiceImpl;
	}
	
}