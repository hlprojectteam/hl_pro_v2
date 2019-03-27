package com.datacenter.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.datacenter.module.OperatingData;

/**
 * @Description 
 * @author xuezb
 * @date 2019年2月18日
 */
public class OperatingDataVo extends OperatingData{
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDateStart;                  //日期Start
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDateEnd;              		 //日期End
	
	private String dutyDateStr;				//日期字符串
	
	
	public String getDutyDateStr() {
		return dutyDateStr;
	}
	public void setDutyDateStr(String dutyDateStr) {
		this.dutyDateStr = dutyDateStr;
	}
	public Date getDutyDateStart() {
		return dutyDateStart;
	}
	public void setDutyDateStart(Date dutyDateStart) {
		this.dutyDateStart = dutyDateStart;
	}
	public Date getDutyDateEnd() {
		return dutyDateEnd;
	}
	public void setDutyDateEnd(Date dutyDateEnd) {
		this.dutyDateEnd = dutyDateEnd;
	}

}
