package com.dangjian.service;

import java.util.List;

import net.sf.json.JSONArray;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.dangjian.module.Activities;
import com.dangjian.module.ActivitiesLaunch;
import com.dangjian.module.ActivitiesLaunchReview;
import com.dangjian.vo.ActivitiesLaunchReviewVo;
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
	
	public Pager queryALByStatusPager(Integer page, Integer rows,Integer[] Status,String userId);
	
	/**
	 * 
	 * @方法：@param activitiesLaunchVo
	 * @方法：@param atList
	 * @方法：@return
	 * @描述：活动汇总表
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月31日
	 */
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
	
	
	/**********以下是 党建活动评审************/
	/**
	 * 
	 * @方法：@param activitiesLaunchReview
	 * @描述：保存评审意见
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月29日
	 */
	public void saveOrUpdateALR(ActivitiesLaunchReview activitiesLaunchReview);
	
	/**
	 * 
	 * @方法：@param activitiesLaunchReviewVo
	 * @方法：@return
	 * @描述：查询评审记录-不分页
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月30日
	 */
	public List<ActivitiesLaunchReview> queryALREntityList(ActivitiesLaunchReviewVo activitiesLaunchReviewVo);
	
	/**
	 * 
	 * @方法：@param page
	 * @方法：@param rows
	 * @方法：@param activitiesLaunchReviewVo
	 * @方法：@return
	 * @描述：查询评审记录分页
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月30日
	 */
	public Pager queryALREntityListPager(Integer page, Integer rows,ActivitiesLaunchReviewVo activitiesLaunchReviewVo);
	
	/**
	 * 
	 * @方法：@param activitiesLaunchReviewVo
	 * @方法：@return
	 * @描述：所有党委是否评审
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月29日
	 */
	public boolean isAllCheck(ActivitiesLaunchReviewVo activitiesLaunchReviewVo);
	
	/**
	 * 
	 * @方法：@param activitiesLaunchId
	 * @描述：根据活动开展ID，删除评审记录
	 * @return
	 * @author: qinyongqian
	 * @date:2019年4月8日
	 */
	public void deleteALRByFormAlId(String activitiesLaunchId);
	
	
	

}
