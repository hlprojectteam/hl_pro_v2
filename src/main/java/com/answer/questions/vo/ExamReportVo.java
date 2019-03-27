package com.answer.questions.vo;


public class ExamReportVo {
	private int allPerson;//总共人数
	private int completionPerson;//完成人员
	private String completion;//完成率
	
	private int passPerson;//及格人员
	private String pass;//及格率
	
	public int getAllPerson() {
		return allPerson;
	}
	public void setAllPerson(int allPerson) {
		this.allPerson = allPerson;
	}
	public int getCompletionPerson() {
		return completionPerson;
	}
	public void setCompletionPerson(int completionPerson) {
		this.completionPerson = completionPerson;
	}
	public String getCompletion() {
		return completion;
	}
	public void setCompletion(String completion) {
		this.completion = completion;
	}
	public int getPassPerson() {
		return passPerson;
	}
	public void setPassPerson(int passPerson) {
		this.passPerson = passPerson;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
}
