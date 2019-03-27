package com.common.utils.cache;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.urms.dataDictionary.service.IDataDictionaryService;
import com.urms.subsystem.service.ISubsystemService;
import com.urms.sysConfig.service.ISysConfigService;

public class CacheListener extends ContextLoaderListener  {

	private static final Logger logger = LoggerFactory.getLogger(CacheListener.class);
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);  
		ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());  
		logger.info("加载数据字典...");
		IDataDictionaryService dataDictionaryServiceImpl = (IDataDictionaryService) applicationContext.getBean("dataDictionaryServiceImpl");
		dataDictionaryServiceImpl.getDictByCode();
//		logger.info("加载行政区划js文件...");
//		dataDictionaryServiceImpl.getAreaCodeAndName();
		logger.info("加载系统配置...");
		ISysConfigService sysConfigServiceImpl = (ISysConfigService) applicationContext.getBean("sysConfigServiceImpl");
		sysConfigServiceImpl.getSysConfig();
		logger.info("加载子系统...");
		ISubsystemService subsystemServiceImpl = (ISubsystemService) applicationContext.getBean("subsystemServiceImpl");
		subsystemServiceImpl.getSubsystem();
//		logger.info("加载工作日...");
//		IWorkDayConfigService WorkDayServiceImpl = (IWorkDayConfigService) applicationContext.getBean("workDayConfigServiceImpl");
//		WorkDayServiceImpl.getWorkDayConfig();
//		logger.info("加载API配置信息...");
//		IApiConfigService apiConfigServiceImpl = (IApiConfigService)applicationContext.getBean("apiConfigServiceImpl");
//		apiConfigServiceImpl.getApiConfig();
		
		
	}
}

