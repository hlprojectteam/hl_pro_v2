package com.datacenter.controller;

import com.common.base.controller.BaseController;
import com.common.base.dao.IBaseDao;
import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.JsonDateValueProcessor;
import com.common.utils.helper.Pager;
import com.datacenter.module.Brief;
import com.datacenter.module.EquipmentStatus;
import com.datacenter.service.*;
import com.datacenter.vo.BriefVo;
import com.datacenter.vo.EquipmentStatusVo;
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
import java.util.List;

/**
 * @Description 工作简报	控制层
 * @author xuezb
 * @date 2019年2月18日
 */
@Controller
@RequestMapping("/datecenter/brief")
public class BriefController extends BaseController{

	@Autowired
	private IBriefService briefServiceImpl;

	@Autowired
	private TotalTableController totalTableController;

	@Autowired
	public IBaseDao baseDaoImpl;

	@Autowired
	private IClearingService clearingServiceImpl;
	@Autowired
	private IEquipmentOperationService equipmentOperationServiceImpl;
	@Autowired
	private IExceptionRecordService exceptionRecordServiceImpl;
	@Autowired
	private IFeedBackService feedBackServiceImpl;
	@Autowired
	private IFieldOperationsService fieldOperationsServiceImpl;
	@Autowired
	private IOperatingDataService operatingDataServiceImpl;
	@Autowired
	private IInfoThroughService infoThroughServiceImpl;
	@Autowired
	private IRescueWorkService rescueWorkServiceImpl;
	@Autowired
	private IRoadWorkService roadWorkServiceImpl;
	@Autowired
	private ISurveillanceInspectionService surveillanceInspectionServiceImpl;
	@Autowired
	private ITrafficAccidentService trafficAccidentServiceImpl;
	@Autowired
	private ITrafficJamService trafficJamServiceImpl;
	@Autowired
	private ITransferRegistrationService transferRegistrationServiceImpl;
	@Autowired
	private IEquipmentStatusService equipmentStatusServiceImpl;
	
