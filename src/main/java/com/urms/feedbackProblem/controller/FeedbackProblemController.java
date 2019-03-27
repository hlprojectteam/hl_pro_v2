package com.urms.feedbackProblem.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.common.base.controller.BaseController;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.feedbackProblem.module.FeedbackProblem;
import com.urms.feedbackProblem.service.IFeedbackProblemService;
import com.urms.feedbackProblem.vo.FeedbackProblemVo;

@Controller
@RequestMapping("/urms")
public class FeedbackProblemController extends BaseController{
	
	@Autowired
	public IFeedbackProblemService feedbackProblemServiceImpl;
	
	/**
	 * 说明：数据字典列表页面
	 * 输入：@param httpSession
	 * 输入：@param response
	 * 输入：@param categoryVo
	 * 输入：@return
	 * 输出：String
	 * 创建时间:2016-1-5 下午5:12:04
	 */
	@RequestMapping(value="/feedbackProblem_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/urms/feedbackProblem/feedbackProblem_list";
	}
	
	/**
	 * @intruduction 打开问题反馈页面
	 * @param httpSession
	 * @param response
	 * @param categoryVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年6月6日上午10:19:27
	 */
	@RequestMapping(value="/feedbackProblem_add") 
	public String list(HttpSession httpSession,HttpServletResponse response,FeedbackProblemVo feedbackProblemVo) {
		return "/page/urms/feedbackProblem/feedbackProblem_add";
	}
	
	/**
	 * @intruduction 数据字典目录列表
	 * @param request
	 * @param response
	 * @param categoryVo
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:35:24
	 */
	@RequestMapping(value="/feedbackProblem_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,FeedbackProblemVo feedbackProblemVo,Integer page,Integer rows) {
		Pager pager = this.feedbackProblemServiceImpl.queryEntityList(page, rows, feedbackProblemVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	
	/**
	 * @intruduction 编辑数据字典目录树
	 * @param request
	 * @param id
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:37:38
	 */
	@RequestMapping(value="/feedbackProblem_edit") 
	public String edit(HttpServletRequest request,FeedbackProblemVo feedbackProblemVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(feedbackProblemVo.getId())){
			FeedbackProblem feedbackProblem = this.feedbackProblemServiceImpl.getEntityById(FeedbackProblem.class,feedbackProblemVo.getId());
			BeanUtils.copyProperties(feedbackProblem,feedbackProblemVo);
			request.setAttribute("feedbackProblemVo", feedbackProblemVo);
		}else{
			request.setAttribute("feedbackProblemVo", feedbackProblemVo);
		}
		return "/page/urms/feedbackProblem/feedbackProblem_edit";
	}
	
	/**
	 * @intruduction 保存or更新 类型树
	 * @param httpSession
	 * @param response
	 * @param categoryVo
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:38:26
	 */
	@RequestMapping(value="/feedbackProblem_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,@ModelAttribute FeedbackProblemVo feedbackProblemVo) {
		JsonObject json = new JsonObject();
		try {
			FeedbackProblem feedbackProblem = new FeedbackProblem();
			BeanUtils.copyProperties(feedbackProblemVo,feedbackProblem);
			this.feedbackProblemServiceImpl.saveOrUpdate(feedbackProblem);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @intruduction 删除目录树
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:39:08
	 */
	@RequestMapping(value="/feedbackProblem_delete") 
	public void deleteTree(HttpServletResponse response,String ids) {
		this.feedbackProblemServiceImpl.delete(FeedbackProblem.class,ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}

	public IFeedbackProblemService getFeedbackProblemServiceImpl() {
		return feedbackProblemServiceImpl;
	}

	public void setFeedbackProblemServiceImpl(
			IFeedbackProblemService feedbackProblemServiceImpl) {
		this.feedbackProblemServiceImpl = feedbackProblemServiceImpl;
	}

}