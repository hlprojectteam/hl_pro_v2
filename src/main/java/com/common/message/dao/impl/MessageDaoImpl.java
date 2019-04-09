package com.common.message.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.common.message.dao.IMessageDao;

@SuppressWarnings("unchecked")
@Component("messageDaoImpl")
@Scope("prototype")
public class MessageDaoImpl extends BaseDaoImpl implements IMessageDao{

}
