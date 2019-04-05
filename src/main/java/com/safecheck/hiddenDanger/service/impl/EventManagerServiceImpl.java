package com.safecheck.hiddenDanger.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.o.common.beans.BeanUtils;

import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.Common;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.safecheck.hiddenDanger.dao.IEventManagerDao;
import com.safecheck.hiddenDanger.module.EventHandle;
import com.safecheck.hiddenDanger.module.EventInfo;
import com.safecheck.hiddenDanger.module.EventProcess;
import com.safecheck.hiddenDanger.service.IEventManagerService;
import com.safecheck.hiddenDanger.vo.EventInfoVo;
import com.safecheck.hiddenDanger.vo.EventProcessVo;
import com.urms.role.module.Role;
import com.urms.user.module.User;


/**
 * @Description 事件管理service实现类
 * @author xuezb
 * @Date 2018年5月23日
 */
@Repository("eventManagerServiceImpl")
public class EventManagerServiceImpl extends BaseServiceImpl implements IEventManagerService{
	
	@Autowired
	private IEventManagerDao eventManagerDaoImpl;
	
	@Autowired
	private IAttachService attachServiceImpl;

	
	/**
	 * 事件的保存提交
	 */
	@Override
	public String saveEvent(EventInfoVo eventInfoVo) {
		if(eventInfoVo != null){
			if(StringUtils.isBlank(eventInfoVo.getId())){
				EventInfo eventInfo = new EventInfo();
				BeanUtils.copyProperties(eventInfoVo, eventInfo);
				synchronized (this) {	//设置事件编码
					eventInfo.setEventCode(this.buildEventSerialNum("EVENT_INFO"));
					this.eventManagerDaoImpl.save(eventInfo);
				}
				EventProcess process1 = new EventProcess();
				process1.setEventId(eventInfo.getId());		//事件过程关联的事件id
				
				process1.setEpNowNode("1");						//第一个过程的当前节点编码
				process1.setEpNowNodeArriveTime(new Date());	//第一个过程当前节点的到达时间
				process1.setEpNowNodeLeavleTime(new Date());	//第一个过程当前节点的离开时间
				
				process1.setEpNowPersonId(eventInfo.getCreatorId()); 		//第一个过程当前节点的处理人id (事件上报人id)
				process1.setEpNowPersonName(eventInfo.getCreatorName());	//第一个过程当前节点的处理人姓名 (事件上报人姓名)
				process1.setEpNowRole(Common.yhsbrRoleCode);				//第一个过程当前节点的处理角色编码 (事件上报人角色编码)
				process1.setEpNowRoleName(Common.yhsbrRoleName);			//第一个过程当前节点的处理角色名称 (事件上报人角色名称)
				
				process1.setEpNextNode("2");						//第一个过程的下一节点编码 (即第二过程的当前节点编码)
				process1.setEpNextRole(Common.bmaqyRoleCode);		//第一个过程的下一个节点的处理角色编码 (即第二过程的当前处理角色编码)
				process1.setEpNextRoleName(Common.bmaqyRoleName);	//第一个过程的下一个节点的处理角色名称 (即第二过程的当前处理角色名称)
				
				process1.setEpDealState(3); 								//第一个过程的处理状态  【事件上报完,则立马产生两个事件过程,第一个过程的处理状态为3(处理完),第二个过程的处理状态为1(未处理)】
				process1.setEpDealWay(eventInfoVo.getDataSource()); 		//第一个过程的处理端 (1电脑，2APP，3微信，4其它)
				
				/*List<Attach> attchList = this.attachServiceImpl.queryAttchListByFormId(eventInfoVo.getId());
				StringBuffer attchIds = new StringBuffer();
				for (Attach attach : attchList) {
					attchIds.append(attach + ",");
				}
				if(StringUtils.isNotBlank(attchIds)){
					attchIds.deleteCharAt(attchIds.length()-1);
				}
				process1.setEpAttachId(attchIds.toString());		//上报人拍摄的附件图片ID (最多7个附件)
*/				
				this.eventManagerDaoImpl.save(process1);		/* ** 事件上报完产生的第一个事件过程   ** */
				
				
				EventProcess process2 = new EventProcess();
				process2.setEventId(eventInfo.getId());		//事件过程关联的事件id
				
				process2.setEpUpNode(process1.getEpNowNode());						//第二个过程的上一节点编码 (即第一个过程的当前节点编码)
				process2.setEpUpNodeArriveTime(process1.getEpNowNodeArriveTime());	//第二个过程的上一节点的到达时间 (即第一个过程当前节点的到达时间)
				process2.setEpUpRole(process1.getEpNowRole()); 						//第二个过程的上一节点的处理角色编码 (即第一个过程当前节点的处理角色编码)
				process2.setEpUpRoleName(process1.getEpNowRoleName()); 				//第二个过程的上一节点的处理角色名称 (即第一个过程当前节点的处理角色名称)
				process2.setEpUpPersonId(process1.getEpNowPersonId()); 				//第二个过程的上一节点的处理人id (即第一个过程当前节点的处理人id)
				process2.setEpUpPersonName(process1.getEpNowPersonName()); 			//第二个过程的上一节点的处理人姓名 (即第一个过程当前节点的处理人姓名)
				
				process2.setEpNowNode(process1.getEpNextNode());			//第二个过程的当前节点编码 (即第一个过程的下一节点编码)
				process2.setEpNowRole(process1.getEpNextRole());			//第二个过程的当前节点的处理角色编码 (即第一个过程的下一个节点的处理角色编码)
				process2.setEpNowRoleName(process1.getEpNextRoleName());	//第二个过程的当前节点的处理角色名称 (即第一个过程的下一个节点的处理角色名称)
				
				process2.setEpNowNodeArriveTime(new Date());				//第二个过程当前节点的到达时间
				process2.setEpDealState(1);									//第二个过程的处理状态  【事件上报完,则立马产生两个事件过程,第一个过程的处理状态为3(处理完),第二个过程的处理状态为1(未处理)】
				
				this.eventManagerDaoImpl.save(process2);		/* ** 事件上报完产生的第二个事件过程   ** */
				 
				return eventInfo.getId();			//返回事件Id
			}
		}
		return null;
	}
	
