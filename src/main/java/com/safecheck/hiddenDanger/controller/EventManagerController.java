package com.safecheck.hiddenDanger.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.utils.Common;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.safecheck.hiddenDanger.module.EventInfo;
import com.safecheck.hiddenDanger.module.EventProcess;
import com.safecheck.hiddenDanger.service.IEventManagerService;
import com.safecheck.hiddenDanger.vo.EventInfoVo;
import com.safecheck.hiddenDanger.vo.EventProcessVo;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.role.module.Role;
import com.urms.user.module.User;
import com.urms.user.vo.UserVo;

/**
 * @Description 事件管理
 * @author xuezb
 * @Date 2018年5月23日
 */
@Controller
@RequestMapping("/safecheck/hiddenDanger")
public class EventManagerController extends BaseController{

	@Autowired
	private IEventManagerService eventManagerServiceImpl;
	
	@Autowired
	private IAttachService attachServiceImpl;
	
	/*@Autowired
	public IMessageService messageServiceImpl;*/
	

	/**
	 * @Description 跳转到	上报事件列表页面
	 * @return
	 * @author xuezb
	 * @Date 2018年5月23日
	 */
	@RequestMapping(value="/event_reportList") 
	public String reportList(HttpServletRequest request,HttpServletResponse response, String menuCode) {
		User user = (User)request.getSession().getAttribute("user");	// 获取当前用户信息(包括用户id)
        request.setAttribute("epNowPersonId", user.getId());
        request.setAttribute("menuCode", menuCode);
		return "/page/safecheck/hiddenDanger/event_reportList";
	}
	
	/**
	 * @Description 跳转到	待办事件列表页面
	 * @return
	 * @author xuezb
	 * @Date 2018年5月24日
	 */
	@RequestMapping(value="/event_agendaList") 
	public String agendaList(HttpServletRequest request,HttpServletResponse response) {
		User user = (User)request.getSession().getAttribute("user");	// 获取当前用户信息(包括用户id及其所包含的角色编码)
        request.setAttribute("epNowPersonId", user.getId());	//处理人(当前节点)——用户ID
		return "/page/safecheck/hiddenDanger/event_agendaList";
	}
	
	/**
	 * @Description 跳转到	经办事件列表页面
	 * @return
	 * @author xuezb
	 * @Date 2018年5月24日
	 */
	@RequestMapping(value="/event_handleList") 
	public String handleList(HttpServletRequest request,HttpServletResponse response) {
		User user = (User)request.getSession().getAttribute("user");	// 获取当前用户信息(包括用户id)
        request.setAttribute("epNowPersonId", user.getId());
		return "/page/safecheck/hiddenDanger/event_handleList";
	}
	
	/**
	 * @Description 跳转到	办结事件列表页面
	 * @return
	 * @author xuezb
	 * @Date 2018年5月24日
	 */
	@RequestMapping(value="/event_finishList") 
	public String finishList(HttpServletRequest request,HttpServletResponse response) {
		User user = (User)request.getSession().getAttribute("user");	// 获取当前用户信息(包括用户id)
        request.setAttribute("epNowPersonId", user.getId());
		return "/page/safecheck/hiddenDanger/event_finishList";
	}
	
	/**
	 * @Description 跳转到	所有事件列表页面
	 * @return
	 * @author xuezb
	 * @Date 2018年6月6日
	 */
	@RequestMapping(value="/event_allList") 
	public String allList(HttpServletRequest request,HttpServletResponse response) {
		return "/page/safecheck/hiddenDanger/event_allList";
	}
	
	
	
