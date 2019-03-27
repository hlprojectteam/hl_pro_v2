package com.dangjian.controller;

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
import com.dangjian.module.Branch;
import com.dangjian.module.Introduction;
import com.dangjian.service.IBranchService;
import com.dangjian.service.IIntroductionService;
import com.dangjian.vo.IntroductionVo;
import com.google.gson.JsonObject;
import com.urms.user.module.User;
import com.urms.user.vo.UserVo;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2019年1月22日
 *
 */
@Controller
@RequestMapping(value="/dangjian")
public class IntroductionController extends BaseController{
	
	@Autowired
	public IIntroductionService introductionServiceImpl;
	@Autowired
	public IBranchService branchServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;

	@RequestMapping(value="/introduction_list")
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode,Integer type){
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("type", type);
		User user = this.getSessionUser();
		UserVo userVo=new UserVo();
		BeanUtils.copyProperties(user, userVo);
		Branch branch= branchServiceImpl.getUserBranch(userVo);
		if(branch!=null){
			this.getRequest().setAttribute("branchId", branch.getId());
		}
	    return "/page/dangjian/introduction/introduction_list";
	}

	@RequestMapping(value="/introduction_load")
	public void load(HttpServletRequest request,HttpServletResponse response,IntroductionVo introductionVo,
			Integer page,Integer rows){
		Pager pager = this.introductionServiceImpl.queryEntityList(page, rows, introductionVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	

	@RequestMapping(value="/introduction_edit")
	public String edit(HttpServletRequest request, IntroductionVo introductionVo){
		if(StringUtils.isNotBlank(introductionVo.getId())){
			try{
				Introduction introduction = this.introductionServiceImpl.getEntityById(Introduction.class, introductionVo.getId());
				BeanUtils.copyProperties(introduction, introductionVo);
				if(introduction.getContent()!=null){
					introductionVo.setContentStr(new String(introduction.getContent(), "UTF-8"));
				}
				request.setAttribute("introductionVo", introductionVo);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			User user = this.getSessionUser();
			introductionVo.setAuthorName(user.getUserName());
			request.setAttribute("introductionVo", introductionVo);
		}
		return "/page/dangjian/introduction/introduction_edit";
	}

	@RequestMapping(value="/introduction_del") 
	public void delete(HttpServletResponse response,String ids) {
		this.introductionServiceImpl.deleteIntroduction(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	
	@RequestMapping(value="/introduction_saveOrUpdate",method = RequestMethod.POST)
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,IntroductionVo introductionVo){
		JsonObject json =new JsonObject();
		try{
			Introduction introduction = new Introduction();
			introductionVo = formatVo(introductionVo);
			BeanUtils.copyProperties(introductionVo, introduction);
			 if(StringUtils.isNotEmpty(introductionVo.getContentStr()))
				 introduction.setContent(introductionVo.getContentStr().getBytes("UTF8"));
			this.introductionServiceImpl.saveOrUpdate(introduction);
			json.addProperty("id", introduction.getId());
			json.addProperty("result", true);
		}catch (Exception e) {
			e.printStackTrace();
			json.addProperty("result", false);
		}finally{
			this.print(json.toString());
		}
	}
	
	@RequestMapping(value="/update_introduction_img") 
	public void updateEntityImg(HttpSession httpSession,HttpServletResponse response,String id){
		List<Attach> attachs = this.attachServiceImpl.queryAttchListByFormId(id);
		String path = "";
		for(int i=0;i<attachs.size();i++){
			Attach attach = attachs.get(i);
			if(StringUtils.isNotBlank(attach.getSuffix()) ){
				if(!attach.getAttachType().equals("dj_introduction_file")){
					path = attach.getPathUpload();
				}
			}
		}
		Introduction introduction = this.introductionServiceImpl.queryEntityById(id);
		introduction.setCover(path);
		this.introductionServiceImpl.update(introduction);
	}

	@RequestMapping(value="/introduction_changeState",method = RequestMethod.POST) 
	public void changeState(HttpSession httpSession,HttpServletResponse response,String id) {
		JsonObject json = new JsonObject();
		try {
			String sign = this.introductionServiceImpl.saveChangeState(id);
			json.addProperty("result", true);
			json.addProperty("sign", sign);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	
	private IntroductionVo formatVo(IntroductionVo introductionVo){
		
		if(StringUtils.isNotBlank(introductionVo.getReleaseDateStr())){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			try {
				introductionVo.setReleaseDate(sdf.parse(introductionVo.getReleaseDateStr()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(introductionVo.getVisitNum() == null){
			introductionVo.setVisitNum(0);
		}
		if(introductionVo.getStatus() == null){
			introductionVo.setStatus(0);
		}
		return introductionVo;
	}

}
