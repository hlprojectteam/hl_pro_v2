package com.attendance.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance.dao.ILeaveDao;
import com.attendance.module.Leave;
import com.attendance.ql.AttendanceQl;
import com.attendance.service.ILeaveService;
import com.attendance.vo.LeaveVo;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年5月20日
 *
 */
@Service("leaveServiceImpl")
public class LeaveServiceImpl extends BaseServiceImpl implements ILeaveService{

	@Autowired
	public ILeaveDao leaveDaoImpl;
	@Override
	public Pager queryEntityList(Integer page, Integer rows, LeaveVo leaveVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(leaveVo.getCreatorId())){
			criterionsList.add(Restrictions.eq("creatorId",  leaveVo.getCreatorId()));
		}
		if(leaveVo.getApprovalStatus()!=null){
			criterionsList.add(Restrictions.eq("approvalStatus",  leaveVo.getApprovalStatus()));
		}
		return this.leaveDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime"), Leave.class);
	}

	@Override
	public void deleteEntitys(String ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(Leave leave) {
		if(StringUtils.isBlank(leave.getId())){
			this.save(leave);
		}else{
			this.update(leave);
		}
	}

	@Override
	public List<Leave> queryLeave(LeaveVo leaveVo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer queryCountInMonth(String UserId) {
		String sql="";
		sql = AttendanceQl.MySql.leaveMonthNum;
		sql = sql.replace("?", UserId);
		if(StringUtils.isNotEmpty(sql)){
			List<Object> list= leaveDaoImpl.queryBySql(sql);
			if(list!=null){
				return list.size();
			}
		}
		return 0;
	}

	@Override
	public Pager queryLeaveApproval(Integer page, Integer rows, LeaveVo leaveVo) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(AttendanceQl.MySql.leaveApprovalList);
		if(StringUtils.isNotBlank(leaveVo.getId())){
			sql.append(" AND a.ID = ? ");		
			paramList.add(leaveVo.getId());
		}
		if(StringUtils.isNotBlank(leaveVo.getCreatorId())){
			sql.append(" and a.CREATOR_ID = ? ");		
			paramList.add(leaveVo.getCreatorId());
		}
		if(leaveVo.getApprovalStatus()!=null){
			sql.append(" and a.APPROVAL_STATUS = ? ");		
			paramList.add(leaveVo.getApprovalStatus());
		}
		if(StringUtils.isNotBlank(leaveVo.getApprovalUserId())){
			sql.append(" and b.APPROVAL_USER_ID = ? ");		
			paramList.add(leaveVo.getApprovalUserId());
		}
		sql.append(" ORDER BY a.CREATE_TIME DESC");
	    Pager pager= leaveDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
	    if(pager!=null){
	    	List<LeaveVo> list = new ArrayList<LeaveVo>();
	    	for (int i = 0; i < pager.getPageList().size(); i++) {
	    		Object[] obj = (Object[])pager.getPageList().get(i);
	    		LeaveVo lVo = new LeaveVo();
	    		if(obj[0]!=null) lVo.setId(obj[0].toString());
	    		if(obj[1]!=null) lVo.setCreateTime(DateUtil.getDateFromString(obj[1].toString()));
	    		if(obj[2]!=null) lVo.setApprovalNumber(obj[2].toString());
	    		if(obj[3]!=null) lVo.setCreatorName(obj[3].toString());
	    		if(obj[4]!=null) lVo.setApprovalStatus(Integer.parseInt(obj[4].toString()));
	    		if(obj[5]!=null) lVo.setStartTime(DateUtil.getDateFromString(obj[5].toString()));
	    		if(obj[6]!=null) lVo.setEndTime(DateUtil.getDateFromString(obj[6].toString()));
	    		list.add(lVo);
	    	}
	    	pager.setPageList(list);
	    }
	    return pager;
	}

	

}
