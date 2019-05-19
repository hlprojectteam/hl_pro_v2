package com.institution.service.Impl;

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
import com.institution.dao.IInstitutionDao;
import com.institution.dao.IInstitutionReportDao;
import com.institution.module.Institution;
import com.institution.service.IInstitutionService;
import com.institution.vo.InstitutionVo;
/**
 * 
 * @Description企业制度Service实现类
 * @author xm
 * @date 2016-10-8
 *
 */
@Repository("institutionServiceImpl")
public class InstitutionServiceImpl extends BaseServiceImpl implements IInstitutionService {
	@Autowired 
	public IInstitutionDao institutionDaoImpl;
	@Autowired 
	public IInstitutionReportDao institutionReportDaoImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	
	@Override
	public void saveOrUpdate(Institution institution){  //解决数据库实际返回行数与影响行数不相同的heberilete异常
		if(StringUtils.isBlank(institution.getId())){
			this.save(institution);
		}else{
			this.update(institution);
		}
	}
	
	@Override
	public void deleteInstitution(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			//删除附件
			this.institutionReportDaoImpl.excuteBySql("delete from p_institution_person where institution_id = " + "'"+ idz[i] +"'");
			this.attachServiceImpl.deleteAttachByFormId(idz[i]);
			this.delete(Institution.class, idz[i]);
		}
		
	}
	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, InstitutionVo institutionVo) {
		// TODO Auto-generated method stub
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(institutionVo.getMainTitle())){
			criterionsList.add(Restrictions.like("mainTitle", "%"+ institutionVo.getMainTitle()+"%"));
		}
		if(StringUtils.isNotBlank(institutionVo.getAuthorName())){
			criterionsList.add(Restrictions.like("authorName", "%"+institutionVo.getAuthorName()+"%"));
		}
		if(institutionVo.getCategory()!=null){
			criterionsList.add(Restrictions.eq("category", institutionVo.getCategory()));
		}
		if(institutionVo.getNewSource()!=null){
			criterionsList.add(Restrictions.eq("newSource", institutionVo.getNewSource()));
		}
		if(institutionVo.getStatus()!=null){
			criterionsList.add(Restrictions.eq("status", institutionVo.getStatus()));
		}
																							
		return this.institutionDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("releaseDate"), Institution.class);
	}
	
	@Override
	public String saveChangeState(String id) {
		Institution institution = this.getEntityById(Institution.class, id);
		String sign = "";
		if(institution.getStatus()==null)
			institution.setStatus(0);//默认未发布
		if(institution.getStatus()==1){
			institution.setStatus(0);		
			sign = "down";
		}else{
			institution.setStatus(1);		
			sign = "up";
		}
		this.update(institution);
		return sign;
	}
}
