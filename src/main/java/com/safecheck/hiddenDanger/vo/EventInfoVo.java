package com.safecheck.hiddenDanger.vo;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.safecheck.hiddenDanger.module.EventInfo;

/**
 * @Description EventInfoVo
 * @author xuezb
 * @Date 2018年5月23日 上午10:28:25
 */
public class EventInfoVo extends EventInfo{

	private String epId;		//事件过程Id
	
	private String epUpNode;		//上一节点(节点编码)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date epUpNodeArriveTime;	//到达时间(上一节)
	private String epUpRole;		//处理角色(上一节点)——角色编码
	private String epUpRoleName;	//处理角色名称(上一节点)
	private String epUpPersonId;	//处理人(上一节点)——用户ID
	private String epUpPersonName;	//处理人姓名(上一节点)
	
	private String epNowNode;	//当前节点(节点编码)
	private String epNowRole;		//处理角色(当前节点)——角色编码
	private String epNowRoleName;	//处理角色名称(当前节点)
	private String epNowPersonId;	//处理人(当前节点)——用户ID
	private String epNowPersonName;	//处理人(当前节点)——用户名称
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date epNowNodeArriveTime;	//到达时间(当前节点)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date epNowNodeLeavleTime;	//离开时间(当前节点)
	
	private String epNextNode;			//下一节点(节点编码)
	private String epNextRole;		//处理角色(下一节点)——角色编码
	private String epNextRoleName;	//处理角色名称(下一节点)
	private String epNextPersonId;		//处理人(下一节点)——用户ID
	private String epNextPersonName;	//处理人(下一节点)——用户名称
	
	private Integer epDealState;	//处理状态 (0关闭，1未处理，2处理中，3处理完)
	private String epAttachId;		//附件ID	 (最多7个附件)
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date epLimitTime;			//处理时限 (超时时限)
	private Integer epDealWay;			//处理端(1电脑，2APP，3微信，4其它)
	private String epDealContent;		//处理意见/评价
	private Integer epWhetherFinish;	//是否办结 (1 办结 2未办结)
	private Integer epAppraise;			//满意度(1非常满意，2满意，3一般，4不满意)
	
	private Integer epIsSite;			//是否本站处理
	private Integer epIsReturned;		//是否退回
	private String epReturnReason;		//退回理由
	
	private List<String> imgUrls;		//图片附件的url
	private List<String> imgHandleUrls;		//图片附件的url(处理隐患的图片)
    private String videoUrl;
    
    private String roleCode;			//上报人角色编码
    private String roleName;			//上报人角色名称
    private String loginName;			//上报人的登录账号
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date eventDateStart;                  //日期Start
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date eventDateEnd;               	 //日期End
	 private String totalTime;			//办结总用时
    
    
	public Date getEventDateStart() {
		return eventDateStart;
	}

	public void setEventDateStart(Date eventDateStart) {
		this.eventDateStart = eventDateStart;
	}

	public Date getEventDateEnd() {
		return eventDateEnd;
	}

