package com.datacenter.service.impl;

import com.common.base.service.impl.BaseServiceImpl;
import com.common.utils.helper.Pager;
import com.datacenter.dao.IEquipmentStatusDao;
import com.datacenter.module.EquipmentStatus;
import com.datacenter.service.IEquipmentStatusService;
import com.datacenter.vo.EquipmentStatusVo;
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
 * @Description 联网关键设备运行状态日常检查表 service实现
 * @author xuezb
 * @date 2019年2月18日
 */
@Repository("equipmentStatusServiceImpl")
public class EquipmentStatusServiceImpl extends BaseServiceImpl implements IEquipmentStatusService {

    @Autowired
    private IEquipmentStatusDao equipmentStatusDaoImpl;

    @Autowired
    private TotalTableServiceImpl totalTableServiceImpl;


    @Override
    public Pager queryEntityList(Integer page, Integer rows, EquipmentStatusVo equipmentStatusVo) {
        List<Criterion> params = new ArrayList<Criterion>();
        if(StringUtils.isNotBlank(equipmentStatusVo.getTtId())){
            params.add(Restrictions.eq("ttId", equipmentStatusVo.getTtId()));
        }
        if(equipmentStatusVo.getDutyDateStart() != null){		//日期Start
            params.add(Restrictions.ge("dutyDate", equipmentStatusVo.getDutyDateStart()));
        }
        if(equipmentStatusVo.getDutyDateEnd() != null){		//日期End
            params.add(Restrictions.le("dutyDate", equipmentStatusVo.getDutyDateEnd()));
        }
        return this.equipmentStatusDaoImpl.queryEntityList(page, rows, params, Order.desc("createTime"), EquipmentStatus.class);
    }

    @Override
    public EquipmentStatus saveOrUpdate(EquipmentStatusVo equipmentStatusVo) {
        EquipmentStatus equipmentStatus = new EquipmentStatus();
        BeanUtils.copyProperties(equipmentStatusVo, equipmentStatus);
        if(StringUtils.isBlank(equipmentStatus.getId())){
            this.save(equipmentStatus);
        }else{
            this.update(equipmentStatus);
        }
        return equipmentStatus;
    }

    @Override
    public int deleteByTtId(String ttId) {
        return this.equipmentStatusDaoImpl.excuteBySql("delete from dc_EquipmentStatus where ttId = '" + ttId + "'");
    }

    @Override
    public int updateDutyDate(String ttId, Date dutyDate) {
        List<Criterion> params = new ArrayList<Criterion>();
        if(StringUtils.isNotBlank(ttId)){
            params.add(Restrictions.eq("ttId", ttId));
        }
        List<EquipmentStatus> list = this.equipmentStatusDaoImpl.queryEntityList(params, Order.desc("createTime"), EquipmentStatus.class);	//根据主表Id获取子表关联数据
        for (EquipmentStatus equipmentStatus : list) {
            equipmentStatus.setDutyDate(dutyDate);
            this.update(equipmentStatus);				//修改子表关联数据的日期,保持和主表一致
        }
        return list.size();
    }

    @Override
    public List<EquipmentStatus> queryEntityList(EquipmentStatusVo equipmentStatusVo) {
        List<Object> objectList = new ArrayList<Object>();

        StringBuffer hql = new StringBuffer("from EquipmentStatus where 1 = 1 ");
        if(StringUtils.isNotBlank(equipmentStatusVo.getTtId())){
            objectList.add(equipmentStatusVo.getTtId());
            hql.append(" and ttId = ? ");
        }
        if(equipmentStatusVo.getDutyDateStart() != null){		//日期Start
            objectList.add(equipmentStatusVo.getDutyDateStart());
            hql.append(" and dutyDate >= ? ");
        }
        if(equipmentStatusVo.getDutyDateEnd() != null){		//日期End
            objectList.add(equipmentStatusVo.getDutyDateEnd());
            hql.append(" and dutyDate <= ? ");
        }
        //排序, 根据日期倒序排序
        hql.append(" order by dutyDate desc ");

        List<EquipmentStatus> trList = this.equipmentStatusDaoImpl.queryEntityHQLList(hql.toString(), objectList, EquipmentStatus.class);
        return trList;
    }

    @Override
    public HSSFWorkbook export(EquipmentStatusVo equipmentStatusVo) {

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

        List<EquipmentStatus> esList = queryEntityList(equipmentStatusVo);

        //创建sheet
        HSSFSheet sheet = wb.createSheet("联网设备日常检查汇总");

        //设置列宽
        for (int i = 0; i < 10; i++) {
            sheet.setColumnWidth(i, sheet.getColumnWidth(i)*2);
        }


        //合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 8));

        sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(2, 3, 9, 9));


        //创建行（第一行）
        HSSFRow row0 = sheet.createRow(0 );
        //设置行的高度
        row0.setHeightInPoints(30);
        //创建单元格 并 设置单元格内容
        row0.createCell(0).setCellValue("联网关键设备运行状态日常检查表");
        //设置单元格样式
        row0.getCell(0).setCellStyle(r0_style);

