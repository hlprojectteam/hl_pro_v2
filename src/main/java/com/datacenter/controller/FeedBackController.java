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
import com.datacenter.module.FeedBack;
import com.datacenter.service.IFeedBackService;
import com.datacenter.vo.FeedBackVo;
import com.google.gson.JsonObject;

/**
 * @Description 顾客意见反馈	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/feedBack")
public class FeedBackController extends BaseController{

	@Autowired
	private IFeedBackService feedBackServiceImpl;
	
	
	/**
	 * 顾客意见反馈	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/feedBack_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/feedBack_list";
	}
	
	
	/**
	 * 顾客意见反馈	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param feedBackVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/feedBack_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, FeedBackVo feedBackVo){
		Pager pager = this.feedBackServiceImpl.queryEntityList(page, rows, feedBackVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 顾客意见反馈	编辑
	 * @param request
	 * @param winName
	 * @param feedBackVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/feedBack_edit")
	public String edit(HttpServletRequest request, String winName, FeedBackVo feedBackVo) throws ParseException{
		if(StringUtils.isNotBlank(feedBackVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(feedBackVo.getDutyDateStr());
			feedBackVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(feedBackVo.getId())){
			FeedBack feedBack = this.feedBackServiceImpl.getEntityById(FeedBack.class, feedBackVo.getId());
			BeanUtils.copyProperties(feedBack, feedBackVo);
			request.setAttribute("feedBackVo", feedBackVo);
		}else{
			request.setAttribute("feedBackVo", feedBackVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/feedBack_edit";
	}
	
	
	/**
	 * 顾客意见反馈	保存or修改
	 * @param response
	 * @param feedBackVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/feedBack_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, FeedBackVo feedBackVo){
		JsonObject json = new JsonObject();
		try {
			FeedBack feedBack = this.feedBackServiceImpl.saveOrUpdate(feedBackVo);
			json.addProperty("result", true);
			json.addProperty("id",feedBack.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 顾客意见反馈	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/feedBack_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.feedBackServiceImpl.delete(FeedBack.class, ids);
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
