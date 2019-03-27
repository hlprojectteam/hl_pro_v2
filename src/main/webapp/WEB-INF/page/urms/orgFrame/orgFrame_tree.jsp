<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>组织架构列表</title>
    <!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
</head>
<body>
<div class="ibox-content" style="padding-top: 5px;">
	<div class="row ibox-content">
		<ul id="tree" class="ztree"></ul>
	</div>
	<input type="hidden" id="Id">
	<input type="hidden" id="code">
	<input type="hidden" id="name">
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer">
<div class="pull-right">
<button class="btn btn-success " type="button" onclick="on_sure()"><i class="fa fa-check"></i>&nbsp;确定</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
<script type="text/javascript"> 
    var winName = '${winName}';
    var id = '${id}';
    var code = '${code}';
    var name = '${name}';
    var zTreeObj;
    $(document).ready(function(){
    	zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
    });
    //初始化树
    var setting = {
    		data: {
    			simpleData: {
    				enable: true
    			}
    		},
    		callback:{
    			onClick: zTreeOnClick
    		},
    		async: {//异步加载
    			enable: true,
    			type: "post",
    			dataType: "text",
    			contentType:"application/x-www-form-urlencoded",
    			url: "/urms/orgFrame_loadTree",
    			autoParam: ["id"]
    		}
    	};

    //点击树后回调
    function zTreeOnClick(event, treeId, treeNode) {
    	$("#Id").val(treeNode.id);
    	$("#name").val(treeNode.name);
    	$("#code").val(treeNode.code);
    }
    //
    function on_sure(){
    	if($("#Id").val()!=""&&$("#Id").val()!=null){
    		parent.frames[winName].$("#"+id).val(($("#Id").val()));
    		parent.frames[winName].$("#"+code).val(($("#code").val()));
    		parent.frames[winName].$("#"+name).val(($("#name").val()));
    		on_close();
    	}else{
    		autoAlert("请选择一个组织!",5);
    	}
    }
</script>
</body>
</html>