package com.common.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.common.utils.Common;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class MessageJpush {
	
	protected static final Logger LOG = LoggerFactory.getLogger(MessageJpush.class);
	private static final String appKey ="a8f6b37186cce1b9892f44af";
	private static final String masterSecret = "bdac3ff2111dc46c27f4c634";
	
    /**
     * 
     * @ClassName:MessageJpush.java
     * @Description:事件推送消息
     * @param title 通知标题
     * @param msgContent 事件内容
     * @param Tag 推送的标签，如 有eventTag这一个标签的人都收到，不指定标签时设置为NULL
     * @param Alias 推送的标注，标注一般是每个用户唯一的，如标注为用户ID，只有这个标注的人才收到，不指定标注时指定为NULL
     */
	@SuppressWarnings({ "deprecation", "static-access" })
	public static void sendCommonMsg(String title,com.common.message.module.Message msg){
		 String[] Alias ={};
		 if(StringUtils.isNotBlank(msg.getAlias())){
			 Alias = msg.getAlias().split(",");
		 }
		 String[] Tags = {};
		 if(StringUtils.isNotBlank(msg.getTags())){
			 Tags = msg.getTags().split(",");
		 }
		 Map<String, String> json =new HashMap<String, String>();
		 json.put("type", msg.getType().toString());
		 json.put("msgId", msg.getId());
		 json.put("content", msg.getContent());
		 json.put("title", title);
		 JPushClient jpushClient = new JPushClient(masterSecret,appKey, 3);
	        
	     PushPayload payload = null;
	     Builder builder= PushPayload.newBuilder();
	     //选择手机平台
	     String platform=Common.msg_platform;
	     if(platform.equals("1")){
		     builder.setPlatform(Platform.ios());
	     }else if(platform.equals("2")){
		     builder.setPlatform(Platform.android());
	     }else if(platform.equals("3")){
		     builder.setPlatform(Platform.android_ios());
	     }else{
	    	 builder.setPlatform(Platform.ios());//默认是IOS
	     }
	     builder.setNotification(Notification.newBuilder()
		                		.addPlatformNotification(AndroidNotification.newBuilder()
	                				.setAlert(title)
	                				.addExtras(json)
	                				.build())
		                		.addPlatformNotification(IosNotification.newBuilder()
	                                .setAlert(title)
//	                                .incrBadge(1)
	                                .setSound("happy")
	                                .addExtras(json)
	                                .build())
	                        .build());
		                		
	     //选择是否生产环境
	     String isApnsProduction=Common.msg_ApnsProduction;
	     if(isApnsProduction.equals("1")){
	    	 builder.setOptions(Options.newBuilder().setApnsProduction(true).build());//指IOS生产环境推送
	     }else{
	    	 builder.setOptions(Options.newBuilder().setApnsProduction(false).build());//指IOS开发环境推送
	     }
	     //选择消息是否在APP前台展示
	     String setMessage=Common.msg_setMessage;
	     if(setMessage.equals("1")){
	    	 builder.setMessage(Message.newBuilder().setMsgContent(msg.getContent()).addExtras(json).build());//当APP在前台时，用消息在页面上传输
	     }
	     
	     if(Alias.length == 0 && Tags.length > 0){
	    	 builder.setAudience(Audience.tag(Tags));
	     }else if(Alias.length > 0 && Tags.length == 0){
	    	 builder.setAudience(Audience.alias(Alias));
	     }else if(Alias.length > 0 && Tags.length > 0){
	    	 builder.setAudience(Audience.tag(Tags).alias(Alias));
	     }else{
	    	 builder.setAudience(Audience.all());
	     }
	     
	     payload = builder.build();
	     
	     try {
	         PushResult result = jpushClient.sendPush(payload);
	         LOG.info("Got result - " + result);
//	         return true;
	            
	     } catch (APIConnectionException e) {
	         LOG.error("Connection error. Should retry later. ", e);
//	         return false;
	            
	     } catch (APIRequestException e) {
	         LOG.error("Error response from JPush server. Should review and fix it. ", e);
	         LOG.info("HTTP Status: " + e.getStatus());
	         LOG.info("Error Code: " + e.getErrorCode());
	         LOG.info("Error Message: " + e.getErrorMessage());
	         LOG.info("Msg ID: " + e.getMsgId());
//	         return false;
	     }
	}

}
