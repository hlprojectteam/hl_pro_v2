<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>菜单列表</title>
    <!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
</head>
<body>
<div class="ibox-content" style="padding-top: 5px;">
	<!-- 工具条 -->
	<menu:definition menuCode="${menuCode }"/>
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
		  <div class="row">
		    <label class="col-sm-1 control-label">菜单名称</label>
		    <div class="col-sm-2">
		      	<input type="text" class="form-control" id="menuName" name="menuName" value="" />
		    </div>
		    <label class="col-sm-1 control-label">菜单编码</label>
		    <div class="col-sm-2">
		      	<input type="text" class="form-control" id="menuCode" name="menuCode" value="" />
		    </div>
		    <label class="col-sm-1 control-label">菜单等级</label>
		    <div class="col-sm-2">
		      	<select name="level" id="level" class="form-control">
		      		<option value="">请选择</option>
		      		<option value="1">一级菜单</option>
		      		<option value="2">二级菜单</option>
		      		<option value="3">三级菜单</option>
		      	</select>
		    </div>
			<button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
		  </div>
		</form>
	</div>
	<div class="row ibox-content">
		<div class="col-sm-2">
			<ul id="tree" class="ztree"></ul>
		</div>
		<div class="col-sm-10">
			<table id="grid"></table>
		</div>
	</div>
	<input type="hidden" id="pId">
</div>
<script type="text/javascript"> 
    var zTreeObj;
    $(document).ready(function(){
    	zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
    	loadGrid();//加载列表
    });
    //初始化树
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
    			url: "/urms/menu_loadTree",
    			autoParam: ["id"]
    		}
    	};

    //点击树后回调
    function zTreeOnClick(event, treeId, treeNode) {
    	$("#pId").val(treeNode.id);
    	$("#grid").bootstrapTable("refresh",{url:"/urms/menu_load?id="+treeNode.id});//加载树下的列表
    }
    //加载列表
    function loadGrid() {
    	$("#grid").bootstrapTable({
    		url: "/urms/menu_load",
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
    	              {title: "菜单名称",field: "menuName",width:120,align:"center"}, 
    	              {title: "菜单编码",field: "menuCode",width:150,align:"center"},
    	              {title: "链接", field: "url",align:"center",formatter:function(value,row,index){
		           	  		return "<span title='"+value+"'>"+value+"</span>";
		           	  }},
    	              {title: "父亲节点", field: "pNames",align:"center",formatter:function(value,row,index){
		           	  		return "<span title='"+value+"'>"+value+"</span>";
		           	  }},
    	  			  {title: "排序", field: "order", width: 80,align:"center"},
    	  			  {title: "状态", field: "state", width: 80,align:"center",formatter:function(value,row,index){
    	  				    var showCode = changeDataDictByKey("state",value);
    	  				    if(showCode=='正常'){
	    	  				    return "<i class=\"fa fa-circle\" style=\"color: #46BE8A\"></i> <font color=green>正常</font>";
						    }else if(showCode=='停用'){
	                        	return "<i class=\"fa fa-circle\" style=\"color: #a8bbc2\"></i> 停用";
						    }
       	              }},
    				  {title: "操作", field: "", width: 100,align:"center",formatter:function(value,row,index){
		           	  		return "<a href='#' onclick='on_edit(\""+row.id+"\")'>编辑</a>";
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
	            content: "/urms/menu_edit?pId="+pId
	        });
  		}
  	}
  	
  	//删除菜单
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
					autoAlert("该菜单含有子菜单，不能删除，请检查!",5);
				}else{
					$.ajax({
						type:"post",
						async:false,
						dataType : "json",
						url: '/urms/menu_deleteTree?ids='+ids,
						success : function(data){
							if(data.result=="success"){
								$("#grid").bootstrapTable("refresh",{url:"/urms/menu_load"});//加载树下的列表
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
  			});
		}else{
			autoAlert("请选择删除的菜单",5);
		}
  	}
  	
  	//编辑菜单
  	function on_edit(id){
  		parent.layer.open({
            type: 2,
            title: "编辑菜单",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["60%", "80%"],
            content: "/urms/menu_edit?id="+id
        });
  	}
  	
  //查询
  	function on_search(){
  		$("#grid").bootstrapTable("refresh");
  	}
</script>
</body>
</html>