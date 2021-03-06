package com.datacenter.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.datacenter.module.InfoThrough;

/**
 * @Description 
 * @author xuezb
 * @date 2019年2月18日
 */
public class InfoThroughVo extends InfoThrough{
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDateStart;                  //日期Start
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDateEnd;               	 //日期End
	
	private String dutyDateStr;				//日期字符串

	private String dictValue;				//新增字典值_信息来源
	private String dictValue2;				//新增字典值_通传方式
	private String dictValue3;				//新增字典值_信息类型
	private String dictValue4;				//新增字典值_值班人员

	private String keyword;				//关键字


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

	public String getDictValue3() {
		return dictValue3;
	}

	public void setDictValue3(String dictValue3) {
		this.dictValue3 = dictValue3;
	}

	public String getDictValue4() {
		return dictValue4;
	}

	public void setDictValue4(String dictValue4) {
		this.dictValue4 = dictValue4;
	}
}
