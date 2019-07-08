package com.attendance.module;

import com.common.base.module.BaseModule;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author qinyongqian
 * @Description 考勤班次
 * @date 2019年5月17日
 */
@Entity
@Table(name = "KQ_SHIFTS")
public class Shifts extends BaseModule {

    private String shiftName;            //班次名称
    private String orgFrameId;           //关联收费站ID(关联 OrgFrame 的id),每个收费站都有自己的班次
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date workTimeStart;           //上班打卡时间
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date workTimeEnd;             //下班打卡时间
    private Integer isHaveRestTime;       //是否有休息时间      [数据字典:isNot  1:是, 2:否]
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date restTimeStart;           //休息时间开始
    @DateTimeFormat(pattern = "HH:mm:ss")
    private Date restTimeEnd;             //休息时间结束
    private Integer remindTimeType;       //上班打卡前几分钟提醒 [数据字典:Before_Punching]
    private Integer isNotClockOut;        //下班是否不用打卡     [数据字典:isNot  1:是, 2:否]


    @Column(name = "shift_Name", length = 10)
    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    @Column(name = "orgFrame_Id", length = 32)
    public String getOrgFrameId() {
        return orgFrameId;
    }

    public void setOrgFrameId(String orgFrameId) {
        this.orgFrameId = orgFrameId;
    }

    @Column(name = "workTime_Start")
    public Date getWorkTimeStart() {
        return workTimeStart;
    }

    public void setWorkTimeStart(Date workTimeStart) {
        this.workTimeStart = workTimeStart;
    }

    @Column(name = "workTime_End")
    public Date getWorkTimeEnd() {
        return workTimeEnd;
    }

    public void setWorkTimeEnd(Date workTimeEnd) {
        this.workTimeEnd = workTimeEnd;
    }

    @Column(name = "is_HaveRestTime", length = 2)
    public Integer getIsHaveRestTime() {
        return isHaveRestTime;
    }

    public void setIsHaveRestTime(Integer isHaveRestTime) {
        this.isHaveRestTime = isHaveRestTime;
    }

    @Column(name = "restTime_Start")
    public Date getRestTimeStart() {
        return restTimeStart;
    }

    public void setRestTimeStart(Date restTimeStart) {
        this.restTimeStart = restTimeStart;
    }

    @Column(name = "restTime_End")
    public Date getRestTimeEnd() {
        return restTimeEnd;
    }

    public void setRestTimeEnd(Date restTimeEnd) {
        this.restTimeEnd = restTimeEnd;
    }

    @Column(name = "remindTime_Type", length = 2)
    public Integer getRemindTimeType() {
        return remindTimeType;
    }

    public void setRemindTimeType(Integer remindTimeType) {
        this.remindTimeType = remindTimeType;
    }

    @Column(name = "isNot_ClockOut", length = 2)
    public Integer getIsNotClockOut() {
        return isNotClockOut;
    }

    public void setIsNotClockOut(Integer isNotClockOut) {
        this.isNotClockOut = isNotClockOut;
    }
}
