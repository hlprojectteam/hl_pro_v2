package com.common.base.service;

import java.util.List;

import org.hibernate.criterion.Order;




public interface IBaseService {
	
	/**
	 * @intruduction 保存实体
	 * @param entity
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:10:49
	 */
	public void save(Object entity);
	
	/**
	 * @intruduction 更新实体
	 * @param entity
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:11:08
	 */
	public void update(Object entity);
	
	/**
	 * @intruduction 更新或者保存
	 * @param entity
	 * @author Mr.Wang
	 * @Date 2016年2月17日上午11:21:21
	 */
	public void saveOrUpdate(Object entity);
	
	/**
	 * @intruduction 凭ID查询对象实体
	 * @param objClass 实体.class
	 * @param id 实体id
	 * @return 实体
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:11:22
	 */
	public <T> T getEntityById(Class<T> objClass,String id);
	
	/**
	 * @intruduction 获得所有实体
	 * @param objClass 实体.class
	 * @param order 排序 使用方法 如:Order.desc("createTime") Order.desc("order")
	 * @return 实体List集合
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:11:56
	 */
	public <T>List<T> getAllEntity(Class<T> objClass,Order order);
	
	/**
	 * @intruduction 删除实体
	 * @param objClass 实体class
	 * @param ids 所有实体主键ID  如：xxx,yyy,zzz
	 * @author Mr.Wang
	 * @param <T>
	 * @Date 2016年1月7日下午1:45:55
	 */
	public <T> void delete(Class<T> objClass,String ids);
	
	/**
	 * @intruduction 删除实体
	 * @param entity 实体对象
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:15:01
	 */
	public void delete(Object entity);
	
	/**
	 * @intruduction 通过HQL获得List
	 * @param HQL hql语句
	 * @param attributeList hql中?的属性值
	 * @param objClass 实体.class
	 * @return 实体集合
	 * @author Mr.Wang
	 * @Date 2016年4月8日下午4:09:32
	 */
	public <T>List<T> queryEntityHQLList(String HQL,List<Object> attributeList, Class<T> objClass);
	
}
