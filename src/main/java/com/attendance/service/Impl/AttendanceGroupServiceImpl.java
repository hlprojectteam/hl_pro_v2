package com.attendance.service.Impl;

import com.attendance.dao.IAttendanceGroupDao;
import com.attendance.module.AttendanceGroup;
import com.attendance.service.IAttendanceGroupService;
import com.attendance.vo.AttendanceGroupVo;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("attendanceGroupServiceImpl")
public class AttendanceGroupServiceImpl extends BaseServiceImpl implements IAttendanceGroupService {

    @Autowired
    private IAttendanceGroupDao attendanceGroupDaoImpl;


    @Override
    public Pager queryEntityList(Integer page, Integer rows, AttendanceGroupVo attendanceGroupVo) {
        List<Criterion> params = new ArrayList<>();
        if(StringUtils.isNotBlank(attendanceGroupVo.getGroupName())){
            params.add(Restrictions.like("groupName", "%" + attendanceGroupVo.getGroupName() + "%"));
        }
        if(attendanceGroupVo.getGroupType() != null){
            params.add(Restrictions.eq("groupType", attendanceGroupVo.getGroupType()));
        }
        if(StringUtils.isNotBlank(attendanceGroupVo.getOrgFrameId())){
            params.add(Restrictions.eq("orgFrameId", attendanceGroupVo.getOrgFrameId()));
        }
        return this.attendanceGroupDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), AttendanceGroup.class);
    }

    @Override
    public AttendanceGroup saveOrUpdate(AttendanceGroupVo attendanceGroupVo) {
        AttendanceGroup attendanceGroup = new AttendanceGroup();
        BeanUtils.copyProperties(attendanceGroupVo, attendanceGroup);
        if(StringUtils.isBlank(attendanceGroup.getId())){
            this.save(attendanceGroup);
        }else{
            this.update(attendanceGroup);
        }
        return attendanceGroup;
    }
    
}
