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
import com.datacenter.module.TransferRegistration;
import com.datacenter.service.ITransferRegistrationService;
import com.datacenter.vo.TransferRegistrationVo;
import com.google.gson.JsonObject;

/**
 * @Description 交接班登记表	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/transferRegistration")
public class TransferRegistrationController extends BaseController{

	@Autowired
	private ITransferRegistrationService transferRegistrationServiceImpl;

	@Autowired
	private TotalTableController totalTableController;
	
	
	/**
	 * 交接班登记表	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/transferRegistration_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/transferRegistration_list";
	}
	
	
	/**
	 * 交接班登记表	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param transferRegistrationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/transferRegistration_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, TransferRegistrationVo transferRegistrationVo){
		Pager pager = this.transferRegistrationServiceImpl.queryEntityList(page, rows, transferRegistrationVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateTimeValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 交接班登记表	编辑
	 * @param request
	 * @param winName
	 * @param transferRegistrationVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/transferRegistration_edit")
	public String edit(HttpServletRequest request, String winName, TransferRegistrationVo transferRegistrationVo) throws ParseException{
		if(StringUtils.isNotBlank(transferRegistrationVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(transferRegistrationVo.getDutyDateStr());
			transferRegistrationVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(transferRegistrationVo.getId())){
			TransferRegistration transferRegistration = this.transferRegistrationServiceImpl.getEntityById(TransferRegistration.class, transferRegistrationVo.getId());
			BeanUtils.copyProperties(transferRegistration, transferRegistrationVo);
			request.setAttribute("transferRegistrationVo", transferRegistrationVo);
		}else{
			request.setAttribute("transferRegistrationVo", transferRegistrationVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/transferRegistration_edit";
	}
	
	
	/**
	 * 交接班登记表	保存or修改
	 * @param response
	 * @param transferRegistrationVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/transferRegistration_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, TransferRegistrationVo transferRegistrationVo){
		JsonObject json = new JsonObject();
		try {
			TransferRegistration transferRegistration = this.transferRegistrationServiceImpl.saveOrUpdate(transferRegistrationVo);
			json.addProperty("result", true);
			json.addProperty("id",transferRegistration.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}


	/**
	 * 交接班登记表	批量新增
	 * @param response
	 * @param ttId
	 * @param dutyDateStr
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/transferRegistration_addMany")
	public void addMany(HttpServletResponse response, String ttId, String dutyDateStr){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ttId) && StringUtils.isNotBlank(dutyDateStr)){
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
				Date dutyDate = fmt.parse(dutyDateStr);
				SimpleDateFormat fmt1 = new SimpleDateFormat("HH:mm");
				SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy年MM月dd日");

				TransferRegistrationVo transferRegistrationVo = new TransferRegistrationVo();

				for (int i = 1; i <= 3; i++) {
					transferRegistrationVo.setTtId(ttId);
					transferRegistrationVo.setDutyDate(dutyDate);

					transferRegistrationVo.setTitle(fmt2.format(dutyDate) + "环龙运营控制指挥中心交接班登记表");
					transferRegistrationVo.setFormNumber("HLZXRBB-02");
					transferRegistrationVo.setShift(i);
					transferRegistrationVo.setWeather(1);
					transferRegistrationVo.setHandoverMatters("1、设备运情况：正常；\n2、交通运行情况：正常\n3、其它事项：无");
					transferRegistrationVo.setException("无");
					if(i == 1){
						transferRegistrationVo.setWatchTimeStart(fmt1.parse("08:00"));
						transferRegistrationVo.setWatchTimeEnd(fmt1.parse("16:00"));
						transferRegistrationVo.setHandoverTime(fmt1.parse("08:00"));
					}else if(i == 2){
						transferRegistrationVo.setWatchTimeStart(fmt1.parse("16:00"));
						transferRegistrationVo.setWatchTimeEnd(fmt1.parse("23:00"));
						transferRegistrationVo.setHandoverTime(fmt1.parse("16:00"));
					}else{
						transferRegistrationVo.setWatchTimeStart(fmt1.parse("23:00"));
						transferRegistrationVo.setWatchTimeEnd(fmt1.parse("08:00"));
						transferRegistrationVo.setHandoverTime(fmt1.parse("23:00"));
					}

					this.transferRegistrationServiceImpl.saveOrUpdate(transferRegistrationVo);
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
	 * 交接班登记表	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/transferRegistration_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.transferRegistrationServiceImpl.delete(TransferRegistration.class, ids);
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
	 * @param transferRegistrationVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@RequestMapping(value="/transferRegistration_export")
	public void export(HttpServletRequest request, HttpServletResponse response, TransferRegistrationVo transferRegistrationVo){
		//excel文件名
		String fileName = "交接班登记表汇总";

		//获取excle文档对象
		HSSFWorkbook wb = this.transferRegistrationServiceImpl.export(transferRegistrationVo);

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
