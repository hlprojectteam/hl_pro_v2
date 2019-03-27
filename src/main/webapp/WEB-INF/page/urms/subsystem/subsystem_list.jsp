<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>系统定义列表</title>
</head>
<!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
<body>
<div class="ibox-content" style="padding-top: 5px;">
	<!-- 工具条 -->
	<menu:definition menuCode="${menuCode }"/>
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
		  <div class="row">
		    <label class="col-sm-1 control-label">子系统名称</label>
		    <div class="col-sm-2">
		      	<input type="text" class="form-control" id="sysName" name="sysName" value="" />
		    </div>
		    <label class="col-sm-1 control-label">子系统编码</label>
		    <div class="col-sm-2">
		      	<input type="text" class="form-control" id="sysCode" name="sysCode" value="" />
		    </div>
			<button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
		  </div>
		</form>
	</div>
	<div class="row ibox-content">
		<div class="col-sm-8">
			<table id="grid"></table>
		</div>
		<div class="col-sm-4" id="menuShow" style="display: none;">
			<ul id="menuTree" class="ztree" style="overflow: auto;"></ul>
		<!-- 底部按钮 -->
			<div class="pull-right">
			</br>
			<button class="btn btn-primary " type="button" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;保存</button>
			</div>
		</div>
	</div>
	<input type="hidden" id="pId">
</div>
<script type="text/javascript"> 
    $(document).ready(function(){
    	loadGrid();//加载列表
    	$("#menuTree").css("height",document.body.clientHeight-190);//菜单高度
    });
    //加载列表
    function loadGrid() {
    	$("#grid").bootstrapTable({
    		url: "/urms/subsystem_load",
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
    	    columns: [{checkbox:true},
    	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
		           	  		return index+1;
		              }},
    	              {title: "子系统名称",field: "sysName",align:"center"}, 
    	              {title: "子系统编码",field: "sysCode",align:"center"},
    	              {title: "系统状态",field: "state",align:"center",formatter:function(value,row,index){
    	            	  return changeDataDictByKey("state",value);
    	              }},
    				  {title: "操作", field: "", width: 100,align:"center",formatter:function(value,row,index){
		           	  		return "<a href='#' onclick='on_edit(\""+row.id+"\")'>编辑</a>";
		              }}]
    	 });
    }
    
  	//新增菜单
  	function on_add(){
  		parent.layer.open({
            type: 2,
            title: "新增子系统",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["60%", "80%"],
            content: "/urms/subsystem_edit?pId="+pId
        });
  	}
  	
  	//删除菜单
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
					url: '/urms/subsystem_delete?ids='+ids,
					success : function(data){
						if(data.result=="success"){
							$("#grid").bootstrapTable("refresh",{url:"/urms/subsystem_load"});//加载树下的列表
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
			autoAlert("请选择删除的子系统",5);
		}
  	}
  	
  	//编辑菜单
  	function on_edit(id){
  		parent.layer.open({
            type: 2,
            title: "编辑子系统",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["60%", "80%"],
            content: "/urms/subsystem_edit?id="+id
        });
  	}
  	
//查询
function on_search(){
	$("#grid").bootstrapTable("refresh");
}

var zTreeMenuObj;
var subsystemId;
//点击角色 获得菜单列表的权限
function getMenu(row,tr){
	subsystemId = row.id;
	tr.css("backgroundColor","#DDDDDD");
	tr.siblings().each(function(){
		$(this).css("backgroundColor","white");
	});
	$("#menuShow").show();
	zTreeMenuObj = $.fn.zTree.init($("#menuTree"), settingMenu);//初始化ztree
}
  
var settingMenu = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			onAsyncSuccess: zTreeOnAsyncSuccess,  //异步加载成功回调
			beforeAsync: zTreeBeforeAsync
		},
		async: {//异步加载
			enable: true,
			type: "post",
			dataType: "text",
			contentType:"application/x-www-form-urlencoded",
			url: "/urms/menu_loadTreeSys",
			autoParam: ["id"]
		},
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType: { "Y": "ps", "N": "ps" }
		}
	};
 	
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg){
	expandNodes(zTreeMenuObj.getNodes());//异步全部打开
}

//异步调用前促发
function zTreeBeforeAsync(treeId, treeNode){
	$.fn.zTree.getZTreeObj("menuTree").setting.async.url = "/urms/menu_loadTreeSys?subsystemId="+subsystemId;
}

function expandNodes(nodes) {
	if (!nodes) return;
	for (var i=0, l=nodes.length; i<l; i++) {
		zTreeMenuObj.expandNode(nodes[i], true, false, false);
		if (nodes[i].isParent && nodes[i].zAsync) {
			expandNodes(nodes[i].children);
		}
	}
}
 	
//保存关联菜单
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
			url: '/urms/subsystem_saveOrUpdateMenu?menuIds='+menuIdsZ+'&subsystemId='+subsystemId,
			success : function(result){
				if(result.result){
					autoMsg("保存成功！",1);
				}else{
					autoAlert("保存失败，请检查！",5);
				};
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	};	
};
</script>
</body>
</html>