package com.common.dataimport;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.common.base.controller.BaseController;
import com.common.utils.helper.DateUtil;
import com.common.utils.tld.DictUtils;
import com.dangjian.module.Branch;
import com.dangjian.module.PartyMember;
import com.dangjian.service.IBranchService;
import com.dangjian.service.IPartyMemberService;
import com.dangjian.vo.BranchVo;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

/**
 * 
 * @Description 党建数据导入
 * @author qinyongqian
 * @date 2019年1月23日
 *
 */
@Controller
@RequestMapping("/dataImport")
public class ImportDataForDangJianContrller extends BaseController{
	
	public static Logger logger_excel = Logger.getLogger("EXCEL");//记录导入日志
	
	@Autowired
	private IPartyMemberService partyMemberServiceImpl;
	
	@Autowired
	private IUserService userServiceImpl;
	@Autowired
	private IBranchService branchServiceImpl;
	
	@RequestMapping(value="/dangJianData")
	public String view(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/urms/importData/dangjiandata";
	}
	
	@ResponseBody
	@RequestMapping(value="/submitPartMemberExcel",produces = "application/json;charset=utf-8",method=RequestMethod.POST) 
	public String importExcel(MultipartFile file,HttpServletResponse response) throws Exception{
        InputStream in =null;  
        List<List<Object>> listob = null;  
        if(file.getSize()==0){
        	return "请选择文件!";
        }
        in = file.getInputStream();  
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename(),34);  
        in.close();  
          
        logger_excel.info("-------------党员数据导入开始 开始时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
        logger_excel.info("导入操作用户："+this.getSessionUser().getUserName());
        int successCount=0;//计算失败数和成功数
        for (int i = 0; i < listob.size(); i++) {  
            List<Object> lo = listob.get(i);  
            if(lo.isEmpty())	continue;	//去掉多余的行
            String userName=(String)lo.get(0);
            String branchName=(String)lo.get(1);
            if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(branchName)){
            	continue;
            }
            try {
            	String sex=(String)lo.get(2);
            	String nation=(String)lo.get(3);
            	String originPlace=(String)lo.get(4);
            	String birthday=(String)lo.get(5);
            	String workTime=(String)lo.get(6);
            	String educationBackground=(String)lo.get(7);
            	String degree=(String)lo.get(8);
            	String personalIdentity=(String)lo.get(9);
            	String certificatesNum=(String)lo.get(10);
            	String mobilePhone=(String)lo.get(11);
            	
            	String joininTime=(String)lo.get(12);
            	String changeRegularTime=(String)lo.get(13);
            	String increaseTime=(String)lo.get(14);
            	String increaseType=(String)lo.get(15);
            	String orgRelationshipUnit=(String)lo.get(16);
            	String frontLineSituation=(String)lo.get(17);
            	String address=(String)lo.get(18);
            	
            	User user=null;
            	UserVo userVo=new UserVo();
            	userVo.setUserName(userName);
            	List<User> uList= userServiceImpl.queryUserList(userVo);
            	if(uList!=null){
            		if(uList.size()>0){
            			//找到此姓名的人记录，则更新记录
            			user=uList.get(0);
            			user.setSex(getKeyByValue("sex",sex));
            			user.setNation(getKeyByValue("population_Nation",nation));
            			user.setOriginPlace(originPlace);
            			user.setBirthday(getDate(birthday));
            			user.setWorkTime(getDate(workTime));
            			user.setEducationBackground(getKeyByValue("population_backgroud",educationBackground));
            			user.setDegree(getKeyByValue("population_degree",degree));
            			user.setPersonalIdentity(getKeyByValue("population_personalIdentity",personalIdentity));
            			user.setCertificatesNum(certificatesNum);
            			user.setMobilePhone(mobilePhone);
            			user.setFrontLineSituation(getKeyByValue("population_frontLineSituation",frontLineSituation));
            			user.setAddress(address);
            			userServiceImpl.update(user);
            			logger_excel.info("------------第"+(i+1)+"条记录:"+userName+"，有此人，则（更新基础信息）---------------");
            			//插入党员记录
            			BranchVo branchVo=new BranchVo();
            			branchVo.setBranchName(branchName);
            			List<Branch> bList= branchServiceImpl.queryBranch(branchVo);
                    	if(bList!=null){
                    		if(bList.size()>0){
                    			//找到支部，插入
                    			Branch branch=bList.get(0);
                    			PartyMember partyMember=new PartyMember();
                    			partyMember.setBranchId(branch.getId());
                    			partyMember.setUserId(user.getId());
                    			partyMember.setJoininTime(getDate(joininTime));
                    			partyMember.setIncreaseTime(getDate(increaseTime));
                    			partyMember.setChangeRegularTime(getDate(changeRegularTime));
                    			partyMember.setIncreaseType(getKeyByValue("memberIncreaseType",increaseType));
                    			partyMember.setPoints(0);
                    			partyMember.setOrgRelationshipUnit(orgRelationshipUnit);
                    			partyMemberServiceImpl.save(partyMember);
                    			logger_excel.info("------------第"+(i+1)+"条记录:"+userName+"，（插入党员）---------------");
                    		}else{
                    			//没找到支部，插入不了
                    			logger_excel.info("------------第"+(i+1)+"条记录:"+userName+"，（找不到支部）---------------");
                    		}
                    	}
                    	successCount++;
            		}else{
                		//没有此人记录
                	}
            	}
			} catch (Exception e) {
				// TODO: handle exception
			}
        }
        
        logger_excel.info("成功导入数："+successCount);
        logger_excel.info("-------------党员数据导入结束 结束时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
		return "党员数据导入完毕!";
	}
	
	private Date getDate(String strDate){
		if(org.apache.commons.lang3.StringUtils.isNotBlank(strDate)){
			try {
				return DateUtil.format(strDate, "yyyy-mm-dd");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	private Integer getKeyByValue(String dictCode,String value){
		int key=-1;
		if(org.apache.commons.lang3.StringUtils.isNotBlank(value)){
			String key_= DictUtils.getDictAttrKey(dictCode, value);
			if(org.apache.commons.lang3.StringUtils.isNotBlank(key_))
				key=Integer.parseInt(key_);
		}
		return key;
	}
	
//	private boolean uniqueMember(String mName,String branch) {
//		if(partyMemberServiceImpl.queryCountByBuidlingNameUnitRoomNum(buildingName, unit, roomNum)>0){
//			return false;
//		}else{
//			return true;
//		}
//	}

}
