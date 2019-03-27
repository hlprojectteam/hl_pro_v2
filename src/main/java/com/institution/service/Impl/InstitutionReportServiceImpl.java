package com.institution.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
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
 * @Description制度service实现类
 * @author xm
 * @date 2016-10-8
 *
 */
@Repository("institutionReportServiceImpl")
public class InstitutionReportServiceImpl extends BaseServiceImpl implements IInstitutionReportService {
	@Autowired 
	public IInstitutionReportDao institutionReportDaoImpl;
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
	public String getInstitutionReport(String institutionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveOrUpdate(Institution institution, User user) {
		InstitutionPerson ep = new InstitutionPerson();
		ep.setInstitution(institution);
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
		//this.institutionReportDaoImpl.saveOrUpdate(ep);
	}
	
	public boolean isRepeatData(InstitutionPerson ep){
		String institutionID = ep.getInstitution().getId();
		String personID = ep.getPersonId();
		List epList = this.institutionReportDaoImpl.queryBySql("select id from p_institution_person where person_id="+"'"+personID+"'"+"and institution_id="+"'"+institutionID+"'");
		if(epList.size()>0){
			String id = (String)epList.get(0);
			ep.setId(id);
			return true; //是重複數據
		}else{
			return false;
		}
	}
}
