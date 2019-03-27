package com.dangjian.dao.Impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.dangjian.dao.IBranchDao;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月1日
 *
 */
@SuppressWarnings("unchecked")
@Component("branchDaoImpl")
@Scope("prototype")
public class BranchDaoImpl extends BaseDaoImpl implements IBranchDao{

}
