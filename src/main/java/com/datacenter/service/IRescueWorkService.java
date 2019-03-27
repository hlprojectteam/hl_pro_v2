package com.datacenter.service;

import java.util.Date;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.RescueWork;
import com.datacenter.vo.RescueWorkVo;

/**
 * @Description 拯救作业 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface IRescueWorkService extends IBaseService{
	
	/**
	 * 拯救作业	分页
	 * @param page
	 * @param rows
	 * @param rescueWorkVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, RescueWorkVo rescueWorkVo);
	
	/**
	 * 拯救作业	保存or更新
	 * @param rescueWorkVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public RescueWork saveOrUpdate(RescueWorkVo rescueWorkVo);

	/**
	 * 拯救作业	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 拯救作业	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);
		
}