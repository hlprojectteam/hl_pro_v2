<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>考试管理报表</title>
<!-- ztree -->
<link rel="stylesheet" href="/common/plugins/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/common/plugins/ztree/js/jquery.ztree.all-3.5.js"></script>
</head>
<body>
<div class="ibox-content">
<input type="hidden" id="id" name="id" value="${examManageVo.id}" />

<div class="col-sm-2">
	<ul id="tree" class="ztree"></ul>
</div>	
<div class="col-sm-10">
 <div class="row">
<%-- 完成率 --%>
<div class="col-sm-6">
	<div class="ibox">
	    <div class="ibox-content">
	        <h2>完成率</h2>
	        <div id="completion"></div>
	    </div>
	</div>
</div>
<%-- 及格率 --%>
<div class="col-sm-6">
	<div class="ibox">
	    <div class="ibox-content">
	        <h2>及格率</h2>
	         <div id="pass"></div>
	    </div>
	</div>
</div>
</div>
<div class="row">
	<div class="ibox-content">
		<h2>错题排行</h2>
		<table id="grid"></table>
	</div>
</div>
<div class="row">
	<div class="ibox-content">
	<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
	<div id="main" style="width: 600px;height:400px;"></div>
	</div>
</div>
</div>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<%-- 引入报表 --%>
<script src="/common/plugins/Echarts/echarts4.0.js" type="text/javascript"></script>
<script type="text/javascript">
// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('main'));
myChart.setOption({
	color: ['#3398DB'],//线条颜色
    title: {
        text: '成绩分布人数(人数/分数段)'
    },
    tooltip: {},
    legend: {
        data:['成绩']
    },
    xAxis: {
        data: []
    },
    yAxis: {},
    series: [{
        name: '分数段人数',
        type: 'bar',
        data: []
    }]
});

//分数图表
function sourceReport(orgFrameId){
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/examOnline_sourceReport?examManageId=${examManageVo.id}&orgFrameId='+orgFrameId,
		success : function(data){
			myChart.setOption({
		        xAxis: {
		            data: data.categories
		        },
		        series: [{
		            // 根据名字对应到相应的系列
		            name: '分数成绩分布',
		            data: data.data
		        }]
		    });
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}

$(document).ready(function(){
	zTreeObj = $.fn.zTree.init($("#tree"), setting);//初始化ztree
	sourceReport("");//分数图表
	loadGrid("");//加载列表
	getBaseReport("");//完成率 及格率
});

function zTreeOnAsyncSuccess(){
    expandNodes(zTreeObj.getNodes());//获得当前树节点
}

function expandNodes(nodes){
	for (var i=0, l=nodes.length; i<l; i++) {
		zTreeObj.expandNode(nodes[i], true, false, false);
		if (nodes[i].isParent && nodes[i].zAsync) {//判断是否叶节点
			expandNodes(nodes[i].children);
		}
	}
}

var setting = {
		data: {
			simpleData: {
				enable: true
			}
		},
		callback:{
			onClick: zTreeOnClick,
			onAsyncSuccess: zTreeOnAsyncSuccess//异步正常调用后触发
		},
		async: {//异步加载
			enable: true,
			type: "post",
			dataType: "text",
			contentType:"application/x-www-form-urlencoded",
			url: "/urms/orgFrame_loadTree",
			autoParam: ["id"]
		}
	};
	
//点击树后回调
function zTreeOnClick(event, treeId, treeNode) {
	sourceReport(treeNode.id);
	getBaseReport(treeNode.id);//当部门
	$("#grid").bootstrapTable("refresh",{url:"/answer/worryReport_load?id=${examManageVo.id}&orgFrameId="+treeNode.id});//加载树下的列表
}

//加载列表
function loadGrid(orgFrameId) {
	$("#grid").bootstrapTable({
		url: "/answer/worryReport_load?id=${examManageVo.id}&orgFrameId="+orgFrameId,
		dataType:"json",
		method:"post",
		queryParamsType: "limit",//服务器分页必须
		striped:true,//条纹行
		contentType: "application/x-www-form-urlencoded",
		pageSize:5,//每页大小
		pageNumber:1,//开始页
		pageList:[10,20,50],
		pagination:true,//显示分页工具栏
		sidePagination: "server", //服务端请求
		queryParams: queryParams,//发送请求参数
	    columns: [{checkbox:true},
	              {title: "序号",field: "id",align:"center",width:50,formatter:function(value,row,index){
	           	  		return index+1;
	              }},
	              {title: "题目",field: "title",width: 200,align:"center"}, 
	              {title: "题型",field: "type",width: 200,align:"center",formatter:function(value,row,index){
	            	  if(value==1) return "单选题";
	            	  if(value==2) return "不定项题";
	            	  if(value==3) return "判断题";
	            	  if(value==4) return "填空题";
	              }}, 
	              {title: "错题率",field: "worryPercent",width: 60,align:"center"}, 
	              {title: "错题数量",field: "worryNum",width: 60,align:"center"}, 
				  {title: "操作", field: "", width: 60,align:"center",formatter:function(value,row,index){
					  return "<a href='#' onclick='on_detail(\""+row.id+"\")'>详情</a>";
	              }}]
	 });
}

//错题详情 id:问题id
function on_detail(id){
	parent.layer.open({
        type: 2,
        title: "详情",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["75%", "100%"],
        content: "/answer/getQuestion?id="+id+"&examManageId=${examManageVo.id}"
    });
}

//完成率 及格率
function getBaseReport(orgFrameId){
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/getBaseReport?id=${examManageVo.id}&orgFrameId='+orgFrameId,
		success : function(data){
			//完成率
			var completion = '<h3>'+data.completion +' ('+data.completionPerson +'/'+data.allPerson+' )</h3>'+
		        '<div class="progress progress-mini">'+
           		'<div style="width: '+data.completion +';" class="progress-bar"></div>'+
        		'</div>';
			$("#completion").html(completion);
			//及格率
			var pass = '<h3>'+data.pass +' ('+data.passPerson +'/'+data.allPerson+' )</h3>'+
	        '<div class="progress progress-mini">'+
       		'<div style="width: '+data.pass +';" class="progress-bar"></div>'+
    		'</div>';
			$("#pass").html(pass);
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}
</script>
</html>