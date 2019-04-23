package com.datacenter.service;

import java.util.Date;
import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.EquipmentOperation;
import com.datacenter.vo.EquipmentOperationVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @Description 设备运行情况 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface IEquipmentOperationService extends IBaseService{
	
	/**
	 * 设备运行情况	分页
	 * @param page
	 * @param rows
	 * @param equipmentOperationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, EquipmentOperationVo equipmentOperationVo);
	
	/**
	 * 设备运行情况	保存or更新
	 * @param equipmentOperationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public EquipmentOperation saveOrUpdate(EquipmentOperationVo equipmentOperationVo);

	/**
	 * 设备运行情况	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 设备运行情况	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);

	/**
	 * 设备运行情况	list
	 * @param equipmentOperationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年4月3日
	 */
	public List<EquipmentOperation> queryEntityList(EquipmentOperationVo equipmentOperationVo);

	/**
	 * 设备运行情况	导出Excel
	 * @param equipmentOperationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook export(EquipmentOperationVo equipmentOperationVo);

}

