package com.education.module;

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
 * @intruduction 教育培训模块基础类
 * @author Will
 * @Date 上午9:57:47
 */
@Entity
@Table(name="P_EDUCATION_TRAIN")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Education extends BaseModule{
	private Integer type;	                 //在线培训类型：1、文档，2、视频，3、音频
	private Integer category;		         //内容分类：党教育、收费培训、基本培训
	private String title;	                 //标题
	private String description;	             //教育介绍
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")	
	private Date releaseDate;		         //发布时间
	private String createUserId;	         //创建者id
	private String cover;	                 //封面,缩略图
	private Integer visitNum;		         //查看次数
	private Integer downloadNum;	         //下载次数
	private String authorName;	             //作者名
	private Integer status;	                 //状态：0、撤回 ， 1、发布
	private Integer delFlag;		         //删除标志位
	private String remark;	                 //备注
	
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column(name = "TITLE",length=100)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name = "DESCRIPTION",length=1000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name = "CREATE_USER_ID",length=32)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "COVER",length=200)
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	@Column(name = "VISIT_NUM")
	public Integer getVisitNum() {
		return visitNum;
	}
	public void setVisitNum(Integer visitNum) {
		this.visitNum = visitNum;
	}
	@Column(name = "DOWNLOAD_NUM")
	public Integer getDownloadNum() {
		return downloadNum;
	}
	public void setDownloadNum(Integer downloadNum) {
		this.downloadNum = downloadNum;
	}
	@Column(name = "AUTHOR_NAME",length=20)
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	@Column(name = "STATUS")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "DEL_FLAG" ,length=4)
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	@Column(name = "REMARK",length=400)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "CATEGORY_" ,length=4)
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	@Column(name = "RELEASE_DATE")
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
}
