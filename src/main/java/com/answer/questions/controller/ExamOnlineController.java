package com.answer.questions.controller;



import java.io.IOException;
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

import com.alibaba.fastjson.JSON;
import com.answer.questions.module.ExamManage;
import com.answer.questions.module.ExamPerson;
import com.answer.questions.service.IQuestionsService;
import com.answer.questions.vo.ExamManageVo;
import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.user.module.User;

/**
 * 在线考试
 * @intruduction 
 * @author Dic
 * @Date 2016年9月8日下午2:19:31
 */
@Controller
@RequestMapping("/answer")
public class ExamOnlineController extends BaseController{
	
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
	@RequestMapping(value="/examOnline_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/answer/questions/examOnline/examOnline_list";
	}
	
	/**
	 * 我的考试列表页面
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @return
	 * @author Dic
	 * @Date 2016年9月6日下午3:44:07
	 */
	@RequestMapping(value="/examOnline_myExamList") 
	public String myExamList(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/answer/questions/examOnline/examOnline_myExamList";
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
	@RequestMapping(value="/examOnline_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,ExamManageVo examManageVo,Integer page,Integer rows) {
		Pager pager = this.questionsServiceImpl.queryExamOnlineList(page, rows, examManageVo,this.getSessionUser());
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
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
	@RequestMapping(value="/examOnline_myExamload") 
	public void myExamload(HttpServletRequest request,HttpServletResponse response,ExamManageVo examManageVo,Integer page,Integer rows) {
		Pager pager = this.questionsServiceImpl.queryMyExamOnlineList(page, rows, examManageVo,this.getSessionUser());
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	/**
	 * 开始考试
	 * @intruduction
	 * @param request
	 * @param response
	 * @param examManageVo
	 * @param mark 1:正常开始 2：显示答案
	 * @param sign 1:只显示错题
	 * @return
	 * @author Dic
	 * @Date 2016年10月13日下午4:51:43
	 */
	@RequestMapping(value="/examOnline_start") 
	public String start(HttpServletRequest request,HttpServletResponse response,ExamManageVo examManageVo,Integer mark,Integer sign, Integer redo,String personId) {
		//检查是否生成试题
		boolean chk = true;
		if(!this.questionsServiceImpl.checkExam(examManageVo,this.getSessionUser())){
			chk = this.questionsServiceImpl.submitGenerateExam(examManageVo,this.getSessionUser());//生产试题
		}
		ExamManage examManage = this.questionsServiceImpl.getEntityById(ExamManage.class, examManageVo.getId());
		BeanUtils.copyProperties(examManage, examManageVo);
		//题数 小题 总分
		if(examManage.getSingle()!=null&&examManage.getSingleScore()!=null)
			examManageVo.setSingleTotalScore(examManage.getSingle()*examManage.getSingleScore());
		if(examManage.getMany()!=null&&examManage.getManyScore()!=null)
			examManageVo.setManyTotalScore(examManage.getMany()*examManage.getManyScore());
		if(examManage.getJudge()!=null&&examManage.getJudgeScore()!=null)
			examManageVo.setJudgeTotalScore(examManage.getJudge()*examManage.getJudgeScore());
		if(examManage.getFill()!=null&&examManage.getFillScore()!=null)
			examManageVo.setFillTotalScore(examManage.getFill()*examManage.getFillScore());
		//总分
		Float ingleTotalScore = 0f;
		if(examManageVo.getSingleTotalScore()!=null)
			ingleTotalScore = examManageVo.getSingleTotalScore();
		Float manyTotalScore = 0f;
		if(examManageVo.getManyTotalScore()!=null)
			manyTotalScore = examManageVo.getManyTotalScore();
		Float judgeTotalScore = 0f;
		if(examManageVo.getJudgeTotalScore()!=null)
			judgeTotalScore = examManageVo.getJudgeTotalScore();
		Float fillTotalScore = 0f;
		if(examManageVo.getFillTotalScore()!=null)
			fillTotalScore = examManageVo.getFillTotalScore();
		examManageVo.setTotalScore(ingleTotalScore+manyTotalScore+judgeTotalScore+fillTotalScore);
		this.getRequest().setAttribute("examManageVo", examManageVo);
		this.getRequest().setAttribute("mark", mark);//1：不显示答案  2：显示答案
		this.getRequest().setAttribute("sign", sign);//1:只显示错题
		this.getRequest().setAttribute("redo", redo);//1:在线考试 2:我的考试 根据 刷新不同list列表
		this.getRequest().setAttribute("personId", personId);//考试人员id
		//考试人员记录表
		ExamPerson examPerson = this.questionsServiceImpl.getExamPersonResult(examManageVo.getId(),this.getSessionUser().getId());
		if(examPerson==null){//生成考试人员记录表
			examPerson = new ExamPerson();
			examPerson.setPersonId(this.getSessionUser().getId());
			examPerson.setPersonName(this.getSessionUser().getUserName());
			examPerson.setIsExam(0);//没有完成考试
			examPerson.setExamManage(examManage);
			examPerson.setSysCode(examManage.getSysCode());
			this.questionsServiceImpl.save(examPerson);
		}
		this.getRequest().setAttribute("examPersonVo", examPerson);
		this.getRequest().setAttribute("chk", chk);
		return "/page/answer/questions/examOnline/examOnline_do";
	}
	
	/**
	 * 获得试题
	 * @intruduction
	 * @param request
	 * @param response
	 * @param examManageVo
	 * @param page
	 * @param rows
	 * @param sign 1：只显示错题
	 * @author Dic
	 * @Date 2016年10月13日下午4:55:02
	 */
	@RequestMapping(value="/getOnlineExam") 
	public void getOnlineExam(HttpServletRequest request,HttpServletResponse response,ExamManageVo examManageVo,Integer page,Integer rows,Integer sign,String personId) {
		//获得试题
		User user = null;
		if(sign==null)
			user =this.getQuestionsServiceImpl().getEntityById(User.class, personId);
		else
			user = this.getSessionUser();
		Pager pager = this.questionsServiceImpl.queryOnlineExam(page,rows,examManageVo,user,sign);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		json.put("rows", JSON.toJSONString(pager.getPageList()));
		this.print(json.toString());
		
	}
	
	/**
	 * 保存做的题目
	 * @intruduction
	 * @param request
	 * @param response
	 * @author Dic
	 * @Date 2016年9月18日下午3:44:31
	 */
	@RequestMapping(value="/saveOnlineExam") 
	public void saveOnlineExam(HttpServletRequest request,HttpServletResponse response) {
		String requestStr;
		try {
			requestStr = this.getRequest().getReader().readLine().replaceAll("null", "");
			requestStr = java.net.URLDecoder.decode(requestStr,"UTF-8");
			logger.info("提交试题json数据："+requestStr);
			this.questionsServiceImpl.saveOnlineExam(requestStr,this.getSessionUser());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 检查是否已经做完题目
	 * @intruduction
	 * @param request
	 * @param response
	 * @author Dic
	 * @Date 2016年9月18日下午3:52:39
	 */
	@RequestMapping(value="/checkExamIsOver") 
	public void checkExamIsOver(HttpServletRequest request,HttpServletResponse response,String questionManageIds,String examManageId) {
		JsonObject json = new JsonObject();
		try {
			boolean chk = this.questionsServiceImpl.checkExamIsOver(questionManageIds,examManageId,this.getSessionUser());
			json.addProperty("result", chk);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 提交试卷 计分
	 * @intruduction
	 * @param request
	 * @param response
	 * @author Dic
	 * @Date 2016年9月19日上午11:22:32
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value="/submitOnlineExam") 
	public void submitOnlineExam(HttpServletRequest request,HttpServletResponse response,String examManageId,int consumeTime) {
		JsonObject json = new JsonObject();
		try {
			ExamPerson examPerson = this.questionsServiceImpl.submitOnlineExam(examManageId,this.getSessionUser(), consumeTime);
			json.addProperty("result", true);
			JSONObject jsonObject = new JSONObject();
			jsonObject.fromObject(examPerson);
			json.addProperty("examPerson", jsonObject.toString());
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 保存耗时
	 * @intruduction
	 * @param request
	 * @param response
	 * @param examManageId
	 * @param consumeTime
	 * @author Dic
	 * @Date 2016年9月21日上午9:54:49
	 */
	@RequestMapping(value="/saveOnlineExamConsumeTime") 
	public void saveOnlineExamConsumeTime(HttpServletRequest request,HttpServletResponse response,String examManageId,int consumeTime) {
		JsonObject json = new JsonObject();
		try {
			//考试人员记录表
			ExamPerson examPerson = this.questionsServiceImpl.getExamPersonResult(examManageId,this.getSessionUser().getId());
			examPerson.setConsumeTime(consumeTime);//耗时
			this.questionsServiceImpl.update(examPerson);
			json.addProperty("isExam", examPerson.getIsExam());
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 
	 * @intruduction
	 * @param request
	 * @param response
	 * @param examManageId
	 * @param consumeTime
	 * @author Dic
	 * @Date 2016年9月21日下午3:29:49
	 */
	@RequestMapping(value="/checkExamPerson") 
	public void checkExamPerson(HttpServletRequest request,HttpServletResponse response,String examManageId) {
		JsonObject json = new JsonObject();
		try {
			//考试人员记录表
			ExamPerson examPerson = this.questionsServiceImpl.getExamPersonResult(examManageId,this.getSessionUser().getId());
			if(examPerson==null){
				json.addProperty("isExam", false);
			}else{
				if(examPerson.getIsExam()==1)
					json.addProperty("isExam", true);
				else
					json.addProperty("isExam", false);
			}
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 考试成绩
	 * @intruduction
	 * @param request
	 * @param response
	 * @param examManageVo
	 * @return
	 * @author Dic
	 * @Date 2016年9月19日下午2:57:29
	 */
	@RequestMapping(value="/examOnline_result") 
	public String result(HttpServletRequest request,HttpServletResponse response,ExamManageVo examManageVo,String personId,String redo) {
		ExamManage examManage = this.questionsServiceImpl.getEntityById(ExamManage.class, examManageVo.getId());
		BeanUtils.copyProperties(examManage, examManageVo);
		this.getRequest().setAttribute("examManageVo", examManageVo);
		String userId = "";
		if(StringUtils.isNotBlank(personId))
			userId = personId;
		else
			userId = this.getSessionUser().getId();
		ExamPerson examPerson = this.questionsServiceImpl.getExamPersonResult(examManageVo.getId(),userId);
		this.getRequest().setAttribute("examPersonVo", examPerson);
		this.getRequest().setAttribute("redo", redo);
		return "/page/answer/questions/examOnline/examOnline_result";
	}
	
	public IQuestionsService getQuestionsServiceImpl() {
		return questionsServiceImpl;
	}

	public void setQuestionsServiceImpl(IQuestionsService questionsServiceImpl) {
		this.questionsServiceImpl = questionsServiceImpl;
	}
	
	
}