package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.datacenter.dao.ISurveillanceInspectionDao;
import com.datacenter.module.SurveillanceInspection;
import com.datacenter.module.TotalTable;
import com.datacenter.service.ISurveillanceInspectionService;
import com.datacenter.vo.SurveillanceInspectionVo;
import com.urms.dataDictionary.service.IDataDictionaryService;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 监控巡检 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("surveillanceInspectionServiceImpl")
public class SurveillanceInspectionServiceImpl extends BaseServiceImpl implements ISurveillanceInspectionService{

	@Autowired
	private ISurveillanceInspectionDao surveillanceInspectionDaoImpl;

	@Autowired
	private TotalTableServiceImpl totalTableServiceImpl;
	
	@Autowired
	public IDataDictionaryService dataDictionaryServiceImpl;


	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, SurveillanceInspectionVo surveillanceInspectionVo) {
		List<Criterion> params = new ArrayList<>();
		if(StringUtils.isNotBlank(surveillanceInspectionVo.getTtId())){
			params.add(Restrictions.eq("ttId", surveillanceInspectionVo.getTtId()));
		}
		if(surveillanceInspectionVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", surveillanceInspectionVo.getDutyDateStart()));
		}
		if(surveillanceInspectionVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", surveillanceInspectionVo.getDutyDateEnd()));
		}
		if(surveillanceInspectionVo.getInspectionlocation() != null){		//巡检位置
			params.add(Restrictions.eq("inspectionlocation", surveillanceInspectionVo.getInspectionlocation()));
		}
		if(StringUtils.isNotBlank(surveillanceInspectionVo.getShiftSupervisor())){		//值班主任
			params.add(Restrictions.eq("shiftSupervisor", surveillanceInspectionVo.getShiftSupervisor()));
		}

		if(StringUtils.isNotBlank(surveillanceInspectionVo.getKeyword())){
			params.add(Restrictions.sqlRestriction(" (shift_Supervisor like '%" + surveillanceInspectionVo.getKeyword() + "%' " +
					" or inspection_Details like '%" + surveillanceInspectionVo.getKeyword() + "%' " +
					" or follow_Measure like '%" + surveillanceInspectionVo.getKeyword() + "%' )"));
		}
		return this.surveillanceInspectionDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), SurveillanceInspection.class);
	}

	@Override
	public SurveillanceInspection saveOrUpdate(SurveillanceInspectionVo surveillanceInspectionVo) {
		if(surveillanceInspectionVo.getShiftSupervisor().equals("99")){
			String newKey = this.dataDictionaryServiceImpl.updateCategoryAttributesByCode("dc_headOfDuty", surveillanceInspectionVo.getDictValue());
			surveillanceInspectionVo.setShiftSupervisor(newKey);
		}
		SurveillanceInspection surveillanceInspection = new SurveillanceInspection();
		BeanUtils.copyProperties(surveillanceInspectionVo, surveillanceInspection);
		if(StringUtils.isBlank(surveillanceInspection.getId())){
			this.save(surveillanceInspection);
		}else{
			this.update(surveillanceInspection);
		}
		return surveillanceInspection;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.surveillanceInspectionDaoImpl.excuteBySql("delete from dc_SurveillanceInspection where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<SurveillanceInspection> list = this.surveillanceInspectionDaoImpl.queryEntityList(params, Order.desc("createTime"), SurveillanceInspection.class);	//根据主表Id获取子表关联数据
		for (SurveillanceInspection surveillanceInspection : list) {
			surveillanceInspection.setDutyDate(dutyDate);
			this.update(surveillanceInspection);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<SurveillanceInspection> queryEntityList(SurveillanceInspectionVo surveillanceInspectionVo) {
		List<Object> objectList = new ArrayList<>();

		StringBuilder hql = new StringBuilder("from SurveillanceInspection where 1 = 1 ");
		if(StringUtils.isNotBlank(surveillanceInspectionVo.getTtId())){
			objectList.add(surveillanceInspectionVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(surveillanceInspectionVo.getDutyDateStart() != null){		//日期Start
			objectList.add(surveillanceInspectionVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(surveillanceInspectionVo.getDutyDateEnd() != null){		//日期End
			objectList.add(surveillanceInspectionVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}

		if(surveillanceInspectionVo.getInspectionlocation() != null){		//巡检位置
			objectList.add(surveillanceInspectionVo.getInspectionlocation());
			hql.append(" and inspectionlocation = ? ");
		}

		if(StringUtils.isNotBlank(surveillanceInspectionVo.getKeyword())){
			hql.append(" and (shiftSupervisor like '%").append(surveillanceInspectionVo.getKeyword()).append("%' ").append(" or inspectionDetails like '%").append(surveillanceInspectionVo.getKeyword()).append("%' ").append(" or followMeasure like '%").append(surveillanceInspectionVo.getKeyword()).append("%' )");
		}


		//排序, 根据日期倒序排序，巡检时间Start顺序排序
		hql.append(" order by dutyDate asc,inspectionTimeStart asc ");

		return this.surveillanceInspectionDaoImpl.queryEntityHQLList(hql.toString(), objectList, SurveillanceInspection.class);
	}

	@Override
	public HSSFWorkbook export(SurveillanceInspectionVo surveillanceInspectionVo) {
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
		r0_style.cloneStyleFrom(mainStyle);
		r0_style.setAlignment(HorizontalAlignment.CENTER);		//水平居中
		HSSFFont r0_font = wb.createFont();
		r0_font.setBold(true);						//字体加粗
		r0_font.setFontHeightInPoints((short)12);	//字体大小
		r0_style.setFont(r0_font);

		//设置第二行样式(表单编号样式)
		HSSFCellStyle r1_style = wb.createCellStyle();
		r1_style.cloneStyleFrom(mainStyle);
		r1_style.setAlignment(HorizontalAlignment.RIGHT);		//水平靠右
		HSSFFont r1_font = wb.createFont();
		r1_font.setBold(true);						//字体加粗
		r1_style.setFont(r1_font);

		//设置第三行样式(普通列标题样式)
		HSSFCellStyle r2_style = wb.createCellStyle();
		r2_style.cloneStyleFrom(mainStyle_center);	//基础样式 水平居中
		HSSFFont r2_font = wb.createFont();
		r2_font.setBold(true);						//字体加粗
		r2_style.setFont(r2_font);

		List<SurveillanceInspection> siList = queryEntityList(surveillanceInspectionVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("监控巡检汇总");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));


		//创建行（第一行）
		HSSFRow row0 = sheet.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		row0.createCell(0).setCellValue("监控巡检");
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-03");
		row1.getCell(0).setCellStyle(r1_style);

		//第三行
		HSSFRow row3 = sheet.createRow(2);
		row3.setHeightInPoints(40);		//行高
		HSSFCell cell;
		String[] title = {"日期","巡检起止时间段","值班主任","巡检位置","故障设备","巡检情况描述","跟进措施"};
		for(int i=0;i<title.length;i++){
			cell = row3.createCell(i);		//创建单元格
			cell.setCellValue(title[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式

			//列宽自适应（该方法在老版本的POI中效果不佳）
			/*sheet.autoSizeColumn(i);*/
			//设置列宽
			if(i == 5){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*7);
			}else if(i == 6){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*4);
			}else{
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*3/2);
			}
		}

		//第四行 及 之后的行
		HSSFRow row;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		for (int i = 0; i < siList.size(); i++) {
			row = sheet.createRow(i + 3);	//创建行
			row.setHeightInPoints(60);					//设置行高
			for (int j = 0; j < title.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(sdf1.format(siList.get(i).getDutyDate())); break;
					case 1:	cell.setCellValue(sdf.format(siList.get(i).getInspectionTimeStart()) + "--" + sdf.format(siList.get(i).getInspectionTimeEnd()));	break;
					case 2: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_headOfDuty", siList.get(i).getShiftSupervisor().toString()));	break;
					case 3: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_inspectionlocation", siList.get(i).getInspectionlocation().toString()));	break;
					case 4: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_failureEquipment", siList.get(i).getFailureEquipment().toString()));	break;
					case 5: cell.setCellValue(siList.get(i).getInspectionDetails());	break;
					case 6: cell.setCellValue(siList.get(i).getFollowMeasure());	break;
				}
				//设置单元格样式
				if(j == 5){
					cell.setCellStyle(mainStyle_left);
				}else{
					cell.setCellStyle(mainStyle_center);
				}
			}
		}

		return wb;


	}

}
