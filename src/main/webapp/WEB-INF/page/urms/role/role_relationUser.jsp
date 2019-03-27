<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>关联用户</title>
</head>
<body>
<div class="ibox-content">
	<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
	<div class="form-group">
    	<label class="col-sm-2 control-label"><span style="color: red">*</span>关联用户</label>
    	<button class="btn btn-primary " type="button" onclick="choose_user('userIds','userNames')"><i class="fa fa-external-link"></i>&nbsp;选择用户</button>
  	</div>
	<div class="form-group">
		<div class="col-sm-12">
		<input type="hidden" id="roleId" name="roleId" value="${roleId }">
		<input type="hidden" id="userIds" name="userIds" value="${userIds }">
    	<textarea rows="8" style="width: 100%;" name="userNames" id="userNames" required="required" data-msg-required="请选择用户" class="form-control">${userNames }</textarea>
  		</div>
  	</div>
</form>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_clean()"><i class="fa fa-eraser"></i>&nbsp;清除</button>
<button class="btn btn-primary " type="button" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;保存</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script>
//保存or修改
function on_save(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
        on_submit();//提交表单.  
    } 
}

//提交保存
function on_submit(){ 
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/urms/role_saveRelationUser',
		data:$('#baseForm').serialize(),
		success : function(result){
			if(result.result){
				autoMsg("保存成功！",1);
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

//清除
function on_clean(){
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/urms/role_cleanRelationUser',
		data:$('#baseForm').serialize(),
		success : function(result){
			if(result.result){
				$("#userIds").val("");
				$("#userNames").val("");
				autoMsg("清除成功！",1);
			}else{
				autoAlert("清除失败，请检查！",5);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}
</script>
</html>