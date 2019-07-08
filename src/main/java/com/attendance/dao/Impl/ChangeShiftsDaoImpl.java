package com.attendance.dao.Impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.attendance.dao.IChangeShiftsDao;
import com.common.base.dao.impl.BaseDaoImpl;

/**
 * 
 * @Description调班
 * @author qinyongqian
 * @date 2019年5月19日
 *
 */
@SuppressWarnings("unchecked")
@Repository("changeShiftsDaoImpl")
@Scope("prototype")
public class ChangeShiftsDaoImpl extends BaseDaoImpl implements IChangeShiftsDao{

}
