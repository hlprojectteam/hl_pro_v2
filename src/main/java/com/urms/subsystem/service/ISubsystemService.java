package com.urms.subsystem.service;



import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.subsystem.module.Subsystem;
import com.urms.subsystem.vo.SubsystemVo;
import com.urms.user.module.User;

public interface ISubsystemService extends IBaseService{
	
	/**
	 * 说明：保存or更新
	 * 输入：@param subsystem
	 * 输出：void
	 * 创建时间:2016-1-5 下午5:03:07
	 */
	public void saveOrUpdate(Subsystem subsystem);
	
	/**
	 * 说明：分页
	 * 输入：@param page
	 * 输入：@param rows
	 * 输入：@param subsystemVo
	 * 输入：@return
	 * 输出：Pager
	 * 创建时间:2016-1-5 下午4:46:02
	 */
	public Pager queryEntityList(int page,int rows,SubsystemVo subsystemVo);
	
	/**
	 * 说明：获得集合
	 * 输入：@param subsystemVo
	 * 输入：@return
	 * 输出：List<Subsystem>
	 * 创建时间:2016-1-5 下午4:57:54
	 */
	public List<Subsystem> getSubsystemList(SubsystemVo subsystemVo,User user);

	public List<Subsystem> getSubsystemList(SubsystemVo subsystemVo);
	/**
	 * @intruduction 初始化加载子系统
	 * @author Mr.Wang
	 * @Date 2016年1月13日下午2:22:26
	 */
	public void getSubsystem();
}
