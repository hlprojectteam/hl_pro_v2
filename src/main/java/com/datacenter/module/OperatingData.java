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
 * @Description 营运数据
 * @author xuezb
 * @date 2019年2月15日
 */
@Entity
@Table(name="dc_OperatingData")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class OperatingData extends BaseModule{
	
	private String title;             //标题
	private String formNumber;        //表单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDate;            //日期
	private Integer tollGate;         //收费站		(数据字典：dc_tollGate_operation)
	private Integer totalTraffic;     //出口车流量_总车流
	private Integer ytkTraffic;       //出口车流量_其中粤通卡车流
	private Integer mobilePaymentTraffic;     //出口车流量_移动支付车流
	private Double generalIncome;     //收费额_总收费额		(精确度小数点后两位)
	private Double ytkIncome;         //收费额_其中粤通卡收入	(精确度小数点后两位)
	private Double mobilePaymentIncome;         //收费额_移动支付收入	(精确度小数点后两位)
	private String ttId;              //主表id
	


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
	
	@Column(name = "total_Traffic", length=8)
	public Integer getTotalTraffic() {
		return totalTraffic;
	}
	public void setTotalTraffic(Integer totalTraffic) {
		this.totalTraffic = totalTraffic;
	}
	
	@Column(name = "ytk_Traffic", length=8)
	public Integer getYtkTraffic() {
		return ytkTraffic;
	}
	public void setYtkTraffic(Integer ytkTraffic) {
		this.ytkTraffic = ytkTraffic;
	}

	@Column(name = "general_Income")
	public Double getGeneralIncome() {
		return generalIncome;
	}
	public void setGeneralIncome(Double generalIncome) {
		this.generalIncome = generalIncome;
	}
	
	@Column(name = "ytk_Income")
	public Double getYtkIncome() {
		return ytkIncome;
	}
	public void setYtkIncome(Double ytkIncome) {
		this.ytkIncome = ytkIncome;
	}
	
	@Column(name = "ttId", length=32)
	public String getTtId() {
		return ttId;
	}
	public void setTtId(String ttId) {
		this.ttId = ttId;
	}
	@Column(name = "mobile_Payment_Traffic", length=8)
	public Integer getMobilePaymentTraffic() {
		return mobilePaymentTraffic;
	}
	public void setMobilePaymentTraffic(Integer mobilePaymentTraffic) {
		this.mobilePaymentTraffic = mobilePaymentTraffic;
	}
	@Column(name = "mobile_Payment_Income")
	public Double getMobilePaymentIncome() {
		return mobilePaymentIncome;
	}
	public void setMobilePaymentIncome(Double mobilePaymentIncome) {
		this.mobilePaymentIncome = mobilePaymentIncome;
	}
	
}

