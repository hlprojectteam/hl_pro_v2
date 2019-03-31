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
import com.datacenter.dao.IBriefDao;
import com.datacenter.module.Brief;
import com.datacenter.service.IBriefService;
import com.datacenter.vo.BriefVo;

/**
 * @Description 工作简报 service实现
 * @author xuezb
 * @date 2019年2月18日
 */
@Repository("briefServiceImpl")
public class BriefServiceImpl extends BaseServiceImpl implements IBriefService{

	@Autowired
	private IBriefDao briefDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, BriefVo briefVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(briefVo.getTtId())){
			params.add(Restrictions.eq("ttId", briefVo.getTtId()));
		}
		if(briefVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", briefVo.getDutyDateStart()));
		}
		if(briefVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", briefVo.getDutyDateEnd()));
		}
		return this.briefDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), Brief.class);
	}

	@Override
	public Brief saveOrUpdate(BriefVo briefVo) {
		Brief brief = new Brief();
		BeanUtils.copyProperties(briefVo, brief);
		if(StringUtils.isBlank(brief.getId())){
			this.save(brief);
		}else{
			this.update(brief);
		}
		return brief;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.briefDaoImpl.excuteBySql("delete from dc_Brief where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<Brief> list = this.briefDaoImpl.queryEntityList(params, Order.desc("createTime"), Brief.class);	//根据主表Id获取子表关联数据
		for (Brief brief : list) {
			brief.setDutyDate(dutyDate);
			this.update(brief);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<Brief> queryEntityList(BriefVo briefVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from Brief where 1 = 1 ");
		if(StringUtils.isNotBlank(briefVo.getTtId())){
			objectList.add(briefVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(briefVo.getDutyDateStart() != null){		//日期Start
			objectList.add(briefVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(briefVo.getDutyDateEnd() != null){		//日期End
			objectList.add(briefVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		//排序, 根据日期倒序排序
		hql.append(" order by dutyDate desc ");

		List<Brief> trList = this.briefDaoImpl.queryEntityHQLList(hql.toString(), objectList, Brief.class);
		return trList;
	}

}
