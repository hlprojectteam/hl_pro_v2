package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.datacenter.dao.IOperatingDataDao;
import com.datacenter.module.OperatingData;
import com.datacenter.ql.datacenterQl;
import com.datacenter.service.IOperatingDataService;
import com.datacenter.vo.OperatingDataVo;

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

import java.util.*;

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
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(datacenterQl.MySql.operatingData);
		sql.append(" where 1=1");
		if(StringUtils.isNotBlank(operatingDataVo.getTtId())){
			sql.append(" and t.ttId='"+operatingDataVo.getTtId()+"'");
		}
		if(operatingDataVo.getDutyDateStart() != null){		//日期Start
			sql.append(" and t.duty_Date>='"+ DateUtil.getDateFormatString(operatingDataVo.getDutyDateStart(),"yyyy-MM-dd HH:mm:ss")+"'");
		}
		if(operatingDataVo.getDutyDateEnd() != null){		//日期End
			sql.append(" and t.duty_Date<='"+DateUtil.getDateFormatString(operatingDataVo.getDutyDateEnd(),"yyyy-MM-dd HH:mm:ss")+"'");
		}
		if(operatingDataVo.getTollGate() != null){
			sql.append(" and t.toll_Gate ="+operatingDataVo.getTollGate());
		}
		if(StringUtils.isNotBlank(operatingDataVo.getKeyword())){
			sql.append(" and (");
			sql.append(" t.total_Traffic="+operatingDataVo.getKeyword());
			sql.append(" or t.ytk_Traffic="+operatingDataVo.getKeyword());
			sql.append(" or t.mobile_Payment_Traffic="+operatingDataVo.getKeyword());
			sql.append(" or t.general_Income="+operatingDataVo.getKeyword());
			sql.append(" or t.ytk_Income="+operatingDataVo.getKeyword());
			sql.append(" or t.mobile_Payment_Income="+operatingDataVo.getKeyword());
			sql.append(")");
		}
		sql.append(" ORDER BY t.duty_Date DESC,t.toll_Gate ASC");//按日期倒序，收费站顺序
		Pager pager = this.operatingDataDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
		if(pager!=null){
			List<OperatingData> list = new ArrayList<OperatingData>();
			for (int i = 0; i < pager.getPageList().size(); i++) {
				Object[] obj = (Object[])pager.getPageList().get(i);
				OperatingData od = new OperatingData();
				if(obj[0]!=null) od.setId(obj[0].toString());
				if(obj[1]!=null) od.setCreateTime(DateUtil.getDateFromString(obj[1].toString()));
				if(obj[2]!=null) od.setCreatorId(obj[2].toString());
				if(obj[3]!=null) od.setCreatorName(obj[3].toString());
				if(obj[5]!=null) od.setDutyDate(DateUtil.getDateFromString(obj[5].toString()));
				if(obj[6]!=null) od.setFormNumber(obj[6].toString());
				if(obj[7]!=null) od.setGeneralIncome(Double.parseDouble(obj[7].toString()));
				if(obj[8]!=null) od.setTitle(obj[8].toString());
				if(obj[9]!=null) od.setTollGate(Integer.parseInt(obj[9].toString()));
				if(obj[10]!=null) od.setTotalTraffic(Integer.parseInt(obj[10].toString()));
				if(obj[11]!=null) od.setTtId(obj[11].toString());
				if(obj[12]!=null) od.setYtkIncome(Double.parseDouble(obj[12].toString()));
				if(obj[13]!=null) od.setYtkTraffic(Integer.parseInt(obj[13].toString()));
				if(obj[14]!=null) od.setMobilePaymentIncome(Double.parseDouble(obj[14].toString()));
				if(obj[15]!=null) od.setMobilePaymentTraffic(Integer.parseInt(obj[15].toString()));
				list.add(od);
			}
			pager.setPageList(list);
		}
		return pager;


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
		List<Criterion> params = new ArrayList<>();
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
		List<Object> objectList = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append(datacenterQl.MySql.operatingTotalGroupByTollGate);
		if(StringUtils.isNotBlank(operatingDataVo.getTtId())){
			objectList.add(operatingDataVo.getTtId());
			sql.append(" and t.ttId=? ");
		}
		if(operatingDataVo.getDutyDateStart() != null){		//日期Start
			objectList.add(operatingDataVo.getDutyDateStart());
			sql.append(" and duty_Date >= ? ");
		}
		if(operatingDataVo.getDutyDateEnd() != null){		//日期End
			objectList.add(operatingDataVo.getDutyDateEnd());
			sql.append(" and duty_Date <= ? ");
		}

		if(operatingDataVo.getTollGate() != null){
			objectList.add(operatingDataVo.getTollGate());
			sql.append(" and toll_Gate = ? ");
		}
		if(StringUtils.isNotBlank(operatingDataVo.getKeyword())){
			sql.append(" and (total_Traffic =").append(operatingDataVo.getKeyword())
			.append(" or ytk_Traffic = ").append(operatingDataVo.getKeyword())  
			.append(" or mobile_Payment_Traffic = ").append(operatingDataVo.getKeyword())
			.append(" or general_Income = ").append(operatingDataVo.getKeyword())
			.append(" or ytk_Income = ").append(operatingDataVo.getKeyword())
			.append(" or mobile_Payment_Income = ").append(operatingDataVo.getKeyword());
		}
		sql.append(" GROUP BY t.toll_Gate "); 
		List<Object[]> listOper=this.operatingDataDaoImpl.queryEntitySQLList(sql.toString(), objectList);
		List<OperatingData> listArr = new ArrayList<OperatingData>();
		if(listOper!=null){
			for (Object[] obj : listOper) {
				OperatingData operatingData = new OperatingData();
	    		if(obj[0]!=null) operatingData.setTollGate(Integer.parseInt(obj[0].toString()));
	    		if(obj[1]!=null) operatingData.setTotalTraffic(Integer.parseInt(obj[1].toString()));
	    		if(obj[2]!=null) operatingData.setYtkTraffic(Integer.parseInt(obj[2].toString()));
	    		if(obj[3]!=null) operatingData.setMobilePaymentTraffic(Integer.parseInt(obj[3].toString()));
	    		
	    		if(obj[4]!=null) operatingData.setGeneralIncome(Double.parseDouble(obj[4].toString()));
	    		if(obj[5]!=null) operatingData.setYtkIncome(Double.parseDouble(obj[5].toString()));
	    		if(obj[6]!=null) operatingData.setMobilePaymentIncome(Double.parseDouble(obj[6].toString()));
	    		listArr.add(operatingData);
			}
		}
		return listArr;
		
