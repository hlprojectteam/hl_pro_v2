package com.mobile.institution;

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
import com.institution.module.Institution;
import com.institution.service.IInstitutionReportService;
import com.institution.service.IInstitutionService;
import com.institution.vo.InstitutionVo;
import com.urms.user.module.User;

@Controller
@RequestMapping(value="/institutionobile")
public class MobileInstitutionController extends BaseController{

	@Autowired
	public IInstitutionService institutionServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	public IInstitutionReportService institutionReportServiceImpl;
	
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
	@RequestMapping(value="/institution_list")
	public void mobileNewsList(HttpServletRequest request,HttpServletResponse response,
			InstitutionVo institutionVo,Integer page,Integer rows){
		institutionVo.setStatus(1);
		Pager pager = this.institutionServiceImpl.queryEntityList(page, rows, institutionVo);
		
		JSONObject json = new JSONObject();
		json.put("result", true);
		json.put("total", pager.getRowCount());
		json.put("content", makeList(pager));
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @Description: 根据id查实体
	 * @param request
	 * @param response
	 * @param id
	 * @date 2016-7-7 下午5:06:09
	 */
	@RequestMapping(value="/inistitution_query")
	public void mobileNewsQuery(HttpServletRequest request,HttpServletResponse response,String id){
		SimpleDateFormat format =new SimpleDateFormat("YYYY-MM-dd");
		JSONObject json = new JSONObject();
		json.put("result", false);
		json.put("news", "");
		if(StringUtils.isNotBlank(id) && id.trim()!= ""){
			try{
				Institution institution = new Institution();
				institution = this.institutionServiceImpl.getEntityById(Institution.class, id);
				if(institution != null){
					if(institution.getVisitNum() == null){
						institution.setVisitNum(1);
					}else{
						institution.setVisitNum(institution.getVisitNum() + 1);
						this.saveEReport(institution);
					}
					InstitutionVo institutionVo =new InstitutionVo();
					BeanUtils.copyProperties(institution, institutionVo);
					if(institutionVo.getContent()!=null){
						institutionVo.setContentStr(new String(institution.getContent(), "UTF-8"));
					}
					institutionVo.setReleaseDateStr(format.format(institutionVo.getReleaseDate()));
					institutionVo = queryAttach(id, institutionVo);
					
					JsonConfig config = new JsonConfig();	//自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据 
					config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());	//格式化日期(这里精确到秒)
					String[] excludes = new String[] {"content","createTime","createTimeStr","createUserId","creatorId"
							,"releaseDate","showPlace","status","sysCode","creatorName","delFlag","moduleType","isTop"}; // 列表排除信息内容字段，减少传递时间
					config.setExcludes(excludes);
					json.put("result", true);
					json.put("news", JSONObject.fromObject(institutionVo, config));
					json.put("err", "");
					
					this.institutionServiceImpl.saveOrUpdate(institution);
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
	private String makeList(Pager pager) {
		ArrayList<Institution> list = (ArrayList<Institution>) pager.getPageList();
		JSONArray array =new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int i = 0; i < list.size();i++){
			Institution institution = list.get(i);
			JSONObject object =new JSONObject();
			object.put("mainTitle", institution.getMainTitle());
			object.put("id", institution.getId());
			object.put("viceTitle", institution.getViceTitle());
			object.put("releaseDate", sdf.format(institution.getReleaseDate()));
			object.put("cover", institution.getCover());
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
	private InstitutionVo queryAttach(String id,InstitutionVo institutionVo){
		List<Attach> attachs = this.attachServiceImpl.queryAttchListByFormId(id);
		StringBuilder path = new StringBuilder();
		StringBuilder fileName = new StringBuilder();
		for(int i=0;i<attachs.size();i++){
			Attach attach = attachs.get(i);
			if(StringUtils.isNotBlank(attach.getAttachType()) ){
				if(attach.getAttachType().equals("institutionFile")){
					path.append(attach.getPathUpload()+",");
					fileName.append(attach.getFileName()+",");
				}
			}
		}
		if(StringUtils.isNotBlank(path.toString())){
			institutionVo.setFilePath(path.deleteCharAt(path.length()-1).toString());
			institutionVo.setFileName(fileName.deleteCharAt(fileName.length()-1).toString());
		}
		return institutionVo;
	}
	
	
	/**
	 * 
	 * @intruduction 保存报表数据
	 * @author xm
	 * @Date 下午3:25:21
	 */
	public void saveEReport(Institution institution){
		User user = (User) this.getHttpSession().getAttribute("user");
		if(institution!=null&&user!=null&&user.getOrgFrame()!=null)
		this.institutionReportServiceImpl.saveOrUpdate(institution,user);
	}
}
