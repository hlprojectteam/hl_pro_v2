package com.answer.questions.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.answer.questions.dao.IQuestionsDao;
import com.answer.questions.module.ExamManage;
import com.answer.questions.module.ExamPerson;
import com.answer.questions.module.ExamPersonQuestion;
import com.answer.questions.module.Question;
import com.answer.questions.module.QuestionManage;
import com.answer.questions.module.QuestionProblem;
import com.answer.questions.ql.QuestionsQl;
import com.answer.questions.service.IQuestionsService;
import com.answer.questions.vo.ExamManageVo;
import com.answer.questions.vo.ExamPersonVo;
import com.answer.questions.vo.ExamQuestionVo;
import com.answer.questions.vo.QuestionManageVo;
import com.answer.questions.vo.QuestionProblemVo;
import com.answer.questions.vo.QuestionVo;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.common.utils.helper.SpringUtils;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.role.module.Role;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

@Repository("questionsServiceImpl")
public class QuestionsServiceImpl extends BaseServiceImpl implements IQuestionsService{
	
	@Autowired
	public IQuestionsDao questionsDaoImpl;
	@Autowired
	public IOrgFrameService orgFrameServiceImpl;
	@Autowired
	public IUserService userServiceImpl;
	
	public IQuestionsDao getQuestionsDaoImpl() {
		return questionsDaoImpl;
	}
	public void setQuestionsDaoImpl(IQuestionsDao questionsDaoImpl) {
		this.questionsDaoImpl = questionsDaoImpl;
	}
	public IOrgFrameService getOrgFrameServiceImpl() {
		return orgFrameServiceImpl;
	}
	public void setOrgFrameServiceImpl(IOrgFrameService orgFrameServiceImpl) {
		this.orgFrameServiceImpl = orgFrameServiceImpl;
	}
	@Override
	public void saveOrUpdate(QuestionManageVo questionManageVo){
		QuestionManage questionManage = new QuestionManage();
		BeanUtils.copyProperties(questionManageVo, questionManage);
		if(StringUtils.isBlank(questionManageVo.getId())){
			this.save(questionManage);
		}else{
			this.update(questionManage);	
		}
	}
	

