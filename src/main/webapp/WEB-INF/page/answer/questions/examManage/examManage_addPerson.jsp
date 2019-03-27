<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>新增考试人员</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
	<input type="hidden" name="id" value="${examManageId}">
	<div class="form-group" id="selectUser">
	    <label class="col-sm-2 control-label">人员选择</label>
	    <div class="col-sm-8">
	    	<input type="hidden" id="userIds" name="userIds" value=""/>
	    	<textarea class="form-control" rows="8" id="userNames" name="userNames" data-rule-required="true"  onfocus="this.blur()"></textarea>
	    </div>
	    <div class="col-sm-2">
	    	<button class="btn btn-primary " type="button" onclick="choose_user('userIds','userNames')"><i class="fa fa-external-link"></i>&nbsp;选择人员</button>
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
var examManageId = '${examManageId}';
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
		url: '/answer/examManage_saveAddPerson',
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
				autoMsg("保存成功！",1);
				parent.frames[winName].$("#grid").bootstrapTable("refresh",{url:"/answer/examPerson_load?examManage.id="+examManageId});//加载树下的列表
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