package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.datacenter.dao.IFieldOperationsDao;
import com.datacenter.module.FieldOperations;
import com.datacenter.service.IFieldOperationsService;
import com.datacenter.vo.FieldOperationsVo;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 外勤作业 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("fieldOperationsServiceImpl")
public class FieldOperationsServiceImpl extends BaseServiceImpl implements IFieldOperationsService{

	@Autowired
	private IFieldOperationsDao fieldOperationsDaoImpl;

	@Autowired
	private TotalTableServiceImpl totalTableServiceImpl;
	@Autowired
	public IDataDictionaryService dataDictionaryServiceImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, FieldOperationsVo fieldOperationsVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(fieldOperationsVo.getTtId())){
			params.add(Restrictions.eq("ttId", fieldOperationsVo.getTtId()));
		}
		if(fieldOperationsVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", fieldOperationsVo.getDutyDateStart()));
		}
		if(fieldOperationsVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", fieldOperationsVo.getDutyDateEnd()));
		}

		if(fieldOperationsVo.getReceiptWay() != null){
			params.add(Restrictions.eq("receiptWay", fieldOperationsVo.getReceiptWay()));
		}
		if(StringUtils.isNotBlank(fieldOperationsVo.getScene())){
			params.add(Restrictions.like("scene", "%" + fieldOperationsVo.getScene() + "%"));
		}

		if(StringUtils.isNotBlank(fieldOperationsVo.getKeyword())){
			params.add(Restrictions.sqlRestriction(" (reported_Person like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or out_worker like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or scene_ like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or involved_Units like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or violation_OrderNo like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or receipt_Situation like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or dispose_Desc like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or remark_ like '%" + fieldOperationsVo.getKeyword() + "%' )"));
		}
		return this.fieldOperationsDaoImpl.queryEntityList(page, rows, params, Order.desc("dutyDate"), FieldOperations.class);
	}

	@Override
	public FieldOperations saveOrUpdate(FieldOperationsVo fieldOperationsVo) {
		if(fieldOperationsVo.getReceiptWay().equals(99)){
			String newKey = this.dataDictionaryServiceImpl.updateCategoryAttributesByCode("dc_receiptWay", fieldOperationsVo.getDictValue());
			fieldOperationsVo.setReceiptWay(Integer.parseInt(newKey));
		}
		FieldOperations fieldOperations = new FieldOperations();
		BeanUtils.copyProperties(fieldOperationsVo, fieldOperations);
		if(StringUtils.isBlank(fieldOperations.getId())){
			this.save(fieldOperations);
		}else{
			this.update(fieldOperations);
		}
		return fieldOperations;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.fieldOperationsDaoImpl.excuteBySql("delete from dc_FieldOperations where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<FieldOperations> list = this.fieldOperationsDaoImpl.queryEntityList(params, Order.desc("createTime"), FieldOperations.class);	//根据主表Id获取子表关联数据
		for (FieldOperations fieldOperations : list) {
			fieldOperations.setDutyDate(dutyDate);
			this.update(fieldOperations);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<FieldOperations> queryEntityList(FieldOperationsVo fieldOperationsVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from FieldOperations where 1 = 1 ");
		if(StringUtils.isNotBlank(fieldOperationsVo.getTtId())){
			objectList.add(fieldOperationsVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(fieldOperationsVo.getDutyDateStart() != null){		//日期Start
			objectList.add(fieldOperationsVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(fieldOperationsVo.getDutyDateEnd() != null){		//日期End
			objectList.add(fieldOperationsVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		if(fieldOperationsVo.getReceiptWay() != null){
			objectList.add(fieldOperationsVo.getReceiptWay());
			hql.append(" and receiptWay = ? ");
		}
		if(StringUtils.isNotBlank(fieldOperationsVo.getScene())){
			objectList.add("%" + fieldOperationsVo.getScene() + "%");
			hql.append(" and scene like ? ");
		}

		if(StringUtils.isNotBlank(fieldOperationsVo.getKeyword())){
			hql.append(" and (reportedPerson like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or outworker like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or scene like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or involvedUnits like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or violationOrderNo like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or receiptSituation like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or disposeDesc like '%" + fieldOperationsVo.getKeyword() + "%' " +
					" or remark like '%" + fieldOperationsVo.getKeyword() + "%' )");
		}
		//排序, 根据日期倒序排序,接报时间顺序排序
		hql.append(" order by dutyDate asc,receiptTime asc ");

		List<FieldOperations> trList = this.fieldOperationsDaoImpl.queryEntityHQLList(hql.toString(), objectList, FieldOperations.class);
		return trList;
	}

	@Override
	public HSSFWorkbook export(FieldOperationsVo fieldOperationsVo) {
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


		List<FieldOperations> foList = queryEntityList(fieldOperationsVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("外勤作业汇总");

		//设置列宽
		for (int i = 0; i < 8; i++) {
			if(i == 7){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*5/2);
			}else{
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*2);
			}
		}


		if(foList != null && foList.size() > 0){
			for(int tb = 0; tb < foList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 7));
				sheet.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 7));
				sheet.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 7));
				sheet.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 1, 2));
				sheet.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 6, 7));
				sheet.addMergedRegion(new CellRangeAddress(5 + tb*10, 5 + tb*10, 1, 7));
				sheet.addMergedRegion(new CellRangeAddress(6 + tb*10, 6 + tb*10, 1, 7));
				sheet.addMergedRegion(new CellRangeAddress(7 + tb*10, 7 + tb*10, 1, 7));


				//创建行（第一行）
				HSSFRow row0 = sheet.createRow(0 + tb*10);
				//设置行的高度
				row0.setHeightInPoints(30);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(foList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-15");
				row1.getCell(0).setCellStyle(r1_style);


				//第三行
				HSSFRow row2 = sheet.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + DateUtil.getDateFormatString
						(foList.get(tb).getDutyDate(),DateUtil.JAVA_DATE_FORMAT_CH_YMD));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet.createRow(3 + tb*10);
				row3.setHeightInPoints(40);
				row3.createCell(0).setCellValue("接报时间");
				row3.getCell(0).setCellStyle(r2_style);
				row3.createCell(1).setCellValue(DateUtil.getDateFormatString
						(foList.get(tb).getReceiptTime(),DateUtil.JAVA_DATE_FORMAT_HM));
				row3.getCell(1).setCellStyle(mainStyle_center);
				row3.createCell(2).setCellValue("报告人员");
				row3.getCell(2).setCellStyle(r2_style);
				row3.createCell(3).setCellValue(foList.get(tb).getReportedPerson());
				row3.getCell(3).setCellStyle(mainStyle_center);
				row3.createCell(4).setCellValue("接报方式");
				row3.getCell(4).setCellStyle(r2_style);
				row3.createCell(5).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_receiptWay", foList.get(tb).getReceiptWay().toString()));
				row3.getCell(5).setCellStyle(mainStyle_center);
				row3.createCell(6).setCellValue("外勤人员");
				row3.getCell(6).setCellStyle(r2_style);
				row3.createCell(7).setCellValue(foList.get(tb).getOutworker());
				row3.getCell(7).setCellStyle(mainStyle_center);

				//第五行
				HSSFRow row4 = sheet.createRow(4 + tb*10);
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
				HSSFRow row5 = sheet.createRow(5 + tb*10);
				row5.setHeightInPoints(50);
				row5.createCell(0).setCellValue("接报情况");
				row5.getCell(0).setCellStyle(r2_style);
				row5.createCell(1).setCellValue(foList.get(tb).getReceiptSituation());
				row5.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row5.createCell(i).setCellStyle(r2_style);
				}

				//第七行
				HSSFRow row6 = sheet.createRow(6 + tb*10);
				row6.setHeightInPoints(50);
				row6.createCell(0).setCellValue("处置简述");
				row6.getCell(0).setCellStyle(r2_style);
				row6.createCell(1).setCellValue(foList.get(tb).getDisposeDesc());
				row6.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row6.createCell(i).setCellStyle(r2_style);
				}

				//第八行
				HSSFRow row7 = sheet.createRow(7 + tb*10);
				row7.setHeightInPoints(50);
				row7.createCell(0).setCellValue("备注 ");
				row7.getCell(0).setCellStyle(r2_style);
				row7.createCell(1).setCellValue(foList.get(tb).getRemark());
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
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 2));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 7));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 7));
			sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 7));
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 7));


			//创建行（第一行）
			HSSFRow row0 = sheet.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("外勤作业 ");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-14");
			row1.getCell(0).setCellStyle(r1_style);

			//第三行
			HSSFRow row2 = sheet.createRow(2);
			row2.setHeightInPoints(25);
			row2.createCell(0).setCellValue("日期：         ");
			row2.getCell(0).setCellStyle(r1_style);

			//第四行
			HSSFRow row3 = sheet.createRow(3);
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
			HSSFRow row4 = sheet.createRow(4);
			row4.setHeightInPoints(50);
			row4.createCell(0).setCellValue("事发地点 ");
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
			HSSFRow row5 = sheet.createRow(5);
			row5.setHeightInPoints(50);
			row5.createCell(0).setCellValue("接报情况");
			row5.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 8; i++) {
				row5.createCell(i).setCellStyle(r2_style);
			}

			//第七行
			HSSFRow row6 = sheet.createRow(6);
			row6.setHeightInPoints(50);
			row6.createCell(0).setCellValue("处置简述");
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
