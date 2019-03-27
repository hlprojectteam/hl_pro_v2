package com.institution.controller;

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
import com.google.gson.JsonObject;
import com.institution.dao.IInstitutionReportDao;
import com.institution.module.Institution;
import com.institution.service.IInstitutionReportService;
import com.institution.service.IInstitutionService;
import com.institution.vo.InstitutionVo;
import com.urms.user.dao.IUserDao;
import com.urms.user.module.User;

/**
 * 
 * @Description 企业制度控制类
 * @author xm
 * @date 2016-10-9
 *
 */

@Controller
@RequestMapping(value="/institution")
public class InstitutionController extends BaseController{
	@Autowired
	public IInstitutionService institutionServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	public IInstitutionReportService institutionReportServiceImpl;
	@Autowired 
	public IInstitutionReportDao institutionReportDaoImpl;
	@Autowired 
	public IUserDao userDaoImpl;
	/**
	 * 
	 * @ClassName:InstitutionController.java
	 * @Description:列表页面请求
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @return
	 * @author xm
	 * @date 2016-10-9
	 */
	@RequestMapping(value="/institution_list")
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode){
		this.getRequest().setAttribute("menuCode", menuCode);
	    return "/page/institution/institution_list";
	}
	
	/***
	 * 初始化当前用户组织架构下的所有已读与未读用户
	 */
	@RequestMapping(value="/load_sonOrg_list")
	public void load_sonOrg_list(HttpSession httpSession,HttpServletResponse response,String orgFrameID,String institutionVo_id) throws UnsupportedEncodingException{
		//	List<Object> nameList =null;
		JSONObject json = new JSONObject();
		orgFrameID = new String(orgFrameID.getBytes("iso-8859-1"),"utf-8");
		if(institutionVo_id!=null&&orgFrameID!=null&&orgFrameID.equals("该用户没有组织架构")!=true){//初始化（根据架构查所有子用户）
			// nameList = this.userDaoImpl.queryBySql("select u.id from um_user u,um_orgFrame f where u.orgframe_id = f.id and f.p_id="+"'"+orgFrameID+"'" ); //当前用户的所有子部门用户（数据库目前存在4个）
			 String hadReadedSQL = "SELECT PERSON_NAME FROM p_institution_person p where PERSON_ID  in(select u.id from um_user u,um_orgframe f where u.orgframe_id = f.id and f.p_id="+"'"+orgFrameID+"'"+") and p.institution_ID="+"'"+institutionVo_id+"'";
			 List<Object>  hadReadedCount = this.institutionReportDaoImpl.queryBySql(hadReadedSQL);//已阅读人数
	
			 String notReadSQL = "select u.user_name from um_user u,um_orgframe f where u.orgframe_id = f.id and f.p_id="+"'"+orgFrameID+"'"+" and u.USER_NAME  not in (SELECT PERSON_NAME FROM p_institution_person p where PERSON_ID  in(select u.id from um_user u,um_orgframe f where u.orgframe_id = f.id and f.p_id="+"'"+orgFrameID+"'"+") and p.institution_ID="+"'"+institutionVo_id+"'"+")"; 
			 List<Object>  notReadCount = this.institutionReportDaoImpl.queryBySql(notReadSQL);//未阅读人数
			 json.put("hadReadedCount", hadReadedCount.size());
	  		 json.put("notReadCount", notReadCount.size());
	  		 json.put("notReadName", JSONArray.fromObject(notReadCount));
	  		 json.put("hadReadedName", JSONArray.fromObject(hadReadedCount));
		}else if(institutionVo_id!=null&&orgFrameID.equals("该用户没有组织架构")){//超管查看报表(直接查所有)
			 String hadReadedSQL = "select p.person_name from p_institution_person p where p.institution_id="+"'"+institutionVo_id+"'";
			 List<Object>  hadReadedCount = this.institutionReportDaoImpl.queryBySql(hadReadedSQL);//已阅读人数
	
			 String notReadSQL = "select u.user_name from um_user u where not exists (select * from p_institution_person p where u.id=p.person_id and p.institution_id="+"'"+institutionVo_id+"'"+") and u.ORGFRAME_ID is not null"; 
			 List<Object>  notReadCount = this.institutionReportDaoImpl.queryBySql(notReadSQL);//未阅读人数
			 json.put("hadReadedCount", hadReadedCount.size());
	  		 json.put("notReadCount", notReadCount.size());
	  		 json.put("notReadName", JSONArray.fromObject(notReadCount));
	  		 json.put("hadReadedName", JSONArray.fromObject(hadReadedCount));
			
		}
		//JSONArray json =JSONArray.fromObject(nameList);
		this.print(json);
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
	public void getReportUserList(HttpSession httpSession,HttpServletResponse response,String orgFrameID,String institutionVo_id){
		List<Object> nameList =null;
		if(orgFrameID==""||orgFrameID==null){//初始化没有组织架构的情况下
			 nameList = this.institutionReportDaoImpl.queryBySql("select p.person_name from p_institution_person p where institution_id = "+"'"+institutionVo_id+"'");
		}else{
			nameList = this.institutionReportDaoImpl.queryBySql("select p.person_name from p_institution_person p where orgFrame_id="+"'"+orgFrameID+"'"+" and institution_id = "+"'"+institutionVo_id+"'");
		}
		JSONArray json =JSONArray.fromObject(nameList);
		this.print(json);
	}
	/***
	 * 点击树显示相应未阅读用户
	 */
	@RequestMapping(value="/nodes_NotReaduser_list")
	public void getReportNotReadUserList(HttpSession httpSession,HttpServletResponse response,String orgFrameID,String institutionVo_id){
		List<Object> nameList =null;
		if(orgFrameID==""||orgFrameID==null){//初始化没有组织架构的情况下
			 nameList = this.userDaoImpl.queryBySql("select u.user_name from um_user u  where  not exists (select * from p_institution_person p  where p.PERSON_ID=u.id and p.institution_id = "+"'"+institutionVo_id+"'" +")");
		}else{
			 nameList = this.userDaoImpl.queryBySql("select u.user_name from um_user u  where  not exists (select * from p_institution_person p  where p.PERSON_ID=u.id and p.institution_id = "+"'"+institutionVo_id+"'"+" ) and u.ORGFRAME_ID="+"'"+orgFrameID+"'");
		}
		JSONArray json =JSONArray.fromObject(nameList);
		this.print(json);
	}
	/***
	 * 点击树显示相应报表信息
	 */
	@RequestMapping(value="/pie")
	public void pie(HttpSession httpSession,HttpServletResponse response,String orgFrameID,String institutionVo_id){
		JSONObject json = new JSONObject();
		if(orgFrameID==""||orgFrameID==null){
			List<Object> notReadCount = this.userDaoImpl.queryBySql("select u.user_name from um_user u  where  not exists (select * from p_institution_person p  where p.PERSON_ID=u.id and p.institution_id = "+"'"+institutionVo_id+"'" +")");
			List<Object> hadReadedCount = this.institutionReportDaoImpl.queryBySql("select p.person_name from p_institution_person p where institution_id = "+"'"+institutionVo_id+"'");
			json.put("hadReadedCount", hadReadedCount.size());
			json.put("notReadCount", notReadCount.size());
		}else{
			List<Object> notReadCount = this.userDaoImpl.queryBySql("select u.user_name from um_user u  where  not exists (select * from p_institution_person p  where p.PERSON_ID=u.id and p.institution_id = "+"'"+institutionVo_id+"'"+" ) and u.ORGFRAME_ID="+"'"+orgFrameID+"'");
			List<Object> hadReadedCount = this.institutionReportDaoImpl.queryBySql("select p.person_name from p_institution_person p where orgFrame_id="+"'"+orgFrameID+"'"+" and institution_id = "+"'"+institutionVo_id+"'");
			json.put("hadReadedCount", hadReadedCount.size());
			json.put("notReadCount", notReadCount.size());
		}
		this.print(json);
	}
	/***
	 * 点击数回掉显示数据
	 */
	@RequestMapping(value="/click_tree_data")
	public void click_tree_data(HttpSession httpSession,HttpServletResponse response,String orgFrameID,String institutionVo_id) throws UnsupportedEncodingException{
		JSONObject json = new JSONObject();
		orgFrameID = new String(orgFrameID.getBytes("iso-8859-1"),"utf-8");
		//先判断该节点有没有子节点
		
	   if(orgFrameID.equals("297ef6c65779d187015779f4f78d0000")||orgFrameID.equals("297ef6c657747a9f01577488e5440004")||orgFrameID.equals("297ef6c657747a9f01577482f4e90000")){//点击的节点有子节点（经理室，运营，环龙）
			 String hadReadedSQL = "SELECT PERSON_NAME FROM p_institution_person p where PERSON_ID  in(select u.id from um_user u,um_orgframe f where u.orgframe_id = f.id and f.P_IDS REGEXP "+"'"+orgFrameID+"'"+") and p.institution_ID="+"'"+institutionVo_id+"'";
			 List<Object>  hadReadedCount = this.institutionReportDaoImpl.queryBySql(hadReadedSQL);//已阅读人数 <=orgframe
	
			 String notReadSQL = "select u.user_name from um_user u,um_orgframe f where u.orgframe_id = f.id and f.P_IDS REGEXP "+"'"+orgFrameID+"'"+" and u.USER_NAME  not in (SELECT PERSON_NAME FROM p_institution_person p where PERSON_ID  in(select u.id from um_user u,um_orgframe f where u.orgframe_id = f.id and f.P_IDS REGEXP "+"'"+orgFrameID+"'"+") and p.institution_ID="+"'"+institutionVo_id+"'"+")"; 
			 List<Object>  notReadCount = this.institutionReportDaoImpl.queryBySql(notReadSQL);//未阅读人数
			 
			 String mySelfhadReadedSQL = "select p.person_name from p_institution_person p where orgFrame_id="+"'"+orgFrameID+"'"+" and institution_id = "+"'"+institutionVo_id+"'";
			 List<Object>  mySelfhadReadedCount = this.institutionReportDaoImpl.queryBySql(mySelfhadReadedSQL);//当前节点的用户
	
			 String mySelfnotReadSQL = "select u.user_name from um_user u  where  not exists (select * from p_institution_person p  where p.PERSON_ID=u.id and p.institution_id = "+"'"+institutionVo_id+"'"+" ) and u.ORGFRAME_ID="+"'"+orgFrameID+"'"; 
			 List<Object>  mySelfnotReadCount = this.institutionReportDaoImpl.queryBySql(mySelfnotReadSQL);//当前节点的用户 =orgframe
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
			 String hadReadedSQL = "select p.person_name from p_institution_person p where orgFrame_id="+"'"+orgFrameID+"'"+" and institution_id = "+"'"+institutionVo_id+"'";
			 List<Object>  hadReadedCount = this.institutionReportDaoImpl.queryBySql(hadReadedSQL);//已阅读人数
	
			 String notReadSQL = "select u.user_name from um_user u  where  not exists (select * from p_institution_person p  where p.PERSON_ID=u.id and p.institution_id = "+"'"+institutionVo_id+"'"+" ) and u.ORGFRAME_ID="+"'"+orgFrameID+"'"; 
			 List<Object>  notReadCount = this.institutionReportDaoImpl.queryBySql(notReadSQL);//未阅读人数
			 json.put("hadReadedCount", hadReadedCount.size());
	  		 json.put("notReadCount", notReadCount.size());
	  		 json.put("notReadName", JSONArray.fromObject(notReadCount));
	  		 json.put("hadReadedName", JSONArray.fromObject(hadReadedCount));
			
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @ClassName:InstitutionController.java
	 * @Description:报表
	 * @param httpSession
	 * @param response
	 * @param winName
	 * @param InstitutionVo
	 * @return
	 * @author xm
	 * @date 2016-10-10
	 */
	@RequestMapping(value="/institution_report") 
	public String report(HttpSession httpSession,HttpServletResponse response,String winName,InstitutionVo institutionVo) {
		this.getRequest().setAttribute("winName", winName);
		this.getRequest().setAttribute("institutionVo", institutionVo);
		return "/page/institution/institution_report";
	}
	/**
	 * 
	 * @ClassName:InstitutionController.java
	 * @Description:制度编辑
	 * @return
	 * @author xm
	 * @date 2016-10-9
	 */
	@RequestMapping(value="/institution_edit")
	public String edit(HttpServletRequest request, InstitutionVo institutionVo){
		if(StringUtils.isNotBlank(institutionVo.getId())){
			try{
				Institution institution = this.institutionServiceImpl.
											getEntityById(Institution.class, institutionVo.getId());
				BeanUtils.copyProperties(institution, institutionVo);
				if(institution.getContent()!=null){
					institutionVo.setContentStr(new String(institution.getContent(), "UTF-8"));
				}
				request.setAttribute("institutionVo", institutionVo);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			institutionVo.setAuthorName(this.getSessionUser().getUserName());
			request.setAttribute("institutionVo", institutionVo);
		}
		return "/page/institution/institution_edit";
	}
	/**
	 * 
	 * @ClassName:InstitutionController.java
	 * @Description:保存
	 * @param httpSession
	 * @param response
	 * @param institutionvo
	 * @return  保存对象
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/institution_saveOrUpdate",method = RequestMethod.POST)
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,InstitutionVo institutionVo){
		JsonObject json =new JsonObject();
		try{
			Institution institution = new Institution();
			institutionVo = formatVo(institutionVo);
			BeanUtils.copyProperties(institutionVo, institution);
			 if(StringUtils.isNotEmpty(institutionVo.getContentStr()))
				 institution.setContent(institutionVo.getContentStr().getBytes("UTF8"));
			this.institutionServiceImpl.saveOrUpdate(institution);
			json.addProperty("id", institution.getId());
			json.addProperty("result", true);
		}catch (Exception e) {
			e.printStackTrace();
			json.addProperty("result", false);
		}finally{
			this.print(json.toString());
		}
	}
	
	private InstitutionVo formatVo(InstitutionVo institutionVo){
		User user = this.getSessionUser();
		if(StringUtils.isNotBlank(institutionVo.getId())){
		}else{
			institutionVo.setCreateUserId(user.getId());
		}
		if(StringUtils.isNotBlank(institutionVo.getReleaseDateStr())){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			try {
				institutionVo.setReleaseDate(sdf.parse(institutionVo.getReleaseDateStr()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(institutionVo.getVisitNum() == null){
			institutionVo.setVisitNum(0);
		}
		if(institutionVo.getDelFlag() == null){
			institutionVo.setDelFlag(0);
		}
		if(institutionVo.getStatus() == null){
			institutionVo.setStatus(0);
		}
		return institutionVo;
	}

	/**
	 * 
	 * @ClassName:InstitutionController.java
	 * @Description:删除
	 * @param httpSession
	 * @param response
	 * @param ids
	 * @return  删除对象
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/institution_del") 
	public void delete(HttpServletResponse response,String ids) {
		this.institutionServiceImpl.deleteInstitution(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	/**
	 * 
	 *  @ClassName:InstitutionController.java
	 * @Description:制度页面加载
	 * @param httpSession
	 * @param response
	 * @param institutionvo
	 * @return  分页数据
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/institution_load")
	public void load(HttpServletRequest request,HttpServletResponse response,InstitutionVo institutionVo,
			Integer page,Integer rows){
		Pager pager = this.institutionServiceImpl.queryEntityList(page, rows, institutionVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * 
	 * @ClassName:InstitutionController.java
	 * @Description:状态改变
	 * @param httpSession
	 * @param response
	 * @param id
	 * @return  状态标记
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/institution_changeState",method = RequestMethod.POST) 
	public void changeState(HttpSession httpSession,HttpServletResponse response,String id) {
		JsonObject json = new JsonObject();
		try {
			String sign = this.institutionServiceImpl.saveChangeState(id);
			json.addProperty("result", true);
			json.addProperty("sign", sign);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	@RequestMapping(value="/update_img") 
	public void updateEntityImg(HttpSession httpSession,HttpServletResponse response,String id){
		List<Attach> attachs = this.attachServiceImpl.queryAttchListByFormId(id);
		String path = "";
		for(int i=0;i<attachs.size();i++){
			Attach attach = attachs.get(i);
			if(StringUtils.isNotBlank(attach.getSuffix()) ){
				if(!attach.getAttachType().equals("institutionFile")){
					path = attach.getPathUpload();
				}
			}
		}
		Institution institution = this.institutionServiceImpl.getEntityById(Institution.class, id);
		institution.setCover(path);
		this.institutionServiceImpl.update(institution);
	}
}
