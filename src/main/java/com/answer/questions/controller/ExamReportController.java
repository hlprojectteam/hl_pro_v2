package com.answer.questions.controller;




import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.answer.questions.service.IExamReportService;
import com.answer.questions.service.IQuestionsService;
import com.answer.questions.vo.ExamManageVo;
import com.answer.questions.vo.ExamReportVo;
import com.answer.questions.vo.QuestionVo;
import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.google.gson.Gson;

/**
 * 在线考试报表统计
 * @intruduction 
 * @author Dic
 * @Date 2016年9月8日下午2:19:31
 */
@Controller
@RequestMapping("/answer")
public class ExamReportController extends BaseController{
	
	@Autowired
	public IQuestionsService questionsServiceImpl;
	@Autowired
	public IExamReportService examReportServiceImpl;
	/**
	 * 报表页面
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param winName
	 * @param examManageId
	 * @return
	 * @author Dic
	 * @Date 2016年9月23日上午11:07:11
	 */
	@RequestMapping(value="/examOnline_report") 
	public String report(HttpSession httpSession,HttpServletResponse response,String winName,ExamManageVo examManageVo) {
		this.getRequest().setAttribute("winName", winName);
		this.getRequest().setAttribute("examManageVo", examManageVo);
		return "/page/answer/questions/examManage/examManage_report";
	}
	
	/**
	 * 获得完成率
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param winName
	 * @param examManageVo
	 * @author Dic
	 * @Date 2016年10月14日下午4:23:34
	 */
	@RequestMapping(value="/getBaseReport") 
	public void getBaseReport(HttpSession httpSession,HttpServletResponse response,String winName,ExamManageVo examManageVo,String orgFrameId) {
		ExamReportVo examReportVo = examReportServiceImpl.getBaseReport(examManageVo.getId(),orgFrameId);
		Gson gson =  new Gson();
		this.print(gson.toJson(examReportVo));
	}
	
	@RequestMapping(value="/examOnline_sourceReport") 
	public void sourceReport(HttpSession httpSession,HttpServletResponse response,String examManageId,String orgFrameId) {
		String json = examReportServiceImpl.getExamSourceReport(examManageId,orgFrameId);
		this.print(json.toString());
	}
	
	/**
	 * 易错题 分页
	 * @intruduction
	 * @param request
	 * @param response
	 * @param examManageVo
	 * @param page
	 * @param rows
	 * @author Dic
	 * @Date 2016年10月14日上午10:28:06
	 */
	@RequestMapping(value="/worryReport_load") 
	public void worryReportLoad(HttpServletRequest request,HttpServletResponse response,ExamManageVo examManageVo,Integer page,Integer rows,String orgFrameId) {
		Pager pager = this.examReportServiceImpl.getWorryReport(page, rows, examManageVo.getId(),orgFrameId);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	/**
	 * 做题人员
	 * @intruduction
	 * @param request
	 * @param response
	 * @param examManageVo
	 * @param page
	 * @param rows
	 * @author Dic
	 * @Date 2016年10月14日上午11:47:45
	 */
	@RequestMapping(value="/worryPerson_load") 
	public void worryPersonLoad(HttpServletRequest request,HttpServletResponse response,QuestionVo questionVo,Integer page,Integer rows,String examManageId) {
		Pager pager = this.examReportServiceImpl.getWorryPerson(page, rows, questionVo.getId(),examManageId);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	public IQuestionsService getQuestionsServiceImpl() {
		return questionsServiceImpl;
	}

	public void setQuestionsServiceImpl(IQuestionsService questionsServiceImpl) {
		this.questionsServiceImpl = questionsServiceImpl;
	}
	
	
}