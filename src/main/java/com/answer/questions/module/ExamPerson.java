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
 * 考试人员
 * @intruduction 
 * @author Dic
 * @Date 2016年9月12日上午9:38:17
 */
@Entity
@Table(name="P_ANSWER_EXAM_PERSON")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ExamPerson extends BaseModule{

	private String personId;//人员id
	private String personName;//人员
	private String orgFrameId;//所属部门id
	private String orgFrameName;//所属部门名称
	private Integer isExam;//是否已经考试 0:没考试 1:已经考试
	private Integer singleRight;//单选做多题目
	private Float singleTotalSource;//单选得分
	private Integer manyRight;//多选做多题目
	private Float manyTotalSource;//多选得分
	private Integer judgeRight;//判断做多题目
	private Float judgeTotalSource;//判断得分
	private Integer fillRight;//填空做多题目
	private Float fillTotalSource;//填空得分
	private Float totalSource;//分数成绩
	private Integer consumeTime;//耗时
	private ExamManage examManage;
	
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
	@Column(name = "IS_EXAM")
	public Integer getIsExam() {
		return isExam;
	}
	public void setIsExam(Integer isExam) {
		this.isExam = isExam;
	}
	@Column(name = "TOTAL_SOURCE", columnDefinition="float(5,1)")
	public Float getTotalSource() {
		return totalSource;
	}
	public void setTotalSource(Float totalSource) {
		this.totalSource = totalSource;
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
	@Column(name = "SINGLE_RIGHT")
	public Integer getSingleRight() {
		return singleRight;
	}
	public void setSingleRight(Integer singleRight) {
		this.singleRight = singleRight;
	}
	@Column(name = "SINGLE_TOTAL_SOURCE", columnDefinition="float(4,1)")
	public Float getSingleTotalSource() {
		return singleTotalSource;
	}
	public void setSingleTotalSource(Float singleTotalSource) {
		this.singleTotalSource = singleTotalSource;
	}
	@Column(name = "MANY_RIGHT")
	public Integer getManyRight() {
		return manyRight;
	}
	public void setManyRight(Integer manyRight) {
		this.manyRight = manyRight;
	}
	@Column(name = "MANY_TOTAL_SOURCE", columnDefinition="float(4,1)")
	public Float getManyTotalSource() {
		return manyTotalSource;
	}
	public void setManyTotalSource(Float manyTotalSource) {
		this.manyTotalSource = manyTotalSource;
	}
	@Column(name = "JUDGE_RIGHT")
	public Integer getJudgeRight() {
		return judgeRight;
	}
	public void setJudgeRight(Integer judgeRight) {
		this.judgeRight = judgeRight;
	}
	@Column(name = "JUDGE_TOTAL_SOURCE", columnDefinition="float(4,1)")
	public Float getJudgeTotalSource() {
		return judgeTotalSource;
	}
	public void setJudgeTotalSource(Float judgeTotalSource) {
		this.judgeTotalSource = judgeTotalSource;
	}
	@Column(name = "FILL_RIGHT")
	public Integer getFillRight() {
		return fillRight;
	}
	public void setFillRight(Integer fillRight) {
		this.fillRight = fillRight;
	}
	@Column(name = "FILL_TOTAL_SOURCE", columnDefinition="float(4,1)")
	public Float getFillTotalSource() {
		return fillTotalSource;
	}
	public void setFillTotalSource(Float fillTotalSource) {
		this.fillTotalSource = fillTotalSource;
	}
	@Column(name = "CONSUME_TIME")
	public Integer getConsumeTime() {
		return consumeTime;
	}
	public void setConsumeTime(Integer consumeTime) {
		this.consumeTime = consumeTime;
	}
	@Column(name = "ORGFRAME_ID",length=32)
	public String getOrgFrameId() {
		return orgFrameId;
	}
	public void setOrgFrameId(String orgFrameId) {
		this.orgFrameId = orgFrameId;
	}
	@Column(name = "ORGFRAME_NAME",length=32)
	public String getOrgFrameName() {
		return orgFrameName;
	}
	public void setOrgFrameName(String orgFrameName) {
		this.orgFrameName = orgFrameName;
	}
	@Override
	public int compareTo(Object o) {
		return 1;
	}
}
