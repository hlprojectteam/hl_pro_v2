package com.institution.module;

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
 * @Description 制度通报实体类
 * @author qinyongqian
 * @date 2016-10-12
 *
 */
@Entity
@Table(name="P_INSTITUTION")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Institution extends BaseModule{

	private String createUserId;	//创建者id
	private Integer delFlag;		//删除标志位
	private String mainTitle;		//主标题
	private String viceTitle;		//副标题
	private String authorName;	    //作者名
	private byte[] content;	        //内容
	private Integer newSource;	    //新闻来源
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date releaseDate;		//发布时间
	private Integer status;	        //替换状态
	private Integer visitNum;		//查看次数
	private String cover;		    //缩略图
	private Integer category;	    //风采类型  
	
	@Column(name = "CREATE_USER_ID",length=32)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "DEL_FLAG" ,length=4)
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	@Column(name = "MAIN_TITLE",length=100)
	public String getMainTitle() {
		return mainTitle;
	}
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}
	@Column(name = "VICE_TITLE",length=100)
	public String getViceTitle() {
		return viceTitle;
	}
	public void setViceTitle(String viceTitle) {
		this.viceTitle = viceTitle;
	}
	@Column(name = "AUTHOR_NAME",length=20)
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	@Column(name = "CONTENT", columnDefinition = "MEDIUMBLOB",nullable=true)
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	@Column(name = "NEW_SOURCE")
	public Integer getNewSource() {
		return newSource;
	}
	public void setNewSource(Integer newSource) {
		this.newSource = newSource;
	}
	@Column(name = "RELEASE_DATE")
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "VISIT_NUM")
	public Integer getVisitNum() {
		return visitNum;
	}
	public void setVisitNum(Integer visitNum) {
		this.visitNum = visitNum;
	}
	@Column(name = "COVER",length=200)
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	@Column(name = "CATEGORY_" ,length=4)
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	
}
