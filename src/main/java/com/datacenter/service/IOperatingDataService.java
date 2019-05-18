package com.datacenter.service;

import java.util.Date;
import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.OperatingData;
import com.datacenter.vo.OperatingDataVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @Description 营运数据 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface IOperatingDataService extends IBaseService{
	
	/**
	 * 营运数据	分页
	 * @param page
	 * @param rows
	 * @param operatingDataVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, OperatingDataVo operatingDataVo);
	
	/**
	 * 营运数据	保存or更新
	 * @param operatingDataVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public OperatingData saveOrUpdate(OperatingDataVo operatingDataVo);

	/**
	 * 营运数据	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 营运数据	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);

	/**
	 * 营运数据	list
	 * @param operatingDataVo
	 * @return
	 * @author xuezb
	 * @Date 2019年4月3日
	 */
	public List<OperatingData> queryEntityList(OperatingDataVo operatingDataVo);

	/**
	 * 营运数据	导出Excel
	 * @param operatingDataVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook export(OperatingDataVo operatingDataVo);
	
	/**
	 * 
	 * @方法：@param dutyDate 录入日期
	 * @方法：@param tollGateId 收费站ID
	 * @方法：@return
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月14日
	 */
	public boolean isRecordExist(Date dutyDate,String tollGateId);


}