<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fns" uri="/WEB-INF/taglib/dict.tld"%>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
	<title>管理员列表</title>
</head>
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
					<div class="col-sm-12">
						<table id="grid"></table>
					</div>
				</div>
				<!-- 获得节点id作为新增节点pid -->
				<input type="hidden" id="pId">
			</div>
		</div>
	</div>
</div>
<input type="hidden" id="sysCode">
<input type="hidden" id="systemName">
</body>
<script type="text/javascript">
$(document).ready(function(){
	loadGrid();//加载列表
});

function loadData(sysCode,systemName){
    if (typeof(sysCode) == "undefined") sysCode ="";
    $("#sysCode").val(sysCode);
    $("#systemName").val(systemName);
    $("#grid").bootstrapTable("refresh",{url:"/urms/user_load?sign=1&sysCode="+sysCode});
}

function loadGrid() {
	$("#grid").bootstrapTable({
		url: "/urms/user_load?sign=1",
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
	              {title: "登录名",field: "loginName",align:"center"}, 
	              {title: "姓名",field: "userName",align:"center"},
	              {title: "所属子系统",field: "sysCode",align:"center",width:250,formatter:function(value,row,index){
	            	  return changeSubsystem(value);
	              }},
	              {title: "状态",field: "state",align:"center",formatter:function(value,row,index){
	            	  return changeDataDictByKey("state",value);
	              }},
				  {title: "操作", field: "", width: 150,align:"center",formatter:function(value,row,index){
					  var html = "<a href='#' onclick='on_edit(\""+row.id+"\")'>编辑</a>";
        	  	 	  return html;
	           }}]
	 });
}

//新增用户
function on_add(){
    var sysCode = $("#sysCode").val();
    var systemName = $("#systemName").val();
    if(sysCode==""||sysCode==null){
        autoAlert("请选择一个子系统!",5);
    }else {
        parent.layer.open({
            type: 2,
            title: systemName+"新增子系统管理员",
            shadeClose: true,//打开遮蔽
            shade: 0.6,
            maxmin: true, //开启最大化最小化按钮
            area: ["95%", "95%"],
            content: "/urms/subsystemAdmin_edit?sysCode="+sysCode
        });
    }
}

//编辑用户
function on_edit(id){
	parent.layer.open({
        type: 2,
        title: "编辑子系统管理员",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["95%", "95%"],
        content: "/urms/subsystemAdmin_edit?id="+id
    });
}

//删除用户
function on_del(){
	var rows = $("#grid").bootstrapTable("getSelections");
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
				url: '/urms/user_delete?ids='+ids,
				success : function(data){
					if(data.result){
						$("#grid").bootstrapTable("refresh",{url:"/urms/user_load?sign=1"});//加载树下的列表
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
		autoAlert("请选择删除的角色",5);
	}
}

//查询
function on_search(){
	$("#grid").bootstrapTable("refresh");
}
</script>
</html>