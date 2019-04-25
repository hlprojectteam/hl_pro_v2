package com.datacenter.controller;

import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateTimeValueProcessor;
import com.common.utils.helper.Pager;
import com.datacenter.module.Clearing;
import com.datacenter.service.IClearingService;
import com.datacenter.vo.ClearingVo;
import com.google.gson.JsonObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 清障保洁	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/clearing")
public class ClearingController extends BaseController{

	@Autowired
	private IClearingService clearingServiceImpl;

	@Autowired
	private TotalTableController totalTableController;
	
	
	/**
	 * 清障保洁	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/clearing_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/clearing_list";
	}
	
	
	/**
	 * 清障保洁	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param clearingVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/clearing_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, ClearingVo clearingVo){
		Pager pager = this.clearingServiceImpl.queryEntityList(page, rows, clearingVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 清障保洁	编辑
	 * @param request
	 * @param winName
	 * @param clearingVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/clearing_edit")
	public String edit(HttpServletRequest request, String winName, ClearingVo clearingVo) throws ParseException{
		if(StringUtils.isNotBlank(clearingVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(clearingVo.getDutyDateStr());
			clearingVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(clearingVo.getId())){
			Clearing clearing = this.clearingServiceImpl.getEntityById(Clearing.class, clearingVo.getId());
			BeanUtils.copyProperties(clearing, clearingVo);
			request.setAttribute("clearingVo", clearingVo);
		}else{
			request.setAttribute("clearingVo", clearingVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/clearing_edit";
	}
	
	
	/**
	 * 清障保洁	保存or修改
	 * @param response
	 * @param clearingVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/clearing_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, ClearingVo clearingVo){
		JsonObject json = new JsonObject();
		try {
			Clearing clearing = this.clearingServiceImpl.saveOrUpdate(clearingVo);
			json.addProperty("result", true);
			json.addProperty("id",clearing.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 清障保洁	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/clearing_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.clearingServiceImpl.delete(Clearing.class, ids);
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
	 * @param clearingVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@RequestMapping(value="/clearing_export")
	public void export(HttpServletRequest request, HttpServletResponse response, ClearingVo clearingVo){
		//excel文件名
		String fileName = "清障保洁汇总";

		//获取excle文档对象
		HSSFWorkbook wb = this.clearingServiceImpl.export(clearingVo);

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
