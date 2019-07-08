package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.MathUtil;
import com.common.utils.helper.DateUtil;
import com.common.utils.helper.Pager;
import com.datacenter.dao.IEquipmentOperationDao;
import com.datacenter.module.EquipmentOperation;
import com.datacenter.ql.datacenterQl;
import com.datacenter.service.IEquipmentOperationService;
import com.datacenter.vo.EquipmentOperationVo;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
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
 * @Description 设备运行情况统计表 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("equipmentOperationServiceImpl")
public class EquipmentOperationServiceImpl extends BaseServiceImpl implements IEquipmentOperationService{

	@Autowired
	private IEquipmentOperationDao equipmentOperationDaoImpl;

	@Autowired
	private TotalTableServiceImpl totalTableServiceImpl;

	
	@Override
	public Pager queryEntityList(Integer page, Integer rows, EquipmentOperationVo equipmentOperationVo) {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(datacenterQl.MySql.equipmentOperationData);
		sql.append(" where 1=1");
		if(StringUtils.isNotBlank(equipmentOperationVo.getTtId())){
			sql.append(" and t.ttId='"+equipmentOperationVo.getTtId()+"'");
		}
		if(equipmentOperationVo.getDutyDateStart() != null){		//日期
			sql.append(" and t.duty_Date>='"+ DateUtil.getDateFormatString(equipmentOperationVo.getDutyDateStart(),"yyyy-MM-dd HH:mm:ss")+"'");
		}
		if(equipmentOperationVo.getDutyDateEnd() != null){		//日期End
			sql.append(" and t.duty_Date<='"+DateUtil.getDateFormatString(equipmentOperationVo.getDutyDateEnd(),"yyyy-MM-dd HH:mm:ss")+"'");
		}
		if(equipmentOperationVo.getTollGate() != null){
			sql.append(" and t.toll_Gate ="+equipmentOperationVo.getTollGate());
		}
		if(equipmentOperationVo.getIsOrNot() != null){
			if(equipmentOperationVo.getIsOrNot() == 1){
				sql.append(" and (cdgqzp_=1 and zdfkj_=1 and mtcckcd_=1 and etcckcd_=1 and mtcrkcd_=1 and etcrkcd_=1 and jzcd_=1) ");
			}else if(equipmentOperationVo.getIsOrNot() == 2){
				sql.append("  and (cdgqzp_=2 or zdfkj_=2 or mtcckcd_=2 or etcckcd_=2 or mtcrkcd_=2 or etcrkcd_=2 or jzcd_=2) " +
					" and (cdgqzp_ !=3 and zdfkj_ !=3 and mtcckcd_ !=3 and etcckcd_ !=3 and mtcrkcd_ !=3 and etcrkcd_ !=3 and jzcd_ !=3)");
			}else if(equipmentOperationVo.getIsOrNot() == 3){
				sql.append("  and (cdgqzp_ =3 or zdfkj_ =3 or mtcckcd_ =3  or etcckcd_ =3 or mtcrkcd_ =3 or etcrkcd_ =3 or jzcd_ =3) ");
			}
		}
		if(StringUtils.isNotBlank(equipmentOperationVo.getKeyword())){
			sql.append(" and (");
			sql.append(" t.remark_  like '%" + equipmentOperationVo.getKeyword() + "%' ");
			sql.append(")");
		}
		sql.append(" ORDER BY t.duty_Date DESC,t.toll_Gate ASC");//按日期倒序，收费站顺序
		Pager pager = this.equipmentOperationDaoImpl.queryEntitySQLList(page, rows, sql.toString(), paramList);
		if(pager!=null){
			List<EquipmentOperation> list = new ArrayList<EquipmentOperation>();
			for (int i = 0; i < pager.getPageList().size(); i++) {
				Object[] obj = (Object[])pager.getPageList().get(i);
				EquipmentOperation eo = new EquipmentOperation();
				if(obj[0]!=null) eo.setId(obj[0].toString());
				if(obj[1]!=null) eo.setDutyDate(DateUtil.getDateFromString(obj[1].toString()));
				if(obj[2]!=null) eo.setTollGate(Integer.parseInt(obj[2].toString()));
				if(obj[3]!=null) eo.setCdgqzp(Integer.parseInt(obj[3].toString()));
				if(obj[4]!=null) eo.setZdfkj(Integer.parseInt(obj[4].toString()));
				if(obj[5]!=null) eo.setMtcckcd(Integer.parseInt(obj[5].toString()));
				if(obj[6]!=null) eo.setEtcckcd(Integer.parseInt(obj[6].toString()));
				if(obj[7]!=null) eo.setMtcrkcd(Integer.parseInt(obj[7].toString()));
				if(obj[8]!=null) eo.setEtcrkcd(Integer.parseInt(obj[8].toString()));
				if(obj[9]!=null) eo.setJzcd(Integer.parseInt(obj[9].toString()));
				if(obj[10]!=null) eo.setDownTimeStart(DateUtil.getDateFromString(obj[10].toString()));
				if(obj[11]!=null) eo.setDownTimeEnd(DateUtil.getDateFromString(obj[11].toString()));
				if(obj[12]!=null) eo.setRemark(obj[12].toString());
				if(obj[13]!=null) eo.setTtId(obj[13].toString());
				list.add(eo);
			}
			pager.setPageList(list);
		}
		return pager;
	
	}

