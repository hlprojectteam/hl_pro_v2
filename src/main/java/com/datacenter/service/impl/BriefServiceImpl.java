package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.datacenter.dao.IBriefDao;
import com.datacenter.module.Brief;
import com.datacenter.service.IBriefService;
import com.datacenter.vo.BriefVo;
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
 * @Description 工作简报 service实现
 * @author xuezb
 * @date 2019年2月18日
 */
@Repository("briefServiceImpl")
public class BriefServiceImpl extends BaseServiceImpl implements IBriefService{

	@Autowired
	private IBriefDao briefDaoImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, BriefVo briefVo) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(briefVo.getId())){
			params.add(Restrictions.eq("id", briefVo.getId()));
		}
		if(StringUtils.isNotBlank(briefVo.getTtId())){
			params.add(Restrictions.eq("ttId", briefVo.getTtId()));
		}
		if(briefVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", briefVo.getDutyDateStart()));
		}
		if(briefVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", briefVo.getDutyDateEnd()));
		}
		return this.briefDaoImpl.queryEntityList(page, rows, params, Order.desc("dutyDate"), Brief.class);
	}

	@Override
	public Brief saveOrUpdate(BriefVo briefVo) {
		Brief brief = new Brief();
		BeanUtils.copyProperties(briefVo, brief);
		if(StringUtils.isBlank(brief.getId())){
			brief.setStatus(1);
			this.save(brief);
		}else{
			brief.setStatus(1);
			this.update(brief);
		}
		return brief;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.briefDaoImpl.excuteBySql("delete from dc_Brief where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<Criterion>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<Brief> list = this.briefDaoImpl.queryEntityList(params, Order.desc("createTime"), Brief.class);	//根据主表Id获取子表关联数据
		for (Brief brief : list) {
			brief.setDutyDate(dutyDate);
			this.update(brief);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<Brief> queryEntityList(BriefVo briefVo) {
		List<Object> objectList = new ArrayList<Object>();

		StringBuffer hql = new StringBuffer("from Brief where 1 = 1 ");
		if(StringUtils.isNotBlank(briefVo.getId())){
			objectList.add(briefVo.getId());
			hql.append(" and id = ? ");
		}
		if(StringUtils.isNotBlank(briefVo.getTtId())){
			objectList.add(briefVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(briefVo.getDutyDateStart() != null){		//日期Start
			objectList.add(briefVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(briefVo.getDutyDateEnd() != null){		//日期End
			objectList.add(briefVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}
		if(briefVo.getStatus() != null){		//状态
			objectList.add(briefVo.getStatus());
			hql.append(" and status = ? ");
		}
		//排序, 根据日期倒序排序
		hql.append(" order by dutyDate desc ");

		List<Brief> trList = this.briefDaoImpl.queryEntityHQLList(hql.toString(), objectList, Brief.class);
		return trList;
	}

	@Override
	public HSSFWorkbook export(BriefVo briefVo) {

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


		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.* from (select db.ID,db.CREATE_TIME,db.CREATOR_ID,db.CREATOR_NAME,db.SYS_CODE,db.cwfzjl_,db.duty_Date,db.equipment_Operation,db.fhry_,db.form_Number,db.operating_Data,db.rise_Time,db.title_,db.traffic_Operation,db.ttId,db.zgfzjl_,db.zxfzr_" +
				" from dc_brief db where 1=1 ");
		if(briefVo.getDutyDateStart() != null){		//日期Start
			sql.append(" and db.duty_Date >= ? ");
			params.add(briefVo.getDutyDateStart());
		}
		if(briefVo.getDutyDateEnd() != null){		//日期End
			sql.append(" and db.duty_Date <= ? ");
			params.add(briefVo.getDutyDateEnd());
		}
		sql.append(" order by db.CREATE_TIME desc) t group by t.duty_Date order by t.duty_Date desc");

		List<Object> list = this.briefDaoImpl.queryEntitySQLList(sql.toString(), params);
		ArrayList<Brief> briefList = new ArrayList<Brief>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[])list.get(i);
			Brief brief = new Brief();
			if(obj[0]!=null)  brief.setId(obj[0].toString());
			if(obj[1]!=null)  brief.setCreateTime(DateUtil.getDateFromString(obj[1].toString()));
			if(obj[2]!=null)  brief.setCreatorId(obj[2].toString());
			if(obj[3]!=null)  brief.setCreatorName(obj[3].toString());
			if(obj[4]!=null)  brief.setSysCode(obj[4].toString());
			if(obj[5]!=null)  brief.setCwfzjl(obj[5].toString());
			if(obj[6]!=null)  brief.setDutyDate(DateUtil.getDateFromString(obj[6].toString()));
			if(obj[7]!=null)  brief.setEquipmentOperation(obj[7].toString());
			if(obj[8]!=null)  brief.setFhry(obj[8].toString());
			if(obj[9]!=null)  brief.setFormNumber(obj[9].toString());
			if(obj[10]!=null) brief.setOperatingData(obj[10].toString());
			if(obj[11]!=null) brief.setRiseTime(DateUtil.getDateFromString(obj[11].toString()));
			if(obj[12]!=null) brief.setTitle(obj[12].toString());
			if(obj[13]!=null) brief.setTrafficOperation(obj[13].toString());
			if(obj[14]!=null) brief.setTtId(obj[14].toString());
			if(obj[15]!=null) brief.setZgfzjl(obj[15].toString());
			if(obj[16]!=null) brief.setZxfzr(obj[16].toString());
			briefList.add(brief);
		}


		//创建sheet
		HSSFSheet sheet = wb.createSheet("工作简报汇总");

		//设置列宽
		for (int i = 0; i < 5; i++) {
			if(i != 4){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*3);
			}else{
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*6);
			}
		}


		if(briefList != null && briefList.size() > 0){
			for(int tb = 0; tb < briefList.size(); tb++){		//有多少条记录就有多少张表

				//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
				sheet.addMergedRegion(new CellRangeAddress(0 + tb*7, 0 + tb*7, 0, 4));
				sheet.addMergedRegion(new CellRangeAddress(1 + tb*7, 1 + tb*7, 0, 4));
				sheet.addMergedRegion(new CellRangeAddress(3 + tb*7, 3 + tb*7, 1, 4));
				sheet.addMergedRegion(new CellRangeAddress(4 + tb*7, 4 + tb*7, 1, 4));
				sheet.addMergedRegion(new CellRangeAddress(5 + tb*7, 5 + tb*7, 1, 4));

				//创建行（第一行）
				HSSFRow row0 = sheet.createRow(0 + tb*7);
				//设置行的高度
				row0.setHeightInPoints(60);
				//创建单元格 并 设置单元格内容
				row0.createCell(0).setCellValue(briefList.get(tb).getTitle());
				//设置单元格样式
				HSSFCellStyle bigTitle = wb.createCellStyle();
				bigTitle.cloneStyleFrom(r0_style);
				HSSFFont bigFont = wb.createFont();
				bigFont.setBold(true);						//字体加粗
				bigFont.setFontHeightInPoints((short)24);	//字体大小
				bigTitle.setFont(bigFont);
				row0.getCell(0).setCellStyle(bigTitle);
				for (int i = 1; i < 5; i++) {
					row0.createCell(i).setCellStyle(r0_style);
				}

				//第二行
				HSSFRow row1 = sheet.createRow(1 + tb*7);
				row1.createCell(0).setCellValue("表单编号：HLZXRBB-01");
				row1.getCell(0).setCellStyle(r1_style);
				for (int i = 1; i < 5; i++) {
					row1.createCell(i).setCellStyle(r1_style);
				}

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

				//第三行
				HSSFRow row2 = sheet.createRow(2 + tb*7);
				row2.setHeightInPoints(40);
				row2.createCell(0).setCellValue("常务副总经理：" + briefList.get(tb).getCwfzjl());
				row2.createCell(1).setCellValue("主管副总经理：" + briefList.get(tb).getZgfzjl());
				row2.createCell(2).setCellValue("中心副主任：" + briefList.get(tb).getZxfzr());
				row2.createCell(3).setCellValue("复核：" + briefList.get(tb).getFhry());
				row2.createCell(4).setCellValue("简报生成时间：" + sdf.format(briefList.get(tb).getCreateTime()));
				row2.getCell(0).setCellStyle(mainStyle_center);
				row2.getCell(1).setCellStyle(mainStyle_center);
				row2.getCell(2).setCellStyle(mainStyle_center);
				row2.getCell(3).setCellStyle(mainStyle_center);
				row2.getCell(4).setCellStyle(mainStyle_center);

				//第四行
				HSSFRow row3 = sheet.createRow(3 + tb*7);
				row3.setHeightInPoints(50);			//设置行的高度
				row3.createCell(0).setCellValue("营运数据");
				row3.getCell(0).setCellStyle(mainStyle_center);
				row3.createCell(1).setCellValue(briefList.get(tb).getOperatingData());
				row3.getCell(1).setCellStyle(mainStyle_left);
				row3.createCell(2).setCellStyle(mainStyle_left);
				row3.createCell(3).setCellStyle(mainStyle_left);
				row3.createCell(4).setCellStyle(mainStyle_left);

				//第五行
				HSSFRow row4 = sheet.createRow(4 + tb*7);
				row4.setHeightInPoints(180);			//设置行的高度
				row4.createCell(0).setCellValue("交通运行情况");
				row4.getCell(0).setCellStyle(mainStyle_center);
				row4.createCell(1).setCellValue(briefList.get(tb).getTrafficOperation());
				row4.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 5; i++) {
					row4.createCell(i).setCellStyle(mainStyle_center);
				}

				//第六行
				HSSFRow row5 = sheet.createRow(5 + tb*7);
				row5.setHeightInPoints(250);			//设置行的高度
				row5.createCell(0).setCellValue("设备运行情况");
				row5.getCell(0).setCellStyle(mainStyle_center);
				row5.createCell(1).setCellValue(briefList.get(tb).getEquipmentOperation());
				row5.getCell(1).setCellStyle(mainStyle_left);
				for (int i = 2; i < 5; i++) {
					row5.createCell(i).setCellStyle(mainStyle_center);
				}

			}
		}else{		//空表

			//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 4));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 4));
			sheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 4));

		    //创建行（第一行）
			HSSFRow row0 = sheet.createRow(0);
			//设置行的高度
			row0.setHeightInPoints(30);
			//创建单元格 并 设置单元格内容
			row0.createCell(0).setCellValue("环龙运营控制指挥中心工作简报");
			//设置单元格样式
			row0.getCell(0).setCellStyle(r0_style);
			for (int i = 1; i < 5; i++) {
				row0.createCell(i).setCellStyle(r0_style);
			}

			//第二行
			HSSFRow row1 = sheet.createRow(1);
			row1.createCell(0).setCellValue("表单编号：HLZXRBB-01");
			row1.getCell(0).setCellStyle(r1_style);
			for (int i = 1; i < 5; i++) {
				row0.createCell(i).setCellStyle(r1_style);
			}

			//第三行
			HSSFRow row2 = sheet.createRow(2);
			row2.setHeightInPoints(40);
			row2.createCell(0).setCellValue("常务副总经理：" );
			row2.createCell(1).setCellValue("主管副总经理：");
			row2.createCell(2).setCellValue("中心副主任：");
			row2.createCell(3).setCellValue("复核：");
			row2.createCell(4).setCellValue("简报生成时间：");
			row2.getCell(0).setCellStyle(mainStyle_center);
			row2.getCell(1).setCellStyle(mainStyle_center);
			row2.getCell(2).setCellStyle(mainStyle_center);
			row2.getCell(3).setCellStyle(mainStyle_center);
			row2.getCell(4).setCellStyle(mainStyle_center);

			//第四行
			HSSFRow row3 = sheet.createRow(3);
			row3.setHeightInPoints(40);			//设置行的高度
			row3.createCell(0).setCellValue("营运数据");
			row3.getCell(0).setCellStyle(mainStyle_center);
			for (int i = 1; i < 5; i++) {
			row3.createCell(i).setCellStyle(mainStyle_left);
			}

			//第五行
			HSSFRow row4 = sheet.createRow(4);
			row4.setHeightInPoints(40);			//设置行的高度
			row4.createCell(0).setCellValue("交通运行情况");
			row4.getCell(0).setCellStyle(mainStyle_center);
			for (int i = 1; i < 5; i++) {
				row4.createCell(i).setCellStyle(mainStyle_left);
			}

			//第六行
			HSSFRow row5 = sheet.createRow(5);
			row5.setHeightInPoints(40);			//设置行的高度
			row5.createCell(0).setCellValue("设备运行情况");
			row5.getCell(0).setCellStyle(mainStyle_center);
			for (int i = 1; i < 5; i++) {
				row5.createCell(i).setCellStyle(mainStyle_left);
			}

		}

		return wb;
	}


}
