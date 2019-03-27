package com.dangjian.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.o.common.beans.BeanUtils;

import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.dangjian.module.Branch;
import com.dangjian.service.IBranchService;
import com.dangjian.vo.BranchVo;
import com.google.gson.JsonObject;

/**
 * 
 * @Description 支部控制层
 * @author qinyongqian
 * @date 2018年12月1日
 *
 */
@Controller
@RequestMapping(value="/dangjian")
public class BranchController extends BaseController{

	@Autowired
	public IBranchService branchServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param menuCode
	 * @方法：@return
	 * @描述：党支部列表 
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/branch_list")
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode){
		this.getRequest().setAttribute("menuCode", menuCode);
	    return "/page/dangjian/branch/branch_list";
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param branchVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：加载党支部列表 
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/branch_load")
	public void load(HttpServletRequest request,HttpServletResponse response,BranchVo branchVo,
			Integer page,Integer rows){
		Pager pager = this.branchServiceImpl.queryBranchEntityList(page, rows, branchVo);
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
		String[] excludes = new String[] {"createTime","creatorId","creatorName","sysCode"}; // 列表排除信息内容字段，减少传递时间
		config.setExcludes(excludes);
		json.put("result", true);
		json.put("total", pager.getRowCount());
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param branchVo
	 * @方法：@param type
	 * @方法：@return
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/branch_edit")
	public String edit(HttpServletRequest request,BranchVo branchVo){
		if(StringUtils.isNotBlank(branchVo.getId())){
			try{
				Branch branch = this.branchServiceImpl.getEntityById(Branch.class, branchVo.getId());
				BeanUtils.copyProperties(branch, branchVo);
				request.setAttribute("branchVo", branchVo);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			request.setAttribute("branchVo", branchVo);
		}
		return "/page/dangjian/branch/branch_edit";
			
	}
	
	/**
	 * 
	 * @方法：@param response
	 * @方法：@param ids
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/branch_del") 
	public void delete(HttpServletResponse response,String ids) {
		this.branchServiceImpl.deleteBranchEntitys(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result",true);
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param branchVo
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/branch_saveOrUpdate",method = RequestMethod.POST)
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,BranchVo branchVo){
		JsonObject json =new JsonObject();
		try{
			Branch branch = new Branch();
			BeanUtils.copyProperties(branchVo, branch);
			this.branchServiceImpl.saveOrUpdateBranch(branch);
			json.addProperty("id", branch.getId());
			json.addProperty("result", true);
		}catch (Exception e) {
			e.printStackTrace();
			json.addProperty("result", false);
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @描述：加载所有党支部
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月21日
	 */
	@RequestMapping(value="/branch_loadAll")
	public void loadAllBranch(HttpServletRequest request,HttpServletResponse response){
		List<Branch> list =this.branchServiceImpl.queryALLEntityList();
		JSONObject json = new JSONObject();
		json.put("total", list.size());
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
		json.put("rows", JSONArray.fromObject(list,config));
		this.print(json);
	}
}
