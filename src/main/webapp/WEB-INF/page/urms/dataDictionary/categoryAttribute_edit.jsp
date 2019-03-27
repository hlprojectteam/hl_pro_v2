<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>字典</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="id" name="id" value="${categoryAttributeVo.id}"/>
<input type="hidden" id="pId" name="category.id" value="${categoryAttributeVo.category.id}"/>
	<br><br><br>
	<div class="form-group">
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>字典键</label>
	  <div class="col-sm-4">
	    <input type="text" class="form-control" id="attrKey" name="attrKey" value="${categoryAttributeVo.attrKey}" data-rule-required="true" data-rule-rangelength="[1,20]"/>
	  </div>
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>字典值</label>
	  <div class="col-sm-4">
	    <input type="text" class="form-control" id="attrValue" name="attrValue" value="${categoryAttributeVo.attrValue}" data-rule-required="true" data-rule-rangelength="[1,30]"/>
	  </div>
	</div>
	<div class="form-group">
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>排序</label>
	  <div class="col-sm-4">
	    <input type="text" class="form-control" id="order" name="order" value="${categoryAttributeVo.order}" data-rule-digits="true"/>
	  </div>
	  <label class="col-sm-2 control-label">是否默认值</label>
	  <div class="col-sm-4">
	  	<opt:radio dictKey="isNot" id="isDefault" name="isDefault" value="${categoryAttributeVo.isDefault}"/>
	  	<%-- <opt:select dictKey="isNot" classStyle="form-control" name="isDefault" id="isDefault" value="${categoryAttributeVo.isDefault}" isDefSelect="true" /> --%>
	  </div>
	</div>
</form>
</div>
<br><br>
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
		url: '/urms/categoryAttr_saveOrUpdate',
		data:$('#baseForm').serialize(),
		success : function(result){
			if(result.result){
				autoMsg("保存成功！",1);
				iframeIndex.$("#gridAttr").bootstrapTable("refresh",{url:"/urms/categoryAttr_load?id="+$("#pId").val()});//加载树下的列表
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