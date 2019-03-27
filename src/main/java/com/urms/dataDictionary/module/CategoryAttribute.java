package com.urms.dataDictionary.module;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="UM_CATEGORYATTRIBUTE")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class CategoryAttribute {
	private String id;
	private String attrKey;//键
	private String attrValue;//值
	private Integer order;//排序
	private Integer isDefault;//默认  1:是
	private Category category;
	@Id
    @GeneratedValue(generator = "paymentableGenerator")       
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")   
	@Column(name = "ID", nullable = false,length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "ORDER_")
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	@Column(name = "ATTR_KEY", length=128)
	public String getAttrKey() {
		return attrKey;
	}
	public void setAttrKey(String attrKey) {
		this.attrKey = attrKey;
	}
	@Column(name = "ATTR_VALUE", length=128)
	public String getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
	@ManyToOne(cascade={CascadeType.MERGE})           
    @JoinColumn(name="CATEGORY_ID") 
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	@Column(name = "IS_DEFAULT")
	public Integer getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
	
}
