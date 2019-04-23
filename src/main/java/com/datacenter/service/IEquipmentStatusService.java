package com.datacenter.service;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.datacenter.module.EquipmentStatus;
import com.datacenter.vo.EquipmentStatusVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Date;
import java.util.List;

/**
 * @Description 联网关键设备运行状态日常检查表 service接口
 * @author xuezb
 * @date 2019年2月18日
 */
public interface IEquipmentStatusService extends IBaseService {

    /**
     * 联网设备状态	分页
     * @param page
     * @param rows
     * @param equipmentStatusVo
     * @return
     * @author xuezb
     * @Date 2019年2月18日
     */
    public Pager queryEntityList(Integer page, Integer rows, EquipmentStatusVo equipmentStatusVo);

    /**
     * 联网设备状态	保存or更新
     * @param equipmentStatusVo
     * @return
     * @author xuezb
     * @Date 2019年2月18日
     */
    public EquipmentStatus saveOrUpdate(EquipmentStatusVo equipmentStatusVo);

    /**
     * 联网设备状态	删除
     * @param ttId	主表(TotalTable)Id
     * @return
     * @author xuezb
     * @Date 2019年2月19日
     */
    public int deleteByTtId(String ttId);

    /**
     * 联网设备状态	日期(dutyDate)修改
     * @param ttId		主表(TotalTable)Id
     * @param dutyDate	主表(TotalTable)dutyDate
     * @return
     * @author xuezb
     * @Date 2019年2月19日
     */
    public int updateDutyDate(String ttId, Date dutyDate);

    /**
     * 联网设备状态	list
     * @param equipmentStatusVo
     * @return
     * @author xuezb
     * @Date 2019年3月5日
     */
    public List<EquipmentStatus> queryEntityList(EquipmentStatusVo equipmentStatusVo);

    /**
     * 联网设备状态	导出Excel
     * @param equipmentStatusVo
     * @return
     * @author xuezb
     * @Date 2019年3月5日
     */
    public HSSFWorkbook export(EquipmentStatusVo equipmentStatusVo);
}
