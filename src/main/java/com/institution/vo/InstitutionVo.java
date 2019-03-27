package com.institution.vo;

import com.institution.module.Institution;

/**
 * 
 * @Description 企业制度基础类VO
 * @author xm
 * @date 2016-10-8
 *
 */
public class InstitutionVo  extends Institution{
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
