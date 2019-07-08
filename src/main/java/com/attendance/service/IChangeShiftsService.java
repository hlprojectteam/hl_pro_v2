package com.attendance.service;

import java.util.List;

import com.attendance.module.ChangeShifts;
import com.attendance.vo.ChangeShiftsVo;
import com.attendance.vo.ChangeShiftsVo;
import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;

/**
 * 
 * @Description 调班
 * @author qinyongqian
 * @date 2019年5月20日
 *
 */
public interface IChangeShiftsService extends IBaseService {

	public Pager queryEntityList(Integer page, Integer rows,ChangeShifts changeShiftsVo);
	
	public void deleteEntitys(String ids);
	
	public void saveOrUpdate(ChangeShifts changeShifts);
	
	public List<ChangeShifts> queryChangeShifts(ChangeShiftsVo changeShiftsVo);
	
	/**
	 * 
	 * @方法：@param page
	 * @方法：@param rows
	 * @方法：@param changeShiftsVo
	 * @方法：@return
	 * @描述：联合查询
	 * @return
	 * @author: qinyongqian
	 * @date:2019年7月5日
	 */
	public Pager queryChangeShiftsApproval(Integer page, Integer rows, ChangeShiftsVo changeShiftsVo);
	
	/**
	 * 
	 * @方法：@param UserId
	 * @方法：@return
	 * @描述：查询某人，当月内有多少换班申请
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月27日
	 */
	public Integer queryCountInMonth(String UserId);
}
