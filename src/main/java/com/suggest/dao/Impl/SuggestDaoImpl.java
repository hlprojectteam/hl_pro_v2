package com.suggest.dao.Impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.suggest.dao.ISuggestDao;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年4月21日
 *
 */
@SuppressWarnings("unchecked")
@Component("suggestDaoImpl")
@Scope("prototype")
public class SuggestDaoImpl extends BaseDaoImpl implements ISuggestDao{

}
