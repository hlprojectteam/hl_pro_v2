package com.common.base.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.dao.IBaseDao;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;

@Repository("baseDaoImpl")
@SuppressWarnings("unchecked")
public class BaseDaoImpl implements IBaseDao{

	public static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);  
	
	@Autowired
	public SessionFactory sessionFactory; 
	
	@Override
	public Session getSession(){
		 Session session = this.sessionFactory.getCurrentSession();
		    if (session == null) {
		      session = this.sessionFactory.openSession();
		    }
		    return session;
	}
	
	@Override
	public void save(Object entity) {
		this.getSession().save(entity);
	}

	@Override
	public void update(Object entity) {
		this.getSession().update(entity);
		
	}

	@Override
	public void saveOrUpdate(Object entity) {
		this.getSession().saveOrUpdate(entity);
	}
	
	@Override
	public void delete(Object entity) {
		this.getSession().delete(entity);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("rawtypes")
	public <T>Pager queryEntityList(int page, int rows, List<Criterion> criterionsList,Order order,Class<T> objClass){  
	    Pager pager = null;  
	    try{  
	        Criteria criteria = this.getSession().createCriteria(objClass);
	        if(criterionsList != null){  
	        	for (int i = 0; i < criterionsList.size(); i++) {
	        		criteria.add(criterionsList.get(i));
				}
	        }  
	        // 获取根据条件分页查询的总行数  
	        long rowCount = (Long) criteria.setProjection(Projections.rowCount()).setCacheable(true).uniqueResult();  
	        criteria.setProjection(null);  
	        criteria.setFirstResult((page-1) * rows);  
	        criteria.setMaxResults(rows);  
	        if(order!=null)
	        	criteria.addOrder(order);//排序
			List result = criteria.setCacheable(true).list();  
	        long pageCount = (rowCount%rows==0)?rowCount/rows:(rowCount/rows+1); //总页数
	        pager = new Pager(pageCount,rowCount, result);  
	    } catch (RuntimeException re){  
	    	logger.error(re.getMessage(), re);
	    }   
	    return pager;  
	}  
	
	public <T>Pager queryEntityHQLList(int page, int rows,String HQL,List<Object> attributeList){
		Pager pager = null;  
		try {
			Query query = this.getSession().createQuery(HQL);
			if(attributeList!=null && !attributeList.isEmpty()){
				for (int i = 0, size = attributeList.size(); i < size; i++) {
					query.setParameter(i, attributeList.get(i));
				}
			}
			query.setFirstResult((page-1)*rows); 
			query.setMaxResults(rows); 
			List<Object> list= (List<Object>)query.setCacheable(true).list();
			String countHql = "select count(*) "+HQL; //查询总记录条数
			Query queryCount = this.getSession().createQuery(countHql).setCacheable(true); 
			if(attributeList!=null && !attributeList.isEmpty()){
				for (int i = 0, size = attributeList.size(); i < size; i++) {
					queryCount.setParameter(i, attributeList.get(i));
				}
			}
			long rowCount = (Long)queryCount.uniqueResult();
			long pageCount = (rowCount%rows==0)?rowCount/rows:(rowCount/rows+1); //总页数
			pager = new Pager(pageCount,rowCount,list);  			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}
	
	public <T>Pager queryEntitySQLList(int page, int rows, String SQL, List<Object> attributeList) {
		Pager pager = null;  
		try {
			SQLQuery sqlQuery = this.getSession().createSQLQuery(SQL);
			if(attributeList!=null && !attributeList.isEmpty()){
				for (int i = 0, size = attributeList.size(); i < size; i++) {
					sqlQuery.setParameter(i, attributeList.get(i));
				}
			}
			sqlQuery.setFirstResult((page-1)*rows); 
			sqlQuery.setMaxResults(rows); 
			List<Object> list= (List<Object>)sqlQuery.list();
			String countSql = "select count(*) from ("+SQL+") as t"; //查询总记录条数
			SQLQuery sqlQueryCount = this.getSession().createSQLQuery(countSql); 
			if(attributeList!=null && !attributeList.isEmpty()){
				for (int i = 0, size = attributeList.size(); i < size; i++) {
					sqlQueryCount.setParameter(i, attributeList.get(i));
				}
			}
			BigInteger rowCountBig = (BigInteger)sqlQueryCount.uniqueResult();
			long rowCount = rowCountBig.longValue();
			long pageCount = (rowCount%rows==0)?rowCount/rows:(rowCount/rows+1); //总页数
			pager = new Pager(pageCount,rowCount,list);  			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pager;
	}
	
	public <T>List<T> queryEntitySQLList(String SQL, List<Object> attributeList) {
		List<T> result = null; 
		try {
			SQLQuery sqlQuery = this.getSession().createSQLQuery(SQL);
			if(attributeList!=null && !attributeList.isEmpty()){
				for (int i = 0, size = attributeList.size(); i < size; i++) {
					sqlQuery.setParameter(i, attributeList.get(i));
				}
			}
			result = (List<T>)sqlQuery.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	public <T>List<T>  queryEntityHQLList(String HQL, List<Object> attributeList, Class<T> objClass){
		List<T> result = null; 
		try {
			Query query = this.getSession().createQuery(HQL);
			if(attributeList!=null && !attributeList.isEmpty()){	
				for (int i = 0, size = attributeList.size(); i < size; i++) {
					query.setParameter(i, attributeList.get(i));
				}
			}
			result = (List<T>)query.setCacheable(true).list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	public <T> T getEntityById(Class<T> objClass,String id){
		return (T) this.getSession().get(objClass, id);
	}

	@Override
	public <T> List<T> queryEntityList(List<Criterion> criterionsList,Order order,Class<T> objClass) {
		List<T> result = null;
	    try{  
	        Criteria criteria = this.getSession().createCriteria(objClass);  
	        if(criterionsList != null){  
	        	for (int i = 0; i < criterionsList.size(); i++) {
	        		criteria.add(criterionsList.get(i));
				}
	        }  
	        if(order!=null)
	        	criteria.addOrder(order);//排序
	        criteria.setProjection(null);  
	        result = criteria.setCacheable(true).list();  
	    } catch (RuntimeException re){ 
	    	logger.error(re.getMessage(), re);
	    }   
	    return result; 
	}

	@Override
	public <T> List<T> queryAllEntity(Class<T> objClass,Order order) {
		List<T> result = null;
	    try{  
	        Criteria criteria = this.getSession().createCriteria(objClass);  
	        if(order!=null)
	        	criteria.addOrder(order);//排序
	        criteria.setProjection(null);  
	        result = criteria.setCacheable(true).list();  
	    } catch (RuntimeException re){  
	    	logger.error(re.getMessage(), re);
	    }   
		return result;
	}

	@Override
	public <T> T getEntityByHQL(String hql,List<Object> attributeList) {
		Query query = this.getSession().createQuery(hql);
		if(attributeList!=null && !attributeList.isEmpty()){
			for (int i = 0, size = attributeList.size(); i < size; i++) {
				query.setParameter(i, attributeList.get(i));
			}
		}
		List<T> list = query.setCacheable(true).list();
		if(!list.isEmpty())
			return (T)list.get(0);
		else
			return null;
	}
	
	@Override
	public void executeHQL(String hql,List<Object> attributeList) {
		Query query = this.getSession().createQuery(hql);
		if(attributeList!=null && !attributeList.isEmpty()){
			for (int i = 0, size = attributeList.size(); i < size; i++) {
				query.setParameter(i, attributeList.get(i));
			}
		}
		query.executeUpdate();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List queryBySql(String sql) {    
	     List<Object[]> list = this.getSession().createSQLQuery(sql).list();    
	     return list;    
	}    
	
	@Override
	public Object findEntityBySql(String sql) {    
	     return this.getSession().createSQLQuery(sql).uniqueResult();        
	} 
	
	@Override
	public long queryCountBySql(String sql){
		SQLQuery sqlQueryCount = this.getSession().createSQLQuery(sql);
		BigInteger rowCountBig = (BigInteger)sqlQueryCount.uniqueResult();
		return rowCountBig.longValue();
	}
	
	@Override
	public long queryCountByHql(String HQL,List<Object> attributeList){
		String countHql = "select count(*) "+HQL; //查询总记录条数
		Query queryCount = this.getSession().createQuery(countHql).setCacheable(true); 
		if(attributeList!=null && !attributeList.isEmpty()){
			for (int i = 0, size = attributeList.size(); i < size; i++) {
				queryCount.setParameter(i, attributeList.get(i));
			}
		}
		long rowCount = (Long)queryCount.uniqueResult();
		return rowCount;
	}
	
	@Override
	public int excuteBySql(String sql){    
        SQLQuery query = this.getSession().createSQLQuery(sql);    
        int result = query.executeUpdate();    
        return result;    
    }

	@Override
	public int updateBySql(String sql,List<Object> attributeList) {
		SQLQuery sqlQuery=  this.getSession().createSQLQuery(sql);
		if(attributeList!=null && !attributeList.isEmpty()){
			for (int i = 0, size = attributeList.size(); i < size; i++) {
				sqlQuery.setParameter(i, attributeList.get(i));
			
			}
		}
		int k = sqlQuery.executeUpdate();
		return k;
	}
	
	@Override
	public  <T> T getEntityBySql(String sql,List<Object> attributeList,Class<T> objClass){
		List<T> list = new ArrayList<T>();
		SQLQuery sqlQuery=  this.getSession().createSQLQuery(sql);
		if(attributeList!=null && !attributeList.isEmpty()){
			for (int i = 0, size = attributeList.size(); i < size; i++) {
				sqlQuery.setParameter(i, attributeList.get(i));
			}
		}
		list = sqlQuery.addEntity(objClass).list();
		T entity =null;
		if(list!= null && list.size()==1){
			entity =(T)list.get(0);
		}
		return entity;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public <T>Pager queryList(int page, int rows, List<Criterion> criterionsList,Order order,Class<T> objClass){
		Pager pager = null;
		try{
			Criteria criteria = this.getSession().createCriteria(objClass);
			if(criterionsList != null){
				for (int i = 0; i < criterionsList.size(); i++) {
					criteria.add(criterionsList.get(i));
				}
			}
			// 获取根据条件分页查询的总行数
			long rowCount = (Long) criteria.setProjection(Projections.rowCount()).setCacheable(true).uniqueResult();
			criteria.setProjection(null);
			criteria.setFirstResult((page-1) * rows);
			criteria.setMaxResults(rows);
			if(order!=null)
				criteria.addOrder(order);//排序
			List result = criteria.setCacheable(true).list();
			long pageCount = (rowCount%rows==0)?rowCount/rows:(rowCount/rows+1); //总页数
			pager = new Pager(pageCount,rowCount, result);
		} catch (RuntimeException re){
			logger.error(re.getMessage(), re);
		}
		return pager;
	}

	@Override
	public <T> List<T> queryList(List<Criterion> criterionsList,Order order,Class<T> objClass) {
		List<T> result = null;
		try{
			Criteria criteria = this.getSession().createCriteria(objClass);
			if(criterionsList != null){
				for (int i = 0; i < criterionsList.size(); i++) {
					criteria.add(criterionsList.get(i));
				}
			}
			if(order!=null)
				criteria.addOrder(order);//排序
			criteria.setProjection(null);
			result = criteria.setCacheable(true).list();
		} catch (RuntimeException re){
			logger.error(re.getMessage(), re);
		}
		return result;
	}
}
