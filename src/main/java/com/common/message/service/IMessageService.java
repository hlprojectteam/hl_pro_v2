package com.common.message.service;

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
	
	/**
	 * 
	 * @方法：@param noticeTitle 通知的提示标题
	 * @方法：@param noticeContent 通知的简要内容
	 * @方法：@param userIds 给谁发通知，用户ID的集合，用","分隔
	 * @方法：@param rodeCodes 给哪一类人发通知，如角色的集合，用","分隔
	 * @方法：@param msgType 消息类型
	 * @方法：@param user 会话用户
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2019年4月19日
	 */
	public void sendMsg(String noticeTitle,String noticeContent, String userIds,String rodeCodes,int msgType,User user);

}
