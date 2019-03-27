package com.urms.visit.service;

import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.user.module.User;
import com.urms.visit.vo.VisitVo;
import com.urms.visit.module.Visit;

public interface IVisitService extends IBaseService {
	
	public void saveOrUpdate(Visit visit);
	
	public Pager queryEntityList(int page,int rows,VisitVo visitVo,User user);
	
	public List<Visit> queryVisitList(VisitVo visitVo);
	
}
