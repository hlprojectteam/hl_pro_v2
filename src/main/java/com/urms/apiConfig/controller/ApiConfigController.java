package com.urms.apiConfig.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.common.base.controller.BaseController;
import com.common.utils.cache.Cache;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.apiConfig.module.ApiConfig;
import com.urms.apiConfig.service.IApiConfigService;
import com.urms.apiConfig.vo.ApiConfigVo;
import com.urms.dataDictionary.module.CategoryAttribute;

/**
 * API综合接口配置
 * @author Mr.Wang
 * @date 2018-07-25 17:08:21
 */
@Controller
@RequestMapping("/urms")
public class ApiConfigController extends BaseController{
	
	@Autowired
	public IApiConfigService apiConfigServiceImpl;
	
	/**
	 * @intruduction API列表页面
	 * @param httpSession
	 * @param response
	 * @return
	 * @author Mr.Wang
	 * @Date 2018年7月24日 15:53:41
	 */
	@RequestMapping(value="/apiConfig_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		List<CategoryAttribute> caList = new ArrayList<CategoryAttribute>();
		Set<CategoryAttribute> cas = Cache.getDictByCode.get("API_Type");
		if(!cas.isEmpty()){
			for (CategoryAttribute ca : cas) {
				CategoryAttribute categoryAttribute = new CategoryAttribute();
				if(ca.getAttrValue()!=null){
					categoryAttribute.setAttrKey(ca.getAttrKey());
					categoryAttribute.setAttrValue(ca.getAttrValue());
					caList.add(categoryAttribute);
				}
			}
		}
		this.getRequest().setAttribute("caList", caList);
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/urms/apiConfig/apiConfig_list";
	}

	/**
	 * @intruduction API综合接口列表数据
	 * @param request
	 * @param response
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 * @Date 2018年7月24日 15:53:59
	 */
	@RequestMapping(value="/apiConfig_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,ApiConfigVo apiConfigVo,Integer page,Integer rows, String order) {
		Pager pager = this.apiConfigServiceImpl.queryEntityList(page, rows, apiConfigVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * API综合接口去编辑
	 * @Title:
	 * @param request
	 * @param apiConfigVo
	 * @return
	 */
	@RequestMapping(value="/apiConfig_edit") 
	public String edit(HttpServletRequest request,ApiConfigVo apiConfigVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(apiConfigVo.getId())){
			ApiConfig apiConfig = this.apiConfigServiceImpl.getEntityById(ApiConfig.class,apiConfigVo.getId());
			BeanUtils.copyProperties(apiConfig,apiConfigVo);
			request.setAttribute("apiConfigVo", apiConfigVo);
		}else{
			request.setAttribute("apiConfigVo", apiConfigVo);
		}
		return "/page/urms/apiConfig/apiConfig_edit";
	}
	
	/**
	 * @intruduction 保存API综合配置信息
	 * @param httpSession
	 * @param response
	 * @author Mr.Wang
	 * @Date 2018年7月24日 15:54:09
	 */
	@RequestMapping(value="/apiConfig_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,ApiConfigVo apiConfigVo) {
		JsonObject json = new JsonObject();
		try {
			ApiConfig apiConfig = new ApiConfig();
			BeanUtils.copyProperties(apiConfigVo,apiConfig);
			this.apiConfigServiceImpl.saveOrUpdate(apiConfig);
			json.addProperty("result", true);
			json.addProperty("id", apiConfig.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}

	/**
	 * @intruduction 删除
	 * @param response
	 * @param id
	 * @Date 2016年1月12日下午2:41:51
	 */
	@RequestMapping(value="/apiConfig_delete")
	public void deleteTree(HttpServletResponse response,String id) {
		JsonObject json = new JsonObject();
		ApiConfig apiConfig = this.apiConfigServiceImpl.getEntityById(ApiConfig.class,id);
		if(apiConfig != null){
			if(apiConfig.getApiState() != null && apiConfig.getApiState() == 1){
				json.addProperty("result", false);
				json.addProperty("msg", "正在使用的api配置数据不能删除!");
				this.print(json.toString());
			}else{
				this.apiConfigServiceImpl.delete(apiConfig);
				json.addProperty("result", true);
				json.addProperty("msg", "删除成功!");
				this.print(json.toString());
			}
		}else{
			json.addProperty("result", false);
			json.addProperty("msg", "删除失败!");
			this.print(json.toString());
		}


	}
	
}