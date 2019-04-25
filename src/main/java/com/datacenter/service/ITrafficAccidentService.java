package com.datacenter.service;

import java.util.Date;
import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.TrafficAccident;
import com.datacenter.vo.TrafficAccidentVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @Description 交通事故 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface ITrafficAccidentService extends IBaseService{
	
	/**
	 * 交通事故	分页
	 * @param page
	 * @param rows
	 * @param trafficAccidentVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, TrafficAccidentVo trafficAccidentVo);
	
	/**
	 * 交通事故	保存or更新
	 * @param trafficAccidentVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public TrafficAccident saveOrUpdate(TrafficAccidentVo trafficAccidentVo);

	/**
	 * 交通事故	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 交通事故	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);

	/**
	 * 交通事故	list
	 * @param trafficAccidentVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public List<TrafficAccident> queryEntityList(TrafficAccidentVo trafficAccidentVo);

	/**
	 * 交通事故	导出Excel
	 * @param trafficAccidentVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook export(TrafficAccidentVo trafficAccidentVo);
		
}