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
import com.datacenter.module.InfoThrough;
import com.datacenter.service.IInfoThroughService;
import com.datacenter.vo.InfoThroughVo;
import com.google.gson.JsonObject;

/**
 * @Description 信息通传	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/infoThrough")
public class InfoThroughController extends BaseController{

	@Autowired
	private IInfoThroughService infoThroughServiceImpl;

	@Autowired
	private TotalTableController totalTableController;
	
	
	/**
	 * 信息通传	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/infoThrough_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/infoThrough_list";
	}
	
	
	/**
	 * 信息通传	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param infoThroughVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/infoThrough_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, InfoThroughVo infoThroughVo){
		Pager pager = this.infoThroughServiceImpl.queryEntityList(page, rows, infoThroughVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 信息通传	编辑
	 * @param request
	 * @param winName
	 * @param infoThroughVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/infoThrough_edit")
	public String edit(HttpServletRequest request, String winName, InfoThroughVo infoThroughVo) throws ParseException{
		if(StringUtils.isNotBlank(infoThroughVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(infoThroughVo.getDutyDateStr());
			infoThroughVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(infoThroughVo.getId())){
			InfoThrough infoThrough = this.infoThroughServiceImpl.getEntityById(InfoThrough.class, infoThroughVo.getId());
			BeanUtils.copyProperties(infoThrough, infoThroughVo);
			request.setAttribute("infoThroughVo", infoThroughVo);
		}else{
			request.setAttribute("infoThroughVo", infoThroughVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/infoThrough_edit";
	}
	
	
	/**
	 * 信息通传	保存or修改
	 * @param response
	 * @param infoThroughVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/infoThrough_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, InfoThroughVo infoThroughVo){
		JsonObject json = new JsonObject();
		try {
			InfoThrough infoThrough = this.infoThroughServiceImpl.saveOrUpdate(infoThroughVo);
			json.addProperty("result", true);
			json.addProperty("id",infoThrough.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 信息通传	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/infoThrough_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.infoThroughServiceImpl.delete(InfoThrough.class, ids);
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
	 * @param infoThroughVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@RequestMapping(value="/infoThrough_export")
	public void export(HttpServletRequest request, HttpServletResponse response, InfoThroughVo infoThroughVo){
		//excel文件名
		String fileName = "信息通传汇总";

		//获取excle文档对象
		HSSFWorkbook wb = this.infoThroughServiceImpl.export(infoThroughVo);

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
