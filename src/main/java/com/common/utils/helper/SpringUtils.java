package com.common.utils.helper;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * @intruduction Spring工具类
 * @author Mr.Wang
 * @Date 2016年2月2日下午3:07:36
 */
public class SpringUtils {
	
	/**
	 * @intruduction 获得spring容器中的实例
	 * @param beanName
	 * @return
	 * @author Mr.Wang
	 * @Date 2016年7月1日下午2:30:19
	 */
	public static Object getBean(String beanName) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		return wac.getBean(beanName);
	}
}
