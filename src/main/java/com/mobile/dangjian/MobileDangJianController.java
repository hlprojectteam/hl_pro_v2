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
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.dangjian.module.Activities;
import com.dangjian.module.ActivitiesLaunch;
import com.dangjian.module.Branch;
import com.dangjian.module.Introduction;
import com.dangjian.module.PartyMember;
import com.dangjian.service.IActivitiesService;
import com.dangjian.service.IBranchService;
import com.dangjian.service.IIntroductionService;
import com.dangjian.service.IPartyMemberService;
import com.dangjian.vo.ActivitiesLaunchVo;
import com.dangjian.vo.ActivitiesVo;
import com.dangjian.vo.IntroductionVo;
import com.dangjian.vo.PartyMemberVo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
			ActivitiesLaunch al=new ActivitiesLaunch();
			BeanUtils.copyProperties(alVo, al);
			al.setLaunchDate(DateUtil.getDateFromString(alVo.getLaunchDateStr()));
			
			this.activitiesServiceImpl.saveOrUpdateAL(al);
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
		Pager pager = this.partyMemberServiceImpl.queryPartyMemberEntityList(page, rows, partyMemberVo);
		List<PartyMemberVo> listVo=pager.getPageList();
		
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
		String[] excludes = new String[] {"createTime","creatorId","creatorName","createUserId","points"
				,"sysCode","branchId","branchName","changeRegularTime","increaseTime","increaseType"
				,"joininTime"}; // 列表排除信息内容字段，减少传递时间
		config.setExcludes(excludes);
		json.put("total", pager.getRowCount());
		json.put("curPageSize", pager.getPageList().size());
		json.put("rows", JSONArray.fromObject(listVo, config));
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
	public void partyMemberList(HttpServletRequest request,HttpServletResponse response,PartyMemberVo partyMemberVo){
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
		Pager pager = this.activitiesServiceImpl.queryALEntityListPager(page, rows, activitiesLaunchVo);
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
			
			listVo.add(alVo);
		}
		
		JSONObject json = new JSONObject();
		JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
		config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
		String[] excludes = new String[] {"createTime","createUserId"
				,"sysCode","branchId","completionTimes","isReach","frequency","timeQuantum","year"}; // 列表排除信息内容字段，减少传递时间
		config.setExcludes(excludes);
		json.put("total", pager.getRowCount());
		json.put("curPageSize", pager.getPageList().size());
		json.put("rows", JSONArray.fromObject(listVo, config));
		json.put("result", true);
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
	 * @方法：@param httpSession
	 * @方法：@param response
	 * @方法：@param activitiesLaunchVo
	 * @描述：评审活动，打分
	 * @return
	 * @author: qinyongqian
	 * @date:2019年3月25日
	 */
	@RequestMapping(value="/activitiesLauch_review",method = RequestMethod.POST)
	public void activitiesLauchSaveOrUpdate(HttpSession httpSession,HttpServletResponse response,ActivitiesLaunchVo activitiesLaunchVo){
		JsonObject json =new JsonObject();
		try{
			ActivitiesLaunch activitiesLaunch = this.activitiesServiceImpl.getEntityById(ActivitiesLaunch.class, activitiesLaunchVo.getId());
			activitiesLaunch.setPoints(activitiesLaunchVo.getPoints());
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
	


}
