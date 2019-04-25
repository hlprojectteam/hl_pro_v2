<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>党建活动开展列表</title>
        <!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
</head>
<body>
<div class="ibox-content" style="padding-top:5px;">
	 <!-- 工具条 -->
	<menu:definition menuCode="${menuCode }"/>
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
		  <div class="row">
		  	<label class="col-sm-1 control-label">党支部</label>
		    <div class="col-sm-2">
		      	 <select id="branchSelect" name="branchId" value='${activitiesLaunchVo.branchId}' class="form-control m-b required"></select>
		    </div>
		  	<label class="col-sm-1 control-label">年份</label>
		    <div class="col-sm-2">
		      	<%-- <input type="text" class="form-control" id="year" name="year"  value='${activitiesLaunchVo.year}'/>     --%>
		    
		      	 <input type="text" class="form-control" id="year" name="year"  value='${activitiesLaunchVo.year}'  onfocus="this.blur()" data-rule-required="true" onclick="WdatePicker({dateFmt:'yyyy'})"/>    
		     </div>
			<button class="btn btn-primary" type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
			&nbsp&nbsp&nbsp&nbsp<button class="btn btn-danger" type="button" onclick="on_collect()"><i class="fa fa-search"></i>&nbsp;查看汇总</button>
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
	<!-- 获得节点id作为新增节点orgFrameId -->
	<input type="hidden" id="orgFrameId">
</div>
<script type="text/javascript"> 
	var zTreeObj;
	var treeNodeId;
	$(document).ready(function(){
		zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
		loadGrid();//加载列表
		getBranch();//加载党支部下拉
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
				url: "/dangjian/activities_loadTree",
				autoParam: ["id"]
			}
		};
	
	//点击树后回调
	function zTreeOnClick(event, treeId, treeNode) {
		//$("#pId").val(treeNode.id);
		treeNodeId=treeNode.id;
		if(check()){
			$("#grid").bootstrapTable("refresh",{url:"/dangjian/activitiesLauch_load?activityId="+treeNode.id});//加载树下的列表
		}
	}
    //加载列表
    function loadGrid() {
    	$("#grid").bootstrapTable({
    		url: "/dangjian/activitiesLauch_load",
    		dataType:"json",
    		method:"post",
    		queryParamsType: "limit",//服务器分页必须
    		striped:true,//条纹行
    		contentType: "application/x-www-form-urlencoded",
    		pageSize:12,//每页大小
    		pageNumber:1,//开始页
    		pageList:[10,20,50],
    		pagination:true,//显示分页工具栏
    		sidePagination: "server", //服务端请求
    		queryParams: queryParams,//发送请求参数
    	    columns: [{checkbox:true},
    	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
		           	  		return index+1;
		              }},
    	              {title: "活动标题",field: "title",width: 100,align:"center"}, 
    	              {title: "频率", field: "frequency", width: 80,align:"center",formatter:function(value,row,index){
		           	  		return changeDataDictByKey("dj_activities_frequency",value);
		              }},
		              {title: "时间段",field: "timeQuantum",width: 80,align:"center"}, 
		              {title: "开展次数",field: "completionTimes",width: 50,align:"center"}, 
		              {title: "是否达标", field: "isReach", width: 50,align:"center",formatter:function(value,row,index){
		           	  		return  reachStandardHtml(changeDataDictByKey("isNot",value));
		              }},
    				  {title: "操作", field: "", width: 60,align:"center",formatter:function(value,row,index){
    					  var json = JSON.stringify(row);
    					  var tb = table_button.split("&nbsp;&nbsp;");
    					  var html = "";
    					  for (var i = 0; i < tb.length; i++) {
    							  html+=tb[i]+"&nbsp;&nbsp;";
						  }
    					  return html.replace(new RegExp("row_id_","gm"),""+json+"");
		              }}]
    	 });
    }
    
    //获得党支部下拉选项
    function getBranch(){
    	$.ajax({
    		type:'post',
    		async:false,
    		dataType : 'json',
    		url: '/dangjian/branch_loadAll',
    		success : function(data){
    			$("#branchSelect").append("<option value=''>------请选择------</option>");
    			if(data.rows!=null){
    				/*for (var i in data.rows) {
    				 	 if(sysCode==data[i].sysCode)
    				 		$("#branchSelect").append("<option value='"+data[i].id+"' selected='selected'>"+data[i].branchName+"</option>");
    				 	else
    				 		$("#branchSelect").append("<option value='"+data[i].id+"'>"+data[i].branchName+"</option>"); 
    					
    				 }*/
    				 
    				 for (var i = 0; i < data.rows.length; i++) {
    					$("#branchSelect").append("<option value='"+data.rows[i].id+"'>"+data.rows[i].branchName+"</option>");
    				 }
    			}
    		},
    	});
    }
  	
  	//编辑
  	function on_edit(obj){
  		parent.layer.open({
            type: 2,
            title: "编辑活动",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["80%", "80%"],
            content: "/dangjian/activities_edit?id="+obj.id
        });
  	}
  	
  	function check(){
		if($("#branchSelect").val()==''||$("#branchSelect").val()==undefined){
			alert('请选择党支部');
			return false;
		}
		if($("#year").val()==''||$("#year").val()==undefined){
			alert('请选择年份');
			return false;
		}
		return true;
	}
  	
	//查询
	function on_search(){
		if(check()){
			if(treeNodeId==''||treeNodeId==undefined){
				alert('请选择活动');
			}else{
				$("#grid").bootstrapTable("refresh");
			}
		}
	}
	
	//构造达标字段样式
	function reachStandardHtml(value){
		if(value=='否'){
			return '<span style="color: red">'+value+'</span>';
		}else if(value=='是'){
			return '<span style="color: green">'+value+'</span>';
		}
		return value;
	}
	
	//打开汇总页面
	function on_collect(){
		if(check()){
			parent.layer.open({
	            type: 2,
	            title: "党建活动汇总",
	            shadeClose: true,//打开遮蔽
	            shade: 0.6, 
	            maxmin: true, //开启最大化最小化按钮
	            area: ["98%", "95%"],
	            content: "/dangjian/activitiesLauch_collect?branchId="+$("#branchSelect").val()+"&&year="+$("#year").val()
	        });
			
		}
	}

</script>
</body>
</html>