package com.datacenter.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.FieldOperations;
import com.datacenter.vo.FieldOperationsVo;

import java.util.Date;
import java.util.List;

/**
 * @Description 外勤作业 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface IFieldOperationsService extends IBaseService{
	
	/**
	 * 外勤作业	分页
	 * @param page
	 * @param rows
	 * @param fieldOperationsVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, FieldOperationsVo fieldOperationsVo);
	
	/**
	 * 外勤作业	保存or更新
	 * @param fieldOperationsVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public FieldOperations saveOrUpdate(FieldOperationsVo fieldOperationsVo);

	/**
	 * 外勤作业	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 外勤作业	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);

	/**
	 * 外勤作业	list
	 * @param fieldOperationsVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public List<FieldOperations> queryEntityList(FieldOperationsVo fieldOperationsVo);
	
}