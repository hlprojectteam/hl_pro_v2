package com.dangjian.module;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.common.base.module.BaseModule;

/**
 * 
 * @Description 党建组织简介
 * @author qinyongqian
 * @date 2019年1月22日
 * 
 */
@Entity
@Table(name = "P_DJ_INTRODUCTION")
public class Introduction extends BaseModule {

	private String mainTitle; // 主标题
	private String authorName; // 作者名
	private byte[] content; // 内容
	private Integer newSource; // 新闻来源 (数据字典:news_Source)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date releaseDate; // 发布时间
	private Integer status; // 状态 (数据字典:news_Status) 1 未发布，2已发布，3作废
	private Integer visitNum; // 查看次数
	private String cover; // 缩略图(图片上传路径url)
	private Integer category; // 类型 1架构图 2党务公开 3党支部制度4换届情况
	private Integer showPlace; // 展示位置 (数据字典:news_ShowPlace)
	private Integer isTop; // 是否置顶 (数据字典:isNot) 0：否 1：是 如果都是1，则按时间倒序排列
							// (当状态为作废时,置顶取消)
	private Integer lable; // 文章标记 (数据字典:news_Lable) 如 “推荐”，“热门”
	private String branchId; // 所对应支部ID

	@Column(name = "show_Place", length = 11)
	public Integer getShowPlace() {
		return showPlace;
	}

	public void setShowPlace(Integer showPlace) {
		this.showPlace = showPlace;
	}

	@Column(name = "is_Top", length = 11)
	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	@Column(name = "news_lable", length = 11)
	public Integer getLable() {
		return lable;
	}

	public void setLable(Integer lable) {
		this.lable = lable;
	}

	@Column(name = "MAIN_TITLE", length = 100)
	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	@Column(name = "AUTHOR_NAME", length = 20)
	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	@Column(name = "CONTENT", columnDefinition = "MEDIUMBLOB", nullable = true)
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

	@Column(name = "COVER", length = 200)
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	@Column(name = "CATEGORY_", length = 4)
	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}
	@Column(name = "BRANCH_ID", length = 32)
	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

}
