package com.urms.sms.module;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.common.base.module.BaseModule;

/**
 * @author zengcong
 * @Description: 短信模板
 * @date 2018/7/23 11:01
 */
@Entity
@org.hibernate.annotations.Table(comment = "短信模板", appliesTo = "UM_SMS_TEMPLATE")
@Table(name = "UM_SMS_TEMPLATE")
public class SmsTemplate extends BaseModule{

    private String templateName; //模板名称
    private String templateCode; //模板编码
    private String templateContent;  //内容
    private String templateType; //模板类别
    private String templateRemark;  //备注说明

    @Column(name = "template_Name", length = 20)
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Column(name = "template_Code", length = 50)
    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    @Column(name = "template_Content", length = 255)
	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	@Column(name = "template_Type", length = 50)
	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	@Column(name = "template_Remark", length = 200)
	public String getTemplateRemark() {
		return templateRemark;
	}

	public void setTemplateRemark(String templateRemark) {
		this.templateRemark = templateRemark;
	}

}
