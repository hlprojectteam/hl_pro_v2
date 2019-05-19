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
import com.datacenter.module.EquipmentOperation;
import com.datacenter.service.IEquipmentOperationService;
import com.datacenter.vo.EquipmentOperationVo;
import com.google.gson.JsonObject;

/**
 * @Description 设备运行情况统计表	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/equipmentOperation")
public class EquipmentOperationController extends BaseController{

	@Autowired
	private IEquipmentOperationService equipmentOperationServiceImpl;

	@Autowired
	private TotalTableController totalTableController;
	
	
	/**
	 * 设备运行情况统计表	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/equipmentOperation_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/equipmentOperation_list";
	}
	
	
	/**
	 * 设备运行情况统计表	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param equipmentOperationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/equipmentOperation_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, EquipmentOperationVo equipmentOperationVo){
		Pager pager = this.equipmentOperationServiceImpl.queryEntityList(page, rows, equipmentOperationVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 设备运行情况统计表	编辑
	 * @param request
	 * @param winName
	 * @param equipmentOperationVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/equipmentOperation_edit")
	public String edit(HttpServletRequest request, String winName, EquipmentOperationVo equipmentOperationVo) throws ParseException{
		if(StringUtils.isNotBlank(equipmentOperationVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(equipmentOperationVo.getDutyDateStr());
			equipmentOperationVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(equipmentOperationVo.getId())){
			EquipmentOperation equipmentOperation = this.equipmentOperationServiceImpl.getEntityById(EquipmentOperation.class, equipmentOperationVo.getId());
			BeanUtils.copyProperties(equipmentOperation, equipmentOperationVo);
			request.setAttribute("equipmentOperationVo", equipmentOperationVo);
		}else{
			request.setAttribute("equipmentOperationVo", equipmentOperationVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/equipmentOperation_edit";
	}
	
	
	/**
	 * 设备运行情况统计表	保存or修改
	 * @param response
	 * @param equipmentOperationVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/equipmentOperation_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, EquipmentOperationVo equipmentOperationVo){
		JsonObject json = new JsonObject();
		try {
			EquipmentOperation equipmentOperation = this.equipmentOperationServiceImpl.saveOrUpdate(equipmentOperationVo);
			json.addProperty("result", true);
			json.addProperty("id",equipmentOperation.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}


	/**
	 * 设备运行情况统计表	批量新增
	 * @param response
	 * @param ttId
	 * @param dutyDateStr
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/equipmentOperation_addMany")
	public void delete(HttpServletResponse response, String ttId, String dutyDateStr){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ttId) && StringUtils.isNotBlank(dutyDateStr)){
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
				Date dutyDate = fmt.parse(dutyDateStr);
				SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy年MM月dd日");

				EquipmentOperationVo equipmentOperationVo = new EquipmentOperationVo();

				for (int i = 1; i <= 13; i++) {
					equipmentOperationVo.setTtId(ttId);
					equipmentOperationVo.setDutyDate(dutyDate);

					equipmentOperationVo.setTitle(fmt2.format(dutyDate) + "各站车道设备运行情况统计表");
					equipmentOperationVo.setFormNumber("HLZXRBB-05");
					equipmentOperationVo.setTollGate(i);
					equipmentOperationVo.setCdgqzp(1);
					equipmentOperationVo.setZdfkj(1);
					equipmentOperationVo.setMtcckcd(1);
					equipmentOperationVo.setEtcckcd(1);
					equipmentOperationVo.setMtcrkcd(1);
					equipmentOperationVo.setEtcrkcd(1);
					equipmentOperationVo.setJzcd(1);

					this.equipmentOperationServiceImpl.saveOrUpdate(equipmentOperationVo);
				}

				json.addProperty("result", true);
			}else{
				json.addProperty("result", false);
			}
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 设备运行情况统计表	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/equipmentOperation_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.equipmentOperationServiceImpl.delete(EquipmentOperation.class, ids);
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
	 * @param equipmentOperationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@RequestMapping(value="/equipmentOperation_export")
	public void export(HttpServletRequest request, HttpServletResponse response, EquipmentOperationVo equipmentOperationVo){
		//excel文件名
		String fileName = "设备运行情况汇总";

		//获取excle文档对象
		HSSFWorkbook wb = this.equipmentOperationServiceImpl.export(equipmentOperationVo);

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
