package com.institution.dao;


import com.common.base.dao.IBaseDao;
import com.institution.module.InstitutionPerson;

/**
 * 
 * @Description 制度统计dao
 * @author xm
 * @date 2016-10-12
 *
 */
public interface IInstitutionReportDao extends IBaseDao {
	void saveOrUpdate(InstitutionPerson institutionPerson);
}
