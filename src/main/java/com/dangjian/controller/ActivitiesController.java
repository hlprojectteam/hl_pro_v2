package com.dangjian.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.common.utils.cache.Cache;
import com.urms.dataDictionary.module.CategoryAttribute;
import com.urms.dataDictionary.service.IDataDictionaryService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
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
import com.common.utils.Common;
import com.common.utils.helper.DateUtil;
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
	@Autowired
	public IDataDictionaryService dataDictionaryServiceImpl;
	
	
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
			json.put("result",false);
			json.put("msg",e.getMessage());
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
				if(ac.getFrequency()!=7){
					//如果不是亮点工作，则状态默认为通过
					activitiesLaunchVo.setStatus(3);
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
					this.sendMsg(Common.msgTitle_DJ_ldgz_todo,"亮点工作评审",null,"dangjian_dwwy_cs",Common.msgDJ,this.getSessionUser());
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
		ActivitiesLaunchVo alVo=new ActivitiesLaunchVo();
		String year=DateUtil.getDateFormatString(new Date(), "YYYY");
		alVo.setYear(year);
		this.getRequest().setAttribute("activitiesLaunchVo", alVo);
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
			Branch branch=this.branchServiceImpl.getEntityById(Branch.class, activitiesLaunchVo.getBranchId());				//党支部
			List<Activities> atList= activitiesServiceImpl.getAllEntity(Activities.class,Order.asc("order"));				//活动集合
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
			request.setAttribute("branchName", branch.getBranchName());
			request.setAttribute("branchId", branch.getId());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "/page/dangjian/activities/activities_launch_collect";
	}


	/**
	 * 党建活动汇总表	导出
	 * @param request
	 * @param response
	 * @param activitiesLaunchVo
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@SuppressWarnings({ "resource", "unchecked" })
	@RequestMapping(value="/activitiesLauch_export")
	public void export(HttpServletRequest request, HttpServletResponse response, ActivitiesLaunchVo activitiesLaunchVo){
		try{
			Branch branch = this.branchServiceImpl.getEntityById(Branch.class, activitiesLaunchVo.getBranchId());

			//excel文件名
			String fileName = activitiesLaunchVo.getYear() + branch.getBranchName() + "党建活动汇总表";

			//创建HSSFWorkbook
			HSSFWorkbook wb = new HSSFWorkbook();

			//创建sheet
			HSSFSheet sheet = wb.createSheet("党建活动汇总表");


			//字体样式
			//标题字体
			HSSFFont font1 = wb.createFont();
			font1.setBold(true);						//字体加粗
			font1.setFontHeightInPoints((short)12);		//字体大小
			//普通数据字体
			HSSFFont font2 = wb.createFont();
			font2.setFontHeightInPoints((short)10);		//字体大小

			//基础样式
			HSSFCellStyle mainStyle = wb.createCellStyle();
			mainStyle.setBorderBottom(BorderStyle.THIN); 	//下边框
			mainStyle.setBorderLeft(BorderStyle.THIN);		//左边框
			mainStyle.setBorderTop(BorderStyle.THIN);		//上边框
			mainStyle.setBorderRight(BorderStyle.THIN);		//右边框
			mainStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
			mainStyle.setAlignment(HorizontalAlignment.CENTER);		 //水平居中
			mainStyle.setWrapText(true);					//设置自动换行


			//标题样式
			HSSFCellStyle style1 = wb.createCellStyle();
			style1.cloneStyleFrom(mainStyle);
			style1.setFont(font1);
			//普通数据样式
			HSSFCellStyle style2 = wb.createCellStyle();
			style2.cloneStyleFrom(mainStyle);
			style2.setFont(font2);
			//第一行样式
			HSSFCellStyle style0 = wb.createCellStyle();
			style0.cloneStyleFrom(style1);
			style0.setAlignment(HorizontalAlignment.LEFT);
			//style0.setBorderBottom(BorderStyle.NONE); 	//下边框
			style0.setBorderLeft(BorderStyle.NONE);		//左边框
			style0.setBorderTop(BorderStyle.NONE);		//上边框
			style0.setBorderRight(BorderStyle.NONE);	//右边框


			JSONArray arrayAl = new JSONArray();
			List<Activities> atList = activitiesServiceImpl.getAllEntity(Activities.class,Order.asc("order"));
			if(atList != null){
				arrayAl = activitiesServiceImpl.collectAL(activitiesLaunchVo,atList);
			}
			JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
			String[] excludes = new String[] {"createTime","creatorId","creatorName","createUserId","cover"
					,"id","order","points","sysCode","content"}; // 列表排除信息内容字段，减少传递时间
			config.setExcludes(excludes);
			@SuppressWarnings("unused")
			JSONArray atArray = JSONArray.fromObject(atList,config);


			//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, atList.size()));
			sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, atList.size()));

			//设置列宽
			for (int i = 0; i < atList.size()+1; i++) {
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*3);
			}


			//创建第一行
			HSSFRow row0 = sheet.createRow(0);
			row0.setHeightInPoints(40);
			//创建单元格  并  设置单元格内容
			for (int i = 0; i < atList.size()+1; i++) {
				row0.createCell(0).setCellStyle(style0);
			}



			//第二行
			HSSFRow row1 = sheet.createRow(1);
			row1.setHeightInPoints(40);
			row1.createCell(0).setCellValue("月份");
			row1.createCell(1).setCellValue(activitiesLaunchVo.getYear() + branch.getBranchName() + "各月工作计划及实施");
			for (int i = 2; i < atList.size()+1; i++) {
				row1.createCell(i);
			}
			for (int i = 0; i < atList.size()+1; i++) {
				row1.getCell(i).setCellStyle(style1);
			}

			//第三行
			HSSFRow row2 = sheet.createRow(2);
			row2.setHeightInPoints(60);
			row2.createCell(0);
			for (int i = 0; i < atList.size(); i++) {
				row2.createCell(i + 1).setCellValue(atList.get(i).getTitle() + "\n" + "( 频率：" + getValueByDictAndKey("dj_activities_frequency", atList.get(i).getFrequency().toString()) + " )");
			}
			for (int i = 0; i < atList.size()+1; i++) {
				row2.getCell(i).setCellStyle(style2);
			}


			int mPoints = 0;	//活动总积分
			int mLDPoints = 0;	//亮点工作的积分

			//数据行（）
			for (int i = 0; i < arrayAl.size(); i++) {
				HSSFRow row = sheet.createRow(i+3);
				row.setHeightInPoints(30);
				row.createCell(0).setCellValue(((JSONObject)arrayAl.get(i)).get("month").toString());
				List<Object> listTimeMonth = (List<Object>) ((JSONObject) arrayAl.get(i)).get("atList");
				for (int j = 0; j < listTimeMonth.size(); j++) {
					if(((JSONArray) listTimeMonth.get(j)).size() > 0){
						/*row.createCell(j+1).setCellValue(listTimeMonth.get(j).toString().substring(2,12));*/
						String tt = "";
						for (int k = 0; k < ((JSONArray) listTimeMonth.get(j)).size(); k++) {
							String str = ((JSONArray) listTimeMonth.get(j)).get(k).toString();
							String[] spanArr = str.replace("|",",").split(",");
							tt += spanArr[0] + "\n";
							if(Integer.parseInt(spanArr[3]) == 7){
								mLDPoints += Integer.parseInt(spanArr[2].toString());
							}else{
								mPoints += Integer.parseInt(spanArr[2].toString());
							}
							/*tt += str.substring(0, 10) + "\n";*/
						}
						row.createCell(j+1).setCellValue(tt);
					}else{
						row.createCell(j+1).setCellValue("");
					}
				}

				//设置普通数据样式
				for (int j = 0; j < listTimeMonth.size()+1; j++) {
					row.getCell(j).setCellStyle(style2);
				}
			}

			row0.getCell(0).setCellValue("        活动总积分：" + mPoints + "          （其中亮点工作得分：" + mLDPoints + "）");


			//将文件存到指定位置
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	//发送响应流方法
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(),"ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");	//要保存的文件名
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @intruduction 通过 字典名称dict 和 字典key 获得 value
	 * @param dict 字典名称
	 * @param key 字典key
	 * @return  value
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public String getValueByDictAndKey(String dict,String key){
		String value = "";
		for (CategoryAttribute ca : Cache.getDictByCode.get(dict)) {
			if(ca.getAttrKey().equals(key)){
				value = ca.getAttrValue();
				break;
			}
		}
		return value;
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
			String msgConent= "日期为"+DateUtil.getDateFormatString(activitiesLaunch.getLaunchDate(),"yyyy-mm-dd")+"的亮点工作";
			if(activitiesLaunch.getStatus()==2){
				//复审通过
				if(activitiesLaunchVo.getIsPass()==0){
					//不同意给分，流程结束
					activitiesLaunch.setStatus(4);
					//发送活动提交者
					this.sendMsg(Common.msgTitle_DJ_ldgz_finish,msgConent+"评审未通过",activitiesLaunch.getCreatorId(),null,Common.msgDJ,this.getSessionUser());
				}else if(activitiesLaunchVo.getIsPass()==1){
					//同意给分，流程结束
					activitiesLaunch.setStatus(3);
					activitiesLaunch.setPoints(activitiesLaunchVo.getPoints());
					//发送活动提交者
					this.sendMsg(Common.msgTitle_DJ_ldgz_finish,msgConent+"评审通过",activitiesLaunch.getCreatorId(),null,Common.msgDJ,this.getSessionUser());
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
							//发送活动提交者
							this.sendMsg(Common.msgTitle_DJ_ldgz_finish,msgConent+"评审未通过",activitiesLaunch.getCreatorId(),null,Common.msgDJ,this.getSessionUser());
						}else{
							//初审通过
							activitiesLaunch.setStatus(1);
							//初审通过后，通知其它党委委员进行复审
							RoleVo roleVo=new RoleVo();
							roleVo.setRoleCode("dangjian_dwwy");
							Role role= roleServiceImpl.getRole(roleVo);
							List<User> userList = new ArrayList<>(role.getUsers());
							StringBuffer userIds=new StringBuffer();
							if(userList!=null){
								//插入
								for (User userDwwy : userList) {
									if(!userDwwy.getId().equals(user.getId())){
										//因为其中一个党委委员是也是初审的角色,即当前user，所以这里要排除这个userId
										userIds.append(userDwwy.getId());
										userIds.append(",");
									}
								}
								String sendUserIds=userIds.substring(0, userIds.length()-1);
								//发送给复审的党委委员
								this.sendMsg(Common.msgTitle_DJ_ldgz_todo,msgConent+"等待评审",sendUserIds,null,Common.msgDJ,this.getSessionUser());
							}
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
							//发送给党委委员打分
							this.sendMsg(Common.msgTitle_DJ_ldgz_todo,msgConent+"等待评审打分",null,"dangjian_dwwy_cs",Common.msgDJ,this.getSessionUser());
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
	 * @方法：@param noticeTitle 通知的提示标题
	 * @方法：@param noticeContent 通知的简要内容
	 * @方法：@param userIds 给谁发通知，用户ID的集合，用","分隔
	 * @方法：@param rodeCodes 给哪一类人发通知，如角色的集合，用","分隔
	 * @方法：@param msgType 消息类型
	 * @方法：@param user 会话用户
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2019年4月19日
	 */
	private void sendMsg(String noticeTitle, String noticeContent,
			String userIds, String rodeCodes, int msgType, User user) {
		try {
			Message msg = new Message();
			msg.setTitle(noticeTitle);
			msg.setContent(noticeContent);
			msg.setAlias(userIds);
			msg.setType(msgType);
			msg.setTags(rodeCodes);
			msg.setSender(user.getUserName());
			msg.setCreatorId(user.getId());
			msg.setCreatorName(user.getUserName());
			msg.setSysCode(user.getSysCode());
			this.messageServiceImpl.saveOrUpdate(msg);
			MessageJpush.sendCommonMsg(noticeTitle, msg);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@RequestMapping(value="/testJpush")
	public void testJpush(HttpSession httpSession,HttpServletResponse response){
//		String noticeTitle=Common.msgTitle_DJ_ldgz_todo;
//		String userIds="40284a8d586759f801588b19159100a7";//app_account
////		String userIds="40284a8d6b365949016b3687d2f4005b";//王保佳
//		String roleCodes="";
//		int msgType=Common.msgDJ;
//		User nowPerson=this.getSessionUser();
//		//发送给“部门安全员”角色
//		this.sendMsg(noticeTitle,"测试",userIds,roleCodes,msgType,nowPerson);
//		System.out.println(Common.msg_platform+"-"+Common.msg_ApnsProduction+"-"+Common.msg_setMessage);
//		this.print("测试");
		
		String noticeTitle=Common.msgTitle_KQ_dm;
		String userIds="40284a8d586759f801588b19159100a7";//app_account
//		String userIds="40284a8d6b365949016b3687d2f4005b";//王保佳
		String roleCodes="";
		int msgType=Common.msgKQ;
		User nowPerson=this.getSessionUser();
		//发送给“部门安全员”角色
		this.sendMsg(noticeTitle,"测试",userIds,roleCodes,msgType,nowPerson);
		System.out.println(Common.msg_platform+"-"+Common.msg_ApnsProduction+"-"+Common.msg_setMessage);
		this.print("测试");
	}
	
	@RequestMapping(value="/findUserIdsByUserIdAndRoleCode")
	public void findUserIdsByUserIdAndRoleCode(HttpSession httpSession,HttpServletResponse response){
		String userId="297ef6c668a340fc0168a34690c60048";
		String roleCodes="event_bmaqy,event_bmfzr"; 
		String ids=this.userServiceImpl.findUserIdsByUserIdAndRoleCode(userId, roleCodes);
		System.out.println(ids);
	}
	
	
	
	

	
}
