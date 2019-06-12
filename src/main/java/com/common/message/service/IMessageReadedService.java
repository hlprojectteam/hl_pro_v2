package com.common.message.service;

import com.common.base.service.IBaseService;
import com.common.message.module.MessageReaded;
import com.common.message.vo.MessageReadedVo;
import com.common.utils.helper.Pager;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年6月5日
 *
 */
public interface IMessageReadedService  extends IBaseService{

	/**
	 * 
	 * @方法：@param messageReaded
	 * @描述：保存
	 * @return
	 * @author: qinyongqian
	 * @date:2019年6月5日
	 */
	public void saveOrUpdate(MessageReaded messageReaded);
	
	/**
	 * 
	 * @Description: 列表查询
	 * @param page
	 * @param rows
	 * @param messageReadedVo
	 * @date 2016-8-15 上午11:18:25
	 */
	public Pager queryEntityList(Integer page, Integer rows,MessageReadedVo messageReadedVo);
	
	/**
	 * 
	 * @Description:删除 
	 * @param ids
	 * @date 2016-8-15 上午11:22:15
	 */
	public void deleteMessageReaded(String ids);
	
	/**
	 * 
	 * @Description:	根据id查询 
	 * @param id
	 * @date 2016-8-15 上午11:23:35
	 */
	public MessageReaded queryEntityById(String id);
	
	/**
	 * 
	 * @方法：@param userId
	 * @方法：@return
	 * @描述：通过userid获取已经消息
	 * @return
	 * @author: qinyongqian
	 * @date:2019年6月5日
	 */
	public MessageReaded queryEntityByUserId(String userId);
	

}
