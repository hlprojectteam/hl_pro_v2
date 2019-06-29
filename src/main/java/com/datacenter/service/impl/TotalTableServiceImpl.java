package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.cache.Cache;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.datacenter.dao.ITotalTableDao;
import com.datacenter.module.*;
import com.datacenter.service.*;
import com.datacenter.vo.*;
import com.urms.dataDictionary.module.CategoryAttribute;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 值班汇总表 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("totalTableServiceImpl")
public class TotalTableServiceImpl extends BaseServiceImpl implements ITotalTableService{

	@Autowired
	private ITotalTableDao totalTableDaoImpl;

	@Autowired
	private IBriefService briefServiceImpl;
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



	@Override
	public Pager queryEntityList(Integer page, Integer rows, TotalTableVo totalTableVo) {
		List<Criterion> params = new ArrayList<>();
		if(totalTableVo.getDutyDate() != null){			//日期
			params.add(Restrictions.eq("dutyDate", totalTableVo.getDutyDate()));
		}
		if(totalTableVo.getDutyDateStart() != null){	//日期Start
			params.add(Restrictions.ge("dutyDate", totalTableVo.getDutyDateStart()));
		}
		if(totalTableVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", totalTableVo.getDutyDateEnd()));
		}
		if(totalTableVo.getStatus() != null){		//状态
			params.add(Restrictions.eq("status", totalTableVo.getStatus()));
		}
		return this.totalTableDaoImpl.queryEntityList(page, rows, params, Order.desc("dutyDate"), TotalTable.class);
	}

	@Override
	public TotalTable saveOrUpdate(TotalTableVo totalTableVo) {
		TotalTable totalTable = new TotalTable();
		BeanUtils.copyProperties(totalTableVo, totalTable);
		if(StringUtils.isBlank(totalTable.getId())){
			this.save(totalTable);
			return totalTable;
		}else{
			TotalTable oldTotalTable = this.totalTableDaoImpl.getEntityById(TotalTable.class, totalTable.getId());
			/******************************** start ********************************************/
			if(oldTotalTable.getDutyDate() != totalTable.getDutyDate()){	//如果主表原来的日期  不等于  修改后主表的日期, 则相关的子表数据中的日期也要跟着修改
				updateSubTableDate(totalTable.getId(), totalTable.getDutyDate());

				oldTotalTable.setTitle(totalTable.getTitle());
				oldTotalTable.setDutyDate(totalTable.getDutyDate());
			}
			/********************************  end  ********************************************/
			this.update(oldTotalTable);
			return oldTotalTable;
		}
	}
	
	@Override
	public String saveChangeState(String id) {
		TotalTable totalTable = this.getEntityById(TotalTable.class, id);
		String sign = "";
		if(totalTable.getStatus()==null)
			totalTable.setStatus(0);//默认未发布
		if(totalTable.getStatus()==1){
			totalTable.setStatus(0);		
			sign = "down";
		}else{
			totalTable.setStatus(1);		
			sign = "up";
		}
		this.update(totalTable);
		return sign;
	}


	/**
	 * @Description	修改关联的子表数据中日期
	 * @param	ttId			主表Id
	 * @param	newDutyDate		主表修改后的新的日期
	 */
	private void updateSubTableDate(String ttId, Date newDutyDate){
		this.briefServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.clearingServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.equipmentOperationServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.exceptionRecordServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.feedBackServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.fieldOperationsServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.operatingDataServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.infoThroughServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.rescueWorkServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.roadWorkServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.surveillanceInspectionServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.trafficAccidentServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.trafficJamServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.transferRegistrationServiceImpl.updateDutyDate(ttId, newDutyDate);
		this.equipmentStatusServiceImpl.updateDutyDate(ttId, newDutyDate);
	}


	@Override
	public int deleteEntityByIds(String ids) {
		int count = 0;
		if(StringUtils.isNotBlank(ids)){
			//删除子表数据
			String[] idsArray = ids.split(",");
			for (String ttId : idsArray) {
				this.briefServiceImpl.deleteByTtId(ttId);
				this.clearingServiceImpl.deleteByTtId(ttId);
				this.equipmentOperationServiceImpl.deleteByTtId(ttId);
				this.exceptionRecordServiceImpl.deleteByTtId(ttId);
				this.feedBackServiceImpl.deleteByTtId(ttId);
				this.fieldOperationsServiceImpl.deleteByTtId(ttId);
				this.operatingDataServiceImpl.deleteByTtId(ttId);
				this.infoThroughServiceImpl.deleteByTtId(ttId);
				this.rescueWorkServiceImpl.deleteByTtId(ttId);
				this.roadWorkServiceImpl.deleteByTtId(ttId);
				this.surveillanceInspectionServiceImpl.deleteByTtId(ttId);
				this.trafficAccidentServiceImpl.deleteByTtId(ttId);
				this.trafficJamServiceImpl.deleteByTtId(ttId);
				this.transferRegistrationServiceImpl.deleteByTtId(ttId);
				this.equipmentStatusServiceImpl.deleteByTtId(ttId);
			}
			//删除主表数据
			this.delete(TotalTable.class, ids);
			count = idsArray.length;
		}
		return count;
	}



