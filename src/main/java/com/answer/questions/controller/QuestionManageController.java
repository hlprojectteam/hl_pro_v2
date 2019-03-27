package com.answer.questions.controller;



import java.util.Date;

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

import com.answer.questions.module.QuestionManage;
import com.answer.questions.service.IQuestionsService;
import com.answer.questions.vo.QuestionManageVo;
import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;

/**
 * 题库管理
 * @intruduction 
 * @author Dic
 * @Date 2016年9月8日下午2:19:31
 */
@Controller
@RequestMapping("/answer")
public class QuestionManageController extends BaseController{
	
	@Autowired
	public IQuestionsService questionsServiceImpl;
	
	/**
	 * 列表页面
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @return
	 * @author Dic
	 * @Date 2016年9月6日下午3:44:07
	 */
	@RequestMapping(value="/questionManage_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/answer/questions/questionManage/questionManage_list";
	}
	
	/**
	 * 加载列表
	 * @intruduction
	 * @param request
	 * @param response
	 * @param inquireVo
	 * @param page
	 * @param rows
	 * @author Dic
	 * @Date 2016年9月6日下午3:44:17
	 */
	@RequestMapping(value="/questionManage_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,QuestionManageVo questionManageVo,Integer page,Integer rows) {
		Pager pager = this.questionsServiceImpl.queryEntityList(page, rows, questionManageVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"questions"});  //只要设置这个数组，指定过滤哪些字段。     
		config.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 新增or编辑 页面
	 * @param request
	 * @param userVo
	 * @return
	 * @author Dic
	 * @Date 2016年1月6日下午4:13:07
	 */
	@RequestMapping(value="/questionManage_edit") 
	public String edit(HttpServletRequest request,QuestionManageVo questionManageVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(questionManageVo.getId())){
			QuestionManage questionManage = this.questionsServiceImpl.getEntityById(QuestionManage.class, questionManageVo.getId());
			BeanUtils.copyProperties(questionManage,questionManageVo);
			request.setAttribute("questionManageVo", questionManageVo);
		}else{
			questionManageVo.setSysCode(this.getSessionUser().getSysCode());
			request.setAttribute("inquireVo", questionManageVo);//新增
		}
		return "/page/answer/questions/questionManage/questionManage_edit";
	}
	
	/**
	 * 保持or更新
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param inquireVo
	 * @author Dic
	 * @Date 2016年9月6日下午3:44:31
	 */
	@RequestMapping(value="/questionManage_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,QuestionManageVo questionManageVo) {
		JsonObject json = new JsonObject();
		try {
			this.questionsServiceImpl.saveOrUpdate(questionManageVo);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}

	
	/**
	 * 删除
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param ids
	 * @author Dic
	 * @Date 2016年9月6日下午4:17:05
	 */
	@RequestMapping(value="/questionManage_delete",method = RequestMethod.POST) 
	public void delete(HttpSession httpSession,HttpServletResponse response,String ids) {
		JsonObject json = new JsonObject();
		try {
			this.questionsServiceImpl.deleteQuestionManage(ids);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
		}
		this.print(json.toString());
	}
	
	/**
	 * 题库选择页
	 * @intruduction
	 * @param request
	 * @param questionManageVo
	 * @return
	 * @author Dic
	 * @Date 2016年9月9日下午3:17:06
	 */
	@RequestMapping(value="/questionManage_choose") 
	public String choose(HttpServletRequest request,String winName, String id,String name) {
		this.getRequest().setAttribute("winName", winName);
		this.getRequest().setAttribute("id", id);
		this.getRequest().setAttribute("name", name);
		return "/page/answer/questions/questionManage/questionManage_list_choose";
	}
	
	/**
	 * 检查题库是否已经被使用
	 * @intruduction
	 * @param request
	 * @param winName
	 * @param id
	 * @param name
	 * @author Dic
	 * @Date 2016年10月13日上午9:00:48
	 */
	@RequestMapping(value="/checkQMIsUse") 
	public void checkQMIsUse(HttpServletRequest request,HttpServletResponse response, String id) {
		JsonObject json = new JsonObject();
		try {
			boolean chk = this.questionsServiceImpl.checkQMIsUse(id);
			json.addProperty("result", chk);
		} catch (Exception e) {
			json.addProperty("result", false);
		}
		this.print(json.toString());
	}
	
	
	public IQuestionsService getQuestionsServiceImpl() {
		return questionsServiceImpl;
	}

	public void setQuestionsServiceImpl(IQuestionsService questionsServiceImpl) {
		this.questionsServiceImpl = questionsServiceImpl;
	}
	
	
}