package com.common.message.service;

import com.common.base.service.IBaseService;
import com.common.message.module.Message;
import com.common.message.vo.MessageVo;
import com.common.utils.helper.Pager;

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

}
