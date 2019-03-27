<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>用户自己查看</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="" >
<div class="tab-content ">
<ul class="nav nav-tabs">
    <li class="active"><a data-toggle="tab" href="#base">基本信息</a></li>
    <li class=""><a data-toggle="tab" href="#safe">安全信息</a></li>
</ul>
<%-- 基础信息 --%>
<div id="base" class="tab-pane active">
	<div class="ibox-content">
	<input type="hidden" id="id" name="id" value="${userVo.id}"/>
	<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value="${userVo.createTime}"  pattern="yyyy-MM-dd HH:mm:ss"/>"/>
	<input type="hidden" id="state" name="state" value="${userVo.state}"/><%-- 用户状态 --%>
	<input type="hidden" id="type" name="type" value="${userVo.type}"/><%-- 用户类型 --%>
	<input type="hidden" id="sysCode" name="sysCode" value="${userVo.sysCode}"/><%-- 所属子系统 --%>
	<input type="hidden" id="orgFrameId" name="orgFrame.id" value="${userVo.orgFrame.id}"/>
	<input type="hidden" id="creatorId" name="creatorId" value="${userVo.creatorId}"/><%-- 创建人id --%>
	<input type="hidden" id="creatorName" name="creatorName" value="${userVo.creatorName}"/><%-- 创建人 --%>
		<div class="col-sm-9">
		<div class="form-group">
			<label class="col-sm-2 control-label"><span style="color: red">*</span>登录账号</label>
			<div class="col-sm-4">
      			<input type="text" class="form-control" id="loginName" name="loginName" value="${userVo.loginName }" autocomplete="off" readonly="readonly" data-rule-required="true" data-rule-numbersEnglish="false" data-rule-rangelength="[1,30]" data-rule-remote="/urms/user_checkUserLoginName?id=${userVo.id}" data-msg-remote="登录名称已经存在，请重新输入"/>
   			</div>
			<label class="col-sm-2 control-label">姓名</label>
		    <div class="col-sm-4">
		      	<input type="text" class="form-control" id="userName" name="userName" value="${userVo.userName}" data-rule-rangelength="[1,5]"/>
		    </div>
	    </div>
	    <div class="form-group">
	    <label class="col-sm-2 control-label">性别</label>
	    <div class="col-sm-4">
	      	<opt:select dictKey="sex" isDefSelect="true" id="sex" name="sex" value="${userVo.sex }" classStyle="form-control m-b"/>
	    </div>
	    <label class="col-sm-2 control-label">年龄</label> 
	    <div class="col-sm-4">
	      	<input type="text" class="form-control" id="age" name="age" value="${userVo.age}" data-rule-rangelength="[1,3]"  data-rule-digits="true"/>
	    </div>
		</div>
		<div class="form-group">
		<label class="col-sm-2 control-label">手机</label>
	    <div class="col-sm-4">
  	      	<input type="text" class="form-control" id="mobilePhone" name="mobilePhone" value="${userVo.mobilePhone }" autocomplete="off" data-rule-mobilephone="true" data-rule-remote="/urms/user_checkMobilePhone?id=${userVo.id}" data-msg-remote="手机号码已经存在，请重新输入"/>
	    </div>
		
		<label class="col-sm-2 control-label">工号</label>
		<div class="col-sm-4">
			<input type="text" class="form-control" id="jobNumber" name="jobNumber" value="${userVo.jobNumber}" data-rule-rangelength="[1,8]" />
		 </div>
		</div>
		</div>
		<div class="col-sm-3">
		<%-- 照片 --%> 
			<div id="uploaderDiv" class="wu-example">
				<div class="queueList">
					<div id="dndArea" class="placeholder"> 
					    <div id="filePicker"></div>
						<!-- 图片列表 -->
					</div>
				</div>
			</div>
		<%-- 照片 --%>	
		</div>
	<div class="col-sm-12">
		<div class="form-group">
		
	    <label class="col-sm-1 control-label">出生日期</label>
		<div class="col-sm-3">
			<input type="text" class="form-control" id="birthday" name="birthday" value="<fmt:formatDate value='${userVo.birthday}' pattern='yyyy-MM-dd'/>" onfocus="this.blur();" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
		</div>
	    <label class="col-sm-1 control-label">固话</label>
	    <div class="col-sm-3">
	      	<input type="text" class="form-control" id="telephone" name="telephone" value="${userVo.telephone}" data-rule-telephone="true"/>
	    </div>
	  	<label class="col-sm-1 control-label">Email</label>
	    <div class="col-sm-3">
   		    <input type="text" class="form-control" id="email" name="email" value="${userVo.email }" autocomplete="off" data-rule-email="true" data-rule-remote="/urms/user_checkEmail?id=${userVo.id}" data-msg-remote="Email已经存在，请重新输入"/>
	    </div>	    
	 </div>
	
		<div class="form-group">
		<label class="col-sm-1 control-label">民族</label>
		<div class="col-sm-3">
	    	<opt:select dictKey="national" isDefSelect="true" id="nation" name="nation" value="${userVo.nation }" classStyle="form-control m-b"/>
	    </div>
	    <label class="col-sm-1 control-label">身份证号</label>
	     <div class="col-sm-3">
	      	<input type="text" class="form-control" id="certificatesNum" name="certificatesNum" value="${userVo.certificatesNum}" data-rule-rangelength="[1,32]"/>
	     </div>
		<label class="col-sm-1 control-label">QQ</label>
		 <div class="col-sm-3">
			<input type="text" class="form-control" id="qq" name="qq" value="${userVo.qq}" data-rule-rangelength="[1,15]" />
		 </div>
		</div>
			 
	 
	 
	  <div class="form-group">
	    <label class="col-sm-1 control-label">学历</label>
		<div class="col-sm-3">
	    	<opt:select dictKey="population_backgroud" isDefSelect="true" id="educationBackground" name="educationBackground" value="${userVo.educationBackground }" classStyle="form-control m-b"/>
	    </div>
	     <label class="col-sm-1 control-label">学位</label>
		<div class="col-sm-3">
	    	<opt:select dictKey="population_degree" isDefSelect="true" id="degree" name="degree" value="${userVo.degree }" classStyle="form-control m-b"/>
	    </div>
	    <label class="col-sm-1 control-label">籍贯</label>
	    <div class="col-sm-3">
  	      	<input type="text" class="form-control" id="originPlace" name="originPlace" value="${userVo.originPlace }" autocomplete="off" data-rule-rangelength="[1,50]"/>
	    </div>
	 </div>
	 
	 <div class="form-group">
	    <label class="col-sm-1 control-label">地址</label>
	    <div class="col-sm-11">
	      	<input type="text" class="form-control" id="address" name="address" value="${userVo.address}" data-rule-rangelength="[1,64]"/>
	    </div>
	 </div>
	 <div class="form-group">
	 	<label class="col-sm-1 control-label">备注</label>
	 	 <div class="col-sm-11">
	 	 	<textarea class="form-control" id="memo" name="memo" data-rule-rangelength="[1,256]" rows="1">${userVo.memo}</textarea>
	 	 </div>
	 </div>
	</div>
	</div>
