package com.education.controller;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.common.utils.helper.Pager;
import com.education.dao.IEducationReportDao;
import com.education.module.Education;
import com.education.service.IEducationReportService;
import com.education.service.IEducationService;
import com.education.vo.EducationVo;
import com.google.gson.JsonObject;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.user.dao.IUserDao;
import com.urms.user.module.User;

/**
 * 
 * @Description 教育培训控制类
 * @author qinyongqian
 * @date 2016-10-9
 *
 */
@Controller
@RequestMapping(value="/education")
public class EducationController extends BaseController{

	@Autowired
	public IEducationService educationServiceImpl;
	@Autowired
	public IEducationReportService educationReportServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired 
	public IEducationReportDao educationReportDaoImpl;
	@Autowired 
	public IUserDao userDaoImpl;
	
	/*
	/***
	 * 根据用户组织架构获取相应节点
	 */
	@RequestMapping(value="/nodes_list")
	public void getNodesList(HttpSession httpSession,HttpServletResponse response){
		User user  = (User)this.getHttpSession().getAttribute("user");
		OrgFrame of =  user.getOrgFrame();
		OrgFrameVo ofv = new OrgFrameVo();
		if(of!=null){
			BeanUtils.copyProperties(of, ofv);
			List<OrgFrame> orgFrames = this.educationReportServiceImpl.getNodes(ofv);
			//JSONObject json = new JSONObject();
			//json.put("orgFrames", JSONArray.fromObject(orgFrames,config));
			//this.print(json.toString());
			JsonConfig config = new JsonConfig();
			config.setExcludes(new String[]{"roles","users"});
			JSONArray json =JSONArray.fromObject(orgFrames,config);
			this.print(json);
		}else{
			this.print("该角色没有组织架构！");
		}
	}
	/***
	 * 根据用户组织架构获取相应用户
	 */
	@RequestMapping(value="/get_user")
	public void getUser(HttpSession httpSession,HttpServletResponse response){
		User user  = (User)this.getHttpSession().getAttribute("user");
		if(user.getOrgFrame()!=null){
			this.print(user.getOrgFrame().getId());
		}else{
			this.print("该用户没有组织架构");
		}
	}
	
	/***
	 * 点击树显示相应阅读用户
	 */
	@RequestMapping(value="/nodes_user_list")
	public void getReportUserList(HttpSession httpSession,HttpServletResponse response,String orgFrameID,String educationVo_id){
		List<Object> nameList =null;
		if(orgFrameID==""||orgFrameID==null){//初始化没有组织架构的情况下
			 nameList = this.educationReportDaoImpl.queryBySql("select p.person_name from p_education_person p where education_id = "+"'"+educationVo_id+"'");
		}else{
			nameList = this.educationReportDaoImpl.queryBySql("select p.person_name from p_education_person p where orgFrame_id="+"'"+orgFrameID+"'"+" and education_id = "+"'"+educationVo_id+"'");
		}
		JSONArray json =JSONArray.fromObject(nameList);
		this.print(json);
	}
	/***
	 * 点击树显示相应未阅读用户
	 */
	@RequestMapping(value="/nodes_NotReaduser_list")
	public void getReportNotReadUserList(HttpSession httpSession,HttpServletResponse response,String orgFrameID,String educationVo_id){
		List<Object> nameList =null;
		if(orgFrameID==""||orgFrameID==null){//初始化没有组织架构的情况下
			 nameList = this.userDaoImpl.queryBySql("select u.user_name from um_user u  where  not exists (select * from p_education_person p  where p.PERSON_ID=u.id and p.education_id = "+"'"+educationVo_id+"'" +")");
		}else{
			 nameList = this.userDaoImpl.queryBySql("select u.user_name from um_user u  where  not exists (select * from p_education_person p  where p.PERSON_ID=u.id and p.education_id = "+"'"+educationVo_id+"'"+" ) and u.ORGFRAME_ID="+"'"+orgFrameID+"'");
		}
		JSONArray json =JSONArray.fromObject(nameList);
		this.print(json);
	}
	
