package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.datacenter.dao.IClearingDao;
import com.datacenter.module.Clearing;
import com.datacenter.service.IClearingService;
import com.datacenter.vo.ClearingVo;
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
 * @Description 清障保洁 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("clearingServiceImpl")
public class ClearingServiceImpl extends BaseServiceImpl implements IClearingService{

	@Autowired
	private IClearingDao clearingDaoImpl;

	@Autowired
	private TotalTableServiceImpl totalTableServiceImpl;
	
	@Autowired
	public IDataDictionaryService dataDictionaryServiceImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, ClearingVo clearingVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(clearingVo.getTtId())){
			params.add(Restrictions.eq("ttId", clearingVo.getTtId()));
		}
		if(clearingVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", clearingVo.getDutyDateStart()));
		}
		if(clearingVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", clearingVo.getDutyDateEnd()));
		}

		if(StringUtils.isNotBlank(clearingVo.getReportedDp())){
			params.add(Restrictions.eq("reportedDp", clearingVo.getReportedDp()));
		}
		if(StringUtils.isNotBlank(clearingVo.getReportedPerson())){
			params.add(Restrictions.like("reportedPerson", "%"+clearingVo.getReportedPerson()+"%"));
		}
		if(StringUtils.isNotBlank(clearingVo.getProcessingDp())){
			params.add(Restrictions.eq("processingDp", clearingVo.getProcessingDp()));
		}
		if(clearingVo.getReportedWay() != null){
			params.add(Restrictions.eq("reportedWay", clearingVo.getReportedWay()));
		}
		if(StringUtils.isNotBlank(clearingVo.getKeyword())){
			params.add(Restrictions.sqlRestriction(" ( " +
					" traffic_Road like '%" + clearingVo.getKeyword() + "%' " +
					" or brief_Introduction like '%" + clearingVo.getKeyword() + "%' " +
					" or reported_Dp like '%" + clearingVo.getKeyword() + "%' " +
					" or reported_Person like '%" + clearingVo.getKeyword() + "%' " +
					" or processing_Dp like '%" + clearingVo.getKeyword() + "%' " +
					" or result_ like '%" + clearingVo.getKeyword() + "%' " +
					" or remark_ like '%" + clearingVo.getKeyword() + "%' )"));
		}
		return this.clearingDaoImpl.queryEntityList(page, rows, params, Order.desc("dutyDate"), Clearing.class);
	}

	@Override
	public Clearing saveOrUpdate(ClearingVo clearingVo) {
		if(clearingVo.getReportedDp().equals("其它")){
			clearingVo.setReportedDp(clearingVo.getDictValue());
		}
		if(clearingVo.getReportedPerson().equals("其它")){
			clearingVo.setReportedPerson(clearingVo.getDictValue2());
		}
		if(clearingVo.getProcessingDp().equals("其它")){
			clearingVo.setProcessingDp(clearingVo.getDictValue3());
		}
		Clearing clearing = new Clearing();
		BeanUtils.copyProperties(clearingVo, clearing);
		if(StringUtils.isBlank(clearing.getId())){
			this.save(clearing);
		}else{
			this.update(clearing);
		}
		return clearing;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.clearingDaoImpl.excuteBySql("delete from dc_Clearing where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<Clearing> list = this.clearingDaoImpl.queryEntityList(params, Order.desc("createTime"), Clearing.class);	//根据主表Id获取子表关联数据
		for (Clearing clearing : list) {
			clearing.setDutyDate(dutyDate);
			this.update(clearing);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<Clearing> queryEntityList(ClearingVo clearingVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from Clearing where 1 = 1 ");
		if(StringUtils.isNotBlank(clearingVo.getTtId())){
			objectList.add(clearingVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(clearingVo.getDutyDateStart() != null){		//日期Start
			objectList.add(clearingVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(clearingVo.getDutyDateEnd() != null){		//日期End
			objectList.add(clearingVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		if(StringUtils.isNotBlank(clearingVo.getReportedDp())){
			objectList.add(clearingVo.getReportedDp());
			hql.append(" and reportedDp = ? ");
		}
		if(StringUtils.isNotBlank(clearingVo.getReportedPerson())){
			objectList.add("%"+clearingVo.getReportedPerson()+"%");
			hql.append(" and reportedPerson like ? ");
		}
		if(StringUtils.isNotBlank(clearingVo.getProcessingDp())){
			objectList.add(clearingVo.getProcessingDp());
			hql.append(" and processingDp = ? ");
		}
		if(clearingVo.getReportedWay() != null){
			objectList.add(clearingVo.getReportedWay());
			hql.append(" and reportedWay = ? ");
		}
		if(StringUtils.isNotBlank(clearingVo.getKeyword())){
			hql.append(" and (reportedDp like '%" + clearingVo.getKeyword() + "%' " +
					" or reportedPerson like '%" + clearingVo.getKeyword() + "%' " +
					" or trafficRoad like '%" + clearingVo.getKeyword() + "%' " +
					" or processingDp like '%" + clearingVo.getKeyword() + "%' " +
					" or briefIntroduction like '%" + clearingVo.getKeyword() + "%' " +
					" or result like '%" + clearingVo.getKeyword() + "%' " +
					" or remark like '%" + clearingVo.getKeyword() + "%' )");
		}
		//排序, 根据日期倒序排序,接报时间
		hql.append(" order by dutyDate asc,receiptTime asc ");

		List<Clearing> cList = this.clearingDaoImpl.queryEntityHQLList(hql.toString(), objectList, Clearing.class);
		return cList;
	}

	@Override
	public HSSFWorkbook export(ClearingVo clearingVo) {

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

//		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		List<Clearing> cList = queryEntityList(clearingVo);

		
		//创建sheet
		HSSFSheet sheet = wb.createSheet("清障保洁汇总");


		//设置列宽
		for (int i = 0; i < 8; i++) {
			if(i == 7){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*5/2);
			}else{
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*2);
			}
		}


		if(cList != null && cList.size() > 0){
			for(int tb = 0; tb < cList.size(); tb++){		//有多少条记录就有多少张表


				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 7));
				sheet.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 7));
				sheet.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 7));
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
				row0.createCell(0).setCellValue(cList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-09");
				row1.getCell(0).setCellStyle(r1_style);

//				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

				//第三行
				HSSFRow row2 = sheet.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + DateUtil.getDateFormatString(cList.get(tb).getDutyDate(),DateUtil.JAVA_DATE_FORMAT_CH_YMD));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet.createRow(3 + tb*10);
				row3.setHeightInPoints(40);
				row3.createCell(0).setCellValue("接报时间");
				row3.getCell(0).setCellStyle(r2_style);
				row3.createCell(1).setCellValue(DateUtil.getDateFormatString
						(cList.get(tb).getReceiptTime(),DateUtil.JAVA_DATE_FORMAT_HM));
				row3.getCell(1).setCellStyle(mainStyle_center);
				row3.createCell(2).setCellValue("报告部门");
				row3.getCell(2).setCellStyle(r2_style);
				row3.createCell(3).setCellValue(cList.get(tb).getReportedDp());
				row3.getCell(3).setCellStyle(mainStyle_center);
				row3.createCell(4).setCellValue("报告人员");
				row3.getCell(4).setCellStyle(r2_style);
				row3.createCell(5).setCellValue(cList.get(tb).getReportedPerson());
				row3.getCell(5).setCellStyle(mainStyle_center);
				row3.createCell(6).setCellValue("报告方式");
				row3.getCell(6).setCellStyle(r2_style);
				row3.createCell(7).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_reportedWay", cList.get(tb).getReportedWay().toString()));
				row3.getCell(7).setCellStyle(mainStyle_center);

				//第五行
				HSSFRow row4 = sheet.createRow(4 + tb*10);
				row4.setHeightInPoints(40);
				row4.createCell(0).setCellValue("通行路段");
				row4.getCell(0).setCellStyle(r2_style);
				row4.createCell(1).setCellValue(cList.get(tb).getTrafficRoad());
				row4.getCell(1).setCellStyle(mainStyle_center);
				row4.createCell(4).setCellValue("通知处理部门");
				row4.getCell(4).setCellStyle(r2_style);
				row4.createCell(5).setCellValue(cList.get(tb).getProcessingDp());
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
				row5.createCell(1).setCellValue(cList.get(tb).getBriefIntroduction());
				row5.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row5.createCell(i).setCellStyle(r2_style);
				}

				//第七行
				HSSFRow row6 = sheet.createRow(6 + tb*10);
				row6.setHeightInPoints(50);
				row6.createCell(0).setCellValue("处理结果");
				row6.getCell(0).setCellStyle(r2_style);
				row6.createCell(1).setCellValue(cList.get(tb).getResult());
				row6.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 8; i++) {
					row6.createCell(i).setCellStyle(r2_style);
				}

				//第八行
				HSSFRow row7 = sheet.createRow(7 + tb*10);
				row7.setHeightInPoints(50);
				row7.createCell(0).setCellValue("备注");
				row7.getCell(0).setCellStyle(r2_style);
				row7.createCell(1).setCellValue(cList.get(tb).getRemark());
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
			row0.createCell(0).setCellValue("清障保洁");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-08");
			row1.getCell(0).setCellStyle(r1_style);

			//第三行
			HSSFRow row2 = sheet.createRow(2);
			row2.setHeightInPoints(25);
			row2.createCell(0).setCellValue("日期：    ");
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
