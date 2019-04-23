package com.datacenter.service;

import java.util.Date;
import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.InfoThrough;
import com.datacenter.vo.InfoThroughVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @Description 信息通传 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface IInfoThroughService extends IBaseService{
	
	/**
	 * 信息通传	分页
	 * @param page
	 * @param rows
	 * @param infoThroughVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, InfoThroughVo infoThroughVo);
	
	/**
	 * 信息通传	保存or更新
	 * @param infoThroughVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public InfoThrough saveOrUpdate(InfoThroughVo infoThroughVo);

	/**
	 * 信息通传	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 信息通传	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);

	/**
	 * 信息通传	list
	 * @param infoThroughVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public List<InfoThrough> queryEntityList(InfoThroughVo infoThroughVo);

	/**
	 * 信息通传	导出Excel
	 * @param infoThroughVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook export(InfoThroughVo infoThroughVo);
}