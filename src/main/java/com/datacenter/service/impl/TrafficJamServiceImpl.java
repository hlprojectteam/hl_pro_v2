package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.dangjian.controller.ActivitiesController;
import com.datacenter.dao.ITrafficJamDao;
import com.datacenter.module.TrafficJam;
import com.datacenter.service.ITrafficJamService;
import com.datacenter.vo.TrafficJamVo;
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
 * @Description 交通阻塞 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("trafficJamServiceImpl")
public class TrafficJamServiceImpl extends BaseServiceImpl implements ITrafficJamService{

	@Autowired
	private ITrafficJamDao trafficJamDaoImpl;

	@Autowired
	private TotalTableServiceImpl totalTableServiceImpl;

	@Autowired
	private ActivitiesController activitiesController;
	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, TrafficJamVo trafficJamVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(trafficJamVo.getTtId())){
			params.add(Restrictions.eq("ttId", trafficJamVo.getTtId()));
		}
		if(trafficJamVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", trafficJamVo.getDutyDateStart()));
		}
		if(trafficJamVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", trafficJamVo.getDutyDateEnd()));
		}

		if(trafficJamVo.getReceiptWay() != null){
			params.add(Restrictions.eq("receiptWay", trafficJamVo.getReceiptWay()));
		}
		if(StringUtils.isNotBlank(trafficJamVo.getKeyword())){
			params.add(Restrictions.sqlRestriction(" (reported_Person like '%" + trafficJamVo.getKeyword() + "%' " +
					" or jam_Section like '%" + trafficJamVo.getKeyword() + "%' " +
					" or jam_Reason like '%" + trafficJamVo.getKeyword() + "%' " +
					" or disposal_Situation like '%" + trafficJamVo.getKeyword() + "%' " +
					" or remark_ like '%" + trafficJamVo.getKeyword() + "%' )"));
		}
		return this.trafficJamDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), TrafficJam.class);
	}

	@Override
	public TrafficJam saveOrUpdate(TrafficJamVo trafficJamVo) {
		if(trafficJamVo.getReceiptWay().equals(4)){
			String newKey = this.activitiesController.addCategoryAttributesByCode("dc_receiptWay", trafficJamVo.getDictValue());
			trafficJamVo.setReceiptWay(Integer.parseInt(newKey));
		}
		TrafficJam trafficJam = new TrafficJam();
		BeanUtils.copyProperties(trafficJamVo, trafficJam);
		if(StringUtils.isBlank(trafficJam.getId())){
			this.save(trafficJam);
		}else{
			this.update(trafficJam);
		}
		return trafficJam;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.trafficJamDaoImpl.excuteBySql("delete from dc_TrafficJam where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<TrafficJam> list = this.trafficJamDaoImpl.queryEntityList(params, Order.desc("createTime"), TrafficJam.class);	//根据主表Id获取子表关联数据
		for (TrafficJam trafficJam : list) {
			trafficJam.setDutyDate(dutyDate);
			this.update(trafficJam);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<TrafficJam> queryEntityList(TrafficJamVo trafficJamVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from TrafficJam where 1 = 1 ");
		if(StringUtils.isNotBlank(trafficJamVo.getTtId())){
			objectList.add(trafficJamVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(trafficJamVo.getDutyDateStart() != null){		//日期Start
			objectList.add(trafficJamVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(trafficJamVo.getDutyDateEnd() != null){		//日期End
			objectList.add(trafficJamVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}

		if(trafficJamVo.getReceiptWay() != null){
			objectList.add(trafficJamVo.getReceiptWay());
			hql.append(" and receiptWay = ? ");
		}
		if(StringUtils.isNotBlank(trafficJamVo.getKeyword())){
			hql.append(" and (reportedPerson like '%" + trafficJamVo.getKeyword() + "%' " +
					" or jamSection like '%" + trafficJamVo.getKeyword() + "%' " +
					" or jamReason like '%" + trafficJamVo.getKeyword() + "%' " +
					" or disposalSituation like '%" + trafficJamVo.getKeyword() + "%' " +
					" or remark like '%" + trafficJamVo.getKeyword() + "%' )");
		}
		//排序, 根据日期倒序排序,接报时间顺序排序
		hql.append(" order by dutyDate desc,receiptTime asc ");

		List<TrafficJam> tjList = this.trafficJamDaoImpl.queryEntityHQLList(hql.toString(), objectList, TrafficJam.class);
		return tjList;
	}

	@Override
	public HSSFWorkbook export(TrafficJamVo trafficJamVo) {
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

		List<TrafficJam> tjList = queryEntityList(trafficJamVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("交通阻塞汇总");

		//设置列宽
		for (int i = 0; i < 8; i++) {
			sheet.setColumnWidth(i, sheet.getColumnWidth(i)*2);
		}


		if(tjList != null && tjList.size() > 0){
			for(int tb = 0; tb < tjList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 9));
				sheet.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 9));
				sheet.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 9));
				sheet.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 5, 6));
				sheet.addMergedRegion(new CellRangeAddress(4 + tb*10, 4 + tb*10, 8, 9));
				sheet.addMergedRegion(new CellRangeAddress(5 + tb*10, 5 + tb*10, 1, 9));
				sheet.addMergedRegion(new CellRangeAddress(6 + tb*10, 6 + tb*10, 1, 9));
				sheet.addMergedRegion(new CellRangeAddress(7 + tb*10, 7 + tb*10, 1, 9));


				//创建行（第一行）
				HSSFRow row0 = sheet.createRow(0 + tb*10);
				//设置行的高度
				row0.setHeightInPoints(30);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(tjList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-13");
				row1.getCell(0).setCellStyle(r1_style);

				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

				//第三行
				HSSFRow row2 = sheet.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + sdf1.format(tjList.get(tb).getDutyDate()));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet.createRow(3 + tb*10);
				row3.setHeightInPoints(40);
				row3.createCell(0).setCellValue("接报时间");
				row3.getCell(0).setCellStyle(r2_style);
				row3.createCell(1).setCellValue(sdf2.format(tjList.get(tb).getReceiptTime()));
				row3.getCell(1).setCellStyle(mainStyle_center);
				row3.createCell(2).setCellValue("接报方式");
				row3.getCell(2).setCellStyle(r2_style);
				row3.createCell(3).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_receiptWay", tjList.get(tb).getReceiptWay().toString()));
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
				HSSFRow row4 = sheet.createRow(4 + tb*10);
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
				row4.createCell(5).setCellValue(sdf2.format(tjList.get(tb).getJjdcTime()));
				row4.getCell(5).setCellStyle(mainStyle_center);
				row4.createCell(7).setCellValue("路管员到场时间");
				row4.getCell(7).setCellStyle(r2_style);
				row4.createCell(8).setCellValue(sdf2.format(tjList.get(tb).getLgydcTime()));
				row4.getCell(8).setCellStyle(mainStyle_center);

				row4.createCell(6).setCellStyle(r2_style);
				row4.createCell(9).setCellStyle(r2_style);


				//第六
				HSSFRow row5 = sheet.createRow(5 + tb*10);
				row5.setHeightInPoints(50);
				row5.createCell(0).setCellValue("阻塞原因");
				row5.getCell(0).setCellStyle(r2_style);
				row5.createCell(1).setCellValue(tjList.get(tb).getJamReason());
				row5.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 10; i++) {
					row5.createCell(i).setCellStyle(r2_style);
				}

				//第七行
				HSSFRow row6 = sheet.createRow(6 + tb*10);
				row6.setHeightInPoints(50);
				row6.createCell(0).setCellValue("处理情况");
				row6.getCell(0).setCellStyle(r2_style);
				row6.createCell(1).setCellValue(tjList.get(tb).getDisposalSituation());
				row6.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 10; i++) {
					row6.createCell(i).setCellStyle(r2_style);
				}

				//第八行
				HSSFRow row7 = sheet.createRow(7 + tb*10);
				row7.setHeightInPoints(50);
				row7.createCell(0).setCellValue("备注");
				row7.getCell(0).setCellStyle(r2_style);
				row7.createCell(1).setCellValue(tjList.get(tb).getRemark());
				row7.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 10; i++) {
					row7.createCell(i).setCellStyle(r2_style);
				}

			}
		}else{		//空表

			//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 9));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 5, 6));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 8, 9));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 9));
			sheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 9));
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 1, 9));


			//创建行（第一行）
			HSSFRow row0 = sheet.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("交通阻塞");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-13");
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
			HSSFRow row4 = sheet.createRow(4);
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
			HSSFRow row5 = sheet.createRow(5);
			row5.setHeightInPoints(50);
			row5.createCell(0).setCellValue("阻塞原因");
			row5.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 10; i++) {
				row5.createCell(i).setCellStyle(r2_style);
			}

			//第七行
			HSSFRow row6 = sheet.createRow(6);
			row6.setHeightInPoints(50);
			row6.createCell(0).setCellValue("处理情况");
			row6.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 10; i++) {
				row6.createCell(i).setCellStyle(r2_style);
			}

			//第八行
			HSSFRow row7 = sheet.createRow(7);
			row7.setHeightInPoints(50);
			row7.createCell(0).setCellValue("备注");
			row7.getCell(0).setCellStyle(r2_style);
			for (int i = 1; i < 10; i++) {
				row7.createCell(i).setCellStyle(r2_style);
			}
		}

		return wb;


	}

}
