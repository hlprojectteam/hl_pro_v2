package com.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 短信发送工具类
 * @author Mr.Wang
 * @date 2015年3月15日
 */

public class SMSUtil {

	/**
	 * 
	 * @param content :发送内容
	 * @param mobile ：电话号码 
	 * 
	 * void 返回操作结果 inputline
	 */
	public static String sendSms(String content, String mobile) {
		String inputline = "";
		try {
			// 发送内容
			// String content = "你好，你的注册验证码是123【河西街道办】"+new Date();
			// 创建StringBuffer对象用来操作字符串
			StringBuffer sb = new StringBuffer("http://m.5c.com.cn/api/send/?");
			// APIKEY
			sb.append("apikey=6481824e7ec3f80cedf3bdc6b311b65e");
			// 用户名
			sb.append("&username=yunting");
			// 向StringBuffer追加密码
			sb.append("&password=qwer1234");
			// 向StringBuffer追加手机号码
			sb.append("&mobile=" + mobile);
			// 向StringBuffer追加消息内容转URL标准码
			sb.append("&content=" + URLEncoder.encode(content, "GBK"));
			// 创建url对象
			URL url = new URL(sb.toString());
			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");
			// 发送
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			// 返回发送结果
			inputline = in.readLine();
			// 输出结果
			System.out.println(inputline);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputline;
	}

	/**
	 * 文件名称：sendSMS_demo.java
	 * 
	 * 文件作用：美联软通 http接口使用实例
	 * 
	 * 创建时间：2012-05-18
	 * 
	 *  返回值 说明 
	 *  success:msgid 提交成功，发送状态请见4.1 
	 *  error:msgid 提交失败 
	 *  error:Missing username 用户名为空 
	 *  error:Missing password 密码为空 
	 *  error:Missing apikey APIKEY为空 
	 *  error:Missing recipient 手机号码为空 
	 *  error:Missing message content 短信内容为空
	 *  error:Account is blocked 帐号被禁用
	 *  error:Unrecognized encoding 编码未能识别 
	 *  error:APIKEY or password error APIKEY 或密码错误 
	 *  error:Unauthorized IP address 未授权 IP 地址 
	 *  error:Account balance is insufficient 余额不足 
	 *  error:Black keywords is:党中央 屏蔽词
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		// String content = "IP20151215165303已成功办结，请您即日后携带证件原件(身份证,户口本,结婚证)";
		String content = "IP20151215165303已成功办结，请您即日后携带证件原件(身份证,户口本,结婚证)";
		String mobile = "18502048759,13570330616";
		new SMSUtil().sendSms(content, mobile);
	}

}
