<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>党支部编辑</title>
</head>
<body>
<div id="" class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="id" name="id" value="${branchVo.id}" />		
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${branchVo.createTime}'  pattern='yyyy-MM-dd HH:mm:ss'/>"/>
<input type="hidden" id="creatorId" name="creatorId" value="${branchVo.creatorId}" />	
<input type="hidden" id="creatorName" name="creatorName" value="${branchVo.creatorName}" />	
	<%-- 第1行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>支部名称</label>
	    <div class="col-sm-10">
	      <input type="text" class="form-control" id="branchName" name="branchName" placeholder="输入支部名称,10个字之内" value='${branchVo.branchName}' data-rule-required="true" data-rule-rangelength="[1,10]" />
	    </div>
  	</div>
    <%-- 第2行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>支部描述</label>
	    <div class="col-sm-10">
	       <textarea class="form-control" rows="4" cols="" id="remark" name="remark" placeholder="描述，长度为250字"  data-rule-required="true" data-rule-rangelength="[1,250]" >${branchVo.remark}</textarea>
	    </div>
  	</div>
  	<%-- 第3行 --%>
	<div class="form-group">
	  	<label class="col-sm-2 control-label"><span style="color: red">*</span>排序</label>
	    <div class="col-sm-2">
	       <input type="number" class="form-control" id="order" name="order"  value='${branchVo.order}' data-rule-required="true" data-rule-rangelength="[1,2]" />
	    </div>
  	</div>
    <br>
  
  	<div class="form-group">
	</div>
	<div class="form-group">
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
	var id = '${branchVo.id}';
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
		url: '/dangjian/branch_saveOrUpdate',
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
					autoMsg("保存成功！",1);
					iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/dangjian/branch_load"});//加载树下的列表
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