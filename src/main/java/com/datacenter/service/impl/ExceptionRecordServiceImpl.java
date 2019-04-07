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
import com.datacenter.dao.IExceptionRecordDao;
import com.datacenter.module.ExceptionRecord;
import com.datacenter.service.IExceptionRecordService;
import com.datacenter.vo.ExceptionRecordVo;

/**
 * @Description 营运异常记录 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("exceptionRecordServiceImpl")
public class ExceptionRecordServiceImpl extends BaseServiceImpl implements IExceptionRecordService{

	@Autowired
	private IExceptionRecordDao exceptionRecordDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, ExceptionRecordVo exceptionRecordVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(exceptionRecordVo.getTtId())){
			params.add(Restrictions.eq("ttId", exceptionRecordVo.getTtId()));
		}
		if(exceptionRecordVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", exceptionRecordVo.getDutyDateStart()));
		}
		if(exceptionRecordVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", exceptionRecordVo.getDutyDateEnd()));
		}
		return this.exceptionRecordDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), ExceptionRecord.class);
	}

	@Override
	public ExceptionRecord saveOrUpdate(ExceptionRecordVo exceptionRecordVo) {
		ExceptionRecord exceptionRecord = new ExceptionRecord();
		BeanUtils.copyProperties(exceptionRecordVo, exceptionRecord);
		if(StringUtils.isBlank(exceptionRecord.getId())){
			this.save(exceptionRecord);
		}else{
			this.update(exceptionRecord);
		}
		return exceptionRecord;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.exceptionRecordDaoImpl.excuteBySql("delete from dc_ExceptionRecord where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<ExceptionRecord> list = this.exceptionRecordDaoImpl.queryEntityList(params, Order.desc("createTime"), ExceptionRecord.class);	//根据主表Id获取子表关联数据
		for (ExceptionRecord exceptionRecord : list) {
			exceptionRecord.setDutyDate(dutyDate);
			this.update(exceptionRecord);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<ExceptionRecord> queryEntityList(ExceptionRecordVo exceptionRecordVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from ExceptionRecord where 1 = 1 ");
		if(StringUtils.isNotBlank(exceptionRecordVo.getTtId())){
			objectList.add(exceptionRecordVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(exceptionRecordVo.getDutyDateStart() != null){		//日期Start
			objectList.add(exceptionRecordVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(exceptionRecordVo.getDutyDateEnd() != null){		//日期End
			objectList.add(exceptionRecordVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		//排序, 根据日期倒序排序,异常类型顺序排序
		hql.append(" order by dutyDate desc,exceptionType asc ");

		List<ExceptionRecord> erList = this.exceptionRecordDaoImpl.queryEntityHQLList(hql.toString(), objectList, ExceptionRecord.class);
		return erList;
	}

}
