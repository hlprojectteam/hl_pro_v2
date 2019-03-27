<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>角色选择</title>
</head>
<!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="" >
	<div class="form-group">
		<div class="col-sm-5">
			<input type="text" class="form-control" id="roleName" name="roleName" value="" />
		</div>
		<button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
	</div>
	<div class="form-group">
		<div class="col-sm-3">
			<ul id="tree" class="ztree"></ul>
		</div>
		<div class="col-sm-4">
			<select id="rangeSelect" style="width: 95%" multiple="multiple" class="form-control">
			</select>
		</div>
		<div class="col-sm-1" id="buttonDiv" style="padding-left: 0">
			<button class="btn btn-primary" type="button" onclick="on_add()" id="add">添加</button></br></br></br>
			<button class="btn btn-primary" type="button" onclick="on_remove()" id="remove">删除</button></br></br></br>
			<button class="btn btn-primary" type="button" onclick="on_removeAll()" id="removeAll">全删</button>
		</div>
		<div class="col-sm-4 text-left">
			<select id="chooseRole"  multiple="multiple" style="width: 95%" class="form-control">
			
			</select>
		</div>
	</div>
</form>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_sure()"><i class="fa fa-check"></i>&nbsp;确定</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script type="text/javascript"> 
var winName = '${winName}';//上一ifame name
var backId = '${id}';//回填id
var backName = '${name}';//回填name
var zTreeObj;
$(document).ready(function(){
	zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
	$("#rangeSelect").css("height",document.body.clientHeight-130);//select高度
	$("#chooseRole").css("height",document.body.clientHeight-130);//select高度
	$("#buttonDiv").css("margin-top",document.body.clientHeight/2-180);
	getRole("");//获得角色
	reSelectRole();//回调显示已选角色
});

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
	getRole(treeNode.id);
}

//获得角色
function getRole(pId){
	$("#rangeSelect").html("");
	$.ajax({
		type:"post",
		async:false,
		dataType : "json",
		url: '/urms/role_getRoleList?orgFrame.id='+pId,
		success : function(data){
			if(data!=null){
				 for (var i in data) {
				 	$("#rangeSelect").append("<option value='"+data[i].id+"' ondblclick='on_add()'>"+data[i].roleName+"</option>");
				 }
			}
		},
		error : function(result){
			autoAlert("系统出错",5);
		}
	});
}

//增加
function on_add(){
	var map = new Map();
	var so = $("#chooseRole option");
	for(var i=0;i<so.length;i++){ 
		map.put(so[i].value,so[i].text);
	}
	var ro = $("#rangeSelect option");
	for(var i=0;i<ro.length;i++){  
		if(ro[i].selected){  
			if(map.get(ro[i].value)==ro[i].text){//排除出重复选择
				continue;//出现重复就打断
			}else{
			   	$("#chooseRole").append("<option value='"+ro[i].value+"' ondblclick='on_remove()'>"+ro[i].text+"</option>");
			}
		}
	 } 
}

//删除
function on_remove(){
	 var o = $("#chooseRole option");
	 for(var i=0;i<o.length;i++){     
	     if(o[i].selected){  
	    	 $(o[i]).remove();
	     }  
	 } 
}
//全删
function on_removeAll(){
	var o = $("#chooseRole option");
	 for(var i=0;i<o.length;i++){     
	    $(o[i]).remove();
	 } 
}

//保存
function on_sure(){
	var o = $("#chooseRole option");
	var ids = "";
	var names = "";
	for(var i=0;i<o.length;i++){   
       ids += o[i].value+",";
       names += o[i].text+",";
    }
	parent.frames[winName].$("#"+backId).val(ids.substring(0,ids.length-1));
	parent.frames[winName].$("#"+backName).val(names.substring(0,names.length-1));
	parent.layer.close(index);//关闭
}


//回调显示已选角色
function reSelectRole(){
	var ids =parent.frames[winName].$("#"+backId).val();
	var names =parent.frames[winName].$("#"+backName).val();
	if(ids!=null&&ids!=""){
		var idz = ids.split(",");
		var namez = names.split(",");
		for(var i=0;i<idz.length;i++){   
			$("#chooseRole").append("<option value='"+idz[i]+"' ondblclick='on_remove()'>"+namez[i]+"</option>");
		}
	}
}

//搜索查询
function on_search(){
	$("#rangeSelect").html("");
	$.ajax({
		type:"post",
		async:false,
		dataType : "json",
		data:$('#baseForm').serialize(),
		url: '/urms/role_getRoleList',
		success : function(data){
			if(data!=null){
				 for (var i in data) {
				 	$("#rangeSelect").append("<option value='"+data[i].id+"' ondblclick='on_add()'>"+data[i].roleName+"</option>");
				 }
			}
		},
		error : function(result){
			autoAlert("系统出错",5);
		}
	});
}

//关闭
function on_close(){
	parent.layer.close(index);
}
</script>
</html>