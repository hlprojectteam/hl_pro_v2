package com.common.utils.JobTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.common.message.service.IMessageReadedService;
import com.common.utils.helper.SpringUtils;

public class JobTimeLimitTask {
	
	/*
	 * 初始化时限警告提醒调度
	 */
	private static SchedulerFactory sf = new StdSchedulerFactory();

	public static Logger logger = Logger.getLogger(JobTimeLimitTask.class);

	public static void doTimeLimitTask() {
		try {
			//shutdownJobs();// 停止所有任务 然后重新加载任务
			exeCleanReadMsg();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @方法：
	 * @描述：定期对过长的消息删减，长度长于50条消息ID的，删减到50
	 * @return
	 * @author: qinyongqian
	 * @date:2019年6月24日
	 */
	public static void exeCleanReadMsg() {
//		System.out.println("--"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"-开始清除已经读消息---");
		logger.info("--"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"-开始清除已经读消息---");
		try {
			IMessageReadedService messageReadedServiceImpl = (IMessageReadedService) SpringUtils.getBean("messageReadedServiceImpl");
			messageReadedServiceImpl.updateCleanMessageReaded();
		} catch (Exception e) {
			logger.info("--"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"--清除存在异常："+e.getMessage());
		}
		logger.info("--"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"-结束清除已经读消息---");
	}
	
	/**
	 * 停止所有任务
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = sf.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
