<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>API综合接口管理列表</title>
</head>
<body>
	<div class="page-container">
		<div class="page page-full animation-fade">
			<div class="page-aside">
				<div class="page-aside-inner height-full" data-plugin="slimScroll">
					<section class="page-aside-section">
						<h5 class="page-aside-title"><strong>API接口列表</strong></h5>
						<div class="list-group">
							<c:forEach items="${caList}" var="caList" varStatus="vstatus">
								<a class="list-group-item" href="javascript:;" onclick="loadData('${caList.attrKey}','${caList.attrValue}');"> ${caList.attrValue} </a>
							</c:forEach>
						</div>
					</section>
				</div>
			</div>
			<div class="page-main">
				<div class="ibox-content" style="padding-top: 5px;">
					<!-- 工具条 -->
					<menu:definition menuCode="${menuCode }" />

					<div class="row ibox-content">
						<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
							<input type="hidden" name="apiType" id="apiType" value="1">
						</form>
						<div class="col-sm-12">
							<table id="grid"></table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" id="apiName" value="IP(IP-API)">

</body>
<script type="text/javascript">
	$(document).ready(function() {
        loadIP_API();
	});

	function loadData(apiType,apiName) {
        $("#grid").bootstrapTable("destroy");
        if(apiType == 1){
            loadIP_API();
		}
        if(apiType == 2){
            loadSMS_API();
        }
        if(apiType == 3){
            loadMap_API();
        }
        if(apiType == 4){
            loadWeather_API();
        }
        if(apiType == 5){
            loadVideo_API();
        }
        $("#apiName").val(apiName);
        $("#apiType").val(apiType);
	}

	//加载ip api列表
	function loadIP_API(){
		$("#grid").bootstrapTable({
			url : "/urms/apiConfig_load?apiType=1",
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
			columns : [{radio:true}, {
				title : "序号",
				field : "id",
				align : "center",
				width : 50,
				formatter : function(value, row, index) {
					return index + 1;
				}
			}, {
				title : "API厂商",
				field : "apiName",
				align : "center",
				width : 150
			}, {
				title : "请求链接",
				field : "apiUrl",
				align : "center",
				width : 150
			}, {
				title : "APIKEY",
				field : "apiKey",
				align : "center",
				width : 150
			}, {
				title : "编码",
				field : "apiURLEncoder",
				align : "center",
				width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("API_URLEncoder",value);
                }
			}, {
				title : "请求方式",
				field : "apiRequestMethod",
				align : "center",
				width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("API_RequestMethod",value);
                }
			}, {
				title : "总次数",
				field : "apiTotleNum",
				align : "center",
				width : 100
			}, {
				title : "已用次数",
				field : "apiUseNum",
				align : "center",
				width : 100
			}, {
				title : "剩余次数",
				field : "",
				align : "center",
				width : 100	,
				formatter : function(value, row, index) {
					return row.apiTotleNum-row.apiUseNum;
				}
            }, {
                title : "当前状态",
                field : "apiState",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("state",value);
                }
			}, {
                title : "操作",
                field : "",
                align : "center",
                width: 230,align:"center",formatter:function(value,row,index) {
            	return "<a href='#' onclick='on_edit(\"" + row.id + "\")'>编辑</a>";
        	}
        }]
		});
	}
	//短信
    function loadSMS_API() {
        $("#grid").bootstrapTable({
            url : "/urms/apiConfig_load?apiType=2",
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
            columns : [{radio:true},{
                title : "序号",
                field : "id",
                align : "center",
                width : 50,
                formatter : function(value, row, index) {
                    return index + 1;
                }
            }, {
                title : "API厂商",
                field : "apiName",
                align : "center",
                width : 150
            }, {
                title : "请求链接",
                field : "apiUrl",
                align : "center",
                width : 150
            }, {
                title : "APIKEY",
                field : "apiKey",
                align : "center",
                width : 150
            }, {
                title : "编码",
                field : "apiURLEncoder",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("API_URLEncoder",value);
                }
            }, {
                title : "请求方式",
                field : "apiRequestMethod",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("API_RequestMethod",value);
                }
            }, {
                title : "总次数",
                field : "apiTotleNum",
                align : "center",
                width : 100
            }, {
                title : "已用次数",
                field : "apiUseNum",
                align : "center",
                width : 100
            }, {
                title : "当前状态",
                field : "apiState",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("state",value);
                }
            }, {
                title : "剩余次数",
                field : "",
                align : "center",
                width : 100	,
                formatter : function(value, row, index) {
                    return row.apiTotleNum-row.apiUseNum;
                }
            }, {
                title : "操作",
                field : "",
                align : "center",
                width: 230,align:"center",formatter:function(value,row,index) {
                    return "<a href='#' onclick='on_edit(\"" + row.id + "\")'>编辑</a>";
                }
            }]
        });
    }
    //地图
    function loadMap_API() {
        $("#grid").bootstrapTable({
            url : "/urms/apiConfig_load?apiType=3",
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
            columns : [{radio:true},{
                title : "序号",
                field : "id",
                align : "center",
                width : 50,
                formatter : function(value, row, index) {
                    return index + 1;
                }
            }, {
                title : "API厂商",
                field : "apiName",
                align : "center",
                width : 150
            }, {
                title : "请求链接",
                field : "apiUrl",
                align : "center",
                width : 150
            }, {
                title : "APIKEY",
                field : "apiKey",
                align : "center",
                width : 150
            }, {
                title : "编码",
                field : "apiURLEncoder",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("API_URLEncoder",value);
                }
            }, {
                title : "请求方式",
                field : "apiRequestMethod",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("API_RequestMethod",value);
                }
            }, {
                title : "总次数",
                field : "apiTotleNum",
                align : "center",
                width : 100
            }, {
                title : "已用次数",
                field : "apiUseNum",
                align : "center",
                width : 100
            }, {
                title : "当前状态",
                field : "apiState",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("state",value);
                }
            }, {
                title : "剩余次数",
                field : "",
                align : "center",
                width : 100	,
                formatter : function(value, row, index) {
                    return row.apiTotleNum-row.apiUseNum;
                }
            }, {
                title : "操作",
                field : "",
                align : "center",
                width: 230,align:"center",formatter:function(value,row,index) {
                    return "<a href='#' onclick='on_edit(\"" + row.id + "\")'>编辑</a>";
                }
            }]
        });
    }
    //视频
    function loadVideo_API() {
        $("#grid").bootstrapTable({
            url : "/urms/apiConfig_load?apiType=5",
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
            columns : [{radio:true},{
                title : "序号",
                field : "id",
                align : "center",
                width : 50,
                formatter : function(value, row, index) {
                    return index + 1;
                }
            }, {
                title : "API厂商",
                field : "apiName",
                align : "center",
                width : 150
            }, {
                title : "请求链接",
                field : "apiUrl",
                align : "center",
                width : 150
            }, {
                title : "APIKEY",
                field : "apiKey",
                align : "center",
                width : 150
            }, {
                title : "编码",
                field : "apiURLEncoder",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("API_URLEncoder",value);
                }
            }, {
                title : "请求方式",
                field : "apiRequestMethod",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("API_RequestMethod",value);
                }
            }, {
                title : "总次数",
                field : "apiTotleNum",
                align : "center",
                width : 100
            }, {
                title : "已用次数",
                field : "apiUseNum",
                align : "center",
                width : 100
            }, {
                title : "当前状态",
                field : "apiState",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("state",value);
                }
            }, {
                title : "剩余次数",
                field : "",
                align : "center",
                width : 100	,
                formatter : function(value, row, index) {
                    return row.apiTotleNum-row.apiUseNum;
                }
            }, {
                title : "操作",
                field : "",
                align : "center",
                width: 230,align:"center",formatter:function(value,row,index) {
                    return "<a href='#' onclick='on_edit(\"" + row.id + "\")'>编辑</a>";
                }
            }]
        });
    }
    //天气
    function loadWeather_API() {
        $("#grid").bootstrapTable({
            url : "/urms/apiConfig_load?apiType=4",
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
            columns : [{radio:true},{
                title : "序号",
                field : "id",
                align : "center",
                width : 50,
                formatter : function(value, row, index) {
                    return index + 1;
                }
            }, {
                title : "API厂商",
                field : "apiName",
                align : "center",
                width : 150
            }, {
                title : "请求链接",
                field : "apiUrl",
                align : "center",
                width : 150
            }, {
                title : "APIKEY",
                field : "apiKey",
                align : "center",
                width : 150
            }, {
                title : "编码",
                field : "apiURLEncoder",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("API_URLEncoder",value);
                }
            }, {
                title : "请求方式",
                field : "apiRequestMethod",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("API_RequestMethod",value);
                }
            }, {
                title : "总次数",
                field : "apiTotleNum",
                align : "center",
                width : 100
            }, {
                title : "已用次数",
                field : "apiUseNum",
                align : "center",
                width : 100
            }, {
                title : "当前状态",
                field : "apiState",
                align : "center",
                width : 100,
                formatter : function(value, row, index) {
                    return changeDataDictByKey("state",value);
                }
            }, {
                title : "剩余次数",
                field : "",
                align : "center",
                width : 100	,
                formatter : function(value, row, index) {
                    return row.apiTotleNum-row.apiUseNum;
                }
            }, {
                title : "操作",
                field : "",
                align : "center",
                width: 230,align:"center",formatter:function(value,row,index) {
                    return "<a href='#' onclick='on_edit(\"" + row.id + "\")'>编辑</a>";
                }
            }]
        });
    }

    //新增
    function on_add(){
	    var apiType = $("#apiType").val();
	    var apiName = $("#apiName").val();
        parent.layer.open({
            type: 2,
            title: "新增"+apiName+"信息",
            shadeClose: true,//打开遮蔽
            shade: 0.6,
            maxmin: false, //开启最大化最小化按钮
            area: ["80%", "80%"],
            content: "/urms/apiConfig_edit?apiType="+apiType
        });
    }

    //查询
    function on_search() {
        $("#grid").bootstrapTable("refresh");
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
//                    if(item.apiState == 1){
//                        autoAlert("正在使用的数据不能删除!",5);
//                        return;
//					}
                });
                $.ajax({
                    type:"post",
                    async:false,
                    dataType : "json",
                    url: '/urms/apiConfig_delete?id='+ids,
                    success : function(data){
                        if(data.result){
                            $("#grid").bootstrapTable("refresh",{url:"/urms/apiConfig_load?apiType="+$("#apiType").val()});
                            autoMsg(data.msg,1);
                        }else{
                            autoMsg(data.msg,5);
                        }
                    },
                    error : function(result){
                        autoAlert("系统出错",5);
                    }
                });
            });
        }else{
            autoAlert("请选择删除的api信息!",5);
        }
    }

    //编辑
    function on_edit(id,name){
        var apiName = $("#apiName").val();
        parent.layer.open({
            type: 2,
            title: "编辑"+apiName,
            shadeClose: true,//打开遮蔽
            shade: 0.6,
            maxmin: false, //开启最大化最小化按钮
            area: ["80%", "80%"],
            content: "/urms/apiConfig_edit?id="+id
        });
    }
</script>
</html>