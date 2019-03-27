<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>菜单</title>
</head>
<body>
<div class="ibox-content">
<div class="tab-content ">
<ul class="nav nav-tabs">
    <li class="active"><a data-toggle="tab" href="/urms/menu_edit?id=${menuVo.id}&pId=${menuVo.pId}#form">菜单定义</a>
    </li>
    <li class=""><a data-toggle="tab" href="/urms/menu_listDefinition?id=${menuVo.id}#definition">功能定义</a>
    </li>
</ul>
<div id="form" class="tab-pane active">
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="/urms/menu_save">
<input type="hidden" id="id" name="id" value="${menuVo.id}"/>
<input type="hidden" id="pId" name="pId" value="${menuVo.pId}"/>
<input type="hidden" id="pIds" name="pIds" value="${menuVo.pIds}"/>
<input type="hidden" id="pNames" name="pNames" value="${menuVo.pNames}"/>
<input type="hidden" id="isLeaf" name="isLeaf" value="${menuVo.isLeaf}"/>
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${menuVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"/>
<input type="hidden" id="level" name="level" value="${menuVo.level}"/>
<input type="hidden" id="creatorId" name="creatorId" value="${menuVo.creatorId}"/><%-- 创建人id --%>
<input type="hidden" id="creatorName" name="creatorName" value="${menuVo.creatorName}"/><%-- 创建人 --%>

  <br><br><br>
  <div class="form-group">
    <label class="col-sm-2 control-label"><span style="color: red">*</span>菜单名称</label>
    <div class="col-sm-3">
      <input type="text" class="form-control" id="menuName" name="menuName" value="${menuVo.menuName}" data-rule-required="true" data-rule-rangelength="[2,10]"/>
    </div>
    <label class="col-sm-2 control-label"><span style="color: red">*</span>菜单编码</label>
    <div class="col-sm-3">
      <input type="text" class="form-control" id="menuCode" name="menuCode" value="${menuVo.menuCode}" data-rule-required="true" data-rule-rangelength="[2,50]" data-rule-remote="/urms/menu_checkMenuCode?id=${menuVo.id}" data-msg-remote="菜单编码已经存在，请重新输入">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-2 control-label"><span style="color: red">*</span>菜单类型</label>
    <div class="col-sm-3">
      <opt:select dictKey="menuType" isDefSelect="false" id="menuType" name="menuType" value="${menuVo.menuType }" classStyle="form-control m-b required"/>
    </div>
    <label class="col-sm-2 control-label"><span style="color: red">*</span>排序</label>
    <div class="col-sm-3">
      <input type="text" class="form-control" id="order" name="order" value="${menuVo.order}" data-rule-digits="true" data-rule-required="true">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-2 control-label">图标</label>
    <div class="col-sm-3">
      <input type="text" class="form-control" id="icon" name="icon" value="${menuVo.icon}" onclick="chooseIcon();">
    </div>
    <label class="col-sm-2 control-label">状态</label>
    <div class="col-sm-3">
    	<opt:select dictKey="state" isDefSelect="false" id="state" name="state" value="${menuVo.state }" classStyle="form-control m-b required"/>
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-2 control-label">链接</label>
    <div class="col-sm-8">
      <input type="text" class="form-control" id="url" name="url" value="${menuVo.url}">
    </div>
  </div>
</form>
</div>
<br><br>
<!-- 底部按钮 -->
<div class="footer edit_footer">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_saveFrom()"><i class="fa fa-check"></i>&nbsp;保存</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</div>
<!-- 功能菜单 -->
<div id="definition" class="tab-pane">
<div class="ibox-content">
	<table id="gridDefinition"></table>
	</div>
<!-- 底部按钮 -->
<div class="footer edit_footer">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_addDefinition()"><i class="fa fa-check"></i>&nbsp;增加</button>
<button class="btn btn-danger " type="button" onclick="on_delDefinition()"><i class="fa fa-remove"></i>&nbsp;删除</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</div>
</div>	
</div>
</body>
<script type="text/javascript">
var isUpdate = false;
$(function(){
	if($("#id").val()!=""&&$("#id").val()!=null){
		isUpdate = true;
	}
});
//新增保存更新
function on_saveFrom(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
        on_submit();//提交表单.  
    } 
}

