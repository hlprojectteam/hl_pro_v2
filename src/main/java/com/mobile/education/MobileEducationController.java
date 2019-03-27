package com.mobile.education;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.attach.module.Attach;
import com.common.attach.service.IAttachService;
import com.common.base.controller.BaseController;
import com.common.utils.helper.Pager;
import com.education.module.Education;
import com.education.service.IEducationReportService;
import com.education.service.IEducationService;
import com.education.vo.EducationVo;
import com.urms.dataDictionary.module.Category;
import com.urms.dataDictionary.module.CategoryAttribute;
import com.urms.dataDictionary.service.IDataDictionaryService;
import com.urms.user.module.User;

/**
 * 
 * @intruduction 教育培训手机端接口
 * @author Will
 * @Date 上午11:33:41
 */
@Controller
@RequestMapping(value="/educationMobile")
public class MobileEducationController extends BaseController{

	@Autowired
	public IEducationService educationServiceImpl;
	@Autowired
	public IAttachService attachServiceImpl;
	@Autowired
	public IEducationReportService educationReportServiceImpl;
	public IEducationService getEducationServiceImpl() {
		return educationServiceImpl;
	}
	public void setEducationServiceImpl(IEducationService educationServiceImpl) {
		this.educationServiceImpl = educationServiceImpl;
	}
	public IAttachService getAttachServiceImpl() {
		return attachServiceImpl;
	}
	public void setAttachServiceImpl(IAttachService attachServiceImpl) {
		this.attachServiceImpl = attachServiceImpl;
	}
	
	@Autowired
	public IDataDictionaryService dataDictionaryServiceImpl;
	