	@Override
	public String buildEventSerialNum(String tableName) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss" );
		dateFormat = new SimpleDateFormat("yyyyMMdd");
		String strdate = dateFormat.format(new Date());
		String sql= "select EVENT_CODE from "+ tableName +
				" where EVENT_CODE like 'ET"+strdate +"%' order by EVENT_CODE desc ";
		String eventCode = "ET"+strdate+"0001";
		List<Object> serialNum = this.eventManagerDaoImpl.queryBySql(sql);
		if(serialNum != null && serialNum.size()>0){
			String code = serialNum.get(0).toString();
			code = code.substring(2,code.length());
			long codeInt = Long.parseLong(code)+1;
			eventCode = "ET"+ codeInt;
		}
		return eventCode;
	}
	
	
	/**
	 * (上报人) 上报事件列表	分页查询	
	 */
	@Override
	public Pager queryReportList(Integer page, Integer rows, EventInfoVo eventInfoVo) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		// (上报人)上报事件 : 当前用户为事件上报人
		sql.append("select * from "
					+ "(select ei.id,ei.EVENT_CODE,ei.EVENT_TITLE,ei.EVENT_URGENCY,ei.EVENT_TYPE,ei.CREATOR_NAME,ei.create_Time,ep.Ep_NowNode,ep.Ep_NowRoleName,ep.Ep_DealState "
					+ "from EVENT_INFO ei,EVENT_PROCESS ep "
					+ "where ei.id = ep.EventInfo_ID ");
		if(StringUtils.isNotBlank(eventInfoVo.getEpNowPersonId())){		//当前用户id
			sql.append(" and ei.Creator_id = ? ");						//事件上报人id
			paramList.add(eventInfoVo.getEpNowPersonId());
		}
		
		// 更多筛选条件
		if(StringUtils.isNotBlank(eventInfoVo.getEpNowNode())){		//当前节点编码
			sql.append(" and ep.Ep_NowNode = ");
			paramList.add(eventInfoVo.getEpNowNode().trim());
		}
		if(null != eventInfoVo.getEpDealState()){		//处理状态 (0关闭，1未处理，2处理中，3处理完)
			sql.append(" and ep.Ep_DealState = ? ");
			paramList.add(eventInfoVo.getEpDealState());
		}
		if(StringUtils.isNotBlank(eventInfoVo.getEventTitle())){	//事件标题
			sql.append(" and ei.EVENT_TITLE like ? ");
			paramList.add("%"+eventInfoVo.getEventTitle().trim()+"%");
		}
		if(null != eventInfoVo.getEventurgency()){		//紧急程度
			sql.append(" and ei.EVENT_URGENCY = ? ");
			paramList.add(eventInfoVo.getEventurgency());
		}
		if(null != eventInfoVo.getEventType()){			//事件类型
			sql.append(" and ei.EVENT_TYPE = ? ");
			paramList.add(eventInfoVo.getEventType());
		}
		sql.append(" ORDER BY ep.create_Time DESC,ep.Ep_NowNode DESC) t "
			+ "GROUP BY t.id ORDER BY t.create_Time desc");

		Pager pager = this.eventManagerDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
		List<EventInfoVo> list = new ArrayList<EventInfoVo>();
		
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			EventInfoVo eiVo = new EventInfoVo();
			if(obj[0]!=null) eiVo.setId(obj[0].toString());
			if(obj[1]!=null) eiVo.setEventCode(obj[1].toString());
			if(obj[2]!=null) eiVo.setEventTitle(obj[2].toString());
			if(obj[3]!=null) eiVo.setEventurgency(Integer.parseInt(obj[3].toString()));
			if(obj[4]!=null) eiVo.setEventType(Integer.parseInt(obj[4].toString()));
			if(obj[5]!=null) eiVo.setCreatorName(obj[5].toString());
			if(obj[6]!=null) eiVo.setCreateTime(DateUtil.getDateFromString(obj[6].toString()));
			if(obj[7]!=null) eiVo.setEpNowNode(obj[7].toString());
			if(obj[8]!=null) eiVo.setEpNowRoleName(obj[8].toString());
			if(obj[9]!=null) eiVo.setEpDealState(Integer.parseInt(obj[9].toString()));
			
			list.add(eiVo);
		}
		
		pager.setPageList(list);
		return pager;
	}

	
	/**
	 * (上报人、部门安全员、部门负责人、安保办安全员、处理人) 待办事件列表	分页查询
	 */
	@Override
	public Pager queryAgendaList(Integer page, Integer rows, EventInfoVo eventInfoVo) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		// (上报人、部门安全员、部门负责人、安保办安全员、处理人)待办事件:	事件的当前节点的处理人 为 当前用户,若事件没有指定处理人,则 当前用户角色 包含 当前节点的处理角色 即可;且事件的处理状态为未处理(1)或处理中(2)
		// 若是各个部门的安全员	获取待办事件时，还需要根据	事件上报人所在部门Id	进行筛选
		sql.append("select tt.id,tt.EVENT_CODE,tt.EVENT_TITLE,tt.EVENT_URGENCY,tt.EVENT_TYPE,tt.CREATOR_NAME,tt.create_Time,tt.reporter_OrgId,tt.Ep_NowNode,tt.Ep_NowRoleName,tt.Ep_DealState,tt.epId,tt.Ep_NowNodeArriveTime,tt.Ep_UpNode,tt.Ep_limitTime "
				+ "from "
					+ "(select * from "
						+ "(select ei.id,ei.EVENT_CODE,ei.EVENT_TITLE,ei.EVENT_URGENCY,ei.EVENT_TYPE,ei.CREATOR_NAME,ei.create_Time,ei.reporter_OrgId,"
						+ "ep.Ep_NowNode,ep.Ep_NowRoleName,ep.Ep_NowPersonId,ep.Ep_NowRole,ep.Ep_DealState,ep.id epId,ep.Ep_NowNodeArriveTime,ep.Ep_UpNode,ep.Ep_limitTime "
						+ "from EVENT_INFO ei,EVENT_PROCESS ep  "
						+ "where ei.id = ep.EventInfo_ID "
						+ "ORDER BY ep.create_Time desc,ep.Ep_NowNode desc) t "
					+ "GROUP BY t.id) tt "
				+ "where 1=1 ");
		
		if(StringUtils.isNotBlank(eventInfoVo.getEpNowPersonId())){		// 若事件有指定处理人,以用户是处理人为主
			sql.append(" and (tt.Ep_NowPersonId = ? or tt.Ep_NowPersonId is null or tt.Ep_NowPersonId = '')");
			paramList.add(eventInfoVo.getEpNowPersonId());
		}
		
		User user = eventManagerDaoImpl.getEntityById(User.class, eventInfoVo.getEpNowPersonId());
		Set<Role> roles = user.getRoles();
		
		//当  当前用户是超管或子系统管理员的情况下（用户类型是1或2）, 给当前用户的角色集合中设置一个临时的角色编码，用于条件过滤！！！
		if(user.getType() == 1){
			Role role = new Role();
			role.setRoleCode("superamdin");
			roles.add(role);
		}else if(user.getType() == 2){
			Role role = new Role();
			role.setRoleCode("sysadmin");
			roles.add(role);
		}
	
		if(roles != null && roles.size() > 0){				// 若事件没有指定处理人，则当前用户角色   必须包含  当前节点的处理角色
			sql.append(" and (tt.Ep_NowRole in (");
			for (int i = 0; i < roles.size(); i++) {
				sql.append("?,");
			}
			sql.deleteCharAt(sql.length()-1);	//删除多余的逗号
			sql.append(")");
			for (Role role : roles) {
				paramList.add(role.getRoleCode());
			}
			sql.append(" or tt.Ep_NowRole = '" + Common.yhsbrRoleCode + "')");	/* **注:上报人对用户无角色要求,event_yhsbr只是为了方便角色展示. ** */
		}
		
		
		// 若是各个部门的    安全员 或 负责人     获取待办事件时，还需要根据	事件上报人所在部门Id	进行筛选
		for (Role role : roles) {
			if(StringUtils.isNotBlank(role.getRoleCode()) && (role.getRoleCode().equals(Common.bmaqyRoleCode) || role.getRoleCode().equals(Common.bmfzrRoleCode))){
				if(user.getOrgFrame() != null){
					sql.append(" and (tt.reporter_OrgId = ? or tt.Ep_NowNode = '6' or tt.Ep_NowNode = '7' )");		//各个部门的    安全员 或 负责人      根据  事件上报人所在部门Id 获取其同一部门员工所上报的事件	(如果当前节点是6,7 则可不安这一规则)
					paramList.add(user.getOrgFrame().getId());
				}
			}
		}
		
				
		sql.append(" and tt.Ep_DealState in (?,?) ");
		paramList.add(1);	//未处理
		paramList.add(2);	//处理中
		
		// 更多筛选条件
		if(null != eventInfoVo.getEpDealState()){		//处理状态 (0关闭，1未处理，2处理中，3处理完)
			sql.append(" and tt.Ep_DealState = ? ");
			paramList.add(eventInfoVo.getEpDealState());
		}
		if(StringUtils.isNotBlank(eventInfoVo.getEventTitle())){	//事件标题
			sql.append(" and tt.EVENT_TITLE like ? ");
			paramList.add("%"+eventInfoVo.getEventTitle().trim()+"%");
		}
		if(null != eventInfoVo.getEventurgency()){		//紧急程度
			sql.append(" and tt.EVENT_URGENCY = ? ");
			paramList.add(eventInfoVo.getEventurgency());
		}
		if(null != eventInfoVo.getEventType()){			//事件类型
			sql.append(" and tt.EVENT_TYPE = ? ");
			paramList.add(eventInfoVo.getEventType());
		}
		sql.append(" order by tt.Ep_NowNodeArriveTime desc");		//待办事件列表按当前节点到达时间降序排列
		
		Pager pager = this.eventManagerDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
		List<EventInfoVo> list = new ArrayList<EventInfoVo>();
		
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			EventInfoVo eiVo = new EventInfoVo();
			if(obj[0]!=null) eiVo.setId(obj[0].toString());
			if(obj[1]!=null) eiVo.setEventCode(obj[1].toString());
			if(obj[2]!=null) eiVo.setEventTitle(obj[2].toString());
			if(obj[3]!=null) eiVo.setEventurgency(Integer.parseInt(obj[3].toString()));
			if(obj[4]!=null) eiVo.setEventType(Integer.parseInt(obj[4].toString()));
			if(obj[5]!=null) eiVo.setCreatorName(obj[5].toString());
			if(obj[6]!=null) eiVo.setCreateTime(DateUtil.getDateFromString(obj[6].toString()));
			if(obj[7]!=null) eiVo.setReporterOrgId(obj[7].toString());
			if(obj[8]!=null) eiVo.setEpNowNode(obj[8].toString());
			if(obj[9]!=null) eiVo.setEpNowRoleName(obj[9].toString());
			if(obj[10]!=null) eiVo.setEpDealState(Integer.parseInt(obj[10].toString()));
			if(obj[11]!=null) eiVo.setEpId(obj[11].toString());
			if(obj[12]!=null) eiVo.setEpNowNodeArriveTime(DateUtil.getDateFromString(obj[12].toString()));
			if(obj[13]!=null) eiVo.setEpUpNode(obj[13].toString());
			if(obj[14]!=null) eiVo.setEpLimitTime(DateUtil.getDateFromString(obj[14].toString()));
			list.add(eiVo);
		}
		
		pager.setPageList(list);
		return pager;
	}

	
	/**
	 * (上报人、部门安全员、安保办安全员、处理人) 经办事件列表	分页查询
	 */
	@Override
	public Pager queryHandleList(Integer page, Integer rows, EventInfoVo eventInfoVo) {
		List<Object> attributeList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		//经办事件: 经办事件表中数据
		//经办事件表,事件过程表的关联查询
		if(StringUtils.isNotBlank(eventInfoVo.getEpNowPersonId())){		//当前用户id
			sql.append("select eh.EventInfo_ID,eh.EVENT_CODE,eh.EVENT_TITLE,eh.EVENT_URGENCY,eh.EVENT_TYPE,eh.EVENT_REPORTNAME,eh.EVENT_REPORTTIME,eh.Ep_NowNodeArriveTime,eh.Ep_NowNodeLeavleTime,"
						+ "ep.Ep_NowNode,ep.Ep_NowRoleName,ep.Ep_DealState,eh.SYS_CODE"
						//+ "	from (select * from event_handle where Ep_NowPersonId = ? ) eh "
						+ " from (select * from (select * from event_handle ee where ee.Ep_NowPersonId = ? ORDER BY ee.Ep_NowNodeArriveTime DESC) hh  GROUP BY hh.EventInfo_ID) eh "
						+ "left join (select * from (select ep.EventInfo_ID,ep.Ep_NowNode,ep.Ep_NowRoleName,ep.Ep_DealState,ep.create_Time epCreate_Time"
													+ " from event_process ep where ep.EventInfo_ID not in (select EventInfo_ID from event_process where Ep_NowNode = '9')"		//节点9:办结节点
													+ " ORDER BY ep.CREATE_TIME DESC,ep.Ep_NowNode DESC) t "
									+ "group by t.EventInfo_ID) ep"
						+ " on eh.EventInfo_ID = ep.EventInfo_ID ");
			
			attributeList.add(eventInfoVo.getEpNowPersonId());
		}
		
		// 更多筛选条件
		if(StringUtils.isNotBlank(eventInfoVo.getEventTitle())){	//事件标题
			sql.append(" and eh.EVENT_TITLE like ? ");
			attributeList.add("%"+eventInfoVo.getEventTitle().trim()+"%");
		}
		if(!Common.SYSCODE.equals("tr_city")){	//系統编码
			sql.append(" and eh.SYS_CODE = ? ");
			attributeList.add(Common.SYSCODE);
		}
		if(null != eventInfoVo.getEventurgency()){		//紧急程度
			sql.append(" and eh.EVENT_URGENCY = ? ");
			attributeList.add(eventInfoVo.getEventurgency());
		}
		if(null != eventInfoVo.getEventType()){			//事件类型
			sql.append(" and eh.EVENT_TYPE = ? ");
			attributeList.add(eventInfoVo.getEventType());
		}
		sql.append(" order by ep.epCreate_Time desc");		//经办事件以最后一个过程的创建时间(即事件的办结时间)倒序排列
	
		Pager pager = this.eventManagerDaoImpl.queryEntitySQLList(page, rows, sql.toString(), attributeList);
		
		List<EventInfoVo> list = new ArrayList<EventInfoVo>();
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			EventInfoVo eiVo = new EventInfoVo();
			if(obj[0]!=null) eiVo.setId(obj[0].toString());
			if(obj[1]!=null) eiVo.setEventCode(obj[1].toString());
			if(obj[2]!=null) eiVo.setEventTitle(obj[2].toString());
			if(obj[3]!=null) eiVo.setEventurgency(Integer.parseInt(obj[3].toString()));
			if(obj[4]!=null) eiVo.setEventType(Integer.parseInt(obj[4].toString()));
			if(obj[5]!=null) eiVo.setCreatorName(obj[5].toString());
			if(obj[6]!=null) eiVo.setCreateTime(DateUtil.getDateFromString(obj[6].toString()));
			if(obj[7]!=null) eiVo.setEpNowNodeArriveTime(DateUtil.getDateFromString(obj[7].toString()));
			if(obj[8]!=null) eiVo.setEpNowNodeLeavleTime(DateUtil.getDateFromString(obj[8].toString()));
			if(obj[9]!=null) eiVo.setEpNowNode(obj[9].toString());
			if(obj[10]!=null) eiVo.setEpNowRoleName(obj[10].toString());
			if(obj[11]!=null) eiVo.setEpDealState(Integer.parseInt(obj[11].toString()));
			if(obj[12]!=null) eiVo.setSysCode(obj[12].toString());
			
			list.add(eiVo);
		}
		
		pager.setPageList(list);
		return pager;
	}

	
	/**
	 * (上报人、部门安全员、安保办安全员、处理人) 办结事件列表	分页查询
	 */
	@Override
	public Pager queryFinishList(Integer page, Integer rows, EventInfoVo eventInfoVo) {
		List<Object> attributeList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		//办结事件 : 当前用户为事件处理人之一	且	事件状态为处理完(3)
		//事件表,事件过程表的关联查询
		if(StringUtils.isNotBlank(eventInfoVo.getEpNowPersonId())){		//当前用户id
			sql.append("select tt.id,tt.EVENT_CODE,tt.EVENT_TITLE,tt.EVENT_URGENCY,tt.EVENT_TYPE,tt.CREATOR_NAME,tt.create_Time,tt.Ep_NowNode,tt.Ep_DealState,tt.Ep_NowNodeArriveTime  from ("
							+ "select * from ("
										+ "select ei.id,ei.EVENT_CODE,ei.EVENT_TITLE,ei.EVENT_URGENCY,ei.EVENT_TYPE,ei.CREATOR_NAME,ei.create_Time,"
										+ "ep.Ep_NowNode,ep.Ep_NowPersonId,ep.Ep_NowRole,ep.Ep_DealState,ep.create_Time epCreate_Time,ep.Ep_NowNodeArriveTime  from event_info ei,event_process ep "
										+ " where ei.id = ep.EventInfo_ID and ei.id in "
										+ " (select EventInfo_ID from event_process where Ep_NowPersonId = ? ) "			//	Ep_NowPersonId = ?	判断登录用户是否为某个事件过程的的处理人,即可得知该用户是否为事件处理人之一
										+ " ORDER BY ep.create_Time desc,ep.Ep_NowNode desc "
							+ ") t GROUP BY t.id "
					+ ") tt where tt.Ep_DealState = 3 ");
			
			attributeList.add(eventInfoVo.getEpNowPersonId().trim());
		}
			
		
		// 更多筛选条件
		if(StringUtils.isNotBlank(eventInfoVo.getEventTitle())){	//事件标题
			sql.append(" and tt.EVENT_TITLE like ? ");
			attributeList.add("%"+eventInfoVo.getEventTitle().trim()+"%");
		}
		if(null != eventInfoVo.getEventurgency()){		//紧急程度
			sql.append(" and tt.EVENT_URGENCY = ? ");
			attributeList.add(eventInfoVo.getEventurgency());
		}
		if(null != eventInfoVo.getEventType()){			//事件类型
			sql.append(" and tt.EVENT_TYPE = ? ");
			attributeList.add(eventInfoVo.getEventType());
		}
		sql.append(" order by tt.epCreate_Time desc");			//办结事件以最后一个过程的创建时间(即事件的办结时间)倒序排列

		Pager pager = this.eventManagerDaoImpl.queryEntitySQLList(page, rows, sql.toString(), attributeList);
		List<EventInfoVo> list = new ArrayList<EventInfoVo>();
		
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			EventInfoVo eiVo = new EventInfoVo();
			if(obj[0]!=null) eiVo.setId(obj[0].toString());
			if(obj[1]!=null) eiVo.setEventCode(obj[1].toString());
			if(obj[2]!=null) eiVo.setEventTitle(obj[2].toString());
			if(obj[3]!=null) eiVo.setEventurgency(Integer.parseInt(obj[3].toString()));
			if(obj[4]!=null) eiVo.setEventType(Integer.parseInt(obj[4].toString()));
			if(obj[5]!=null) eiVo.setCreatorName(obj[5].toString());
			if(obj[6]!=null) eiVo.setCreateTime(DateUtil.getDateFromString(obj[6].toString()));
			if(obj[7]!=null) eiVo.setEpNowNode(obj[7].toString());
			if(obj[8]!=null) eiVo.setEpDealState(Integer.parseInt(obj[8].toString()));
			if(obj[9]!=null) eiVo.setEpNowNodeArriveTime(DateUtil.getDateFromString(obj[9].toString()));
			
			list.add(eiVo);
		}
		
		pager.setPageList(list);
		return pager;
	}

	
	/**
	 * 所有事件列表	分页查询
	 */
	@Override
	public Pager queryAllList(Integer page, Integer rows, EventInfoVo eventInfoVo) {
		List<Object> attributeList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();

		//所有事件 : 当前用户为超级管理员,可查看所有事件
		//事件表,事件过程表的关联查询
		sql.append("select * from (select ei.id,ei.EVENT_CODE,ei.EVENT_TITLE,ei.EVENT_URGENCY,ei.EVENT_TYPE,ei.CREATOR_NAME,ei.create_Time,ep.Ep_NowNode,ep.Ep_NowRoleName,ep.Ep_DealState,ei.reporter_OrgId,ei.CREATOR_ID "
				+ "from EVENT_INFO ei,EVENT_PROCESS ep where ei.id = ep.EventInfo_ID ");
		
		// 更多筛选条件
		if(StringUtils.isNotEmpty(eventInfoVo.getCreatorId())){		//事件上报人ID
			sql.append(" and ei.Creator_id = ? ");
			attributeList.add(eventInfoVo.getCreatorId());
		}
		if(StringUtils.isNotBlank(eventInfoVo.getCreatorName())){		//事件上报人姓名
			sql.append(" and ei.Creator_Name like ? ");
			attributeList.add("%"+eventInfoVo.getCreatorName().trim()+"%");
		}
		if(StringUtils.isNotBlank(eventInfoVo.getEpNowNode())){		//当前节点编码
			sql.append(" and ep.Ep_NowNode = ");
			attributeList.add(eventInfoVo.getEpNowNode().trim());
		}
		if(StringUtils.isNotEmpty(eventInfoVo.getEventCode())){		//事件编码
			sql.append(" and ei.EVENT_CODE = ? ");
			attributeList.add(eventInfoVo.getEventCode().trim());
		}
		if(null != eventInfoVo.getEpDealState()){		//处理状态 (0关闭，1未处理，2处理中，3处理完)
			sql.append(" and ep.Ep_DealState = ? ");
			attributeList.add(eventInfoVo.getEpDealState());
		}
		if(StringUtils.isNotBlank(eventInfoVo.getEventTitle())){	//事件标题
			sql.append(" and ei.EVENT_TITLE like ? ");
			attributeList.add("%"+eventInfoVo.getEventTitle().trim()+"%");
		}
		if(null != eventInfoVo.getEventurgency()){		//紧急程度
			sql.append(" and ei.EVENT_URGENCY = ? ");
			attributeList.add(eventInfoVo.getEventurgency());
		}
		if(null != eventInfoVo.getEventType()){			//事件类型
			sql.append(" and ei.EVENT_TYPE = ? ");
			attributeList.add(eventInfoVo.getEventType());
		}
		
		sql.append(" ORDER BY ep.create_Time DESC,ep.Ep_NowNode DESC) t GROUP BY t.id ORDER BY t.create_Time desc");		//根据 事件上报时间 倒序排列

		Pager pager = this.eventManagerDaoImpl.queryEntitySQLList(page, rows, sql.toString(), attributeList);
		List<EventInfoVo> list = new ArrayList<EventInfoVo>();
		
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			EventInfoVo eiVo = new EventInfoVo();
			if(obj[0]!=null) eiVo.setId(obj[0].toString());
			if(obj[1]!=null) eiVo.setEventCode(obj[1].toString());
			if(obj[2]!=null) eiVo.setEventTitle(obj[2].toString());
			if(obj[3]!=null) eiVo.setEventurgency(Integer.parseInt(obj[3].toString()));
			if(obj[4]!=null) eiVo.setEventType(Integer.parseInt(obj[4].toString()));
			if(obj[5]!=null) eiVo.setCreatorName(obj[5].toString());
			if(obj[6]!=null) eiVo.setCreateTime(DateUtil.getDateFromString(obj[6].toString()));
			if(obj[7]!=null) eiVo.setEpNowNode(obj[7].toString());
			if(obj[8]!=null) eiVo.setEpNowRoleName(obj[8].toString());
			if(obj[9]!=null) eiVo.setEpDealState(Integer.parseInt(obj[9].toString()));
			if(obj[10]!=null) eiVo.setReporterOrgId(obj[10].toString());
			if(obj[11]!=null) eiVo.setCreatorId(obj[11].toString());
			list.add(eiVo);
		}
		
		pager.setPageList(list);
		return pager;
	}

	
	/**
	 * 事件过程处理状态的修改		(将过程状态由  1:未处理      改为      2:处理中   )
	 */
	@Override
	public String saveModifyProcessStatus(String epId) {
		EventProcess eventProcess = this.eventManagerDaoImpl.getEntityById(EventProcess.class, epId);
		if(eventProcess != null){
			if(null != eventProcess.getEpDealState() && eventProcess.getEpDealState() == 1){	// 如果事件过程的处理状态不为空 且 处于未处理状态,将其改为处理中状态
				eventProcess.setEpDealState(2);
				this.eventManagerDaoImpl.saveOrUpdate(eventProcess);
				return eventProcess.getId();
			}
		}
		return null;
	}
	
	
	/**
	 * 各部门安全员	将事件上报给	   部门负责人		(2->3)
	 */
	@Override
	public String saveReportEventToBMFZR(EventInfoVo eventInfoVo) {
		EventProcess process2 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
		process2.setEpNowNodeLeavleTime(new Date());					//第二个过程的当前节点的离开时间
		process2.setEpNowPersonId(eventInfoVo.getEpNowPersonId());		//第二个过程的当前节点的处理人id
		process2.setEpNowPersonName(eventInfoVo.getEpNowPersonName());	//第二个过程的当前节点的处理人姓名
		
		process2.setEpNextNode("3");						//第二个过程的下一节点编码 (即第三过程的当前节点编码)
		process2.setEpNextRole(Common.bmfzrRoleCode);		//第一个过程的下一个节点的处理角色编码 (即第二过程的当前处理角色编码)
		process2.setEpNextRoleName(Common.bmfzrRoleName);	//第一个过程的下一个节点的处理角色名称 (即第二过程的当前处理角色名称)
		
		process2.setEpDealState(3);									//第二个过程的处理状态
		process2.setEpDealWay(eventInfoVo.getDataSource());			//第二个过程的处理端 (1电脑，2APP，3微信，4其它)
		process2.setEpAttachId(eventInfoVo.getEpAttachId());		//部门安全员拍摄的附件图片ID(如果部门安全员也上传提交附件的话)
		process2.setEpWhetherFinish(eventInfoVo.getEpWhetherFinish()); 	//是否办结
		process2.setEpDealContent(eventInfoVo.getEpDealContent());	//部门安全员给出的处理意见
		
		this.eventManagerDaoImpl.update(process2);		/* ** 部门安全员  将事件上报给   部门负责人   后更新第二个事件过程   ** */
		saveEventHandle(process2);		//新增经办事件
		
		
		EventProcess process3 = new EventProcess();
		process3.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
		
		process3.setEpUpNode(process2.getEpNowNode());						//第三个过程的上一节点编码 (即第二个过程的当前节点编码)
		process3.setEpUpNodeArriveTime(process2.getEpNowNodeArriveTime());	//第三个过程的上一节点的到达时间 (即第二个过程当前节点的到达时间)
		process3.setEpUpRole(process2.getEpNowRole()); 						//第三个过程的上一节点的处理角色编码 (即第二个过程当前节点的处理角色编码)
		process3.setEpUpRoleName(process2.getEpNowRoleName()); 				//第三个过程的上一节点的处理角色名称 (即第二个过程当前节点的处理角色名称)
		process3.setEpUpPersonId(process2.getEpNowPersonId()); 				//第三个过程的上一节点的处理人id (即第二个过程当前节点的处理人id)
		process3.setEpUpPersonName(process2.getEpNowPersonName()); 			//第三个过程的上一节点的处理人姓名 (即第二个过程当前节点的处理人姓名)
		
		process3.setEpNowNode(process2.getEpNextNode());			//第三个过程的当前节点编码 (即第二个过程的下一节点编码)
		process3.setEpNowRole(process2.getEpNextRole());			//第三个过程的当前节点的处理角色编码 (即第二个过程的下一个节点的处理角色编码)
		process3.setEpNowRoleName(process2.getEpNextRoleName());	//第三个过程的当前节点的处理角色名称 (即第二个过程的下一个节点的处理角色名称)
		
		process3.setEpNowNodeArriveTime(new Date());				//第三个过程当前节点的到达时间
		process3.setEpDealState(1);									//第三个过程的处理状态
		
		this.eventManagerDaoImpl.save(process3);		/* ** 部门安全员  将事件上报给   部门负责人    产生的第三个事件过程   ** */
		
		return eventInfoVo.getId();			//返回事件Id
	}
	
	
	/**
	 *  部门负责人	将事件上报给	   安保办安全员	(3->4)
	 */
	@Override
	public String saveReportEventToABBAQY(EventInfoVo eventInfoVo) {
		EventProcess process3 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
		process3.setEpNowNodeLeavleTime(new Date());					//第三个过程的当前节点的离开时间
		process3.setEpNowPersonId(eventInfoVo.getEpNowPersonId());		//第三个过程的当前节点的处理人id
		process3.setEpNowPersonName(eventInfoVo.getEpNowPersonName());	//第三个过程的当前节点的处理人姓名
		
		process3.setEpNextNode("4");						//第三个过程的下一节点编码 (即第三过程的当前节点编码)
		process3.setEpNextRole(Common.abbaqyRoleCode);		//第一个过程的下一个节点的处理角色编码 (即第二过程的当前处理角色编码)
		process3.setEpNextRoleName(Common.abbaqyRoleName);	//第一个过程的下一个节点的处理角色名称 (即第二过程的当前处理角色名称)
		
		process3.setEpDealState(3);									//第三个过程的处理状态
		process3.setEpDealWay(eventInfoVo.getDataSource());			//第三个过程的处理端 (1电脑，2APP，3微信，4其它)
		process3.setEpAttachId(eventInfoVo.getEpAttachId());		//部门安全员拍摄的附件图片ID(如果部门安全员也上传提交附件的话)
		process3.setEpWhetherFinish(eventInfoVo.getEpWhetherFinish()); 	//是否办结
		process3.setEpDealContent(eventInfoVo.getEpDealContent());	//部门安全员给出的处理意见
		
		this.eventManagerDaoImpl.update(process3);		/* ** 部门负责人  将事件上报给   安保办安全员   后更新第三个事件过程   ** */
		saveEventHandle(process3);		//新增经办事件
		
		
		EventProcess process4 = new EventProcess();
		process4.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
		
		process4.setEpUpNode(process3.getEpNowNode());						//第四个过程的上一节点编码 (即第三个过程的当前节点编码)
		process4.setEpUpNodeArriveTime(process3.getEpNowNodeArriveTime());	//第四个过程的上一节点的到达时间 (即第三个过程当前节点的到达时间)
		process4.setEpUpRole(process3.getEpNowRole()); 						//第四个过程的上一节点的处理角色编码 (即第三个过程当前节点的处理角色编码)
		process4.setEpUpRoleName(process3.getEpNowRoleName()); 				//第四个过程的上一节点的处理角色名称 (即第三个过程当前节点的处理角色名称)
		process4.setEpUpPersonId(process3.getEpNowPersonId()); 				//第四个过程的上一节点的处理人id (即第三个过程当前节点的处理人id)
		process4.setEpUpPersonName(process3.getEpNowPersonName()); 			//第四个过程的上一节点的处理人姓名 (即第三个过程当前节点的处理人姓名)
		
		process4.setEpNowNode(process3.getEpNextNode());			//第四个过程的当前节点编码 (即第三个过程的下一节点编码)
		process4.setEpNowRole(process3.getEpNextRole());			//第四个过程的当前节点的处理角色编码 (即第三个过程的下一个节点的处理角色编码)
		process4.setEpNowRoleName(process3.getEpNextRoleName());	//第四个过程的当前节点的处理角色名称 (即第三个过程的下一个节点的处理角色名称)
		
		process4.setEpNowNodeArriveTime(new Date());				//第四个过程当前节点的到达时间
		process4.setEpDealState(1);									//第四个过程的处理状态
		
		this.eventManagerDaoImpl.save(process4);		/* ** 部门负责人  将事件上报给   安保办安全员 产生的第四个事件过程   ** */
		
		return eventInfoVo.getId();			//返回事件Id
	}
	
	
	/**
	 *  安保办安全员	将事件上报给	   安保办主任		(4->5)
	 */
	@Override
	public String saveReportEventToABBZR(EventInfoVo eventInfoVo) {
		EventProcess process4 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
		process4.setEpNowNodeLeavleTime(new Date());					//第四个过程的当前节点的离开时间
		process4.setEpNowPersonId(eventInfoVo.getEpNowPersonId());		//第四个过程的当前节点的处理人id
		process4.setEpNowPersonName(eventInfoVo.getEpNowPersonName());	//第四个过程的当前节点的处理人姓名
		
		process4.setEpNextNode("5");						//第四个过程的下一节点编码 (即第五个过程的当前节点编码)
		process4.setEpNextRole(Common.abbzrRoleCode);		//第四个过程的下一个节点的处理角色编码 (即第五个过程的当前处理角色编码)
		process4.setEpNextRoleName(Common.abbzrRoleName);	//第四个过程的下一个节点的处理角色名称 (即第五个过程的当前处理角色名称)
		
		process4.setEpDealState(3);									//第四个过程的处理状态
		process4.setEpDealWay(eventInfoVo.getDataSource());			//第四个过程的处理端 (1电脑，2APP，3微信，4其它)
		process4.setEpAttachId(eventInfoVo.getEpAttachId());		//部门安全员拍摄的附件图片ID(如果部门安全员也上传提交附件的话)
		process4.setEpWhetherFinish(eventInfoVo.getEpWhetherFinish()); 	//是否办结
		process4.setEpDealContent(eventInfoVo.getEpDealContent());	//部门安全员给出的处理意见
		
		this.eventManagerDaoImpl.update(process4);		/* ** 安保办安全员  将事件上报给   安保办主任   后更新第四个事件过程   ** */
		saveEventHandle(process4);		//新增经办事件
		
		
		EventProcess process5 = new EventProcess();
		process5.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
		
		process5.setEpUpNode(process4.getEpNowNode());						//第五个过程的上一节点编码 (即第四个过程的当前节点编码)
		process5.setEpUpNodeArriveTime(process4.getEpNowNodeArriveTime());	//第五个过程的上一节点的到达时间 (即第四个过程当前节点的到达时间)
		process5.setEpUpRole(process4.getEpNowRole()); 						//第五个过程的上一节点的处理角色编码 (即第四个过程当前节点的处理角色编码)
		process5.setEpUpRoleName(process4.getEpNowRoleName()); 				//第五个过程的上一节点的处理角色名称 (即第四个过程当前节点的处理角色名称)
		process5.setEpUpPersonId(process4.getEpNowPersonId()); 				//第五个过程的上一节点的处理人id (即第四个过程当前节点的处理人id)
		process5.setEpUpPersonName(process4.getEpNowPersonName()); 			//第五个过程的上一节点的处理人姓名 (即第四个过程当前节点的处理人姓名)
		
		process5.setEpNowNode(process4.getEpNextNode());			//第五个过程的当前节点编码 (即第四个过程的下一节点编码)
		process5.setEpNowRole(process4.getEpNextRole());			//第五个过程的当前节点的处理角色编码 (即第四个过程的下一个节点的处理角色编码)
		process5.setEpNowRoleName(process4.getEpNextRoleName());	//第五个过程的当前节点的处理角色名称 (即第四个过程的下一个节点的处理角色名称)
		
		process5.setEpNowNodeArriveTime(new Date());				//第五个过程当前节点的到达时间
		process5.setEpDealState(1);									//第五个过程的处理状态
		
		this.eventManagerDaoImpl.save(process5);		/* ** 安保办安全员  将事件上报给   安保办主任 产生的第五个事件过程   ** */
		
		return eventInfoVo.getId();			//返回事件Id
	}
	
	
	/**
	 * 安保办主任	将事件派遣给	  部门负责人		(5->6)
	 */
	@Override
	public String saveDispatchEventToBMFZR(EventInfoVo eventInfoVo){
		EventProcess process5 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
		process5.setEpNowNodeLeavleTime(new Date());					//第五个过程的当前节点的离开时间
		process5.setEpNowPersonId(eventInfoVo.getEpNowPersonId());		//第五个过程的当前节点的处理人id
		process5.setEpNowPersonName(eventInfoVo.getEpNowPersonName());	//第五个过程的当前节点的处理人姓名
		
		process5.setEpNextNode("6");						//第五个过程的下一节点编码 (即第六个过程的当前节点编码)
		process5.setEpNextRole(Common.bmfzrRoleCode);		//第五个过程的下一个节点的处理角色编码 (即第六个过程的当前处理角色编码)
		process5.setEpNextRoleName(Common.bmfzrRoleName);	//第五个过程的下一个节点的处理角色名称 (即第六个过程的当前处理角色名称)
		process5.setEpNextPersonId(eventInfoVo.getEpNextPersonId());	//第五个过程的下一个节点的处理人id (即第六个过程的当前处理人id)
		process5.setEpNextPersonName(eventInfoVo.getEpNextPersonName());//第五个过程的下一个节点的处理人姓名 (即第六个过程的当前处理人姓名)
		       
		process5.setEpDealState(3);									//第五个过程的处理状态
		process5.setEpDealWay(eventInfoVo.getDataSource());			//第五个过程的处理端 (1电脑，2APP，3微信，4其它)
		process5.setEpAttachId(eventInfoVo.getEpAttachId());		//安保办安全员拍摄的附件图片ID(如果安保办安全员也上传提交附件的话)
		process5.setEpWhetherFinish(eventInfoVo.getEpWhetherFinish()); 	//是否办结
		process5.setEpDealContent(eventInfoVo.getEpDealContent());	//安保办安全员给出的处理意见
		process5.setEpLimitTime(eventInfoVo.getEpLimitTime()); 		//处理时限
		
		this.eventManagerDaoImpl.update(process5);		 /** 安保办主任	将事件派遣给	  部门负责人     后更新第五个事件过程   **/
		saveEventHandle(process5);		//新增经办事件
		
		
		EventProcess process6 = new EventProcess();
		process6.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
		       
		process6.setEpUpNode(process5.getEpNowNode());						//第六个过程的上一节点编码 (即第五个过程的当前节点编码)
		process6.setEpUpNodeArriveTime(process5.getEpNowNodeArriveTime());	//第六个过程的上一节点的到达时间 (即第五个过程当前节点的到达时间)
		process6.setEpUpRole(process5.getEpNowRole()); 						//第六个过程的上一节点的处理角色编码 (即第五个过程当前节点的处理角色编码)
		process6.setEpUpRoleName(process5.getEpNowRoleName()); 				//第六个过程的上一节点的处理角色名称 (即第五个过程当前节点的处理角色名称)
		process6.setEpUpPersonId(process5.getEpNowPersonId()); 				//第六个过程的上一节点的处理人id (即第五个过程当前节点的处理人id)
		process6.setEpUpPersonName(process5.getEpNowPersonName()); 			//第六个过程的上一节点的处理人姓名 (即第五个过程当前节点的处理人姓名)
		       
		process6.setEpNowNode(process5.getEpNextNode());			//第六个过程的当前节点编码 (即第五个过程的下一节点编码)
		process6.setEpNowRole(process5.getEpNextRole());			//第六个过程的当前节点的处理角色编码 (即第五个过程的下一个节点的处理角色编码)
		process6.setEpNowRoleName(process5.getEpNextRoleName());	//第六个过程的当前节点的处理角色名称 (即第五个过程的下一个节点的处理角色名称)
		process6.setEpNowPersonId(process5.getEpNextPersonId());	//第六个过程的当前节点的处理人id (即第五个过程的下一个节点的处理人id)
		process6.setEpNowPersonName(process5.getEpNextPersonName());//第六个过程的当前节点的处理人姓名 (即第五个过程的下一个节点的处理人姓名)
		       
		process6.setEpNowNodeArriveTime(new Date());				//第六个过程当前节点的到达时间
		process6.setEpDealState(1);									//第六个过程的处理状态
		process6.setEpLimitTime(eventInfoVo.getEpLimitTime()); 		//处理时限
		
		this.eventManagerDaoImpl.save(process6);		 /** 安保办主任	将事件派遣给	  部门负责人   后产生的第六个事件过程   **/ 
		
		return eventInfoVo.getId();			//返回事件Id
	};
	
	
	/**
	 * 部门负责人	将事件派遣给	  处理人	(6->7)
	 */
	@Override
	public String saveDispatchEventToDealPeople(EventInfoVo eventInfoVo){
		EventProcess process6 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
		process6.setEpNowNodeLeavleTime(new Date());					//第六个过程的当前节点的离开时间
		process6.setEpNowPersonId(eventInfoVo.getEpNowPersonId());		//第六个过程的当前节点的处理人id
		process6.setEpNowPersonName(eventInfoVo.getEpNowPersonName());	//第六个过程的当前节点的处理人姓名
		
		process6.setEpNextNode("7");						//第六个过程的下一节点编码 (即第七个过程的当前节点编码)
		process6.setEpNextRole(Common.yhclrRoleCode);		//第六个过程的下一个节点的处理角色编码 (即第七个过程的当前处理角色编码)
		process6.setEpNextRoleName(Common.yhclrRoleName);	//第六个过程的下一个节点的处理角色名称 (即第七个过程的当前处理角色名称)
		process6.setEpNextPersonId(eventInfoVo.getEpNextPersonId());	//第六个过程的下一个节点的处理人id (即第七个过程的当前处理人id)
		process6.setEpNextPersonName(eventInfoVo.getEpNextPersonName());//第六个过程的下一个节点的处理人姓名 (即第七个过程的当前处理人姓名)
		       
		process6.setEpDealState(3);									//第六个过程的处理状态
		process6.setEpDealWay(eventInfoVo.getDataSource());			//第六个过程的处理端 (1电脑，2APP，3微信，4其它)
		process6.setEpAttachId(eventInfoVo.getEpAttachId());		//安保办安全员拍摄的附件图片ID(如果安保办安全员也上传提交附件的话)
		process6.setEpIsReturned(eventInfoVo.getEpIsReturned()); 	//是否将事件退回
		process6.setEpDealContent(eventInfoVo.getEpDealContent());	//安保办安全员给出的处理意见
		
		this.eventManagerDaoImpl.update(process6);		 /** 部门负责人	将事件派遣给	  处理人     后更新第六个事件过程   **/
		saveEventHandle(process6);		//新增经办事件
		
		
		EventProcess process7 = new EventProcess();
		process7.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
		       
		process7.setEpUpNode(process6.getEpNowNode());						//第七个过程的上一节点编码 (即第六个过程的当前节点编码)
		process7.setEpUpNodeArriveTime(process6.getEpNowNodeArriveTime());	//第七个过程的上一节点的到达时间 (即第六个过程当前节点的到达时间)
		process7.setEpUpRole(process6.getEpNowRole()); 						//第七个过程的上一节点的处理角色编码 (即第六个过程当前节点的处理角色编码)
		process7.setEpUpRoleName(process6.getEpNowRoleName()); 				//第七个过程的上一节点的处理角色名称 (即第六个过程当前节点的处理角色名称)
		process7.setEpUpPersonId(process6.getEpNowPersonId()); 				//第七个过程的上一节点的处理人id (即第六个过程当前节点的处理人id)
		process7.setEpUpPersonName(process6.getEpNowPersonName()); 			//第七个过程的上一节点的处理人姓名 (即第六个过程当前节点的处理人姓名)
		       
		process7.setEpNowNode(process6.getEpNextNode());			//第七个过程的当前节点编码 (即第六个过程的下一节点编码)
		process7.setEpNowRole(process6.getEpNextRole());			//第七个过程的当前节点的处理角色编码 (即第六个过程的下一个节点的处理角色编码)
		process7.setEpNowRoleName(process6.getEpNextRoleName());	//第七个过程的当前节点的处理角色名称 (即第六个过程的下一个节点的处理角色名称)
		process7.setEpNowPersonId(process6.getEpNextPersonId());	//第七个过程的当前节点的处理人id (即第六个过程的下一个节点的处理人id)
		process7.setEpNowPersonName(process6.getEpNextPersonName());//第七个过程的当前节点的处理人姓名 (即第六个过程的下一个节点的处理人姓名)
		       
		process7.setEpNowNodeArriveTime(new Date());				//第七个过程当前节点的到达时间
		process7.setEpDealState(1);									//第七个过程的处理状态
		process7.setEpLimitTime(process6.getEpLimitTime()); 		//处理时限
		
		this.eventManagerDaoImpl.save(process7);		 /** 部门负责人	将事件派遣给	  处理人   后产生的第七个事件过程   **/ 
		
		return eventInfoVo.getId();			//返回事件Id
	};
	
	/**
	 * 处理人	将事件解决后提交给	安保办安全员   核查		(7->8)
	 */
	@Override
	public String saveFeedBackEventToABBAQY(EventInfoVo eventInfoVo){
		EventProcess process7 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
		process7.setEpNowNodeLeavleTime(new Date());					//第七个过程的当前节点的离开时间
		process7.setEpNowPersonId(eventInfoVo.getEpNowPersonId());		//第七个过程的当前节点的处理人id
		process7.setEpNowPersonName(eventInfoVo.getEpNowPersonName());	//第七个过程的当前节点的处理人姓名
		       
		process7.setEpNextNode("8");								//第七个过程的下一节点编码 (即第八个过程的当前节点编码)
		process7.setEpNextRole(Common.abbaqyRoleCode);				//第七个过程的下一个节点的处理角色编码 (即第八个过程的当前处理角色编码)
		process7.setEpNextRoleName(Common.abbaqyRoleName);			//第七个过程的下一个节点的处理角色名称 (即第八个过程的当前处理角色名称)
		       
		process7.setEpDealState(3);									//第七个过程的处理状态
		process7.setEpDealWay(eventInfoVo.getDataSource());			//第七个过程的处理端 (1电脑，2APP，3微信，4其它)
		process7.setEpAttachId(eventInfoVo.getEpAttachId());		//处理人拍摄的附件图片ID(如果处理人也上传提交附件的话)
		process7.setEpIsReturned(eventInfoVo.getEpIsReturned()); 	//是否将事件退回
		process7.setEpDealContent(eventInfoVo.getEpDealContent());	//处理人给出的处理意见
		
		
		this.eventManagerDaoImpl.update(process7);		/* ** 处理人	将事件解决后提交给	安保办安全员   后更新第七个事件过程   ** */
		saveEventHandle(process7);		//新增经办事件
		
		
		EventProcess process8 = new EventProcess();
		process8.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
		       
		process8.setEpUpNode(process7.getEpNowNode());						//第八个过程的上一节点编码 (即第七个过程的当前节点编码)
		process8.setEpUpNodeArriveTime(process7.getEpNowNodeArriveTime());	//第八个过程的上一节点的到达时间 (即第七个过程当前节点的到达时间)
		process8.setEpUpRole(process7.getEpNowRole()); 						//第八个过程的上一节点的处理角色编码 (即第七个过程当前节点的处理角色编码)
		process8.setEpUpRoleName(process7.getEpNowRoleName()); 				//第八个过程的上一节点的处理角色名称 (即第七个过程当前节点的处理角色名称)
		process8.setEpUpPersonId(process7.getEpNowPersonId()); 				//第八个过程的上一节点的处理人id (即第七个过程当前节点的处理人id)
		process8.setEpUpPersonName(process7.getEpNowPersonName()); 			//第八个过程的上一节点的处理人姓名 (即第七个过程当前节点的处理人姓名)
		       
		process8.setEpNowNode(process7.getEpNextNode());			//第八个过程的当前节点编码 (即第七个过程的下一节点编码)
		process8.setEpNowRole(process7.getEpNextRole());			//第八个过程的当前节点的处理角色编码 (即第七个过程的下一个节点的处理角色编码)
		process8.setEpNowRoleName(process7.getEpNextRoleName());	//第八个过程的当前节点的处理角色名称 (即第七个过程的下一个节点的处理角色名称)
		                                                                
		process8.setEpNowNodeArriveTime(new Date());				//第八个过程当前节点的到达时间
		process8.setEpDealState(1);									//第八个过程的处理状态
		
		this.eventManagerDaoImpl.save(process8);		/* ** 处理人	将事件解决后提交给	安保办安全员   后产生的第八个事件过程   ** */
		
		return eventInfoVo.getId();			//返回事件Id
	};

	

	/**
	 * 安保办主任	对此事件向上级	分管领导	请示	(5->10)
	 */
	@Override
	public String saveGoUpstairsToFGLD(EventInfoVo eventInfoVo) {
			EventProcess process5 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
			process5.setEpNowNodeLeavleTime(new Date());					//第五个过程的当前节点的离开时间
			process5.setEpNowPersonId(eventInfoVo.getEpNowPersonId());		//第五个过程的当前节点的处理人id
			process5.setEpNowPersonName(eventInfoVo.getEpNowPersonName());	//第五个过程的当前节点的处理人姓名
			
			process5.setEpNextNode("10");						//第五个过程的下一节点编码 (即第十个过程的当前节点编码)
			process5.setEpNextRole(Common.fgldRoleCode);		//第五个过程的下一个节点的处理角色编码 (即第十个过程的当前处理角色编码)
			process5.setEpNextRoleName(Common.fgldRoleName);	//第五个过程的下一个节点的处理角色名称 (即第十个过程的当前处理角色名称)
		    /*#######################################################################################################################*/
			process5.setEpNextPersonId(eventInfoVo.getEpNextPersonId());	//第五个过程的下一个节点的处理人id (即第十个过程的当前处理人id)
			process5.setEpNextPersonName(eventInfoVo.getEpNextPersonName());//第五个过程的下一个节点的处理人姓名 (即第十个过程的当前处理人姓名)
		    /*#######################################################################################################################*/
			
			process5.setEpDealState(3);									//第五个过程的处理状态
			process5.setEpDealWay(eventInfoVo.getDataSource());			//第五个过程的处理端 (1电脑，2APP，3微信，4其它)
			process5.setEpAttachId(eventInfoVo.getEpAttachId());		//安保办主任拍摄的附件图片ID(如果安保办主任也上传提交附件的话)
			process5.setEpDealContent(eventInfoVo.getEpDealContent());	//安保办主任给出的处理意见
			
			this.eventManagerDaoImpl.update(process5);		/* ** 安保办主任	对此事件向上级	分管领导   后更新第八个事件过程   ** */
			saveEventHandle(process5);		//新增经办事件
			
			
			EventProcess process10 = new EventProcess();
			process10.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
			
			process10.setEpUpNode(process5.getEpNowNode());						//第十个过程的上一节点编码 (即第五个过程的当前节点编码)
			process10.setEpUpNodeArriveTime(process5.getEpNowNodeArriveTime());	//第十个过程的上一节点的到达时间 (即第五个过程当前节点的到达时间)
			process10.setEpUpRole(process5.getEpNowRole()); 						//第十个过程的上一节点的处理角色编码 (即第五个过程当前节点的处理角色编码)
			process10.setEpUpRoleName(process5.getEpNowRoleName()); 				//第十个过程的上一节点的处理角色名称 (即第五个过程当前节点的处理角色名称)
			process10.setEpUpPersonId(process5.getEpNowPersonId()); 				//第十个过程的上一节点的处理人id (即第五个过程当前节点的处理人id)
			process10.setEpUpPersonName(process5.getEpNowPersonName()); 			//第十个过程的上一节点的处理人姓名 (即第五个过程当前节点的处理人姓名)

			process10.setEpNowNode(process5.getEpNextNode());			//第十个过程的当前节点编码 (即第五个过程的下一节点编码)
			process10.setEpNowRole(process5.getEpNextRole());			//第十个过程的当前节点的处理角色编码 (即第五个过程的下一个节点的处理角色编码)
			process10.setEpNowRoleName(process5.getEpNextRoleName());	//第十个过程的当前节点的处理角色名称 (即第五个过程的下一个节点的处理角色名称)
		    /*#######################################################################################################################*/
		    process10.setEpNowPersonId(process5.getEpNextPersonId());	//第六个过程的当前节点的处理人id (即第五个过程的下一个节点的处理人id)
		    process10.setEpNowPersonName(process5.getEpNextPersonName());//第六个过程的当前节点的处理人姓名 (即第五个过程的下一个节点的处理人姓名)
		    /*#######################################################################################################################*/
			
			process10.setEpNowNodeArriveTime(new Date());				//第十个过程当前节点的到达时间
			process10.setEpDealState(1);									//第十个过程的处理状态
			
			this.eventManagerDaoImpl.save(process10);		/* ** 安保办主任	对此事件向上级	分管领导   后产生的第九个事件过程   ** */
			
			return eventInfoVo.getId();			//返回事件Id
	}

	/**
	 * 分管领导	对此事件向上级	常务副总经理		请示	(10->11)
	 */
	@Override
	public String saveGoUpstairsToCWFZJL(EventInfoVo eventInfoVo) {
			EventProcess process10 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
			process10.setEpNowNodeLeavleTime(new Date());					//第十个过程的当前节点的离开时间
			process10.setEpNowPersonId(eventInfoVo.getEpNowPersonId());		//第十个过程的当前节点的处理人id
			process10.setEpNowPersonName(eventInfoVo.getEpNowPersonName());	//第十个过程的当前节点的处理人姓名
			
			process10.setEpNextNode("11");							//第十个过程的下一节点编码 (即第十一个过程的当前节点编码)
			process10.setEpNextRole(Common.cwfzjlRoleCode);			//第十个过程的下一个节点的处理角色编码 (即第十一个过程的当前处理角色编码)
			process10.setEpNextRoleName(Common.cwfzjlRoleName);		//第十个过程的下一个节点的处理角色名称 (即第十一个过程的当前处理角色名称)
			
			process10.setEpDealState(3);									//第十个过程的处理状态
			process10.setEpDealWay(eventInfoVo.getDataSource());			//第十个过程的处理端 (1电脑，2APP，3微信，4其它)
			process10.setEpAttachId(eventInfoVo.getEpAttachId());			//部门安全员拍摄的附件图片ID(如果部门安全员也上传提交附件的话)
			process10.setEpDealContent(eventInfoVo.getEpDealContent());		//部门安全员给出的处理意见
			
			this.eventManagerDaoImpl.update(process10);		/* ** 分管领导	对此事件向上级	常务副总经理	后更新第十个事件过程   ** */
			saveEventHandle(process10);		//新增经办事件
			
			
			EventProcess process11 = new EventProcess();
			process11.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
			
			process11.setEpUpNode(process10.getEpNowNode());						//第十一个过程的上一节点编码 (即第十个过程的当前节点编码)
			process11.setEpUpNodeArriveTime(process10.getEpNowNodeArriveTime());	//第十一个过程的上一节点的到达时间 (即第十个过程当前节点的到达时间)
			process11.setEpUpRole(process10.getEpNowRole()); 						//第十一个过程的上一节点的处理角色编码 (即第十个过程当前节点的处理角色编码)
			process11.setEpUpRoleName(process10.getEpNowRoleName()); 				//第十一个过程的上一节点的处理角色名称 (即第十个过程当前节点的处理角色名称)
			process11.setEpUpPersonId(process10.getEpNowPersonId()); 				//第十一个过程的上一节点的处理人id (即第十个过程当前节点的处理人id)
			process11.setEpUpPersonName(process10.getEpNowPersonName()); 			//第十一个过程的上一节点的处理人姓名 (即第十个过程当前节点的处理人姓名)
			
			process11.setEpNowNode(process10.getEpNextNode());			//第十一个过程的当前节点编码 (即第十个过程的下一节点编码)
			process11.setEpNowRole(process10.getEpNextRole());			//第十一个过程的当前节点的处理角色编码 (即第十个过程的下一个节点的处理角色编码)
			process11.setEpNowRoleName(process10.getEpNextRoleName());	//第十一个过程的当前节点的处理角色名称 (即第十个过程的下一个节点的处理角色名称)
			
			process11.setEpNowNodeArriveTime(new Date());				//第十一个过程当前节点的到达时间
			process11.setEpDealState(1);								//第十一个过程的处理状态
			
			this.eventManagerDaoImpl.save(process11);		/* ** 分管领导	对此事件向上级	常务副总经理    后产生的第十一个事件过程   ** */
			
			return eventInfoVo.getId();			//返回事件Id
	}
	
	/**
	 * 常务副总经理		审批后将事件反馈给		分管领导	(11->10)
	 */
	@Override
	public String saveFeedBackToFGLD(EventInfoVo eventInfoVo) {
		EventProcess process = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
		process.setEpNowNodeLeavleTime(new Date());						//当前过程的当前节点的离开时间
		process.setEpNowPersonId(eventInfoVo.getEpNowPersonId());		//当前过程的当前节点的处理人id
		process.setEpNowPersonName(eventInfoVo.getEpNowPersonName());	//当前过程的当前节点的处理人姓名
		
		process.setEpNextNode(process.getEpUpNode());				//当前过程的下一节点编码 (因为是常务副总经理的反馈,当前过程的下一节点编码只会是当前过程的上一前节点编码)
		process.setEpNextRole(process.getEpUpRole());				//当前过程的下一个节点的处理角色编码 (因为是常务副总经理的反馈,当前过程的下一节点的处理角色编码只会是当前过程的上一前节点的处理角色编码)
		process.setEpNextRoleName(process.getEpUpRoleName());		//当前过程的下一个节点的处理角色名称 (因为是常务副总经理的反馈,当前过程的下一节点的处理角色名称只会是当前过程的上一前节点的处理角色名称)
		process.setEpNextPersonId(process.getEpUpPersonId());		//当前过程的下一个节点的处理人id (因为是常务副总经理的反馈,当前过程的下一节点的处理人id只会是当前过程的上一前节点的处理人id)
		process.setEpNextPersonName(process.getEpUpPersonName());	//当前过程的下一个节点的处理人姓名 (因为是常务副总经理的反馈,当前过程的下一节点的处理人姓名只会是当前过程的上一前节点的处理人姓名)
		
		process.setEpDealState(3);									//当前过程的处理状态
		process.setEpDealWay(eventInfoVo.getDataSource());			//当前过程的处理端 (1电脑，2APP，3微信，4其它)
		process.setEpAttachId(eventInfoVo.getEpAttachId());			//当前过程的附件图片ID
		process.setEpDealContent(eventInfoVo.getEpDealContent());	//常务副总经理的处理意见
		
		this.eventManagerDaoImpl.update(process);		/* ** 常务副总经理		审批后将事件反馈给		分管领导	更新当前过程   ** */
		saveEventHandle(process);		//新增经办事件
		
		
		EventProcess nextProcess = new EventProcess();
		nextProcess.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
		
		nextProcess.setEpUpNode(process.getEpNowNode());						//下一过程的上一节点编码 (即当前过程的当前节点编码)
		nextProcess.setEpUpNodeArriveTime(process.getEpNowNodeArriveTime());	//下一过程的上一节点的到达时间 (即当前过程当前节点的到达时间)
		nextProcess.setEpUpRole(process.getEpNowRole()); 						//下一过程的上一节点的处理角色编码 (即当前过程当前节点的处理角色编码)
		nextProcess.setEpUpRoleName(process.getEpNowRoleName()); 				//下一过程的上一节点的处理角色名称 (即当前过程当前节点的处理角色名称)
		nextProcess.setEpUpPersonId(process.getEpNowPersonId()); 				//下一过程的上一节点的处理人id (即当前过程当前节点的处理人id)
		nextProcess.setEpUpPersonName(process.getEpNowPersonName()); 			//下一过程的上一节点的处理人姓名 (即当前过程当前节点的处理人姓名)
		
		nextProcess.setEpNowNode(process.getEpNextNode());				//下一过程的当前节点编码 (即当前过程的下一节点编码)
		nextProcess.setEpNowRole(process.getEpNextRole());				//下一过程的当前节点的处理角色编码 (即当前过程的下一个节点的处理角色编码)
		nextProcess.setEpNowRoleName(process.getEpNextRoleName());		//下一过程的当前节点的处理角色名称 (即当前过程的下一个节点的处理角色名称)
		nextProcess.setEpNowPersonId(process.getEpNextPersonId()); 		//下一过程的当前节点的处理人id (即当前过程的下一个节点的处理人id)
		nextProcess.setEpNowPersonName(process.getEpNextPersonName());	//下一过程的当前节点的处理人姓名 (即当前过程的下一个节点的处理人姓名)
		
		nextProcess.setEpNowNodeArriveTime(new Date());				//下一过程当前节点的到达时间
		nextProcess.setEpDealState(1);								//下一过程的处理状态
		
		this.eventManagerDaoImpl.save(nextProcess);		/* ** 常务副总经理		审批后将事件反馈给		分管领导	产生的下一过程   ** */
		
		return eventInfoVo.getId();			//返回事件Id
	}

	/**
	 * 分管领导	审批后将事件反馈给		安保办主任	(10->5)
	 */
	@Override
	public String saveFeedBackToABBZR(EventInfoVo eventInfoVo) {
		EventProcess process10 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
		process10.setEpNowNodeLeavleTime(new Date());						//当前过程的当前节点的离开时间
		process10.setEpNowPersonId(eventInfoVo.getEpNowPersonId());			//当前过程的当前节点的处理人id
		process10.setEpNowPersonName(eventInfoVo.getEpNowPersonName());		//当前过程的当前节点的处理人姓名
		
		process10.setEpNextNode("5");								//当前过程的下一节点编码
		process10.setEpNextRole(Common.abbzrRoleCode);				//当前过程的下一个节点的处理角色编码 
		process10.setEpNextRoleName(Common.abbzrRoleName);			//当前过程的下一个节点的处理角色名称
		
		process10.setEpDealState(3);								//当前过程的处理状态
		process10.setEpDealWay(eventInfoVo.getDataSource());		//当前过程的处理端 (1电脑，2APP，3微信，4其它)
		process10.setEpAttachId(eventInfoVo.getEpAttachId());		//当前过程的附件图片ID
		process10.setEpDealContent(eventInfoVo.getEpDealContent());	//分管领导给出的处理意见
		
		this.eventManagerDaoImpl.update(process10);		/* ** 分管领导	审批后将事件反馈给		安保办主任	更新当前过程   ** */
		saveEventHandle(process10);		//新增经办事件
		
		
		EventProcess process5 = new EventProcess();
		process5.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
		
		process5.setEpUpNode(process10.getEpNowNode());							//下一过程的上一节点编码 (即当前过程的当前节点编码)
		process5.setEpUpNodeArriveTime(process10.getEpNowNodeArriveTime());		//下一过程的上一节点的到达时间 (即当前过程当前节点的到达时间)
		process5.setEpUpRole(process10.getEpNowRole()); 						//下一过程的上一节点的处理角色编码 (即当前过程当前节点的处理角色编码)
		process5.setEpUpRoleName(process10.getEpNowRoleName()); 				//下一过程的上一节点的处理角色名称 (即当前过程当前节点的处理角色名称)
		process5.setEpUpPersonId(process10.getEpNowPersonId()); 				//下一过程的上一节点的处理人id (即当前过程当前节点的处理人id)
		process5.setEpUpPersonName(process10.getEpNowPersonName()); 			//下一过程的上一节点的处理人姓名 (即当前过程当前节点的处理人姓名)
		
		process5.setEpNowNode(process10.getEpNextNode());				//下一过程的当前节点编码 (即当前过程的下一节点编码)
		process5.setEpNowRole(process10.getEpNextRole());				//下一过程的当前节点的处理角色编码 (即当前过程的下一个节点的处理角色编码)
		process5.setEpNowRoleName(process10.getEpNextRoleName());		//下一过程的当前节点的处理角色名称 (即当前过程的下一个节点的处理角色名称)
		
		process5.setEpNowNodeArriveTime(new Date());				//下一过程当前节点的到达时间
		process5.setEpDealState(1);									//下一过程的处理状态
		
		this.eventManagerDaoImpl.save(process5);		/* ** 分管领导	审批后将事件反馈给		安保办主任	产生的下一过程   ** */
		
		return eventInfoVo.getId();			//返回事件Id
	}

	
	
	/**
	 * 部门负责人	将事件退回给	安保办主任	(6->5)
	 */
	@Override
	public String saveReturnToABBZR(EventInfoVo eventInfoVo) {
		EventProcess process6 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
		process6.setEpNowNodeLeavleTime(new Date());						//当前过程的当前节点的离开时间
		process6.setEpNowPersonId(eventInfoVo.getEpNowPersonId());			//当前过程的当前节点的处理人id
		process6.setEpNowPersonName(eventInfoVo.getEpNowPersonName());		//当前过程的当前节点的处理人姓名
		
		process6.setEpNextNode("5");								//当前过程的下一节点编码
		process6.setEpNextRole(Common.abbzrRoleCode);				//当前过程的下一个节点的处理角色编码 
		process6.setEpNextRoleName(Common.abbzrRoleName);			//当前过程的下一个节点的处理角色名称
		
		process6.setEpDealState(3);									//当前过程的处理状态
		process6.setEpDealWay(eventInfoVo.getDataSource());			//当前过程的处理端 (1电脑，2APP，3微信，4其它)
		process6.setEpAttachId(eventInfoVo.getEpAttachId());		//当前过程的附件图片ID
		process6.setEpIsReturned(eventInfoVo.getEpIsReturned()); 	//是否将事件退回
		process6.setEpReturnReason(eventInfoVo.getEpReturnReason());//安保办安全员的退回理由
		
		this.eventManagerDaoImpl.update(process6);		 /* ** 部门负责人	将事件退回给	安保办主任	更新当前过程   ** */
		saveEventHandle(process6);		//新增经办事件
		
		
		EventProcess process5 = new EventProcess();
		process5.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
		
		process5.setEpUpNode(process6.getEpNowNode());						//下一过程的上一节点编码 (即当前过程的当前节点编码)
		process5.setEpUpNodeArriveTime(process6.getEpNowNodeArriveTime());	//下一过程的上一节点的到达时间 (即当前过程当前节点的到达时间)
		process5.setEpUpRole(process6.getEpNowRole()); 						//下一过程的上一节点的处理角色编码 (即当前过程当前节点的处理角色编码)
		process5.setEpUpRoleName(process6.getEpNowRoleName()); 				//下一过程的上一节点的处理角色名称 (即当前过程当前节点的处理角色名称)
		process5.setEpUpPersonId(process6.getEpNowPersonId()); 				//下一过程的上一节点的处理人id (即当前过程当前节点的处理人id)
		process5.setEpUpPersonName(process6.getEpNowPersonName()); 			//下一过程的上一节点的处理人姓名 (即当前过程当前节点的处理人姓名)
		
		process5.setEpNowNode(process6.getEpNextNode());				//下一过程的当前节点编码 (即当前过程的下一节点编码)
		process5.setEpNowRole(process6.getEpNextRole());				//下一过程的当前节点的处理角色编码 (即当前过程的下一个节点的处理角色编码)
		process5.setEpNowRoleName(process6.getEpNextRoleName());		//下一过程的当前节点的处理角色名称 (即当前过程的下一个节点的处理角色名称)
		
		process5.setEpNowNodeArriveTime(new Date());				//下一过程当前节点的到达时间
		process5.setEpDealState(1);									//下一过程的处理状态
		
		this.eventManagerDaoImpl.save(process5);		/* ** 部门负责人	将事件退回给	安保办主任	产生的下一过程   ** */
		
		return eventInfoVo.getId();			//返回事件Id
	}
	
	/**
	 * 处理人	将事件退回给		部门负责人	(7->6)
	 */
	@Override
	public String saveReturnToBMFZR(EventInfoVo eventInfoVo) {
		EventProcess process7 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
		process7.setEpNowNodeLeavleTime(new Date());						//当前过程的当前节点的离开时间
		process7.setEpNowPersonId(eventInfoVo.getEpNowPersonId());			//当前过程的当前节点的处理人id
		process7.setEpNowPersonName(eventInfoVo.getEpNowPersonName());		//当前过程的当前节点的处理人姓名
		
		process7.setEpNextNode("6");								//当前过程的下一节点编码
		process7.setEpNextRole(Common.bmfzrRoleCode);				//当前过程的下一个节点的处理角色编码 
		process7.setEpNextRoleName(Common.bmfzrRoleName);			//当前过程的下一个节点的处理角色名称
		
		process7.setEpDealState(3);									//当前过程的处理状态
		process7.setEpDealWay(eventInfoVo.getDataSource());			//当前过程的处理端 (1电脑，2APP，3微信，4其它)
		process7.setEpAttachId(eventInfoVo.getEpAttachId());		//当前过程的附件图片ID
		process7.setEpIsReturned(eventInfoVo.getEpIsReturned()); 	//是否将事件退回
		process7.setEpReturnReason(eventInfoVo.getEpReturnReason());//安保办安全员的退回理由
		
		this.eventManagerDaoImpl.update(process7);		 /* ** 处理人	将事件退回给	部门负责人	更新当前过程   ** */
		saveEventHandle(process7);		//新增经办事件
		
		
		EventProcess process6 = new EventProcess();
		process6.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
		
		process6.setEpUpNode(process7.getEpNowNode());						//下一过程的上一节点编码 (即当前过程的当前节点编码)
		process6.setEpUpNodeArriveTime(process7.getEpNowNodeArriveTime());	//下一过程的上一节点的到达时间 (即当前过程当前节点的到达时间)
		process6.setEpUpRole(process7.getEpNowRole()); 						//下一过程的上一节点的处理角色编码 (即当前过程当前节点的处理角色编码)
		process6.setEpUpRoleName(process7.getEpNowRoleName()); 				//下一过程的上一节点的处理角色名称 (即当前过程当前节点的处理角色名称)
		process6.setEpUpPersonId(process7.getEpNowPersonId()); 				//下一过程的上一节点的处理人id (即当前过程当前节点的处理人id)
		process6.setEpUpPersonName(process7.getEpNowPersonName()); 			//下一过程的上一节点的处理人姓名 (即当前过程当前节点的处理人姓名)
		
		process6.setEpNowNode(process7.getEpNextNode());				//下一过程的当前节点编码 (即当前过程的下一节点编码)
		process6.setEpNowRole(process7.getEpNextRole());				//下一过程的当前节点的处理角色编码 (即当前过程的下一个节点的处理角色编码)
		process6.setEpNowRoleName(process7.getEpNextRoleName());		//下一过程的当前节点的处理角色名称 (即当前过程的下一个节点的处理角色名称)
		
		process6.setEpNowNodeArriveTime(new Date());				//下一过程当前节点的到达时间
		process6.setEpDealState(1);									//下一过程的处理状态
		
		this.eventManagerDaoImpl.save(process6);		/* ** 处理人	将事件退回给	部门负责人	产生的下一过程   ** */
		
		return eventInfoVo.getId();			//返回事件Id
	}
	
	/**
	 * 安保办安全员	审核后觉得处理不到位将事件退回给	处理人		(8->7)
	 */
	@Override
	public String saveReturnToDealPeople(EventInfoVo eventInfoVo) {
		EventProcess process8 = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
		process8.setEpNowNodeLeavleTime(new Date());						//当前过程的当前节点的离开时间
		process8.setEpNowPersonId(eventInfoVo.getEpNowPersonId());			//当前过程的当前节点的处理人id
		process8.setEpNowPersonName(eventInfoVo.getEpNowPersonName());		//当前过程的当前节点的处理人姓名
		
		process8.setEpNextNode(process8.getEpUpNode());				//当前过程的下一节点编码
		process8.setEpNextRole(process8.getEpUpRole());				//当前过程的下一个节点的处理角色编码 
		process8.setEpNextRoleName(process8.getEpUpRoleName());		//当前过程的下一个节点的处理角色名称
		process8.setEpNextPersonId(process8.getEpUpPersonId());		//当前过程的下一个节点的处理人id
		process8.setEpNextPersonName(process8.getEpUpPersonName());	//当前过程的下一个节点的处理人姓名
		
		process8.setEpDealState(3);									//当前过程的处理状态
		process8.setEpDealWay(eventInfoVo.getDataSource());			//当前过程的处理端 (1电脑，2APP，3微信，4其它)
		process8.setEpAttachId(eventInfoVo.getEpAttachId());		//当前过程的附件图片ID
		process8.setEpWhetherFinish(eventInfoVo.getEpWhetherFinish()); 	//是否办结
		process8.setEpReturnReason(eventInfoVo.getEpReturnReason());//安保办安全员的退回理由
		
		this.eventManagerDaoImpl.update(process8);		 /* ** 安保办安全员	审核后觉得处理不到位将事件退回给	处理人	更新当前过程   ** */
		saveEventHandle(process8);		//新增经办事件
		
		
		EventProcess process7 = new EventProcess();
		process7.setEventId(eventInfoVo.getId());		//事件过程关联的事件id
		
		process7.setEpUpNode(process8.getEpNowNode());						//下一过程的上一节点编码 (即当前过程的当前节点编码)
		process7.setEpUpNodeArriveTime(process8.getEpNowNodeArriveTime());	//下一过程的上一节点的到达时间 (即当前过程当前节点的到达时间)
		process7.setEpUpRole(process8.getEpNowRole()); 						//下一过程的上一节点的处理角色编码 (即当前过程当前节点的处理角色编码)
		process7.setEpUpRoleName(process8.getEpNowRoleName()); 				//下一过程的上一节点的处理角色名称 (即当前过程当前节点的处理角色名称)
		process7.setEpUpPersonId(process8.getEpNowPersonId()); 				//下一过程的上一节点的处理人id (即当前过程当前节点的处理人id)
		process7.setEpUpPersonName(process8.getEpNowPersonName()); 			//下一过程的上一节点的处理人姓名 (即当前过程当前节点的处理人姓名)
		
		process7.setEpNowNode(process8.getEpNextNode());				//下一过程的当前节点编码 (即当前过程的下一节点编码)
		process7.setEpNowRole(process8.getEpNextRole());				//下一过程的当前节点的处理角色编码 (即当前过程的下一个节点的处理角色编码)
		process7.setEpNowRoleName(process8.getEpNextRoleName());		//下一过程的当前节点的处理角色名称 (即当前过程的下一个节点的处理角色名称)
		
		process7.setEpNowNodeArriveTime(new Date());				//下一过程当前节点的到达时间
		process7.setEpDealState(1);									//下一过程的处理状态
		
		this.eventManagerDaoImpl.save(process7);		/* ** 安保办安全员	审核后觉得处理不到位将事件退回给	处理人	产生的下一过程   ** */
		
		return eventInfoVo.getId();			//返回事件Id
	}
	
	
	/**
	 * 两种情况: 1、各部门安全员、各部门负责人、安保办安全员、安保办主任 	认为该事件无需处理(达不到隐患级别或已处理完毕) , 事件办结		( 2、3、4、5 -> 9)
	 * 		  2、事件处理经处理人处理后,	安保办安全员	      对事件核查无误后  ,  事件办结	( 8 -> 9)
	 */
	@Override
	public String saveConfirmEventFinish(EventInfoVo eventInfoVo) {
			EventProcess process = this.eventManagerDaoImpl.getEntityById(EventProcess.class, eventInfoVo.getEpId());
			process.setEpNowNodeLeavleTime(new Date());							//当前过程的当前节点的离开时间
			process.setEpNowPersonId(eventInfoVo.getEpNowPersonId());			//当前过程的当前节点的处理人id
			process.setEpNowPersonName(eventInfoVo.getEpNowPersonName());		//当前过程的当前节点的处理人姓名
			       
			process.setEpNextNode("9");									  //当前过程的下一节点编码 (即第七过程的当前节点编码)
			//节点九: 办结节点,不存在处理人
			       
			process.setEpDealState(3);									  //当前过程的处理状态
			process.setEpDealWay(eventInfoVo.getDataSource());			  //当前过程的处理端 (1电脑，2APP，3微信，4其它)
			process.setEpAttachId(eventInfoVo.getEpAttachId());			  //事件上报人核查后拍摄的附件图片ID(如果事件上报人核查后也上传提交附件的话)
			process.setEpWhetherFinish(eventInfoVo.getEpWhetherFinish()); //事件是否办结
			process.setEpDealContent(eventInfoVo.getEpDealContent());	  //事件上报人核查确认完结后给出的评价
			process.setEpAppraise(eventInfoVo.getEpAppraise()); 		  //事件上报人核查后给出的满意度
			
			this.eventManagerDaoImpl.update(process);
			
			
			EventProcess nextProcess = new EventProcess();
			nextProcess.setEventId(eventInfoVo.getId());				//事件过程关联的事件id
			       
			nextProcess.setEpUpNode(process.getEpNowNode());						//下一过程的上一节点编码 (即当前过程的当前节点编码)
			nextProcess.setEpUpNodeArriveTime(process.getEpNowNodeArriveTime());	//下一过程的上一节点的到达时间 (即当前过程的当前节点的到达时间)
			nextProcess.setEpUpRole(process.getEpNowRole()); 						//下一过程的上一节点的处理角色编码 (即当前过程的当前节点的处理角色编码)
			nextProcess.setEpUpRoleName(process.getEpNowRoleName()); 				//下一过程的上一节点的处理角色名称 (即当前过程的当前节点的处理角色名称)
			nextProcess.setEpUpPersonId(process.getEpNowPersonId()); 				//下一过程的上一节点的处理人id (即当前过程的当前节点的处理人id)
			nextProcess.setEpUpPersonName(process.getEpNowPersonName()); 			//下一过程的上一节点的处理人姓名 (即当前过程的当前节点的处理人姓名)
			       
			nextProcess.setEpNowNode(process.getEpNextNode());				//下一过程的当前节点编码 (即第六个过程的下一节点编码)
			//节点九: 办结节点,不存在处理人
			                                                                
			nextProcess.setEpNowNodeArriveTime(new Date());					//下一过程当前节点的到达时间
			nextProcess.setEpDealState(3);									//下一过程的处理状态
			nextProcess.setEpDealWay(eventInfoVo.getDataSource());			//下一过程的处理端 (1电脑，2APP，3微信，4其它)
			
			this.eventManagerDaoImpl.save(nextProcess);	
			//事件办结后,将经办事件表(Event_handle)中关联的事件过程全部删除掉
			deleteEventHandleByEId(nextProcess.getEventId());
			
			return eventInfoVo.getId();			//返回事件Id
	}
	
	
	/**
	 * 新增经办事件
	 */
	@Override
	public void saveEventHandle(EventProcess eventProcess) {
		EventHandle eventHandle = new EventHandle();
		eventHandle.setEventId(eventProcess.getEventId());				//事件id
		eventHandle.setEpNowNode(eventProcess.getEpNowNode());			//当前节点编码
		eventHandle.setEpNowNodeArriveTime(eventProcess.getEpNowNodeArriveTime());		//当前过程的当前节点到达时间
		eventHandle.setEpNowNodeLeavleTime(eventProcess.getEpNowNodeLeavleTime());		//当前过程的当前节点离开时间
		eventHandle.setEpNowPersonId(eventProcess.getEpNowPersonId());					//当前过程的当前处理人id
		eventHandle.setEpNowPersonName(eventProcess.getEpNowPersonName());				//当前过程的当前处理人姓名
		eventHandle.setEpNowRole(eventProcess.getEpNowRole());							//当前过程的当前处理角色编码
		eventHandle.setEpNowRoleName(eventProcess.getEpNowRoleName());					//当前过程的当前处理角色名称
		
		EventInfo eventInfo = this.eventManagerDaoImpl.getEntityById(EventInfo.class, eventHandle.getEventId());	//因为本方法接受到的eventInfoVo内容不完整,所以得再从数据库获取一次
		eventHandle.setEventReportTime(eventInfo.getCreateTime());		//事件上报时间
		eventHandle.setEventCode(eventInfo.getEventCode());				//事件编码
		eventHandle.setEventTitle(eventInfo.getEventTitle());			//事件标题
		eventHandle.setEventurgency(eventInfo.getEventurgency());		//紧急程度
		eventHandle.setEventType(eventInfo.getEventType());				//事件类型
		eventHandle.setEventReportName(eventInfo.getCreatorName()); 	//事件上报人姓名
		
		//如果是第一次网格员审查未合格(事件未办结),则指挥中心第二次派遣(或相关部门第二次提交)时,则应在经办事件表中将第一次派遣(或提交)的经办事件删除掉再新增新的经办事件
		List<EventHandle> eventHandleList = this.getEventHandleByIdAndNode(eventHandle.getEventId(), eventHandle.getEpNowPersonId(), eventHandle.getEpNowNode());
		if(eventHandleList != null && eventHandleList.size() > 0){
			for (EventHandle eh : eventHandleList) {
				this.eventManagerDaoImpl.delete(eh);
			}
		}
		this.eventManagerDaoImpl.save(eventHandle);
	}
	
	/**
	 * 根据事件id,当前处理人,当前节点编码  获取 经办事件表中记录
	 */
	@Override
	public List<EventHandle> getEventHandleByIdAndNode(String eventId, String epNowPersonId, String epNowNode) {
		List<Object> objectList = new ArrayList<Object>();
		objectList.add(eventId);
		objectList.add(epNowPersonId);
		objectList.add(epNowNode);
		String hql = "from EventHandle where eventId = ? and epNowPersonId = ? and epNowNode = ? order by createTime desc,epNowNode desc";
		List<EventHandle> eventHandleList = this.eventManagerDaoImpl.queryEntityHQLList(hql,objectList,EventHandle.class);
		return eventHandleList;
	}
	
	/**
	 * 根据事件id 获取 经办事件表(Event_Handle)中关联的事件过程
	 */
	@Override
	public List<EventHandle> getEventHandleByEId(String eventId) {
		List<Object> objectList = new ArrayList<Object>();
		objectList.add(eventId);
		String hql = "from EventHandle where eventId = ? order by createTime desc,epNowNode desc";
		
		List<EventHandle> eventHandleList = this.eventManagerDaoImpl.queryEntityHQLList(hql,objectList,EventHandle.class);
		return eventHandleList;
	}

	/**
	 * 根据事件id 删除 经办事件表(Event_Handle)中关联的事件过程
	 */
	@Override
	public void deleteEventHandleByEId(String eventId) {
		List<EventHandle> eventHandleList = this.getEventHandleByEId(eventId);
		if(eventHandleList != null){
			for (EventHandle eh : eventHandleList) {
				this.eventManagerDaoImpl.delete(eh);
			}
		}
	}

	
	
	@Override
	public List<EventProcess> getEProcessByEId(String eventId) {
		List<Object> objectList = new ArrayList<Object>();
		objectList.add(eventId);
		String hql = "from EventProcess where eventId = ? order by createTime desc,epNowNode desc";
		
		List<EventProcess> eventProcesslist = this.eventManagerDaoImpl.queryEntityHQLList(hql,objectList,EventProcess.class);
		return eventProcesslist;
	}

	@Override
	public List<EventProcessVo> getEProcessVoByEId(String eventId) {
		List<Object> objectList = new ArrayList<Object>();
		objectList.add(eventId);
		String hql = "from EventProcess where eventId = ? order by createTime desc,epNowNode desc";
		
		List<EventProcess> eventProcesses = this.eventManagerDaoImpl.queryEntityHQLList(hql,objectList,EventProcess.class);
		List<EventProcessVo> result = new ArrayList<>();
		if(eventProcesses != null && eventProcesses.size()>0){
			for(EventProcess eventProcess : eventProcesses){
				EventProcessVo vo = new EventProcessVo();
				List<Attach> attaches = this.attachServiceImpl.queryAttchListByFormId(eventProcess.getId());
				BeanUtils.copyProperties(eventProcess,vo);
				if(attaches != null && attaches.size()>0){
					List<String> imgUrls = new ArrayList<>();
					for(Attach attach : attaches){
						if(attach.getSuffix().equals("mp4")){
							vo.setVideoUrl(attach.getPathUpload());
						}else {
							imgUrls.add(attach.getPathUpload());
						}
					}
					vo.setImgUrls(imgUrls);
				}
				result.add(vo);
			}
		}
		return result;
	}

	@Override
	public EventProcess getEProcessByEIdAndEpNowNode(String eventId, String epNowNode) {
		List<Object> attributeList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select ep.ep_UpNode,ep.ep_UpNodeArriveTime,ep.ep_UpRole,ep.ep_UpRoleName,ep.ep_UpPersonId,ep.ep_UpPersonName,"
				+ "ep.ep_NowNode,ep.ep_NowNodeArriveTime,ep.ep_NowNodeLeavleTime,ep.ep_NowRole,ep.ep_NowRoleName,ep.ep_NowPersonId,ep.ep_NowPersonName,"
				+ "ep.ep_NextNode,ep.ep_NextNodeLeavleTime,ep.ep_NextRole,ep.ep_NextRoleName,ep.ep_NextPersonId,ep.ep_NextPersonName,"
				+ "ep.ep_AttachId,ep.ep_DealState,ep.ep_LimitTime,ep.ep_DealWay,ep.ep_DealContent,ep.ep_Appraise,ep.ep_IsSite,ep.ep_IsReturned,ep.ep_ReturnReason,"
				+ "ep.id  from event_process ep where 1=1 ");
		
		if(StringUtils.isNotBlank(eventId)){
			sql.append(" and ep.EventInfo_ID = ? ");
			attributeList.add(eventId.trim());
		}
		if(StringUtils.isNotBlank(epNowNode)){
			sql.append(" and ep.Ep_NowNode = ? ");
			attributeList.add(epNowNode.trim());
		}
		sql.append(" order by ep.create_Time desc");
		
		List<Object> list = this.eventManagerDaoImpl.queryEntitySQLList(sql.toString(), attributeList);
		ArrayList<EventProcess> eventProcessList = new ArrayList<EventProcess>();
		for(int i = 0; i < list.size(); i++){
			Object[] obj = (Object[])list.get(i);
			EventProcess eventProcess = new EventProcess();
			if(obj[0]!=null) eventProcess.setEpUpNode(obj[0].toString());
			if(obj[1]!=null) eventProcess.setEpUpNodeArriveTime(DateUtil.getDateFromString(obj[1].toString()));
			if(obj[2]!=null) eventProcess.setEpUpRole(obj[2].toString());
			if(obj[3]!=null) eventProcess.setEpUpRoleName(obj[3].toString());
			if(obj[4]!=null) eventProcess.setEpUpPersonId(obj[4].toString());
			if(obj[5]!=null) eventProcess.setEpUpPersonName(obj[5].toString());
			
			if(obj[6]!=null) eventProcess.setEpNowNode(obj[6].toString());
			if(obj[7]!=null) eventProcess.setEpNowNodeArriveTime(DateUtil.getDateFromString(obj[7].toString()));
			if(obj[8]!=null) eventProcess.setEpNowNodeLeavleTime(DateUtil.getDateFromString(obj[8].toString()));
			if(obj[9]!=null) eventProcess.setEpNowRole(obj[9].toString());
			if(obj[10]!=null) eventProcess.setEpNowRoleName(obj[10].toString());
			if(obj[11]!=null) eventProcess.setEpNowPersonId(obj[11].toString());
			if(obj[12]!=null) eventProcess.setEpNowPersonName(obj[12].toString());
			
			if(obj[13]!=null) eventProcess.setEpNextNode(obj[13].toString());
			if(obj[14]!=null) eventProcess.setEpNextNodeLeavleTime(DateUtil.getDateFromString(obj[14].toString()));
			if(obj[15]!=null) eventProcess.setEpNextRole(obj[15].toString());
			if(obj[16]!=null) eventProcess.setEpNextRoleName(obj[16].toString());
			if(obj[17]!=null) eventProcess.setEpNextPersonId(obj[17].toString());
			if(obj[18]!=null) eventProcess.setEpNextPersonName(obj[18].toString());
			
			if(obj[19]!=null) eventProcess.setEpAttachId(obj[19].toString());
			if(obj[20]!=null) eventProcess.setEpDealState(Integer.parseInt(obj[20].toString()));
			if(obj[21]!=null) eventProcess.setEpLimitTime(DateUtil.getDateFromString(obj[21].toString()));
			if(obj[22]!=null) eventProcess.setEpDealWay(Integer.parseInt(obj[22].toString()));
			if(obj[23]!=null) eventProcess.setEpDealContent(obj[23].toString());
			if(obj[24]!=null) eventProcess.setEpAppraise(Integer.parseInt(obj[24].toString()));
			
			if(obj[25]!=null) eventProcess.setEpIsSite(Integer.parseInt(obj[25].toString()));
			if(obj[26]!=null) eventProcess.setEpIsReturned(Integer.parseInt(obj[26].toString()));
			if(obj[27]!=null) eventProcess.setEpReturnReason(obj[27].toString());
			
			if(obj[28]!=null) eventProcess.setId(obj[28].toString());
			
			eventProcessList.add(eventProcess);
		}
		
		return eventProcessList.get(0);		//因为是根据创建事件倒序排列,所以即使是同一事件且当前节点相同的多个事件过程,获取最新的事件过程就只要取第一条事件过程即可
	}

	
	/**
	 * 根据隐患处理人角色编码、处理人姓名	查询	用户列表
	 */
	@Override
	public List<User> getUserListByRoleCode(String roleCode, String departmentId, String userName) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(roleCode)){
			params.add(Restrictions.sqlRestriction("this_.id in (select user_id from um_user_role where role_id in ("
					+ "select id from um_role where role_code = '" + roleCode + "'))"));
		}
		if(StringUtils.isNotBlank(departmentId)){
			params.add(Restrictions.sqlRestriction("this_.ORGFRAME_ID = '" + departmentId + "'"));
		}
		if(StringUtils.isNotBlank(userName)){
			params.add(Restrictions.like("userName", "%"+userName+"%"));
		}
		
		return this.eventManagerDaoImpl.queryEntityList(params, Order.desc("createTime"), User.class);
	}
}
