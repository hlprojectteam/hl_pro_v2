package com.answer.questions.vo;


import com.answer.questions.module.ExamManage;

public class ExamManageVo extends ExamManage{
	
	private Float singleTotalScore;//单选总分值
	private Float manyTotalScore;//多选分值
	private Float judgeTotalScore;//判断分值
	private Float fillTotalScore;//填空分值 
	
	private Float totalScore;//总分

	public Float getSingleTotalScore() {
		return singleTotalScore;
	}

	public void setSingleTotalScore(Float singleTotalScore) {
		this.singleTotalScore = singleTotalScore;
	}

	public Float getManyTotalScore() {
		return manyTotalScore;
	}

	public void setManyTotalScore(Float manyTotalScore) {
		this.manyTotalScore = manyTotalScore;
	}

	public Float getJudgeTotalScore() {
		return judgeTotalScore;
	}

	public void setJudgeTotalScore(Float judgeTotalScore) {
		this.judgeTotalScore = judgeTotalScore;
	}

	public Float getFillTotalScore() {
		return fillTotalScore;
	}

	public void setFillTotalScore(Float fillTotalScore) {
		this.fillTotalScore = fillTotalScore;
	}

	public Float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Float totalScore) {
		this.totalScore = totalScore;
	}
	
}
