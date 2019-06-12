<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>题库列表</title>
</head>
<body>
<div class="ibox-content" style="padding-top:5px;">
	 <!-- 工具条 -->
	<menu:definition menuCode="${menuCode }"/>
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
		  <div class="row">
		    <label class="col-sm-1 control-label">题库名称</label>
		    <div class="col-sm-4">
		      	<input type="text" class="form-control" id="subject" name="subject" value="" data-rule-rangelength="[1,25]"/>
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
    		url: "/answer/questionManage_load",
    		dataType:"json",
    		method:"post",
    		queryParamsType: "limit",//服务器分页必须
    		striped:true,//条纹行
    		contentType: "application/x-www-form-urlencoded",
    		pageSize:10,//每页大小
    		pageNumber:1,//开始页
    		pageList:[10,20,50],
    		pagination:true,//显示分页工具栏
    		singleSelect:true,//只能单选
    		sidePagination: "server", //服务端请求
    		queryParams: queryParams,//发送请求参数
    	    columns: [{checkbox:true},
    	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
		           	  		return index+1;
		              }},
    	              {title: "题库名称",field: "subject",width: 200,align:"center"}, 
    	              {title: "创建时间",field: "createTime",width: 200,align:"center"}, 
    				  {title: "操作", field: "", width: 90,align:"center",formatter:function(value,row,index){
    					  var json = JSON.stringify(row);
    					  return table_button.replace(new RegExp("row_id_","gm"),""+json+"");
		              }}]
    	 });
    }
    
  	//新增
  	function on_add(){
  		parent.layer.open({
            type: 2,
            title: "新增题库",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["50%", "30%"],
            content: "/answer/questionManage_edit"
        });
  	}
  	
  	//删除
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
					type : 'post',
					async:false,
					dataType : 'json',
					url: '/answer/checkQMIsUse?id='+ids,
					success : function(data){
						if(data.result){
							autoAlert("题库已经被使用，不能删除！",1);
						}else{
							$.ajax({
								type:"post",
								async:false,
								dataType : "json",
								url: '/answer/questionManage_delete?ids='+ids,
								success : function(data){
									if(data.result){
										$("#grid").bootstrapTable("refresh",{url:"/answer/questionManage_load"});//加载树下的列表
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
					},
		  			error: function(XMLHttpRequest, textStatus, errorThrown) {
		  				autoAlert("系统出错，请检查！",5);
		  			}
			  	});
  			});
  		}else{
  			autoAlert("请选择删除的数据",5);
  		}
  	
  	
  	
  	}
  	
  	//编辑
  	function on_edit(obj){
  		$.ajax({
  			type : 'post',
  			async:false,
  			dataType : 'json',
  			url: '/answer/checkQMIsUse?id='+obj.id,
  			success : function(data){
  				if(data.result){
  					autoAlert("题库已经被使用，不能编辑修改！",1);
  				}else{
		 		   	parent.layer.open({
		 	             type: 2,
		 	             title: "编辑题库",
		 	             shadeClose: true,//打开遮蔽
		 	             shade: 0.6, 
		 	             maxmin: true, //开启最大化最小化按钮
		 	             area: ["50%", "30%"],
		 	             content: "/answer/questionManage_edit?id="+obj.id
		 	         });
  				}
  			},
  			error: function(XMLHttpRequest, textStatus, errorThrown) {
  				autoAlert("系统出错，请检查！",5);
  			}
  		});
  	}
	//题库管理
  	function on_questionMag(obj){
  		parent.layer.open({
            type: 2,
            title: "编辑题库",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["100%", "100%"],
            content: "/answer/question_edit?id="+obj.id+"&winName="+window.name
        });
  	}
    //题库导入
  	function on_import(obj){
  		parent.layer.open({
            type: 2,
            title: "导入题库",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["60%", "60%"],
            content: "/answer/question_import?id="+obj.id+"&winName="+window.name
        });
  	}
//查询
function on_search(){
	$("#grid").bootstrapTable("refresh");
}
</script>
</body>
</html>