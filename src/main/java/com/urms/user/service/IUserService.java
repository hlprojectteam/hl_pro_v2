package com.urms.user.service;


import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.role.vo.RoleVo;
import com.urms.user.module.User;
import com.urms.user.vo.UserVo;

public interface IUserService extends IBaseService{
	
	/**
	 * @intruduction 列表分页
	 * @param page 
	 * @param rows
	 * @param userVo
	 * @param sign 标识   1：查询管理员  2：查询所有客户
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月12日下午8:26:15
	 */
	public Pager queryEntityList(int page,int rows,UserVo userVo,User user,int sign);
	
	/**
	 * @intruduction 用户list
	 * @param userVo 查询条件
	 * @param sysCode 子系统编码
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年3月29日上午10:49:11
	 */
	public List<User> queryUserList(UserVo userVo);
	
	/**
	 * 可通过组织id获得组织下级所有用户list
	 * @intruduction 
	 * @param userVo 查询条件
	 * @param sysCode 子系统编码
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年3月29日上午10:49:11
	 */
	public List<User> queryUserChildList(UserVo userVo);
	
	/**
	 * @intruduction 保存or提交
	 * @param user
	 * @author Mr.Wang
	 * @Date 2016年1月7日上午10:58:58
	 */
	public void saveOrUpdate(User user);
	
	/**
	 * @intruduction 用户登录 支持登录名、手机号、Email
	 * @param loginName 用户名  支持登录名、手机号、Email
	 * @param password 密码
	 * @param type 用户类型 1=后台登录 null或其他值为前台公众登录
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月7日上午10:17:23
	 */
	public User login(String loginName,String password,Integer type);
	
	/**
	 * @intruduction 删除用户
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月27日上午9:09:06
	 */
	public void deleteUser(String ids);
	
	/**
	 * 获得系统管理员
	 * @intruduction
	 * @param sysCode
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年10月26日上午9:46:57
	 */
	public List<User> querySysAdmin(String sysCode);
	
	/**
	 * 凭userid字符窜获得 user集合
	 * @intruduction
	 * @param ids 
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年10月26日下午4:34:52
	 */
	public List<User> queryUser(String userIds);
	
	/**
	 * 
	 * @方法：@param userId
	 * @方法：@param loninName
	 * @方法：@param mobilePhone
	 * @方法：@param jobNumber
	 * @方法：@return
	 * @描述：获取唯一用户对象
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月11日
	 */
	public User getUser(String userId,String loninName,String mobilePhone,String jobNumber);
	
	/**
	 * 
	 * @方法：@param userVo
	 * @方法：@return
	 * @描述：是否存在用户
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月31日
	 */
	public String isUserExist(UserVo userVo);
	
}
