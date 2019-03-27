package com.common.mobile.banner.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.o.common.beans.BeanUtils;

import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.mobile.banner.module.Banner;
import com.common.mobile.banner.service.IBannerService;
import com.common.mobile.banner.vo.BannerVo;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.user.module.User;

@Controller
@RequestMapping(value="/banner")
public class BannerController extends BaseController{
	@Autowired
	public IBannerService bannerServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	/**
	 * 
	 * @ClassName:bannerController.java
	 * @Description:列表页面加载
	 * @param httpSession
	 * @param response
	 * @param bannervo
	 * @return  分页数据
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/banner_load")
	public void load(HttpServletRequest request,HttpServletResponse response,BannerVo bannerVo,
			Integer page,Integer rows){
		Pager pager = this.bannerServiceImpl.queryEntityList(page, rows, bannerVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	/**
	 * 
	 * @ClassName:bannerController.java
	 * @Description:图片编辑
	 * @param httpSession
	 * @param response
	 * @param id
	 * @return  编辑对象
	 * @author xm
	 * @date 2016-10-12
	 */
	
	@RequestMapping(value="/update_img") 
	public void updateEntityImg(HttpSession httpSession,HttpServletResponse response,String id){
		List<Attach> attachs = this.attachServiceImpl.queryAttchListByFormId(id);
		String path = "";
		for(int i=0;i<attachs.size();i++){
			Attach attach = attachs.get(i);
			if(StringUtils.isNotBlank(attach.getSuffix()) ){
				if(!attach.getAttachType().equals("bannerFile")){
					path = attach.getPathUpload();
				}
			}
		}
		Banner banner = this.bannerServiceImpl.queryEntityById(id);
		banner.setCover(path);
		this.bannerServiceImpl.update(banner);
	}
	/**
	 * 
	 * @ClassName:BannerController.java
	 * @Description:列表页面请求
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @return  
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/banner_list")
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode){
		this.getRequest().setAttribute("menuCode", menuCode);
	    return "/page/mobileSet/banner/banner_list";
	}
	/**
	 * 
	 * @ClassName:BannerController.java
	 * @Description:页面编辑
	 * @param httpSession
	 * @param response
	 * @param bannerVo
	 * @return  编辑对象
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/banner_edit")
	public String edit(HttpServletRequest request, BannerVo bannerVo){
		if(StringUtils.isNotBlank(bannerVo.getId())){
			try{
				Banner banner = this.bannerServiceImpl.getEntityById(Banner.class, bannerVo.getId());
				BeanUtils.copyProperties(banner, bannerVo);
				request.setAttribute("bannerVo", bannerVo);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			request.setAttribute("bannerVo", bannerVo);
		}
		return "/page/mobileSet/banner/banner_edit";
			
	}
	/**
	 * 
	 * @ClassName:BannerController.java
	 * @Description:删除
	 * @param httpSession
	 * @param response
	 * @param ids
	 * @return  删除对象
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/banner_del") 
	public void delete(HttpServletResponse response,String ids) {
		this.bannerServiceImpl.deleteBanner(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}/**
	 * 
	 * @ClassName:BannerController.java
	 * @Description:状态改变
	 * @param httpSession
	 * @param response
	 * @param id
	 * @return  状态标记
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/banner_changeState",method = RequestMethod.POST) 
	public void changeState(HttpSession httpSession,HttpServletResponse response,String id) {
		JsonObject json = new JsonObject();
		try {
			String sign = this.bannerServiceImpl.saveChangeState(id);
			json.addProperty("result", true);
			json.addProperty("sign", sign);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 
	 * @ClassName:BannerController.java
	 * @Description:保存
	 * @param httpSession
	 * @param response
	 * @param bannervo
	 * @return  保存对象
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/banner_saveOrUpdate",method = RequestMethod.POST)
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,BannerVo bannerVo){
		JsonObject json =new JsonObject();
		try{
			Banner banner = new Banner();
			bannerVo = formatVo(bannerVo);
			BeanUtils.copyProperties(bannerVo, banner);
			this.bannerServiceImpl.saveOrUpdate(banner);
			json.addProperty("id", banner.getId());
			json.addProperty("result", true);
		}catch (Exception e) {
			e.printStackTrace();
			json.addProperty("result", false);
		}finally{
			this.print(json.toString());
		}
	}
	private BannerVo formatVo(BannerVo bannerVo){
		User user = this.getSessionUser();
		if(StringUtils.isNotBlank(bannerVo.getId())){
		}else{
			bannerVo.setCreateUserId(user.getId());
		}
		if(bannerVo.getStatus() == null){
			bannerVo.setStatus(0);
		}
		return bannerVo;
	}
}
