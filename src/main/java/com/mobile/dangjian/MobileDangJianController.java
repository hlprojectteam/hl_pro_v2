package com.mobile.dangjian;

import java.text.SimpleDateFormat;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.o.common.beans.BeanUtils;

import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.message.service.IMessageService;
import com.common.utils.Common;
import com.common.utils.StrUtils;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.dangjian.module.Activities;
import com.dangjian.module.ActivitiesLaunch;
import com.dangjian.module.ActivitiesLaunchReview;
import com.dangjian.module.Branch;
import com.dangjian.module.Introduction;
import com.dangjian.module.PartyMember;
import com.dangjian.service.IActivitiesService;
import com.dangjian.service.IBranchService;
import com.dangjian.service.IIntroductionService;
import com.dangjian.service.IPartyMemberService;
import com.dangjian.vo.ActivitiesLaunchReviewVo;
import com.dangjian.vo.ActivitiesLaunchVo;
import com.dangjian.vo.ActivitiesVo;
import com.dangjian.vo.IntroductionVo;
import com.dangjian.vo.PartyMemberVo;
import com.google.gson.Gson;
import com.urms.role.module.Role;
import com.urms.role.service.IRoleService;
import com.urms.role.vo.RoleVo;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

/**
 * 
 * @Description 党建移动端接口
 * @author qinyongqian
 * @date 2019年1月3日
 *
 */
@Controller
@RequestMapping(value="/dangJianMobile")
public class MobileDangJianController extends BaseController{
	
	@Autowired
	public IActivitiesService activitiesServiceImpl;
	@Autowired
	public IBranchService branchServiceImpl;
	@Autowired
	public IPartyMemberService partyMemberServiceImpl;
	@Autowired
	public IUserService userServiceImpl;
	@Autowired
	public IIntroductionService introductionServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	public IRoleService roleServiceImpl;
	@Autowired
	public IMessageService messageServiceImpl;
	
