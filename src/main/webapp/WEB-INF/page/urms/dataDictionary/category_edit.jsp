<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>菜单</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
<input type="hidden" id="id" name="id" value="${categoryVo.id}"/>
<input type="hidden" id="pId" name="pId" value="${categoryVo.pId}"/>
<input type="hidden" id="pIds" name="pIds" value="${categoryVo.pIds}"/>
<input type="hidden" id="pNames" name="pNames" value="${categoryVo.pNames}"/>
<input type="hidden" id="isLeaf" name="isLeaf" value="${categoryVo.isLeaf}"/>
<input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value="${categoryVo.createTime}"  pattern="yyyy-MM-dd HH:mm:ss"/>"/>
<input type="hidden" id="level" name="level" value="${categoryVo.level}"/>

	<br><br><br>
	<div class="form-group">
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>目录名称</label>
	  <div class="col-sm-4">
	    <input type="text" class="form-control" id="categoryName" name="categoryName" value="${categoryVo.categoryName}" data-rule-required="true" data-rule-rangelength="[1,20]"/>
	  </div>
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>目录编码</label>
	  <div class="col-sm-4">
	    <input type="text" class="form-control" id="categoryCode" name="categoryCode" value="${categoryVo.categoryCode}" data-rule-required="true" data-rule-rangelength="[1,50]" data-rule-remote="/urms/category_checkCategoryCode?id=${categoryVo.id}" data-msg-remote="目录编码已经存在，请重新输入"/>
	  </div>
	</div>
	<div class="form-group">
	  <label class="col-sm-2 control-label"><span style="color: red">*</span>目录类型</label>
	  <div class="col-sm-4">
	  	<opt:select dictKey="categoryType" isDefSelect="true" id="categoryType" name="categoryType" classStyle="form-control m-b required" value="${categoryVo.categoryType}"/>
	  </div>
	  <label class="col-sm-2 control-label">排序</label>
	  <div class="col-sm-4">
	    <input type="text" class="form-control" id="order" name="order" value="${categoryVo.order}" data-rule-digits="true"/>
	  </div>
	</div>
	<%-- <div class="form-group isGrid">
		<label class="col-sm-2 control-label"><span style="color: red">*</span>是否是网格</label>
		<div class="col-sm-4">
			<opt:select dictKey="isNot" isDefSelect="true" id="isGrid" name="isGrid" classStyle="form-control m-b required" value="${categoryVo.isGrid}"/>
		</div>
	</div> --%>
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
var isUpdate = false;
$().ready(function() {
	$("#baseForm").validate();//初始化验证
	if($("#id").val()!=""&&$("#id").val()!=null){
		isUpdate = true;
	}
    changeCategoryTtpe();
});
//新增保存更新
function on_save(){
	if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
        on_submit();//提交表单.  
    } 
}

 $("#categoryType").change(function(){
     changeCategoryTtpe();
 });

function changeCategoryTtpe() {
    if($("#categoryType").val() == 3){
        $(".isGrid").show();
    }else{
        $(".isGrid").hide();
    }
}
//保存
function on_submit(){  
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/urms/category_saveOrUpdate',
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
				autoMsg("保存成功！",1);
				iframeIndex.$("#grid").bootstrapTable("refresh",{url:"/urms/category_load?id="+$("#pId").val()});//加载树下的列表
				//加载树
				var pNode = iframeIndex.zTreeObj.getNodeByParam("id", $("#pId").val(), null);//父节点
				if(isUpdate){//更新
					var node = iframeIndex.zTreeObj.getNodeByParam("id", data.id, null);
					node.name = $("#categoryName").val();
					iframeIndex.zTreeObj.updateNode(node);
				}else{
					iframeIndex.zTreeObj.addNodes(pNode, {id:""+data.id+"",name:""+$("#categoryName").val()+""});//新增
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
</script>
</html>