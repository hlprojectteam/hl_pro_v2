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
import com.datacenter.module.SurveillanceInspection;
import com.datacenter.service.ISurveillanceInspectionService;
import com.datacenter.vo.SurveillanceInspectionVo;
import com.google.gson.JsonObject;

/**
 * @Description 监控巡检	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/surveillanceInspection")
public class SurveillanceInspectionController extends BaseController{

	@Autowired
	private ISurveillanceInspectionService surveillanceInspectionServiceImpl;

	@Autowired
	private TotalTableController totalTableController;



	/**
	 * 监控巡检	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/surveillanceInspection_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/surveillanceInspection_list";
	}
	
	
	/**
	 * 监控巡检	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param surveillanceInspectionVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/surveillanceInspection_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, SurveillanceInspectionVo surveillanceInspectionVo){
		Pager pager = this.surveillanceInspectionServiceImpl.queryEntityList(page, rows, surveillanceInspectionVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 监控巡检	编辑
	 * @param request
	 * @param winName
	 * @param surveillanceInspectionVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/surveillanceInspection_edit")
	public String edit(HttpServletRequest request, String winName, SurveillanceInspectionVo surveillanceInspectionVo) throws ParseException{
		if(StringUtils.isNotBlank(surveillanceInspectionVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(surveillanceInspectionVo.getDutyDateStr());
			surveillanceInspectionVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(surveillanceInspectionVo.getId())){
			SurveillanceInspection surveillanceInspection = this.surveillanceInspectionServiceImpl.getEntityById(SurveillanceInspection.class, surveillanceInspectionVo.getId());
			BeanUtils.copyProperties(surveillanceInspection, surveillanceInspectionVo);
			request.setAttribute("surveillanceInspectionVo", surveillanceInspectionVo);
		}else{
			request.setAttribute("surveillanceInspectionVo", surveillanceInspectionVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/surveillanceInspection_edit";
	}
	
	
	/**
	 * 监控巡检	保存or修改
	 * @param response
	 * @param surveillanceInspectionVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/surveillanceInspection_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, SurveillanceInspectionVo surveillanceInspectionVo){
		JsonObject json = new JsonObject();
		try {
			SurveillanceInspection surveillanceInspection = this.surveillanceInspectionServiceImpl.saveOrUpdate(surveillanceInspectionVo);
			json.addProperty("result", true);
			json.addProperty("id",surveillanceInspection.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 监控巡检	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/surveillanceInspection_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.surveillanceInspectionServiceImpl.delete(SurveillanceInspection.class, ids);
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
	 * @param surveillanceInspectionVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@RequestMapping(value="/surveillanceInspection_export")
	public void export(HttpServletRequest request, HttpServletResponse response, SurveillanceInspectionVo surveillanceInspectionVo){
		//excel文件名
		String fileName = "监控巡检汇总";

		//获取excle文档对象
		HSSFWorkbook wb = this.surveillanceInspectionServiceImpl.export(surveillanceInspectionVo);

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
