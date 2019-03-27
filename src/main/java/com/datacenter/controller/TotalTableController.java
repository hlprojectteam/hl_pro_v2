package com.datacenter.controller;

import com.common.base.controller.BaseController;
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.datacenter.module.TotalTable;
import com.datacenter.service.ITotalTableService;
import com.datacenter.service.ITransferRegistrationService;
import com.datacenter.vo.TotalTableVo;
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
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 值班汇总表	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/totalTable")
public class TotalTableController extends BaseController{

	@Autowired
	private ITotalTableService totalTableServiceImpl;

	@Autowired
	private ITransferRegistrationService transferRegistrationServiceImpl;


	/**
	 * 值班汇总表	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/totalTable_list")
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode) {
		this.getRequest().setAttribute("menuCode", menuCode);
		return "/page/datecenter/totalTable_list";
	}


	/**
	 * 值班汇总表	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param totalTableVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/totalTable_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, TotalTableVo totalTableVo){
		Pager pager = this.totalTableServiceImpl.queryEntityList(page, rows, totalTableVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}


	/**
	 * 值班汇总表	编辑
	 * @param request
	 * @param totalTableVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/totalTable_edit")
	public String edit(HttpServletRequest request, TotalTableVo totalTableVo){
		if(StringUtils.isNotBlank(totalTableVo.getId())){
			TotalTable totalTable = this.totalTableServiceImpl.getEntityById(TotalTable.class, totalTableVo.getId());
			BeanUtils.copyProperties(totalTable, totalTableVo);
			request.setAttribute("totalTableVo", totalTableVo);
		}else{
			request.setAttribute("totalTableVo", totalTableVo);
		}
		return "/page/datecenter/totalTable_edit";
	}


	/**
	 * 值班汇总表	保存or修改
	 * @param response
	 * @param totalTableVo
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/totalTable_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, TotalTableVo totalTableVo){
		JsonObject json = new JsonObject();
		try {
			TotalTable totalTable = this.totalTableServiceImpl.saveOrUpdate(totalTableVo);		//主表日期修改时,相关的字表数据中宁的日期也跟着该表
			json.addProperty("result", true);
			json.addProperty("id",totalTable.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}


	/**
	 * 值班汇总表	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/totalTable_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.totalTableServiceImpl.deleteEntityByIds(ids);		//删除主表时相关的子表数据也一并删除
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
	 * 判断日期是否已存在
	 * @param response
	 * @param dutyDateStr
	 * @author xuezb
	 * @throws ParseException
	 * @Date 2019年2月20日
	 */
	@RequestMapping(value="/totalTable_isExist")
	public void isExist(HttpServletResponse response, String dutyDateStr) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dutyDate = format.parse(dutyDateStr);
		TotalTableVo totalTableVo = new TotalTableVo();
		totalTableVo.setDutyDate(dutyDate);
		long rowCount = this.totalTableServiceImpl.queryEntityList(1, 10, totalTableVo).getRowCount();
		JsonObject json = new JsonObject();
		if(rowCount == 0){		//数据库表中查无数据,该日期在表中无数据
			json.addProperty("result", false);
		}else{
			json.addProperty("result", true);
		}
		this.print(json.toString());
	}


	/**
	 * 子表数据
	 * @param request
	 * @param ttId
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @throws ParseException
	 * @Date 2019年2月20日
	 */
	@RequestMapping(value="/totalTable_relation")
	public String relation(HttpServletRequest request, String ttId, String dutyDateStr) throws ParseException{
		request.setAttribute("ttId", ttId);
		request.setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/totalTable_relation";
	}


	/**
	 * 值班汇总表	下载
	 * @param request
	 * @param response
	 * @param totalTableVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@RequestMapping(value="/totalTable_downLoad")
	public void downLoad(HttpServletRequest request, HttpServletResponse response, TotalTableVo totalTableVo){
		TotalTable totalTable = this.totalTableServiceImpl.getEntityById(TotalTable.class, totalTableVo.getId());

		//excel文件名
		String fileName = totalTable.getTitle();

		//获取excle文档对象
		HSSFWorkbook wb = this.totalTableServiceImpl.downLoad(totalTableVo.getId());

		//将文件存到指定位置
		try {
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	//发送响应流方法
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(),"ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+ fileName +".xls");	//要保存的文件名
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
