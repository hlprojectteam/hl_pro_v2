<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>数据字典</title>
<!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
</head>
<body>
<div class="ibox-content" style="padding-top: 5px;">
	<div class="row" style="padding:0px 10px 5px 10px;" id="button">
		<button class="btn btn-primary " type="button" onclick="on_add()"><i class="fa fa-check"></i>&nbsp;新增目录</button>
		<button class="btn btn-primary " type="button" onclick="on_del()"><i class="fa fa-remove"></i>&nbsp;删除目录</button>
	</div>
	<div class="row" style="padding:0px 10px 5px 10px;" id="buttonAttr">
		<button class="btn btn-primary " type="button" onclick="on_addAttr()"><i class="fa fa-check"></i>&nbsp;新增字典</button>
		<button class="btn btn-primary " type="button" onclick="on_delAttr()"><i class="fa fa-remove"></i>&nbsp;删除字典</button>
	</div>
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
		  <div class="row">
		    <label class="col-sm-1 control-label">目录名称</label>
		    <div class="col-sm-2">
		      	<input type="text" class="form-control" id="categoryName" name="categoryName" value="" />
		    </div>
		    <label class="col-sm-1 control-label">目录编码</label>
		    <div class="col-sm-2">
		      	<input type="text" class="form-control" id="categoryCode" name="categoryCode" value="" />
		    </div>
			<button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
		  </div>
		</form>
	</div>
	<div class="row ibox-content">
		<div class="col-sm-2">
			<ul id="tree" class="ztree"></ul>
		</div>
		<div class="col-sm-10" id="showGrid">
			<table id="grid"></table>
		</div>
		<div class="col-sm-10" id="showGridAttr">
			<table id="gridAttr"></table>
		</div>
		<input type="hidden" id="pId">
	</div>
</div>
</body>
<script type="text/javascript">
var zTreeObj;
$(document).ready(function(){
	$("#button").show();
	$("#buttonAttr").hide();
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
			contentType:"application/x-www-form-urlencoded",
			url: "/urms/category_loadTree",
			autoParam: ["id"]
		}
	};
//点击树后回调
function zTreeOnClick(event, treeId, treeNode) {
	if(treeNode.categoryType=='2'){
		$("#button").hide();
		$("#buttonAttr").show();
		$("#showGrid").hide();
		$("#showGridAttr").show();
		if(attrSign)
			$("#gridAttr").bootstrapTable("refresh",{url:"/urms/categoryAttr_load?id="+treeNode.id});//加载树下的列表
		else
			loadGridAttr(treeNode.id);//加载树下的列表
	}else{
		$("#button").show();
		$("#buttonAttr").hide();
		$("#showGrid").show();
		$("#showGridAttr").hide();
		$("#grid").bootstrapTable("refresh",{url:"/urms/category_load?id="+treeNode.id});//加载树下的列表
	}
	$("#pId").val(treeNode.id);
}

//加载目录列表
function loadGrid(){
   	$("#grid").bootstrapTable({
   		url: "/urms/category_load",
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
   	    columns: [{checkbox:true},
   	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
	           	  		return index+1;
	              }},
   	              {title: "目录名称",field: "categoryName",align:"center"}, 
   	              {title: "目录编码",field: "categoryCode",align:"center"},
   	              {title: "目录类型", field: "categoryType", width: 250,align:"center",formatter:function(value,row,index){
   	            		return changeDataDictByKey("categoryType",value);
   	              }},
   	  			  {title: "排序", field: "order", width: 100,align:"center"},
   				  {title: "操作", field: "", width: 100,align:"center",formatter:function(value,row,index){
	           	  		return "<a href='#' onclick='on_edit(\""+row.id+"\")'>编辑</a>";
	              }}]
   	 });
}

