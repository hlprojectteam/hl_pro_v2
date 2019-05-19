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
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.dangjian.module.PartyMember;
import com.dangjian.service.IBranchService;
import com.dangjian.service.IPartyMemberService;
import com.dangjian.vo.PartyMemberVo;
import com.google.gson.JsonObject;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月1日
 *
 */
@Controller
@RequestMapping(value="/dangjian")
public class PartyMemberController extends BaseController{

	@Autowired
	public IPartyMemberService partyMemberServiceImpl;
	@Autowired
	public IBranchService branchServiceImpl;
	@Autowired
	public IUserService userServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param menuCode
	 * @方法：@return
	 * @描述：党员档案列表 
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/partyMember_list")
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode){
		this.getRequest().setAttribute("menuCode", menuCode);
	    return "/page/dangjian/partyMember/partyMember_list";
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param partyMemberVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：加载党员列表 
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/partyMember_load")
	public void load(HttpServletRequest request,HttpServletResponse response,PartyMemberVo partyMemberVo,
			Integer page,Integer rows){
		Pager pager = this.partyMemberServiceImpl.queryPartyMemberEntityList(page, rows, partyMemberVo);
		
		List<PartyMemberVo> listVo=pager.getPageList();
		
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
		json.put("total", pager.getRowCount());
		json.put("curPageSize", pager.getPageList().size());
		json.put("rows", JSONArray.fromObject(listVo, config));
		json.put("result", true);
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param partyMemberVo
	 * @方法：@param type
	 * @方法：@return
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/partyMember_edit")
	public String edit(HttpServletRequest request,PartyMemberVo partyMemberVo){
		if(StringUtils.isNotBlank(partyMemberVo.getId())){
			try{
				PartyMember partyMember = this.partyMemberServiceImpl.getEntityById(PartyMember.class, partyMemberVo.getId());
				BeanUtils.copyProperties(partyMember, partyMemberVo);
				User user=this.userServiceImpl.getEntityById(User.class, partyMember.getUserId());
				partyMemberVo.setUserName(user.getUserName());
				request.setAttribute("partyMemberVo", partyMemberVo);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			request.setAttribute("partyMemberVo", partyMemberVo);
		}
		return "/page/dangjian/partyMember/partyMember_edit";
			
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
	@RequestMapping(value="/partyMember_del") 
	public void delete(HttpServletResponse response,String ids) {
		this.partyMemberServiceImpl.deletePartyMemberEntitys(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result",true);
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param partyMemberVo
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/partyMember_saveOrUpdate",method = RequestMethod.POST)
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,PartyMemberVo partyMemberVo){
		JsonObject json =new JsonObject();
		json.addProperty("result", false);
		try{
			if(StringUtils.isBlank(partyMemberVo.getId())){
				//添加
				PartyMemberVo pVo=new PartyMemberVo();
				pVo.setUserId(partyMemberVo.getUserId());
				Pager pager= this.partyMemberServiceImpl.queryPartyMemberEntityList(1, 1, pVo);
				if(pager!=null){
					if(pager.getRowCount()>0){
						//些用户已经是党员
						json.addProperty("msg", "该用户已经是党员");
					}else{
						//添加
						PartyMember partyMember = new PartyMember();
						BeanUtils.copyProperties(partyMemberVo, partyMember);
						this.partyMemberServiceImpl.saveOrUpdatePartyMember(partyMember);
						json.addProperty("id", partyMember.getId());
						json.addProperty("result", true);
					}
				}
			}else{
				//更新
				PartyMember partyMember = new PartyMember();
				BeanUtils.copyProperties(partyMemberVo, partyMember);
				this.partyMemberServiceImpl.saveOrUpdatePartyMember(partyMember);
				json.addProperty("id", partyMember.getId());
				json.addProperty("result", true);
			}
		}catch (Exception e) {
			e.printStackTrace();
			json.addProperty("result", false);
			json.addProperty("msg", "保存失败");
		}finally{
			this.print(json.toString());
		}
	}

}
