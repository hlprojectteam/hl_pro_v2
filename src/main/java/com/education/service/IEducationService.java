package com.education.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.education.module.Education;
import com.education.vo.EducationVo;

/**
 * 
 * @Description 教育基础service接口
 * @author qinyongqian
 * @date 2016-10-8
 *
 */
public interface IEducationService extends IBaseService{
	
	
	/**
	 * 
	 * @ClassName:IEducationService.java
	 * @Description:保存或更新
	 * @param education
	 * @author qinyongqian
	 * @date 2016-10-8
	 */
	public void saveOrUpdate(Education education);
	
	/**
	 * 
	 * @ClassName:IEducationService.java
	 * @Description:分页查询 
	 * @param page
	 * @param rows
	 * @param educationVo
	 * @return
	 * @author qinyongqian
	 * @date 2016-10-8
	 */
	public Pager queryEntityList(Integer page, Integer rows,EducationVo educationVo);
	
	/**
	 * 
	 * @ClassName:IEducationService.java
	 * @Description:根据id删除咨讯
	 * @param ids
	 * @author qinyongqian
	 * @date 2016-10-8
	 */
	public void deleteEducations(String ids);
	
	/**
	 * 
	 * @ClassName:IEducationService.java
	 * @Description:ID得到实体
	 * @param id
	 * @return
	 * @author qinyongqian
	 * @date 2016-10-8
	 */
	public Education queryEntityById(String id);
	
	
	/**
	 * 
	 * @ClassName:IEducationService.java
	 * @Description:改变状态 发布or撤销
	 * @param id
	 * @return
	 * @author qinyongqian
	 * @date 2016-10-9
	 */
	public String saveChangeState(String id);

}
