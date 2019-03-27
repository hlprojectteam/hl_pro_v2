package com.dangjian.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.dangjian.module.Introduction;
import com.dangjian.vo.IntroductionVo;

public interface IIntroductionService extends IBaseService{

	public Pager queryEntityList(Integer page, Integer rows,IntroductionVo introductionVo);

	public Introduction queryEntityById(String id);

	public void deleteIntroduction(String ids);

	public String saveChangeState(String id);

	public void saveOrUpdate(Introduction introduction);

}
