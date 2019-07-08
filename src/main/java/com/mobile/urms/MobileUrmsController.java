package com.mobile.urms;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.subsystem.service.ISubsystemService;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

/**
 * 
 * @Description 移动端，基础框架接口
 * @author qinyongqian
 * @date 2019年7月3日
 *
 */
@Controller
@RequestMapping("/mobileUrms")
public class MobileUrmsController extends BaseController{
	
	@Autowired
	public IUserService userServiceImpl;
	@Autowired
	public ISubsystemService subsystemServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	public IOrgFrameService orgFrameServiceImpl;
	
	@RequestMapping(value="/OrgFrameList") 
	public void mobileOrgFrameList(HttpServletRequest request,HttpServletResponse response,OrgFrameVo orgFrameVo){
		JSONObject json = new JSONObject();
		json.put("result", true);
		try{
			List<OrgFrame> orgList  = this.orgFrameServiceImpl.queryOrgFrameList(orgFrameVo);
			if(orgList!=null){
				JsonConfig config = new JsonConfig();
				config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
				String[] excludes = new String[] {"users","roles","createTime","content","creatorName","creatorId"
						,"order","isLeaf","level","pId","pIds","pNames","sysCode","orgFrameType"}; // 列表排除信息内容字段，减少传递时间
				config.setExcludes(excludes);
				json.put("result", true);
				json.put("total", orgList.size());
				json.put("rows", JSONArray.fromObject(orgList,config));
			}
		}catch(Exception e){
			e.printStackTrace();
			json.put("result", false);
			json.put("msg", e.getMessage());
		}
		this.print(json.toString());
	}
	
	@RequestMapping(value="/UserList") 
	public void mobileOrgFrameList(HttpServletRequest request,HttpServletResponse response,Integer page,Integer rows,UserVo userVo){
		JSONObject json = new JSONObject();
		json.put("result", true);
		try{
			User user = this.getSessionUser();
			OrgFrame org=new OrgFrame();
			org.setId(userVo.getOrgFrameId());
			userVo.setOrgFrame(org);
			Pager pager = this.userServiceImpl.queryEntityList(page, rows, userVo, user, 0);
			json.put("total", pager.getRowCount());
			JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
			config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
			config.setExcludes(new String[]{"roles","orgFrame","userExtend","address","certificatesNum","certificatesType","createTime"
					,"creatorId","creatorName","dataSource","degree","educationBackground","email","frontLineSituation","keyWord"
					,"lastLoginTime","loginIp","loginName","loginTimes","memo","nation","orgFrameId","orgFrameName","orgFrameNames","originPlace"
					,"password","personalIdentity","qq","state","sysCode","telephone","type","workTime"
					});  //只要设置这个数组，指定过滤哪些字段。     
			json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
			json.put("result", true);
		}catch(Exception e){
			e.printStackTrace();
			json.put("result", false);
			json.put("msg", e.getMessage());
		}
		this.print(json.toString());
	}

}
