package com.suggest.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.o.common.beans.BeanUtils;

import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.message.service.IMessageService;
import com.common.utils.Common;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.dangjian.module.Branch;
import com.dangjian.service.IBranchService;
import com.suggest.module.Suggest;
import com.suggest.service.ISuggestService;
import com.suggest.vo.SuggestVo;
import com.urms.user.module.User;
import com.urms.user.service.IUserService;
import com.urms.user.vo.UserVo;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年4月21日
 *
 */
@Controller
@RequestMapping(value="/suggest")
public class SuggestController extends BaseController{
	
	@Autowired
	public ISuggestService suggestServiceImpl;
	@Autowired
	public IBranchService branchServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	public IUserService userServiceImpl;
	@Autowired
	public IMessageService messageServiceImpl;

	@RequestMapping(value="/suggest_list")
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode,Integer moduleClass){
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("moduleClass", moduleClass);
		User user = this.getSessionUser();
		UserVo userVo=new UserVo();
		BeanUtils.copyProperties(user, userVo);
		Branch branch= branchServiceImpl.getUserBranch(userVo);
		if(branch!=null){
			this.getRequest().setAttribute("branchId", branch.getId());
		}
	    return "/page/suggest/suggest_list";
	}

	@RequestMapping(value="/suggest_load")
	public void load(HttpServletRequest request,HttpServletResponse response,SuggestVo suggestVo,
			Integer page,Integer rows){
		JSONObject json = new JSONObject();
		json.put("result",false);
		try {
			Pager pager = this.suggestServiceImpl.queryEntityList(page, rows, suggestVo);
			if(page!=null){
				@SuppressWarnings("unchecked")
				List<Suggest> list=pager.getPageList();
				List<SuggestVo> listVo=new ArrayList<>();
				for (Suggest suggest : list) {
					User user=this.userServiceImpl.getEntityById(User.class,suggest.getCreatorId());
					SuggestVo vo=new SuggestVo();
					BeanUtils.copyProperties(suggest, vo);
					vo.setSex(user.getSex());
					
					List<Attach> listAttach= this.attachServiceImpl.queryAttchByFormIdAndOnlyPicture(user.getId());
					for (Attach attach : listAttach) {
						vo.setPhoto(attach.getPathUpload());
					}
					listVo.add(vo);
				}
				json.put("total", pager.getRowCount());
				json.put("curPageSize", pager.getPageList().size());
				JsonConfig config = new JsonConfig();
				String[] excludes = new String[] {"creatorId","moduleClass","rebackUserId","sysCode"}; // 列表排除信息内容字段，减少传递时间
				config.setExcludes(excludes);
				config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
				json.put("rows", JSONArray.fromObject(listVo,config));
				json.put("result",true);
			}
		} catch (Exception e) {
			json.put("result",false);
			json.put("msg",e.getMessage());
		}
		this.print(json);
	}
	
	@RequestMapping(value="/suggest_detail")
	public void suggestDetail(HttpServletRequest request,HttpServletResponse response,SuggestVo suggestVo){
		JSONObject json = new JSONObject();
		json.put("result", false);
		try {
			Suggest suggest=this.suggestServiceImpl.getEntityById(Suggest.class, suggestVo.getId());
			if(suggest!=null){
				updateStatus(suggest,suggestVo.getRebackUserId());
				BeanUtils.copyProperties(suggest, suggestVo);
				if(StringUtils.isNotBlank(suggest.getRebackUserId())){
					User user=this.userServiceImpl.getEntityById(User.class, suggest.getRebackUserId());
					suggestVo.setRebackUserName(user.getUserName());
				}
				JsonConfig config = new JsonConfig(); // 自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据
				config.registerJsonValueProcessor(Date.class,new JsonDateTimeValueProcessor()); // 格式化日期
				json.put("result", true);
				json.put("content", JSONObject.fromObject(suggestVo, config));
			}
		} catch (Exception e) {
			json.put("result",false);
			json.put("msg",e.getMessage());
		}
		this.print(json);
	}
	
	/**
	 * 
	 * @方法：@param suggest
	 * @方法：@param userId
	 * @描述：更新状态
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月8日
	 */
	private void updateStatus(Suggest suggest,String userId){
		if(suggest.getStatus()==1){
			//未阅状态
			if(!suggest.getCreatorId().equals(userId)){
				//查看人不是自己，则状态变成已阅
				suggest.setStatus(2);
				suggest.setReadDate(new Date());
				this.suggestServiceImpl.saveOrUpdate(suggest);
			}
		}
	}
	
	@RequestMapping(value="/suggest_answer")
	public void suggestAnswer(HttpServletRequest request,HttpServletResponse response,SuggestVo suggestVo){
		JSONObject json = new JSONObject();
		json.put("result", false);
		try {
			Suggest suggest=this.suggestServiceImpl.getEntityById(Suggest.class, suggestVo.getId());
			if(suggest!=null){
				if(suggest.getStatus()==3){
					json.put("msg","该建议已被回复");
				}else{
					suggest.setReback(suggestVo.getReback());
					suggest.setRebackDate(new Date());
					suggest.setRebackUserId(this.getSessionUser().getId());
					suggest.setStatus(3);
					this.suggestServiceImpl.saveOrUpdate(suggest);
					String noticeTitle="";
					String userIds=suggest.getCreatorId();
					String roleCodes="";
					int msgType=0;
					if(suggestVo.getModuleClass()==1){
						//安全管理
						noticeTitle=Common.msgTitle_AQ_jyxc_finish;
						msgType=Common.msgAQ;
					}else if(suggestVo.getModuleClass()==2){
						//党建
						noticeTitle=Common.msgTitle_DJ_yj_finish;
						msgType=Common.msgDJ;
					}
					//发送给意见提出者
					this.messageServiceImpl.submitSendMsg(
							noticeTitle,"收到一条意见回复",userIds,roleCodes,msgType,this.getSessionUser());
					json.put("result", true);
				}
			}
		} catch (Exception e) {
			json.put("result",false);
			json.put("msg",e.getMessage());
		}finally{
			this.print(json.toString());
		}
	}
	
	
	
	
