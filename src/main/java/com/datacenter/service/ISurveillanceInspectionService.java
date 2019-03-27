package com.datacenter.service;

import java.util.Date;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.SurveillanceInspection;
import com.datacenter.vo.SurveillanceInspectionVo;

/**
 * @Description 监控巡检 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface ISurveillanceInspectionService extends IBaseService{
	
	/**
	 * 监控巡检	分页
	 * @param page
	 * @param rows
	 * @param surveillanceInspectionVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, SurveillanceInspectionVo surveillanceInspectionVo);
	
	/**
	 * 监控巡检	保存or更新
	 * @param surveillanceInspectionVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public SurveillanceInspection saveOrUpdate(SurveillanceInspectionVo surveillanceInspectionVo);

	/**
	 * 监控巡检	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 监控巡检	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);
		
}