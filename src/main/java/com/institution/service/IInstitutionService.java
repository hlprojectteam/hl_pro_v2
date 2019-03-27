package com.institution.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.institution.module.Institution;
import com.institution.vo.InstitutionVo;
import com.news.module.News;


/**
 * 
 * @Description 企业制度service接口
 * @author xm
 * @date 2016-10-8
 *
 */
public interface IInstitutionService extends IBaseService{

	public void saveOrUpdate(Institution institution);

	public void deleteInstitution(String ids);

	public Pager queryEntityList(Integer page, Integer rows, InstitutionVo institutionVo);

	public String saveChangeState(String id);


}
