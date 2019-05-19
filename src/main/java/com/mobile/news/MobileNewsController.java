package com.mobile.news;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.news.module.News;
import com.news.service.INewsService;
import com.news.vo.NewsVo;

@Controller
@RequestMapping(value="/newsMobile")
public class MobileNewsController extends BaseController{
	@Autowired
	public INewsService newsServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	public INewsService getNewsServiceImpl() {
		return newsServiceImpl;
	}
	public void setNewsServiceImpl(INewsService newsServiceImpl) {
		this.newsServiceImpl = newsServiceImpl;
	}
	public IAttachService getAttachServiceImpl() {
		return attachServiceImpl;
	}
	public void setAttachServiceImpl(IAttachService attachServiceImpl) {
		this.attachServiceImpl = attachServiceImpl;
	}
	
	/**
	 * 
	 * @intruduction 列表查询
	 * @author Will
	 * @Date 上午10:00:39
	 * @param request
	 * @param response
	 * @param type
	 * @param page
	 * @param rows
	 */
	@RequestMapping(value="/news_list")
	public void mobileNewsList(HttpServletRequest request,HttpServletResponse response,NewsVo newsVo,Integer page,Integer rows){
		newsVo.setStatus(1);
		Pager pager = this.newsServiceImpl.queryEntityList(page, rows, newsVo);
		
		JSONObject json = new JSONObject();
		json.put("result", true);
		json.put("total", pager.getRowCount());
		json.put("content", makeNewsList(pager));
		this.print(json);
	}
	/**
	 * 
	 * @Description: 根据id查实体
	 * @param request
	 * @param response
	 * @param id
	 * @date 2016-7-7 下午5:06:09
	 */
	@RequestMapping(value="/news_query")
	public void mobileNewsQuery(HttpServletRequest request,HttpServletResponse response,String id){
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		JSONObject json = new JSONObject();
		json.put("result", true);
		json.put("news", "");
		if(StringUtils.isNotBlank(id) && id.trim()!= ""){
			try{
				News news = new News();
				news.setId(id);
				news = this.newsServiceImpl.queryEntityById(id);
				if(news != null){
					if(news.getVisitNum() == null){
						news.setVisitNum(1);
					}else{
						news.setVisitNum(news.getVisitNum() + 1);
					}
					NewsVo newsVo =new NewsVo();
					BeanUtils.copyProperties(news, newsVo);
					if(news.getContent()!=null){
						newsVo.setContentStr(new String(news.getContent(), "UTF-8"));
					}
					newsVo.setReleaseDateStr(format.format(newsVo.getReleaseDate()));
					newsVo = queryAttach(id, newsVo);
					
					JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
					config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
					String[] excludes = new String[] {"content","createTime","createTimeStr","createUserId","creatorId"
							,"releaseDate","showPlace","status","sysCode","creatorName","delFlag","moduleType","isTop"}; // 列表排除信息内容字段，减少传递时间
					config.setExcludes(excludes);
					
					json.put("result", true);
					json.put("news", JSONObject.fromObject(newsVo, config));
					json.put("err", "");
					
					this.newsServiceImpl.saveOrUpdate(news);
				}else{
					json.put("err", "没有查到该条新闻！");
				}
			}catch (Exception e) {
				e.printStackTrace();
				json.put("err", "解析错误!");
			}
		}else{
			json.put("err", "id为空");
		}
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @intruduction 格式化列表数据
	 * @author Will
	 * @Date 上午10:12:19
	 * @param pager
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String makeNewsList(Pager pager) {
		ArrayList<News> list = (ArrayList<News>) pager.getPageList();
		JSONArray array =new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int i = 0; i < list.size();i++){
			News news = list.get(i);
			JSONObject object =new JSONObject();
			object.put("mainTitle", news.getMainTitle());
			object.put("id", news.getId());
			object.put("viceTitle", news.getViceTitle());
			object.put("releaseDate", sdf.format(news.getReleaseDate()));
			object.put("cover", news.getCover());
			object.put("authorName", news.getAuthorName());
			object.put("lable", news.getLable());
			object.put("showPlace", news.getShowPlace());
			array.add(object);
		}
		return array.toString();
	}
	
	/**
	 * 
	 * @Description: 附件查询
	 * @param id
	 * @return
	 * @date 2016-8-24 下午5:21:10
	 */
	private NewsVo queryAttach(String id,NewsVo newsVo){
		List<Attach> attachs = this.attachServiceImpl.queryAttchListByFormId(id);
		StringBuilder path = new StringBuilder();
		StringBuilder fileName = new StringBuilder();
		for(int i=0;i<attachs.size();i++){
			Attach attach = attachs.get(i);
			if(StringUtils.isNotBlank(attach.getAttachType()) ){
				if(attach.getAttachType().equals("newsFile")){
					path.append(attach.getPathUpload()+",");
					fileName.append(attach.getFileName()+",");
				}
			}
		}
		if(StringUtils.isNotBlank(path.toString())){
			newsVo.setFilePath(path.deleteCharAt(path.length()-1).toString());
			newsVo.setFileName(fileName.deleteCharAt(fileName.length()-1).toString());
		}
		return newsVo;
	}
}