	/***
	 * 初始化当前用户组织架构下的所有已读与未读用户
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value="/load_sonOrg_list")
	public void load_sonOrg_list(HttpSession httpSession,HttpServletResponse response,String orgFrameID,String educationVo_id) throws UnsupportedEncodingException{
		//	List<Object> nameList =null;
		JSONObject json = new JSONObject();
		orgFrameID = new String(orgFrameID.getBytes("iso-8859-1"),"utf-8");
		if(educationVo_id!=null&&orgFrameID!=null&&orgFrameID.equals("该用户没有组织架构")!=true){//初始化
			// nameList = this.userDaoImpl.queryBySql("select u.id from um_user u,um_orgFrame f where u.orgframe_id = f.id and f.p_id="+"'"+orgFrameID+"'" ); //当前用户的所有子部门用户（数据库目前存在4个）
			 String hadReadedSQL = "SELECT PERSON_NAME FROM p_education_person p where PERSON_ID  in(select u.id from um_user u,um_orgframe f where u.orgframe_id = f.id and f.p_id="+"'"+orgFrameID+"'"+") and p.EDUCATION_ID="+"'"+educationVo_id+"'";
			 List<Object>  hadReadedCount = this.educationReportDaoImpl.queryBySql(hadReadedSQL);//已阅读人数
	
			 String notReadSQL = "select u.user_name from um_user u,um_orgframe f where u.orgframe_id = f.id and f.p_id="+"'"+orgFrameID+"'"+" and u.USER_NAME  not in (SELECT PERSON_NAME FROM p_education_person p where PERSON_ID  in(select u.id from um_user u,um_orgframe f where u.orgframe_id = f.id and f.p_id="+"'"+orgFrameID+"'"+") and p.EDUCATION_ID="+"'"+educationVo_id+"'"+")"; 
			 List<Object>  notReadCount = this.educationReportDaoImpl.queryBySql(notReadSQL);//未阅读人数
			 json.put("hadReadedCount", hadReadedCount.size());
	  		 json.put("notReadCount", notReadCount.size());
	  		 json.put("notReadName", JSONArray.fromObject(notReadCount));
	  		 json.put("hadReadedName", JSONArray.fromObject(hadReadedCount));
		}else if(educationVo_id!=null&&orgFrameID.equals("该用户没有组织架构")){//超管查看报表
			 String hadReadedSQL = "select p.person_name from p_education_person p where p.education_id="+"'"+educationVo_id+"'";
			 List<Object>  hadReadedCount = this.educationReportDaoImpl.queryBySql(hadReadedSQL);//已阅读人数
	
			 String notReadSQL = "select u.user_name from um_user u where not exists (select * from p_education_person p where u.id=p.person_id and p.education_id="+"'"+educationVo_id+"'"+") and u.ORGFRAME_ID is not null"; 
			 List<Object>  notReadCount = this.educationReportDaoImpl.queryBySql(notReadSQL);//未阅读人数
			 json.put("hadReadedCount", hadReadedCount.size());
	  		 json.put("notReadCount", notReadCount.size());
	  		 json.put("notReadName", JSONArray.fromObject(notReadCount));
	  		 json.put("hadReadedName", JSONArray.fromObject(hadReadedCount));
			
		}
		//JSONArray json =JSONArray.fromObject(nameList);
		this.print(json);
	}
	
	/***
	 * 点击数回掉显示数据
	 */
	@RequestMapping(value="/click_tree_data")
	public void click_tree_data(HttpSession httpSession,HttpServletResponse response,String orgFrameID,String educationVo_id) throws UnsupportedEncodingException{
		JSONObject json = new JSONObject();
		orgFrameID = new String(orgFrameID.getBytes("iso-8859-1"),"utf-8");
		//先判断该节点有没有子节点
		
	   if(orgFrameID.equals("297ef6c65779d187015779f4f78d0000")||orgFrameID.equals("297ef6c657747a9f01577488e5440004")||orgFrameID.equals("297ef6c657747a9f01577482f4e90000")){//点击的节点有子节点（经理室，运营，环龙）
			 String hadReadedSQL = "SELECT PERSON_NAME FROM p_education_person p where PERSON_ID  in(select u.id from um_user u,um_orgframe f where u.orgframe_id = f.id and f.P_IDS REGEXP "+"'"+orgFrameID+"'"+") and p.EDUCATION_ID="+"'"+educationVo_id+"'";
			 List<Object>  hadReadedCount = this.educationReportDaoImpl.queryBySql(hadReadedSQL);//已阅读人数
	
			 String notReadSQL = "select u.user_name from um_user u,um_orgframe f where u.orgframe_id = f.id and f.P_IDS REGEXP "+"'"+orgFrameID+"'"+" and u.USER_NAME  not in (SELECT PERSON_NAME FROM p_education_person p where PERSON_ID  in(select u.id from um_user u,um_orgframe f where u.orgframe_id = f.id and f.P_IDS REGEXP "+"'"+orgFrameID+"'"+") and p.EDUCATION_ID="+"'"+educationVo_id+"'"+")"; 
			 List<Object>  notReadCount = this.educationReportDaoImpl.queryBySql(notReadSQL);//未阅读人数
			
			 String mySelfhadReadedSQL = "select p.person_name from p_education_person p where orgFrame_id="+"'"+orgFrameID+"'"+" and education_id = "+"'"+educationVo_id+"'";
			 List<Object>  mySelfhadReadedCount = this.educationReportDaoImpl.queryBySql(mySelfhadReadedSQL);//当前节点的用户
	
			 String mySelfnotReadSQL = "select u.user_name from um_user u  where  not exists (select * from p_education_person p  where p.PERSON_ID=u.id and p.education_id = "+"'"+educationVo_id+"'"+" ) and u.ORGFRAME_ID="+"'"+orgFrameID+"'"; 
			 List<Object>  mySelfnotReadCount = this.educationReportDaoImpl.queryBySql(mySelfnotReadSQL);//当前节点的用户 =orgframe
			 if(mySelfhadReadedCount.size()>0){
				 hadReadedCount.addAll(mySelfhadReadedCount);
			 }
			 if(mySelfnotReadCount.size()>0){
				 notReadCount.addAll(mySelfnotReadCount);
			 }
			 
			 
			 json.put("hadReadedCount", hadReadedCount.size());
	  		 json.put("notReadCount", notReadCount.size());
	  		 json.put("notReadName", JSONArray.fromObject(notReadCount));
	  		 json.put("hadReadedName", JSONArray.fromObject(hadReadedCount));
		
		}else{//点击的节点没有子节点
			 String hadReadedSQL = "select p.person_name from p_education_person p where orgFrame_id="+"'"+orgFrameID+"'"+" and education_id = "+"'"+educationVo_id+"'";
			 List<Object>  hadReadedCount = this.educationReportDaoImpl.queryBySql(hadReadedSQL);//已阅读人数
	
			 String notReadSQL = "select u.user_name from um_user u  where  not exists (select * from p_education_person p  where p.PERSON_ID=u.id and p.education_id = "+"'"+educationVo_id+"'"+" ) and u.ORGFRAME_ID="+"'"+orgFrameID+"'"; 
			 List<Object>  notReadCount = this.educationReportDaoImpl.queryBySql(notReadSQL);//未阅读人数
			 json.put("hadReadedCount", hadReadedCount.size());
	  		 json.put("notReadCount", notReadCount.size());
	  		 json.put("notReadName", JSONArray.fromObject(notReadCount));
	  		 json.put("hadReadedName", JSONArray.fromObject(hadReadedCount));
			
		}
		this.print(json);
	}
	
	
	
	
	/***
	 * 点击树显示相应报表信息
	 */
	@RequestMapping(value="/pie")
	public void pie(HttpSession httpSession,HttpServletResponse response,String orgFrameID,String educationVo_id){
		JSONObject json = new JSONObject();
		if(orgFrameID==""||orgFrameID==null){
			List<Object> notReadCount = this.userDaoImpl.queryBySql("select u.user_name from um_user u  where  not exists (select * from p_education_person p  where p.PERSON_ID=u.id and p.education_id = "+"'"+educationVo_id+"'" +")");
			List<Object> hadReadedCount = this.educationReportDaoImpl.queryBySql("select p.person_name from p_education_person p where education_id = "+"'"+educationVo_id+"'");
			json.put("hadReadedCount", hadReadedCount.size());
			json.put("notReadCount", notReadCount.size());
		}else{
			List<Object> notReadCount = this.userDaoImpl.queryBySql("select u.user_name from um_user u  where  not exists (select * from p_education_person p  where p.PERSON_ID=u.id and p.education_id = "+"'"+educationVo_id+"'"+" ) and u.ORGFRAME_ID="+"'"+orgFrameID+"'");
			List<Object> hadReadedCount = this.educationReportDaoImpl.queryBySql("select p.person_name from p_education_person p where orgFrame_id="+"'"+orgFrameID+"'"+" and education_id = "+"'"+educationVo_id+"'");
			json.put("hadReadedCount", hadReadedCount.size());
			json.put("notReadCount", notReadCount.size());
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @ClassName:EducationController.java
	 * @Description:列表页面请求
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param type 1文档 2视频 3音频
	 * @return
	 * @author qinyongqian
	 * @date 2016-10-9
	 */
	@RequestMapping(value="/education_list")
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode,Integer type){
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("type", type);
	    return "/page/education/education_list";
	}
	
	/**
	 * 
	 * @ClassName:EducationController.java
	 * @Description:列表数据加载
	 * @param request
	 * @param response
	 * @param educationVo
	 * @param page
	 * @param rows
	 * @author qinyongqian
	 * @date 2016-10-9
	 */
	@RequestMapping(value="/education_load")
	public void load(HttpServletRequest request,HttpServletResponse response,EducationVo educationVo,
			Integer page,Integer rows){
		Pager pager = this.educationServiceImpl.queryEntityList(page, rows, educationVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * 
	 * @ClassName:EducationController.java
	 * @Description:培训材料编辑
	 * @param request
	 * @param educationVo
	 * @param newType
	 * @return
	 * @author qinyongqian
	 * @date 2016-10-9
	 */
	@RequestMapping(value="/education_edit")
	public String edit(HttpServletRequest request, EducationVo educationVo,Integer type){
		if(StringUtils.isNotBlank(educationVo.getId())){
			try{
				Education education = this.educationServiceImpl.
											getEntityById(Education.class, educationVo.getId());
				BeanUtils.copyProperties(education, educationVo);
				request.setAttribute("educationVo", educationVo);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			educationVo.setType(type);
			request.setAttribute("educationVo", educationVo);
		}
			return "/page/education/education_edit";
	}
	
	/**
	 * 
	 * @ClassName:EducationController.java
	 * @Description:培训材料保存
	 * @param httpSession
	 * @param response
	 * @param educationVo
	 * @author qinyongqian
	 * @date 2016-10-9
	 */
	@RequestMapping(value="/education_saveOrUpdate")
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,EducationVo educationVo){
		JsonObject json =new JsonObject();
		try{
			Education education = new Education();
			educationVo = formatVo(educationVo);
			BeanUtils.copyProperties(educationVo, education);
			this.educationServiceImpl.saveOrUpdate(education);
			json.addProperty("id", education.getId());
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
	 * @ClassName:EducationController.java
	 * @Description:删除培训材料
	 * @param response
	 * @param ids
	 * @author qinyongqian
	 * @date 2016-10-9
	 */
	@RequestMapping(value="/education_delete") 
	public void delete(HttpServletResponse response,String ids) {
		this.educationServiceImpl.deleteEducations(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @ClassName:EducationController.java
	 * @Description: 更新缩略图片路径
	 * @param httpSession
	 * @param response
	 * @param id
	 * @author qinyongqian
	 * @date 2016-10-9
	 */
	@RequestMapping(value="/update_img") 
	public void updateEntityImg(HttpSession httpSession,HttpServletResponse response,String id){
		List<Attach> attachs = this.attachServiceImpl.queryAttchListByFormId(id);
		String path = "";
		for(int i=0;i<attachs.size();i++){
			Attach attach = attachs.get(i);
			if(StringUtils.isNotBlank(attach.getSuffix()) ){
				if(!attach.getAttachType().equals("educationFile")){
					path = attach.getPathUpload();
				}
			}
		}
		Education education = this.educationServiceImpl.queryEntityById(id);
		education.setCover(path);
		this.educationServiceImpl.update(education);
	}
	
	private EducationVo formatVo(EducationVo educationVo){
		User user = this.getSessionUser();
		if(StringUtils.isNotBlank(educationVo.getId())){
		}else{
			educationVo.setCreateUserId(user.getId());
		}
		if(StringUtils.isNotBlank(educationVo.getReleaseDateStr())){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			try {
				educationVo.setReleaseDate(sdf.parse(educationVo.getReleaseDateStr()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(educationVo.getDownloadNum() == null){
			educationVo.setDownloadNum(0);
		}
		if(educationVo.getVisitNum() == null){
			educationVo.setVisitNum(0);
		}
		if(educationVo.getDelFlag() == null){
			educationVo.setDelFlag(0);
		}
		if(educationVo.getStatus() == null){
			educationVo.setStatus(0);
		}
		return educationVo;
	}
	
	/**
	 * 
	 * @ClassName:EducationController.java
	 * @Description:改变发布状态
	 * @param httpSession
	 * @param response
	 * @param id
	 * @author qinyongqian
	 * @date 2016-10-9
	 */
	@RequestMapping(value="/education_changeState",method = RequestMethod.POST) 
	public void changeState(HttpSession httpSession,HttpServletResponse response,String id) {
		JsonObject json = new JsonObject();
		try {
			String sign = this.educationServiceImpl.saveChangeState(id);
			json.addProperty("result", true);
			json.addProperty("sign", sign);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 
	 * @ClassName:EducationController.java
	 * @Description:报表
	 * @param httpSession
	 * @param response
	 * @param winName
	 * @param educationVo
	 * @return
	 * @author qinyongqian
	 * @date 2016-10-10
	 */
	@RequestMapping(value="/education_report") 
	public String report(HttpSession httpSession,HttpServletResponse response,String winName,EducationVo educationVo) {
		this.getRequest().setAttribute("winName", winName);
		this.getRequest().setAttribute("educationVo", educationVo);
		return "/page/education/education_report";
	}
	
}
