package com.urms.visit.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * 访问量
 * Title: Visit
 * Description: 	
 * @author Mr.Wang
 * @date 2016-12-28 上午11:26:53
 */
@Entity
@Table(name="um_system_Visit")
public class Visit {
	
	private String visitId; //主键ID
	private String sysCode; //系统编码
	private String visitIp; //访问IP
	private String visitOperatorAdress; //访问IP的来源地址  例如：中国贵州电信、中国贵州联通
	private Integer visitOperatorType; //运营商类型：1电信、2联通 、3移动、4铁通、5长城宽带、6其它
	private String visitAgent; //系统+版本+来访
	private String visitSystem; //访问来源的系统: window 10
	private String visitBrowser; //访问使用的浏览器：Firefox/47
	private Date visitStartTime; //访问时间
	private Date visitEndTime; //离开时间
	private Integer visitDuration; //在线时长，存毫秒数
	private Integer visitSourceType;  //1电脑、2APP、3微信、4其它
	private Integer visitorType; //1游客、2会员
	private String visitUserId; //如果是会员，记录会员的ID
	private String visitUrl; //访问的请求路径
	
	@Id
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")   
	@Column(name = "visit_Id", nullable = false,length = 32)
	public String getVisitId() {
		return visitId;
	}
	public void setVisitId(String visitId) {
		this.visitId = visitId;
	}
	@Column(name = "sys_code", length=16)
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	@Column(name = "visit_Ip", length=50)
	public String getVisitIp() {
		return visitIp;
	}
	public void setVisitIp(String visitIp) {
		this.visitIp = visitIp;
	}
	@Column(name = "visit_OperatorAdress", length=100)
	public String getVisitOperatorAdress() {
		return visitOperatorAdress;
	}
	public void setVisitOperatorAdress(String visitOperatorAdress) {
		this.visitOperatorAdress = visitOperatorAdress;
	}
	@Column(name = "visit_OperatorType", length=2)
	public Integer getVisitOperatorType() {
		return visitOperatorType;
	}
	public void setVisitOperatorType(Integer visitOperatorType) {
		this.visitOperatorType = visitOperatorType;
	}
	@Column(name = "visit_Agent", length=500)
	public String getVisitAgent() {
		return visitAgent;
	}
	public void setVisitAgent(String visitAgent) {
		this.visitAgent = visitAgent;
	}
	@Column(name = "visit_System", length=100)
	public String getVisitSystem() {
		return visitSystem;
	}
	public void setVisitSystem(String visitSystem) {
		this.visitSystem = visitSystem;
	}
	@Column(name = "visit_Browser", length=100)
	public String getVisitBrowser() {
		return visitBrowser;
	}
	public void setVisitBrowser(String visitBrowser) {
		this.visitBrowser = visitBrowser;
	}
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "visit_StartTime")
	public Date getVisitStartTime() {
		return visitStartTime;
	}
	public void setVisitStartTime(Date visitStartTime) {
		this.visitStartTime = visitStartTime;
	}
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Column(name = "visit_EndTime")
	public Date getVisitEndTime() {
		return visitEndTime;
	}
	public void setVisitEndTime(Date visitEndTime) {
		this.visitEndTime = visitEndTime;
	}
	@Column(name = "visit_Duration", length=10)
	public Integer getVisitDuration() {
		return visitDuration;
	}
	public void setVisitDuration(Integer visitDuration) {
		this.visitDuration = visitDuration;
	}
	@Column(name = "visit_SourceType", length=2)
	public Integer getVisitSourceType() {
		return visitSourceType;
	}
	public void setVisitSourceType(Integer visitSourceType) {
		this.visitSourceType = visitSourceType;
	}
	@Column(name = "visitor_Type", length=2)
	public Integer getVisitorType() {
		return visitorType;
	}
	public void setVisitorType(Integer visitorType) {
		this.visitorType = visitorType;
	}
	@Column(name = "visit_UserId", length=32)
	public String getVisitUserId() {
		return visitUserId;
	}
	public void setVisitUserId(String visitUserId) {
		this.visitUserId = visitUserId;
	}
	@Column(name = "visit_Url", length=500)
	public String getVisitUrl() {
		return visitUrl;
	}
	public void setVisitUrl(String visitUrl) {
		this.visitUrl = visitUrl;
	}
	
}