var attrSign = false;
//加载字典列表
function loadGridAttr(id){
	attrSign  = true;
   	$("#gridAttr").bootstrapTable({
   		url: "/urms/categoryAttr_load?id="+id,
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
   	    columns: [{checkbox:true},
   	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
	           	  		return index+1;
	              }},
   	              {title: "字典键",field: "attrKey",align:"center"}, 
   	              {title: "字典值",field: "attrValue",align:"center"},
   	              {title: "是否默认值",field: "isDefault",width:150,align:"center",formatter:function(value,row,index){
   	            		return changeDataDictByKey("isNot",value);
   	              }},
   	  			  {title: "排序", field: "order", width: 100,align:"center"},
   				  {title: "操作", field: "", width: 100,align:"center",formatter:function(value,row,index){
	           	  		return "<a href='#' onclick='on_editAttr(\""+row.id+"\")'>编辑</a>";
	              }}]
   	 });
}

//新增菜单
function on_add(){
	var pId = $("#pId").val();
		if(pId==""||pId==null){
			autoAlert("请选择一个菜单作为父节点!",5);
		}else{
  		parent.layer.open({
            type: 2,
            title: "新增菜单",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["60%", "80%"],
            content: "/urms/category_edit?pId="+pId
        });
		}
}

//新增字典
function on_addAttr(){
	var pId = $("#pId").val();
	if(pId==""||pId==null){
		autoAlert("请选择一个菜单作为父节点!",5);
	}else{
		parent.layer.open({
        type: 2,
        title: "新增字典",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["60%", "80%"],
        content: "/urms/categoryAttr_edit?category.id="+pId
    });
	}
}

//编辑
function on_edit(id){
	parent.layer.open({
        type: 2,
        title: "编辑菜单",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["60%", "80%"],
        content: "/urms/category_edit?id="+id
    });
}

//编辑字典
function on_editAttr(id){
	parent.layer.open({
        type: 2,
        title: "编辑字典",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["60%", "80%"],
        content: "/urms/categoryAttr_edit?id="+id
    });
}

//删除目录
function on_del(){
	var rows = $("#grid").bootstrapTable("getSelections");
	if(rows.length>0){
		parent.layer.confirm("确定删除所选数据？", {
				btn: ["确定","取消"] //按钮
			}, function(){
			var ids = [];
			var isLeafs = [];
			$.each(rows, function(index, item){
				ids.push(item.id);
				isLeafs.push(item.isLeaf);
			}); 
			if(isLeafs.toString().indexOf('0')>-1){
				$.dialog.alert("该目录含有子菜单，不能删除，请检查！",null);
			}else{
				$.ajax({
					type:'post',
					async:false,
					dataType : 'json',
					contentType:"application/json;charset=utf-8",
					url: '/urms/category_delete?ids='+ids,
					success : function(data){
						if(data.result=="success"){
							$("#grid").bootstrapTable("refresh",{url:"/urms/category_load"});//加载树下的列表
							for (var i = 0; i < ids.length; i++) {
								var node = zTreeObj.getNodeByParam("id", ids[i], null);
								zTreeObj.removeNode(node);
							}
							autoMsg("删除成功！",1);
						}else{
							autoMsg("删除失败！",1);
						}
					},
					error : function(result){
						autoAlert("系统出错",5);
					}
				});
			}
		})
	}else{
		autoAlert("请选择删除的目录",5);
	}
}

//删除字典
function on_delAttr(){
	var rows = $("#gridAttr").bootstrapTable("getSelections");
	if(rows.length>0){
		parent.layer.confirm("确定删除所选数据？", {
				btn: ["确定","取消"] //按钮
			}, function(){
			var ids = [];
			$.each(rows, function(index, item){
				ids.push(item.id);
			}); 
			$.ajax({
				type:'post',
				async:false,
				dataType : 'json',
				contentType:"application/json;charset=utf-8",
				url: '/urms/categoryAttr_delete?ids='+ids,
				success : function(data){
					if(data.result=="success"){
						$("#gridAttr").bootstrapTable("refresh",{url:"/urms/categoryAttr_load?id="+$("#pId").val()});//加载树下的列表
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
		autoAlert("请选择删除的字典",5);
	}
}

//查询
	function on_search(){
		$("#grid").bootstrapTable("refresh");
	}
</script>
</html>