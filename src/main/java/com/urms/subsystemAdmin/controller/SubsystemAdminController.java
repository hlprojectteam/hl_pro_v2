package com.urms.subsystemAdmin.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.urms.subsystem.module.Subsystem;
import com.urms.subsystem.service.ISubsystemService;
import com.urms.subsystem.vo.SubsystemVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.base.controller.BaseController;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

import java.util.List;

@Controller
@RequestMapping("/urms")
public class SubsystemAdminController extends BaseController{
	
	@Autowired
	public IUserService userServiceImpl;
	@Autowired
	public ISubsystemService subsystemServiceImpl;
	
	/**
	 * @intruduction 系统管理员列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:21:41
	 */
	@RequestMapping(value="/subsystemAdmin_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		List<Subsystem> subsystems = this.subsystemServiceImpl.getSubsystemList(new SubsystemVo());
		this.getRequest().setAttribute("subsystems", subsystems);
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/urms/subsystemAdmin/subsystemAdmin_list";
	}
	
	/**
	 * @intruduction 新增or编辑 页面
	 * @param request
	 * @param userVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月6日下午4:13:07
	 */
	@RequestMapping(value="/subsystemAdmin_edit") 
	public String edit(HttpServletRequest request,UserVo userVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(userVo.getId())){
			User user = this.userServiceImpl.getEntityById(User.class, userVo.getId());
			BeanUtils.copyProperties(user,userVo);
			request.setAttribute("userVo", userVo);
		}else{
			User user = this.getSessionUser();
			if(user.getType()!=1)
				userVo.setSysCode(user.getSysCode());				
			request.setAttribute("userVo", userVo);
		}
		return "/page/urms/subsystemAdmin/subsystemAdmin_edit";
	}

	@RequestMapping(value="/subsystemSuperAdmin_edit") 
	public String superAdminEdit(HttpServletRequest request,UserVo userVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(userVo.getId())){
			User user = this.userServiceImpl.getEntityById(User.class, userVo.getId());
			BeanUtils.copyProperties(user,userVo);
			request.setAttribute("userVo", userVo);
		}
		return "/page/urms/subsystemAdmin/subsystemSuperAdmin_edit";
	}
	
	public IUserService getUserServiceImpl() {
		return userServiceImpl;
	}

	public void setUserServiceImpl(IUserService userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}
	
}