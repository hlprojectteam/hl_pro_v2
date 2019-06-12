package com.attendance.service.Impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance.dao.ILeaveDao;
import com.attendance.module.Leave;
import com.attendance.ql.AttendanceQl;
import com.attendance.service.ILeaveService;
import com.attendance.vo.LeaveVo;
import com.common.base.service.impl.BaseServiceImpl;
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
		// TODO Auto-generated method stub
		return null;
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

	

}
