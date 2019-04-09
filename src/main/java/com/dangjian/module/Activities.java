package com.dangjian.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 活动表
 * @author qinyongqian
 * @date 2018年12月2日
 *
 */
@Entity
@Table(name="P_DJ_ACTIVITIES")
public class Activities extends BaseModule{
	
	private String title ;		    //活动标题，名称
	private Integer frequency ;     //活动频率   数据字典：dj_activities_frequency 1一周次 2一月次 3一个季度一次 4半年一次 5一个季度或者半年一次 6一年一次 7无限制
	private Integer points ;        //活动分值
	private String cover;           //缩略图
	private String content;         //描述
	private Integer order;          //排序
	
	@Column(name = "TITLE" ,length=50)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "FREQUENCY" ,length=4)
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	@Column(name = "POINTS" ,length=4)
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	@Column(name = "COVER" ,length=200)
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	@Column(name = "CONTENT" ,length=1000)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "ORDER_" ,length=2)
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	

}
