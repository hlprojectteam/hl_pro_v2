package com.answer.questions.vo;

import com.answer.questions.module.ExamPersonQuestion;

public class ExamPersonQuestionVo extends ExamPersonQuestion{
	
	private String orgFrameId;//所属部门id
	private String orgFrameName;//所属部门名称
	private String jobNumber;//工号
	public String getOrgFrameId() {
		return orgFrameId;
	}
	public void setOrgFrameId(String orgFrameId) {
		this.orgFrameId = orgFrameId;
	}
	public String getOrgFrameName() {
		return orgFrameName;
	}
	public void setOrgFrameName(String orgFrameName) {
		this.orgFrameName = orgFrameName;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
}
