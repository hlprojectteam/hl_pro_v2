package com.common.token.service;

import com.common.base.service.IBaseService;
import com.common.token.module.Token;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2018年3月24日
 *
 */
public interface ITokenService extends IBaseService{

	/**
	 * 
	 * @ClassName:ITokenService.java
	 * @Description:保存或者更新
	 * @param token
	 * @author qinyongqian
	 * @date 2016-8-13
	 */
	public void saveOrUpdate(Token token);
	
	/**
	 * 
	 * @ClassName:ITokenService.java
	 * @Description:根据id删除token
	 * @param id
	 * @author qinyongqian
	 * @date 2016-8-13
	 */
	public void deleteToken(String id);
	
	/**
	 * 
	 * @ClassName:ITokenService.java
	 * @Description:根据userid删除token
	 * @param userId
	 * @author qinyongqian
	 * @date 2016-8-15
	 */
	public void deleteTokenByUserId(String userId);
	
	/**
	 * 
	 * @ClassName:ITokenService.java
	 * @Description:根据id获取token
	 * @param id
	 * @return
	 * @author qinyongqian
	 * @date 2016-8-13
	 */
	public Token queryEntityById(String id);
	
	/**
	 * 
	 * @ClassName:ITokenService.java
	 * @Description:是否有令牌
	 * @param token
	 * @return
	 * @author qinyongqian
	 * @date 2016-8-15
	 */
	public boolean isHaveToken(String token);
	
	/**
	 * 
	 * @ClassName:ITokenService.java
	 * @Description:获取令牌
	 * @param token
	 * @return
	 * @author qinyongqian
	 * @date 2016-8-15
	 */
	public Token getToken(String token);

}
