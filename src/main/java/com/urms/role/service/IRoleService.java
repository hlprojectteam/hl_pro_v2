package com.urms.role.service;




import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.role.module.Role;
import com.urms.role.vo.RoleVo;
import com.urms.user.module.User;

public interface IRoleService extends IBaseService{
	
	/**
	 * @intruduction 分页列表
	 * @param page
	 * @param rows
	 * @param roleVo
	 * @param user 当前登录人
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月7日上午10:53:37
	 */
	public Pager queryEntityList(int page,int rows,RoleVo roleVo,User user);
	
	/**
	 * @intruduction 列表
	 * @param roleVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月9日下午4:27:34
	 */
	public List<Role> queryRoleList(RoleVo roleVo);
	
	/**
	 * @intruduction 保存or更新
	 * @param role
	 * @author Mr.Wang
	 * @Date 2016年1月7日上午10:54:36
	 */
	public void saveOrUpdate(Role role);
	
	/**
	 * @intruduction 获得所有角色关联的用户 分页
	 * @param page
	 * @param rows
	 * @param roleVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月7日上午10:53:53
	 */
	public Pager queryRelationUser(int page,int rows,RoleVo roleVo);
	
	/**
	 * 
	 * @方法：@param roleVo
	 * @方法：@return
	 * @描述：是否存在角色
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月31日
	 */
	public boolean isRoleExist(RoleVo roleVo);
	
	/**
	 * 
	 * @方法：@param roleVo
	 * @方法：@return
	 * @描述：获取角色
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月31日
	 */
	public Role getRole(RoleVo roleVo);
}
