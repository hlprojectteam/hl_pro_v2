package com.urms.sysConfig.module;


import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 超级密码
 */
@Entity
@Table(name="UM_SUPER_PASSWORD")
public class SuperPassword {
	
	private String id;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date createTime;
	private String plaintext;//明文密码
	private String encryptedText;//加密
	private Integer isEenabled;//是否启用 1=启用

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
	@Column(name = "PLAIN_TEXT",length=16)
	public String getPlaintext() {
		return plaintext;
	}

	public void setPlaintext(String plaintext) {
		this.plaintext = plaintext;
	}
	@Column(name = "ENCRYPTED_TEXT",length=64)
	public String getEncryptedText() {
		return encryptedText;
	}

	public void setEncryptedText(String encryptedText) {
		this.encryptedText = encryptedText;
	}
	@Column(name = "IS_EENABLED")
	public Integer getIsEenabled() {
		return isEenabled;
	}

	public void setIsEenabled(Integer isEenabled) {
		this.isEenabled = isEenabled;
	}
}
