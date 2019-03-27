package com.datacenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.datacenter.dao.ITrafficAccidentDao;
import com.datacenter.module.TrafficAccident;
import com.datacenter.service.ITrafficAccidentService;
import com.datacenter.vo.TrafficAccidentVo;

/**
 * @Description 交通事故 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("trafficAccidentServiceImpl")
public class TrafficAccidentServiceImpl extends BaseServiceImpl implements ITrafficAccidentService{

	@Autowired
	private ITrafficAccidentDao trafficAccidentDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, TrafficAccidentVo trafficAccidentVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(trafficAccidentVo.getTtId())){
			params.add(Restrictions.eq("ttId", trafficAccidentVo.getTtId()));
		}
		if(trafficAccidentVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", trafficAccidentVo.getDutyDateStart()));
		}
		if(trafficAccidentVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", trafficAccidentVo.getDutyDateEnd()));
		}
		return this.trafficAccidentDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), TrafficAccident.class);
	}

	@Override
	public TrafficAccident saveOrUpdate(TrafficAccidentVo trafficAccidentVo) {
		TrafficAccident trafficAccident = new TrafficAccident();
		BeanUtils.copyProperties(trafficAccidentVo, trafficAccident);
		if(StringUtils.isBlank(trafficAccident.getId())){
			this.save(trafficAccident);
		}else{
			this.update(trafficAccident);
		}
		return trafficAccident;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.trafficAccidentDaoImpl.excuteBySql("delete from dc_TrafficAccident where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<TrafficAccident> list = this.trafficAccidentDaoImpl.queryEntityList(params, Order.desc("createTime"), TrafficAccident.class);	//根据主表Id获取子表关联数据
		for (TrafficAccident trafficAccident : list) {
			trafficAccident.setDutyDate(dutyDate);
			this.update(trafficAccident);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

}
