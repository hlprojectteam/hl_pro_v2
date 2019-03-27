<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>题库</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="questionManageId" name="questionManage.id" value="${questionManageId}" />	
<input type="hidden" id="id" name="id" value="${questionVo.id}" />	
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value="${questionVo.createTime}"  pattern="yyyy-MM-dd HH:mm:ss"/>"/>
<div class="tab-content ">
<ul class="nav nav-tabs">
    <li class="active"><a data-toggle="tab" href="#single">单选</a></li>
    <li class=""><a data-toggle="tab" href="#many">多选(不定项)</a></li>
    <li class=""><a data-toggle="tab" href="#judge">判断</a></li>
    <li class=""><a data-toggle="tab" href="#fill">填空</a></li>
</ul>
<%-- 单选--%>
<div id="single" class="tab-pane active">
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<button class="btn btn-primary " type="button" onclick="on_addSingle()" style="margin-left: 15px;"><i class="fa fa-plus"></i>&nbsp;新增单选</button>
		<button class="btn btn-danger " type="button" onclick="on_delSingle()" style="margin-left: 15px;"><i class="fa fa-remove"></i>&nbsp;删除单选</button>
	</div>
	<table id="gridSingle"></table>
</div>
<%-- 多选--%>
<div id="many" class="tab-pane">
	<div class="form-group" style="padding:5px 0 5px 0;">
		<button class="btn btn-primary " type="button" onclick="on_addMany()" style="margin-left: 15px;"><i class="fa fa-plus"></i>&nbsp;新增多选</button>
    	<button class="btn btn-danger " type="button" onclick="on_delMany()" style="margin-left: 15px;"><i class="fa fa-remove"></i>&nbsp;删除多选</button>
	</div>
	<table id="gridMany"></table>
</div>
<%-- 判断 --%>
<div id="judge" class="tab-pane">
	<div class="form-group" style="padding:5px 0 5px 0;">
		<button class="btn btn-primary " type="button" onclick="on_addJudge()" style="margin-left: 15px;"><i class="fa fa-plus"></i>&nbsp;新增判断</button>
    	<button class="btn btn-danger " type="button" onclick="on_delJudge()" style="margin-left: 15px;"><i class="fa fa-remove"></i>&nbsp;删除判断</button>		
	</div>
	<table id="gridJudge"></table>
</div>
<%-- 填空 --%>
<div id="fill" class="tab-pane">
	<div class="form-group" style="padding:5px 0 5px 0;">
		<button class="btn btn-primary " type="button" onclick="on_addFill()" style="margin-left: 15px;"><i class="fa fa-plus"></i>&nbsp;新增填空</button>
    	<button class="btn btn-danger " type="button" onclick="on_delFill()" style="margin-left: 15px;"><i class="fa fa-remove"></i>&nbsp;删除填空</button>
	</div>
	<table id="gridFill"></table>
</div>
</div>
</form>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script type="text/javascript">
var winName = '${winName}';
$(function(){
	loadGrid();//加载列表
});

//新增单选
function on_addSingle(){
	parent.layer.open({
         type: 2,
         title: "新增单选",
         shadeClose: true,//打开遮蔽
         shade: 0.6, 
         maxmin: true, //开启最大化最小化按钮
         area: ["100%", "100%"],
         content: "/answer/question_edit_single?questionManageId="+$("#questionManageId").val()+"&winName="+window.name
    });
}

//新增多选
function on_addMany(){
	parent.layer.open({
         type: 2,
         title: "新增多选",
         shadeClose: true,//打开遮蔽
         shade: 0.6, 
         maxmin: true, //开启最大化最小化按钮
         area: ["100%", "100%"],
         content: "/answer/question_edit_many?questionManageId="+$("#questionManageId").val()+"&winName="+window.name
    });
}

//新增判断
function on_addJudge(){
	parent.layer.open({
         type: 2,
         title: "新增判断",
         shadeClose: true,//打开遮蔽
         shade: 0.6, 
         maxmin: true, //开启最大化最小化按钮
         area: ["80%", "50%"],
         content: "/answer/question_edit_judge?questionManageId="+$("#questionManageId").val()+"&winName="+window.name
    });
}

//新增填空
function on_addFill(){
	parent.layer.open({
         type: 2,
         title: "新增填空",
         shadeClose: true,//打开遮蔽
         shade: 0.6, 
         maxmin: true, //开启最大化最小化按钮
         area: ["80%", "100%"],
         content: "/answer/question_edit_fill?questionManageId="+$("#questionManageId").val()+"&winName="+window.name
    });
}

