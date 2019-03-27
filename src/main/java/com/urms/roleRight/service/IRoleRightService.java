package com.urms.roleRight.service;




import java.util.List;

import com.common.base.service.IBaseService;
import com.urms.roleRight.module.RoleRight;
import com.urms.roleRight.vo.RoleRightVo;

public interface IRoleRightService extends IBaseService{
	
	/**
	 * @intruduction 列表
	 * @param roleRightVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月9日下午4:27:34
	 */
	public List<RoleRight> queryRoleRightList(RoleRightVo roleRightVo);
	
	/**
	 * @intruduction 保存or更新
	 * @param roleRight
	 * @author Mr.Wang
	 * @Date 2016年1月7日上午10:54:36
	 */
	public void saveOrUpdate(String menuIds, String roleId,String sysCode);

	/**
	 * 清除角色菜单权限
	 * @intruduction
	 * @param roleId 角色id
	 * @param sysCode 子系统编码
	 * @author Mr.Wang
	 * @Date 2016年8月16日下午2:15:26
	 */
	public void saveClean(String roleId, String sysCode);

}
