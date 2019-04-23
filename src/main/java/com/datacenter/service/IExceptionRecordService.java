package com.datacenter.service;

import java.util.Date;
import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.ExceptionRecord;
import com.datacenter.vo.ExceptionRecordVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @Description 营运异常记录 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface IExceptionRecordService extends IBaseService{
	
	/**
	 * 营运异常记录	分页
	 * @param page
	 * @param rows
	 * @param exceptionRecordVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, ExceptionRecordVo exceptionRecordVo);
	
	/**
	 * 营运异常记录	保存or更新
	 * @param exceptionRecordVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public ExceptionRecord saveOrUpdate(ExceptionRecordVo exceptionRecordVo);

	/**
	 * 营运异常记录	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 营运异常记录	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);

	/**
	 * 营运异常记录	list
	 * @param exceptionRecordVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public List<ExceptionRecord> queryEntityList(ExceptionRecordVo exceptionRecordVo);

	/**
	 * 营运异常记录	导出Excel
	 * @param exceptionRecordVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook export(ExceptionRecordVo exceptionRecordVo);
	
}