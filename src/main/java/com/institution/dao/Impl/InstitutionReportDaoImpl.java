package com.institution.dao.Impl;


import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.institution.dao.IInstitutionReportDao;
import com.institution.module.InstitutionPerson;

/**
 * 
 * @Description  制度报表DAO实现类
 * @author xm
 * @date 2016-10-8
 *
 */
@SuppressWarnings("unchecked")
@Component("institutionReportDaoImpl")
@Scope("prototype")
public class InstitutionReportDaoImpl extends BaseDaoImpl implements  IInstitutionReportDao {

	@Override
	public void saveOrUpdate(InstitutionPerson institutionPerson) {
		if(StringUtils.isNotBlank(institutionPerson.getId())){
			this.update(institutionPerson);
		}else{
			this.save(institutionPerson);
		}
	}
	
}
