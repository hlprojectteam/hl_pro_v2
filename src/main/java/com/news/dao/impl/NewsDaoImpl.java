package com.news.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.common.base.dao.impl.BaseDaoImpl;
import com.news.dao.INewsDao;
/**
 * 
 * @Description 企业风采dao实现类
 * @author xm
 * @date 2016-10-8
 *
 */
@SuppressWarnings("unchecked")
@Component("newsDaoImpl")
@Scope("prototype")
public class NewsDaoImpl extends BaseDaoImpl implements INewsDao {

}
