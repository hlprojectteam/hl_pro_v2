package com.urms.sms.controller;

import com.common.base.controller.BaseController;
import com.common.utils.helper.Pager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author zengcong
 * @Description: 短信模板
 * @date 2018/7/23 15:13
 */
@Controller
@RequestMapping("/urms")
public class SmsTemplateController extends BaseController{

    @RequestMapping(value="/smsTemplate_list")
    public String list(HttpSession httpSession, HttpServletResponse response, String menuCode) {
        this.getRequest().setAttribute("menuCode", menuCode);
        return "/page/urms/sms/smsTemplate/smsTemplate_list";
    }

 //   @RequestMapping(value="/smsTemplate_load")
//    public void load(HttpServletRequest request, HttpServletResponse response, SmsTemplateVo smsTemplateVo, Integer page, Integer rows) {
//        Pager pager = this.SmsTemplateServiceImpl.queryEntityList(page, rows, smsTemplateVo);
//        JSONObject json = new JSONObject();
//        json.put("total", pager.getRowCount());
//        JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
//        json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
//        this.print(json);
//    }
}
