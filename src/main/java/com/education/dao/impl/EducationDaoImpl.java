package com.education.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.education.dao.IEducationDao;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2016-10-8
 *
 */
@SuppressWarnings("unchecked")
@Component("educationDaoImpl")
@Scope("prototype")
public class EducationDaoImpl extends BaseDaoImpl implements IEducationDao{

}
