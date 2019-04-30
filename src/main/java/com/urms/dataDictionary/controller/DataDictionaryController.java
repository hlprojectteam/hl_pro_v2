package com.urms.dataDictionary.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.common.base.controller.BaseController;
import com.common.utils.cache.Cache;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.dataDictionary.module.Category;
import com.urms.dataDictionary.module.CategoryAttribute;
import com.urms.dataDictionary.service.IDataDictionaryService;
import com.urms.dataDictionary.vo.CategoryAttributeVo;
import com.urms.dataDictionary.vo.CategoryVo;

@Controller
@RequestMapping("/urms")
public class DataDictionaryController extends BaseController{
	
	@Autowired
	public IDataDictionaryService dataDictionaryServiceImpl;
	
	/**
	 * 说明：数据字典列表页面
	 * 输入：@param httpSession
	 * 输入：@param response
	 * 输入：@param categoryVo
	 * 输入：@return
	 * 输出：String
	 * 创建时间:2016-1-5 下午5:12:04
	 */
	@RequestMapping(value="/dataDictionary_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,CategoryVo categoryVo) {
		return "/page/urms/dataDictionary/dataDictionary_list";
	}
	
	/**
	 * @intruduction 数据字典目录列表
	 * @param request
	 * @param response
	 * @param categoryVo
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:35:24
	 */
	@RequestMapping(value="/category_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,CategoryVo categoryVo,Integer page,Integer rows) {
		Pager pager = this.dataDictionaryServiceImpl.queryEntityCategoryList(page, rows, categoryVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"categoryAttributes"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 字典列表
	 * @param request
	 * @param response
	 * @param id
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:35:51
	 */
	@RequestMapping(value="/categoryAttr_load") 
	public void attrLoad(HttpServletRequest request,HttpServletResponse response,String id,Integer page,Integer rows) {
		Pager pager = this.dataDictionaryServiceImpl.queryEntityCategoryAttrList(page, rows, id);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"category"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 编辑数据字典目录树
	 * @param request
	 * @param id
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:37:38
	 */
	@RequestMapping(value="/category_edit") 
	public String edit(HttpServletRequest request,CategoryVo categoryVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(categoryVo.getId())){
			Category category = this.dataDictionaryServiceImpl.getEntityById(Category.class,categoryVo.getId());
			BeanUtils.copyProperties(category,categoryVo);
			request.setAttribute("categoryVo", categoryVo);
		}else{
			request.setAttribute("categoryVo", categoryVo);
		}
		return "/page/urms/dataDictionary/category_edit";
	}
	
	/**
	 * @intruduction 编辑字典
	 * @param request
	 * @param id
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:37:06
	 */
	@RequestMapping(value="/categoryAttr_edit") 
	public String editAttr(HttpServletRequest request,CategoryAttributeVo categoryAttributeVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(categoryAttributeVo.getId())){
			CategoryAttribute categoryAttribute = this.dataDictionaryServiceImpl.getEntityById(CategoryAttribute.class,categoryAttributeVo.getId());
			BeanUtils.copyProperties(categoryAttribute,categoryAttributeVo);
			request.setAttribute("categoryAttributeVo", categoryAttributeVo);
		}else{
			request.setAttribute("categoryAttributeVo", categoryAttributeVo);
		}
		return "/page/urms/dataDictionary/categoryAttribute_edit";
	}
	
	/**
	 * @intruduction 保存or更新 类型树
	 * @param httpSession
	 * @param response
	 * @param categoryVo
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:38:26
	 */
	@RequestMapping(value="/category_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,@ModelAttribute CategoryVo categoryVo) {
		JsonObject json = new JsonObject();
		try {
			Category category = new Category();
			BeanUtils.copyProperties(categoryVo,category);
			this.dataDictionaryServiceImpl.saveOrUpdateCategory(category);
			json.addProperty("result", true);
			json.addProperty("id", category.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @intruduction 加载数据字典树结构
	 * @param response
	 * @param id
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:38:45
	 */
	@RequestMapping(value="/category_loadTree") 
	public void loadTree(HttpServletResponse response,String id) {
		StringBuffer tree = new StringBuffer();
		tree.append("[");
		if(StringUtils.isBlank(id)){
			id = "0";//初始化菜单 根节点为 0
			Category c = this.dataDictionaryServiceImpl.getEntityById(Category.class,id);
			tree.append("{");
			tree.append("id:'"+c.getId()+"',");
			tree.append("pId:'',");
			tree.append("name:'"+c.getCategoryName()+"',");
			tree.append("categoryType:'"+c.getCategoryType()+"',");
			tree.append("open:true");
			tree.append("},");
		}
		List<Category> categoryList = this.dataDictionaryServiceImpl.queryEntityListByPId(id);
		for (int i = 0; i < categoryList.size(); i++) {
			Category c = categoryList.get(i);
			tree.append("{");
			tree.append("id:'"+c.getId()+"',");
			tree.append("pId:'"+c.getpId()+"',");
			tree.append("name:'"+c.getCategoryName()+"',");
			tree.append("categoryType:'"+c.getCategoryType()+"'");
			if(c.getIsLeaf()==0)
				tree.append(",isParent:true");
			tree.append("},");
		}
		tree.deleteCharAt(tree.toString().length()-1);
		tree.append("]");
		logger.info("输出树结构:"+tree.toString());
		this.print(tree.toString());
	}
	
	/**
	 * @intruduction 删除目录树
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月6日上午9:39:08
	 */
	@RequestMapping(value="/category_delete") 
	public void deleteTree(HttpServletResponse response,String ids) {
		this.dataDictionaryServiceImpl.deleteTree(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}

	/*
	 * 保存字典
	 */
	@RequestMapping(value="/categoryAttr_saveOrUpdate") 
	public void categoryAttrSaveOrUpdate(HttpServletResponse response,CategoryAttributeVo categoryAttributeVo) {
		JsonObject json = new JsonObject();
		try {
			CategoryAttribute categoryAttribute = new CategoryAttribute();
			BeanUtils.copyProperties(categoryAttributeVo,categoryAttribute);
			this.dataDictionaryServiceImpl.saveOrUpdateCategoryAttr(categoryAttribute);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @intruduction 删除字典
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月13日上午9:47:40
	 */
	@RequestMapping(value="/categoryAttr_delete") 
	public void categoryAttrDelete(HttpServletResponse response,String ids) {
		this.dataDictionaryServiceImpl.deleteCategoryAttr(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 加载树结构
	 * @param response
	 * @param categoryCode
	 * @param id
	 * @author Mr.Wang
	 * @Date 2016年1月6日下午3:08:20
	 */
	@RequestMapping(value="/category_tree") 
	public void categoryTree(HttpServletResponse response,String categoryCode,String id) {
		StringBuffer tree = new StringBuffer();
		tree.append("[");
		if(StringUtils.isBlank(id)){
			Category category = this.dataDictionaryServiceImpl.getCategoryByCode(categoryCode);
			tree.append("{");
			tree.append("id:'"+category.getId()+"',");
			tree.append("pId:'',");
			tree.append("name:'"+category.getCategoryName()+"',");
			tree.append("categoryType:'"+category.getCategoryType()+"',");
			tree.append("open:true");
			tree.append("},");
			id = category.getId();
		}
		List<Category> categoryList = this.dataDictionaryServiceImpl.queryEntityListByPId(id);
		for (int i = 0; i < categoryList.size(); i++) {
			Category c = categoryList.get(i);
			tree.append("{");
			tree.append("id:'"+c.getId()+"',");
			tree.append("pId:'"+c.getpId()+"',");
			tree.append("name:'"+c.getCategoryName()+"',");
			tree.append("categoryType:'"+c.getCategoryType()+"'");
			if(c.getIsLeaf()==0)
				tree.append(",isParent:true");
			tree.append("},");
		}
		tree.deleteCharAt(tree.toString().length()-1);
		tree.append("]");
		logger.info("输出树结构:"+tree.toString());
		this.print(tree.toString());
	}
	
	/**
	 * @intruduction 加载树结构 包括父节点 行政区域树 402880ed54f001de0154f0157c710004
	 * @param response
	 * @param id
	 * @param id 父亲节点id
	 * @author Mr.Wang
	 * @Date 2016年1月6日下午3:08:20
	 */
//	@RequestMapping(value="/category_areaTree") 
//	public void categoryAreaTree(HttpServletResponse response,String id) {
//		StringBuffer tree = new StringBuffer();
//		tree.append("[");
//		if(StringUtils.isBlank(id)){
//			Category c = dataDictionaryServiceImpl.getCategoryByCode(Common.MapCode);
//			id = c.getId();
//			tree.append("{");
//			tree.append("id:'"+c.getId()+"',");
//			tree.append("pId:'',");
//			tree.append("name:'"+c.getCategoryName()+"',");
//			tree.append("categoryCode:'"+c.getCategoryCode()+"',");
//			tree.append("categoryType:'"+c.getCategoryType()+"',");
//			tree.append("open:true");
//			tree.append("},");
//		}
//		List<Category> categoryList = this.dataDictionaryServiceImpl.queryEntityListByPId(id);
//		for (int i = 0; i < categoryList.size(); i++) {
//			Category c = categoryList.get(i);
//			tree.append("{");
//			tree.append("id:'"+c.getId()+"',");
//			tree.append("pId:'"+c.getpId()+"',");
//			tree.append("name:'"+c.getCategoryName()+"',");
//			tree.append("categoryCode:'"+c.getCategoryCode()+"',");
//			tree.append("categoryType:'"+c.getCategoryType()+"'");
//			if(c.getIsLeaf()==0)
//				tree.append(",isParent:true");
//			tree.append("},");
//		}
//		tree.deleteCharAt(tree.toString().length()-1);
//		tree.append("]");
//		logger.info("输出树结构:"+tree.toString());
//		this.print(tree.toString());
//	}
	
	@RequestMapping(value="/dataDict_list") 
	public String dataDictList(HttpSession httpSession,HttpServletResponse response,String winName){
		this.getRequest().setAttribute("winName",winName);
		return "/page/urms/dataDictionary/dataDict_list";
	}
	
	@RequestMapping(value="/dataCategory_load") 
	public void dataCategoryload(HttpServletRequest request,HttpServletResponse response,CategoryVo categoryVo,Integer page,Integer rows) {
		Pager pager = this.dataDictionaryServiceImpl.queryDataCategoryList(page, rows,categoryVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"categoryAttributes"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	
	/**
	 * @intruduction 数据字典翻译 通过字典名称he字典key获得 value
	 * @param response
	 * @param dict 字典名称
	 * @param key 字典key
	 * @author Mr.Wang
	 * @Date 2016年1月13日上午9:10:01
	 */
	@RequestMapping(value="/changeDataDictByKey") 
	public void changeDataDictByKey(HttpServletResponse response,String dict,String key){
		String value = "";
		for (CategoryAttribute ca : Cache.getDictByCode.get(dict)) {
			if(ca.getAttrKey().equals(key)){
				value = ca.getAttrValue();
				break;
			}			
		}
		this.print(value);
	}

	/**
	 * @intruduction 根据行政区划编码获取对应的行政区划名称
	 * @param response
	 * @author Mr.Wang
	 * @Date 2016年1月13日上午9:10:01
	 */
	@RequestMapping(value="/changeAreaNameByAreaCode")
	public void changeAreaNameByAreaCode(HttpServletResponse response,String areaCode){
		String value = "";
		if(StringUtils.isNotBlank(areaCode) ){
			if(Cache.getDict.get(areaCode) != null){
				value = Cache.getDict.get(areaCode);
			}
		}
		this.print(value);
	}

	/**
	 * @intruduction 获得字典值
	 * @param response
	 * @param dict
	 * @author Mr.Wang
	 * @Date 2016年3月31日下午8:19:24
	 */
	@RequestMapping(value="/getDataDict") 
	public void getDataDict(HttpServletResponse response,String dict){
		StringBuffer keySb = new StringBuffer();
		StringBuffer valueSb = new StringBuffer();
		for (CategoryAttribute ca : Cache.getDictByCode.get(dict)) {
			keySb.append(ca.getAttrKey()+",");
			valueSb.append(ca.getAttrValue()+",");
		}
		JSONObject json = new JSONObject();
		json.put("keys", keySb.deleteCharAt(keySb.length()-1).toString());
		json.put("values", valueSb.deleteCharAt(valueSb.length()-1).toString());
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 字典目录翻译
	 * @param response
	 * @param dict
	 * @author Mr.Wang
	 * @Date 2016年6月1日下午2:16:19
	 */
	@RequestMapping(value="/getDict") 
	public void getDict(HttpServletResponse response,String dict){
		JSONObject json = new JSONObject();
		json.put("dict", Cache.getDict.get(dict));
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 字典目录选择页面
	 * @param winName
	 * @param categoryCode
	 * @param id
	 * @param name
	 * @param selectNum
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年6月1日下午2:15:59
	 */
	@RequestMapping(value="/category_choose") 
	public String chooseCategory(String winName,String categoryCode,String id,String name,String selectNum){
		this.getRequest().setAttribute("winName",winName);
		this.getRequest().setAttribute("categoryCode",categoryCode);
		this.getRequest().setAttribute("id", id);
		this.getRequest().setAttribute("name", name);
		this.getRequest().setAttribute("selectNum", selectNum);
		return "/page/urms/dataDictionary/category_choose";
	}
	
	/**
	 * @intruduction 加载字典目录树结构
	 * @param response
	 * @param categoryCode
	 * @param id 父亲节点id
	 * @author Mr.Wang
	 * @Date 2016年1月6日下午3:08:20
	 */
	@RequestMapping(value="/category_categoryTree") 
	public void queryCategoryTree(HttpServletResponse response,String categoryCode,String id) {
		StringBuffer tree = new StringBuffer();
		tree.append("[");
		if(StringUtils.isBlank(id)){
			Category c = this.dataDictionaryServiceImpl.getCategoryByCode(categoryCode);
			tree.append("{");
			tree.append("id:'"+c.getId()+"',");
			tree.append("pId:'',");
			tree.append("name:'"+c.getCategoryName()+"',");
			tree.append("categoryCode:'"+c.getCategoryCode()+"',");
			tree.append("categoryType:'"+c.getCategoryType()+"',");
			tree.append("open:true");
			tree.append("},");
			id = c.getId();
		}
		List<Category> categoryList = this.dataDictionaryServiceImpl.queryEntityListByPId(id);
		for (int i = 0; i < categoryList.size(); i++) {
			Category c = categoryList.get(i);
			tree.append("{");
			tree.append("id:'"+c.getId()+"',");
			tree.append("pId:'"+c.getpId()+"',");
			tree.append("name:'"+c.getCategoryName()+"',");
			tree.append("categoryCode:'"+c.getCategoryCode()+"',");
			tree.append("categoryType:'"+c.getCategoryType()+"'");
			if(c.getIsLeaf()==0)
				tree.append(",isParent:true");
			tree.append("},");
		}
		tree.deleteCharAt(tree.toString().length()-1);
		tree.append("]");
		logger.info("输出树结构:"+tree.toString());
		this.print(tree.toString());
	}
	
	/**
	 * @intruduction 检查字典目录是否唯一
	 * @param response
	 * @param categoryVo
	 * @author Mr.Wang
	 * @Date 2016年7月13日下午5:54:31
	 */
	@RequestMapping(value="/category_checkCategoryCode") 
	public void checkCategoryCode(HttpServletResponse response,CategoryVo categoryVo) {
		if(StringUtils.isBlank(categoryVo.getId())){//新增情况下
			Category category = this.dataDictionaryServiceImpl.getCategoryByCode(categoryVo.getCategoryCode());
			if(category!=null)
				this.print(false);
			else
				this.print(true);
		}else{//修改情况下
			Category category= this.dataDictionaryServiceImpl.getCategoryByCode(categoryVo.getCategoryCode());
			if(category!=null){
				this.print(true);//已经存在				
			}else{
				Category c = this.dataDictionaryServiceImpl.getCategoryByCode(categoryVo.getCategoryCode());
				if(c!=null)
					this.print(false);
				else
					this.print(true);
			}
		}
	}
	
	/**
	 * 通过字典目录编码 获得下级所有字典目录
	 * @intruduction
	 * @param response
	 * @param categoryCode
	 * @author Mr.Wang
	 * @Date 2016年11月17日下午4:16:24
	 */
	@RequestMapping(value="/queryCategoryByCode") 
	public void checkCategoryCode(HttpServletResponse response,String categoryCode) {
		Category category = this.dataDictionaryServiceImpl.getCategoryByCode(categoryCode);
		String json = "";
		if(category!=null){
			List<Category> categoryList = this.dataDictionaryServiceImpl.queryEntityListByPId(category.getId());
			JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
			config.setExcludes(new String[]{"categoryAttributes"});  //只要设置这个数组，指定过滤哪些字段。     
			json = JSONArray.fromObject(categoryList,config).toString();
		}
		this.print(json);
	}

	/**
	 * 更新行政区划json文件
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/refreshAreaFile")
	public void refreshAreaFile(HttpServletRequest request, HttpServletResponse response) {
		this.dataDictionaryServiceImpl.getAreaCodeAndName();
		this.print("update success!");
	}
	
	public IDataDictionaryService getDataDictionaryServiceImpl() {
		return dataDictionaryServiceImpl;
	}

	public void setDataDictionaryServiceImpl(
			IDataDictionaryService dataDictionaryServiceImpl) {
		this.dataDictionaryServiceImpl = dataDictionaryServiceImpl;
	}

}