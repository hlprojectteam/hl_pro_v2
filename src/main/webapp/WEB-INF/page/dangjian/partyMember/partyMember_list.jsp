<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>党员档案列表</title>
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
		      	 <select id="branchSelect" name="branchId" value='' class="form-control m-b required"></select>
		    </div>
		  	<label class="col-sm-1 control-label">姓名</label>
		    <div class="col-sm-2">
		      	<input type="text" class="form-control" id="userName" name="userName" value="" />
		    </div>
			<button class="btn btn-primary " type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
		  </div>
		</form>
	</div>
	<div class="row ibox-content">
		<div class="col-sm-12">
			<table id="grid"></table>
		</div>
	</div>
</div>
<script type="text/javascript"> 
    $(document).ready(function(){
    	loadGrid();//加载列表
    	getBranch();//加载党支部下拉
    });
    //加载列表
    function loadGrid() {
    	$("#grid").bootstrapTable({
    		url: "/dangjian/partyMember_load",
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
    	              {title: "姓名",field: "userName",width:80,align:"center"}, 
    	              {title: "性别", field: "sex", width: 80,align:"center",formatter:function(value,row,index){
		           	  		return changeDataDictByKey("sex",value);
		              }},
    	              {title: "支部",field: "branchName",width: 80,align:"center"}, 
    	              {title: "头衔", field: "memberDuty", width: 80,align:"center",formatter:function(value,row,index){
		           	  		return changeDataDictByKey("dj_partyMember_duty",value);
		              }},
		              {title: "入党时间",field: "joininTime",width: 100,align:"center"},
    				  {title: "操作", field: "", width: 120,align:"center",formatter:function(value,row,index){
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
    
  	//新增
  	function on_add(){
  		parent.layer.open({
            type: 2,
            title: "新增党员",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["95%", "95%"],
            content: "/dangjian/partyMember_edit?"
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
					type:"post",
					async:false,
					dataType : "json",
					url: "/dangjian/partyMember_del?ids="+ids,
					success : function(data){
						if(data.result){
							$("#grid").bootstrapTable("refresh",{url:"/dangjian/partyMember_load"});//加载树下的列表
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
			autoAlert("请选择删除的实体",5);
		}
  	}
  	
  	//编辑
  	function on_edit(obj){
  		parent.layer.open({
            type: 2,
            title: "编辑党员",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["95%", "95%"],
            content: "/dangjian/partyMember_edit?id="+obj.id
        });
  	}
  	
	//查询
	function on_search(){
		$("#grid").bootstrapTable("refresh");
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

</script>
</body>
</html>