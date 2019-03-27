package com.safecheck.hiddenDanger.service;

import com.common.base.service.IBaseService;
import com.safecheck.hiddenDanger.vo.EventInfoVo;


/**
 * @Description 隐患事件统计	service接口
 * @author xuezb
 * @Date 2019年1月21日
 */
public interface IEventCountService extends IBaseService{

	/**
	 * 隐患事件统计——我的上报
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2019年1月21日
	 */
	int queryReportCount(EventInfoVo eventInfoVo);
	
	/**
	 * 隐患事件统计——我的待办
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2019年1月21日
	 */
	int queryAgendaCount(EventInfoVo eventInfoVo);
	
	/**
	 * 隐患事件统计——我的经办
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2019年1月21日
	 */
	int queryHandleCount(EventInfoVo eventInfoVo);
	
	/**
	 * 隐患事件统计——我的办结
	 * @param eventInfoVo
	 * @return
	 * @author xuezb
	 * @Date 2019年1月21日
	 */
	int queryFinishCount(EventInfoVo eventInfoVo);
	
}
