package com.datacenter.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.datacenter.module.TotalTable;

/**
 * @Description 
 * @author xuezb
 * @date 2019年2月18日
 */
public class TotalTableVo extends TotalTable{
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDateStart;                  //日期Start
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDateEnd;               	 //日期End
	
	
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