	public void setEventDateEnd(Date eventDateEnd) {
		this.eventDateEnd = eventDateEnd;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getEpDealWay() {
		return epDealWay;
	}

	public void setEpDealWay(Integer epDealWay) {
		this.epDealWay = epDealWay;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
	
	
	
	public Integer getEpWhetherFinish() {
		return epWhetherFinish;
	}
	public void setEpWhetherFinish(Integer epWhetherFinish) {
		this.epWhetherFinish = epWhetherFinish;
	}
	public Integer getEpAppraise() {
		return epAppraise;
	}
	public void setEpAppraise(Integer epAppraise) {
		this.epAppraise = epAppraise;
	}
	public String getEpNextNode() {
		return epNextNode;
	}
	public void setEpNextNode(String epNextNode) {
		this.epNextNode = epNextNode;
	}
	public String getEpUpNode() {
		return epUpNode;
	}
	public void setEpUpNode(String epUpNode) {
		this.epUpNode = epUpNode;
	}
	public Date getEpUpNodeArriveTime() {
		return epUpNodeArriveTime;
	}
	public void setEpUpNodeArriveTime(Date epUpNodeArriveTime) {
		this.epUpNodeArriveTime = epUpNodeArriveTime;
	}
	public String getEpUpRole() {
		return epUpRole;
	}
	public void setEpUpRole(String epUpRole) {
		this.epUpRole = epUpRole;
	}
	public String getEpUpRoleName() {
		return epUpRoleName;
	}
	public void setEpUpRoleName(String epUpRoleName) {
		this.epUpRoleName = epUpRoleName;
	}
	public String getEpUpPersonId() {
		return epUpPersonId;
	}
	public void setEpUpPersonId(String epUpPersonId) {
		this.epUpPersonId = epUpPersonId;
	}
	public String getEpUpPersonName() {
		return epUpPersonName;
	}
	public void setEpUpPersonName(String epUpPersonName) {
		this.epUpPersonName = epUpPersonName;
	}
	public Date getEpLimitTime() {
		return epLimitTime;
	}
	public void setEpLimitTime(Date epLimitTime) {
		this.epLimitTime = epLimitTime;
	}
	public String getEpDealContent() {
		return epDealContent;
	}
	public void setEpDealContent(String epDealContent) {
		this.epDealContent = epDealContent;
	}
	public String getEpId() {
		return epId;
	}
	public void setEpId(String epId) {
		this.epId = epId;
	}
	public String getEpNowRoleName() {
		return epNowRoleName;
	}
	public void setEpNowRoleName(String epNowRoleName) {
		this.epNowRoleName = epNowRoleName;
	}
	public String getEpNextRoleName() {
		return epNextRoleName;
	}
	public void setEpNextRoleName(String epNextRoleName) {
		this.epNextRoleName = epNextRoleName;
	}
	public Integer getEpIsSite() {
		return epIsSite;
	}
	public void setEpIsSite(Integer epIsSite) {
		this.epIsSite = epIsSite;
	}
	public Integer getEpIsReturned() {
		return epIsReturned;
	}
	public void setEpIsReturned(Integer epIsReturned) {
		this.epIsReturned = epIsReturned;
	}
	public String getEpReturnReason() {
		return epReturnReason;
	}
	public void setEpReturnReason(String epReturnReason) {
		this.epReturnReason = epReturnReason;
	}
	public String getEpNextRole() {
		return epNextRole;
	}
	public void setEpNextRole(String epNextRole) {
		this.epNextRole = epNextRole;
	}
	public String getEpNextPersonId() {
		return epNextPersonId;
	}
	public void setEpNextPersonId(String epNextPersonId) {
		this.epNextPersonId = epNextPersonId;
	}
	public String getEpNextPersonName() {
		return epNextPersonName;
	}
	public void setEpNextPersonName(String epNextPersonName) {
		this.epNextPersonName = epNextPersonName;
	}
	public String getEpNowPersonId() {
		return epNowPersonId;
	}
	public void setEpNowPersonId(String epNowPersonId) {
		this.epNowPersonId = epNowPersonId;
	}
	public String getEpNowRole() {
		return epNowRole;
	}
	public void setEpNowRole(String epNowRole) {
		this.epNowRole = epNowRole;
	}
	public String getEpNowNode() {
		return epNowNode;
	}
	public void setEpNowNode(String epNowNode) {
		this.epNowNode = epNowNode;
	}
	public Integer getEpDealState() {
		return epDealState;
	}
	public void setEpDealState(Integer epDealState) {
		this.epDealState = epDealState;
	}
	public String getEpAttachId() {
		return epAttachId;
	}
	public void setEpAttachId(String epAttachId) {
		this.epAttachId = epAttachId;
	}
	public String getEpNowPersonName() {
		return epNowPersonName;
	}
	public void setEpNowPersonName(String epNowPersonName) {
		this.epNowPersonName = epNowPersonName;
	}

	public Date getEpNowNodeArriveTime() {
		return epNowNodeArriveTime;
	}

	public void setEpNowNodeArriveTime(Date epNowNodeArriveTime) {
		this.epNowNodeArriveTime = epNowNodeArriveTime;
	}
	public Date getEpNowNodeLeavleTime() {
		return epNowNodeLeavleTime;
	}
	public void setEpNowNodeLeavleTime(Date epNowNodeLeavleTime) {
		this.epNowNodeLeavleTime = epNowNodeLeavleTime;
	}

	public List<String> getImgHandleUrls() {
		return imgHandleUrls;
	}

	public void setImgHandleUrls(List<String> imgHandleUrls) {
		this.imgHandleUrls = imgHandleUrls;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
}
