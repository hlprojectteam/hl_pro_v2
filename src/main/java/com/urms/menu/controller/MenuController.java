package com.urms.menu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.common.base.controller.BaseController;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.menu.module.Menu;
import com.urms.menu.module.MenuDefinition;
import com.urms.menu.service.IMenuService;
import com.urms.menu.vo.MenuDefinitionVo;
import com.urms.menu.vo.MenuVo;
import com.urms.subsystem.module.Subsystem;

@Controller
@RequestMapping("/urms")
public class MenuController extends BaseController{
	
	@Autowired
	public IMenuService menuServiceImpl;
	
	/**
	 * @intruduction 菜单列表页面
	 * @param httpSession
	 * @param response
	 * @param menuVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:21:41
	 */
	@RequestMapping(value="/menu_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/urms/menu/menu_list";
	}
	
	/**
	 * @intruduction 菜单列表数据
	 * @param request
	 * @param response
	 * @param menuVo
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:22:06
	 */
	@RequestMapping(value="/menu_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,MenuVo menuVo,Integer page,Integer rows, String order) {
		Pager pager = this.menuServiceImpl.queryEntityList(page, rows, menuVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"roleRights","subsystems","menuDefinitions"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * @intruduction 编辑页面
	 * @param request
	 * @param menuVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:24:46
	 */
	@RequestMapping(value="/menu_edit") 
	public String edit(HttpServletRequest request,MenuVo menuVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(menuVo.getId())){
			Menu menu = this.menuServiceImpl.getEntityById(Menu.class,menuVo.getId());
			BeanUtils.copyProperties(menu,menuVo);
			request.setAttribute("menuVo", menuVo);
		}else{
			request.setAttribute("menuVo", menuVo);
		}
		return "/page/urms/menu/menu_edit";
	}
	
	/**
	 * @intruduction 保存菜单
	 * @param httpSession
	 * @param response
	 * @param menuVo
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:52:46
	 */
	@RequestMapping(value="/menu_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,MenuVo menuVo) {
		JsonObject json = new JsonObject();
		try {
			Menu menu = new Menu();
			BeanUtils.copyProperties(menuVo,menu);
			this.menuServiceImpl.saveOrUpdate(menu);
			json.addProperty("result", true);
			json.addProperty("id", menu.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 加载所有菜单树
	 * @param httpSession
	 * @param response
	 * @param menuVo
	 */
	@RequestMapping(value="/menu_loadTree") 
	public void loadTree(HttpServletResponse response,String id) {
		StringBuffer tree = new StringBuffer();
		tree.append("[");
		if(StringUtils.isBlank(id)){
			id = "0";//初始化菜单 根节点为 0
			Menu m = this.menuServiceImpl.getEntityById(Menu.class, id);
			tree.append("{");
			tree.append("id:'"+m.getId()+"',");
			tree.append("pId:'00',");
			tree.append("name:'"+m.getMenuName()+"',");
			tree.append("open:true");
			tree.append("},");
		}
		List<Menu> menuList = this.menuServiceImpl.queryEntityListByPId(id);
		for (int i = 0; i < menuList.size(); i++) {
			Menu m = menuList.get(i);
			tree.append("{");
			tree.append("id:'"+m.getId()+"',");
			tree.append("pId:'"+m.getpId()+"',");
			tree.append("name:'"+m.getMenuName()+"'");
			if(m.getIsLeaf()==0)
				tree.append(",isParent:true");
			tree.append("},");
		}
		tree.deleteCharAt(tree.toString().length()-1);
		tree.append("]");
		logger.info("输出树结构:"+tree.toString());
		this.print(tree.toString());
	}
	
	/**
	 * 加载菜单树 除了超管菜单
	 * @param httpSession
	 * @param response
	 * @param menuVo
	 */
	@RequestMapping(value="/menu_loadTreeSys") 
	public void loadTreeSys(HttpServletResponse response,String id,String subsystemId) {
		Subsystem s = this.menuServiceImpl.getEntityById(Subsystem.class, subsystemId);
		StringBuffer sb = new StringBuffer();
		for (Menu menu : s.getMenus()) {
			sb.append(menu.getId()+",");//有权限的id集合
		}
		StringBuffer tree = new StringBuffer();
		tree.append("[");
		if(StringUtils.isBlank(id)){
			id = "0";//初始化菜单 根节点为 0
			Menu m = this.menuServiceImpl.getEntityById(Menu.class, id);
			tree.append("{");
			tree.append("id:'"+m.getId()+"',");
			tree.append("pId:'00',");
			tree.append("name:'"+m.getMenuName()+"',");
			if(sb.toString().contains(m.getId()))
				tree.append("checked:true,");		
			tree.append("open:true");
			tree.append("},");
		}
		List<Menu> menuList = this.menuServiceImpl.queryEntityListByPId(id);
		for (int i = 0; i < menuList.size(); i++) {
			Menu m = menuList.get(i);
			if(m.getMenuType()!=1){//系统菜单
				tree.append("{");
				tree.append("id:'"+m.getId()+"',");
				tree.append("pId:'"+m.getpId()+"',");
				tree.append("name:'"+m.getMenuName()+"'");
				if(sb.toString().contains(m.getId()))
					tree.append(",checked:true");
				if(m.getIsLeaf()==0)
					tree.append(",isParent:true");
				tree.append("},");
			}
		}
		tree.deleteCharAt(tree.toString().length()-1);
		tree.append("]");
		logger.info("输出树结构:"+tree.toString());
		this.print(tree.toString());
	}
	
	/**
	 * @intruduction 删除树
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月12日下午2:41:51
	 */
	@RequestMapping(value="/menu_deleteTree") 
	public void deleteTree(HttpServletResponse response,String ids) {
		this.menuServiceImpl.deleteTree(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 检查菜单编码是否唯一
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月12日下午2:41:44
	 */
	@RequestMapping(value="/menu_checkMenuCode") 
	public void checkMenuCode(HttpServletResponse response,MenuVo menuVo) {
		if(StringUtils.isBlank(menuVo.getId())){//新增情况下
			MenuVo mvo = new MenuVo();
			mvo.setMenuCode(menuVo.getMenuCode());
			List<Menu> list= this.menuServiceImpl.queryMenuList(mvo);
			if(!list.isEmpty())
				this.print(false);
			else
				this.print(true);
		}else{//修改情况下
			List<Menu> list= this.menuServiceImpl.queryMenuList(menuVo);
			if(!list.isEmpty()){
				this.print(true);//已经存在				
			}else{
				MenuVo mvo = new MenuVo();
				mvo.setMenuCode(menuVo.getMenuCode());
				List<Menu> list2= this.menuServiceImpl.queryMenuList(mvo);
				if(!list2.isEmpty())
					this.print(false);
				else
					this.print(true);
			}
		}
	}
	
	/**
	 * @intruduction 功能点列表
	 * @param response
	 * @param menuDefinitionVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年2月2日上午9:56:43
	 */
	@RequestMapping(value="/menu_listDefinition") 
	public String listDefinition(HttpServletResponse response,MenuDefinitionVo menuDefinitionVo) {
		return "/page/urms/menu/menu_edit";
	}
	
	/**
	 * @intruduction 一对多分页
	 * @param response
	 * @param page
	 * @param rows
	 * @param menuVo
	 * @author Mr.Wang
	 * @Date 2016年2月2日上午10:44:03
	 */
	@RequestMapping(value="/menu_loadDefinition") 
	public void loadDefinition(HttpServletResponse response,Integer page,Integer rows,MenuVo menuVo) {
		Pager pager = this.menuServiceImpl.queryMenuDefinitionList(page, rows, menuVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"menu","roles"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * @intruduction 菜单功能点定义
	 * @param response
	 * @param menuVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年2月1日下午3:21:08
	 */
	@RequestMapping(value="/menu_editDefinition") 
	public String editDefinition(HttpServletRequest request,HttpServletResponse response,MenuDefinitionVo menuDefinitionVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(menuDefinitionVo.getId())){
			MenuDefinition menuDefinition = this.menuServiceImpl.getEntityById(MenuDefinition.class,menuDefinitionVo.getId());
			BeanUtils.copyProperties(menuDefinition,menuDefinitionVo);
			request.setAttribute("menuDefinitionVo", menuDefinitionVo);
		}else{
			request.setAttribute("menuDefinitionVo", menuDefinitionVo);
		}
		return "/page/urms/menu/menu_editDefinition";
	}
	
	/**
	 * @intruduction 保存菜单功能点
	 * @param response
	 * @param menuVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年2月2日上午9:07:39
	 */
	@RequestMapping(value="/menu_saveOrUpdateDefinition") 
	public void saveOrUpdateDefinition(HttpServletResponse response,MenuDefinitionVo menuDefinitionVo) {
		JsonObject json = new JsonObject();
		try {
			MenuDefinition menuDefinition = new MenuDefinition();
			BeanUtils.copyProperties(menuDefinitionVo,menuDefinition);
			this.menuServiceImpl.saveOrUpdateDefinition(menuDefinition);
			json.addProperty("result", true);
			json.addProperty("id", menuDefinition.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @intruduction 删除保存菜单功能点
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年2月2日下午1:53:16
	 */
	@RequestMapping(value="/menu_deleteDefinition") 
	public void deleteDefinition(HttpServletResponse response,String ids) {
		this.menuServiceImpl.delete(MenuDefinition.class, ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	
	public IMenuService getMenuServiceImpl() {
		return menuServiceImpl;
	}
	public void setMenuServiceImpl(IMenuService menuServiceImpl) {
		this.menuServiceImpl = menuServiceImpl;
	}
	
}