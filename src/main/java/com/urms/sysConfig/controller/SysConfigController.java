package com.urms.sysConfig.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.urms.subsystem.module.Subsystem;
import com.urms.subsystem.service.ISubsystemService;
import com.urms.subsystem.vo.SubsystemVo;
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
import com.urms.sysConfig.module.SysConfig;
import com.urms.sysConfig.service.ISysConfigService;
import com.urms.sysConfig.vo.SysConfigVo;

@Controller
@RequestMapping("/urms")
public class SysConfigController extends BaseController{
	
	@Autowired
	public ISysConfigService sysConfigServiceImpl;
	@Autowired
	public ISubsystemService subsystemServiceImpl;
	
	/**
	 * @intruduction 子系统列表页面
	 * @param httpSession
	 * @param response
	 * @param sysConfigVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:21:41
	 */
	@RequestMapping(value="/sysConfig_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,SysConfigVo sysConfigVo,String menuCode) {
		List<Subsystem> subsystems = this.subsystemServiceImpl.getSubsystemList(new SubsystemVo());
		this.getRequest().setAttribute("subsystems", subsystems);
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/urms/sysConfig/sysConfig_list";
	}
	
	/**
	 * @intruduction 子系统列表数据
	 * @param request
	 * @param response
	 * @param sysConfigVo
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:22:06
	 */
	@RequestMapping(value="/sysConfig_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,SysConfigVo sysConfigVo,Integer page,Integer rows) {
		Pager pager = this.sysConfigServiceImpl.queryEntityList(page, rows, sysConfigVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
//		config.setExcludes(new String[]{"menus"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * @intruduction 编辑页面
	 * @param request
	 * @param sysConfigVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:24:46
	 */
	@RequestMapping(value="/sysConfig_edit") 
	public String edit(HttpServletRequest request,SysConfigVo sysConfigVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(sysConfigVo.getId())){
			SysConfig sysConfig = this.sysConfigServiceImpl.getEntityById(SysConfig.class,sysConfigVo.getId());
			BeanUtils.copyProperties(sysConfig,sysConfigVo);
			request.setAttribute("sysConfigVo", sysConfigVo);
		}else{
			request.setAttribute("sysConfigVo", sysConfigVo);
		}
		return "/page/urms/sysConfig/sysConfig_edit";
	}
	
	/**
	 * @intruduction 保存子系统
	 * @param httpSession
	 * @param response
	 * @param sysConfigVo
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:52:46
	 */
	@RequestMapping(value="/sysConfig_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,SysConfigVo sysConfigVo) {
		JsonObject json = new JsonObject();
		try {
			SysConfig sysConfig = new SysConfig();
			BeanUtils.copyProperties(sysConfigVo,sysConfig);
			this.sysConfigServiceImpl.saveOrUpdate(sysConfig);
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
	@RequestMapping(value="/sysConfig_delete",method = RequestMethod.POST) 
	public void delete(HttpSession httpSession,HttpServletResponse response,String ids) {
		this.sysConfigServiceImpl.delete(SysConfig.class,ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	
	
	/**
	 * @intruduction 检查子系统编码是否唯一
	 * @param response
	 * @param sysConfigVo
	 * @author Mr.Wang
	 * @Date 2016年1月12日下午2:41:44
	 */
	@RequestMapping(value="/sysConfig_checkSysKey") 
	public void checkSysKey(HttpServletResponse response,SysConfigVo sysConfigVo) {
		if(StringUtils.isBlank(sysConfigVo.getId())){//新增情况下
			SysConfigVo svo = new SysConfigVo();
			svo.setSysKey(sysConfigVo.getSysKey());
			svo.setSysCode(sysConfigVo.getSysCode());
			List<SysConfig> list= this.sysConfigServiceImpl.querySysConfigList(sysConfigVo);
			if(!list.isEmpty())
				this.print(false);
			else
				this.print(true);
		}else{//修改情况下
			List<SysConfig> list= this.sysConfigServiceImpl.querySysConfigList(sysConfigVo);
			if(!list.isEmpty()){
				this.print(true);//已经存在				
			}else{
				SysConfigVo svo = new SysConfigVo();
				svo.setSysCode(sysConfigVo.getSysCode());
				svo.setSysKey(sysConfigVo.getSysKey());
				List<SysConfig> list2= this.sysConfigServiceImpl.querySysConfigList(svo);
				if(!list2.isEmpty())
					this.print(false);
				else
					this.print(true);
			}
		}
	}
	
	public ISysConfigService getSysConfigServiceImpl() {
		return sysConfigServiceImpl;
	}
	public void setSysConfigServiceImpl(ISysConfigService sysConfigServiceImpl) {
		this.sysConfigServiceImpl = sysConfigServiceImpl;
	}
	
}