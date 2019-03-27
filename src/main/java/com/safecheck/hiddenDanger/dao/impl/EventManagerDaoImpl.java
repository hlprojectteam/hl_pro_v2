package com.safecheck.hiddenDanger.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.safecheck.hiddenDanger.dao.IEventManagerDao;


/**
 * @Description 事件管理Dao实现
 * @author xuezb
 * @Date 2018年5月23日 下午3:10:22
 */
@SuppressWarnings("unchecked")
@Component("eventManagerDaoImpl")
@Scope("prototype")
public class EventManagerDaoImpl extends BaseDaoImpl implements IEventManagerDao{

}
