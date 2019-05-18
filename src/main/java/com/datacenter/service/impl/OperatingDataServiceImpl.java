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
import com.datacenter.dao.IOperatingDataDao;
import com.datacenter.module.OperatingData;
import com.datacenter.service.IOperatingDataService;
import com.datacenter.vo.OperatingDataVo;

/**
 * @Description 营运数据 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("operatingDataServiceImpl")
public class OperatingDataServiceImpl extends BaseServiceImpl implements IOperatingDataService{

	@Autowired
	private IOperatingDataDao operatingDataDaoImpl;

	@Autowired
	private TotalTableServiceImpl totalTableServiceImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, OperatingDataVo operatingDataVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(operatingDataVo.getTtId())){
			params.add(Restrictions.eq("ttId", operatingDataVo.getTtId()));
		}
		if(operatingDataVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", operatingDataVo.getDutyDateStart()));
		}
		if(operatingDataVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", operatingDataVo.getDutyDateEnd()));
		}

		if(operatingDataVo.getTollGate() != null){
			params.add(Restrictions.eq("tollGate", operatingDataVo.getTollGate()));
		}
		if(StringUtils.isNotBlank(operatingDataVo.getKeyword())){
			params.add(Restrictions.sqlRestriction(" (total_Traffic like '%" + operatingDataVo.getKeyword() + "%' " +
					" or ytk_Traffic like '%" + operatingDataVo.getKeyword() + "%' " +
					" or general_Income like '%" + operatingDataVo.getKeyword() + "%' " +
					" or ytk_Income like '%" + operatingDataVo.getKeyword() + "%' )"));
		}
		return this.operatingDataDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), OperatingData.class);
	}

	@Override
	public OperatingData saveOrUpdate(OperatingDataVo operatingDataVo) {
		OperatingData operatingData = new OperatingData();
		BeanUtils.copyProperties(operatingDataVo, operatingData);
		if(StringUtils.isBlank(operatingData.getId())){
			this.save(operatingData);
		}else{
			this.update(operatingData);
		}
		return operatingData;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.operatingDataDaoImpl.excuteBySql("delete from dc_OperatingData where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<OperatingData> list = this.operatingDataDaoImpl.queryEntityList(params, Order.desc("createTime"), OperatingData.class);	//根据主表Id获取子表关联数据
		for (OperatingData operatingData : list) {
			operatingData.setDutyDate(dutyDate);
			this.update(operatingData);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<OperatingData> queryEntityList(OperatingDataVo operatingDataVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from OperatingData where 1 = 1 ");
		if(StringUtils.isNotBlank(operatingDataVo.getTtId())){
			objectList.add(operatingDataVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(operatingDataVo.getDutyDateStart() != null){		//日期Start
			objectList.add(operatingDataVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(operatingDataVo.getDutyDateEnd() != null){		//日期End
			objectList.add(operatingDataVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		//排序, 根据日期倒序排序，收费站顺序排序
		hql.append(" order by dutyDate desc,tollGate asc ");

		List<OperatingData> odList = this.operatingDataDaoImpl.queryEntityHQLList(hql.toString(), objectList, OperatingData.class);
		return odList;
	}

	@Override
	public HSSFWorkbook export(OperatingDataVo operatingDataVo) {
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

		List<OperatingData> odList = queryEntityList(operatingDataVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("营运数据汇总");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 4, 5));

		sheet.addMergedRegion(new CellRangeAddress(odList.size() + 4, odList.size() + 4, 0, 1));

		for (int i = 0; i < 6; i++) {
			//列宽自适应（该方法在老版本的POI中效果不佳）
			/*sheet.autoSizeColumn(i);*/
			//设置列宽
			sheet.setColumnWidth(i, sheet.getColumnWidth(i)*5/2);
		}


		//创建行（第一行）
		HSSFRow row0 = sheet.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		row0.createCell(0).setCellValue("各站营运数据");
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-06");
		row1.getCell(0).setCellStyle(r1_style);


		//第三行
		HSSFRow row2 = sheet.createRow(2);
		row2.setHeightInPoints(30);		//行高
		HSSFCell cell = null;
		String[] title1 = {"日期","收费站","出口车流量","收费额"};
		row2.createCell(3).setCellStyle(r2_style);
		row2.createCell(5).setCellStyle(r2_style);
		for(int i=0;i<title1.length;i++){
			if(i == 3){
				cell = row2.createCell(4);		//创建单元格
			}else{
				cell = row2.createCell(i);		//创建单元格
			}
			cell.setCellValue(title1[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}

		//第四行
		HSSFRow row3 = sheet.createRow(3);
		row3.setHeightInPoints(30);		//行高
		String[] title2 = {"总车流","其中粤通卡车流","总收费额","其中粤通卡收入"};
		row3.createCell(0).setCellStyle(r2_style);
		for(int i=0;i<title2.length;i++){
			cell = row3.createCell(i + 2);		//创建单元格
			cell.setCellValue(title2[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");

		//第五行 及 之后的行
		HSSFRow row = null;
		String[][] content = new String[odList.size()][];
		Integer hj_totalTraffic = 0;
		Integer hj_ytkTraffic = 0;
		Double hj_generalIncome = 0.0;
		Double hj_ytkIncome = 0.0;
		for (int i = 0; i < odList.size(); i++) {
			row = sheet.createRow(i + 4);	//创建行
			row.setHeightInPoints(30);				//设置行高
			for (int j = 0; j < 6; j++) {
				cell = row.createCell(j);			//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(sdf1.format(odList.get(i).getDutyDate())); break;
					case 1: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_tollGate", odList.get(i).getTollGate().toString()));	break;
					case 2: cell.setCellValue(odList.get(i).getTotalTraffic());	break;
					case 3: cell.setCellValue(odList.get(i).getYtkTraffic());	break;
					case 4: cell.setCellValue(odList.get(i).getGeneralIncome());	 break;
					case 5: cell.setCellValue(odList.get(i).getYtkIncome());	break;
				}
				//设置单元格样式
				cell.setCellStyle(mainStyle_center);
			}
			hj_totalTraffic += odList.get(i).getTotalTraffic();
			hj_ytkTraffic += odList.get(i).getYtkTraffic();
			hj_generalIncome += odList.get(i).getGeneralIncome();
			hj_ytkIncome += odList.get(i).getYtkIncome();
		}

		//最后一行
		row = sheet.createRow( odList.size() + 4);
		row.setHeightInPoints(30);
		row.createCell(0).setCellValue("合计");
		row.getCell(0).setCellStyle(r2_style);
		row.createCell(1).setCellStyle(r2_style);
		row.createCell(2).setCellValue(hj_totalTraffic);
		row.getCell(2).setCellStyle(mainStyle_center);
		row.createCell(3).setCellValue(hj_ytkTraffic);
		row.getCell(3).setCellStyle(mainStyle_center);
		row.createCell(4).setCellValue(hj_generalIncome);
		row.getCell(4).setCellStyle(mainStyle_center);
		row.createCell(5).setCellValue(hj_ytkIncome);
		row.getCell(5).setCellStyle(mainStyle_center);

		return wb;
	}

	@Override
	public boolean isRecordExist(Date dutyDate, String tollGateId) {
		OperatingDataVo operatingDataVo=new OperatingDataVo();
		operatingDataVo.setDutyDate(dutyDate);
		if(tollGateId!=null){
			operatingDataVo.setTollGate(Integer.parseInt(tollGateId));
		}
		
		List<Criterion> params = new ArrayList<Criterion>();
		if(operatingDataVo.getDutyDate() != null){		//日期
			params.add(Restrictions.eq("dutyDate", operatingDataVo.getDutyDate()));
		}
		if(operatingDataVo.getTollGate() != null){		//收费站
			params.add(Restrictions.eq("tollGate", operatingDataVo.getTollGate()));
		}
		List<OperatingData> list= this.operatingDataDaoImpl.queryEntityList(params, Order.desc("createTime"), OperatingData.class);
		if(list!=null){
			if(list.size()>0){
				return true;
			}
		}
		return false;
	}

}
