package com.datacenter.module;

import com.common.base.module.BaseModule;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Description 联网关键设备运行状态日常检查表
 * @author xuezb
 * @date 2019年2月18日
 */
@Entity
@Table(name="dc_EquipmentStatus")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class EquipmentStatus extends BaseModule {

    private String title;          //标题
    private String formNumber;     //表单编号
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date dutyDate;         //日期

    @DateTimeFormat(pattern="HH:mm")
    private Date checkTime;         //记录时间

    private Integer eqStatusR1;     //设备状态   (数据字典: dc_eqStatus;  1:正常 2:异常)
    private Integer eqStatusR2;
    private Integer eqStatusE1;
    private Integer eqStatusA;
    private Integer eqStatusB;
    private Integer eqStatusC;
    private Integer eqStatusD;

    private Double successRateR1;   //标识成功率
    private Double successRateR2;
    private Double successRateE1;
    private Double successRateA;
    private Double successRateB;
    private Double successRateC;
    private Double successRateD;

    private Integer mislabelNumR1;   //误标数量
    private Integer mislabelNumR2;
    private Integer mislabelNumE1;
    private Integer mislabelNumA;
    private Integer mislabelNumB;
    private Integer mislabelNumC;
    private Integer mislabelNumD;

    private String remark;          //备注
    private String ttId;            //主表id


    @Column(name = "title_", length=50)
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "form_Number", length=12)
    public String getFormNumber() {
        return formNumber;
    }
    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    @Column(name = "duty_Date")
    public Date getDutyDate() {
        return dutyDate;
    }
    public void setDutyDate(Date dutyDate) {
        this.dutyDate = dutyDate;
    }

    @Column(name = "check_Time")
    public Date getCheckTime() {
        return checkTime;
    }
    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    @Column(name = "eqStatus_R1", length=11)
    public Integer getEqStatusR1() {
        return eqStatusR1;
    }
    public void setEqStatusR1(Integer eqStatusR1) {
        this.eqStatusR1 = eqStatusR1;
    }

    @Column(name = "eqStatus_R2", length=11)
    public Integer getEqStatusR2() {
        return eqStatusR2;
    }
    public void setEqStatusR2(Integer eqStatusR2) {
        this.eqStatusR2 = eqStatusR2;
    }

    @Column(name = "eqStatus_E1", length=11)
    public Integer getEqStatusE1() {
        return eqStatusE1;
    }
    public void setEqStatusE1(Integer eqStatusE1) {
        this.eqStatusE1 = eqStatusE1;
    }

    @Column(name = "eqStatus_A", length=11)
    public Integer getEqStatusA() {
        return eqStatusA;
    }
    public void setEqStatusA(Integer eqStatusA) {
        this.eqStatusA = eqStatusA;
    }

    @Column(name = "eqStatus_B", length=11)
    public Integer getEqStatusB() {
        return eqStatusB;
    }
    public void setEqStatusB(Integer eqStatusB) {
        this.eqStatusB = eqStatusB;
    }

    @Column(name = "eqStatus_C", length=11)
    public Integer getEqStatusC() {
        return eqStatusC;
    }
    public void setEqStatusC(Integer eqStatusC) {
        this.eqStatusC = eqStatusC;
    }

    @Column(name = "eqStatus_D", length=11)
    public Integer getEqStatusD() {
        return eqStatusD;
    }
    public void setEqStatusD(Integer eqStatusD) {
        this.eqStatusD = eqStatusD;
    }


    @Column(name = "successRate_R1")
    public Double getSuccessRateR1() {
        return successRateR1;
    }
    public void setSuccessRateR1(Double successRateR1) {
        this.successRateR1 = successRateR1;
    }

    @Column(name = "successRate_R2")
    public Double getSuccessRateR2() {
        return successRateR2;
    }
    public void setSuccessRateR2(Double successRateR2) {
        this.successRateR2 = successRateR2;
    }

    @Column(name = "successRate_E1")
    public Double getSuccessRateE1() {
        return successRateE1;
    }
    public void setSuccessRateE1(Double successRateE1) {
        this.successRateE1 = successRateE1;
    }

    @Column(name = "successRate_A")
    public Double getSuccessRateA() {
        return successRateA;
    }
    public void setSuccessRateA(Double successRateA) {
        this.successRateA = successRateA;
    }

    @Column(name = "successRate_B")
    public Double getSuccessRateB() {
        return successRateB;
    }
    public void setSuccessRateB(Double successRateB) {
        this.successRateB = successRateB;
    }

    @Column(name = "successRate_C")
    public Double getSuccessRateC() {
        return successRateC;
    }
    public void setSuccessRateC(Double successRateC) {
        this.successRateC = successRateC;
    }

    @Column(name = "successRate_D")
    public Double getSuccessRateD() {
        return successRateD;
    }
    public void setSuccessRateD(Double successRateD) {
        this.successRateD = successRateD;
    }

    @Column(name = "mislabelNum_R1")
    public Integer getMislabelNumR1() {
        return mislabelNumR1;
    }
    public void setMislabelNumR1(Integer mislabelNumR1) {
        this.mislabelNumR1 = mislabelNumR1;
    }

    @Column(name = "mislabelNum_R2")
    public Integer getMislabelNumR2() {
        return mislabelNumR2;
    }
    public void setMislabelNumR2(Integer mislabelNumR2) {
        this.mislabelNumR2 = mislabelNumR2;
    }

    @Column(name = "mislabelNum_E1")
    public Integer getMislabelNumE1() {
        return mislabelNumE1;
    }
    public void setMislabelNumE1(Integer mislabelNumE1) {
        this.mislabelNumE1 = mislabelNumE1;
    }

    @Column(name = "mislabelNum_A")
    public Integer getMislabelNumA() {
        return mislabelNumA;
    }
    public void setMislabelNumA(Integer mislabelNumA) {
        this.mislabelNumA = mislabelNumA;
    }

    @Column(name = "mislabelNum_B")
    public Integer getMislabelNumB() {
        return mislabelNumB;
    }
    public void setMislabelNumB(Integer mislabelNumB) {
        this.mislabelNumB = mislabelNumB;
    }

    @Column(name = "mislabelNum_C")
    public Integer getMislabelNumC() {
        return mislabelNumC;
    }
    public void setMislabelNumC(Integer mislabelNumC) {
        this.mislabelNumC = mislabelNumC;
    }

    @Column(name = "mislabelNum_D")
    public Integer getMislabelNumD() {
        return mislabelNumD;
    }
    public void setMislabelNumD(Integer mislabelNumD) {
        this.mislabelNumD = mislabelNumD;
    }

    @Column(name = "remark_", length=150)
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "ttId", length=32)
    public String getTtId() {
        return ttId;
    }
    public void setTtId(String ttId) {
        this.ttId = ttId;
    }
}
