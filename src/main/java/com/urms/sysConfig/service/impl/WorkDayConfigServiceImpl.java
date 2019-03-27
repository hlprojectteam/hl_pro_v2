package com.urms.sysConfig.service.impl;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.cache.Cache;
import com.common.utils.helper.Pager;
import com.urms.sysConfig.dao.IWorkDayConfigDao;
import com.urms.sysConfig.module.WorkDayConfig;
import com.urms.sysConfig.service.IWorkDayConfigService;
import com.urms.sysConfig.vo.WorkDayConfigVo;

@Repository("workDayConfigServiceImpl")
public class WorkDayConfigServiceImpl extends BaseServiceImpl implements IWorkDayConfigService{
	
	@Autowired
	public IWorkDayConfigDao workDayConfigDaoImpl;

	@Override
	public void saveOrUpdate(WorkDayConfig workDayConfig){
		if(StringUtils.isBlank(workDayConfig.getId())){
			this.save(workDayConfig);
		}else{
			this.update(workDayConfig);			
		}
		this.getWorkDayConfig();//获得子系统键值对
	}
	
	@Override
	public Pager queryEntityList(int page,int rows,WorkDayConfigVo workDayConfigVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		return workDayConfigDaoImpl.queryList(page, rows, criterionsList, Order.desc("createTime") ,WorkDayConfig.class);
	}
	
	@Override
	public List<WorkDayConfig> queryWorkDayConfigList(WorkDayConfigVo workDayConfigVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(workDayConfigVo.getId()))
			criterionsList.add(Restrictions.eq("id", workDayConfigVo.getId()));
		return workDayConfigDaoImpl.queryList(criterionsList,Order.asc("createTime"), WorkDayConfig.class);
	}
	
	@Override
	public void getWorkDayConfig() {
		List<WorkDayConfig> list = this.workDayConfigDaoImpl.queryAllEntity(WorkDayConfig.class, null);
		Cache.getWorkDayConfig.clear();
		for (int i = 0; i < list.size(); i++) {
			Cache.getWorkDayConfig.put(Integer.parseInt(list.get(i).getSysKey()), list.get(i).getSysValue());
		}
	}

	public IWorkDayConfigDao getWorkDayConfigDaoImpl() {
		return workDayConfigDaoImpl;
	}

	public void setWorkDayConfigDaoImpl(IWorkDayConfigDao workDayConfigDaoImpl) {
		this.workDayConfigDaoImpl = workDayConfigDaoImpl;
	}
	
}
