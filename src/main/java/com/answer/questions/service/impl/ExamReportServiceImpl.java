package com.answer.questions.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.answer.questions.dao.IQuestionsDao;
import com.answer.questions.module.ExamManage;
import com.answer.questions.module.ExamPerson;
import com.answer.questions.module.ExamPersonQuestion;
import com.answer.questions.ql.QuestionsQl;
import com.answer.questions.service.IExamReportService;
import com.answer.questions.vo.ExamPersonQuestionVo;
import com.answer.questions.vo.ExamReportVo;
import com.answer.questions.vo.QuestionVo;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.common.utils.helper.SpringUtils;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.user.module.User;

@Repository("examReportServiceImpl")
public class ExamReportServiceImpl extends BaseServiceImpl implements IExamReportService{
	
	@Autowired
	public IQuestionsDao questionsDaoImpl;
	
	public IQuestionsDao getQuestionsDaoImpl() {
		return questionsDaoImpl;
	}
	public void setQuestionsDaoImpl(IQuestionsDao questionsDaoImpl) {
		this.questionsDaoImpl = questionsDaoImpl;
	}
	
	//考试成绩
	@Override
	public String getExamSourceReport(String examManageId,String orgFrameId) {
		JSONObject json = new JSONObject();
		ExamManage examManage = this.getEntityById(ExamManage.class, examManageId);
		//获得考试成绩总分
		int totalSource = examManage.getScore();//总分
		//考试成绩总分 动态分段统计
		sourceList.clear();//先清空
		sourceList.add(totalSource+"");
		int subsection = totalSource/(examManage.getSubsection()-1);
		this.sourceX(totalSource,subsection);
		StringBuffer sb = new StringBuffer();
		StringBuffer countSb = new StringBuffer();
		String hqlC = "";
		if(StringUtils.isNotBlank(orgFrameId)){//组织下面
			IOrgFrameService orgFrameServiceImpl = (IOrgFrameService)SpringUtils.getBean("orgFrameServiceImpl");
			List<OrgFrame> ofList = orgFrameServiceImpl.getOrgFrameChildren(orgFrameId);
			if(ofList.size()>0){					
				String[] orgFrameIds = new String[ofList.size()+1];
				for (int i = 0; i < ofList.size(); i++) {
					orgFrameIds[i] = ofList.get(i).getId();
				}
				String orgFrameIdz = Arrays.toString(orgFrameIds);
				orgFrameIdz = orgFrameIdz.substring(1, orgFrameIdz.length()-1);
				hqlC = " and orgFrameId in ('"+orgFrameIdz.replace(" ", "").replace(",", "','")+"') ";//下级所有用户	
			}else{
				hqlC = " and orgFrameId = '"+orgFrameId+"' ";
			}
		}
		sb.append("[");
		countSb.append("[");
		for (int i = sourceList.size()-1; i >= 0; i--) {
			String HQL =  QuestionsQl.HQL.queryExamPerson;
			if(StringUtils.isNotBlank(orgFrameId)){//组织下面
				HQL += hqlC;
			}
			List<Object> attributeList = new ArrayList<Object>();
			String sourcePart = sourceList.get(i);//分数段
			sb.append("\""+sourcePart+"\",");
			attributeList.add(examManageId);
			if(sourcePart.indexOf("—")<0){
				HQL += "and totalSource = ?";
				attributeList.add(Float.parseFloat(sourcePart));
				long count = this.baseDaoImpl.queryCountByHql(HQL, attributeList);
				countSb.append(count+",");
			}else{
				//分数段人数
				Float low = Float.parseFloat(sourcePart.split("—")[0]);//低
				Float heigh = Float.parseFloat(sourcePart.split("—")[1]);//高				
				HQL += "and totalSource >= ? and totalSource <= ?";
				attributeList.add(low);
				attributeList.add(heigh);
				long count = this.baseDaoImpl.queryCountByHql(HQL, attributeList);
				countSb.append(count+",");
			}
		}
		sb.deleteCharAt(sb.length()-1).append("]");
		countSb.deleteCharAt(countSb.length()-1).append("]");
		json.put("categories", sb.toString());
		json.put("data", countSb.toString());
		return json.toString();
	}
	