//	@RequestMapping(value="/introduction_edit")
//	public String edit(HttpServletRequest request, SuggestVo suggestVo){
//		if(StringUtils.isNotBlank(suggestVo.getId())){
//			try{
//				Introduction introduction = this.suggestServiceImpl.getEntityById(Introduction.class, suggestVo.getId());
//				BeanUtils.copyProperties(introduction, suggestVo);
//				if(introduction.getContent()!=null){
//					suggestVo.setContentStr(new String(introduction.getContent(), "UTF-8"));
//				}
//				request.setAttribute("suggestVo", suggestVo);
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//		}else{
//			User user = this.getSessionUser();
//			suggestVo.setAuthorName(user.getUserName());
//			request.setAttribute("suggestVo", suggestVo);
//		}
//		return "/page/dangjian/introduction/introduction_edit";
//	}
//
//	@RequestMapping(value="/introduction_del") 
//	public void delete(HttpServletResponse response,String ids) {
//		this.suggestServiceImpl.deleteIntroduction(ids);
//		JsonObject json = new JsonObject();
//		json.addProperty("result", "success");
//		this.print(json.toString());
//	}
//	
//	@RequestMapping(value="/introduction_saveOrUpdate",method = RequestMethod.POST)
//	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,SuggestVo suggestVo){
//		JsonObject json =new JsonObject();
//		try{
//			Introduction introduction = new Introduction();
//			suggestVo = formatVo(suggestVo);
//			BeanUtils.copyProperties(suggestVo, introduction);
//			 if(StringUtils.isNotEmpty(suggestVo.getContentStr()))
//				 introduction.setContent(suggestVo.getContentStr().getBytes("UTF8"));
//			this.suggestServiceImpl.saveOrUpdate(introduction);
//			json.addProperty("id", introduction.getId());
//			json.addProperty("result", true);
//		}catch (Exception e) {
//			e.printStackTrace();
//			json.addProperty("result", false);
//		}finally{
//			this.print(json.toString());
//		}
//	}
//	
//	@RequestMapping(value="/update_introduction_img") 
//	public void updateEntityImg(HttpSession httpSession,HttpServletResponse response,String id){
//		List<Attach> attachs = this.attachServiceImpl.queryAttchListByFormId(id);
//		String path = "";
//		for(int i=0;i<attachs.size();i++){
//			Attach attach = attachs.get(i);
//			if(StringUtils.isNotBlank(attach.getSuffix()) ){
//				if(!attach.getAttachType().equals("dj_introduction_file")){
//					path = attach.getPathUpload();
//				}
//			}
//		}
//		Introduction introduction = this.suggestServiceImpl.queryEntityById(id);
//		introduction.setCover(path);
//		this.suggestServiceImpl.update(introduction);
//	}
//
//	@RequestMapping(value="/introduction_changeState",method = RequestMethod.POST) 
//	public void changeState(HttpSession httpSession,HttpServletResponse response,String id) {
//		JsonObject json = new JsonObject();
//		try {
//			String sign = this.suggestServiceImpl.saveChangeState(id);
//			json.addProperty("result", true);
//			json.addProperty("sign", sign);
//		} catch (Exception e) {
//			json.addProperty("result", false);
//			e.printStackTrace();
//		}finally{
//			this.print(json.toString());
//		}
//	}
//	
//	
//	private SuggestVo formatVo(SuggestVo suggestVo){
//		
//		if(StringUtils.isNotBlank(suggestVo.getReleaseDateStr())){
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//			try {
//				suggestVo.setReleaseDate(sdf.parse(suggestVo.getReleaseDateStr()));
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		if(suggestVo.getVisitNum() == null){
//			suggestVo.setVisitNum(0);
//		}
//		if(suggestVo.getStatus() == null){
//			suggestVo.setStatus(0);
//		}
//		return suggestVo;
//	}

}
