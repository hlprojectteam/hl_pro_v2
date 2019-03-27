package com.urms.feedbackProblem.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.feedbackProblem.vo.FeedbackProblemVo;

public interface IFeedbackProblemService extends IBaseService{

	/**
	 * @intruduction 分页
	 * @param page
	 * @param rows
	 * @param feedbackProblemVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年6月6日上午9:55:05
	 */
	Pager queryEntityList(Integer page, Integer rows,FeedbackProblemVo feedbackProblemVo);
	
}