</div>

<%-- 安全信息 --%>
<div id="safe" class="tab-pane">
<div class="ibox-content">
	<div class="form-group">
	    <label class="col-sm-2 control-label"><span style="color: red">*</span>密码</label>
	    <div class="col-sm-4">
	      	<input type="password" class="form-control" id="password" name="password" value="${userVo.password }" autocomplete="off" data-rule-required="true" data-rule-minlength="6" data-rule-maxlength="32" data-msg-required="密码不能为空" data-msg-minlength="至少设置6位密码"/>
	    </div>
	</div>   
	<div class="form-group">    
	    <label class="col-sm-2 control-label"><span style="color: red">*</span>确认密码</label>
	    <div class="col-sm-4">
	      	<input type="password" class="form-control" id="password2" name="" value="${userVo.password }" autocomplete="off" data-rule-equalTo="#password" data-rule-minlength="6" data-rule-maxlength="32" data-msg-equalTo="两次密码不一致"/>
	    </div>
	</div>
</div>	
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

<!-- 文件上传 -->
<link href="/common/plugins/webUploader/webuploader.css" rel="stylesheet">
<link href="/common/plugins/webUploader/user/user_photo.css" rel="stylesheet">
<script src="/common/plugins/webUploader/webuploader.js"></script>
<script src="/common/plugins/webUploader/photoByUserAvatar.js"></script>
<script>
$().ready(function() {
	getSubsystem();
});
//新增保存更新
function on_save(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
		var password = $("#password").val();//加密
		if(password.length>=30){
		   on_submit();
		}else{
		   $("#password").val(md5(password));
		   on_submit();
		}
    } 
}

//提交保存
function on_submit(){
	$.ajax({
		type: 'post',
		async: true,
		dataType: 'json',
		url: '/urms/user_saveOrUpdate',
		data: $('#baseForm').serialize(),
		success: function(data){
			if(data.result){
				if(uploader.getFiles().length>0){//检查上传对列是否有照片
					uploader.options.server = '/attach_upload?formId='+data.id+'&sysCode='+data.sysCode;
					uploader.upload();//上传
					uploader.on( 'uploadSuccess', function( file ) {
						 autoMsg("保存成功！",1);
						 iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/urms/user_load?sign=2"});//加载树下的列表
						 parent.layer.close(index);
					 });
				}else{
					autoMsg("保存成功！",1);
					iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/urms/user_load?sign=2"});//加载树下的列表
					parent.layer.close(index);
				}
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
			var sysCode = '${userVo.sysCode}';
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