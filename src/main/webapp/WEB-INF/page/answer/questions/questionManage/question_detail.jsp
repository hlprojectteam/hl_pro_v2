<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>问题详情</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<%-- 题目类型 1：单选 --%>
<input type="hidden" id="type" name="type" value="1" />	
    <%-- 第1行 --%>
   	<div class="ibox-content">
   		<h3>题目：${questionVo.title }</h3>
  	</div>
<%-- 单选 --%>	
   <c:if test="${questionVo.type==1}">
   <div class="form-group">
	<div class="ibox-content">
	    <c:forEach var="questionProblem" items="${questionVo.questionProblems}" varStatus="status">
	       	<div>
	       		<div class="radio i-checks">
	       			<c:if test="${questionProblem.answer==1}">
	       				<label style="font-size: 18px;"><input checked="true" type="radio" value="" name="single" id=""/>
	       					${questionProblem.no}、${questionProblem.option}
	   					</label>
	       			</c:if>
	       			<c:if test="${questionProblem.answer!=1}">
	       				<label style="font-size: 18px;"><input type="radio" value="" name="single" id=""/>
	       					${questionProblem.no}、${questionProblem.option}
	   					</label>	
	       			</c:if>
	       		</div>
	       	</div>
	    </c:forEach>
	</div>
   </div>
   </c:if>
<%-- 多选 --%>	   
   <c:if test="${questionVo.type==2}">
	<div class="ibox-content">
	    <c:forEach var="questionProblem" items="${questionVo.questionProblems}" varStatus="status">
	       	<div>
	       		<div class="checkbox i-checks">
	       			<c:if test="${questionProblem.answer==1}">
		       			<label style="font-size: 18px;"><input checked="true" type="checkbox" value="" name="many" id="'"/>
		       				${questionProblem.no}、${questionProblem.option}
		       			</label>
	       			</c:if>
	       			<c:if test="${questionProblem.answer!=1}">
		       			<label style="font-size: 18px;"><input type="checkbox" value="" name="many" id="'"/>
		       				${questionProblem.no}、${questionProblem.option}
		       			</label>
	       			</c:if>
	       		</div>
	       	</div>
	    </c:forEach>
	</div>
   </c:if>
   <%-- 判断 --%>	
   <c:if test="${questionVo.type==3}">
   		<div class="ibox-content">
	    	<c:forEach var="questionProblem" items="${questionVo.questionProblems}" varStatus="status">
	    		<c:if test="${questionProblem.answer==1}">
	    			<label style="font-size: 18px;">答案：√</label>
	    		</c:if>
	    		<c:if test="${questionProblem.answer==0}">
	    			<label style="font-size: 18px;">答案：×</label>
	    		</c:if>
	    	</c:forEach>
    	</div>
   </c:if>
<%-- 填空 --%>	   
    <c:if test="${questionVo.type==4}">
    	<div class="ibox-content">
	    	<c:forEach var="questionProblem" items="${questionVo.questionProblems}" varStatus="status">
	    		<label style="font-size: 18px;">答案：${questionProblem.answer}</label>
	    	</c:forEach>
    	</div>
    </c:if>
   <%-- 错题人员 --%>
   <div class="ibox-content">
		<h2>错题人员</h2>
		<table id="grid"></table>
	</div>
   </div>
</form>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script type="text/javascript" src="/common/index/js/iCheck/icheck.min.js"></script>
<link rel="stylesheet" href="/common/index/css/iCheck/custom.css" type="text/css">
<script type="text/javascript">
$(function(){
	initICheck();
	loadGrid();
	$("input[name^='single']").each(function(){
		this.disabled = true;
	});
	$("input[name^='many']").each(function(){
		this.disabled = true;
	});
	$("select").each(function(){
		this.disabled = true;
	});
});
function initICheck(){
	//自定义样式
	 $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
    });
}

//加载列表
function loadGrid() {
	$("#grid").bootstrapTable({
		url: "/answer/worryPerson_load?id=${questionVo.id}&examManageId=${examManageId}",
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
	              {title: "姓名",field: "personName",width: 100,align:"center"}, 
	              {title: "工号",field: "jobNumber",width: 100,align:"center"}, 
	              {title: "部门",field: "orgFrameName",width: 100,align:"center"}, 
	              {title: "所作答案",field: "personAnswer",width: 200,align:"center"}]
	 });
}
</script>
</html>