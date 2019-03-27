package com.datacenter.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.datacenter.dao.ISurveillanceInspectionDao;

/**
 * @Description 
 * @author xuezb
 * @date 2019年2月18日
 */
@SuppressWarnings("unchecked")
@Component("surveillanceInspectionDaoImpl")
@Scope("prototype")
public class SurveillanceInspectionDaoImpl extends BaseDaoImpl implements ISurveillanceInspectionDao{

}

