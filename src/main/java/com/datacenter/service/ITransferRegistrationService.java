package com.datacenter.service;

import java.util.Date;
import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.TransferRegistration;
import com.datacenter.vo.TransferRegistrationVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @Description 交接班登记表 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface ITransferRegistrationService extends IBaseService{

	/**
	 * 交接班登记表	分页
	 * @param page
	 * @param rows
	 * @param transferRegistrationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, TransferRegistrationVo transferRegistrationVo);
	
	/**
	 * 交接班登记表	保存or更新
	 * @param transferRegistrationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public TransferRegistration saveOrUpdate(TransferRegistrationVo transferRegistrationVo);

	/**
	 * 交接班登记表	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 交接班登记表	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);

	/**
	 * 交接班登记表	list
	 * @param transferRegistrationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public List<TransferRegistration> queryEntityList(TransferRegistrationVo transferRegistrationVo);

	/**
	 * 交接班登记表	导出Excel
	 * @param transferRegistrationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook export(TransferRegistrationVo transferRegistrationVo);
		
}