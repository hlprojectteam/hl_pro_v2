package com.answer.questions.vo;

import java.util.List;


/**
 * 在线考试，试题
 * @intruduction 
 * @author Dic
 * @Date 2016年9月14日下午3:01:35
 */
public class ExamQuestionVo {
	
	private String questionId;//问题id
	private String title;
	private Integer type;//问卷类型 1单选 2多选 3判断 4填空 
	
	private String answer;//正确答案
	private String personAnswer;//
	private Integer isRight;//是否正确;
	private Integer order;//题号 排序
	
	private List<QuestionProblemVo> questionProblem;
	
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getPersonAnswer() {
		return personAnswer;
	}
	public void setPersonAnswer(String personAnswer) {
		this.personAnswer = personAnswer;
	}
	public Integer getIsRight() {
		return isRight;
	}
	public void setIsRight(Integer isRight) {
		this.isRight = isRight;
	}
	public List<QuestionProblemVo> getQuestionProblem() {
		return questionProblem;
	}
	public void setQuestionProblem(List<QuestionProblemVo> questionProblem) {
		this.questionProblem = questionProblem;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}
