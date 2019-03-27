package com.urms.sms.module;

import com.common.base.module.BaseModule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zengcong
 * @Description: 短信记录
 * @date 2018/7/23 15:05
 */
@Entity
@org.hibernate.annotations.Table(comment = "短信记录", appliesTo = "UM_SMS_MESSAGE_LOG")
@Table(name = "UM_SMS_MESSAGE_LOG")
public class SmsMessageLog extends BaseModule{

    private String acceptPhoneNum;   // 接受号码
    private String smsContent; // 短信内容
    private String tempplateCode;  // 模板编码
    private String getbackSMS;  // 返回消息
    private Integer sendType; //发送类型   1主动发送，2请求发送
    private Integer sendState; //本短信状态  1发送成功，2发送失败
    private String logRemark; //失败原因，手机号码为空，编码异常，短信内容为空，余数不足，包含关键字 等等
    
    @Column(name = "accept_PhoneNum", length = 15)
	public String getAcceptPhoneNum() {
		return acceptPhoneNum;
	}
	public void setAcceptPhoneNum(String acceptPhoneNum) {
		this.acceptPhoneNum = acceptPhoneNum;
	}
	
	@Column(name = "sms_Content", length = 255)
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	
	@Column(name = "tempplate_Code", length = 50)
	public String getTempplateCode() {
		return tempplateCode;
	}
	public void setTempplateCode(String tempplateCode) {
		this.tempplateCode = tempplateCode;
	}
	
	@Column(name = "getback_SMS", length = 100)
	public String getGetbackSMS() {
		return getbackSMS;
	}
	public void setGetbackSMS(String getbackSMS) {
		this.getbackSMS = getbackSMS;
	}
	
	@Column(name = "send_Type")
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	
	@Column(name = "send_State")
	public Integer getSendState() {
		return sendState;
	}
	public void setSendState(Integer sendState) {
		this.sendState = sendState;
	}
	
	@Column(name = "log_remark", length = 100)
	public String getLogRemark() {
		return logRemark;
	}
	public void setLogRemark(String logRemark) {
		this.logRemark = logRemark;
	}
}
