package com.urms.sysConfig.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.urms.sysConfig.module.WorkDayConfig;
import com.urms.sysConfig.service.IWorkDayConfigService;
import com.urms.sysConfig.vo.WorkDayConfigVo;

@Controller
@RequestMapping("/urms")
public class WorkDayConfigController extends BaseController{
	
	@Autowired
	public IWorkDayConfigService workDayConfigServiceImpl;
	
	/**
	 * @intruduction 子系统列表页面
	 * @param httpSession
	 * @param response
	 * @param workDayConfigVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:21:41
	 */
	@RequestMapping(value="/workDayConfig_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,WorkDayConfigVo workDayConfigVo,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/urms/sysConfig/workDayConfig_list";
	}
	
	/**
	 * @intruduction 子系统列表数据
	 * @param request
	 * @param response
	 * @param workDayConfigVo
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:22:06
	 */
	@RequestMapping(value="/workDayConfig_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,WorkDayConfigVo workDayConfigVo,Integer page,Integer rows) {
		Pager pager = this.workDayConfigServiceImpl.queryEntityList(page, rows, workDayConfigVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
//		config.setExcludes(new String[]{"menus"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * @intruduction 编辑页面
	 * @param request
	 * @param workDayConfigVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:24:46
	 */
	@RequestMapping(value="/workDayConfig_edit") 
	public String edit(HttpServletRequest request,WorkDayConfigVo workDayConfigVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(workDayConfigVo.getId())){
			WorkDayConfig workDayConfig = this.workDayConfigServiceImpl.getEntityById(WorkDayConfig.class,workDayConfigVo.getId());
			BeanUtils.copyProperties(workDayConfig,workDayConfigVo);
			request.setAttribute("workDayConfigVo", workDayConfigVo);
		}else{
			request.setAttribute("workDayConfigVo", workDayConfigVo);
		}
		return "/page/urms/sysConfig/workDayConfig_edit";
	}
	
	/**
	 * @intruduction 保存子系统
	 * @param httpSession
	 * @param response
	 * @param workDayConfigVo
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:52:46
	 */
	@RequestMapping(value="/workDayConfig_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,WorkDayConfigVo workDayConfigVo) {
		JsonObject json = new JsonObject();
		try {
			WorkDayConfig workDayConfig = new WorkDayConfig();
			BeanUtils.copyProperties(workDayConfigVo,workDayConfig);
			this.workDayConfigServiceImpl.saveOrUpdate(workDayConfig);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @intruduction 删除
	 * @param httpSession
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月12日下午5:08:58
	 */
	@RequestMapping(value="/workDayConfig_delete",method = RequestMethod.POST) 
	public void delete(HttpSession httpSession,HttpServletResponse response,String ids) {
		this.workDayConfigServiceImpl.delete(WorkDayConfig.class,ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	
	
	public IWorkDayConfigService getWorkDayConfigServiceImpl() {
		return workDayConfigServiceImpl;
	}
	public void setWorkDayConfigServiceImpl(IWorkDayConfigService workDayConfigServiceImpl) {
		this.workDayConfigServiceImpl = workDayConfigServiceImpl;
	}
	
}