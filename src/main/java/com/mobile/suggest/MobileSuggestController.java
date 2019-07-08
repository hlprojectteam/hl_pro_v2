package com.mobile.suggest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.o.common.beans.BeanUtils;

import com.common.base.controller.BaseController;
import com.common.message.service.IMessageService;
import com.common.utils.Common;
import com.suggest.module.Suggest;
import com.suggest.service.ISuggestService;
import com.suggest.vo.SuggestVo;
import com.urms.user.module.User;

/**
 * 
 * @Description 意见
 * @author qinyongqian
 * @date 2019年5月7日
 *
 */
@Controller
@RequestMapping(value="/suggest")
public class MobileSuggestController extends BaseController{

	@Autowired
	public ISuggestService suggestServiceImpl;
	@Autowired
	public IMessageService messageServiceImpl;
	
	@RequestMapping(value = "/submit_suggest")
	public void submitSuggest(HttpServletRequest request,HttpServletResponse response, SuggestVo suggestVo) {
		JSONObject json = new JSONObject();
		try {
		    User user=this.getSessionUser();
		    suggestVo.setCreatorId(user.getId());
		    suggestVo.setCreatorName(user.getUserName());
		    suggestVo.setStatus(1);
		    
			Suggest suggest=new Suggest();
			BeanUtils.copyProperties(suggestVo, suggest);
			
			this.suggestServiceImpl.saveOrUpdate(suggest);
			String noticeTitle="";
			String userIds="";
			String roleCodes="";
			int msgType=0;
			if(suggestVo.getModuleClass()==1){
				//安全管理
				noticeTitle=Common.msgTitle_AQ_jyxc_todo;
				msgType=Common.msgAQ;
			}else if(suggestVo.getModuleClass()==2){
				//党建
				noticeTitle=Common.msgTitle_DJ_yj_todo;
				msgType=Common.msgDJ;
				roleCodes="dangjian_dwgzry,dangjian_dwwy";
			}
			//发送给“党委委员,党务工作者”角色
			this.messageServiceImpl.submitSendMsg(
					noticeTitle,"收到一条新的意见信息",userIds,roleCodes,msgType,this.getSessionUser());
			
			//this.sendMsg(noticeTitle,"收到一条新的意见信息","40284a8d586759f801588b19159100a7",null,msgType,this.getSessionUser());
			json.put("result", true);
			json.put("msg", "");
			json.put("suggestId", suggest.getId());
		} catch (Exception e) {
			e.printStackTrace();
			json.put("result", false);
			json.put("msg", e.toString());
		}
		this.print(json);
	}
	
}
