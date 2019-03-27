package com.urms.subsystem.controller;


import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
import com.common.utils.cache.Cache;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.menu.module.Menu;
import com.urms.subsystem.module.Subsystem;
import com.urms.subsystem.service.ISubsystemService;
import com.urms.subsystem.vo.SubsystemVo;

@Controller
@RequestMapping("/urms")
public class SubsystemController extends BaseController{
	
	@Autowired
	public ISubsystemService subsystemServiceImpl;
	
	/**
	 * @intruduction 子系统列表页面
	 * @param httpSession
	 * @param response
	 * @param subsystemVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:21:41
	 */
	@RequestMapping(value="/subsystem_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,SubsystemVo subsystemVo,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/urms/subsystem/subsystem_list";
	}
	
	/**
	 * @intruduction 子系统列表数据
	 * @param request
	 * @param response
	 * @param subsystemVo
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:22:06
	 */
	@RequestMapping(value="/subsystem_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,SubsystemVo subsystemVo,Integer page,Integer rows) {
		Pager pager = this.subsystemServiceImpl.queryEntityList(page, rows, subsystemVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"menus"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * @intruduction 编辑页面
	 * @param request
	 * @param subsystemVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:24:46
	 */
	@RequestMapping(value="/subsystem_edit") 
	public String edit(HttpServletRequest request,SubsystemVo subsystemVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(subsystemVo.getId())){
			Subsystem subsystem = this.subsystemServiceImpl.getEntityById(Subsystem.class,subsystemVo.getId());
			BeanUtils.copyProperties(subsystem,subsystemVo);
			request.setAttribute("subsystemVo", subsystemVo);
		}else{
			request.setAttribute("subsystemVo", subsystemVo);
		}
		return "/page/urms/subsystem/subsystem_edit";
	}
	
	/**
	 * @intruduction 保存子系统
	 * @param httpSession
	 * @param response
	 * @param subsystemVo
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:52:46
	 */
	@RequestMapping(value="/subsystem_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,SubsystemVo subsystemVo) {
		JsonObject json = new JsonObject();
		try {
			Subsystem subsystem = new Subsystem();
			BeanUtils.copyProperties(subsystemVo,subsystem);
			this.subsystemServiceImpl.saveOrUpdate(subsystem);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * @intruduction 删除
	 * @param httpSession
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月12日下午5:08:58
	 */
	@RequestMapping(value="/subsystem_delete",method = RequestMethod.POST) 
	public void delete(HttpSession httpSession,HttpServletResponse response,String ids) {
		this.subsystemServiceImpl.delete(Subsystem.class,ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	
	
	/**
	 * @intruduction 检查子系统编码是否唯一
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月12日下午2:41:44
	 */
	@RequestMapping(value="/subsystem_checkSysCode") 
	public void checkSysCode(HttpServletResponse response,SubsystemVo subsystemVo) {
		if(StringUtils.isBlank(subsystemVo.getId())){//新增情况下
			SubsystemVo svo = new SubsystemVo();
			svo.setSysCode(subsystemVo.getSysCode());
			List<Subsystem> list= this.subsystemServiceImpl.getSubsystemList(subsystemVo);
			if(!list.isEmpty())
				this.print(false);
			else
				this.print(true);
		}else{//修改情况下
			List<Subsystem> list= this.subsystemServiceImpl.getSubsystemList(subsystemVo);
			if(!list.isEmpty()){
				this.print(true);//已经存在				
			}else{
				SubsystemVo svo = new SubsystemVo();
				svo.setSysCode(subsystemVo.getSysCode());
				List<Subsystem> list2= this.subsystemServiceImpl.getSubsystemList(svo);
				if(!list2.isEmpty())
					this.print(false);
				else
					this.print(true);
			}
		}
	}
	
	/** 
	 * @intruduction 获得所有子系统
	 * @author Mr.Wang
	 * @Date 2016年1月12日下午9:29:43
	 */
	@RequestMapping(value="/subsystem_getSubsystem") 
	public void getSubsystem(HttpServletResponse response,SubsystemVo subsystemVo){
		List<Subsystem> list = this.subsystemServiceImpl.getSubsystemList(subsystemVo,this.getSessionUser());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"menus"});  //只要设置这个数组，指定过滤哪些字段。     
		this.print(JSONArray.fromObject(list,config));		
	}
	
	/**
	 * @intruduction 子名称系统翻译
	 * @param response
	 * @param sysCode
	 * @author Mr.Wang
	 * @Date 2016年1月13日下午2:32:45
	 */
	@RequestMapping(value="/changeSubsystem") 
	public void changeSubsystem(HttpServletResponse response,String sysCode){
		String sysName = Cache.getSubsystem.get(sysCode);
		if(StringUtils.isBlank(sysName))
			sysName = "";
		this.print(sysName);		
	}
	
	/**
	 * @intruduction 保存关联的菜单
	 * @param httpSession
	 * @param response
	 * @param subsystemVo
	 * @author Mr.Wang
	 * @Date 2016年1月15日下午3:34:34
	 */
	@RequestMapping(value="/subsystem_saveOrUpdateMenu",method = RequestMethod.POST) 
	public void saveOrUpdateMenu(HttpSession httpSession,HttpServletResponse response,String menuIds,String subsystemId) {
		JsonObject json = new JsonObject();
		try {
			Subsystem subsystem = this.subsystemServiceImpl.getEntityById(Subsystem.class, subsystemId);
			Set<Menu> menus = new TreeSet<Menu>();
			String[] menuIdsz  = menuIds.split(",");
			for(int i = 0; i < menuIdsz.length; i++) {
				Menu menu = this.subsystemServiceImpl.getEntityById(Menu.class, menuIdsz[i]);
				menus.add(menu);
			}
			subsystem.setMenus(menus);
			this.subsystemServiceImpl.update(subsystem);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	public ISubsystemService getSubsystemServiceImpl() {
		return subsystemServiceImpl;
	}
	public void setSubsystemServiceImpl(ISubsystemService subsystemServiceImpl) {
		this.subsystemServiceImpl = subsystemServiceImpl;
	}
	
}