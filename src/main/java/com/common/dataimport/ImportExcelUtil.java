package com.common.dataimport;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 
 * @Description
 * @author qinyongqian
 * @date 2018年4月10日
 * 
 */
public class ImportExcelUtil {
	public static Logger logger_excel = Logger.getLogger("EXCEL");// 记录导入日志

	private final static String excel2003L = ".xls"; // 2003- 版本的excel
	private final static String excel2007U = ".xlsx"; // 2007+ 版本的excel

	/**
	 * 描述：获取IO流中的数据，组装成List<List<Object>>对象
	 * 
	 * @param in
	 *            ,fileName
	 * @return
	 * @throws IOException
	 */
	public List<List<Object>> getBankListByExcel(InputStream in, String fileName, int cellCount) throws Exception {
		List<List<Object>> list = null;

		// 创建Excel工作薄
		Workbook work = this.getWorkbook(in, fileName);
		if (null == work) {
			logger_excel.error("创建的Excel工作薄为空！");
		}
		Sheet sheet = null;
		Row row = null;
		Cell cell = null;

		list = new ArrayList<List<Object>>();
		// 遍历Excel中所有的sheet
		for (int i = 0; i < work.getNumberOfSheets(); i++) {
			sheet = work.getSheetAt(i);
			if (sheet == null) {
				continue;
			}

			// 遍历当前sheet中的所有行
			for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum() + 1; j++) {
				row = sheet.getRow(j);
				if (row == null || row.getFirstCellNum() == j) {
					continue;
				}

				// 遍历所有的列
				List<Object> li = new ArrayList<Object>();
				for (int y = row.getFirstCellNum(); y < cellCount + 1; y++) { // row.getLastCellNum()+1
					cell = row.getCell(y);
					li.add(this.getCellValue(cell));
				}
				list.add(li);
			}
		}
		work.close();
		return list;
	}

	/**
	 * 描述：根据文件后缀，自适应上传文件的版本
	 * 
	 * @param inStr
	 *            ,fileName
	 * @return
	 * @throws Exception
	 */
	public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
		Workbook wb = null;
		String fileType = fileName.substring(fileName.lastIndexOf("."));
		if (excel2003L.equals(fileType)) {
			wb = new HSSFWorkbook(inStr); // 2003-
		} else if (excel2007U.equals(fileType)) {
			logger_excel.error("xlsx格式暂不支持！");
			// wb = new XSSFWorkbook(inStr); ; //2007+
		} else {
			logger_excel.error("解析的文件格式有误！");
		}
		return wb;
	}

	/**
	 * 描述：对表格中数值进行格式化
	 * 
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Object getCellValue(Cell cell) {
		Object value = null;
		DecimalFormat df = new DecimalFormat("0.00"); // 格式化number String字符
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); // 日期格式化
		DecimalFormat df2 = new DecimalFormat("0"); // 格式化数字

		if (cell == null)
			return null;

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC: // 10-五月-2015
			if ("General".equals(cell.getCellStyle().getDataFormatString())) {
				//excle表格常规格式
				value = df.format(cell.getNumericCellValue());
			} else if((cell.getCellStyle().getDataFormatString().contains("0.00_"))){
				//excle表格数值格式
				value = df.format(cell.getNumericCellValue());
			}else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
				value = sdf.format(cell.getDateCellValue());
			}else {
				if (HSSFDateUtil.isCellDateFormatted(cell)) { // 判断是否属于时间格式（防止内容类型错乱）
					value = cell.getStringCellValue();
				} else {
					value = df2.format(cell.getNumericCellValue());
				}
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;
		default:
			break;
		}
		return value;
	}
}