	@Override
	public Pager queryEntityList(int page,int rows,QuestionManageVo questionManageVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(questionManageVo.getSubject()))
			criterionsList.add(Restrictions.like("subject","%"+questionManageVo.getSubject()+"%"));
		return this.questionsDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime") ,QuestionManage.class);
	}

	@Override
	public void deleteQuestionManage(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			QuestionManage questionManage = this.getEntityById(QuestionManage.class, idz[i]);
//			for (InquireQuestion iq : inquire.getInquireQuestions()) {
//				this.delete(iq);
//			}
			this.delete(questionManage);
		}
	}

	//单选 多选
	@Override
	public void saveOrUpdateQuestion(QuestionVo questionVo) {
		Question question = new Question();
		BeanUtils.copyProperties(questionVo, question);
		if(StringUtils.isBlank(questionVo.getId())){
			question.setNum(DateUtil.getYYMMDDHHMMSS(""));
			this.save(question);
			for (QuestionProblem qp : questionVo.getProblems()) {
				qp.setQuestion(question);
				this.save(qp);
			}
		}else{
			this.update(question);
			for (QuestionProblem qp : questionVo.getProblems()) {
				qp.setQuestion(question);
				this.update(qp);
			}
		}
	}


	//保存判断
	@Override
	public void saveOrUpdateQuestionJudge(QuestionVo questionVo) {
		Question question = new Question();
		BeanUtils.copyProperties(questionVo, question);
		if(StringUtils.isBlank(questionVo.getId())){
			question.setNum(DateUtil.getYYMMDDHHMMSS(""));
			this.save(question);
			QuestionProblem qp = new QuestionProblem();
			qp.setAnswer(questionVo.getQuestionProblemAnswer());
			qp.setNo(questionVo.getQuestionProblemNo());
			qp.setOption(questionVo.getQuestionProblemOption());
			qp.setQuestion(question);
			this.save(qp);
		}else{
			this.update(question);
			List<Criterion> criterionsList = new ArrayList<Criterion>();
			criterionsList.add(Restrictions.eq("question.id", question.getId()));
			List<QuestionProblem> list = this.baseDaoImpl.queryEntityList(criterionsList, null, QuestionProblem.class);
			if(list.size()>0){
				QuestionProblem qp =list.get(0);
				qp.setAnswer(questionVo.getQuestionProblemAnswer());//修改判断题答案
				this.update(qp);
			}
		}
	}

	//保存填空
	@Override
	public void saveOrUpdateQuestionFill(QuestionVo questionVo) {
		Question question = new Question();
		BeanUtils.copyProperties(questionVo, question);
		if(StringUtils.isBlank(questionVo.getId())){
			question.setNum(DateUtil.getYYMMDDHHMMSS(""));
			this.save(question);
			for (QuestionProblem qp : questionVo.getProblems()) {
				qp.setOption("填空题");
				qp.setQuestion(question);
				this.save(qp);
			}
		}else{
			this.update(question);
			for (QuestionProblem qp : questionVo.getProblems()) {
				qp.setOption("填空题");
				qp.setQuestion(question);
				this.update(qp);
			}
		}
	}
	
	@Override
	public Pager queryQuestionList(Integer page, Integer rows, QuestionVo questionVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("type", questionVo.getType()));
		criterionsList.add(Restrictions.eq("questionManage.id", questionVo.getQuestionManage().getId()));
		return this.questionsDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime") ,Question.class);
	}

	//删除题目
	@Override
	public void deleteQuestion(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			Question question = this.getEntityById(Question.class, idz[i]);
			for (QuestionProblem questionProblem : question.getQuestionProblems()) {
				this.delete(questionProblem);
			}
			this.delete(question);
		}
	}

	//----------试卷--------------------------------------------------------
	@Override
	public Pager queryExamManageList(Integer page, Integer rows,ExamManageVo examManageVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(examManageVo.getSubject()))
			criterionsList.add(Restrictions.eq("subject",examManageVo.getSubject()));
		if(examManageVo.getType() != null){
			criterionsList.add(Restrictions.eq("type", examManageVo.getType()));
		}
		if(examManageVo.getState() != null){
			criterionsList.add(Restrictions.eq("state", examManageVo.getState()));
		}
		return this.questionsDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime") ,ExamManage.class);
	}


	@Override
	public void saveOrUpdateExamManage(ExamManageVo examManageVo) {
		ExamManage examManage = new ExamManage();
		BeanUtils.copyProperties(examManageVo, examManage);
		if(StringUtils.isBlank(examManageVo.getId())){
			//获得当前做题人员
			Map<String,String> map = new HashMap<String,String>();
			if(StringUtils.isNotBlank(examManage.getUserIds())){//人员
				String[] idz = examManage.getUserIds().split(",");
				String[] namez = examManage.getUserNames().split(",");
				for (int i = 0; i < idz.length; i++) {
					map.put(idz[i], namez[i]);
				}
			}else if(StringUtils.isNotBlank(examManage.getRoleIds())){//角色
				String[] idz = examManage.getRoleIds().split(",");
				for (int i = 0; i < idz.length; i++){
					Role role = this.getEntityById(Role.class, idz[i]);
					for (User user : role.getUsers()) {
						if(user.getState()==1)//正常状态下
							map.put(user.getId(), user.getUserName());
					}	
				}
			}else if(StringUtils.isNotBlank(examManage.getOrgFrameIds())){//组织
				String[] idz = examManage.getOrgFrameIds().split(",");
				for (int i = 0; i < idz.length; i++){
					//当前节点
					OrgFrame orgFrame = this.getEntityById(OrgFrame.class, idz[i]);
					for (User user : orgFrame.getUsers()) {
						if(user.getState()==1)//正常状态下
							map.put(user.getId(), user.getUserName());
					}
					//下级节点
					IOrgFrameService orgFrameServiceImpl = (IOrgFrameService)SpringUtils.getBean("orgFrameServiceImpl");
					List<OrgFrame> list = orgFrameServiceImpl.getOrgFrameChildren(orgFrame.getId());//获得下级所有节点结合
					for (OrgFrame of : list) {
						for (User user : of.getUsers()) {
							if(user.getState()==1)//正常状态下
								map.put(user.getId(), user.getUserName());
						}
					}
				}
			}
			this.save(examManage);
			//保存考试人员
			for (String key : map.keySet()) {
				ExamPerson ep = new ExamPerson();
				ep.setPersonId(key);
				ep.setPersonName(map.get(key));
				User user = this.getEntityById(User.class, key);
				ep.setOrgFrameId(user.getOrgFrame().getId());//所属部门
				ep.setOrgFrameName(user.getOrgFrame().getOrgFrameName());
				ep.setIsExam(0);//没有考试
				ep.setExamManage(examManage);
				ep.setSysCode(examManage.getSysCode());
				this.save(ep);
			}
		}else{
			this.update(examManage);	
		}
	}


	@Override
	public int deleteExamManage(String ids) {
		String[] idz = ids.split(",");
		int make = 0;
		for (int i = 0; i < idz.length; i++) {
			List<Criterion> criterionsList = new ArrayList<Criterion>();
			criterionsList.add(Restrictions.eq("examManage.id", idz[i]));
			//检查是否已经有人考试
			List<ExamPersonQuestion> epqList = this.questionsDaoImpl.queryEntityList(criterionsList, null ,ExamPersonQuestion.class);
			if(epqList.size()==0){
				ExamManage examManage = this.getEntityById(ExamManage.class, idz[i]);
				//由于有外键关，所以需先删除考试的人员
				List<ExamPerson> list = this.questionsDaoImpl.queryEntityList(criterionsList, null ,ExamPerson.class);
				for (ExamPerson examPerson : list) {
					this.delete(examPerson);
				}
				this.delete(examManage);
				make = 1;
			}
		}
		return make;
	}


	@Override
	public Pager queryExamPsersonList(Integer page, Integer rows,String sort,String order ,ExamPersonVo examPersonVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("examManage.id", examPersonVo.getExamManage().getId()));
		if(StringUtils.isNotBlank(examPersonVo.getPersonName()))
			criterionsList.add(Restrictions.like("personName", "%"+examPersonVo.getPersonName()+"%"));
		if(StringUtils.isNotBlank(examPersonVo.getJobNumber())){//工号
			UserVo userVo = new UserVo();
			userVo.setJobNumber(examPersonVo.getJobNumber());
			List<User> userList = this.userServiceImpl.queryUserList(userVo);
			if(userList!=null){
				if(userList.size()>0){
					criterionsList.add(Restrictions.eq("personId", userList.get(0).getId()));
				}else{
					criterionsList.add(Restrictions.eq("personId", null));
				}
			}
		}
		if(StringUtils.isNotBlank(examPersonVo.getOrgFrameId())){
			List<OrgFrame> ofList = orgFrameServiceImpl.getOrgFrameChildren(examPersonVo.getOrgFrameId());
			if(ofList.size()>0){			
				String[] orgFrameIds = new String[ofList.size()];
				for (int i = 0; i < ofList.size(); i++) {
					orgFrameIds[i] = ofList.get(i).getId();
				}
				criterionsList.add(Restrictions.in("orgFrameId", orgFrameIds));//下级所有用户	
			}else{
				criterionsList.add(Restrictions.eq("orgFrameId", examPersonVo.getOrgFrameId()));	
			}
		}
		if(StringUtils.isNotBlank(sort)){//有排序
			if(order.equals("desc"))
				return this.questionsDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc(sort) ,ExamPerson.class);
			else
				return this.questionsDaoImpl.queryEntityList(page, rows, criterionsList, Order.asc(sort) ,ExamPerson.class);
		}else{
			return this.questionsDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime") ,ExamPerson.class);			
		}
	}


	@Override
	public void deleteExamPerson(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			ExamPerson examPerson = this.getEntityById(ExamPerson.class, idz[i]);
			//删除考试人员做的试题
			List<Criterion> criterionsList = new ArrayList<Criterion>();
			criterionsList.add(Restrictions.eq("personId", examPerson.getPersonId()));
			criterionsList.add(Restrictions.eq("examManage.id", examPerson.getExamManage().getId()));
			List<ExamPersonQuestion> epqList = this.baseDaoImpl.queryEntityList(criterionsList, null, ExamPersonQuestion.class);//考试人的答题
			for (ExamPersonQuestion epq :epqList) {
				this.delete(epq);
			}
			this.delete(examPerson);//删除考试人员
		}
	}

	//可以考试的试卷
	@Override
	public Pager queryExamOnlineList(Integer page, Integer rows,ExamManageVo examManageVo,User user) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("personId", user.getId()));
		criterionsList.add(Restrictions.eq("isExam", 0));//没有考试
		List<ExamPerson> list = this.questionsDaoImpl.queryEntityList(criterionsList, null, ExamPerson.class);//考试人员列表
		StringBuffer sb = new StringBuffer();
		for (ExamPerson examPerson : list) {
			sb.append("'"+examPerson.getExamManage().getId()+"',");
		}
		//状态是1发布  时间限制
		List<Object> attributeList = new ArrayList<Object>();
		Pager pager = new Pager(0, 0, attributeList);
		String queryExamOnline = QuestionsQl.MySql.queryExamOnline;
		if(StringUtils.isNotBlank(examManageVo.getSubject())){//试卷名称
			queryExamOnline = queryExamOnline.replace("##",  " and subject_ like ? ");
			attributeList.add("%"+examManageVo.getSubject()+"%");
		}else{
			queryExamOnline = queryExamOnline.replace("##",  "");
		}
		if(sb.length()>0){//考试人员列表没有人 即对所有人开放考试
			queryExamOnline = queryExamOnline.replace("@@",  sb.deleteCharAt(sb.length()-1).toString());
		}else{
			queryExamOnline = queryExamOnline.replace("@@",  "''");
		}	
		pager = this.questionsDaoImpl.queryEntitySQLList(page, rows, queryExamOnline, attributeList);
		List<ExamManage> emList = new ArrayList<ExamManage>();
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			ExamManage em = new ExamManage();
			if(obj[0]!=null) em.setId(obj[0].toString());
			if(obj[1]!=null) em.setSubject(obj[1].toString());
			if(obj[2]!=null) em.setBeginTime(DateUtil.getDateFromString(obj[2].toString()));
			if(obj[3]!=null) em.setEndTime(DateUtil.getDateFromString(obj[3].toString()));
			if(obj[4]!=null) em.setExamTime(Integer.parseInt(obj[4].toString()));
			if(obj[5]!=null) em.setType(Integer.parseInt(obj[5].toString()));
			emList.add(em);
		}
		pager.setPageList(emList);
		return pager;
	}

	//撤销 发布
	@Override
	public String saveChangeState(String id) {
		ExamManage examManage = this.getEntityById(ExamManage.class, id);
		String sign = "";
		if(examManage.getState()==1){
			examManage.setState(0);		
			sign = "down";
		}else{
			examManage.setState(1);		
			sign = "up";
		}
		this.update(examManage);
		return sign;
	}

	//试题生成
	@Override
	public boolean submitGenerateExam(ExamManageVo examManageVo,User user) {
		boolean chk = true;
		int n = 100;//随机查询100次 如果还没有查出题目 就跳出 避免死循环
		//获得考试题型
		ExamManage examManage = this.getEntityById(ExamManage.class, examManageVo.getId());
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		List<Object> sList = new ArrayList<Object>();//随机出题集合
		List<Question> bList = new ArrayList<Question>();//必出题集合
		if(examManage.getSingle()!=null){
			if(examManage.getSingle()>0){//单选
				criterionsList.add(Restrictions.eq("state", 1));//状态 1 正常
				criterionsList.add(Restrictions.eq("isRandom", 0));//0,表示不是随机
				criterionsList.add(Restrictions.in("questionManage.id", examManage.getQuestionManageIds().split(",")));
				criterionsList.add(Restrictions.eq("type", 1));//1 单选
				List<Question> qList = this.questionsDaoImpl.queryEntityList(criterionsList, null, Question.class);//必出问题
				if(examManage.getSingle()>qList.size()){//必选题目 小于 设定需要题目
					int num = examManage.getSingle()-qList.size();//随机出题数
					List<Object> qrList;
					int a = 0;
					while(true){
						qrList = this.randomExamQuestion("1", examManage.getQuestionManageIds(), num);
						if(qrList.size()==num){
							break;
						}
						a++;
						if(a==n){//
							chk = false;
							break;
						}
					}
					sList.addAll(qrList);//
					bList.addAll(qList);
				}else{//必出题 大于 设定需要题目 只选择部分
					for (int i = 0; i < examManage.getSingle(); i++) {
						bList.add(qList.get(i));
					}
//					bList.addAll(qList);
				}
				//保存单选
				if(chk){
					int k = 1;
					for (Question question : bList) {//必出题
						ExamPersonQuestion epq = new ExamPersonQuestion();
						epq.setSysCode(user.getSysCode());//系统编码
						epq.setPersonId(user.getId());//人员id
						epq.setPersonName(user.getUserName());//人员
						epq.setOrder(k++);//题号 排序
						epq.setQuestion(question);
						epq.setExamManage(examManage);
						this.save(epq);
					}
					for (int i = 0; i < sList.size(); i++) {//随机出题
						Object[] obj = (Object[])sList.get(i);
						ExamPersonQuestion epq = new ExamPersonQuestion();
						epq.setSysCode(user.getSysCode());//系统编码
						epq.setPersonId(user.getId());//人员id
						epq.setPersonName(user.getUserName());//人员
						epq.setOrder(k++);//题号 排序
						Question q = new Question();
						q.setId(obj[0].toString());//id
						epq.setQuestion(q);
						epq.setExamManage(examManage);
						this.save(epq);
					}
				}
			}
		}
		if(examManage.getMany()!=null){
			if(examManage.getMany()>0){//多选
				sList.clear();//清除集合
				bList.clear();//清除集合
				criterionsList.clear();
				criterionsList.add(Restrictions.eq("state", 1));//状态 1 正常
				criterionsList.add(Restrictions.eq("isRandom", 0));//0,表示不是随机
				criterionsList.add(Restrictions.in("questionManage.id", examManage.getQuestionManageIds().split(",")));
				criterionsList.add(Restrictions.eq("type", 2));//2 多选
				List<Question> qList = this.questionsDaoImpl.queryEntityList(criterionsList, null, Question.class);//必出问题
				if(examManage.getMany()>qList.size()){//必选题目 小于 设定需要题目
					int num = examManage.getMany()-qList.size();//随机出题数
					List<Object> qrList;
					int a = 0;
					while(true){
						qrList = this.randomExamQuestion("2", examManage.getQuestionManageIds(), num);
						if(qrList.size()==num){
							break;
						}
						a++;
						if(a==n){//
							chk = false;
							break;
						}
					}
					sList.addAll(qrList);//
					bList.addAll(qList);
				}else{//必出题
					for (int i = 0; i < examManage.getMany(); i++) {
						bList.add(qList.get(i));
					}
//					bList.addAll(qList);
				}
				//保存多选
				if(chk){
					int k = 1;
					for (Question question : bList) {//必出题
						ExamPersonQuestion epq = new ExamPersonQuestion();
						epq.setSysCode(user.getSysCode());//系统编码
						epq.setPersonId(user.getId());//人员id
						epq.setPersonName(user.getUserName());//人员
						epq.setOrder(k++);//题号 排序
						epq.setQuestion(question);
						epq.setExamManage(examManage);
						this.save(epq);
					}
					for (int i = 0; i < sList.size(); i++) {//随机出题
						Object[] obj = (Object[])sList.get(i);
						ExamPersonQuestion epq = new ExamPersonQuestion();
						epq.setSysCode(user.getSysCode());//系统编码
						epq.setPersonId(user.getId());//人员id
						epq.setPersonName(user.getUserName());//人员
						epq.setOrder(k++);//题号 排序
						Question q = new Question();
						q.setId(obj[0].toString());//id
						epq.setQuestion(q);
						epq.setExamManage(examManage);
						this.save(epq);
					}
				}
			}
		}
		if(examManage.getJudge()!=null){//判断
			if(examManage.getJudge()>0){//判断
				sList.clear();//清除集合
				bList.clear();//清除集合
				criterionsList.clear();
				criterionsList.add(Restrictions.eq("state", 1));//状态 1 正常
				criterionsList.add(Restrictions.eq("isRandom", 0));//0,表示不是随机
				criterionsList.add(Restrictions.in("questionManage.id", examManage.getQuestionManageIds().split(",")));
				criterionsList.add(Restrictions.eq("type", 3));//3判断
				List<Question> qList = this.questionsDaoImpl.queryEntityList(criterionsList, null, Question.class);//问题
				if(examManage.getJudge()>qList.size()){//必选题目 小于 设定需要题目
					int num = examManage.getJudge()-qList.size();//随机出题数
					List<Object> qrList;
					int a = 0;
					while(true){
						qrList = this.randomExamQuestion("3", examManage.getQuestionManageIds(), num);
						if(qrList.size()==num){
							break;
						}
						a++;
						if(a==n){//
							chk = false;
							break;
						}
					}
					sList.addAll(qrList);//
					bList.addAll(qList);
				}else{//必出题
//					bList.addAll(qList);
					for (int i = 0; i < examManage.getJudge(); i++) {
						bList.add(qList.get(i));
					}
				}
				if(chk){
					//保存判断
					int k = 1;
					for (Question question : bList) {//必出题
						ExamPersonQuestion epq = new ExamPersonQuestion();
						epq.setSysCode(user.getSysCode());//系统编码
						epq.setPersonId(user.getId());//人员id
						epq.setPersonName(user.getUserName());//人员
						epq.setOrder(k++);//题号 排序
						epq.setQuestion(question);
						epq.setExamManage(examManage);
						this.save(epq);
					}
					for (int i = 0; i < sList.size(); i++) {//随机出题
						Object[] obj = (Object[])sList.get(i);
						ExamPersonQuestion epq = new ExamPersonQuestion();
						epq.setSysCode(user.getSysCode());//系统编码
						epq.setPersonId(user.getId());//人员id
						epq.setPersonName(user.getUserName());//人员
						epq.setOrder(k++);//题号 排序
						Question q = new Question();
						q.setId(obj[0].toString());//id
						epq.setQuestion(q);
						epq.setExamManage(examManage);
						this.save(epq);
					}			
				}
			}
		}
		if(examManage.getFill()!=null){
			if(examManage.getFill()>0){//填空
				sList.clear();//清除集合
				bList.clear();//清除集合
				criterionsList.clear();
				criterionsList.add(Restrictions.eq("state", 1));//状态 1 正常
				criterionsList.add(Restrictions.eq("isRandom", 0));//0,表示不是随机
				criterionsList.add(Restrictions.in("questionManage.id", examManage.getQuestionManageIds().split(",")));
				criterionsList.add(Restrictions.eq("type", 4));//4 填空
				List<Question> qList = this.questionsDaoImpl.queryEntityList(criterionsList, null, Question.class);//问题
				if(examManage.getFill()>qList.size()){//必选题目 小于 设定需要题目
					int num = examManage.getFill()-qList.size();//随机出题数
					List<Object> qrList;
					int a = 0;
					while(true){
						qrList = this.randomExamQuestion("4", examManage.getQuestionManageIds(), num);
						if(qrList.size()==num){
							break;
						}
						a++;
						if(a==n){//
							chk = false;
							break;
						}
					}
					sList.addAll(qrList);//
					bList.addAll(qList);
				}else{//必出题
					for (int i = 0; i < examManage.getFill(); i++) {
						bList.add(qList.get(i));
					}
//					bList.addAll(qList);
				}
				//保存填空
				if(chk){
					int k = 1;
					for (Question question : bList) {//必出题
						ExamPersonQuestion epq = new ExamPersonQuestion();
						epq.setSysCode(user.getSysCode());//系统编码
						epq.setPersonId(user.getId());//人员id
						epq.setPersonName(user.getUserName());//人员
						epq.setOrder(k++);//题号 排序
						epq.setQuestion(question);
						epq.setExamManage(examManage);
						this.save(epq);
					}
					for (int i = 0; i < sList.size(); i++) {//随机出题
						Object[] obj = (Object[])sList.get(i);
						ExamPersonQuestion epq = new ExamPersonQuestion();
						epq.setSysCode(user.getSysCode());//系统编码
						epq.setPersonId(user.getId());//人员id
						epq.setPersonName(user.getUserName());//人员
						epq.setOrder(k++);//题号 排序
						Question q = new Question();
						q.setId(obj[0].toString());//id
						epq.setQuestion(q);
						epq.setExamManage(examManage);
						this.save(epq);
					}
				}
			}
		}
		return chk;
	}

	//随机出题
	public List<Object> randomExamQuestion(String type,String questionManageIds, Integer num){
		List<Object> attributeList = new ArrayList<Object>();
		attributeList.add(num);
		String sql = QuestionsQl.MySql.randomExamQuestion2.replaceAll("@@",type);//填空
		sql = sql.replaceAll("##", questionManageIds.replaceAll(",", "','").toString());
		return this.questionsDaoImpl.queryEntitySQLList(sql, attributeList);//随机
	}
	
	//检查试题是否生成
	@Override
	public boolean checkExam(ExamManageVo examManageVo, User user) {
		ExamManage examManage = this.getEntityById(ExamManage.class, examManageVo.getId());
		String sql = QuestionsQl.MySql.examPersonQuestionNum.replace("?", user.getId());//试题人
				sql = sql.replace("##", examManage.getQuestionManageIds().replaceAll(",", "','"));
				sql = sql.replace("@@", examManage.getId());//处于不同考题
		long count = this.questionsDaoImpl.queryCountBySql(sql);
		if(count>0)
			return true;
		else
			return false;
	}
	
	@Override
	public Pager queryOnlineExam(Integer page, Integer rows, ExamManageVo examManageVo , User user, Integer sign) {
		List<Object> attributeList = new ArrayList<Object>();
		attributeList.add(user.getId());//当前登录人
		attributeList.add(examManageVo.getId());//所属考题
		String queryOnlineExam = "";
		if(sign!=null){
			queryOnlineExam = QuestionsQl.HQL.queryOnlineExam.replace("##", " and epq.isRight !=? ");
			attributeList.add(sign);//只显示错题
		}else{
			queryOnlineExam = QuestionsQl.HQL.queryOnlineExam.replace("##", " ");
		}
		Pager pager = this.questionsDaoImpl.queryEntityHQLList(page, rows, queryOnlineExam, attributeList);
		List<ExamQuestionVo> list = new ArrayList<ExamQuestionVo>();
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			Question q = (Question)obj[0];
			ExamPersonQuestion epq = (ExamPersonQuestion)obj[1];
			ExamQuestionVo eqVo = new ExamQuestionVo();
			eqVo.setQuestionId(q.getId());
			eqVo.setTitle(q.getTitle());
			eqVo.setType(q.getType());//题目类型
			eqVo.setIsRight(epq.getIsRight());//是否正确
			if(StringUtils.isNotBlank(epq.getPersonAnswer()))
				eqVo.setPersonAnswer(epq.getPersonAnswer());//考试人员的答案
			else
				eqVo.setPersonAnswer("");//考试人员的答案
			eqVo.setOrder(epq.getOrder());//题号 排序
			//寻找答案
			List<Criterion> qpCriterionsList = new ArrayList<Criterion>();
			qpCriterionsList.add(Restrictions.eq("question.id", q.getId()));
			if(q.getType()==1){//单选
				qpCriterionsList.add(Restrictions.eq("answer", "1"));
				List<QuestionProblem> pqList = this.baseDaoImpl.queryEntityList(qpCriterionsList, null, QuestionProblem.class);//问题答案
				QuestionProblem qp = pqList.get(0);
				eqVo.setAnswer(qp.getNo());//正确答案
			}else if(q.getType()==2){//多选
				qpCriterionsList.add(Restrictions.eq("answer", "1"));
				List<QuestionProblem> pqList = this.baseDaoImpl.queryEntityList(qpCriterionsList, null, QuestionProblem.class);//问题答案
				StringBuffer sb = new StringBuffer();
				for (QuestionProblem qp : pqList) {
					sb.append(qp.getNo()+",");
				}
				eqVo.setAnswer(sb.deleteCharAt(sb.length()-1).toString());//正确答案
			}else if(q.getType()==3){//判断
				List<QuestionProblem> pqList = this.baseDaoImpl.queryEntityList(qpCriterionsList, null, QuestionProblem.class);//问题答案
				eqVo.setAnswer(pqList.get(0).getAnswer());//正确答案
			}else if(q.getType()==4){//填空
				List<QuestionProblem> pqList = this.baseDaoImpl.queryEntityList(qpCriterionsList, null, QuestionProblem.class);//问题答案
				eqVo.setAnswer(pqList.get(0).getAnswer());//正确答案
				StringBuffer sb = new StringBuffer();
				for (QuestionProblem qp : pqList) {
					sb.append(qp.getAnswer()+",");
				}
				eqVo.setAnswer(sb.deleteCharAt(sb.length()-1).toString());//正确答案
			}
			List<QuestionProblemVo> eqVoList = new ArrayList<QuestionProblemVo>();
			for (QuestionProblem questionProblem :q.getQuestionProblems()) {
				QuestionProblemVo qpVo = new QuestionProblemVo();
				qpVo.setId(questionProblem.getId());
				qpVo.setOption(questionProblem.getOption());//选项
				qpVo.setAnswer(questionProblem.getAnswer());//答案
				qpVo.setNo(questionProblem.getNo());//题号
				eqVoList.add(qpVo);
			}
			eqVo.setQuestionProblem(eqVoList);
			list.add(eqVo);
		}
		pager.setPageList(list);
		return pager;
	}


	@Override
	public boolean checkExamIsOver(String questionManageIds,String examManageId,User user) {
		String sql = QuestionsQl.MySql.checkExamIsOver.replace("?", user.getId());//试题人
				sql = sql.replace("##", questionManageIds.replaceAll(",", "','"));
				sql = sql.replace("@@", examManageId);
		long count = this.questionsDaoImpl.queryCountBySql(sql);
		if(count>0)
			return false;
		else
			return true;
	}


	@SuppressWarnings("static-access")
	@Override
	public void saveOnlineExam(String requestStr,User user) {
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonObj = jsonArray.fromObject(requestStr.replace("{", "[").replace("}", "]").replace(":", ","));
		String examManageId = "";
		for (int i = 0; i < jsonObj.size(); i++) {
			if(jsonObj.getString(i).equals("examManageId"))
				examManageId = jsonObj.getString(i+1);
		}
		
		for (int i = 0; i < jsonObj.size(); i++) {
			if(!jsonObj.getString(i).equals("examManageId")){
				String questionType = jsonObj.getString(i).split("_")[0];//问题type
				String questionId = jsonObj.getString(i).split("_")[1];//问题id
				String answer = jsonObj.getString(i+1);
	            if(StringUtils.isNotBlank(answer)){
					List<Criterion> criterionsList = new ArrayList<Criterion>();
					criterionsList.add(Restrictions.eq("personId", user.getId()));
					criterionsList.add(Restrictions.eq("question.id", questionId));
					criterionsList.add(Restrictions.eq("examManage.id", examManageId));//试题管理
					List<ExamPersonQuestion> epqList = this.baseDaoImpl.queryEntityList(criterionsList, null, ExamPersonQuestion.class);//考试人的答题
					if(epqList.size()>0){
						ExamPersonQuestion epq = epqList.get(0);//考题
						List<Criterion> qpCriterionsList = new ArrayList<Criterion>();
						qpCriterionsList.add(Restrictions.eq("question.id", questionId));
						if(questionType.contains("single")){//单选
							qpCriterionsList.add(Restrictions.eq("answer", "1"));
							List<QuestionProblem> pqList = this.baseDaoImpl.queryEntityList(qpCriterionsList, null, QuestionProblem.class);//问题答案
							QuestionProblem qp = pqList.get(0);
							if(qp.getNo().equals(answer))
								epq.setIsRight(1);//答案相同
							else
								epq.setIsRight(0);//答案不相同
							epq.setPersonAnswer(answer);
							this.update(epq);
						}else if(questionType.contains("many")){//多选
							qpCriterionsList.add(Restrictions.eq("answer", "1"));
							List<QuestionProblem> pqList = this.baseDaoImpl.queryEntityList(qpCriterionsList, Order.asc("no"), QuestionProblem.class);//问题答案
							StringBuffer sb = new StringBuffer();
							for (QuestionProblem questionProblem : pqList) {
								sb.append(","+questionProblem.getNo());
							}
							if(sb.toString().equals(answer))
								epq.setIsRight(1);//答案相同
							else
								epq.setIsRight(0);//答案不相同
							epq.setPersonAnswer(answer);
							this.update(epq);
						}else if(questionType.contains("judge")){//判断
							List<QuestionProblem> pqList = this.baseDaoImpl.queryEntityList(qpCriterionsList, null, QuestionProblem.class);//问题答案
							QuestionProblem qp = pqList.get(0);
							if(qp.getAnswer().equals(answer))
								epq.setIsRight(1);//答案相同
							else
								epq.setIsRight(0);//答案不相同
							epq.setPersonAnswer(answer);
							this.update(epq);
						}else if(questionType.contains("fill")){//填空
							List<QuestionProblem> pqList = this.baseDaoImpl.queryEntityList(qpCriterionsList, null, QuestionProblem.class);//问题答案
							String no = jsonObj.getString(i).split("_")[2];//答案序号
							QuestionProblem qp = pqList.get(Integer.parseInt(no));//an答案顺序
							if(no.equals("0")){
								epq.setPersonAnswer(answer);
								if(qp.getAnswer().indexOf(answer)>-1)
									epq.setIsRight(1);//答案相同
								else
									epq.setIsRight(0);//答案不相同
							}else{
								epq.setPersonAnswer(epq.getPersonAnswer()+","+answer);
								if(epq.getIsRight()==1){//如果第一个答案是对才能进入对比
									if(qp.getAnswer().indexOf(answer)>-1)
										epq.setIsRight(1);//答案相同
									else
										epq.setIsRight(0);//答案不相同
								}
							}
							this.update(epq);
						}
					}
	            }
	            i++;
			}else{
				i++;
			}
        }
	}

	//提交试卷 计分
	@Override
	public ExamPerson submitOnlineExam(String examManageId, User user,int consumeTime) {
		ExamManage examManage = this.questionsDaoImpl.getEntityById(ExamManage.class, examManageId);
		String sql = QuestionsQl.MySql.examRight.replace("?", user.getId());//试题人
		sql = sql.replace("##", examManage.getQuestionManageIds().replaceAll(",", "','"));
		sql = sql.replace("&&", examManageId);
		//考试人员记录表
		ExamPerson examPerson = this.getExamPersonResult(examManageId,user.getId());
		if(examPerson==null)//如果没有记录考试人员信息 即 所有人都可以参加考试
			examPerson = new ExamPerson();
		//单选
		Float singleTotalSource = 0f;
		if(examManage.getSingle()!=null){
			String singleSql = sql.replace("@@","1");
			int count = (int)this.questionsDaoImpl.queryCountBySql(singleSql);//答对题数	
			examPerson.setSingleRight(count);
			singleTotalSource = count*examManage.getSingleScore();//
			examPerson.setSingleTotalSource(singleTotalSource);
		}
		//多选
		Float manyTotalSource = 0f;
		if(examManage.getMany()!=null){
			String manySql = sql.replace("@@","2");
			int count = (int)this.questionsDaoImpl.queryCountBySql(manySql);
			examPerson.setManyRight(count);
			manyTotalSource = count*examManage.getManyScore();//
			examPerson.setManyTotalSource(manyTotalSource);
		}
		//判断
		Float judgeTotalSource = 0f;
		if(examManage.getJudge()!=null){
			String judgeSql = sql.replace("@@","3");
			int count = (int)this.questionsDaoImpl.queryCountBySql(judgeSql);
			examPerson.setJudgeRight(count);
			judgeTotalSource = count*examManage.getJudgeScore();//
			examPerson.setJudgeTotalSource(judgeTotalSource);
		}
		//填空
		Float fillTotalSource = 0f;
		if(examManage.getFill()!=null){
			String fillSql = sql.replace("@@","4");
			int count = (int)this.questionsDaoImpl.queryCountBySql(fillSql);
			examPerson.setFillRight(count);
			fillTotalSource = count*examManage.getFillScore();//
			examPerson.setFillTotalSource(fillTotalSource);
		}
		examPerson.setTotalSource(singleTotalSource+manyTotalSource+judgeTotalSource+fillTotalSource);//总分
		examPerson.setIsExam(1);//已经考试
		examPerson.setConsumeTime(consumeTime);//耗时 s秒
		if(StringUtils.isNotBlank(examPerson.getId())){
			this.update(examPerson);			
		}else{
			examPerson.setPersonId(user.getId());
			examPerson.setPersonName(user.getUserName());
			examPerson.setSysCode(user.getSysCode());
			examPerson.setExamManage(examManage);
			this.save(examPerson);			
		}
		return examPerson;
	}


	@Override
	public ExamPerson getExamPersonResult(String examManageId, String userId) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("personId", userId));
		criterionsList.add(Restrictions.eq("examManage.id", examManageId));
		List<ExamPerson> epList = this.baseDaoImpl.queryEntityList(criterionsList, null, ExamPerson.class);
		if(epList.size()>0)
			return epList.get(0);
		else
			return null;
	}
	
	//我的在线考试 已考
		@Override
		public Pager queryMyExamOnlineList(Integer page, Integer rows, ExamManageVo examManageVo, User user) {
			List<Object> attributeList = new ArrayList<Object>();
			Pager pager = new Pager(0, 0, attributeList);
			attributeList.add(user.getId());
			String myOnlineExam = QuestionsQl.MySql.myOnlineExam;
			if(StringUtils.isNotBlank(examManageVo.getSubject())){//试卷名称
				myOnlineExam += " and paem.subject_ like ? ";
				attributeList.add("%"+examManageVo.getSubject()+"%");
			}
			pager = this.baseDaoImpl.queryEntitySQLList(page, rows, myOnlineExam, attributeList);
			List<ExamPersonVo> emList = new ArrayList<ExamPersonVo>();
			for (int i = 0; i < pager.getPageList().size(); i++) {
				Object[] obj = (Object[])pager.getPageList().get(i);
				ExamPersonVo epVo = new ExamPersonVo();
				ExamManage examManage = new ExamManage();
				examManage.setId(obj[0].toString());
				if(obj[0]!=null) epVo.setExamManage(examManage);
				if(obj[1]!=null) epVo.setSubject(obj[1].toString());
				if(obj[2]!=null) epVo.setTotalSource(Float.parseFloat(obj[2].toString()));//总分
				if(obj[3]!=null) epVo.setConsumeTime(Integer.parseInt(obj[3].toString()));//耗时
				emList.add(epVo);
			}
			pager.setPageList(emList);
			return pager;
		}
		
		@Override
		public void saveAddPerson(ExamManageVo examManageVo) {
			List<Criterion> criterionsList = new ArrayList<Criterion>();
			criterionsList.add(Restrictions.eq("examManage.id", examManageVo.getId()));
			List<ExamPerson> epList = this.baseDaoImpl.queryEntityList(criterionsList, null, ExamPerson.class);
			StringBuffer sb = new StringBuffer();
			for (ExamPerson examPerson : epList) {
				sb.append(examPerson.getPersonId()+",");//排重
			}
			String[] userIdz = examManageVo.getUserIds().split(",");
			String[] userNames = examManageVo.getUserNames().split(",");
			ExamManage examManage = new ExamManage();
			BeanUtils.copyProperties(examManageVo, examManage);
			for (int i = 0; i < userIdz.length; i++) {
				if(sb.toString().indexOf(userIdz[i])>-1){//人员已经存在 不需要添加
					continue;
				}else{					
					ExamPerson ep = new ExamPerson();
					ep.setPersonId(userIdz[i]);
					ep.setPersonName(userNames[i]);
					User user = this.getEntityById(User.class, userIdz[i]);
					ep.setOrgFrameId(user.getOrgFrame().getId());//所属部门
					ep.setOrgFrameName(user.getOrgFrame().getOrgFrameName());
					ep.setIsExam(0);//没有考试
					ep.setExamManage(examManage);
					ep.setSysCode(examManage.getSysCode());
					this.save(ep);
				}
			}
		}
		
		
		@Override
		public boolean checkQMIsUse(String id) {
			List<Criterion> criterionsList = new ArrayList<Criterion>();
			if(StringUtils.isNotBlank(id))
				criterionsList.add(Restrictions.like("questionManageIds","%"+id+"%"));
			List<ExamManage> list = this.questionsDaoImpl.queryEntityList(criterionsList, null, ExamManage.class);
			if(list.size()>0){
				return true;
			}else{
				return false;
			}
		}
		
		@Override
		public long checkIsExam(ExamManageVo examManageVo) {
			List<Object> attr = new ArrayList<Object>();
			attr.add(examManageVo.getId());
			return this.baseDaoImpl.queryCountByHql(QuestionsQl.HQL.queryExamPersonCount, attr);
		}
	
	
}
