package com.institution.dao.Impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.common.base.dao.impl.BaseDaoImpl;
import com.common.base.service.impl.BaseServiceImpl;
import com.education.module.EducationPerson;
import com.education.service.IEducationReportService;
import com.institution.dao.IInstitutionReportDao;
import com.institution.module.Institution;
import com.institution.module.InstitutionPerson;
import com.institution.service.IInstitutionReportService;
import com.urms.orgFrame.dao.IOrgFrameDao;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.user.module.User;

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
