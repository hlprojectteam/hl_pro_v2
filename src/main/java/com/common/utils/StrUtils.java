package com.common.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @Description 字符工具类
 * @author qinyongqian
 * @date 2019年5月11日
 *
 */
public class StrUtils {
	
	/**
	 * 
	 * @方法：@param str 原字符
	 * @方法：@param len 截取的长度
	 * @方法：@return 截取后的字符
	 * @描述：判断字符长度后，截取一定长度的字符
	 * @return
	 * @author: qinyongqian
	 * @date:2019年5月11日
	 */
	public static String subString(String str,int len) {
		if (StringUtils.isBlank(str))
			return "";

		if(str.length()>len){
			return str.substring(0, len);
		}else{
			return str;
		}
	}


}
