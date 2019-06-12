package com.answer.questions.controller;



import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.answer.questions.module.Question;
import com.answer.questions.module.QuestionManage;
import com.answer.questions.module.QuestionProblem;
import com.answer.questions.service.IQuestionsService;
import com.answer.questions.vo.QuestionManageVo;
import com.answer.questions.vo.QuestionVo;
import com.common.base.controller.BaseController;
import com.common.dataimport.ImportExcelUtil;
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.common.utils.tld.DictUtils;
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
	
	public static Logger logger_excel = Logger.getLogger("EXCEL");//记录导入日志

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
	
	
	/*******************导入题库 start***********************/
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param winName
	 * @方法：@param operatingDataVo
	 * @方法：@return
	 * @方法：@throws ParseException
	 * @描述：批量导入数据
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月14日
	 */
	@RequestMapping(value="/question_import")
	public String questionImport(HttpServletRequest request, QuestionManageVo questionManageVo) throws ParseException{
		if(org.apache.commons.lang.StringUtils.isNotBlank(questionManageVo.getId())){
			request.setAttribute("questionManageVo", questionManageVo);
		}
		return "/page/answer/questions/questionManage/question_import";
	}
	
	/**
	 * 
	 * @方法：@param file
	 * @方法：@param response
	 * @方法：@return
	 * @方法：@throws Exception
	 * @描述：导入
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月14日
	 */
	@Transactional
	@RequestMapping(value="/question_import_submit",produces = "application/json;charset=utf-8",method=RequestMethod.POST) 
	public void questionImportSubmit(MultipartFile file,HttpServletResponse response,QuestionManageVo questionManageVo) throws Exception{
		InputStream in =null;  
        List<List<Object>> listob = null;  
        if(file.getSize()==0){
        	this.print("请选择文件!");
        	return ;
        }
        in = file.getInputStream();  
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename(),34);  
        in.close();  
        if(StringUtils.isBlank(questionManageVo.getId())){
        	this.print("没关联到题库!");
        	return;
        }
        //找到题库主体，以下的题目都是插入到该题库里
        QuestionManage questionManage=this.questionsServiceImpl.getEntityById(QuestionManage.class, questionManageVo.getId());
          
        logger_excel.info("-------------题库导入开始 开始时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
        logger_excel.info("导入操作用户："+this.getSessionUser().getUserName());
        int successCount=0;//计算失败数和成功数
        StringBuffer strErrMsg=new StringBuffer();
        boolean isImportReady=true;
        String tempTitle="";//记录当前题目内容，与后一条题目对比，相同则视为同一条题目，不相同就是下一条题目
        QuestionVo tempQuestion=new QuestionVo();
        List<QuestionProblem> questionProblemList=new ArrayList<>();
        List<QuestionVo> questionList=new ArrayList<>();//存储所有整理后的题目，待循环插入到数据库
        for (int i = 0; i < listob.size(); i++) {
        	System.out.println("第"+(i+1)+"条数据");
        	List<Object> lo = listob.get(i);  
        	if(lo.isEmpty())	continue;	//去掉多余的行
        	String title=(String)lo.get(0);
        	if(title.isEmpty())	break;//退出循环	
        	if(StringUtils.isEmpty(title)||title.length()>2048){
        		strErrMsg.append("第"+(i+1)+"条数据：【题目】列存有格式问题，或者长度大于2048字节\r\n");
        		isImportReady=false;
        	}
        	String type=(String)lo.get(1);
        	if(StringUtils.isEmpty(type)){
        		strErrMsg.append("第"+(i+1)+"条数据：【题目类型】列存有格式问题，不能为空\r\n");
        		isImportReady=false;
        	}
        	String typeKey= DictUtils.getDictAttrKey("title_type", type);
        	if(StringUtils.isEmpty(typeKey)){
        		strErrMsg.append("第"+(i+1)+"条数据：【题目类型】列存有格式问题,是否【单选】、【多选】，【判断】，【填空】其中的一类\r\n");
        		isImportReady=false;
        	}
        	
        	String no=(String)lo.get(2);
        	if(StringUtils.isEmpty(no)||no.length()>1){
        		strErrMsg.append("第"+(i+1)+"条数据：【选项（题号）】列存有格式问题，或者长度大于1字节\r\n");
        		isImportReady=false;
        	}
        	
        	String option=(String)lo.get(3);
        	if(type.equals("单选")||type.equals("多选")){
        		if(StringUtils.isEmpty(option)||option.length()>1024){
        			strErrMsg.append("第"+(i+1)+"条数据：【选项内容】列存有格式问题，或者长度大于1024字节\r\n");
        			isImportReady=false;
        		}
        	}
        	String answer=(String)lo.get(4);
        	if(StringUtils.isEmpty(answer)||answer.length()>256){
        		strErrMsg.append("第"+(i+1)+"条数据：【答案】列存有格式问题，或者长度大于256字节\r\n");
        		isImportReady=false;
        	}
        	String answerKey="";
        	if(type.equals("单选")||type.equals("多选")||type.equals("判断")){
        		answerKey= DictUtils.getDictAttrKey("isNot", answer);
        		if(StringUtils.isEmpty(answerKey)){
        			strErrMsg.append("第"+(i+1)+"条数据：【答案】列存有格式问题,是否【是】、【否】其中的一项\r\n");
        			isImportReady=false;
        		}
        	}else if(type.equals("填空")){
        		answerKey=answer;
        	}
        	
        	String isRandom=(String)lo.get(5);
        	if(StringUtils.isEmpty(isRandom)){
        		strErrMsg.append("第"+(i+1)+"条数据：【是否随机】列存有格式问题，不能为空\r\n");
        		isImportReady=false;
        	}
        	String isRandomKey= DictUtils.getDictAttrKey("isNot", isRandom);
        	if(StringUtils.isEmpty(isRandomKey)){
        		strErrMsg.append("第"+(i+1)+"条数据：【是否随机】列存有格式问题,是否【是】、【否】其中的一项\r\n");
        		isImportReady=false;
        	}
        	if(isImportReady){
        		//以上excel内容格式检查通过，开始整理实体
        		if (StringUtils.isEmpty(tempTitle)){
        			//开始第一条题目
        			tempTitle=title;
        			//set题目
        			QuestionVo question=new QuestionVo();
        			question.setTitle(title);
        			question.setType(Integer.parseInt(typeKey));
        			question.setCreateTime(new Date());
        			question.setState(1);
        			question.setIsRandom(Integer.parseInt(isRandomKey));
        			question.setQuestionManage(questionManage);
        			question.setSysCode("hl");
        			BeanUtils.copyProperties(question, tempQuestion);
        			
        			questionProblemList=new ArrayList<>();
        			//set选项
        			QuestionProblem questionProblem=new QuestionProblem();	
        			questionProblem.setOption(option);
        			questionProblem.setAnswer(answerKey);
        			questionProblem.setNo(no);
        			questionProblem.setSysCode("hl");
        			questionProblemList.add(questionProblem);
        			
        		}else if(StringUtils.isNotEmpty(tempTitle)&&tempTitle.equals(title)){
        			//同一条题目
        			QuestionProblem questionProblem=new QuestionProblem();	
        			questionProblem.setOption(option);
        			questionProblem.setAnswer(answerKey);
        			questionProblem.setNo(no);
        			questionProblem.setSysCode("hl");
        			questionProblemList.add(questionProblem);
        			
        		}else if(StringUtils.isNotEmpty(tempTitle)&&!tempTitle.equals(title)){
        			//与上一条题目不相同，为新的题目
        			//将上一条题目的所有选项set到题目里，将题目add到题目LIST里
        			tempQuestion.setProblems(questionProblemList);
        			QuestionVo qvo=new QuestionVo();
        			BeanUtils.copyProperties(tempQuestion, qvo);
        			questionList.add(qvo);
        			//开始下一条题目的set
        			tempTitle=title;
        			//set题目
        			QuestionVo question=new QuestionVo();
        			question.setTitle(title);
        			question.setType(Integer.parseInt(typeKey));
        			question.setCreateTime(new Date());
        			question.setState(1);
        			question.setIsRandom(Integer.parseInt(isRandomKey));
        			question.setQuestionManage(questionManage);
        			question.setSysCode("hl");
        			BeanUtils.copyProperties(question, tempQuestion);
        			
        			questionProblemList=new ArrayList<>();
        			//set选项
        			QuestionProblem questionProblem=new QuestionProblem();	
        			questionProblem.setOption(option);
        			questionProblem.setAnswer(answerKey);
        			questionProblem.setNo(no);
        			questionProblem.setSysCode("hl");
        			questionProblemList.add(questionProblem);
        		}
        	}
        }
        if(isImportReady){
        	//添加最后一条记录
        	tempQuestion.setProblems(questionProblemList);
        	questionList.add(tempQuestion);
        	try {
        		//开始插入库
        		for (Question question : questionList) {
        			QuestionVo qvo=new QuestionVo();
        			BeanUtils.copyProperties(question, qvo);
        			if(question.getType()==1||question.getType()==2){
        				this.questionsServiceImpl.saveOrUpdateQuestion(qvo);
        			}else if(question.getType()==3){
        				this.questionsServiceImpl.saveOrUpdateQuestionJudge(qvo);
        			}else if(question.getType()==4){
        				this.questionsServiceImpl.saveOrUpdateQuestionFill(qvo);
        			}
        			successCount++;
        		}
        	} catch (Exception e) {
        		return;
        	}
        	logger_excel.info("导入数："+successCount);
        	logger_excel.info("-------------题库导入结束 结束时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
        	this.print("题库导入完毕,导入数："+successCount);
        }else{
        	strErrMsg.append("----导入失败----");
        	this.print(strErrMsg.toString());
        }
	}
	
	/*******************导入题库 end***********************/
	
	public IQuestionsService getQuestionsServiceImpl() {
		return questionsServiceImpl;
	}

	public void setQuestionsServiceImpl(IQuestionsService questionsServiceImpl) {
		this.questionsServiceImpl = questionsServiceImpl;
	}
	
	
	
}