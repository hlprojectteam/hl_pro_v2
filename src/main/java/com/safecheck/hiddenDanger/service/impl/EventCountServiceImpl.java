package com.safecheck.hiddenDanger.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.Common;
import com.safecheck.hiddenDanger.dao.IEventManagerDao;
import com.safecheck.hiddenDanger.service.IEventCountService;
import com.safecheck.hiddenDanger.vo.EventInfoVo;
import com.urms.role.module.Role;
import com.urms.user.module.User;

/**
 * @Description 隐患事件统计	service实现类
 * @author xuezb
 * @Date 2019年1月21日
 */
@Repository("eventCountServiceImpl")
public class EventCountServiceImpl extends BaseServiceImpl implements IEventCountService{
	
	@Autowired
	private IEventManagerDao eventManagerDaoImpl;
	

	@Override
	public int queryReportCount(EventInfoVo eventInfoVo) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select count(*) from (select * from "
					+ "(select ei.id,ei.EVENT_CODE,ei.EVENT_TITLE,ei.EVENT_URGENCY,ei.EVENT_TYPE,ei.CREATOR_NAME,ei.create_Time,ep.Ep_NowNode,ep.Ep_NowRoleName,ep.Ep_DealState "
					+ "from EVENT_INFO ei,EVENT_PROCESS ep "
					+ "where ei.id = ep.EventInfo_ID ");
		
		if(StringUtils.isNotBlank(eventInfoVo.getEpNowPersonId())){		//当前用户id
			sql.append(" and ei.Creator_id = ? ");						//事件上报人id
			paramList.add(eventInfoVo.getEpNowPersonId());
		}
		
		sql.append(" ORDER BY ep.create_Time DESC,ep.Ep_NowNode DESC) t "
				+ "GROUP BY t.id ORDER BY t.create_Time desc) c");
		
