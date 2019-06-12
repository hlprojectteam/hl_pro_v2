<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>题库选择列表</title>
</head>
<body>
<div class="ibox-content" style="padding-top:5px;">
	 <!-- 工具条 -->
	<menu:definition menuCode="${menuCode }"/>
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
		  <div class="row">
		    <label class="col-sm-1 control-label">主题</label>
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
<!-- 底部按钮 -->
<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_sure()"><i class="fa fa-check"></i>&nbsp;确定</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
<script type="text/javascript"> 
var winName = '${winName}';
var id = '${id}';
var name = '${name}';
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
		sidePagination: "server", //服务端请求
		queryParams: queryParams,//发送请求参数
	    columns: [{checkbox:true},
	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
         	  		return index+1;
            }},
	              {title: "主题",field: "subject",width: 200,align:"center"}, 
	              {title: "创建时间",field: "createTime",width: 200,align:"center"}
            ]
	 });
}
    
//确定
function on_sure(){
	var num = 5;
	var msg = "最多只能选择5个题库，请重新选择！";
	var map = new Map();
	var rows = $("#grid").bootstrapTable("getSelections");
	if(rows.length>0){
		if(rows.length<=num){
			parent.layer.confirm("确定所选题库？", {
				btn: ["确定","取消"] //按钮
			}, function(ind){
		  		var ids = [];
		  		var names = [];
				$.each(rows, function(index, item){
					ids.push(item.id);
					names.push(item.subject);
				});
				var idStr = parent.frames[winName].$("#"+id).val();
				var nameStr = parent.frames[winName].$("#"+name).val();
				if(idStr==""||idStr==null){
					if(ids.length>num){
						autoAlert(msg,5);
					}else{
						parent.frames[winName].$("#"+id).val(ids);
						parent.frames[winName].$("#"+name).val(names);	
						parent.layer.close(ind);
						parent.layer.close(index);
					}
				}else{//已经有值 排重
					var idz = idStr.split(",");
					var namez = nameStr.split(",");
					for (var i = 0; i < idz.length; i++) {
						map.put(idz[i],namez[i]);
					}
					for (var i = 0; i < ids.length; i++) {
						map.put(ids[i],names[i]);
					}
					var idsz = [];
			  		var namesz = [];
			  		if(map.arr.length>num){
			  			autoAlert(msg,5);
			  		}else{
						for (var i = 0; i < map.arr.length; i++) {
							idsz.push(map.arr[i].key);
							namesz.push(map.arr[i].value);
						}
						parent.frames[winName].$("#"+id).val(idsz);
						parent.frames[winName].$("#"+name).val(namesz);
						parent.layer.close(ind);
						parent.layer.close(index);
			  		}
				}
			});
		}else{
			autoAlert(msg,5);
		}
	}else{
		autoAlert("请选择题库",5);
	}
}    
    
//查询
function on_search(){
	$("#grid").bootstrapTable("refresh");
}

</script>
</body>
</html>