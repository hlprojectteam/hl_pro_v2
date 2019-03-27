package com.answer.questions.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * 
 * @intruduction 考卷
 * @author Dic
 * @Date 2016年9月6日下午2:44:28
 */
@Entity
@Table(name="P_ANSWER_EXAM_MANAGE")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class ExamManage extends BaseModule{

	private String subject;//考题名称
	private String questionManageIds;//所属题库
	private String questionManageNames;//所属题库
	private String userIds;//用户范围id
	private String userNames;//
	private String roleIds;//角色范围id
	private String roleNames;//
	private String orgFrameIds;//组织范围id
	private String orgFrameNames;//组织范围名称
	
	private Integer single;//单选
	private Float singleScore;//单选分值
	private Integer many;//多选
	private Float manyScore;//多选分值
	private Integer judge;//判断
	private Float judgeScore;//判断分值
	private Integer fill;//填空
	private Float fillScore;//填空分值
	
	private Integer isShow;//提交是否显示答案
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date beginTime;//开始时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date endTime;//结束时间	
	private Integer examTime;//做题时长
	
	private Integer state;//状态 0：撤回  1：发布
	private Integer type;//考试类型 1：练习 2：正式考试
	private Integer score;//总分
	private Integer passScore;//及格分数
	private Integer subsection;//分数分段统计
	
	@Column(name = "SUBJECT_",length=128)
	public String getSubject() {
		return subject;
	}
	@Column(name = "QUESTION_MANAGE_IDS",length=1000)
	public String getQuestionManageIds() {
		return questionManageIds;
	}
	@Column(name = "USER_IDS",length=65532)
	public String getUserIds() {
		return userIds;
	}
	@Column(name = "USER_NAMES",length=65532)
	public String getUserNames() {
		return userNames;
	}
	@Column(name = "ROLE_IDS",length=1000)
	public String getRoleIds() {
		return roleIds;
	}
	@Column(name = "ROLE_NAMES",length=1000)
	public String getRoleNames() {
		return roleNames;
	}
	@Column(name = "ORGFRAME_IDS",length=1000)
	public String getOrgFrameIds() {
		return orgFrameIds;
	}
	@Column(name = "ORGFRAME_NAMES",length=1000)
	public String getOrgFrameNames() {
		return orgFrameNames;
	}
	@Column(name = "IS_SHOW")
	public Integer getIsShow() {
		return isShow;
	}
	@Column(name = "BEGIN_TIME")
	public Date getBeginTime() {
		return beginTime;
	}
	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}
	@Column(name = "EXAM_TIME")
	public Integer getExamTime() {
		return examTime;
	}
	@Column(name = "QUESTION_MANAGE_NAMES",length=1000)
	public String getQuestionManageNames() {
		return questionManageNames;
	}
	@Column(name = "STATE_")
	public Integer getState() {
		return state;
	}
	@Column(name = "SINGLE_")
	public Integer getSingle() {
		return single;
	}
	
	public void setSingle(Integer single) {
		this.single = single;
	}
	@Column(name = "SINGLE_SCORE", columnDefinition="float(3,1)")
	public Float getSingleScore() {
		return singleScore;
	}
	public void setSingleScore(Float singleScore) {
		this.singleScore = singleScore;
	}
	@Column(name = "MANY_")
	public Integer getMany() {
		return many;
	}
	public void setMany(Integer many) {
		this.many = many;
	}
	@Column(name = "MANY_SCORE", columnDefinition="float(3,1)")
	public Float getManyScore() {
		return manyScore;
	}
	public void setManyScore(Float manyScore) {
		this.manyScore = manyScore;
	}
	@Column(name = "JUDGE_")
	public Integer getJudge() {
		return judge;
	}
	public void setJudge(Integer judge) {
		this.judge = judge;
	}
	@Column(name = "JUDGE_SCORE", columnDefinition="float(3,1)")
	public Float getJudgeScore() {
		return judgeScore;
	}
	public void setJudgeScore(Float judgeScore) {
		this.judgeScore = judgeScore;
	}
	@Column(name = "FILL_")
	public Integer getFill() {
		return fill;
	}
	public void setFill(Integer fill) {
		this.fill = fill;
	}
	@Column(name = "FILL_SCORE", columnDefinition="float(3,1)")
	public Float getFillScore() {
		return fillScore;
	}
	public void setFillScore(Float fillScore) {
		this.fillScore = fillScore;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public void setQuestionManageNames(String questionManageNames) {
		this.questionManageNames = questionManageNames;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setQuestionManageIds(String questionManageIds) {
		this.questionManageIds = questionManageIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
	public void setOrgFrameIds(String orgFrameIds) {
		this.orgFrameIds = orgFrameIds;
	}
	public void setOrgFrameNames(String orgFrameNames) {
		this.orgFrameNames = orgFrameNames;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void setExamTime(Integer examTime) {
		this.examTime = examTime;
	}
	@Column(name = "TYPE_")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column(name = "SCORE_")
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	@Column(name = "PASS_SCORE")
	public Integer getPassScore() {
		return passScore;
	}
	public void setPassScore(Integer passScore) {
		this.passScore = passScore;
	}
	@Column(name = "SUBSECTION_")
	public Integer getSubsection() {
		return subsection;
	}
	public void setSubsection(Integer subsection) {
		this.subsection = subsection;
	}
	
}
