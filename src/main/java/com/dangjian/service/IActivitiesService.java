package com.dangjian.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.dangjian.module.Activities;
import com.dangjian.module.ActivitiesLaunch;
import com.dangjian.vo.ActivitiesLaunchVo;
import com.dangjian.vo.ActivitiesVo;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2018年12月29日
 *
 */
public interface IActivitiesService extends IBaseService{
	
	/**********以下是 党建活动************/
	public Pager queryEntityList(Integer page, Integer rows,ActivitiesVo activitiesVo);
	
	public List<Activities> queryALLEntityList();
	
	public void deleteEntitys(String ids);
	
	public void saveOrUpdate(Activities activities);
	
	
	
	/**********以下是 党建活动开展情况************/
    public List<ActivitiesLaunchVo> queryALEntityList(ActivitiesLaunchVo activitiesLaunchVo);
    
    /**
	 * 
	 * @方法：@param page
	 * @方法：@param rows
	 * @方法：@param activitiesLaunchVo
	 * @方法：@return
	 * @描述：查询活动开展列表，通过时间段范围，如查询2019年的一月份的活动
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月22日
	 */
	public List<ActivitiesLaunchVo> queryALEntityListPagerByTimeQuantum(ActivitiesLaunchVo activitiesLaunchVo);
	
	public void deleteALEntitys(String ids);
	
	public void saveOrUpdateAL(ActivitiesLaunch activitiesLaunch);
	
	public Pager queryALEntityListPager(Integer page, Integer rows,ActivitiesLaunchVo activitiesLaunchVo);
	
	public JSONArray collectAL(ActivitiesLaunchVo activitiesLaunchVo,List<Activities> atList);
	
	/**
	 * 
	 * @方法：@param year
	 * @方法：@return
	 * @描述：积分排名
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月22日
	 */
	public List<ActivitiesLaunchVo> queryActivityRanking(String year);
	

}
