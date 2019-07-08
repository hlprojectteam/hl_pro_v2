package com.attendance.dao.Impl;

import com.attendance.dao.IAttendanceGroupDao;
import com.common.base.dao.impl.BaseDaoImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository("attendanceGroupDaoImpl")
@Scope("prototype")
public class AttendanceGroupDaoImpl extends BaseDaoImpl implements IAttendanceGroupDao {
}
