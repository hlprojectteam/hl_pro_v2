package com.common.message.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.o.common.beans.BeanUtils;

import com.common.base.controller.BaseController;
import com.common.message.MessageJpush;
import com.common.message.module.Message;
import com.common.message.module.MessageReaded;
import com.common.message.service.IMessageReadedService;
import com.common.message.service.IMessageService;
import com.common.message.vo.MessageReadedVo;
import com.common.message.vo.MessageVo;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.role.module.Role;
import com.urms.user.module.User;

@Controller
@RequestMapping(value="/message")
public class MessageController extends BaseController{
	@Autowired
	public IMessageService messageServiceImpl;
	@Autowired
	public IMessageReadedService messageReadedServiceImpl;
	
	/**
	 * 
	 * @Description: 返回列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @return
	 * @date 2016-8-15 上午11:55:32
	 */
	@RequestMapping(value="/message_list")
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode){
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/message/message_list";
	}
	/**
	 * 
	 * @Description: 列表查询
	 * @param request
	 * @param response
	 * @param messageVo
	 * @param page
	 * @param rows
	 * @date 2016-8-15 上午11:57:17
	 */
	@RequestMapping(value="/message_load")
	public void load(HttpServletRequest request,HttpServletResponse response,MessageVo messageVo,Integer page,Integer rows){
		JSONObject json = new JSONObject();
		json.put("result", false);
		User user = this.getSessionUser();
		messageVo.setAlias(user.getId());
		try{
			if(user.getType()>2){//不是管理员时候
				if(user.getRoles() !=null && user.getRoles().size()>0){
					StringBuffer tags = new StringBuffer();
					for(Role role : user.getRoles()){
						tags.append(role.getRoleCode()+",");
					}
					messageVo.setTags(tags.deleteCharAt(tags.length()-1).toString());
				}
			}
			Pager pager = this.messageServiceImpl.queryEntityList(page, rows, messageVo);
			json.put("total", pager.getRowCount());
			JsonConfig config = new JsonConfig();
			String[] excludes = new String[] {"alias","creatorId","creatorName","sender","sysCode",
					"tags"}; // 列表排除信息内容字段，减少传递时间
			config.setExcludes(excludes);
			config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
			json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
			json.put("result", true);
		}catch(Exception e){
			e.printStackTrace();
			json.put("result", false);
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param messageVo
	 * @方法：@param days
	 * @描述：获取当前时间多少天内的消息
	 * @return
	 * @author: qinyongqian
	 * @date:2019年6月5日
	 */
	@RequestMapping(value="/message_load_indays")
	public void messageLoadIndays(HttpServletRequest request,HttpServletResponse response,MessageVo messageVo,int days){
		JSONObject json = new JSONObject();
		json.put("result", false);
		User user = this.getSessionUser();
		messageVo.setAlias(user.getId());
		try{
			List<Message> list = this.messageServiceImpl.queryEntityList(messageVo, days);
			if(list!=null){
				json.put("total", list.size());
				JsonConfig config = new JsonConfig();
				String[] excludes = new String[] {"content","creatorId","creatorName","sysCode",
						"tags","alias","sender"}; // 列表排除信息内容字段，减少传递时间
				config.setExcludes(excludes);
				config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
				json.put("rows", JSONArray.fromObject(list,config));
				json.put("result", true);
			}
		}catch(Exception e){
			e.printStackTrace();
			json.put("result", false);
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @Description: 
	 * @param request
	 * @param messageVo
	 * @return
	 * @date 2016-8-15 下午1:58:15
	 */
	@RequestMapping(value="/message_edit")
	public String edit(HttpServletRequest request,HttpServletResponse response,MessageVo messageVo){
		String returnUrl = "";
		if(StringUtils.isNotBlank(messageVo.getId())){
			try{
				Message message = this.messageServiceImpl.queryEntityById(messageVo.getId());
				BeanUtils.copyProperties(message, messageVo);
				messageVo = formatVo(messageVo);
				request.setAttribute("messageVo", messageVo);
				returnUrl = "/page/message/message_edit";
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			request.setAttribute("messageVo", messageVo);
			returnUrl = "/page/message/message_add";
		}
		return returnUrl;
	}
	
	/**
	 * 
	 * @Description: 保存发送消息
	 * @param request
	 * @param messageVo
	 * @date 2016-8-15 下午3:09:31
	 */
	@RequestMapping("/message_save")
	public void saveOrUpdate(HttpServletRequest request,HttpServletResponse response,MessageVo messageVo){
		JsonObject json = new JsonObject();
		try{
			Message message = new Message();
			if(messageVo.getType() == null){
				messageVo.setType(1);
			}
			User user = this.getSessionUser();
			messageVo.setCreatorId(user.getId());
			if(StringUtils.isBlank(messageVo.getSender())){
				messageVo.setSender(user.getUserName());
			}
			BeanUtils.copyProperties(messageVo, message);
			this.messageServiceImpl.saveOrUpdate(message);
			sendMsg(message);
			json.addProperty("id", message.getId());
			json.addProperty("result", true);
		}catch (Exception e) {
			e.printStackTrace();
			json.addProperty("result", false);
		}finally{
			this.print(json);
		}
	}
	
	/**
	 * 
	 * @Description: 删除消息
	 * @param response
	 * @param ids
	 * @date 2016-8-15 下午3:17:05
	 */
	@RequestMapping(value="message_delete")
	public void delete(HttpServletResponse response,String ids) {
		this.messageServiceImpl.deleteMessage(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	
	/********************************已读消息 start******************************************/
	@RequestMapping("/messageRead_save")
	public void saveOrUpdateMessageRead(HttpServletRequest request,HttpServletResponse response,MessageReadedVo messageReadedVo){
		JsonObject json = new JsonObject();
		try{
			MessageReaded messageReaded=messageReadedServiceImpl.queryEntityByUserId(messageReadedVo.getUserId());
			if(messageReaded==null){
				//已读消息不存在，则添加记录
				messageReaded=new MessageReaded();
				messageReaded.setUserId(messageReadedVo.getUserId());
				messageReaded.setNewReadMsg(messageReadedVo.getNewReadMsg());
				messageReaded.setReadMsgs(messageReadedVo.getNewReadMsg());
				if(StringUtils.isNotBlank(messageReadedVo.getUpdateTimeStr())){
					messageReaded.setUpdateTime(DateUtil.format(messageReadedVo.getUpdateTimeStr(), "yyyy-MM-dd HH:mm:ss"));
				}
				this.messageReadedServiceImpl.saveOrUpdate(messageReaded);
			}else{
				//已读消息存在
				if(!messageReaded.getReadMsgs().contains(messageReadedVo.getNewReadMsg())){
					//库中不包括这次的消息ID，则追加入库
					messageReaded.setReadMsgs(messageReaded.getReadMsgs()+","+messageReadedVo.getNewReadMsg());
					messageReaded.setNewReadMsg(messageReadedVo.getNewReadMsg());
					if(StringUtils.isNotBlank(messageReadedVo.getUpdateTimeStr())){
						messageReaded.setUpdateTime(DateUtil.format(messageReadedVo.getUpdateTimeStr(), "yyyy-MM-dd HH:mm:ss"));
					}
					this.messageReadedServiceImpl.saveOrUpdate(messageReaded);
				}
			}
			json.addProperty("id", messageReaded.getId());
			json.addProperty("result", true);
		}catch (Exception e) {
			e.printStackTrace();
			json.addProperty("result", false);
		}finally{
			this.print(json);
		}
	}
	
	@RequestMapping(value="/messageRead_load")
	public void messageReadLoad(HttpServletRequest request,HttpServletResponse response,MessageReadedVo messageReadedVo){
		JSONObject json = new JSONObject();
		json.put("result", false);
		try{
			MessageReaded messageReaded = this.messageReadedServiceImpl.queryEntityByUserId(messageReadedVo.getUserId());
			if(messageReaded!=null){
				String readMsgs=messageReaded.getReadMsgs();
				json.put("readMsgs", readMsgs);
			}else{
				json.put("readMsgs", "");
			}
			json.put("result", true);
		}catch(Exception e){
			e.printStackTrace();
			json.put("result", false);
			json.put("err", e.getMessage());
		}
		this.print(json);
	}
	
	
	/********************************已读消息 end******************************************/
	
	
	/********************************以前是私有方法******************************************/
	private void sendMsg(final Message msg){
		new Thread(new Runnable() {
			@Override
			public void run() {
				MessageJpush.sendCommonMsg(msg.getTitle(), msg);
			}
		}).run();
	}
	
	private MessageVo formatVo(MessageVo messageVo){
		if(StringUtils.isNotBlank(messageVo.getTags())){
			StringBuffer tagsName = new StringBuffer();
			String[] tags = messageVo.getTags().split(",");
			for(int i = 0;i < tags.length; i++){
				if(i!=0){
					tagsName.append(",");
				}
				Role role = this.messageServiceImpl.getEntityById(Role.class, tags[i]);
				tagsName.append(role.getRoleName());
			}
			messageVo.setTagsName(tagsName.toString());
		}
		if(StringUtils.isNotBlank(messageVo.getAlias())){
			StringBuffer aliasName = new StringBuffer();
			String[] alias = messageVo.getAlias().split(",");
			for(int i = 0;i < alias.length; i++){
				if(i!=0){
					aliasName.append(",");
				}
				User user = this.messageServiceImpl.getEntityById(User.class, alias[i]);
				aliasName.append(user.getUserName());
			}
			messageVo.setAliasName(aliasName.toString());
		}
		return messageVo;
	}

}
