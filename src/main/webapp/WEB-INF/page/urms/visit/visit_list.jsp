<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld"%>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld"%>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>访问量列表</title>
</head>
<body>
	<div class="ibox-content" style="padding-top: 5px;">
		<!-- 工具条 -->
		<menu:definition menuCode="${menuCode }" />
		<div class="row ibox-content" style="padding:5px 0 5px 0;">
			<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="">
				<div class="row">
					<label class="col-sm-1 control-label">访问IP</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" id="visitIp" name="visitIp" value="" />
					</div>
					<label class="col-sm-1 control-label">运营商</label>
					<div class="col-sm-2">
						<opt:select dictKey="visit_operator" classStyle="form-control" id="visitOperatorType" name="visitOperatorType" isDefSelect="true" />
					</div>
					<label class="col-sm-1 control-label">来源</label>
					<div class="col-sm-2">
						<opt:select dictKey="visit_Source" classStyle="form-control" id="visitSourceType" name="visitSourceType" isDefSelect="true" />
					</div>
					<button class="btn btn-primary " type="button" onclick="on_search()">
						<i class="fa fa-search"></i>&nbsp;搜索
					</button>
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
		$(document).ready(function() {
			loadGrid();//加载列表
		});
		//加载列表
		function loadGrid() {
			$("#grid").bootstrapTable({
				url : "/visit/visit_load",
				dataType : "json",
				method : "post",
				queryParamsType : "limit",//服务器分页必须
				striped : false,//条纹行
				contentType : "application/x-www-form-urlencoded",
				pageSize : 10,//每页大小
				pageNumber : 1,//开始页
				pageList : [ 10, 20, 50 ],
				pagination : true,//显示分页工具栏
				sidePagination : "server", //服务端请求
				queryParams : queryParams,//发送请求参数
				columns : [{
					title : "序号",
					field : "id",
					align : "center",
					width : 50,
					formatter : function(value, row, index) {
						return index + 1;
					}
				}, {
					title : "IP",
					field : "visitIp",
					align : "center",
					width : 150
				}, {
					title : "运营商",
					field : "visitOperatorType",
					align : "center",
					width : 100,
					formatter:function(value,row,index){
		           	  	return changeDataDictByKey("visit_operator",value);
		            }	
				}, {
					title : "访问时间",
					field : "visitStartTime",
					align : "center",
					width : 150,
					formatter : function(value, row, index) {
						return FormatDate(value);
					}
				}, {
					title : "来源地址",
					field : "visitOperatorAdress",
					align : "center"
				}, {
					title : "浏览器",
					field : "visitBrowser",
					align : "center",formatter:function(value,row,index){
					    if(value.indexOf("Edge") != -1 || value.indexOf("IE") != -1){
							return "<i class=\"fa fa-edge\" aria-hidden=\"true\"></i> "+value;
						}else if(value.indexOf("Firefox") != -1 ){
                            return "<i class=\"fa fa-firefox\" aria-hidden=\"true\"></i> "+value;
                        }else if(value.indexOf("Chrome") != -1 ){
                            return "<i class=\"fa fa-chrome\" aria-hidden=\"true\"></i> "+value;
                        }else if(value.indexOf("Safari") != -1 ){
                            return "<i class=\"fa fa-safari\" aria-hidden=\"true\"></i> "+value;
                        }else if(value.indexOf("Opera") != -1 ){
                            return "<i class=\"fa fa-opera\" aria-hidden=\"true\"></i> "+value;
                        }else{
                            return value;
						}
					}
				}, {
					title : "系统",
					field : "visitSystem",
					align : "center",formatter:function(value,row,index){
                        if(value.indexOf("Windows") != -1){
                            return "<i class=\"fa fa-windows\" aria-hidden=\"true\"></i> "+value;
                        }else if(value.indexOf("Mac OS") != -1 ){
                            return "<i class=\"fa fa-apple\" aria-hidden=\"true\"></i> "+value;
                        }else if(value.indexOf("Linux") != -1 ){
                            return "<i class=\"fa fa-linux\" aria-hidden=\"true\"></i> "+value;
                        }else if(value.indexOf("Android") != -1 ){
                            return "<i class=\"fa fa-android\" aria-hidden=\"true\"></i> "+value;
                        }else if(value.indexOf("IOS") != -1 ){
                            return "<i class=\"fa fa-apple\" aria-hidden=\"true\"></i> "+value;
                        }else{
                            return value;
                        }
					}
				}, {
					title : "来源",
					field : "visitSourceType",
					align : "center",
					width : 100	,
					formatter:function(value,row,index){
		           	  	//return changeDataDictByKey("visit_Source",value);
						if(value == 1){
                            return "<i class=\"fa fa-desktop\" aria-hidden=\"true\"></i> 电脑";
						}
                        if(value == 2){
                            return "<i class=\"fa fa-tablet\" aria-hidden=\"true\"></i> 手机";
                        }
		            }		
				}]
			});
		}

		//查询
		function on_search() {
			$("#grid").bootstrapTable("refresh");
		}
		
		//格式化时间
		function FormatDate(now) {
			if (now != null) {
				var time = now.time;
				var dateTime = new Date(time);
				var month = dateTime.getMonth();//此处获得的月份少一个月（js date特性）
				month = month + 1;
				return dateTime.getFullYear() + '-' + month + '-' + dateTime.getDate()+' '+dateTime.getHours()+':'+dateTime.getMinutes()+':'+dateTime.getSeconds();
			} else {
				return "";
			}
		}
	</script>
</body>
</html>