package com.urms.feedbackProblem.service.impl;



import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.urms.feedbackProblem.dao.IFeedbackProblemDao;
import com.urms.feedbackProblem.module.FeedbackProblem;
import com.urms.feedbackProblem.service.IFeedbackProblemService;
import com.urms.feedbackProblem.vo.FeedbackProblemVo;

@Repository("feedbackProblemServiceImpl")
public class FeedbackProblemServiceImpl extends BaseServiceImpl implements IFeedbackProblemService{
	
	@Autowired
	public IFeedbackProblemDao feedbackProblemDaoImpl;	
	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, FeedbackProblemVo feedbackProblemVo) {
		List<Criterion> criterionsList = new ArrayList<Criterion>();
//			if(StringUtils.isNotBlank(feedbackProblemVo.getOrgFrameName())){
//				criterionsList.add(Restrictions.like("orgFrameName", "%"+feedbackProblemVo.getOrgFrameName()+"%"));
//			}
//			if(StringUtils.isNotBlank(feedbackProblemVo.getOrgFrameCode())){
//				criterionsList.add(Restrictions.like("orgFrameCode", "%"+feedbackProblemVo.getOrgFrameCode()+"%"));
//			}
		return feedbackProblemDaoImpl.queryEntityList(page, rows, criterionsList, Order.desc("createTime") ,FeedbackProblem.class);
	}
	
	public IFeedbackProblemDao getFeedbackProblemDaoImpl() {
		return feedbackProblemDaoImpl;
	}

	public void setFeedbackProblemDaoImpl(IFeedbackProblemDao feedbackProblemDaoImpl) {
		this.feedbackProblemDaoImpl = feedbackProblemDaoImpl;
	}


}
