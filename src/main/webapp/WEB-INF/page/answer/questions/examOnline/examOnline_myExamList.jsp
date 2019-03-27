<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>考试列表</title>
</head>
<body>
<div class="ibox-content" style="padding-top:5px;">
	 <!-- 工具条 -->
	<menu:definition menuCode="${menuCode }"/>
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
		  <div class="row">
		    <label class="col-sm-1 control-label">试题名称</label>
		    <div class="col-sm-4">
		      	<input type="text" class="form-control" id="subject" name="subject" value="" />
		    </div>
		    <button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
		   </div>
		</form>
	</div>
	<div class="row ibox-content">
		<table id="grid"></table>
	</div>
</div>
<script type="text/javascript"> 
$(document).ready(function(){
	loadGrid();//加载列表
});
    //加载列表
    function loadGrid() {
    	$("#grid").bootstrapTable({
    		url: "/answer/examOnline_myExamload",
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
    	              {title: "试题名称",field: "subject",width: 200,align:"center"}, 
    	              {title: "成绩",field: "totalSource",width: 100,align:"center"},
    	              {title: "耗时",field: "consumeTime",width: 100,align:"center",formatter:function(value,row,index){
    	            		if(value<60){
    	            			html = value;
    	            		}else{
    	            			html = Math.floor(value/60)+":"+(value%60);
    	            		}
    	            		return html;
    	              }},
    				  {title: "操作", field: "", width: 90,align:"center",formatter:function(value,row,index){
    					  var json = JSON.stringify(row);
    					  return table_button.replace(new RegExp("row_id_","gm"),""+json+"");
		              }}]
    	 });
    }
    
  	
	//查询
	function on_search(){
		$("#grid").bootstrapTable("refresh");
	}
	
	//成绩
	function on_result(obj){
		parent.layer.open({
            type: 2,
            title: "考试成绩",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["50%", "60%"],
            content: "/answer/examOnline_result?id="+obj.examManage.id+"&redo=2"
        });
	}
</script>
</body>
</html>