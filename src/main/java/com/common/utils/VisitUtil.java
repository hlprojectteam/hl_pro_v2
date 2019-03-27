package com.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.common.utils.cache.Cache;
import com.urms.apiConfig.module.ApiConfig;

/**
 * @访问记录信息类
 * @描述：主要用来获取访问客户端等一些数据。IP,访问系统版本，浏览器等
 * @author Mr.Wang
 * @date 2016年6月13日 17:14:10
 */
public class VisitUtil {

	// 测试Main方法
	public static void main(String[] args) {
		try {
			// String str = getVisitIp();
			// getVisitIpOperatorAdress();
			// getVisitOperatorType();
			// JSONObject myJsonObject = new JSONObject(str); //{"ret":"ok","ip":"183.5.247.28","data":["中国","广东","广州","电信"]}
			// System.out.println(str); //183.5.247.28
			// System.out.println(myJsonObject.getString("data")); //["中国","广东","广州","电信"]
			// String str = getVisitIpOperatorAdress();
			// int i = getVisitOperatorType();
			// String str = getVisitUserAgent();
			// System.out.println(str);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 访问的IP地址 比如：183.5.247.28 （外网IP地址）
	 * @Title: 
	 * @param visitServerName  访问地址双斜杠后面的访问名字
	 * @param userAgent 浏览器与系统信息
	 * @return ip + "@@@" + operatorType + "@@@" + address + "@@@" +agent + "@@@" + os + "@@@" + browser + "@@@" + pcOrMobile;
	 */
	public static String getVisitIp(String visitServerName,String userAgent) {
		String getInfo = "";//获得返回的数据
		String ip = ""; //外网IP地址
		int operatorType=0;//运营商类型 - 1电信、2联通 、3移动、4铁通、5长城宽带、6其它
		String address = ""; //地址  - 比如：["中国","广东","广州","电信"]
		
		if (visitServerName.startsWith("localhost") || visitServerName.startsWith("127.") || visitServerName.startsWith("192.168") || visitServerName.startsWith("10.") || visitServerName.startsWith("172.16.")) {
			// 如果访问的地址来自：localhost、127.0.0.1、192.168.X.X 说明来自局域网，
			// 调用http://2018.ip138.com/ic.asp，即可获取相关信息

			getInfo = getHttpVisitInfo(); // HTTP 不安全协议
			//IP
			if (StringUtils.isNotEmpty(getInfo)) {
				int start = getInfo.indexOf("[") + 1;
				int end = getInfo.indexOf("]");
				ip = getInfo.substring(start, end);
			}
			//运营商地址
			int start = getInfo.indexOf("来自：") + 3;
			int end = getInfo.indexOf("</center>");
			if(end>=start){
				address = getInfo.substring(start, end);
			}
		} else {
			// 除上述情况外，调用 https://api.ip138.com/query/ 安全访问获取相关外网IP及地址信息
			getInfo = getHttpsVisitInfo(); // HTTPS 安全协议
			//IP
			String[] arr = getInfo.split(" ");
			StringBuffer sb = new StringBuffer();
			if (arr.length > 0) {
				ip = arr[0];
			}
			//运营商地址
			StringBuffer sbAddress = new StringBuffer();
			for (int i = 1; i < arr.length; i++) {
				sbAddress.append(arr[i]); // 第一个IP地址不要，只要详细地址
			}
			address = sbAddress.toString();
		}
		//判断运营商类型 
		operatorType = getVisitOperatorType(getInfo);
		
		//用户系统，浏览器版本
		//agent + "@@@" + os + "@@@" + browser + "@@@" + pcOrMobile;
		String info = getVisitUserAgent(userAgent);
		
		//returnInfo=ip + "@@@" + operatorType + "@@@" + address + "@@@" +agent + "@@@" + os + "@@@" + browser + "@@@" + pcOrMobile;
		String returnInfo = ip + "@@@" + operatorType + "@@@" + address + "@@@" + info;
		return returnInfo;
	}

	/**
	 * 获得访问IP地址的运营商：电信，联通，网通，长城宽带
	 * 1电信、2联通 、3移动、4铁通、5长城宽带、6其它
	 * @return
	 * @throws JSONException
	 */
	public static int getVisitOperatorType(String getInfo) {
		int type = 0;
		if (getInfo.contains("电信")) {
			type = 1;
		} else if (getInfo.contains("联通")) {
			type = 2;
		} else if (getInfo.contains("移动")) {
			type = 3;
		} else if (getInfo.contains("铁通")) {
			type = 4;
		} else if (getInfo.contains("长城")) {
			type = 5;
		} else {
			type = 6;
		}
		return type;
	}

	/**
	 * HTTP访问方式 获得访问的IP地址等信息
	 * 
	 * @param obj
	 * @return
	 */
	public static String getHttpVisitInfo() {
		URL url = null; // 网络的url地址
		BufferedReader in = null; // 输入流
		StringBuffer sb = new StringBuffer();
		try {
			url = new URL("http://2018.ip138.com/ic.asp"); // 或可用 http://ip.chinaz.com/getip.aspx
			in = new BufferedReader(new InputStreamReader(url.openStream(), "gb2312")); // UTF-8
			String str = null;
			while ((str = in.readLine()) != null) {
				sb.append(str);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		String result = sb.toString();
		return result;
	}

	/**
	 * HTTPS访问方式 获得访问的IP地址等信息
	 * 
	 * @param obj
	 * @return
	 */
	public static String getHttpsVisitInfo() {
		URL url = null; // 网络的url地址
		String urlString = "";
		BufferedReader br = null; // 输入流
		String resultStr = "";
		try {
			ApiConfig apiConfig = (ApiConfig) Cache.getApiConfig.get(1); // 1-IP,2-SMS,3-MAP,4-Weather,5-Video
			urlString = apiConfig.getApiUrl();
			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(5 * 1000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("token", apiConfig.getApiToken());
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				StringBuilder builder = new StringBuilder();
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
				for (String s = br.readLine(); s != null; s = br.readLine()) {
					builder.append(s);
				}
				br.close();
				resultStr = builder.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return resultStr;
	}

	/**
	 * 获得访问用户的浏览器与系统相关信息
	 * 
	 * @Title:
	 * @param agent 获得访问用户的系统和浏览器信息
	 * @author Mr.Wang
	 * @date 2016年3月10日 10:12:37
	 * @return
	 */
	public static String getVisitUserAgent(String agent) {
		StringBuilder userAgent = new StringBuilder("[");
		userAgent.append(agent);
		userAgent.append("]");

		// 系统
		int indexOfMac = userAgent.indexOf("Mac OS X");// 电脑系统
		int indexOfWindows = userAgent.indexOf("Windows NT");// 电脑系统
		int indexOfLinux = userAgent.indexOf("Linux"); // 电脑系统
		/* 由于安卓系统底层也是linux所以，必须结合indexOfLinux一起来判断才能区分电脑linux和手机安卓系统 */
		int indexOfAndroid = userAgent.indexOf("Android"); // 手机系统
		int indexOfiPhone = userAgent.indexOf("iPhone"); // 手机系统

		// 浏览器
		int indexOfEdge = userAgent.indexOf("Edge");
		int indexOfIE = userAgent.indexOf("MSIE");
		int indexOfIE11 = userAgent.indexOf("rv:");
		int indexOfFF = userAgent.indexOf("Firefox");
		int indexOfSogou = userAgent.indexOf("MetaSr");
		int indexOfChrome = userAgent.indexOf("Chrome"); // 谷歌浏览器
		int indexOfSafari = userAgent.indexOf("Safari"); // Safari浏览器(苹果自带)

		boolean isMac = indexOfMac > 0;
		boolean isWindows = indexOfWindows > 0;
		boolean isLinux = indexOfLinux > 0 && indexOfAndroid == -1;
		boolean isAndroid = indexOfAndroid > 0;
		boolean isiPhone = indexOfiPhone > 0;
		boolean containIE = indexOfIE > 0 || (isWindows && (indexOfIE11 > 0));
		boolean containFF = indexOfFF > 0;
		boolean containSogou = indexOfSogou > 0;
		boolean containChrome = indexOfChrome > 0;
		boolean containSafari = indexOfSafari > 0;

		String browser = ""; // 浏览器
		if (containSogou) {
			if (containIE) {
				browser = "IE" + userAgent.substring(indexOfIE, indexOfIE + "MSIE x.x".length());
			} else if (containChrome) {
				browser = "Chrome" + userAgent.substring(indexOfChrome, indexOfChrome + "Chrome/xx".length());
			}
		} else if (containChrome) {
			if (indexOfEdge > 0) {
				browser = userAgent.substring(indexOfEdge, indexOfEdge + "Edge/xx".length());
			} else {
				browser = userAgent.substring(indexOfChrome, indexOfChrome + "Chrome/xx".length());
			}
		} else if (containSafari) {
			int indexOfSafariVersion = userAgent.indexOf("Version");
			browser = "Safari " + userAgent.substring(indexOfSafariVersion, indexOfSafariVersion + "Version/x.x.x".length());
		} else if (containFF) {
			browser = userAgent.substring(indexOfFF, indexOfFF + "Firefox/xx".length());
		} else if (containIE) {
			if (indexOfIE11 > 0) {
				browser = "Internet Explorer 11";
			} else {
				browser = userAgent.substring(indexOfIE, indexOfIE + "MSIE x.x".length());
			}
		}
		String os = ""; // 系统
		int pcOrMobile = 0; // 电脑，还是手机
		if (isMac) {
			os = userAgent.substring(indexOfMac, indexOfMac + "MacOS X xxxx".length());
			pcOrMobile = 1;
		} else if (isLinux) {
			os = "Linux";
			pcOrMobile = 1;
		} else if (isWindows) {
			os = "Windows ";
			pcOrMobile = 1;
			String version = userAgent.substring(indexOfWindows + "Windows NT".length(), indexOfWindows + "Windows NTx.x".length());
			if ("5.0".equals(version.trim())) {
				os += "2000";
			} else if ("5.1".equals(version.trim())) {
				os += "XP";
			} else if ("5.2".equals(version.trim())) {
				os += "2003";
			} else if ("6.0".equals(version.trim())) {
				os += "Vista";
			} else if ("6.1".equals(version.trim())) {
				os += "7";
			} else if ("6.2".equals(version.trim())) {
				os += "8";
			} else if ("10".equals(version.trim())) {
				os += "10";
			}
		} else if (isAndroid) {
			os = "Android ";
			os += userAgent.substring(indexOfAndroid + "Android ".length(), indexOfAndroid + "Android x.x.x".length());
			pcOrMobile = 2;
		} else if (isiPhone) {
			os = "iPhone ";
			os += userAgent.substring(indexOfAndroid + "iPhone OS ".length(), indexOfAndroid + "iPhone OS x_x_x".length());
			pcOrMobile = 2;
		}
		String info = agent + "@@@" + os + "@@@" + browser + "@@@" + pcOrMobile;
		return info;
	}

	/**
	 * 直接输出JSON
	 * 
	 * @param json
	 * @param response
	 */
	public static void write(String json, HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/plain;charset=UTF-8");
		try {
			PrintWriter writer = response.getWriter();
			writer.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}