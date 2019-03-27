<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>问题反馈</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="id" name="id" value="${feedbackProblemVo.id}"/>
<input type="hidden" id="sysCode" name="sysCode" value="${feedbackProblemVo.sysCode}"/>
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value="${feedbackProblemVo.createTime}"  pattern="yyyy-MM-dd HH:mm:ss"/>"/>
	<div class="ibox-title">
    	<h5>问题反馈</h5>
	</div>
	<div class="form-group">
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>反馈人类型</label>
	  <div class="col-sm-4">
	  	<opt:select dictKey="feedbackPeopleType" classStyle="form-control" id="feedbackPeopleType" name="feedbackPeopleType" isDefSelect="true" value="${feedbackProblemVo.feedbackPeopleType}"/>
	  </div>
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>反馈类型</label>
	  <div class="col-sm-4">
	  	<opt:select dictKey="feedbackType" classStyle="form-control" id="feedbackType" name="feedbackType" isDefSelect="true" value="${feedbackProblemVo.feedbackType}"/>
	  </div>
	</div>
	<div class="form-group">
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>反馈人姓名</label>
	  <div class="col-sm-4">
	    <input type="text" class="form-control" id="feedbackPeople" name="feedbackPeople" data-rule-required="true" data-rule-rangelength="[1,10]" value="${feedbackProblemVo.feedbackPeople}"/>
	  </div>
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>反馈人联系电话</label>
	  <div class="col-sm-4">
	    <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" data-rule-required="true" data-rule-rangelength="[1,13]" value="${feedbackProblemVo.phoneNumber}"/>
	  </div>
	</div>
	<div class="form-group">
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>问题所属菜单</br>(如:基础库-建筑信息管理)</label>
	  <div class="col-sm-10">
	    <input type="text" class="form-control" id="typeCode" name="typeCode" data-rule-required="true" value="${feedbackProblemVo.typeCode}"/>
	  </div>
	</div>
	<div class="form-group">
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>反馈内容</label>
	  <div class="col-sm-10">
	  	<textarea name="description" id="description" class="form-control" rows="4">${feedbackProblemVo.description}</textarea>
	  </div>
	</div>
	<div class="ibox-title">
    	<h5>处理情况</h5>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"><span style="color: red">*</span>处理状态</label>
		 <div class="col-sm-3">
	  		<opt:select dictKey="feedbackProblemState" classStyle="form-control" id="state" name="state" value="${feedbackProblemVo.state}"/>
	  	</div>
	</div>
	<div class="form-group">
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>处理意见</label>
	  <div class="col-sm-10">
	  	<textarea name="handlingSuggestion" id="handlingSuggestion" class="form-control" rows="4">${feedbackProblemVo.handlingSuggestion}</textarea>
	  </div>
	</div>
	<div class="form-group">
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>处理结果</label>
	  <div class="col-sm-10">
	  	<textarea name="handlingResults" id="handlingResults" class="form-control" rows="4">${feedbackProblemVo.handlingResults}</textarea>
	  </div>
	</div>
</form>
</div>
<%-- 底部按钮 --%>
<div class="footer edit_footer">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;保存</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script type="text/javascript"> 
$().ready(function() {
	 $("#baseForm").validate();//初始化验证
});
//新增保存更新
function on_save(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
        on_submit();//提交表单.  
    } 
}

//保存
function on_submit(){  
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/urms/feedbackProblem_saveOrUpdate',
		data:$('#baseForm').serialize(),
		success : function(result){
			if(result.result){
				autoMsg("保存成功！",1);
				iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/urms/feedbackProblem_load"});//加载树下的列表
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