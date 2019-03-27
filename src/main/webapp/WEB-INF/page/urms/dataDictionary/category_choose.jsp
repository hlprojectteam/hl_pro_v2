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
<title>字典目录选择</title>
</head>
<!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="" >
	<div class="form-group">
		<div class="col-sm-5">
			<ul id="tree" class="ztree"></ul>
		</div>
		<div class="col-sm-1" id="buttonDiv" style="padding-left: 0">
			<button class="btn btn-primary" type="button" onclick="on_add()" id="add">添加</button></br></br></br>
			<button class="btn btn-primary" type="button" onclick="on_remove()" id="remove">删除</button></br></br></br>
			<button class="btn btn-primary" type="button" onclick="on_removeAll()" id="removeAll">全删</button>
		</div>
		<div class="col-sm-5 text-left">
			<select id="chooseCategory"  multiple="multiple" style="width: 95%" class="form-control">
			
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
<script>
var winName = '${winName}';//上一ifame name
var categoryCode = '${categoryCode}';
var id = '${id}';//回填id
var name = '${name}';//回填name
var selectNum = '${selectNum}';//单选还是多选

var zTreeObj;
$(document).ready(function(){
	zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
	$("#chooseCategory").css("height",document.body.clientHeight-90);//select高度
	$("#buttonDiv").css("margin-top",document.body.clientHeight/2-150);
	reSelectUser();//回调显示已选用户
});

var setting = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			onClick: zTreeOnClick,
			onDblClick: zTreeOnDblClick//双击
		},
		async: {//异步加载
			enable: true,
			type: "post",
			dataType: "text",
			contentType:"application/x-www-form-urlencoded",
			url: "/urms/category_categoryTree?categoryCode="+categoryCode,
			autoParam: ["id"]
		}
	};
var categoryName = "";
var categoryCode = "";
//点击树后回调
function zTreeOnClick(event, treeId, treeNode) {
	categoryName = treeNode.name;
	categoryCode = treeNode.categoryCode;
}

function zTreeOnDblClick(event, treeId, treeNode){
	categoryName = treeNode.name;
	categoryCode = treeNode.categoryCode;
	on_add();
}


//增加
function on_add(){
	var so = $("#chooseCategory option");
	if(selectNum=="single"){//单选
		if(so.length>0){
			autoMsg("只能选择一个",2);
			return false;
		}else{
			add_category(categoryName,categoryCode);
		}
	}else{//多选
		if(so.length>29){//最多只能30个
			return false;
		}else{
			add_category(categoryName,categoryCode);
		}
	}
}

//增加用户
function add_category(categoryName,categoryCode){
	$("#chooseCategory").append("<option value='"+categoryCode+"' ondblclick='on_remove()'>"+categoryName+"</option>");
}
//删除
function on_remove(){
	 var o = $("#chooseCategory option");
	 for(var i=0;i<o.length;i++){     
	     if(o[i].selected){  
	    	 $(o[i]).remove();
	     }  
	 } 
}

//删除全部
function on_removeAll(){
	var o = $("#chooseCategory option");
	 for(var i=0;i<o.length;i++){     
	     $(o[i]).remove();
	 } 
}

//保存
function on_sure(){
	var o = $("#chooseCategory option");
	var ids = "";
	var names = "";
	for(var i=0;i<o.length;i++){   
       ids += o[i].value+",";
       names += o[i].text+",";
    }
	parent.frames[winName].$("#"+id).val(ids.substring(0,ids.length-1));
	parent.frames[winName].$("#"+name).val(names.substring(0,names.length-1));
	parent.layer.close(index);//关闭
}


//回调显示已选用户
function reSelectUser(){
	var ids =parent.frames[winName].$("#"+id).val();
	var names =parent.frames[winName].$("#"+name).val();
	if(ids!=null&&ids!=""){
		var idz = ids.split(",");
		var namez = names.split(",");
		for(var i=0;i<idz.length;i++){   
			$("#chooseCategory").append("<option value='"+idz[i]+"' ondblclick='on_remove()'>"+namez[i]+"</option>");
		}
	}
}

function on_close(){
	parent.layer.close(index);
}
</script>
</html>