<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>党建活动开展汇总</title>
    <link rel="stylesheet" href="/common/dangjian/css/iconfont.css" />
    <style>
		*{
			padding:0;
			margin:0;
		}
		.bor-none {
			border: none;
		}
		
		table {
			border-collapse: collapse;
		}
		
		.bor-top {
			border-top: 1px solid #333;
		}
		
		.bor-right {
			border-right: 1px solid #333;
		}
		
		.col-5 {
			width: 5%;
		}
		
		.col-10 {
			width: 10%;
		}
		
		.col-15 {
			width: 15%;
		}
		tr{
			height:60px;
		}
		td {
			text-align: center;
			min-height:100px;
		}
		a{
			text-decoration: none;
			display: block;
			text-align: left;
			margin-bottom:5px;
			color:#2369b6;
			font-size:14px;
		}
	</style>
</head>
<body>
	
	<!-- <table border="1px solid #333" cellspacing="0" cellpadding="0" id="">
		<tr class="tit-bor">
			<td ></td>
		</tr>
	</table> -->
	<div style=" height:60px; line-height: 60px;">
		<span style="margin-left: 20px;">活动总积分:</span><span style="color: red;" id="point"></span>
		<span style="margin-left: 20px;">（其中亮点工作得分：</span><span style="color: red;" id="ldpoint"></span><span>）</span>
		<button class="btn btn-primary" type="button" onclick="on_export()"><i class="fa fa-file-excel-o"></i>&nbsp;导出Excle</button>
	</div>


	<table border="1px solid #333" cellspacing="0" cellpadding="0" id="tableconent">
		
	</table>
		

</body>
<script type="text/javascript"> 
var mPoints=0;//活动总积分
var mLDPoints=0;//亮点工作的积分

$().ready(function() {
	var atList = '${atList}';
	var alList = '${alList}';
	var year = '${year}';
	var branchName = '${branchName}';
	
	var htmlStr='';
	htmlStr+='<tr class="tit-bor"><td rowspan="2" class="col-10">月份</td><td class="" colspan="10">'+year+'年'+branchName+'各月工作计划及实施</td></tr>';
	
	htmlStr+= initTitle(atList);
	htmlStr+= initActivitiesLaunch(alList);
	var ob=document.getElementById("tableconent");
	ob.innerHTML =htmlStr;
	initPoints();
});



function initTitle(atList){
	var josnAt=JSON.parse(atList);
	var tdHtml="";
	for(var i = 0; i < josnAt.length; i++){
		tdHtml+='<td class="col-10">';
		tdHtml+=josnAt[i].title;
		tdHtml+='<p>（频率：'+ changeDataDictByKey("dj_activities_frequency",josnAt[i].frequency) +'）</p>';
		tdHtml+='</td>';
	}
	return tdHtml;
}

function initActivitiesLaunch(alList){
	var josnAl=JSON.parse(alList);
	var trHtml="";
	for(var i = 0; i < josnAl.length; i++){
		var m=josnAl[i].month;
		trHtml +='<tr>'; 
		trHtml +='<td>'+m+'</td>';
		
		var aList=josnAl[i].atList;
		for(var m = 0; m < aList.length; m++){
			var ar=aList[m];
			if(ar!=null){
				trHtml +='<td>';
				if(ar.length>0){
					//有活动内容
					for(var j = 0; j < ar.length; j++){
						var str=ar[j].split('|');
						if(str.length>0){
							var alTime=str[0];//活动开展时间
							var alId=str[1];//活动开展的Id
							var alPoint=str[2];//活动开展的积分
							var alFrequency=str[3];//活动频率
							var _href="/dangjian/activitiesLauch_detail?id="+alId;
							trHtml +='<a href="'+_href+'" ><i class="icon iconfont icon-jiantou1"></i>'+alTime+';</a>';
							//trHtml +='<a onclick="on_detail(this.url)" url="1"><i class="icon iconfont icon-jiantou1"></i>'+alTime+';</a>';
							if(alFrequency=='7'){
								//属于亮点工作
								mLDPoints= mLDPoints+parseInt(alPoint);
							}else{
								mPoints= mPoints+parseInt(alPoint);
							}
						}
					}
				}else{
					//无活动内容
					trHtml +='<a href=""></a>';
				}
				trHtml +='</td>';
			}
		}
		trHtml +='</tr>'; 
	}
	return trHtml;
}

function initPoints(){
	var p1=document.getElementById("point");
	p1.innerHTML =mPoints;
	var p2=document.getElementById("ldpoint");
	p2.innerHTML =mLDPoints;
	
}

//打开明细
function on_detail(_href){
	alert('a');
	alert(_href.url);
		parent.layer.open({
            type: 2,
            title: "党建活动明细",
            shadeClose: true,//打开遮蔽
            shade: 0.6, 
            maxmin: true, //开启最大化最小化按钮
            area: ["95%", "95%"],
            content: _href
        });
		
}


//导出Excel
function on_export(){
    var year = '${year}';
    var branchId = '${branchId}';
	window.location.href = "/dangjian/activitiesLauch_export?year="+ year + "&branchId=" + branchId
}

</script>
</html>