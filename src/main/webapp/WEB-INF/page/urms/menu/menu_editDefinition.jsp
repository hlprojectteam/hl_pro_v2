<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld"%>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>菜单</title>
</head>
<body>
	<div class="ibox-content">
		<div id="form" class="tab-pane active">
			<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
				<input type="hidden" id="id" name="id" value="${menuDefinitionVo.id}" /> <input type="hidden" id="menuId" name="menu.id" value="${menuDefinitionVo.menu.id}" /> <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${menuDefinitionVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" /> <br>
				<br>
				<br>
				<div class="form-group">
					<label class="col-sm-2 control-label"><span style="color: red">*</span>功能点名称</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="definitionName" name="definitionName" value="${menuDefinitionVo.definitionName}" data-rule-required="true" data-rule-rangelength="[2,10]" />
					</div>
					<label class="col-sm-2 control-label"><span style="color: red">*</span>功能点编码</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="definitionCode" name="definitionCode" value="${menuDefinitionVo.definitionCode}" data-rule-required="true" data-rule-rangelength="[2,50]" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label" id="iconLabel">图标</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="icon" name="icon" value="${menuDefinitionVo.icon}">
					</div>
					<label class="col-sm-2 control-label"><span style="color: red">*</span>排序</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="order" name="order" value="${menuDefinitionVo.order}" data-rule-digits="true" data-rule-required="true">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><span style="color: red">*</span>页面类型</label>
					<div class="col-sm-3">
						<opt:select dictKey="pageType" name="pageType" id="pageType" value="${menuDefinitionVo.pageType}" classStyle="form-control m-b required" onchange="changeIcon()" />
					</div>
					<label class="col-sm-2 control-label">颜色(默认绿色)</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="colour" name="colour" value="${menuDefinitionVo.colour}">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><span style="color: red">*</span>功能点方法</label>
					<div class="col-sm-8">
						<input type="text" class="form-control" id="definitionMethod" name="definitionMethod" value="${menuDefinitionVo.definitionMethod}" data-rule-required="true" />
					</div>
				</div>
			</form>
			<!-- 底部按钮 -->
			<div class="footer edit_footer">
				<div class="pull-right">
					<button class="btn btn-primary " type="button" onclick="on_save()">
						<i class="fa fa-check"></i>&nbsp;保存
					</button>
					<button class="btn btn-danger " type="button" onclick="on_close()">
						<i class="fa fa-close"></i>&nbsp;关闭
					</button>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	//新增保存更新
	function on_save() {
		if ($("#menuId").val() != null && $("#menuId").val() != "") {
			if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
				on_submit();//提交表单.  
			}
		} else {
			autoAlert("请保存菜单", 5);
		}
	}

	function on_submit() {
		$.ajax({
			type : 'post',
			async : false,
			dataType : 'json',
			url : '/urms/menu_saveOrUpdateDefinition',
			data : $('#baseForm').serialize(),
			success : function(data) {
				if (data.result) {
					autoMsg("保存成功！", 1);
					for (var i = 0; i < parent.$("iframe").length; i++) {
						if (parent.$("iframe")[i].src
								.indexOf("/urms/menu_edit?id=") > -1) {
							parent.$("iframe")[i].contentWindow.$(
									"#gridDefinition").bootstrapTable(
									"refresh", {
										url : "/urms/menu_loadDefinition"
									});//加载树下的列表
						}
					}
					parent.layer.close(index);
				} else {
					autoAlert("保存失败，请检查！", 5);
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				autoAlert("系统出错，请检查！", 5);
			}
		});
	}

	$(function() {
		changeIcon();
	});

	//是否必填
	function changeIcon() {
		var pageType = $("#pageType").val();
		if (pageType == "list") {
			$("#icon").attr("data-rule-required", true);
			$("#iconLabel").html('<span style="color: red">*</span>图标');
		} else {
			$("#icon").attr("data-rule-required", false);
			$("#iconLabel").html('图标');
		}
	}
</script>
</html>