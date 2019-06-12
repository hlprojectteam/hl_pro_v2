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
	


}
