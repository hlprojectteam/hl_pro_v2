package com.attendance.dao.Impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.attendance.dao.IApprovalDao;
import com.common.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年5月19日
 *
 */
@SuppressWarnings("unchecked")
@Repository("approvalDaoImpl")
@Scope("prototype")
public class ApprovalDaoImpl extends BaseDaoImpl implements IApprovalDao{

}
