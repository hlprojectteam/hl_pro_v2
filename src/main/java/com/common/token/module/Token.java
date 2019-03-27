package com.common.token.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;
/**
 * 
 * @Description 令牌类 （应用在APP访问请求认证）
 * @author qinyongqian
 * @date 2018-3-22
 *
 */
@Entity
@Table(name="P_TOKEN")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Token extends BaseModule{

	private String userId;	//关联的用户ID
	private String signature; //令牌字符
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date releaseDate;  //token生效时间
	
	@Column(name = "USER_ID",length=32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name = "SIGNATURE",length=50)
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	@Column(name = "RELEASE_DATE")
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
}
