package com.common.dataimport;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.common.base.controller.BaseController;
import com.common.utils.helper.MD5Utils;
import com.dangjian.service.IPartyMemberService;
import com.urms.orgFrame.module.OrgFrame;
import com.urms.orgFrame.service.IOrgFrameService;
import com.urms.orgFrame.vo.OrgFrameVo;
import com.urms.role.module.Role;
import com.urms.role.service.IRoleService;
import com.urms.role.vo.RoleVo;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

/**
 * 
 * @Description 用户数据导入
 * @author qinyongqian
 * @date 2019年1月31日
 *
 */
@Controller
@RequestMapping("/dataImport")
public class ImportDataForUserContrller extends BaseController{

    public static Logger logger_excel = Logger.getLogger("EXCEL");//记录导入日志
	
	@Autowired
	private IPartyMemberService partyMemberServiceImpl;
	
	@Autowired
	private IUserService userServiceImpl;
	
	@Autowired
	private IRoleService roleServiceImpl;
	@Autowired
	private IOrgFrameService orgServiceImpl;
	
	@RequestMapping(value="/userData")
	public String view(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/urms/importData/userdata";
	}
	
	@ResponseBody
	@RequestMapping(value="/submitUserRoleExcel",produces = "application/json;charset=utf-8",method=RequestMethod.POST) 
	public String importUserRoleExcel(MultipartFile file,HttpServletResponse response) throws Exception{
        InputStream in =null;  
        List<List<Object>> listob = null;  
        if(file.getSize()==0){
        	return "请选择文件!";
        }
        in = file.getInputStream();  
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename(),34);  
        in.close();  
          
