package com.urms.sysConfig.service;


import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.sysConfig.module.SysConfig;
import com.urms.sysConfig.vo.SysConfigVo;

public interface ISysConfigService extends IBaseService{
	
	/**
	 * 说明：保存or更新
	 * 输入：@param subsystem
	 * 输出：void
	 * 创建时间:2016-1-5 下午5:03:07
	 */
	public void saveOrUpdate(SysConfig sysConfig);
	
	/**
	 * 说明：分页
	 * 输入：@param page
	 * 输入：@param rows
	 * 输入：@param subsystemVo
	 * 输入：@return
	 * 输出：Pager
	 * 创建时间:2016-1-5 下午4:46:02
	 */
	public Pager queryEntityList(int page,int rows,SysConfigVo sysConfigVo);
	
	/**
	 * @intruduction 
	 * @param sysConfigVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年5月3日下午2:19:18
	 */
	public List<SysConfig> querySysConfigList(SysConfigVo sysConfigVo);
	
	public void getSysConfig();
	
	/**
	 * 
	 * @方法：@param key
	 * @方法：@return
	 * @描述：根据KEY获取Value
	 * @return
	 * @author: qinyongqian
	 * @date:2019年6月30日
	 */
	public String getConfigValueByKey(String key);
}
