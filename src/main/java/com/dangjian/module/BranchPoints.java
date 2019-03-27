package com.dangjian.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 党支部
 * @author qinyongqian
 * @date 2018年12月2日
 *
 */
@Entity
@Table(name="P_DJ_BRANCH_POINTS")
public class BranchPoints extends BaseModule{
	
	private String branchId;		//支部id
	@DateTimeFormat(pattern="yyyy-MM-dd")	
	private Date year;		        //年份
	private Integer points ;        //分值
	private String remark;		    //备注
	
	@Column(name = "BRANCH_ID" ,length=32)
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	@Column(name = "YEAR")
	public Date getYear() {
		return year;
	}
	public void setYear(Date year) {
		this.year = year;
	}
	@Column(name = "POINTS" ,length=4)
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	@Column(name = "REMARK" ,length=200)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
