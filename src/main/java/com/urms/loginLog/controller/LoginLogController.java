package com.urms.loginLog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.common.utils.Common;
import com.urms.user.module.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.common.base.controller.BaseController;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.loginLog.module.LoginLog;
import com.urms.loginLog.service.ILoginLogService;
import com.urms.loginLog.vo.LoginLogVo;

/**
 * 用户登录日志
 * @author Mr.Wang
 * 2018年7月24日 15:11:47
 */
@Controller
@RequestMapping("/urms")
public class LoginLogController extends BaseController{
	
	@Autowired
	public ILoginLogService loginLogServiceImpl;
	
	/**
	 * @intruduction 登录日志列表页面
	 * @param httpSession
	 * @param response
	 * @return
	 * @author Mr.Wang
	 * @Date 2018年7月24日 15:53:41
	 */
	@RequestMapping(value="/loginLog_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		User user = this.getSessionUser();
		this.getRequest().setAttribute("userType", user.getType());
		return "/page/urms/loginLog/loginLog_list";
	}
	
	/**
	 * @intruduction 登录日志列表数据
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 * @Date 2018年7月24日 15:53:59
	 */
	@RequestMapping(value="/loginLog_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,LoginLogVo loginLogVo,Integer page,Integer rows, String order) {
		User user = this.getSessionUser();
		if(user.getType() == 3){//后台普通用户
			loginLogVo.setUserId(user.getId());
		}
		if(user.getType() == 2){//子系统管理员
			loginLogVo.setSysCode(Common.SYSCODE);
		}
		Pager pager = this.loginLogServiceImpl.queryEntityList(page, rows, loginLogVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * @intruduction 保存登录日志
	 * @param httpSession
	 * @param response
	 * @author Mr.Wang
	 * @Date 2018年7月24日 15:54:09
	 */
	@RequestMapping(value="/loginLog_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,LoginLogVo loginLogVo) {
		JsonObject json = new JsonObject();
		try {
			LoginLog loginLog = new LoginLog();
			BeanUtils.copyProperties(loginLogVo,loginLog);
			this.loginLogServiceImpl.saveOrUpdate(loginLog);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}

	public ILoginLogService getLoginLogServiceImpl() {
		return loginLogServiceImpl;
	}

	public void setLoginLogServiceImpl(ILoginLogService loginLogServiceImpl) {
		this.loginLogServiceImpl = loginLogServiceImpl;
	}
	
}