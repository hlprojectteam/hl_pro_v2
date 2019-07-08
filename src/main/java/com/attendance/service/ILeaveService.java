package com.attendance.service;

import java.util.List;

import com.attendance.module.Leave;
import com.attendance.vo.LeaveVo;
import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年5月20日
 *
 */
public interface ILeaveService extends IBaseService {

	/**
	 * 
	 * @方法：@param page
	 * @方法：@param rows
	 * @方法：@param leaveVo
	 * @方法：@return
	 * @描述：单表分页查询
	 * @return
	 * @author: qinyongqian
	 * @date:2019年7月5日
	 */
	public Pager queryEntityList(Integer page, Integer rows,LeaveVo leaveVo);
	
	public void deleteEntitys(String ids);
	
	public void saveOrUpdate(Leave leave);
	
	public List<Leave> queryLeave(LeaveVo leaveVo);
	
	/**
	 * 
	 * @方法：@param page
	 * @方法：@param rows
	 * @方法：@param leaveVo
	 * @方法：@return
	 * @描述：多表分页查询
	 * @return
	 * @author: qinyongqian
	 * @date:2019年7月5日
	 */
	public Pager queryLeaveApproval(Integer page, Integer rows, LeaveVo leaveVo);
	
	/**
	 * 
	 * @方法：@param UserId
	 * @方法：@return
	 * @描述：查询某人，当月内有多少请假申请
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月27日
	 */
	public Integer queryCountInMonth(String UserId);
}
