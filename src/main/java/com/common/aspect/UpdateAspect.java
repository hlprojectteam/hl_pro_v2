package com.common.aspect;

import com.common.utils.Common;
import com.urms.user.module.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 */

@Aspect    //该标签把UpdateAspect类声明为一个切面
@Order(3)  //设置切面的优先级：如果有多个切面，可通过设置优先级控制切面的执行顺序（数值越小，优先级越高）
@Component //该标签把LoggerAspect类放到IOC容器中
public class UpdateAspect {
	
	public static final Logger logger = LoggerFactory.getLogger(UpdateAspect.class);
	
    /**
     * 查询实体前拦截
     */
    @Pointcut("execution(* com.common.base.dao.impl.BaseDaoImpl.update(..))")
    public void declearUpdatePointExpression() {}
    
    /**  
     * 前置通知 用于拦截
     * @param joinPoint 切点  
     */    
    @Before("declearUpdatePointExpression()")
    public void afterUpdateMethod(JoinPoint joinPoint){
    	//在获得request的上下文才使用反射
        if(RequestContextHolder.getRequestAttributes() != null && ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest() != null){

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //读取session中的用户
            User user = (User)request.getSession().getAttribute("user");
            List<Object> args = Arrays.asList(joinPoint.getArgs());//获得输入参数对象
            Object obj = args.get(0);
            try {
                Class<?> clazz = obj.getClass();
                Method[] methods = clazz.getMethods();
                for(int i = 0; i < methods.length; i++){  // 遍历所有方法
                    Method method = methods[i];
                    if(method.getName().equals("setSysCode")){//写入当前子系统编码
                        PropertyDescriptor pd = new PropertyDescriptor("sysCode", clazz);
                        if(pd.getReadMethod().invoke(obj)==null||pd.getReadMethod().invoke(obj)=="")//没有syscode的时候才自动补上
                            pd.getWriteMethod().invoke(obj, Common.SYSCODE);
                    }
                    if(method.getName().equals("setUpdateId")){//更新人id
                        PropertyDescriptor pd = new PropertyDescriptor("updateId", clazz);
                        pd.getWriteMethod().invoke(obj, user.getId());
                    }
                    if(method.getName().equals("setUpdateName")){//更新人
                        PropertyDescriptor pd = new PropertyDescriptor("updateName", clazz);
                        pd.getWriteMethod().invoke(obj, user.getUserName());
                    }
                    if(method.getName().equals("setUpdateTime")){//更新时间
                        PropertyDescriptor pd = new PropertyDescriptor("updateTime", clazz);
                        pd.getWriteMethod().invoke(obj, new Date());
                    }
                }
            } catch (Exception e) {
                //logger.error(this.getClass(),e);
            }
        }
    }
	
}
