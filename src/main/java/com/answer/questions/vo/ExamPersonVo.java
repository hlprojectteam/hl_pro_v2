package com.answer.questions.vo;


import com.answer.questions.module.ExamPerson;

public class ExamPersonVo extends ExamPerson{
	
	private String subject;

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	private String jobNumber;//工号

	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	
}
