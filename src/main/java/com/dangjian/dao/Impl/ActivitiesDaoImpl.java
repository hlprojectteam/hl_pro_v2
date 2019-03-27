package com.dangjian.dao.Impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.dangjian.dao.IActivitesDao;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2018年12月29日
 *
 */
@SuppressWarnings("unchecked")
@Component("activitiesDaoImpl")
@Scope("prototype")
public class ActivitiesDaoImpl extends BaseDaoImpl implements IActivitesDao{

}
