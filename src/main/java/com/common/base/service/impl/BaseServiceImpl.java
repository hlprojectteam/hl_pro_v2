package com.common.base.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.dao.IBaseDao;
import com.common.base.service.IBaseService;
@Repository("baseServiceImpl")
public class BaseServiceImpl implements IBaseService{
	
	public static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);  
	
	@Autowired
	public IBaseDao baseDaoImpl;

	@Override
	public void save(Object entity) {
		this.baseDaoImpl.save(entity);
	}

	@Override
	public void saveOrUpdate(Object entity) {
		this.baseDaoImpl.saveOrUpdate(entity);
	}
	
	@Override
	public void update(Object entity) {
		this.baseDaoImpl.update(entity);
	}

	@Override
	public <T> T getEntityById(Class<T> objClass,String id){
		return this.baseDaoImpl.getEntityById(objClass, id);
	}
	
	@Override
	public <T> List<T> getAllEntity(Class<T> objClass, Order order) {
		return this.baseDaoImpl.queryAllEntity(objClass, order);
	}
	
	@Override
	public <T> void delete(Class<T> objClass,String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			this.baseDaoImpl.delete(this.getEntityById(objClass,idz[i]));
		}
	}
	
	@Override
	public void delete(Object entity) {
		this.baseDaoImpl.delete(entity);
	}
	
	public IBaseDao getBaseDaoImpl() {
		return baseDaoImpl;
	}
	
	public void setBaseDaoImpl(IBaseDao baseDaoImpl) {
		this.baseDaoImpl = baseDaoImpl;
	}

	@Override
	public <T> List<T> queryEntityHQLList(String HQL,List<Object> attributeList, Class<T> objClass) {
		return this.baseDaoImpl.queryEntityHQLList(HQL, attributeList, objClass);
	}

}
