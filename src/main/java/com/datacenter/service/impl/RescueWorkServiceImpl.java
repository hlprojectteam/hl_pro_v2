package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.datacenter.dao.IRescueWorkDao;
import com.datacenter.module.RescueWork;
import com.datacenter.service.IRescueWorkService;
import com.datacenter.vo.RescueWorkVo;
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
 * @Description 拯救作业 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("rescueWorkServiceImpl")
public class RescueWorkServiceImpl extends BaseServiceImpl implements IRescueWorkService{

	@Autowired
	private IRescueWorkDao rescueWorkDaoImpl;

	@Autowired
	private TotalTableServiceImpl totalTableServiceImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, RescueWorkVo rescueWorkVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(rescueWorkVo.getTtId())){
			params.add(Restrictions.eq("ttId", rescueWorkVo.getTtId()));
		}
		if(rescueWorkVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", rescueWorkVo.getDutyDateStart()));
		}
		if(rescueWorkVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", rescueWorkVo.getDutyDateEnd()));
		}
		return this.rescueWorkDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), RescueWork.class);
	}

	@Override
	public RescueWork saveOrUpdate(RescueWorkVo rescueWorkVo) {
		RescueWork rescueWork = new RescueWork();
		BeanUtils.copyProperties(rescueWorkVo, rescueWork);
		if(StringUtils.isBlank(rescueWork.getId())){
			this.save(rescueWork);
		}else{
			this.update(rescueWork);
		}
		return rescueWork;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.rescueWorkDaoImpl.excuteBySql("delete from dc_RescueWork where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<RescueWork> list = this.rescueWorkDaoImpl.queryEntityList(params, Order.desc("createTime"), RescueWork.class);	//根据主表Id获取子表关联数据
		for (RescueWork rescueWork : list) {
			rescueWork.setDutyDate(dutyDate);
			this.update(rescueWork);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<RescueWork> queryEntityList(RescueWorkVo rescueWorkVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from RescueWork where 1 = 1 ");
		if(StringUtils.isNotBlank(rescueWorkVo.getTtId())){
			objectList.add(rescueWorkVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(rescueWorkVo.getDutyDateStart() != null){		//日期Start
			objectList.add(rescueWorkVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(rescueWorkVo.getDutyDateEnd() != null){		//日期End
			objectList.add(rescueWorkVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		//排序, 根据日期倒序排序,接报时间顺序排序
		hql.append(" order by dutyDate desc,receiptTime asc ");

		List<RescueWork> rwList = this.rescueWorkDaoImpl.queryEntityHQLList(hql.toString(), objectList, RescueWork.class);
		return rwList;
	}

	@Override
	public HSSFWorkbook export(RescueWorkVo rescueWorkVo) {
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

		List<RescueWork> rwList = queryEntityList(rescueWorkVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("拯救作业汇总");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 14));
		sheet.addMergedRegion(new CellRangeAddress(rwList.size() + 3, rwList.size() + 3, 0, 14));

		//列宽自适应（该方法在老版本的POI中效果不佳）
		/*sheet.autoSizeColumn(i);*/
		//设置列宽
		for (int i = 0; i < 15; i++) {
			sheet.setColumnWidth(i, sheet.getColumnWidth(i)*2);
		}


		//创建行（第一行）
		HSSFRow row0 = sheet.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		row0.createCell(0).setCellValue("拯救作业");
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-07");
		row1.getCell(0).setCellStyle(r1_style);


		//第三行
		HSSFRow row2 = sheet.createRow(2);
		row2.setHeightInPoints(30);		//行高
		HSSFCell cell = null;
		String[] title = {"日期","接报时间","到达时间","到场用时","清场时间","地点","故障车","车型","缴费单号","拯救费","拖车里程","车辆去向","拯救车","司机电话","备注"};
		for(int i=0;i<title.length;i++){
			cell = row2.createCell(i);		//创建单元格
			cell.setCellValue(title[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}

		//第四行 及 之后的行
		HSSFRow row = null;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
		String[][] content = new String[rwList.size()][];
		for (int i = 0; i < rwList.size(); i++) {
			row = sheet.createRow(i + 3);	//创建行
			row.setHeightInPoints(60);					//设置行高
			for (int j = 0; j < title.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(sdf1.format(rwList.get(i).getDutyDate()));	break;
					case 1: cell.setCellValue(sdf2.format(rwList.get(i).getReceiptTime()));	break;
					case 2: cell.setCellValue(sdf2.format(rwList.get(i).getArrivalTime()));	break;
					case 3: cell.setCellValue(rwList.get(i).getUsedTime());		break;
					case 4: cell.setCellValue(sdf2.format(rwList.get(i).getEvacuationTime()));	break;
					case 5: cell.setCellValue(rwList.get(i).getSite());		break;
					case 6: cell.setCellValue(rwList.get(i).getFaultPlates());		break;
					case 7: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_carType", rwList.get(i).getCarType().toString()));		break;
					case 8: cell.setCellValue(rwList.get(i).getPaymentOrder());		break;
					case 9: cell.setCellValue(rwList.get(i).getRescueCharge());		break;
					case 10: cell.setCellValue(rwList.get(i).getTrailerMileage());		break;
					case 11: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_whereabouts", rwList.get(i).getWhereabouts().toString()));		break;
					case 12: cell.setCellValue(rwList.get(i).getRescuePlates());		break;
					case 13: cell.setCellValue(rwList.get(i).getDriverPhone());		break;
					case 14: cell.setCellValue(rwList.get(i).getRemark());		break;
				}

				//设置单元格样式
				cell.setCellStyle(mainStyle_center);
			}
		}

		//最后一行
		row = sheet.createRow( rwList.size() + 3);
		row.setHeightInPoints(40);		//行高
		row.createCell(0).setCellValue("备注：对于每宗拯救作业中心接报后，均已向车主说明情况，我司目前没有相关拯救队伍，拖车服务委托业务实力较好的广州交投汽车援救服务有限公司实施，故障车可根据实际情况拖到司机指定位置，并提醒车主拖车里程根据广东省物价局计价标准付费（并向其说明收费标准），如有异议可拨打我司服务电话进行投诉，我司会帮司机跟进，并提醒其在高速路面必须注意安全，摆放有效安全区等候救援。");
		HSSFCellStyle rwStyle = wb.createCellStyle();
		rwStyle.cloneStyleFrom(mainStyle_left);
		HSSFFont r3_font = wb.createFont();
		r2_font.setBold(true);
		rwStyle.setFont(r3_font);
		row.getCell(0).setCellStyle(rwStyle);
		for (int i = 1; i < 15; i++) {
			row.createCell(i).setCellStyle(r2_style);
		}

		return wb;
	}

}
