package com.common.utils.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.utils.cache.Cache;

/**
 * @类： <code>EmailSender.java</code>
 * @描述：日期工具类
 * @版权所有：
 */
@SuppressWarnings("deprecation")
public class DateUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);  
	
	public static final String JAVA_DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
	public static final String JAVA_DATE_FORMAT_YMDHM = "yyyy-MM-dd HH:mm";
	public static final String JAVA_DATE_FORMAT_YMD = "yyyy-MM-dd";
	public static final String JAVA_DATE_FORMAT_YM = "yyyy-MM";
	public static final String JAVA_DATE_FORMAT_HM = "HH:mm";
	public static final String JAVA_DATE_FORMAT_HMS = "HH:mm:ss";
	public static final String JAVA_DATE_FORMAT_YMDW = "yyyy-MM-dd EEEE";
	public static final String JAVA_DATE_FORMAT_W = "EEEE";
	public static final String JAVA_DATE_FORMAT_CH_YMDHMS = "yyyy年MM月dd日 HH:mm:ss";
	public static final String JAVA_DATE_FORMAT_CH_YMDHM = "yyyy年MM月dd日 HH:mm";
	public static final String JAVA_DATE_FORMAT_CH_YMD = "yyyy年MM月dd日";
	public static final String JAVA_DATE_FORMAT_CH_YM = "yyyy年MM月";
	public static final String JAVA_DATE_FORMAT_CH_MD = "MM月dd日";
	public static final String JAVA_DATE_FORMAT_CH_YMDW = "yyyy年MM月dd日EEEE";

	/**
	 * @方法：getWeekDayDesc
	 * @描述：根据指定日期获取对应的星期几
	 * @param date
	 *            指定日期
	 */
	public static String getWeekDayDesc(java.util.Date date) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		int wd = rightNow.get(7);
		if (wd == 2)
			return "星期一";
		if (wd == 3)
			return "星期二";
		if (wd == 4)
			return "星期三";
		if (wd == 5)
			return "星期四";
		if (wd == 6)
			return "星期五";
		if (wd == 7)
			return "星期六";
		if (wd == 1) {
			return "星期日";
		}
		return "星期";
	}

	/**
	 * @方法：getFormatDate
	 * @描述：将字符串类型日期转换成数据库DATE类型
	 * @param dateString
	 *            字符串日期
	 * @param fromFormat
	 *            转换前日期格式
	 * @param toFormat
	 *            需要转换成date类型日期格式
	 * @return
	 * @throws Exception
	 */
	public static String getFormatDate(String dateString, String fromFormat, String toFormat) throws Exception {
		String sDateString = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat);
			java.util.Date objDate = dateFormat.parse(dateString);
			dateFormat = new SimpleDateFormat(toFormat);
			sDateString = dateFormat.format(objDate);
		} catch (Exception ex) {
			throw new Exception("日期格式错误：1-" + fromFormat + ";2-" + toFormat);
		}

		return sDateString;
	}

	/**
	 * @方法：format
	 * @描述：将字符类型的日期转成date类型的日期
	 * @param dateString
	 *            字符串类型日期
	 * @param format
	 *            字符串类型日期格式 "yyyy-MM-dd HH:mm:ss"是正确格式，其它都出错
	 * @return
	 * @throws Exception
	 */
	public static Date format(String dateString, String format)	throws Exception {
		Date result = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			result = dateFormat.parse(dateString);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception("日期格式错误：" + format);
		}

		return result;
	}
	
	public static String getDateFormatString(Date date,String formate)
	{	
		if(date==null)
			return "";
		SimpleDateFormat sf = new SimpleDateFormat(formate);
		String result = sf.format(date);
		return result;
	}

	/**
	 * 获取Date格式时间
	 * @param strDate
	 * @return
	 */
	public static Date getDateFromString(String strDate) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(strDate); // Thu Jan 18 00:00:00 CST 2007
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		return date;
	}

	private static String padChar(String num) {
		if (num.length() == 2) {
			return num;
		}
		return "0" + num;
	}

	private static String padChar(int num) {
		return padChar(String.valueOf(num));
	}

	private static int fixYear(int year) {
		return year + 1900;
	}

	@SuppressWarnings("unused")
	private static int fixSetYear(int year) {
		return year - 1900;
	}

	/**
	 * @方法：getSybaseDBSmallDateStr
	 * @描述：将date类型日期转换成字符串类型日期格式为（yyyy-MM-dd）
	 * @param date
	 *            日期
	 * @return： 返回格式为yyyy-MM-dd字符串日期
	 */
	public static String getSybaseDBSmallDateStr(Date date) {
		if (date == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(fixYear(date.getYear()));
		sb.append("-");
		sb.append(padChar(date.getMonth() + 1));
		sb.append("-");
		sb.append(padChar(date.getDate()));

		return sb.toString();
	}

	/**
	 * @方法：getSybaseDBTimeStrForSqlQuery
	 * @描述：将date类型日期转换成字符串类型日期格式为（yyyy-MM-dd HH:mm:ss）
	 * @param date
	 *            日期
	 * @return： 返回格式为yyyy-MM-dd HH:mm:ss字符串日期
	 */
	public static String getSybaseDBTimeStrForSqlQuery(Date date) {
		if (date == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(fixYear(date.getYear()));
		sb.append("-");
		sb.append(padChar(date.getMonth() + 1));
		sb.append("-");
		sb.append(padChar(date.getDate()));

		sb.append(" ");
		sb.append(padChar(date.getHours()));
		sb.append(":");
		sb.append(padChar(date.getMinutes()));
		sb.append(":");
		sb.append(padChar(date.getSeconds()));

		return sb.toString();
	}

	/**
	 * @方法：getSybaseDBDateStrForSqlQuery
	 * @描述：将date类型日期转换成字符串类型日期格式为（yyyy-MM-dd HH:mm）
	 * @param date
	 *            日期
	 * @return： 返回格式为yyyy-MM-dd HH:mm字符串日期
	 */
	public static String getSybaseDBDateStrForSqlQuery(Date date) {
		if (date == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(fixYear(date.getYear()));
		sb.append("-");
		sb.append(padChar(date.getMonth() + 1));
		sb.append("-");
		sb.append(padChar(date.getDate()));

		sb.append(" ");
		sb.append(padChar(date.getHours()));
		sb.append(":");
		sb.append(padChar(date.getMinutes()));
		return sb.toString();
	}

	public static String getSybaseDBDateStrForSqlQueryBefore(Date date) {
		if (date == null) {
			return "";
		}
		date.setHours(23);
		date.setMinutes(59);

		StringBuffer sb = new StringBuffer();
		sb.append(fixYear(date.getYear()));
		sb.append("-");
		sb.append(padChar(date.getMonth() + 1));
		sb.append("-");
		sb.append(padChar(date.getDate()));

		sb.append(" ");
		sb.append(padChar(date.getHours()));
		sb.append(":");
		sb.append(padChar(date.getMinutes()));

		return sb.toString();
	}

	
	
	/**
	 * @方法：getFormatHHMM
	 * @描述：将date类型日期转换成字符串类型时间格式为（HH:mm）
	 * @param date
	 *            日期
	 * @return： 返回格式为HH:mm字符时间
	 */
	public static String getFormatHHMM(Date date) {
		if (date == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(padChar(date.getHours()));
		sb.append(":");
		sb.append(padChar(date.getMinutes()));

		return sb.toString();
	}

	/**
	 * @方法：getWebDateStr
	 * @描述：将date类型日期转换成字符串类型日期格式为（yyyy年MM月dd日）
	 * @param date
	 *            日期
	 * @return： 返回格式为yyyy年MM月dd日字符串日期
	 */
	public static String getWebDateStr(Date date) {
		if (date == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(fixYear(date.getYear()));
		sb.append("年");
		sb.append(padChar(date.getMonth() + 1));
		sb.append("月");
		sb.append(padChar(date.getDate()));
		sb.append("日");

		return sb.toString();
	}

	/**
	 * @方法：getWebDateTimeSecondStr
	 * @描述：将date类型日期转换成字符串类型日期格式为（yyyy年MM月dd日 HH:mm:ss）
	 * @param date
	 *            日期
	 * @return： 返回格式为yyyy年MM月dd日 HH:mm:ss字符串日期
	 */
	public static String getWebDateTimeSecondStr(Date date) {
		if (date == null) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		sb.append(fixYear(date.getYear()));
		sb.append("年");
		sb.append(padChar(date.getMonth() + 1));
		sb.append("月");
		sb.append(padChar(date.getDate()));
		sb.append("日");

		sb.append(" ");
		sb.append(padChar(date.getHours()));
		sb.append(":");
		sb.append(padChar(date.getMinutes()));
		sb.append(":");
		sb.append(padChar(date.getSeconds()));

		return sb.toString();
	}

	/**
	 * @方法：getSybaseDBTodayStartEnds
	 * @描述：获取当前一天yyyy-MM-dd 00:00:00 到 yyyy-MM-dd 23:59:59
	 * @return：
	 */
	public static String[] getSybaseDBTodayStartEnds() {
		String[] res = new String[2];

		Calendar cal = Calendar.getInstance();

		cal.set(11, 0);
		cal.set(12, 0);
		cal.set(13, 0);

		res[0] = getSybaseDBTimeStrForSqlQuery(cal.getTime());

		cal.set(11, 23);
		cal.set(12, 59);
		cal.set(13, 59);
		res[1] = getSybaseDBTimeStrForSqlQuery(cal.getTime());
		return res;
	}

	/**
	 * @方法：getSybaseDBWeekStartEnds
	 * @描述：获取当前日期一周内第一天日期和最后一天日期
	 */
	public static String[] getSybaseDBWeekStartEnds() {
		String[] res = new String[2];
		Calendar cal = Calendar.getInstance();
		int dayWeek = cal.get(7);
		if (dayWeek == 1) {
			dayWeek = 8;
		}
		cal.add(5, 2 - dayWeek);
		cal.set(11, 0);
		cal.set(12, 0);
		cal.set(13, 0);

		res[0] = getSybaseDBTimeStrForSqlQuery(cal.getTime());
		cal.add(5, 6);
		cal.set(11, 23);
		cal.set(12, 59);
		cal.set(13, 59);

		res[1] = getSybaseDBTimeStrForSqlQuery(cal.getTime());

		return res;
	}
	
	public static boolean isDateBefore(String date1,String date2,String format){
		  try{		  
		   SimpleDateFormat df = new SimpleDateFormat(format);		 
		   return df.parse(date1).before(df.parse(date2));
		  }catch(ParseException e){			
		   return false;
		  }
		}
	
	public static boolean isDateOn(String date1,String date2,String format){
		  try{		  
			   SimpleDateFormat df = new SimpleDateFormat(format);
			   Date now = new Date();
			   if(now.after(df.parse(date1)) && now.before(df.parse(date2)))
				   return true;
			   else
				   return false;
			  }catch(ParseException e){			
			   return false;
			  }
	}
	
   public static List<Date> getMiddleDate(Date beginDate,Date endDate){
	    if(endDate.after(beginDate)){
	    	List<Date> middleDateList = new ArrayList<Date>();
	    	Calendar start = Calendar.getInstance();
	    	start.setTime(beginDate);	    	
	    	Long startTIme = start.getTimeInMillis();  
	    	Calendar end = Calendar.getInstance();  
	    	end.setTime(endDate);
	    	Long endTime = end.getTimeInMillis();  
	    	Long oneDay = 1000 * 60 * 60 * 24l;  	    	
	    	Long time = startTIme;  
	    	while (time <= endTime) {  
	    		Date d = new Date(time);  
	    		middleDateList.add(d);
	    		time += oneDay;  
	    	}
	    	return middleDateList;
	   }else{
	    	return null;
       }
   }

   /**
	 * @方法：getSybaseDBMonthStartEnds
	 * @描述：获取当前月份第一天日期和最后一天日期
	 */
	public static String[] getSybaseDBMonthStartEnds() {
		String[] res = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar begin = Calendar.getInstance();
        begin.set(Calendar.DAY_OF_MONTH, begin.getActualMinimum(Calendar.DAY_OF_MONTH));
        begin.set(Calendar.SECOND, 0);
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        res[0] = sdf.format(begin.getTime());
        Calendar end = Calendar.getInstance();
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        res[1] = sdf.format(end.getTime());
		return res;
	}

	/**
     * 某一个月第一天和最后一天
     * @param date
     * @return
     */
    public static String[] getFirstday_Lastday_Month(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 0);
        Date theDate = calendar.getTime();
        
        //上个月第一天
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        day_first = str.toString();

        //上个月最后一天
        calendar.add(Calendar.MONTH, 1);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
        day_last = endStr.toString();
        String[] res = new String[2];
        res[0] = day_first;
        res[1] = day_last;
        return res;
    }
    
    /**
     * 根据输入的日期字符串 和 提前天数 ，
     * 获得 指定日期提前几天的日期对象
     * @param dateString 日期对象 ，格式如 2015-3-13
     * @param lazyDays 倒推的天数
     * @return 指定日期倒推指定天数后的日期对象
     * @throws ParseException 
     */
    public static Date getDate(String dateString , int beforeDays) throws ParseException{
    	if(StringUtils.isEmpty(dateString))
    		return null;
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate = dateFormat.parse(dateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR , inputDayOfYear-beforeDays );
        return cal.getTime();
    }
    
    /**
     * 
     * @方法：timeDifference
     * @描述：
     * @param: DateUtil
     * @param dateOne
     * @param dateTwo
     * @param type:day-天数;hour-小时数;minute-分钟数;其他都判断为秒数
     */
	public static long timeDifference(Date dateOne, Date dateTwo, String type){
		if(dateOne==null)
			return 0;
		long different = Math.abs(dateOne.getTime() - dateTwo.getTime());
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        if(type.equals("day")){
        	long elapsedDays = different / daysInMilli;
        	return elapsedDays;
        }else if(type.equals("hour")){
        	long elapsedHours = different / hoursInMilli;
        	return elapsedHours;
        }else if(type.equals("minute")){
        	long elapsedMinutes = different / minutesInMilli;
        	return elapsedMinutes;
        }else{
        	long elapsedSeconds = different / secondsInMilli;
        	return elapsedSeconds;
        }
	}
	/**
	 * 根据串生成一串唯一字符串
	 * @param temp
	 * @return  IP201511211140580001
	 */
	public static String getYYMMDDHHMMSS(String temp){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		if(StringUtils.isNotEmpty(temp)){
			str=temp+str;
		}
		return str;
	}
	
	/**
	 * 
	 * @方法：@param temp
	 * @方法：@return
	 * @描述：根据串生成一串唯一字符串
	 * @return 20151121114058+temp
	 * @author: qinyongqian
	 * @date:2019年5月20日
	 */
	public static String getYYMMDDHHMMSS2(String temp){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String str = sdf.format(date);
		if(StringUtils.isNotEmpty(temp)){
			str=str+temp;
		}
		return str;
	}
	
	//-------------------------------------------------------------------------------------------------------
	/**
	 * @intruduction 两天时间差
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @throws ParseException
	 * @author Mr.Wang
	 * @Date 2016年8月8日上午11:31:55
	 */
	public static String dateDiff(Date startTime, Date endTime) throws ParseException {
		//按照传入的格式生成一个simpledateformate对象
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//一秒钟的毫秒数long diff;try {
		//获得两个时间的毫秒时间差异
		long diff = endTime.getTime() - startTime.getTime();
		long day = diff/nd;//计算差多少天
		long hour = diff%nd/nh;//计算差多少小时
		long min = diff%nd%nh/nm;//计算差多少分钟
		long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
		return day+"天"+hour+"小时"+min+"分钟"+sec+"秒";
	}
	
	/**
	 * 判断当前日期是星期几
	 * @intruduction
	 * @param pTime  修要判断的时间
	 * @return dayForWeek 判断结果
	 * @throws Exception
	 * @author Mr.Wang
	 * @Date 2016年8月6日下午1:44:45
	 */
	 public static int dayForWeek(Date date) throws Exception {
	     Calendar c = Calendar.getInstance();
	     c.setTime(date);
	     int dayForWeek = 0;
	     if(c.get(Calendar.DAY_OF_WEEK) == 1){
	    	 dayForWeek = 7;
	     }else{
	    	 dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
	     }
	     return dayForWeek;
	 }
	
	 
	 /**
		 * 
		 * @intruduction 判断几天后是否工作日
		 * @param now 当前时间
		 * @param afterDay 相加的天数
		 * @return
		 * @throws Exception
		 * @author Mr.Wang
		 * @Date 2016年8月8日上午10:09:25
		 */
		public static boolean isWorkDay(Calendar now,int afterDay) throws Exception{
			String workDayConfig = Cache.getWorkDayConfig.get(now.get(Calendar.YEAR));//获得当前日期年份工作日配置
			boolean chk = false;
			String ssDay = workDayConfig.split("#")[0];//正常放假周6、日
			String workFjDay = workDayConfig.split("#")[1];//放假的工作日
			String[] fjDay = workFjDay.split(",");//工作日但放假
			List<String> fjDayList = new ArrayList<String>();
			for (String string : fjDay) {
				fjDayList.add(string);
			}
			String exceptDay = workDayConfig.split("#")[2];//上班的星期6 日
			String[] gzDay = exceptDay.split(",");//放假但要工作
			List<String> gzDayList = new ArrayList<String>();
			for (String string : gzDay) {
				gzDayList.add(string);
			}
			now.add(Calendar.DATE,afterDay);//+天数
			int dayForWeek = dayForWeek(now.getTime());
			int month = now.get(Calendar.MONTH) + 1;
			int day = now.get(Calendar.DATE);
			String bjDay = month+"-"+day;//比较日期
			if(ssDay.indexOf(dayForWeek+"")>-1){//得到星期六日
				if(gzDayList.contains(bjDay)){
					chk = true;
				}
			}else{//一般工作日
				if(!fjDayList.contains(bjDay)){
					chk = true;
				}
			}
			return chk;
		}
	
		
		/**
		 * @intruduction x天x小时后的 日期，除去工作日
		 * @param calendar 
		 * @param day x日
		 * @param time x小时
		 * @return
		 * @throws Exception
		 * @author Mr.Wang
		 * @Date 2016年8月8日下午2:25:37
		 */
		public static Calendar afterDay(Calendar calendar,int day,int time) throws Exception{
			int k = 0;
			Calendar aftrtDay = null;
			for (int i = 0; i < 30; i++) {//最多支持30天
				if(isWorkDay(calendar,1)){//工作日
					k++;
					if(k==day){
						aftrtDay = calendar;
						break;
					}
				}
			}
			if(time>0){//小时
				SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");
				String afDay = sdfDay.format(aftrtDay.getTime());
				aftrtDay.add(Calendar.HOUR_OF_DAY,time);//+N小时
				if(!sdfDay.format(aftrtDay.getTime()).equals(afDay)){//如果不是同一天，就判断是否工作日
					for (int i = 0; i < 10; i++) {
						if(isWorkDay(aftrtDay,0)){//如果是工作日
							break;
						}
						aftrtDay.add(Calendar.DATE, 1);
					}
				}
			}
			return aftrtDay;
		}
		
		/**
		 * @intruduction 两天时间差除去工作日
		 * @param startTime 开始时间
		 * @param endTime 结束时间
		 * @return
		 * @throws ParseException
		 * @author Mr.Wang
		 * @Date 2016年8月8日上午11:31:55
		 */
		public static String dateDiffExWorkDay(Date startTime, Date endTime) throws Exception {
			long nd = 1000*24*60*60;//一天的毫秒数
			long nh = 1000*60*60;//一小时的毫秒数
			long nm = 1000*60;//一分钟的毫秒数
			long ns = 1000;//一秒钟的毫秒数long diff;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			if(sdf.format(startTime).equals(sdf.format(endTime))){//如果是同一天
				//获得两个时间的毫秒时间差异
				long diff = endTime.getTime() - startTime.getTime();
				long day = diff/nd;//计算差多少天
				long hour = diff%nd/nh;//计算差多少小时
				long min = diff%nd%nh/nm;//计算差多少分钟
				long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
				return day+"天"+hour+"小时"+min+"分"+sec+"秒";
			}else{
				Calendar cStart = Calendar.getInstance();
				cStart.setTime(startTime);
				long diff = endTime.getTime() - startTime.getTime();
				long day = diff/nd;//计算差多少天
				int k = 0;
				for (int i = 0; i < day; i++) {
					if(!isWorkDay(cStart, i)){
						k++;
					}
				}
				day -= k;
				long hour = diff%nd/nh;//计算差多少小时
				long min = diff%nd%nh/nm;//计算差多少分钟
				long sec = diff%nd%nh%nm/ns;//计算差多少秒//输出结果
				return day+"天"+hour+"小时"+min+"分"+sec+"秒";
			}
		}
		//-------------------------------------------------------------------------------------------

	/**
	 * 根据一个时间点：查询这一整天的数据
	 * @Title:
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date[] getDateBeginAndEnd(Date date) throws ParseException {
		if(date==null)
			return null;
		Date[] list = new Date[2];
		SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat formater2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date start = formater2.parse(formater.format(date)+ " 00:00:00");
		Date end = formater2.parse(formater.format(date)+ " 23:59:59");
		list[0] = start;
		list[1] = end;
		return list;
	}
}

