package com.common.mobile.banner.dao.Impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.common.mobile.banner.dao.IBannerDao;

/**
 * 
 * @Description 横幅dao实现类
 * @author xm
 * @date 2016-10-8
 *
 */
@SuppressWarnings("unchecked")
@Component("bannerDaoImpl")
@Scope("prototype") 
public class BannerDaoImpl extends BaseDaoImpl implements IBannerDao{ //不能implements其他类实现的dao例如INewsDao

}
