<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
	<title>用户权限</title>
</head>
<!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
<body>
<div class="ibox-content" style="padding-top: 5px;">
	<div class="row ibox-content">
		<div class="col-sm-3">
			<ul id="tree" class="ztree"></ul>
		</div>
		<div class="col-sm-3">
			<table id="grid"></table>
		</div>
		<div class="col-sm-3">
			<ul id="menuTree" class="ztree" style="overflow: auto;"></ul>
			<div class="pull-right">
				<button class="btn btn-primary " type="button" onclick="on_save()" style="display: none;margin-top: 5px;" id="menuSaveButton"><i class="fa fa-check"></i>&nbsp;保存角色菜单权限</button>
				<button class="btn btn-danger " type="button" onclick="on_clean()" style="display: none;margin-top: 5px;" id="menuCleanButton"><i class="fa fa-eraser"></i>&nbsp;清除</button>
			</div>
		</div>
		<div class="col-sm-3">
			<table id="gridDefinition"></table>
			<div class="pull-right">
				<button class="btn btn-primary " type="button" onclick="on_saveDef()" style="display: none;margin-top: 5px;" id="defSaveButton"><i class="fa fa-check"></i>&nbsp;保存功能点</button>
				<button class="btn btn-danger " type="button" onclick="on_cleanDef()" style="display: none;margin-top: 5px;" id="defCleanButton"><i class="fa fa-eraser"></i>&nbsp;清除功能点</button>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
var zTreeObj;
$(document).ready(function(){
	zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
	loadGrid();//加载列表
	$("#menuTree").css("height",document.body.clientHeight-100);
});

var setting = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			onClick: zTreeOnClickRoleTree
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
function zTreeOnClickRoleTree(event, treeId, treeNode) {
	$("#orgFrameId").val(treeNode.id);
	$("#grid").bootstrapTable("refresh",{url:"/urms/role_load?orgFrame.id="+treeNode.id});//加载树下的列表
}

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
		onClickRow:getMenu,
	    columns: [{title: "角色名称",field: "roleName",align:"center",formatter:function(value,row,index){
	    	return "<span style='cursor:pointer'>"+value+"</span>";
	    }}]
	 });
}

var zTreeMenuObj;
var roleId;
var sysCode;
//点击角色 获得菜单列表的权限
function getMenu(row,tr){
	roleId = row.id;
	sysCode = row.sysCode;
	tr.css("backgroundColor","#DDDDDD");
	tr.siblings().each(function(){
		$(this).css("backgroundColor","white");
	});
	zTreeMenuObj = $.fn.zTree.init($("#menuTree"), settingMenu);//初始化ztree
	$("#menuSaveButton").show();
	$("#menuCleanButton").show();
}

var settingMenu = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			onClick: zTreeOnClick,
			beforeAsync: zTreeBeforeAsync
		},
		async: {//异步加载
			enable: true,
			type: "post",
			dataType: "text",
			contentType:"application/x-www-form-urlencoded",
			url: "/urms/roleRight_menuTree",
			autoParam: ["id"]
		},
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "ps", "N": "ps" }
		}
	};
	
//点击树后回调
var menuId = "";
function zTreeOnClick(event, treeId, treeNode) {
	menuId = treeNode.id;
	$("#gridDefinition").bootstrapTable("refresh",{url:"/urms/menu_loadDefinition?id="+treeNode.id});//加载树下的列表
}

//异步调用前促发
function zTreeBeforeAsync(treeId, treeNode){
	loadGridDefinition();//初始化
	$.fn.zTree.getZTreeObj("menuTree").setting.async.url = "/urms/roleRight_menuTree?roleId="+roleId+"&sysCode="+sysCode;
	$("#gridDefinition").bootstrapTable("refresh",{url:"/urms/menu_loadDefinition?id=1"});//加载树下的列表
}

//保存角色菜单权限
function on_save(){
	var nodes = zTreeMenuObj.getCheckedNodes(true);
	if(nodes.length==0){
		autoAlert("请选择菜单！",5);
	}else{
		var menuIds = "";
		for(var i=0;i<nodes.length;i++){
			menuIds += nodes[i].id + ",";//获取选中节点的值
	    }
		var menuIdsZ = menuIds.substring(0, menuIds.length-1);
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/roleRight_saveOrUpdate?menuIds='+menuIdsZ+'&roleId='+roleId+'&sysCode='+sysCode,
			success : function(data){
				if(data.result){
					autoMsg("保存成功！",1);
				}else{
					autoAlert("保存失败，请检查！",5);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}	
}

//清除菜单角色权限
function on_clean(){
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/urms/roleRight_clean?roleId='+roleId+'&sysCode='+sysCode,
		success : function(data){
			if(data.result){
				zTreeMenuObj.checkAllNodes(false);//取消所有勾选
				autoMsg("清除成功！",1);
			}else{
				autoAlert("清除失败，请检查！",5);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}

//加载列表
function loadGridDefinition() {
	$("#defSaveButton").show();//显示按钮
	$("#defCleanButton").show();//显示按钮
	$("#gridDefinition").bootstrapTable({
		url: "/urms/menu_loadDefinition",
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
		onLoadSuccess:checked,//成功加载后触发
	    columns: [{checkbox:true},
	              {title: "ID",field: "id",align:"center",visible:false},
	              {title: "功能名称",field: "definitionName",align:"center"}]
	 });
}

//已经选择的功能点
function checked(){
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'text',
		url: '/urms/checkRoleMenuDefinition?roleId='+roleId,
		success : function(data){
		 	$("#gridDefinition").bootstrapTable("checkBy", {field:"id", values:data.split(',')});
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}

//保存功能点
function on_saveDef(){
	var rows = $("#gridDefinition").bootstrapTable("getSelections");
	if(rows.length>0){
		var ids = [];
		$.each(rows, function(index, item){
			ids.push(item.id);
		});
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/roleMenuDefinition_saveOrUpdate?menuDefinitionIds='+ids+'&roleId='+roleId+'&menuId='+menuId,
			success : function(data){
				if(data.result){
					autoMsg("保存功能点成功！",1);
				}else{
					autoAlert("保存功能点失败，请检查！",5);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}else{
		autoAlert("请选择菜单功能点",5);
	}
}

//清除功能点
function on_cleanDef(){
	var rows = $("#gridDefinition").bootstrapTable("getData");//返回所选行
	var ids = [];
	$.each(rows, function(index, item){
		ids.push(item.id);
	});
	if(ids!=null&&ids!=""){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/urms/roleMenuDefinition_claen?roleId='+roleId+'&menuDefinitionIds='+ids,
			success : function(data){
				if(data.result){
					$("#gridDefinition").bootstrapTable("uncheckAll");
					autoMsg("清除功能点成功！",1);
				}else{
					autoAlert("清除功能点失败，请检查！",5);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}
}
</script>
</html>