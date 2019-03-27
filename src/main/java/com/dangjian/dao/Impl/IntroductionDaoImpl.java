package com.dangjian.dao.Impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.dangjian.dao.IIntroductionDao;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月22日
 *
 */
@SuppressWarnings("unchecked")
@Component("introductionDaoImpl")
@Scope("prototype")
public class IntroductionDaoImpl extends BaseDaoImpl implements IIntroductionDao{

}
