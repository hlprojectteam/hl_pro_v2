package com.news.service.impl;

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
import com.news.dao.INewsDao;
import com.news.module.News;
import com.news.service.INewsService;
import com.news.vo.NewsVo;
/**
 * 
 * @Description企业风采service实现类
 * @author xm
 * @date 2016-10-8
 *
 */
@Repository("newsServiceImpl")
public class NewsServiceImpl extends BaseServiceImpl implements INewsService {

	@Autowired
	public INewsDao newsDaoImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, NewsVo newsVo) {
		// TODO Auto-generated method stub
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(newsVo.getMainTitle())){
			criterionsList.add(Restrictions.like("mainTitle", "%"+ newsVo.getMainTitle()+"%"));
		}
		if(StringUtils.isNotBlank(newsVo.getAuthorName())){
			criterionsList.add(Restrictions.like("authorName", "%"+newsVo.getAuthorName()+"%"));
		}
		if(newsVo.getCategory()!=null){
			criterionsList.add(Restrictions.eq("category", newsVo.getCategory()));
		}
		if(newsVo.getStatus()!=null){
			criterionsList.add(Restrictions.eq("status", newsVo.getStatus()));
		}
		if(newsVo.getNewSource()!=null){
			criterionsList.add(Restrictions.eq("newSource", newsVo.getNewSource()));
		}
		
		if(StringUtils.isNotBlank(newsVo.getModuleType())){
			criterionsList.add(Restrictions.eq("moduleType", newsVo.getModuleType()));
		}
		if(newsVo.getShowPlace()!=null){
			criterionsList.add(Restrictions.eq("showPlace", newsVo.getShowPlace()));
		}
		if(newsVo.getIsTop()!=null){
			criterionsList.add(Restrictions.eq("isTop", newsVo.getIsTop()));
		}
		if(newsVo.getLable()!=null){
			criterionsList.add(Restrictions.eq("lable", newsVo.getLable()));
		}
		return this.newsDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("releaseDate"), News.class);
	}
	
	@Override
	public News queryEntityById(String id) {
		News news = newsDaoImpl.getEntityById(News.class, id);
		return news;
	}
	
	@Override
	public void deleteNews(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			//删除附件
			this.attachServiceImpl.deleteAttachByFormId(idz[i]);
			this.delete(News.class, idz[i]);
		}
		
	}
	@Override
	public void saveOrUpdate(News news){
		if(StringUtils.isBlank(news.getId())){
			this.save(news);
		}else{
			this.update(news);
		}
	}
	@Override
	public String saveChangeState(String id) {
		News news = this.getEntityById(News.class, id);
		String sign = "";
		if(news.getStatus()==null)
			news.setStatus(0);//默认未发布
		if(news.getStatus()==1){
			news.setStatus(0);		
			sign = "down";
		}else{
			news.setStatus(1);		
			sign = "up";
		}
		this.update(news);
		return sign;
	}


}
