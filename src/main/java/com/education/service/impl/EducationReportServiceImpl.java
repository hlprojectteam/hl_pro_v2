package com.education.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.education.dao.IEducationReportDao;
import com.education.module.Education;
import com.education.module.EducationPerson;
import com.education.service.IEducationReportService;
import com.urms.orgFrame.dao.IOrgFrameDao;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.user.module.User;
/**
 * 
 * @Description教育报表service实现类
 * @author xm
 * @date 2016-10-8
 *
 */
@Repository("educationReportServiceImpl")
public class EducationReportServiceImpl extends BaseServiceImpl implements IEducationReportService{
	@Autowired 
	public IEducationReportDao educationReportDaoImpl;
	@Autowired 
	public IOrgFrameDao orgFrameDaoImpl;
	@Autowired
	public IOrgFrameService orgFrameServiceImpl;
	
	@Override
	public List<OrgFrame> getNodes(OrgFrameVo orgFrameVo){
		List<OrgFrame> orgFrames = this.orgFrameServiceImpl.queryOrgFrameList(orgFrameVo);
		List<OrgFrame> children = this.orgFrameServiceImpl.getOrgFrameChildren(orgFrames.get(0).getId());
		orgFrames.addAll(children);
		return orgFrames;
	}
	
	
	@Override
	public String getEducationReport(String educationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrUpdate(Education education, User user) {
		EducationPerson ep = new EducationPerson();
		ep.setEducation(education);
		if(user.getId()!=null)
		ep.setPersonId(user.getId());
		if(user.getUserName()!=null)
		ep.setPersonName(user.getUserName());
		OrgFrame of = user.getOrgFrame();//机构
		if(user.getOrgFrame()!=null)
		ep.setOrgFrameId(of.getId());
		
		if(isRepeatData(ep)){
			this.update(ep);
		}else{
			this.save(ep);
		}
		//this.educationReportDaoImpl.saveOrUpdate(ep);
	}
	
	@SuppressWarnings("rawtypes")
	public boolean isRepeatData(EducationPerson ep){
		String educationID = ep.getEducation().getId();
		String personID = ep.getPersonId();
		List epList = this.educationReportDaoImpl.queryBySql("select id from p_education_person where person_id="+"'"+personID+"'"+"and education_id="+"'"+educationID+"'");
		if(epList.size()>0){
			String id = (String)epList.get(0);
			ep.setId(id);
			return true; //是重複數據
		}else{
			return false;
		}
	}
}
