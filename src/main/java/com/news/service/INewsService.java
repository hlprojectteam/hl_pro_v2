package com.news.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.news.module.News;
import com.news.vo.NewsVo;

/**
 * 
 * @Description 企业风采service
 * @author xm
 * @date 2016-10-8
 *
 */
public interface INewsService  extends IBaseService{

	public Pager queryEntityList(Integer page, Integer rows,NewsVo newsVo);

	public News queryEntityById(String id);

	public void deleteNews(String ids);

	public String saveChangeState(String id);

	public void saveOrUpdate(News news);

}