        //第二行
        HSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("表单编号：HLZXRBB-15");
        row1.getCell(0).setCellStyle(r1_style);

        //第三行
        HSSFRow row2 = sheet.createRow(2);
        row2.setHeightInPoints(25);
        row2.createCell(0).setCellValue("日期");
        row2.createCell(1);
        row2.createCell(2).setCellValue("RFID");
        row2.createCell(3);
        row2.createCell(4).setCellValue("5.8G");
        row2.createCell(5).setCellValue("高清卡口");
        for (int i = 6; i < 9; i++) {
            row2.createCell(i);
        }
        row2.createCell(9).setCellValue("备注");
        for (int i = 0; i < 10; i++) {
            row2.getCell(i).setCellStyle(r2_style);	//设置单元格样式
        }

        //第四行
        HSSFRow row3 = sheet.createRow(3);
        row3.setHeightInPoints(25);
        row3.createCell(0);
        row3.createCell(1);
        row3.createCell(2).setCellValue("R1");
        row3.createCell(3).setCellValue("R2");
        row3.createCell(4).setCellValue("E1");
        row3.createCell(5).setCellValue("10306");
        row3.createCell(6).setCellValue("10307");
        row3.createCell(7).setCellValue("10308");
        row3.createCell(8).setCellValue("10309");
        row3.createCell(9);
        for (int i = 0; i < 10; i++) {
            row3.getCell(i).setCellStyle(r2_style);	//设置单元格样式
        }


        //数据展示
        if(esList != null && esList.size() > 0){

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

            for(int tb = 0; tb < esList.size(); tb++){

                //合并单元格  CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
                sheet.addMergedRegion(new CellRangeAddress(4 + tb*3,  6 + tb*3, 0, 0));


                HSSFRow row4 = sheet.createRow(4 + tb*3);
                row4.setHeightInPoints(25);
                row4.createCell(0).setCellValue(sdf1.format(esList.get(tb).getDutyDate()));
                row4.createCell(1);
                row4.createCell(2).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusR1().toString()));
                row4.createCell(3).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusR2().toString()));
                row4.createCell(4).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusE1().toString()));
                row4.createCell(5).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusA().toString()));
                row4.createCell(6).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusB().toString()));
                row4.createCell(7).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusC().toString()));
                row4.createCell(8).setCellValue(totalTableServiceImpl.getValueByDictAndKey("dc_eqStatus", esList.get(tb).getEqStatusD().toString()));
                row4.createCell(9).setCellValue(sdf2.format(esList.get(tb).getCheckTime()));
                for (int i = 0; i < 10; i++) {
                    row4.getCell(i).setCellStyle(mainStyle_center);	//设置单元格样式
                }


                HSSFRow row5 = sheet.createRow(5 + tb*3);
                row5.setHeightInPoints(25);
                row5.createCell(0);
                row5.createCell(1).setCellValue("标识成功率");
                row5.createCell(2).setCellValue(esList.get(tb).getSuccessRateR1() + "%");
                row5.createCell(3).setCellValue(esList.get(tb).getSuccessRateR2() + "%");
                row5.createCell(4).setCellValue(esList.get(tb).getSuccessRateE1() + "%");
                row5.createCell(5).setCellValue(esList.get(tb).getSuccessRateA() + "%");
                row5.createCell(6).setCellValue(esList.get(tb).getSuccessRateB() + "%");
                row5.createCell(7).setCellValue(esList.get(tb).getSuccessRateC() + "%");
                row5.createCell(8).setCellValue(esList.get(tb).getSuccessRateD() + "%");
                row5.createCell(9).setCellValue(esList.get(tb).getRemark());
                for (int i = 0; i < 10; i++) {
                    row5.getCell(i).setCellStyle(mainStyle_center);	//设置单元格样式
                }


                HSSFRow row6 = sheet.createRow(6 + tb*3);
                row6.setHeightInPoints(25);
                row6.createCell(0);
                row6.createCell(1).setCellValue("误标数量 ");
                row6.createCell(2).setCellValue(esList.get(tb).getMislabelNumR1());
                row6.createCell(3).setCellValue(esList.get(tb).getMislabelNumR2() + "%");
                row6.createCell(4).setCellValue(esList.get(tb).getMislabelNumE1() + "%");
                row6.createCell(5).setCellValue(esList.get(tb).getMislabelNumA() + "%");
                row6.createCell(6).setCellValue(esList.get(tb).getMislabelNumB() + "%");
                row6.createCell(7).setCellValue(esList.get(tb).getMislabelNumC() + "%");
                row6.createCell(8).setCellValue(esList.get(tb).getMislabelNumD() + "%");
                row6.createCell(9);
                for (int i = 0; i < 10; i++) {
                    row6.getCell(i).setCellStyle(mainStyle_center);	//设置单元格样式
                }

            }

        }

        return wb;
        

    }
}
