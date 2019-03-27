package com.common.utils.helper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @intruduction 反射
 * @author Mr.Wang
 * @Date 2016年8月1日下午4:49:33
 */
public class ReflectedGetSet {
	
	private static final Logger logger = LoggerFactory.getLogger(ReflectedGetSet.class);  
	
	public static Object getMethod(Object obj, String filed) {  
		Object o = null;
	    try {  
	        Class<?> clazz = obj.getClass();  
	        PropertyDescriptor pd = new PropertyDescriptor(filed, clazz);  
	        Method rm = pd.getReadMethod();//获得get方法  
	        if (pd != null) {  
	            o = rm.invoke(obj);//执行get方法返回一个Object  
	        }  
	    } catch (Exception e) {  
	    	logger.error(e.getMessage(), e);
	    }  
	    return o;
	}  
	
	public static Object setMethod(Object obj, String filed ,Object objVal) {  
	    try {  
	        Class<?> clazz = obj.getClass();  
	        PropertyDescriptor pd = new PropertyDescriptor(filed, clazz);  
	        Method wm = pd.getWriteMethod();
	        if (pd != null) {  
	        	wm.invoke(obj, objVal);
	        }  
	    } catch (Exception e) {  
	    	logger.error(e.getMessage(), e);
	    }  
	    return obj;
	} 

}
