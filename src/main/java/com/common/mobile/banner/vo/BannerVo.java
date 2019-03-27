package com.common.mobile.banner.vo;

import com.common.mobile.banner.module.Banner;

/**
 * 
 * @Description 横幅VO
 * @author xm
 * @date 2016-10-8
 *
 */
public class BannerVo extends Banner{
	private String createTimeStr;
	private String filePath;
	private String fileName; 
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
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
