package com.urms.dataDictionary.service.impl;



import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.cache.Cache;
import com.common.utils.helper.Pager;
import com.urms.dataDictionary.dao.IDataDictionaryDao;
import com.urms.dataDictionary.module.Category;
import com.urms.dataDictionary.module.CategoryAttribute;
import com.urms.dataDictionary.service.IDataDictionaryService;
import com.urms.dataDictionary.vo.CategoryVo;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Repository("dataDictionaryServiceImpl")
public class DataDictionaryServiceImpl extends BaseServiceImpl implements IDataDictionaryService{
	
	@Autowired
	public IDataDictionaryDao dataDictionaryDaoImpl;

	public IDataDictionaryDao getDataDictionaryDaoImpl() {
		return dataDictionaryDaoImpl;
	}

	public void setDataDictionaryDaoImpl(IDataDictionaryDao dataDictionaryDaoImpl) {
		this.dataDictionaryDaoImpl = dataDictionaryDaoImpl;
	}

	@Override
	public void saveOrUpdateCategory(Category category){
		if(StringUtils.isBlank(category.getId()))
			this.saveCategory(category);
		else
			this.update(category);
	}
	
	@Override
	public void saveOrUpdateCategoryAttr(CategoryAttribute categoryAttribute){
		if(StringUtils.isBlank(categoryAttribute.getId()))
			this.save(categoryAttribute);			
		else
			this.update(categoryAttribute);	
		refreshCategory(categoryAttribute);//刷新数据字典
	}
	
	/*
	 * 保存
	 */
	public void saveCategory(Category category){
		Category pCategory = this.getEntityById(Category.class,category.getpId());
		category.setIsLeaf(1);//叶子节点
		if(StringUtils.isNotBlank(pCategory.getpIds())){
			category.setpIds(pCategory.getpIds()+"/"+pCategory.getId());			
			category.setLevel(category.getpIds().split("/").length);//层数
		}else{
			category.setpIds(pCategory.getId());	
			category.setLevel(1);//层数
		}
		if(StringUtils.isNotBlank(pCategory.getpNames()))
			category.setpNames(pCategory.getpNames()+"/"+pCategory.getCategoryName());			
		else
			category.setpNames(pCategory.getCategoryName());	
		dataDictionaryDaoImpl.save(category);
		if(pCategory.getIsLeaf()==1){
			pCategory.setIsLeaf(0);
			dataDictionaryDaoImpl.update(pCategory);
		}
	}

