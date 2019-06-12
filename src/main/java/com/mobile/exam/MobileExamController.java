package com.mobile.exam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.answer.questions.vo.ExamPersonVo;
import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
/**
 * 
 * @intruduction 
 * @author Will
 * @Date 上午9:37:43
 */
@Controller
@RequestMapping("/mobile_exam")
public class MobileExamController extends BaseController{

	@Autowired
	public IQuestionsService questionsServiceImpl;
	/**
	 * 
	 * @intruduction 
	 * @author Will
	 * @Date 上午9:37:26
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value="/examOnline_start") 
	public void start(HttpServletRequest request,HttpServletResponse response,String id,boolean isRedo) {
		//检查是否生成试题
		ExamManageVo examManageVo = new ExamManageVo();
		examManageVo.setId(id);
		boolean chk = true;
		//如重做，清空之前的记录
		if(isRedo) this.questionsServiceImpl.deletePersonExamRecord(id, this.getSessionUser().getId());
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
		JSONObject json = new JSONObject();
		json.put("examTime", examManageVo.getExamTime());
		json.put("singleScore", examManageVo.getSingleScore());
		json.put("manyScore", examManageVo.getManyScore());
		json.put("judgeScore", examManageVo.getJudgeScore());
		json.put("fillScore", examManageVo.getFillScore());
		json.put("questionManageIds", examManageVo.getQuestionManageIds());
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
		json.put("consumeTime", examPerson.getConsumeTime());
		json.put("chk", chk);
		this.print(json.toString());
	}
	/**
	 * 
	 * @intruduction 移动端在线考试列表
	 * @author Will
	 * @Date 下午3:46:51
	 * @param request
	 * @param response
	 * @param examManageVo
	 * @param page
	 * @param rows
	 */
	@RequestMapping(value="/examOnline_list") 
	public void onlineExamLoad(HttpServletRequest request,HttpServletResponse response,Integer page,Integer rows) {
		ExamManageVo examManageVo =new ExamManageVo();
		Pager pager = this.questionsServiceImpl.queryExamOnlineList(page, rows, examManageVo,this.getSessionUser());
		JSONObject json = new JSONObject();
		json.put("result", "true");
		json.put("total", pager.getRowCount());
		json.put("content",makeOnlineExamList(pager));
		this.print(json.toString());
	}
	/**
	 * 
	 * @intruduction 我的考试列表
	 * @author Will
	 * @Date 下午4:27:01
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 */
	@RequestMapping(value="/myExam_list") 
	public void myExamLoad(HttpServletRequest request,HttpServletResponse response,Integer page,Integer rows) {
		ExamManageVo examManageVo =new ExamManageVo();
		Pager pager = this.questionsServiceImpl.queryMyExamOnlineList(page, rows, examManageVo,this.getSessionUser());
		JSONObject json = new JSONObject();
		json.put("result", "true");
		json.put("total", pager.getRowCount());
		json.put("content", makeMyExamList(pager));
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @intruduction 获得试题
	 * @author Will
	 * @Date 下午5:48:00
	 * @param request
	 * @param response
	 * @param id
	 * @param page
	 * @param rows
	 * @param sign 1：只显示错题
	 */
	@RequestMapping(value="/getOnlineExam") 
	public void getOnlineExam(HttpServletRequest request,HttpServletResponse response,String id,Integer page,Integer rows,Integer sign) {
		//获得试题
		ExamManageVo examManageVo = new ExamManageVo();
		examManageVo.setId(id);
		Pager pager = this.questionsServiceImpl.queryOnlineExam(page,rows,examManageVo,this.getSessionUser(),sign);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		json.put("rows", JSON.toJSONString(pager.getPageList()));
		this.print(json.toString());
		
	}
	/**
	 * 
	 * @intruduction 
	 * @author Will
	 * @Date 上午9:44:33
	 * @param request
	 * @param response
	 * @param answer
	 * @param examMangeId
	 * @param consumeTime
	 */
	@RequestMapping(value="/saveOnlineExam") 
	public void saveOnlineExam(HttpServletRequest request,HttpServletResponse response,String answer,String examManageId, Integer consumeTime){
		JsonObject json = new JsonObject();
		try{
			if(StringUtils.isNotBlank(answer)){
				this.questionsServiceImpl.saveOnlineExam(answer, this.getSessionUser());
			}
			//考试人员记录表
			ExamPerson examPerson = this.questionsServiceImpl.getExamPersonResult(examManageId,this.getSessionUser().getId());
			examPerson.setConsumeTime(consumeTime);//耗时
			this.questionsServiceImpl.update(examPerson);
			json.addProperty("isExam", examPerson.getIsExam());
			json.addProperty("result", true);
		}catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();	
		}finally{
			this.print(json.toString());
		}
	}
	/**
	 * 
	 * @intruduction 
	 * @author Will
	 * @Date 上午10:21:08
	 * @param request
	 * @param response
	 * @param questionManageIds
	 * @param examManageId
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
	 * 
	 * @intruduction 
	 * @author Will
	 * @Date 上午10:29:02
	 * @param request
	 * @param response
	 * @param examManageId
	 * @param consumeTime
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/submitOnlineExam") 
	public void submitOnlineExam(HttpServletRequest request,HttpServletResponse response,String examManageId,int consumeTime) {
		JsonObject json = new JsonObject();
		try {
			ExamPerson examPerson = this.questionsServiceImpl.submitOnlineExam(examManageId,this.getSessionUser(), consumeTime);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	@RequestMapping(value="/examOnline_result") 
	public void queryResult(HttpServletRequest request,HttpServletResponse response,String examManageId) {
		ExamManageVo examManageVo =new ExamManageVo();
		ExamManage examManage = this.questionsServiceImpl.getEntityById(ExamManage.class, examManageId);
		BeanUtils.copyProperties(examManage, examManageVo);
		String userId = this.getSessionUser().getId();
		ExamPerson examPerson = this.questionsServiceImpl.getExamPersonResult(examManageVo.getId(),userId);
		JSONObject json = new JSONObject();
		json.put("result",true);
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("examManageVo", JSONObject.fromObject(examManageVo, config));
		json.put("examPersonVo", JSONObject.fromObject(examPerson, config));
		this.print(json.toString());
	}
	@SuppressWarnings("unchecked")
	private String makeOnlineExamList(Pager pager){
		ArrayList<ExamManage> list = (ArrayList<ExamManage>) pager.getPageList();
		JSONArray array =new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
		for(int i = 0; i < list.size(); i++){
			ExamManage examManage = list.get(i);
			JSONObject obj =new JSONObject();
			obj.put("id", examManage.getId());
			obj.put("beginTime", sdf.format(examManage.getBeginTime()));
			obj.put("endTime", sdf.format(examManage.getEndTime()));
			obj.put("examTime", examManage.getExamTime());
			obj.put("type", examManage.getType());
			obj.put("subject", examManage.getSubject());
			ExamPerson examPerson = this.questionsServiceImpl.getExamPersonResult(examManage.getId(),this.getSessionUser().getId());
			if(examPerson==null){
				obj.put("isExam", false);
			}else{
				if(examPerson.getIsExam()==1)
					obj.put("isExam", true);
				else
					obj.put("isExam", false);
			}
			array.add(obj);
		}
		return array.toString();
	}
	@SuppressWarnings("unchecked")
	private String makeMyExamList(Pager pager){
		ArrayList<ExamPersonVo> list = (ArrayList<ExamPersonVo>) pager.getPageList();
		JSONArray array =new JSONArray();
//		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
		for(int i = 0; i< list.size(); i++){
			ExamPersonVo examPersonVo = list.get(i);
			ExamManage examManage = examPersonVo.getExamManage();
			JSONObject obj =new JSONObject();
			obj.put("totalSource", examPersonVo.getTotalSource());
			obj.put("subject", examPersonVo.getSubject());
			obj.put("consumeTime", examPersonVo.getConsumeTime());
			obj.put("examManageId", examManage.getId());
			obj.put("type", examPersonVo.getType());
			array.add(obj);
		}
		return array.toString();
	}

}
