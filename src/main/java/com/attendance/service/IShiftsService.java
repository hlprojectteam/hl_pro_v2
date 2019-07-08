package com.attendance.service;

import com.attendance.module.Shifts;
import com.attendance.vo.ShiftsVo;
import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;


public interface IShiftsService extends IBaseService {

    /**
     * 考勤班次	分页
     * @param page
     * @param rows
     * @param shiftsVo
     * @return
     * @author xuezb
     * @Date 2019年6月10日
     */
    Pager queryEntityList(Integer page, Integer rows, ShiftsVo shiftsVo);

    /**
     * 考勤班次	保存or更新
     * @param shiftsVo
     * @return
     * @author xuezb
     * @Date 2019年6月10日
     */
    Shifts saveOrUpdate(ShiftsVo shiftsVo);
    
}
