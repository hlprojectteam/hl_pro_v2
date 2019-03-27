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
import java.util.List;

/**
 * 保存前拦截
 * @author Mr.Wang
 * @Date 2017年4月10日上午9:17:55
 */

@Aspect    //该标签把SaveAspect类声明为一个切面
@Order(1)  //设置切面的优先级：如果有多个切面，可通过设置优先级控制切面的执行顺序（数值越小，优先级越高）
@Component //该标签把LoggerAspect类放到IOC容器中
public class SaveAspect {
	
	public static final Logger logger = LoggerFactory.getLogger(SaveAspect.class);  
	
	/**
     * 定义一个方法，用于声明切入点表达式，方法中一般不需要添加其他代码
     * 使用@Pointcut声明切入点表达式
     * 后面的通知直接使用方法名来引用当前的切点表达式；如果是其他类使用，加上包名即可
     */
    @Pointcut("execution(* com.common.base.dao.impl.BaseDaoImpl.save(..))")
    public void declearSavePointExpression(){}
    
    /**
     * 保存实体前拦截  写入当前子系统
     * @param joinPoint
     */
    @Before("declearSavePointExpression()") //该标签声明次方法是一个前置通知：在目标方法开始之前执行
    public void beforSaveMethod(JoinPoint joinPoint){
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
                        if(method.getName().equals("setSysCode")){//写入当前子系统名称
                            PropertyDescriptor pd = new PropertyDescriptor("sysCode", clazz);
                            if(pd.getReadMethod().invoke(obj)==null||pd.getReadMethod().invoke(obj)=="") {//没有syscode的时候才自动补上
                                pd.getWriteMethod().invoke(obj, Common.SYSCODE);
                            }
                        }
                        if(method.getName().equals("setCreatorId")){//创建人
                            PropertyDescriptor pd = new PropertyDescriptor("creatorId", clazz);
                            pd.getWriteMethod().invoke(obj, user.getId());
                        }
                        if(method.getName().equals("setCreatorName")){//创建人名称
                            PropertyDescriptor pd = new PropertyDescriptor("creatorName", clazz);
                            pd.getWriteMethod().invoke(obj, user.getUserName());
                        }
                    }
            } catch (Exception e) {
                //logger.error(this.getClass(),e);
            }
        }
    }
	
}
