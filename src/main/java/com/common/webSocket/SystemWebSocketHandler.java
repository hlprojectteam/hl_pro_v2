package com.common.webSocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.urms.user.module.User;

public class SystemWebSocketHandler implements WebSocketHandler{

	private static final Logger logger = Logger.getLogger(SystemWebSocketHandler.class);
	
	public static final List<WebSocketSession> wsUsers = new ArrayList<WebSocketSession>();
	public static final List<User> users = new ArrayList<User>();
	
	@Override
	public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
		User user = (User) webSocketSession.getAttributes().get("WB_USER");
		logger.debug("用户:"+user.getLoginName()+" 退出系统，时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		wsUsers.remove(webSocketSession);
		users.remove(user);
	}
	
	//初次链接成功执行
	@Override
	public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
		User user = (User) webSocketSession.getAttributes().get("WB_USER");
		logger.debug("用户:"+user.getLoginName()+" 连接webSocket成功，时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		wsUsers.add(webSocketSession);
		users.add(user);
	}

	//接受消息处理消息
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
//		sendMessageToUsers(new TextMessage(webSocketMessage.getPayload() + ""));
	}

	@Override
	public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
		if(webSocketSession.isOpen()){
			webSocketSession.close();
		}
		User user = (User) webSocketSession.getAttributes().get("WB_USER");
		logger.debug(user.getUserName()+"链接出错，关闭链接...时间："+new Date());
		wsUsers.remove(webSocketSession);
		users.remove(user);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	 /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(String message) {
        for (WebSocketSession wsUser : wsUsers) {
            try {
                if (wsUser.isOpen()) {
                	wsUser.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userId, String message) {
        for (WebSocketSession wsUser : wsUsers) {
        	User user = (User)wsUser.getAttributes().get("WB_USER");
            if (user.getId().equals(userId)) {
                try {
                    if (wsUser.isOpen()) {
                    	wsUser.sendMessage(new TextMessage(message));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
