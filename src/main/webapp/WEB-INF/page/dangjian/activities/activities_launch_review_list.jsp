<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>党建活动评审列表</title>
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
		      	<input type="text" class="form-control" id="year" name="year" value="<fmt:formatDate value='${activitiesLaunchVo.year}' pattern='yyyy'/>" onfocus="this.blur()" data-rule-required="true" onclick="WdatePicker({dateFmt:'yyyy'})"/>    
		    </div>
			<button class="btn btn-primary" type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
		  </div>
		</form>
	</div>
	
	<div class="row ibox-content">
		<div class="col-sm-12">
			<table id="grid"></table>
		</div>
	</div>
	<!-- 获得节点id作为新增节点orgFrameId -->
	<input type="hidden" id="orgFrameId">
</div>
<script type="text/javascript"> 
	$(document).ready(function(){
		loadGrid();//加载列表
		getBranch();//加载党支部下拉
	});
	
    //加载列表
    function loadGrid() {
    	$("#grid").bootstrapTable({
    		url: "/dangjian/activitiesLauch_reviewload",
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
    	              {title: "活动标题",field: "title",width: 100,align:"center"}, 
		              {title: "开展时间",field: "launchDate",width: 100,align:"center"}, 
		              {title: "积分",field: "points",width: 50,align:"center"}, 
		              {title: "上报人",field: "creatorName",width: 50,align:"center"}, 
		              {title: "上报支部",field: "branchName",width: 50,align:"center"}, 
		              {title: "评审状态", field: "status", width: 50,align:"center",formatter:function(value,row,index){
		           	  		return changeDataDictByKey("dj_activity_status",value);
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
  	
  	//评审
  	function on_edit(obj){
  		parent.layer.open({
            type: 2,
            title: "评审活动",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["95%", "95%"],
            content: "/dangjian/activitiesLauch_review?id="+obj.id
        });
  	}
  	
  	
	//查询
	function on_search(){
		$("#grid").bootstrapTable("refresh");
	}
	
	
	

</script>
</body>
</html>