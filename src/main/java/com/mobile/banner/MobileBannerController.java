package com.mobile.banner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.mobile.banner.service.IBannerService;
import com.common.mobile.banner.vo.BannerVo;
import com.common.utils.helper.Pager;

/**
 * 
 * @intruduction 
 * @author Will
 * @Date 下午3:31:07
 */
@Controller
@RequestMapping(value="/mobile_banner")
public class MobileBannerController extends BaseController{
	@Autowired
	public IBannerService bannerServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	
	@RequestMapping(value="/banner_list")
	public void mobileBannerList(HttpServletRequest request,HttpServletResponse response,
			Integer page,Integer rows){
		JSONObject json = new JSONObject();
		BannerVo bannerVo = new BannerVo();
		bannerVo.setStatus(1);
		bannerVo.setCategory(1);
		Pager pager = this.bannerServiceImpl.queryEntityList(page, rows, bannerVo);
		json.put("result", "true");
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
}
