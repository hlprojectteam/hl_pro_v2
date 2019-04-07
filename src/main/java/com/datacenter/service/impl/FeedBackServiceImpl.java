package com.datacenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.datacenter.dao.IFeedBackDao;
import com.datacenter.module.FeedBack;
import com.datacenter.service.IFeedBackService;
import com.datacenter.vo.FeedBackVo;

/**
 * @Description 顾客意见反馈 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("feedBackServiceImpl")
public class FeedBackServiceImpl extends BaseServiceImpl implements IFeedBackService{

	@Autowired
	private IFeedBackDao feedBackDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, FeedBackVo feedBackVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(feedBackVo.getTtId())){
			params.add(Restrictions.eq("ttId", feedBackVo.getTtId()));
		}
		if(feedBackVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", feedBackVo.getDutyDateStart()));
		}
		if(feedBackVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", feedBackVo.getDutyDateEnd()));
		}
		return this.feedBackDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), FeedBack.class);
	}

	@Override
	public FeedBack saveOrUpdate(FeedBackVo feedBackVo) {
		FeedBack feedBack = new FeedBack();
		BeanUtils.copyProperties(feedBackVo, feedBack);
		if(StringUtils.isBlank(feedBack.getId())){
			this.save(feedBack);
		}else{
			this.update(feedBack);
		}
		return feedBack;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.feedBackDaoImpl.excuteBySql("delete from dc_FeedBack where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<FeedBack> list = this.feedBackDaoImpl.queryEntityList(params, Order.desc("createTime"), FeedBack.class);	//根据主表Id获取子表关联数据
		for (FeedBack feedBack : list) {
			feedBack.setDutyDate(dutyDate);
			this.update(feedBack);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<FeedBack> queryEntityList(FeedBackVo feedBackVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from FeedBack where 1 = 1 ");
		if(StringUtils.isNotBlank(feedBackVo.getTtId())){
			objectList.add(feedBackVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(feedBackVo.getDutyDateStart() != null){		//日期Start
			objectList.add(feedBackVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(feedBackVo.getDutyDateEnd() != null){		//日期End
			objectList.add(feedBackVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		//排序, 根据日期倒序排序，接报时间顺序排序
		hql.append(" order by dutyDate desc,receiptTime asc ");

		List<FeedBack> fbList = this.feedBackDaoImpl.queryEntityHQLList(hql.toString(), objectList, FeedBack.class);
		return fbList;
	}

}
