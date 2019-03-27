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
		    <div class="col-sm-3">
		      	<input type="text" class="form-control" id="subject" name="subject" value="" data-rule-rangelength="[1,25]"/>
		    </div>
		     <label class="col-sm-1 control-label">考试类型</label>
		    <div class="col-sm-2">
		      <opt:select dictKey="exam_type" classStyle="form-control" id="type" name="type" isDefSelect="true"/>
		    </div>
		     <label class="col-sm-1 control-label">状态</label>
		    <div class="col-sm-2">
		      <opt:select dictKey="answer_state" classStyle="form-control" id="state" name="state" isDefSelect="true"/>
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
    		url: "/answer/examManage_load",
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
    	              {title: "时长",field: "examTime",width: 80,align:"center",formatter:function(value,row,index){
    	            	  if(value==0)
    	            		  return "不限时";
    	            	  else
    	            		  return value+"分钟";
    	              }},
    	              {title: "考试类型", field: "type", width: 70,align:"center",formatter:function(value,row,index){
	            			return changeDataDictByKey("exam_type",value);
	              	  }},
    	              {title: "状态", field: "state", width: 50,align:"center",formatter:function(value,row,index){
	            			return changeDataDictByKey("answer_state",value);
	              	  }},
    				  {title: "操作", field: "", width: 130,align:"center",formatter:function(value,row,index){
    					  var json = JSON.stringify(row);
    					  var tb = table_button.split("&nbsp;&nbsp;");
    					  var html = "";
    					  if(row.state==0){//撤回 下线
	    					  for (var i = 0; i < tb.length; i++) {
	    						  if(tb[i].indexOf("撤回")<0)
	    							  html+=tb[i]+"&nbsp;&nbsp;";
							  }
    					  }else{
    						  for (var i = 0; i < tb.length; i++) {
    							  if(tb[i].indexOf("发布")<0)
	    							  html+=tb[i]+"&nbsp;&nbsp;";
							  }
    					  }
    					  return html.replace(new RegExp("row_id_","gm"),""+json+"");
		              }}]
    	 });
    }
    
  	//新增
  	function on_add(){
  		parent.layer.open({
            type: 2,
            title: "新增考试",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["100%", "100%"],
            content: "/answer/examManage_edit"
        });
  	}
  	
  	//删除
  	function on_del(){
  		var rows = $("#grid").bootstrapTable("getSelections");
  		if(rows.length>0){
  			parent.layer.confirm("确定删除所选数据？", {
  				btn: ["确定","取消"] //按钮
  			}, function(){
  				var loadIndex = parent.layer.load(0, {shade: 0.6});//加载层	
	  	  		var ids = [];
	  			$.each(rows, function(index, item){
	  				ids.push(item.id);
	  			});
  				$.ajax({
  					type:"post",
  					async:false,
  					dataType : "json",
  					url: '/answer/examManage_delete?ids='+ids,
  					success : function(data){
  						if(data.result){
  							$("#grid").bootstrapTable("refresh",{url:'/answer/examManage_load'});//加载树下的列表
  							autoMsg("删除成功！",1);
  						}else{
  							autoMsg(data.msg,1);
  						}
  						parent.layer.close(loadIndex);
  					},
  					error : function(result){
  						autoAlert("系统出错",5);
  						parent.layer.close(loadIndex);
  					}
  				});
  			});
  		}else{
  			autoAlert("请选择删除的数据",5);
  		}
  	}
  	
  	//编辑
  	function on_edit(obj){
  		parent.layer.open({
            type: 2,
            title: "编辑考试",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["100%", "100%"],
            content: "/answer/examManage_edit?id="+obj.id
        });
  	}
  	
	//考试人员
	function on_examPerson(obj){
		parent.layer.open({
            type: 2,
            title: "考试人员",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["80%", "100%"],
            content: "/answer/examPerson_list?id="+obj.id
        });
	}
  	
//查询
function on_search(){
	$("#grid").bootstrapTable("refresh");
}

//撤回
function on_withdraw(obj){
	changeState(obj);
}

//发布
function on_release(obj){
	changeState(obj);
}	
	
function changeState(obj){
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/examManage_changeState?id='+obj.id,
		success : function(data){
			if(data.result){
				if(data.sign=="up")
					autoMsg("发布成功！",1);
				else
					autoMsg("撤回成功！",1);
				iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/answer/examManage_load"});//加载树下的列表
			}else{
				autoAlert("操作失败，请检查！",5);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}

//报表
function on_examReport(obj){
	parent.layer.open({
        type: 2,
        title: "统计报表",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["100%", "100%"],
        content: "/answer/examOnline_report?id="+obj.id+"&winName="+window.name
    });
}
</script>
</body>
</html>