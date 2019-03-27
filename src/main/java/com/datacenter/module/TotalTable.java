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
 * @Description 值班汇总表
 * @author xuezb
 * @date 2019年2月15日
 */
@Entity
@Table(name="dc_TotalTable")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class TotalTable extends BaseModule{
	
	private String title;		//标题
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date dutyDate;		//值班日期 (不能为空)
	
	
	@Column(name = "title_", length=50)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "duty_Date")
	public Date getDutyDate() {
		return dutyDate;
	}
	public void setDutyDate(Date dutyDate) {
		this.dutyDate = dutyDate;
	}
	
}
