package com.urms.dataDictionary.service;



import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.dataDictionary.module.Category;
import com.urms.dataDictionary.module.CategoryAttribute;
import com.urms.dataDictionary.vo.CategoryVo;

public interface IDataDictionaryService extends IBaseService{
	
	/**
	 * @intruduction 目录列表
	 * @param page
	 * @param rows
	 * @param categoryVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:41:00
	 */
	public Pager queryEntityCategoryList(int page,int rows,CategoryVo categoryVo);
	
	/**
	 * @intruduction 字典列表 categoryId：字典目录id
	 * @param page
	 * @param rows
	 * @param categoryId
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:41:18
	 */
	public Pager queryEntityCategoryAttrList(int page,int rows,String categoryId);
	
	/**
	 * 
	 * @方法：@param categoryId
	 * @方法：@return
	 * @描述：获取所有属性项
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月1日
	 */
	public List<CategoryAttribute> queryEntityCategoryAttrListALL(String categoryId);
	
	/**
	 * @intruduction 凭父ID获得所有实体
	 * @param pid
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午11:06:08
	 */
	public List<Category> queryEntityListByPId(String pid);

	/**
	 * @intruduction 字典目录表保存or更新
	 * @param category
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午10:48:48
	 */
	public void saveOrUpdateCategory(Category category);
	
	/**
	 * @intruduction 字典保存or更新
	 * @param category
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午10:48:48
	 */
	public void saveOrUpdateCategoryAttr(CategoryAttribute categoryAttribute);
	/**
	 * @intruduction 删除目录树
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午11:07:03
	 */
	public void deleteTree(String ids);
	
	/**
	 * @intruduction 删除字典
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午11:07:20
	 */
	public void deleteCategoryAttr(String ids);
	
	/**
	 * @intruduction 系统启动时加载数据字典
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午11:07:30
	 */
	public void getDictByCode();
	
	/**
	 * @intruduction 凭code查询数据字典目录
	 * @param code
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午11:07:36
	 */
	public Category getCategoryByCode(String code);
	
	/**
	 * @intruduction 获得所有数据字典
	 * @param page
	 * @param rows
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年3月29日下午5:00:17
	 */
	public Pager queryDataCategoryList(int page,int rows,CategoryVo categoryVo);

	/**
	 * @intruduction 行政区划四级联动所需数据
	 * @author xuezb
	 * @Date 2018年8月6日
	 */
	void getAreaCodeAndName();
	
	
	/**
	 * 
	 * @方法：@param category
	 * @方法：@return
	 * @描述：获取字典下选项最大的选项的key，如找到性别下“女”的key是最大值，为2
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月1日
	 */
	public String findMaxCategoryAttributesKey(Category category);
	
	/**
	 * 
	 * @方法：@param category
	 * @方法：@return
	 * @描述：获取字典下选项最大的order
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月1日
	 */
	public Integer findMaxCategoryAttributesOrder(Category category);
	
	/**
	 * 
	 * @方法：@param CategoryCode 字典Code,如“sex”
	 * @方法：@param CategoryAttributesValue 新加的字典选项value，如"中性"
	 * @方法：@return 新加的字典选项key
	 * @描述：如性别要加一个值，调用  addCategoryAttributesByCode("sex","中性")  返回 key 为3
	 * @return
	 * @author: qinyongqian
	 * @date:2019年4月30日
	 */
	public String updateCategoryAttributesByCode(String CategoryCode,String CategoryAttributesValue);

	/**
	 * @intruduction 行政区划五级联动所需数据
	 * @author xuezb
	 * @Date 2018年11月21日
	 */
	//void getAreaCodeAndName5();
}
