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
    		url: "/answer/examOnline_load",
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
    	              {title: "开始时间",field: "beginTime",width: 100,align:"center"}, 
    	              {title: "结束时间",field: "endTime",width: 100,align:"center"},
    	              {title: "考试类型", field: "type", width: 70,align:"center",formatter:function(value,row,index){
	            			return changeDataDictByKey("exam_type",value);
	              	  }},
    	              {title: "时长",field: "examTime",width: 100,align:"center",formatter:function(value,row,index){
    	            	  if(value==0)
    	            		  return "不限时";
    	            	  else
    	            		  return value+"分钟";
    	              }},
    				  {title: "操作", field: "", width: 90,align:"center",formatter:function(value,row,index){
    					  var json = JSON.stringify(row);
    					  var tb = table_button.split("&nbsp;&nbsp;");
	    				  var html = "";
    					  $.ajax({//检查是否能做题
   		            		type : 'post',
   		            		async:false,
   		            		dataType : 'json',
   		            		url: '/answer/checkExamPerson?examManageId='+row.id,
   		            		success : function(data){
   		            			if(data.isExam){//已结考试
   		            				for (var i = 0; i < tb.length; i++) {
   		    						  if(tb[i].indexOf("开始考试")<0)
   		    							  html+=tb[i]+"&nbsp;&nbsp;";
   									};
   		            			}else{//没有考试
   		            				for (var i = 0; i < tb.length; i++) {
   		    						  if(tb[i].indexOf("成绩")<0)
   		    							  html+=tb[i]+"&nbsp;&nbsp;";
   									};
   		            			}
   		            		}
   		            	});
    					  return html.replace(new RegExp("row_id_","gm"),""+json+"");
		              }}]
    	 });
    }
    
  	
	//查询
	function on_search(){
		$("#grid").bootstrapTable("refresh");
	}
	
	//开始考试 mark:1不显示答案
	function on_beginExam(obj){
		parent.layer.open({
            type: 2,
            title: "考试",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["100%", "100%"],
            content: "/answer/examOnline_start?id="+obj.id+"&mark=1",
            cancel: function(index){  //右上角关闭回调
            	var body = parent.layer.getChildFrame('body', index);
                var examManageId = body.find('#examManageId').val(); //得到iframe页的body内容
                var consumeTime = body.find('#consumeTime').val(); //得到iframe页的body内容
                $.ajax({
            		type : 'post',
            		async:false,
            		dataType : 'json',
            		url: '/answer/saveOnlineExamConsumeTime?examManageId='+examManageId+'&consumeTime='+consumeTime,
            		success : function(data){
            			if(data.result)
            				return true;
            		}
            	});
            }
        });
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
            content: "/answer/examOnline_result?id="+obj.id+"&redo=1"
        });
	}
</script>
</body>
</html>