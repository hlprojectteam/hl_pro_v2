package com.education.module;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 教育材料与人员关系类
 * @author qinyongqian
 * @date 2016-10-8
 *
 */
@Entity
@Table(name="P_EDUCATION_PERSON")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class EducationPerson extends BaseModule{

	private String personId;//人员id
	private String personName;//人员名称
	private String orgFrameId;//人员机构id
	private Education education;//关联教育材料
	
	@Column(name = "PERSON_ID",length=32)
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	@Column(name = "PERSON_NAME",length=32)
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	@Column(name = "ORGFRAME_ID",length=32)
	public String getOrgFrameId() {
		return orgFrameId;
	}
	public void setOrgFrameId(String orgFrameId) {
		this.orgFrameId = orgFrameId;
	}
	@ManyToOne(cascade={CascadeType.MERGE})           
    @JoinColumn(name="EDUCATION_ID")
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public Education getEducation() {
		return education;
	}
	public void setEducation(Education education) {
		this.education = education;
	}
	
	
	
}
