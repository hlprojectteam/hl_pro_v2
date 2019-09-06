package com.common.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.common.utils.helper.DateUtil;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String str="在单进程的系就需要对";
//		
//		String str2=StrUtils.subString(str, 9);
//		System.out.println(str2);
		
//		String tt=MathUtil.randomTwoNumber();
//		System.out.println(DateUtil.getYYMMDDHHMMSS2(tt));
		
//		String dateStr="2019-05-28 12:23:00";
//		try {
//			//"yyyy-MM-dd HH:mm:ss"是正确格式，其它都出错
//			Date d=DateUtil.format(dateStr,"yyyy-MM-dd HH:mm:ss");
//			System.out.println(d);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		String msg="297ef6c66b507393016b5076e0b50001,297ef6c66b507393016b508747100006,297ef6c66b507393016b507c46060005,297ef6c66b507393016b507c1aa40004";
//		if(msg.indexOf(",")>0){
//			String[] msgArr= msg.split(",");
//			int lmsg=msgArr.length;
//			if(lmsg>1){
//				//长度大于50个id，进行删减
//				//index 多出的个数
//				int index=lmsg-1;
//				for (int i = 0; i < index; i++) {
//					msg= msg.substring(msg.indexOf(",")+1, msg.length()); 
//				}
//				System.out.println(msg);
//			}
//		}
		
		//获取指定日期的最后时刻
//		Date date =new Date();
//		Date dateEnd= DateUtil.getDateEndTime(date);
//		String dateStr=DateUtil.getDateFormatString(dateEnd, DateUtil.JAVA_DATE_FORMAT_YMDHMS);
//		System.out.println(dateStr);
		
		//获取指定日期的当月的第一天与最后一天
//		Date date =new Date();
//		String[] arr= DateUtil.getFirstday_Lastday_Month(date);
//		System.out.println(arr[0]+"-"+arr[1]);
		
	}
	
	@org.junit.Test
	public void testMethod(){
		
		for (int i = 0; i < 100; i++) {
			int num=MathUtil.getRandomNumber(4)-1;
			System.out.println(num+"");
		}
		
		
	}

}
