<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'app.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  	<div style="width:100%;font-size:20px;padding:20px;color:blue;">下载中，请稍候...</div>
  </body>
  <script type="text/javascript">
  	var Terminal = {
            platform : function(){
                var u = navigator.userAgent, app = navigator.appVersion;
                return {
                    android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1,
                    iPhone: u.indexOf('iPhone') > -1 ,
                    iPad: u.indexOf('iPad') > -1
                };
            }(),
            language : (navigator.browserLanguage || navigator.language).toLowerCase()
        }
  	var theUrl = 'http://downloadpkg.apicloud.com/app/download?path=http://A6929498257526.qiniucdn.apicloud-system.com/95677fe5d911cf321d0da9c79d5fbb31_d';
        if(Terminal.platform.android){
        	
        }else if(Terminal.platform.iPhone){
            theUrl = 'https://itunes.apple.com/cn/app/huan-long-ying-yun-tong/id1170974616?mt=8';
        }
 
        window.location.href = theUrl;
  </script>
</html>
