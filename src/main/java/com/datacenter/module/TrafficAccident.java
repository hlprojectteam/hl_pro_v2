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
 * @Description 交通事故
 * @author xuezb
 * @date 2019年2月18日
 */
@Entity
@Table(name="dc_TrafficAccident")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class TrafficAccident extends BaseModule{
	
	private String title;                 //标题
    private String formNumber;            //表单编号
    @DateTimeFormat(pattern="yyyy-MM-dd")                 
	private Date dutyDate;                //日期
	private Integer weather;              //天气情况	(数据字典：dc_weather, 1:晴  2:阴  3:多云  4:小雨 5:中雨 6:大雨 7:暴雨 8:雷阵雨  9:霜冻 10:雾 )
	@DateTimeFormat(pattern="HH:mm")                     
	private Date receiptTime;             //接报时间
	private Integer receiptWay;           //接报方式	(数据字典：dc_reportedWay, 1：服务热线 2：内线电话 3：路面监控)
	private Integer source;               //消息来源	(数据字典：dc_receiptWay  1：服务热线 2：内线电话 3：路面监控 4：其它（人工输入）)
	private String accidentSite;          //事故地点
	private Integer accidentType;         //事故类型	(数据字典：dc_accidentType 1：追尾 2：自撞 3：刮擦 4：碰撞 5：翻车 6：自燃 7：其它（人工输入）)
	private String carType;               //车辆类型	(数据字典：dc_carType,	1:小型客车	2:中型货车	3:大型货车) 
	private Integer involveCarNum;        //涉及车辆
	private String involvePlates;         //涉事车牌
	private Integer minorInjuryNum;       //轻伤人数
	private Integer seriousInjuryNum;     //重伤人数
	private Integer deathNum;             //死亡人数
	private Integer laneClosedNum;        //封闭车道	(数据字典: dc_laneClosedNum; 1：0； 2：0.5； 3：1； 4：2 ； 5：3； 6：4 )
	private String roadLoss;              //路产损失
	private double roadIndemnity;         //路产赔偿
	private String claimNote;             //索赔单号
	private String accidentDetails;       //事故详情
	private String remark;                //备注
	private String ttId;                  //主表id
	
	private double x;				 	  //坐标X
	private double y;				      //坐标y
	
	
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
	
	@Column(name = "weather_", length=11)
	public Integer getWeather() {
		return weather;
	}
	public void setWeather(Integer weather) {
		this.weather = weather;
	}
	
	@Column(name = "receipt_Time")
	public Date getReceiptTime() {
		return receiptTime;
	}
	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}
	
	@Column(name = "receipt_Way", length=11)
	public Integer getReceiptWay() {
		return receiptWay;
	}
	public void setReceiptWay(Integer receiptWay) {
		this.receiptWay = receiptWay;
	}
	
	@Column(name = "source_", length=11)
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	
	@Column(name = "accident_Site", length=50)
	public String getAccidentSite() {
		return accidentSite;
	}
	public void setAccidentSite(String accidentSite) {
		this.accidentSite = accidentSite;
	}
	
	@Column(name = "accident_Type", length=11)
	public Integer getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(Integer accidentType) {
		this.accidentType = accidentType;
	}
	
	@Column(name = "car_Type", length=20)
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	
	@Column(name = "involveCar_Num", length=3)
	public Integer getInvolveCarNum() {
		return involveCarNum;
	}
	public void setInvolveCarNum(Integer involveCarNum) {
		this.involveCarNum = involveCarNum;
	}
	
	@Column(name = "involve_Plates", length=50)
	public String getInvolvePlates() {
		return involvePlates;
	}
	public void setInvolvePlates(String involvePlates) {
		this.involvePlates = involvePlates;
	}
	
	@Column(name = "minorInjury_Num", length=3)
	public Integer getMinorInjuryNum() {
		return minorInjuryNum;
	}
	public void setMinorInjuryNum(Integer minorInjuryNum) {
		this.minorInjuryNum = minorInjuryNum;
	}
	
	@Column(name = "seriousInjury_Num", length=3)
	public Integer getSeriousInjuryNum() {
		return seriousInjuryNum;
	}
	public void setSeriousInjuryNum(Integer seriousInjuryNum) {
		this.seriousInjuryNum = seriousInjuryNum;
	}
	
	@Column(name = "death_Num", length=3)
	public Integer getDeathNum() {
		return deathNum;
	}
	public void setDeathNum(Integer deathNum) {
		this.deathNum = deathNum;
	}
	
	@Column(name = "laneClosed_Num", length=3)
	public Integer getLaneClosedNum() {
		return laneClosedNum;
	}
	public void setLaneClosedNum(Integer laneClosedNum) {
		this.laneClosedNum = laneClosedNum;
	}
	
	@Column(name = "road_Loss", length=50)
	public String getRoadLoss() {
		return roadLoss;
	}
	public void setRoadLoss(String roadLoss) {
		this.roadLoss = roadLoss;
	}
	
	@Column(name = "road_Indemnity")
	public double getRoadIndemnity() {
		return roadIndemnity;
	}
	public void setRoadIndemnity(double roadIndemnity) {
		this.roadIndemnity = roadIndemnity;
	}
	
	@Column(name = "claim_Note", length=20)
	public String getClaimNote() {
		return claimNote;
	}
	public void setClaimNote(String claimNote) {
		this.claimNote = claimNote;
	}
	
	@Column(name = "accident_Details", length=500)
	public String getAccidentDetails() {
		return accidentDetails;
	}
	public void setAccidentDetails(String accidentDetails) {
		this.accidentDetails = accidentDetails;
	}
	
	@Column(name = "remark_", length=250)
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
	
	@Column(name = "x_")
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	
	@Column(name = "y_")
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
}
