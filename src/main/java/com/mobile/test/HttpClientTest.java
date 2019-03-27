package com.mobile.test;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;

import sun.misc.BASE64Decoder;

import com.common.utils.AESUtils;
import com.common.utils.Common;
import com.common.utils.helper.MD5Utils;

public class HttpClientTest {
	public static void main(String[] args){
        //登陆 Url
        String loginUrl = "http://127.0.0.1:8080/mobileLogin";
        //需登陆后访问的 Url
        String dataUrl = "http://127.0.0.1:8080/mobileTest";
        HttpClient httpClient = new HttpClient();
        //模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
        PostMethod postMethod = new PostMethod(loginUrl);
        //设置登陆时要求的信息，一般就用户名和密码，验证码自己处理了
        NameValuePair[] data = {
                new NameValuePair("loginName", "hx1"),
                new NameValuePair("password", MD5Utils.MD5("111111"))
        };
        postMethod.setRequestBody(data);
        try {
            //设置 HttpClient 接收 Cookie,用与浏览器一样的策略
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.executeMethod(postMethod);
            //获得登陆后的 Cookie
            Cookie[] cookies=httpClient.getState().getCookies();
            String tmpcookies= "";
            for(Cookie c:cookies){
                tmpcookies += c.toString()+";";
            }
            //进行登陆后的操作
            PostMethod post = new PostMethod(dataUrl);
            //每次访问需授权的网址时需带上前面的 cookie 作为通行证
            post.setRequestHeader("cookie",tmpcookies);
            //你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
//          postMethod.setRequestHeader("User-Agent","Unmi Spot");
            httpClient.executeMethod(post);
            //打印出返回数据，检验一下是否成功
            BASE64Decoder decoder = new BASE64Decoder();  
            String date = new String(AESUtils.decrypt(decoder.decodeBuffer(post.getResponseBodyAsString()), Common.AESPassword));
            System.err.println("解密后: " + date);   
        } catch (Exception e) {
            e.printStackTrace();
        }   
    }
	
}