//加载列表
function loadGrid() {
	//单选
	$("#gridSingle").bootstrapTable({
		url: "/answer/question_load?type=1&questionManageId="+$("#questionManageId").val(),
		dataType:"json",
		method:"post",
		queryParamsType: "limit",//服务器分页必须
		striped:true,//条纹行
		contentType: "application/x-www-form-urlencoded",
		pageSize:10,//每页大小
		pageNumber:1,//开始页
		pageList:[10,20,50],
		pagination:true,//显示分页工具栏
		sidePagination: "server", //服务端请求
		queryParams: queryParams,//发送请求参数
	    columns: [{checkbox:true},
	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
	           	  		return index+1;
	              }},
	              {title: "题目",field: "title",width: 200,align:"center"}, 
	              {title: "状态",field: "state",width: 100,align:"center",formatter:function(value,row,index){
	            	  return changeDataDictByKey("state",value); 
	              }}, 
				  {title: "操作", field: "", width: 90,align:"center",formatter:function(value,row,index){
                      return "<a href='#' onclick='on_optionSingle(\""+row.id+"\")'>编辑选项</a>";
	              }}]
	 });
	//多选 不定项
	$("#gridMany").bootstrapTable({
		url: "/answer/question_load?type=2&questionManageId="+$("#questionManageId").val(),
		dataType:"json",
		method:"post",
		queryParamsType: "limit",//服务器分页必须
		striped:true,//条纹行
		contentType: "application/x-www-form-urlencoded",
		pageSize:10,//每页大小
		pageNumber:1,//开始页
		pageList:[10,20,50],
		pagination:true,//显示分页工具栏
		sidePagination: "server", //服务端请求
		queryParams: queryParams,//发送请求参数
	    columns: [{checkbox:true},
	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
	           	  		return index+1;
	              }},
	              {title: "题目",field: "title",width: 200,align:"center"}, 
	              {title: "状态",field: "state",width: 100,align:"center",formatter:function(value,row,index){
	            	  return changeDataDictByKey("state",value); 
	              }}, 
				  {title: "操作", field: "", width: 90,align:"center",formatter:function(value,row,index){
                      return "<a href='#' onclick='on_optionMany(\""+row.id+"\")'>编辑选项</a>";
	              }}]
	 });
	//判断
	$("#gridJudge").bootstrapTable({
		url: "/answer/question_load?type=3&questionManageId="+$("#questionManageId").val(),
		dataType:"json",
		method:"post",
		queryParamsType: "limit",//服务器分页必须
		striped:true,//条纹行
		contentType: "application/x-www-form-urlencoded",
		pageSize:10,//每页大小
		pageNumber:1,//开始页
		pageList:[10,20,50],
		pagination:true,//显示分页工具栏
		sidePagination: "server", //服务端请求
		queryParams: queryParams,//发送请求参数
	    columns: [{checkbox:true},
	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
	           	  		return index+1;
	              }},
	              {title: "题目",field: "title",width: 200,align:"center"}, 
	              {title: "状态",field: "state",width: 100,align:"center",formatter:function(value,row,index){
	            	  return changeDataDictByKey("state",value); 
	              }}, 
				  {title: "操作", field: "", width: 90,align:"center",formatter:function(value,row,index){
                      return "<a href='#' onclick='on_optionJudge(\""+row.id+"\")'>编辑选项</a>";
	              }}]
	 });
	//填空
	$("#gridFill").bootstrapTable({
		url: "/answer/question_load?type=4&questionManageId="+$("#questionManageId").val(),
		dataType:"json",
		method:"post",
		queryParamsType: "limit",//服务器分页必须
		striped:true,//条纹行
		contentType: "application/x-www-form-urlencoded",
		pageSize:10,//每页大小
		pageNumber:1,//开始页
		pageList:[10,20,50],
		pagination:true,//显示分页工具栏
		sidePagination: "server", //服务端请求
		queryParams: queryParams,//发送请求参数
	    columns: [{checkbox:true},
	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
	           	  		return index+1;
	              }},
	              {title: "题目",field: "title",width: 200,align:"center"}, 
	              {title: "状态",field: "state",width: 100,align:"center",formatter:function(value,row,index){
	            	  return changeDataDictByKey("state",value); 
	              }}, 
				  {title: "操作", field: "", width: 90,align:"center",formatter:function(value,row,index){
                      return "<a href='#' onclick='on_optionFill(\""+row.id+"\")'>编辑选项</a>";
	              }}]
	 });
}

//单选编辑
function on_optionSingle(id){
	parent.layer.open({
        type: 2,
        title: "编辑单选",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["100%", "100%"],
        content: "/answer/question_edit_single?questionManageId="+$("#questionManageId").val()+"&id="+id+"&winName="+window.name
   });
}

//多选编辑
function on_optionMany(id){
	parent.layer.open({
        type: 2,
        title: "编辑多选",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["100%", "100%"],
        content: "/answer/question_edit_many?questionManageId="+$("#questionManageId").val()+"&id="+id+"&winName="+window.name
   });
}

//判断编辑
function on_optionJudge(id){
	parent.layer.open({
        type: 2,
        title: "编辑判断",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["80%", "50%"],
        content: "/answer/question_edit_judge?questionManageId="+$("#questionManageId").val()+"&id="+id+"&winName="+window.name
   });
}

//填空编辑
function on_optionFill(id){
	parent.layer.open({
        type: 2,
        title: "编辑填空",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["80%", "100%"],
        content: "/answer/question_edit_fill?questionManageId="+$("#questionManageId").val()+"&id="+id+"&winName="+window.name
   });
}

//-------删除单选---------------------------------------------------------------------
function on_delSingle(){
	delete_tableData("gridSingle","/answer/question_delete","/answer/question_load?type=1&questionManageId=${questionManageId}");
}
//删除多选
function on_delMany(){
	delete_tableData("gridMany","/answer/question_delete","/answer/question_load?type=2&questionManageId=${questionManageId}");
}
//删除判断
function on_delJudge(){
	delete_tableData("gridJudge","/answer/question_delete","/answer/question_load?type=3&questionManageId=${questionManageId}");
}
//删除填空
function on_delFill(){
	delete_tableData("gridFill","/answer/question_delete","/answer/question_load?type=4&questionManageId=${questionManageId}");
}
</script>
</html>