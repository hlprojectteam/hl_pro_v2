<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="opt" uri="/WEB-INF/taglib/option.tld" %>
<jsp:include page="/common/common.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>清障保洁编辑</title>
</head>
<body>
<div id="" class="ibox-content">
	<form id="baseForm" method="post" class="form-horizontal" name="baseForm" action="">
		<%-- baseModule start --%>
	    <input type="hidden" id="id" name="id" value="${clearingVo.id}" />
	    <input type="hidden" id="creatorId" name="creatorId" value="${clearingVo.creatorId}" />
	    <input type="hidden" id="creatorName" name="creatorName" value="${clearingVo.creatorName}" />
	    <input type="hidden" id="createTime" name="createTime" value="<fmt:formatDate value='${clearingVo.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>" />
	    <input type="hidden" id="sysCode" name="sysCode" value="${clearingVo.sysCode}" />
	    <%-- baseModule end --%>
	    
	    <input type="hidden" id="ttId" name="ttId" value="${clearingVo.ttId}" />
	    <input type="hidden" id="formNumber" name="formNumber" value="HLZXRBB-08" />
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>标题</label>
		    <div class="col-sm-8">
		      <c:if test="${not empty clearingVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="${clearingVo.title}" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		      <c:if test="${empty clearingVo.title}">
		      	<input type="text" class="form-control" id="title" name="title" placeholder="输入标题" value="<fmt:formatDate value='${clearingVo.dutyDate}' pattern='yyyy年MM月dd日清障保洁'/>" data-rule-required="true" data-rule-rangelength="[1,50]" />
		      </c:if>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>日期</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${clearingVo.dutyDate}' pattern='yyyy-MM-dd'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" data-rule-required="true" disabled="disabled"  />
	            <input type="hidden" id="dutyDate" name="dutyDate" value="<fmt:formatDate value='${clearingVo.dutyDate}' pattern='yyyy-MM-dd'/>" />
	        </div>
        </div>
	  	
	  	<div class="form-group">
	  		<label class="col-sm-2 control-label"><span style="color: red">*</span>接报时间</label>
	        <div class="col-sm-3">
	            <input type="text" class="form-control" id="receiptTime" name="receiptTime" value="<fmt:formatDate value='${clearingVo.receiptTime}' pattern='HH:mm'/>" onfocus="this.blur()" onclick="WdatePicker({dateFmt:'HH:mm'})" data-rule-required="true"  />
	        </div>
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>报告部门 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="reportedDp" name="reportedDp" value="${clearingVo.reportedDp}" data-rule-required="true" data-rule-rangelength="[1,20]" />    
			</div>
		</div>
	 
	    <div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>报告人员 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="reportedPerson" name="reportedPerson" value="${clearingVo.reportedPerson}" data-rule-required="true" data-rule-rangelength="[1,20]" />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>报告方式</label>
		    <div class="col-sm-3">
		    	<opt:select dictKey="dc_reportedWay" classStyle="form-control required" name="reportedWay" id="reportedWay" value="${clearingVo.reportedWay}" isDefSelect="true" />
			</div>
		</div>
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>通行路段 </label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="trafficRoad" name="trafficRoad" value="${clearingVo.trafficRoad}" data-rule-required="true" data-rule-rangelength="[1,30]" />    
			</div>
			<label class="col-sm-2 control-label"><span style="color: red">*</span>通知处理部门</label>
		    <div class="col-sm-3">
				<input type="text" class="form-control" id="processingDp" name="processingDp" value="${clearingVo.processingDp}" data-rule-required="true" data-rule-rangelength="[1,30]" />    
			</div>
		</div>
		
		
		<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>情况简述</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="8" cols="" id="briefIntroduction" name="briefIntroduction" data-rule-required="true" data-rule-rangelength="[1,300]" >${clearingVo.briefIntroduction}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label"><span style="color: red">*</span>处理结果</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="8" cols="" id="result" name="result" data-rule-required="true" data-rule-rangelength="[1,300]" >${clearingVo.result}</textarea>
		    </div>
	  	</div>
	  	
	  	<div class="form-group">
		  	<label class="col-sm-2 control-label">备注</label>
		    <div class="col-sm-8">
		       <textarea class="form-control" rows="6" cols="" id="remark" name="remark" data-rule-rangelength="[0,250]" >${clearingVo.remark}</textarea>
		    </div>
	  	</div>
		<br><br><br>
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
	var winName = "${winName}";
	var URLStr = "/datecenter/clearing/clearing_";

	//新增或编辑
	function on_save(){
		if ($("#baseForm").valid()) {//如果表单验证成功，则进行提交。  
	        on_submit();//提交表单.  
	    }else{
            autoAlert("信息提交不正确，请检查！", 5);
        }
	}

	//提交表单
	function on_submit(){  
		$.ajax({
			type : 'post',
			async:false,
			dataType : 'json',
			url: URLStr + 'saveOrUpdate',
			data:$('#baseForm').serialize(),
			success : function(data) {
                if (data.result) {
                    autoMsg("保存成功！", 1);
                    parent.frames[winName].$("#grid").bootstrapTable("refresh", {
                        url : URLStr + "load"
                    });//加载树下的列表
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

</script>
</html>