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
import com.datacenter.module.Brief;
import com.datacenter.service.IBriefService;
import com.datacenter.vo.BriefVo;
import com.google.gson.JsonObject;

/**
 * @Description 工作简报	控制层
 * @author xuezb
 * @date 2019年2月18日
 */
@Controller
@RequestMapping("/datecenter/brief")
public class BriefController extends BaseController{

	@Autowired
	private IBriefService briefServiceImpl;
	
	
	/**
	 * 工作简报	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月18日
	 */
	@RequestMapping(value="/brief_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/brief_list";
	}
	
	
	/**
	 * 工作简报	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param briefVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月18日
	 */
	@RequestMapping(value="/brief_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, BriefVo briefVo){
		Pager pager = this.briefServiceImpl.queryEntityList(page, rows, briefVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 工作简报	编辑
	 * @param request
	 * @param winName
	 * @param briefVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月18日
	 */
	@RequestMapping(value="/brief_edit")
	public String edit(HttpServletRequest request, String winName, BriefVo briefVo) throws ParseException{
		if(StringUtils.isNotBlank(briefVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(briefVo.getDutyDateStr());
			briefVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(briefVo.getId())){
			Brief brief = this.briefServiceImpl.getEntityById(Brief.class, briefVo.getId());
			BeanUtils.copyProperties(brief, briefVo);
			request.setAttribute("briefVo", briefVo);
		}else{
			request.setAttribute("briefVo", briefVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/brief_edit";
	}
	
	
	/**
	 * 工作简报	保存or修改
	 * @param response
	 * @param briefVo
	 * @author xuezb 
	 * @Date 2019年2月18日
	 */
	@RequestMapping(value="/brief_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, BriefVo briefVo){
		JsonObject json = new JsonObject();
		try {
			Brief brief = this.briefServiceImpl.saveOrUpdate(briefVo);
			json.addProperty("result", true);
			json.addProperty("id",brief.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 工作简报	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月18日
	 */
	@RequestMapping(value="/brief_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.briefServiceImpl.delete(Brief.class, ids);
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
