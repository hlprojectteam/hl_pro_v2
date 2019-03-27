<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>系统配置</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" name="baseForm" class="form-horizontal" action="" >
<input type="hidden" id="id" name="id" value="${apiConfigVo.id }"/>
<input type="hidden" id="apiType" name="apiType" value="1"/>
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${apiConfigVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"/>
	<br>
    <div class="form-group">
  	    <label class="col-sm-2 control-label"><span style="color: red">*</span>平台名称</label>
  	    <div class="col-sm-4">
  	      	<input type="text" class="form-control" id="apiName" name="apiName" value="${apiConfigVo.apiName}" data-rule-required="true" />
  	    </div>
  	    <label class="col-sm-2 control-label"><span style="color: red">*</span>平台网站</label>
  	    <div class="col-sm-4">
  	      	<input type="text" class="form-control" id="apiSite" name="apiSite" value="${apiConfigVo.apiSite}" data-rule-required="true" />
  	    </div>
    </div>
	<div class="form-group">
		<label class="col-sm-2 control-label">请求链接</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="apiUrl" name="apiUrl" value="${apiConfigVo.apiUrl}" />
		</div>
		<label class="col-sm-2 control-label">密匙(APIKEY)</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="apiKey" name="apiKey" value="${apiConfigVo.apiKey}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">用户名</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="apiUserName" name="apiUserName" value="${apiConfigVo.apiUserName}" />
		</div>
		<label class="col-sm-2 control-label">登录密码</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="apiPassword" name="apiPassword" value="${apiConfigVo.apiPassword}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">标记(Token)</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="apiToken" name="apiToken" value="${apiConfigVo.apiToken}" />
		</div>
		<label class="col-sm-2 control-label">发送号码</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="sendPhoneNum" name="sendPhoneNum" value="${apiConfigVo.sendPhoneNum}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">文本编码</label>
		<div class="col-sm-4">
			<opt:select dictKey="API_URLEncoder" classStyle="form-control" id="apiURLEncoder" name="apiURLEncoder" value="${apiConfigVo.apiURLEncoder}" isDefSelect="true" />
		</div>
		<label class="col-sm-2 control-label">请求方式</label>
		<div class="col-sm-4">
			<opt:select dictKey="API_RequestMethod" classStyle="form-control" id="apiRequestMethod" name="apiRequestMethod" value="${apiConfigVo.apiRequestMethod}" isDefSelect="true" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">请求全称</label>
		<div class="col-sm-10">
			<input type="text" class="form-control" id="apiAllURL" name="apiAllURL" value="${apiConfigVo.apiAllURL}" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">调用最大次数</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="apiTotleNum" name="apiTotleNum" value="${apiConfigVo.apiTotleNum}" data-rule-number="true"/>
		</div>
		<label class="col-sm-2 control-label">已使用次数</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="apiUseNum" name="apiUseNum" value="${apiConfigVo.apiUseNum}" data-rule-number="true"/>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">是否收费</label>
		<div class="col-sm-4">
			<opt:select dictKey="isNot" classStyle="form-control" id="apiIsPay" name="apiIsPay" value="${apiConfigVo.apiIsPay}" isDefSelect="true" />
		</div>
		<label class="col-sm-2 control-label">状态</label>
		<div class="col-sm-4">
			<opt:select dictKey="state" classStyle="form-control" id="apiState" name="apiState" value="${apiConfigVo.apiState}" isDefSelect="true" />

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
		url: '/urms/apiConfig_saveOrUpdate',
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
</script>
</html>