	public Pager queryEntityCategoryList(int page,int rows,CategoryVo categoryVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(categoryVo.getId())){
			if(categoryVo.getId()!="0"){
				//criterionsList.add(Restrictions.like("pIds", "%"+categoryVo.getId()+"%"));	// Mr.Wang 只查下一级的数据，2018年7月31日 10:45:19			
				criterionsList.add(Restrictions.eq("pId", categoryVo.getId()));
			}
		}else{
			criterionsList.add(Restrictions.eq("pId", "0"));		
		}
		if(StringUtils.isNotBlank(categoryVo.getCategoryType()))
			criterionsList.add(Restrictions.eq("categoryType", categoryVo.getCategoryType()));	
		if(StringUtils.isNotBlank(categoryVo.getCategoryName()))
			criterionsList.add(Restrictions.like("categoryName", "%"+categoryVo.getCategoryName()+"%"));		
		if(StringUtils.isNotBlank(categoryVo.getCategoryCode()))
			criterionsList.add(Restrictions.like("categoryCode", "%"+categoryVo.getCategoryCode()+"%"));	
		return dataDictionaryDaoImpl.queryList(page, rows, criterionsList, Order.desc("createTime") ,Category.class);
	}
	
	public Pager queryDataCategoryList(int page,int rows,CategoryVo categoryVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("categoryType", "2"));		
		if(StringUtils.isNotBlank(categoryVo.getCategoryName()))
			criterionsList.add(Restrictions.like("categoryName", "%"+categoryVo.getCategoryName()+"%"));		
		if(StringUtils.isNotBlank(categoryVo.getCategoryCode()))
			criterionsList.add(Restrictions.like("categoryCode", "%"+categoryVo.getCategoryCode()+"%"));	
		return dataDictionaryDaoImpl.queryList(page, rows, criterionsList, Order.desc("createTime") ,Category.class);
	}
	
	public Pager queryEntityCategoryAttrList(int page,int rows,String categoryId){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(categoryId)){
				criterionsList.add(Restrictions.eq("category.id", categoryId));
		}
		return dataDictionaryDaoImpl.queryList(page, rows, criterionsList, Order.asc("order") ,CategoryAttribute.class);
	}
	
	public List<CategoryAttribute> queryEntityCategoryAttrList(String categoryId){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(categoryId)){
				criterionsList.add(Restrictions.eq("category.id", categoryId));
		}
		return dataDictionaryDaoImpl.queryList(criterionsList, Order.asc("order") ,CategoryAttribute.class);
	}
	@Override
	public List<CategoryAttribute> queryEntityCategoryAttrListALL(String categoryId) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(categoryId)){
				criterionsList.add(Restrictions.eq("category.id", categoryId));
		}
		return dataDictionaryDaoImpl.queryList(criterionsList, Order.asc("order") ,CategoryAttribute.class);
	}
	
	public List<Category> queryEntityListByPId(String pid){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("pId", pid));
		return dataDictionaryDaoImpl.queryList(criterionsList, Order.asc("order") ,Category.class);
	}
	
	public List<Category> queryEntityList(CategoryVo categoryVo){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(categoryVo.getpId()))
			criterionsList.add(Restrictions.eq("pId", categoryVo.getpId()));
		if(categoryVo.getLevel()!=null)
			criterionsList.add(Restrictions.eq("level", categoryVo.getLevel()));
		return dataDictionaryDaoImpl.queryList(criterionsList,Order.asc("order"), Category.class);
	}
	
	
	@Override
	public void deleteTree(String ids) {
		String[] idsZ = ids.split(",");
		for (int i = 0; i < idsZ.length; i++) {
			Category category = this.getEntityById(Category.class, idsZ[i]);
			String pid = category.getpId();//父id
			//删除子项
			if(category.getCategoryAttributes()!=null){
				for (CategoryAttribute ca : category.getCategoryAttributes()) {
					dataDictionaryDaoImpl.delete(ca);
				}
			}
			dataDictionaryDaoImpl.delete(category);
			//修改父页面
			CategoryVo categoryVo = new CategoryVo();
			categoryVo.setpId(pid);
			List<Category> list = this.queryEntityList(categoryVo);
			if(list.isEmpty()){
				Category pcategory = this.getEntityById(Category.class,pid);
				pcategory.setIsLeaf(1);
				dataDictionaryDaoImpl.update(pcategory);
			}
		}
	}

	/**
	 * 删除字典
	 */
	public void deleteCategoryAttr(String ids){
		String[] idsZ = ids.split(",");
		for (int i = 0; i < idsZ.length; i++) {
			CategoryAttribute categoryAttr = this.getEntityById(CategoryAttribute.class, idsZ[i]);
			dataDictionaryDaoImpl.delete(categoryAttr);
		}
	}
	
	/**
	 * 加载数据字典
	 */
	public void getDictByCode(){
		Cache.getDictByCode.clear();//先清除
		Cache.getDictByCodeMap.clear();//先清除
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("categoryType", "2"));//数据字典
		List<Category> categoryList = dataDictionaryDaoImpl.queryList(criterionsList, null ,Category.class);
		for (int i = 0; i < categoryList.size(); i++) {
			Category category = categoryList.get(i);
			Map<String,String> map = new LinkedHashMap<String,String>();
			for (CategoryAttribute ca : category.getCategoryAttributes()) {  //使用了延时加载 故不能删掉
				map.put(ca.getAttrKey(), ca.getAttrValue());
			} 
			Cache.getDictByCode.put(category.getCategoryCode(), category.getCategoryAttributes());//为了带上默认值
			Cache.getDictByCodeMap.put(category.getCategoryCode(), map);
		}
		//字典目录
		Cache.getDict.clear();//先清除
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.or(Restrictions.eq("categoryType", "1"),Restrictions.eq("categoryType", "3")));//字典目录
		List<Category> categoryL = dataDictionaryDaoImpl.queryList(criterions, null ,Category.class);
		for (int i = 0; i < categoryL.size(); i++) {
			Category category = categoryL.get(i);
			Cache.getDict.put(category.getCategoryCode(), category.getCategoryName());
		}
	}

	/**
	 * @intruduction 刷新字典缓存
	 * @author Mr.Wang
	 * @Date 2016年1月13日上午10:10:59
	 */
	public void refreshCategory(CategoryAttribute categoryAttribute){
		Category category = this.getEntityById(Category.class, categoryAttribute.getCategory().getId());
		Map<String,String> dictMap = Cache.getDictByCodeMap.get(category.getCategoryCode());
		if(dictMap!=null)
			dictMap.clear();
		Map<String,String> map = new LinkedHashMap<String,String>();
		for (CategoryAttribute ca : category.getCategoryAttributes()) {//使用了延时加载 故不能删掉
			map.put(ca.getAttrKey(), ca.getAttrValue());
		} 
		Cache.getDictByCode.put(category.getCategoryCode(), category.getCategoryAttributes());//带默认值
		Cache.getDictByCodeMap.put(category.getCategoryCode(), map);
	}
	
	/**
	 * 凭code查询数据字典目录
	 */
	public Category getCategoryByCode(String code){
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		criterionsList.add(Restrictions.eq("categoryCode", code));
		List<Category> categoryList = dataDictionaryDaoImpl.queryList(criterionsList, null ,Category.class);
		Category category = null;
		if(!categoryList.isEmpty())
			category = categoryList.get(0);
		return category;
	}

