package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.datacenter.dao.ITrafficJamDao;
import com.datacenter.module.TrafficJam;
import com.datacenter.service.ITrafficJamService;
import com.datacenter.vo.TrafficJamVo;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 交通阻塞 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("trafficJamServiceImpl")
public class TrafficJamServiceImpl extends BaseServiceImpl implements ITrafficJamService{

	@Autowired
	private ITrafficJamDao trafficJamDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, TrafficJamVo trafficJamVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(trafficJamVo.getTtId())){
			params.add(Restrictions.eq("ttId", trafficJamVo.getTtId()));
		}
		if(trafficJamVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", trafficJamVo.getDutyDateStart()));
		}
		if(trafficJamVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", trafficJamVo.getDutyDateEnd()));
		}
		return this.trafficJamDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), TrafficJam.class);
	}

	@Override
	public TrafficJam saveOrUpdate(TrafficJamVo trafficJamVo) {
		TrafficJam trafficJam = new TrafficJam();
		BeanUtils.copyProperties(trafficJamVo, trafficJam);
		if(StringUtils.isBlank(trafficJam.getId())){
			this.save(trafficJam);
		}else{
			this.update(trafficJam);
		}
		return trafficJam;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.trafficJamDaoImpl.excuteBySql("delete from dc_TrafficJam where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<TrafficJam> list = this.trafficJamDaoImpl.queryEntityList(params, Order.desc("createTime"), TrafficJam.class);	//根据主表Id获取子表关联数据
		for (TrafficJam trafficJam : list) {
			trafficJam.setDutyDate(dutyDate);
			this.update(trafficJam);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<TrafficJam> queryEntityList(TrafficJamVo trafficJamVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from TrafficJam where 1 = 1 ");
		if(StringUtils.isNotBlank(trafficJamVo.getTtId())){
			objectList.add(trafficJamVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(trafficJamVo.getDutyDateStart() != null){		//日期Start
			objectList.add(trafficJamVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(trafficJamVo.getDutyDateEnd() != null){		//日期End
			objectList.add(trafficJamVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		//排序, 根据日期倒序排序,接报时间顺序排序
		hql.append(" order by dutyDate desc,receiptTime asc ");

		List<TrafficJam> tjList = this.trafficJamDaoImpl.queryEntityHQLList(hql.toString(), objectList, TrafficJam.class);
		return tjList;
	}

}
