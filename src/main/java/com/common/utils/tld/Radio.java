package com.common.utils.tld;


import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.common.utils.cache.Cache;
import com.urms.dataDictionary.module.CategoryAttribute;


/**
 * @日期 2015-3-17下午02:02:16
 * @描述 单选
 */
@SuppressWarnings("serial")
public class Radio extends TagSupport {
	
	private String dictKey;//字典编码
	private String id;
	private String name;
	private String value;//选中的值
	private String classStyle;
	private String style;//样式
	private String onchange;//触发事件
	private String onclick;//触发事件
	private String disabled;//是否置灰
	
	private String fieldlong;//字段长度 只在自定义表单用到
	private String fieldtype;//字段类型 只在自定义表单用到
	
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		StringBuffer stringBuffer = new StringBuffer();
		try {
			Set<CategoryAttribute> set = Cache.getDictByCode.get(dictKey);
			if(StringUtils.isNotBlank(value)){//如果有值情况下
				if(set!=null){
					for (CategoryAttribute ca : set) {
						stringBuffer.append("<label class=\"radio-inline\"><input type=\"radio\" name=\""+name+"\" onchange=\""+onchange+"\" onclick=\""+onclick+"\" ");
						stringBuffer.append(" value=\""+ca.getAttrKey()+"\" ");
						if(ca.getAttrKey().equals(value))
							stringBuffer.append(" checked=\"checked\" ");
						stringBuffer.append(">");
						stringBuffer.append(ca.getAttrValue());
						stringBuffer.append("</label>");
					}
				}else{
					stringBuffer.append("<input type=\"radio\"/>请配置数据字典");		
				}
			}else{//没有值情况下 如果有默认值选上
				if(set!=null){
					for (CategoryAttribute ca : set) {
						stringBuffer.append("<label class=\"radio-inline\"><input type=\"radio\" name=\""+name+"\" onchange=\""+onchange+"\" onclick=\""+onclick+"\" ");
						stringBuffer.append(" value=\""+ca.getAttrKey()+"\" ");
						if(ca.getIsDefault()!=null && ca.getIsDefault()==1){
							stringBuffer.append(" checked=\"checked\" ");
						}
						stringBuffer.append(">");
						stringBuffer.append(ca.getAttrValue());
						stringBuffer.append("</label>");
					}
				}else{
					stringBuffer.append("<input type=\"radio\"/>请配置数据字典");		
				}
			}
			out.println(stringBuffer.toString());
		} catch (Exception e) {
			 throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	 }

	public String getDictKey() {
		return dictKey;
	}

	public void setDictKey(String dictKey) {
		this.dictKey = dictKey;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	public String getStyle() {
		return style;
	}

	public String getOnchange() {
		return onchange;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassStyle() {
		return classStyle;
	}

	public void setClassStyle(String classStyle) {
		this.classStyle = classStyle;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFieldtype() {
		return fieldtype;
	}

	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}

	public String getFieldlong() {
		return fieldlong;
	}

	public void setFieldlong(String fieldlong) {
		this.fieldlong = fieldlong;
	}

}
