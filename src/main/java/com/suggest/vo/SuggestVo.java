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
	private Integer sex;// 性别
	private String photo;   //头像

	public String getRebackUserName() {
		return rebackUserName;
	}

	public void setRebackUserName(String rebackUserName) {
		this.rebackUserName = rebackUserName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
