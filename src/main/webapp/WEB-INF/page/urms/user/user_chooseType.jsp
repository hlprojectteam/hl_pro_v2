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
<title>人员、组织、角色选择</title>
</head>
<!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
<body>
<div class="ibox-content">
<div class="tab-content ">
<ul class="nav nav-tabs">
    <li id="userLi" class=""><a data-toggle="tab" href="#user" onclick="on_user(1)">人员</a>
    </li>
    <li id="orgFrameLi" class=""><a data-toggle="tab" href="#orgFrame" onclick="on_user(2)">组织（部门）</a>
    </li>
    <li id="roleLi" class=""><a data-toggle="tab" href="#role" onclick="on_user(3)">角色</a>
    </li>
</ul>
<div id="user" class="tab-pane">
	
</div>
<div id="orgFrame" class="tab-pane">
	
</div>
<div id="role" class="tab-pane">
	
</div>
	<form id="userForm" method="post" class="form-horizontal" name="userForm" action="" >
		<div class="form-group"></div>
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
				<select id="chooseUser"  multiple="multiple" style="width: 95%" class="form-control">
				
				</select>
			</div>
		</div>
	</form>
</div>
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
var nameId = '${name}';//回填name
var typeId = '${type}';//回填type
var selectNum = '${selectNum}';//单选还是多选
var type = parent.frames[winName].$("#"+typeId).val();//类型 1 用户 2 组织 3 角色
var zTreeObj;
$(document).ready(function(){
	zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
	$("#rangeSelect").css("height",document.body.clientHeight-150);//select高度
	$("#chooseUser").css("height",document.body.clientHeight-150);//select高度
	$("#buttonDiv").css("margin-top",document.body.clientHeight/2-150);
	if(type==2){
		$("#orgFrame").attr("class", "active"); 
		$("#orgFrameLi").attr("class", "active"); 
	}else if(type==3){
		$("#role").attr("class", "active"); 
		$("#roleLi").attr("class", "active"); 
	}else{
		$("#user").attr("class", "active"); 
		$("#userLi").attr("class", "active"); 
		type = 1;//没有类型时候，初始化为1
	}
// 	getUser("");//获得用户
	reSelectUser();//回调显示已选用户
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
	getUser(treeNode.id,type);
}

//选择人员、组织、角色
function on_user(typeT){
	type = typeT;
	$("#rangeSelect").html("");
	$("#chooseUser").html("");
}

//获得用户
function getUser(pId,type){
	$("#rangeSelect").html("");
	var callUrl = "";
	if(type==1){
		callUrl = '/urms/user_getUserChildList?orgFrame.id='+pId;
	}else if(type==2){
		callUrl = '/urms/orgFrame_queryOrgFrameList?pIds='+pId;
	}else if(type==3){
		callUrl = '/urms/role_getRoleList?orgFrame.id='+pId;
	}
	$.ajax({
		type:"post",
		async:false,
		dataType : "json",
		url: callUrl,
		success : function(data){
			if(data!=null){
				 for (var i in data) {
					if(type==1){
						$("#rangeSelect").append("<option value='"+data[i].id+"' ondblclick='on_add()'>"+data[i].userName+"</option>");
					}else if(type==2){
						$("#rangeSelect").append("<option value='"+data[i].id+"' ondblclick='on_add()'>"+data[i].orgFrameName+"</option>");
					}else if(type==3){
						$("#rangeSelect").append("<option value='"+data[i].id+"' ondblclick='on_add()'>"+data[i].roleName+"</option>");
					}
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
			if(type==1){
				autoMsg("只能选择一个用户",2);
			}else if(type==2){
				autoMsg("只能选择一个组织",2);
			}else if(type==3){
				autoMsg("只能选择一个角色",2);
			}
			return false;
		}else{
			add_user();
		}
	}else{//多选
		if(so.length>9){//最多只能10个用户
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
	parent.frames[winName].$("#"+nameId).val(names.substring(0,names.length-1));
	parent.frames[winName].$("#"+typeId).val(type);
	parent.layer.close(index);//关闭
}


//回调显示已选用户
function reSelectUser(){
	var ids =parent.frames[winName].$("#"+id).val();
	var names =parent.frames[winName].$("#"+nameId).val();
	if(ids!=null&&ids!=""){
		var idz = ids.split(",");
		var namez = names.split(",");
		for(var i=0;i<idz.length;i++){   
			$("#chooseUser").append("<option value='"+idz[i]+"' ondblclick='on_remove()'>"+namez[i]+"</option>");
		}
	}
}

</script>
</html>