	/**
	 * 工作简报	列表页面
	 * @param httpSession
	 * @param response
	 * @param menuCode
	 * @param ttId			
	 * @param dutyDateStr
	 * @return
	 * @author xuezb
	 * @Date 2019年2月18日
	 */
	@RequestMapping(value="/brief_list") 
	public String list(HttpSession httpSession,HttpServletResponse response,String menuCode, String ttId, String dutyDateStr) {
		this.getRequest().setAttribute("menuCode", menuCode);
		this.getRequest().setAttribute("ttId", ttId);
		this.getRequest().setAttribute("dutyDateStr", dutyDateStr);
		return "/page/datecenter/brief_list";
	}
	
	
	/**
	 * 工作简报	列表数据加载
	 * @param response
	 * @param page
	 * @param rows
	 * @param briefVo
	 * @return
	 * @author xuezb
	 * @Date 2019年2月18日
	 */
	@RequestMapping(value="/brief_load")
	public void load(HttpServletResponse response, Integer page, Integer rows, BriefVo briefVo){
		Pager pager = this.briefServiceImpl.queryEntityList(page, rows, briefVo);
		JSONObject json = new JSONObject();
		json.put("total", pager.getRowCount());
		JsonConfig config = new JsonConfig();  //自定义JsonConfig用于过滤Hibernate配置文件所产生的递归数据  
		config.registerJsonValueProcessor(Date.class , new JsonDateValueProcessor());//格式化日期
		json.put("rows", JSONArray.fromObject(pager.getPageList(),config));
		this.print(json);
	}
	
	
	/**
	 * 工作简报	编辑
	 * @param request
	 * @param winName
	 * @param briefVo
	 * @return
	 * @author xuezb 
	 * @throws ParseException 
	 * @Date 2019年2月18日
	 */
	@RequestMapping(value="/brief_edit")
	public String edit(HttpServletRequest request, String winName, BriefVo briefVo, String sign) throws ParseException{
		/*if(StringUtils.isNotBlank(briefVo.getDutyDateStr())){
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			Date dutyDate = fmt.parse(briefVo.getDutyDateStr());
			briefVo.setDutyDate(dutyDate);
		}
		if(StringUtils.isNotBlank(briefVo.getId())){
			Brief brief = this.briefServiceImpl.getEntityById(Brief.class, briefVo.getId());
			BeanUtils.copyProperties(brief, briefVo);
			request.setAttribute("briefVo", briefVo);
		}else{
			request.setAttribute("briefVo", briefVo);
		}
		request.setAttribute("winName", winName);
		return "/page/datecenter/brief_edit";*/

		List<Brief> briefList = this.briefServiceImpl.queryEntityList(briefVo);

		if(briefList != null && briefList.size() > 0){			//若 briefList.size()，则代表已经生成过简报内容，并保存过。

			Brief brief = briefList.get(0);
			BeanUtils.copyProperties(brief, briefVo);

		}else{

			if(StringUtils.isNotBlank(briefVo.getDutyDateStr())){
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
				Date dutyDate = fmt.parse(briefVo.getDutyDateStr());
				briefVo.setDutyDate(dutyDate);

				SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy年MM月dd日");
				briefVo.setTitle(fmt1.format(dutyDate) + "环龙运营控制指挥中心工作简报");
			}

			briefVo.setFormNumber("HLZXRBB-01");
			briefVo.setStatus(0);		//初始状态

			if(StringUtils.isNotBlank(briefVo.getTtId())){		//关联主表id不为空，则根据主表id 获取其它子表数据
				String ttId = briefVo.getTtId();

				/**交通运行情况*/
				//一、交通概况
				long count_rescuework = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_rescuework t where t.ttId = '" + ttId + "'");
				String jt_1 = "一、交通概况 ：\n" + "	拯救作业" + count_rescuework + "宗。";

				//二、路面施工
				long count_roadwork = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_roadwork t where t.ttId = '" + ttId + "'");
				String jt_2 = "\n二、路面施工：\n" + "	全天涉路施工" + count_roadwork + "宗。";

				//三、其它情况
				long count_exceptionrecord = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_exceptionrecord t where t.exception_Type = 1 and t.ttId = '" + ttId + "'");
				long count_infothrough = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_infothrough t where t.ttId = '" + ttId + "'");
				long count_feedback = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_feedback t where t.fb_Type = 1 and t.ttId = '" + ttId + "'");
				String jt_3 = "";
				if(count_feedback > 0){
					jt_3 = "\n二、其它情况：\n" +
							"	1、营运异常记录" + count_exceptionrecord + "宗;\n" +
							"	2、信息通传" + count_infothrough + "宗;\n" +
							"	3、顾客投诉" + count_feedback + "宗。";
				}else{
					jt_3 = "\n二、其它情况：\n" +
							"	1、营运异常记录" + count_exceptionrecord + "宗;\n" +
							"	2、信息通传" + count_infothrough + "宗;\n" +
							"	3、全线收费站有序开展超限非现场执法，无投诉及收费纠纷。";
				}

				briefVo.setTrafficOperation(jt_1 + jt_2 + jt_3);



				/**设备运行情况*/
				//一、联网关键设备运行监测情况
				String sb_1 = "一、联网关键设备运行监测情况：\n";

				EquipmentStatusVo equipmentStatusVo = new EquipmentStatusVo();
				equipmentStatusVo.setTtId(ttId);
				List<EquipmentStatus> equipmentStatusList = this.equipmentStatusServiceImpl.queryEntityList(equipmentStatusVo);
				if(equipmentStatusList.size() > 0){
					EquipmentStatus equipmentStatus = equipmentStatusList.get(0);
					sb_1 += "	1、标识点运行情况：\n " +
							"	R1标识成功率" + equipmentStatus.getSuccessRateR1() + "%、R1误标数量" + equipmentStatus.getMislabelNumR1() + "；R2标识成功率" + equipmentStatus.getSuccessRateR2() + "%、R2误标数量" + equipmentStatus.getMislabelNumR2() + "；5.8标识成功率" + equipmentStatus.getSuccessRateE1() + "% ；";
					sb_1 += "	2、高清卡口运行情况（车牌识别成功率）：\n" +
							"	细沥-黄阁（东行）" + equipmentStatus.getSuccessRateA() + "%；细沥-黄阁（西行）" + equipmentStatus.getSuccessRateB() + "%；庙贝沙-横沥（北行）" + equipmentStatus.getSuccessRateC() + "%；庙贝沙-横沥（南行）" + equipmentStatus.getSuccessRateD() + "%；中心已通知相关单位跟进处理。";
				}else{
					sb_1 += "	1、标识点运行情况：\n 暂无数据；\n" +
							"	2、高清卡口运行情况（车牌识别成功率）：\n暂无数据；\n";
				}


				//二、路面监控系统
				String sb_2 = "\n二、路面监控系统：\n";
				//路面摄像枪
				long count_si2 = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_SurveillanceInspection t where t.failure_Equipment = 2 and t.ttId = '" + ttId + "'");
				//收费站广场摄像枪
				long count_si3 = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_SurveillanceInspection t where t.failure_Equipment = 3 and t.ttId = '" + ttId + "'");
				//情报板系统
				long count_si4 = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_SurveillanceInspection t where t.failure_Equipment = 4 and t.ttId = '" + ttId + "'");

				if(count_si2 > 0){
					sb_2 += "	1、路面摄像枪：全线共有65支摄像枪，故障" + count_si2 + "支（详见监控巡检），其它" + (65-count_si2) + "支能正常使用；\n";
				}else{
					sb_2 += "	1、路面摄像枪：全线共有65支摄像枪，均正常使用；\n";
				}
				if(count_si3 > 0){
					sb_2 += "	2、收费站广场摄像枪：全线共有32支，故障" + count_si3 + "支（详见监控巡检），其它" + (32-count_si3) + "支能正常使用；\n";
				}else{
					sb_2 += "	2、收费站广场摄像枪：全线共有32支，均正常使用；\n";
				}
				if(count_si4 > 0){
					sb_2 += "	3、情报板系统：故障数量" + count_si4 + "\n";
				}else{
					sb_2 += "	3、情报板系统：南行0+100KM、南行12+250KM、北行12+300KM均能正常显示。\n";
				}


				//三、各站收费设备运行情况
				String sb_3 = "\n三、各站收费设备运行情况：\n";
				//所有车道都正常的收费站数量
				long count_eq1 = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_EquipmentOperation t where t.cdgqzp_ = 1 and t.zdfkj_ = 1 and t.mtcckcd_ = 1 " +
						" and t.etcckcd_ = 1 and t.mtcrkcd_ = 1 and t.etcrkcd_ = 1 and t.jzcd_ = 1 and t.ttId = '" + ttId + "'");
				//存在车道故障，无法使用的收费站数量
				long count_eq2 = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_EquipmentOperation t where (t.cdgqzp_ = 3 or t.zdfkj_ = 3 or t.mtcckcd_ = 3 or t.etcckcd_ = 3 or t.mtcrkcd_ = 3 or t.etcrkcd_ = 3 or t.jzcd_ = 3) " +
						" and t.ttId =  '" + ttId + "'");

				if(count_eq1 == 13){
					sb_3 += "设备故障报修及跟踪情况详见《各站车道设备运行情况统计表》。";
				}else if(count_eq2 > 0){
					sb_3 += "现有" + count_eq2 + "个收费站的车道故障无法正常使用，详见《各站车道设备运行情况统计表》。";
				}else{
					sb_3 += "现有部分收费站的车道故障，暂不影响发卡、收费，详见《各站车道设备运行情况统计表》。";
				}

				briefVo.setEquipmentOperation(sb_1 + sb_2 + sb_3);
			}


		}

		request.setAttribute("briefVo", briefVo);
		request.setAttribute("winName", winName);
		request.setAttribute("sign", sign);
		return "/page/datecenter/brief_edit";
	}


	/**
	 * 工作简报 重新加载数据
	 * @param response
	 * @param ttId
	 * @author xuezb
	 * @Date 2019年2月18日
	 */
	@RequestMapping(value="/brief_reload")
	public void reload(HttpServletResponse response, String ttId){
		JsonObject json = new JsonObject();
		try {
			//交通运行情况
			String trafficOperation = "";
			//设备运行情况
			String equipmentOperation = "";

			if(StringUtils.isNotBlank(ttId)){

				/**交通运行情况*/
				//一、交通概况
				long count_rescuework = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_rescuework t where t.ttId = '" + ttId + "'");
				String jt_1 = "一、交通概况 ：\n" + "	拯救作业" + count_rescuework + "宗。";

				//二、路面施工
				long count_roadwork = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_roadwork t where t.ttId = '" + ttId + "'");
				String jt_2 = "\n二、路面施工：\n" + "	全天涉路施工" + count_roadwork + "宗。";

				//三、其它情况
				long count_exceptionrecord = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_exceptionrecord t where t.exception_Type = 1 and t.ttId = '" + ttId + "'");
				long count_infothrough = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_infothrough t where t.ttId = '" + ttId + "'");
				long count_feedback = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_feedback t where t.fb_Type = 1 and t.ttId = '" + ttId + "'");
				String jt_3 = "";
				if(count_feedback > 0){
					jt_3 = "\n二、其它情况：\n" +
							"	1、营运异常记录" + count_exceptionrecord + "宗;\n" +
							"	2、信息通传" + count_infothrough + "宗;\n" +
							"	3、顾客投诉" + count_feedback + "宗。";
				}else{
					jt_3 = "\n二、其它情况：\n" +
							"	1、营运异常记录" + count_exceptionrecord + "宗;\n" +
							"	2、信息通传" + count_infothrough + "宗;\n" +
							"	3、全线收费站有序开展超限非现场执法，无投诉及收费纠纷。";
				}

				trafficOperation = jt_1 + jt_2 + jt_3;



				/**设备运行情况*/
				//一、联网关键设备运行监测情况
				String sb_1 = "一、联网关键设备运行监测情况：\n";

				EquipmentStatusVo equipmentStatusVo = new EquipmentStatusVo();
				equipmentStatusVo.setTtId(ttId);
				List<EquipmentStatus> equipmentStatusList = this.equipmentStatusServiceImpl.queryEntityList(equipmentStatusVo);
				if(equipmentStatusList.size() > 0){
					EquipmentStatus equipmentStatus = equipmentStatusList.get(0);
					sb_1 += "	1、标识点运行情况：\n " +
							"	R1标识成功率" + equipmentStatus.getSuccessRateR1() + "%、R1误标数量" + equipmentStatus.getMislabelNumR1() + "；R2标识成功率" + equipmentStatus.getSuccessRateR2() + "%、R2误标数量" + equipmentStatus.getMislabelNumR2() + "；5.8标识成功率" + equipmentStatus.getSuccessRateE1() + "% ；";
					sb_1 += "	2、高清卡口运行情况（车牌识别成功率）：\n" +
							"	细沥-黄阁（东行）" + equipmentStatus.getSuccessRateA() + "%；细沥-黄阁（西行）" + equipmentStatus.getSuccessRateB() + "%；庙贝沙-横沥（北行）" + equipmentStatus.getSuccessRateC() + "%；庙贝沙-横沥（南行）" + equipmentStatus.getSuccessRateD() + "%；中心已通知相关单位跟进处理。";
				}else{
					sb_1 += "	1、标识点运行情况：\n 暂无数据；\n" +
							"	2、高清卡口运行情况（车牌识别成功率）：\n暂无数据； \n";
				}


				//二、路面监控系统
				String sb_2 = "\n二、路面监控系统：\n";
				//路面摄像枪
				long count_si2 = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_SurveillanceInspection t where t.failure_Equipment = 2 and t.ttId = '" + ttId + "'");
				//收费站广场摄像枪
				long count_si3 = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_SurveillanceInspection t where t.failure_Equipment = 3 and t.ttId = '" + ttId + "'");
				//情报板系统
				long count_si4 = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_SurveillanceInspection t where t.failure_Equipment = 4 and t.ttId = '" + ttId + "'");

				if(count_si2 > 0){
					sb_2 += "	1、路面摄像枪：全线共有65支摄像枪，故障" + count_si2 + "支（详见监控巡检），其它" + (65-count_si2) + "支能正常使用；\n";
				}else{
					sb_2 += "	1、路面摄像枪：全线共有65支摄像枪，均正常使用；\n";
				}
				if(count_si3 > 0){
					sb_2 += "	2、收费站广场摄像枪：全线共有32支，故障" + count_si3 + "支（详见监控巡检），其它" + (32-count_si3) + "支能正常使用；\n";
				}else{
					sb_2 += "	2、收费站广场摄像枪：全线共有32支，均正常使用；\n";
				}
				if(count_si4 > 0){
					sb_2 += "	3、情报板系统：故障数量" + count_si4 + "\n";
				}else{
					sb_2 += "	3、情报板系统：南行0+100KM、南行12+250KM、北行12+300KM均能正常显示。\n";
				}


				//三、各站收费设备运行情况
				String sb_3 = "\n三、各站收费设备运行情况：\n";
				//所有车道都正常的收费站数量
				long count_eq1 = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_EquipmentOperation t where t.cdgqzp_ = 1 and t.zdfkj_ = 1 and t.mtcckcd_ = 1 " +
						" and t.etcckcd_ = 1 and t.mtcrkcd_ = 1 and t.etcrkcd_ = 1 and t.jzcd_ = 1 and t.ttId = '" + ttId + "'");
				//存在车道故障，无法使用的收费站数量
				long count_eq2 = this.baseDaoImpl.queryCountBySql("select count(t.id) from dc_EquipmentOperation t where (t.cdgqzp_ = 3 or t.zdfkj_ = 3 or t.mtcckcd_ = 3 or t.etcckcd_ = 3 or t.mtcrkcd_ = 3 or t.etcrkcd_ = 3 or t.jzcd_ = 3) " +
						" and t.ttId =  '" + ttId + "'");

				if(count_eq1 == 13){
					sb_3 += "设备故障报修及跟踪情况详见《各站车道设备运行情况统计表》。";
				}else if(count_eq2 > 0){
					sb_3 += "现有" + count_eq2 + "个收费站的车道故障无法正常使用，详见《各站车道设备运行情况统计表》。";
				}else{
					sb_3 += "现有部分收费站的车道故障，暂不影响发卡、收费，详见《各站车道设备运行情况统计表》。 ";
				}

				equipmentOperation = sb_1 + sb_2 + sb_3;
			}

			json.addProperty("trafficOperation", trafficOperation);
			json.addProperty("equipmentOperation", equipmentOperation);
			json.addProperty("result", true);
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 工作简报	保存or修改
	 * @param response
	 * @param briefVo
	 * @author xuezb 
	 * @Date 2019年2月18日
	 */
	@RequestMapping(value="/brief_saveOrUpdate")
	public void saveOrUpdate(HttpServletResponse response, BriefVo briefVo){
		JsonObject json = new JsonObject();
		try {
			Brief brief = this.briefServiceImpl.saveOrUpdate(briefVo);
			json.addProperty("result", true);
			json.addProperty("id",brief.getId());
		} catch (Exception e) {
			json.addProperty("result", false);
			logger.error(e.getMessage(), e);
		}finally{
			this.print(json.toString());
		}
	}
	
	
	/**
	 * 工作简报	删除
	 * @param response
	 * @param ids
	 * @author xuezb
	 * @Date 2019年2月18日
	 */
	@RequestMapping(value="/brief_delete")
	public void delete(HttpServletResponse response, String ids){
		JsonObject json = new JsonObject();
		try {
			if (StringUtils.isNotBlank(ids)){
				this.briefServiceImpl.delete(Brief.class, ids);
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
	 * @param briefVo
	 * @return
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	@RequestMapping(value="/brief_export")
	public void export(HttpServletRequest request, HttpServletResponse response, BriefVo briefVo){
		//excel文件名
		String fileName = "工作简报汇总";

		//获取excle文档对象
		HSSFWorkbook wb = this.briefServiceImpl.export(briefVo);

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
