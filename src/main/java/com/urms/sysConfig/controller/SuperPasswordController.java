package com.urms.sysConfig.controller;


import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.sysConfig.module.SuperPassword;
import com.urms.sysConfig.service.ISuperPasswordService;
import com.urms.user.module.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/urms")
public class SuperPasswordController extends BaseController{
	
	@Autowired
	public ISuperPasswordService superPasswordService;

	/**
	 * 列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @return
	 */
	@RequestMapping(value="/superPassword_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/urms/sysConfig/superPassword_list";
	}

	/**
	 * 数据加载
	 * @param request
	 * @param response
	 * @param superPassword
	 * @param page
	 * @param rows
	 */
	@RequestMapping(value="/superPassword_load") 
	public void load(HttpServletRequest request, HttpServletResponse response, SuperPassword superPassword, Integer page, Integer rows) {
		Pager pager = this.superPasswordService.queryEntityList(page, rows, superPassword);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}

	/**
	 * 编辑新增
	 * @param request
	 * @param superPassword
	 * @return
	 */
	@RequestMapping(value="/superPassword_edit") 
	public String edit(HttpServletRequest request,SuperPassword superPassword) {
		if(StringUtils.isNotBlank(superPassword.getId())){
			SuperPassword sPassword = this.superPasswordService.getEntityById(SuperPassword.class,superPassword.getId());
			BeanUtils.copyProperties(sPassword,superPassword);
			request.setAttribute("superPassword", superPassword);
		}else{
			request.setAttribute("superPassword", superPassword);
		}
		return "/page/urms/sysConfig/superPassword_edit";
	}

	/**
	 * 保存更新
	 * @param httpSession
	 * @param response
	 * @param superPassword
	 */
	@RequestMapping(value="/superPassword_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,SuperPassword superPassword) {
		JsonObject json = new JsonObject();
		try {
			this.superPasswordService.saveOrUpdate(superPassword);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	

}