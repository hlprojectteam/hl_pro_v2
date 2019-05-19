package com.datacenter.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.common.utils.helper.DateUtil;
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
import com.datacenter.dao.ITransferRegistrationDao;
import com.datacenter.module.TransferRegistration;
import com.datacenter.service.ITransferRegistrationService;
import com.datacenter.vo.TransferRegistrationVo;

/**
 * @Description 交接班登记表 service实现
 * @author xuezb
 * @date 2019年2月19日
 */
@Repository("transferRegistrationServiceImpl")
public class TransferRegistrationServiceImpl extends BaseServiceImpl implements ITransferRegistrationService{

	@Autowired
	private ITransferRegistrationDao transferRegistrationDaoImpl;

	@Autowired
	private TotalTableServiceImpl totalTableServiceImpl;


	@Override
	public Pager queryEntityList(Integer page, Integer rows, TransferRegistrationVo transferRegistrationVo) {
		List<Object> objectList = new ArrayList<>();

		StringBuilder sql = new StringBuilder("select  id,create_time,duty_Date,exception_,form_Number,handover_Matters,handover_Time,lase_Watcher,shift_,this_Watcher,title_,ttId,watch_Time_End,watch_Time_Start,weather_  from dc_TransferRegistration where 1 = 1 ");
		if(StringUtils.isNotBlank(transferRegistrationVo.getTtId())){
			objectList.add(transferRegistrationVo.getTtId());
			sql.append(" and ttId = ? ");
		}
		if(transferRegistrationVo.getDutyDateStart() != null){		//日期Start
			objectList.add(transferRegistrationVo.getDutyDateStart());
			sql.append(" and duty_Date >= ? ");
		}
		if(transferRegistrationVo.getDutyDateEnd() != null){		//日期End
			objectList.add(transferRegistrationVo.getDutyDateEnd());
			sql.append(" and duty_Date <= ? ");
		}

		if(StringUtils.isNotBlank(transferRegistrationVo.getKeyword())){
			sql.append(" and (");
			objectList.add("%" + transferRegistrationVo.getKeyword() + "%");
			sql.append(" this_Watcher like ? ");
			objectList.add("%" + transferRegistrationVo.getKeyword() + "%");
			sql.append(" or lase_Watcher like ? ");
			objectList.add("%" + transferRegistrationVo.getKeyword() + "%");
			sql.append(" or handover_Matters like ? ");
			objectList.add("%" + transferRegistrationVo.getKeyword() + "%");
			sql.append(" or exception_ like ? ");
			sql.append(" )");
		}


		//排序, 先根据日期倒序排序,再根据班次顺序排序
		sql.append(" order by duty_Date desc,shift_ asc ");

		Pager pager = this.transferRegistrationDaoImpl.queryEntitySQLList(page, rows, sql.toString(), objectList);

		ArrayList<TransferRegistrationVo> trVoList = new ArrayList<>();
		for (int i = 0; i < pager.getPageList().size(); i++) {
			Object[] obj = (Object[])pager.getPageList().get(i);
			TransferRegistrationVo trVo = new TransferRegistrationVo();
			if(obj[0]!=null)  trVo.setId(obj[0].toString());
			if(obj[1]!=null)  trVo.setCreateTime(DateUtil.getDateFromString(obj[1].toString()));
			if(obj[2]!=null)  trVo.setDutyDate(DateUtil.getDateFromString(obj[2].toString()));
			if(obj[3]!=null)  trVo.setException(obj[3].toString());
			if(obj[4]!=null)  trVo.setFormNumber(obj[4].toString());
			if(obj[5]!=null)  trVo.setHandoverMatters(obj[5].toString());
			if(obj[6]!=null)  trVo.setHandoverTime(DateUtil.getDateFromString(obj[6].toString()));
			if(obj[7]!=null)  trVo.setLaseWatcher(obj[7].toString());
			if(obj[8]!=null)  trVo.setShift(Integer.parseInt(obj[8].toString()));
			if(obj[9]!=null)  trVo.setThisWatcher(obj[9].toString());
			if(obj[10]!=null)  trVo.setTitle(obj[10].toString());
			if(obj[11]!=null)  trVo.setTtId(obj[11].toString());
			if(obj[12]!=null)  trVo.setWatchTimeEnd(DateUtil.getDateFromString(obj[12].toString()));
			if(obj[13]!=null)  trVo.setWatchTimeStart(DateUtil.getDateFromString(obj[13].toString()));
			if(obj[14]!=null)  trVo.setWeather(Integer.parseInt(obj[14].toString()));
			trVoList.add(trVo);
		}
		pager.setPageList(trVoList);
		return pager;
	}

	@Override
	public TransferRegistration saveOrUpdate(TransferRegistrationVo transferRegistrationVo) {
		TransferRegistration transferRegistration = new TransferRegistration();
		BeanUtils.copyProperties(transferRegistrationVo, transferRegistration);
		if(StringUtils.isBlank(transferRegistration.getId())){
			this.save(transferRegistration);
		}else{
			this.update(transferRegistration);
		}
		return transferRegistration;
	}

	@Override
	public int deleteByTtId(String ttId) {
		return this.transferRegistrationDaoImpl.excuteBySql("delete from dc_TransferRegistration where ttId = '" + ttId + "'");
	}

