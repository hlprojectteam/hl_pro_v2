package com.mobile.hiddenDanger;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.o.common.beans.BeanUtils;

import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.google.gson.Gson;
import com.safecheck.hiddenDanger.module.EventInfo;
import com.safecheck.hiddenDanger.module.EventProcess;
import com.safecheck.hiddenDanger.service.IEventManagerService;
import com.safecheck.hiddenDanger.vo.EventInfoVo;
import com.safecheck.hiddenDanger.vo.EventProcessVo;
import com.urms.role.module.Role;
import com.urms.user.module.User;

/**
 * 
 * @Description 隐患接口
 * @author qinyongqian
 * @date 2019年1月11日
 *
 */
@Controller
@RequestMapping(value="/hiddenDangerMobile")
public class MobileHiddenDangerController extends BaseController{

	@Autowired
	private IEventManagerService eventManagerServiceImpl;
	
	@Autowired
	private IAttachService attachServiceImpl;
	
	@RequestMapping(value="/event_addReportEvent",method = RequestMethod.POST)
	public void addReportEvent(HttpSession httpSession, HttpServletResponse response,String jsonData){
		JSONObject json = new JSONObject();
		Gson gson = new Gson();
		try {
			EventInfoVo eventVo = gson.fromJson(jsonData, EventInfoVo.class);
			String id = this.eventManagerServiceImpl.saveEvent(eventVo);
			json.put("result", true);
			json.put("id", id);
			
			//事件上报，给指挥中心推送提醒
			//sendJpushMsg("新事件："+ eventInfoVo.getEventTitle(), eventInfoVo.getEventTitle(), null, "event_CommandCenter");
		} catch (Exception e) {
			json.put("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json);
		}
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param page
	 * @方法：@param rows
	 * @方法：@param eventInfoVo
	 * @描述：上报事件列表		加载
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月15日
	 */
	@RequestMapping(value="/event_loadReportList",method = RequestMethod.POST)
	public void loadReportList(HttpSession httpSession, HttpServletResponse response,Integer page, Integer rows,EventInfoVo eventInfoVo){
		JSONObject json = new JSONObject();
		try {
			Pager pager = this.eventManagerServiceImpl.queryReportList(page, rows, eventInfoVo);
			JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
			config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
			String[] excludes = new String[] {"contactPhone","creatorId","dataSource","epAppraise",
					"epAttachId","epDealContent","epDealWay","epId","epIsReturned","epIsSite","epLimitTime","epNextNode","epNextPersonId",
					"epNextPersonName","epNextRole","epNextRoleName","imgUrls","videoUrl","epNowNodeArriveTime","epNowNodeLeavleTime",
					"eventGPSX","eventGPSY","eventType","loginName",
					"reporterOrgId","roleCode","roleName","sysCode",
					"epNowPersonName","epNowRole","epReturnReason","epUpNode","epUpNodeArriveTime","epUpPersonId",
					"epUpPersonName","epUpRole","epUpRoleName","epWhetherFinish","eventAddress","eventContent"}; // 列表排除信息内容字段，减少传递时间
			config.setExcludes(excludes);
			json.put("result", true);
			json.put("total", pager.getRowCount());
			json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		} catch (Exception e) {
			json.put("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json);
		}
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param page
	 * @方法：@param rows
	 * @方法：@param eventInfoVo
	 * @描述：待办事件
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月16日
	 */
	@RequestMapping(value="/event_loadAgendaList",method = RequestMethod.POST)
	public void loadAgendaList(HttpSession httpSession, HttpServletResponse response,Integer page, Integer rows,EventInfoVo eventInfoVo){
		JSONObject json = new JSONObject();
		try {
			Pager pager = this.eventManagerServiceImpl.queryAgendaList(page, rows, eventInfoVo);
			JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
			config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
			String[] excludes = new String[] {"contactPhone","creatorId","creatorName","dataSource","epAppraise",
					"epAttachId","epDealContent","epDealWay","epIsReturned","epIsSite","epLimitTime","epNextNode","epNextPersonId",
					"epNextPersonName","epNextRole","epNextRoleName","imgUrls","videoUrl","epNowNodeLeavleTime",
					"eventGPSX","eventGPSY","eventType","loginName",
					"reporterOrgId","roleCode","roleName","sysCode",
					"epNowPersonName","epNowRole","epReturnReason","epUpNode","epUpNodeArriveTime","epUpPersonId",
					"epUpPersonName","epUpRole","epUpRoleName","epWhetherFinish","eventAddress","eventContent"}; // 列表排除信息内容字段，减少传递时间
			config.setExcludes(excludes);
			json.put("result", true);
			json.put("total", pager.getRowCount());
			json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		} catch (Exception e) {
			json.put("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json);
		}
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param page
	 * @方法：@param rows
	 * @方法：@param eventInfoVo
	 * @描述：经办事件
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月16日
	 */
	@RequestMapping(value="/event_loadHandleList",method = RequestMethod.POST)
	public void loadHandleList(HttpSession httpSession, HttpServletResponse response,Integer page, Integer rows,EventInfoVo eventInfoVo){
		JSONObject json = new JSONObject();
		try {
			Pager pager = this.eventManagerServiceImpl.queryHandleList(page, rows, eventInfoVo);
			JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
			config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
			String[] excludes = new String[] {"contactPhone","creatorId","creatorName","dataSource","epAppraise",
					"epAttachId","epDealContent","epDealWay","epIsReturned","epIsSite","epLimitTime","epNextNode","epNextPersonId",
					"epNextPersonName","epNextRole","epNextRoleName","imgUrls","videoUrl",
					"eventGPSX","eventGPSY","eventType","loginName",
					"reporterOrgId","roleCode","roleName","sysCode",
					"epNowPersonName","epNowRole","epReturnReason","epUpNode","epUpNodeArriveTime","epUpPersonId",
					"epUpPersonName","epUpRole","epUpRoleName","epWhetherFinish","eventAddress","eventContent"}; // 列表排除信息内容字段，减少传递时间
			config.setExcludes(excludes);
			json.put("result", true);
			json.put("total", pager.getRowCount());
			json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		} catch (Exception e) {
			json.put("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json);
		}
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param page
	 * @方法：@param rows
	 * @方法：@param eventInfoVo
	 * @描述：办结事件
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月16日
	 */
	@RequestMapping(value="/event_loadFinishList",method = RequestMethod.POST)
	public void loadFinishList(HttpSession httpSession, HttpServletResponse response,Integer page, Integer rows,EventInfoVo eventInfoVo){
		JSONObject json = new JSONObject();
		try {
			Pager pager = this.eventManagerServiceImpl.queryFinishList(page, rows, eventInfoVo);
			JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
			config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
			String[] excludes = new String[] {"contactPhone","creatorId","creatorName","dataSource","epAppraise",
					"epAttachId","epDealContent","epDealWay","epIsReturned","epIsSite","epLimitTime","epNextNode","epNextPersonId",
					"epNextPersonName","epNextRole","epNextRoleName","imgUrls","videoUrl","epNowNodeLeavleTime",
					"eventGPSX","eventGPSY","eventType","loginName",
					"reporterOrgId","roleCode","roleName","sysCode",
					"epNowPersonName","epNowRole","epReturnReason","epUpNode","epUpNodeArriveTime","epUpPersonId",
					"epUpPersonName","epUpRole","epUpRoleName","epWhetherFinish","eventAddress","eventContent","epNowPersonId","epNowRoleName"}; // 列表排除信息内容字段，减少传递时间
			config.setExcludes(excludes);
			json.put("result", true);
			json.put("total", pager.getRowCount());
			json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		} catch (Exception e) {
			json.put("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json);
		}
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param jsonData
	 * @描述：待办事件明细
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月16日
	 */
	@RequestMapping(value = "/event_agendaDetail")
	public void agendaDetail(HttpServletRequest request,HttpServletResponse response, String jsonData) {
		JSONObject json = new JSONObject();
		try {
			JSONObject jsonObject = JSONObject.fromObject(jsonData);
			String id = jsonObject.getString("eventId");
			String epNowNode = jsonObject.getString("epNowNode");
			String epId = jsonObject.getString("epId");
			this.eventManagerServiceImpl.saveModifyProcessStatus(epId);//修改事件处理状态，变成处理中
			
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
			
			JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
			config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期(这里精确到秒)
			json.put("processes", JSONArray.fromObject(getDpName(processes), config));
			json.put("eventInfoVo", JSONObject.fromObject(eventInfoVo, config));
			json.put("dpId", user.getOrgFrame().getId());
			json.put("result", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.put("result", false);
			json.put("msg", e.toString());
		}
		this.print(json);
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
	
	
}
