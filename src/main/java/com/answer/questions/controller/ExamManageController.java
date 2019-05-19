package com.answer.questions.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.answer.questions.module.ExamManage;
import com.answer.questions.module.ExamPerson;
import com.answer.questions.service.IQuestionsService;
import com.answer.questions.vo.ExamManageVo;
import com.answer.questions.vo.ExamPersonVo;
import com.common.base.controller.BaseController;
import com.common.poi.ExportExcel;
import com.common.utils.FileTools;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.google.gson.JsonObject;
import com.urms.user.module.User;
import com.urms.user.vo.UserVo;

/**
 * 考卷管理
 * @intruduction 
 * @author Dic
 * @Date 2016年9月8日下午2:19:31
 */
@Controller
@RequestMapping("/answer")
public class ExamManageController extends BaseController{
	
	@Autowired
	public IQuestionsService questionsServiceImpl;
	
	/**
	 * 列表页面
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @return
	 * @author Dic
	 * @Date 2016年9月6日下午3:44:07
	 */
	@RequestMapping(value="/examManage_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/answer/questions/examManage/examManage_list";
	}
	
	/**
	 * 加载列表
	 * @intruduction
	 * @param request
	 * @param response
	 * @param inquireVo
	 * @param page
	 * @param rows
	 * @author Dic
	 * @Date 2016年9月6日下午3:44:17
	 */
	@RequestMapping(value="/examManage_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,ExamManageVo examManageVo,Integer page,Integer rows) {
		Pager pager = this.questionsServiceImpl.queryExamManageList(page, rows, examManageVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	/**
	 * @intruduction 新增or编辑 页面
	 * @param request
	 * @param userVo
	 * @return
	 * @author Dic
	 * @Date 2016年1月6日下午4:13:07
	 */
	@RequestMapping(value="/examManage_edit") 
	public String edit(HttpServletRequest request,ExamManageVo examManageVo) {
		if(org.apache.commons.lang.StringUtils.isNotBlank(examManageVo.getId())){
			ExamManage examManage = this.questionsServiceImpl.getEntityById(ExamManage.class, examManageVo.getId());
			long count = this.questionsServiceImpl.checkIsExam(examManageVo);
			request.setAttribute("count", count);
			BeanUtils.copyProperties(examManage,examManageVo);
			request.setAttribute("examManageVo", examManageVo);
		}else{
			examManageVo.setSysCode(this.getSessionUser().getSysCode());
			request.setAttribute("count", 0);
			request.setAttribute("examManageVo", examManageVo);//新增
		}
		return "/page/answer/questions/examManage/examManage_edit";
	}
	
	/**
	 * 保持or更新
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param inquireVo
	 * @author Dic
	 * @Date 2016年9月6日下午3:44:31
	 */
	@RequestMapping(value="/examManage_saveOrUpdate",method = RequestMethod.POST) 
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,ExamManageVo examManageVo) {
		JsonObject json = new JsonObject();
		try {
			this.questionsServiceImpl.saveOrUpdateExamManage(examManageVo);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}

	
	/**
	 * 删除
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param ids
	 * @author Dic
	 * @Date 2016年9月6日下午4:17:05
	 */
	@RequestMapping(value="/examManage_delete",method = RequestMethod.POST) 
	public void delete(HttpSession httpSession,HttpServletResponse response,String ids) {
		JsonObject json = new JsonObject();
		try {
			int mark  = this.questionsServiceImpl.deleteExamManage(ids);
			if(mark==1){
				json.addProperty("result", true);
			}else{
				json.addProperty("result", false);
				json.addProperty("msg", "该考试已经生成，不能删除！");
			}
		} catch (Exception e) {
			json.addProperty("result", false);
			json.addProperty("msg", "删除失败");
		}
		this.print(json.toString());
	}
	
	/**
	 * 改变状态 撤回 发起
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param inquireVo
	 * @author Dic
	 * @Date 2016年9月8日上午9:50:04
	 */
	@RequestMapping(value="/examManage_changeState",method = RequestMethod.POST) 
	public void changeState(HttpSession httpSession,HttpServletResponse response,String id) {
		JsonObject json = new JsonObject();
		try {
			String sign = this.questionsServiceImpl.saveChangeState(id);
			json.addProperty("result", true);
			json.addProperty("sign", sign);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	//------------------------------------------------------------
	/**
	 * 考试人员页面
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @return
	 * @author Dic
	 * @Date 2016年9月12日上午11:08:30
	 */
	@RequestMapping(value="/examPerson_list") 
	public String examPersonList(HttpSession httpSession,HttpServletResponse response,String id) {
		this.getRequest().setAttribute("id", id);
		return "/page/answer/questions/examManage/examPerson_list";
	}
	
	/**
	 * 考试人员列表
	 * @intruduction
	 * @param request
	 * @param response
	 * @param examManageVo
	 * @param page
	 * @param rows
	 * @author Dic
	 * @Date 2016年9月12日上午11:08:58
	 */
	@RequestMapping(value="/examPerson_load") 
	public void examPersonLoad(HttpServletRequest request,HttpServletResponse response,ExamPersonVo examPersonVo,Integer page,Integer rows,String sortName,String sortOrder) {
		Pager pager = this.questionsServiceImpl.queryExamPsersonList(page, rows,sortName,sortOrder, examPersonVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json.toString());
	}
	
	/**
	 * 删除考试人员
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param ids
	 * @author Dic
	 * @Date 2016年9月12日上午11:52:10
	 */
	@RequestMapping(value="/examPerson_delete",method = RequestMethod.POST) 
	public void examPersonDelete(HttpSession httpSession,HttpServletResponse response,String ids) {
		JsonObject json = new JsonObject();
		try {
			this.questionsServiceImpl.deleteExamPerson(ids);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
		}
		this.print(json.toString());
	}
	
	/**
	 * 增加在线考试人员
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param id
	 * @return
	 * @author Dic
	 * @Date 2016年9月22日下午4:54:01
	 */
	@RequestMapping(value="/examOnline_addPerson") 
	public String addPerson(HttpSession httpSession,HttpServletResponse response,String winName,String examManageId) {
		this.getRequest().setAttribute("winName", winName);
		this.getRequest().setAttribute("examManageId", examManageId);
		return "/page/answer/questions/examManage/examManage_addPerson";
	}
	
	/**
	 * 批量增加新入职考试人员
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param winName
	 * @param examManageId
	 * @return
	 * @author Dic
	 * @Date 2016年12月14日上午10:50:22
	 */
	@RequestMapping(value="/examOnline_addBatchPerson") 
	public String addBatchPerson(HttpSession httpSession,HttpServletResponse response,String winName,String examManageId) {
		this.getRequest().setAttribute("winName", winName);
		this.getRequest().setAttribute("examManageId", examManageId);
		return "/page/answer/questions/examManage/examManage_addBatchPerson";
	}
	
	/**
	 * 新入职考试人员
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param winName
	 * @param examManageId
	 * @return
	 * @author Dic
	 * @throws UnsupportedEncodingException 
	 * @Date 2016年12月14日下午2:19:19
	 */
	@RequestMapping(value="/examOnline_batchPerson") 
	public void batchPerson(HttpSession httpSession,HttpServletResponse response,String examManageId,String personName,String jobNumber) throws UnsupportedEncodingException {
		personName = new String(personName.getBytes("ISO-8859-1"),"UTF-8");//重新编码
		//获得考试管理
		ExamManage examManage = this.questionsServiceImpl.getEntityById(ExamManage.class, examManageId);
		ExamPersonVo examPersonVo = new ExamPersonVo();
		examPersonVo.setExamManage(examManage);
		Pager pager = this.questionsServiceImpl.queryExamPsersonList(1, 1000, "createTime", "desc", examPersonVo);
		List<String> userIdList = new ArrayList<String>();
		for (int i = 0; i < pager.getPageList().size(); i++) {
			ExamPerson examPerson = (ExamPerson) pager.getPageList().get(i);
			userIdList.add(examPerson.getPersonId());
		}
		StringBuffer HQL = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isNotBlank(examManage.getRoleIds())){
			HQL.append("select u from User u join u.roles r where ");
			HQL.append("u.createTime > '"+sdf.format(examManage.getCreateTime())+"' and r.id in ('"+examManage.getRoleIds().replaceAll(",", "','").toString()+"') ");			
			if(StringUtils.isNotBlank(personName))
				HQL.append(" and u.userName like '%"+personName+"%' ");
			if(StringUtils.isNotBlank(jobNumber))
				HQL.append(" and u.jobNumber = '"+jobNumber+"' ");
			HQL.append(" order by u.createTime desc");
		}else if(StringUtils.isNotBlank(examManage.getOrgFrameIds())){
			HQL.append("select u from User u where ");
			HQL.append("u.createTime > '"+sdf.format(examManage.getCreateTime())+"' and u.orgFrame.id in ('"+examManage.getOrgFrameIds().replaceAll(",", "','").toString()+"') ");
			if(StringUtils.isNotBlank(personName))
				HQL.append(" and u.userName like '%"+personName+"%' ");
			if(StringUtils.isNotBlank(jobNumber))
				HQL.append(" and u.jobNumber = '"+jobNumber+"' ");
			HQL.append(" order by u.createTime desc");
		}else{
			//所有新入职人员
			HQL.append("select u from User u where u.createTime > '"+sdf.format(examManage.getCreateTime())+"' order by u.createTime desc");		
		}
		List<User> userList = this.questionsServiceImpl.queryEntityHQLList(HQL.toString(), null, User.class);
		//检查人员是否已经在考试人员表中有记录
		List<UserVo> list = new ArrayList<UserVo>();
		for (User user : userList) {
			if(!userIdList.contains(user.getId())){
				UserVo userVo = new UserVo();
				BeanUtils.copyProperties(user, userVo);
				userVo.setOrgFrameNames(user.getOrgFrame().getOrgFrameName());
				list.add(userVo);
			}
		}
//		this.getRequest().setAttribute("userList", list);
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		config.setExcludes(new String[]{"roles","orgFrame"});  //只要设置这个数组，指定过滤哪些字段。     
		this.print(JSONArray.fromObject(list,config));
	}
	
	/**
	 * 保存新人员
	 * @intruduction
	 * @param httpSession
	 * @param response
	 * @param id
	 * @author Dic
	 * @Date 2016年9月22日下午5:03:18
	 */
	@RequestMapping(value="/examManage_saveAddPerson",method = RequestMethod.POST) 
	public void saveAddPerson(HttpSession httpSession,HttpServletResponse response,ExamManageVo examManageVo) {
		JsonObject json = new JsonObject();
		try {
			examManageVo.setSysCode(this.getSessionUser().getSysCode());//系编码
			this.questionsServiceImpl.saveAddPerson(examManageVo);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	/**
	 * 
	 * @ClassName:ExamManageController.java
	 * @Description:打开导出选择页面
	 * @param httpSession
	 * @param response
	 * @param winName
	 * @param examManageId
	 * @return
	 * @author qinyongqian
	 * @date 2016-12-30
	 */
	@RequestMapping(value="/examManage_selExlPath") 
	public String selExlPath(HttpSession httpSession,HttpServletResponse response,String examManageId) {
		this.getRequest().setAttribute("examManageId", examManageId);
		return "/page/answer/questions/examManage/examManage_selExlPath";
	}
	
	/**
	 * 
	 * @ClassName:ExamManageController.java
	 * @Description:导出成绩表
	 * @param httpSession
	 * @param response
	 * @param examManageId
	 * @param filePath
	 * @author qinyongqian
	 * @date 2016-12-30
	 */
	@RequestMapping(value="/examManage_expScore",method = RequestMethod.POST) 
	public void expScore(HttpSession httpSession,HttpServletResponse response,String examManageId) {
		JsonObject json = new JsonObject();
		boolean flag=false;
		try {
			String rootPath= httpSession.getServletContext().getRealPath("")+"\\common\\download";
			FileTools.delAllFile(rootPath);
			String fileName="score-"+DateUtil.getYYMMDDHHMMSS("")+".xls";
			String filePath=rootPath+"\\"+fileName;
			String fileUrl = this.getRequest().getScheme() //当前链接使用的协议
				    +"://" + this.getRequest().getServerName()//服务器地址 
				    + ":" + this.getRequest().getServerPort() //端口号 
			        +"/common/download/"+fileName;
		
			File file=new File(filePath);
			file.createNewFile();
			if(file.exists()){
				ExamPersonVo examPersonVo=new ExamPersonVo();
				ExamManage em=new ExamManage();
				em.setId(examManageId);
				examPersonVo.setExamManage(em);
				
				String[] headers={"姓名","部门","是否考试","耗时","成绩"}; 
				Pager pager = this.questionsServiceImpl.queryExamPsersonList(1, 10000,null,"asc", examPersonVo);
				try {
					OutputStream out = new FileOutputStream(filePath);
					@SuppressWarnings("unchecked")
					List<ExamPerson> list= pager.getPageList();
					ExportExcel exp=new ExportExcel();
					exp.exportExcel("成绩表", headers, list, out);
					flag=true;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					flag=false;
					json.addProperty("err", e.getMessage());
				}
			}else{
				flag=false;
				json.addProperty("err","文件路径有误");
			}
			json.addProperty("result", flag);
			json.addProperty("fileUrl", fileUrl);
			json.addProperty("fileName", fileName);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
			json.addProperty("err", e.getMessage());
		}finally{
			this.print(json.toString());
		}
	}
	
}