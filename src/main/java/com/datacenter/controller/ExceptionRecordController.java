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
import com.datacenter.module.ExceptionRecord;
import com.datacenter.service.IExceptionRecordService;
import com.datacenter.vo.ExceptionRecordVo;
import com.google.gson.JsonObject;

/**
 * @Description 营运异常记录	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/exceptionRecord")
public class ExceptionRecordController extends BaseController{

	@Autowired
	private IExceptionRecordService exceptionRecordServiceImpl;

	@Autowired
	private TotalTableController totalTableController;
	
	
	/**
	 * 营运异常记录	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/exceptionRecord_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/exceptionRecord_list";
	}
	
	
	/**
	 * 营运异常记录	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param exceptionRecordVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/exceptionRecord_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, ExceptionRecordVo exceptionRecordVo){
		Pager pager = this.exceptionRecordServiceImpl.queryEntityList(page, rows, exceptionRecordVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 营运异常记录	编辑
	 * @param request
	 * @param winName
	 * @param exceptionRecordVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/exceptionRecord_edit")
	public String edit(HttpServletRequest request, String winName, ExceptionRecordVo exceptionRecordVo) throws ParseException{
		if(StringUtils.isNotBlank(exceptionRecordVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(exceptionRecordVo.getDutyDateStr());
			exceptionRecordVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(exceptionRecordVo.getId())){
			ExceptionRecord exceptionRecord = this.exceptionRecordServiceImpl.getEntityById(ExceptionRecord.class, exceptionRecordVo.getId());
			BeanUtils.copyProperties(exceptionRecord, exceptionRecordVo);
			request.setAttribute("exceptionRecordVo", exceptionRecordVo);
		}else{
			request.setAttribute("exceptionRecordVo", exceptionRecordVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/exceptionRecord_edit";
	}
	
	
	/**
	 * 营运异常记录	保存or修改
	 * @param response
	 * @param exceptionRecordVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/exceptionRecord_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, ExceptionRecordVo exceptionRecordVo){
		JsonObject json = new JsonObject();
		try {
			ExceptionRecord exceptionRecord = this.exceptionRecordServiceImpl.saveOrUpdate(exceptionRecordVo);
			json.addProperty("result", true);
			json.addProperty("id",exceptionRecord.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 营运异常记录	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/exceptionRecord_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.exceptionRecordServiceImpl.delete(ExceptionRecord.class, ids);
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
	 * @param exceptionRecordVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@RequestMapping(value="/exceptionRecord_export")
	public void export(HttpServletRequest request, HttpServletResponse response, ExceptionRecordVo exceptionRecordVo){
		//excel文件名
		String fileName = "营运异常记录汇总";

		//获取excle文档对象
		HSSFWorkbook wb = this.exceptionRecordServiceImpl.export(exceptionRecordVo);

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
