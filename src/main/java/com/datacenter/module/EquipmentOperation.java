package com.datacenter.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * @Description 设备运行情况统计表
 * @author xuezb
 * @date 2019年2月18日
 */
@Entity
@Table(name="dc_EquipmentOperation")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class EquipmentOperation extends BaseModule{
	
	private String title;          //标题
	private String formNumber;     //表单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDate;         //日期
    private Integer tollGate;      //收费站	(数据字典：dc_tollGate)
	private Integer cdgqzp;        //车道高清抓拍	(数据字典：dc_equipmentStatus,	1:车道使用正常	2:车道有故障，但不影响收费及发卡操作		3:车道故障，无法使用)
	private Integer zdfkj;         //自动发卡机
	private Integer mtcckcd;       //MTC出口车道
	private Integer etcckcd;       //ETC出口车道
	private Integer mtcrkcd;       //MTC入口车道
	private Integer etcrkcd;       //ETC入口车道
	private Integer jzcd;          //计重车道
	private String remark;         //备注
	@DateTimeFormat(pattern="HH:mm")
	private Date downTimeStart;    //车道停用时间段Start
	@DateTimeFormat(pattern="HH:mm")
	private Date downTimeEnd;      //车道停用时间段End
	private String ttId;           //主表id
	
	
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
	
	@Column(name = "toll_Gate", length=11)
	public Integer getTollGate() {
		return tollGate;
	}
	public void setTollGate(Integer tollGate) {
		this.tollGate = tollGate;
	}
	
	@Column(name = "cdgqzp_", length=11)
	public Integer getCdgqzp() {
		return cdgqzp;
	}
	public void setCdgqzp(Integer cdgqzp) {
		this.cdgqzp = cdgqzp;
	}
	
	@Column(name = "zdfkj_", length=11)
	public Integer getZdfkj() {
		return zdfkj;
	}
	public void setZdfkj(Integer zdfkj) {
		this.zdfkj = zdfkj;
	}
	
	@Column(name = "mtcckcd_", length=11)
	public Integer getMtcckcd() {
		return mtcckcd;
	}
	public void setMtcckcd(Integer mtcckcd) {
		this.mtcckcd = mtcckcd;
	}
	
	@Column(name = "etcckcd_", length=11)
	public Integer getEtcckcd() {
		return etcckcd;
	}
	public void setEtcckcd(Integer etcckcd) {
		this.etcckcd = etcckcd;
	}
	
	@Column(name = "mtcrkcd_", length=11)
	public Integer getMtcrkcd() {
		return mtcrkcd;
	}
	public void setMtcrkcd(Integer mtcrkcd) {
		this.mtcrkcd = mtcrkcd;
	}
	
	@Column(name = "etcrkcd_", length=11)
	public Integer getEtcrkcd() {
		return etcrkcd;
	}
	public void setEtcrkcd(Integer etcrkcd) {
		this.etcrkcd = etcrkcd;
	}
	
	@Column(name = "jzcd_", length=11)
	public Integer getJzcd() {
		return jzcd;
	}
	public void setJzcd(Integer jzcd) {
		this.jzcd = jzcd;
	}
	
	@Column(name = "remark_", length=250)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "down_Time_Start")
	public Date getDownTimeStart() {
		return downTimeStart;
	}
	public void setDownTimeStart(Date downTimeStart) {
		this.downTimeStart = downTimeStart;
	}
	
	@Column(name = "down_Time_End")
	public Date getDownTimeEnd() {
		return downTimeEnd;
	}
	public void setDownTimeEnd(Date downTimeEnd) {
		this.downTimeEnd = downTimeEnd;
	}
	
	@Column(name = "ttId", length=32)
	public String getTtId() {
		return ttId;
	}
	public void setTtId(String ttId) {
		this.ttId = ttId;
	}
	
	
}
