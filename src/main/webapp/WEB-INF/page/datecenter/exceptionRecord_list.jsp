<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>营运异常记录</title>
</head>
<body>
<div class="ibox-content" style="padding-top:5px;">
	 <!-- 工具条 -->
	<menu:definition menuCode="${menuCode }"/>
	<c:if test="${empty menuCode }">
		<div class="row" style="padding:0px 10px 5px 10px;">
			<button class="btn btn-primary " id="add" style="margin-left: 5px;" type="button" onclick="on_add()"><i class="fa fa-plus"></i>&nbsp;新增</button>
			<button class="btn btn-danger " id="del" style="margin-left: 5px;" type="button" onclick="on_del()"><i class="fa fa-remove"></i>&nbsp;删除</button>
		</div>
	</c:if>
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
			<c:if test="${not empty menuCode}">
				<div class="row">
					<label class="col-sm-1 control-label">起始日期</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" id="dutyDateStart" name="dutyDateStart" value="" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</div>
					<label class="col-sm-1 control-label">终止日期</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" id="dutyDateEnd" name="dutyDateEnd" value="" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</div>
					<label class="col-sm-1 control-label">异常类型</label>
					<div class="col-sm-2">
						<opt:select dictKey="dc_exceptionType" classStyle="form-control" id="exceptionType" name="exceptionType" isDefSelect="true"/>
					</div>
					<label class="col-sm-1 control-label">关键字</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" id="keyword" name="keyword" value="" />
					</div>
				</div>

				<div class="row" style="margin-top: 10px;">
					<label class="col-sm-1 control-label">报告部门</label>
					<div class="col-sm-2">
						<opt:select dictKey="dc_reportingDepartment" classStyle="form-control" id="reportedDp" name="reportedDp" isDefSelect="true"/>
					</div>
					<label class="col-sm-1 control-label">报告人员</label>
					<div class="col-sm-2">
						<opt:select dictKey="dc_reportingPerson" classStyle="form-control" id="reportedPerson" name="reportedPerson" isDefSelect="true"/>
					</div>
					<label class="col-sm-1 control-label">处理部门</label>
					<div class="col-sm-2">
						<opt:select dictKey="dc_NotificationDepartment" classStyle="form-control" id="processingDp" name="processingDp" isDefSelect="true"/>
					</div>

					<button class="btn btn-primary" type="button" onclick="on_search()"><i class="fa fa-search"></i>&nbsp;搜索</button>
					<button class="btn btn-primary" type="button" onclick="on_export()"><i class="fa fa-file-excel-o"></i>&nbsp;导出Excle</button>
				</div>
			</c:if>
			<c:if test="${empty menuCode}">
				<input type="hidden" id="ttId" name="ttId" value="${ttId}" />
			</c:if>
		</form>
	</div>
	<div class="row ibox-content">
		<div class="col-sm-12">
			<table id="grid"></table>
		</div>
	</div>
</div>
<script type="text/javascript">
	var winName = window.name;
	var URLStr = "/datecenter/exceptionRecord/exceptionRecord_";
	
    $(document).ready(function(){
    	loadGrid();//加载列表
    });
    //加载列表
    function loadGrid() {
    	$("#grid").bootstrapTable({
    		url: URLStr + "load",
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
    	              /*{title: "标题", field: "title",width: 250,align:"center"}, */
    	              {title: "日期", field: "dutyDate",width: 80,align:"center",formatter:function(value,row,index){
		           	  		return value.substr(0,10);
		              }},
				      {title: "异常类型", field: "exceptionType",width: 80,align:"center",formatter:function(value,row,index){
							return changeDataDictByKey("dc_exceptionType",value);
					   }},
  	              	  {title: "接报时间", field: "receiptTime",width: 80,align:"center",formatter:function(value,row,index){
		           	  		return value.substr(11,5);
		              }},
		              {title: "报告部门", field: "reportedDp",width: 80,align:"center"},
		              {title: "报告人员", field: "reportedPerson",width: 80,align:"center"},
		               {title: "报告方式", field: "reportedWay",width: 80,align:"center"},
		              {title: "通行路段", field: "trafficRoad",width: 150,align:"center"},
		              {title: "通知处理部门", field: "processingDp",width: 80,align:"center"},
    	             /* {title: "情况简述", field: "briefIntroduction",width: 200,align:"center"},
    	              {title: "处理结果", field: "result",width: 200,align:"center"},
    	              {title: "备注", field: "remark",width: 200,align:"center"},*/
    				  {title: "操作", field: "", width: 60,align:"center",formatter:function(value,row,index){
    					  return "<a href='#' onclick='on_edit(\""+row.id+"\")'>编辑</a>";
		              }}]
    	 });
    }
    
    
    
    var ttId = "${ttId}";
	var dutyDateStr = "${dutyDateStr}";
  	//新增
  	function on_add(){
  		parent.layer.open({
            type: 2,
            title: "新增营运异常记录",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["95%", "95%"],
            content: URLStr + "edit?winName="+winName+"&ttId="+ttId+"&dutyDateStr="+dutyDateStr
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
					url: URLStr + "delete?ids="+ids,
					success : function(data){
						if(data.result){
							$("#grid").bootstrapTable("refresh",{url: URLStr + "load"});//加载树下的列表
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
  	function on_edit(id){
  		parent.layer.open({
            type: 2,
            title: "编辑营运异常记录",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["95%", "95%"],
            content: URLStr + "edit?winName="+winName+"&id="+id
        });
  	}
  	
	//查询
	function on_search(){
		$("#grid").bootstrapTable("refresh");
	}

    //导出Excel
    function on_export(){
        let star = $("#dutyDateStart").val();
        let end = $("#dutyDateEnd").val();
        if(star == null || star == "" || end == null || end == ""){
            autoAlert("注意：导出Excel时筛选条件不能为空!",5);
        }else if((new Date(end) - new Date(star))/(24*60*60*1000) > 92){
            autoAlert("注意：导出Excel时筛选时间区间必须三个月以内，以防数据量过大!",5);
        }else{
            window.location.href = URLStr + "export?dutyDateStart="+$("#dutyDateStart").val()+"&dutyDateEnd="+$("#dutyDateEnd").val()+"&reportedDp="+$("#reportedDp").val()+"&processingDp="+$("#processingDp").val()+"&keyword="+$("#keyword").val()
        }
    }

</script>
</body>
</html>