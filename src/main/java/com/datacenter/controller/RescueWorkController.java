package com.datacenter.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.datacenter.module.RescueWork;
import com.datacenter.service.IRescueWorkService;
import com.datacenter.vo.RescueWorkVo;
import com.google.gson.JsonObject;

/**
 * @Description 拯救作业	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/rescueWork")
public class RescueWorkController extends BaseController{

	@Autowired
	private IRescueWorkService rescueWorkServiceImpl;
	
	
	/**
	 * 拯救作业	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/rescueWork_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/rescueWork_list";
	}
	
	
	/**
	 * 拯救作业	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param rescueWorkVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/rescueWork_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, RescueWorkVo rescueWorkVo){
		Pager pager = this.rescueWorkServiceImpl.queryEntityList(page, rows, rescueWorkVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 拯救作业	编辑
	 * @param request
	 * @param winName
	 * @param rescueWorkVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/rescueWork_edit")
	public String edit(HttpServletRequest request, String winName, RescueWorkVo rescueWorkVo) throws ParseException{
		if(StringUtils.isNotBlank(rescueWorkVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(rescueWorkVo.getDutyDateStr());
			rescueWorkVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(rescueWorkVo.getId())){
			RescueWork rescueWork = this.rescueWorkServiceImpl.getEntityById(RescueWork.class, rescueWorkVo.getId());
			BeanUtils.copyProperties(rescueWork, rescueWorkVo);
			request.setAttribute("rescueWorkVo", rescueWorkVo);
		}else{
			request.setAttribute("rescueWorkVo", rescueWorkVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/rescueWork_edit";
	}
	
	
	/**
	 * 拯救作业	保存or修改
	 * @param response
	 * @param rescueWorkVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/rescueWork_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, RescueWorkVo rescueWorkVo){
		JsonObject json = new JsonObject();
		try {
			RescueWork rescueWork = this.rescueWorkServiceImpl.saveOrUpdate(rescueWorkVo);
			json.addProperty("result", true);
			json.addProperty("id",rescueWork.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 拯救作业	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/rescueWork_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.rescueWorkServiceImpl.delete(RescueWork.class, ids);
				json.addProperty("result", true);
			}
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
}
