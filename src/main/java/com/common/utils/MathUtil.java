package com.common.utils;

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


}
