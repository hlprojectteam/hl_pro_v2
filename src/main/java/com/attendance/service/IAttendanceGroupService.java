package com.attendance.service;

import com.attendance.module.AttendanceGroup;
import com.attendance.vo.AttendanceGroupVo;
import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;

public interface IAttendanceGroupService extends IBaseService {

    /**
     * 考勤组	分页
     * @param page
     * @param rows
     * @param attendanceGroupVo
     * @return
     * @author xuezb
     * @Date 2019年6月10日
     */
    Pager queryEntityList(Integer page, Integer rows, AttendanceGroupVo attendanceGroupVo);

    /**
     * 考勤组	保存or更新
     * @param attendanceGroupVo
     * @return
     * @author xuezb
     * @Date 2019年6月10日
     */
    AttendanceGroup saveOrUpdate(AttendanceGroupVo attendanceGroupVo);
    
}
