package com.attendance.service;

import java.util.List;

import com.attendance.module.AttendanceApproval;
import com.attendance.vo.AttendanceApprovalVo;
import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年5月20日
 *
 */
public interface IApprovalService extends IBaseService {

	public Pager queryEntityList(Integer page, Integer rows,AttendanceApprovalVo attendanceApprovalVo);
	
	public void deleteEntitys(String ids);
	
	public void saveOrUpdate(AttendanceApproval attendanceApproval);
	
	public List<AttendanceApproval> queryAttendanceApproval(AttendanceApprovalVo attendanceApprovalVo);
}