	@Override
	public int updateDutyDate(String ttId, Date dutyDate) {
		List<Criterion> params = new ArrayList<>();
		if(StringUtils.isNotBlank(ttId)){
			params.add(Restrictions.eq("ttId", ttId));
		}
		List<TransferRegistration> list = this.transferRegistrationDaoImpl.queryEntityList(params, Order.desc("createTime"), TransferRegistration.class);	//根据主表Id获取子表关联数据
		for (TransferRegistration transferRegistration : list) {
			transferRegistration.setDutyDate(dutyDate);
			this.update(transferRegistration);				//修改子表关联数据的日期,保持和主表一致
		}
		return list.size();
	}

	@Override
	public List<TransferRegistration> queryEntityList(TransferRegistrationVo transferRegistrationVo) {
		List<Object> objectList = new ArrayList<>();

		StringBuilder hql = new StringBuilder("from TransferRegistration where 1 = 1 ");
		if(StringUtils.isNotBlank(transferRegistrationVo.getTtId())){
			objectList.add(transferRegistrationVo.getTtId());
			hql.append(" and ttId = ? ");
		}
		if(transferRegistrationVo.getDutyDateStart() != null){		//日期Start
			objectList.add(transferRegistrationVo.getDutyDateStart());
			hql.append(" and dutyDate >= ? ");
		}
		if(transferRegistrationVo.getDutyDateEnd() != null){		//日期End
			objectList.add(transferRegistrationVo.getDutyDateEnd());
			hql.append(" and dutyDate <= ? ");
		}

		if(StringUtils.isNotBlank(transferRegistrationVo.getKeyword())){
			hql.append(" and (thisWatcher like '%").append(transferRegistrationVo.getKeyword()).append("%' ").append(" or laseWatcher like '%").append(transferRegistrationVo.getKeyword()).append("%' ").append(" or handoverMatters like '%").append(transferRegistrationVo.getKeyword()).append("%' ").append(" or exception like '%").append(transferRegistrationVo.getKeyword()).append("%' )");
		}
		//排序, 先根据日期倒序排序,再根据班次顺序排序
		hql.append(" order by dutyDate desc,shift asc ");

		return this.transferRegistrationDaoImpl.queryEntityHQLList(hql.toString(), objectList, TransferRegistration.class);
	}

	@Override
	public HSSFWorkbook export(TransferRegistrationVo transferRegistrationVo) {
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

		List<TransferRegistration> trList = queryEntityList(transferRegistrationVo);

		//创建sheet
		HSSFSheet sheet = wb.createSheet("交接班登记表汇总");

		//合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));

		//创建行（第一行）
		HSSFRow row0 = sheet.createRow(0);
		//设置行的高度
		row0.setHeightInPoints(30);
		//创建单元格 并 设置单元格内容
		row0.createCell(0).setCellValue("环龙运营控制指挥中心交接班登记表");
		//设置单元格样式
		row0.getCell(0).setCellStyle(r0_style);


		//第二行
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue("表单编号：HLZXRBB-02");
		row1.getCell(0).setCellStyle(r1_style);


		//第三行
		HSSFRow row2 = sheet.createRow(2);
		row2.setHeightInPoints(30);		//行高
		HSSFCell cell;
		String[] title = {"日期","班次","天气","本班次值班人员","值班时间","上班次值班人员","交接时间","交接事项","接班异常情况"};
		for(int i=0;i<title.length;i++){
			cell = row2.createCell(i);		//创建单元格
			cell.setCellValue(title[i]);	//设置单元格内容
			cell.setCellStyle(r2_style);	//设置单元格样式

			//列宽自适应（该方法在老版本的POI中效果不佳）
			/*sheet.autoSizeColumn(i);*/
			//设置列宽
			if(i == 6){
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*4);
			}else{
				sheet.setColumnWidth(i, sheet.getColumnWidth(i)*5/2);
			}
		}

		//第四行 及 之后的行
		HSSFRow row;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
		for (int i = 0; i < trList.size(); i++) {
			row = sheet.createRow(i + 3);	//创建行
			row.setHeightInPoints(60);					//设置行高
			for (int j = 0; j < title.length; j++) {
				cell = row.createCell(j);				//创建单元格
				//设置单元格内容
				switch (j){
					case 0:	cell.setCellValue(sdf1.format(trList.get(i).getDutyDate())); break;
					case 1:	cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_shift", trList.get(i).getShift().toString())); break;
					case 2: cell.setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_weather", trList.get(i).getWeather().toString()));	break;
					case 3: cell.setCellValue(trList.get(i).getThisWatcher()); break;
					case 4:
						if(trList.get(i).getShift() == 3){
							cell.setCellValue(sdf.format(trList.get(i).getWatchTimeStart()) + "--次日" + sdf.format(trList.get(i).getWatchTimeEnd()));	break;
						}else{
							cell.setCellValue(sdf.format(trList.get(i).getWatchTimeStart()) + "--" + sdf.format(trList.get(i).getWatchTimeEnd()));	break;
						}
					case 5: cell.setCellValue(trList.get(i).getLaseWatcher());	break;
					case 6: cell.setCellValue(sdf.format(trList.get(i).getHandoverTime()));	break;
					case 7: cell.setCellValue(trList.get(i).getHandoverMatters()); break;
					case 8: cell.setCellValue(trList.get(i).getException()); break;
				}
				//设置单元格样式
				if(j == 7){
					cell.setCellStyle(mainStyle_left);
				}else{
					cell.setCellStyle(mainStyle_center);
				}
			}
		}

		return wb;
	}

}
