package com.common.poi;

import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.answer.questions.module.ExamPerson;

/**
 * 
 * @Description 导出EXCEL
 * @author qinyongqian
 * @date 2016-12-30
 *
 */
public class ExportExcel {

	public void exportExcel(String title, String[] headers,
			List<ExamPerson> list, OutputStream out) {
		
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet(title);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
		
        // 产生表格标题行
 		for (short i = 0; i < headers.length; i++) {
 			HSSFCell cell = row.createCell(i);
 			cell.setCellStyle(style);
 			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
 			cell.setCellValue(text);
 		}
 		
 		if(list.size()>0){
 			for (int i = 0; i < list.size(); i++)  
 	        {  
 	            row = sheet.createRow((int) i + 1);  
 	            ExamPerson examPerson = (ExamPerson) list.get(i);  
 	            row.createCell((short) 0).setCellValue(examPerson.getPersonName());  
 	            row.createCell((short) 1).setCellValue(examPerson.getOrgFrameName());  
 	            if(examPerson.getIsExam()==0){
 	            	 row.createCell((short) 2).setCellValue("否");  
 	            }else{
 	            	 row.createCell((short) 2).setCellValue("是");  
 	            }
 	            if(examPerson.getConsumeTime()==null){
 	               row.createCell((short) 3).setCellValue(0.0);  
 	            }else{
 	               row.createCell((short) 3).setCellValue(secToTime(examPerson.getConsumeTime()));  
 	            }
 	           if(examPerson.getTotalSource()==null){
 	               row.createCell((short) 4).setCellValue(0);  
 	            }else{
 	               row.createCell((short) 4).setCellValue(examPerson.getTotalSource());  
 	            }
 	         
 	        }  
 	        // 第六步，将文件存到指定位置  
 	        try  
 	        {  
 	            wb.write(out);  
 	            out.close();  
 	        }  
 	        catch (Exception e)  
 	        {  
 	            e.printStackTrace();  
 	        }  
 		}
	}
	
	public  String secToTime(int time) {  
        String timeStr = null;  
        int hour = 0;  
        int minute = 0;  
        int second = 0;  
        if (time <= 0)  
            return "00:00";  
        else {  
            minute = time / 60;  
            if (minute < 60) {  
                second = time % 60;  
                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
            } else {  
                hour = minute / 60;  
                if (hour > 99)  
                    return "99:59:59";  
                minute = minute % 60;  
                second = time - hour * 3600 - minute * 60;  
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
            }  
        }  
        return timeStr;  
    }  
	 public  String unitFormat(int i) {  
        String retStr = null;  
        if (i >= 0 && i < 10)  
            retStr = "0" + Integer.toString(i);  
        else  
            retStr = "" + i;  
        return retStr;  
    }  
}
