<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>考试成绩</title>
</head>
<body>
<div class="ibox-content">
<table class="table text-center">
	<tr>
		<th></th>
		<c:if test="${!empty examManageVo.single}">
			<th class="text-center">单选题</th>
		</c:if>
		<c:if test="${!empty examManageVo.many}">
			<th class="text-center">多选题</th>
		</c:if>
		<c:if test="${!empty examManageVo.judge}">
			<th class="text-center">判断题</th>
		</c:if>
		<c:if test="${!empty examManageVo.fill}">
			<th class="text-center">填空题</th>
		</c:if>
	</tr>
	<tr>
		<td>题数</td>
		<c:if test="${!empty examManageVo.single}">
			<td>${examManageVo.single }</td>
		</c:if>
		<c:if test="${!empty examManageVo.many}">
			<td>${examManageVo.many }</td>
		</c:if>
		<c:if test="${!empty examManageVo.judge}">
			<td>${examManageVo.judge }</td>
		</c:if>
		<c:if test="${!empty examManageVo.fill}">
			<td>${examManageVo.fill }</td>
		</c:if>
	</tr>
	<tr>
		<td>答对</td>
		<c:if test="${!empty examManageVo.single}">
			<td>${examPersonVo.singleRight }</td>
		</c:if>
		<c:if test="${!empty examManageVo.many}">
			<td>${examPersonVo.manyRight }</td>
		</c:if>
		<c:if test="${!empty examManageVo.judge}">
			<td>${examPersonVo.judgeRight }</td>
		</c:if>
		<c:if test="${!empty examManageVo.fill}">
			<td>${examPersonVo.fillRight }</td>
		</c:if>
	</tr>
	<tr>
		<td>得分</td>
		<c:if test="${!empty examManageVo.single}">
			<td>${examPersonVo.singleTotalSource }</td>
		</c:if>
		<c:if test="${!empty examManageVo.many}">	
			<td>${examPersonVo.manyTotalSource }</td>
		</c:if>
		<c:if test="${!empty examManageVo.judge}">	
			<td>${examPersonVo.judgeTotalSource }</td>
		</c:if>
		<c:if test="${!empty examManageVo.fill}">
			<td>${examPersonVo.fillTotalSource }</td>
		</c:if>	
	</tr>
</table>
<div class="well" style="background-color: #87CEEB;">
	<div id="" class="text-center"><h2><span id="showTime"></span>&nbsp;&nbsp;&nbsp;&nbsp;成绩：${examPersonVo.totalSource }</h2></div>
</div>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_viewExam()"><i class="fa fa-search"></i>&nbsp;查看试卷</button>
<%-- 普通测试模式 --%>
<c:if test="${examManageVo.type==1}">
	<c:if test="${sessionScope.user.id==examPersonVo.personId}"><%-- 当前登录人是查看人 --%>
	<button class="btn btn-primary " type="button" onclick="on_examRedo()"><i class="fa fa-edit"></i>&nbsp;试卷重做</button>
	<button class="btn btn-primary " type="button" onclick="on_worryExamRedo()"><i class="fa fa-file-word-o"></i>&nbsp;错题重做</button>
	</c:if>
</c:if>	
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
	
<script type="text/javascript"> 
$(document).ready(function(){
	var ss = '${examPersonVo.consumeTime }';//耗时
	if(ss<60){
		html = ss;
	}else{
		html = Math.floor(ss/60)+":"+(ss%60);
	}
	$("#showTime").html("耗时  "+html);
});

//查看试卷 mark:2 显示答案
function on_viewExam(){
	var indexP = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.open({
        type: 2,
        title: "查看考题",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["100%", "100%"],
        content: "/answer/examOnline_start?id=${examManageVo.id }&mark=2&redo=${redo}&personId=${examPersonVo.personId}",
     	success: function(layero, index){
          	parent.layer.close(indexP);
        }
    });
}

//错题重做
function on_worryExamRedo(){
	var indexP = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.open({
        type: 2,
        title: "错题重做",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["100%", "100%"],
        content: "/answer/examOnline_start?id=${examManageVo.id }&mark=1&sign=1&redo=${redo}",
        success: function(layero, index){
        	parent.layer.close(indexP);
        }
    });
} 

//试题重做
function on_examRedo(){
	var indexP = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	parent.layer.open({
        type: 2,
        title: "试题重做",
        shadeClose: true,//打开遮蔽
        shade: 0.6, 
        maxmin: true, //开启最大化最小化按钮
        area: ["100%", "100%"],
        content: "/answer/examOnline_start?id=${examManageVo.id }&mark=1&redo=${redo}",
        success: function(layero, index){
        	parent.layer.close(indexP);
        }
    });
} 
</script>
</body>
</html>