		List<Object> list = this.eventManagerDaoImpl.queryEntitySQLList(sql.toString(), paramList);
		int count = Integer.parseInt(list.get(0).toString());
		return count;
	}

	@Override
	public int queryAgendaCount(EventInfoVo eventInfoVo) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		// (上报人、部门安全员、部门负责人、安保办安全员、处理人)待办事件:	事件的当前节点的处理人 为 当前用户,若事件没有指定处理人,则 当前用户角色 包含 当前节点的处理角色 即可;且事件的处理状态为未处理(1)或处理中(2)
		// 若是各个部门的安全员	获取待办事件时，还需要根据	事件上报人所在部门Id	进行筛选
		sql.append("select count(*) from (select tt.id,tt.EVENT_CODE,tt.EVENT_TITLE,tt.EVENT_URGENCY,tt.EVENT_TYPE,tt.CREATOR_NAME,tt.create_Time,tt.reporter_OrgId,tt.Ep_NowNode,tt.Ep_NowRoleName,tt.Ep_DealState,tt.epId,tt.Ep_NowNodeArriveTime,tt.Ep_UpNode,tt.Ep_limitTime "
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
			sql.append(" or tt.Ep_NowRole = '" + Common.yhsbrRoleCode +"')");	/* **注:上报人对用户无角色要求,event_yhsbr只是为了方便角色展示. ** */
		}
		
		
		// 若是各个部门的    安全员 或 负责人     获取待办事件时，还需要根据	事件上报人所在部门Id	进行筛选
		for (Role role : roles) {
			if(StringUtils.isNotBlank(role.getRoleCode()) && (role.getRoleCode().equals(Common.bmaqyRoleCode) || role.getRoleCode().equals(Common.bmfzrRoleCode))){
				if(user.getOrgFrame() != null){
					sql.append(" and (tt.reporter_OrgId = ? or tt.Ep_NowNode = '6' or tt.Ep_NowNode = '7')");		//各个部门的    安全员 或 负责人      根据  事件上报人所在部门Id 获取其同一部门员工所上报的事件	(如果当前节点是6,则可不安这一规则)
					paramList.add(user.getOrgFrame().getId());
				}
			}
		}
		
		if(eventInfoVo.getEventurgency() != null){		// 若事件有指定处理人,以用户是处理人为主
			sql.append(" and tt.EVENT_URGENCY = ? ");
			paramList.add(eventInfoVo.getEventurgency());
		}
				
		sql.append(" and tt.Ep_DealState in (1,2) ");
		sql.append(" order by tt.Ep_NowNodeArriveTime desc) c");		//待办事件列表按当前节点到达时间降序排列
		
		List<Object> list = this.eventManagerDaoImpl.queryEntitySQLList(sql.toString(), paramList);
		int count = Integer.parseInt(list.get(0).toString());
		return count;
	}

	@Override
	public int queryHandleCount(EventInfoVo eventInfoVo) {
		List<Object> attributeList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		//经办事件: 经办事件表中数据
		//经办事件表,事件过程表的关联查询
		if(StringUtils.isNotBlank(eventInfoVo.getEpNowPersonId())){		//当前用户id
			sql.append("select count(*)"
						//+ "	from (select * from event_handle where Ep_NowPersonId = ? ) eh "
						+ " from (select * from (select * from event_handle ee where ee.Ep_NowPersonId = ? ORDER BY ee.Ep_NowNodeArriveTime DESC) hh  GROUP BY hh.EventInfo_ID) eh "
						+ "left join (select * from (select ep.EventInfo_ID,ep.Ep_NowNode,ep.Ep_NowRoleName,ep.Ep_DealState,ep.create_Time epCreate_Time"
													+ " from event_process ep where ep.EventInfo_ID not in (select EventInfo_ID from event_process where Ep_NowNode = '9')"
													+ " ORDER BY ep.CREATE_TIME DESC,ep.Ep_NowNode DESC) t "
									+ "group by t.EventInfo_ID) ep"
						+ " on eh.EventInfo_ID = ep.EventInfo_ID ");
			
			attributeList.add(eventInfoVo.getEpNowPersonId());
		}
		
		List<Object> list = this.eventManagerDaoImpl.queryEntitySQLList(sql.toString(), attributeList);
		int count = Integer.parseInt(list.get(0).toString());
		return count;
	}

	@Override
	public int queryFinishCount(EventInfoVo eventInfoVo) {
		List<Object> attributeList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		//办结事件 : 当前用户为事件处理人之一	且	事件状态为处理完(3)
		//事件表,事件过程表的关联查询
		if(StringUtils.isNotBlank(eventInfoVo.getEpNowPersonId())){		//当前用户id
			sql.append("select count(*) from ("
							+ "select * from ("
										+ "select ei.id,ei.EVENT_CODE,ei.EVENT_TITLE,ei.EVENT_URGENCY,ei.EVENT_TYPE,ei.CREATOR_NAME,ei.create_Time,"
										+ "ep.Ep_NowNode,ep.Ep_NowPersonId,ep.Ep_NowRole,ep.Ep_DealState,ep.create_Time epCreate_Time,ep.Ep_NowNodeArriveTime  from event_info ei,event_process ep "
										+ " where ei.id = ep.EventInfo_ID and ei.id in "
										+ " (select EventInfo_ID from event_process where Ep_NowPersonId = ? ) "			//	Ep_NowPersonId = ?	判断登录用户是否为某个事件过程的的处理人,即可得知该用户是否为事件处理人之一
										+ " ORDER BY ep.create_Time desc,ep.Ep_NowNode desc "
							+ ") t GROUP BY t.id "
					+ ") tt where tt.Ep_DealState = 3 ");
			
			attributeList.add(eventInfoVo.getEpNowPersonId());
		}
			
		List<Object> list = this.eventManagerDaoImpl.queryEntitySQLList(sql.toString(), attributeList);
		int count = Integer.parseInt(list.get(0).toString());
		return count;
	}

}
