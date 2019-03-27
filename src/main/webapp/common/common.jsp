<!DOCTYPE html>
<html>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<head>
    <!-- h+ begin -->
    <link href="/common/index/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
    <link href="/common/index/css/font-awesome.min.css?v=4.3.0" rel="stylesheet">
    <link href="/common/index/css/animate.min.css" rel="stylesheet">
    <link href="/common/index/css/style.min.css?v=3.2.0" rel="stylesheet">
    <!-- h+ end -->
    
	<!-- bootstrapTable表单 -->
	<link rel="stylesheet" href="/common/plugins/bootstrapTable/css/bootstrap-table.css">
	<!-- 基础css -->
	<link rel="stylesheet" href="/common/css/base.css">
	<!-- 文件上传 -->
	<link rel="stylesheet" type="text/css" href="/common/plugins/webUploader/webuploader.css">
	<!-- 照片预览 -->
	<link rel="stylesheet" href="/common/plugins/blueimp/css/blueimp-gallery.min.css" type="text/css">
	<!-- 主题样式 -->
	<link rel="stylesheet" href="/common/newIndexFrame/css/themes/classic/skins/site.css" id="siteStyle">		
</head>
<body>
	<!-- 全局js -->
    <script src="/common/index/js/jquery-2.1.1.min.js"></script>
    <script src="/common/index/js/bootstrap.min.js?v=3.4.0"></script>
    <!-- 虚拟滚动条 -->
    <script src="/common/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script src="/common/plugins/ajaxfileupload.js"></script>
    <script src="/common/plugins/metisMenu/jquery.metisMenu.js"></script>
    <!-- 自定义js -->
    <script src="/common/index/js/content.min.js"></script>
    
	<!-- bootstrapTable表单 -->
	<script src="/common/plugins/bootstrapTable/js/bootstrap-table.js"></script>
	<script src="/common/plugins/bootstrapTable/js/bootstrap-table-zh-CN.js"></script>
	<!-- 即时通讯start 包含 layer弹框插件-->
	<script src="/common/plugins/layui/layui.js" charset="utf-8"></script>
	<!-- 即时通end -->
	<!-- 基础js -->
	<script src="/common/js/base.js"></script>
	<script src="/common/js/form2Json.js"></script>
	<script src="/common/js/alert_tip.js"></script>
	<!-- md5加密 -->
	<script type="text/javascript" src="/common/js/md5.js"></script>
	<!-- js实现map功能 -->
	<script type="text/javascript" src="/common/js/map.js"></script>
	<!-- 日期控件 -->
	<script type="text/javascript" src="/common/plugins/My97DatePicker/WdatePicker.js"></script>
	<!-- 表单验证 -->
	<script src="/common/plugins/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="/common/plugins/validate/messages_zh.min.js" type="text/javascript"></script>
	<!-- 文件上传 -->
	<script type="text/javascript" src="/common/plugins/webUploader/webuploader.js"></script>
	<!-- 照片预览 -->
	<script src="/common/plugins/blueimp/js/jquery.blueimp-gallery.min.js"></script>
	<script>
	//主题颜色
	var themeColor = localStorage.getItem("themeColor");
	if(themeColor!=null&&themeColor!=""&&themeColor!="null"){
		$("#siteStyle").attr("href","/common/newIndexFrame/css/themes/classic/skins/"+themeColor+".css");
	}
	</script>
</body>
</html>