//		List<Object> objectList = new ArrayList<>();

//		StringBuilder hql = new StringBuilder("from OperatingData where 1 = 1 ");
//		if(StringUtils.isNotBlank(operatingDataVo.getTtId())){
//			objectList.add(operatingDataVo.getTtId());
//			hql.append(" and ttId = ? ");
//		}
//		if(operatingDataVo.getDutyDateStart() != null){		//日期Start
//			objectList.add(operatingDataVo.getDutyDateStart());
//			hql.append(" and dutyDate >= ? ");
//		}
//		if(operatingDataVo.getDutyDateEnd() != null){		//日期End
//			objectList.add(operatingDataVo.getDutyDateEnd());
//			hql.append(" and dutyDate <= ? ");
//		}
//
//		if(operatingDataVo.getTollGate() != null){
//			objectList.add(operatingDataVo.getTollGate());
//			hql.append(" and tollGate = ? ");
//		}
//		if(StringUtils.isNotBlank(operatingDataVo.getKeyword())){
//			hql.append(" and (totalTraffic =").append(operatingDataVo.getKeyword())
//			.append(" or ytkTraffic = ").append(operatingDataVo.getKeyword())
//			.append(" or generalIncome = ").append(operatingDataVo.getKeyword())
//			.append(" or ytkIncome = ").append(operatingDataVo.getKeyword());
//		}
//		//排序, 根据日期倒序排序，收费站顺序排序
//		hql.append(" order by dutyDate desc,tollGate asc ");
//
//		return this.operatingDataDaoImpl.queryEntityHQLList(hql.toString(), objectList, OperatingData.class);
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

		List<OperatingData> odList = queryEntityList(operatingDataVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("营运数据汇总");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 4));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 7));

		sheet.addMergedRegion(new CellRangeAddress(odList.size() + 4, odList.size() + 4, 0, 1));

		sheet.setColumnWidth(0, sheet.getColumnWidth(0));
		for (int i = 1; i < 8; i++) {
			//列宽自适应（该方法在老版本的POI中效果不佳）
			/*sheet.autoSizeColumn(i);*/
			//设置列宽
			sheet.setColumnWidth(i, sheet.getColumnWidth(i)*2);
		}


		//创建行（第一行）
		HSSFRow row0 = sheet.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		if(operatingDataVo.getDutyDateStart()!=null&&operatingDataVo.getDutyDateEnd()!=null){
			String dual=DateUtil.getDateFormatString(operatingDataVo.getDutyDateStart(), DateUtil.JAVA_DATE_FORMAT_YMD);
			dual+="-"+DateUtil.getDateFormatString(operatingDataVo.getDutyDateEnd(), DateUtil.JAVA_DATE_FORMAT_YMD);
			row0.createCell(0).setCellValue("各站拆分前营运数据("+dual+")");
		}else{
			row0.createCell(0).setCellValue("各站拆分前营运数据");
		}
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-07");
		row1.getCell(0).setCellStyle(r1_style);


		//第三行
		HSSFRow row2 = sheet.createRow(2);
		row2.setHeightInPoints(30);		//行高
		HSSFCell cell;
		String[] title1 = {"序号","收费站","出口车流量","收费额"};
		row2.createCell(3).setCellStyle(r2_style);
		row2.createCell(4).setCellStyle(r2_style);
		row2.createCell(6).setCellStyle(r2_style);
		row2.createCell(7).setCellStyle(r2_style);
		for(int i=0;i<title1.length;i++){
			if(i == 3){
				cell = row2.createCell(5);		//创建单元格
			}else{
				cell = row2.createCell(i);		//创建单元格
			}
			cell.setCellValue(title1[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}

		//第四行
		HSSFRow row3 = sheet.createRow(3);
		row3.setHeightInPoints(30);		//行高
		String[] title2 = {"总车流","其中粤通卡车流","移动支付车流","总收费额","其中粤通卡收入","移动支付收入"};
		row3.createCell(0).setCellStyle(r2_style);
		for(int i=0;i<title2.length;i++){
			cell = row3.createCell(i + 2);		//创建单元格
			cell.setCellValue(title2[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}

		//第五行 及 之后的行
		HSSFRow row;
		Integer hj_totalTraffic = 0;
		Integer hj_ytkTraffic = 0;
		Integer hj_mobilePaymentTraffic = 0;
		Double hj_generalIncome = 0.0;
		Double hj_ytkIncome = 0.0;
		Double hj_mobilePaymentIncome = 0.0;
		for (int i = 0; i < odList.size(); i++) {
			row = sheet.createRow(i + 4);	//创建行
			row.setHeightInPoints(20);				//设置行高
			for (int j = 0; j < 8; j++) {
				cell = row.createCell(j);			//创建单元格
				//设置单元格内容
				switch (j){
					case 0: cell.setCellValue(i+1);	break;
					case 1: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_tollGate_operation", odList.get(i).getTollGate().toString()));	break;
					case 2: cell.setCellValue(odList.get(i).getTotalTraffic());	break;
					case 3: cell.setCellValue(odList.get(i).getYtkTraffic());	break;
					case 4:
						if(odList.get(i).getMobilePaymentTraffic() != null){
							cell.setCellValue(odList.get(i).getMobilePaymentTraffic());
						}else{
							cell.setCellValue("");
						}
						break;
					case 5: cell.setCellValue(odList.get(i).getGeneralIncome());	break;
					case 6: cell.setCellValue(odList.get(i).getYtkIncome());	break;
					case 7:
						if(odList.get(i).getMobilePaymentIncome() != null){
							cell.setCellValue(odList.get(i).getMobilePaymentIncome());
						}else{
							cell.setCellValue("");
						}
						break;
				}
				//设置单元格样式
				cell.setCellStyle(mainStyle_center);
			}
			hj_totalTraffic += odList.get(i).getTotalTraffic();
			hj_ytkTraffic += odList.get(i).getYtkTraffic();
			if(odList.get(i).getMobilePaymentTraffic() != null){
				hj_mobilePaymentTraffic += odList.get(i).getMobilePaymentTraffic();
			}
			hj_generalIncome += odList.get(i).getGeneralIncome();
			hj_ytkIncome += odList.get(i).getYtkIncome();
			if(odList.get(i).getMobilePaymentIncome() != null){
				hj_mobilePaymentIncome += odList.get(i).getMobilePaymentIncome();
			}
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
		row.createCell(4).setCellValue(hj_mobilePaymentTraffic);
		row.getCell(4).setCellStyle(mainStyle_center);
		row.createCell(5).setCellValue(hj_generalIncome);
		row.getCell(5).setCellStyle(mainStyle_center);
		row.createCell(6).setCellValue(hj_ytkIncome);
		row.getCell(6).setCellStyle(mainStyle_center);
		row.createCell(7).setCellValue(hj_mobilePaymentIncome);
		row.getCell(7).setCellStyle(mainStyle_center);

		return wb;
	}

	@Override
	public boolean isRecordExist(Date dutyDate, String tollGateId) {
		OperatingDataVo operatingDataVo=new OperatingDataVo();
		operatingDataVo.setDutyDate(dutyDate);
		if(tollGateId!=null){
			operatingDataVo.setTollGate(Integer.parseInt(tollGateId));
		}

		List<Criterion> params = new ArrayList<>();
		if(operatingDataVo.getDutyDate() != null){		//日期
			params.add(Restrictions.eq("dutyDate", operatingDataVo.getDutyDate()));
		}
		if(operatingDataVo.getTollGate() != null){		//收费站
			params.add(Restrictions.eq("tollGate", operatingDataVo.getTollGate()));
		}
		List<OperatingData> list= this.operatingDataDaoImpl.queryEntityList(params, Order.desc("createTime"), OperatingData.class);
		if(list!=null){
			return list.size() > 0;
		}
		return false;
	}

	@Override
	public Map<String, String> operatingDataTotal(OperatingDataVo operatingDataVo) {

		Map<String, String> m=new HashMap<String, String>();
		m.put("totalTraffic","0");
		m.put("ytkTraffic","0");
		m.put("mobilePaymentTraffic","0");
		m.put("generalIncome","0");
		m.put("ytkIncome","0");
		m.put("mobilePaymentIncome","0");

		StringBuffer sql = new StringBuffer();
		sql.append(datacenterQl.MySql.operatingTotal);
		sql.append(" where 1=1");
		if(StringUtils.isNotBlank(operatingDataVo.getTtId())){
			sql.append(" and t.ttId='"+operatingDataVo.getTtId()+"'");
		}
		if(operatingDataVo.getDutyDateStart() != null){		//日期Start
			sql.append(" and t.duty_Date>='"+ DateUtil.getDateFormatString(operatingDataVo.getDutyDateStart(),"yyyy-MM-dd HH:mm:ss")+"'");
		}
		if(operatingDataVo.getDutyDateEnd() != null){		//日期End
			sql.append(" and t.duty_Date<='"+DateUtil.getDateFormatString(operatingDataVo.getDutyDateEnd(),"yyyy-MM-dd HH:mm:ss")+"'");
		}
		if(operatingDataVo.getTollGate() != null){
			sql.append(" and t.toll_Gate ="+operatingDataVo.getTollGate());
		}

		List<Object> list  = this.operatingDataDaoImpl.queryBySql(sql.toString());
		if(list!=null){
			Object[] obj = (Object[])list.get(0);
			if(obj!=null){
				if(obj[0]!=null){
					m.put("totalTraffic", obj[0].toString());
				}
				if(obj[1]!=null){
					m.put("ytkTraffic", obj[1].toString());
				}
				if(obj[2]!=null){
					m.put("mobilePaymentTraffic", obj[2].toString());
				}
				if(obj[3]!=null){
					m.put("generalIncome", obj[3].toString());
				}
				if(obj[4]!=null){
					m.put("ytkIncome", obj[4].toString());
				}
				if(obj[5]!=null){
					m.put("mobilePaymentIncome", obj[5].toString());
				}
			}
		}
		return m;
	}

}
