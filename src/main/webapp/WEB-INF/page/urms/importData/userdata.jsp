<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld"%>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Excel导入</title>
</head>
<body>

	<div class="ibox-content" style="padding-top: 5px;">
		<!-- 工具条 -->
		<menu:definition menuCode="${menuCode }" />
		<form id="roleForm" name="roleForm" action=submitUserRoleExcel
			enctype="multipart/form-data" class="form-horizontal" method="post">
			<div class="ibox-content">
				<div class="form-group">
					<label class="col-sm-2 control-label">角色EXCEL导入</label>
					<div class="col-sm-4">
						<input type="file" name="file">
					</div>

					<div class="col-sm-1">
						<button type="submit" class="btn btn-success">
							<i class="fa fa-check"></i>&nbsp;提交
						</button>
					</div>
				</div>
			</div>
		</form>
		
		
		<form id="userForm" name="userForm" action="submitUserExcel"
			enctype="multipart/form-data" class="form-horizontal" method="post">
			<div class="ibox-content">
				<div class="form-group">
					<label class="col-sm-2 control-label">员工EXCEL导入</label>
					<div class="col-sm-4">
						<input type="file" name="file">
					</div>

					<div class="col-sm-1">
						<button type="submit" class="btn btn-success">
							<i class="fa fa-check"></i>&nbsp;提交
						</button>
					</div>
				</div>
			</div>
		</form>
		
		<form id="userRoleForm" name="userRoleForm" action="setUserRoleExcel"
			enctype="multipart/form-data" class="form-horizontal" method="post">
			<div class="ibox-content">
				<div class="form-group">
					<label class="col-sm-2 control-label">批量添加员工角色</label>
					<div class="col-sm-4">
						<input type="file" name="file">
					</div>
					<div class="col-sm-1">
						<button type="submit" class="btn btn-success">
							<i class="fa fa-check"></i>&nbsp;提交
						</button>
					</div>
				</div>
			</div>
		</form>

	</div>


</body>
<script>
	
</script>
</html>