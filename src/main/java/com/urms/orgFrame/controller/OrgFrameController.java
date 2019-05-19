package com.urms.orgFrame.controller;

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
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.user.module.User;

@Controller
@RequestMapping("/urms")
public class OrgFrameController extends BaseController{
	
	@Autowired
	public IOrgFrameService orgFrameServiceImpl;
	/**
	 * @intruduction 菜单列表页面
	 * @param httpSession
	 * @param response
	 * @param orgFrameVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:21:41
	 */
	@RequestMapping(value="/orgFrame_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/urms/orgFrame/orgFrame_list";
	}
	
	/**
	 * @intruduction 菜单列表数据
	 * @param request
	 * @param response
	 * @param orgFrameVo
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:22:06
	 */
	@RequestMapping(value="/orgFrame_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,OrgFrameVo orgFrameVo,Integer page,Integer rows) {
		User user =(User)this.getHttpSession().getAttribute("user");
		Pager pager = this.orgFrameServiceImpl.queryEntityList(page, rows, orgFrameVo,user);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"users","roles"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * @intruduction 编辑页面
	 * @param requestr
	 * @param orgFrameVo
	 * @return
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:24:46
	 */
	@RequestMapping(value="/orgFrame_edit") 
	public String edit(HttpServletRequest request,OrgFrameVo orgFrameVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(orgFrameVo.getId())){
			OrgFrame orgFrame = this.orgFrameServiceImpl.getEntityById(OrgFrame.class,orgFrameVo.getId());
			BeanUtils.copyProperties(orgFrame,orgFrameVo);
			request.setAttribute("orgFrameVo", orgFrameVo);
		}else{
			OrgFrame orgFrame = this.orgFrameServiceImpl.getEntityById(OrgFrame.class,orgFrameVo.getpId());
			orgFrameVo.setSysCode(orgFrame.getSysCode());
			request.setAttribute("orgFrameVo", orgFrameVo);
		}
		return "/page/urms/orgFrame/orgFrame_edit";
	}
	
