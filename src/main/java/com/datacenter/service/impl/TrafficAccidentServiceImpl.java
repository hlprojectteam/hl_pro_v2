package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.dangjian.controller.ActivitiesController;
import com.datacenter.dao.ITrafficAccidentDao;
import com.datacenter.module.TrafficAccident;
import com.datacenter.service.ITrafficAccidentService;
import com.datacenter.vo.TrafficAccidentVo;
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
 * @Description 交通事故 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("trafficAccidentServiceImpl")
public class TrafficAccidentServiceImpl extends BaseServiceImpl implements ITrafficAccidentService{

	@Autowired
	private ITrafficAccidentDao trafficAccidentDaoImpl;

	@Autowired
	private TotalTableServiceImpl totalTableServiceImpl;

	@Autowired
	private ActivitiesController activitiesController;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, TrafficAccidentVo trafficAccidentVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(trafficAccidentVo.getTtId())){
			params.add(Restrictions.eq("ttId", trafficAccidentVo.getTtId()));
		}
		if(trafficAccidentVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", trafficAccidentVo.getDutyDateStart()));
		}
		if(trafficAccidentVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", trafficAccidentVo.getDutyDateEnd()));
		}

		if(trafficAccidentVo.getWeather() != null){
			params.add(Restrictions.eq("weather", trafficAccidentVo.getWeather()));
		}
		if(trafficAccidentVo.getAccidentType() != null){
			params.add(Restrictions.eq("accidentType", trafficAccidentVo.getAccidentType()));
		}
		if(StringUtils.isNotBlank(trafficAccidentVo.getAccidentSite())){
			params.add(Restrictions.like("accidentSite", "%" + trafficAccidentVo.getAccidentSite() + "%"));
		}
		if(trafficAccidentVo.getMinorInjuryNum() != null){
			params.add(Restrictions.eq("minorInjuryNum", trafficAccidentVo.getMinorInjuryNum()));
		}
		if(trafficAccidentVo.getSeriousInjuryNum() != null){
			params.add(Restrictions.eq("seriousInjuryNum", trafficAccidentVo.getSeriousInjuryNum()));
		}
		if(trafficAccidentVo.getDeathNum() != null){
			params.add(Restrictions.like("deathNum", trafficAccidentVo.getDeathNum()));
		}
		if(StringUtils.isNotBlank(trafficAccidentVo.getKeyword())){
			params.add(Restrictions.sqlRestriction(" (accident_Site like '%" + trafficAccidentVo.getKeyword() + "%' " +
					" or involveCar_Num like '%" + trafficAccidentVo.getKeyword() + "%' " +
					" or involve_Plates like '%" + trafficAccidentVo.getKeyword() + "%' " +
					" or road_Loss like '%" + trafficAccidentVo.getKeyword() + "%' " +
					" or claim_Note like '%" + trafficAccidentVo.getKeyword() + "%' " +
					" or accident_Details like '%" + trafficAccidentVo.getKeyword() + "%' " +
					" or remark_ like '%" + trafficAccidentVo.getKeyword() + "%' )"));
		}
		return this.trafficAccidentDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), TrafficAccident.class);
	}

	@Override
	public TrafficAccident saveOrUpdate(TrafficAccidentVo trafficAccidentVo) {
		if(trafficAccidentVo.getReceiptWay().equals(4)){
			String newKey = this.activitiesController.addCategoryAttributesByCode("dc_receiptWay", trafficAccidentVo.getDictValue());
			trafficAccidentVo.setReceiptWay(Integer.parseInt(newKey));
		}
		if(trafficAccidentVo.getSource().equals(4)){
			String newKey2 = this.activitiesController.addCategoryAttributesByCode("dc_source", trafficAccidentVo.getDictValue2());
			trafficAccidentVo.setSource(Integer.parseInt(newKey2));
		}
		if(trafficAccidentVo.getAccidentType().equals(7)){
			String newKey3 = this.activitiesController.addCategoryAttributesByCode("dc_accidentType", trafficAccidentVo.getDictValue3());
			trafficAccidentVo.setAccidentType(Integer.parseInt(newKey3));
		}
		/*if(trafficAccidentVo.getCarType().equals(8)){
			String newKey4 = this.activitiesController.addCategoryAttributesByCode("dc_carType", trafficAccidentVo.getDictValue4());
			trafficAccidentVo.setCarType(Integer.parseInt(newKey4));
		}*/
		TrafficAccident trafficAccident = new TrafficAccident();
		BeanUtils.copyProperties(trafficAccidentVo, trafficAccident);
		if(StringUtils.isBlank(trafficAccident.getId())){
			this.save(trafficAccident);
		}else{
			this.update(trafficAccident);
		}
		return trafficAccident;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.trafficAccidentDaoImpl.excuteBySql("delete from dc_TrafficAccident where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<TrafficAccident> list = this.trafficAccidentDaoImpl.queryEntityList(params, Order.desc("createTime"), TrafficAccident.class);	//根据主表Id获取子表关联数据
		for (TrafficAccident trafficAccident : list) {
			trafficAccident.setDutyDate(dutyDate);
			this.update(trafficAccident);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<TrafficAccident> queryEntityList(TrafficAccidentVo trafficAccidentVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from TrafficAccident where 1 = 1 ");
		if(StringUtils.isNotBlank(trafficAccidentVo.getTtId())){
			objectList.add(trafficAccidentVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(trafficAccidentVo.getDutyDateStart() != null){		//日期Start
			objectList.add(trafficAccidentVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(trafficAccidentVo.getDutyDateEnd() != null){		//日期End
			objectList.add(trafficAccidentVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		//排序, 根据日期倒序排序，接报时间顺序排序
		hql.append(" order by dutyDate desc,receiptTime asc ");

		List<TrafficAccident> trList = this.trafficAccidentDaoImpl.queryEntityHQLList(hql.toString(), objectList, TrafficAccident.class);
		return trList;
	}

	@Override
	public HSSFWorkbook export(TrafficAccidentVo trafficAccidentVo) {
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

		List<TrafficAccident> taList = queryEntityList(trafficAccidentVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("交通事故汇总");

		//设置列宽
		for (int i = 0; i < 8; i++) {
			sheet.setColumnWidth(i, sheet.getColumnWidth(i)*2);
		}


		if(taList != null && taList.size() > 0){
			for(int tb = 0; tb < taList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet.addMergedRegion(new CellRangeAddress(0 + tb*10, 0 + tb*10, 0, 7));
				sheet.addMergedRegion(new CellRangeAddress(1 + tb*10, 1 + tb*10, 0, 7));
				sheet.addMergedRegion(new CellRangeAddress(2 + tb*10, 2 + tb*10, 0, 7));
				sheet.addMergedRegion(new CellRangeAddress(7 + tb*10, 7 + tb*10, 0, 7));
				sheet.addMergedRegion(new CellRangeAddress(8 + tb*10, 8 + tb*10, 0, 7));


				//创建行（第一行）
				HSSFRow row0 = sheet.createRow(0 + tb*10);
				//设置行的高度
				row0.setHeightInPoints(30);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(taList.get(tb).getTitle());
				//设置单元格样式
				row0.getCell(0).setCellStyle(r0_style);

				//第二行
				HSSFRow row1 = sheet.createRow(1 + tb*10);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-10");
				row1.getCell(0).setCellStyle(r1_style);

				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

				//第三行
				HSSFRow row2 = sheet.createRow(2 + tb*10);
				row2.setHeightInPoints(25);
				row2.createCell(0).setCellValue("日期：" + sdf1.format(taList.get(tb).getDutyDate()));
				row2.getCell(0).setCellStyle(r1_style);

				//第四行
				HSSFRow row3 = sheet.createRow(3 + tb*10);
				row3.setHeightInPoints(40);
				row3.createCell(0).setCellValue("天气情况");
				row3.getCell(0).setCellStyle(r2_style);
				row3.createCell(1).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_weather", taList.get(tb).getWeather().toString()));
				row3.getCell(1).setCellStyle(mainStyle_center);
				row3.createCell(2).setCellValue("接报时间");
				row3.getCell(2).setCellStyle(r2_style);
				row3.createCell(3).setCellValue(sdf2.format(taList.get(0).getReceiptTime()));
				row3.getCell(3).setCellStyle(mainStyle_center);
				row3.createCell(4).setCellValue("接报方式");
				row3.getCell(4).setCellStyle(r2_style);
				row3.createCell(5).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_receiptWay", taList.get(tb).getReceiptWay().toString()));
				row3.getCell(5).setCellStyle(mainStyle_center);
				row3.createCell(6).setCellValue("消息来源");
				row3.getCell(6).setCellStyle(r2_style);
				row3.createCell(7).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_source", taList.get(tb).getSource().toString()));
				row3.getCell(7).setCellStyle(mainStyle_center);

				//第五行
				HSSFRow row4 = sheet.createRow(4 + tb*10);
				row4.setHeightInPoints(40);
				row4.createCell(0).setCellValue("事故地点");
				row4.getCell(0).setCellStyle(r2_style);
				row4.createCell(1).setCellValue(taList.get(tb).getAccidentSite());
				row4.getCell(1).setCellStyle(mainStyle_center);
				row4.createCell(2).setCellValue("事故类型");
				row4.getCell(2).setCellStyle(r2_style);
				row4.createCell(3).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_accidentType", taList.get(tb).getAccidentType().toString()));
				row4.getCell(3).setCellStyle(mainStyle_center);
				row4.createCell(4).setCellValue("车辆类型");
				row4.getCell(4).setCellStyle(r2_style);
				if(StringUtils.isNotBlank(taList.get(tb).getCarType())){
					String[] carTypeArr = taList.get(tb).getCarType().split(",");
					String carTypeStr = "";
					for (int i = 0; i < carTypeArr.length; i++) {
						carTypeStr += totalTableServiceImpl.getValueByDictAndKey("dc_carType", carTypeArr[i]) + ",";
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
				HSSFRow row5 = sheet.createRow(5 + tb*10);
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
				HSSFRow row6 = sheet.createRow(6 + tb*10);
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
				HSSFRow row7 = sheet.createRow(7 + tb*10);
				row7.setHeightInPoints(120);
				row7.createCell(0).setCellValue(taList.get(tb).getAccidentDetails());
				row7.getCell(0).setCellStyle(mainStyle_left);
				for (int i = 1; i < 8; i++) {
					row7.createCell(i).setCellStyle(r2_style);
				}

				//第九行
				HSSFRow row8 = sheet.createRow(8 + tb*10);
				row8.setHeightInPoints(40);
				row8.createCell(0).setCellValue("备注：" + taList.get(tb).getRemark());
				row8.getCell(0).setCellStyle(mainStyle_left);
				for (int i = 1; i < 8; i++) {
					row8.createCell(i).setCellStyle(r2_style);
				}

			}
		}else{		//空表

			//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 7));


			//创建行（第一行）
			HSSFRow row0 = sheet.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("交通事故");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);

			//第二行
			HSSFRow row1 = sheet.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-10");
			row1.getCell(0).setCellStyle(r1_style);

			//第三行
			HSSFRow row2 = sheet.createRow(2);
			row2.setHeightInPoints(25);
			row2.createCell(0).setCellValue("日期：          ");
			row2.getCell(0).setCellStyle(r1_style);

			//第四行
			HSSFRow row3 = sheet.createRow(3);
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
			HSSFRow row4 = sheet.createRow(4);
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
			HSSFRow row5 = sheet.createRow(5);
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
			HSSFRow row6 = sheet.createRow(6);
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
			HSSFRow row7 = sheet.createRow(7);
			row7.setHeightInPoints(40);
			for (int i = 0; i < 8; i++) {
				row7.createCell(i).setCellStyle(r2_style);
			}

			//第九行
			HSSFRow row8 = sheet.createRow(8);
			row8.setHeightInPoints(40);
			row8.createCell(0).setCellValue("备注：");
			row8.getCell(0).setCellStyle(mainStyle_left);
			for (int i = 1; i < 8; i++) {
				row8.createCell(i).setCellStyle(r2_style);
			}
		}

		return wb;
	}

}
