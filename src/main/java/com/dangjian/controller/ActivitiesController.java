package com.dangjian.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.o.common.beans.BeanUtils;

import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.message.MessageJpush;
import com.common.message.module.Message;
import com.common.message.service.IMessageService;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.dangjian.module.Activities;
import com.dangjian.module.ActivitiesLaunch;
import com.dangjian.module.ActivitiesLaunchReview;
import com.dangjian.module.Branch;
import com.dangjian.module.PartyMember;
import com.dangjian.service.IActivitiesService;
import com.dangjian.service.IBranchService;
import com.dangjian.service.IPartyMemberService;
import com.dangjian.vo.ActivitiesLaunchReviewVo;
import com.dangjian.vo.ActivitiesLaunchVo;
import com.dangjian.vo.ActivitiesVo;
import com.google.gson.JsonObject;
import com.urms.role.module.Role;
import com.urms.role.service.IRoleService;
import com.urms.role.vo.RoleVo;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

/**
 * 
 * @Description 党建活动控制层
 * @author qinyongqian
 * @date 2018年12月29日
 *
 */
@Controller
@RequestMapping(value="/dangjian")
public class ActivitiesController extends BaseController{

	@Autowired
	public IActivitiesService activitiesServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	public IBranchService branchServiceImpl;
	@Autowired
	public IUserService userServiceImpl;
	@Autowired
	public IPartyMemberService partyMemberServiceImpl;
	@Autowired
	public IRoleService roleServiceImpl;
	@Autowired
	public IMessageService messageServiceImpl;
	
	
	/***********************活动方法 start*******************************/
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param menuCode
	 * @方法：@return
	 * @描述：党建活动列表 
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/activities_list")
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode){
		this.getRequest().setAttribute("menuCode", menuCode);
	    return "/page/dangjian/activities/activities_list";
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param activitiesVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：加载党建活动列表 
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/activities_load")
	public void load(HttpServletRequest request,HttpServletResponse response,ActivitiesVo activitiesVo,
			Integer page,Integer rows){
		Pager pager = this.activitiesServiceImpl.queryEntityList(page, rows, activitiesVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();
		String[] excludes = new String[] {"createTime","content","cover","creatorId","creatorName",
				"sysCode"}; // 列表排除信息内容字段，减少传递时间
		config.setExcludes(excludes);
		config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param activitiesVo
	 * @方法：@param type
	 * @方法：@return
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/activities_edit")
	public String edit(HttpServletRequest request,ActivitiesVo activitiesVo){
		if(StringUtils.isNotBlank(activitiesVo.getId())){
			try{
				Activities activities = this.activitiesServiceImpl.getEntityById(Activities.class, activitiesVo.getId());
				BeanUtils.copyProperties(activities, activitiesVo);
				request.setAttribute("activitiesVo", activitiesVo);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			request.setAttribute("activitiesVo", activitiesVo);
		}
		return "/page/dangjian/activities/activities_edit";
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
	@RequestMapping(value="/activities_del") 
	public void delete(HttpServletResponse response,String ids) {
		this.activitiesServiceImpl.deleteEntitys(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result",true);
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param activitiesVo
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2018年12月29日
	 */
	@RequestMapping(value="/activities_saveOrUpdate",method = RequestMethod.POST)
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,ActivitiesVo activitiesVo){
		JsonObject json =new JsonObject();
		try{
			Activities activities = new Activities();
			BeanUtils.copyProperties(activitiesVo, activities);
			this.activitiesServiceImpl.saveOrUpdate(activities);
			json.addProperty("id", activities.getId());
			json.addProperty("result", true);
		}catch (Exception e) {
			e.printStackTrace();
			json.addProperty("result", false);
		}finally{
			this.print(json.toString());
		}
	}
	
	@RequestMapping(value="/update_activities_img") 
	public void updateEntityImg(HttpSession httpSession,HttpServletResponse response,String id){
		List<Attach> attachs = this.attachServiceImpl.queryAttchListByFormId(id);
		String path = "";
		for(int i=0;i<attachs.size();i++){
			Attach attach = attachs.get(i);
			if(StringUtils.isNotBlank(attach.getSuffix()) ){
				if(!attach.getAttachType().equals("cover")){
					path = attach.getPathUpload();
				}
			}
		}
		Activities activities = this.activitiesServiceImpl.getEntityById(Activities.class, id);
		activities.setCover(path);
		this.activitiesServiceImpl.update(activities);
	}
	/***********************活动方法 end*******************************/
	
	
	/***********************活动开展方法 start*******************************/
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param menuCode
	 * @方法：@return
	 * @描述：打开我的活动列表，加载当前人同支部的活动列表
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月26日
	 */
	@RequestMapping(value="/activitiesLauch_mylist")
	public String activitiesLauchMyList(HttpSession httpSession,HttpServletResponse response,String menuCode){
		this.getRequest().setAttribute("menuCode", menuCode);
	    return "/page/dangjian/activities/activities_launch_mylist";
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param activitiesLaunchVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：加载当前人同支部的活动列表
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月26日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/activitiesLauch_myload")
	public void activitiesLauchMyLoad(HttpServletRequest request,HttpServletResponse response,ActivitiesLaunchVo activitiesLaunchVo,
			Integer page,Integer rows){
		JSONObject json = new JSONObject();
		json.put("result",false);
		try {
			User user= this.getSessionUser();
			UserVo userVo=new UserVo();
			BeanUtils.copyProperties(user, userVo);
			PartyMember partyMember=this.partyMemberServiceImpl.getPartyMemberByUser(userVo);
			if(partyMember!=null){
				String branchId=partyMember.getBranchId();
				activitiesLaunchVo.setBranchId(branchId);
				Pager pager = this.activitiesServiceImpl.queryALEntityListPager(page, rows, activitiesLaunchVo);
				List<ActivitiesLaunch> listAL=pager.getPageList();
				List<ActivitiesLaunchVo> listAlVo=new ArrayList<>();
				for (ActivitiesLaunch activitiesLaunch : listAL) {
					ActivitiesLaunchVo alVo=new ActivitiesLaunchVo();
					BeanUtils.copyProperties(activitiesLaunch, alVo);
					Activities at=this.activitiesServiceImpl.getEntityById(Activities.class, activitiesLaunch.getActivityId()); 
					alVo.setTitle(at.getTitle());
					Branch branch= branchServiceImpl.getEntityById(Branch.class, activitiesLaunch.getBranchId());
					alVo.setBranchName(branch.getBranchName());
					listAlVo.add(alVo);
				}
				json.put("total", pager.getRowCount());
				json.put("curPageSize", pager.getPageList().size());
				JsonConfig config = new JsonConfig();
				String[] excludes = new String[] {"createTime","activityId","branchId","completionTimes","creatorId",
						"frequency","imgUrls","isReach","launchAddress","launchContent","launchDateStr",
						"sysCode","timeQuantum","year"}; // 列表排除信息内容字段，减少传递时间
				config.setExcludes(excludes);
				config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
				json.put("rows", JSONArray.fromObject(listAlVo,config));
				json.put("result",true);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param activitiesVo
	 * @方法：@return
	 * @描述：编辑活动发布
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月26日
	 */
	@RequestMapping(value="/activitiesLaunch_edit")
	public String activitiesLaunchEdit(HttpServletRequest request,ActivitiesLaunchVo activitiesLaunchVo){
		if(StringUtils.isNotBlank(activitiesLaunchVo.getId())){
			try{
				ActivitiesLaunch al=activitiesServiceImpl.getEntityById(ActivitiesLaunch.class, activitiesLaunchVo.getId());
				ActivitiesLaunchVo alVo=new ActivitiesLaunchVo();
				if(al!=null){
					BeanUtils.copyProperties(al, alVo);
					
					String branchId=al.getBranchId();
					Branch branch= branchServiceImpl.getEntityById(Branch.class, branchId);
					alVo.setBranchName(branch.getBranchName());
					
					String activityId=al.getActivityId();
					Activities activities= activitiesServiceImpl.getEntityById(Activities.class, activityId);
					alVo.setTitle(activities.getTitle());
					alVo.setFrequency(activities.getFrequency());
					
					List<Attach> attchList = this.attachServiceImpl.queryAttchListByFormId(activitiesLaunchVo.getId());
					List<String> imgUrls = new ArrayList<String>();
					for (Attach attach : attchList) {
						imgUrls.add(attach.getPathUpload());
					}
					alVo.setImgUrls(imgUrls);
					request.setAttribute("alObject", alVo);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			User user= this.getSessionUser();
			UserVo userVo=new UserVo();
			activitiesLaunchVo.setCreatorName(user.getUserName());
			BeanUtils.copyProperties(user, userVo);
			PartyMember partyMember=this.partyMemberServiceImpl.getPartyMemberByUser(userVo);
			if(partyMember!=null){
				String branchId=partyMember.getBranchId();
				Branch branch= branchServiceImpl.getEntityById(Branch.class, branchId);
				activitiesLaunchVo.setBranchName(branch.getBranchName());
				activitiesLaunchVo.setBranchId(branchId);
				activitiesLaunchVo.setStatus(0);//默认为0
			}
			request.setAttribute("alObject", activitiesLaunchVo);
		}
		return "/page/dangjian/activities/activities_launch_edit";
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param activitiesLaunchVo
	 * @描述：活动发布保存
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月31日
	 */
	@RequestMapping(value="/activitiesLauchEdit_saveOrUpdate")
	public void activitiesLauchEditSaveOrUpdate(HttpSession httpSession,HttpServletResponse response,ActivitiesLaunchVo activitiesLaunchVo){
		JsonObject json =new JsonObject();
		try{
			if(StringUtils.isBlank(activitiesLaunchVo.getId())){
				//新增活动情况
				Activities ac=activitiesServiceImpl.getEntityById(Activities.class, activitiesLaunchVo.getActivityId());
			    if(ac!=null){
			    	activitiesLaunchVo.setPoints(ac.getPoints());
			    }
				ActivitiesLaunch activitiesLaunch = new ActivitiesLaunch();
				BeanUtils.copyProperties(activitiesLaunchVo, activitiesLaunch);
				this.activitiesServiceImpl.saveOrUpdateAL(activitiesLaunch);
				if(ac.getFrequency()==7){
					//如果活动类型是亮点工作，则同时在活动评审表预插入对应的角色的评审记录，等待评审
					//查找所有的党委委员用户
					RoleVo roleVo=new RoleVo();
					roleVo.setRoleCode("dangjian_dwwy");
					Role role= roleServiceImpl.getRole(roleVo);
					List<User> userList = new ArrayList<>(role.getUsers());
					if(userList!=null){
						//插入
						for (User user : userList) {
							ActivitiesLaunchReview alr=new ActivitiesLaunchReview();
							alr.setActivitiesLaunchId(activitiesLaunch.getId());
							alr.setUserId(user.getId());
							this.activitiesServiceImpl.saveOrUpdateALR(alr);
						}
					}
					//发送给“党委委员-初审”角色初审
					sendJpushMsg("党建通知","亮点工作评审",null,"dangjian_dwwy_cs");
				}
				json.addProperty("id", activitiesLaunch.getId());
			}else{
				//更新活动情况
				ActivitiesLaunch activitiesLaunch=this.activitiesServiceImpl.getEntityById(ActivitiesLaunch.class, activitiesLaunchVo.getId());
				Activities ac=activitiesServiceImpl.getEntityById(Activities.class, activitiesLaunchVo.getActivityId());
			    if(ac!=null){
			    	activitiesLaunchVo.setPoints(ac.getPoints());
			    }
				BeanUtils.copyProperties(activitiesLaunchVo, activitiesLaunch);
				this.activitiesServiceImpl.saveOrUpdateAL(activitiesLaunch);
				json.addProperty("id", activitiesLaunch.getId());
			}
			json.addProperty("result", true);
		}catch (Exception e) {
			e.printStackTrace();
			json.addProperty("result", false);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	@RequestMapping(value="/activitiesLauch_del") 
	public void activitiesLauchDelete(HttpServletResponse response,String ids) {
		this.activitiesServiceImpl.deleteALEntitys(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", true);
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param menuCode
	 * @方法：@return
	 * @描述：打开活动开展情况列表
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月26日
	 */
	@RequestMapping(value="/activitiesLauch_list")
	public String activitiesLauchList(HttpSession httpSession,HttpServletResponse response,String menuCode){
		this.getRequest().setAttribute("menuCode", menuCode);
	    return "/page/dangjian/activities/activities_launch_list";
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param activitiesLaunchVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：加载活动开展记录
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月10日
	 */
	@RequestMapping(value="/activitiesLauch_load")
	public void activitiesLauchLoad(HttpServletRequest request,HttpServletResponse response,ActivitiesLaunchVo activitiesLaunchVo,
			Integer page,Integer rows){
		JSONObject json = new JSONObject();
		json.put("result",false);
		try {
			if(StringUtils.isNotEmpty(activitiesLaunchVo.getActivityId())&&
					StringUtils.isNotEmpty(activitiesLaunchVo.getBranchId())&& 
					StringUtils.isNotEmpty(activitiesLaunchVo.getYear())){
				List<ActivitiesLaunchVo> listAL = this.activitiesServiceImpl.queryALEntityList(activitiesLaunchVo);
				json.put("total", listAL.size());
				JsonConfig config = new JsonConfig();
				String[] excludes = new String[] {"createTime","branchName","imgUrls","launchAddress",
						"launchContent","launchDateStr","points","sysCode","year"}; // 列表排除信息内容字段，减少传递时间
				config.setExcludes(excludes);
				json.put("rows", JSONArray.fromObject(listAL,config));
				json.put("result",true);
			}
		} catch (Exception e) {
			// TODO: handle exception
			json.put("msg",e.getMessage());
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param activitiesLaunchVo
	 * @方法：@return
	 * @描述：党建活动汇总页面
	 * @return
	 * @author: qinyongqian
	 * @date:2019年2月24日
	 */
	@RequestMapping(value="/activitiesLauch_collect")
	public String activitiesLauchCollect(HttpServletRequest request,ActivitiesLaunchVo activitiesLaunchVo){
		try{
			JSONArray arrayAl=new JSONArray();
			List<Activities> atList= activitiesServiceImpl.getAllEntity(Activities.class,Order.asc("order"));
			if(atList!=null){
				arrayAl=activitiesServiceImpl.collectAL(activitiesLaunchVo,atList);
			}
			JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
			String[] excludes = new String[] {"createTime","creatorId","creatorName","createUserId","cover"
					,"id","order","points","sysCode","content"}; // 列表排除信息内容字段，减少传递时间
			config.setExcludes(excludes);
			JSONArray atArray=JSONArray.fromObject(atList,config);
			request.setAttribute("atList", atArray);
			request.setAttribute("alList", arrayAl);
			request.setAttribute("year", activitiesLaunchVo.getYear());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "/page/dangjian/activities/activities_launch_collect";
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param activitiesLaunchVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：查询活动开展列表，根据时间范围
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月21日
	 */
	@RequestMapping(value="/activitiesLauchList_bytimeQuantum")
	public void activitiesLauchListBytimeQuantum(HttpServletRequest request,HttpServletResponse response,ActivitiesLaunchVo activitiesLaunchVo,
			Integer page,Integer rows){
		JSONObject json = new JSONObject();
		json.put("result",false);
		try {
			if(StringUtils.isNotEmpty(activitiesLaunchVo.getActivityId())&&
					StringUtils.isNotEmpty(activitiesLaunchVo.getBranchId())&& 
					StringUtils.isNotEmpty(activitiesLaunchVo.getYear())){
				List<ActivitiesLaunchVo> listAL = this.activitiesServiceImpl.queryALEntityListPagerByTimeQuantum(activitiesLaunchVo);
				json.put("total", listAL.size());
				JsonConfig config = new JsonConfig();
				config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
				String[] excludes = new String[] {"activityId","branchId","branchName","completionTimes","createTime"
						,"creatorId","frequency","imgUrls","isReach","launchContent","launchDateStr","points"
						,"sysCode","timeQuantum","timeQuantumValue","year"}; // 列表排除信息内容字段，减少传递时间
				config.setExcludes(excludes);
				json.put("rows", JSONArray.fromObject(listAL,config));
				json.put("result",true);
			}
		} catch (Exception e) {
			// TODO: handle exception
			json.put("msg",e.getMessage());
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param activitiesLaunchVo
	 * @方法：@return
	 * @描述：活动开展情况明细
	 * @return
	 * @author: qinyongqian
	 * @date:2019年2月25日
	 */
	@RequestMapping(value="/activitiesLauch_detail")
	public String activitiesLauchDetail(HttpServletRequest request,ActivitiesLaunchVo activitiesLaunchVo){
		if(StringUtils.isNotBlank(activitiesLaunchVo.getId()) && activitiesLaunchVo.getId().trim()!= ""){
			try{
				ActivitiesLaunch al=activitiesServiceImpl.getEntityById(ActivitiesLaunch.class, activitiesLaunchVo.getId());
				ActivitiesLaunchVo alVo=new ActivitiesLaunchVo();
				if(al!=null){
					BeanUtils.copyProperties(al, alVo);
					
					String branchId=al.getBranchId();
					Branch branch= branchServiceImpl.getEntityById(Branch.class, branchId);
					alVo.setBranchName(branch.getBranchName());
					
					String activityId=al.getActivityId();
					Activities activities= activitiesServiceImpl.getEntityById(Activities.class, activityId);
					alVo.setTitle(activities.getTitle());
					alVo.setFrequency(activities.getFrequency());
					
					List<Attach> attchList = this.attachServiceImpl.queryAttchListByFormId(activitiesLaunchVo.getId());
					List<String> imgUrls = new ArrayList<String>();
					for (Attach attach : attchList) {
						imgUrls.add(attach.getPathUpload());
					}
					alVo.setImgUrls(imgUrls);	//获取上报人上报隐患时提交的图片url
					
					JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
					config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
					String[] excludes = new String[] {"createTime","createUserId"
							,"sysCode","branchId","completionTimes","isReach","timeQuantum","year"}; // 列表排除信息内容字段，减少传递时间
					config.setExcludes(excludes);
					JSONObject alObject=JSONObject.fromObject(alVo, config);
					request.setAttribute("alObject", alObject);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "/page/dangjian/activities/activities_launch_detail";
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param year
	 * @描述：支部积分排名
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月26日
	 */
	@RequestMapping(value="/activities_ranking")
	public void activitiesRanking(HttpServletRequest request,HttpServletResponse response,String year){
		JSONObject json = new JSONObject();
		json.put("result",false);
		try {
			if(StringUtils.isNotEmpty(year)){
				List<ActivitiesLaunchVo> listAL = this.activitiesServiceImpl.queryActivityRanking(year);
				json.put("total", listAL.size());
				JsonConfig config = new JsonConfig();
				config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
				String[] excludes = new String[] {"activityId","completionTimes","createTime","creatorId","creatorName",
						"frequency","id","imgUrls","isReach","launchAddress","launchContent","launchDate","launchDateStr",
						"sysCode","timeQuantum","timeQuantumValue","title","year"}; // 列表排除信息内容字段，减少传递时间
				config.setExcludes(excludes);
				json.put("rows", JSONArray.fromObject(listAL,config));
				json.put("result",true);
			}
		} catch (Exception e) {
			// TODO: handle exception
			json.put("msg",e.getMessage());
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param menuCode
	 * @方法：@return
	 * @描述：打开活动评审列表页面
	 * @return
	 * @author: qinyongqian
	 * @date:2019年2月27日
	 */
	@RequestMapping(value="/activitiesLauch_reviewlist")
	public String activitiesLauchReviewList(HttpSession httpSession,HttpServletResponse response,String menuCode){
		this.getRequest().setAttribute("menuCode", menuCode);
	    return "/page/dangjian/activities/activities_launch_review_list";
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param activitiesLaunchVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：加载活动评审列表
	 * @return
	 * @author: qinyongqian
	 * @date:2019年2月27日
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/activitiesLauch_reviewload")
	public void activitiesLauchReviewLoad(HttpServletRequest request,HttpServletResponse response,ActivitiesLaunchVo activitiesLaunchVo,
			Integer page,Integer rows){
		JSONObject json = new JSONObject();
		json.put("result",false);
		try {
			User user=this.getSessionUser();
			List<Role> roleList = new ArrayList<>(user.getRoles());
			boolean isCS=false;//是否初审角色
			for (Role role : roleList) {
				if(role.getRoleCode().equals("dangjian_dwwy_cs"))
					isCS=true;
			}
			Integer[] status=null;
			if(isCS){
				//初审角色，查询两个状态的活动记录
				status=new Integer[]{0,2};
			}else{
				status=new Integer[]{1};
			}
			Pager pager = activitiesServiceImpl.queryALByStatusPager(page, rows, status,user.getId());
			List<ActivitiesLaunchVo> listAlVo=pager.getPageList();
			json.put("total", pager.getRowCount());
			JsonConfig config = new JsonConfig();
			String[] excludes = new String[] {"createTime","activityId","branchId","completionTimes","creatorId",
					"frequency","imgUrls","isReach","launchAddress","launchContent","launchDateStr",
					"sysCode","timeQuantum","year"}; // 列表排除信息内容字段，减少传递时间
			config.setExcludes(excludes);
			config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
			json.put("rows", JSONArray.fromObject(listAlVo,config));
			json.put("result",true);
		} catch (Exception e) {
			json.put("err",e.getMessage());
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param activitiesLaunchVo
	 * @方法：@return
	 * @描述：加载活动评审页面
	 * @return
	 * @author: qinyongqian
	 * @date:2019年2月27日
	 */
	@RequestMapping(value="/activitiesLauch_review")
	public String activitiesLauchReview(HttpServletRequest request,ActivitiesLaunchVo activitiesLaunchVo){
		if(StringUtils.isNotBlank(activitiesLaunchVo.getId()) && activitiesLaunchVo.getId().trim()!= ""){
			try{
				ActivitiesLaunch al=activitiesServiceImpl.getEntityById(ActivitiesLaunch.class, activitiesLaunchVo.getId());
				ActivitiesLaunchVo alVo=new ActivitiesLaunchVo();
				if(al!=null){
					BeanUtils.copyProperties(al, alVo);
					
					String branchId=al.getBranchId();
					Branch branch= branchServiceImpl.getEntityById(Branch.class, branchId);
					alVo.setBranchName(branch.getBranchName());
					
					String activityId=al.getActivityId();
					Activities activities= activitiesServiceImpl.getEntityById(Activities.class, activityId);
					alVo.setTitle(activities.getTitle());
					alVo.setFrequency(activities.getFrequency());
					
					List<Attach> attchList = this.attachServiceImpl.queryAttchListByFormId(activitiesLaunchVo.getId());
					List<String> imgUrls = new ArrayList<String>();
					for (Attach attach : attchList) {
						imgUrls.add(attach.getPathUpload());
					}
					alVo.setImgUrls(imgUrls);	//获取上报人上报隐患时提交的图片url
					
					//查询此活动评审记录
					ActivitiesLaunchReviewVo alrVo=new ActivitiesLaunchReviewVo();
					alrVo.setActivitiesLaunchId(activitiesLaunchVo.getId());
					List<ActivitiesLaunchReview> listAlr= activitiesServiceImpl.queryALREntityList(alrVo);
					StringBuffer str=new StringBuffer();
					for (ActivitiesLaunchReview activitiesLaunchReview : listAlr) {
						if(activitiesLaunchReview.getIsPass()!=null){
							String isPass=activitiesLaunchReview.getIsPass()==1?"通过":"不通过";
							User user=this.userServiceImpl.getEntityById(User.class, activitiesLaunchReview.getUserId());
							String opinion= activitiesLaunchReview.getOpinion();
							str.append(user.getUserName()+" ");
							str.append(isPass+" ");
							str.append(opinion);
							str.append("\n");
						}
					}
					if(str.length()>0){
						//str.setLength(str.length()-1);
					}
					alVo.setExOpinion(str.toString());
					request.setAttribute("alObject", alVo);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "/page/dangjian/activities/activities_launch_review";
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param activitiesLaunchVo
	 * @描述：评审保存
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月26日
	 */
	@RequestMapping(value="/activitiesLauchReview_Save",method = RequestMethod.POST)
	public void activitiesLauchReviewSave(HttpSession httpSession,HttpServletResponse response,ActivitiesLaunchVo activitiesLaunchVo){
		JsonObject json =new JsonObject();
		json.addProperty("result", false);
		try{
			User user=this.getSessionUser();
			ActivitiesLaunch activitiesLaunch = this.activitiesServiceImpl.getEntityById(ActivitiesLaunch.class, activitiesLaunchVo.getId());
			if(activitiesLaunch.getStatus()==2){
				//复审通过
				activitiesLaunch.setPoints(activitiesLaunchVo.getPoints());
				if(activitiesLaunchVo.getIsPass()==0){
					//初审未通过
					activitiesLaunch.setStatus(4);
				}else if(activitiesLaunchVo.getIsPass()==1){
					//初审通过
					activitiesLaunch.setStatus(3);
				}
			}else{
				//未评审、初审通过情况
				ActivitiesLaunchReviewVo alrVo=new ActivitiesLaunchReviewVo();
				alrVo.setUserId(user.getId());
				alrVo.setActivitiesLaunchId(activitiesLaunchVo.getId());
				List<ActivitiesLaunchReview> listAlr= activitiesServiceImpl.queryALREntityList(alrVo);
				if(listAlr!=null){
					ActivitiesLaunchReview alr=listAlr.get(0);
					alr.setReviewTime(new Date());
					alr.setOpinion(activitiesLaunchVo.getOpinion());
					alr.setIsPass(activitiesLaunchVo.getIsPass());
					if(activitiesLaunch.getStatus()==0){
						//未评审
						if(activitiesLaunchVo.getIsPass()==0){
							//初审未通过
							activitiesLaunch.setStatus(4);
						}else{
							//初审通过
							activitiesLaunch.setStatus(1);
						}
						alr.setStatusNode(1);
					}else if(activitiesLaunch.getStatus()==1){
						//初审通过
						alr.setStatusNode(2);
						//检查是否所有党委委员都评审过
						ActivitiesLaunchReviewVo alrVo2=new ActivitiesLaunchReviewVo();
						alrVo2.setActivitiesLaunchId(activitiesLaunchVo.getId());
						if(this.activitiesServiceImpl.isAllCheck(alrVo2)){
							//所有通过
							activitiesLaunch.setStatus(2);
						}
					}
					this.activitiesServiceImpl.saveOrUpdateALR(alr);
				}
			}
			this.activitiesServiceImpl.update(activitiesLaunch);
			json.addProperty("id", activitiesLaunch.getId());
			json.addProperty("result", true);
		}catch (Exception e) {
			e.printStackTrace();
			json.addProperty("result", false);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	@RequestMapping(value="/activities_loadTree") 
	public void loadTree(HttpServletResponse response,String id) {
		StringBuffer tree = new StringBuffer();
		tree.append("[");
		tree.append("{");
		tree.append("id:'0',");
		tree.append("pId:'00',");
		tree.append("name:'活动',");
		tree.append("open:true");
		tree.append("},");
		
		List<Activities> activitiesList = this.activitiesServiceImpl.queryALLEntityList();
		for (int i = 0; i < activitiesList.size(); i++) {
			Activities ac = activitiesList.get(i);
			tree.append("{");
			tree.append("id:'"+ac.getId()+"',");
			tree.append("pId:'0',");
			tree.append("name:'"+ac.getTitle()+"'");
			tree.append("},");
		}
		tree.deleteCharAt(tree.toString().length()-1);
		tree.append("]");
		logger.info("输出树结构:"+tree.toString());
		this.print(tree.toString());
	}
	
	
	/***********************活动开展方法 end*******************************/
	
	/**
	 * 
	 * @param noticeTitle 通知的提示标题
	 * @param noticeContent 通知的简要内容
	 * @param userIds 给谁发通知，用户ID的集合，用","分隔
	 * @param tags 给哪一类人发通知，如角色的集合，用","分隔
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2019年4月8日
	 */
	private void sendJpushMsg(String noticeTitle,String noticeContent, String userIds,String tags){
		try {
			Message msg = new Message();
			msg.setTitle(noticeTitle);
			msg.setContent(noticeContent);
			msg.setAlias(userIds);
			msg.setType(5); //消息类型 3 为事件
			msg.setTags(tags);
			msg.setSender(this.getSessionUser().getUserName());
			msg.setCreatorId(this.getSessionUser().getId());
			msg.setCreatorName(this.getSessionUser().getUserName());
			msg.setSysCode(this.getSessionUser().getSysCode());
			this.messageServiceImpl.saveOrUpdate(msg);
			MessageJpush.sendCommonMsg(noticeTitle, msg);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
