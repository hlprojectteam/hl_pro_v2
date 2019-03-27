package com.urms.visit.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.urms.user.module.User;
import com.urms.visit.dao.IVisitDao;
import com.urms.visit.module.Visit;
import com.urms.visit.service.IVisitService;
import com.urms.visit.vo.VisitVo;


@Repository("visitServiceImpl")
public class VisitServiceImpl extends BaseServiceImpl implements IVisitService {

	@Autowired
	public IVisitDao visitDaoImpl;

	@Override
	public void saveOrUpdate(Visit visit){
		if(StringUtils.isBlank(visit.getVisitId())){
			this.save(visit);
		}else{
			this.update(visit);
		}
	}

	@Override
	public Pager queryEntityList(int page,int rows,VisitVo visitVo,User user){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(visitVo.getVisitIp())){
			criterionsList.add(Restrictions.eq("visitIp", visitVo.getVisitIp()));
		}
		if(!"".equals(visitVo.getVisitOperatorType()) && visitVo.getVisitOperatorType()!=null){
			criterionsList.add(Restrictions.eq("visitOperatorType", visitVo.getVisitOperatorType()));
		}
		if(!"".equals(visitVo.getVisitSourceType()) && visitVo.getVisitSourceType()!=null){
			criterionsList.add(Restrictions.eq("visitSourceType", visitVo.getVisitSourceType()));
		}
		//权限过滤
		if(user.getType()!=1)//不是超级管理员情况下只能看到自己当前子系统组织
			criterionsList.add(Restrictions.eq("sysCode", user.getSysCode()));
		return visitDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("visitStartTime") ,Visit.class);
	}
	
	
	@Override
	public List<Visit> queryVisitList(VisitVo isitVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		/*if(StringUtils.isNotBlank(govOrgVo.getId()))
			criterionsList.add(Restrictions.eq("id", govOrgVo.getId()));
		if(StringUtils.isNotBlank(govOrgVo.getpId()))
			criterionsList.add(Restrictions.eq("pId", govOrgVo.getpId()));
		if(govOrgVo.getLevel()!=null)
			criterionsList.add(Restrictions.eq("level", govOrgVo.getLevel()));
		if(govOrgVo.getGovOrgCode()!=null)
			criterionsList.add(Restrictions.eq("govOrgCode", govOrgVo.getGovOrgCode()));
		if(govOrgVo.getSysCode()!=null)
			criterionsList.add(Restrictions.eq("sysCode", govOrgVo.getSysCode()));*/
		return visitDaoImpl.queryEntityList(criterionsList,Order.desc("visitStartTime"), Visit.class);
	}
	
	public IVisitDao getVisitDaoImpl() {
		return visitDaoImpl;
	}

	public void setgovOrgDaoImpl(IVisitDao visitDaoImpl) {
		this.visitDaoImpl = visitDaoImpl;
	}
}
