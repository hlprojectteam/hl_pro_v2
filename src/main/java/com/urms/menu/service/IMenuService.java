package com.urms.menu.service;

import java.util.List;

import com.common.base.service.IBaseService;
import com.common.utils.helper.Pager;
import com.urms.menu.module.Menu;
import com.urms.menu.module.MenuDefinition;
import com.urms.menu.vo.MenuVo;

public interface IMenuService extends IBaseService{
	
	/**
	 * 说明：保存or更新
	 * 输入：@param menu
	 * 输出：void
	 * 创建时间:2016-1-5 下午5:03:07
	 */
	public void saveOrUpdate(Menu menu);
	
	/**
	 * 说明：分页
	 * 输入：@param page
	 * 输入：@param rows
	 * 输入：@param menuVo
	 * 输入：@return
	 * 输出：Pager
	 * 创建时间:2016-1-5 下午4:46:02
	 */
	public Pager queryEntityList(int page,int rows,MenuVo menuVo);
	
	/**
	 * 说明：获得菜单集合
	 * 输入：@param menuVo
	 * 输入：@return
	 * 输出：List<Menu>
	 * 创建时间:2016-1-5 下午4:57:54
	 */
	public List<Menu> queryMenuList(MenuVo menuVo);

	/**
	 * 说明：凭父ID获得菜单集合
	 * 输入：@param pid
	 * 输入：@return
	 * 输出：List<Menu>
	 * 创建时间:2016-1-5 下午4:48:56
	 */
	public List<Menu> queryEntityListByPId(String pid);

	/**
	 * 说明：删除菜单树
	 * 输入：@param ids
	 * 输出：void
	 * 创建时间:2016-1-5 下午4:51:36
	 */
	public void deleteTree(String ids);

	/**
	 * @intruduction 保存和更新功能项
	 * @param menuDefinition
	 * @author Mr.Wang
	 * @Date 2016年2月2日上午9:44:52
	 */
	public void saveOrUpdateDefinition(MenuDefinition menuDefinition);

	/**
	 * @intruduction 菜单功能点分页
	 * @param page
	 * @param rows
	 * @param menuDefinitionVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年2月2日上午10:31:30
	 */
	public Pager queryMenuDefinitionList(int page,int rows,MenuVo menuVo);
	
	/**
	 * @intruduction 菜单功能点
	 * @param menuVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年6月21日下午5:06:45
	 */
	public List<MenuDefinition> queryMenuDefinitionList(MenuVo menuVo);
	/**
	 * @intruduction 菜单编码查询菜单
	 * @param menuCode
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年6月8日上午11:12:32
	 */
	public Menu getMenuByCode(String menuCode);
}
