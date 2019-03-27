package com.education.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.education.dao.IEducationDao;
import com.education.module.EducationPerson;

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
