package com.common.inteceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.common.token.module.Token;
import com.common.token.service.ITokenService;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
/**
 * @intruduction  拦截器
 * @Date 2016年1月13日下午4:23:35
 */
public class MyInteceptor implements HandlerInterceptor{
	
	@Autowired
	public ITokenService tokenServiceImpl;
	
	@Autowired
	public IUserService userServiceImpl;
	
	/**
	 * 执行1
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object obj) throws Exception {
		User user = (User) request.getSession().getAttribute("user");
		request.getSession().removeAttribute("msg");
		if(user!=null){
			return true;		
		}else{
			//判断令牌是否过期
			String token=request.getHeader("token");
			if(isPermit(token,request)){
				return true;
			}
			
			request.getSession().setAttribute("msg", "您好:请登陆系统！");
			response.sendRedirect("/login.jsp"); 
			return false;
		}
	}

	/**
	 * 执行2
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,Object obj, ModelAndView mav) throws Exception {
	}
	
	/**
	 * 执行3
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object obj, Exception exp) throws Exception {
	}
	
	/**
	 * 
	 * @ClassName:MyInteceptor.java
	 * @Description:是否有权访问
	 * @param token
	 * @param request
	 * @return
	 * @author qinyongqian
	 * @date 2016-8-15
	 */
	private boolean isPermit(String token,HttpServletRequest request){
		if(token!=null){
			Token t= tokenServiceImpl.getToken(token);
			if(t!=null){
				//获取到令牌
				
				//判断令牌是否过期
	            Date dt1 = new Date();//当前时间
	            Date dt2 = t.getReleaseDate();//令牌生效时间
	            long diff = dt1.getTime() - dt2.getTime();
	            long days = diff / (1000 * 60 * 60 * 24);
	            if(days>3){
	            	//3天过期,重新登录获取新的token
	            	return false;
	            }
				
	            //重新取得session会话
				String userId=t.getUserId();
				User user= userServiceImpl.getEntityById(User.class, userId);
				if(user!=null){
					request.getSession().setAttribute("user", user);
					return true;
				}
			}
		}
		return false;
	}
}
