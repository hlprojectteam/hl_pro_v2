package com.education.service;

import java.util.List;

import com.common.base.service.IBaseService;
import com.education.module.Education;
import com.education.module.EducationPerson;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.user.module.User;

/**
 * 
 * @Description 教育培训统计service接口
 * @author qinyongqian
 * @date 2016-10-12
 *
 */
public interface IEducationReportService  extends IBaseService{

	/**
	 * 
	 * @ClassName:IEducationReportService.java
	 * @Description:教育材料阅读统计
	 * @param educationId
	 * @return
	 * @author qinyongqian
	 * @date 2016-10-12
	 */
	public String getEducationReport(String educationId);

	public void saveOrUpdate(Education education, User user);

	List<OrgFrame> getNodes(OrgFrameVo orgFrameVo);

}