	@Override
	public HSSFWorkbook downLoad(String ttId) {

		//创建HSSFWorkbook
		HSSFWorkbook wb = new HSSFWorkbook();

		//单元格 基础样式
		HSSFCellStyle mainStyle = wb.createCellStyle();
		mainStyle.setBorderBottom(BorderStyle.THIN); 	//下边框
		mainStyle.setBorderLeft(BorderStyle.THIN);		//左边框
		mainStyle.setBorderTop(BorderStyle.THIN);		//上边框
		mainStyle.setBorderRight(BorderStyle.THIN);		//右边框
		mainStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
		mainStyle.setWrapText(true);					//设置自动换行

		//基础样式 水平居中
		HSSFCellStyle mainStyle_center = wb.createCellStyle();
		mainStyle_center.cloneStyleFrom(mainStyle);
		mainStyle_center.setAlignment(HorizontalAlignment.CENTER);
		//基础样式 水平靠左
		HSSFCellStyle mainStyle_left = wb.createCellStyle();
		mainStyle_left.cloneStyleFrom(mainStyle);
		mainStyle_left.setAlignment(HorizontalAlignment.LEFT);

		//设置第一行样式(工作表名称样式)
		HSSFCellStyle r0_style = wb.createCellStyle();
		r0_style.setAlignment(HorizontalAlignment.CENTER);		//水平居中
		r0_style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
		HSSFFont r0_font = wb.createFont();
		r0_font.setBold(true);						//字体加粗
		r0_font.setFontHeightInPoints((short)12);	//字体大小
		r0_style.setFont(r0_font);

		//设置第二行样式(表单编号样式)
		HSSFCellStyle r1_style = wb.createCellStyle();
		r1_style.setAlignment(HorizontalAlignment.RIGHT);		//水平靠右
		r0_style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
		HSSFFont r1_font = wb.createFont();
		r1_font.setBold(true);						//字体加粗
		r1_style.setFont(r1_font);

		//设置第三行样式(普通列标题样式)
		HSSFCellStyle r2_style = wb.createCellStyle();
		r2_style.cloneStyleFrom(mainStyle_center);	//基础样式 水平居中
		HSSFFont r2_font = wb.createFont();
		r2_font.setBold(true);						//字体加粗
		r2_style.setFont(r2_font);


		//创建目录
		HSSFSheet sheet0 = wb.createSheet("目录");
		CreationHelper createHelper = wb.getCreationHelper();


		//设置列宽
		sheet0.setColumnWidth(0, sheet0.getColumnWidth(0)*4);
		sheet0.setColumnWidth(1, sheet0.getColumnWidth(1)*11);

		//创建字体
		HSSFFont linkFont = wb.createFont();
		linkFont.setUnderline(FontFormatting.U_SINGLE);//设置下划线
		linkFont.setColor(HSSFColor.BLUE.index);//设置字体颜色
		linkFont.setFontHeightInPoints((short)13);	//字体大小
		HSSFCellStyle linkStyle = wb.createCellStyle();
		linkStyle.cloneStyleFrom(mainStyle_center);
		linkStyle.setFont(linkFont);


		//第一行
		HSSFRow row0 = sheet0.createRow(0);
		row0.createCell(0).setCellValue("目录");
		row0.createCell(1).setCellValue("备注");
		row0.setHeightInPoints(30);
		row0.getCell(0).setCellStyle(r2_style);
		row0.getCell(1).setCellStyle(r2_style);

		HSSFRow row1 = sheet0.createRow(1);
		row1.createCell(0).setCellValue("运营控制指挥中心每日简报");
		row1.createCell(1).setCellValue("反映每天车流路费、交通及设备运行基本情况");
		Hyperlink link1 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link1.setAddress("#运营控制指挥中心每日简报!A1");
		row1.getCell(0).setHyperlink(link1);
		row1.setHeightInPoints(25);
		row1.getCell(0).setCellStyle(linkStyle);
		row1.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row2 = sheet0.createRow(2);
		row2.createCell(0).setCellValue("交接班");
		row2.createCell(1).setCellValue("反映中心监控员工作交接事项");
		Hyperlink link2 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link2.setAddress("#交接班!A1");
		row2.getCell(0).setHyperlink(link2);
		row2.setHeightInPoints(25);
		row2.getCell(0).setCellStyle(linkStyle);
		row2.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row3 = sheet0.createRow(3);
		row3.createCell(0).setCellValue("监控巡检");
		row3.createCell(1).setCellValue("反映主线、收费站的视频监控巡检情况");
		Hyperlink link3 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link3.setAddress("#监控巡检!A1");
		row3.getCell(0).setHyperlink(link3);
		row3.setHeightInPoints(25);
		row3.getCell(0).setCellStyle(linkStyle);
		row3.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row4 = sheet0.createRow(4);
		row4.createCell(0).setCellValue("涉路施工");
		row4.createCell(1).setCellValue("反映施工项目及相关业务部门对施工现场检查情况");
		Hyperlink link4 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link4.setAddress("#涉路施工!A1");
		row4.getCell(0).setHyperlink(link4);
		row4.setHeightInPoints(25);
		row4.getCell(0).setCellStyle(linkStyle);
		row4.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row5 = sheet0.createRow(5);
		row5.createCell(0).setCellValue("设备运行情况统计表");
		row5.createCell(1).setCellValue("反映车道收费系统运行情况");
		Hyperlink link5 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link5.setAddress("#设备运行情况统计表!A1");
		row5.getCell(0).setHyperlink(link5);
		row5.getCell(0).setHyperlink(link4);
		row5.setHeightInPoints(25);
		row5.getCell(0).setCellStyle(linkStyle);
		row5.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row15 = sheet0.createRow(6);
		row15.createCell(0).setCellValue("联网设备日常检查表");
		row15.createCell(1).setCellValue("反映联网关键设备运行情况");
		Hyperlink link15 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link15.setAddress("#联网设备日常检查表!A1");
		row15.getCell(0).setHyperlink(link15);
		row15.setHeightInPoints(25);
		row15.getCell(0).setCellStyle(linkStyle);
		row15.getCell(1).setCellStyle(mainStyle_left);


		HSSFRow row6 = sheet0.createRow(7);
		row6.createCell(0).setCellValue("各站营运数据");
		row6.createCell(1).setCellValue("反映各收费站拆分前车流路费数据");
		Hyperlink link6 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link6.setAddress("#各站营运数据!A1");
		row6.getCell(0).setHyperlink(link6);
		row6.setHeightInPoints(25);
		row6.getCell(0).setCellStyle(linkStyle);
		row6.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row7 = sheet0.createRow(8);
		row7.createCell(0).setCellValue("拯救作业");
		row7.createCell(1).setCellValue("反映故障车的拯救情况");
		Hyperlink link7 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link7.setAddress("#拯救作业!A1");
		row7.getCell(0).setHyperlink(link7);
		row7.setHeightInPoints(25);
		row7.getCell(0).setCellStyle(linkStyle);
		row7.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row8 = sheet0.createRow(9);
		row8.createCell(0).setCellValue("清障保洁");
		row8.createCell(1).setCellValue(" 反映影响道路交通安全的处理情况");
		Hyperlink link8 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link8.setAddress("#清障保洁!A1");
		row8.getCell(0).setHyperlink(link8);
		row8.setHeightInPoints(25);
		row8.getCell(0).setCellStyle(linkStyle);
		row8.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row9 = sheet0.createRow(10);
		row9.createCell(0).setCellValue("营运异常记录");
		row9.createCell(1).setCellValue(" 反映对收费站广场、车道、亭内的安全检查，对收费工作的监督和稽查,以及协助收費站处理的其它异常");
		Hyperlink link9 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link9.setAddress("#异常记录!A1");
		row9.getCell(0).setHyperlink(link9);
		row9.setHeightInPoints(25);
		row9.getCell(0).setCellStyle(linkStyle);
		row9.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row10 = sheet0.createRow(11);
		row10.createCell(0).setCellValue("交通事故");
		row10.createCell(1).setCellValue(" 反映交通事故的处置概况");
		Hyperlink link10 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link10.setAddress("#交通事故!A1");
		row10.getCell(0).setHyperlink(link10);
		row10.setHeightInPoints(25);
		row10.getCell(0).setCellStyle(linkStyle);
		row10.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row11 = sheet0.createRow(12);
		row11.createCell(0).setCellValue("信息通传");
		row11.createCell(1).setCellValue(" 反映重要信息的获取及传达情况");
		Hyperlink link11 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link11.setAddress("#信息通传!A1");
		row11.getCell(0).setHyperlink(link11);
		row11.setHeightInPoints(25);
		row11.getCell(0).setCellStyle(linkStyle);
		row11.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row12 = sheet0.createRow(13);
		row12.createCell(0).setCellValue("顾客意见反馈");
		row12.createCell(1).setCellValue(" 反映顾客咨询、建议、投诉及处理情况");
		Hyperlink link12 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link12.setAddress("#顾客意见反馈!A1");
		row12.getCell(0).setHyperlink(link12);
		row12.setHeightInPoints(25);
		row12.getCell(0).setCellStyle(linkStyle);
		row12.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row13 = sheet0.createRow(14);
		row13.createCell(0).setCellValue("交通阻塞");
		row13.createCell(1).setCellValue("反映交通阻塞及处理情况");
		Hyperlink link13 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link13.setAddress("#交通阻塞!A1");
		row13.getCell(0).setHyperlink(link13);
		row13.setHeightInPoints(25);
		row13.getCell(0).setCellStyle(linkStyle);
		row13.getCell(1).setCellStyle(mainStyle_left);

		HSSFRow row14 = sheet0.createRow(15);
		row14.createCell(0).setCellValue("外勤作业");
		row14.createCell(1).setCellValue("反映相关业务部门对涉路案件的处理情况");
		Hyperlink link14 = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link14.setAddress("#外勤作业!A1");
		row14.getCell(0).setHyperlink(link14);
		row14.setHeightInPoints(25);
		row14.getCell(0).setCellStyle(linkStyle);
		row14.getCell(1).setCellStyle(mainStyle_left);



		//1.工作简报
		getBriefData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style);
		//2.交接班
		getTransferRegistrationData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//3.监控巡检
		getSurveillanceInspectionData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//4.涉路施工
		getRoadWorkData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//5.设备运行情况统计表
		getEquipmentOperationData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//6.联网设备日常检查表
		getEquipmentStatusData(wb, ttId, mainStyle_center, r0_style, r1_style, r2_style);
		//7.营运数据
		getOperatingData(wb, ttId, mainStyle_center, r0_style, r1_style, r2_style);
		//8.拯救作业
		getRescueWorkData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//9.清障保洁
		getClearingData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//10.营运异常记录
		getExceptionRecordData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//11.交通事故
		getTrafficAccidentData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//12.信息通传
		getInfoThroughData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//13.顾客意见反馈
		getFeedBackData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//14.交通阻塞
		getTrafficJamData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//15.外勤作业
		getFieldOperationsData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);

		return wb;
	}


	/**
	 * @intruduction 获取工作简报数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook getBriefData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style){
		BriefVo briefVo = new BriefVo();
		briefVo.setTtId(ttId);
		List<Brief> brList = this.briefServiceImpl.queryEntityList(briefVo);
		Brief brief = new Brief();
		if(brList != null && brList.size()>0){
			brief = brList.get(0);					//工作简报只取当天最新一条数据
		}

		//创建sheet
		HSSFSheet sheet1 = wb.createSheet("运营控制指挥中心每日简报");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
		sheet1.addMergedRegion(new CellRangeAddress(3, 3, 1, 4));
		sheet1.addMergedRegion(new CellRangeAddress(4, 4, 1, 4));
		sheet1.addMergedRegion(new CellRangeAddress(5, 5, 1, 4));

		//设置列宽
		for (int i = 0; i < 5; i++) {
			if(i != 4){
				sheet1.setColumnWidth(i, sheet1.getColumnWidth(i)*3);
			}else{
				sheet1.setColumnWidth(i, sheet1.getColumnWidth(i)*6);
			}
		}


		//创建行（第一行）
		HSSFRow row0 = sheet1.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(60);
		//创建单元格 并 设置单元格内容
		if(brList != null && brList.size() > 0){
			row0.createCell(0).setCellValue(brief.getTitle());
		}else{
			row0.createCell(0).setCellValue("环龙运营控制指挥中心工作简报");
		}
		//设置单元格样式
		HSSFCellStyle bigTitle = wb.createCellStyle();
		bigTitle.cloneStyleFrom(r0_style);
		HSSFFont bigFont = wb.createFont();
		bigFont.setBold(true);						//字体加粗
		bigFont.setFontHeightInPoints((short)24);	//字体大小
		bigTitle.setFont(bigFont);
		row0.getCell(0).setCellStyle(bigTitle);


		//第二行
		HSSFRow row1 = sheet1.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-01");
		row1.getCell(0).setCellStyle(r1_style);

		//第三行
		HSSFRow row2 = sheet1.createRow(2);
		row2.setHeightInPoints(40);
		if(StringUtils.isNotBlank(brief.getCwfzjl())){
			row2.createCell(0).setCellValue("常务副总经理：" + brief.getCwfzjl());
		}else{
			row2.createCell(0).setCellValue("常务副总经理：" );
		}
		if(StringUtils.isNotBlank(brief.getZgfzjl())){
			row2.createCell(1).setCellValue("主管副总经理：" + brief.getZgfzjl());
		}else{
			row2.createCell(1).setCellValue("主管副总经理：");
		}
		if(StringUtils.isNotBlank(brief.getZxfzr())){
			row2.createCell(2).setCellValue("中心主任：" + brief.getZxfzr());
		}else{
			row2.createCell(2).setCellValue("中心主任：");
		}
		if(StringUtils.isNotBlank(brief.getFhry())){
			row2.createCell(3).setCellValue("复核：" + brief.getFhry());
		}else{
			row2.createCell(3).setCellValue("复核：");
		}
		if(brief.getCreateTime() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			row2.createCell(4).setCellValue("简报生成时间：" + sdf.format(brief.getRiseTime()));
		}else{
			row2.createCell(4).setCellValue("简报生成时间：");
		}
		row2.getCell(0).setCellStyle(mainStyle_center);
		row2.getCell(1).setCellStyle(mainStyle_center);
		row2.getCell(2).setCellStyle(mainStyle_center);
		row2.getCell(3).setCellStyle(mainStyle_center);
		row2.getCell(4).setCellStyle(mainStyle_center);

		//第四行
		HSSFRow row3 = sheet1.createRow(3);
		row3.setHeightInPoints(50);			//设置行的高度
		row3.createCell(0).setCellValue("营运数据");
		row3.getCell(0).setCellStyle(mainStyle_center);
		row3.createCell(1).setCellValue(brief.getOperatingData());
		row3.getCell(1).setCellStyle(mainStyle_left);
		row3.createCell(2).setCellStyle(mainStyle_left);
		row3.createCell(3).setCellStyle(mainStyle_left);
		row3.createCell(4).setCellStyle(mainStyle_left);

		//第五行
		HSSFRow row4 = sheet1.createRow(4);
		row4.setHeightInPoints(160);			//设置行的高度
		row4.createCell(0).setCellValue("交通运行情况");
		row4.getCell(0).setCellStyle(mainStyle_center);
		row4.createCell(1).setCellValue(brief.getTrafficOperation());
		row4.getCell(1).setCellStyle(mainStyle_left);
        for (int i = 2; i < 5; i++) {
            row4.createCell(i).setCellStyle(mainStyle_center);
        }

		//第六行
		HSSFRow row5 = sheet1.createRow(5);
		row5.setHeightInPoints(200);			//设置行的高度
		row5.createCell(0).setCellValue("设备运行情况");
		row5.getCell(0).setCellStyle(mainStyle_center);
		row5.createCell(1).setCellValue(brief.getEquipmentOperation());
		row5.getCell(1).setCellStyle(mainStyle_left);
        for (int i = 2; i < 5; i++) {
            row5.createCell(i).setCellStyle(mainStyle_center);
        }


		return wb;
	}


	/**
	 * @intruduction 获取交接班登记表数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月28日
	 */
	public HSSFWorkbook getTransferRegistrationData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		TransferRegistrationVo transferRegistrationVo = new TransferRegistrationVo();
		transferRegistrationVo.setTtId(ttId);
		List<TransferRegistration> trList = this.transferRegistrationServiceImpl.queryEntityList(transferRegistrationVo);

		//创建sheet
		HSSFSheet sheet2 = wb.createSheet("交接班");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		sheet2.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

		//创建行（第一行）
		HSSFRow row0 = sheet2.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		if(trList != null && trList.size() > 0){
			row0.createCell(0).setCellValue(trList.get(0).getTitle());
		}else{
			row0.createCell(0).setCellValue("环龙运营控制指挥中心交接班登记表");
		}
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet2.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-02");
		row1.getCell(0).setCellStyle(r1_style);


		//第三行
		HSSFRow row2 = sheet2.createRow(2);
		row2.setHeightInPoints(30);		//行高
		HSSFCell cell;
		String[] title = {"班次","天气","本班次值班人员","值班时间","上班次值班人员","交接时间","交接事项","接班异常情况"};
		for(int i=0;i<title.length;i++){
			cell = row2.createCell(i);		//创建单元格
			cell.setCellValue(title[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式

			//列宽自适应（该方法在老版本的POI中效果不佳）
			/*sheet2.autoSizeColumn(i);*/
			//设置列宽
			if(i == 6){
				sheet2.setColumnWidth(i, sheet2.getColumnWidth(i)*4);
			}else{
				sheet2.setColumnWidth(i, sheet2.getColumnWidth(i)*5/2);
			}
		}

		//第四行 及 之后的行
		HSSFRow row;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int i = 0; i < trList.size(); i++) {
			row = sheet2.createRow(i + 3);	//创建行
			row.setHeightInPoints(60);					//设置行高
			for (int j = 0; j < title.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(getValueByDictAndKey("dc_shift", trList.get(i).getShift().toString())); break;
					case 1: cell.setCellValue(getValueByDictAndKey("dc_weather", trList.get(i).getWeather().toString()));	break;
					case 2: cell.setCellValue(getValueByDictAndKey("dc_dutyPerson", trList.get(i).getThisWatcher().toString()));	break;
					case 3:
						if(trList.get(i).getShift() == 3){
							cell.setCellValue(sdf.format(trList.get(i).getWatchTimeStart()) + "--次日" + sdf.format(trList.get(i).getWatchTimeEnd()));	break;
						}else{
							cell.setCellValue(sdf.format(trList.get(i).getWatchTimeStart()) + "--" + sdf.format(trList.get(i).getWatchTimeEnd()));	break;
						}
					case 4: cell.setCellValue(getValueByDictAndKey("dc_dutyPerson", trList.get(i).getLaseWatcher().toString()));	break;
					case 5: cell.setCellValue(sdf.format(trList.get(i).getHandoverTime()));	break;
					case 6: cell.setCellValue(trList.get(i).getHandoverMatters()); break;
					case 7: cell.setCellValue(trList.get(i).getException()); break;
				}
				//设置单元格样式
				if(j == 6){
					cell.setCellStyle(mainStyle_left);
				}else{
					cell.setCellStyle(mainStyle_center);
				}
			}
		}

		return wb;
	}


	/**
	 * @intruduction 获取监控巡检数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年4月2日
	 */
	public HSSFWorkbook getSurveillanceInspectionData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		TotalTable tt=this.totalTableDaoImpl.getEntityById(TotalTable.class, ttId);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		SurveillanceInspectionVo surveillanceInspectionVo = new SurveillanceInspectionVo();
		surveillanceInspectionVo.setTtId(ttId);
		List<SurveillanceInspection> siList = this.surveillanceInspectionServiceImpl.queryEntityList(surveillanceInspectionVo);

		//创建sheet
		HSSFSheet sheet3 = wb.createSheet("监控巡检");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet3.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		sheet3.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
		sheet3.addMergedRegion(new CellRangeAddress(2, 2, 0, 5));


		//创建行（第一行）
		HSSFRow row0 = sheet3.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		if(siList != null && siList.size() > 0){
			row0.createCell(0).setCellValue(siList.get(0).getTitle());
		}else{
			row0.createCell(0).setCellValue("监控巡检");
		}
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet3.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-03");
		row1.getCell(0).setCellStyle(r1_style);

		//第三行
		HSSFRow row2 = sheet3.createRow(2);
		row2.setHeightInPoints(25);
		if(siList != null && siList.size() > 0){
			row2.createCell(0).setCellValue("日期：" + sdf1.format(siList.get(0).getDutyDate()) + "         天气：" + getValueByDictAndKey("dc_weather", siList.get(0).getWeather().toString()));
		}else{
			row2.createCell(0).setCellValue("日期：" + sdf1.format(tt.getDutyDate()));
		}
		row2.getCell(0).setCellStyle(r1_style);


		//第四行
		HSSFRow row3 = sheet3.createRow(3);
		row3.setHeightInPoints(40);		//行高
		HSSFCell cell = null;
		String[] title = {"巡检起止时间段","值班主任","巡检位置","故障设备","巡检情况描述","跟进措施"};
		for(int i=0;i<title.length;i++){
			cell = row3.createCell(i);		//创建单元格
			cell.setCellValue(title[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式

			//列宽自适应（该方法在老版本的POI中效果不佳）
			/*sheet3.autoSizeColumn(i);*/
			//设置列宽
			if(i == 4){
				sheet3.setColumnWidth(i, sheet3.getColumnWidth(i)*7);
			}else if(i == 5){
				sheet3.setColumnWidth(i, sheet3.getColumnWidth(i)*4);
			}else{
				sheet3.setColumnWidth(i, sheet3.getColumnWidth(i)*3/2);
			}
		}

		//第五行 及 之后的行
		HSSFRow row;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int i = 0; i < siList.size(); i++) {
			row = sheet3.createRow(i + 4);	//创建行
			row.setHeightInPoints(60);					//设置行高
			for (int j = 0; j < title.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(sdf.format(siList.get(i).getInspectionTimeStart()) + "--" + sdf.format(siList.get(i).getInspectionTimeEnd()));	break;
					case 1: cell.setCellValue(getValueByDictAndKey("dc_headOfDuty", siList.get(i).getShiftSupervisor().toString()));	break;
					case 2: cell.setCellValue(getValueByDictAndKey("dc_inspectionlocation", siList.get(i).getInspectionlocation().toString()));	break;
					case 3:
						if(siList.get(i).getFailureEquipment() != null){
							cell.setCellValue(getValueByDictAndKey("dc_failureEquipment", siList.get(i).getFailureEquipment().toString()));
						}else{
							cell.setCellValue("");
						}
						break;
					case 4: cell.setCellValue(siList.get(i).getInspectionDetails());	break;
					case 5: cell.setCellValue(siList.get(i).getFollowMeasure());	break;
				}
				//设置单元格样式
				if(j == 4){
					cell.setCellStyle(mainStyle_left);
				}else{
					cell.setCellStyle(mainStyle_center);
				}
			}
		}

		return wb;
	}

	/**
	 * @intruduction 获取涉路施工数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月28日
	 */
	public HSSFWorkbook getRoadWorkData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		RoadWorkVo roadWorkVo = new RoadWorkVo();
		roadWorkVo.setTtId(ttId);
		List<RoadWork> rwList = this.roadWorkServiceImpl.queryEntityList(roadWorkVo);

		//创建sheet
		HSSFSheet sheet4 = wb.createSheet("涉路施工");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		//跨列
		sheet4.addMergedRegion(new CellRangeAddress(0, 0, 0, 13));
		sheet4.addMergedRegion(new CellRangeAddress(1, 1, 0, 13));
		sheet4.addMergedRegion(new CellRangeAddress(2, 2, 3, 4));
		sheet4.addMergedRegion(new CellRangeAddress(2, 2, 5, 8));
		sheet4.addMergedRegion(new CellRangeAddress(2, 2, 9, 12));
		//跨行
		sheet4.addMergedRegion(new CellRangeAddress(2, 3, 13, 13));
		sheet4.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
		sheet4.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
		sheet4.addMergedRegion(new CellRangeAddress(2, 3, 2, 2));


        //列宽自适应（该方法在老版本的POI中效果不佳）
        /*sheet4.autoSizeColumn(i);*/
        //设置列宽
        for (int i = 0; i < 14; i++) {
            if(i == 3 || i == 4 || i == 8){
                sheet4.setColumnWidth(i, sheet4.getColumnWidth(i)*3);
            }else if(i == 6){
                sheet4.setColumnWidth(i, sheet4.getColumnWidth(i)*5);
      	        }else if(i == 11){
                sheet4.setColumnWidth(i, sheet4.getColumnWidth(i)*6);
            }else{
                sheet4.setColumnWidth(i, sheet4.getColumnWidth(i)*3/2);
            }
        }


		//创建行（第一行）
		HSSFRow row0 = sheet4.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		if(rwList != null && rwList.size() > 0){
			row0.createCell(0).setCellValue(rwList.get(0).getTitle());
		}else{
			row0.createCell(0).setCellValue("涉路施工");
		}
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet4.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-04");
		row1.getCell(0).setCellStyle(r1_style);


		//第三行
		HSSFRow row2 = sheet4.createRow(2);
		row2.setHeightInPoints(30);		//行高
		HSSFCell cell = null;
		String[] title1 = {"日期","进场时间","撤场时间","施工单位","施工情况","现场安全检查情况","施工报备情况"};
		for(int i=0;i<title1.length;i++){
		    if(i == 4){
                cell = row2.createCell(5);		//创建单元格
            }else if(i == 5){
                cell = row2.createCell(9);		//创建单元格
            }else if(i == 6){
                cell = row2.createCell(13);	//创建单元格
            }else{
                cell = row2.createCell(i);		//创建单元格
            }
			cell.setCellValue(title1[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}
        row2.createCell(4).setCellStyle(r2_style);
        row2.createCell(6).setCellStyle(r2_style);
        row2.createCell(7).setCellStyle(r2_style);
        row2.createCell(8).setCellStyle(r2_style);
        row2.createCell(10).setCellStyle(r2_style);
        row2.createCell(11).setCellStyle(r2_style);
        row2.createCell(12).setCellStyle(r2_style);


		//第四行
		HSSFRow row3 = sheet4.createRow(3);
        row3.setHeightInPoints(30);		//行高
		String[] title2 = {"单位名称","现场负责人联系方式","位置属性","具体位置","施工内容","占道情况","检查时间","检查人员","施工现场情况简要描述","整改措施"};
        row3.createCell(0).setCellStyle(r2_style);
        row3.createCell(1).setCellStyle(r2_style);
		row3.createCell(2).setCellStyle(r2_style);
		row3.createCell(13).setCellStyle(r2_style);
		for(int i=0;i<title2.length;i++){
			cell = row3.createCell(i + 3);		//创建单元格
			cell.setCellValue(title2[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}


		//第五行 及 之后的行
		HSSFRow row = null;
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM月dd日");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		String[][] content = new String[rwList.size()][];
		for (int i = 0; i < rwList.size(); i++) {
			row = sheet4.createRow(i + 4);	//创建行
			row.setHeightInPoints(60);					//设置行高
			for (int j = 0; j < 14; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(sdf1.format(rwList.get(i).getDutyDate()));	break;
					case 1: cell.setCellValue(sdf2.format(rwList.get(i).getApproachTime()));	break;
					case 2: cell.setCellValue(sdf2.format(rwList.get(i).getDepartureTime()));	break;
					case 3: cell.setCellValue(getValueByDictAndKey("dc_ConstructionUnitName", rwList.get(i).getUnitName().toString()));	break;
					case 4: cell.setCellValue(rwList.get(i).getRelationPerson() + rwList.get(i).getRelationPhone());	break;
					case 5: 
						//cell.setCellValue(getValueByDictAndKey("dc_positionAttributes", rwList.get(i).getPositionAttributes().toString()));	break;
						if(StringUtils.isNotBlank(rwList.get(i).getPositionAttributes())){
							String[] paArr = rwList.get(i).getPositionAttributes().split(",");
							String paStr = "";
							for (int m = 0; m < paArr.length; m++) {
								paStr += getValueByDictAndKey("dc_positionAttributes", paArr[m]) + ",";
							}
							cell.setCellValue(paStr.substring(0, paStr.length()-1));
						}else{
							cell.setCellValue("");
						}
						break;
					case 6: cell.setCellValue(rwList.get(i).getSpecificLocation()); break;
					case 7: cell.setCellValue(rwList.get(i).getConstructionContent()); break;
					case 8: cell.setCellValue(rwList.get(i).getJeevesSituation()); break;
					case 9: cell.setCellValue(sdf2.format(rwList.get(i).getCheckTime()));	break;
					case 10: cell.setCellValue(getValueByDictAndKey("dc_Inspectors", rwList.get(i).getChecker().toString()));	break;
					case 11: cell.setCellValue(rwList.get(i).getDescription()); break;
					case 12: cell.setCellValue(rwList.get(i).getRectificationMeasures()); break;
					case 13: cell.setCellValue(rwList.get(i).getReportedSituation()); break;

				}
				//设置单元格样式
				if(j == 11){
					cell.setCellStyle(mainStyle_left);
				}else{
					cell.setCellStyle(mainStyle_center);
				}
			}
		}

		return wb;
	}


	/**
	 * @intruduction 获取设备运行情况统计表数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年4月3日
	 */
	public HSSFWorkbook getEquipmentOperationData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		EquipmentOperationVo equipmentOperationVo = new EquipmentOperationVo();
		equipmentOperationVo.setTtId(ttId);
		List<EquipmentOperation> eoList = this.equipmentOperationServiceImpl.queryEntityList(equipmentOperationVo);

		//创建sheet
		HSSFSheet sheet5 = wb.createSheet("设备运行情况统计表");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet5.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
		sheet5.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
		sheet5.addMergedRegion(new CellRangeAddress(2, 2, 0, 9));

		//列宽自适应（该方法在老版本的POI中效果不佳）
		/*sheet5.autoSizeColumn(i);*/
		//设置列宽
		for (int i = 0; i < 10; i++) {
			if(i == 8){
				sheet5.setColumnWidth(i, sheet5.getColumnWidth(i)*4);
			}else{
				sheet5.setColumnWidth(i, sheet5.getColumnWidth(i)*3/2);
			}
		}


		//创建行（第一行）
		HSSFRow row0 = sheet5.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		if(eoList != null && eoList.size() > 0){
			row0.createCell(0).setCellValue(eoList.get(0).getTitle());
		}else{
			row0.createCell(0).setCellValue("各站车道设备运行情况统计表");
		}
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet5.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-05");
		row1.getCell(0).setCellStyle(r1_style);

		//第三行
		HSSFRow row2 = sheet5.createRow(2);
		row2.setHeightInPoints(25);
		if(eoList != null && eoList.size() > 0){
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
			row2.createCell(0).setCellValue("日期：" + sdf1.format(eoList.get(0).getDutyDate()));
		}else{
			row2.createCell(0).setCellValue("日期：           ");
		}
		row2.getCell(0).setCellStyle(r1_style);


		//第四行
		HSSFRow row3 = sheet5.createRow(3);
		row3.setHeightInPoints(30);		//行高
		HSSFCell cell;
		String[] title = {"部门","车道高清抓拍","自动发卡机","MTC出口车道","ETC出口车道","MTC入口车道","ETC入口车道","计重车道","备注","车道停用时间段"};
		for(int i=0;i<title.length;i++){
			cell = row3.createCell(i);		//创建单元格
			cell.setCellValue(title[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}

       /* //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
        HSSFPatriarch drawingPatriarch = sheet5.createDrawingPatriarch();
        // anchor主要用于设置图片的属性
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 250, (short) 1, 1+i*10, (short) 5, 8+i*10);
        // 插入图片
        drawingPatriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));*/

        HSSFCellStyle greenStyle = wb.createCellStyle();
        HSSFCellStyle yellowStyle = wb.createCellStyle();
        HSSFCellStyle redStyle = wb.createCellStyle();
        greenStyle.cloneStyleFrom(mainStyle_center);
        yellowStyle.cloneStyleFrom(mainStyle_center);
        redStyle.cloneStyleFrom(mainStyle_center);

        HSSFFont greenFont = wb.createFont();
        HSSFFont yellowFont = wb.createFont();
        HSSFFont redFont = wb.createFont();
        greenFont.setColor((short)64);
        greenFont.setFontHeightInPoints((short)28);	//字体大小
        redFont.setColor(Font.COLOR_RED);
		redFont.setFontHeightInPoints((short)28);	//字体大小
        yellowFont.setColor((short)9);
		yellowFont.setFontHeightInPoints((short)28);	//字体大小

        greenStyle.setFont(greenFont);
        yellowStyle.setFont(yellowFont);
        redStyle.setFont(redFont);


        //第五行 及 之后的行
		HSSFRow row;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		for (int i = 0; i < eoList.size(); i++) {
			row = sheet5.createRow(i + 4);	//创建行
			row.setHeightInPoints(65);					//设置行高
			for (int j = 0; j < title.length; j++) {
				cell = row.createCell(j);				//创建单元格
				Integer value = 0;
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(getValueByDictAndKey("dc_tollGate", eoList.get(i).getTollGate().toString()));	break;
					case 1: setCell(cell,wb,mainStyle_center,eoList.get(i).getCdgqzp());  break;
					case 2: setCell(cell,wb,mainStyle_center,eoList.get(i).getZdfkj());  	break;
					case 3: setCell(cell,wb,mainStyle_center,eoList.get(i).getMtcckcd());  	break;
					case 4: setCell(cell,wb,mainStyle_center,eoList.get(i).getEtcckcd());  	break;
					case 5: setCell(cell,wb,mainStyle_center,eoList.get(i).getMtcrkcd());  	break;
					case 6: setCell(cell,wb,mainStyle_center,eoList.get(i).getEtcckcd());  	break;
					case 7: setCell(cell,wb,mainStyle_center,eoList.get(i).getJzcd());  	break;

					/*case 1:
                        cell.setCellValue("●");
                        value = eoList.get(i).getCdgqzp();
                        if(value == 1){
                            cell.setCellStyle(greenStyle);
                        }else if(value == 2){
                            cell.setCellStyle(yellowStyle);
                        }else{
                            cell.setCellStyle(redStyle);
                        }
                        break;
					case 2:
						cell.setCellValue("●");
						value = eoList.get(i).getZdfkj();
						if(value == 1){
							cell.setCellStyle(greenStyle);
						}else if(value == 2){
							cell.setCellStyle(yellowStyle);
						}else{
							cell.setCellStyle(redStyle);
						}
						break;
					case 3:
						cell.setCellValue("●");
						value = eoList.get(i).getMtcckcd();
						if(value == 1){
							cell.setCellStyle(greenStyle);
						}else if(value == 2){
							cell.setCellStyle(yellowStyle);
						}else{
							cell.setCellStyle(redStyle);
						}
						break;
					case 4:
						cell.setCellValue("●");
						value = eoList.get(i).getEtcckcd();
						if(value == 1){
							cell.setCellStyle(greenStyle);
						}else if(value == 2){
							cell.setCellStyle(yellowStyle);
						}else{
							cell.setCellStyle(redStyle);
						}
						break;
					case 5:
						cell.setCellValue("●");
						value = eoList.get(i).getMtcrkcd();
						if(value == 1){
							cell.setCellStyle(greenStyle);
						}else if(value == 2){
							cell.setCellStyle(yellowStyle);
						}else{
							cell.setCellStyle(redStyle);
						}
						break;
					case 6:
						cell.setCellValue("●");
						value = eoList.get(i).getEtcckcd();
						if(value == 1){
							cell.setCellStyle(greenStyle);
						}else if(value == 2){
							cell.setCellStyle(yellowStyle);
						}else{
							cell.setCellStyle(redStyle);
						}
						break;
					case 7:
						cell.setCellValue("●");
						value = eoList.get(i).getJzcd();
						if(value == 1){
							cell.setCellStyle(greenStyle);
						}else if(value == 2){
							cell.setCellStyle(yellowStyle);
						}else{
							cell.setCellStyle(redStyle);
						}
						break;*/
					case 8: cell.setCellValue(eoList.get(i).getRemark());	break;
					case 9:
						String d1 = "";
						String d2 = "";
						if(eoList.get(i).getDownTimeStart() != null){
							d1 = sdf.format(eoList.get(i).getDownTimeStart());
						}
						if(eoList.get(i).getDownTimeEnd() != null){
							d2 = sdf.format(eoList.get(i).getDownTimeEnd());
						}
						if(StringUtils.isNotBlank(d1) && StringUtils.isNotBlank(d2)){
							cell.setCellValue(d1 + "——" + d2);
						}else {
							cell.setCellValue("");
						}
						break;
				}
				//设置单元格样式
				if(j == 8) {
					cell.setCellStyle(mainStyle_left);
				}else if(j>0&&j<8){
					
				}else{
					cell.setCellStyle(mainStyle_center);
				}
			}
		}

		return wb;
	}
	
	private void setCell(HSSFCell cell,HSSFWorkbook wb,HSSFCellStyle mainStyle, int status){
		HSSFCellStyle style_ = wb.createCellStyle();
		if(status==1){
			//正常样式
			style_.cloneStyleFrom(mainStyle);
			style_.setAlignment(HorizontalAlignment.CENTER);
			HSSFFont r2_font = wb.createFont();
			r2_font.setBold(true);						//字体加粗
			r2_font.setColor(IndexedColors.GREEN.getIndex());
			r2_font.setFontHeightInPoints((short)20);	//字体大小
			style_.setFont(r2_font);
		}else if(status==2){
			//损坏但不影响样式
			style_.cloneStyleFrom(mainStyle);
			style_.setAlignment(HorizontalAlignment.CENTER);
			HSSFFont r2_font = wb.createFont();
			r2_font.setBold(true);						//字体加粗
			r2_font.setColor(IndexedColors.YELLOW.getIndex());
			r2_font.setFontHeightInPoints((short)20);	//字体大小
			style_.setFont(r2_font);
		}else if(status==3){
			//损坏样式
			style_.cloneStyleFrom(mainStyle);
			style_.setAlignment(HorizontalAlignment.CENTER);
			HSSFFont r2_font = wb.createFont();
			r2_font.setBold(true);						//字体加粗
			r2_font.setColor(IndexedColors.RED.getIndex());
			r2_font.setFontHeightInPoints((short)20);	//字体大小
			style_.setFont(r2_font);
		}
		cell.setCellValue("●");
		cell.setCellStyle(style_);
	}

	/**
	 * @intruduction 获取联网设备日常检查表数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook getEquipmentStatusData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		EquipmentStatusVo equipmentStatusVo = new EquipmentStatusVo();
		equipmentStatusVo.setTtId(ttId);
		List<EquipmentStatus> esList = this.equipmentStatusServiceImpl.queryEntityList(equipmentStatusVo);


		//创建sheet
		HSSFSheet sheet15 = wb.createSheet("联网设备日常检查表");

		//设置列宽
		for (int i = 0; i < 10; i++) {
			sheet15.setColumnWidth(i, sheet15.getColumnWidth(i)*2);
		}


		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet15.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
		sheet15.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
		sheet15.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));
		sheet15.addMergedRegion(new CellRangeAddress(2, 2, 5, 8));

		sheet15.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
		sheet15.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
		sheet15.addMergedRegion(new CellRangeAddress(2, 3, 9, 9));


		//创建行（第一行）
		HSSFRow row0 = sheet15.createRow(0 );
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		row0.createCell(0).setCellValue("联网关键设备运行状态日常检查表");
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);

		//第二行
		HSSFRow row1 = sheet15.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-06");
		row1.getCell(0).setCellStyle(r1_style);

		//第三行
		HSSFRow row2 = sheet15.createRow(2);
		row2.setHeightInPoints(25);
		row2.createCell(0).setCellValue("日期");
		row2.createCell(1);
		row2.createCell(2).setCellValue("RFID");
		row2.createCell(3);
		row2.createCell(4).setCellValue("5.8G");
		row2.createCell(5).setCellValue("高清卡口");
		for (int i = 6; i < 9; i++) {
			row2.createCell(i);
		}
		row2.createCell(9).setCellValue("备注");
		for (int i = 0; i < 10; i++) {
			row2.getCell(i).setCellStyle(r2_style);	//设置单元格样式
		}

		//第四行
		HSSFRow row3 = sheet15.createRow(3);
		row3.setHeightInPoints(25);
		row3.createCell(0);
		row3.createCell(1);
		row3.createCell(2).setCellValue("R1");
		row3.createCell(3).setCellValue("R2");
		row3.createCell(4).setCellValue("E1");
		row3.createCell(5).setCellValue("10306");
		row3.createCell(6).setCellValue("10307");
		row3.createCell(7).setCellValue("10308");
		row3.createCell(8).setCellValue("10309");
		row3.createCell(9);
		for (int i = 0; i < 10; i++) {
			row3.getCell(i).setCellStyle(r2_style);	//设置单元格样式
		}

		int esListRows=0;
		//数据展示
		if(esList != null && esList.size() > 0){
			esListRows=esList.size()*3;//占用了几行
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
			SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

			for(int tb = 0; tb < esList.size(); tb++){

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet15.addMergedRegion(new CellRangeAddress(4 + tb*3,  6 + tb*3, 0, 0));


				HSSFRow row4 = sheet15.createRow(4 + tb*3);
				row4.setHeightInPoints(25);
				row4.createCell(0).setCellValue(sdf1.format(esList.get(tb).getDutyDate()));
				row4.createCell(1);
				row4.createCell(2).setCellValue(getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusR1().toString()));
				row4.createCell(3).setCellValue(getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusR2().toString()));
				row4.createCell(4).setCellValue(getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusE1().toString()));
				row4.createCell(5).setCellValue(getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusA().toString()));
				row4.createCell(6).setCellValue(getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusB().toString()));
				row4.createCell(7).setCellValue(getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusC().toString()));
				row4.createCell(8).setCellValue(getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusD().toString()));
				row4.createCell(9).setCellValue(sdf2.format(esList.get(tb).getCheckTime()));
				for (int i = 0; i < 10; i++) {
					row4.getCell(i).setCellStyle(mainStyle_center);	//设置单元格样式
				}


				HSSFRow row5 = sheet15.createRow(5 + tb*3);
				row5.setHeightInPoints(25);
				row5.createCell(0);
				row5.createCell(1).setCellValue("标识成功率");
				row5.createCell(2).setCellValue(esList.get(tb).getSuccessRateR1() + "%");
				row5.createCell(3).setCellValue(esList.get(tb).getSuccessRateR2() + "%");
				row5.createCell(4).setCellValue(esList.get(tb).getSuccessRateE1() + "%");
				row5.createCell(5).setCellValue(esList.get(tb).getSuccessRateA() + "%");
				row5.createCell(6).setCellValue(esList.get(tb).getSuccessRateB() + "%");
				row5.createCell(7).setCellValue(esList.get(tb).getSuccessRateC() + "%");
				row5.createCell(8).setCellValue(esList.get(tb).getSuccessRateD() + "%");
				row5.createCell(9).setCellValue(esList.get(tb).getRemark());
				for (int i = 0; i < 10; i++) {
					row5.getCell(i).setCellStyle(mainStyle_center);	//设置单元格样式
				}


				HSSFRow row6 = sheet15.createRow(6 + tb*3);
				row6.setHeightInPoints(25);
				row6.createCell(0);
				row6.createCell(1).setCellValue("误标数量");
				row6.createCell(2).setCellValue(esList.get(tb).getMislabelNumR1());
				row6.createCell(3).setCellValue(esList.get(tb).getMislabelNumR2());
				row6.createCell(4).setCellValue(0);
				row6.createCell(5).setCellValue(0);
				row6.createCell(6).setCellValue(0);
				row6.createCell(7).setCellValue(0);
				row6.createCell(8).setCellValue(0);
				row6.createCell(9);
				for (int i = 0; i < 10; i++) {
					row6.getCell(i).setCellStyle(mainStyle_center);	//设置单元格样式
				}

			}

		}
		int LastRowIndex=esListRows+4;
        //合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet15.addMergedRegion(new CellRangeAddress(LastRowIndex,LastRowIndex,0,9));
        //创建行（最后一行）
        HSSFRow rowLast = sheet15.createRow(LastRowIndex);
        //设置行的高度
        rowLast.setHeightInPoints(50);
        //创建单元格 并 设置单元格内容
        rowLast.createCell(0).setCellValue("注：实时状态指每日早上在省营运平台进行登录。依次检查RFID、5.8G、高清卡口的运行状态情况。显示绿灯为正常运行，打√，显示红灯为设备故障，打×，需要向广交机及工程部进行设备报修，并在设备修复后记录修复时间。标识成功率指的是各联网关键设备在上一工班日的标识情况统计。误标数量指的是两个RFID标识点的疑似误标流水条数。");
        //设置单元格样式
        rowLast.getCell(0).setCellStyle(r2_style);	//设置单元格样式

		return wb;
	}

	/**
	 * @intruduction 获取营运数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月28日
	 */
	public HSSFWorkbook getOperatingData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		TotalTable tt=this.totalTableDaoImpl.getEntityById(TotalTable.class, ttId);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		OperatingDataVo operatingDataVo = new OperatingDataVo();
		operatingDataVo.setTtId(ttId);
		List<OperatingData> odList = this.operatingDataServiceImpl.queryEntityList(operatingDataVo);

		//创建sheet
		HSSFSheet sheet6 = wb.createSheet("各站营运数据");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet6.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		sheet6.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
		sheet6.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
		sheet6.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
		sheet6.addMergedRegion(new CellRangeAddress(2, 2, 2, 4));
		sheet6.addMergedRegion(new CellRangeAddress(2, 2, 5, 7));

		sheet6.addMergedRegion(new CellRangeAddress(odList.size() + 4, odList.size() + 4, 0, 1));

		for (int i = 0; i < 8; i++) {
			//列宽自适应（该方法在老版本的POI中效果不佳）
			/*sheet6.autoSizeColumn(i);*/
			//设置列宽
			sheet6.setColumnWidth(i, sheet6.getColumnWidth(i)*5/2);
		}


		//创建行（第一行）
		HSSFRow row0 = sheet6.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
//		if(odList != null && odList.size() > 0){
//			row0.createCell(0).setCellValue(odList.get(0).getTitle());
//		}else{
//			row0.createCell(0).setCellValue("各站营运数据");
//		}
		row0.createCell(0).setCellValue(sdf1.format(tt.getDutyDate())+"各站拆分前营运数据");
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet6.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-07");
		row1.getCell(0).setCellStyle(r1_style);


		//第三行
		HSSFRow row2 = sheet6.createRow(2);
		row2.setHeightInPoints(30);		//行高
		HSSFCell cell = null;
		String[] title1 = {"序号","收费站","出口车流量","收费额"};
		row2.createCell(3).setCellStyle(r2_style);
		row2.createCell(4).setCellStyle(r2_style);
        row2.createCell(6).setCellStyle(r2_style);
		row2.createCell(7).setCellStyle(r2_style);
		for(int i=0;i<title1.length;i++){
		    if(i == 3){
                cell = row2.createCell(5);		//创建单元格
            }else{
                cell = row2.createCell(i);		//创建单元格
            }
			cell.setCellValue(title1[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}

		//第四行
		HSSFRow row3 = sheet6.createRow(3);
		row3.setHeightInPoints(30);		//行高
		String[] title2 = {"总车流","其中粤通卡车流","移动支付车流","总收费额","其中粤通卡收入","移动支付收入"};
        row3.createCell(0).setCellStyle(r2_style);
        for(int i=0;i<title2.length;i++){
			cell = row3.createCell(i + 2);		//创建单元格
			cell.setCellValue(title2[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}


		//第五行 及 之后的行
		HSSFRow row;
		Integer hj_totalTraffic = 0;
		Integer hj_ytkTraffic = 0;
		Integer hj_mobilePaymentTraffic = 0;
		Double hj_generalIncome = 0.0;
		Double hj_ytkIncome = 0.0;
		Double hj_mobilePaymentIncome = 0.0;
		for (int i = 0; i < odList.size(); i++) {
			row = sheet6.createRow(i + 4);	//创建行
			row.setHeightInPoints(26);					//设置行高
			for (int j = 0; j < 8; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0: cell.setCellValue(i+1);	break;
					case 1: cell.setCellValue(getValueByDictAndKey("dc_tollGate_operation", odList.get(i).getTollGate().toString()));	break;
					case 2: cell.setCellValue(odList.get(i).getTotalTraffic());	break;
					case 3: cell.setCellValue(odList.get(i).getYtkTraffic());	break;
					case 4:
						if(odList.get(i).getMobilePaymentTraffic() != null){
							cell.setCellValue(odList.get(i).getMobilePaymentTraffic());
						}else{
							cell.setCellValue("");
						}
						break;
					case 5: cell.setCellValue(odList.get(i).getGeneralIncome());	break;
					case 6: cell.setCellValue(odList.get(i).getYtkIncome());	break;
					case 7:
						if(odList.get(i).getMobilePaymentIncome() != null){
							cell.setCellValue(odList.get(i).getMobilePaymentIncome());
						}else{
							cell.setCellValue("");
						}
						break;
				}
				//设置单元格样式
				cell.setCellStyle(mainStyle_center);
			}
            hj_totalTraffic += odList.get(i).getTotalTraffic();
            hj_ytkTraffic += odList.get(i).getYtkTraffic();
            if(odList.get(i).getMobilePaymentTraffic() != null){
				hj_mobilePaymentTraffic += odList.get(i).getMobilePaymentTraffic();
			}
            hj_generalIncome += odList.get(i).getGeneralIncome();
            hj_ytkIncome += odList.get(i).getYtkIncome();
            if(odList.get(i).getMobilePaymentIncome() != null){
				hj_mobilePaymentIncome += odList.get(i).getMobilePaymentIncome();
			}
		}

		//最后一行
		row = sheet6.createRow( odList.size() + 4);
        row.setHeightInPoints(30);
		row.createCell(0).setCellValue("合计");
		row.getCell(0).setCellStyle(r2_style);
		row.createCell(1).setCellStyle(r2_style);
		row.createCell(2).setCellValue(hj_totalTraffic);
		row.getCell(2).setCellStyle(mainStyle_center);
		row.createCell(3).setCellValue(hj_ytkTraffic);
		row.getCell(3).setCellStyle(mainStyle_center);
		row.createCell(4).setCellValue(hj_mobilePaymentTraffic);
		row.getCell(4).setCellStyle(mainStyle_center);
		row.createCell(5).setCellValue(hj_generalIncome);
		row.getCell(5).setCellStyle(mainStyle_center);
		row.createCell(6).setCellValue(hj_ytkIncome);
		row.getCell(6).setCellStyle(mainStyle_center);
		row.createCell(7).setCellValue(hj_mobilePaymentIncome);
		row.getCell(7).setCellStyle(mainStyle_center);

		return wb;
	}


	/**
	 * @intruduction 获取拯救作业数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月28日
	 */
	public HSSFWorkbook getRescueWorkData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		RescueWorkVo rescueWorkVo = new RescueWorkVo();
		rescueWorkVo.setTtId(ttId);
		List<RescueWork> rwList = this.rescueWorkServiceImpl.queryEntityList(rescueWorkVo);

		//创建sheet
		HSSFSheet sheet7 = wb.createSheet("拯救作业");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet7.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
		sheet7.addMergedRegion(new CellRangeAddress(1, 1, 0, 14));
		sheet7.addMergedRegion(new CellRangeAddress(rwList.size() + 3, rwList.size() + 3, 0, 14));

		//列宽自适应（该方法在老版本的POI中效果不佳）
		/*sheet7.autoSizeColumn(i);*/
		//设置列宽
		for (int i = 0; i < 15; i++) {
			sheet7.setColumnWidth(i, sheet7.getColumnWidth(i)*2);
		}


		//创建行（第一行）
		HSSFRow row0 = sheet7.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		if(rwList != null && rwList.size() > 0){
			row0.createCell(0).setCellValue(rwList.get(0).getTitle());
		}else{
			row0.createCell(0).setCellValue("拯救作业");
		}
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet7.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-08");
		row1.getCell(0).setCellStyle(r1_style);


		//第三行
		HSSFRow row2 = sheet7.createRow(2);
		row2.setHeightInPoints(30);		//行高
		HSSFCell cell;
		String[] title = {"日期","接报时间","到达时间","到场用时","清场时间","地点","故障车","车型","缴费单号","拯救费","拖车里程","车辆去向","拯救车","司机电话","备注"};
		for(int i=0;i<title.length;i++){
			cell = row2.createCell(i);		//创建单元格
			cell.setCellValue(title[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}

		//第四行 及 之后的行
		HSSFRow row;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		for (int i = 0; i < rwList.size(); i++) {
			row = sheet7.createRow(i + 3);	//创建行
			row.setHeightInPoints(60);					//设置行高
			for (int j = 0; j < title.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(sdf1.format(rwList.get(i).getDutyDate()));	break;
					case 1: cell.setCellValue(sdf2.format(rwList.get(i).getReceiptTime()));	break;
					case 2: cell.setCellValue(sdf2.format(rwList.get(i).getArrivalTime()));	break;
					case 3: cell.setCellValue(rwList.get(i).getUsedTime());		break;
					case 4: cell.setCellValue(sdf2.format(rwList.get(i).getEvacuationTime()));	break;
					case 5: cell.setCellValue(rwList.get(i).getSite());		break;
					case 6: cell.setCellValue(rwList.get(i).getFaultPlates());		break;
					case 7: cell.setCellValue(getValueByDictAndKey("dc_carType", rwList.get(i).getCarType().toString()));		break;
					case 8: cell.setCellValue(rwList.get(i).getPaymentOrder());		break;
					case 9: cell.setCellValue(rwList.get(i).getRescueCharge());		break;
					case 10: cell.setCellValue(rwList.get(i).getTrailerMileage());		break;
					case 11: cell.setCellValue(getValueByDictAndKey("dc_whereabouts", rwList.get(i).getWhereabouts().toString()));		break;
					case 12: cell.setCellValue(rwList.get(i).getRescuePlates());		break;
					case 13: cell.setCellValue(rwList.get(i).getDriverPhone());		break;
					case 14: cell.setCellValue(rwList.get(i).getRemark());		break;
				}

				//设置单元格样式
				cell.setCellStyle(mainStyle_center);
			}
		}

		//最后一行
		row = sheet7.createRow( rwList.size() + 3);
		row.setHeightInPoints(40);		//行高
		row.createCell(0).setCellValue("备注：对于每宗拯救作业中心接报后，均已向车主说明情况，我司目前没有相关拯救队伍，拖车服务委托业务实力较好的广州交投汽车援救服务有限公司实施，故障车可根据实际情况拖到司机指定位置，并提醒车主拖车里程根据广东省物价局计价标准付费（并向其说明收费标准），如有异议可拨打我司服务电话进行投诉，我司会帮司机跟进，并提醒其在高速路面必须注意安全，摆放有效安全区等候救援。");
        HSSFCellStyle rwStyle = wb.createCellStyle();
        rwStyle.cloneStyleFrom(mainStyle_left);
		HSSFFont r2_font = wb.createFont();
		r2_font.setBold(true);
        rwStyle.setFont(r2_font);
		row.getCell(0).setCellStyle(rwStyle);
        for (int i = 1; i < 15; i++) {
            row.createCell(i).setCellStyle(r2_style);
        }

		return wb;
	}


	/**
	 * @intruduction 获取清障保洁数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook getClearingData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		TotalTable tt=this.totalTableDaoImpl.getEntityById(TotalTable.class, ttId);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		
		ClearingVo clearingVo = new ClearingVo();
		clearingVo.setTtId(ttId);
		List<Clearing> cList = this.clearingServiceImpl.queryEntityList(clearingVo);


		//创建sheet
		HSSFSheet sheet8 = wb.createSheet("清障保洁");

		//设置列宽
		for (int i = 0; i < 8; i++) {
			if(i == 7){
				sheet8.setColumnWidth(i, sheet8.getColumnWidth(i)*5/2);
			}else{
				sheet8.setColumnWidth(i, sheet8.getColumnWidth(i)*2);
			}
		}


		if(cList != null && cList.size() > 0){
			for(int tb = 0; tb < cList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet8.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 7));
				sheet8.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 7));
				sheet8.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 7));
				sheet8.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 1, 3));
				sheet8.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 5, 7));
				sheet8.addMergedRegion(new CellRangeAddress(5 + tb*10, 5 + tb*10, 1, 7));
				sheet8.addMergedRegion(new CellRangeAddress(6 + tb*10, 6 + tb*10, 1, 7));
				sheet8.addMergedRegion(new CellRangeAddress(7 + tb*10, 7 + tb*10, 1, 7));


				//创建行（第一行）
				HSSFRow row0 = sheet8.createRow(0 + tb*10);
				//设置行的高度
				row0.setHeightInPoints(30);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(cList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet8.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-09");
				row1.getCell(0).setCellStyle(r1_style);

				
				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

				//第三行
				HSSFRow row2 = sheet8.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + sdf1.format(cList.get(tb).getDutyDate()));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet8.createRow(3 + tb*10);
				row3.setHeightInPoints(40);
				row3.createCell(0).setCellValue("接报时间");
				row3.getCell(0).setCellStyle(r2_style);
				row3.createCell(1).setCellValue(sdf2.format(cList.get(tb).getReceiptTime()));
				row3.getCell(1).setCellStyle(mainStyle_center);
				row3.createCell(2).setCellValue("报告部门");
				row3.getCell(2).setCellStyle(r2_style);
				row3.createCell(3).setCellValue(getValueByDictAndKey("dc_reportingDepartment", cList.get(tb).getReportedDp().toString()));
				row3.getCell(3).setCellStyle(mainStyle_center);
				row3.createCell(4).setCellValue("报告人员");
				row3.getCell(4).setCellStyle(r2_style);
				row3.createCell(5).setCellValue(getValueByDictAndKey("dc_reportingPerson", cList.get(tb).getReportedPerson().toString()));
				row3.getCell(5).setCellStyle(mainStyle_center);
				row3.createCell(6).setCellValue("报告方式");
				row3.getCell(6).setCellStyle(r2_style);
				row3.createCell(7).setCellValue(getValueByDictAndKey("dc_reportedWay", cList.get(tb).getReportedWay().toString()));
				row3.getCell(7).setCellStyle(mainStyle_center);

				//第五行
				HSSFRow row4 = sheet8.createRow(4 + tb*10);
				row4.setHeightInPoints(40);
				row4.createCell(0).setCellValue("通行路段");
				row4.getCell(0).setCellStyle(r2_style);
				row4.createCell(1).setCellValue(cList.get(tb).getTrafficRoad());
				row4.getCell(1).setCellStyle(mainStyle_center);
				row4.createCell(4).setCellValue("通知处理部门");
				row4.getCell(4).setCellStyle(r2_style);
				row4.createCell(5).setCellValue(getValueByDictAndKey("dc_NotificationDepartment", cList.get(tb).getProcessingDp().toString()));
				
				row4.getCell(5).setCellStyle(mainStyle_center);
                for (int i = 2; i < 8; i++) {
                    if(i != 4 && i!= 5){
                        row4.createCell(i).setCellStyle(r2_style);
                    }
                }

				//第六
				HSSFRow row5 = sheet8.createRow(5 + tb*10);
				row5.setHeightInPoints(50);
				row5.createCell(0).setCellValue("情况简述");
				row5.getCell(0).setCellStyle(r2_style);
				row5.createCell(1).setCellValue(cList.get(tb).getBriefIntroduction());
				row5.getCell(1).setCellStyle(mainStyle_left);
                for (int i = 2; i < 8; i++) {
                    row5.createCell(i).setCellStyle(r2_style);
                }

				//第七行
				HSSFRow row6 = sheet8.createRow(6 + tb*10);
				row6.setHeightInPoints(50);
				row6.createCell(0).setCellValue("处理结果");
				row6.getCell(0).setCellStyle(r2_style);
				row6.createCell(1).setCellValue(cList.get(tb).getResult());
				row6.getCell(1).setCellStyle(mainStyle_left);
                for (int i = 2; i < 8; i++) {
                    row6.createCell(i).setCellStyle(r2_style);
                }

				//第八行
				HSSFRow row7 = sheet8.createRow(7 + tb*10);
				row7.setHeightInPoints(50);
				row7.createCell(0).setCellValue("备注");
				row7.getCell(0).setCellStyle(r2_style);
				row7.createCell(1).setCellValue(cList.get(tb).getRemark());
				row7.getCell(1).setCellStyle(mainStyle_left);
                for (int i = 2; i < 8; i++) {
                    row7.createCell(i).setCellStyle(r2_style);
                }

			}
		}else{
			//空表

			//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet8.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			sheet8.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
			sheet8.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet8.addMergedRegion(new CellRangeAddress(4, 4, 1, 3));
			sheet8.addMergedRegion(new CellRangeAddress(4, 4, 5, 7));
			sheet8.addMergedRegion(new CellRangeAddress(5, 5, 1, 7));
			sheet8.addMergedRegion(new CellRangeAddress(6, 6, 1, 7));
			sheet8.addMergedRegion(new CellRangeAddress(7, 7, 1, 7));


			//创建行（第一行）
			HSSFRow row0 = sheet8.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("清障保洁");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet8.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-09");
			row1.getCell(0).setCellStyle(r1_style);

			//第三行
			HSSFRow row2 = sheet8.createRow(2);
			row2.setHeightInPoints(25);
			row2.createCell(0).setCellValue("日期：" + sdf1.format(tt.getDutyDate()));
			row2.getCell(0).setCellStyle(r1_style);

			//第四行
			HSSFRow row3 = sheet8.createRow(3);
			row3.setHeightInPoints(40);
			row3.createCell(0).setCellValue("接报时间");
			row3.getCell(0).setCellStyle(r2_style);
			row3.createCell(1).setCellStyle(r2_style);
			row3.createCell(2).setCellValue("报告部门");
			row3.getCell(2).setCellStyle(r2_style);
			row3.createCell(3).setCellStyle(r2_style);
			row3.createCell(4).setCellValue("报告人员");
			row3.getCell(4).setCellStyle(r2_style);
			row3.createCell(5).setCellStyle(r2_style);
			row3.createCell(6).setCellValue("报告方式");
			row3.getCell(6).setCellStyle(r2_style);
			row3.createCell(7).setCellStyle(r2_style);

			//第五行
			HSSFRow row4 = sheet8.createRow(4);
			row4.setHeightInPoints(50);
			row4.createCell(0).setCellValue("通行路段");
			row4.getCell(0).setCellStyle(r2_style);
			row4.createCell(4).setCellValue("通知处理部门");
			row4.getCell(4).setCellStyle(r2_style);
            for (int i = 1; i < 8; i++) {
                if(i != 4){
                    row4.createCell(i).setCellStyle(r2_style);
                }
            }

			//第六
			HSSFRow row5 = sheet8.createRow(5);
			row5.setHeightInPoints(50);
			row5.createCell(0).setCellValue("情况简述");
			row5.getCell(0).setCellStyle(r2_style);
            for (int i = 1; i < 8; i++) {
                row5.createCell(i).setCellStyle(r2_style);
            }

			//第七行
			HSSFRow row6 = sheet8.createRow(6);
			row6.setHeightInPoints(50);
			row6.createCell(0).setCellValue("处理结果");
			row6.getCell(0).setCellStyle(r2_style);
            for (int i = 1; i < 8; i++) {
                row6.createCell(i).setCellStyle(r2_style);
            }

			//第八行
			HSSFRow row7 = sheet8.createRow(7);
			row7.setHeightInPoints(50);
			row7.createCell(0).setCellValue("备注");
			row7.getCell(0).setCellStyle(r2_style);
            for (int i = 1; i < 8; i++) {
                row7.createCell(i).setCellStyle(r2_style);
            }
		}

		return wb;
	}


	/**
	 * @intruduction 获取营运异常记录数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook getExceptionRecordData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		TotalTable tt=this.totalTableDaoImpl.getEntityById(TotalTable.class, ttId);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		ExceptionRecordVo exceptionRecordVo = new ExceptionRecordVo();
		exceptionRecordVo.setTtId(ttId);
		List<ExceptionRecord> erList = this.exceptionRecordServiceImpl.queryEntityList(exceptionRecordVo);


		//创建sheet
		HSSFSheet sheet9 = wb.createSheet("营运异常记录");

		//设置列宽
		for (int i = 0; i < 8; i++) {
			if(i == 7){
				sheet9.setColumnWidth(i, sheet9.getColumnWidth(i)*5/2);
			}else{
				sheet9.setColumnWidth(i, sheet9.getColumnWidth(i)*2);
			}
		}


		if(erList != null && erList.size() > 0){
			for(int tb = 0; tb < erList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet9.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 7));
				sheet9.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 7));
				sheet9.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 7));
				if(erList.get(tb).getExceptionType() == 1){
					sheet9.addMergedRegion(new CellRangeAddress(3 + tb*10, 3 + tb*10, 1, 3));
					sheet9.addMergedRegion(new CellRangeAddress(3 + tb*10, 3 + tb*10, 5, 7));
				}
				sheet9.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 1, 3));
				sheet9.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 5, 7));
				sheet9.addMergedRegion(new CellRangeAddress(5 + tb*10, 5 + tb*10, 1, 7));
				sheet9.addMergedRegion(new CellRangeAddress(6 + tb*10, 6 + tb*10, 1, 7));
				sheet9.addMergedRegion(new CellRangeAddress(7 + tb*10, 7 + tb*10, 1, 7));


				//创建行（第一行）
				HSSFRow row0 = sheet9.createRow(0 + tb*10);
				//设置行的高度
				row0.setHeightInPoints(30);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(erList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet9.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-10");
				row1.getCell(0).setCellStyle(r1_style);

				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

				//第三行
				HSSFRow row2 = sheet9.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + sdf1.format(erList.get(tb).getDutyDate()));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet9.createRow(3 + tb*10);
				if(erList.get(tb).getExceptionType() == 1){
					row3.setHeightInPoints(40);
					row3.createCell(0).setCellValue("报告部门");
					row3.getCell(0).setCellStyle(r2_style);
					row3.createCell(1).setCellValue(getValueByDictAndKey("dc_reportingDepartment", erList.get(tb).getReportedDp().toString()));
					row3.getCell(1).setCellStyle(mainStyle_center);
					row3.createCell(4).setCellValue("报告人员");
					row3.getCell(4).setCellStyle(r2_style);
					row3.createCell(5).setCellValue(getValueByDictAndKey("dc_reportingPerson", erList.get(tb).getReportedPerson().toString()));
					row3.getCell(5).setCellStyle(mainStyle_center);
					for (int i = 2; i < 8; i++) {
						if(i != 4 && i!= 5){
							row3.createCell(i).setCellStyle(r2_style);
						}
					}
				}else{
					row3.setHeightInPoints(40);
					row3.createCell(0).setCellValue("接报时间");
					row3.getCell(0).setCellStyle(r2_style);
					row3.createCell(1).setCellValue(sdf2.format(erList.get(tb).getReceiptTime()));
					row3.getCell(1).setCellStyle(mainStyle_center);
					row3.createCell(2).setCellValue("报告部门");
					row3.getCell(2).setCellStyle(r2_style);
					row3.createCell(3).setCellValue(getValueByDictAndKey("dc_reportingDepartment", erList.get(tb).getReportedDp().toString()));
					row3.getCell(3).setCellStyle(mainStyle_center);
					row3.createCell(4).setCellValue("报告人员");
					row3.getCell(4).setCellStyle(r2_style);
					row3.createCell(5).setCellValue(getValueByDictAndKey("dc_reportingPerson", erList.get(tb).getReportedPerson().toString()));
					row3.getCell(5).setCellStyle(mainStyle_center);
					row3.createCell(6).setCellValue("报告方式");
					row3.getCell(6).setCellStyle(r2_style);
					row3.createCell(7).setCellValue(getValueByDictAndKey("dc_reportedWay_ER", erList.get(tb).getReportedWay().toString()));
					row3.getCell(7).setCellStyle(mainStyle_center);
				}


				//第五行
				HSSFRow row4 = sheet9.createRow(4 + tb*10);
				row4.setHeightInPoints(40);
				row4.createCell(0).setCellValue("通行路段");
				row4.getCell(0).setCellStyle(r2_style);
				row4.createCell(1).setCellValue(erList.get(tb).getTrafficRoad());
				row4.getCell(1).setCellStyle(mainStyle_center);
				row4.createCell(4).setCellValue("通知处理部门");
				row4.getCell(4).setCellStyle(r2_style);
				row4.createCell(5).setCellValue(getValueByDictAndKey("dc_NotificationDepartment", erList.get(tb).getProcessingDp().toString()));
				row4.getCell(5).setCellStyle(mainStyle_center);
				for (int i = 2; i < 8; i++) {
					if(i != 4 && i!= 5){
						row4.createCell(i).setCellStyle(r2_style);
					}
				}

				//第六
				HSSFRow row5 = sheet9.createRow(5 + tb*10);
				row5.setHeightInPoints(50);
				row5.createCell(0).setCellValue("情况简述");
				row5.getCell(0).setCellStyle(r2_style);
				row5.createCell(1).setCellValue(erList.get(tb).getBriefIntroduction());
				row5.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row5.createCell(i).setCellStyle(r2_style);
				}

				//第七行
				HSSFRow row6 = sheet9.createRow(6 + tb*10);
				row6.setHeightInPoints(50);
				row6.createCell(0).setCellValue("处理结果");
				row6.getCell(0).setCellStyle(r2_style);
				row6.createCell(1).setCellValue(erList.get(tb).getResult());
				row6.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row6.createCell(i).setCellStyle(r2_style);
				}

				//第八行
				HSSFRow row7 = sheet9.createRow(7 + tb*10);
				row7.setHeightInPoints(50);
				row7.createCell(0).setCellValue("备注");
				row7.getCell(0).setCellStyle(r2_style);
				row7.createCell(1).setCellValue(erList.get(tb).getRemark());
				row7.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row7.createCell(i).setCellStyle(r2_style);
				}

			}
		}else{		//空表

			//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet9.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			sheet9.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
			sheet9.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet9.addMergedRegion(new CellRangeAddress(4, 4, 1, 3));
			sheet9.addMergedRegion(new CellRangeAddress(4, 4, 5, 7));
			sheet9.addMergedRegion(new CellRangeAddress(5, 5, 1, 7));
			sheet9.addMergedRegion(new CellRangeAddress(6, 6, 1, 7));
			sheet9.addMergedRegion(new CellRangeAddress(7, 7, 1, 7));


			//创建行（第一行）
			HSSFRow row0 = sheet9.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("异常记录");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet9.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-10");
			row1.getCell(0).setCellStyle(r1_style);

			//第三行
			HSSFRow row2 = sheet9.createRow(2);
			row2.setHeightInPoints(25);
			row2.createCell(0).setCellValue("日期：" + sdf1.format(tt.getDutyDate()));
			row2.getCell(0).setCellStyle(r1_style);

			//第四行
			HSSFRow row3 = sheet9.createRow(3);
			row3.setHeightInPoints(40);
			row3.createCell(0).setCellValue("接报时间");
			row3.getCell(0).setCellStyle(r2_style);
			row3.createCell(1).setCellStyle(r2_style);
			row3.createCell(2).setCellValue("报告部门");
			row3.getCell(2).setCellStyle(r2_style);
			row3.createCell(3).setCellStyle(r2_style);
			row3.createCell(4).setCellValue("报告人员");
			row3.getCell(4).setCellStyle(r2_style);
			row3.createCell(5).setCellStyle(r2_style);
			row3.createCell(6).setCellValue("报告方式");
			row3.getCell(6).setCellStyle(r2_style);
			row3.createCell(7).setCellStyle(r2_style);

			//第五行
			HSSFRow row4 = sheet9.createRow(4);
			row4.setHeightInPoints(50);
			row4.createCell(0).setCellValue("通行路段");
			row4.getCell(0).setCellStyle(r2_style);
			row4.createCell(4).setCellValue("通知处理部门");
			row4.getCell(4).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				if(i != 4){
					row4.createCell(i).setCellStyle(r2_style);
				}
			}

			//第六
			HSSFRow row5 = sheet9.createRow(5);
			row5.setHeightInPoints(50);
			row5.createCell(0).setCellValue("情况简述");
			row5.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row5.createCell(i).setCellStyle(r2_style);
			}

			//第七行
			HSSFRow row6 = sheet9.createRow(6);
			row6.setHeightInPoints(50);
			row6.createCell(0).setCellValue("处理结果");
			row6.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row6.createCell(i).setCellStyle(r2_style);
			}

			//第八行
			HSSFRow row7 = sheet9.createRow(7);
			row7.setHeightInPoints(50);
			row7.createCell(0).setCellValue("备注");
			row7.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row7.createCell(i).setCellStyle(r2_style);
			}
		}

		return wb;
	}


	/**
	 * @intruduction 获取交通事故数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook getTrafficAccidentData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		TotalTable tt=this.totalTableDaoImpl.getEntityById(TotalTable.class, ttId);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		TrafficAccidentVo trafficAccidentVo = new TrafficAccidentVo();
		trafficAccidentVo.setTtId(ttId);
		List<TrafficAccident> taList = this.trafficAccidentServiceImpl.queryEntityList(trafficAccidentVo);


		//创建sheet
		HSSFSheet sheet10 = wb.createSheet("交通事故");

		//设置列宽
		for (int i = 0; i < 8; i++) {
			sheet10.setColumnWidth(i, sheet10.getColumnWidth(i)*2);
		}


		if(taList != null && taList.size() > 0){
			for(int tb = 0; tb < taList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet10.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 7));
				sheet10.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 7));
				sheet10.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 7));
				sheet10.addMergedRegion(new CellRangeAddress(7 + tb*10, 7 + tb*10, 0, 7));
				sheet10.addMergedRegion(new CellRangeAddress(8 + tb*10, 8 + tb*10, 0, 7));


				//创建行（第一行）
				HSSFRow row0 = sheet10.createRow(0 + tb*10);
				//设置行的高度
				row0.setHeightInPoints(30);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(taList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet10.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-11");
				row1.getCell(0).setCellStyle(r1_style);

				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

				//第三行
				HSSFRow row2 = sheet10.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + sdf1.format(taList.get(tb).getDutyDate()));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet10.createRow(3 + tb*10);
				row3.setHeightInPoints(40);
				row3.createCell(0).setCellValue("天气情况");
				row3.getCell(0).setCellStyle(r2_style);
				row3.createCell(1).setCellValue(getValueByDictAndKey("dc_weather", taList.get(tb).getWeather().toString()));
				row3.getCell(1).setCellStyle(mainStyle_center);
				row3.createCell(2).setCellValue("接报时间");
				row3.getCell(2).setCellStyle(r2_style);
				row3.createCell(3).setCellValue(sdf2.format(taList.get(0).getReceiptTime()));
				row3.getCell(3).setCellStyle(mainStyle_center);
				row3.createCell(4).setCellValue("接报方式");
				row3.getCell(4).setCellStyle(r2_style);
				row3.createCell(5).setCellValue(getValueByDictAndKey("dc_receiptWay", taList.get(tb).getReceiptWay().toString()));
				row3.getCell(5).setCellStyle(mainStyle_center);
				row3.createCell(6).setCellValue("消息来源");
				row3.getCell(6).setCellStyle(r2_style);
				row3.createCell(7).setCellValue(getValueByDictAndKey("dc_source", taList.get(tb).getSource().toString()));
				row3.getCell(7).setCellStyle(mainStyle_center);

				//第五行
				HSSFRow row4 = sheet10.createRow(4 + tb*10);
				row4.setHeightInPoints(40);
				row4.createCell(0).setCellValue("事故地点");
				row4.getCell(0).setCellStyle(r2_style);
				row4.createCell(1).setCellValue(taList.get(tb).getAccidentSite());
				row4.getCell(1).setCellStyle(mainStyle_center);
				row4.createCell(2).setCellValue("事故类型");
				row4.getCell(2).setCellStyle(r2_style);
				row4.createCell(3).setCellValue(getValueByDictAndKey("dc_accidentType", taList.get(tb).getAccidentType().toString()));
				row4.getCell(3).setCellStyle(mainStyle_center);
				row4.createCell(4).setCellValue("车辆类型");
				row4.getCell(4).setCellStyle(r2_style);
				if(StringUtils.isNotBlank(taList.get(tb).getCarType())){
					String[] carTypeArr = taList.get(tb).getCarType().split(",");
					String carTypeStr = "";
					for (int i = 0; i < carTypeArr.length; i++) {
						carTypeStr += getValueByDictAndKey("dc_carType", carTypeArr[i]) + ",";
					}
					row4.createCell(5).setCellValue(carTypeStr.substring(0, carTypeStr.length()-1));
				}else{
					row4.createCell(5).setCellValue("");
				}
				row4.getCell(5).setCellStyle(mainStyle_center);
				row4.createCell(6).setCellValue("涉及车辆");
				row4.getCell(6).setCellStyle(r2_style);
				row4.createCell(7).setCellValue(taList.get(tb).getInvolveCarNum());
				row4.getCell(7).setCellStyle(mainStyle_center);

				//第六
				HSSFRow row5 = sheet10.createRow(5 + tb*10);
				row5.setHeightInPoints(40);
				row5.createCell(0).setCellValue("涉事车牌");
				row5.getCell(0).setCellStyle(r2_style);
				row5.createCell(1).setCellValue(taList.get(tb).getInvolvePlates());
				row5.getCell(1).setCellStyle(mainStyle_center);
				row5.createCell(2).setCellValue("轻伤人数");
				row5.getCell(2).setCellStyle(r2_style);
				row5.createCell(3).setCellValue(taList.get(tb).getMinorInjuryNum());
				row5.getCell(3).setCellStyle(mainStyle_center);
				row5.createCell(4).setCellValue("重伤人数");
				row5.getCell(4).setCellStyle(r2_style);
				row5.createCell(5).setCellValue(taList.get(tb).getMinorInjuryNum());
				row5.getCell(5).setCellStyle(mainStyle_center);
				row5.createCell(6).setCellValue("死亡人数");
				row5.getCell(6).setCellStyle(r2_style);
				row5.createCell(7).setCellValue(taList.get(tb).getDeathNum());
				row5.getCell(7).setCellStyle(mainStyle_center);

				//第七行
				HSSFRow row6 = sheet10.createRow(6 + tb*10);
				row6.setHeightInPoints(40);
				row6.createCell(0).setCellValue("封闭车道");
				row6.getCell(0).setCellStyle(r2_style);
				row6.createCell(1).setCellValue(taList.get(tb).getLaneClosedNum());
				row6.getCell(1).setCellStyle(mainStyle_center);
				row6.createCell(2).setCellValue("路产损失");
				row6.getCell(2).setCellStyle(r2_style);
				row6.createCell(3).setCellValue(taList.get(tb).getRoadLoss());
				row6.getCell(3).setCellStyle(mainStyle_center);
				row6.createCell(4).setCellValue("路产赔偿");
				row6.getCell(4).setCellStyle(r2_style);
				row6.createCell(5).setCellValue(taList.get(tb).getRoadIndemnity());
				row6.getCell(5).setCellStyle(mainStyle_center);
				row6.createCell(6).setCellValue("索赔单号");
				row6.getCell(6).setCellStyle(r2_style);
				row6.createCell(7).setCellValue(taList.get(tb).getClaimNote());
				row6.getCell(7).setCellStyle(mainStyle_center);

				//第八行
				HSSFRow row7 = sheet10.createRow(7 + tb*10);
				row7.setHeightInPoints(120);
				row7.createCell(0).setCellValue(taList.get(tb).getAccidentDetails());
				row7.getCell(0).setCellStyle(mainStyle_left);
				for (int i = 1; i < 8; i++) {
					row7.createCell(i).setCellStyle(r2_style);
				}

				//第九行
				HSSFRow row8 = sheet10.createRow(8 + tb*10);
				row8.setHeightInPoints(40);
				row8.createCell(0).setCellValue("备注：" + taList.get(tb).getRemark());
				row8.getCell(0).setCellStyle(mainStyle_left);
				for (int i = 1; i < 8; i++) {
					row8.createCell(i).setCellStyle(r2_style);
				}

			}
		}else{
			//空表

			//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet10.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			sheet10.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
			sheet10.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet10.addMergedRegion(new CellRangeAddress(7, 7, 0, 7));
			sheet10.addMergedRegion(new CellRangeAddress(8, 8, 0, 7));


			//创建行（第一行）
			HSSFRow row0 = sheet10.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("交通事故");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet10.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-11");
			row1.getCell(0).setCellStyle(r1_style);

			//第三行
			HSSFRow row2 = sheet10.createRow(2);
			row2.setHeightInPoints(25);
			row2.createCell(0).setCellValue("日期：" + sdf1.format(tt.getDutyDate()));
			row2.getCell(0).setCellStyle(r1_style);

			//第四行
			HSSFRow row3 = sheet10.createRow(3);
			row3.setHeightInPoints(40);
			row3.createCell(0).setCellValue("天气情况");
			row3.getCell(0).setCellStyle(r2_style);
			row3.createCell(1).setCellStyle(mainStyle_center);
			row3.createCell(2).setCellValue("接报时间");
			row3.getCell(2).setCellStyle(r2_style);
			row3.createCell(3).setCellStyle(mainStyle_center);
			row3.createCell(4).setCellValue("接报方式");
			row3.getCell(4).setCellStyle(r2_style);
			row3.createCell(5).setCellStyle(mainStyle_center);
			row3.createCell(6).setCellValue("消息来源");
			row3.getCell(6).setCellStyle(r2_style);
			row3.createCell(7).setCellStyle(mainStyle_center);

			//第五行
			HSSFRow row4 = sheet10.createRow(4);
			row4.setHeightInPoints(40);
			row4.createCell(0).setCellValue("事故地点");
			row4.getCell(0).setCellStyle(r2_style);
			row4.createCell(1).setCellStyle(mainStyle_center);
			row4.createCell(2).setCellValue("事故类型");
			row4.getCell(2).setCellStyle(r2_style);
			row4.createCell(3).setCellStyle(mainStyle_center);
			row4.createCell(4).setCellValue("车辆类型");
			row4.getCell(4).setCellStyle(r2_style);
			row4.createCell(5).setCellStyle(mainStyle_center);
			row4.createCell(6).setCellValue("涉及车辆");
			row4.getCell(6).setCellStyle(r2_style);
			row4.createCell(7).setCellStyle(mainStyle_center);

			//第六
			HSSFRow row5 = sheet10.createRow(5);
			row5.setHeightInPoints(40);
			row5.createCell(0).setCellValue("涉事车牌");
			row5.getCell(0).setCellStyle(r2_style);
			row5.createCell(1).setCellStyle(mainStyle_center);
			row5.createCell(2).setCellValue("轻伤人数");
			row5.getCell(2).setCellStyle(r2_style);
			row5.createCell(3).setCellStyle(mainStyle_center);
			row5.createCell(4).setCellValue("重伤人数");
			row5.getCell(4).setCellStyle(r2_style);
			row5.createCell(5).setCellStyle(mainStyle_center);
			row5.createCell(6).setCellValue("死亡人数");
			row5.getCell(6).setCellStyle(r2_style);
			row5.createCell(7).setCellStyle(mainStyle_center);

			//第七行
			HSSFRow row6 = sheet10.createRow(6);
			row6.setHeightInPoints(40);
			row6.createCell(0).setCellValue("封闭车道");
			row6.getCell(0).setCellStyle(r2_style);
			row6.createCell(1).setCellStyle(mainStyle_center);
			row6.createCell(2).setCellValue("路产损失");
			row6.getCell(2).setCellStyle(r2_style);
			row6.createCell(3).setCellStyle(mainStyle_center);
			row6.createCell(4).setCellValue("路产赔偿");
			row6.getCell(4).setCellStyle(r2_style);
			row6.createCell(5).setCellStyle(mainStyle_center);
			row6.createCell(6).setCellValue("索赔单号");
			row6.getCell(6).setCellStyle(r2_style);
			row6.createCell(7).setCellStyle(mainStyle_center);

			//第八行
			HSSFRow row7 = sheet10.createRow(7);
			row7.setHeightInPoints(40);
			for (int i = 0; i < 8; i++) {
				row7.createCell(i).setCellStyle(r2_style);
			}

			//第九行
			HSSFRow row8 = sheet10.createRow(8);
			row8.setHeightInPoints(40);
			row8.createCell(0).setCellValue("备注：");
			row8.getCell(0).setCellStyle(mainStyle_left);
			for (int i = 1; i < 8; i++) {
				row8.createCell(i).setCellStyle(r2_style);
			}
		}

		return wb;
	}


	/**
	 * @intruduction 获取信息通传数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook getInfoThroughData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		TotalTable tt=this.totalTableDaoImpl.getEntityById(TotalTable.class, ttId);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		InfoThroughVo infoThroughVo = new InfoThroughVo();
		infoThroughVo.setTtId(ttId);
		List<InfoThrough> itList = this.infoThroughServiceImpl.queryEntityList(infoThroughVo);


		//创建sheet
		HSSFSheet sheet11 = wb.createSheet("信息通传");

		//设置列宽
		for (int i = 0; i < 8; i++) {
			if(i == 7){
				sheet11.setColumnWidth(i, sheet11.getColumnWidth(i)*5/2);
			}else{
				sheet11.setColumnWidth(i, sheet11.getColumnWidth(i)*2);
			}
		}


		if(itList != null && itList.size() > 0){
			for(int tb = 0; tb < itList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet11.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 7));
				sheet11.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 7));
				sheet11.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 7));
				sheet11.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 1, 3));
				sheet11.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 5, 7));
				sheet11.addMergedRegion(new CellRangeAddress(5 + tb*10, 5 + tb*10, 1, 7));
				sheet11.addMergedRegion(new CellRangeAddress(6 + tb*10, 6 + tb*10, 1, 7));
				sheet11.addMergedRegion(new CellRangeAddress(7 + tb*10, 7 + tb*10, 1, 7));


				//创建行（第一行）
				HSSFRow row0 = sheet11.createRow(0 + tb*10);
				//设置行的高度
				row0.setHeightInPoints(30);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(itList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet11.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-12");
				row1.getCell(0).setCellStyle(r1_style);

				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

				//第三行
				HSSFRow row2 = sheet11.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + sdf1.format(itList.get(tb).getDutyDate()));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet11.createRow(3 + tb*10);
				row3.setHeightInPoints(40);
				row3.createCell(0).setCellValue("通报时间");
				row3.getCell(0).setCellStyle(r2_style);
				row3.createCell(1).setCellValue(sdf2.format(itList.get(tb).getThroughTime()));
				row3.getCell(1).setCellStyle(mainStyle_center);
				row3.createCell(2).setCellValue("报告人员");
				row3.getCell(2).setCellStyle(r2_style);
				row3.createCell(3).setCellValue(itList.get(tb).getReportedPerson());
				row3.getCell(3).setCellStyle(mainStyle_center);
				row3.createCell(4).setCellValue("信息类型");
				row3.getCell(4).setCellStyle(r2_style);
				row3.createCell(5).setCellValue(getValueByDictAndKey("dc_infoType", itList.get(tb).getInfoType().toString()));
				row3.getCell(5).setCellStyle(mainStyle_center);
				row3.createCell(6).setCellValue("信息来源");
				row3.getCell(6).setCellStyle(r2_style);
				row3.createCell(7).setCellValue(getValueByDictAndKey("dc_infoSource", itList.get(tb).getInfoSource().toString()));
				row3.getCell(7).setCellStyle(mainStyle_center);

				//第五行
				HSSFRow row4 = sheet11.createRow(4 + tb*10);
				row4.setHeightInPoints(40);
				row4.createCell(0).setCellValue("通传方式");
				row4.getCell(0).setCellStyle(r2_style);
				row4.createCell(1).setCellValue(getValueByDictAndKey("dc_throughWay", itList.get(tb).getThroughWay().toString()));
				row4.getCell(1).setCellStyle(mainStyle_center);
				row4.createCell(4).setCellValue("值班员");
				row4.getCell(4).setCellStyle(r2_style);
				row4.createCell(5).setCellValue(getValueByDictAndKey("dc_dutyPerson", itList.get(tb).getWatcher().toString()));
				row4.getCell(5).setCellStyle(mainStyle_center);
				for (int i = 2; i < 8; i++) {
					if(i != 4 && i!= 5){
						row4.createCell(i).setCellStyle(r2_style);
					}
				}

				//第六
				HSSFRow row5 = sheet11.createRow(5 + tb*10);
				row5.setHeightInPoints(50);
				row5.createCell(0).setCellValue("信息内容");
				row5.getCell(0).setCellStyle(r2_style);
				row5.createCell(1).setCellValue(itList.get(tb).getInfoContent());
				row5.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row5.createCell(i).setCellStyle(r2_style);
				}

				//第七行
				HSSFRow row6 = sheet11.createRow(6 + tb*10);
				row6.setHeightInPoints(50);
				row6.createCell(0).setCellValue("通传情况");
				row6.getCell(0).setCellStyle(r2_style);
				row6.createCell(1).setCellValue(itList.get(tb).getThroughSituation());
				row6.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row6.createCell(i).setCellStyle(r2_style);
				}

				//第八行
				HSSFRow row7 = sheet11.createRow(7 + tb*10);
				row7.setHeightInPoints(50);
				row7.createCell(0).setCellValue("备注");
				row7.getCell(0).setCellStyle(r2_style);
				row7.createCell(1).setCellValue(itList.get(tb).getRemark());
				row7.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row7.createCell(i).setCellStyle(r2_style);
				}

			}
		}else{		//空表

			//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet11.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			sheet11.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
			sheet11.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet11.addMergedRegion(new CellRangeAddress(4, 4, 1, 3));
			sheet11.addMergedRegion(new CellRangeAddress(4, 4, 5, 7));
			sheet11.addMergedRegion(new CellRangeAddress(5, 5, 1, 7));
			sheet11.addMergedRegion(new CellRangeAddress(6, 6, 1, 7));
			sheet11.addMergedRegion(new CellRangeAddress(7, 7, 1, 7));


			//创建行（第一行）
			HSSFRow row0 = sheet11.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("信息通传");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet11.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-12");
			row1.getCell(0).setCellStyle(r1_style);

			//第三行
			HSSFRow row2 = sheet11.createRow(2);
			row2.setHeightInPoints(25);
			row2.createCell(0).setCellValue("日期：" + sdf1.format(tt.getDutyDate()));
			row2.getCell(0).setCellStyle(r1_style);

			//第四行
			HSSFRow row3 = sheet11.createRow(3);
			row3.setHeightInPoints(40);
			row3.createCell(0).setCellValue("通报时间");
			row3.getCell(0).setCellStyle(r2_style);
			row3.createCell(1).setCellStyle(r2_style);
			row3.createCell(2).setCellValue("报告人员");
			row3.getCell(2).setCellStyle(r2_style);
			row3.createCell(3).setCellStyle(r2_style);
			row3.createCell(4).setCellValue("信息类型");
			row3.getCell(4).setCellStyle(r2_style);
			row3.createCell(5).setCellStyle(r2_style);
			row3.createCell(6).setCellValue("信息来源");
			row3.getCell(6).setCellStyle(r2_style);
			row3.createCell(7).setCellStyle(r2_style);

			//第五行
			HSSFRow row4 = sheet11.createRow(4);
			row4.setHeightInPoints(50);
			row4.createCell(0).setCellValue("通传方式");
			row4.getCell(0).setCellStyle(r2_style);
			row4.createCell(4).setCellValue("值班员");
			row4.getCell(4).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				if(i != 4){
					row4.createCell(i).setCellStyle(r2_style);
				}
			}

			//第六
			HSSFRow row5 = sheet11.createRow(5);
			row5.setHeightInPoints(50);
			row5.createCell(0).setCellValue("信息内容");
			row5.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row5.createCell(i).setCellStyle(r2_style);
			}

			//第七行
			HSSFRow row6 = sheet11.createRow(6);
			row6.setHeightInPoints(50);
			row6.createCell(0).setCellValue("通传情况");
			row6.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row6.createCell(i).setCellStyle(r2_style);
			}

			//第八行
			HSSFRow row7 = sheet11.createRow(7);
			row7.setHeightInPoints(50);
			row7.createCell(0).setCellValue("备注");
			row7.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row7.createCell(i).setCellStyle(r2_style);
			}
		}

		return wb;
	}


	/**
	 * @intruduction 获取顾客意见反馈数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook getFeedBackData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		TotalTable tt=this.totalTableDaoImpl.getEntityById(TotalTable.class, ttId);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		FeedBackVo feedBackVo = new FeedBackVo();
		feedBackVo.setTtId(ttId);
		List<FeedBack> fbList = this.feedBackServiceImpl.queryEntityList(feedBackVo);


		//创建sheet
		HSSFSheet sheet12 = wb.createSheet("顾客意见反馈");

		//设置列宽
		for (int i = 0; i < 8; i++) {
			if(i == 7){
				sheet12.setColumnWidth(i, sheet12.getColumnWidth(i)*5/2);
			}else{
				sheet12.setColumnWidth(i, sheet12.getColumnWidth(i)*2);
			}
		}


		if(fbList != null && fbList.size() > 0){
			for(int tb = 0; tb < fbList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet12.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 7));
				sheet12.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 7));
				sheet12.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 7));
				sheet12.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 1, 2));
				sheet12.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 4, 5));
				sheet12.addMergedRegion(new CellRangeAddress(5 + tb*10, 5 + tb*10, 1, 7));
				sheet12.addMergedRegion(new CellRangeAddress(6 + tb*10, 6 + tb*10, 1, 7));
				sheet12.addMergedRegion(new CellRangeAddress(7 + tb*10, 7 + tb*10, 1, 7));


				//创建行（第一行）
				HSSFRow row0 = sheet12.createRow(0 + tb*10);
				//设置行的高度
				row0.setHeightInPoints(30);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(fbList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet12.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-13");
				row1.getCell(0).setCellStyle(r1_style);

				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

				//第三行
				HSSFRow row2 = sheet12.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + sdf1.format(fbList.get(tb).getDutyDate()));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet12.createRow(3 + tb*10);
				row3.setHeightInPoints(40);
				row3.createCell(0).setCellValue("接报时间");
				row3.getCell(0).setCellStyle(r2_style);
				row3.createCell(1).setCellValue(sdf2.format(fbList.get(tb).getReceiptTime()));
				row3.getCell(1).setCellStyle(mainStyle_center);
				row3.createCell(2).setCellValue("报告人员");
				row3.getCell(2).setCellStyle(r2_style);
				row3.createCell(3).setCellValue(fbList.get(tb).getReportedPerson());
				row3.getCell(3).setCellStyle(mainStyle_center);
				row3.createCell(4).setCellValue("性别");
				row3.getCell(4).setCellStyle(r2_style);
				row3.createCell(5).setCellValue(getValueByDictAndKey("sex", fbList.get(tb).getCustomerSex().toString()));
				row3.getCell(5).setCellStyle(mainStyle_center);
				row3.createCell(6).setCellValue("车辆号牌");
				row3.getCell(6).setCellStyle(r2_style);
				row3.createCell(7).setCellValue(fbList.get(tb).getPlateNum());
				row3.getCell(7).setCellStyle(mainStyle_center);

				//第五行
				HSSFRow row4 = sheet12.createRow(4 + tb*10);
				row4.setHeightInPoints(40);
				row4.createCell(0).setCellValue("联系电话");
				row4.getCell(0).setCellStyle(r2_style);
				row4.createCell(1).setCellValue(fbList.get(tb).getCustomerPhone());
				row4.getCell(1).setCellStyle(mainStyle_center);
				row4.createCell(3).setCellValue("反馈类型");
				row4.getCell(3).setCellStyle(r2_style);
				row4.createCell(4).setCellValue(getValueByDictAndKey("dc_fbType", fbList.get(tb).getFbType().toString()));
				row4.getCell(4).setCellStyle(mainStyle_center);
				row4.createCell(6).setCellValue("值班员");
				row4.getCell(6).setCellStyle(r2_style);
				row4.createCell(7).setCellValue(getValueByDictAndKey("dc_dutyPerson", fbList.get(tb).getWatcher().toString()));
				row4.getCell(7).setCellStyle(mainStyle_center);

				row4.createCell(2).setCellStyle(r2_style);
				row4.createCell(5).setCellStyle(r2_style);


				//第六
				HSSFRow row5 = sheet12.createRow(5 + tb*10);
				row5.setHeightInPoints(50);
				row5.createCell(0).setCellValue("情况概述");
				row5.getCell(0).setCellStyle(r2_style);
				row5.createCell(1).setCellValue(fbList.get(tb).getSituationDesc());
				row5.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row5.createCell(i).setCellStyle(r2_style);
				}

				//第七行
				HSSFRow row6 = sheet12.createRow(6 + tb*10);
				row6.setHeightInPoints(50);
				row6.createCell(0).setCellValue("处理情况");
				row6.getCell(0).setCellStyle(r2_style);
				row6.createCell(1).setCellValue(fbList.get(tb).getDisposalSituation());
				row6.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row6.createCell(i).setCellStyle(r2_style);
				}

				//第八行
				HSSFRow row7 = sheet12.createRow(7 + tb*10);
				row7.setHeightInPoints(50);
				row7.createCell(0).setCellValue("备注");
				row7.getCell(0).setCellStyle(r2_style);
				row7.createCell(1).setCellValue(fbList.get(tb).getRemark());
				row7.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row7.createCell(i).setCellStyle(r2_style);
				}

			}
		}else{		//空表

			//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet12.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			sheet12.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
			sheet12.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet12.addMergedRegion(new CellRangeAddress(4, 4, 1, 2));
			sheet12.addMergedRegion(new CellRangeAddress(4, 4, 4, 5));
			sheet12.addMergedRegion(new CellRangeAddress(5, 5, 1, 7));
			sheet12.addMergedRegion(new CellRangeAddress(6, 6, 1, 7));
			sheet12.addMergedRegion(new CellRangeAddress(7, 7, 1, 7));


			//创建行（第一行）
			HSSFRow row0 = sheet12.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("顾客意见反馈");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet12.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-13");
			row1.getCell(0).setCellStyle(r1_style);

			//第三行
			HSSFRow row2 = sheet12.createRow(2);
			row2.setHeightInPoints(25);
			row2.createCell(0).setCellValue("日期：" + sdf1.format(tt.getDutyDate()));
			row2.getCell(0).setCellStyle(r1_style);

			//第四行
			HSSFRow row3 = sheet12.createRow(3);
			row3.setHeightInPoints(40);
			row3.createCell(0).setCellValue("接报时间");
			row3.getCell(0).setCellStyle(r2_style);
			row3.createCell(1).setCellStyle(r2_style);
			row3.createCell(2).setCellValue("报告人员");
			row3.getCell(2).setCellStyle(r2_style);
			row3.createCell(3).setCellStyle(r2_style);
			row3.createCell(4).setCellValue("性别");
			row3.getCell(4).setCellStyle(r2_style);
			row3.createCell(5).setCellStyle(r2_style);
			row3.createCell(6).setCellValue("车辆号牌");
			row3.getCell(6).setCellStyle(r2_style);
			row3.createCell(7).setCellStyle(r2_style);

			//第五行
			HSSFRow row4 = sheet12.createRow(4);
			row4.setHeightInPoints(50);
			row4.createCell(0).setCellValue("联系电话");
			row4.getCell(0).setCellStyle(r2_style);
			row4.createCell(3).setCellValue("反馈类型");
			row4.getCell(3).setCellStyle(r2_style);
			row4.createCell(6).setCellValue("值班员");
			row4.getCell(6).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				if(i != 3 && i != 6){
					row4.createCell(i).setCellStyle(r2_style);
				}
			}

			//第六
			HSSFRow row5 = sheet12.createRow(5);
			row5.setHeightInPoints(50);
			row5.createCell(0).setCellValue("情况概述");
			row5.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row5.createCell(i).setCellStyle(r2_style);
			}

			//第七行
			HSSFRow row6 = sheet12.createRow(6);
			row6.setHeightInPoints(50);
			row6.createCell(0).setCellValue("处理情况");
			row6.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row6.createCell(i).setCellStyle(r2_style);
			}

			//第八行
			HSSFRow row7 = sheet12.createRow(7);
			row7.setHeightInPoints(50);
			row7.createCell(0).setCellValue("备注");
			row7.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row7.createCell(i).setCellStyle(r2_style);
			}
		}

		return wb;
	}


	/**
	 * @intruduction 获取交通阻塞数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook getTrafficJamData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		TotalTable tt=this.totalTableDaoImpl.getEntityById(TotalTable.class, ttId);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		TrafficJamVo trafficJamVo = new TrafficJamVo();
		trafficJamVo.setTtId(ttId);
		List<TrafficJam> tjList = this.trafficJamServiceImpl.queryEntityList(trafficJamVo);


		//创建sheet
		HSSFSheet sheet13 = wb.createSheet("交通阻塞");

		//设置列宽
		for (int i = 0; i < 8; i++) {
			sheet13.setColumnWidth(i, sheet13.getColumnWidth(i)*2);
		}


		if(tjList != null && tjList.size() > 0){
			for(int tb = 0; tb < tjList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet13.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 9));
				sheet13.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 9));
				sheet13.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 9));
				sheet13.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 5, 6));
				sheet13.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 8, 9));
				sheet13.addMergedRegion(new CellRangeAddress(5 + tb*10, 5 + tb*10, 1, 9));
				sheet13.addMergedRegion(new CellRangeAddress(6 + tb*10, 6 + tb*10, 1, 9));
				sheet13.addMergedRegion(new CellRangeAddress(7 + tb*10, 7 + tb*10, 1, 9));


				//创建行（第一行）
				HSSFRow row0 = sheet13.createRow(0 + tb*10);
				//设置行的高度
				row0.setHeightInPoints(30);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(tjList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet13.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-14");
				row1.getCell(0).setCellStyle(r1_style);

				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

				//第三行
				HSSFRow row2 = sheet13.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + sdf1.format(tjList.get(tb).getDutyDate()));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet13.createRow(3 + tb*10);
				row3.setHeightInPoints(40);
				row3.createCell(0).setCellValue("接报时间");
				row3.getCell(0).setCellStyle(r2_style);
				row3.createCell(1).setCellValue(sdf2.format(tjList.get(tb).getReceiptTime()));
				row3.getCell(1).setCellStyle(mainStyle_center);
				row3.createCell(2).setCellValue("接报方式");
				row3.getCell(2).setCellStyle(r2_style);
				row3.createCell(3).setCellValue(getValueByDictAndKey("dc_receiptWay", tjList.get(tb).getReceiptWay().toString()));
				row3.getCell(3).setCellStyle(mainStyle_center);
				row3.createCell(4).setCellValue("报告人员");
				row3.getCell(4).setCellStyle(r2_style);
				row3.createCell(5).setCellValue(tjList.get(tb).getReportedPerson());
				row3.getCell(5).setCellStyle(mainStyle_center);
				row3.createCell(6).setCellValue("阻塞路段");
				row3.getCell(6).setCellStyle(r2_style);
				row3.createCell(7).setCellValue(tjList.get(tb).getJamSection());
				row3.getCell(7).setCellStyle(mainStyle_center);
				row3.createCell(8).setCellValue("阻塞距离");
				row3.getCell(8).setCellStyle(r2_style);
				row3.createCell(9).setCellValue(tjList.get(tb).getJamDistance() + " 公里");
				row3.getCell(9).setCellStyle(mainStyle_center);

				//第五行
				HSSFRow row4 = sheet13.createRow(4 + tb*10);
				row4.setHeightInPoints(40);
				row4.createCell(0).setCellValue("开始时间");
				row4.getCell(0).setCellStyle(r2_style);
				row4.createCell(1).setCellValue(sdf2.format(tjList.get(tb).getStartTime()));
				row4.getCell(1).setCellStyle(mainStyle_center);
				row4.createCell(2).setCellValue("结束时间");
				row4.getCell(2).setCellStyle(r2_style);
				row4.createCell(3).setCellValue(sdf2.format(tjList.get(tb).getEndTime()));
				row4.getCell(3).setCellStyle(mainStyle_center);
				row4.createCell(4).setCellValue("交警到场时间");
				row4.getCell(4).setCellStyle(r2_style);
				if(tjList.get(tb).getJjdcTime() != null){
					row4.createCell(5).setCellValue(sdf2.format(tjList.get(tb).getJjdcTime()));
				}else{
					row4.createCell(5);
				}
				row4.getCell(5).setCellStyle(mainStyle_center);
				row4.createCell(7).setCellValue("路管员到场时间");
				row4.getCell(7).setCellStyle(r2_style);
				if(tjList.get(tb).getJjdcTime() != null){
					row4.createCell(8).setCellValue(sdf2.format(tjList.get(tb).getLgydcTime()));
				}else{
					row4.createCell(8);
				}
				row4.getCell(8).setCellStyle(mainStyle_center);

				row4.createCell(6).setCellStyle(r2_style);
				row4.createCell(9).setCellStyle(r2_style);


				//第六
				HSSFRow row5 = sheet13.createRow(5 + tb*10);
				row5.setHeightInPoints(50);
				row5.createCell(0).setCellValue("阻塞原因");
				row5.getCell(0).setCellStyle(r2_style);
				row5.createCell(1).setCellValue(tjList.get(tb).getJamReason());
				row5.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 10; i++) {
					row5.createCell(i).setCellStyle(r2_style);
				}

				//第七行
				HSSFRow row6 = sheet13.createRow(6 + tb*10);
				row6.setHeightInPoints(50);
				row6.createCell(0).setCellValue("处理情况");
				row6.getCell(0).setCellStyle(r2_style);
				row6.createCell(1).setCellValue(tjList.get(tb).getDisposalSituation());
				row6.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 10; i++) {
					row6.createCell(i).setCellStyle(r2_style);
				}

				//第八行
				HSSFRow row7 = sheet13.createRow(7 + tb*10);
				row7.setHeightInPoints(50);
				row7.createCell(0).setCellValue("备注");
				row7.getCell(0).setCellStyle(r2_style);
				row7.createCell(1).setCellValue(tjList.get(tb).getRemark());
				row7.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 10; i++) {
					row7.createCell(i).setCellStyle(r2_style);
				}

			}
		}else{
			//空表

			//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet13.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
			sheet13.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
			sheet13.addMergedRegion(new CellRangeAddress(2, 2, 0, 9));
			sheet13.addMergedRegion(new CellRangeAddress(4, 4, 5, 6));
			sheet13.addMergedRegion(new CellRangeAddress(4, 4, 8, 9));
			sheet13.addMergedRegion(new CellRangeAddress(5, 5, 1, 9));
			sheet13.addMergedRegion(new CellRangeAddress(6, 6, 1, 9));
			sheet13.addMergedRegion(new CellRangeAddress(7, 7, 1, 9));


			//创建行（第一行）
			HSSFRow row0 = sheet13.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("交通阻塞");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet13.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-14");
			row1.getCell(0).setCellStyle(r1_style);

			//第三行
			HSSFRow row2 = sheet13.createRow(2);
			row2.setHeightInPoints(25);
			row2.createCell(0).setCellValue("日期：" + sdf1.format(tt.getDutyDate()));
			row2.getCell(0).setCellStyle(r1_style);

			//第四行
			HSSFRow row3 = sheet13.createRow(3);
			row3.setHeightInPoints(40);
			row3.createCell(0).setCellValue("接报时间");
			row3.getCell(0).setCellStyle(r2_style);
			row3.createCell(1).setCellStyle(r2_style);
			row3.createCell(2).setCellValue("接报方式");
			row3.getCell(2).setCellStyle(r2_style);
			row3.createCell(3).setCellStyle(r2_style);
			row3.createCell(4).setCellValue("报告人员");
			row3.getCell(4).setCellStyle(r2_style);
			row3.createCell(5).setCellStyle(r2_style);
			row3.createCell(6).setCellValue("阻塞路段");
			row3.getCell(6).setCellStyle(r2_style);
			row3.createCell(7).setCellStyle(r2_style);
			row3.createCell(8).setCellValue("阻塞距离");
			row3.getCell(8).setCellStyle(r2_style);
			row3.createCell(9).setCellStyle(r2_style);

			//第五行
			HSSFRow row4 = sheet13.createRow(4);
			row4.setHeightInPoints(50);
			row4.createCell(0).setCellValue("开始时间");
			row4.getCell(0).setCellStyle(r2_style);
			row4.createCell(2).setCellValue("结束时间");
			row4.getCell(2).setCellStyle(r2_style);
			row4.createCell(4).setCellValue("交警到场时间");
			row4.getCell(4).setCellStyle(r2_style);
			row4.createCell(7).setCellValue("路管员到场时间");
			row4.getCell(7).setCellStyle(r2_style);
			for (int i = 1; i < 10; i++) {
				if(i != 2 && i != 4 && i != 7){
					row4.createCell(i).setCellStyle(r2_style);
				}
			}

			//第六
			HSSFRow row5 = sheet13.createRow(5);
			row5.setHeightInPoints(50);
			row5.createCell(0).setCellValue("阻塞原因");
			row5.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 10; i++) {
				row5.createCell(i).setCellStyle(r2_style);
			}

			//第七行
			HSSFRow row6 = sheet13.createRow(6);
			row6.setHeightInPoints(50);
			row6.createCell(0).setCellValue("处理情况");
			row6.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 10; i++) {
				row6.createCell(i).setCellStyle(r2_style);
			}

			//第八行
			HSSFRow row7 = sheet13.createRow(7);
			row7.setHeightInPoints(50);
			row7.createCell(0).setCellValue("备注");
			row7.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 10; i++) {
				row7.createCell(i).setCellStyle(r2_style);
			}
		}

		return wb;
	}


	/**
	 * @intruduction 获取外勤作业数据
	 * @param wb excel文档对象
	 * @param ttId 主表id
	 * @param mainStyle_center
	 * @param mainStyle_left
	 * @param r0_style
	 * @param r1_style
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook getFieldOperationsData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		TotalTable tt=this.totalTableDaoImpl.getEntityById(TotalTable.class, ttId);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		FieldOperationsVo fieldOperationsVo = new FieldOperationsVo();
		fieldOperationsVo.setTtId(ttId);
		List<FieldOperations> foList = this.fieldOperationsServiceImpl.queryEntityList(fieldOperationsVo);


		//创建sheet
		HSSFSheet sheet14 = wb.createSheet("外勤作业");

		//设置列宽
		for (int i = 0; i < 8; i++) {
			if(i == 7){
				sheet14.setColumnWidth(i, sheet14.getColumnWidth(i)*5/2);
			}else{
				sheet14.setColumnWidth(i, sheet14.getColumnWidth(i)*2);
			}
		}


		if(foList != null && foList.size() > 0){
			for(int tb = 0; tb < foList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet14.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 7));
				sheet14.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 7));
				sheet14.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 7));
				sheet14.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 1, 2));
				sheet14.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 6, 7));
				sheet14.addMergedRegion(new CellRangeAddress(5 + tb*10, 5 + tb*10, 1, 7));
				sheet14.addMergedRegion(new CellRangeAddress(6 + tb*10, 6 + tb*10, 1, 7));
				sheet14.addMergedRegion(new CellRangeAddress(7 + tb*10, 7 + tb*10, 1, 7));


				//创建行（第一行）
				HSSFRow row0 = sheet14.createRow(0 + tb*10);
				//设置行的高度
				row0.setHeightInPoints(30);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(foList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet14.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-15");
				row1.getCell(0).setCellStyle(r1_style);

				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

				//第三行
				HSSFRow row2 = sheet14.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + sdf1.format(foList.get(tb).getDutyDate()));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet14.createRow(3 + tb*10);
				row3.setHeightInPoints(40);
				row3.createCell(0).setCellValue("接报时间");
				row3.getCell(0).setCellStyle(r2_style);
				row3.createCell(1).setCellValue(sdf2.format(foList.get(tb).getReceiptTime()));
				row3.getCell(1).setCellStyle(mainStyle_center);
				row3.createCell(2).setCellValue("报告人员");
				row3.getCell(2).setCellStyle(r2_style);
				row3.createCell(3).setCellValue(foList.get(tb).getReportedPerson());
				row3.getCell(3).setCellStyle(mainStyle_center);
				row3.createCell(4).setCellValue("接报方式");
				row3.getCell(4).setCellStyle(r2_style);
				row3.createCell(5).setCellValue(getValueByDictAndKey("dc_receiptWay", foList.get(tb).getReceiptWay().toString()));
				row3.getCell(5).setCellStyle(mainStyle_center);
				row3.createCell(6).setCellValue("外勤人员");
				row3.getCell(6).setCellStyle(r2_style);
				row3.createCell(7).setCellValue(foList.get(tb).getOutworker());
				row3.getCell(7).setCellStyle(mainStyle_center);

				//第五行
				HSSFRow row4 = sheet14.createRow(4 + tb*10);
				row4.setHeightInPoints(40);
				row4.createCell(0).setCellValue("事发地点");
				row4.getCell(0).setCellStyle(r2_style);
				row4.createCell(1).setCellValue(foList.get(tb).getScene());
				row4.getCell(1).setCellStyle(mainStyle_center);
				row4.createCell(3).setCellValue("涉事单位");
				row4.getCell(3).setCellStyle(r2_style);
				row4.createCell(4).setCellValue(foList.get(tb).getInvolvedUnits());
				row4.getCell(4).setCellStyle(mainStyle_center);
				row4.createCell(5).setCellValue("违章单号");
				row4.getCell(5).setCellStyle(r2_style);
				row4.createCell(6).setCellValue(foList.get(tb).getViolationOrderNo());
				row4.getCell(6).setCellStyle(mainStyle_center);

				row4.createCell(2).setCellStyle(r2_style);
				row4.createCell(7).setCellStyle(r2_style);


				//第六
				HSSFRow row5 = sheet14.createRow(5 + tb*10);
				row5.setHeightInPoints(50);
				row5.createCell(0).setCellValue("接报情况");
				row5.getCell(0).setCellStyle(r2_style);
				row5.createCell(1).setCellValue(foList.get(tb).getReceiptSituation());
				row5.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row5.createCell(i).setCellStyle(r2_style);
				}

				//第七行
				HSSFRow row6 = sheet14.createRow(6 + tb*10);
				row6.setHeightInPoints(50);
				row6.createCell(0).setCellValue("处置简述");
				row6.getCell(0).setCellStyle(r2_style);
				row6.createCell(1).setCellValue(foList.get(tb).getDisposeDesc());
				row6.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row6.createCell(i).setCellStyle(r2_style);
				}

				//第八行
				HSSFRow row7 = sheet14.createRow(7 + tb*10);
				row7.setHeightInPoints(50);
				row7.createCell(0).setCellValue("备注");
				row7.getCell(0).setCellStyle(r2_style);
				row7.createCell(1).setCellValue(foList.get(tb).getRemark());
				row7.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row7.createCell(i).setCellStyle(r2_style);
				}

			}
		}else{		//空表

			//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet14.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			sheet14.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
			sheet14.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet14.addMergedRegion(new CellRangeAddress(4, 4, 1, 2));
			sheet14.addMergedRegion(new CellRangeAddress(4, 4, 6, 7));
			sheet14.addMergedRegion(new CellRangeAddress(5, 5, 1, 7));
			sheet14.addMergedRegion(new CellRangeAddress(6, 6, 1, 7));
			sheet14.addMergedRegion(new CellRangeAddress(7, 7, 1, 7));


			//创建行（第一行）
			HSSFRow row0 = sheet14.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("外勤作业");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet14.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-15");
			row1.getCell(0).setCellStyle(r1_style);

			//第三行
			HSSFRow row2 = sheet14.createRow(2);
			row2.setHeightInPoints(25);
			row2.createCell(0).setCellValue("日期：" + sdf1.format(tt.getDutyDate()));
			row2.getCell(0).setCellStyle(r1_style);

			//第四行
			HSSFRow row3 = sheet14.createRow(3);
			row3.setHeightInPoints(40);
			row3.createCell(0).setCellValue("接报时间");
			row3.getCell(0).setCellStyle(r2_style);
			row3.createCell(1).setCellStyle(r2_style);
			row3.createCell(2).setCellValue("报告人员");
			row3.getCell(2).setCellStyle(r2_style);
			row3.createCell(3).setCellStyle(r2_style);
			row3.createCell(4).setCellValue("接报方式");
			row3.getCell(4).setCellStyle(r2_style);
			row3.createCell(5).setCellStyle(r2_style);
			row3.createCell(6).setCellValue("外勤人员");
			row3.getCell(6).setCellStyle(r2_style);
			row3.createCell(7).setCellStyle(r2_style);

			//第五行
			HSSFRow row4 = sheet14.createRow(4);
			row4.setHeightInPoints(50);
			row4.createCell(0).setCellValue("事发地点");
			row4.getCell(0).setCellStyle(r2_style);
			row4.createCell(3).setCellValue("涉事单位");
			row4.getCell(3).setCellStyle(r2_style);
			row4.createCell(5).setCellValue("违章单号");
			row4.getCell(5).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				if(i != 3 && i != 5){
					row4.createCell(i).setCellStyle(r2_style);
				}
			}

			//第六
			HSSFRow row5 = sheet14.createRow(5);
			row5.setHeightInPoints(50);
			row5.createCell(0).setCellValue("接报情况");
			row5.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row5.createCell(i).setCellStyle(r2_style);
			}

			//第七行
			HSSFRow row6 = sheet14.createRow(6);
			row6.setHeightInPoints(50);
			row6.createCell(0).setCellValue("处置简述");
			row6.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row6.createCell(i).setCellStyle(r2_style);
			}

			//第八行
			HSSFRow row7 = sheet14.createRow(7);
			row7.setHeightInPoints(50);
			row7.createCell(0).setCellValue("备注");
			row7.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row7.createCell(i).setCellStyle(r2_style);
			}
		}

		return wb;
	}


	




	/**
	 * @intruduction 通过 字典名称dict 和 字典key 获得 value
	 * @param dict 字典名称
	 * @param key 字典key
	 * @return  value
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public String getValueByDictAndKey(String dict,String key){
		String value = "";
		for (CategoryAttribute ca : Cache.getDictByCode.get(dict)) {
			if(ca.getAttrKey().equals(key)){
				value = ca.getAttrValue();
				break;
			}
		}
		return value;
	}

}
