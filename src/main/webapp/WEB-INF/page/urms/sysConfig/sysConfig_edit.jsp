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
<input type="hidden" id="id" name="id" value="${sysConfigVo.id }"/>
<input type="hidden" id="sysCode" name="sysCode" value="${sysConfigVo.sysCode }"/>
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${sysConfigVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"/>
	<br><br><br>
    <div class="form-group">
  	    <label class="col-sm-2 control-label"><span style="color: red">*</span>配置名</label>
  	    <div class="col-sm-4">
  	      	<input type="text" class="form-control" id="sysKey" name="sysKey" value="${sysConfigVo.sysKey}" data-rule-required="true" data-rule-rangelength="[2,20]" data-rule-remote="/urms/sysConfig_checkSysKey?id=${sysConfigVo.id}&sysCode=${sysConfigVo.sysCode}" data-msg-remote="配置名已经存在，请重新输入"/>
  	    </div>
  	    <label class="col-sm-2 control-label"><span style="color: red">*</span>配置值</label>
  	    <div class="col-sm-4">
  	      	<input type="text" class="form-control" id="sysValue" name="sysValue" value="${sysConfigVo.sysValue}" data-rule-required="true" data-rule-rangelength="[1,64]" />
  	    </div>
    </div>
    <div class="form-group">
	    <label class="col-sm-2 control-label">说明</label>
	    <div class="col-sm-10">
			<textarea class="form-control" id="memo" name="memo">${sysConfigVo.memo}</textarea>
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
		url: '/urms/sysConfig_saveOrUpdate',
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