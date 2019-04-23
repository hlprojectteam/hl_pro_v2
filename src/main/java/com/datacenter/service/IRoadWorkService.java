package com.datacenter.service;

import java.util.Date;
import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.RoadWork;
import com.datacenter.vo.RoadWorkVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @Description 涉路施工 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface IRoadWorkService extends IBaseService{
	
	/**
	 * 涉路施工	分页
	 * @param page
	 * @param rows
	 * @param roadWorkVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, RoadWorkVo roadWorkVo);
	
	/**
	 * 涉路施工	保存or更新
	 * @param roadWorkVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public RoadWork saveOrUpdate(RoadWorkVo roadWorkVo);

	/**
	 * 涉路施工	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 涉路施工	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);

	/**
	 * 涉路施工	list
	 * @param roadWorkVo
	 * @return
	 * @author xuezb
	 * @Date 2019年4月2日
	 */
	public List<RoadWork> queryEntityList(RoadWorkVo roadWorkVo);

	/**
	 * 涉路施工	导出Excel
	 * @param roadWorkVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook export(RoadWorkVo roadWorkVo);

		
}