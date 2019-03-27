<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>数据字典列表</title>
</head>
<body>
<div class="ibox-content" style="padding-top: 5px;">
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
		  <div class="row">
		    <label class="col-sm-2 control-label">字典名称</label>
		    <div class="col-sm-3">
		      	<input type="text" class="form-control" id="categoryName" name="categoryName" value="" />
		    </div>
		    <label class="col-sm-2 control-label">字典编码</label>
		    <div class="col-sm-3">
		      	<input type="text" class="form-control" id="categoryCode" name="categoryCode" value="" />
		    </div>
			<button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
		  </div>
		</form>
	</div>
	<div class="row ibox-content">
		<div class="col-sm-12" id="showGrid">
			<table id="grid"></table>
		</div>
	</div>
</div>
<%-- 底部按钮 --%>
<div class="footer edit_footer">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;保存</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script type="text/javascript">
var winName = '${winName}';
$(document).ready(function(){
	loadGrid();//加载列表
});

//加载目录列表
function loadGrid(){
   	$("#grid").bootstrapTable({
   		url: "/urms/dataCategory_load",
   		dataType:"json",
   		method:"post",
   		"queryParamsType": "limit",//服务器分页必须
   		striped:true,//条纹行
   		contentType: "application/x-www-form-urlencoded",
   		pageSize:10,//每页大小
   		pageNumber:1,//开始页
   		pageList:[10,20,50],
   		pagination:true,//显示分页工具栏
   		sidePagination: "server", //服务端请求
   		queryParams: queryParams,//发送请求参数
   		onClickRow:getDict,
   	    columns: [{title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
	           	  		return index+1;
	              }},
   	              {title: "字典名称",field: "categoryName",align:"center"}, 
   	              {title: "字典编码",field: "categoryCode",align:"center"}]
   	 });
}

//数据字典选择
var categoryCode;
var categoryName;
//点击角色 获得菜单列表的权限
function getDict(row,tr){
	categoryName = row.categoryName;
	categoryCode = row.categoryCode;
	tr.css("backgroundColor","#DDDDDD");
	tr.siblings().each(function(){
		$(this).css("backgroundColor","white");
	});
}

function on_save(){
	 parent.frames[winName].$("#categoryName").val(categoryName);
	 parent.frames[winName].$("#categoryCode").val(categoryCode);
	 parent.layer.close(index);//关闭
}

//查询
function on_search(){
	$("#grid").bootstrapTable("refresh");
}
</script>
</html>