function on_submit(){  
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/urms/menu_saveOrUpdate',
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
				autoMsg("保存成功！",1);
				iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/urms/menu_load"});//加载树下的列表
				var pNode = iframeIndex.zTreeObj.getNodeByParam("id", $("#pId").val(), null);//父节点
				if(isUpdate){//更新
					var node = iframeIndex.zTreeObj.getNodeByParam("id", data.id, null);
					node.name = $("#menuName").val();
					iframeIndex.zTreeObj.updateNode(node);
				}else{
					iframeIndex.zTreeObj.addNodes(pNode, {id:""+data.id+"",name:""+$("#menuName").val()+""});//新增
				}
				$("#id").val(data.id);
				parent.layer.close(index);
			}else{
				autoAlert("保存失败，请检查！",5);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}

//菜单功能点分页
$(document).ready(function(){
  	loadGrid();//加载列表
});

//加载列表
function loadGrid() {
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
	    columns: [{checkbox:true},
	              {title: "功能名称",field: "definitionName",align:"center",formatter:function(value,row,index){
	            	  return "<button class='btn "+row.colour+"' style='margin-left: 5px;' type='button'><i class='fa fa-"+row.icon+"'></i>&nbsp;"+value+"</button>"
	              }}, 
	              {title: "功能编码",field: "definitionCode",align:"center"},
	              {title: "方法/URL", field: "definitionMethod", width: 150,align:"center"},
	              {title: "颜色", field: "colour",align:"center"},
	              {title: "类型", field: "pageType",align:"center"},
	  			  {title: "排序", field: "order", width: 80,align:"center"},
				  {title: "操作", field: "", width: 100,align:"center",formatter:function(value,row,index){
	           	  		return "<a href='#' onclick='on_editDefinition(\""+row.id+"\")'>编辑</a>";
	              }}]
	 });
}

//新增菜单功能点
function on_addDefinition(){
	parent.layer.open({
        type: 2,
        title: "菜单功能点定义",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["60%", "80%"],
        content: "/urms/menu_editDefinition?menu.id=${menuVo.id}"
    });
}

//编辑菜单功能点
function on_editDefinition(id){
	parent.layer.open({
        type: 2,
        title: "菜单功能点定义",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["60%", "80%"],
        content: "/urms/menu_editDefinition?id="+id
    });
}
//删除菜单功能点
function on_delDefinition(){
	var rows = $("#gridDefinition").bootstrapTable("getSelections");
		if(rows.length>0){
			parent.layer.confirm("确定删除所选数据？", {
				btn: ["确定","取消"] //按钮
			}, function(){
	  		var ids = [];
			$.each(rows, function(index, item){
				ids.push(item.id);
			});
			$.ajax({
				type:"post",
				async:false,
				dataType : "json",
				url: '/urms/menu_deleteDefinition?ids='+ids,
				success : function(data){
					if(data.result=="success"){
						$("#gridDefinition").bootstrapTable("refresh",{url:"/urms/menu_loadDefinition"});//加载树下的列表
						autoMsg("删除成功！",1);
					}else{
						autoMsg("删除失败！",1);
					}
				},
				error : function(result){
					autoAlert("系统出错",5);
				}
			});
			});
	}else{
		autoAlert("请选择删除的菜单功能点",5);
	}
}
//新增菜单-选择图标
function chooseIcon(){
	var menuName = $("#menuName").val();
	parent.layer.open({
        type: 2,
        title: "为 "+menuName+" 菜单选择图标",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["85%", "80%"],
        content: "/menu_Icons?winName="+window.name  //写在了 LoginController.java 最下面的方法
    });
}
</script>
</html>