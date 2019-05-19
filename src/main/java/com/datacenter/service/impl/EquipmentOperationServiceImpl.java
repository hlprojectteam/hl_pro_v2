package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.datacenter.dao.IEquipmentOperationDao;
import com.datacenter.module.EquipmentOperation;
import com.datacenter.service.IEquipmentOperationService;
import com.datacenter.vo.EquipmentOperationVo;
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
		List<Criterion> params = new ArrayList<>();
		if(StringUtils.isNotBlank(equipmentOperationVo.getTtId())){
			params.add(Restrictions.eq("ttId", equipmentOperationVo.getTtId()));
		}
		if(equipmentOperationVo.getDutyDateStart() != null){		//日期Start
			params.add(Restrictions.ge("dutyDate", equipmentOperationVo.getDutyDateStart()));
		}
		if(equipmentOperationVo.getDutyDateEnd() != null){		//日期End
			params.add(Restrictions.le("dutyDate", equipmentOperationVo.getDutyDateEnd()));
		}

		if(equipmentOperationVo.getTollGate() != null){
			params.add(Restrictions.eq("tollGate", equipmentOperationVo.getTollGate()));
		}
		if(equipmentOperationVo.getIsOrNot() != null){
			if(equipmentOperationVo.getIsOrNot() == 1){
				params.add(Restrictions.sqlRestriction(" (cdgqzp_=3 or zdfkj_=3 or mtcckcd_=3 or etcckcd_=3 or mtcrkcd_=3 or etcrkcd_=3 or jzcd_=3) "));
			}else{
				params.add(Restrictions.sqlRestriction(" (cdgqzp_!=3 and zdfkj_!=3 and mtcckcd_!=3 and etcckcd_!=3 and mtcrkcd_!=3 and etcrkcd_!=3 and jzcd_!=3) "));
			}
		}

		if(StringUtils.isNotBlank(equipmentOperationVo.getKeyword())){
			params.add(Restrictions.sqlRestriction(" remark_ like '%" + equipmentOperationVo.getKeyword() + "%' "));
		}
		return this.equipmentOperationDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), EquipmentOperation.class);
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
				hql.append(" and (cdgqzp_=3 or zdfkj_=3 or mtcckcd_=3 or etcckcd_=3 or mtcrkcd_=3 or etcrkcd_=3 or jzcd_=3) ");
			}else{
				hql.append(" and (cdgqzp_!=3 and zdfkj_!=3 and mtcckcd_!=3 and etcckcd_!=3 and mtcrkcd_!=3 and etcrkcd_!=3 and jzcd_!=3) ");
			}
		}

		if(StringUtils.isNotBlank(equipmentOperationVo.getKeyword())){
			hql.append(" and remark_ like '%").append(equipmentOperationVo.getKeyword()).append("%' ");
		}
		//排序, 根据日期倒序排序，部门(收费站)顺序排序
		hql.append(" order by dutyDate desc,tollGate asc ");

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
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*5);
			}else{
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*2);
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
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		for (int i = 0; i < eoList.size(); i++) {
			row = sheet.createRow(i + 3);	//创建行
			row.setHeightInPoints(65);					//设置行高
			for (int j = 0; j < title.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(sdf1.format(eoList.get(i).getDutyDate())); break;
					case 1:	cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_tollGate", eoList.get(i).getTollGate().toString()));	break;
					case 2: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_equipmentStatus", eoList.get(i).getCdgqzp().toString()));	break;
					case 3: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_equipmentStatus", eoList.get(i).getZdfkj().toString()));	break;
					case 4: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_equipmentStatus", eoList.get(i).getMtcckcd().toString()));	break;
					case 5: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_equipmentStatus", eoList.get(i).getEtcckcd().toString()));	break;
					case 6: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_equipmentStatus", eoList.get(i).getMtcrkcd().toString()));	break;
					case 7: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_equipmentStatus", eoList.get(i).getEtcckcd().toString()));	break;
					case 8: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_equipmentStatus", eoList.get(i).getJzcd().toString()));	break;
					case 9: cell.setCellValue(eoList.get(i).getRemark());	break;
					case 10:
					if(eoList.get(i).getDownTimeStart() != null && eoList.get(i).getDownTimeEnd() != null){
						 cell.setCellValue(sdf.format(eoList.get(i).getDownTimeStart()) + "--" + sdf.format(eoList.get(i).getDownTimeEnd()));	break;
					}else{
						 cell.setCellValue("");	break;
					}

				}
				//设置单元格样式
				if(j == 9) {
					cell.setCellStyle(mainStyle_left);
				}else{
					cell.setCellStyle(mainStyle_center);
				}
			}
		}

		return wb;


	}

}
