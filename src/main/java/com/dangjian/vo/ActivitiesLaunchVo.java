package com.dangjian.vo;

import java.util.List;

import com.dangjian.module.ActivitiesLaunch;

/**
 * 
 * @Description 
 * @author qinyongqian
 * @date 2018年12月29日
 *
 */
public class ActivitiesLaunchVo extends ActivitiesLaunch{
	
	private String title="";
	private Integer frequency ;     //活动频率   数据字典：dj_activities_frequency
	private String launchDateStr="";
	private String year="";          //周期年
	private Integer completionTimes ; //周期内完成次数     
	private String timeQuantum="";          //时间段  一个月，一个季度，半年，一年
	private String timeQuantumValue="";          //时间段的值   一个月：01，一个季度：1，半年：1，一年：2019
	private Integer isReach ;       //周期内是否达标     
	private String branchName;      //开展的部门名称
	private List<String> imgUrls;		//图片附件的url
	
	public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }
	
	public Integer getCompletionTimes() {
		return completionTimes;
	}

	public void setCompletionTimes(Integer completionTimes) {
		this.completionTimes = completionTimes;
	}

	public Integer getIsReach() {
		return isReach;
	}

	public void setIsReach(Integer isReach) {
		this.isReach = isReach;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public String getLaunchDateStr() {
		return launchDateStr;
	}

	public void setLaunchDateStr(String launchDateStr) {
		this.launchDateStr = launchDateStr;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getTimeQuantum() {
		return timeQuantum;
	}

	public void setTimeQuantum(String timeQuantum) {
		this.timeQuantum = timeQuantum;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getTimeQuantumValue() {
		return timeQuantumValue;
	}

	public void setTimeQuantumValue(String timeQuantumValue) {
		this.timeQuantumValue = timeQuantumValue;
	}


}