	/**
	 * @Description 上报事件列表		加载
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年5月23日
	 */
	@RequestMapping(value="/event_loadReportList")
	public void loadReportList(HttpServletRequest request,HttpServletResponse response,Integer page, Integer rows,EventInfoVo eventInfoVo){
		Pager pager = this.eventManagerServiceImpl.queryReportList(page, rows, eventInfoVo);
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
		json.put("total", pager.getRowCount());
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * @Description 待办事件列表		加载
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年5月28日
	 */
	@RequestMapping(value="/event_loadAgendaList")
	public void loadAgendaList(HttpServletRequest request,HttpServletResponse response,Integer page, Integer rows,EventInfoVo eventInfoVo){
		Pager pager = this.eventManagerServiceImpl.queryAgendaList(page, rows, eventInfoVo);
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
		json.put("total", pager.getRowCount());
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * @Description 经办事件列表		加载
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年5月29日
	 */
	@RequestMapping(value="/event_loadHandleList")
	public void loadHandleList(HttpServletRequest request,HttpServletResponse response,Integer page, Integer rows,EventInfoVo eventInfoVo){
		Pager pager = this.eventManagerServiceImpl.queryHandleList(page, rows, eventInfoVo);
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
		json.put("total", pager.getRowCount());
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * @Description 办结事件列表		加载
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年5月29日
	 */
	@RequestMapping(value="/event_loadFinishList")
	public void loadFinishList(HttpServletRequest request,HttpServletResponse response,Integer page, Integer rows,EventInfoVo eventInfoVo){
		Pager pager = this.eventManagerServiceImpl.queryFinishList(page, rows, eventInfoVo);
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
		json.put("total", pager.getRowCount());
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * @Description 所有事件列表		加载
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年6月6日
	 */
	@RequestMapping(value="/event_loadAllList")
	public void loadAllList(HttpServletRequest request,HttpServletResponse response,Integer page, Integer rows,EventInfoVo eventInfoVo){
		Pager pager = this.eventManagerServiceImpl.queryAllList(page, rows, eventInfoVo);
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
		json.put("total", pager.getRowCount());
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * @Description 跳转到	上报事件新增页面
	 * @return
	 * @author xuezb
	 * @Date 2018年5月23日
	 */
	@RequestMapping(value="/event_reportEdit") 
	public String reportEdit(HttpServletRequest request,String winName) {
		User user = (User)request.getSession().getAttribute("user");
		OrgFrame orgFrame = user.getOrgFrame();
		
		EventInfoVo eventInfoVo = new EventInfoVo();
		eventInfoVo.setCreatorId(user.getId());
		eventInfoVo.setCreatorName(user.getUserName());
		eventInfoVo.setContactPhone(user.getMobilePhone());
		if(orgFrame != null){
			eventInfoVo.setReporterOrgId(orgFrame.getId());		//用户所在部门(组织机构)id
		}
		
		request.setAttribute("eventInfoVo", eventInfoVo);
		request.setAttribute("winName", winName);
		return "/page/safecheck/hiddenDanger/event_reportEdit";
	}
	
	/**
	 * @Description 上报事件		新增
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2018年5月23日
	 */
	@RequestMapping(value="/event_addReportEvent",method = RequestMethod.POST)
	public void addReportEvent(HttpSession httpSession, HttpServletResponse response,EventInfoVo eventInfoVo){
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveEvent(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
			
			//事件上报，给指挥中心推送提醒
			//sendJpushMsg("新事件："+ eventInfoVo.getEventTitle(), eventInfoVo.getEventTitle(), null, "event_CommandCenter");
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * @Description 修改事件过程的处理状态
	 * @param epId	事件过程id
	 * @return
	 * @author xuezb
	 * @Date 2018年6月4日
	 */
	@RequestMapping(value="/event_modifyProcessStatus",method = RequestMethod.POST)
	public void modifyProcessStatus(HttpSession httpSession, HttpServletResponse response,String epId){
		JsonObject json = new JsonObject();
		try {
			this.eventManagerServiceImpl.saveModifyProcessStatus(epId);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * @Description 跳转到	上报事件明细页面
	 * @param id	事件表id
	 * @return
	 * @author xuezb
	 * @Date 2018年5月23日
	 */
	@RequestMapping(value="/event_reportDetail") 
	public String reportDetail(HttpServletRequest request,String id, String epNowNode,Integer epDealState) {
		EventInfo eventInfo = this.eventManagerServiceImpl.getEntityById(EventInfo.class, id);
		EventInfoVo eventInfoVo = new EventInfoVo();
		BeanUtils.copyProperties(eventInfo, eventInfoVo);
		eventInfoVo.setEpNowNode(epNowNode);
		eventInfoVo.setEpDealState(epDealState);
		List<Attach> attchList = this.attachServiceImpl.queryAttchListByFormId(eventInfoVo.getId());
		List<String> imgUrls = new ArrayList<String>();
		for (Attach attach : attchList) {
			imgUrls.add(attach.getPathUpload());
		}
		eventInfoVo.setImgUrls(imgUrls);	//获取上报人上报隐患时提交的图片url
		List<EventProcessVo> processes = this.eventManagerServiceImpl.getEProcessVoByEId(id); //获取事件所有过程
		request.setAttribute("processes", getDpName(processes));
		request.setAttribute("eventInfoVo", eventInfoVo);
		return "/page/safecheck/hiddenDanger/event_reportDetail";
	}
	
	/**
	 * @Description 跳转到	待办事件明细页面
	 * @param id	事件表id
	 * @param epNowNode		当前节点编码
	 * @param winName	列表页面窗口名称
	 * @return
	 * @author xuezb
	 * @Date 2018年5月29日
	 */
	@RequestMapping(value="/event_agendaDetail") 
	public String agendaDetail(HttpServletRequest request, String id, String epNowNode, String winName) {
		EventInfo eventInfo = this.eventManagerServiceImpl.getEntityById(EventInfo.class, id);
		EventProcess eventProcess = this.eventManagerServiceImpl.getEProcessByEIdAndEpNowNode(id, epNowNode);
		
		EventInfoVo eventInfoVo = new EventInfoVo();
		BeanUtils.copyProperties(eventInfo, eventInfoVo);
		
		eventInfoVo.setEpId(eventProcess.getId());
		
		eventInfoVo.setEpNowNode(eventProcess.getEpNowNode());
		eventInfoVo.setEpNowRole(eventProcess.getEpNowRole());
		eventInfoVo.setEpNowRoleName(eventProcess.getEpNowRoleName());
		eventInfoVo.setEpNowPersonId(eventProcess.getEpNowPersonId());
		eventInfoVo.setEpNowPersonName(eventProcess.getEpNowPersonName());
		eventInfoVo.setEpNowNodeArriveTime(eventProcess.getEpNowNodeArriveTime());		//当前节点到达时间
		
		eventInfoVo.setEpNextRole(eventProcess.getEpNextRole());
		eventInfoVo.setEpNextRoleName(eventProcess.getEpNextRoleName());
		eventInfoVo.setEpNextPersonId(eventProcess.getEpNextPersonId());
		eventInfoVo.setEpNextPersonName(eventProcess.getEpNextPersonName());
		
		eventInfoVo.setEpDealState(eventProcess.getEpDealState());
		eventInfoVo.setEpAttachId(eventProcess.getEpAttachId());
		eventInfoVo.setEpLimitTime(eventProcess.getEpLimitTime()); 		//超时时限
		
		eventInfoVo.setEpIsSite(eventProcess.getEpIsSite());
		eventInfoVo.setEpIsReturned(eventProcess.getEpIsReturned());
		eventInfoVo.setEpReturnReason(eventProcess.getEpReturnReason());
		
		User user = (User)request.getSession().getAttribute("user");	// 获取当前用户信息(包括用户id及其所包含的角色编码)
        eventInfoVo.setEpNowPersonId(user.getId());						//处理人(当前节点)——用户ID
        eventInfoVo.setEpNowPersonName(user.getUserName());
        
        StringBuffer roles = new StringBuffer();
        StringBuffer roleNames = new StringBuffer();
        for (Iterator<Role> iterator = user.getRoles().iterator(); iterator.hasNext();) {
        	Role role = (Role) iterator.next();
			roles.append(role.getRoleCode()+",");
			roleNames.append(role.getRoleName()+",");
		}
        String rolestr = new String();
        String roleNamestr = new String();
        if(roles.length() > 0){
        	 rolestr = roles.substring(0, roles.length()-1);
        }
        eventInfoVo.setEpNowRole(rolestr);					//处理角色(当前节点)——角色编码
        if(roleNames.length() > 0){
        	 roleNamestr = roleNames.substring(0, roleNames.length()-1);
        }
        eventInfoVo.setEpNowRoleName(roleNamestr);
        
        List<Attach> attchList = this.attachServiceImpl.queryAttchListByFormId(eventInfoVo.getId());
		List<String> imgUrls = new ArrayList<String>();
		for (Attach attach : attchList) {
			imgUrls.add(attach.getPathUpload());
		}
		eventInfoVo.setImgUrls(imgUrls);	//获取上报人上报隐患时提交的图片url
		
        
		List<EventProcessVo> processes = this.eventManagerServiceImpl.getEProcessVoByEId(id); //获取事件所有过程
		request.setAttribute("processes", getDpName(processes));
		request.setAttribute("eventInfoVo", eventInfoVo);
		request.setAttribute("dpId", user.getOrgFrame().getId());
		request.setAttribute("winName", winName);
		
		return "/page/safecheck/hiddenDanger/event_agendaDetail";
	}
	
	/**
	 * @Description 跳转到	经办事件明细页面
	 * @param id	事件表id
	 * @return
	 * @author xuezb
	 * @Date 2018年5月29日
	 */
	@RequestMapping(value="/event_handleDetail") 
	public String handleDetail(HttpServletRequest request,String id,String epNowNode,Integer epDealState) {
		EventInfo eventInfo = this.eventManagerServiceImpl.getEntityById(EventInfo.class, id);
		EventInfoVo eventInfoVo = new EventInfoVo();
		BeanUtils.copyProperties(eventInfo, eventInfoVo);
		eventInfoVo.setEpNowNode(epNowNode);
		eventInfoVo.setEpDealState(epDealState);
		List<Attach> attchList = this.attachServiceImpl.queryAttchListByFormId(eventInfoVo.getId());
		List<String> imgUrls = new ArrayList<String>();
		for (Attach attach : attchList) {
			imgUrls.add(attach.getPathUpload());
		}
		eventInfoVo.setImgUrls(imgUrls);	//获取上报人上报隐患时提交的图片url
		List<EventProcessVo> processes = this.eventManagerServiceImpl.getEProcessVoByEId(id); //获取事件所有过程
		request.setAttribute("processes", getDpName(processes));
		request.setAttribute("eventInfoVo", eventInfoVo);
		return "/page/safecheck/hiddenDanger/event_handleDetail";
	}
	
	/**
	 * @Description 跳转到	办结事件明细页面
	 * @param id	事件表id
	 * @return
	 * @author xuezb
	 * @Date 2018年5月29日
	 */
	@RequestMapping(value="/event_finishDetail") 
	public String finishDetail(HttpServletRequest request,String id,String epNowNode,Integer epDealState) {
		EventInfo eventInfo = this.eventManagerServiceImpl.getEntityById(EventInfo.class, id);
		EventInfoVo eventInfoVo = new EventInfoVo();
		BeanUtils.copyProperties(eventInfo, eventInfoVo);
		eventInfoVo.setEpNowNode(epNowNode);
		eventInfoVo.setEpDealState(epDealState);
		List<Attach> attchList = this.attachServiceImpl.queryAttchListByFormId(eventInfoVo.getId());
		List<String> imgUrls = new ArrayList<String>();
		for (Attach attach : attchList) {
			imgUrls.add(attach.getPathUpload());
		}
		eventInfoVo.setImgUrls(imgUrls);	//获取上报人上报隐患时提交的图片url
		List<EventProcessVo> processes = this.eventManagerServiceImpl.getEProcessVoByEId(id); //获取事件所有过程
		request.setAttribute("processes", getDpName(processes));
		request.setAttribute("eventInfoVo", eventInfoVo);
		return "/page/safecheck/hiddenDanger/event_finishDetail";
	}
	
	/**
	 * @Description 跳转到	所有事件明细页面
	 * @param id	事件表id
	 * @return
	 * @author xuezb
	 * @Date 2018年6月6日
	 */
	@RequestMapping(value="/event_allDetail") 
	public String allDetail(HttpServletRequest request,String id,String epNowNode,Integer epDealState) {
		EventInfo eventInfo = this.eventManagerServiceImpl.getEntityById(EventInfo.class, id);
		EventInfoVo eventInfoVo = new EventInfoVo();
		BeanUtils.copyProperties(eventInfo, eventInfoVo);
		eventInfoVo.setEpNowNode(epNowNode);
		eventInfoVo.setEpDealState(epDealState);
		List<Attach> attchList = this.attachServiceImpl.queryAttchListByFormId(eventInfoVo.getId());
		List<String> imgUrls = new ArrayList<String>();
		for (Attach attach : attchList) {
			imgUrls.add(attach.getPathUpload());
		}
		eventInfoVo.setImgUrls(imgUrls);	//获取上报人上报隐患时提交的图片url
		List<EventProcessVo> processes = this.eventManagerServiceImpl.getEProcessVoByEId(id); //获取事件所有过程
		request.setAttribute("processes", getDpName(processes));
		request.setAttribute("eventInfoVo", eventInfoVo);
		return "/page/safecheck/hiddenDanger/event_allDetail";
	}
	
	/**
	 * @Description 补全当前处理人所在部门名称
	 * @param List<EventProcessVo>
	 * @return List<EventProcessVo>
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	private List<EventProcessVo> getDpName(List<EventProcessVo> processes){
		List<EventProcessVo> processesVo = new ArrayList<EventProcessVo>();
		for (EventProcessVo p : processes) {
			if(StringUtils.isNotBlank(p.getEpNowPersonId())){
				User user = this.eventManagerServiceImpl.getEntityById(User.class, p.getEpNowPersonId());
				if(user.getOrgFrame() != null){
					p.setEpNowPersonDpName(user.getOrgFrame().getOrgFrameName());	//获取当前处理人所在部门名称
				}
			}
			processesVo.add(p);
		}
		return processesVo;
	}

	
	
	/**
	 * @Description 各部门安全员	将事件上报给	   部门负责人
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月8日
	 */
	@RequestMapping(value="/reportEventToBMFZR",method = RequestMethod.POST) 
	public void reportEventToBMFZR(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveReportEventToBMFZR(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
			
			/*//事件推送给下一节点，发提醒
			String eventTitle=eventInfoVo.getEventTitle();
			String epNextPersonId=eventInfoVo.getEpNextPersonId();
			String epNextRole=eventInfoVo.getEpNextRole();
			
			//如果事件未办结,则将发送提醒给下一处理人
			if(StringUtils.isNotBlank(epNextPersonId) && StringUtils.isNotBlank(epNextRole)){
				sendJpushMsg(eventTitle, eventInfoVo.getEventContent(), epNextPersonId,epNextRole);
			}else{		//如果事件已办结,则不存在下一节点处理人id和下一节点处理角色,此时发送提醒的对象为事件上报人
				EventProcess eventProcess = this.eventManagerServiceImpl.getEProcessByEIdAndEpNowNode(eventInfoVo.getId(), "1");
				sendJpushMsg(eventTitle, eventInfoVo.getEventContent(), eventProcess.getEpNowPersonId(),eventProcess.getEpNowRole());
			}*/
			
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 部门负责人	将事件上报给	   安保办安全员
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月8日
	 */
	@RequestMapping(value="/reportEventToABBAQY") 
	public void reportEventToABBAQY(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveReportEventToABBAQY(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 安保办安全员	将事件上报给	   安保办主任
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月8日
	 */
	@RequestMapping(value="/reportEventToABBZR") 
	public void reportEventToABBZR(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveReportEventToABBZR(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 安保办主任	将事件派遣给	  部门负责人
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月8日
	 */
	@RequestMapping(value="/dispatchEventToBMFZR") 
	public void dispatchEventToBMFZR(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveDispatchEventToBMFZR(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description  部门负责人	将事件派遣给	  处理人
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月8日
	 */
	@RequestMapping(value="/dispatchEventToDealPeople") 
	public void dispatchEventToDealPeople(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveDispatchEventToDealPeople(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 处理人	将事件解决后提交给	安保办安全员   核查	
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月8日
	 */
	@RequestMapping(value="/feedBackEventToABBAQY") 
	public void feedBackEventToABBAQY(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveFeedBackEventToABBAQY(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 安保办主任	对此事件向上级	分管领导	请示
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	@RequestMapping(value="/goUpstairsToFGLD") 
	public void goUpstairsToFGLD(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveGoUpstairsToFGLD(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 分管领导	对此事件向上级	常务副总经理		请示
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	@RequestMapping(value="/goUpstairsToCWFZJL") 
	public void goUpstairsToCWFZJL(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveGoUpstairsToCWFZJL(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 常务副总经理		审批后将事件反馈给		分管领导
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	@RequestMapping(value="/feedBackToFGLD") 
	public void feedBackToFGLD(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveFeedBackToFGLD(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 分管领导	审批后将事件反馈给		安保办主任
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	@RequestMapping(value="/feedBackToABBZR") 
	public void feedBackToABBZR(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveFeedBackToABBZR(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 部门负责人	将事件退回给		安保办主任
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	@RequestMapping(value="/returnToABBZR") 
	public void returnToABBZR(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveReturnToABBZR(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 处理人	将事件退回给		部门负责人
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	@RequestMapping(value="/returnToBMFZR") 
	public void returnToBMFZR(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveReturnToBMFZR(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 安保办安全员	审核后觉得处理不到位将事件退回给	处理人	
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	@RequestMapping(value="/returnToDealPeople") 
	public void returnToDealPeople(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveReturnToDealPeople(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @Description 	事件办结
	 * @param eventInfoVo
	 * @author xuezb
	 * @Date 2019年1月14日
	 */
	@RequestMapping(value="/confirmEventFinish") 
	public void confirmEventFinish(HttpSession httpSession, HttpServletResponse response, EventInfoVo eventInfoVo) {
		JsonObject json = new JsonObject();
		try {
			String id = this.eventManagerServiceImpl.saveConfirmEventFinish(eventInfoVo);
			json.addProperty("result", true);
			json.addProperty("id", id);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	
	/**
	 * @Description 跳转到	选择派遣对象页面	部门负责人
	 * @return
	 * @author xuezb
	 * @Date 2018年5月30日
	 */
	@RequestMapping(value="/event_selectHandDepart") 
	public String selectHandDepart(HttpServletRequest request, String winName) {
		this.getRequest().setAttribute("winName", winName);
		return "/page/safecheck/hiddenDanger/event_selectEventHandler";
	}
	
	/**
	 * @Description 跳转到	选择派遣对象页面	隐患处理人
	 * @return
	 * @author xuezb
	 * @Date 2018年5月30日
	 */
	@RequestMapping(value="/event_selectEventHandler") 
	public String selectEventHandler(HttpServletRequest request, String dpId, String winName) {
		this.getRequest().setAttribute("dpId", dpId);
		this.getRequest().setAttribute("winName", winName);
		return "/page/safecheck/hiddenDanger/event_selectEventHandler";
	}
	
	/**
	 * @Description 隐患处理人列表		加载
	 * @param response
	 * @param userName  
	 * @author xuezb
	 * @Daten 2019年1月8日
	 */
	@RequestMapping(value = "/loadUserList")
	public void loadUserList(HttpServletResponse response, String dpId, String userName) {
		List<User> userList = new ArrayList<>();
		if(StringUtils.isNotBlank(dpId)){
			userList= this.eventManagerServiceImpl.getUserListByRoleCode(Common.yhclrRoleCode, dpId, userName);
		}else{
			userList = this.eventManagerServiceImpl.getUserListByRoleCode(Common.bmfzrRoleCode, dpId, userName);
		}
		 
		List<UserVo> userVoList = new ArrayList<UserVo>();
		for (User user : userList) {
			UserVo userVo = new UserVo();
			BeanUtils.copyProperties(user, userVo);
			if(user.getOrgFrame() != null){
				userVo.setOrgFrameId(user.getOrgFrame().getId());
				userVo.setOrgFrameName(user.getOrgFrame().getOrgFrameName());
			}
			userVoList.add(userVo);
		}
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
		config.setExcludes(new String[]{"orgFrame","roles","createTime","lastLoginTime","loginIp","memo","password","loginTimes"
				,"loginName","orgFrameNames","personalIdentity","qq","sysCode","telephone","workTime","address","age","birthday","certificatesNum"
				,"certificatesType","creatorId","creatorName","dataSource","degree","educationBackground","email","frontLineSituation","nation"
				,"originPlace"});
		json.put("result", true);
		json.put("total", userVoList.size());
		json.put("userList", JSONArray.fromObject(userVoList,config));
		this.print(json);
	}
	
	
	
	/**
	 * @Description 跳转到	   安全隐患整改报告	页面
	 * @param request
	 * @param eventInfoId  
	   * @author xuezb
	 * @Daten 2019年1月8日
	 */
	@RequestMapping(value = "/goToReportPage")
	public String goToReportPage(HttpServletRequest request, String eventInfoId) {
		EventInfo eventInfo = this.eventManagerServiceImpl.getEntityById(EventInfo.class, eventInfoId);
		EventInfoVo eventInfoVo = new EventInfoVo();
		BeanUtils.copyProperties(eventInfo, eventInfoVo);
		
		List<Attach> attachList = this.attachServiceImpl.queryAttchListByFormId(eventInfoId);
		List<String> list = new ArrayList<String>();
		for (Attach attach : attachList) {
			list.add(attach.getPathUpload());
		}
		eventInfoVo.setImgUrls(list);
		
		List<EventProcessVo> processVoList = this.eventManagerServiceImpl.getEProcessVoByEId(eventInfoId);
		EventProcessVo processVo = new EventProcessVo();
		for (EventProcessVo vo : processVoList) {
			if(vo.getEpNowNode().equals("5")){
				processVo = vo;
				break;
			}
		}
		
		this.getRequest().setAttribute("eventInfoVo", eventInfoVo);
		this.getRequest().setAttribute("processVo", processVo);
		return "/page/safecheck/hiddenDanger/rectificationReport";
	}
	
	
	/**
	 * 
	 * @ClassName:EventManagerController.java
	 * @Description: 推送消息到APP
	 * @param noticeTitle 消息标题
	 * @param noticeContent 消息内容
	 * @param userIds 目标用户ID集合，用","分隔，如"123,321"
	 * @param tags 目标用户角色编码集合，用","分隔，如"asd,fds"
	 * @author qinyongqian
	 * @date 2018年6月27日
	 */
	/*public void sendJpushMsg(String noticeTitle,String noticeContent, String userIds,String tags){
		Message msg = new Message();
		msg.setTitle(noticeTitle);
		msg.setContent(noticeContent);
		msg.setAlias(userIds);
		msg.setType(3); //消息类型 3 为事件
		msg.setTags(tags);
		msg.setSender( ((User)this.getHttpSession().getAttribute("user")).getUserName());
		msg.setCreatorId( ((User)this.getHttpSession().getAttribute("user")).getId());
		msg.setCreatorName( ((User)this.getHttpSession().getAttribute("user")).getLoginName());
		msg.setSysCode( ((User)this.getHttpSession().getAttribute("user")).getSysCode());
		this.messageServiceImpl.saveOrUpdate(msg);
		MessageJpush.sendCommonMsg(noticeTitle, msg);
	}*/
	
}
