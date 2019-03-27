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
import com.datacenter.dao.IOperatingDataDao;
import com.datacenter.module.OperatingData;
import com.datacenter.service.IOperatingDataService;
import com.datacenter.vo.OperatingDataVo;

/**
 * @Description 营运数据 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("operatingDataServiceImpl")
public class OperatingDataServiceImpl extends BaseServiceImpl implements IOperatingDataService{

	@Autowired
	private IOperatingDataDao operatingDataDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, OperatingDataVo operatingDataVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(operatingDataVo.getTtId())){
			params.add(Restrictions.eq("ttId", operatingDataVo.getTtId()));
		}
		if(operatingDataVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", operatingDataVo.getDutyDateStart()));
		}
		if(operatingDataVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", operatingDataVo.getDutyDateEnd()));
		}
		return this.operatingDataDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), OperatingData.class);
	}

	@Override
	public OperatingData saveOrUpdate(OperatingDataVo operatingDataVo) {
		OperatingData operatingData = new OperatingData();
		BeanUtils.copyProperties(operatingDataVo, operatingData);
		if(StringUtils.isBlank(operatingData.getId())){
			this.save(operatingData);
		}else{
			this.update(operatingData);
		}
		return operatingData;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.operatingDataDaoImpl.excuteBySql("delete from dc_OperatingData where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<OperatingData> list = this.operatingDataDaoImpl.queryEntityList(params, Order.desc("createTime"), OperatingData.class);	//根据主表Id获取子表关联数据
		for (OperatingData operatingData : list) {
			operatingData.setDutyDate(dutyDate);
			this.update(operatingData);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

}
