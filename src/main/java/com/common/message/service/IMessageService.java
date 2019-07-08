package com.common.message.service;

import java.util.List;

import com.common.base.service.IBaseService;
import com.common.message.module.Message;
import com.common.message.vo.MessageVo;
import com.common.utils.helper.Pager;
import com.urms.user.module.User;

public interface IMessageService  extends IBaseService{

	/**
	 * 
	 * @Description: 保存
	 * @param msg
	 * @date 2016-8-15 上午11:13:23
	 */
	public void saveOrUpdate(Message msg);
	
	/**
	 * 
	 * @Description: 列表查询
	 * @param page
	 * @param rows
	 * @param messageVo
	 * @date 2016-8-15 上午11:18:25
	 */
	public Pager queryEntityList(Integer page, Integer rows,MessageVo messageVo);
	
	/**
	 * 
	 * @方法：@param messageVo
	 * @方法：@param days
	 * @方法：@return
	 * @描述：获取当前时间多少天内的消息
	 * @return
	 * @author: qinyongqian
	 * @date:2019年6月5日
	 */
	public List<Message> queryEntityList(MessageVo messageVo,int days);
	
	/**
	 * 
	 * @Description:删除 
	 * @param ids
	 * @date 2016-8-15 上午11:22:15
	 */
	public void deleteMessage(String ids);
	
	/**
	 * 
	 * @Description:	根据id查询 
	 * @param id
	 * @date 2016-8-15 上午11:23:35
	 */
	public Message queryEntityById(String id);
	
	public void submitSendMsg(String noticeTitle, String noticeContent,
			String userIds, String rodeCodes, int msgType, User user);

}
