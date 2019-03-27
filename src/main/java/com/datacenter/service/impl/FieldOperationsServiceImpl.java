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
import com.datacenter.dao.IFieldOperationsDao;
import com.datacenter.module.FieldOperations;
import com.datacenter.service.IFieldOperationsService;
import com.datacenter.vo.FieldOperationsVo;

/**
 * @Description 外勤作业 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("fieldOperationsServiceImpl")
public class FieldOperationsServiceImpl extends BaseServiceImpl implements IFieldOperationsService{

	@Autowired
	private IFieldOperationsDao fieldOperationsDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, FieldOperationsVo fieldOperationsVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(fieldOperationsVo.getTtId())){
			params.add(Restrictions.eq("ttId", fieldOperationsVo.getTtId()));
		}
		if(fieldOperationsVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", fieldOperationsVo.getDutyDateStart()));
		}
		if(fieldOperationsVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", fieldOperationsVo.getDutyDateEnd()));
		}
		return this.fieldOperationsDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), FieldOperations.class);
	}

	@Override
	public FieldOperations saveOrUpdate(FieldOperationsVo fieldOperationsVo) {
		FieldOperations fieldOperations = new FieldOperations();
		BeanUtils.copyProperties(fieldOperationsVo, fieldOperations);
		if(StringUtils.isBlank(fieldOperations.getId())){
			this.save(fieldOperations);
		}else{
			this.update(fieldOperations);
		}
		return fieldOperations;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.fieldOperationsDaoImpl.excuteBySql("delete from dc_FieldOperations where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<FieldOperations> list = this.fieldOperationsDaoImpl.queryEntityList(params, Order.desc("createTime"), FieldOperations.class);	//根据主表Id获取子表关联数据
		for (FieldOperations fieldOperations : list) {
			fieldOperations.setDutyDate(dutyDate);
			this.update(fieldOperations);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

}
