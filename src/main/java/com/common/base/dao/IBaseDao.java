package com.common.base.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.common.utils.helper.Pager;

public interface IBaseDao {
	
	
	public Session getSession();
	/**
	 * 保存实体
	 * @param entity
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:18:51
	 */
	public void save(Object entity);
	
	/**
	 * 查询带分页
	 * @param page 开始页
	 * @param rows 每页显示条数
	 * @param criterionsList 条件
	 * @param order 排序
	 * @param objClass 类.class
	 * @return 分页实体类
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:19:04
	 */
	public <T>Pager queryEntityList(int page, int rows, List<Criterion> criterionsList,Order order,Class<T> objClass);
	
	/**
	 * 查询实体不带分页
	 * @param criterionsList 条件
	 * @param order 排序
	 * @param objClass
	 * @return 实体List集合
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:20:10
	 */
	public <T>List<T> queryEntityList(List<Criterion> criterionsList,Order order,Class<T> objClass);
	
	/**
	 * HQL分页查询
	 * @param page 开始页
	 * @param rows 每页大小
	 * @param HQL 查询语句
	 * @param attributeList 查询条件
	 * @return 分页实体类
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:21:25
	 */
	public <T>Pager queryEntityHQLList(int page, int rows,String HQL,List<Object> attributeList);
	
	/**
	 * SQL分页查询
	 * @param page 开始页
	 * @param rows 每页大小
	 * @param SQL 查询语句
	 * @param attributeList 查询条件
	 * @return 分页实体类
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:22:11
	 */
	public <T>Pager queryEntitySQLList(int page, int rows, String SQL, List<Object> attributeList);
	
	/**
	 * SQL查询
	 * @param SQL 
	 * @param attributeList 查询条件
	 * @return List实体集合
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:23:05
	 */
	public <T>List<T> queryEntitySQLList(String SQL, List<Object> attributeList);
	
	/**
	 * HQL查询
	 * @param HQL 
	 * @param attributeList  查询条件
	 * @param objClass 实体类
	 * @return List实体集合
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:24:07
	 */
	public <T>List<T> queryEntityHQLList(String HQL, List<Object> attributeList, Class<T> objClass);
	
	/**
	 * 更新
	 * @param entity 实体
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:24:49
	 */
	public void update(Object entity);
	
	/**
	 * 执行HQL
	 * @param HQL
	 * @param attributeList 条件
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:25:03
	 */
	public void executeHQL(String HQL,List<Object> attributeList);
	
	/**
	 * @intruduction 删除实体
	 * @param entity
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:25:16
	 */
	public void delete(Object entity);
	
	/**
	 * @intruduction 保存or更新
	 * @param entity
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:25:27
	 */
	public void saveOrUpdate(Object entity);
	
	/**
	 * @intruduction 凭ID查询对象实体
	 * @param objClass 实体类
	 * @param id 实体id
	 * @return 实体对象
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:25:34
	 */
	public <T> T getEntityById(Class<T> objClass,String id);
	
	/**
	 * @intruduction 获得所有实体
	 * @param objClass 类
	 * @param order 排序 如:Order.desc("createTime") Order.desc("order")
	 * @return 实体集合
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:26:05
	 */
	public <T>List<T> queryAllEntity(Class<T> objClass,Order order);
	
	/**
	 * @intruduction 查询sql
	 * @param sql
	 * @return 集合
	 * @author Mr.Wang
	 * @Date 2016年2月29日下午4:55:01
	 */
	public List<Object> queryBySql(String sql);
	
	/**
	 * @intruduction 通过sql获得字段值（返回一个对象）
	 * @param sql
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年3月22日下午3:50:44
	 */
	public Object findEntityBySql(String sql);
	
	/**
	 * @intruduction 执行原始sql
	 * @param sql
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年2月29日下午4:52:19
	 */
	public int excuteBySql(String sql);
	
	/**
	 * @intruduction 查询表总条数sql
	 * @param sql
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年3月11日下午3:34:20
	 */
	public long queryCountBySql(String sql);

	/**
	 * @intruduction 通过hql获得实体
	 * @param hql
	 * @param attributeList
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年4月20日上午10:09:21
	 */
	public <T> T getEntityByHQL(String hql,List<Object> attributeList);
	
	/**
	 * @intruduction 更新bySql
	 * @param sql
	 * @param attributeList 查询条件
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年6月6日下午3:17:21
	 */
	public int updateBySql(String sql,List<Object> attributeList);
	
	/**
	 * @intruduction 通过sql查询实体
	 * @param sql
	 * @param attributeList 查询条件
	 * @param objClass 类
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年6月6日下午3:35:24
	 */
	public  <T> T getEntityBySql(String sql,List<Object> attributeList,Class<T> objClass);
	
	/**
	 * 通过hql查询总条数
	 * @intruduction
	 * @param HQL
	 * @param attributeList
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年9月7日下午3:38:12
	 */
	public long queryCountByHql(String HQL,List<Object> attributeList);

	/**
	 * 查询带分页 不过滤系统编码
	 * @param page 开始页
	 * @param rows 每页显示条数
	 * @param criterionsList 条件
	 * @param order 排序
	 * @param objClass 类.class
	 * @return 分页实体类
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:19:04
	 */
	<T>Pager queryList(int page, int rows, List<Criterion> criterionsList,Order order,Class<T> objClass);

	/**
	 * 查询不带分页 不过滤系统编码
	 * @param criterionsList 条件
	 * @param order 排序
	 * @param objClass 类.class
	 * @return 分页实体类
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:19:04
	 */
	<T>List<T> queryList(List<Criterion> criterionsList,Order order,Class<T> objClass);
}
