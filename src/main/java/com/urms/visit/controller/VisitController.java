package com.urms.visit.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.common.base.controller.BaseController;
import com.common.utils.VisitUtil;
import com.common.utils.helper.Pager;
import com.urms.user.module.User;
import com.urms.visit.module.Visit;
import com.urms.visit.service.IVisitService;
import com.urms.visit.vo.VisitVo;


/**
 * 访问量管理
 * Title: VisitController
 * Description: 	
 * @author Mr.Wang
 * @date 2016-12-28 上午11:24:40
 */
@Controller
@RequestMapping("/visit")
public class VisitController extends BaseController {

	@Autowired
	public IVisitService visitServiceImpl;
	
	/**
	 * 跳转访问量页面
	 * Description:
	 * @return	
	 * @author Mr.Wang
	 * @date 2016-12-28 下午2:49:27
	 */
	@RequestMapping(value="/visit_list")
	public String parkProfileList(){
		return "/page/urms/visit/visit_list";
	}
	
	/**
	 * @intruduction 访问记录列表
	 * @param request
	 * @param response
	 * @param VisitVo
	 * @param page
	 * @param rows
	 * @author Mr.Wang
	 */
	@RequestMapping(value="/visit_load") 
	public void load(HttpServletRequest request,HttpServletResponse response,VisitVo VisitVo,Integer page,Integer rows) {
		User user =(User)this.getHttpSession().getAttribute("user");
		Pager pager = this.visitServiceImpl.queryEntityList(page, rows, VisitVo,user);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.setExcludes(new String[]{"users","roles"});  //只要设置这个数组，指定过滤哪些字段。     
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * 添加访问记录
	 * Description:
	 * @param httpSession
	 * @param response	
	 * @author Mr.Wang
	 * @date 2016-12-28 下午2:00:52
	 */
	@RequestMapping(value="/visit_saveVisit",method = RequestMethod.POST)
	public void saveVisit(HttpSession httpSession,HttpServletRequest request,HttpServletResponse response){
		Visit visit = new Visit();
		String visitUrl = request.getScheme() + "://" + request.getServerName();  //+ request.getRequestURI()
		String visitServerName = request.getServerName(); //获得访问地址的双斜杠后面的参数
		String userAgent = request.getHeader("user-agent");
		String returnInfo = VisitUtil.getVisitIp(visitServerName,userAgent); //获得访问的外网ip地址
		// ip + "@@@" + operatorType + "@@@" + address + "@@@" +agent + "@@@" + os + "@@@" + browser + "@@@" + pcOrMobile;
		String str[] = returnInfo.split("@@@");
		if(StringUtils.isNotEmpty(str[0])){visit.setVisitIp(str[0]);} // 外网IP
		if(StringUtils.isNotEmpty(str[1])){visit.setVisitOperatorType(Integer.parseInt(str[1]));} // 运营商类型
		if(StringUtils.isNotEmpty(str[2])){visit.setVisitOperatorAdress(str[2]);} //获得访问IP地址的运营商的地址，比如：["中国","广东","广州","电信"]
		if(StringUtils.isNotEmpty(str[3])){visit.setVisitAgent(str[0]);} ////系统+版本+来访
		if(StringUtils.isNotEmpty(str[4])){visit.setVisitSystem(str[1]);}  //访问的系统
		if(StringUtils.isNotEmpty(str[5])){visit.setVisitBrowser(str[2]);} //使用的浏览器
		if(StringUtils.isNotEmpty(str[6])){visit.setVisitSourceType(Integer.parseInt(str[6]));} //访问来源端  //1电脑、2移动端
		visit.setVisitStartTime(new Date());
		
		User user =(User)this.getHttpSession().getAttribute("user");
		if (user != null) {
			visit.setVisitorType(2); //会员
			visit.setVisitUserId(user.getId()); //会员ID
		} else {
			visit.setVisitorType(1);
			visit.setVisitUserId("");
		}
		visit.setVisitUrl(visitUrl);
		visitServiceImpl.save(visit);
	}
	
	
	public IVisitService getVisitServiceImpl() {
		return visitServiceImpl;
	}
	public void setVisitServiceImpl(IVisitService visitServiceImpl) {
		this.visitServiceImpl = visitServiceImpl;
	}
}
