package com.suggest.vo;

import com.suggest.module.Suggest;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年4月21日
 *
 */
public class SuggestVo extends Suggest{
	
	private String rebackUserName;		//反馈人

	public String getRebackUserName() {
		return rebackUserName;
	}

	public void setRebackUserName(String rebackUserName) {
		this.rebackUserName = rebackUserName;
	}

}
