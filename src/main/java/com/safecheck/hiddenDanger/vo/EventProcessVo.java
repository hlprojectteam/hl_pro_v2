package com.safecheck.hiddenDanger.vo;


import java.util.List;

import com.safecheck.hiddenDanger.module.EventProcess;

/**
 * @Description EventInfoVo
 * @author xuezb
 * @Date 2018年5月23日 上午10:28:25
 */
public class EventProcessVo extends EventProcess{
    private List<String> imgUrls;
    private String videoUrl;
    
    private String epNowPersonDpName;	//处理人(当前节点)——所在部门名称
    

    public String getEpNowPersonDpName() {
		return epNowPersonDpName;
	}

	public void setEpNowPersonDpName(String epNowPersonDpName) {
		this.epNowPersonDpName = epNowPersonDpName;
	}

	public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
