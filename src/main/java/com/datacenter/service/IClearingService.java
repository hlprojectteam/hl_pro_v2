package com.datacenter.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.Clearing;
import com.datacenter.vo.ClearingVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Date;
import java.util.List;

/**
 * @Description 清障保洁 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface IClearingService extends IBaseService{
	
	/**
	 * 清障保洁	分页
	 * @param page
	 * @param rows
	 * @param clearingVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, ClearingVo clearingVo);
	
	/**
	 * 清障保洁	保存or更新
	 * @param clearingVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Clearing saveOrUpdate(ClearingVo clearingVo);

	/**
	 * 清障保洁	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 清障保洁	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);

	/**
	 * 清障保洁	list
	 * @param clearingVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public List<Clearing> queryEntityList(ClearingVo clearingVo);

	/**
	 * 清障保洁	导出Excel
	 * @param clearingVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook export(ClearingVo clearingVo);

}
