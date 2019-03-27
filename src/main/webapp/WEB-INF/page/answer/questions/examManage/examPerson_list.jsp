<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>考试人员列表</title>
</head>
<!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
<body>
<div class="ibox-content" style="padding-top:5px;">
	 <!-- 工具条 -->
	<menu:definition menuCode="${menuCode }"/>
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
		  <div class="row">
			    <label class="col-sm-1 control-label">姓名</label>
			    <div class="col-sm-2">
			      	<input type="text" class="form-control" id="personName" name="personName" value="" data-rule-rangelength="[1,10]"/>
			    </div>
			    <label class="col-sm-1 control-label">工号</label>
			    <div class="col-sm-2">
			      	<input type="text" class="form-control" id="jobNumber" name="jobNumber" value="" data-rule-rangelength="[1,10]"/>
			    </div>
			    <button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
		  	 	<button class="btn btn-danger " type="button" onclick="on_del()"><i class="fa fa-remove"></i>&nbsp;删除</button>
		   </div>
		</form>
	</div>
	<div class="row ibox-content" style="padding:5px 0 5px 15px;">
		<div class="row">
		    <div class="col-sm-2">
		   	  <button class="btn btn-primary " type="button" onclick="on_add()"><i class="fa fa-plus"></i>&nbsp;增加考试人员</button>
			</div>
			<div class="col-sm-3">
		   	  <button class="btn btn-primary " type="button" onclick="on_addBatch()"><i class="fa fa-plus"></i>&nbsp;批量增加新入职考试人员</button>
			</div>
			<div class="col-sm-2">
		   	  <button class="btn btn-info " type="button" onclick="on_expScore()">导出员工考试成绩</button>
			</div>
		</div>
	</div>
	<div class="row ibox-content">
		<div class="col-sm-2">
			<ul id="tree" class="ztree"></ul>
		</div>
		<div class="col-sm-10">
			<table id="grid"></table>
		</div>
	</div>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
<script type="text/javascript"> 
var examManageId = '${id}';//试卷id
var zTreeObj;
$(document).ready(function(){
	zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
	loadGrid();//加载列表
});

function zTreeOnAsyncSuccess(){
    expandNodes(zTreeObj.getNodes());//获得当前树节点
}

function expandNodes(nodes){
	for (var i=0, l=nodes.length; i<l; i++) {
		zTreeObj.expandNode(nodes[i], true, false, false);
		if (nodes[i].isParent && nodes[i].zAsync) {//判断是否叶节点
			expandNodes(nodes[i].children);
		}
	}
}

var setting = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			onClick: zTreeOnClick,
			onAsyncSuccess: zTreeOnAsyncSuccess//异步正常调用后触发
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
	$("#grid").bootstrapTable("refresh",{url:"/answer/examPerson_load?examManage.id="+examManageId+"&orgFrameId="+treeNode.id});//加载树下的列表
}
	
    //加载列表
    function loadGrid() {
    	$("#grid").bootstrapTable({
    		url: "/answer/examPerson_load?examManage.id="+examManageId,
    		dataType:"json",
    		method:"post",
    		queryParamsType: "limit",//服务器分页必须
    		striped:true,//条纹行
    		contentType: "application/x-www-form-urlencoded",
    		pageSize:10,//每页大小
    		pageNumber:1,//开始页
    		sortable:true,
    		pageList:[10,20,50],
    		pagination:true,//显示分页工具栏
    		sidePagination: "server", //服务端请求
    		queryParams: queryParams,//发送请求参数
    	    columns: [{checkbox:true},
    	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
		           	  		return index+1;
		              }},
    	              {title: "姓名",field: "personName",width: 100,align:"center"}, 
    	              {title: "部门",field: "orgFrameName",width: 100,align:"center"}, 
    	              {title: "是否考试",field: "isExam",width: 100,align:"center",formatter:function(value,row,index){
    	            	  return changeDataDictByKey("isNot",value);
    	              }}, 
    	              {title: "耗时",field: "consumeTime",width: 150,align:"center",sortable: true,formatter:function(value,row,index){
    	              	  return  Math.floor(value/60)+":"+(value%60);
    	              }}, 
    	              {title: "成绩",field: "totalSource",width: 150,align:"center",sortable: true}, 
    				  {title: "操作", field: "", width: 90,align:"center",formatter:function(value,row,index){
    					  return "<a href='#' onclick='on_view(\""+row.personId+"\")'>成绩详情</a>";
		              }}]
    	 });
    }
    
    //详情
  	function on_view(personId){
  		parent.layer.open({
            type: 2,
            title: "考试成绩",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["50%", "60%"],
            content: "/answer/examOnline_result?id="+examManageId+"&personId="+personId   //考试管理id examManageId
        });
  	}
  	
  	//删除
  	function on_del(){
  	//列表tableId,删除请求url,刷新列表url
  		delete_tableData("grid","/answer/examPerson_delete","/answer/examPerson_load?examManage.id="+examManageId);
  	}
  	
  	//增加人员
  	function on_add(){
  		parent.layer.open({
            type: 2,
            title: "增加考试人员",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["50%", "60%"],
            content: "/answer/examOnline_addPerson?examManageId="+examManageId+"&winName="+window.name   //考试管理id examManageId
        });
  	}
  	
  	//批量增加新入职考试人员
  	function on_addBatch(){
  		parent.layer.open({
            type: 2,
            title: "批量增加新入职考试人员",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["80%", "100%"],
            content: "/answer/examOnline_addBatchPerson?examManageId="+examManageId+"&winName="+window.name   //考试管理id examManageId
        });
  	}
  	
  	//导出考试成绩
  	function on_expScore(){
  		parent.layer.open({
            type: 2,
            title: "导出成绩",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["60%", "30%"],
            content: "/answer/examManage_selExlPath?examManageId="+examManageId   //考试管理id examManageId
        });
  	}
  	
//查询
function on_search(){
	$("#grid").bootstrapTable("refresh");
}
</script>
</body>
</html>