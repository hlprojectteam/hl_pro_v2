package com.common.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathUtil {
	
	/**
	 * 判断字符是不是整形
	 * @方法：@param string
	 * @方法：@return
	 * @描述：
	 * @return
	 * @author: qinyongqian
	 * @date:2019年4月30日
	 */
	public static boolean isInt(String string) {
		if (string == null)
			return false;

		String regEx1 = "[\\-|\\+]?\\d+";
		Pattern p;
		Matcher m;
		p = Pattern.compile(regEx1);
		m = p.matcher(string);
		if (m.matches())
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @方法：@return
	 * @描述：随机生成2位数
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月19日
	 */
	public static String randomTwoNumber() {
		String num="";
		Random random = new Random();
		int ends = random.nextInt(99);
		if(ends<10){
			num="0"+ends;
		}else{
			num=ends+"";
		}
		return num;
	}
	
	
	/**
	* 生成[1, max]之间的随机数
	*/
	public static Integer getRandomNumber(Integer max) {
	    Random rd = new Random();
	    return rd.nextInt(max)+1;
	}
	
	public static int stringToInt(String str) {
		int num=0;
		try {
			if(str.indexOf(".")>0){
				//double型
				double d= Double.parseDouble(str);
				num=new Double(d).intValue();
			}else{
				num=Integer.parseInt(str);
			}
		} catch (Exception e) {
			return 0;
		}
		return num;
	}
	
	public static float getCellHeight(int cellWithPX,String cellContent,float defaultHeight) {
//		System.out.println(cellWithPX+"-"+cellContentLength+"-"+defaultHeight);
		float cellHeight=0;
		try {
			
			if(cellContent.length()>0){
				if(cellContent.indexOf("\n")>0){
					//存在换行符，按换行符计算高度
					int rwsTemp = cellContent.split("\n").length; //得出几行
					if(rwsTemp>1){
						//不只一行，行数*每行高
						cellHeight=rwsTemp*defaultHeight*8/10;
					}else{
						cellHeight=defaultHeight;
					}
				}else{
					//不存在换行符，按字符长度换算高度
					double a= cellWithPX/256;//像素除以256
					double b= cellContent.getBytes().length;//字节长度
					if(b>a){
						cellHeight=(float)(b/a)*defaultHeight;//取得实际高
					}else{
						cellHeight=defaultHeight;
					}
				}
			}else{
				//内容为空，取defaultHeight
				cellHeight=defaultHeight;
			}
		} catch (Exception e) {
			return defaultHeight;
		}
		return cellHeight;
	}
	


}
