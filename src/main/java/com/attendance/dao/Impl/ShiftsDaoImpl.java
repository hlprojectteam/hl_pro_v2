package com.attendance.dao.Impl;

import com.attendance.dao.IShiftsDao;
import com.common.base.dao.impl.BaseDaoImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("shiftsDaoImpl")
@Scope("prototype")
public class ShiftsDaoImpl extends BaseDaoImpl implements IShiftsDao {
}
