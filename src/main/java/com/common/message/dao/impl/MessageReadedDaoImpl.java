package com.common.message.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.common.message.dao.IMessageReadedDao;

@SuppressWarnings("unchecked")
@Component("messageReadedDaoImpl")
@Scope("prototype")
public class MessageReadedDaoImpl extends BaseDaoImpl implements IMessageReadedDao{

}
