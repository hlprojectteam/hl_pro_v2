package com.education.vo;

import com.education.module.Education;

/**
 * 
 * @Description 教育培训模块基础类VO
 * @author qinyongqian
 * @date 2016-10-8
 *
 */
public class EducationVo extends Education{
	
	private String createTimeStr;
	private String releaseDateStr;
	private String filePath;
	private String fileName;
	private Long fileSize;
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
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	

}
