package com.common.mobile.banner.service;

import com.common.base.service.IBaseService;
import com.common.mobile.banner.module.Banner;
import com.common.mobile.banner.vo.BannerVo;
import com.common.utils.helper.Pager;
/**
 * 
 * @Description 横幅service
 * @author xm
 * @date 2016-10-8
 *
 */
public interface IBannerService extends IBaseService {

	void saveOrUpdate(Banner banner);

	Pager queryEntityList(Integer page, Integer rows, BannerVo bannerVo);

	void deleteBanner(String ids);

	String saveChangeState(String id);

	Banner queryEntityById(String id);

}