	@RequestMapping(value="/activities_list")
	public void mobileNewsList(HttpServletRequest request,HttpServletResponse response,
			ActivitiesVo activitiesVo,Integer page,Integer rows){

		Pager pager = this.activitiesServiceImpl.queryEntityList(page, rows, activitiesVo);
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
		String[] excludes = new String[] { "createTime","content","creatorName","creatorId" }; // 列表排除信息内容字段，减少传递时间
		config.setExcludes(excludes);
		json.put("total", pager.getRowCount());
		json.put("curPageSize", pager.getPageList().size());
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		json.put("result", true);
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param userVo
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2019年1月4日
	 */
	@RequestMapping(value="/get_user_branch")
	public void getUserBranch(HttpServletRequest request,HttpServletResponse response,UserVo userVo){
		
		Branch branch= this.branchServiceImpl.getUserBranch(userVo);
		
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
		if(branch!=null){
			json.put("rows", JSONArray.fromObject(branch,config));
			json.put("result", true);
		}else{
			json.put("result", false);
		}
		this.print(json);
	}
	
	@RequestMapping(value = "/submit_activities")
	public void submitActivities(HttpServletRequest request,HttpServletResponse response, String jsonData) {
		JSONObject json = new JSONObject();
		Gson gson = new Gson();
		try {
			ActivitiesLaunchVo alVo = gson.fromJson(jsonData, ActivitiesLaunchVo.class);
		    Activities ac=activitiesServiceImpl.getEntityById(Activities.class, alVo.getActivityId());
		    if(ac!=null){
		    	alVo.setPoints(ac.getPoints());
		    }
		    if(ac.getFrequency()==7){
		    	alVo.setStatus(0);//未评审状态
		    }else{
		    	alVo.setStatus(3);//默认通过状态
		    }
			ActivitiesLaunch al=new ActivitiesLaunch();
			BeanUtils.copyProperties(alVo, al);
			al.setLaunchDate(DateUtil.getDateFromString(alVo.getLaunchDateStr()));
			
			this.activitiesServiceImpl.saveOrUpdateAL(al);
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
						alr.setActivitiesLaunchId(al.getId());
						alr.setUserId(user.getId());
						this.activitiesServiceImpl.saveOrUpdateALR(alr);
					}
				}
				
				//发送给“党委委员-初审”角色初审
				this.messageServiceImpl.submitSendMsg(
						Common.msgTitle_DJ_ldgz_todo,StrUtils.subString(alVo.getLaunchContent(), 100) ,null,"dangjian_dwwy_cs",Common.msgDJ,this.getSessionUser());
				
			}else{
				//普通活动，发普通给党员浏览
				this.messageServiceImpl.submitSendMsg(
						Common.msgTitle_DJ_hdfb_info,StrUtils.subString(alVo.getLaunchContent(), 100) ,null,"dangjian_dy",Common.msgDJ,this.getSessionUser());
				
			}
			json.put("result", true);
			json.put("msg", "");
			json.put("ActivitiesLaunchId", al.getId());
		} catch (Exception e) {
			e.printStackTrace();
			json.put("result", false);
			json.put("msg", e.toString());
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param partyMemberVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：党员列表
	 * @return
	 * @author: qinyongqian
	 * @date:2019年2月1日
	 */
	@RequestMapping(value="/partyMemberList")
	public void partyMemberList(HttpServletRequest request,HttpServletResponse response,PartyMemberVo partyMemberVo,
			Integer page,Integer rows){
		JSONObject json = new JSONObject();
		json.put("result", false);
		Pager pager = this.partyMemberServiceImpl.queryPartyMemberEntityList(page, rows, partyMemberVo);
		@SuppressWarnings("unchecked")
		List<PartyMemberVo> listVo=pager.getPageList();
		if(listVo!=null){
			JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
			config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
			String[] excludes = new String[] {"createTime","creatorId","creatorName","createUserId","points"
					,"sysCode","branchId","branchName","changeRegularTime","increaseTime","increaseType"
					,"joininTime"}; // 列表排除信息内容字段，减少传递时间
			config.setExcludes(excludes);
			json.put("total", pager.getRowCount());
			json.put("curPageSize", pager.getPageList().size());
			json.put("rows", JSONArray.fromObject(listVo, config));
		}
		json.put("result", true);
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param partyMemberVo
	 * @描述：党员明细
	 * @return
	 * @author: qinyongqian
	 * @date:2019年2月1日
	 */
	@RequestMapping(value="/partyMemberDetail")
	public void partyMemberDetail(HttpServletRequest request,HttpServletResponse response,PartyMemberVo partyMemberVo){
		JSONObject json = new JSONObject();
		json.put("result", false);
		if(partyMemberVo.getId()!=null){
			PartyMember partyMember=this.partyMemberServiceImpl.getEntityById(PartyMember.class, partyMemberVo.getId());
			if(partyMember!=null){
				User user=this.userServiceImpl.getEntityById(User.class, partyMember.getUserId());
				PartyMemberVo pmVo=new PartyMemberVo();
				BeanUtils.copyProperties(partyMember, pmVo);
				pmVo.setUserName(user.getUserName());
				pmVo.setSex(user.getSex());
				pmVo.setMobilePhone(user.getMobilePhone());
				JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
				config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
				String[] excludes = new String[] {"creatorName","creatorId","createTime","sysCode",
						"userId"}; // 列表排除信息内容字段，减少传递时间
				config.setExcludes(excludes);
				
				json.put("result", true);
				json.put("partyMember", JSONObject.fromObject(pmVo, config));
			}
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param introductionVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：党建概述列表
	 * @return
	 * @author: qinyongqian
	 * @date:2019年2月14日
	 */
	@RequestMapping(value="/introduction_list")
	public void introductionList(HttpServletRequest request,HttpServletResponse response,IntroductionVo introductionVo,Integer page,Integer rows){
		introductionVo.setStatus(1);
		Pager pager = this.introductionServiceImpl.queryEntityList(page, rows, introductionVo);
		
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
		String[] excludes = new String[] { "createTime","content","creatorName","creatorId","sysCode"}; // 列表排除信息内容字段，减少传递时间
		config.setExcludes(excludes);
		json.put("total", pager.getRowCount());
		json.put("curPageSize", pager.getPageList().size());
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		json.put("result", true);
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param id
	 * @描述：概述明细
	 * @return
	 * @author: qinyongqian
	 * @date:2019年2月14日
	 */
	@RequestMapping(value="/introduction_detail")
	public void introductionDetail(HttpServletRequest request,HttpServletResponse response,String id){
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		JSONObject json = new JSONObject();
		json.put("result", false);
		json.put("rows", "");
		if(StringUtils.isNotBlank(id) && id.trim()!= ""){
			try{
				Introduction news = new Introduction();
				news.setId(id);
				news = this.introductionServiceImpl.queryEntityById(id);
				if(news != null){
					if(news.getVisitNum() == null){
						news.setVisitNum(1);
					}else{
						news.setVisitNum(news.getVisitNum() + 1);
					}
					IntroductionVo introductionVo =new IntroductionVo();
					BeanUtils.copyProperties(news, introductionVo);
					if(news.getContent()!=null){
						introductionVo.setContentStr(new String(news.getContent(), "UTF-8"));
					}
					introductionVo.setReleaseDateStr(format.format(introductionVo.getReleaseDate()));
					introductionVo = queryAttach(id, introductionVo);
					
					JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
					config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
					String[] excludes = new String[] {"content","createTime","createTimeStr","createUserId","creatorId"
							,"releaseDate","showPlace","status","sysCode","creatorName","delFlag","moduleType","isTop"}; // 列表排除信息内容字段，减少传递时间
					config.setExcludes(excludes);
					
					json.put("result", true);
					json.put("rows", JSONObject.fromObject(introductionVo, config));
					json.put("err", "");
					
					this.introductionServiceImpl.saveOrUpdate(news);
				}else{
					json.put("err", "没有查到该条新闻！");
				}
			}catch (Exception e) {
				e.printStackTrace();
				json.put("err", "解析错误!");
			}
		}else{
			json.put("err", "id为空");
		}
		this.print(json.toString());
	}
	
	private IntroductionVo queryAttach(String id,IntroductionVo introductionVo){
		List<Attach> attachs = this.attachServiceImpl.queryAttchListByFormId(id);
		StringBuilder path = new StringBuilder();
		StringBuilder fileName = new StringBuilder();
		for(int i=0;i<attachs.size();i++){
			Attach attach = attachs.get(i);
			if(StringUtils.isNotBlank(attach.getAttachType()) ){
				if(attach.getAttachType().equals("dj_intro_file")){
					path.append(attach.getPathUpload()+",");
					fileName.append(attach.getFileName()+",");
				}
			}
		}
		if(StringUtils.isNotBlank(path.toString())){
			introductionVo.setFilePath(path.deleteCharAt(path.length()-1).toString());
			introductionVo.setFileName(fileName.deleteCharAt(fileName.length()-1).toString());
		}
		return introductionVo;
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param activitiesLaunchVo
	 * @方法：@param page
	 * @方法：@param rows
	 * @描述：加载活动开展列表
	 * @return
	 * @author: qinyongqian
	 * @date:2019年2月19日
	 */
	@RequestMapping(value="/activitiesLauchList")
	public void activitiesLauchList(HttpServletRequest request,HttpServletResponse response,ActivitiesLaunchVo activitiesLaunchVo,
			Integer page,Integer rows){
		JSONObject json = new JSONObject();
		json.put("result", false);
		try {
			Pager pager = this.activitiesServiceImpl.queryALEntityListPager(page, rows, activitiesLaunchVo);
			@SuppressWarnings("unchecked")
			List<ActivitiesLaunch> list= pager.getPageList(); 
			List<ActivitiesLaunchVo> listVo=new ArrayList<>();
			for (ActivitiesLaunch activitiesLaunch : list) {
				ActivitiesLaunchVo alVo=new ActivitiesLaunchVo();
				BeanUtils.copyProperties(activitiesLaunch, alVo);
				
				String branchId=activitiesLaunch.getBranchId();
				Branch branch= branchServiceImpl.getEntityById(Branch.class, branchId);
				alVo.setBranchName(branch.getBranchName());
				
				String activityId=activitiesLaunch.getActivityId();
				Activities activities= activitiesServiceImpl.getEntityById(Activities.class, activityId);
				alVo.setTitle(activities.getTitle());
				
				alVo.setLaunchContent(StrUtils.subString(activitiesLaunch.getLaunchContent(), 60));
				
				List<Attach> listAttach= this.attachServiceImpl.queryAttchByFormIdAndType(activitiesLaunch.getId(), "dj_activitiesLaunch");
				if(listAttach!=null){
					List<String> imgUrls=new ArrayList<>();
					for (Attach attach : listAttach) {
						imgUrls.add(attach.getPathUpload());
					}
					alVo.setImgUrls(imgUrls);
				}
				listVo.add(alVo);
			}
			JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
			config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
			String[] excludes = new String[] {"createTime","createUserId","creatorId"
					,"sysCode","branchId","completionTimes","isReach","frequency","timeQuantum","year","month"
					,"activityId","exOpinion","isPass","launchAddress","launchDateStr","opinion"
					,"status","timeQuantumValue"}; // 列表排除信息内容字段，减少传递时间
			config.setExcludes(excludes);
			json.put("total", pager.getRowCount());
			json.put("curPageSize", pager.getPageList().size());
			json.put("rows", JSONArray.fromObject(listVo, config));
			json.put("result", true);
		} catch (Exception e) {
			json.put("result", false);
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param id
	 * @描述：活动开展内容明细
	 * @return
	 * @author: qinyongqian
	 * @date:2019年2月19日
	 */
	@RequestMapping(value="/activitiesLauch_detail")
	public void activitiesLauchDetail(HttpServletRequest request,HttpServletResponse response,String id){
		JSONObject json = new JSONObject();
		json.put("result", false);
		if(StringUtils.isNotBlank(id) && id.trim()!= ""){
			try{
				ActivitiesLaunch al=activitiesServiceImpl.getEntityById(ActivitiesLaunch.class, id);
				ActivitiesLaunchVo alVo=new ActivitiesLaunchVo();
				if(al!=null){
					BeanUtils.copyProperties(al, alVo);
					
					String branchId=al.getBranchId();
					Branch branch= branchServiceImpl.getEntityById(Branch.class, branchId);
					alVo.setBranchName(branch.getBranchName());
					
					String activityId=al.getActivityId();
					Activities activities= activitiesServiceImpl.getEntityById(Activities.class, activityId);
					alVo.setTitle(activities.getTitle());
					
					List<Attach> attchList = this.attachServiceImpl.queryAttchListByFormId(id);
					List<String> imgUrls = new ArrayList<String>();
					for (Attach attach : attchList) {
						imgUrls.add(attach.getPathUpload());
					}
					alVo.setImgUrls(imgUrls);	//获取上报人上报隐患时提交的图片url
					
					JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
					config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
					String[] excludes = new String[] {"createTime","createUserId"
							,"sysCode","branchId","completionTimes","isReach","frequency","timeQuantum","year"}; // 列表排除信息内容字段，减少传递时间
					config.setExcludes(excludes);
					json.put("object", JSONObject.fromObject(alVo, config));
					json.put("result", true);
				}else{
					json.put("err", "没有查到该记录！");
				}
			}catch (Exception e) {
				e.printStackTrace();
				json.put("err", "解析错误!");
			}
		}else{
			json.put("err", "id为空");
		}
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @方法：@param request
	 * @方法：@param response
	 * @方法：@param id
	 * @描述：删除活动发布
	 * @return
	 * @author: qinyongqian
	 * @date:2019年6月29日
	 */
	@RequestMapping(value="/activitiesLauch_del")
	public void activitiesLauchDelete(HttpServletRequest request,HttpServletResponse response,String id){
		JSONObject json = new JSONObject();
		json.put("result", false);
		if(StringUtils.isNotBlank(id) && id.trim()!= ""){
			try{
				User user=this.getSessionUser();
				ActivitiesLaunch al=activitiesServiceImpl.getEntityById(ActivitiesLaunch.class, id);
				if(user.getId().equals(al.getCreatorId())){
					//是自己写的活动，可以删除
					this.activitiesServiceImpl.deleteALEntitys(id);
					json.put("result", true);
				}else{
					//否则不能删除
					json.put("msg", "不能删除别人的活动发布");
				}
			}catch (Exception e) {
				e.printStackTrace();
				json.put("msg", e.getMessage());
			}
		}else{
			json.put("msg", "id为空");
		}
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param activitiesLaunchVo
	 * @描述：加载活动评审页面
	 * @return
	 * @author: qinyongqian
	 * @date:2019年4月23日
	 */
	@RequestMapping(value="/activitiesLauch_review",method = RequestMethod.POST)
	public void activitiesLauchSaveOrUpdate(HttpSession httpSession,HttpServletResponse response,ActivitiesLaunchVo activitiesLaunchVo){
		JSONObject json = new JSONObject();
		json.put("result", false);
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
				
				JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
				config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
				String[] excludes = new String[] {"branchId","completionTimes","createTime","creatorId","isReach","launchDateStr",
						"sysCode","timeQuantum","timeQuantumValue","year"}; // 列表排除信息内容字段，减少传递时间
				config.setExcludes(excludes);
				json.put("object", JSONObject.fromObject(alVo, config));
				json.put("result", true);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			this.print(json);
		}
	}
	
	/******************************私有方法****************************************/
	


}
