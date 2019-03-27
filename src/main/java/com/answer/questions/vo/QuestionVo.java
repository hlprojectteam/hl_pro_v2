package com.answer.questions.vo;

import java.util.List;

import com.answer.questions.module.Question;
import com.answer.questions.module.QuestionProblem;

public class QuestionVo extends Question{
	
	private List<QuestionProblem> problems;

	private int worryNum;//错题数
	private String worryPercent;//错题率
	
	public List<QuestionProblem> getProblems() {
		return problems;
	}

	public void setProblems(List<QuestionProblem> problems) {
		this.problems = problems;
	}

	//-----------判断题---------------------------
	private String questionProblemId;
	private String questionProblemOption;//选项
	private String questionProblemAnswer;//答案
	private String questionProblemNo;//题号

	public String getQuestionProblemId() {
		return questionProblemId;
	}

	public void setQuestionProblemId(String questionProblemId) {
		this.questionProblemId = questionProblemId;
	}

	public String getQuestionProblemOption() {
		return questionProblemOption;
	}

	public void setQuestionProblemOption(String questionProblemOption) {
		this.questionProblemOption = questionProblemOption;
	}

	public String getQuestionProblemAnswer() {
		return questionProblemAnswer;
	}

	public void setQuestionProblemAnswer(String questionProblemAnswer) {
		this.questionProblemAnswer = questionProblemAnswer;
	}

	public String getQuestionProblemNo() {
		return questionProblemNo;
	}

	public void setQuestionProblemNo(String questionProblemNo) {
		this.questionProblemNo = questionProblemNo;
	}

	public int getWorryNum() {
		return worryNum;
	}

	public void setWorryNum(int worryNum) {
		this.worryNum = worryNum;
	}

	public String getWorryPercent() {
		return worryPercent;
	}

	public void setWorryPercent(String worryPercent) {
		this.worryPercent = worryPercent;
	}
	
}
