package com.datacenter.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dangjian.controller.ActivitiesController;
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
import com.datacenter.dao.IExceptionRecordDao;
import com.datacenter.module.ExceptionRecord;
import com.datacenter.service.IExceptionRecordService;
import com.datacenter.vo.ExceptionRecordVo;

/**
 * @Description 营运异常记录 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("exceptionRecordServiceImpl")
public class ExceptionRecordServiceImpl extends BaseServiceImpl implements IExceptionRecordService{

	@Autowired
	private IExceptionRecordDao exceptionRecordDaoImpl;

	@Autowired
	private TotalTableServiceImpl totalTableServiceImpl;

	@Autowired
	private ActivitiesController activitiesController;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, ExceptionRecordVo exceptionRecordVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(exceptionRecordVo.getTtId())){
			params.add(Restrictions.eq("ttId", exceptionRecordVo.getTtId()));
		}
		if(exceptionRecordVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", exceptionRecordVo.getDutyDateStart()));
		}
		if(exceptionRecordVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", exceptionRecordVo.getDutyDateEnd()));
		}

		if(StringUtils.isNotBlank(exceptionRecordVo.getReportedDp())){
			params.add(Restrictions.like("reportedDp", "%" + exceptionRecordVo.getReportedDp() + "%"));
		}
		if(StringUtils.isNotBlank(exceptionRecordVo.getProcessingDp())){
			params.add(Restrictions.like("processingDp", "%" + exceptionRecordVo.getProcessingDp() + "%"));
		}
		if(StringUtils.isNotBlank(exceptionRecordVo.getKeyword())){
			params.add(Restrictions.sqlRestriction(" (reported_Dp like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or reported_Person like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or traffic_Road like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or processing_Dp like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or brief_Introduction like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or result_ like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or remark_ like '%" + exceptionRecordVo.getKeyword() + "%' )"));
		}
		return this.exceptionRecordDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), ExceptionRecord.class);
	}

	@Override
	public ExceptionRecord saveOrUpdate(ExceptionRecordVo exceptionRecordVo) {
		if(exceptionRecordVo.getReportedWay() != null && exceptionRecordVo.getReportedWay().equals(5)){
			String newKey = this.activitiesController.addCategoryAttributesByCode("dc_reportedWay_ER", exceptionRecordVo.getDictValue());
			exceptionRecordVo.setReportedWay(Integer.parseInt(newKey));
		}
		ExceptionRecord exceptionRecord = new ExceptionRecord();
		BeanUtils.copyProperties(exceptionRecordVo, exceptionRecord);
		if(StringUtils.isBlank(exceptionRecord.getId())){
			this.save(exceptionRecord);
		}else{
			this.update(exceptionRecord);
		}
		return exceptionRecord;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.exceptionRecordDaoImpl.excuteBySql("delete from dc_ExceptionRecord where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<ExceptionRecord> list = this.exceptionRecordDaoImpl.queryEntityList(params, Order.desc("createTime"), ExceptionRecord.class);	//根据主表Id获取子表关联数据
		for (ExceptionRecord exceptionRecord : list) {
			exceptionRecord.setDutyDate(dutyDate);
			this.update(exceptionRecord);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<ExceptionRecord> queryEntityList(ExceptionRecordVo exceptionRecordVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from ExceptionRecord where 1 = 1 ");
		if(StringUtils.isNotBlank(exceptionRecordVo.getTtId())){
			objectList.add(exceptionRecordVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(exceptionRecordVo.getDutyDateStart() != null){		//日期Start
			objectList.add(exceptionRecordVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(exceptionRecordVo.getDutyDateEnd() != null){		//日期End
			objectList.add(exceptionRecordVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}

		if(StringUtils.isNotBlank(exceptionRecordVo.getReportedDp())){
			objectList.add("%" + exceptionRecordVo.getReportedDp() + "%");
			hql.append(" and reportedDp like ? ");
		}
		if(StringUtils.isNotBlank(exceptionRecordVo.getProcessingDp())){
			objectList.add("%" + exceptionRecordVo.getProcessingDp() + "%");
			hql.append(" and processingDp like ? ");
		}
		if(StringUtils.isNotBlank(exceptionRecordVo.getKeyword())){
			hql.append(" and (reportedDp like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or reportedPerson like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or trafficRoad like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or processingDp like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or briefIntroduction like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or result like '%" + exceptionRecordVo.getKeyword() + "%' " +
					" or remark like '%" + exceptionRecordVo.getKeyword() + "%' )");
		}
		//排序, 根据日期倒序排序,异常类型顺序排序
		hql.append(" order by dutyDate desc,exceptionType asc ");

		List<ExceptionRecord> erList = this.exceptionRecordDaoImpl.queryEntityHQLList(hql.toString(), objectList, ExceptionRecord.class);
		return erList;
	}

	@Override
	public HSSFWorkbook export(ExceptionRecordVo exceptionRecordVo) {
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


		List<ExceptionRecord> erList = queryEntityList(exceptionRecordVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("营运异常记录汇总");

		//设置列宽
		for (int i = 0; i < 8; i++) {
			if(i == 7){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*5/2);
			}else{
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*2);
			}
		}


		if(erList != null && erList.size() > 0){
			for(int tb = 0; tb < erList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 7));
				sheet.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 7));
				sheet.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 7));
				if(erList.get(tb).getExceptionType() == 1){
					sheet.addMergedRegion(new CellRangeAddress(3 + tb*10, 3 + tb*10, 1, 3));
					sheet.addMergedRegion(new CellRangeAddress(3 + tb*10, 3 + tb*10, 5, 7));
				}
				sheet.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 1, 3));
				sheet.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 5, 7));
				sheet.addMergedRegion(new CellRangeAddress(5 + tb*10, 5 + tb*10, 1, 7));
				sheet.addMergedRegion(new CellRangeAddress(6 + tb*10, 6 + tb*10, 1, 7));
				sheet.addMergedRegion(new CellRangeAddress(7 + tb*10, 7 + tb*10, 1, 7));


				//创建行（第一行）
				HSSFRow row0 = sheet.createRow(0 + tb*10);
				//设置行的高度
				row0.setHeightInPoints(30);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(erList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-09");
				row1.getCell(0).setCellStyle(r1_style);

				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

				//第三行
				HSSFRow row2 = sheet.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + sdf1.format(erList.get(tb).getDutyDate()));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet.createRow(3 + tb*10);
				if(erList.get(tb).getExceptionType() == 1){
					row3.setHeightInPoints(40);
					row3.createCell(0).setCellValue("报告部门 ");
					row3.getCell(0).setCellStyle(r2_style);
					row3.createCell(1).setCellValue(erList.get(tb).getReportedDp());
					row3.getCell(1).setCellStyle(mainStyle_center);
					row3.createCell(4).setCellValue("报告人员");
					row3.getCell(4).setCellStyle(r2_style);
					row3.createCell(5).setCellValue(erList.get(tb).getReportedPerson());
					row3.getCell(5).setCellStyle(mainStyle_center);
					for (int i = 2; i < 8; i++) {
						if(i != 4 && i!= 5){
							row3.createCell(i).setCellStyle(r2_style);
						}
					}
				}else{
					row3.setHeightInPoints(40);
					row3.createCell(0).setCellValue("接报时间 ");
					row3.getCell(0).setCellStyle(r2_style);
					row3.createCell(1).setCellValue(sdf2.format(erList.get(tb).getReceiptTime()));
					row3.getCell(1).setCellStyle(mainStyle_center);
					row3.createCell(2).setCellValue("报告部门");
					row3.getCell(2).setCellStyle(r2_style);
					row3.createCell(3).setCellValue(erList.get(tb).getReportedDp());
					row3.getCell(3).setCellStyle(mainStyle_center);
					row3.createCell(4).setCellValue("报告人员");
					row3.getCell(4).setCellStyle(r2_style);
					row3.createCell(5).setCellValue(erList.get(tb).getReportedPerson());
					row3.getCell(5).setCellStyle(mainStyle_center);
					row3.createCell(6).setCellValue("报告方式");
					row3.getCell(6).setCellStyle(r2_style);
					row3.createCell(7).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_reportedWay_ER", erList.get(tb).getReportedWay().toString()));
					row3.getCell(7).setCellStyle(mainStyle_center);
				}


				//第五行
				HSSFRow row4 = sheet.createRow(4 + tb*10);
				row4.setHeightInPoints(40);
				row4.createCell(0).setCellValue("通行路段");
				row4.getCell(0).setCellStyle(r2_style);
				row4.createCell(1).setCellValue(erList.get(tb).getTrafficRoad());
				row4.getCell(1).setCellStyle(mainStyle_center);
				row4.createCell(4).setCellValue("通知处理部门");
				row4.getCell(4).setCellStyle(r2_style);
				row4.createCell(5).setCellValue(erList.get(tb).getProcessingDp());
				row4.getCell(5).setCellStyle(mainStyle_center);
				for (int i = 2; i < 8; i++) {
					if(i != 4 && i!= 5){
						row4.createCell(i).setCellStyle(r2_style);
					}
				}

				//第六
				HSSFRow row5 = sheet.createRow(5 + tb*10);
				row5.setHeightInPoints(50);
				row5.createCell(0).setCellValue("情况简述");
				row5.getCell(0).setCellStyle(r2_style);
				row5.createCell(1).setCellValue(erList.get(tb).getBriefIntroduction());
				row5.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row5.createCell(i).setCellStyle(r2_style);
				}

				//第七行
				HSSFRow row6 = sheet.createRow(6 + tb*10);
				row6.setHeightInPoints(50);
				row6.createCell(0).setCellValue("处理结果");
				row6.getCell(0).setCellStyle(r2_style);
				row6.createCell(1).setCellValue(erList.get(tb).getResult());
				row6.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row6.createCell(i).setCellStyle(r2_style);
				}

				//第八行
				HSSFRow row7 = sheet.createRow(7 + tb*10);
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
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 3));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 5, 7));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 7));
			sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 7));
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 7));


			//创建行（第一行）
			HSSFRow row0 = sheet.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("异常记录");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-09");
			row1.getCell(0).setCellStyle(r1_style);

			//第三行
			HSSFRow row2 = sheet.createRow(2);
			row2.setHeightInPoints(25);
			row2.createCell(0).setCellValue("日期：          ");
			row2.getCell(0).setCellStyle(r1_style);

			//第四行
			HSSFRow row3 = sheet.createRow(3);
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
			HSSFRow row4 = sheet.createRow(4);
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
			HSSFRow row5 = sheet.createRow(5);
			row5.setHeightInPoints(50);
			row5.createCell(0).setCellValue("情况简述");
			row5.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row5.createCell(i).setCellStyle(r2_style);
			}

			//第七行
			HSSFRow row6 = sheet.createRow(6);
			row6.setHeightInPoints(50);
			row6.createCell(0).setCellValue("处理结果");
			row6.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row6.createCell(i).setCellStyle(r2_style);
			}

			//第八行
			HSSFRow row7 = sheet.createRow(7);
			row7.setHeightInPoints(50);
			row7.createCell(0).setCellValue("备注");
			row7.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row7.createCell(i).setCellStyle(r2_style);
			}
		}

		return wb;

	}


}
