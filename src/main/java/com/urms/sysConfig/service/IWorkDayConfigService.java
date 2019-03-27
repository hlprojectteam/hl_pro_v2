package com.urms.sysConfig.service;


import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.sysConfig.module.WorkDayConfig;
import com.urms.sysConfig.vo.WorkDayConfigVo;

public interface IWorkDayConfigService extends IBaseService{
	
	/**
	 * 说明：保存or更新
	 * 输入：@param subworkDaytem
	 * 输出：void
	 * 创建时间:2016-1-5 下午5:03:07
	 */
	public void saveOrUpdate(WorkDayConfig workDayConfig);
	
	/**
	 * 说明：分页
	 * 输入：@param page
	 * 输入：@param rows
	 * 输入：@param subworkDaytemVo
	 * 输入：@return
	 * 输出：Pager
	 * 创建时间:2016-1-5 下午4:46:02
	 */
	public Pager queryEntityList(int page,int rows,WorkDayConfigVo workDayConfigVo);
	
	/**
	 * @intruduction 
	 * @param workDayConfigVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年5月3日下午2:19:18
	 */
	public List<WorkDayConfig> queryWorkDayConfigList(WorkDayConfigVo workDayConfigVo);
	
	public void getWorkDayConfig();
}
