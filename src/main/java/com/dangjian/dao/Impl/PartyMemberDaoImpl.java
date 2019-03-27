package com.dangjian.dao.Impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.dangjian.dao.IPartyMemberDao;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月1日
 *
 */
@SuppressWarnings("unchecked")
@Component("partyMemberDaoImpl")
@Scope("prototype")
public class PartyMemberDaoImpl extends BaseDaoImpl implements IPartyMemberDao{

}
