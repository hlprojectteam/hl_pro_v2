package com.datacenter.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.common.utils.cache.Cache;
import com.datacenter.module.TransferRegistration;
import com.datacenter.vo.TransferRegistrationVo;
import com.urms.dataDictionary.module.CategoryAttribute;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.datacenter.dao.ITotalTableDao;
import com.datacenter.module.TotalTable;
import com.datacenter.service.IBriefService;
import com.datacenter.service.IClearingService;
import com.datacenter.service.IEquipmentOperationService;
import com.datacenter.service.IExceptionRecordService;
import com.datacenter.service.IFeedBackService;
import com.datacenter.service.IFieldOperationsService;
import com.datacenter.service.IInfoThroughService;
import com.datacenter.service.IOperatingDataService;
import com.datacenter.service.IRescueWorkService;
import com.datacenter.service.IRoadWorkService;
import com.datacenter.service.ISurveillanceInspectionService;
import com.datacenter.service.ITotalTableService;
import com.datacenter.service.ITrafficAccidentService;
import com.datacenter.service.ITrafficJamService;
import com.datacenter.service.ITransferRegistrationService;
import com.datacenter.vo.TotalTableVo;

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



	@Override
	public Pager queryEntityList(Integer page, Integer rows, TotalTableVo totalTableVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(totalTableVo.getDutyDate() != null){			//日期
			params.add(Restrictions.eq("dutyDate", totalTableVo.getDutyDate()));
		}
		if(totalTableVo.getDutyDateStart() != null){	//日期Start
			params.add(Restrictions.ge("dutyDate", totalTableVo.getDutyDateStart()));
		}
		if(totalTableVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", totalTableVo.getDutyDateEnd()));
		}
		return this.totalTableDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), TotalTable.class);
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

		//单元格基础样式
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
		//基础样式 水平靠右
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

		//工作简报
		getBriefData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);
		//交接班
		/*getTransferRegistrationData(wb, ttId, mainStyle_center, mainStyle_left, r0_style, r1_style, r2_style);*/


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
	 * @param r2_style
	 * @return HSSFWorkbook
	 * @author xuezb
	 * @Date 2019年3月5日
	 */
	public HSSFWorkbook getBriefData(HSSFWorkbook wb, String ttId, HSSFCellStyle mainStyle_center, HSSFCellStyle mainStyle_left, HSSFCellStyle r0_style, HSSFCellStyle r1_style, HSSFCellStyle r2_style){
		TransferRegistrationVo transferRegistrationVo = new TransferRegistrationVo();
		transferRegistrationVo.setTtId(ttId);
		List<TransferRegistration> trList = this.transferRegistrationServiceImpl.queryEntityList(transferRegistrationVo);

		//创建sheet
		HSSFSheet sheet1 = wb.createSheet("交接班");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

		//创建行
		HSSFRow row0 = sheet1.createRow(0);
		row0.setHeightInPoints(30);			//设置行的高度
		HSSFRow row1 = sheet1.createRow(1);
		//创建单元格 并 设置单元格内容
		HSSFCell r0_0 = row0.createCell(0);
		r0_0.setCellStyle(r0_style);
		if(trList != null && trList.size() > 0){
			r0_0.setCellValue(trList.get(0).getTitle());
		}else{
			r0_0.setCellValue("环龙运营控制指挥中心交接班登记表");
		}
		HSSFCell r1_0 = row1.createCell(0);
		r1_0.setCellStyle(r1_style);
		r1_0.setCellValue("表单编号：HLZXRBB-02");


		HSSFRow row2 = sheet1.createRow(2);
		row2.setHeightInPoints(30);			//设置行的高度
		HSSFCell cell = null;
		String[] title = {"班次","天气","本班次值班人员","值班时间","上班次值班人员","交接时间","交接事项","接班异常情况"};
		for(int i=0;i<title.length;i++){
			cell = row2.createCell(i);
			cell.setCellValue(title[i]);
			cell.setCellStyle(r2_style);

			sheet1.autoSizeColumn(i);
			if(i == 6){
				sheet1.setColumnWidth(i, sheet1.getColumnWidth(i)*4);
			}else{
				sheet1.setColumnWidth(i, sheet1.getColumnWidth(i)*5/2);
			}
		}

		HSSFRow row = null;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String[][] content = new String[trList.size()][];
		for (int i = 0; i < trList.size(); i++) {
			row = sheet1.createRow(i + 3);
			row.setHeightInPoints(60);
			for (int j = 0; j < title.length; j++) {
				cell = row.createCell(j);
				int k = 6;
				if(j == k){
					cell.setCellStyle(mainStyle_left);
				}else{
					cell.setCellStyle(mainStyle_center);
				}

				switch (j){
					case 0:	cell.setCellValue(getValueByDictAndKey("dc_shift", trList.get(i).getShift().toString())); break;
					case 1: cell.setCellValue(getValueByDictAndKey("dc_weather", trList.get(i).getWeather().toString()));	break;
					case 2: cell.setCellValue(trList.get(i).getThisWatcher()); break;
					case 3:
						if(trList.get(i).getShift() == 3){
							cell.setCellValue(sdf.format(trList.get(i).getWatchTimeStart()) + "--次日" + sdf.format(trList.get(i).getWatchTimeEnd()));	break;
						}else{
							cell.setCellValue(sdf.format(trList.get(i).getWatchTimeStart()) + "--" + sdf.format(trList.get(i).getWatchTimeEnd()));	break;
						}
					case 4: cell.setCellValue(trList.get(i).getLaseWatcher());	break;
					case 5: cell.setCellValue(sdf.format(trList.get(i).getHandoverTime()));	break;
					case 6: cell.setCellValue(trList.get(i).getHandoverMatters()); break;
					case 7: cell.setCellValue(trList.get(i).getException()); break;
				}
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