	public List<String> sourceList = new ArrayList<String>();
	
	//分数分段
	public void sourceX(int source, int subsectionScore){
		source--;
		int b = source-(subsectionScore-1);
		sourceList.add(b+"—"+source);
		if(b>=subsectionScore){
			sourceX(b,subsectionScore);
		}else if(b>=1){
			sourceList.add(0+"—"+b);
		}
	}
	
	//完成率 及格率
	@Override
	public ExamReportVo getBaseReport(String examManageId,String orgFrameId) {
		ExamReportVo examReportVo = new ExamReportVo();
		List<Object> attributeList = new ArrayList<Object>();
		String HQL = QuestionsQl.HQL.queryExamPerson;
		attributeList.add(examManageId);
		if(StringUtils.isNotBlank(orgFrameId)){//组织下面
			IOrgFrameService orgFrameServiceImpl = (IOrgFrameService)SpringUtils.getBean("orgFrameServiceImpl");
			List<OrgFrame> ofList = orgFrameServiceImpl.getOrgFrameChildren(orgFrameId);
			if(ofList.size()>0){					
				String[] orgFrameIds = new String[ofList.size()+1];
				for (int i = 0; i < ofList.size(); i++) {
					orgFrameIds[i] = ofList.get(i).getId();
				}
				String orgFrameIdz = Arrays.toString(orgFrameIds);
				orgFrameIdz = orgFrameIdz.substring(1, orgFrameIdz.length()-1);
				HQL += " and orgFrameId in ('"+orgFrameIdz.replace(" ", "").replace(",", "','")+"') ";//下级所有用户	
			}else{
				HQL += " and orgFrameId = ? ";
				attributeList.add(orgFrameId);	
			}
		}
		int allPerson = (int)this.baseDaoImpl.queryCountByHql(HQL, attributeList);
		examReportVo.setAllPerson(allPerson);
		HQL += " and isExam = 1";//已经考试
		int completionPerson = (int)this.baseDaoImpl.queryCountByHql(HQL, attributeList);
		examReportVo.setCompletionPerson(completionPerson);
		double percent = (double)completionPerson / (double)allPerson;//小数
	    NumberFormat nt = NumberFormat.getPercentInstance();//获取格式化对象
	    nt.setMinimumFractionDigits(0); //设置百分数精确度0即保留两位小数
		examReportVo.setCompletion(nt.format(percent).toString());
		//及格率
		HQL += " and totalSource>=?";
		ExamManage examManage = this.getEntityById(ExamManage.class, examManageId);
		attributeList.add((float)examManage.getPassScore());//及格分数
		int passPerson = (int)this.baseDaoImpl.queryCountByHql(HQL, attributeList);
		examReportVo.setPassPerson(passPerson);
		double passPercent = (double)passPerson / (double)allPerson;//小数
		examReportVo.setPass(nt.format(passPercent).toString());
		return examReportVo;
	}
	