	/**
	 * @intruduction 保存菜单
	 * @param httpSession
	 * @param response
	 * @param orgFrameVo
	 * @author Mr.Wang
	 * @Date 2015年12月31日上午10:52:46
	 */
	@RequestMapping(value="/orgFrame_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,OrgFrameVo orgFrameVo) {
		JsonObject json = new JsonObject();
		try {
			OrgFrame orgFrame = new OrgFrame();
			BeanUtils.copyProperties(orgFrameVo,orgFrame);
			this.orgFrameServiceImpl.saveOrUpdate(orgFrame);
			json.addProperty("result", true);
			json.addProperty("id", orgFrame.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 加载树
	 * @param response
	 */
	@RequestMapping(value="/orgFrame_loadTree") 
	public void loadTree(HttpServletResponse response,String id) {
		StringBuffer tree = new StringBuffer();
		tree.append("[");
		User user =(User)this.getHttpSession().getAttribute("user");
		if(StringUtils.isBlank(id)){
			if (user.getType()==1) {//如果是超级管理员情况下
					id = "0";//初始化菜单 根节点为 0
					OrgFrame m = this.orgFrameServiceImpl.getEntityById(OrgFrame.class, id);
					tree.append("{");
					tree.append("id:'"+m.getId()+"',");
					tree.append("pId:'00',");
					tree.append("name:'"+m.getOrgFrameName()+"',");
					tree.append("open:true");
					tree.append("},");
			}else{//只能查看当前子系统组织
				OrgFrameVo oVo = new OrgFrameVo();
				oVo.setSysCode(user.getSysCode());
//				oVo.setLevel(1);//一级菜单  如果不是超管 直接显示当前子系统的跟节点 否则会显示 “组织架构”的节点 0级菜单
				List<OrgFrame> list = this.orgFrameServiceImpl.queryOrgFrameList(oVo);
				id = list.get(0).getId();
				tree.append("{");
				tree.append("id:'"+list.get(0).getId()+"',");
				tree.append("pId:'0',");
				tree.append("name:'"+list.get(0).getOrgFrameName()+"',");
				tree.append("open:true");
				tree.append("},");
			}
		}
		List<OrgFrame> orgFrameList = this.orgFrameServiceImpl.queryEntityListByPId(id);
		for (int i = 0; i < orgFrameList.size(); i++) {
			OrgFrame m = orgFrameList.get(i);
			tree.append("{");
			tree.append("id:'"+m.getId()+"',");
			tree.append("pId:'"+m.getpId()+"',");
			tree.append("name:'"+m.getOrgFrameName()+"'");
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
	 * 加载树
	 * @param response
	 */
	@RequestMapping(value="/orgFrame_loadTreeBySysCode")
	public void loadTree(HttpServletResponse response,String id, String sysCode) {
		StringBuffer tree = new StringBuffer();
		tree.append("[");
		User user =(User)this.getHttpSession().getAttribute("user");
		if(StringUtils.isBlank(id)) {
			if (user.getType() == 1) {//如果是超级管理员情况下
				if(StringUtils.isNotBlank(sysCode)){
					OrgFrameVo oVo = new OrgFrameVo();
					oVo.setSysCode(sysCode);
					List<OrgFrame> list = this.orgFrameServiceImpl.queryOrgFrameList(oVo);
					if(list.size()>0){
						id = list.get(0).getId();
						tree.append("{");
						tree.append("id:'" + list.get(0).getId() + "',");
						tree.append("pId:'0',");
						tree.append("name:'" + list.get(0).getOrgFrameName() + "',");
						tree.append("open:true");
						tree.append("},");
					}
				}else{
					id = "0";//初始化菜单 根节点为 0
					OrgFrame m = this.orgFrameServiceImpl.getEntityById(OrgFrame.class, id);
					tree.append("{");
					tree.append("id:'"+m.getId()+"',");
					tree.append("pId:'00',");
					tree.append("name:'"+m.getOrgFrameName()+"',");
					tree.append("open:true");
					tree.append("},");
				}

			}
		}
		List<OrgFrame> orgFrameList = this.orgFrameServiceImpl.queryEntityListByPId(id);
		for (int i = 0; i < orgFrameList.size(); i++) {
			OrgFrame m = orgFrameList.get(i);
			tree.append("{");
			tree.append("id:'"+m.getId()+"',");
			tree.append("pId:'"+m.getpId()+"',");
			tree.append("name:'"+m.getOrgFrameName()+"'");
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
	 * @intruduction 删除树
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月12日下午2:41:51
	 */
	@RequestMapping(value="/orgFrame_deleteTree") 
	public void deleteTree(HttpServletResponse response,String ids) {
		this.orgFrameServiceImpl.deleteTree(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 检查编码是否唯一
	 * @param response
	 * @param ids
	 * @author Mr.Wang
	 * @Date 2016年1月12日下午2:41:44
	 */
	@RequestMapping(value="/orgFrame_checkOrgFrameCode") 
	public void checkOrgFrameCode(HttpServletResponse response,OrgFrameVo orgFrameVo) {
		if(StringUtils.isBlank(orgFrameVo.getId())){//新增情况下
			OrgFrameVo mvo = new OrgFrameVo();
			mvo.setOrgFrameCode(orgFrameVo.getOrgFrameCode());
			mvo.setSysCode(orgFrameVo.getSysCode());
			List<OrgFrame> list= this.orgFrameServiceImpl.queryOrgFrameList(mvo);
			if(!list.isEmpty())
				this.print(false);
			else
				this.print(true);
		}else{//修改情况下
			List<OrgFrame> list= this.orgFrameServiceImpl.queryOrgFrameList(orgFrameVo);
			if(!list.isEmpty()){
				this.print(true);//已经存在				
			}else{
				OrgFrameVo mvo = new OrgFrameVo();
				mvo.setOrgFrameCode(orgFrameVo.getOrgFrameCode());
				List<OrgFrame> list2= this.orgFrameServiceImpl.queryOrgFrameList(mvo);
				if(!list2.isEmpty())
					this.print(false);
				else
					this.print(true);
			}
		}
	}
	
	/**
	 * @intruduction 选择组织架构页面
	 * @param winName iframe name
	 * @param id 回填id
	 * @param name 回填name
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年1月11日上午10:45:42
	 */
	@RequestMapping(value="/orgFrame_choose") 
	public String chooseOrgFrame(String winName,String id,String name){
		this.getRequest().setAttribute("winName",winName);
		this.getRequest().setAttribute("id",id);
		this.getRequest().setAttribute("name", name);
		return "/page/urms/orgFrame/orgFrame_choose";
	}
	
	/**
	 * @intruduction 获得组织架构
	 * @param httpSession
	 * @param response
	 * @param roleVo 查询条件
	 * @author Mr.Wang
	 * @Date 2016年1月9日下午4:42:49
	 */
	@RequestMapping(value="/orgFrame_queryOrgFrameList") 
	public void queryOrgFrameList(HttpSession httpSession,HttpServletResponse response,OrgFrameVo orgFrameVo){
		orgFrameVo.setSysCode(this.getSessionUser().getSysCode());//子系统编码
		List<OrgFrame> list = this.orgFrameServiceImpl.queryOrgFrameList(orgFrameVo);
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"users","roles"});  //只要设置这个数组，指定过滤哪些字段。     
		this.print(JSONArray.fromObject(list,config));
	}
	
	/**
	 * 组织架构树
	 * @intruduction
	 * @param winName
	 * @return
	 * @author Mr.Wang
	 * @Date 2017年5月19日下午5:02:51
	 */
	@RequestMapping(value="/orgFrame_tree") 
	public String orgFrameTree(String winName,String id,String name,String code){
		this.getRequest().setAttribute("winName",winName);
		this.getRequest().setAttribute("id",id);
		this.getRequest().setAttribute("name",name);
		this.getRequest().setAttribute("code",code);
		return "/page/urms/orgFrame/orgFrame_tree";
	}
	
	
	public IOrgFrameService getOrgFrameServiceImpl() {
		return orgFrameServiceImpl;
	}
	public void setOrgFrameServiceImpl(IOrgFrameService orgFrameServiceImpl) {
		this.orgFrameServiceImpl = orgFrameServiceImpl;
	}
	
}