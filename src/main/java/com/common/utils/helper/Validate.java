package com.common.utils.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {
	/**
	 * 手机号验证 
	 * @intruduction
	 * @param mobile
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年11月1日下午2:34:12
	 */
    public static boolean isMobile(String mobile) {   
        boolean flag = false;   
        Pattern p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(mobile);  
        flag = m.matches();   
        return flag;  
    }  
    
    /**
     * 电子邮箱验证
     * @intruduction
     * @param email
     * @return
     * @author Mr.Wang
     * @Date 2016年11月1日下午2:39:47
     */
    public static boolean isEmail(String email){
        boolean flag = false;
        Pattern regex = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher matcher = regex.matcher(email);
        flag = matcher.matches();
        return flag;
    }
}
