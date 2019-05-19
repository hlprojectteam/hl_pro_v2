package com.institution.service;

import java.util.List;

import com.common.base.service.IBaseService;
import com.institution.module.Institution;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.user.module.User;

/**
 * 
 * @Description 制度统计service接口
 * @author xm
 * @date 2016-10-12
 *
 */
public interface IInstitutionReportService extends IBaseService{
	/**
	 * 
	 * @ClassName:IinstitutionReportService.java
	 * @Description:制度阅读统计
	 * @param institutionId
	 * @return
	 * @author xm
	 * @date 2016-10-12
	 */
	public String getInstitutionReport(String institutionId);

	public void saveOrUpdate(Institution institution, User user);

	List<OrgFrame> getNodes(OrgFrameVo orgFrameVo);
}
