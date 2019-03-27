package com.institution.dao.Impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.institution.dao.IInstitutionDao;

/**
 * 
 * @Description  企业制度dao
 * @author xm
 * @date 2016-10-8
 *
 */
@SuppressWarnings("unchecked")
@Component("institutionDaoImpl")
@Scope("prototype")
public class InstitutionDaoImpl extends BaseDaoImpl implements IInstitutionDao{

}
