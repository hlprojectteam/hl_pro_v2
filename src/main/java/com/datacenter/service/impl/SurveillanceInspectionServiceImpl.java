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
import com.datacenter.dao.ISurveillanceInspectionDao;
import com.datacenter.module.SurveillanceInspection;
import com.datacenter.service.ISurveillanceInspectionService;
import com.datacenter.vo.SurveillanceInspectionVo;

/**
 * @Description 监控巡检 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("surveillanceInspectionServiceImpl")
public class SurveillanceInspectionServiceImpl extends BaseServiceImpl implements ISurveillanceInspectionService{

	@Autowired
	private ISurveillanceInspectionDao surveillanceInspectionDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, SurveillanceInspectionVo surveillanceInspectionVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(surveillanceInspectionVo.getTtId())){
			params.add(Restrictions.eq("ttId", surveillanceInspectionVo.getTtId()));
		}
		if(surveillanceInspectionVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", surveillanceInspectionVo.getDutyDateStart()));
		}
		if(surveillanceInspectionVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", surveillanceInspectionVo.getDutyDateEnd()));
		}
		return this.surveillanceInspectionDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), SurveillanceInspection.class);
	}

	@Override
	public SurveillanceInspection saveOrUpdate(SurveillanceInspectionVo surveillanceInspectionVo) {
		SurveillanceInspection surveillanceInspection = new SurveillanceInspection();
		BeanUtils.copyProperties(surveillanceInspectionVo, surveillanceInspection);
		if(StringUtils.isBlank(surveillanceInspection.getId())){
			this.save(surveillanceInspection);
		}else{
			this.update(surveillanceInspection);
		}
		return surveillanceInspection;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.surveillanceInspectionDaoImpl.excuteBySql("delete from dc_SurveillanceInspection where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<SurveillanceInspection> list = this.surveillanceInspectionDaoImpl.queryEntityList(params, Order.desc("createTime"), SurveillanceInspection.class);	//根据主表Id获取子表关联数据
		for (SurveillanceInspection surveillanceInspection : list) {
			surveillanceInspection.setDutyDate(dutyDate);
			this.update(surveillanceInspection);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

}
