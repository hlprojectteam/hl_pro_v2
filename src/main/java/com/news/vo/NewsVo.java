package com.news.vo;

import com.news.module.News;
/**
 * 
 * @Description 企业风采VO
 * @author xm
 * @date 2016-10-8
 *
 */
public class NewsVo extends News{
	private String contentStr;
	private String createTimeStr;
	private String releaseDateStr;
	private String filePath;
	private String fileName; 
	
	public String getContentStr() {
		return contentStr;
	}
	public void setContentStr(String contentStr) {
		this.contentStr = contentStr;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getReleaseDateStr() {
		return releaseDateStr;
	}
	public void setReleaseDateStr(String releaseDateStr) {
		this.releaseDateStr = releaseDateStr;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
