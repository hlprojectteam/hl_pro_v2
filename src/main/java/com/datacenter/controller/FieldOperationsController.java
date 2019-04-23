package com.datacenter.controller;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.datacenter.module.FieldOperations;
import com.datacenter.service.IFieldOperationsService;
import com.datacenter.vo.FieldOperationsVo;
import com.google.gson.JsonObject;

/**
 * @Description 外勤作业	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/fieldOperations")
public class FieldOperationsController extends BaseController{

	@Autowired
	private IFieldOperationsService fieldOperationsServiceImpl;

	@Autowired
	private TotalTableController totalTableController;
	
	
	/**
	 * 外勤作业	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/fieldOperations_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/fieldOperations_list";
	}
	
	
	/**
	 * 外勤作业	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param fieldOperationsVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/fieldOperations_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, FieldOperationsVo fieldOperationsVo){
		Pager pager = this.fieldOperationsServiceImpl.queryEntityList(page, rows, fieldOperationsVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 外勤作业	编辑
	 * @param request
	 * @param winName
	 * @param fieldOperationsVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/fieldOperations_edit")
	public String edit(HttpServletRequest request, String winName, FieldOperationsVo fieldOperationsVo) throws ParseException{
		if(StringUtils.isNotBlank(fieldOperationsVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(fieldOperationsVo.getDutyDateStr());
			fieldOperationsVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(fieldOperationsVo.getId())){
			FieldOperations fieldOperations = this.fieldOperationsServiceImpl.getEntityById(FieldOperations.class, fieldOperationsVo.getId());
			BeanUtils.copyProperties(fieldOperations, fieldOperationsVo);
			request.setAttribute("fieldOperationsVo", fieldOperationsVo);
		}else{
			request.setAttribute("fieldOperationsVo", fieldOperationsVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/fieldOperations_edit";
	}
	
	
	/**
	 * 外勤作业	保存or修改
	 * @param response
	 * @param fieldOperationsVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/fieldOperations_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, FieldOperationsVo fieldOperationsVo){
		JsonObject json = new JsonObject();
		try {
			FieldOperations fieldOperations = this.fieldOperationsServiceImpl.saveOrUpdate(fieldOperationsVo);
			json.addProperty("result", true);
			json.addProperty("id",fieldOperations.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 外勤作业	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/fieldOperations_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.fieldOperationsServiceImpl.delete(FieldOperations.class, ids);
				json.addProperty("result", true);
			}
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}

	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 * @param fieldOperationsVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@RequestMapping(value="/fieldOperations_export")
	public void export(HttpServletRequest request, HttpServletResponse response, FieldOperationsVo fieldOperationsVo){
		//excel文件名
		String fileName = "外勤作业汇总";

		//获取excle文档对象
		HSSFWorkbook wb = this.fieldOperationsServiceImpl.export(fieldOperationsVo);

		//将文件存到指定位置
		try {
			this.totalTableController.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