        logger_excel.info("-------------角色数据导入开始 开始时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
        logger_excel.info("导入操作用户："+this.getSessionUser().getUserName());
        int successCount=0;//计算失败数和成功数
        for (int i = 0; i < listob.size(); i++) {  
            List<Object> lo = listob.get(i);  
            if(lo.isEmpty())	continue;	//去掉多余的行
            String roleName=(String)lo.get(0);
            String roleCode=(String)lo.get(1);
            if(StringUtils.isEmpty(roleName)||StringUtils.isEmpty(roleCode)){
            	continue;
            }
            try {
            	RoleVo roleVo=new RoleVo();
            	roleVo.setRoleName(roleName);
            	boolean flag=roleServiceImpl.isRoleExist(roleVo);
            	if(flag){
        			//存在角色
        			logger_excel.info("-------------存在角色，不导入："+roleName+"---------------");
        			continue;
            	}else{
            		//不存在则导入
            		Role role=new Role();
            		role.setRoleName(roleName);
            		role.setRoleCode(roleCode);
            		role.setCreateTime(new Date());
            		role.setCreatorId(this.getSessionUser().getId());
            		role.setCreatorName(this.getSessionUser().getUserName());
            		OrgFrame org=orgServiceImpl.getEntityById(OrgFrame.class, "297ef6c657747a9f01577482f4e90000");
            		if(org==null){
            			logger_excel.info("------------没找到部门---------------");
            			continue;
            		}
            		role.setOrgFrame(org);
            		role.setSysCode("hl");
            		roleServiceImpl.saveOrUpdate(role);
            		successCount++;
            	}
			} catch (Exception e) {
				logger_excel.error("第"+(i+1)+"条数据，角色："+roleName+"失败数据。 ",e);
			}
        }
        
        logger_excel.info("成功导入数："+successCount);
        logger_excel.info("-------------角色数据导入结束 结束时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
		return "党员数据导入完毕!";
	}
	
	@ResponseBody
	@RequestMapping(value="/submitUserExcel",produces = "application/json;charset=utf-8",method=RequestMethod.POST) 
	public String importExcel(MultipartFile file,HttpServletResponse response) throws Exception{
        InputStream in =null;  
        List<List<Object>> listob = null;  
        if(file.getSize()==0){
        	return "请选择文件!";
        }
        in = file.getInputStream();  
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename(),34);  
        in.close();  
          
        logger_excel.info("-------------用户数据导入开始 开始时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
        logger_excel.info("导入操作用户："+this.getSessionUser().getUserName());
        int successCount=0;//计算失败数和成功数
        for (int i = 0; i < listob.size(); i++) {  
            List<Object> lo = listob.get(i);  
            if(lo.isEmpty())	continue;	//去掉多余的行
            //String disabilityID = (String)lo.get(1);
            //if(StringUtils.isBlank(disabilityID))	continue;//去掉多余的空内容
            String jobNum=(String)lo.get(0);
            String userName=(String)lo.get(1);
            String depName=(String)lo.get(2);
            String roleName=(String)lo.get(3);
            String mobilePhone=(String)lo.get(4);
            if(StringUtils.isEmpty(jobNum)||StringUtils.isEmpty(userName)||StringUtils.isEmpty(roleName)){
            	continue;
            }
            try {
            	User user=null;
            	UserVo userVo=new UserVo();
            	userVo.setUserName(userName);
            	List<User> uList= userServiceImpl.queryUserList(userVo);
            	if(uList!=null){
            		if(uList.size()>0){
            			//找到此姓名的人记录，则更新记录
            			user=uList.get(0);
            			//设置部门
                    	OrgFrameVo orgFrameVo=new OrgFrameVo();
                    	orgFrameVo.setOrgFrameName(depName);
                    	List<OrgFrame> listOrg= orgServiceImpl.queryOrgFrameList(orgFrameVo);
                    	if(listOrg!=null){
                    		if(listOrg.size()>0){
                    			user.setOrgFrame(listOrg.get(0));
                    		}
                    	}
                    	//配对角色
                    	RoleVo roleVo=new RoleVo();
                    	roleVo.setRoleName(roleName);
                    	Role role= roleServiceImpl.getRole(roleVo);
                    	if(role!=null){
                			//先清空旧角色
                			user.getRoles().clear();
                			//再导入新角色
                			Set<Role> roles = new TreeSet<Role>();
                			roles.add(role);
                			user.setRoles(roles);
                    	}
                    	user.setLoginName(userName);
                    	user.setPassword(MD5Utils.MD5("111111"));
                    	user.setJobNumber(jobNum);
                    	user.setMobilePhone(mobilePhone);
                    	user.setSysCode("hl");
                    	user.setMemo("");
            			userServiceImpl.update(user);
            			logger_excel.info("------------第"+(i+1)+"条记录:"+userName+"，有此人，则（更新）---------------");
            		}else{
            			user=new User();
            			//设置部门
                    	OrgFrameVo orgFrameVo=new OrgFrameVo();
                    	orgFrameVo.setOrgFrameName(depName);
                    	List<OrgFrame> listOrg= orgServiceImpl.queryOrgFrameList(orgFrameVo);
                    	if(listOrg!=null){
                    		if(listOrg.size()>0){
                    			user.setOrgFrame(listOrg.get(0));
                    		}
                    	}
                    	//配对角色
                    	RoleVo roleVo=new RoleVo();
                    	roleVo.setRoleName(roleName);
                    	Role role= roleServiceImpl.getRole(roleVo);
                    	if(role!=null){
                			//先清空旧角色
                			user.getRoles().clear();
                			//再导入新角色
                			Set<Role> roles = new TreeSet<Role>();
                			roles.add(role);
                			user.setRoles(roles);
                    	}
                    	user.setLoginName(userName);
                    	user.setPassword(MD5Utils.MD5("111111"));
                    	user.setUserName(userName);
                    	user.setJobNumber(jobNum);
                    	user.setMobilePhone(mobilePhone);
                    	user.setType(3);
                    	user.setState(1);
                    	user.setDataSource(1);
                    	user.setSysCode("hl");
                    	user.setMemo("");
                    	userServiceImpl.save(user);
            			logger_excel.info("------------第"+(i+1)+"条记录:"+userName+"，没有此人，则（++插入++）---------------");
            		}
            		successCount++;
            	}
			} catch (Exception e) {
				// TODO: handle exception
				logger_excel.error("第"+(i+1)+"条数据，姓名："+userName+"失败数据。 ",e);
			}
        }
        
        logger_excel.info("成功导入数："+successCount);
        logger_excel.info("-------------用户数据导入结束 结束时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
		return "用户数据导入完毕!";
	}
	
	/**
	 * 
	 * @方法：@param file
	 * @方法：@param response
	 * @方法：@throws Exception
	 * @描述：批量添加角色
	 * @return
	 * @author: qinyongqian
	 * @date:2019年6月7日
	 */
	@Transactional
	@RequestMapping(value="/setUserRoleExcel",produces = "application/json;charset=utf-8",method=RequestMethod.POST) 
	public void setUserRoleExcel(MultipartFile file,HttpServletResponse response) throws Exception{
		InputStream in =null;  
        List<List<Object>> listob = null;  
        if(file.getSize()==0){
        	this.print("请选择文件!");
        	return ;
        }
        in = file.getInputStream();  
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename(),34);  
        in.close();  
          
        logger_excel.info("-------------数据导入开始 开始时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
        logger_excel.info("导入操作用户："+this.getSessionUser().getUserName());
        int successCount=0;//计算失败数和成功数
        StringBuffer strErrMsg=new StringBuffer();
        boolean isImportReady=true;
        List<User> importUserList=new ArrayList<>();

        for (int i = 0; i < listob.size(); i++) {
        	System.out.println("第"+(i+1)+"条数据");
        	
            List<Object> lo = listob.get(i);  
            if(lo.isEmpty())	continue;	//去掉多余的行
            //员工号
            String jobNumber=(String)lo.get(0);
        	if(StringUtils.isEmpty(jobNumber)){
        		break;//退出循环	
        	}
        	//员工名称
        	String userName=(String)lo.get(1);
        	if(StringUtils.isEmpty(userName)){
        		strErrMsg.append("第"+(i+1)+"条数据：【员工姓名】列存有格式问题，不能空\r\n");
        		isImportReady=false;
        	}
        	//角色名称
        	String roleName=(String)lo.get(2);
        	if(StringUtils.isEmpty(roleName)){
        		strErrMsg.append("第"+(i+1)+"条数据：【角色名称】列存有格式问题，不能空\r\n");
        		isImportReady=false;
        	}

        	if(isImportReady){
        		//以上excel内容格式检查通过，开始整理实体
        		User user=null;
            	UserVo userVo=new UserVo();
            	userVo.setUserName(userName);
            	userVo.setJobNumber(jobNumber);
            	List<User> uList= userServiceImpl.queryUserList(userVo);
            	if(uList!=null){
            		if(uList.size()>0){
            			//有此人
            			user=uList.get(0);
            			RoleVo roleVo=new RoleVo();
                    	roleVo.setRoleName(roleName);
                    	Role role= roleServiceImpl.getRole(roleVo);
                    	if(role!=null){
                			//再导入新角色
                			Set<Role> roles = user.getRoles();
                			roles.add(role);
                			user.setRoles(roles);
                			
                			importUserList.add(user);
                    	}else{
                    		strErrMsg.append("第"+(i+1)+"条数据：角色："+roleName+"，系统找不到此角色\r\n");
                    		isImportReady=false;
                    	}
            		}else{
            			strErrMsg.append("第"+(i+1)+"条数据：员工号："+jobNumber+"，姓名："+userName+"，系统找不到此人\r\n");
                		isImportReady=false;
            		}
            	}else{
            		//没有此人
            		strErrMsg.append("第"+(i+1)+"条数据：员工号："+jobNumber+"，姓名："+userName+"，系统找不到此人\r\n");
            		isImportReady=false;
            	}
                
        	}
        }
        if(isImportReady){
        	//开始插入库
        	for (User user : importUserList) {
        		userServiceImpl.update(user);
        		successCount++;
			}
        	logger_excel.info("导入数："+successCount);
        	logger_excel.info("-------------数据导入结束 结束时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
        	this.print("数据导入完毕,导入数："+successCount);
        }else{
        	strErrMsg.append("----导入失败----");
        	this.print(strErrMsg.toString());
        }
	}

}