	@Override
	public EquipmentOperation saveOrUpdate(EquipmentOperationVo equipmentOperationVo) {
		EquipmentOperation equipmentOperation = new EquipmentOperation();
		BeanUtils.copyProperties(equipmentOperationVo, equipmentOperation);
		if(StringUtils.isBlank(equipmentOperation.getId())){
			this.save(equipmentOperation);
		}else{
			this.update(equipmentOperation);
		}
		return equipmentOperation;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.equipmentOperationDaoImpl.excuteBySql("delete from dc_EquipmentOperation where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<EquipmentOperation> list = this.equipmentOperationDaoImpl.queryEntityList(params, Order.desc("createTime"), EquipmentOperation.class);	//根据主表Id获取子表关联数据
		for (EquipmentOperation equipmentOperation : list) {
			equipmentOperation.setDutyDate(dutyDate);
			this.update(equipmentOperation);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<EquipmentOperation> queryEntityList(EquipmentOperationVo equipmentOperationVo) {
		List<Object> objectList = new ArrayList<>();

		StringBuilder hql = new StringBuilder("from EquipmentOperation where 1 = 1 ");
		if(StringUtils.isNotBlank(equipmentOperationVo.getTtId())){
			objectList.add(equipmentOperationVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(equipmentOperationVo.getDutyDateStart() != null){		//日期Start
			objectList.add(equipmentOperationVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(equipmentOperationVo.getDutyDateEnd() != null){		//日期End
			objectList.add(equipmentOperationVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}

		if(equipmentOperationVo.getTollGate() != null){
			objectList.add(equipmentOperationVo.getTollGate());
			hql.append(" and tollGate = ? ");
		}
		if(equipmentOperationVo.getIsOrNot() != null){
			if(equipmentOperationVo.getIsOrNot() == 1){
				hql.append(" and (cdgqzp=1 and zdfkj=1 and mtcckcd=1 and etcckcd=1 and mtcrkcd=1 and etcrkcd=1 and jzcd=1) ");
			}else if(equipmentOperationVo.getIsOrNot() == 2){
				hql.append(" and (cdgqzp=2 or zdfkj=2 or mtcckcd=2 or etcckcd=2 or mtcrkcd=2 or etcrkcd=2 or jzcd=2) "+ 
						" and (cdgqzp !=3 and zdfkj !=3 and mtcckcd !=3 and etcckcd !=3 and mtcrkcd !=3 and etcrkcd !=3 and jzcd !=3)");
			}else if(equipmentOperationVo.getIsOrNot() == 3){
				hql.append(" and (cdgqzp =3 or zdfkj =3 or mtcckcd =3  or etcckcd =3 or mtcrkcd =3 or etcrkcd =3 or jzcd =3) ");
			}
		}

		if(StringUtils.isNotBlank(equipmentOperationVo.getKeyword())){
			hql.append(" and remark_ like '%").append(equipmentOperationVo.getKeyword()).append("%' ");
		}
		//排序, 根据日期倒序排序，部门(收费站)顺序排序
		hql.append(" order by dutyDate asc,tollGate asc ");

		return this.equipmentOperationDaoImpl.queryEntityHQLList(hql.toString(), objectList, EquipmentOperation.class);
	}

	@Override
	public HSSFWorkbook export(EquipmentOperationVo equipmentOperationVo) {
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


		List<EquipmentOperation> eoList = queryEntityList(equipmentOperationVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("设备运行情况汇总");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 10));

		//列宽自适应（该方法在老版本的POI中效果不佳）
		/*sheet.autoSizeColumn(i);*/
		//设置列宽
		for (int i = 0; i < 11; i++) {
			if(i == 9){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*4);
			}else{
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*3/2);
			}
		}

		//创建行（第一行）
		HSSFRow row0 = sheet.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		row0.createCell(0).setCellValue("各站车道设备运行情况统计表");
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-05");
		row1.getCell(0).setCellStyle(r1_style);


		//第三行
		HSSFRow row3 = sheet.createRow(2);
		row3.setHeightInPoints(30);		//行高
		HSSFCell cell;
		String[] title = {"日期","部门","车道高清抓拍","自动发卡机","MTC出口车道","ETC出口车道","MTC入口车道","ETC入口车道","计重车道","备注","车道停用时间段"};
		for(int i=0;i<title.length;i++){
			cell = row3.createCell(i);		//创建单元格
			cell.setCellValue(title[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式
		}

		//第四行 及 之后的行
		HSSFRow row;
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		for (int i = 0; i < eoList.size(); i++) {
			row = sheet.createRow(i + 3);	//创建行
//			row.setHeightInPoints(45);					//设置行高
			float defaultRowH=30;
			float realRowH=30;
			for (int j = 0; j < title.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(DateUtil.getDateFormatString(eoList.get(i).getDutyDate(),DateUtil.JAVA_DATE_FORMAT_CH_YMD)); break;
					case 1:	cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_tollGate", eoList.get(i).getTollGate().toString()));	break;
					case 2: setCell(cell,wb,mainStyle_center,eoList.get(i).getCdgqzp());  break;
					case 3: setCell(cell,wb,mainStyle_center,eoList.get(i).getZdfkj());  	break;
					case 4: setCell(cell,wb,mainStyle_center,eoList.get(i).getMtcckcd());  	break;
					case 5: setCell(cell,wb,mainStyle_center,eoList.get(i).getEtcckcd());  	break;
					case 6: setCell(cell,wb,mainStyle_center,eoList.get(i).getMtcrkcd());  	break;
					case 7: setCell(cell,wb,mainStyle_center,eoList.get(i).getEtcrkcd());  	break;
					case 8: setCell(cell,wb,mainStyle_center,eoList.get(i).getJzcd());  	break;
					case 9: 
						cell.setCellValue(eoList.get(i).getRemark());	
						if(MathUtil.getCellHeight(sheet.getColumnWidth(row.getCell(j).getColumnIndex()),
								eoList.get(i).getRemark(), defaultRowH)>realRowH){
							realRowH=MathUtil.getCellHeight(sheet.getColumnWidth(row.getCell(j).getColumnIndex()),
									eoList.get(i).getRemark(),defaultRowH);
						}
						break;
					case 10:
					if(eoList.get(i).getDownTimeStart() != null && eoList.get(i).getDownTimeEnd() != null){
						String cellContent=DateUtil.getDateFormatString(eoList.get(i).getDownTimeStart(),DateUtil.JAVA_DATE_FORMAT_HM) 
								 + "--" + DateUtil.getDateFormatString(eoList.get(i).getDownTimeEnd(),DateUtil.JAVA_DATE_FORMAT_HM);
						 cell.setCellValue(cellContent);	
						 if(MathUtil.getCellHeight(sheet.getColumnWidth(row.getCell(j).getColumnIndex()),
								 cellContent, defaultRowH)>realRowH){
								realRowH=MathUtil.getCellHeight(sheet.getColumnWidth(row.getCell(j).getColumnIndex()),
										cellContent,defaultRowH);
							}
						 
						 break;
					}else{
						 cell.setCellValue("");	break;
					}
				}
				//设置单元格样式
				if(j == 9) {
					cell.setCellStyle(mainStyle_left);
				}else if(j>1&&j<9){
					
				}else{
					cell.setCellStyle(mainStyle_center);
				}
			}
			row.setHeightInPoints(realRowH);
		}
		return wb;
	}
	
	private void setCell(HSSFCell cell,HSSFWorkbook wb,HSSFCellStyle mainStyle, int status){
		HSSFCellStyle style_ = wb.createCellStyle();
		if(status==1){
			//正常样式
			style_.cloneStyleFrom(mainStyle);
			style_.setAlignment(HorizontalAlignment.CENTER);
			HSSFFont r2_font = wb.createFont();
			r2_font.setBold(true);						//字体加粗
			r2_font.setColor(IndexedColors.GREEN.getIndex());
			r2_font.setFontHeightInPoints((short)20);	//字体大小
			style_.setFont(r2_font);
		}else if(status==2){
			//损坏但不影响样式
			style_.cloneStyleFrom(mainStyle);
			style_.setAlignment(HorizontalAlignment.CENTER);
			HSSFFont r2_font = wb.createFont();
			r2_font.setBold(true);						//字体加粗
			r2_font.setColor(IndexedColors.YELLOW.getIndex());
			r2_font.setFontHeightInPoints((short)20);	//字体大小
			style_.setFont(r2_font);
		}else if(status==3){
			//损坏样式
			style_.cloneStyleFrom(mainStyle);
			style_.setAlignment(HorizontalAlignment.CENTER);
			HSSFFont r2_font = wb.createFont();
			r2_font.setBold(true);						//字体加粗
			r2_font.setColor(IndexedColors.RED.getIndex());
			r2_font.setFontHeightInPoints((short)20);	//字体大小
			style_.setFont(r2_font);
		}
		cell.setCellValue("●");
		cell.setCellStyle(style_);
	}

}
