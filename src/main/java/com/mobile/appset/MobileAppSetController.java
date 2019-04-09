package com.mobile.appset;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.base.controller.BaseController;
import com.common.utils.Common;
import com.urms.dataDictionary.module.Category;
import com.urms.dataDictionary.module.CategoryAttribute;
import com.urms.dataDictionary.service.IDataDictionaryService;

@Controller
public class MobileAppSetController extends BaseController{
	
	@Autowired
	public IDataDictionaryService dataDictionaryServiceImpl;
	
	@RequestMapping(value="/app_download")
	public String list(HttpSession httpSession,HttpServletResponse response){
	    return "/app";
	}
	@RequestMapping(value="/app_download_cache")
	public void list(HttpServletRequest request,HttpServletResponse response){
		String categoryCode = Common.CategoryCode;
		JSONObject json = new JSONObject();
		JSONArray result=new JSONArray();
		json.put("result", false);
		try {
			JSONArray array=JSONArray.fromObject(categoryCode);
			if(array!=null){
				for (Object object : array) {
					String code=object.toString();
					//List<Map<String, String>> list=getCategorys(code);
					JSONObject itemList=getCategorys(code);
					JSONObject jb=new JSONObject();
					jb.put(code, itemList);
					result.add(jb);
				}
			}
			json.put("rows", result);
			json.put("result", true);
		} catch (Exception e) {
			json.put("result", false);
			json.put("msg", e.getMessage());
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param categoryCode
	 * @方法：@return
	 * @描述：通过字典目录编码 获得下级所有字典目录
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月1日
	 */
	private JSONObject getCategorys(String categoryCode){
		JSONObject json=new JSONObject();
		Category category = this.dataDictionaryServiceImpl.getCategoryByCode(categoryCode);
		if(category!=null){
			List<CategoryAttribute> CategoryAttributeList=dataDictionaryServiceImpl.queryEntityCategoryAttrListALL(category.getId());
			for (CategoryAttribute categoryAttribute : CategoryAttributeList) {
				//Map<String, String> m=new HashMap<String, String>();
				//m.put(categoryAttribute.getAttrKey(),categoryAttribute.getAttrValue());
				json.put(categoryAttribute.getAttrKey(),categoryAttribute.getAttrValue());
			}
		}
		return json;
	}

}
