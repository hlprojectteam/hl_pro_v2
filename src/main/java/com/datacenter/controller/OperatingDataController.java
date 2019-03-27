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
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.datacenter.module.OperatingData;
import com.datacenter.service.IOperatingDataService;
import com.datacenter.vo.OperatingDataVo;
import com.google.gson.JsonObject;

/**
 * @Description 营运数据	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/operatingData")
public class OperatingDataController extends BaseController{

	@Autowired
	private IOperatingDataService operatingDataServiceImpl;
	
	
	/**
	 * 营运数据	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/operatingData_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/operatingData_list";
	}
	
	
	/**
	 * 营运数据	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param operatingDataVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/operatingData_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, OperatingDataVo operatingDataVo){
		Pager pager = this.operatingDataServiceImpl.queryEntityList(page, rows, operatingDataVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 营运数据	编辑
	 * @param request
	 * @param winName
	 * @param operatingDataVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/operatingData_edit")
	public String edit(HttpServletRequest request, String winName, OperatingDataVo operatingDataVo) throws ParseException{
		if(StringUtils.isNotBlank(operatingDataVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(operatingDataVo.getDutyDateStr());
			operatingDataVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(operatingDataVo.getId())){
			OperatingData operatingData = this.operatingDataServiceImpl.getEntityById(OperatingData.class, operatingDataVo.getId());
			BeanUtils.copyProperties(operatingData, operatingDataVo);
			request.setAttribute("operatingDataVo", operatingDataVo);
		}else{
			request.setAttribute("operatingDataVo", operatingDataVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/operatingData_edit";
	}
	
	
	/**
	 * 营运数据	保存or修改
	 * @param response
	 * @param operatingDataVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/operatingData_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, OperatingDataVo operatingDataVo){
		JsonObject json = new JsonObject();
		try {
			OperatingData operatingData = this.operatingDataServiceImpl.saveOrUpdate(operatingDataVo);
			json.addProperty("result", true);
			json.addProperty("id",operatingData.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 营运数据	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/operatingData_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.operatingDataServiceImpl.delete(OperatingData.class, ids);
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
