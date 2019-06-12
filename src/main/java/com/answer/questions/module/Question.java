package com.answer.questions.module;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @intruduction 题库
 * @author Dic
 * @Date 2016年9月6日下午2:44:28
 */
@Entity
@Table(name="P_ANSWER_QUESTION")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Question{
	
	private String id;
	private String title;//题目
	private Integer type;//题目类型  字典：title_type 1单选 2多选 3判断 4填空 
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	private Integer state;//状态 字典：state 1:正常 2:停用
	private Integer isRandom;//是否随机 字典：title_type  1:是 0:否
	
	private QuestionManage questionManage;

	private Set<QuestionProblem> questionProblems = new TreeSet<QuestionProblem>();
	
	private String num;//随机出题使用 比较大小
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
	@Column(name = "TITLE_",length=2048)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "TYPE_")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime==null?new Date():createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE})           
    @JoinColumn(name="QUESTIONMANAGE_ID") 
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public QuestionManage getQuestionManage() {
		return questionManage;
	}

	public void setQuestionManage(QuestionManage questionManage) {
		this.questionManage = questionManage;
	}
	
	@OneToMany(cascade=CascadeType.MERGE,mappedBy="question",fetch=FetchType.LAZY)
	@OrderBy("no asc")
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public Set<QuestionProblem> getQuestionProblems() {
		return questionProblems;
	}

	public void setQuestionProblems(Set<QuestionProblem> questionProblems) {
		this.questionProblems = questionProblems;
	}
	
	@Column(name = "STATE_")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	@Column(name = "IS_RANDOM")
	public Integer getIsRandom() {
		return isRandom;
	}

	public void setIsRandom(Integer isRandom) {
		this.isRandom = isRandom;
	}
	
	@Column(name = "NUM_")
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	@Column(name = "SYS_CODE",length=16)	
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	
}
