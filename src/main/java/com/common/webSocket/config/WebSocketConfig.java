package com.common.webSocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.common.webSocket.SystemWebSocketHandler;
import com.common.webSocket.interceptor.HandshakeInterceptor;

/**
 * @intruduction 消息处理中心
 * @author Dic
 * @Date 2016年4月30日下午5:07:26
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new SystemWebSocketHandler(),"/echo").addInterceptors(new HandshakeInterceptor()); //支持websocket 的访问链接
        registry.addHandler(new SystemWebSocketHandler(),"/sockjs/echo").addInterceptors(new HandshakeInterceptor()).withSockJS(); //不支持websocket的访问链接
	}

}
