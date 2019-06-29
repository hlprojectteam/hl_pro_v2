package com.datacenter.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.datacenter.module.TransferRegistration;

/**
 * @Description 
 * @author xuezb
 * @date 2019年2月18日
 */
public class TransferRegistrationVo extends TransferRegistration{
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDateStart;                  //日期Start
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDateEnd;               	 //日期End
	
	private String dutyDateStr;				//日期字符串

	private String keyword;				//关键字
	
	private String dictValue;				//新增字典值_本班次值班人员
	
	private String dictValue2;				//新增字典值_上班次值班人员


	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

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

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	public String getDictValue2() {
		return dictValue2;
	}

	public void setDictValue2(String dictValue2) {
		this.dictValue2 = dictValue2;
	}

}
