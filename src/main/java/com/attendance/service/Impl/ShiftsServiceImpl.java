package com.attendance.service.Impl;

import com.attendance.dao.IShiftsDao;
import com.attendance.module.Shifts;
import com.attendance.service.IShiftsService;
import com.attendance.vo.ShiftsVo;
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

@Repository("shiftsServiceImpl")
public class ShiftsServiceImpl extends BaseServiceImpl implements IShiftsService {

    @Autowired
    private IShiftsDao shiftsDaoImpl;
    

    @Override
    public Pager queryEntityList(Integer page, Integer rows, ShiftsVo shiftsVo) {
        List<Criterion> params = new ArrayList<>();
        if(StringUtils.isNotBlank(shiftsVo.getShiftName())){
            params.add(Restrictions.like("shiftName", "%" + shiftsVo.getShiftName() + "%"));
        }
        if(StringUtils.isNotBlank(shiftsVo.getOrgFrameId())){
            params.add(Restrictions.eq("orgFrameId", shiftsVo.getOrgFrameId()));
        }
        return this.shiftsDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), Shifts.class);
    }

    @Override
    public Shifts saveOrUpdate(ShiftsVo shiftsVo) {
        Shifts shifts = new Shifts();
        BeanUtils.copyProperties(shiftsVo, shifts);
        if(StringUtils.isBlank(shifts.getId())){
            this.save(shifts);
        }else{
            this.update(shifts);
        }
        return shifts;
    }

}
