package com.education.dao;

import com.common.base.dao.IBaseDao;
import com.education.module.EducationPerson;

/**
 * 
 * @Description 报表dao
 * @author xm
 * @date 2016-10-8
 *
 */
public interface IEducationReportDao extends IBaseDao {

	void saveOrUpdate(EducationPerson educationPerson);
}
