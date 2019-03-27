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
import com.datacenter.module.TrafficAccident;
import com.datacenter.service.ITrafficAccidentService;
import com.datacenter.vo.TrafficAccidentVo;
import com.google.gson.JsonObject;

/**
 * @Description 交通事故	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/trafficAccident")
public class TrafficAccidentController extends BaseController{

	@Autowired
	private ITrafficAccidentService trafficAccidentServiceImpl;
	
	
	/**
	 * 交通事故	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/trafficAccident_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/trafficAccident_list";
	}
	
	
	/**
	 * 交通事故	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param trafficAccidentVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/trafficAccident_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, TrafficAccidentVo trafficAccidentVo){
		Pager pager = this.trafficAccidentServiceImpl.queryEntityList(page, rows, trafficAccidentVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 交通事故	编辑
	 * @param request
	 * @param winName
	 * @param trafficAccidentVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/trafficAccident_edit")
	public String edit(HttpServletRequest request, String winName, TrafficAccidentVo trafficAccidentVo) throws ParseException{
		if(StringUtils.isNotBlank(trafficAccidentVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(trafficAccidentVo.getDutyDateStr());
			trafficAccidentVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(trafficAccidentVo.getId())){
			TrafficAccident trafficAccident = this.trafficAccidentServiceImpl.getEntityById(TrafficAccident.class, trafficAccidentVo.getId());
			BeanUtils.copyProperties(trafficAccident, trafficAccidentVo);
			request.setAttribute("trafficAccidentVo", trafficAccidentVo);
		}else{
			request.setAttribute("trafficAccidentVo", trafficAccidentVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/trafficAccident_edit";
	}
	
	
	/**
	 * 交通事故	保存or修改
	 * @param response
	 * @param trafficAccidentVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/trafficAccident_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, TrafficAccidentVo trafficAccidentVo){
		JsonObject json = new JsonObject();
		try {
			TrafficAccident trafficAccident = this.trafficAccidentServiceImpl.saveOrUpdate(trafficAccidentVo);
			json.addProperty("result", true);
			json.addProperty("id",trafficAccident.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 交通事故	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/trafficAccident_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.trafficAccidentServiceImpl.delete(TrafficAccident.class, ids);
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
