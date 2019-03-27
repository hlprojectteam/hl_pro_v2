package com.education.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.attach.service.IAttachService;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.education.dao.IEducationDao;
import com.education.dao.IEducationReportDao;
import com.education.module.Education;
import com.education.service.IEducationService;
import com.education.vo.EducationVo;

/**
 * 
 * @Description教育基础service实现类
 * @author qinyongqian
 * @date 2016-10-8
 *
 */
@Repository("educationServiceImpl")
public class EducationServiceImpl extends BaseServiceImpl implements IEducationService{

	@Autowired 
	public IEducationDao educationDaoImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired 
	public IEducationReportDao educationReportDaoImpl;
	
	
	

	@Override
	public void saveOrUpdate(Education education) {
		if(StringUtils.isNotBlank(education.getId())){
			this.update(education);
		}else{
			this.save(education);
		}
	}

	@Override
	public Pager queryEntityList(Integer page, Integer rows,
			EducationVo educationVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(educationVo.getTitle())){
			criterionsList.add(Restrictions.like("title", "%"+ educationVo.getTitle()+"%"));
		}
		if(StringUtils.isNotBlank(educationVo.getAuthorName())){
			criterionsList.add(Restrictions.like("authorName", "%"+educationVo.getAuthorName()+"%"));
		}
		if(educationVo.getCategory()!=null){
			criterionsList.add(Restrictions.eq("category", educationVo.getCategory()));
		}
		if(educationVo.getType()!=null){
			criterionsList.add(Restrictions.eq("type", educationVo.getType()));
		}
		if(educationVo.getStatus()!=null){
			criterionsList.add(Restrictions.eq("status", educationVo.getStatus()));
		}
		
		return this.educationDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("releaseDate"), Education.class);
	}

	@Override
	public void deleteEducations(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			//删除附件
			this.educationReportDaoImpl.excuteBySql("delete from p_education_person where education_id = " + "'"+ idz[i] +"'");
			this.attachServiceImpl.deleteAttachByFormId(idz[i]);
			this.delete(Education.class, idz[i]);
		}
		
	}

	@Override
	public Education queryEntityById(String id) {
		Education education = educationDaoImpl.getEntityById(Education.class, id);
		return education;
	}
	
	@Override
	public String saveChangeState(String id) {
		Education education = this.getEntityById(Education.class, id);
		String sign = "";
		if(education.getStatus()==1){
			education.setStatus(0);		
			sign = "down";
		}else{
			education.setStatus(1);		
			sign = "up";
		}
		this.update(education);
		return sign;
	}
	
	public IEducationDao getEducationDaoImpl() {
		return educationDaoImpl;
	}

	public void setEducationDaoImpl(IEducationDao educationDaoImpl) {
		this.educationDaoImpl = educationDaoImpl;
	}

	public IAttachService getAttachServiceImpl() {
		return attachServiceImpl;
	}

	public void setAttachServiceImpl(IAttachService attachServiceImpl) {
		this.attachServiceImpl = attachServiceImpl;
	}

	

}