//	@Override
//	public void getAreaCodeAndName() {
//		String province = "";
//		String city = "";
//		String district = "";
//		String street = "";
//
//		List<Object> p0 = new ArrayList<Object>();
//		p0.add("area");
//		String hql0 = "from Category where categoryCode = ?";
//		List<Category> list0 = this.dataDictionaryDaoImpl.queryEntityHQLList(hql0, p0, Category.class);
//		for (Category category0 : list0) {
//			List<Object> p1 = new ArrayList<Object>();
//			p1.add(category0.getId());
//			String hql1 = "from Category where pId = ?";
//			List<Category> list1 = this.dataDictionaryDaoImpl.queryEntityHQLList(hql1, p1, Category.class);
//			for (Category category1 : list1) {
//				province = province + "{\"id\":\"" + category1.getCategoryCode() + "\",\"name\":\"" + category1.getCategoryName() + "\"},";	//省级
//
//				List<Object> p2 = new ArrayList<Object>();
//				p2.add(category1.getId());
//				String hql2 = "from Category where pId = ?";
//				List<Category> list2 = this.dataDictionaryDaoImpl.queryEntityHQLList(hql2, p2, Category.class);
//				for (Category category2 : list2) {
//					city = city + "{\"id\":\"" + category2.getCategoryCode() + "\",\"name\":\"" + category2.getCategoryName() + "\",\"pid\":\"" + category1.getCategoryCode() + "\"},";	//市级
//
//					List<Object> p3 = new ArrayList<Object>();
//					p3.add(category2.getId());
//					String hql3 = "from Category where pId = ?";
//					List<Category> list3 = this.dataDictionaryDaoImpl.queryEntityHQLList(hql3, p3, Category.class);
//					for (Category category3 : list3) {
//						district = district + "{\"id\":\"" + category3.getCategoryCode() + "\",\"name\":\"" + category3.getCategoryName() + "\",\"pid\":\"" + category2.getCategoryCode() + "\"},";	//区级
//
//						List<Object> p4 = new ArrayList<Object>();
//						p4.add(category3.getId());
//						String hql4 = "from Category where pId = ?";
//						List<Category> list4 = this.dataDictionaryDaoImpl.queryEntityHQLList(hql4, p4, Category.class);
//						for (Category category4 : list4) {
//							street = street + "{\"id\":\"" + category4.getCategoryCode() + "\",\"name\":\"" + category4.getCategoryName() + "\",\"pid\":\"" + category3.getCategoryCode() + "\"},";	//镇级
//						}
//					}
//				}
//			}
//		}
//
//		//去掉最后一个逗号
//		province = province.substring(0, province.length()-1);
//		city = city.substring(0, city.length()-1);
//		district = district.substring(0, district.length()-1);
//		street = street.substring(0, street.length()-1);
//
//		String conent = "var province = [" + province + "]" + " \n "
//				+ "var city = [" + city + "]" + " \n "
//				+ "var district =[" + district + "]" + " \n "
//				+ "var street =[" + street + "]";
//
//		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
//		ServletContext servletContext = webApplicationContext.getServletContext();
//		String path = servletContext.getRealPath("/common/chooseAdress/js/dataJson.js");
//		this.clearInfoForFile(path);	//清空 文件内容
//		this.fileAdditionalContent(path,conent);	//追加 文件内容
//	}

	@Override
	public void getAreaCodeAndName() {
		String province = "";
		String city = "";
		String district = "";
		String street = "";
		String village = "";

		List<Object> p0 = new ArrayList<Object>();
		p0.add("area");
		String hql0 = "from Category where categoryCode = ?";
		List<Category> list0 = this.dataDictionaryDaoImpl.queryEntityHQLList(hql0, p0, Category.class);
		for (Category category0 : list0) {
			List<Object> p1 = new ArrayList<Object>();
			p1.add(category0.getId());
			String hql1 = "from Category where pId = ?";
			List<Category> list1 = this.dataDictionaryDaoImpl.queryEntityHQLList(hql1, p1, Category.class);
			for (Category category1 : list1) {
				province = province + "{\"id\":\"" + category1.getCategoryCode() + "\",\"name\":\"" + category1.getCategoryName() + "\"},";	//省级

				List<Object> p2 = new ArrayList<Object>();
				p2.add(category1.getId());
				String hql2 = "from Category where pId = ?";
				List<Category> list2 = this.dataDictionaryDaoImpl.queryEntityHQLList(hql2, p2, Category.class);
				for (Category category2 : list2) {
					city = city + "{\"id\":\"" + category2.getCategoryCode() + "\",\"name\":\"" + category2.getCategoryName() + "\",\"pid\":\"" + category1.getCategoryCode() + "\"},";	//市级

					List<Object> p3 = new ArrayList<Object>();
					p3.add(category2.getId());
					String hql3 = "from Category where pId = ?";
					List<Category> list3 = this.dataDictionaryDaoImpl.queryEntityHQLList(hql3, p3, Category.class);
					for (Category category3 : list3) {
						district = district + "{\"id\":\"" + category3.getCategoryCode() + "\",\"name\":\"" + category3.getCategoryName() + "\",\"pid\":\"" + category2.getCategoryCode() + "\"},";	//区级

						List<Object> p4 = new ArrayList<Object>();
						p4.add(category3.getId());
						String hql4 = "from Category where pId = ?";
						List<Category> list4 = this.dataDictionaryDaoImpl.queryEntityHQLList(hql4, p4, Category.class);
						for (Category category4 : list4) {
							street = street + "{\"id\":\"" + category4.getCategoryCode() + "\",\"name\":\"" + category4.getCategoryName() + "\",\"pid\":\"" + category3.getCategoryCode() + "\"},";	//镇级

							List<Object> p5 = new ArrayList<Object>();
							p5.add(category4.getId());
							String hql5 = "from Category where pId = ?";
							List<Category> list5 = this.dataDictionaryDaoImpl.queryEntityHQLList(hql5, p5, Category.class);
							for (Category category5 : list5) {
								village = village + "{\"id\":\"" + category5.getCategoryCode() + "\",\"name\":\"" + category5.getCategoryName() + "\",\"pid\":\"" + category4.getCategoryCode() + "\"},";	//村级
							}
						}
					}
				}
			}
		}

		//去掉最后一个逗号
		province = province.substring(0, province.length()-1);
		city = city.substring(0, city.length()-1);
		district = district.substring(0, district.length()-1);
		street = street.substring(0, street.length()-1);
		village = village.substring(0, village.length()-1);

		String conent = "var province = [" + province + "]" + " \n "
				+ "var city = [" + city + "]" + " \n "
				+ "var district =[" + district + "]" + " \n "
				+ "var street =[" + street + "]" + " \n "
				+ "var village =[" + village + "]";;

		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		String path = servletContext.getRealPath("/common/plugins/chooseAdress/js/dataJson.js");
		this.clearInfoForFile(path);	//清空 文件内容
		this.fileAdditionalContent(path,conent);	//追加 文件内容
	}

	/**
	 * 清空文件内容
	 * @param fileName
	 * @return
	 * @author xuezb
	 * @Date 2018年8月6日
	 */
	private void clearInfoForFile(String fileName) {
		File file =new File(fileName);
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter =new FileWriter(file);
			fileWriter.write("");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 追加文件内容
	 * @param file
	 * @param conent
	 * @return
	 * @author xuezb
	 * @Date 2018年8月6日
	 */
	private void fileAdditionalContent(String file, String conent) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true),"UTF-8"));
			out.write(conent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
