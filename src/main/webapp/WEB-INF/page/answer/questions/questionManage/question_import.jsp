<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="menu" uri="/WEB-INF/taglib/menuDefinition.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>题库导入</title>
</head>
<body>
	<div class="ibox-content" style="padding-top: 5px;">
		<!-- 工具条 -->
		<form id="roleForm" name="roleForm" action=question_import_submit
			enctype="multipart/form-data" class="form-horizontal" method="post">
			<input type="hidden" id="id" name="id" value="${questionManageVo.id}" />
			<div class="ibox-content">
				<div class="form-group">
					<label class="col-sm-2 control-label">题库EXCEL导入</label>
					<div class="col-sm-4">
						<input type="file" name="file">
					</div>
					<div class="col-sm-1">
						<button type="submit" class="btn btn-success">
							<i class="fa fa-check"></i>&nbsp;提交
						</button>
					</div>
					<div class="col-sm-1" style="margin-left: 20px;">
						<button type="button" class="btn btn-success" onclick="downloadFile()">
							<i class=" file-excel-o"></i>&nbsp;下载导入模版
						</button>
					</div>
					
				</div>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript">
//下载模版
function downloadFile() {   
    try{ 
    	var url='/common/download/ExamQuestionTemplate.xls';
    	window.location.href=url;
    }catch(e){ 

    } 
}
	
</script>
</html>