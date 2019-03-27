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
import com.datacenter.dao.IRescueWorkDao;
import com.datacenter.module.RescueWork;
import com.datacenter.service.IRescueWorkService;
import com.datacenter.vo.RescueWorkVo;

/**
 * @Description 拯救作业 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("rescueWorkServiceImpl")
public class RescueWorkServiceImpl extends BaseServiceImpl implements IRescueWorkService{

	@Autowired
	private IRescueWorkDao rescueWorkDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, RescueWorkVo rescueWorkVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(rescueWorkVo.getTtId())){
			params.add(Restrictions.eq("ttId", rescueWorkVo.getTtId()));
		}
		if(rescueWorkVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", rescueWorkVo.getDutyDateStart()));
		}
		if(rescueWorkVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", rescueWorkVo.getDutyDateEnd()));
		}
		return this.rescueWorkDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), RescueWork.class);
	}

	@Override
	public RescueWork saveOrUpdate(RescueWorkVo rescueWorkVo) {
		RescueWork rescueWork = new RescueWork();
		BeanUtils.copyProperties(rescueWorkVo, rescueWork);
		if(StringUtils.isBlank(rescueWork.getId())){
			this.save(rescueWork);
		}else{
			this.update(rescueWork);
		}
		return rescueWork;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.rescueWorkDaoImpl.excuteBySql("delete from dc_RescueWork where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<RescueWork> list = this.rescueWorkDaoImpl.queryEntityList(params, Order.desc("createTime"), RescueWork.class);	//根据主表Id获取子表关联数据
		for (RescueWork rescueWork : list) {
			rescueWork.setDutyDate(dutyDate);
			this.update(rescueWork);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

}
