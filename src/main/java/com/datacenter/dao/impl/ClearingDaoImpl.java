package com.datacenter.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.datacenter.dao.IClearingDao;

/**
 * @Description 
 * @author xuezb
 * @date 2019年2月18日
 */
@SuppressWarnings("unchecked")
@Component("clearingDaoImpl")
@Scope("prototype")
public class ClearingDaoImpl extends BaseDaoImpl implements IClearingDao{

}
