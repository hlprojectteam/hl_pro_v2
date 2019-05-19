package com.common.mobile.banner.module;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description手机首页BANNER设置类实体
 * @author qinyongqian
 * @date 2016-10-17
 *
 */
@Entity
@Table(name="UM_MOBILE_BANNER")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Banner extends BaseModule{

	private String createUserId;	         //创建者id
	private Integer category;		         //banner分类:1首页，2内容页
	private String title;	                 //标题
	private String cover;	                 //封面,缩略图
	private Integer order;		             //排序
	private Integer status;	                 //状态：0、撤回 ， 1、发布
	
	@Column(name = "CREATE_USER_ID",length=32)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "CATEGORY_" ,length=4)
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	@Column(name = "TITLE_" ,length=32)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "COVER",length=200)
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	@Column(name = "ORDER_",length=200)
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
