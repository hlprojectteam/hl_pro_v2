package com.suggest.service;

import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.suggest.module.Suggest;
import com.suggest.vo.SuggestVo;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年4月21日
 *
 */
public interface ISuggestService extends IBaseService{
	
	public Pager queryEntityList(Integer page, Integer rows,SuggestVo suggestVo);
	
	public List<Suggest> queryALLEntityList();
	
	public void deleteEntitys(String ids);
	
	public void saveOrUpdate(Suggest suggest);

}
