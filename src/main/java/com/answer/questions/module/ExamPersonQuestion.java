package com.answer.questions.module;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.common.base.module.BaseModule;

/**
 * 考试人员做题
 * @intruduction 
 * @author Dic
 * @Date 2016年9月12日上午9:38:17
 */
@Entity
@Table(name="P_ANSWER_EXAM_PERSON_QUESTION")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ExamPersonQuestion extends BaseModule{

	private String personId;//人员id
	private String personName;//人员
	private String personAnswer;//答案
	private Integer isRight;//是否正确
	//分值
	private Question question;
	private Integer order;//排序
	private ExamManage examManage;//试题管理
	
	@Column(name = "PERSON_ID",length=32)
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	@Column(name = "PERSON_NAME",length=32)
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	@Column(name = "PERSON_ANSWER",length=512)
	public String getPersonAnswer() {
		return personAnswer;
	}
	public void setPersonAnswer(String personAnswer) {
		this.personAnswer = personAnswer;
	}
	@Column(name = "IS_RIGHT")
	public Integer getIsRight() {
		return isRight;
	}
	public void setIsRight(Integer isRight) {
		this.isRight = isRight;
	}
	@ManyToOne(cascade={CascadeType.MERGE})           
    @JoinColumn(name="QUESTION_ID") 
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	@Column(name = "ORDER_")
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	@ManyToOne(cascade={CascadeType.MERGE})           
    @JoinColumn(name="EXAMMANAGE_ID") 
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public ExamManage getExamManage() {
		return examManage;
	}
	public void setExamManage(ExamManage examManage) {
		this.examManage = examManage;
	}
	
}
