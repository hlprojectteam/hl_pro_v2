package com.answer.questions.vo;


import com.answer.questions.module.ExamPerson;

public class ExamPersonVo extends ExamPerson{
	
	private String subject;
	private Integer type;//考试类型 1：练习 2：正式考试
	private String jobNumber;//工号
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
