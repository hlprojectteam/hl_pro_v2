package com.common.login.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.utils.Common;
import com.common.webSocket.SystemWebSocketHandler;
import com.urms.menu.module.Menu;
import com.urms.menu.service.IMenuService;
import com.urms.menu.vo.MenuVo;
import com.urms.role.module.Role;
import com.urms.roleRight.module.RoleRight;
import com.urms.subsystem.module.Subsystem;
import com.urms.subsystem.service.ISubsystemService;
import com.urms.subsystem.vo.SubsystemVo;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;

/**
 * @intruduction 
 * @author Dic
 * @Date 2015年12月26日下午1:17:08
 */
@Controller
public class LoginController extends BaseController{
	
	@Autowired
	public IMenuService menuServiceImpl;
	@Autowired
	public IUserService userServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	public ISubsystemService subsystemServiceImpl;
	
	/**
	 * @intruduction：用户登录后台主页
	 * @param request
	 * @param response
	 * @param loginName 用户名
	 * @param password 密码 md5
	 * @param attr
	 * @return
	 * @author Dic
	 * @Date 2015年12月26日下午1:15:56
	 */
	@RequestMapping(value="/indexMain") 
	public String indexMain(HttpServletRequest request,HttpServletResponse response,String loginName,String password) {
		User user =(User)this.getHttpSession().getAttribute("user");
		String returnUrl = "/page/mainPage/index";//登录后台菜单栏页面
//		String returnUrl = "/page/mainPage/indexMain";//登录后主页面  地图
		String returnLoginJsp = "redirect:/login.jsp";//登录页面
		this.getHttpSession().removeAttribute("msg");//清除session
		if(user!=null){
			return returnUrl;
		}else{
			if(StringUtils.isNotBlank(loginName)){
				user = userServiceImpl.login(loginName, password,null);
				if(user!=null){
					if(user.getState()!=null){
						if(user.getState()==1){//正常账号
							if(Common.SingleLogin.equals("1")){//配置 只能一个账号登陆
								boolean chk = false;
								List<User> users = SystemWebSocketHandler.users;
								for (User u : users) {
									if(u.getId().indexOf(user.getId())>-1){//用户已经登陆
										chk = true;
										break;
									}
								}
								if(chk){
									this.getHttpSession().setAttribute("msg", "该账号已经在其他地方登陆！");
									return returnLoginJsp;
								}else{
									user.setLoginTimes(user.getLoginTimes()+1);//登录次数
									user.setLastLoginTime(new Date());//最后一次登录时间
									user.setLoginIp(request.getRemoteAddr());//登录id
									userServiceImpl.update(user);
									//写入session
									this.getHttpSession().setAttribute("user", user);
									//获得用户菜单
									List<List<Menu>> list = this.getMenu(user);
									if(list.get(0)!=null)
										this.getHttpSession().setAttribute("firstList", list.get(0));
									if(list.get(1)!=null)
										this.getHttpSession().setAttribute("secondList", list.get(1));
									if(list.get(2)!=null)
										this.getHttpSession().setAttribute("thirdList", list.get(2));
									List<Attach> attachList = attachServiceImpl.queryAttchListByFormId(user.getId());
									for (int i = 0; i < attachList.size(); i++) {
										this.getHttpSession().setAttribute("attachPhoto", attachList.get(0));//头像路径加入缓存
									}
									logger.debug("用户:"+user.getLoginName()+" 登录系统，时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
									//地图菜单
									this.getHttpSession().setAttribute("firstMapList", list.get(3));
									return returnUrl;
								}
							}else{//一个账户多浏览器登录
								user.setLoginTimes(user.getLoginTimes()+1);//登录次数
								user.setLastLoginTime(new Date());//最后一次登录时间
								user.setLoginIp(request.getRemoteAddr());//登录id
								userServiceImpl.update(user);
								//写入session
								this.getHttpSession().setAttribute("user", user);
								//获得用户菜单
								List<List<Menu>> list = this.getMenu(user);
								if(list.get(0)!=null)
									this.getHttpSession().setAttribute("firstList", list.get(0));
								if(list.get(1)!=null)
									this.getHttpSession().setAttribute("secondList", list.get(1));
								if(list.get(2)!=null)
									this.getHttpSession().setAttribute("thirdList", list.get(2));
								List<Attach> attachList = attachServiceImpl.queryAttchListByFormId(user.getId());
								for (int i = 0; i < attachList.size(); i++) {
									this.getHttpSession().setAttribute("attachPhoto", attachList.get(0));//头像路径加入缓存
								}
								logger.debug("用户:"+user.getLoginName()+" 登录系统，时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
								//地图菜单
								this.getHttpSession().setAttribute("firstMapList", list.get(3));
								return returnUrl;
							}
						}else{//冻结
							this.getHttpSession().setAttribute("msg", "该账号已经被冻结！");
							return returnLoginJsp;
						}
					}else{//状态为空，视频为冻结
						this.getHttpSession().setAttribute("msg", "该账号已经被冻结！");
						return returnLoginJsp;
					}
				}else{//账号密码错误
					this.getHttpSession().setAttribute("msg", "账号或密码错误！");
					return returnLoginJsp;
				}
			}else{
				this.getHttpSession().setAttribute("msg", "请重新登录！");
				return returnLoginJsp;
			}
		}
	}
	
	/**
	 * @intruduction 返回主页面头文件
	 * @param request
	 * @param response
	 * @return
	 * @author Dic
	 * @Date 2016年6月2日上午9:38:38
	 */
	@RequestMapping(value="/indexMain_hearder") 
	public String indexHearder(HttpServletRequest request,HttpServletResponse response) {
		return "/page/mainPage/common/hearder";
	}
	
	/**
	 * @intruduction 进入后台
	 * @param request
	 * @param response
	 * @return
	 * @author Dic
	 * @Date 2016年6月2日上午10:26:59
	 */
	@RequestMapping(value="/index") 
	public String index(HttpServletRequest request,HttpServletResponse response) {
		return "/page/mainPage/index";
	}
	
	/**
	 * @intruduction 获得菜单
	 * @param user 当前登录用户
	 * @return
	 * @author Dic
	 * @Date 2016年1月15日上午11:33:53
	 */
	@SuppressWarnings("unchecked")
	public List<List<Menu>> getMenu(User user){
		List<List<Menu>> list = new ArrayList<List<Menu>>();
		if(user.getType()==1){//超级管理员			
			MenuVo menuVo = new MenuVo();
			menuVo.setMenuType(12);//菜单类型  普通菜单和超管菜单 
			menuVo.setState(1);//可用状态
			menuVo.setLevel(1);//一级菜单
			List<Menu> firstList = menuServiceImpl.queryMenuList(menuVo);
			list.add(0, firstList);
			menuVo.setLevel(2);//二级菜单
			List<Menu> secondList = menuServiceImpl.queryMenuList(menuVo);
			list.add(1, secondList);
			menuVo.setLevel(3);//三级菜单
			List<Menu> thirdList = menuServiceImpl.queryMenuList(menuVo);
			list.add(2, thirdList);
			//地图菜单
			menuVo.setMenuType(3);//3：地图菜单编码
			menuVo.setLevel(2);
			List<Menu> firstMapList = menuServiceImpl.queryMenuList(menuVo);
			list.add(3, firstMapList);
		}else if(user.getType()==2){//子系统超管
			List<Menu> firstList = new ArrayList<Menu>();
			List<Menu> secondList = new ArrayList<Menu>();
			List<Menu> thirdList = new ArrayList<Menu>();
			List<Menu> firstMapList = new ArrayList<Menu>();
			SubsystemVo subsystemVo = new SubsystemVo();
			subsystemVo.setSysCode(user.getSysCode());
			List<Subsystem> subsystemList= subsystemServiceImpl.getSubsystemList(subsystemVo);
			if(subsystemList.size()>0){
				for (Menu menu : subsystemList.get(0).getMenus()) {
					if(menu.getState()==1){//可用状态
						if(menu.getMenuType()==2){//普通菜单
							if(menu.getLevel()==1){//一级菜单
								firstList.add(menu);
							}else if(menu.getLevel()==2){//二级菜单
								secondList.add(menu);
							}else if(menu.getLevel()==3){//三级菜单
								thirdList.add(menu);
							}
						}else if(menu.getMenuType()==3){//地图菜单
							if(menu.getLevel()==2){//二级菜单
								firstMapList.add(menu);
							}
						}
					}
				}
				firstList = removeDuplicate(firstList);
				secondList = removeDuplicate(secondList);
				thirdList = removeDuplicate(thirdList);
				firstMapList = removeDuplicate(firstMapList);

				ComparatorMenu comparator=new ComparatorMenu();
				Collections.sort(firstList, comparator);
				list.add(0, firstList);
				Collections.sort(secondList, comparator);
				list.add(1, secondList);
				Collections.sort(thirdList, comparator);
				list.add(2, thirdList);
				Collections.sort(firstMapList, comparator);
				list.add(3, firstMapList);
			}
		}else{//普通用户
			List<Menu> firstList = new ArrayList<Menu>();
			List<Menu> secondList = new ArrayList<Menu>();
			List<Menu> thirdList = new ArrayList<Menu>();
			List<Menu> firstMapList = new ArrayList<Menu>();
			for (Role role : user.getRoles()) {//一用户多角色 合并起来
				for(RoleRight roleRights :role.getRoleRights()){
					Menu menu= roleRights.getMenu();
					if(menu.getState()==1){//可用状态
						if(menu.getMenuType()==2){//普通菜单
							if(menu.getLevel()==1){//一级菜单
								firstList.add(menu);
							}else if(menu.getLevel()==2){//二级菜单
								secondList.add(menu);
							}else if(menu.getLevel()==3){//三级菜单
								thirdList.add(menu);
							}
						}else if(menu.getMenuType()==3){//地图菜单
							if(menu.getLevel()==2){//二级菜单
								firstMapList.add(menu);
							}
						}
					}
				}
			}
			firstList = removeDuplicate(firstList);
			secondList = removeDuplicate(secondList);
			thirdList = removeDuplicate(thirdList);
			firstMapList = removeDuplicate(firstMapList);

			ComparatorMenu comparator=new ComparatorMenu();
			Collections.sort(firstList, comparator);
			list.add(0, firstList);
			Collections.sort(secondList, comparator);
			list.add(1, secondList);
			Collections.sort(thirdList, comparator);
			list.add(2, thirdList);
			Collections.sort(firstMapList, comparator);
			list.add(3, firstMapList);
		}
		return list;
	}
	
	//菜单排序
	public class ComparatorMenu implements Comparator<Object>{
		public int compare(Object arg0, Object arg1) {
			Menu menu0=(Menu)arg0;
			Menu menu1=(Menu)arg1;
			return menu0.getOrder().compareTo(menu1.getOrder());
		}
	}
	
	
	/**
	 * @intruduction 登录后右边框架
	 * @param request
	 * @param response
	 * @return
	 * @author Dic
	 * @Date 2016年1月26日下午1:42:13
	 */
	@RequestMapping(value="/indexFrame") 
	public String indexFrame(HttpServletRequest request,HttpServletResponse response) {
		return "/page/mainPage/indexFrame";
	}
	
	/**
	 * @intruduction 登出
	 * @return 登录页面
	 * @author Dic
	 * @Date 2015年12月28日上午8:56:17
	 */
	@RequestMapping(value="/loginOut") 
	public String loginOut() {
		User user = this.getSessionUser();
		if(user!=null){
			this.getHttpSession().invalidate();//清除所有session
		}
		return "redirect:/login.jsp";
	}
	
	/**
	 * @intruduction 返回主页面头文件
	 * @param request
	 * @param response
	 * @return
	 * @author Mr.joker
	 * @Date 2016年7月12日 15:43:17
	 */
	@RequestMapping(value="/indexMain_top") 
	public String indexTop(HttpServletRequest request,HttpServletResponse response) {
		return "/page/mainPage/common/top";
	}
	
	/**
	 * @intruduction list去重
	 * @param list
	 * @return
	 * @author Mr.joker
	 * @Date 2016年7月12日 15:43:17
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List removeDuplicate(List list) {
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}

	
	public IMenuService getMenuServiceImpl() {
		return menuServiceImpl;
	}

	public void setMenuServiceImpl(IMenuService menuServiceImpl) {
		this.menuServiceImpl = menuServiceImpl;
	}

	public IUserService getUserServiceImpl() {
		return userServiceImpl;
	}

	public void setUserServiceImpl(IUserService userServiceImpl) {
		this.userServiceImpl = userServiceImpl;
	}

	public IAttachService getAttachServiceImpl() {
		return attachServiceImpl;
	}

	public void setAttachServiceImpl(IAttachService attachServiceImpl) {
		this.attachServiceImpl = attachServiceImpl;
	}

	public ISubsystemService getSubsystemServiceImpl() {
		return subsystemServiceImpl;
	}

	public void setSubsystemServiceImpl(ISubsystemService subsystemServiceImpl) {
		this.subsystemServiceImpl = subsystemServiceImpl;
	}

}
