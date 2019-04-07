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
import com.datacenter.dao.IRoadWorkDao;
import com.datacenter.module.RoadWork;
import com.datacenter.service.IRoadWorkService;
import com.datacenter.vo.RoadWorkVo;

/**
 * @Description 涉路施工 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("roadWorkServiceImpl")
public class RoadWorkServiceImpl extends BaseServiceImpl implements IRoadWorkService{

	@Autowired
	private IRoadWorkDao roadWorkDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, RoadWorkVo roadWorkVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(roadWorkVo.getTtId())){
			params.add(Restrictions.eq("ttId", roadWorkVo.getTtId()));
		}
		if(roadWorkVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", roadWorkVo.getDutyDateStart()));
		}
		if(roadWorkVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", roadWorkVo.getDutyDateEnd()));
		}
		return this.roadWorkDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), RoadWork.class);
	}

	@Override
	public RoadWork saveOrUpdate(RoadWorkVo roadWorkVo) {
		RoadWork roadWork = new RoadWork();
		BeanUtils.copyProperties(roadWorkVo, roadWork);
		if(StringUtils.isBlank(roadWork.getId())){
			this.save(roadWork);
		}else{
			this.update(roadWork);
		}
		return roadWork;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.roadWorkDaoImpl.excuteBySql("delete from dc_RoadWork where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<RoadWork> list = this.roadWorkDaoImpl.queryEntityList(params, Order.desc("createTime"), RoadWork.class);	//根据主表Id获取子表关联数据
		for (RoadWork roadWork : list) {
			roadWork.setDutyDate(dutyDate);
			this.update(roadWork);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<RoadWork> queryEntityList(RoadWorkVo roadWorkVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from RoadWork where 1 = 1 ");
		if(StringUtils.isNotBlank(roadWorkVo.getTtId())){
			objectList.add(roadWorkVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(roadWorkVo.getDutyDateStart() != null){		//日期Start
			objectList.add(roadWorkVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(roadWorkVo.getDutyDateEnd() != null){		//日期End
			objectList.add(roadWorkVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		//排序, 根据日期倒序排序，进场时间顺序排序
		hql.append(" order by dutyDate desc,approachTime asc ");

		List<RoadWork> rwList = this.roadWorkDaoImpl.queryEntityHQLList(hql.toString(), objectList, RoadWork.class);
		return rwList;
	}

}
