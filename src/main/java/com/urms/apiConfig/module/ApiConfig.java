package com.urms.apiConfig.module;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.common.base.module.BaseModule;

/**
 * API 综合接口配置
 * 
 * @author Mr.Wang 
 * @date 2018-07-25 16:33:15
 */

@Entity
@org.hibernate.annotations.Table(comment = "API综合接口配置", appliesTo = "um_api_config")
@Table(name = "um_api_config")
public class ApiConfig extends BaseModule {

	private Integer apiType; // API类型  1IP，2短信，3地图，4天气，5视频会话
	private String apiName; // API平台名字 ，聚合数据，美联软通
	private String apiSite; // API平台官网
	private String apiUrl; // 请求链接
	private String apiKey; // 密钥
	private String apiUserName; //用户名
	private String apiPassword; //登录密码
	private String apiToken; // 标记
	private String sendPhoneNum; //发送号码
	private Integer apiURLEncoder; // 请求文本的编码   GBK ,UTF8,UTF-8
	private Integer apiRequestMethod; //请求方式  POST,GET
	private String apiAllURL; // 请求全称
	private Integer apiTotleNum; //调用最大次数
	private Integer apiUseNum; //已用次数
	private Integer apiIsPay; //是否收费  isNot ，1是，2否
	private Integer apiState; // API状态， 1启用，2停用
	
	@Column(name = "api_Type")
	public Integer getApiType() {
		return apiType;
	}
	public void setApiType(Integer apiType) {
		this.apiType = apiType;
	}
	
	@Column(name = "api_Name", length = 50)
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	
	@Column(name = "api_Site", length = 50)
	public String getApiSite() {
		return apiSite;
	}
	public void setApiSite(String apiSite) {
		this.apiSite = apiSite;
	}
	
	@Column(name = "api_Url", length = 200)
	public String getApiUrl() {
		return apiUrl;
	}
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	@Column(name = "api_Key", length = 100)
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	
	@Column(name = "api_UserName", length = 100)
	public String getApiUserName() {
		return apiUserName;
	}
	public void setApiUserName(String apiUserName) {
		this.apiUserName = apiUserName;
	}
	
	@Column(name = "api_Password", length = 100)
	public String getApiPassword() {
		return apiPassword;
	}
	public void setApiPassword(String apiPassword) {
		this.apiPassword = apiPassword;
	}
	
	@Column(name = "api_Token", length = 50)
	public String getApiToken() {
		return apiToken;
	}
	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}
	
	@Column(name = "send_PhoneNum", length = 30)
	public String getSendPhoneNum() {
		return sendPhoneNum;
	}
	public void setSendPhoneNum(String sendPhoneNum) {
		this.sendPhoneNum = sendPhoneNum;
	}
	
	@Column(name = "api_URLEncoder")
	public Integer getApiURLEncoder() {
		return apiURLEncoder;
	}
	public void setApiURLEncoder(Integer apiURLEncoder) {
		this.apiURLEncoder = apiURLEncoder;
	}
	
	@Column(name = "api_RequestMethod")
	public Integer getApiRequestMethod() {
		return apiRequestMethod;
	}
	public void setApiRequestMethod(Integer apiRequestMethod) {
		this.apiRequestMethod = apiRequestMethod;
	}
	
	@Column(name = "api_AllURL",length = 255)
	public String getApiAllURL() {
		return apiAllURL;
	}
	public void setApiAllURL(String apiAllURL) {
		this.apiAllURL = apiAllURL;
	}
	
	@Column(name = "api_TotleNum")
	public Integer getApiTotleNum() {
		return apiTotleNum;
	}
	public void setApiTotleNum(Integer apiTotleNum) {
		this.apiTotleNum = apiTotleNum;
	}
	
	@Column(name = "api_UseNum")
	public Integer getApiUseNum() {
		return apiUseNum;
	}
	public void setApiUseNum(Integer apiUseNum) {
		this.apiUseNum = apiUseNum;
	}
	
	@Column(name = "api_State")
	public Integer getApiState() {
		return apiState;
	}
	public void setApiState(Integer apiState) {
		this.apiState = apiState;
	}
	
	@Column(name = "api_IsPay")
	public Integer getApiIsPay() {
		return apiIsPay;
	}
	public void setApiIsPay(Integer apiIsPay) {
		this.apiIsPay = apiIsPay;
	}
	
	
}
