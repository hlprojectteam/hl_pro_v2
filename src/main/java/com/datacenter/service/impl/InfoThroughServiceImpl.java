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
import com.datacenter.dao.IInfoThroughDao;
import com.datacenter.module.InfoThrough;
import com.datacenter.service.IInfoThroughService;
import com.datacenter.vo.InfoThroughVo;

/**
 * @Description 信息通传 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("infoThroughServiceImpl")
public class InfoThroughServiceImpl extends BaseServiceImpl implements IInfoThroughService{

	@Autowired
	private IInfoThroughDao infoThroughDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, InfoThroughVo infoThroughVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(infoThroughVo.getTtId())){
			params.add(Restrictions.eq("ttId", infoThroughVo.getTtId()));
		}
		if(infoThroughVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", infoThroughVo.getDutyDateStart()));
		}
		if(infoThroughVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", infoThroughVo.getDutyDateEnd()));
		}
		return this.infoThroughDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), InfoThrough.class);
	}

	@Override
	public InfoThrough saveOrUpdate(InfoThroughVo infoThroughVo) {
		InfoThrough infoThrough = new InfoThrough();
		BeanUtils.copyProperties(infoThroughVo, infoThrough);
		if(StringUtils.isBlank(infoThrough.getId())){
			this.save(infoThrough);
		}else{
			this.update(infoThrough);
		}
		return infoThrough;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.infoThroughDaoImpl.excuteBySql("delete from dc_InfoThrough where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<InfoThrough> list = this.infoThroughDaoImpl.queryEntityList(params, Order.desc("createTime"), InfoThrough.class);	//根据主表Id获取子表关联数据
		for (InfoThrough infoThrough : list) {
			infoThrough.setDutyDate(dutyDate);
			this.update(infoThrough);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<InfoThrough> queryEntityList(InfoThroughVo infoThroughVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from InfoThrough where 1 = 1 ");
		if(StringUtils.isNotBlank(infoThroughVo.getTtId())){
			objectList.add(infoThroughVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(infoThroughVo.getDutyDateStart() != null){		//日期Start
			objectList.add(infoThroughVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(infoThroughVo.getDutyDateEnd() != null){		//日期End
			objectList.add(infoThroughVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		//排序, 根据日期倒序排序,通报时间顺序排序
		hql.append(" order by dutyDate desc,throughTime asc ");

		List<InfoThrough> itList = this.infoThroughDaoImpl.queryEntityHQLList(hql.toString(), objectList, InfoThrough.class);
		return itList;
	}

}
