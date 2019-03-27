<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>成绩导出</title>
</head>
<body>
<div class="ibox-content">
<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">

    <%-- 第1行 --%>
	<div class="form-group">
   		<label class="col-sm-2 control-label">成绩表</label>
    	<div class="col-sm-4">
	      <input type="text" class="form-control" id="fileName" name="fileName" value="" readonly="readonly"/>
	    </div>
	    <div class="col-sm-2" id="div_download" style="display: none;">
	       <a id="download" href="" class="form-control">下载到本地</a>
	     </div>
	</div>
</form>
</div>

<div class="footer edit_footer" style="z-index: 99999;">
<div class="pull-right">
	<button class="btn btn-primary " type="button" onclick="on_export()"><i class="fa fa-check"></i>&nbsp;生成成绩表</button>
	<button class="btn btn-danger " type="button" onclick="on_close()"><i class="fa fa-close"></i>&nbsp;关闭</button>
</div>
</div>

</body>
<script type="text/javascript">
var examManageId = '${examManageId}';
$(function(){
	
});

function on_export(){  
	$.ajax({
		type : 'post',
		async:false,
		dataType : 'json',
		url: '/answer/examManage_expScore?examManageId='+examManageId,
		data:$('#baseForm').serialize(),
		success : function(data){
			if(data.result){
				$("#fileName").val(data.fileName);
				$("#download").attr("href",data.fileUrl);
				
				$("#div_download").show();
				
			}else{
				autoAlert("导出失败:"+data.err,5);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			autoAlert("系统出错，请检查！",5);
		}
	});
}


</script>
</html>