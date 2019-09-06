package com.dangjian.service.Impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.attach.service.IAttachService;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.dangjian.dao.IActivitesDao;
import com.dangjian.module.Activities;
import com.dangjian.module.ActivitiesLaunch;
import com.dangjian.module.ActivitiesLaunchReview;
import com.dangjian.ql.DangjianQl;
import com.dangjian.service.IActivitiesService;
import com.dangjian.vo.ActivitiesLaunchReviewVo;
import com.dangjian.vo.ActivitiesLaunchVo;
import com.dangjian.vo.ActivitiesVo;
import com.urms.dataDictionary.service.IDataDictionaryService;
import com.urms.role.service.IRoleService;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2018年12月29日
 *
 */
@Service("activitiesServiceImpl")
public class ActivitiesServiceImpl extends BaseServiceImpl implements IActivitiesService{

	@Autowired
	public IActivitesDao activitesDaoImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	public IRoleService roleServiceImpl;
	@Autowired
	public IDataDictionaryService dataDictionaryServiceImpl;
	@Override
	public Pager queryEntityList(Integer page, Integer rows,
			ActivitiesVo activitiesVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(activitiesVo.getTitle())){
			criterionsList.add(Restrictions.like("title", "%"+ activitiesVo.getTitle()+"%"));
		}
		return this.activitesDaoImpl.queryEntityList(page, rows, criterionsList, Order.asc("order"), Activities.class);
	}
	@Override
	public List<Activities> queryALLEntityList() {
		// TODO Auto-generated method stub
		return activitesDaoImpl.queryAllEntity(Activities.class, Order.asc("order"));
	}
	@Transactional
	@Override
	public void deleteEntitys(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			this.delete(Activities.class, idz[i]);
		}
		for (int j = 0; j < idz.length; j++) {
			//删除附件
			this.attachServiceImpl.deleteAttachByFormId(idz[j]);
		}
	}
	@Override
	public void saveOrUpdate(Activities activities) {
		if(StringUtils.isBlank(activities.getId())){
			this.save(activities);
		}else{
			this.update(activities);
		}
		
	}
	@Override
	public List<ActivitiesLaunchVo> queryALEntityList(ActivitiesLaunchVo activitiesLaunchVo) {
		List<ActivitiesLaunchVo> listVo=new ArrayList<>();
		if(activitiesLaunchVo.getActivityId()==null||activitiesLaunchVo.getBranchId()==null)
			return listVo;
		Activities ac=activitesDaoImpl.getEntityById(Activities.class, activitiesLaunchVo.getActivityId());
		String sql="";
		if(ac.getFrequency()==2){
			//按月统计
			sql = DangjianQl.MySql.activitiesMonthNum;
		}else if(ac.getFrequency()==3){
			//按季度统计
			sql = DangjianQl.MySql.activitiesQuarterNum;
		}else if(ac.getFrequency()==4||ac.getFrequency()==5){
			//按半年统计 或 一个季度或半年
			sql = DangjianQl.MySql.activitiesHalfYearNum;
		}else if(ac.getFrequency()==6){
			//按一年统计
			sql = DangjianQl.MySql.activitiesYearNum;
		}else if(ac.getFrequency()==7){
			//按无限制统计，也是按一年内
			sql = DangjianQl.MySql.activitiesYearNum;
		}
		sql = sql.replace("?", activitiesLaunchVo.getActivityId());//活动ID
		sql = sql.replace("@@", activitiesLaunchVo.getBranchId());//支部ID
		sql = sql.replace("##", activitiesLaunchVo.getYear());//活动年份
		if(StringUtils.isNotEmpty(sql)){
			List<Object> listAL=activitesDaoImpl.queryBySql(sql);
			if(listAL!=null){
				for (int j = 0; j < listAL.size(); j++) {
					Object[] obj = (Object[])listAL.get(j);
					ActivitiesLaunchVo vo=new ActivitiesLaunchVo();
					vo.setTitle(ac.getTitle());
					vo.setFrequency(ac.getFrequency());
					if(obj[2]!=null){
						vo.setCompletionTimes(Integer.parseInt(obj[2].toString()));
						if(Integer.parseInt(obj[2].toString())>0){
							vo.setIsReach(1);
						}
					}else{
						vo.setCompletionTimes(0);
						vo.setIsReach(0);
						if(ac.getFrequency()==7){
							//若是无限制的活动，则次数为0也是达标的
							vo.setIsReach(1);
						}
					}
					if(obj[3]!=null) vo.setTimeQuantum(obj[3].toString());
					if(obj[4]!=null) vo.setTimeQuantumValue(obj[4].toString());
					
					listVo.add(vo);
				}
			}
		}
		return listVo;
	}
	@Override
	public List<ActivitiesLaunchVo> queryALEntityListPagerByTimeQuantum(ActivitiesLaunchVo activitiesLaunchVo) {
		List<ActivitiesLaunchVo> listVo=new ArrayList<>();
		if(activitiesLaunchVo.getActivityId()==null||activitiesLaunchVo.getBranchId()==null)
			return listVo;
		String sql="";
		if(activitiesLaunchVo.getFrequency()==2){
			//按月查询
			sql = DangjianQl.MySql.activitiesMonthList;
		}else if(activitiesLaunchVo.getFrequency()==3){
			//按季度查询
			sql = DangjianQl.MySql.activitiesQuarterList;
		}else if(activitiesLaunchVo.getFrequency()==4||activitiesLaunchVo.getFrequency()==5){
			//按半年查询 或 一个季度或半年
			sql = DangjianQl.MySql.activitiesHalfYearList;
		}else if(activitiesLaunchVo.getFrequency()==6){
			//按一年查询
			sql = DangjianQl.MySql.activitiesYearList;
		}else if(activitiesLaunchVo.getFrequency()==7){
			//按无限制查询，也是按一年内
			sql = DangjianQl.MySql.activitiesYearList;
		}
		sql = sql.replace("?", activitiesLaunchVo.getActivityId());//活动ID
		sql = sql.replace("@@", activitiesLaunchVo.getBranchId());//支部ID
		sql = sql.replace("##", activitiesLaunchVo.getYear());//活动年份
		sql = sql.replace("%%", activitiesLaunchVo.getTimeQuantumValue());//活动时间范围值
		if(StringUtils.isNotEmpty(sql)){
			List<Object> listAL=activitesDaoImpl.queryBySql(sql);
			if(listAL!=null){
				for (int j = 0; j < listAL.size(); j++) {
					Object[] obj = (Object[])listAL.get(j);
					ActivitiesLaunchVo vo=new ActivitiesLaunchVo();
					if(obj[0]!=null){
						vo.setTitle(obj[0].toString());
					}
					if(obj[1]!=null){
						try {
							vo.setLaunchDate(DateUtil.getDateFromString(obj[1].toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if(obj[2]!=null){
						vo.setCreatorName(obj[2].toString());
					}
					if(obj[3]!=null){
						vo.setId(obj[3].toString());
					}
					listVo.add(vo);
				}
			}
		}
		return listVo;
	}
	@Transactional
	@Override
	public void deleteALEntitys(String ids) {
		// TODO Auto-generated method stub
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			//判断是否亮点工作，如果是，则要删除它的评审记录
			ActivitiesLaunch al=this.getEntityById(ActivitiesLaunch.class, idz[i]);
			if(al!=null){
				Activities ac=this.getEntityById(Activities.class, al.getActivityId());
				if(ac!=null){
					//如果是亮点工作
					if(ac.getFrequency()==7){
						this.deleteALRByFormAlId(al.getId());
					}
				}
			}
			this.delete(ActivitiesLaunch.class, idz[i]);
		}
		for (int i = 0; i < idz.length; i++) {
			//删除附件
			this.attachServiceImpl.deleteAttachByFormId(idz[i]);
		}
		
	}
	@Override
	public void saveOrUpdateAL(ActivitiesLaunch activitiesLaunch) {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(activitiesLaunch.getId())){
			this.save(activitiesLaunch);
		}else{
			this.update(activitiesLaunch);
		}
		
	}
	@Override
	public Pager queryALEntityListPager(Integer page, Integer rows,
			ActivitiesLaunchVo activitiesLaunchVo) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT t.ID,t.LAUNCH_DATE,t.BRANCH_ID,t.ACTIVITY_ID,t.CREATOR_NAME,t.LAUNCH_POINTS,t.LAUNCH_STATUS,t.LAUNCH_CONTENT FROM `p_dj_activities_launch` t where 1=1 ");
		if(StringUtils.isNotBlank(activitiesLaunchVo.getBranchId())){
			sql.append(" and t.BRANCH_ID = ? ");		
			paramList.add(activitiesLaunchVo.getBranchId());
		}
		if(StringUtils.isNotBlank(activitiesLaunchVo.getActivityId())){
			sql.append(" and t.ACTIVITY_ID = ? ");		
			paramList.add(activitiesLaunchVo.getActivityId());
		}
		if(StringUtils.isNotBlank(activitiesLaunchVo.getCreatorId())){
			sql.append(" and t.CREATOR_ID = ? ");		
			paramList.add(activitiesLaunchVo.getCreatorId());
		}
		if(activitiesLaunchVo.getStatus()!=null){
			sql.append(" and t.LAUNCH_STATUS = ? ");		
			paramList.add(activitiesLaunchVo.getStatus());
		}
		if(StringUtils.isNotBlank(activitiesLaunchVo.getYear())){
			sql.append(" and DATE_FORMAT(t.LAUNCH_DATE, '%Y') = ? ");		
			paramList.add(activitiesLaunchVo.getYear());
		}
		if(StringUtils.isNotBlank(activitiesLaunchVo.getMonth())){
			sql.append(" and date_format(t.LAUNCH_DATE, '%m')= ? ");		
			paramList.add(activitiesLaunchVo.getMonth());
		}
		sql.append(" ORDER BY t.LAUNCH_DATE DESC");
	    Pager pager= activitesDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
	    if(pager!=null){
	    	List<ActivitiesLaunch> list = new ArrayList<ActivitiesLaunch>();
	    	for (int i = 0; i < pager.getPageList().size(); i++) {
	    		Object[] obj = (Object[])pager.getPageList().get(i);
	    		ActivitiesLaunch alVo = new ActivitiesLaunch();
	    		if(obj[0]!=null) alVo.setId(obj[0].toString());
	    		if(obj[1]!=null) alVo.setLaunchDate(DateUtil.getDateFromString(obj[1].toString()));
	    		if(obj[2]!=null) alVo.setBranchId(obj[2].toString());
	    		if(obj[3]!=null) alVo.setActivityId(obj[3].toString());
	    		if(obj[4]!=null) alVo.setCreatorName(obj[4].toString());
	    		if(obj[5]!=null) alVo.setPoints(Integer.parseInt(obj[5].toString()));
	    		if(obj[6]!=null) alVo.setStatus(Integer.parseInt(obj[6].toString()));
	    		if(obj[7]!=null) alVo.setLaunchContent(obj[7].toString());
	    		list.add(alVo);
	    	}
	    	pager.setPageList(list);
	    }
	    return pager;
		
		
	}
	
	@Override
	public Pager queryALByStatusPager(Integer page, Integer rows,Integer[] Status,String userId) {
		List<Object> objectList = new ArrayList<Object>();
		String sql = "";
	    if(Status.length==1){
	    	sql=DangjianQl.MySql.activitiesFC;
	    	objectList.add(userId);
	    }else if(Status.length>1){
	    	sql=DangjianQl.MySql.activitiesCS;
	    	objectList.add(Status[0]);
	    	objectList.add(Status[1]);
	    }
	    Pager pager= activitesDaoImpl.queryEntitySQLList(page, rows, sql, objectList);
	    if(pager!=null){
	    	List<ActivitiesLaunchVo> list = new ArrayList<ActivitiesLaunchVo>();
	    	for (int i = 0; i < pager.getPageList().size(); i++) {
	    		Object[] obj = (Object[])pager.getPageList().get(i);
	    		ActivitiesLaunchVo alVo = new ActivitiesLaunchVo();
	    		if(obj[0]!=null) alVo.setId(obj[0].toString());
	    		if(obj[1]!=null) alVo.setTitle(obj[1].toString());
	    		if(obj[2]!=null) alVo.setLaunchDate(DateUtil.getDateFromString(obj[2].toString()));
	    		if(obj[3]!=null) alVo.setPoints(Integer.parseInt(obj[3].toString()));
	    		if(obj[4]!=null) alVo.setCreatorName(obj[4].toString());
	    		if(obj[5]!=null) alVo.setBranchName(obj[5].toString());
	    		if(obj[6]!=null) alVo.setStatus(Integer.parseInt(obj[6].toString()));
	    		list.add(alVo);
	    	}
	    	pager.setPageList(list);
	    }
		return pager;
	}
	
	@Override
	public JSONArray collectAL(ActivitiesLaunchVo activitiesLaunchVo,List<Activities> atList) {
		String[] months={"01","02","03","04","05","06","07","08","09","10","11","12"};
		
		String sql = DangjianQl.MySql.collectActivitiesLanuch2;
		sql = sql.replace("@@", activitiesLaunchVo.getBranchId());//支部ID
		sql = sql.replace("##", activitiesLaunchVo.getYear());//活动年份
		String sql2=sql;
		
		List<Object> listTimeYear=new ArrayList<>();
		for (int i = 0; i < months.length; i++) {
			//按月分循环查询开展活动
			String m=months[i];
			sql = sql2.replace("?", m);//活动月份
			if(StringUtils.isNotEmpty(sql)){
				List<Object> listAL=activitesDaoImpl.queryBySql(sql);
				if(atList!=null){
					JSONObject jsonMonth=new JSONObject();
					jsonMonth.put("month", m);
					List<Object> listTimeMonth=new ArrayList<>();
					for (Activities activities : atList) {
						//遍历所有活动
						String atTitle=activities.getTitle();
						if(listAL!=null){
							//listAtSpan 是每个汇总表每个空格的内容的集合，有可能是一个活动，有可能是多个活动
							List<String> listAtSpan=new ArrayList<>();
							for (int j = 0; j < listAL.size(); j++) {
								Object[] obj = (Object[])listAL.get(j);
								
								if(obj[0]!=null){
									String _atTitle=obj[0].toString();
									String _atTime= (obj[1]!=null)? obj[1].toString().substring(0, 10):"";//开展时间
									String _alId= (obj[2]!=null)?obj[2].toString():"";//开展活动的ID
									String _alpoint=(obj[3]!=null)?obj[3].toString():"0";//得分
									String _alFrequency=(obj[4]!=null)?obj[4].toString():"";//频率
									String _status=(obj[5]!=null)?obj[5].toString():"";//评审状态
									if(atTitle.equals(_atTitle)){
										if(_status.equals("3")){
											listAtSpan.add(_atTime+"|"+_alId+"|"+_alpoint+"|"+_alFrequency);
										}
									}
								}
							}
							listTimeMonth.add(listAtSpan);
						}
					}
					jsonMonth.put("atList", listTimeMonth);
					listTimeYear.add(jsonMonth);
				}
			}
		}
		return JSONArray.fromObject(listTimeYear);
	}
	@Override
	public List<ActivitiesLaunchVo> queryActivityRanking(String year) {
		List<ActivitiesLaunchVo> listVo=new ArrayList<>();
		String sql="";
		//按无限制查询，也是按一年内
		sql = DangjianQl.MySql.activitiesRanking;
		sql = sql.replace("##",year);//年份
		if(StringUtils.isNotEmpty(sql)){
			List<Object> listAL=activitesDaoImpl.queryBySql(sql);
			if(listAL!=null){
				for (int j = 0; j < listAL.size(); j++) {
					Object[] obj = (Object[])listAL.get(j);
					ActivitiesLaunchVo vo=new ActivitiesLaunchVo();
					if(obj[0]!=null){
						vo.setBranchId(obj[0].toString());
					}
					if(obj[1]!=null){
						vo.setBranchName(obj[1].toString());
					}
					if(obj[2]!=null){
						vo.setPoints(Integer.parseInt(obj[2].toString()));
					}
					listVo.add(vo);
				}
			}
		}
		return listVo;
	}
	@Override
	public void saveOrUpdateALR(ActivitiesLaunchReview activitiesLaunchReview) {
		if(StringUtils.isBlank(activitiesLaunchReview.getId())){
			this.save(activitiesLaunchReview);
		}else{
			this.update(activitiesLaunchReview);
		}
	}
	@Override
	public List<ActivitiesLaunchReview> queryALREntityList(
			ActivitiesLaunchReviewVo activitiesLaunchReviewVo) {
		// TODO Auto-generated method stub
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(activitiesLaunchReviewVo.getActivitiesLaunchId())){
			criterionsList.add(Restrictions.eq("activitiesLaunchId", activitiesLaunchReviewVo.getActivitiesLaunchId()));
		}
		if(StringUtils.isNotBlank(activitiesLaunchReviewVo.getUserId())){
			criterionsList.add(Restrictions.eq("userId", activitiesLaunchReviewVo.getUserId()));
		}

		return activitesDaoImpl.queryEntityList(criterionsList, Order.desc("createTime"), ActivitiesLaunchReview.class);
	}
	@Override
	public Pager queryALREntityListPager(Integer page, Integer rows,
			ActivitiesLaunchReviewVo activitiesLaunchReviewVo) {
		// TODO Auto-generated method stub
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(activitiesLaunchReviewVo.getActivitiesLaunchId())){
			criterionsList.add(Restrictions.eq("activitiesLaunchId", activitiesLaunchReviewVo.getActivitiesLaunchId()));
		}
		if(StringUtils.isNotBlank(activitiesLaunchReviewVo.getUserId())){
			criterionsList.add(Restrictions.eq("userId", activitiesLaunchReviewVo.getUserId()));
		}
		return this.activitesDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime"), ActivitiesLaunchReview.class);
	}
	@Override
	public boolean isAllCheck(ActivitiesLaunchReviewVo activitiesLaunchReviewVo) {
		boolean flag=true;
		List<ActivitiesLaunchReview> listALR= this.queryALREntityList(activitiesLaunchReviewVo);
		if(listALR!=null){
			for (ActivitiesLaunchReview activitiesLaunchReview : listALR) {
				if(activitiesLaunchReview.getIsPass()==null){
					flag=false;
				}
			}
		}
		return flag;
	}
	@Override
	public void deleteALRByFormAlId(String activitiesLaunchId) {
		ActivitiesLaunchReviewVo alrVo=new ActivitiesLaunchReviewVo();
		alrVo.setActivitiesLaunchId(activitiesLaunchId);
		List<ActivitiesLaunchReview> list = this.queryALREntityList(alrVo);
		if(list!=null){
			for (ActivitiesLaunchReview activitiesLaunchReview : list) {
				this.delete(activitiesLaunchReview);
			}
		}
	}
	
	
	


}
