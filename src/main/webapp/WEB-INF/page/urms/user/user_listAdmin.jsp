<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
	<title>用户列表</title>
</head>
<!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
<body>
<div class="page-container">
	<div class="page page-full animation-fade">
		<%-- <div class="page-aside">
			<div class="page-aside-inner height-full" data-plugin="slimScroll">
				<section class="page-aside-section">
					<h5 class="page-aside-title">系统列表</h5>
					<div class="list-group">
						<c:forEach items="${subsystems}" var="subsystem" varStatus="vstatus">
							<a class="list-group-item" href="javascript:;" onclick="loadData('${subsystem.sysCode}','${subsystem.sysName}');">
									${subsystem.sysName}
									${fns:getDictValue("state",subsystem.state)}
							</a>
						</c:forEach>
					</div>
				</section>
			</div>

		</div> --%>
		<div class="page-main">
			<div class="ibox-content" style="padding-top: 5px;">
				<!-- 工具条 -->
				<menu:definition menuCode="${menuCode }"/>
				<div class="row ibox-content">
					<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
						<div class="row">
							<label class="col-sm-1 control-label">登录名</label>
							<div class="col-sm-2">
								<input type="text" class="form-control" id="loginName" name="loginName" value="" />
							</div>
							<label class="col-sm-1 control-label">姓名</label>
							<div class="col-sm-2">
								<input type="text" class="form-control" id="userName" name="userName" value="" />
							</div>
							<button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
						</div>
					</form>
				</div>
				<div class="row ibox-content">
					<div class="col-sm-3">
						<ul id="tree" class="ztree"></ul>
					</div>
					<div class="col-sm-9">
						<table id="grid"></table>
					</div>
				</div>
				<!-- 获得节点id作为新增节点orgFrameId -->
				<input type="hidden" id="orgFrameId">
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="sysCode">
<input type="hidden" id="systemName">

</body>
<script type="text/javascript">
var zTreeObj;
var sysCode1="";
$(document).ready(function(){
	zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
	loadGrid();//加载列表
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
            otherParam:{"sysCode":function(){return sysCode1}},
			contentType:"application/x-www-form-urlencoded",
			url: "/urms/orgFrame_loadTreeBySysCode",
			autoParam: ["id"]
		}
	};

//点击树后回调
function zTreeOnClick(event, treeId, treeNode) {
	$("#orgFrameId").val(treeNode.id);
	$("#grid").bootstrapTable("refresh",{url:"/urms/user_load?sign=2&orgFrame.id="+treeNode.id});//加载树下的列表
}

function loadData(sysCode,systemName){
    if (typeof(sysCode) == "undefined") sysCode ="";
    sysCode1 = sysCode;
    $("#sysCode").val(sysCode);
    $("#systemName").val(systemName);
    zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
    $("#grid").bootstrapTable("refresh",{url:"/urms/user_load?sign=2&sysCode="+sysCode});
}

//加载列表
function loadGrid() {
	$("#grid").bootstrapTable({
		url: "/urms/user_load?sign=2",
		dataType:"json",
		method:"post",
		queryParamsType: "limit",//服务器分页必须
		striped:true,//条纹行
		contentType: "application/x-www-form-urlencoded",
		pageSize:20,//每页大小
		pageNumber:1,//开始页
		pageList:[10,20,50],
		pagination:true,//显示分页工具栏
		sidePagination: "server", //服务端请求
		queryParams: queryParams,//发送请求参数
	    columns: [{checkbox:true},
	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
	        	  		return index+1;
	              }},
	              {title: "登录名",field: "loginName",width:120,align:"center"}, 
	              {title: "姓名",field: "userName",width:120,align:"center"},
	              {title: "工号",field: "jobNumber",width:50,align:"center"},
	              {title: "所属组织",field: "orgFrameNames",align:"center"},
	              {title: "状态", field: "state", width: 80,align:"center",formatter:function(value,row,index){
	            		return changeDataDictByKey("state",value);
 	              }},
				  {title: "操作", field: "", width: 150,align:"center",formatter:function(value,row,index){
					  var html = "<a href='#' onclick='on_edit(\""+row.id+"\")'>编辑</a>";
				      html +=  "&nbsp;&nbsp;&nbsp;&nbsp;";
					  html +=  "<a href='#' onclick='relation_role(\""+row.id+"\")'>关联角色</a>";
        	  	 	  return html;
	           }}]
	 });
}

//新增用户
function on_add(){
	var orgFrameId = $("#orgFrameId").val();
	if(orgFrameId==""||orgFrameId==null){
			autoAlert("请选择一个菜单作为父节点!",5);
	}else{
		parent.layer.open({
          type: 2,
          title: "新增用户",
          shadeClose: true,//打开遮蔽
          shade: 0.6, 
          maxmin: true, //开启最大化最小化按钮
          area: ["80%", "80%"],
          content: "/urms/user_edit?orgFrame.id="+orgFrameId
      });
	}
}

//编辑用户
function on_edit(id){
	parent.layer.open({
        type: 2,
        title: "编辑用户",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["80%", "80%"],
        content: "/urms/user_edit?id="+id
    });
}

//删除用户
function on_del(){
	//列表tableId,删除请求url,刷新列表url
	delete_tableData("grid","/urms/user_delete","/urms/user_load?sign=2");
}

//关联角色
function relation_role(id){
	parent.layer.open({
        type: 2,
        title: "关联角色",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["50%", "50%"],
        content: "/urms/user_relationRole?id="+id
    });
}

//查询
function on_search(){
	$("#grid").bootstrapTable("refresh");
}
</script>
</html>