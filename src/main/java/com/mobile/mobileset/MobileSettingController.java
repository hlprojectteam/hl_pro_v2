package com.mobile.mobileset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.base.controller.BaseController;
import com.common.mobile.banner.service.IBannerService;

/**
 * 
 * @Description 移动端设置接口类
 * @author qinyongqian
 * @date 2016-10-17
 *
 */
@Controller
@RequestMapping(value="/settingMobile")
public class MobileSettingController extends BaseController{
	
	@Autowired
	public IBannerService bannerServiceImpl;
	
	

}
