<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>组织架构</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="id" name="id" value="${orgFrameVo.id}"/>
<input type="hidden" id="pId" name="pId" value="${orgFrameVo.pId}"/>
<input type="hidden" id="pIds" name="pIds" value="${orgFrameVo.pIds}"/>
<input type="hidden" id="pNames" name="pNames" value="${orgFrameVo.pNames}"/>
<input type="hidden" id="isLeaf" name="isLeaf" value="${orgFrameVo.isLeaf}"/>
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${orgFrameVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>"/>
<input type="hidden" id="level" name="level" value="${orgFrameVo.level}"/>
<input type="hidden" id="creatorId" name="creatorId" value="${orgFrameVo.creatorId}"/><%-- 创建人id --%>
<input type="hidden" id="creatorName" name="creatorName" value="${orgFrameVo.creatorName}"/><%-- 创建人 --%>
  <div class="form-group">
    <label class="col-sm-3 control-label"><span style="color: red">*</span>组织名称</label>
    <div class="col-sm-9">
      <input type="text" class="form-control" id="orgFrameName" name="orgFrameName" value="${orgFrameVo.orgFrameName}" data-rule-required="true" data-rule-rangelength="[2,10]"/>
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-3 control-label"><span style="color: red">*</span>组织编码</label>
    <div class="col-sm-9">
      <input type="text" class="form-control" id="orgFrameCode" name="orgFrameCode" value="${orgFrameVo.orgFrameCode}" data-rule-required="true" data-rule-rangelength="[2,30]" data-rule-remote="/urms/orgFrame_checkOrgFrameCode?id=${orgFrameVo.id}&sysCode=${orgFrameVo.sysCode}" data-msg-remote="菜单编码已经存在，请重新输入">
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-3 control-label"><span style="color: red">*</span>所属子系统</label>
    <div class="col-sm-9">
      	<select id="subsystemSelect" name="sysCode" class="form-control m-b required" value="${orgFrameVo.sysCode}">
      	</select>
    </div>
  </div>
  <div class="form-group">
    <label class="col-sm-3 control-label">排序</label>
    <div class="col-sm-9">
      <input type="text" class="form-control" id="order" name="order" value="${orgFrameVo.order}" data-rule-digits="true">
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
var isUpdate = false;
$().ready(function() {
	getSubsystem();		 
	if($("#id").val()!=""&&$("#id").val()!=null){
		isUpdate = true;
	}
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
		url: '/urms/orgFrame_saveOrUpdate',
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
				autoMsg("保存成功！",1);
				iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/urms/orgFrame_load"});//加载树下的列表
				//加载树
				var pNode = iframeIndex.zTreeObj.getNodeByParam("id", $("#pId").val(), null);//父节点
				if(isUpdate){//更新
					var node = iframeIndex.zTreeObj.getNodeByParam("id", data.id, null);
					node.name = $("#orgFrameName").val();
					iframeIndex.zTreeObj.updateNode(node);
				}else{
					iframeIndex.zTreeObj.addNodes(pNode, {id:""+data.id+"",name:""+$("#orgFrameName").val()+""});//新增
				}
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
			var sysCode = '${orgFrameVo.sysCode}';
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