package com.datacenter.service;

import java.util.Date;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.Brief;
import com.datacenter.vo.BriefVo;

/**
 * @Description 工作简报 service接口
 * @author xuezb
 * @date 2019年2月18日
 */
public interface IBriefService extends IBaseService{
	
	/**
	 * 工作简报	分页
	 * @param page
	 * @param rows
	 * @param briefVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月18日
	 */
	public Pager queryEntityList(Integer page, Integer rows, BriefVo briefVo);
	
	/**
	 * 工作简报	保存or更新
	 * @param briefVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月18日
	 */
	public Brief saveOrUpdate(BriefVo briefVo);
	
	/**
	 * 工作简报	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);
	
	/**
	 * 工作简报	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);

}
