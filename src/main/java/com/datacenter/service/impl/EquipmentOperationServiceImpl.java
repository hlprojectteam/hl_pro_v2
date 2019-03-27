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
import com.datacenter.dao.IEquipmentOperationDao;
import com.datacenter.module.EquipmentOperation;
import com.datacenter.service.IEquipmentOperationService;
import com.datacenter.vo.EquipmentOperationVo;

/**
 * @Description 设备运行情况统计表 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("equipmentOperationServiceImpl")
public class EquipmentOperationServiceImpl extends BaseServiceImpl implements IEquipmentOperationService{

	@Autowired
	private IEquipmentOperationDao equipmentOperationDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, EquipmentOperationVo equipmentOperationVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(equipmentOperationVo.getTtId())){
			params.add(Restrictions.eq("ttId", equipmentOperationVo.getTtId()));
		}
		if(equipmentOperationVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", equipmentOperationVo.getDutyDateStart()));
		}
		if(equipmentOperationVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", equipmentOperationVo.getDutyDateEnd()));
		}
		return this.equipmentOperationDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), EquipmentOperation.class);
	}

	@Override
	public EquipmentOperation saveOrUpdate(EquipmentOperationVo equipmentOperationVo) {
		EquipmentOperation equipmentOperation = new EquipmentOperation();
		BeanUtils.copyProperties(equipmentOperationVo, equipmentOperation);
		if(StringUtils.isBlank(equipmentOperation.getId())){
			this.save(equipmentOperation);
		}else{
			this.update(equipmentOperation);
		}
		return equipmentOperation;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.equipmentOperationDaoImpl.excuteBySql("delete from dc_EquipmentOperation where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<EquipmentOperation> list = this.equipmentOperationDaoImpl.queryEntityList(params, Order.desc("createTime"), EquipmentOperation.class);	//根据主表Id获取子表关联数据
		for (EquipmentOperation equipmentOperation : list) {
			equipmentOperation.setDutyDate(dutyDate);
			this.update(equipmentOperation);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

}
