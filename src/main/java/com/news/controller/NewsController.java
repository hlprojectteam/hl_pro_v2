package com.news.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;//确实 servlet和jsp-api会报错

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
import com.google.gson.JsonObject;
import com.news.module.News;
import com.news.service.INewsService;
import com.news.vo.NewsVo;
import com.urms.user.module.User;

@Controller
@RequestMapping(value="/news")
public class NewsController extends BaseController{
	
	@Autowired
	public INewsService newsServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	/**
	 * 
	 * @ClassName:NewsController.java
	 * @Description:列表页面请求
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @return  
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/news_list")
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode,String moduleType){
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("moduleType", moduleType);
	    return "/page/news/news_list";
	}
	/**
	 * 
	 * @ClassName:NewsController.java
	 * @Description:列表页面加载
	 * @param httpSession
	 * @param response
	 * @param newsvo
	 * @return  分页数据
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/news_load")
	public void load(HttpServletRequest request,HttpServletResponse response,NewsVo newsVo,
			Integer page,Integer rows){
		Pager pager = this.newsServiceImpl.queryEntityList(page, rows, newsVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	/**
	 * 
	 * @ClassName:NewsController.java
	 * @Description:页面编辑
	 * @param httpSession
	 * @param response
	 * @param newsvo
	 * @return  编辑对象
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/news_edit")
	public String edit(HttpServletRequest request, NewsVo newsVo){
		if(StringUtils.isNotBlank(newsVo.getId())){
			try{
				News news = this.newsServiceImpl.getEntityById(News.class, newsVo.getId());
				BeanUtils.copyProperties(news, newsVo);
				if(news.getContent()!=null){
					newsVo.setContentStr(new String(news.getContent(), "UTF-8"));
				}
				request.setAttribute("newsVo", newsVo);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			newsVo.setAuthorName(this.getSessionUser().getUserName());
			request.setAttribute("newsVo", newsVo);
		}
		return "/page/news/news_edit";
			
	}
	
	/**
	 * 
	 * @ClassName:NewsController.java
	 * @Description:删除
	 * @param httpSession
	 * @param response
	 * @param ids
	 * @return  删除对象
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/news_del") 
	public void delete(HttpServletResponse response,String ids) {
		this.newsServiceImpl.deleteNews(ids);
		JsonObject json = new JsonObject();
		json.addProperty("result", "success");
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @ClassName:NewsController.java
	 * @Description:保存
	 * @param httpSession
	 * @param response
	 * @param newsvo
	 * @return  保存对象
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/news_saveOrUpdate",method = RequestMethod.POST)
	public void saveOrUpdate(HttpSession httpSession,HttpServletResponse response,NewsVo newsVo){
		JsonObject json =new JsonObject();
		try{
			News news = new News();
			newsVo = formatVo(newsVo);
			BeanUtils.copyProperties(newsVo, news);
			 if(StringUtils.isNotEmpty(newsVo.getContentStr()))
				 news.setContent(newsVo.getContentStr().getBytes("UTF8"));
			this.newsServiceImpl.saveOrUpdate(news);
			json.addProperty("id", news.getId());
			json.addProperty("result", true);
		}catch (Exception e) {
			e.printStackTrace();
			json.addProperty("result", false);
		}finally{
			this.print(json.toString());
		}
	}
	/**
	 * 
	 * @ClassName:NewsController.java
	 * @Description:图片编辑
	 * @param httpSession
	 * @param response
	 * @param id
	 * @return  编辑对象
	 * @author xm
	 * @date 2016-10-12
	 */
	
	@RequestMapping(value="/update_img") 
	public void updateEntityImg(HttpSession httpSession,HttpServletResponse response,String id){
		List<Attach> attachs = this.attachServiceImpl.queryAttchListByFormId(id);
		String path = "";
		for(int i=0;i<attachs.size();i++){
			Attach attach = attachs.get(i);
			if(StringUtils.isNotBlank(attach.getSuffix()) ){
				if(!attach.getAttachType().equals("newsFile")){
					path = attach.getPathUpload();
				}
			}
		}
		News news = this.newsServiceImpl.queryEntityById(id);
		news.setCover(path);
		this.newsServiceImpl.update(news);
	}
	/**
	 * 
	 * @ClassName:NewsController.java
	 * @Description:状态改变
	 * @param httpSession
	 * @param response
	 * @param id
	 * @return  状态标记
	 * @author xm
	 * @date 2016-10-12
	 */
	@RequestMapping(value="/news_changeState",method = RequestMethod.POST) 
	public void changeState(HttpSession httpSession,HttpServletResponse response,String id) {
		JsonObject json = new JsonObject();
		try {
			String sign = this.newsServiceImpl.saveChangeState(id);
			json.addProperty("result", true);
			json.addProperty("sign", sign);
		} catch (Exception e) {
			json.addProperty("result", false);
			e.printStackTrace();
		}finally{
			this.print(json.toString());
		}
	}
	
	
	private NewsVo formatVo(NewsVo newsVo){
		User user = this.getSessionUser();
		if(StringUtils.isBlank(newsVo.getId())){
			newsVo.setCreateUserId(user.getId());
		}
		if(StringUtils.isNotBlank(newsVo.getReleaseDateStr())){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			try {
				newsVo.setReleaseDate(sdf.parse(newsVo.getReleaseDateStr()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(newsVo.getVisitNum() == null){
			newsVo.setVisitNum(0);
		}
		if(newsVo.getDelFlag() == null){
			newsVo.setDelFlag(0);
		}
		if(newsVo.getStatus() == null){
			newsVo.setStatus(0);
		}
		return newsVo;
	}
}
