package com.common.utils.helper;

import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过注解javax.persistence.Table获取数据库表的具体信息
 * java hibernate 根据 Table 注解 获取 数据库 表名 字段名 工具类
 * 需要 注解方式为 javax.persistence.Table的注解
 * @author Mr.Wang
 *
 */
public class HibernateToolsUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(HibernateToolsUtil.class);  
	
	/**
     * 获得表名
     * @param clazz 映射到数据库的po类
     * @return String
     */
    public static String getTableName(Class<?> clazz) {
        Table annotation = (Table)clazz.getAnnotation(Table.class);
        if(annotation != null){
            return annotation.name();
        }
        return null;
    }
 
    /**
     * 获得列名
     * 
     * @param clazz 映射到数据库的po类
     * @param icol 第几列
     * @return String
     */
    public static String getColumnName(Class<?> clazz, String name) {
        try {
            new Exception("---貌似java属性如果用注解形式的话 属性和 数据库字段名一致 因为项目没有需要用 写出没有进过有效验证 所以暂未放出 如果谁想用或者有着方面的需求 可以给我留评论");
        }catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }
        return "";
    }
}
