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
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="" >
	<div class="form-group">
		<div class="col-sm-5">
			<table id="grid"></table>
		</div>
		<div class="col-sm-3">
			<select id="rangeSelect" style="width: 95%" multiple="multiple" class="form-control">
			</select>
		</div>
		<div class="col-sm-1" id="buttonDiv" style="padding-left: 0">
			<button class="btn btn-primary" type="button" onclick="on_add()" id="add">添加</button></br></br></br>
			<button class="btn btn-primary" type="button" onclick="on_remove()" id="remove">删除</button></br></br></br>
			<button class="btn btn-primary" type="button" onclick="on_removeAll()" id="removeAll">全删</button>
		</div>
		<div class="col-sm-3 text-left">
			<select id="chooseRole"  multiple="multiple" style="width: 95%">
			
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
var id = '${id}';//回填id
var name = '${name}';//回填name
var zTreeObj;
var sysCode = '${sysCode}';
var roleId = '';
$(document).ready(function(){
	loadGrid();//
	$("#rangeSelect").css("height",document.body.clientHeight-90);//select高度
	$("#chooseRole").css("height",document.body.clientHeight-90);//select高度
	$("#buttonDiv").css("margin-top",document.body.clientHeight/2-150);
	reSelectRole();//回调显示已选角色
	queryUser("");//显示所有用户
});

//加载列表
function loadGrid(){
	var sysCode = $("#sysCode").val();
	$("#grid").bootstrapTable({
		url: "/urms/role_load?sysCode="+sysCode,
		dataType:"json",
		method:"post",
		queryParamsType: "limit",//服务器分页必须
		striped:false,//条纹行
		contentType: "application/x-www-form-urlencoded",
		pageSize:10,//每页大小
		pageNumber:1,//开始页
		pageList:[10,20,50],
		pagination:true,//显示分页工具栏
		sidePagination: "server", //服务端请求
		queryParams: queryParams,//发送请求参数
		onClickRow:onClickRow,
	    columns: [{title: "角色名称",field: "roleName",align:"center",formatter:function(value,row,index){
	    	return "<span style='cursor:pointer'>"+value+"</span>";
	    }}]
	 });
}

//点击角色 获得菜单列表的权限
function onClickRow(row,tr){
	roleId = row.id;
	sysCode = row.sysCode;
	tr.css("backgroundColor","#DDDDDD");
	tr.siblings().each(function(){
		$(this).css("backgroundColor","white");
	});
	queryUser(roleId)
}

//获得角色人员
function queryUser(roleId){
	$("#rangeSelect").html("");
	$.ajax({
		type:"post",
		async:false,
		dataType : "json",
		url: '/urms/role_queryUserList?roleId='+roleId,
		success : function(data){
			if(data!=null){
				 for (var i in data) {
				 	$("#rangeSelect").append("<option value='"+data[i].id+"' ondblclick='on_add()'>"+data[i].userName+"</option>");
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
	parent.frames[winName].$("#"+id).val(ids.substring(0,ids.length-1));
	parent.frames[winName].$("#"+name).val(names.substring(0,names.length-1));
	if(parent.frames[winName].$("#showUserName").length>0)//流程显示选择的用户
		parent.frames[winName].$("#showUserName").html(names.substring(0,names.length-1));
	parent.layer.close(index);//关闭
}


//回调显示已选角色
function reSelectRole(){
	var ids =parent.frames[winName].$("#"+id).val();
	var names =parent.frames[winName].$("#"+name).val();
	if(ids!=null&&ids!=""){
		var idz = ids.split(",");
		var namez = names.split(",");
		for(var i=0;i<idz.length;i++){   
			$("#chooseRole").append("<option value='"+idz[i]+"' ondblclick='on_remove()'>"+namez[i]+"</option>");
		}
	}
}

function on_close(){
	parent.layer.close(index);
}
</script>
</html>