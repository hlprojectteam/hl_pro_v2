package com.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 
 * @author Mr.Wang
 * @Date 2017年4月10日上午9:17:55
 */

@Aspect    //该标签把SaveAspect类声明为一个切面
@Order(2)  //设置切面的优先级：如果有多个切面，可通过设置优先级控制切面的执行顺序（数值越小，优先级越高）
@Component //该标签把LoggerAspect类放到IOC容器中
public class QueryAspect {
	
	public static final Logger logger = LoggerFactory.getLogger(QueryAspect.class);  
	
    /**
     * 查询实体前拦截
     */
    @Pointcut("execution(* com.common.base.dao.impl.BaseDaoImpl.queryEntityList(..))")
    public void declearQueryPointExpression() {}    
    
    /**  
     * 前置通知 用于拦截
     * @param joinPoint 切点  
     */    
    @Before("declearQueryPointExpression()") 
    public void beforQueryMethod(JoinPoint joinPoint){
    	@SuppressWarnings("static-access")
		String sysCode = this.getProperty("sysCode");//子系统编码
		List<Object> args = Arrays.asList(joinPoint.getArgs());//获得输入参数对象
		Object obj = null;
		if(args.size()==5)
			obj = args.get(2);
		else
			obj = args.get(0);
		try{
			Class<?> clazz = obj.getClass();
			Method[] methods = clazz.getMethods();
			for(int i = 0; i < methods.length; i++){  // 遍历所有方法
				Method method = methods[i];
				if(method.getName().equals("add")&&method.getGenericReturnType().toString().equals("boolean")){
					if(!sysCode.equals("tr_city") && !sysCode.equals("tr_outside"))//不是市平台
						method.invoke(obj, Restrictions.eq("sysCode", sysCode));//默认只能查看本系统
				}
			}
		} catch (Exception e) {
			//logger.error(this.getClass(),e);
		}
    	//在获得request的上下文才使用反射
//    	if(RequestContextHolder.getRequestAttributes()!=null){
//    		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//    		//读取session中的用户
//    		User user = (User)request.getSession().getAttribute("user");
//    		if(user!=null){
//    			List<Object> args = Arrays.asList(joinPoint.getArgs());//获得输入参数对象
//    			Object obj = null;
//    			if(args.size()==5)
//    				obj = args.get(2);
//    			else
//    				obj = args.get(0);
//    			try{
//    				Class<?> clazz = obj.getClass();
//    				Method[] methods = clazz.getMethods();
//    				for(int i = 0; i < methods.length; i++){  // 遍历所有方法
//    					Method method = methods[i];
//    					if(method.getName().equals("add")&&method.getGenericReturnType().toString().equals("boolean")){
//    						if(!sysCode.equals("tr_city"))//不是市平台账号
//    							method.invoke(obj, Restrictions.eq("sysCode", user.getSysCode()));//默认只能查看本系统
//    					}
//    				}
//    			} catch (Exception e) {
//    				//logger.error(this.getClass(),e);
//    			}
//    		}
//    	}
    }

	//读取配置文件
	public static String getProperty(String key) {
		String str = "";
		try {
			str = new String(ResourceBundle.getBundle("config").getString(key).getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("com.common.utils.Common_读取配置信息", e);
		}
		return str;
	}
}
