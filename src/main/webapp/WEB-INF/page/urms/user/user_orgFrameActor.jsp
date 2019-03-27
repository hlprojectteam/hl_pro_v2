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
<title>用户选择</title>
</head>
<!-- ztree -->
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="" >
	<div class="form-group">
		<div class="col-sm-5">
			<select id="orgFrameSelect" style="width: 95%" multiple="multiple" class="form-control">
				 <c:forEach var="orgFrameList" items="${orgFrameList}" varStatus="status">
				 	<option value='${orgFrameList.id}' ondblclick='queryUser("${orgFrameList.id}")'>${orgFrameList.orgFrameName}</option>
				 </c:forEach>
			</select>
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
			<select id="chooseUser"  multiple="multiple" style="width: 95%" class="form-control">
			
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
var id = '${id}';//回填id
var name = '${name}';//回填name
var selectNum = '${selectNum}';//单选还是多选
var zTreeObj;
$(document).ready(function(){
	$("#orgFrameSelect").css("height",document.body.clientHeight-90);//select高度
	$("#rangeSelect").css("height",document.body.clientHeight-90);//select高度
	$("#chooseUser").css("height",document.body.clientHeight-90);//select高度
	$("#buttonDiv").css("margin-top",document.body.clientHeight/2-150);
	reSelectUser();//回调显示已选用户
});

//获得用户
function queryUser(orgFrameId){
	$("#rangeSelect").html("");
	$.ajax({
		type:"post",
		async:false,
		dataType : "json",
		url: '/urms/user_queryOrgFrameActor?orgFrameId='+orgFrameId,
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
	var so = $("#chooseUser option");
	if(selectNum=="single"){//单选
		if(so.length>0){
			autoMsg("只能选择一个用户",2);
			return false;
		}else{
			add_user();
		}
	}else{//多选
		if(so.length>29){//最多只能30个用户
			return false;
		}else{
			add_user();
		}
	}
}

//增加用户
function add_user(){
	var map = new Map();
	var so = $("#chooseUser option");
	for(var i=0;i<so.length;i++){ 
		map.put(so[i].value,so[i].text);
	}
	var ro = $("#rangeSelect option");
	for(var i=0;i<ro.length;i++){  
		if(ro[i].selected){  
			if(map.get(ro[i].value)==ro[i].text){//排除出重复选择
				continue;//出现重复就打断
			}else{
			   	$("#chooseUser").append("<option value='"+ro[i].value+"' ondblclick='on_remove()'>"+ro[i].text+"</option>");
			}
		}
	}
}
//删除
function on_remove(){
	 var o = $("#chooseUser option");
	 for(var i=0;i<o.length;i++){     
	     if(o[i].selected){  
	    	 $(o[i]).remove();
	     }  
	 } 
}

//删除全部
function on_removeAll(){
	var o = $("#chooseUser option");
	 for(var i=0;i<o.length;i++){     
	     $(o[i]).remove();
	 } 
}

//保存
function on_sure(){
	var o = $("#chooseUser option");
	var ids = "";
	var names = "";
	for(var i=0;i<o.length;i++){   
       ids += o[i].value+",";
       names += o[i].text+",";
    }
	parent.frames[winName].$("#"+id).val(ids.substring(0,ids.length-1));
	parent.frames[winName].$("#"+name).val(names.substring(0,names.length-1));
	if(parent.frames[winName].$("#showUserName").length>0)
		parent.frames[winName].$("#showUserName").html(names.substring(0,names.length-1));
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
			$("#chooseUser").append("<option value='"+idz[i]+"' ondblclick='on_remove()'>"+namez[i]+"</option>");
		}
	}
}

function on_close(){
	parent.layer.close(index);
}
</script>
</html>