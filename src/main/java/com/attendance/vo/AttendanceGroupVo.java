package com.attendance.vo;

import com.attendance.module.AttendanceGroup;

public class AttendanceGroupVo extends AttendanceGroup {

    private String memberUserNames;      //参与考勤人员
    private String principalUserNames;   //考勤组负责人
    private String shiftsName;           //选择的班次


    public String getMemberUserNames() {
        return memberUserNames;
    }

    public void setMemberUserNames(String memberUserNames) {
        this.memberUserNames = memberUserNames;
    }

    public String getPrincipalUserNames() {
        return principalUserNames;
    }

    public void setPrincipalUserNames(String principalUserNames) {
        this.principalUserNames = principalUserNames;
    }

    public String getShiftsName() {
        return shiftsName;
    }

    public void setShiftsName(String shiftsName) {
        this.shiftsName = shiftsName;
    }
}
