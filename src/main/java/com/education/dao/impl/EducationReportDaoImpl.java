package com.education.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.education.dao.IEducationReportDao;
import com.education.module.EducationPerson;

/**
 * 
 * @Description  报表dao实现类
 * @author xm
 * @date 2016-10-8
 *
 */
@SuppressWarnings("unchecked")
@Component("educationReportDaoImpl")
@Scope("prototype")
public class EducationReportDaoImpl extends BaseDaoImpl implements IEducationReportDao {
	@Override
	public void saveOrUpdate(EducationPerson educationPerson) {
		if(StringUtils.isNotBlank(educationPerson.getId())){
			this.update(educationPerson);
		}else{
			this.save(educationPerson);
		}
	}
}
