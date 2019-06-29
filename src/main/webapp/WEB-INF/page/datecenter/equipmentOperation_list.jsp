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
    <title>设备运行情况统计表</title>
</head>
<body>
<div class="ibox-content" style="padding-top:5px;">
	 <!-- 工具条 -->
	<menu:definition menuCode="${menuCode }"/>
	<c:if test="${empty menuCode }">
		<div class="row" style="padding:0px 10px 5px 10px;">
			<button class="btn btn-primary " id="add" style="margin-left: 5px;" type="button" onclick="on_add()"><i class="fa fa-plus"></i>&nbsp;新增</button>
			<button class="btn btn-danger " id="del" style="margin-left: 5px;" type="button" onclick="on_del()"><i class="fa fa-remove"></i>&nbsp;删除</button>
			<button class="btn btn-primary " id="addMany" style="margin-left: 5px;" type="button" onclick="on_addMany()"><i class="fa fa-plus"></i>&nbsp;批量新增</button>
		</div>
	</c:if>
	<div class="row ibox-content" style="padding:5px 0 5px 0;">
		<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
			<c:if test="${not empty menuCode}">
				<div class="row">
					<label class="col-sm-2 control-label">起始日期</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" id="dutyDateStart" name="dutyDateStart" value="" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</div>
					<label class="col-sm-1 control-label">终止日期</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" id="dutyDateEnd" name="dutyDateEnd" value="" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</div>

					<label class="col-sm-1 control-label">收费站</label>
					<div class="col-sm-2">
						<opt:select dictKey="dc_tollGate" classStyle="form-control" id="tollGate" name="tollGate" isDefSelect="true"/>
					</div>
				</div>

				<div class="row"  style="margin-top: 10px;">
					<label class="col-sm-2 control-label">是否有故障的设备</label>
					<div class="col-sm-2">
						<opt:select dictKey="dc_equipmentStatus" classStyle="form-control" id="isOrNot" name="isOrNot" isDefSelect="true"/>
					</div>

					<label class="col-sm-1 control-label">关键字</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" id="keyword" name="keyword" value="" />
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
	var URLStr = "/datecenter/equipmentOperation/equipmentOperation_";
	
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
    		pageSize:13,//每页大小
    		pageNumber:1,//开始页
    		pageList:[10,20,50],
    		pagination:true,//显示分页工具栏
    		sidePagination: "server", //服务端请求
    		queryParams: queryParams,//发送请求参数
    	    columns: [{checkbox:true},
    	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
		           	  		return index+1;
		              }},
    	              /*{title: "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;标题&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;", field: "title",align:"center"}, */
    	              {title: "&nbsp;&nbsp;&nbsp;&nbsp;日期&nbsp;&nbsp;&nbsp;&nbsp;", field: "dutyDate",align:"center",width:70,formatter:function(value,row,index){
		           	  		return value.substr(0,10);
		              }},
    	              {title: "&nbsp;&nbsp;收费站&nbsp;&nbsp;", field: "tollGate",align:"center",width:100,formatter:function(value,row,index){
		           	  		return changeDataDictByKey("dc_tollGate",value);
		              }},
		              {title: "车道高清抓拍", field: "cdgqzp",align:"center",formatter:function(value,row,index){
                              if(value == 1){
                                  return "<i style='color: green;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else if(value == 2){
                                  return "<i style='color: yellow;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else{
                                  return "<i style='color: red;' class='fa fa-circle' aria-hidden='true'></i>"
                              }
		           	  		/*return changeDataDictByKey("dc_equipmentStatus",value);*/
		              }},
		              {title: "自动发卡机", field: "zdfkj",align:"center",formatter:function(value,row,index){
                              if(value == 1){
                                  return "<i style='color: green;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else if(value == 2){
                                  return "<i style='color: yellow;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else{
                                  return "<i style='color: red;' class='fa fa-circle' aria-hidden='true'></i>"
                              }
		              }},
		              {title: "MTC出口车道", field: "mtcckcd",align:"center",formatter:function(value,row,index){
                              if(value == 1){
                                  return "<i style='color: green;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else if(value == 2){
                                  return "<i style='color: yellow;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else{
                                  return "<i style='color: red;' class='fa fa-circle' aria-hidden='true'></i>"
                              }
		              }},
		              {title: "ETC出口车道", field: "etcckcd",align:"center",formatter:function(value,row,index){
                              if(value == 1){
                                  return "<i style='color: green;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else if(value == 2){
                                  return "<i style='color: yellow;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else{
                                  return "<i style='color: red;' class='fa fa-circle' aria-hidden='true'></i>"
                              }
		              }},
		              {title: "MTC入口车道", field: "mtcrkcd",align:"center",formatter:function(value,row,index){
                              if(value == 1){
                                  return "<i style='color: green;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else if(value == 2){
                                  return "<i style='color: yellow;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else{
                                  return "<i style='color: red;' class='fa fa-circle' aria-hidden='true'></i>"
                              }
		              }},
		              {title: "ETC入口车道", field: "etcrkcd",align:"center",formatter:function(value,row,index){
                              if(value == 1){
                                  return "<i style='color: green;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else if(value == 2){
                                  return "<i style='color: yellow;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else{
                                  return "<i style='color: red;' class='fa fa-circle' aria-hidden='true'></i>"
                              }
		              }},
		              {title: "计重车道", field: "jzcd",align:"center",formatter:function(value,row,index){
                              if(value == 1){
                                  return "<i style='color: green;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else if(value == 2){
                                  return "<i style='color: yellow;' class='fa fa-circle' aria-hidden='true'></i>"
                              }else{
                                  return "<i style='color: red;' class='fa fa-circle' aria-hidden='true'></i>"
                              }
		              }},
    	              {title: "停用时间开始", field: "downTimeStart",align:"center",formatter:function(value,row,index){
		           	  		return value.substr(11,5);
		              }},
    	              {title: "停用时间结束", field: "downTimeEnd",align:"center",formatter:function(value,row,index){
		           	  		return value.substr(11,5);
		              }},
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
            title: "新增设备运行情况统计表",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["95%", "95%"],
            content: URLStr + "edit?winName="+winName+"&ttId="+ttId+"&dutyDateStr="+dutyDateStr
        });
  	}

    //批量新增
    function on_addMany(){
        parent.layer.confirm("确定批量新增数据？", {
            btn: ["确定","取消"] //按钮
        }, function(){
            $.ajax({
                type:"post",
                async:false,
                dataType : "json",
                url: URLStr + "addMany?ttId="+ttId+"&dutyDateStr="+dutyDateStr,
                success : function(data){
                    if(data.result){
                        $("#grid").bootstrapTable("refresh",{url: URLStr + "load"});//加载树下的列表
                        autoMsg("批量新增成功！",1);
                    }else{
                        autoMsg("批量新增失败！",5);
                    }
                },
                error : function(result){
                    autoAlert("系统出错",5);
                }
            });
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
            title: "编辑设备运行情况统计表",
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
            window.location.href = URLStr + "export?dutyDateStart="+$("#dutyDateStart").val()+"&dutyDateEnd="+$("#dutyDateEnd").val()+"&tollGate="+$("#tollGate").val()+"&isOrNot="+$("#isOrNot").val()+"&keyword="+$("#keyword").val()
        }
    }
</script>
</body>
</html>