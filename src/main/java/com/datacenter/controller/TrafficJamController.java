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
import com.datacenter.module.TrafficJam;
import com.datacenter.service.ITrafficJamService;
import com.datacenter.vo.TrafficJamVo;
import com.google.gson.JsonObject;

/**
 * @Description 交通阻塞	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/trafficJam")
public class TrafficJamController extends BaseController{

	@Autowired
	private ITrafficJamService trafficJamServiceImpl;
	
	
	/**
	 * 交通阻塞	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/trafficJam_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/trafficJam_list";
	}
	
	
	/**
	 * 交通阻塞	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param trafficJamVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/trafficJam_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, TrafficJamVo trafficJamVo){
		Pager pager = this.trafficJamServiceImpl.queryEntityList(page, rows, trafficJamVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 交通阻塞	编辑
	 * @param request
	 * @param winName
	 * @param trafficJamVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/trafficJam_edit")
	public String edit(HttpServletRequest request, String winName, TrafficJamVo trafficJamVo) throws ParseException{
		if(StringUtils.isNotBlank(trafficJamVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(trafficJamVo.getDutyDateStr());
			trafficJamVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(trafficJamVo.getId())){
			TrafficJam trafficJam = this.trafficJamServiceImpl.getEntityById(TrafficJam.class, trafficJamVo.getId());
			BeanUtils.copyProperties(trafficJam, trafficJamVo);
			request.setAttribute("trafficJamVo", trafficJamVo);
		}else{
			request.setAttribute("trafficJamVo", trafficJamVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/trafficJam_edit";
	}
	
	
	/**
	 * 交通阻塞	保存or修改
	 * @param response
	 * @param trafficJamVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/trafficJam_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, TrafficJamVo trafficJamVo){
		JsonObject json = new JsonObject();
		try {
			TrafficJam trafficJam = this.trafficJamServiceImpl.saveOrUpdate(trafficJamVo);
			json.addProperty("result", true);
			json.addProperty("id",trafficJam.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 交通阻塞	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/trafficJam_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.trafficJamServiceImpl.delete(TrafficJam.class, ids);
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
