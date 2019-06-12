package com.attendance.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance.dao.IApprovalDao;
import com.attendance.module.AttendanceApproval;
import com.attendance.service.IApprovalService;
import com.attendance.vo.AttendanceApprovalVo;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年5月20日
 *
 */
@Service("approvalServiceImpl")
public class ApprovalServiceImpl extends BaseServiceImpl implements IApprovalService{

	@Autowired
	private IApprovalDao approvalDaoImpl;
	@Override
	public Pager queryEntityList(Integer page, Integer rows,
			AttendanceApprovalVo attendanceApprovalVo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEntitys(String ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveOrUpdate(AttendanceApproval attendanceApproval) {
		if(StringUtils.isBlank(attendanceApproval.getId())){
			this.save(attendanceApproval);
		}else{
			this.update(attendanceApproval);
		}
	}

	@Override
	public List<AttendanceApproval> queryAttendanceApproval(
			AttendanceApprovalVo attendanceApprovalVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(attendanceApprovalVo.getApprovalRecordId())){
			criterionsList.add(Restrictions.eq("approvalRecordId", attendanceApprovalVo.getApprovalRecordId()));
		}
		return this.approvalDaoImpl.queryEntityList(criterionsList, Order.desc("createTime"), AttendanceApproval.class);
	}

	
	

}
