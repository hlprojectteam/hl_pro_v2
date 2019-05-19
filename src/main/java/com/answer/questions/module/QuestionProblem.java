package com.answer.questions.module;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @intruduction 题库题目选项
 * @author Dic
 * @Date 2016年9月6日下午2:44:28
 */
@Entity
@Table(name="P_ANSWER_QUESTION_PROBLEM")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class QuestionProblem{
	
	private String id;
	private String option;//选项
	private String answer;//答案
	private String no;//题号
	private Question question;
	private String sysCode;//子系统编码
	
	@Id
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")   
	@Column(name = "ID", nullable = false,length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "OPTION_",length=1024)
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	@Column(name = "ANSWER_",length=256)
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
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
	@Column(name = "NO_",length=2)
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	@Column(name = "SYS_CODE",length=16)	
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	
}
