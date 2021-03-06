package com.datacenter.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.common.base.controller.BaseController;
import com.common.dataimport.ImportExcelUtil;
import com.common.utils.MathUtil;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.common.utils.tld.DictUtils;
import com.datacenter.module.OperatingData;
import com.datacenter.service.IOperatingDataService;
import com.datacenter.vo.OperatingDataVo;
import com.google.gson.JsonObject;

/**
 * @Description 营运数据	控制层
 * @author xuezb
 * @date 2019年2月19日
 */
@Controller
@RequestMapping("/datecenter/operatingData")
public class OperatingDataController extends BaseController{
	
	public static Logger logger_excel = Logger.getLogger("EXCEL");//记录导入日志

	@Autowired
	private IOperatingDataService operatingDataServiceImpl;

	@Autowired
	private TotalTableController totalTableController;



	/**
	 * 营运数据	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/operatingData_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/operatingData_list";
	}
	
	
	/**
	 * 营运数据	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param operatingDataVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/operatingData_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, OperatingDataVo operatingDataVo){
		JSONObject json = new JSONObject();
		Pager pager = this.operatingDataServiceImpl.queryEntityList(page, rows, operatingDataVo);
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	@RequestMapping(value="/operatingData_Total")
	public void operatingDataTotal(HttpServletResponse response, OperatingDataVo operatingDataVo){
		JSONObject json = new JSONObject();
		json.put("result", false);
		try {
			Map<String, String> m= this.operatingDataServiceImpl.operatingDataTotal(operatingDataVo);
			json.put("totalTraffic", m.get("totalTraffic"));
			json.put("ytkTraffic", m.get("ytkTraffic"));
			json.put("mobilePaymentTraffic", m.get("mobilePaymentTraffic"));
			json.put("generalIncome", m.get("generalIncome"));
			json.put("ytkIncome", m.get("ytkIncome"));
			json.put("mobilePaymentIncome", m.get("mobilePaymentIncome"));
			json.put("result", true);
		} catch (Exception e) {
			json.put("result", false);
			logger.error(e.getMessage(), e);
		}
		this.print(json);
		
	}
	
	
	/**
	 * 营运数据	编辑
	 * @param request
	 * @param winName
	 * @param operatingDataVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/operatingData_edit")
	public String edit(HttpServletRequest request, String winName, OperatingDataVo operatingDataVo) throws ParseException{
		if(StringUtils.isNotBlank(operatingDataVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(operatingDataVo.getDutyDateStr());
			operatingDataVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(operatingDataVo.getId())){
			OperatingData operatingData = this.operatingDataServiceImpl.getEntityById(OperatingData.class, operatingDataVo.getId());
			BeanUtils.copyProperties(operatingData, operatingDataVo);
			request.setAttribute("operatingDataVo", operatingDataVo);
		}else{
			request.setAttribute("operatingDataVo", operatingDataVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/operatingData_edit";
	}
	
	
	/**
	 * 营运数据	保存or修改
	 * @param response
	 * @param operatingDataVo
	 * @author xuezb 
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/operatingData_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, OperatingDataVo operatingDataVo){
		JsonObject json = new JsonObject();
		try {
			OperatingData operatingData = this.operatingDataServiceImpl.saveOrUpdate(operatingDataVo);
			json.addProperty("result", true);
			json.addProperty("id",operatingData.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 营运数据	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月19日
	 */
	@RequestMapping(value="/operatingData_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.operatingDataServiceImpl.delete(OperatingData.class, ids);
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
	 * 
	 * @方法：@param request
	 * @方法：@param winName
	 * @方法：@param operatingDataVo
	 * @方法：@return
	 * @方法：@throws ParseException
	 * @描述：批量导入数据
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月14日
	 */
	@RequestMapping(value="/operatingData_import")
	public String dataImport(HttpServletRequest request, String winName, OperatingDataVo operatingDataVo) throws ParseException{
		if(StringUtils.isNotBlank(operatingDataVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(operatingDataVo.getDutyDateStr());
			operatingDataVo.setDutyDate(dutyDate);
		}
		request.setAttribute("operatingDataVo", operatingDataVo);
		request.setAttribute("winName", winName);
		return "/page/datecenter/operatingData_import";
	}
	
	/**
	 * 
	 * @方法：@param file
	 * @方法：@param response
	 * @方法：@return
	 * @方法：@throws Exception
	 * @描述：导入
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月14日
	 */
	@Transactional
	@RequestMapping(value="/submitOperatingDataExcel",produces = "application/json;charset=utf-8",method=RequestMethod.POST) 
	public void importExcel(MultipartFile file,HttpServletResponse response,OperatingDataVo operatingDataVo) throws Exception{
		InputStream in =null;  
        List<List<Object>> listob = null;  
        if(file.getSize()==0){
        	this.print("请选择文件!");
        	return ;
        }
        in = file.getInputStream();  
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename(),34);  
        in.close();  
          
        logger_excel.info("-------------营运数据导入开始 开始时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
        logger_excel.info("导入操作用户："+this.getSessionUser().getUserName());
        int successCount=0;//计算失败数和成功数
        StringBuffer strErrMsg=new StringBuffer();
        boolean isImportReady=true;
        List<OperatingData> importOperatingDataList=new ArrayList<>();
        //TTID
    	if(StringUtils.isEmpty(operatingDataVo.getTtId())){
    		strErrMsg.append("与主表关联失效，无法导入数据\r\n");
    		strErrMsg.append("----导入失败----");
        	this.print(strErrMsg.toString());
        	return;
    	}
        for (int i = 0; i < listob.size(); i++) {
        	System.out.println("第"+(i+1)+"条数据");
        	
        	
            List<Object> lo = listob.get(i);  
            if(lo.isEmpty())	continue;	//去掉多余的行
            //收费站
            String tollGateName=(String)lo.get(0);
        	if(tollGateName.isEmpty())	break;//退出循环	
        	String tollGateId= DictUtils.getDictAttrKey("dc_tollGate_operation", tollGateName);
        	if(StringUtils.isEmpty(tollGateId)){
        		strErrMsg.append("第"+(i+1)+"条数据：【收费站】列存有格式问题,是否正确填写收费站名称\r\n");
        		isImportReady=false;
        	}
        	//总车流
        	String totalTrafficStr=(String)lo.get(1);
        	if(StringUtils.isEmpty(totalTrafficStr)){
        		strErrMsg.append("第"+(i+1)+"条数据：【总车流】列存有格式问题，不能空\r\n");
        		isImportReady=false;
        	}
        	int totalTraffic=0;
        	try {
        		totalTraffic=MathUtil.stringToInt(totalTrafficStr);
			} catch (Exception e) {
				strErrMsg.append("第"+(i+1)+"条数据：【总车流】列存有格式问题，检查是否为整数\r\n");
        		isImportReady=false;
			}
        	//其中粤通卡车流
        	String ytkTrafficStr=(String)lo.get(2);
        	if(StringUtils.isEmpty(ytkTrafficStr)){
        		strErrMsg.append("第"+(i+1)+"条数据：【其中粤通卡车流】列存有格式问题，不能空\r\n");
        		isImportReady=false;
        	}
        	int ytkTraffic=0;
        	try {
        		ytkTraffic=MathUtil.stringToInt(ytkTrafficStr);
			} catch (Exception e) {
				strErrMsg.append("第"+(i+1)+"条数据：【其中粤通卡车流】列存有格式问题，检查是否为整数\r\n");
        		isImportReady=false;
			}
        	//移动支付车流
        	String mobilePaymentTrafficStr=(String)lo.get(3);
        	if(StringUtils.isEmpty(mobilePaymentTrafficStr)){
        		strErrMsg.append("第"+(i+1)+"条数据：【移动支付车流】列存有格式问题，不能空\r\n");
        		isImportReady=false;
        	}
        	int mobilePaymentTraffic=0;
        	try {
        		mobilePaymentTraffic=MathUtil.stringToInt(mobilePaymentTrafficStr);
			} catch (Exception e) {
				strErrMsg.append("第"+(i+1)+"条数据：【移动支付车流】列存有格式问题，检查是否为整数\r\n");
        		isImportReady=false;
			}
        	//总收费额
        	String generalIncomeStr=(String)lo.get(4);
        	if(StringUtils.isEmpty(generalIncomeStr)){
        		strErrMsg.append("第"+(i+1)+"条数据：【总收费额】列存有格式问题，不能空\r\n");
        		isImportReady=false;
        	}
        	double generalIncome=0.0;
        	try {
        		generalIncome=Double.parseDouble(generalIncomeStr);
			} catch (Exception e) {
				strErrMsg.append("第"+(i+1)+"条数据：【总收费额】列存有格式问题，检查是否为数值\r\n");
        		isImportReady=false;
			}
        	//其中粤通卡收入
        	String ytkIncomeStr=(String)lo.get(5);
        	if(StringUtils.isEmpty(ytkIncomeStr)){
        		strErrMsg.append("第"+(i+1)+"条数据：【其中粤通卡收入】列存有格式问题，不能空\r\n");
        		isImportReady=false;
        	}
        	double ytkIncome=0.0;
        	try {
        		ytkIncome=Double.parseDouble(ytkIncomeStr);
			} catch (Exception e) {
				strErrMsg.append("第"+(i+1)+"条数据：【其中粤通卡收入】列存有格式问题，检查是否为数值\r\n");
        		isImportReady=false;
			}
        	//移动支付收入
        	String mobilePaymentIncomeStr=(String)lo.get(6);
        	if(StringUtils.isEmpty(mobilePaymentIncomeStr)){
        		strErrMsg.append("第"+(i+1)+"条数据：【移动支付收入】列存有格式问题，不能空\r\n");
        		isImportReady=false;
        	}
        	double mobilePaymentIncome=0.0;
        	try {
        		mobilePaymentIncome=Double.parseDouble(mobilePaymentIncomeStr);
			} catch (Exception e) {
				strErrMsg.append("第"+(i+1)+"条数据：【移动支付收入】列存有格式问题，检查是否为数值\r\n");
        		isImportReady=false;
			}

        	if(isImportReady){
        		//以上excel内容格式检查通过，开始整理实体
        		 String dutyDateStr=operatingDataVo.getDutyDateStr(); //录入时间
                 Date dutyDate =DateUtil.format(operatingDataVo.getDutyDateStr(), "yyyy-MM-dd");
                 try {
                 	boolean flag=operatingDataServiceImpl.isRecordExist(dutyDate,tollGateId);
                 	if(flag){
             			//存在重复的记录
             			logger_excel.info("-------------存在相同的记录，不导入："+dutyDateStr+"-"+tollGateName+"---------------");
             			continue;
                 	}else{
                 		//不存在则导入
                 		OperatingData operatingData=new OperatingData();
                 		operatingData.setTtId(operatingDataVo.getTtId());
                 		operatingData.setTitle(operatingDataVo.getTitle());
                 		operatingData.setDutyDate(dutyDate);
                 		operatingData.setFormNumber("HLZXRBB-06");
                 		operatingData.setTollGate(Integer.parseInt(tollGateId));
                 		operatingData.setTotalTraffic(totalTraffic);
                 		operatingData.setYtkTraffic(ytkTraffic);
                 		operatingData.setMobilePaymentTraffic(mobilePaymentTraffic);
                 		operatingData.setGeneralIncome(generalIncome);
                 		operatingData.setYtkIncome(ytkIncome);
                 		operatingData.setMobilePaymentIncome(mobilePaymentIncome);
                 		importOperatingDataList.add(operatingData);
                 	}
     			} catch (Exception e) {
     				this.print("第"+(i+1)+"条数据："+tollGateName+"，存有问题，导入中止 ");
     				return;
     			}
        	}
        }
        if(isImportReady){
        	//开始插入库
        	for (OperatingData operatingData : importOperatingDataList) {
        		operatingDataServiceImpl.save(operatingData);
        		successCount++;
        	}
        	logger_excel.info("导入数："+successCount);
        	logger_excel.info("-------------营运数据导入结束 结束时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"---------------");
        	this.print("营运数据导入完毕,导入数："+successCount);
        }else{
        	strErrMsg.append("----导入失败----");
        	this.print(strErrMsg.toString());
        }
	}


	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 * @param operatingDataVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@RequestMapping(value="/operatingData_export")
	public void export(HttpServletRequest request, HttpServletResponse response, OperatingDataVo operatingDataVo){
		//excel文件名
		String fileName = "营运数据汇总";

		//获取excle文档对象
		HSSFWorkbook wb = this.operatingDataServiceImpl.export(operatingDataVo);

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
