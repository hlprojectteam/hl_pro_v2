<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>党组织简介列表</title>
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
		      	 <select id="branchSelect" name="branchId" value='${introductionVo.branchId}' class="form-control m-b required"></select>
		    </div>
		  	<label class="col-sm-1 control-label">主标题</label>
		    <div class="col-sm-2">
		      	<input type="text" class="form-control" id="mainTitle" name="mainTitle" value="" />
		    </div>
		    <label class="col-sm-1 control-label">来源</label>
		    <div class="col-sm-2">
		      	 <opt:select dictKey="news_Source" classStyle="form-control" id="newSource" name="newSource" isDefSelect="true"/>
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
    var mType = "${type}";//简介类型
    var mBranchId = "${branchId}";//当前支部ID
    
    $(document).ready(function(){
    	loadGrid();//加载列表
    	getBranch();//加载党支部下拉
    });
    //加载列表
    function loadGrid() {
    	$("#grid").bootstrapTable({
    		url: "/dangjian/introduction_load?category="+mType,
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
    	              {title: "主标题",field: "mainTitle",width: 100,align:"center"}, 
    	              {title: "作者",field: "authorName",width: 100,align:"center"}, 
    	              {title: "发布时间", field: "releaseDate", width: 100,align:"center",formatter:function(value,row,index){
   	            			var strs = '';
   	            			if(value != undefined){
   	            				strs += ''+(value.year+1900)+'-';
   	            				value.month++;
   	            				if(value.month < 10){
   	            					value.month = '0'+value.month;
   	            				}
   	            				strs += value.month +'-';
   	            				if(value.date < 10){
   	            					value.day = '0'+value.date;
   	            				}
   	            				strs += value.date;
   	            			}
   	            			return strs;
   	              	  }},
   	              	  {title: "点击量",field: "visitNum",width: 100,align:"center"}, 
    				  {title: "操作", field: "", width: 120,align:"center",formatter:function(value,row,index){
    					  var json = JSON.stringify(row);//将对象解析成json字符串
    					  var tb = table_button.split("&nbsp;&nbsp;");
    					  var html = "";
    					  if(row.status==0){//撤回 下线
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
    					 if(mBranchId==data.rows[i].id){
	    					 $("#branchSelect").append("<option value='"+data.rows[i].id+"'>"+data.rows[i].branchName+"</option>");
    					 }
    				 }
    			}
    		},
    	});
    }
    
  	//新增
  	function on_add(){
  		parent.layer.open({
            type: 2,
            title: "新增",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["95%", "95%"],
            content: "/dangjian/introduction_edit?category="+mType
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
					url: "/dangjian/introduction_del?ids="+ids,
					success : function(data){
						if(data.result=="success"){
							$("#grid").bootstrapTable("refresh",{url:"/dangjian/introduction_load?category="+mType});//加载树下的列表
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
			autoAlert("请选择删除的记录",5);
		}
  	}
  	
  	//编辑
  	function on_edit(obj){
  		parent.layer.open({
            type: 2,
            title: "编辑",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["95%", "95%"],
            content: "/dangjian/introduction_edit?id="+obj.id
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
		
	//更改状态	
	function changeState(obj){
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: '/dangjian/introduction_changeState?id='+obj.id,//此处写成obj.id会造成后台id=undefind
			success : function(data){
				if(data.result){
					if(data.sign=="up")
						autoMsg("发布成功！",1);
					else
						autoMsg("撤回成功！",1);
					iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/dangjian/introduction_load?category="+mType});//加载树下的列表
				}else{
					autoAlert("操作失败，请检查！",5);
				}
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！",5);
			}
		});
	}

</script>
</body>
</html>