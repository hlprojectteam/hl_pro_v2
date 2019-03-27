package com.datacenter.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.datacenter.dao.ITransferRegistrationDao;

/**
 * @Description 
 * @author xuezb
 * @date 2019年2月18日
 */
@SuppressWarnings("unchecked")
@Component("transferRegistrationDaoImpl")
@Scope("prototype")
public class TransferRegistrationDaoImpl extends BaseDaoImpl implements ITransferRegistrationDao{

}
