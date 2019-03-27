package com.common.mobile.banner.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.attach.service.IAttachService;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.mobile.banner.dao.IBannerDao;
import com.common.mobile.banner.module.Banner;
import com.common.mobile.banner.service.IBannerService;
import com.common.mobile.banner.vo.BannerVo;
import com.common.utils.helper.Pager;

/**
 * 
 * @Description横幅service实现类
 * @author xm
 * @date 2016-10-8
 *
 */
@Repository("bannerServiceImpl")
public class BannerServiceImpl extends BaseServiceImpl implements IBannerService {

	@Autowired
	public IBannerDao bannerServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	
	@Override
	public void saveOrUpdate(Banner banner){
		if(StringUtils.isBlank(banner.getId())){
			this.save(banner);
		}else{
			this.update(banner);
		}
	}
	@Override
	public Pager queryEntityList(Integer page, Integer rows, BannerVo bannerVo) {
		// TODO Auto-generated method stub
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(bannerVo.getTitle())){
			criterionsList.add(Restrictions.like("title", "%"+ bannerVo.getTitle()+"%"));
		}
		if(bannerVo.getCategory()!=null){
			criterionsList.add(Restrictions.eq("category", bannerVo.getCategory()));
		}
		if(bannerVo.getStatus() != null){
			criterionsList.add(Restrictions.eq("status", bannerVo.getStatus()));
		}
		return this.bannerServiceImpl.queryEntityList(page, rows, criterionsList, Order.asc("order"), Banner.class);
	}
	@Override
	public void deleteBanner(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			//删除附件
			this.attachServiceImpl.deleteAttachByFormId(idz[i]);
			this.delete(Banner.class, idz[i]);
		}
		
	}
	@Override
	public String saveChangeState(String id) {
		Banner banner = this.getEntityById(Banner.class, id);
		String sign = "";
		if(banner.getStatus()==null)
			banner.setStatus(0);//默认未发布
		if(banner.getStatus()==1){
			banner.setStatus(0);		
			sign = "down";
		}else{
			banner.setStatus(1);		
			sign = "up";
		}
		this.update(banner);
		return sign;
	}
	
	@Override
	public Banner queryEntityById(String id) {
		Banner banner = bannerServiceImpl.getEntityById(Banner.class, id);
		return banner;
	}
}
