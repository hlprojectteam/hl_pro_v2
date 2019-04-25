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
 * @Description 涉路施工
 * @author xuezb
 * @date 2019年2月15日
 */
@Entity
@Table(name="dc_RoadWork")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class RoadWork extends BaseModule{
	
	private String title;                     //标题
	private String formNumber;                //表单编号
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDate;                    //日期
	@DateTimeFormat(pattern="HH:mm")
	private Date approachTime;                //进场时间
	@DateTimeFormat(pattern="HH:mm")
	private Date departureTime;               //撤场时间
	private String unitName;                  //施工单位名称
	private String relationPerson;            //现场负责人
	private String relationPhone;			  //联系方式
	private Integer positionAttributes;       //位置属性	(数据字典：dc_positionAttributes, 1:主线	2：收费站)
	private String specificLocation;          //具体位置
	private String constructionContent;       //施工内容
	private String jeevesSituation;           //占道情况
	@DateTimeFormat(pattern="HH:mm")
	private Date checkTime;                   //检查时间
	private String checker;                   //检查人员
	private String description;               //施工现场情况简要描述
	private String rectificationMeasures;     //整改措施	(可以为空)
	private String reportedSituation;         //施工报备情况
	private String ttId;                      //主表id
	
	private double x;						  //施工坐标X
	private double y;						  //施工坐标y
	
	
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
	
	@Column(name = "approach_Time")
	public Date getApproachTime() {
		return approachTime;
	}
	public void setApproachTime(Date approachTime) {
		this.approachTime = approachTime;
	}
	
	@Column(name = "departure_Time")
	public Date getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}
	
	@Column(name = "unit_Name", length=50)
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	@Column(name = "relation_Person", length=50)
	public String getRelationPerson() {
		return relationPerson;
	}
	public void setRelationPerson(String relationPerson) {
		this.relationPerson = relationPerson;
	}
	
	@Column(name = "position_Attributes", length=11)
	public Integer getPositionAttributes() {
		return positionAttributes;
	}
	public void setPositionAttributes(Integer positionAttributes) {
		this.positionAttributes = positionAttributes;
	}
	
	@Column(name = "specific_Location", length=50)
	public String getSpecificLocation() {
		return specificLocation;
	}
	public void setSpecificLocation(String specificLocation) {
		this.specificLocation = specificLocation;
	}
	
	@Column(name = "construction_Content", length=100)
	public String getConstructionContent() {
		return constructionContent;
	}
	public void setConstructionContent(String constructionContent) {
		this.constructionContent = constructionContent;
	}
	
	@Column(name = "jeeves_Situation", length=100)
	public String getJeevesSituation() {
		return jeevesSituation;
	}
	public void setJeevesSituation(String jeevesSituation) {
		this.jeevesSituation = jeevesSituation;
	}
	
	@Column(name = "check_Time")
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	
	@Column(name = "checker_", length=20)
	public String getChecker() {
		return checker;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	
	@Column(name = "description_", length=300)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "rectification_Measures", length=200)
	public String getRectificationMeasures() {
		return rectificationMeasures;
	}
	public void setRectificationMeasures(String rectificationMeasures) {
		this.rectificationMeasures = rectificationMeasures;
	}
	
	@Column(name = "reported_Situation", length=100)
	public String getReportedSituation() {
		return reportedSituation;
	}
	public void setReportedSituation(String reportedSituation) {
		this.reportedSituation = reportedSituation;
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

	@Column(name = "relation_Phone")
	public String getRelationPhone() {
		return relationPhone;
	}

	public void setRelationPhone(String relationPhone) {
		this.relationPhone = relationPhone;
	}
}
