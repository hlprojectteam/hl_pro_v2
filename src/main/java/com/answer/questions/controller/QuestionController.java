package com.answer.questions.controller;



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

import com.answer.questions.module.Question;
import com.answer.questions.module.QuestionManage;
import com.answer.questions.module.QuestionProblem;
import com.answer.questions.service.IQuestionsService;
import com.answer.questions.vo.QuestionVo;
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
public class QuestionController extends BaseController{
	
	@Autowired
	public IQuestionsService questionsServiceImpl;

	/**
	 * 
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param winName
	 * @return
	 * @author Dic
	 * @Date 2016年9月8日下午3:49:29
	 */
	@RequestMapping(value="/question_edit") 
	public String edit(HttpSession httpSession,HttpServletResponse response,String winName,String id) {
		this.getRequest().setAttribute("winName", winName);
		this.getRequest().setAttribute("questionManageId", id);
		return "/page/answer/questions/questionManage/question_edit";
	}
	
	/**
	 * 单选
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param winName
	 * @param id
	 * @return
	 * @author Dic
	 * @Date 2016年9月8日下午4:43:49
	 */
	@RequestMapping(value="/question_edit_single") 
	public String editSingle(HttpSession httpSession,HttpServletResponse response,String questionManageId,String id,String winName) {
		this.getRequest().setAttribute("questionManageId", questionManageId);
		this.getRequest().setAttribute("winName", winName);
		QuestionVo questionVo = new QuestionVo();
		if(StringUtils.isNotBlank(id)){//
			Question question = this.getQuestionsServiceImpl().getEntityById(Question.class, id);
			this.getRequest().setAttribute("questionVo", question);
		}else{
			this.getRequest().setAttribute("questionVo", questionVo);
		}
		return "/page/answer/questions/questionManage/question_edit_single";
	}
	
	/**
	 * 多选（不定项）
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param questionManageId
	 * @param id
	 * @return
	 * @author Dic
	 * @Date 2016年9月12日下午1:57:21
	 */
	@RequestMapping(value="/question_edit_many") 
	public String editMany(HttpSession httpSession,HttpServletResponse response,String questionManageId,String id,String winName) {
		this.getRequest().setAttribute("questionManageId", questionManageId);
		this.getRequest().setAttribute("winName", winName);
		QuestionVo questionVo = new QuestionVo();
		if(StringUtils.isNotBlank(id)){//
			Question question = this.getQuestionsServiceImpl().getEntityById(Question.class, id);
			this.getRequest().setAttribute("questionVo", question);
		}else{
			this.getRequest().setAttribute("questionVo", questionVo);
		}
		return "/page/answer/questions/questionManage/question_edit_many";
	}
	
	/**
	 * 判断
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param questionManageId
	 * @param id
	 * @return
	 * @author Dic
	 * @Date 2016年9月12日下午1:58:48
	 */
	@RequestMapping(value="/question_edit_judge") 
	public String editJudge(HttpSession httpSession,HttpServletResponse response,String questionManageId,String id,String winName) {
		this.getRequest().setAttribute("questionManageId", questionManageId);
		this.getRequest().setAttribute("winName", winName);
		QuestionVo questionVo = new QuestionVo();
		if(StringUtils.isNotBlank(id)){//
			Question question = this.getQuestionsServiceImpl().getEntityById(Question.class, id);
			BeanUtils.copyProperties(question, questionVo);
			for (QuestionProblem qp : question.getQuestionProblems()) {  
				questionVo.setQuestionProblemId(qp.getId());
			    questionVo.setQuestionProblemAnswer(qp.getAnswer());
			    questionVo.setQuestionProblemOption(qp.getOption());
			    questionVo.setQuestionProblemNo(qp.getNo());
			}
			this.getRequest().setAttribute("questionVo", questionVo);
		}else{
			this.getRequest().setAttribute("questionVo", questionVo);
		}
		return "/page/answer/questions/questionManage/question_edit_judge";
	}
	
