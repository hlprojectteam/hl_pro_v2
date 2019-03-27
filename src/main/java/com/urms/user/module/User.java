package com.urms.user.module;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.role.module.Role;

@Entity
@Table(name = "UM_USER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "urms_cache")
public class User extends BaseModule {
	private String loginName;// 登录名
	private String password;// 密码 
	private Integer state;// 状态 数据字典key:state
	private String userName;// 真实姓名
	private Integer sex;// 性别 数据字典key:sex
	private Integer age;// 年龄
	private Integer nation;// 民族 数据字典key:national
	private String  originPlace;         //籍贯
	private Integer educationBackground; //学历 数据字典key:population_backgroud
	private Integer degree; //学位 数据字典key:population_degree
	private Integer certificatesType;// 证件类型 数据字典key:population_Certificate
	private String certificatesNum;// 证件号码
	@DateTimeFormat(pattern="yyyy-MM-dd")	
	private Date birthday;//生日
	
	private String mobilePhone;// 手提电话
	private String telephone;// 固定电话
	private String email;// Email
	private String qq;// qq号码
	private String address;// 地址
	private String memo;// 备注
	private String jobNumber;// 员工编号
	@DateTimeFormat(pattern="yyyy-MM-dd")	
	private Date workTime;//工作时间，某年开始工作
	private Integer personalIdentity ; //个人身份 数据字典key:population_personalIdentity
	private Integer frontLineSituation ; //一线情况 数据字典key:population_frontLineSituation

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;// 最后一次登录时间
	private Integer loginTimes;// 登录次数 新增初始化为0
	private String loginIp;// 登录ip

	private Integer type;// 用户类型 1：超级管理员 2：子系统管理员 3：各子后台系统用户 4：系统前台用户（有需要时使用）

	private OrgFrame orgFrame;
	private Set<Role> roles = new TreeSet<Role>();
	
	private String avatarPathUpload;//头像虚拟路径
	private Integer dataSource; // 数据来源   1:WEB端注册 2：手机APP注册，3公众号注册/小程序

	@Column(name = "LOGIN_NAME", length = 128)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name = "PASSWORD_", length = 128)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "STATE_")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "USER_NAME", length = 16)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "SEX_")
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "AGE_")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name = "NATION_")
	public Integer getNation() {
		return nation;
	}

	public void setNation(Integer nation) {
		this.nation = nation;
	}

	@Column(name = "CERTIFICATES_TYPE")
	public Integer getCertificatesType() {
		return certificatesType;
	}

	public void setCertificatesType(Integer certificatesType) {
		this.certificatesType = certificatesType;
	}

	@Column(name = "CERTIFICATES_NUM", length = 64)
	public String getCertificatesNum() {
		return certificatesNum;
	}

	public void setCertificatesNum(String certificatesNum) {
		this.certificatesNum = certificatesNum;
	}

	@Column(name = "MOBILE_PHONE", length = 20)
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "TELEPHONE_", length = 20)
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "E_MAIL", length = 128)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "QQ_", length = 20)
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "ADDRESS_", length = 256)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "MEMO_", length = 2048)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "last_Login_Time")
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Column(name = "login_Times")
	public Integer getLoginTimes() {
		return loginTimes == null ? 0 : loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	@Column(name = "login_Ip", length = 64)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@Column(name = "type_")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@ManyToMany
	@JoinTable(name = "um_user_role", joinColumns = { @JoinColumn(name = "user_Id") }, inverseJoinColumns = { @JoinColumn(name = "role_Id") })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "urms_cache")
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "ORGFRAME_ID")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "urms_cache")
	public OrgFrame getOrgFrame() {
		return orgFrame;
	}

	public void setOrgFrame(OrgFrame orgFrame) {
		this.orgFrame = orgFrame;
	}

	@Column(name = "JOB_NUMBER", length = 64)
	public String getJobNumber() {
		return this.jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	@Column(name = "AVATAR_PATH_UPLOAD",length=256)
	public String getAvatarPathUpload() {
		return avatarPathUpload;
	}
	public void setAvatarPathUpload(String avatarPathUpload) {
		this.avatarPathUpload = avatarPathUpload;
	}
	//获取数据源 1电脑；2手机；3其他；
	@Column(name="DATA_SOURCE",length = 3)
	public Integer getDataSource() {
		return dataSource;
	}

	public void setDataSource(Integer dataSource) {
		this.dataSource = dataSource;
	}
	@Column(name = "ORIGIN_PLACE", length = 50)
	public String getOriginPlace() {
		return originPlace;
	}

	public void setOriginPlace(String originPlace) {
		this.originPlace = originPlace;
	}

	@Column(name = "EDUCATION_BACKGROUND", length = 2)
	public Integer getEducationBackground() {
		return educationBackground;
	}

	public void setEducationBackground(Integer educationBackground) {
		this.educationBackground = educationBackground;
	}

	@Column(name = "DEGREE", length = 2)
	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	@Column(name = "BIRTHDAY")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "WORK_TIME")
	public Date getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Date workTime) {
		this.workTime = workTime;
	}

	@Column(name = "PERSONAL_IDENTITY", length = 2)
	public Integer getPersonalIdentity() {
		return personalIdentity;
	}

	public void setPersonalIdentity(Integer personalIdentity) {
		this.personalIdentity = personalIdentity;
	}

	@Column(name = "FRONTLINE_SITUATION", length = 2)
	public Integer getFrontLineSituation() {
		return frontLineSituation;
	}

	public void setFrontLineSituation(Integer frontLineSituation) {
		this.frontLineSituation = frontLineSituation;
	}
}
