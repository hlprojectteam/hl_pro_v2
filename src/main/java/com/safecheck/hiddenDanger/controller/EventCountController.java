package com.safecheck.hiddenDanger.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.base.controller.BaseController;
import com.safecheck.hiddenDanger.service.IEventCountService;
import com.safecheck.hiddenDanger.vo.EventInfoVo;
import com.urms.user.module.User;

/**
 * @Description 隐患事件统计(后台首页)
 * @author xuezb
 * @Date 2018年5月23日
 */
@Controller
@RequestMapping("/safecheck/hiddenDanger")
public class EventCountController extends BaseController{
	
	@Autowired
	private IEventCountService eventCountServiceImpl;
	
	
	/**
	 * 首页隐患事件统计数据
	 * @param request
	 * @param response
	 * @author xuezb
	 * @date 2019年1月21日
	 */
	@RequestMapping("/queryEventCount")
	@ResponseBody
	public String count(HttpServletRequest request,HttpServletResponse response){
		
		User user = (User)request.getSession().getAttribute("user");	// 获取当前用户信息(包括用户id)
        
        EventInfoVo eventInfoVo = new EventInfoVo();
        eventInfoVo.setEpNowPersonId(user.getId());
        
        //我的上报
        int reportCount = this.eventCountServiceImpl.queryReportCount(eventInfoVo);
        //我的待办
        int agendaCount = this.eventCountServiceImpl.queryAgendaCount(eventInfoVo);
        //我的经办
        int handleCount = this.eventCountServiceImpl.queryHandleCount(eventInfoVo);
        //我的办结
        int finishCount = this.eventCountServiceImpl.queryFinishCount(eventInfoVo);
        
        //待办的重大隐患
        eventInfoVo.setEventurgency(3);
        int majorAgendaCount = this.eventCountServiceImpl.queryAgendaCount(eventInfoVo);
		 
		HashMap<Object,Object> map = new HashMap<>();
		map.put("reportCount", reportCount);
		map.put("agendaCount", agendaCount);
		map.put("handleCount", handleCount);
		map.put("finishCount", finishCount);
		map.put("majorAgendaCount", majorAgendaCount);
		
		return JSONObject.fromObject(map).toString();
	}

}
