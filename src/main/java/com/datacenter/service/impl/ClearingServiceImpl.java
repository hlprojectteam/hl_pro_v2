package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.datacenter.dao.IClearingDao;
import com.datacenter.module.Clearing;
import com.datacenter.service.IClearingService;
import com.datacenter.vo.ClearingVo;
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
 * @Description 清障保洁 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("clearingServiceImpl")
public class ClearingServiceImpl extends BaseServiceImpl implements IClearingService{

	@Autowired
	private IClearingDao clearingDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, ClearingVo clearingVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(clearingVo.getTtId())){
			params.add(Restrictions.eq("ttId", clearingVo.getTtId()));
		}
		if(clearingVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", clearingVo.getDutyDateStart()));
		}
		if(clearingVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", clearingVo.getDutyDateEnd()));
		}
		return this.clearingDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), Clearing.class);
	}

	@Override
	public Clearing saveOrUpdate(ClearingVo clearingVo) {
		Clearing clearing = new Clearing();
		BeanUtils.copyProperties(clearingVo, clearing);
		if(StringUtils.isBlank(clearing.getId())){
			this.save(clearing);
		}else{
			this.update(clearing);
		}
		return clearing;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.clearingDaoImpl.excuteBySql("delete from dc_Clearing where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<Clearing> list = this.clearingDaoImpl.queryEntityList(params, Order.desc("createTime"), Clearing.class);	//根据主表Id获取子表关联数据
		for (Clearing clearing : list) {
			clearing.setDutyDate(dutyDate);
			this.update(clearing);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<Clearing> queryEntityList(ClearingVo clearingVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from Clearing where 1 = 1 ");
		if(StringUtils.isNotBlank(clearingVo.getTtId())){
			objectList.add(clearingVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(clearingVo.getDutyDateStart() != null){		//日期Start
			objectList.add(clearingVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(clearingVo.getDutyDateEnd() != null){		//日期End
			objectList.add(clearingVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		//排序, 根据日期倒序排序,接报时间
		hql.append(" order by dutyDate desc,receiptTime asc ");

		List<Clearing> cList = this.clearingDaoImpl.queryEntityHQLList(hql.toString(), objectList, Clearing.class);
		return cList;
	}

}
