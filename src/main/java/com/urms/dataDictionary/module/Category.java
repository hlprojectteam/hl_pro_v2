package com.urms.dataDictionary.module;


import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.common.base.module.BaseModule;

@Entity
@Table(name="UM_CATEGORY")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Category extends BaseModule{
	
	private String pId;//父id
	private String categoryName;//目录名称
	private String categoryCode;//目录编码
	private String categoryType;//目录类型
	private String pIds;//所有父id
	private String pNames;//所有父name
	private Integer isLeaf;//是否叶节点
	private Integer order;//排序
	private Integer level;//层数
	private Set<CategoryAttribute> categoryAttributes = new TreeSet<CategoryAttribute>();
	private Integer isGrid; //当目录类型为行政区划时 用来区分行政区划和网格  =1是网格 否则为普通行政区划
	
	@Column(name = "CATEGORY_NAME",length=128)
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	@Column(name = "CATEGORY_CODE",length=128)
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	@Column(name = "ORDER_")
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	@Column(name = "P_ID", length=32)
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	@Column(name = "P_IDS", length=1000)
	public String getpIds() {
		return pIds;
	}
	public void setpIds(String pIds) {
		this.pIds = pIds;
	}
	@Column(name = "P_NAMES", length=1000)
	public String getpNames() {
		return pNames;
	}
	public void setpNames(String pNames) {
		this.pNames = pNames;
	}
	@Column(name = "IS_LEAF")
	public Integer getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
	@Column(name = "CATEGORY_TYPE",length=64)
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	@Column(name = "LEVEL_")
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	@OneToMany(cascade=CascadeType.MERGE,mappedBy="category",fetch=FetchType.LAZY)
	@OrderBy("order asc")
	public Set<CategoryAttribute> getCategoryAttributes() {
		return categoryAttributes;
	}
	public void setCategoryAttributes(Set<CategoryAttribute> categoryAttributes) {
		this.categoryAttributes = categoryAttributes;
	}
	@Column(name = "IS_GRID")
	public Integer getIsGrid() {
		return isGrid;
	}

	public void setIsGrid(Integer isGrid) {
		this.isGrid = isGrid;
	}
}
