package com.common.datasource;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

/**
 * 配置AOP切面类
 * @author Mr.Wang
 * @Date 2016年1月29日上午9:45:49
 */
public class DataSourceAdvice implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(DataSourceAdvice.class);  
    
	// service方法执行之前被调用
    public void before(Method method, Object[] args, Object target) {
    	logger.info("切入点: " + target.getClass().getName() + "类中" + method.getName() + "方法");
        if(method.getName().startsWith("add") 
            || method.getName().startsWith("create")
            || method.getName().startsWith("save")
            || method.getName().startsWith("edit")
            || method.getName().startsWith("update")
            || method.getName().startsWith("delete")
            || method.getName().startsWith("remove")){
        	logger.info("切换到: master");
            DataSourceSwitcher.setMaster();
        }else{
        	logger.info("切换到: slave");
            DataSourceSwitcher.setSlave();
        }
    }

    // service方法执行完之后被调用
    public void afterReturning(Object arg0, Method method, Object[] args, Object target) {
    }

    // 抛出Exception之后被调用
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) {
        DataSourceSwitcher.setSlave();
        logger.error("出现异常,切换到: slave");
    }

}