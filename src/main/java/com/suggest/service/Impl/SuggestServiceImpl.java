package com.suggest.service.Impl;

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
import com.suggest.dao.ISuggestDao;
import com.suggest.module.Suggest;
import com.suggest.service.ISuggestService;
import com.suggest.vo.SuggestVo;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年4月21日
 *
 */
@Repository("suggestServiceImpl")
public class SuggestServiceImpl extends BaseServiceImpl implements ISuggestService{

	@Autowired
	public ISuggestDao suggestDaoImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Override
	public Pager queryEntityList(Integer page, Integer rows, SuggestVo suggestVo) {
		// TODO Auto-generated method stub
		List<Criterion> criterionsList = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(suggestVo.getContent())){
			criterionsList.add(Restrictions.like("content", "%"+ suggestVo.getContent()+"%"));
		}
		if(StringUtils.isNotBlank(suggestVo.getCreatorId())){
			criterionsList.add(Restrictions.eq("creatorId",  suggestVo.getCreatorId()));
		}
		if(suggestVo.getModuleClass()!=null){
			criterionsList.add(Restrictions.eq("moduleClass",  suggestVo.getModuleClass()));
		}
		if(suggestVo.getStatus()!=null){
			if(suggestVo.getStatus()==-1){
				//表示获取所有未回复的
				criterionsList.add(Restrictions.or(Restrictions.eq("status", 1),Restrictions.eq("status", 2)));//字典目录
			}
			
		}
		return this.suggestDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime"), Suggest.class);
	}

	@Override
	public List<Suggest> queryALLEntityList() {
		// TODO Auto-generated method stub
		return suggestDaoImpl.queryAllEntity(Suggest.class, Order.desc("createTime"));
	}

	@Override
	public void deleteEntitys(String ids) {
		String[] idz = ids.split(",");
		for (int i = 0; i < idz.length; i++) {
			//删除附件
			this.attachServiceImpl.deleteAttachByFormId(idz[i]);
			this.delete(Suggest.class, idz[i]);
		}
	}

	@Override
	public void saveOrUpdate(Suggest suggest) {
		if(StringUtils.isBlank(suggest.getId())){
			this.save(suggest);
		}else{
			this.update(suggest);
		}
	}

}
