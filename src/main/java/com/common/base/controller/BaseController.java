package com.common.base.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.urms.user.module.User;

public class BaseController {
	
	public static final Logger logger = LoggerFactory.getLogger(BaseController.class);  
	@Resource
	private HttpServletRequest request;
	@Resource
	private HttpServletResponse response;
	@Resource
	private HttpSession httpSession;
	
	
	/**
	 * 说明：
	 * 创建时间:2015-11-3 下午12:14:36
	 */
	public void print(Object str){
		try {
			this.response.getWriter().print(str);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 获得当前session User对象
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月27日上午11:08:00
	 */
	public User getSessionUser(){
		return (User)this.getHttpSession().getAttribute("user");
	}
	
//	public UserExtend getSessionUserExtend(){
//		return (UserExtend)this.getHttpSession().getAttribute("userExtend");
//	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

}
