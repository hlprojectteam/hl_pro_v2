package com.attendance.module;

import com.common.base.module.BaseModule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author qinyongqian
 * @Description 考勤组
 * @date 2019年5月17日
 */
@Entity
@Table(name = "KQ_ATTENDANCE_GROUP")
public class AttendanceGroup extends BaseModule {

    private String groupName;                      //考勤组名称	如“仑头站晚点名考勤”
    private Integer groupType;                     //考勤组类型	字典：Attendance_GroupType 如：晚点名，工作班
    private String orgFrameId;                     //关联收费站ID（每个收费站都有自己的考勤组)
    private String memberUserIds;                  //参与考勤人员 Set<user>
    private String principalUserIds;               //考勤组负责人, 保存userid，多个用“，”隔开，最多10个
    private String shiftsId;                       //选择的班次,关联kq_shifts主键
    private String address;                        //考勤地址,设置打卡的坐标地址
    private Double x;                              //考勤地址坐标X
    private Double y;                              //考勤地址坐标y
    private Integer effectiveRange;                //打卡有效范围  字典：Effective_Range实际打卡坐标与设定坐标在多少米范围内有效


    @Column(name = "group_Name", length = 20)
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Column(name = "group_Type", length = 2)
    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    @Column(name = "orgFrame_Id", length = 32)
    public String getOrgFrameId() {
        return orgFrameId;
    }

    public void setOrgFrameId(String orgFrameId) {
        this.orgFrameId = orgFrameId;
    }

    @Column(name = "member_UserIds", length = 400)
    public String getMemberUserIds() {
        return memberUserIds;
    }

    public void setMemberUserIds(String memberUserIds) {
        this.memberUserIds = memberUserIds;
    }

    @Column(name = "principal_UserIds", length = 400)
    public String getPrincipalUserIds() {
        return principalUserIds;
    }

    public void setPrincipalUserIds(String principalUserIds) {
        this.principalUserIds = principalUserIds;
    }

    @Column(name = "shifts_Id", length = 32)
    public String getShiftsId() {
        return shiftsId;
    }

    public void setShiftsId(String shiftsId) {
        this.shiftsId = shiftsId;
    }

    @Column(name = "address_", length = 50)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "x_")
    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    @Column(name = "y_")
    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Column(name = "effective_Range", length = 4)
    public Integer getEffectiveRange() {
        return effectiveRange;
    }

    public void setEffectiveRange(Integer effectiveRange) {
        this.effectiveRange = effectiveRange;
    }
}
