package com.datacenter.service;

import java.util.Date;
import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.FeedBack;
import com.datacenter.vo.FeedBackVo;

/**
 * @Description 顾客意见反馈 service接口
 * @author xuezb
 * @date 2019年2月19日
 */
public interface IFeedBackService extends IBaseService{
	
	/**
	 * 顾客意见反馈	分页
	 * @param page
	 * @param rows
	 * @param feedBackVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public Pager queryEntityList(Integer page, Integer rows, FeedBackVo feedBackVo);
	
	/**
	 * 顾客意见反馈	保存or更新
	 * @param feedBackVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public FeedBack saveOrUpdate(FeedBackVo feedBackVo);

	/**
	 * 顾客意见反馈	删除
	 * @param ttId	主表(TotalTable)Id
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int deleteByTtId(String ttId);

	/**
	 * 顾客意见反馈	日期(dutyDate)修改
	 * @param ttId		主表(TotalTable)Id
	 * @param dutyDate	主表(TotalTable)dutyDate
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	public int updateDutyDate(String ttId, Date dutyDate);

	/**
	 * 顾客意见反馈	list
	 * @param feedBackVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public List<FeedBack> queryEntityList(FeedBackVo feedBackVo);
	
}