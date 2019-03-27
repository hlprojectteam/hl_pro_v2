<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>角色</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
<input type="hidden" style="width: 100%" id="id" name="id" value="${roleVo.id }"/>
<input type="hidden" id="orgFrameId" name="orgFrame.id" value="${roleVo.orgFrame.id}"/>
<input type="hidden" id="creatorId" name="creatorId" value="${roleVo.creatorId}"/><%-- 创建人id --%>
<input type="hidden" id="creatorName" name="creatorName" value="${roleVo.creatorName}"/><%-- 创建人 --%>
	<br><br><br>
  <div class="form-group">
    <label class="col-sm-3 control-label"><span style="color: red">*</span>角色名称</label>
    <div class="col-sm-8">
      	<input type="text" class="form-control" id="roleName" name="roleName" value="${roleVo.roleName}" data-rule-rangelength="[1,20]"/>
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-3 control-label"><span style="color: red">*</span>角色编码</label>
    <div class="col-sm-8">
      	<input type="text" class="form-control" id="roleCode" name="roleCode" value="${roleVo.roleCode}" data-rule-rangelength="[1,32]"/>
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-3 control-label"><span style="color: red">*</span>所属子系统</label>
    <div class="col-sm-8">
        <select id="subsystemSelect" name="sysCode" class="form-control m-b required">
        </select>
    </div>
  </div>
</form>
</div>
<!-- 底部按钮 -->
<div class="footer edit_footer">
<div class="pull-right">
<button class="btn btn-primary " type="button" onclick="on_save()"><i class="fa fa-check"></i>&nbsp;保存</button>
<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>
</body>
<script type="text/javascript"> 
$().ready(function() {
	getSubsystem();		 
});
//新增保存更新
function on_save(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
       on_submit();//提交表单.  
   } 
}
iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/urms/role_load"});//加载树下的列表
//保存
function on_submit(){  
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/urms/role_saveOrUpdate',
		data:$('#baseForm').serialize(),
		success : function(result){
			if(result.result){
				autoMsg("保存成功！",1);
				iframeIndex.$("#grid").bootstrapTable("refresh");//加载树下的列表
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

//获得子系统
function getSubsystem(){
	$.ajax({
		type:'post',
		async:false,
		dataType : 'json',
		url: '/urms/subsystem_getSubsystem',
		success : function(data){
			var sysCode = '${roleVo.sysCode}';
			if(data!=null){
				$("#subsystemSelect").append("<option value=''>------请选择------</option>");
				 for (var i in data) {
				 	if(sysCode==data[i].sysCode)
				 		$("#subsystemSelect").append("<option value='"+data[i].sysCode+"' selected='selected'>"+data[i].sysName+"</option>");
				 	else
				 		$("#subsystemSelect").append("<option value='"+data[i].sysCode+"'>"+data[i].sysName+"</option>");
				 }
			}
		},
	});
}
</script>
</html>