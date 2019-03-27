package com.common.utils.helper;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * MD5单向加密算法
 * @intruduction 
 * @author Mr.Wang
 * @Date 2016年8月8日下午4:20:18
 */
public class MD5Utils {
	
	private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);  
	
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            return null;
        }
    }
	
	//测试
    public static void main(String[] args) {
//        System.out.println(MD5Utils.MD5("20121221"));
//        System.out.println(MD5Utils.MD5("加密"));
    }
}
