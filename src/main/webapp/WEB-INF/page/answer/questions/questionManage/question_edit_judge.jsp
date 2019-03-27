<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>判断题目</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="id" name="id" value="${questionVo.id}" />
<input type="hidden" id="questionManageId" name="questionManage.id" value="${questionManageId}" />
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value="${questionVo.createTime}"  pattern="yyyy-MM-dd HH:mm:ss"/>"/>
<%-- 题目类型 3：判断 --%>
<input type="hidden" id="type" name="type" value="3" />	
    <%-- 第1行 --%>
	<div class="form-group">
   		<label class="col-sm-1 control-label"><span style="color: red">*</span>题目</label>
    	<div class="col-sm-11">
    		<input type="hidden" id="questionProblemOption" name="questionProblemOption" value="判断题">
    		<textarea  class="form-control" rows="6" id="title" name="title" data-rule-required="true" data-rule-rangelength="[1,2048]">${questionVo.title }</textarea>
   		</div>
	</div>
	<%-- 第2行 --%>
	<div class="form-group">
		<%-- id --%>
		<input type="hidden" id="" name="questionProblemId" value="${questionVo.questionProblemId }">
		<%-- 题号 --%>
		<input type="hidden" id="" name=questionProblemNo value="1">
		<label class="col-sm-1 control-label"><span style="color: red">*</span>是否正确</label>
		<div class="col-sm-1">
			<opt:select dictKey="isNot" isDefSelect="false" id="" name="questionProblemAnswer" value="${questionVo.questionProblemAnswer }" classStyle="form-control m-b required"/>
		</div>
   		<label class="col-sm-1 control-label"><span style="color: red">*</span>状态</label>
   		<div class="col-sm-1">
   			<opt:select dictKey="state" isDefSelect="false" id="state" name="state" value="${questionVo.state }" classStyle="form-control m-b required"/>
   		</div>
		<label class="col-sm-2 control-label"><span style="color: red">*</span>是否随机</label>
    	<div class="col-sm-1">
    		<opt:select dictKey="isNot" id="isRandom" name="isRandom" value="${questionVo.isRandom }" classStyle="form-control"/>
   		</div>
	</div>
	
</form>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;保存</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script type="text/javascript">
var winName = '${winName}';
$(function(){
});

//新增保存更新
function on_save(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
        on_submit();//提交表单.  
    } 
}

function on_submit(){  
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/question_saveOrUpdateJudge',
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
				autoMsg("保存成功！",1);
				parent.frames[winName].$("#gridJudge").bootstrapTable("refresh",{url:"/answer/question_load?type=3&questionManageId=${questionManageId}"});//加载树下的列表
				parent.layer.close(index);
			}else{
				autoAlert("保存失败，请检查！",5);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}

</script>
</html>