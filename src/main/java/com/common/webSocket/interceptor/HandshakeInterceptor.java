package com.common.webSocket.interceptor;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;

import com.urms.user.module.User;

public class HandshakeInterceptor implements org.springframework.web.socket.server.HandshakeInterceptor{

	//进入hander之前的拦截
	@Override
	public boolean beforeHandshake(ServerHttpRequest request,ServerHttpResponse response, WebSocketHandler webSocketHandler,Map<String, Object> map) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession(false);
			User user = (User) session.getAttribute("user");//当前登录人员信息
			if (session != null) {
				//使用子系统编码+登陆登陆用户名 区分WebSocketHandler，以便定向发送消息
				map.put("WB_USER",user);
			}
		}
		return true;
	}
	
	//进入hander之后的拦截
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler webSocketHandler, Exception e) {
	}

}
