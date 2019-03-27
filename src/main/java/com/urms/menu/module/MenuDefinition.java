package com.urms.menu.module;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.urms.role.module.Role;

/**
 * @intruduction 菜单功能按钮
 * @author Mr.Wang
 * @Date 2016年2月2日上午8:49:14
 */
@Entity
@Table(name="UM_MENU_DEFINITION")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
public class MenuDefinition implements Comparable<Object>{
	private String id;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	
	private String definitionName;//功能点名称
	private String definitionCode;//功能点编码
	private Integer order;//显示顺序
	private String icon;//图标
	private String colour;//按钮底色
	private String pageType;//页面类型
	private String definitionMethod;//方法
	private String memo;//备注
	
	private Menu menu;
	
	private Set<Role> roles = new TreeSet<Role>();
	
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
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime==null?new Date():createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "DEFINITION_NAME",length=32)	
	public String getDefinitionName() {
		return definitionName;
	}
	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}
	@Column(name = "DEFINITION_CODE",length=32)	
	public String getDefinitionCode() {
		return definitionCode;
	}
	public void setDefinitionCode(String definitionCode) {
		this.definitionCode = definitionCode;
	}
	@Column(name = "ORDER_")	
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	@Column(name = "ICON_",length=32)	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Column(name = "DEFINITION_METHOD",length=128)	
	public String getDefinitionMethod() {
		return definitionMethod;
	}
	public void setDefinitionMethod(String definitionMethod) {
		this.definitionMethod = definitionMethod;
	}
	@Column(name = "MEMO_",length=256)	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@ManyToOne(cascade={CascadeType.MERGE})           
    @JoinColumn(name="MENU_ID") 
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	@Column(name = "COLOUR_",length=32)	
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	@Column(name = "PAGE_TYPE",length=16)	
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	@ManyToMany(cascade={CascadeType.REFRESH}) 
    @JoinTable(name = "UM_ROLE_MENU_DEFINITION", joinColumns = { @JoinColumn(name = "MENU_DEFINITION_Id") }, inverseJoinColumns = { @JoinColumn(name = "role_Id") })  
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="urms_cache")
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	@Override
	public int compareTo(Object o) {
		return 1;
	}
	
}
