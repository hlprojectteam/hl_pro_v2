package com.common.utils.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.urms.dataDictionary.module.CategoryAttribute;

/**
 * @intruduction 缓存
 * @author Mr.Wang
 * @Date 2016年7月1日下午2:28:48
 */
public class Cache {

	/**
	 * 所有数据字典带默认值
	 */
	public static Map<String,Set<CategoryAttribute>> getDictByCode = new HashMap<String, Set<CategoryAttribute>>();
	
	/**
	 * 所有数据字典Map方便直接找到字典数据 进行翻译
	 */
	public static Map<String,Map<String,String>> getDictByCodeMap = new HashMap<String, Map<String,String>>();
	
	/**
	 * 字典目录对翻译
	 */
	public static Map<String,String> getDict = new HashMap<String, String>();
	
	/**
	 * 子系统列表
	 */
	public static Map<String,String> getSubsystem = new HashMap<String, String>();
	
	/**
	 * 系统配置
	 */
	public static Map<String,String> getSysConfig = new HashMap<String, String>();
	
	/**
	 * 工作日配置
	 */
	public static Map<Integer,String> getWorkDayConfig = new HashMap<Integer, String>();
	
	/**
	 * APIConfig 接口配置
	 * 1-IP,2-SMS,3-MAP,4-Weather,5-Video
	 */
	public static Map<Integer,Object> getApiConfig = new HashMap<Integer, Object>(); 
	
}
