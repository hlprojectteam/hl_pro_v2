package com.dangjian.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 党支部
 * @author qinyongqian
 * @date 2018年12月2日
 *
 */
@Entity
@Table(name="P_DJ_BRANCH")
public class Branch  extends BaseModule{
	
	private String branchName;		//支部名称
	private String remark;		//备注
	private Integer order; //排序
	
	@Column(name = "BRANCH_NAME" ,length=10)
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	@Column(name = "REMARK" ,length=256)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "ORDER_" ,length=2)
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	
	

}