	/**
	 * 填空题
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param questionManageId
	 * @param id
	 * @return
	 * @author Dic
	 * @Date 2016年9月12日下午1:59:16
	 */
	@RequestMapping(value="/question_edit_fill") 
	public String editFill(HttpSession httpSession,HttpServletResponse response,String questionManageId,String id,String winName) {
		this.getRequest().setAttribute("questionManageId", questionManageId);
		this.getRequest().setAttribute("winName", winName);
		QuestionVo questionVo = new QuestionVo();
		if(StringUtils.isNotBlank(id)){//
			Question question = this.getQuestionsServiceImpl().getEntityById(Question.class, id);
			BeanUtils.copyProperties(question, questionVo);
			for (QuestionProblem qp : question.getQuestionProblems()) {  
				questionVo.setQuestionProblemId(qp.getId());
			    questionVo.setQuestionProblemAnswer(qp.getAnswer());
			    questionVo.setQuestionProblemOption(qp.getOption());
			    questionVo.setQuestionProblemNo(qp.getNo());
			}
			this.getRequest().setAttribute("questionVo", questionVo);
		}else{
			this.getRequest().setAttribute("questionVo", questionVo);
		}
		return "/page/answer/questions/questionManage/question_edit_fill";
	}
	
	/**
	 * 保存单选和多选
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param questionVo
	 * @author Dic
	 * @Date 2016年9月12日下午3:37:08
	 */
	@RequestMapping(value="/question_saveOrUpdate") 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,QuestionVo questionVo) {
		JsonObject json = new JsonObject();
		try {
			this.questionsServiceImpl.saveOrUpdateQuestion(questionVo);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 保存判断填空
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param questionVo
	 * @author Dic
	 * @Date 2016年9月12日下午3:37:25
	 */
	@RequestMapping(value="/question_saveOrUpdateJudge") 
	public void saveOrUpdateJudge(HttpSession httpSession,HttpServletResponse response,QuestionVo questionVo) {
		JsonObject json = new JsonObject();
		try {
			this.questionsServiceImpl.saveOrUpdateQuestionJudge(questionVo);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 保存判断填空
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param questionVo
	 * @author Dic
	 * @Date 2016年9月12日下午3:37:25
	 */
	@RequestMapping(value="/question_saveOrUpdateFill") 
	public void saveOrUpdateFill(HttpSession httpSession,HttpServletResponse response,QuestionVo questionVo) {
		JsonObject json = new JsonObject();
		try {
			this.questionsServiceImpl.saveOrUpdateQuestionFill(questionVo);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 题库题型列表
	 * @intruduction
	 * @param request
	 * @param response
	 * @param questionManageVo
	 * @param page
	 * @param rows
	 * @author Dic
	 * @Date 2016年9月9日上午9:28:26
	 */
	@RequestMapping(value="/question_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,QuestionVo questionVo,String questionManageId,Integer page,Integer rows) {
		QuestionManage questionManage = new QuestionManage();
		questionManage.setId(questionManageId);
		questionVo.setQuestionManage(questionManage);
		Pager pager = this.questionsServiceImpl.queryQuestionList(page, rows, questionVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"questionManage","questionProblems"});  //只要设置这个数组，指定过滤哪些字段。     
		config.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	/**
	 * 删除问题
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param ids
	 * @author Dic
	 * @Date 2016年9月9日上午10:57:47
	 */
	@RequestMapping(value="/question_delete") 
	public void delete(HttpSession httpSession,HttpServletResponse response,String ids) {
		JsonObject json = new JsonObject();
		try {
			this.questionsServiceImpl.deleteQuestion(ids);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
		}
		this.print(json.toString());
	}	
	
	/**
	 * 获得问题详情
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param id
	 * @author Dic
	 * @Date 2016年10月14日上午10:47:17
	 */
	@RequestMapping(value="/getQuestion") 
	public String getQuestion(HttpSession httpSession,HttpServletResponse response,QuestionVo questionVo,String examManageId) {
		Question question = this.questionsServiceImpl.getEntityById(Question.class, questionVo.getId());
		BeanUtils.copyProperties(question, questionVo);
		this.getRequest().setAttribute("questionVo", questionVo);
		this.getRequest().setAttribute("examManageId", examManageId);
		return "/page/answer/questions/questionManage/question_detail";
	}
	
	public IQuestionsService getQuestionsServiceImpl() {
		return questionsServiceImpl;
	}

	public void setQuestionsServiceImpl(IQuestionsService questionsServiceImpl) {
		this.questionsServiceImpl = questionsServiceImpl;
	}
	
}