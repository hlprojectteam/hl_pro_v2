package com.mobile.login;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.o.common.beans.BeanUtils;

import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.message.MessageJpush;
import com.common.message.module.Message;
import com.common.message.service.IMessageService;
import com.common.utils.Common;
import com.common.utils.helper.JsonDateValueProcessor;
import com.google.gson.Gson;
import com.urms.loginLog.module.LoginLog;
import com.urms.loginLog.service.ILoginLogService;
import com.urms.orgFrame.dao.IOrgFrameDao;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.role.module.Role;
import com.urms.subsystem.service.ISubsystemService;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

/**
 * @intruduction 移动端登录
 * @author Dic
 * @Date 2015年12月26日下午1:17:08
 */
@Controller
public class MobileLoginController extends BaseController{
	
	@Autowired
	public IUserService userServiceImpl;
	@Autowired
	public ILoginLogService loginLogServiceImpl;
	@Autowired
	public ISubsystemService subsystemServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	public IOrgFrameService orgFrameServiceImpl;
	
	@Autowired 
	public IOrgFrameDao orgFrameDaoImpl;
	@Autowired
	public IMessageService messageServiceImpl;
	
	/**
	 * @intruduction：用户登录后台主页
	 * @param request
	 * @param response
	 * @param loginName 用户名
	 * @param password 密码 md5
	 * @param attr
	 * @return
	 * @author Dic
	 * @Date 2015年12月26日下午1:15:56
	 */
	@RequestMapping(value="/mobileLogin") 
	public void mobileLogin(HttpServletRequest request,HttpServletResponse response,String loginName,String password) {
		JSONObject json = new JSONObject();
		boolean isLogin;
		String msg = "";
		User user=null;
			if(StringUtils.isNotBlank(loginName)){
				user = userServiceImpl.login(loginName, password,null);
				if(user!=null){
					if(user.getState()==1){//正常账号
						//写入session
						this.getHttpSession().setAttribute("user", user);
						List<Attach> attachList = attachServiceImpl.queryAttchListByFormId(user.getId());
						String avatarUrl = null;
						for (int i = 0; i < attachList.size(); i++) {
							this.getHttpSession().setAttribute("attachPhoto", attachList.get(0));//头像路径加入缓存
							avatarUrl = attachList.get(0).getPathUpload();
						}
						logger.debug("用户:"+user.getLoginName()+" 登录系统，时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						msg = "登录成功！";
						/*保存登录信息 start*/
						saveLoginLog(request,user.getLoginName(),user.getUserName(),user.getType(),user.getId());  // 调用保存日志
						
						/*保存登录信息 end*/
						isLogin = true;
						//json.put("user", JSONObject.fromObject(user, new JsonConfig()));
						json.put("user", this.getUserJson(user,avatarUrl));
						//写入token
//						String tokenStr=addToken(user);
//						json.put("token", tokenStr);
					}else{//冻结
						msg = "该账号已经被冻结或未激活！";
						isLogin = false;
					}
				}else{//账号密码错误
					msg = "账号或密码错误！";
					isLogin = false;
				}
			}else{
				msg = "请重新登录！";
				isLogin = false;
			}
		//}
		json.put("result", isLogin);
		json.put("msg", msg);
		this.print(json.toString());
	}
	/**
	 * 
	 * @ClassName:MobileLoginController.java
	 * @Description:得到user json对象
	 * @param u
	 * @return
	 * @author qinyongqian
	 * @date 2016-8-13
	 */
	private JSONObject getUserJson(User u,String url){
		JSONObject json=new JSONObject();
		try {
			@SuppressWarnings("rawtypes")
			Class cls = u.getClass();
			java.lang.reflect.Field[] flds = cls.getDeclaredFields();
			if ( flds != null )
			{
				for (Field field : flds) {
					field.setAccessible(true); 
					if(field.get(u)!=null && !"".equals(field.get(u).toString())){
					    //System.out.println(field.getName()+"   "+field.get(u).toString());
					    json.put(field.getName(), field.get(u).toString());
					}else{
						json.put(field.getName(), "");
					}
				}
			}
			//base类的属性值
			json.put("id", u.getId());
			json.put("sysCode", u.getSysCode());
			OrgFrame orgFrame = u.getOrgFrame();
			if(orgFrame != null){
				json.put("orgFrameName", orgFrame.getOrgFrameName());
				json.put("orgFrameId", orgFrame.getId());
				json.put("orgFrameCode", orgFrame.getOrgFrameCode());

			}
			//传送角色id值
			Set<Role> roleSet= u.getRoles();
			StringBuffer roleIds = new StringBuffer();
			StringBuffer roleCode = new StringBuffer();
			String rolesIdstr = "";
			String rolesCodestr = "";
			if(roleSet.size()>0){
				for (Role role : roleSet) {
					roleIds.append(role.getId()+",");
					roleCode.append(role.getRoleCode()+",");
				}
				if(roleIds.length() > 0){
					rolesIdstr = roleIds.substring(0, roleIds.length()-1);
		        }
				if(roleCode.length() > 0){
					rolesCodestr = roleCode.substring(0, roleCode.length()-1);
		        }
			}
			json.put("roleIds",rolesIdstr);
			json.put("roleCodes",rolesCodestr);
			if(StringUtils.isNotBlank(url)){
				json.put("avatar", url);
			}
			JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
			config.registerJsonValueProcessor(Date.class,new JsonDateValueProcessor()); // 格式化日期
			String[] excludes = new String[] {"orgFrame","roles"
					,"avatarPathUpload","sysCode","loginIp","lastLoginTime","loginTimes"}; // 列表排除信息内容字段，减少传递时间
			config.setExcludes(excludes);
			json=JSONObject.fromObject(json, config);
		} catch (Exception e) {
			logger.error("错误："+e.getMessage()+"，时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
		return json;
	}
	/**
	 * @intruduction 移动端退出登录
	 * @return 登录页面
	 * @author Dic
	 * @Date 2015年12月28日上午8:56:17
	 */
	@RequestMapping(value="/mobileLoginOut") 
	public void mobileLoginOut() {
		User user = this.getSessionUser();
		logger.info("用户："+user.getUserName()+" 于"+new Date()+"退出");
		if(user!=null)
			this.getHttpSession().invalidate();//清除所有session
		JSONObject json = new JSONObject();
		json.put("result", true);
		json.put("msg", "退出登录！");
		this.print(json.toString());
	}
	/**
	 * 
	 * @intruduction 删除头像
	 * @author Will
	 * @Date 下午2:30:22
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value="/mobile_delete_avator")
	public void deleteAvator(HttpServletRequest request,HttpServletResponse response,String id){
		this.attachServiceImpl.deleteAttachByFormId(id);
		JSONObject json = new JSONObject();
		json.put("result", true);
		this.print(json);
	}
	
	/**
	 * 
	 * @intruduction 用户资料修改
	 * @author Will
	 * @Date 下午2:55:56
	 * @param request
	 * @param response
	 * @param id
	 * @param data
	 */
	@RequestMapping(value="/mobile_user_edit")
	public void editUserInfo(HttpServletRequest request,HttpServletResponse response,UserVo userVo){
		User user = this.userServiceImpl.getEntityById(User.class, userVo.getId());
		JSONObject json = new JSONObject();
		json.put("result", false);
		try{
			user.setUserName(userVo.getUserName());
			user.setJobNumber(userVo.getJobNumber());
			user.setMobilePhone(userVo.getMobilePhone());
			user.setSex(userVo.getSex());
			user.setAge(userVo.getAge());
			user.setNation(user.getNation());
			user.setCertificatesNum(user.getCertificatesNum());
			this.userServiceImpl.saveOrUpdate(user);
			
			json.put("sysCode", user.getSysCode());
			json.put("result", true);
		}catch (Exception e) {
			e.printStackTrace();
			json.put("result", false);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 
	 * @ClassName:MobileLoginController.java
	 * @Description:APP提交注册
	 * @param request
	 * @param response
	 * @param data
	 * @author qinyongqian
	 * @date 2016-7-27
	 */
	@RequestMapping(value="/mobileRegister") 
	public void mobileRegister(HttpServletRequest request,HttpServletResponse response,String data) {
		JSONObject json=new JSONObject();
		boolean isSuc=false;
		Gson gson = new Gson();
		try {
			UserVo userVo = gson.fromJson(data, UserVo.class);
			//判断用户是否已经注册
			String msg= userServiceImpl.isUserExist(userVo);
			if(msg.length()>0){
				isSuc=false;
				json.put("result", isSuc);
				json.put("msg",msg);
				this.print(json.toString());
				return;
			}
			if(userVo.getOrgFrameId()!=null){
				OrgFrame org=this.orgFrameServiceImpl.getEntityById(OrgFrame.class, userVo.getOrgFrameId());
				userVo.setOrgFrame(org);
			}else{
				json.put("result", isSuc);
				json.put("msg","没有部门信息");
				this.print(json.toString());
				return;
			}
			User user = new User();
			BeanUtils.copyProperties(userVo, user);
			userServiceImpl.save(user);
			
			/********发送事件通知 start*********/
			String noticeTitle=Common.msgTitle_SYS_info;
			String userIds="";
			String roleCodes="sysManager";
			int msgType=Common.msgSYS;
			User nowPerson=null;
			//发送给“部门安全员”角色
			this.sendMsg(noticeTitle,"收到新的用户注册记录",userIds,roleCodes,msgType,nowPerson);
			/********发送事件通知 end*********/
			
			isSuc=true;
			json.put("result", isSuc);
			json.put("msg","");
			this.print(json.toString());
		} catch (Exception e) {
			json.put("result", isSuc);
			json.put("msg",e.getMessage());
			this.print(json.toString());
		}
	}
	
	/**
	 * 
	 * @Description: 组织架构列表查询
	 * @param request
	 * @param response
	 * @date 2016-8-3 下午5:01:55
	 */
	@RequestMapping(value="/mobileOrgFrameList") 
	public void mobileOrgFrameList(HttpServletRequest request,HttpServletResponse response){
		boolean isSuc=false;
		JSONObject json = new JSONObject();
		json.put("result", true);
		try{
			List<Object> orgList =null;
			orgList = this.orgFrameDaoImpl.queryBySql("SELECT t.ID,t.ORG_FRAME_NAME,t.ORG_FRAME_CODE FROM `um_orgframe` t  where t.LEVEL_>1 order by t.LEVEL_ ASC;");
			JSONArray arrOrg  =JSONArray.fromObject(orgList);
			json.put("orgs", arrOrg.toString());
			json.put("msg", "");
		}catch(Exception e){
			e.printStackTrace();
			isSuc = false;
			json.put("result", isSuc);
			json.put("msg", "查询失败！");
		}
		this.print(json.toString());
	}
	/**
	 * 
	 * @intruduction 找回密码时验证用户信息：账号名，姓名，工号
	 * @author Will
	 * @Date 上午11:45:43
	 * @param request
	 * @param response
	 * @param loginName
	 * @param userName
	 * @param jobNumber
	 */
	@RequestMapping(value="/mobile_validateInfo")
	public void validateInfo(HttpServletRequest request,HttpServletResponse response,String loginName,String userName,String jobNumber){
		UserVo userVo = new UserVo();
		userVo.setLoginName(loginName);
		List<User> users = this.userServiceImpl.queryUserList(userVo);
		JSONObject json = new JSONObject();
		json.put("result", true);
		json.put("msg", "");
		if(users.size() > 0){
			User user = users.get(0);
			if(!user.getJobNumber().equals(jobNumber)){
				json.put("msg", "工号不匹配！");
				json.put("result", false);
			} 
			if(!user.getUserName().equals(userName)){
				json.put("msg", "姓名不匹配！");
				json.put("result", false);
			}	
		}else{
			json.put("result", false);
			json.put("msg", "该账户尚未注册！");
		}
		this.print(json);
	}
	@RequestMapping(value="/mobile_reset_password")
	public void resetPsw(HttpServletRequest request,HttpServletResponse response,String loginName,String md5Psw){
		UserVo userVo = new UserVo();
		userVo.setLoginName(loginName);
		List<User> users = this.userServiceImpl.queryUserList(userVo);
		JSONObject json = new JSONObject();
		json.put("result", true);
		json.put("msg", "");
		if(users.size() > 0){
			User user = users.get(0);
			user.setPassword(md5Psw);
			this.userServiceImpl.update(user);
		}else{
			json.put("result", false);
			json.put("msg", "重设失败，请重试！");
		}
		this.print(json);
	}
	
	/**
	 * 根据登录用户的信息，添加登录日志
	 * @Title:
	 * @param loginName 登录帐号
	 * @param userName 帐号
	 */
	public void saveLoginLog(HttpServletRequest request,String loginName,String userName,Integer userType,String userId){
		
		LoginLog loginLog = new LoginLog();
		if(userType != null && userType !=1){//如果不是超管
			loginLog.setSysCode(Common.SYSCODE);
		}
		loginLog.setLoginAccount(loginName);
		loginLog.setLoginName(userName);
		loginLog.setUserId(userId);

//		String visitServerName = request.getServerName(); //获得访问地址的双斜杠后面的参数
//		String userAgent = request.getHeader("user-agent");
//		String returnInfo = VisitUtil.getVisitIp(visitServerName,userAgent); //获得访问的外网ip地址
//		// ip + "@@@" + operatorType + "@@@" + address + "@@@" +agent + "@@@" + os + "@@@" + browser + "@@@" + pcOrMobile;
//		String str[] = returnInfo.split("@@@");
//		if(StringUtils.isNotEmpty(str[0])){loginLog.setLoginIp(str[0]);} // 外网IP
//		if(StringUtils.isNotEmpty(str[2])){loginLog.setLoginAddress(str[2]);} //获得访问IP地址的运营商的地址，比如：["中国","广东","广州","电信"]
		
		loginLog.setLoginTime(new Date());
		loginLog.setLoginSource(1);
		loginLog.setLoginState(1);
		
		loginLogServiceImpl.saveOrUpdate(loginLog);
		//保存成功，把loginLogId 写入session，用于修改
		this.getHttpSession().setAttribute("loginLogId", loginLog.getLoginLogId());
	}
	
	
	/*****************************以下是私有方法****************************************/
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
			msg.setSysCode(Common.SYSCODE);
			if(user!=null){
				msg.setSender(user.getUserName());
				msg.setCreatorId(user.getId());
				msg.setCreatorName(user.getUserName());
			}
			this.messageServiceImpl.saveOrUpdate(msg);
			MessageJpush.sendCommonMsg(noticeTitle, msg);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
