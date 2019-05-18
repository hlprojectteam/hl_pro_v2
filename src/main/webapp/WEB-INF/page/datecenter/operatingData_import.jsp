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
<title>营运数据导入</title>
</head>
<body>
	<div class="ibox-content" style="padding-top: 5px;">
		<!-- 工具条 -->
		<form id="roleForm" name="roleForm" action=submitOperatingDataExcel
			enctype="multipart/form-data" class="form-horizontal" method="post">
			<input type="hidden" id="ttId" name="ttId" value="${operatingDataVo.ttId}" />
			<input type="hidden" id="dutyDateStr" name="dutyDateStr" value="${operatingDataVo.dutyDateStr}" />
			<input type="hidden" id="title" name="title"  value="<fmt:formatDate value='${operatingDataVo.dutyDate}' pattern='yyyy年MM月dd日各站营运数据'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
			<div class="ibox-content">
				<div class="form-group">
					<label class="col-sm-2 control-label">营运数据EXCEL导入</label>
					<div class="col-sm-4">
						<input type="file" name="file">
					</div>
					<div class="col-sm-1">
						<button type="submit" class="btn btn-success">
							<i class="fa fa-check"></i>&nbsp;提交
						</button>
					</div>
					<div class="col-sm-1">
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
    	var url='/common/download/OperationDataTemplate.xls';
    	window.location.href=url;
    }catch(e){ 

    } 
}
	
</script>
</html>