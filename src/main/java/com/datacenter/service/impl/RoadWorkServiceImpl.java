package com.datacenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.common.utils.MathUtil;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.datacenter.dao.IRoadWorkDao;
import com.datacenter.module.RoadWork;
import com.datacenter.service.IRoadWorkService;
import com.datacenter.vo.RoadWorkVo;
import com.urms.dataDictionary.service.IDataDictionaryService;

/**
 * @Description 涉路施工 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("roadWorkServiceImpl")
public class RoadWorkServiceImpl extends BaseServiceImpl implements IRoadWorkService{

	@Autowired
	private IRoadWorkDao roadWorkDaoImpl;

	@Autowired
	private TotalTableServiceImpl totalTableServiceImpl;
	
	@Autowired
	public IDataDictionaryService dataDictionaryServiceImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, RoadWorkVo roadWorkVo) {
		List<Criterion> params = new ArrayList<>();
		if(StringUtils.isNotBlank(roadWorkVo.getTtId())){
			params.add(Restrictions.eq("ttId", roadWorkVo.getTtId()));
		}
		if(roadWorkVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", roadWorkVo.getDutyDateStart()));
		}
		if(roadWorkVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", roadWorkVo.getDutyDateEnd()));
		}
		if(StringUtils.isNotBlank(roadWorkVo.getPositionAttributes())){
			params.add(Restrictions.like("positionAttributes", "%" + roadWorkVo.getPositionAttributes() + "%"));
		}
		if(StringUtils.isNotBlank(roadWorkVo.getUnitName())){
			params.add(Restrictions.eq("unitName", roadWorkVo.getUnitName()));
		}
		if(StringUtils.isNotBlank(roadWorkVo.getConstructionContent())){
			params.add(Restrictions.like("constructionContent", "%" + roadWorkVo.getConstructionContent() + "%"));
		}
		if(StringUtils.isNotBlank(roadWorkVo.getRectificationMeasures())){
			params.add(Restrictions.like("rectificationMeasures", "%" + roadWorkVo.getRectificationMeasures() + "%"));
		}

		if(StringUtils.isNotBlank(roadWorkVo.getKeyword())){
			params.add(Restrictions.sqlRestriction(" ( " +
					" relation_Person like '%" + roadWorkVo.getKeyword() + "%' " +
					" or unit_Name like '%" + roadWorkVo.getKeyword() + "%' " +
					" or relation_Phone like '%" + roadWorkVo.getKeyword() + "%' " +
					" or specific_Location like '%" + roadWorkVo.getKeyword() + "%' " +
					" or construction_Content like '%" + roadWorkVo.getKeyword() + "%' " +
					" or jeeves_Situation like '%" + roadWorkVo.getKeyword() + "%' " +
					" or checker_ like '%" + roadWorkVo.getKeyword() + "%' " +
					" or description_ like '%" + roadWorkVo.getKeyword() + "%' " +
					" or rectification_Measures like '%" + roadWorkVo.getKeyword() + "%' " +
					" or reported_Situation like '%" + roadWorkVo.getKeyword() + "%' )"));
		}
		return this.roadWorkDaoImpl.queryEntityList(page, rows, params, Order.desc("dutyDate"), RoadWork.class);
	}

	@Override
	public RoadWork saveOrUpdate(RoadWorkVo roadWorkVo) {
		if(roadWorkVo.getUnitName().equals("其它")){
			roadWorkVo.setUnitName(roadWorkVo.getDictValue());
		}
		roadWorkVo.setChecker(roadWorkVo.getDictValue2());
		RoadWork roadWork = new RoadWork();
		BeanUtils.copyProperties(roadWorkVo, roadWork);
		if(StringUtils.isBlank(roadWork.getId())){
			this.save(roadWork);
		}else{
			this.update(roadWork);
		}
		return roadWork;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.roadWorkDaoImpl.excuteBySql("delete from dc_RoadWork where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<RoadWork> list = this.roadWorkDaoImpl.queryEntityList(params, Order.desc("createTime"), RoadWork.class);	//根据主表Id获取子表关联数据
		for (RoadWork roadWork : list) {
			roadWork.setDutyDate(dutyDate);
			this.update(roadWork);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<RoadWork> queryEntityList(RoadWorkVo roadWorkVo) {
		List<Object> objectList = new ArrayList<>();

		StringBuilder hql = new StringBuilder("from RoadWork where 1 = 1 ");
		if(StringUtils.isNotBlank(roadWorkVo.getTtId())){
			objectList.add(roadWorkVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(roadWorkVo.getDutyDateStart() != null){		//日期Start
			objectList.add(roadWorkVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(roadWorkVo.getDutyDateEnd() != null){		//日期End
			objectList.add(roadWorkVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}

		if(StringUtils.isNotBlank(roadWorkVo.getPositionAttributes())){
			objectList.add(roadWorkVo.getPositionAttributes());
			hql.append(" and positionAttributes = ? ");
		}
		if(StringUtils.isNotBlank(roadWorkVo.getUnitName())){
			objectList.add(roadWorkVo.getUnitName());
			hql.append(" and unitName = ? ");
		}
		if(StringUtils.isNotBlank(roadWorkVo.getConstructionContent())){
			objectList.add("%" + roadWorkVo.getConstructionContent() + "%");
			hql.append(" and constructionContent like ? ");
		}
		if(StringUtils.isNotBlank(roadWorkVo.getRectificationMeasures())){
			objectList.add("%" + roadWorkVo.getRectificationMeasures() + "%");
			hql.append(" and rectificationMeasures like ? ");
		}
		if(StringUtils.isNotBlank(roadWorkVo.getKeyword())){
			hql.append(" and (unitName like '%").append(roadWorkVo.getKeyword()).append("%' ").append(" or relationPerson like '%").append(roadWorkVo.getKeyword()).append("%' ").append(" or relationPhone like '%").append(roadWorkVo.getKeyword()).append("%' ").append(" or specificLocation like '%").append(roadWorkVo.getKeyword()).append("%' ").append(" or constructionContent like '%").append(roadWorkVo.getKeyword()).append("%' ").append(" or jeevesSituation like '%").append(roadWorkVo.getKeyword()).append("%' ").append(" or checker like '%").append(roadWorkVo.getKeyword()).append("%' ").append(" or description like '%").append(roadWorkVo.getKeyword()).append("%' ").append(" or rectificationMeasures like '%").append(roadWorkVo.getKeyword()).append("%' ").append(" or reportedSituation like '%").append(roadWorkVo.getKeyword()).append("%' )");
		}
		//排序, 根据日期倒序排序，进场时间顺序排序
		hql.append(" order by dutyDate asc,approachTime asc ");

		return this.roadWorkDaoImpl.queryEntityHQLList(hql.toString(), objectList, RoadWork.class);
	}

	@Override
	public HSSFWorkbook export(RoadWorkVo roadWorkVo) {
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

		List<RoadWork> rwList = queryEntityList(roadWorkVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("涉路施工汇总");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		//跨列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 13));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 13));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 8));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 9, 12));
		//跨行
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 13, 13));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 2, 2));


		//列宽自适应（该方法在老版本的POI中效果不佳）
		/*sheet.autoSizeColumn(i);*/
		//设置列宽
		for (int i = 0; i < 14; i++) {
			if(i == 3 || i == 4 || i == 8){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*3);
			}else if(i == 6){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*5);
			}else if(i == 11){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*6);
			}else{
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*3/2);
			}
		}


		//创建行（第一行）
		HSSFRow row0 = sheet.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		row0.createCell(0).setCellValue("涉路施工");
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-04");
		row1.getCell(0).setCellStyle(r1_style);


		//第三行
		HSSFRow row2 = sheet.createRow(2);
		row2.setHeightInPoints(30);		//行高
		HSSFCell cell;
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
		HSSFRow row3 = sheet.createRow(3);
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
		HSSFRow row;
		for (int i = 0; i < rwList.size(); i++) {
			row = sheet.createRow(i + 4);	//创建行
//			row.setHeightInPoints(60);					//设置行高
			float defaultRowH=30;
			float realRowH=30;
			for (int j = 0; j < 14; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(DateUtil.getDateFormatString(rwList.get(i).getDutyDate(),DateUtil.JAVA_DATE_FORMAT_CH_YMD));	break;
					case 1: cell.setCellValue(DateUtil.getDateFormatString(rwList.get(i).getApproachTime(),DateUtil.JAVA_DATE_FORMAT_HM));	break;
					case 2: cell.setCellValue(DateUtil.getDateFormatString(rwList.get(i).getDepartureTime(),DateUtil.JAVA_DATE_FORMAT_HM));	break;
					case 3: cell.setCellValue(rwList.get(i).getUnitName());	break;
					case 4: cell.setCellValue(rwList.get(i).getRelationPerson() + rwList.get(i).getRelationPhone());	break;
					case 5: cell.setCellValue(rwList.get(i).getPositionAttributes());	break;
//					case 5: 
//						//cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_positionAttributes", rwList.get(i).getPositionAttributes().toString()));	break;
//						if(StringUtils.isNotBlank(rwList.get(i).getPositionAttributes())){
//							String[] paArr = rwList.get(i).getPositionAttributes().split(",");
//							String paStr = "";
//							for (int m = 0; m < paArr.length; m++) {
//								paStr += totalTableServiceImpl.getValueByDictAndKey("dc_positionAttributes", paArr[m]) + ",";
//							}
//							cell.setCellValue(paStr.substring(0, paStr.length()-1));
//						}else{
//							cell.setCellValue("");
//						}
//						break;
					case 6: cell.setCellValue(rwList.get(i).getSpecificLocation()); break;
					case 7: 
						cell.setCellValue(rwList.get(i).getConstructionContent()); 
						if(MathUtil.getCellHeight(sheet.getColumnWidth(row.getCell(j).getColumnIndex()),
								rwList.get(i).getConstructionContent(), defaultRowH)>realRowH){
							realRowH=MathUtil.getCellHeight(sheet.getColumnWidth(row.getCell(j).getColumnIndex()),
									rwList.get(i).getConstructionContent(),defaultRowH);
						}
						break;
					case 8: cell.setCellValue(rwList.get(i).getJeevesSituation()); break;
					case 9: cell.setCellValue(DateUtil.getDateFormatString(rwList.get(i).getCheckTime(),DateUtil.JAVA_DATE_FORMAT_HM));	break;
					case 10: cell.setCellValue(rwList.get(i).getChecker());	break;
					case 11: 
						cell.setCellValue(rwList.get(i).getDescription()); 
						if(MathUtil.getCellHeight(sheet.getColumnWidth(row.getCell(j).getColumnIndex()),
								rwList.get(i).getDescription(), defaultRowH)>realRowH){
							realRowH=MathUtil.getCellHeight(sheet.getColumnWidth(row.getCell(j).getColumnIndex()),
									rwList.get(i).getDescription(),defaultRowH);
						}
						break;
					case 12: 
						cell.setCellValue(rwList.get(i).getRectificationMeasures()); 
						if(MathUtil.getCellHeight(sheet.getColumnWidth(row.getCell(j).getColumnIndex()),
								rwList.get(i).getRectificationMeasures(), defaultRowH)>realRowH){
							realRowH=MathUtil.getCellHeight(sheet.getColumnWidth(row.getCell(j).getColumnIndex()),
									rwList.get(i).getRectificationMeasures(),defaultRowH);
						}
						break;
					case 13: cell.setCellValue(rwList.get(i).getReportedSituation()); break;

				}
				//设置单元格样式
				if(j == 11){
					cell.setCellStyle(mainStyle_left);
				}else{
					cell.setCellStyle(mainStyle_center);
				}
			}
			row.setHeightInPoints(realRowH);
		}

		return wb;
	}

}
