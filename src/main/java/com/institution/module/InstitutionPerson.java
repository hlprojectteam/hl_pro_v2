package com.institution.module;

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
 * @Description 制度与人员关系类
 * @author xm
 * @date 2016-10-8
 *
 */
@Entity
@Table(name="P_INSTITUTION_PERSON")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class InstitutionPerson extends BaseModule {
	
	private String personId;//人员id
	private String personName;//人员名称
	private String orgFrameId;//人员机构id
	private Institution institution;//关联制度材料
	
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
    @JoinColumn(name="INSTITUTION_ID")
	@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
	public Institution getInstitution() {
		return institution;
	}
	public void setInstitution(Institution institution) {
		this.institution = institution;
	}
	
	
}