	/**
	 * 
	 * @ClassName:MobileEducationController.java
	 * @Description:获取教育材料分类
	 * @param request
	 * @param response
	 * @param type
	 * @param page
	 * @param rows
	 * @author qinyongqian
	 * @date 2016-12-27
	 */
	@RequestMapping(value="education_category")
	public void mobileEducationCategorys(HttpServletRequest request,HttpServletResponse response){
		JSONObject json = new JSONObject();
		Category category=dataDictionaryServiceImpl.getCategoryByCode("Education_Category");
		if(category!=null){
			Pager pager =dataDictionaryServiceImpl.queryEntityCategoryAttrList(1, 20, category.getId());
			if(pager!=null){
				json.put("result", "true");
				json.put("total", pager.getRowCount());
				json.put("content", makeEducationCategoryAtrList(pager));
			}
		}else{
			json.put("result", "false");
		}
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @intruduction 手机端列表查询
	 * @author Will
	 * @Date 上午11:36:55
	 * @param request
	 * @param response
	 * @param type
	 * @param page
	 * @param rows
	 */
	@RequestMapping(value="education_list")
	public void mobileEducationList(HttpServletRequest request,HttpServletResponse response,
			Integer type,Integer category,Integer page,Integer rows){
		EducationVo educationVo = new EducationVo();
		JSONObject json = new JSONObject();
		if(type != null){
			educationVo.setType(type);
		}
		if(type != null){
			educationVo.setCategory(category);
		}
		educationVo.setStatus(1);//只选择已发布的
		Pager pager = this.educationServiceImpl.queryEntityList(page, rows, educationVo);
		json.put("result", "true");
		json.put("total", pager.getRowCount());
		json.put("content", makeEducationList(pager));
		this.print(json.toString());
	}
	
	/**
	 * 
	 * @intruduction 根据id查教育
	 * @author Will
	 * @Date 下午3:03:16
	 * @param request
	 * @param response
	 * @param id
	 */
	@RequestMapping(value="education_query")
	public void queryEductationById(HttpServletRequest request,HttpServletResponse response,String id){
		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
		JSONObject json = new JSONObject();
		json.put("result", "false");
		json.put("news", "");
		if(StringUtils.isNotBlank(id) && id.trim()!= ""){
			try{
				Education education = this.educationServiceImpl.getEntityById(Education.class, id);
				if(education != null){
					EducationVo educationVo = new EducationVo();
					BeanUtils.copyProperties(education, educationVo);
					Attach attach = queryAttach(id);
					educationVo.setFilePath(attach.getPathUpload());
					educationVo.setFileName(attach.getFileName());
					educationVo.setFileSize(attach.getSize());
					educationVo.setCreateTimeStr(format.format(educationVo.getCreateTime()));
					json.put("result", "true");
					json.put("education", JSONObject.fromObject(educationVo, new JsonConfig()));
					json.put("err", "");
				}else{
					json.put("err", "没有查到该条新闻！");
				}
			}catch (Exception e) {
				e.printStackTrace();
				json.put("err", "解析错误!");
			}
		}
		this.print(json.toString());
	}
	/**
	 * 
	 * @intruduction 结果字段拼接
	 * @author Will
	 * @Date 下午2:06:30
	 * @param pager
	 * @return
	 */
	private String makeEducationList(Pager pager) {
		ArrayList<Education> list = (ArrayList<Education>) pager.getPageList();
		JSONArray array =new JSONArray();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(int i = 0; i < list.size();i++){
			JSONObject object =new JSONObject();
			Education education = list.get(i);
			object.put("title", education.getTitle());
			object.put("cover", education.getCover());
			object.put("type", education.getType());
			object.put("description", education.getDescription());
			object.put("id", education.getId());
			object.put("createTimeStr", sdf.format(education.getCreateTime()));
			object.put("category", education.getCategory());
			array.add(object);
		}
		return array.toString();
	}
	
	private String makeEducationCategoryAtrList(Pager pager) {
		ArrayList<CategoryAttribute> list = (ArrayList<CategoryAttribute>) pager.getPageList();
		JSONArray array =new JSONArray();
		for(int i = 0; i < list.size();i++){
			JSONObject object =new JSONObject();
			CategoryAttribute atr = list.get(i);
			object.put("attrKey", atr.getAttrKey());
			object.put("attrValue", atr.getAttrValue());
			
			array.add(object);
		}
		return array.toString();
	}
	/**
	 * 
	 * @intruduction 查询附件
	 * @author Will
	 * @Date 下午2:35:23
	 * @param id
	 * @return
	 */
	private Attach queryAttach(String id){
		List<Attach> list = this.attachServiceImpl.queryAttchListByFormId(id);
		Attach attach = new Attach();
		for(int i = 0;i < list.size();i++){
			if(list.get(i).getAttachType().equals("educationFile")){
				attach = list.get(i);
			}
		}
		return attach;
	}
	/**
	 * 
	 * @intruduction 统计下载量 查看量
	 * @author Will
	 * @Date 下午3:25:21
	 * @param request
	 * @param response
	 * @param id
	 * @param type :1、查看 2、下载
	 */
	@RequestMapping(value="add_visitCount")
	public void downloadAttach(HttpServletRequest request,HttpServletResponse response,String id,
			Integer type){
		if(StringUtils.isNotBlank(id) && id.trim()!= ""){
			try{
				Education education = this.educationServiceImpl.getEntityById(Education.class, id);
				if(education != null){
					if(type == 1){
						education.setVisitNum(education.getVisitNum()+1);
						this.saveEReport(education);
					}else{
						education.setDownloadNum(education.getDownloadNum() + 1);
						this.saveEReport(education);
					}
					this.educationServiceImpl.saveOrUpdate(education);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.print("");
	}
	/**
	 * 
	 * @intruduction 保存报表数据
	 * @author xm
	 * @Date 下午3:25:21
	 * @param request
	 * @param response
	 */
	public void saveEReport(Education education){
		User user = (User) this.getHttpSession().getAttribute("user");
		if(education!=null&&user!=null&&user.getOrgFrame()!=null)
		this.educationReportServiceImpl.saveOrUpdate(education,user);
	}
}
