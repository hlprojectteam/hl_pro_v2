package com.urms.subsystem.module;


import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.urms.menu.module.Menu;

/**
 * @intruduction 系统定义 子系统
 * @author Mr.Wang
 * @Date 2016年1月21日上午9:06:53
 */
@Entity
@Table(name="UM_SUBSYSTEM")
public class Subsystem{
	private String sysName;//子系统名称
	private String sysCode;//子系统编码
	private Integer state;//状态
	private Integer order;//顺序
	private String memo;//备注
	
	private String id;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	
	private Set<Menu> menus = new TreeSet<Menu>();
	
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
	@Column(name = "sys_Name", length=32)
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	@Column(name = "sys_Code", length=32)
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	@Column(name = "state_")
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@Column(name = "memo_", length=512)
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Column(name = "order_")
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	@ManyToMany(cascade={CascadeType.MERGE},fetch=FetchType.LAZY)   
    @JoinTable(name = "um_subsystem_menu", joinColumns = { @JoinColumn(name = "subsystem_Id") }, inverseJoinColumns = { @JoinColumn(name = "menu_Id") })  
	public Set<Menu> getMenus() {
		return menus;
	}
	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}
}
