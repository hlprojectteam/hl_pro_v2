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
import com.datacenter.module.RoadWork;
import com.datacenter.service.IRoadWorkService;
import com.datacenter.vo.RoadWorkVo;
import com.google.gson.JsonObject;

/**
 * @Description 涉路施工	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/roadWork")
public class RoadWorkController extends BaseController{

	@Autowired
	private IRoadWorkService roadWorkServiceImpl;

	@Autowired
	private TotalTableController totalTableController;



	/**
	 * 涉路施工	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/roadWork_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/roadWork_list";
	}
	
	
	/**
	 * 涉路施工	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param roadWorkVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/roadWork_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, RoadWorkVo roadWorkVo){
		Pager pager = this.roadWorkServiceImpl.queryEntityList(page, rows, roadWorkVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 涉路施工	编辑
	 * @param request
	 * @param winName
	 * @param roadWorkVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/roadWork_edit")
	public String edit(HttpServletRequest request, String winName, RoadWorkVo roadWorkVo) throws ParseException{
		if(StringUtils.isNotBlank(roadWorkVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(roadWorkVo.getDutyDateStr());
			roadWorkVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(roadWorkVo.getId())){
			RoadWork roadWork = this.roadWorkServiceImpl.getEntityById(RoadWork.class, roadWorkVo.getId());
			BeanUtils.copyProperties(roadWork, roadWorkVo);
			request.setAttribute("roadWorkVo", roadWorkVo);
		}else{
			request.setAttribute("roadWorkVo", roadWorkVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/roadWork_edit";
	}
	
	
	/**
	 * 涉路施工	保存or修改
	 * @param response
	 * @param roadWorkVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/roadWork_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, RoadWorkVo roadWorkVo){
		JsonObject json = new JsonObject();
		try {
			RoadWork roadWork = this.roadWorkServiceImpl.saveOrUpdate(roadWorkVo);
			json.addProperty("result", true);
			json.addProperty("id",roadWork.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 涉路施工	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/roadWork_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.roadWorkServiceImpl.delete(RoadWork.class, ids);
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
	 * @param roadWorkVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@RequestMapping(value="/roadWork_export")
	public void export(HttpServletRequest request, HttpServletResponse response, RoadWorkVo roadWorkVo){
		//excel文件名
		String fileName = "涉路施工汇总";

		//获取excle文档对象
		HSSFWorkbook wb = this.roadWorkServiceImpl.export(roadWorkVo);

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
