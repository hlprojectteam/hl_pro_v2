package com.institution.dao;

import java.util.List;

import com.common.base.dao.IBaseDao;
import com.common.base.service.IBaseService;
import com.common.mobile.banner.dao.IBannerDao;
import com.education.module.EducationPerson;
import com.institution.module.Institution;
import com.institution.module.InstitutionPerson;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.user.module.User;

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
