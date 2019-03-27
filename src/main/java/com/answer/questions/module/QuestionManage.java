package com.answer.questions.module;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.common.base.module.BaseModule;

/**
 * 
 * @intruduction 题库管理
 * @author Dic
 * @Date 2016年9月6日下午2:44:28
 */
@Entity
@Table(name="P_ANSWER_QUESTION_MANAGE")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class QuestionManage extends BaseModule{
	
	private String subject;//题库名称

	private Set<Question> questions = new TreeSet<Question>();
	
	@Column(name = "SUBJECT_",length=128)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	@OneToMany(cascade=CascadeType.MERGE,mappedBy="questionManage",fetch=FetchType.LAZY)
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public Set<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(Set<Question> questions) {
		this.questions = questions;
	}
	
}
