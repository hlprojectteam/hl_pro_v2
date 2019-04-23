package com.datacenter.service.impl;

import java.text.SimpleDateFormat;
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
import com.common.utils.helper.Pager;
import com.datacenter.dao.IRoadWorkDao;
import com.datacenter.module.RoadWork;
import com.datacenter.service.IRoadWorkService;
import com.datacenter.vo.RoadWorkVo;

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

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, RoadWorkVo roadWorkVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(roadWorkVo.getTtId())){
			params.add(Restrictions.eq("ttId", roadWorkVo.getTtId()));
		}
		if(roadWorkVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", roadWorkVo.getDutyDateStart()));
		}
		if(roadWorkVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", roadWorkVo.getDutyDateEnd()));
		}
		return this.roadWorkDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), RoadWork.class);
	}

	@Override
	public RoadWork saveOrUpdate(RoadWorkVo roadWorkVo) {
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
		List<Criterion> params = new ArrayList<Criterion>();
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
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from RoadWork where 1 = 1 ");
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
		//排序, 根据日期倒序排序，进场时间顺序排序
		hql.append(" order by dutyDate desc,approachTime asc ");

		List<RoadWork> rwList = this.roadWorkDaoImpl.queryEntityHQLList(hql.toString(), objectList, RoadWork.class);
		return rwList;
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
		HSSFRow row = null;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		String[][] content = new String[rwList.size()][];
		for (int i = 0; i < rwList.size(); i++) {
			row = sheet.createRow(i + 4);	//创建行
			row.setHeightInPoints(60);					//设置行高
			for (int j = 0; j < 14; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(sdf1.format(rwList.get(i).getDutyDate()));	break;
					case 1: cell.setCellValue(sdf2.format(rwList.get(i).getApproachTime()));	break;
					case 2: cell.setCellValue(sdf2.format(rwList.get(i).getDepartureTime()));	break;
					case 3: cell.setCellValue(rwList.get(i).getUnitName());	break;
					case 4: cell.setCellValue(rwList.get(i).getRelationPerson() + rwList.get(i).getRelationPhone());	break;
					case 5: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_positionAttributes", rwList.get(i).getPositionAttributes().toString()));	break;
					case 6: cell.setCellValue(rwList.get(i).getSpecificLocation()); break;
					case 7: cell.setCellValue(rwList.get(i).getConstructionContent()); break;
					case 8: cell.setCellValue(rwList.get(i).getJeevesSituation()); break;
					case 9: cell.setCellValue(sdf2.format(rwList.get(i).getCheckTime()));	break;
					case 10: cell.setCellValue(rwList.get(i).getChecker()); break;
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

}