	//易错题
	@Override
	public Pager getWorryReport(Integer page, Integer rows,String examManageId,String orgFrameId) {
		List<Object> attributeList = new ArrayList<Object>();
		Pager pager = new Pager(0, 0, null);
		List<QuestionVo> questionVoList = new ArrayList<QuestionVo>();
		attributeList.add(examManageId);
		List<Object> attributeExamPerson = new ArrayList<Object>();
		String HQL = QuestionsQl.HQL.queryExamPerson;
		String SQL = QuestionsQl.MySql.worryTitle;
		attributeExamPerson.add(examManageId);
		if(StringUtils.isNotBlank(orgFrameId)){//组织下面
			IOrgFrameService orgFrameServiceImpl = (IOrgFrameService)SpringUtils.getBean("orgFrameServiceImpl");
			List<OrgFrame> ofList = orgFrameServiceImpl.getOrgFrameChildren(orgFrameId);
			if(ofList.size()>0){					
				String[] orgFrameIds = new String[ofList.size()+1];
				for (int i = 0; i < ofList.size(); i++) {
					orgFrameIds[i] = ofList.get(i).getId();
				}
				String orgFrameIdz = Arrays.toString(orgFrameIds);
				orgFrameIdz = orgFrameIdz.substring(1, orgFrameIdz.length()-1);
				String ofId = orgFrameIdz.replace(" ", "").replace(",", "','");
				HQL += " and orgFrameId in ('"+ofId+"') ";//下级所有用户	
				SQL = SQL.replace("@@", " and aep.orgFrame_Id in ('"+ofId+"')");
			}else{
				HQL += " and orgFrameId = ? ";
				attributeExamPerson.add(orgFrameId);	
				SQL = SQL.replace("@@", " and aep.orgFrame_Id = '"+orgFrameId+"'");
			}
		}else{
			SQL = SQL.replace("@@", "");
		}
		pager = this.baseDaoImpl.queryEntitySQLList(page, rows, SQL, attributeList);//错题分页
		int allPerson = (int)this.baseDaoImpl.queryCountByHql(HQL, attributeExamPerson);
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			QuestionVo qvo = new QuestionVo();
			if(obj[0]!=null) {
				int worryNum = Integer.parseInt(obj[0].toString());
				qvo.setWorryNum(worryNum);	
				double worryPercent = (double)worryNum / (double)allPerson;//小数
				NumberFormat nt = NumberFormat.getPercentInstance();//获取格式化对象
				nt.setMinimumFractionDigits(1); //设置百分数精确度0即保留两位小数
				qvo.setWorryPercent(nt.format(worryPercent).toString());
			}
			if(obj[1]!=null) qvo.setId(obj[1].toString());
			if(obj[2]!=null) qvo.setTitle(obj[2].toString());
			if(obj[3]!=null) qvo.setType(Integer.parseInt(obj[3].toString()));
			questionVoList.add(qvo);
		}
		pager.setPageList(questionVoList);
		return pager;
	}
	
	//错题人员
	@Override
	public Pager getWorryPerson(Integer page, Integer rows, String id,String examManageId) {
		List<Object> criterionsList = new ArrayList<Object>();
		if(StringUtils.isNotBlank(id)){
			criterionsList.add(id);
		}
		criterionsList.add(0);
		criterionsList.add(examManageId);
		Pager pager = this.baseDaoImpl.queryEntityHQLList(page, rows, QuestionsQl.HQL.worryPerson, criterionsList);
		List<ExamPersonQuestionVo> list = new ArrayList<ExamPersonQuestionVo>();
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			ExamPersonQuestion epq = (ExamPersonQuestion)obj[0];
			ExamPerson ep = (ExamPerson)obj[1];
			ExamPersonQuestionVo examPersonQuestionVo = new ExamPersonQuestionVo();
			examPersonQuestionVo.setPersonId(epq.getPersonId());
			examPersonQuestionVo.setPersonName(epq.getPersonName());
			if(epq.getPersonAnswer().startsWith(","))
				examPersonQuestionVo.setPersonAnswer(epq.getPersonAnswer().substring(1, epq.getPersonAnswer().length()));
			else
				examPersonQuestionVo.setPersonAnswer(epq.getPersonAnswer());
			examPersonQuestionVo.setOrgFrameId(ep.getOrgFrameId());
			examPersonQuestionVo.setOrgFrameName(ep.getOrgFrameName());
			User u = this.getEntityById(User.class, epq.getPersonId());
			examPersonQuestionVo.setJobNumber(u.getJobNumber());
			list.add(examPersonQuestionVo);
		}
		pager.setPageList(list);
		return pager;
	}
	
}
